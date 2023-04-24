package com.mmjang.ankihelper.data.dict.mdict;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mmjang.ankihelper.data.database.ExternalDatabase;
import com.mmjang.ankihelper.data.dict.DictLanguageType;
import com.mmjang.ankihelper.data.dict.IDictionary;
import com.mmjang.ankihelper.data.dict.Mdict;
import com.mmjang.ankihelper.util.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ss on 2022/11/13.
 */

public class MdictManager {
    private static final String DICTIONARY_FILE_EXTENSION = ".mdx";
    private static final String DEFAULT_ENCODING = "UTF8";
    private static final String EQUAL = "=";
    private static final int MAX_ENTRIES_ONE_WRITE = 100000;
    private static final String META= "META";
    private static final String ENDMETA= "ENDMETA";
    private static final String META_VERSION = "VERSION";
    private static final String META_DICT_NAME = "DICT_NAME";
    private static final String META_DICT_INTRO = "DICT_INTRO";
    private static final String META_DICT_LANG = "DICT_LANG";
    private static final String META_DEF_TMPL = "DEF_TMPL";
    private static final String META_FIELDS = "FIELDS";

    public ExternalDatabase db;
    String mDictionaryPath;
    Context mContext;

    List<File> tabDictionaryFiles = new ArrayList<>();

    private enum PARSE_STATE{
        START,
        META,
        DATA
    }

    public MdictManager(Context context, @NonNull String dictionaryPath){
        db = ExternalDatabase.getInstance();
        mDictionaryPath = dictionaryPath;
        mContext = context;
    }

    public boolean reFreshDB(){
        List<File> files = findDictionaryFiles(mDictionaryPath);
        if(files.size() == 0){
            return false;
        }
        db.clearMdictDB();
        int i = 0;
        for(File file : files){
            if(processOneDictionaryFile(i, file)){
                i ++;
            }
        }

        if(i == 0)  //all .txt is illegal
        {
            return false;
        }else{
            return true;
        }
    }

    public List<File> findDictionaryFiles(String dictionaryPath){
        File directory = new File(dictionaryPath);
        File[] files = directory.listFiles();
        List<File> result = new ArrayList<>();
        if(files != null){
            for(File file : files){
                if(file.isFile() && file.getPath().endsWith(DICTIONARY_FILE_EXTENSION)) {
                    result.add(file);
                }
            }
        }
        return result;
    }

    public void clearDictionaries(){
        db.clearMdictDB();
    }

    public void clearDictById(int index) { db.deleteMdictById(index);}

    public void updateOrder(int id, int to) {
        db.updateOrderMdict(id, to);
    }

    public int updateMdictInformation(MdictInformation mdictInformation) {
        return db.updateMdictInformation(mdictInformation);
    }

    public MdictInformation getMdictInfoById(int id) {
        try {
            return db.getMdictInfoById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public MdictInformation getMdictInfoByOrder(int order) {
        try {
            return db.getMdictInfoByOrder(order);
        } catch (Exception e) {
            return null;
        }
    }

    public List<IDictionary> getDictionaryList(){
        List<IDictionary> dictionaries = new ArrayList<>();
        for(int id : db.getMdictIdList()){
            dictionaries.add(new Mdict(mContext, db, id));
        }
        return  dictionaries;
    }

    public List<MdictInformation> getDictInfoList() {
        List<MdictInformation> dictInfoList = new ArrayList<>();
        for(int id : db.getMdictIdList()){
            dictInfoList.add(db.getMdictInfoById(id));
        }
        return  dictInfoList.stream().sorted(Comparator.comparing(MdictInformation::getOrder)).collect(Collectors.toList());
    }

    public boolean processOneDictionaryFile(int id, File dictFile){
        try {
//            String dictFileName = dictFile.getPath();//dictFile.getName().replace(DICTIONARY_FILE_EXTENSION, "");
//            String dictName = dictFile.getPath();; //the default name is the file name
//            String dictIntro = "test";
//            String dictLang = "all"; //en jp fr etc
//            String tmpl = "{{单词}}<br/>{{中文释义}}"; //if empty, just join all fields.
//            String[] fields = new String[] {"单词", "中文释义"};
//            //insert remaining entries
//            db.addMdictInformation(id, dictName, dictLang, fields, dictIntro, tmpl, id);
            MdictInformation mdictInformation = new MdictInformation(
                    id,
                    dictFile.getPath(),
                    "来自本地" + dictFile.getPath(),
                    DictLanguageType.getLanguageNameList().length-1,
                    "{{单词}}<br/>{{释义}}<br/>",
                    "\\t",
                    new String[] {"单词",
                            "释义",
                            Constant.DICT_FIELD_COMPLEX_ONLINE,
                            Constant.DICT_FIELD_COMPLEX_OFFLINE},
                    id
            );
            db.addMdictInformation(mdictInformation);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String[] splitLineByTab(String line){
        String[] results = line.split("\t");
        for(String s : results){
            s = s.trim();
        }
        return results;
    }

    @Nullable
    private String getMetaValue(String metaString){
        int index = metaString.indexOf(EQUAL);
        int len = metaString.length();
        if(index > 0){
            return metaString.substring(index + 1, len);
        }else{
            return null;
        }
    }
}
