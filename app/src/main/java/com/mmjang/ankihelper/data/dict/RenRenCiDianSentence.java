package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.FieldUtil;
import com.mmjang.ankihelper.util.StringUtil;
import com.mmjang.ankihelper.util.Trace;
import com.mmjang.ankihelper.util.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by liao on 2017/4/28.
 */

public class RenRenCiDianSentence implements IDictionary {

    private final int lt = DictLanguageType.ENG;
    private static final String AUDIO_TAG = "MP3";
    private static final String DICT_NAME = "人人词典原声例句";
    private static final String DICT_INTRO = "";
    private static final String[] EXP_ELE = new String[] {
            Constant.DICT_FIELD_WORD,
            "例句中文",
            "英文例句",
            "🖼💾<img>",
            "复合型Offline"/*"原声例句"*/,
            Constant.DICT_FIELD_COMPLEX_ONLINE,
            Constant.DICT_FIELD_COMPLEX_OFFLINE};

    private static final String wordUrl = "http://www.91dict.com/words?w=";
    private static final String tplt_card = "<div class='rrcd_sentence'>" +
            "<div class='rrcd_en'>%s</div>" +
            "<div class='rrcd_cn'>%s</div>" +
            "<div class='rrcd_title'>%s</div>" +
            "<img src='%s' class='rrcd_img'/>" +
            "<a class='rrcd_context' href='%s'>Context</a>" +
             "</div>";
    private static final String tplt_ui = "" +
            "<span>\uD83D\uDD0A%s</span>" +
            "<span>%s</span>" +
            "<span>%s</span>" +
            "";


    Context mContext;
    AnkiDroidHelper mAnkiDroid;

    public int getLanguageType() {
        return lt;
    }

