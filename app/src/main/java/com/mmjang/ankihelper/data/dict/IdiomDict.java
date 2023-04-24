package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.FieldUtil;
import com.mmjang.ankihelper.util.StringUtil;
import com.mmjang.ankihelper.util.Trace;
import com.mmjang.ankihelper.util.Utils;

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

public class IdiomDict implements IDictionary {

    private final int lt = DictLanguageType.ENG;
    private static final String AUDIO_TAG = "MP3";
    private static final String DICT_NAME = "短语词典";
    private static final String DICT_INTRO = "数据来自 freedictionary ";
    private static final String[] EXP_ELE = new String[] {
            Constant.DICT_FIELD_WORD,
            Constant.DICT_FIELD_DEFINITION,
            Constant.DICT_FIELD_COMPLEX_ONLINE,
            Constant.DICT_FIELD_COMPLEX_OFFLINE,
            Constant.DICT_FIELD_COMPLEX_MUTE
    };

    private static final String wordUrl = "https://idioms.thefreedictionary.com/";

    public int getLanguageType() {
        return lt;
    }

    public IdiomDict(Context context) {

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
        return false;
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
            Request request = new Request.Builder().url(wordUrl + key).build();
            String rawhtml = MyApplication.getOkHttpClient().newCall(request).execute().body().string();
            Document doc = Jsoup.parse(rawhtml);
            ArrayList<Definition> defList = new ArrayList<>();
            Elements posList = doc.select("#Definition section");
            for(Element pos : posList){
                String word = VocabCom.getSingleQueryResult(pos, "h2", false);
                String copyright = VocabCom.getSingleQueryResult(pos, "div.brand_copy", false);
                Elements defs = pos.select(".ds-list");
                for (Element def : defs){
                    //TODO: .ownText() is executable and recall the right definition？？？
                    String definition = def.text();
                    String illus = def.select("span.illustration").text();
                    definition = definition.replace(illus, "<i><font color=#808080>" + illus + "</font></i>").trim();
                    LinkedHashMap<String, String> defMap = new LinkedHashMap<>();
                    String complex = FieldUtil.formatComplexTplWord(DICT_NAME, key, "", definition, Constant.AUDIO_INDICATOR_MP3);
                    String muteComplex = FieldUtil.formatComplexTplWord(DICT_NAME, key, "", definition, "");
                    defMap.put(Constant.DICT_FIELD_WORD, key);
                    defMap.put(Constant.DICT_FIELD_DEFINITION, definition);
                    defMap.put(Constant.DICT_FIELD_COMPLEX_ONLINE, complex);
                    defMap.put(Constant.DICT_FIELD_COMPLEX_OFFLINE, complex);
                    defMap.put(Constant.DICT_FIELD_COMPLEX_MUTE, muteComplex);
                    //TODO: setting proper font size for the copyright. The main purpose of
                    //setting copyright is to getting the reference.
                    String html = "<div class='idiom'><danci>" + word +" </danci> " + "-"+ copyright + "-" + "<span class=ys>" + definition +"</span></div>";
                    String audioFileName = Utils.getSpecificFileName(word);
                    Definition.ResInformation resInfor = new Definition.ResInformation(
                            "", audioFileName, Constant.MP3_SUFFIX
                    );
                    defList.add(new Definition(defMap, html, resInfor));


                }
               
            }
            return defList;
        } catch (Exception e) {
            Trace.e("time out", Log.getStackTraceString(e));
            return new ArrayList<Definition>();
        }
    }

    public ListAdapter getAutoCompleteAdapter(Context context, int layout) {
        return null;
    }
}
