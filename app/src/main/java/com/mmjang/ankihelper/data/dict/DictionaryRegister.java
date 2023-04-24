package com.mmjang.ankihelper.data.dict;

import android.content.Context;
import android.os.BaseBundle;
import android.os.Build;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.data.dict.customdict.CustomDictionaryManager;
import com.mmjang.ankihelper.data.dict.mdict.MdictManager;
import com.mmjang.ankihelper.util.BuildConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liao on 2017/4/27.
 */

public class DictionaryRegister {
    //在这里注册词典类
    private static Class[] classList = new Class[]{
            Handian.class,
            Wordbean.class,
            Ode2.class,
            BingOxford.class,
            WebsterLearners.class,
            Collins.class,
            CollinsEnEn.class,
            DictionaryDotCom.class,
            VocabCom.class,
            Mnemonic.class,
            IdiomDict.class,
            UrbanDict.class,
            Dub91Sentence.class,
            RenRenCiDianSentence.class,
            EudicSentence.class,
            Getyarn.class,
            Cloze.class,
            BingImage.class,
            Kuromoji.class,
            JiSho.class,
            HujiangJapanese.class,
            Dedict.class,
            Frdict.class,
            Esdict.class,
//            SolrDictionary.class,
            DictTango.class,
            Mdict.class
//            EnglishDictSet.class,
//            EnglishSentenceSet.class
    };

    private static List<IDictionary> dictList;

    public Class[] getDictionaryClassArray() {
        return classList;
    }

    public static List<IDictionary> getDictionaryObjectList() {
        //if (dictList == null) {
            dictList = new ArrayList<>();
            for (Class c : classList) {
                try {
                    //切换 debug release
                    if(!BuildConfig.isDebug && (
                            c == Wordbean.class ||
                            c == Mdict.class ||
                            c == Handian.class ||
//                            c == Getyarn.class ||
                            c == DictTango.class))
                        continue;
                    dictList.add(
                            (IDictionary) c.getConstructor(Context.class).newInstance(MyApplication.getContext())
                    );
                } catch (NoSuchMethodException nsme) {
                } catch (InstantiationException ie) {
                } catch (IllegalAccessException ie) {
                } catch (InvocationTargetException ite) {
                }
            }
            List<IDictionary> customDictionaries = (new CustomDictionaryManager(MyApplication.getContext(), "")).getDictionaryList();
            dictList.addAll(customDictionaries);

            List<IDictionary> mdicts = (new MdictManager(MyApplication.getContext(), "")).getDictionaryList();
            dictList.addAll(mdicts);
        //}
        return dictList;
    }
}
