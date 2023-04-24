package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.Trace;
import com.mmjang.ankihelper.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by liao on 2017/4/28.
 */

public class BingImage implements IDictionary {

    private final int lt = DictLanguageType.ENG;

    public int getLanguageType() {
        return lt;
    }

    private String mAudioUrl = "";

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
    private void emptyAudioUrl() {
        mAudioUrl = "";
    }
    public boolean isExistAudioUrl() {
        return false;
    }


    private static final String AUDIO_TAG = "MP3";
    private static final String DICT_NAME = "必应图片搜索";
    private static final String DICT_INTRO = "图片保存至 collection.media/ankihelper_image/";
    private static final String[] EXP_ELE = new String[]{
            Constant.DICT_FIELD_WORD,
            "🖼💾<img>"
    };

    private static final String wordUrl = "https://cn.bing.com/images/search?q=";
    private static final String mp3Url = "https://audio.vocab.com/1.0/us/";

    public BingImage(Context context) {
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
            Request request = new Request.Builder().url(wordUrl + key + "&FORM=BESBTB&ensearch=1")
                    //.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36")
                    .addHeader("User-Agent", Constant.UA)
                    .build();
            String rawhtml = MyApplication.getOkHttpClient().newCall(request).execute().body().string();
            Document doc = Jsoup.parse(rawhtml);
            List<Definition> definitionList = new ArrayList<>();

            for (Element imgEle : doc.select(".imgpt > a")) {
                String fileName = Utils.getSpecificFileName(key);
                String imageName = fileName + ".png";
                String imageTag = String.format(Constant.TPL_HTML_IMG_TAG, imageName);

                String json = imgEle.attr("m");
                String url;
                try {
                    url = (new JSONObject(json)).getString("turl");
                } catch (JSONException e) {
                    continue;
                }
                LinkedHashMap<String, String> eleMap = new LinkedHashMap<>();
                eleMap.put(Constant.DICT_FIELD_WORD, key);
                eleMap.put(EXP_ELE[1], imageTag);
                String audioFileName = fileName;
                Definition.ResInformation resInfor = new Definition.ResInformation(
                        url, imageName, "", audioFileName, Constant.MP3_SUFFIX
                );
                definitionList.add(new Definition(eleMap, "", resInfor));
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

    static String getSingleQueryResult(Document soup, String query, boolean toString) {
        Elements re = soup.select(query);
        if (!re.isEmpty()) {
            if (toString) {
                return re.get(0).toString();
            } else {
                return re.get(0).text();
            }
        } else {
            return "";
        }
    }

    static String getSingleQueryResult(Element soup, String query, boolean toString) {
        Elements re = soup.select(query);
        if (!re.isEmpty()) {
            if (toString) {
                return re.get(0).toString();
            } else {
                return re.get(0).text();
            }
        } else {
            return "";
        }
    }

    private String getMp3Url(String id) {
        return "[sound:" + mp3Url + id + ".mp3]";
    }

    private String getDefHtml(Element def) {
        String sense = def.toString().replaceAll("<h3.+?>", "<div class='vocab_def'>").replace("</h3>", "</div>").replaceAll("<a.+?>", "<b>").replace("</a>", "</b>");
        //String defString = def.child(1).text().trim();
        return sense;
    }
}

