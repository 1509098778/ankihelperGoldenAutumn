package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.widget.ListAdapter;

import com.mmjang.ankihelper.data.database.ExternalDatabase;
import com.mmjang.ankihelper.data.dict.JPDeinflector.Deinflection;
import com.mmjang.ankihelper.data.dict.JPDeinflector.Deinflector;
import com.mmjang.ankihelper.data.dict.mdict.MdictInformation;
import com.mmjang.ankihelper.ui.tango.OutputLocatorPOJO;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.FieldUtil;
import com.mmjang.ankihelper.util.RegexUtil;
import com.mmjang.ankihelper.util.StringUtil;
import com.mmjang.ankihelper.util.Utils;
import com.mmjang.ankihelper.util.WanaKanaJava;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by liao on 2017/8/11.
 */

public class Mdict implements IDictionary {

    private int lt;

    private Context mContext;
    private ExternalDatabase mDatabase;
    private int mDictId;
    private static WanaKanaJava mWanaKanaJava;
    public MdictInformation mDictInformation;

    static {
        System.loadLibrary("jni-layer");
    }

    public Mdict(Context context, ExternalDatabase db, int dictId){
        mContext = context;
        mDatabase = db;
        mDictId = dictId;
        mDictInformation = mDatabase.getMdictInfoById(dictId);
        lt = (int) Math.pow(2, mDictInformation.getDictLang());

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
    public boolean isExistAudioUrl() {
        return false;
    }


    public int getId(){
        return mDictId;
    }

    @Override
    public String getDictionaryName() {
        return mDictInformation.getDictName().substring(mDictInformation.getDictName().lastIndexOf(File.separatorChar)+1);
    }

    @Override
    public int getLanguageType() {
        return lt;
    }

    @Override
    public String getIntroduction() {
        return mDictInformation.getDictIntro();
    }

    @Override
    public String[] getExportElementsList() {
        return mDictInformation.getFields();
    }

    @Override
    public List<Definition> wordLookup(String key) {
        key = Utils.keyCleanup(key);
        List<Definition> re = queryDefinition(key);
        if(mDictInformation.getDictLang() == DictLanguageType.ENG) {
            String[] deflectResult = FormsUtil.getInstance(mContext).getForms(key);
            if (deflectResult.length >= 0) {
                for (String deflectedWrod : deflectResult) {
                    re.addAll(queryDefinition(deflectedWrod));
                }
            }
        }

        //for handling japanese
        if(mDictInformation.getDictLang() == DictLanguageType.JPN){
            if(mWanaKanaJava == null){//lazy init of wanakana
                mWanaKanaJava = new WanaKanaJava(false);
            }

            if(mWanaKanaJava.isKatakana(key) || RegexUtil.isEnglish(key)){
                key = mWanaKanaJava.toHiragana(key);
            }
            re.addAll(queryDefinition(key));
            for(Deinflection df : Deinflector.deinflect(key)){
                String base = df.getBaseForm();
                List<Definition> defs = queryDefinition(base);
                re.addAll(defs);
            }
        }

        return re;
    }

    public ListAdapter getAutoCompleteAdapter(Context context, int layout) {
        return null;
    }

    private List<Definition> queryDefinition(String q){
        ArrayList<Definition> re = new ArrayList<>();
        String dictPath = mDictInformation.getDictName();
        File file = new File(dictPath);
//        if(file.exists()) {
//            String[] results = entryPoint(dictPath, q).split("\t");
//            for(String result : results) {
//                if(!result.equals(""))
//                    re.add(fromResultsToDefinition(new String[]{q, result}));
//            }
//        }

        if(file.exists()) {
            String[] results = entryPoint(dictPath, q).split(mDictInformation.getDefRegex());
            for(String result : results) {
                if(!result.equals(""))
                    re.add(fromResultsToDefinition(new String[]{q, result}));
            }
        }

        return re;
    }

    private Definition fromResultsToDefinition(String[] result){
        String[] fields = getExportElementsList();
        LinkedHashMap<String, String> eleMap = new LinkedHashMap<>();
        for(int i = 0; i < result.length; i ++){
            eleMap.put(fields[i], result[i]);
        }
        String complex = FieldUtil.formatComplexTplWord(
                mDictInformation.getDictName(),
                result[0],
                "",
                eleMap.get(Constant.DICT_FIELD_DEFINITION),
                Constant.AUDIO_INDICATOR_MP3);
        eleMap.put(
                Constant.DICT_FIELD_COMPLEX_ONLINE,
                complex);
        eleMap.put(
                Constant.DICT_FIELD_COMPLEX_OFFLINE,
                complex);
        String displayedHtml = "";
        String tmpl = mDictInformation.getDefTpml();
        if(tmpl.isEmpty()){   //no tmpl just join fields
            StringBuilder sb = new StringBuilder();
            for(String s : result){
                sb.append(s);
            }
            displayedHtml = sb.toString();
        }else{
            displayedHtml = Utils.renderTmpl(tmpl, eleMap);
        }
        String audioFileName = Utils.getSpecificFileName(result[0]);
        Definition.ResInformation resInfor = new Definition.ResInformation(
                "", audioFileName, Constant.MP3_SUFFIX
        );
        return new Definition(eleMap, displayedHtml, resInfor);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String entryPoint(String argument1, String argument2);

}
