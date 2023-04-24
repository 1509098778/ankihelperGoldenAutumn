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

/**
 * Created by liao on 2017/4/28.
 */

public class Mnemonic implements IDictionary {

    private final int lt = DictLanguageType.ENG;
    //private static final String AUDIO_TAG = "MP3";
    private static final String DICT_NAME = "Mnemonic助记词典";
    private static final String DICT_INTRO = "全英文助记，慎入。";
    private static final String[] EXP_ELE = new String[] {
            Constant.DICT_FIELD_WORD,
            "助记",
            Constant.DICT_FIELD_COMPLEX_ONLINE,
            Constant.DICT_FIELD_COMPLEX_OFFLINE,
            Constant.DICT_FIELD_COMPLEX_MUTE
    };
    private static final String wordUrl = "http://www.mnemonicdictionary.com/?word=";
    //private static final String mp3Url = "https://audio.vocab.com/1.0/us/";

    private Context mContext;
    public int getLanguageType() {
        return lt;
    }

    public Mnemonic(Context context){
        this.mContext = context;
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
            Document doc = Jsoup.connect(wordUrl + key)
                    .userAgent("Mozilla")
                    .timeout(5000)
                    .get();
            List<Definition> definitionList = new ArrayList<>();
            for(Element memo : doc.select(".span9")){
//                String audioIndicator = "<font color='#227D51' >" + AUDIO_TAG + "</font>";
                String complex = FieldUtil.formatComplexTplWord(DICT_NAME, key, "", memo.text(), Constant.AUDIO_INDICATOR_MP3);
                String muteComplex = FieldUtil.formatComplexTplWord(DICT_NAME, key, "", memo.text(), "");

                LinkedHashMap<String, String> defMap = new LinkedHashMap<>();
                defMap.put(Constant.DICT_FIELD_WORD, key);
                defMap.put(EXP_ELE[1], memo.text());
                defMap.put(Constant.DICT_FIELD_COMPLEX_ONLINE, complex);
                defMap.put(Constant.DICT_FIELD_COMPLEX_OFFLINE, complex);
                defMap.put(Constant.DICT_FIELD_COMPLEX_MUTE, muteComplex);
                String audioFileName = Utils.getSpecificFileName(key);
                Definition.ResInformation resInfor = new Definition.ResInformation(
                        "", audioFileName, Constant.MP3_SUFFIX
                );
                definitionList.add(new Definition(defMap, memo.text(), resInfor));
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

    private static String getSingleQueryResult(Document soup, String query, boolean toString){
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

    private static String getSingleQueryResult(Element soup, String query, boolean toString){
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

