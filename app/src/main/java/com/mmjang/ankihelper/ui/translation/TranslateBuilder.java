package com.mmjang.ankihelper.ui.translation;

import android.content.Context;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.data.dict.Definition;
import com.mmjang.ankihelper.data.dict.DictLanguageType;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.RegexUtil;
import com.mmjang.ankihelper.util.StringUtil;
import com.mmjang.ankihelper.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.ui.translation
 * @ClassName: TranslateUtil
 * @Description: java类作用描述
 * @Author: 唐朝
 * @CreateDate: 2022/8/17 10:02 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/8/17 10:02 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TranslateBuilder {
    String result;
    Translator translator;

    private static Class[] classList = new Class[]{
            BaiduTranslator.class,
            CaiyunTranslator.class,
            YoudaoTranslator.class,
            MicrosoftTranslator.class
    };

    public static String[] getNameArr()
    {
        ArrayList<String>  nameList = new ArrayList();
        for(int index=0; index < classList.length; index++) {
            try {
                Translator translator = (Translator)classList[index].getConstructor().newInstance();
                nameList.add(translator.name());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return nameList.toArray(new String[0]);
    }
    public TranslateBuilder(int index) {
        try {
            translator = (Translator) classList[index].getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public Translator getTranslator() {
        return this.translator;
    }
    public String getTextFromZhToEn(String text) {
        return translator.translate(text, translator.getZh(), translator.getEn());
    }

    public String getTextFromAutoToZh(String text) {
        return translator.translate(text, translator.getAuto(), translator.getZh());
    }

    public Definition getDefinition(String key, String[] DICT_EXP_ELE_LIST) {
        if (RegexUtil.isChineseSentence(key)) {
            result = translator.translate(key, translator.getZh(), translator.getEn());
        } else {
            result = translator.translate(key, translator.getAuto(), translator.getZh());
        }

        String notiString = "<font color='gray'>本词典和有道词典均未查到，以下是在线翻译</font><br/>";
        String definition = "<font color='gray'>翻译</font><br/>" + result;
        final String[] EXP_ELE_LIST = new String[]{
                Constant.DICT_FIELD_WORD,
                Constant.DICT_FIELD_DEFINITION,
        };
        if(Arrays.asList(DICT_EXP_ELE_LIST).containsAll(Arrays.asList(EXP_ELE_LIST))) {
            LinkedHashMap<String, String> exp = new LinkedHashMap<>();
            exp.put(EXP_ELE_LIST[0], key);
            exp.put(EXP_ELE_LIST[1], definition);
            String audioFileName = Utils.getSpecificFileName(key);
            return new Definition(exp, notiString + definition);
        } else {
            return null;
        }
    }

    public String translate(String text) {
        String from, to;
        if (RegexUtil.isChineseSentence(text)) {
            from = translator.getZh();
            to = translator.getEn();
        } else {
            from = translator.getAuto();
            to = translator.getZh();
        }
        return translator.translate(StringUtil.htmlTagFilter(text), from, to);
    }

    public String translate(String text, int dictLanguageType) {
        String from, to;
        if (RegexUtil.isChineseSentence(text)) {
            from = translator.getZh();
            switch(dictLanguageType) {
                case DictLanguageType.RUS:
                    to = translator.getRus();
                    break;
                case DictLanguageType.FRA:
                    to = translator.getFra();
                    break;
                case DictLanguageType.DEU:
                    to = translator.getDeu();
                    break;
                case DictLanguageType.SPA:
                    to = translator.getSpa();
                    break;
                case DictLanguageType.JPN:
                    to = translator.getJpn();
                    break;
                case DictLanguageType.KOR:
                    to = translator.getKor();
                    break;
                case DictLanguageType.THA:
                    to = translator.getTha();
                    break;
                default:
                    to = translator.getEn();
            }
        } else {
            from = translator.getAuto();
            to = translator.getZh();
        }
        return translator.translate(StringUtil.htmlTagFilter(text), from, to);
    }
}
