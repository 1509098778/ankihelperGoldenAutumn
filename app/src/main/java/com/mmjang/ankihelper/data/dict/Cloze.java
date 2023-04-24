package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.widget.ListAdapter;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.FieldUtil;
import com.mmjang.ankihelper.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by liao on 2017/5/6.
 */

public class Cloze implements IDictionary {

    private int lt = DictLanguageType.ALL;
    private static final String DICT_NAME = "挖空笔记";
    private static final String[] EXP_ELE_LIST = new String[]{
            "挖空内容",
            Constant.DICT_FIELD_COMPLEX_ONLINE,
            Constant.DICT_FIELD_COMPLEX_OFFLINE,
            Constant.DICT_FIELD_COMPLEX_MUTE
    };

    public Cloze(Context context) {
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
    private void emptyAudioUrl() { mAudioUrl = ""; }
    public boolean isExistAudioUrl() {
        return false;
    }


    public int getLanguageType() {
        return lt;
    }

    public String getDictionaryName() {
        return "制作填空";
    }

    public String getIntroduction() {
        return "无释义，用于快速制作填空";
    }

    public String[] getExportElementsList() {
        return EXP_ELE_LIST;
    }

    public List<Definition> wordLookup(String key) {
        ArrayList<Definition> result = new ArrayList<>();
        LinkedHashMap<String, String> defMap = new LinkedHashMap<>();

        String audioFileName = Utils.getSpecificFileName(key);
        String complex = FieldUtil.formatComplexTplCloze(DICT_NAME, Constant.AUDIO_INDICATOR_MP3);
        String muteComplex = FieldUtil.formatComplexTplCloze(DICT_NAME, "");

        defMap.put(EXP_ELE_LIST[0], key);
        defMap.put(Constant.DICT_FIELD_COMPLEX_ONLINE, complex);
        defMap.put(Constant.DICT_FIELD_COMPLEX_OFFLINE, complex);
        defMap.put(Constant.DICT_FIELD_COMPLEX_MUTE, muteComplex);
        Definition.ResInformation resInfor = new Definition.ResInformation(
                "", audioFileName, Constant.MP3_SUFFIX
        );
        result.add(new Definition(defMap, "制作填空卡片", resInfor));
        return result;
    }

    public ListAdapter getAutoCompleteAdapter(Context context, int layout) {
        return null;
    }

}
