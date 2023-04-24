package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.util.Constant;
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

public class Handian implements IDictionary {

    private final int lt = DictLanguageType.ZHO;
    private static final String AUDIO_TAG = "MP3";
    private static final String DICT_NAME = "汉典";
    private static final String DICT_INTRO = "数据来自 www.zdic.net";
    private static final String[] EXP_ELE = new String[] {"字词", "释义"};

    private static final String wordUrl = "https://www.zdic.net/search/?sclb=tm&q=";
    private static final String autoCompleteUrl = "https://www.esdict.cn/dicts/prefix/";
    private static final String COOKIE = "_ga=GA1.2.841713493.1645618096; __gads=ID=c7117986932c7901-22f0d00bb8d00076:T=1645618095:RT=1645618095:S=ALNI_MYmN8DXY2C7GjnpBcvnmhi6nRb-Hg; _gid=GA1.2.994897762.1645795084; _gat_gtag_UA_161009_3=1";

    public int getLanguageType() {
        return lt;
    }

    public Handian(Context context) {

    }

    public Handian(){

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
//            Document doc = Jsoup.connect(wordUrl + key)
//                    .userAgent(DEFAULT_UA)
//                    .timeout(5000)
//                    .get();
//            String html = doc.toString();
            Request request = new Request.Builder().url(wordUrl + key)
                    .header("User-Agent", Constant.UA)
                    .build();
            String rawhtml = MyApplication.getOkHttpClient().newCall(request).execute().body().string();
            Document doc = Jsoup.parse(rawhtml);
            Elements entrys = doc.select("div.content.definitions");

            ArrayList<Definition> defList = new ArrayList<>();
            if (entrys.size() > 0) {
                if(entrys.size() > 1) {
                    entrys.remove(entrys.size() - 1);
                    if (entrys.size() > 5) {
                        entrys.remove(entrys.size() - 1);
                    }
                }
                String word = key;
                for(Element ele : entrys) {
                    LinkedHashMap<String, String> eleMap = new LinkedHashMap<>();
                    String meaning = ele.toString();
                    meaning = meaning.replaceAll("href=\"/", "href=\"https://www.zdic.net/");
                    meaning = meaning.replaceAll("data-original=\"/", "src=\"https:/");
                    meaning = meaning.replaceAll("&amp;", "&");
                    meaning = meaning.replaceAll("<div id=\"[(gg_cslot)|(ad)]([\\w\\W]*)</div>", "");
                    String definition = meaning;
                    String audioFileName = Utils.getSpecificFileName(word);
                    eleMap.put(EXP_ELE[0], word);
                    eleMap.put(EXP_ELE[1], definition);
                    Definition.ResInformation resInfor = new Definition.ResInformation(
                            "", audioFileName, Constant.MP3_SUFFIX
                    );
                    defList.add(new Definition(eleMap, definition, resInfor));
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


    public static void main(String[] args){
        IDictionary dic = new Handian();
        //dic.wordLookup(UrlEscapers.urlFragmentEscaper().escape("娘"));
        dic.wordLookup("娘");
    }
}