    public RenRenCiDianSentence(Context context){
        mContext = context;
        mAnkiDroid = MyApplication.getAnkiDroid();
    }
    private static String mAudioUrl = "";
    public String getAudioUrl() {
        return mAudioUrl;
    }
    public boolean setAudioUrl(String audioUrl) {
        try {
            mAudioUrl = audioUrl;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private void emptyAudioUrl() { mAudioUrl = ""; }
    public boolean isExistAudioUrl() {
        return true;
    }


    public String getDictionaryName() {
        return DICT_NAME;
    }

    public String getIntroduction() {
        return DICT_INTRO;
    }

    public String[] getExportElementsList() {
        return EXP_ELE;
    }

    public List<Definition> wordLookup(String key) {
        try {
//            Document doc = Jsoup.connect(wordUrl + key)
//                    .userAgent("Mozilla")
//                    .timeout(5000)
//                    .get();
            emptyAudioUrl();
            Request request = new Request.Builder().url(wordUrl + key)
                    //.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36")
                    .addHeader("User-Agent", Constant.UA)
                    .build();
            String rawhtml = MyApplication.getOkHttpClient().newCall(request).execute().body().string();
            Document doc = Jsoup.parse(rawhtml);
            List<Definition> definitionList = new ArrayList<>();

            for(Element audioEle : doc.select("ul.slides > li")){
                LinkedHashMap<String, String> eleMap = new LinkedHashMap<>();
                String audioUrl = "";
                Elements audioElements = audioEle.select("audio");
                if(audioElements.size() > 0){
                    audioUrl = audioElements.get(0).attr("src");
                }

                String imageUrl = "";
                Elements imageElements = audioEle.select("img");
                if(imageElements.size() > 0){
                    imageUrl = imageElements.get(0).attr("src");
                }


                String fileName = Utils.getSpecificFileName(key);

                String audioName = fileName;
                String imageName = fileName + ".png";
                String imageTag = String.format(Constant.TPL_HTML_IMG_TAG, imageName);

                String channel = getSingleQueryResult(audioEle, "div.mTop", false).trim();
                String cn = getSingleQueryResult(audioEle, "div.mFoot", true)
                        .replaceAll("<em>", "<b>")
                        .replaceAll("</em>", "</b>")
                        .replaceAll(" class=\".+\"", "")
                        .replaceAll("<div>", "")
                        .replaceAll("</div>", "")
                        .replaceAll("\"", "\\\\\"")
                        .replaceAll("\\s", " ")
                        .trim();
                String en = getSingleQueryResult(audioEle, "div.mBottom", true)
                        .replaceAll("<em>", "<b>")
                        .replaceAll("</em>", "</b>")
                        .replaceAll(" class=\".+\"", "")
                        .replaceAll("<div>", "")
                        .replaceAll("</div>", "")
                        .replaceAll("\"", "\\\\\"")
                        .replaceAll("\\s", " ")
                        .trim();

                String context = getSingleQueryResult(audioEle, "div.mTextend", true);
                String detailUrl = "http://www.91dict.com" + audioEle.select("a.viewdetail").get(0).attr("href");
//                String audioTag = String.format("[sound:%s]",  audioName + Constant.MP3_SUFFIX);
//                String audioTag = PlayAudioManager.getSoundTag(mContext, en, audioUrl);
                String html = String.format(tplt_card,
                        en,
                        //audioTag,
                        cn,
                        "<font color=grey>" + channel + "</font>",
                         imageName,
                        detailUrl
                        );

                String html_ui = String.format(tplt_ui,
                        en,
                        cn,
                        "<font color=grey>" + channel + "</font>"
                );

                String audioIndicator = "";
                if(!audioUrl.isEmpty()){
                    audioIndicator = "<font color='#227D51' >" + AUDIO_TAG + "</font>";
                }
                String complex = FieldUtil.formatComplexTplSentence(channel, key, en, cn, Constant.AUDIO_INDICATOR_MP3).
                        replace(Constant._TPL_HTML_MEDIA_TAG_LOCATION_, imageTag + Constant._TPL_HTML_MEDIA_TAG_LOCATION_);
//                String complex = String.format(
//                        Constant.TPL_COMPLEX_TAG,
//                        channel,
//                        Constant.AUDIO_INDICATOR_MP3,
//                        String.format(Constant.TPL_COMPLEX_DICT_SENTENCE_TAG,
//                                StringUtil.appendTagAll(StringUtil.stripHtml(en), key, "<b>","</b>").
//                                        trim().replaceAll("[\n\r]{2,}", "<br/>"),
//                                StringUtil.stripHtml(cn)),
//                        imageTag+"%s");
                eleMap.put(EXP_ELE[0], key);
                eleMap.put(EXP_ELE[1], cn);
                eleMap.put(EXP_ELE[2], en);
                eleMap.put(EXP_ELE[3], imageTag);
                eleMap.put(EXP_ELE[4], html);
                eleMap.put(EXP_ELE[5], complex);
                eleMap.put(EXP_ELE[6], complex);
                Definition.ResInformation resInfor = new Definition.ResInformation(
                        imageUrl, imageName, audioUrl, audioName, Constant.MP3_SUFFIX
                );
                definitionList.add(new Definition(eleMap, html_ui, resInfor));
            }

            return definitionList;
        } catch (Exception e) {
            Trace.e("time out", Log.getStackTraceString(e));
            return new ArrayList<Definition>();
        }
    }

    public ListAdapter getAutoCompleteAdapter(Context context, int layout) {
        return null;
    }

    static String getSingleQueryResult(Document soup, String query, boolean toString){
        Elements re = soup.select(query);
        if(!re.isEmpty()){
            if(toString){
                return re.get(0).toString();
            }else {
                return re.get(0).text();
            }
        }else{
            return "";
        }
    }

    static String getSingleQueryResult(Element soup, String query, boolean toString){
        Elements re = soup.select(query);
        if(!re.isEmpty()){
            if(toString) {
                return re.get(0).toString();
            }
            else{
                return re.get(0).text();
            }
        }else{
            return "";
        }
    }
}

