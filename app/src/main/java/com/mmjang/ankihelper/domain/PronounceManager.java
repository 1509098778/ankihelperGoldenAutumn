package com.mmjang.ankihelper.domain;

import android.util.Log;

import com.mmjang.ankihelper.data.dict.DictLanguageType;
import com.mmjang.ankihelper.data.dict.IDictionary;
import com.mmjang.ankihelper.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Gao on 2017/7/15.
 */

public class PronounceManager {

    //音频来源于以下词典 soundSource
    public static final int SOUND_SOURCE_YOUDAO = 1;
    public static final int SOUND_SOURCE_EUDICT = 2;
    public static final int SOUND_SOURCE_TTS = 3;
    public static final int SOUND_SOURCE_ONLINE = 4;

    public static ArrayList<SoundInformation> soundInforList = new ArrayList<>();


    public void PronounceManager() {
    }

    public static SoundInformation getDictInformationByIndex(int index) {
        try {
            if(index == -1) {
                return soundInforList.get(soundInforList.size()-1);
            } else if(index > -1 || index < soundInforList.size()) {
                return soundInforList.get(index);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static int getDictInformationSize() {
        return soundInforList.size();
    }

    public static int getSoundInforIndexByList(int DICT_LANGUAGE_TYPE) {
        for(int index=0; index < soundInforList.size(); index++) {
            if(soundInforList.get(index).getDictLanguageType() == DICT_LANGUAGE_TYPE)
                return index;
        }
        return -1;
    }
    public static String[] getAvailablePronounceLanguage(final IDictionary dict, final boolean toLoadTTS) {

        int dictLanguageType = dict.getLanguageType();
        String soundName = dict.getDictionaryName();
        boolean isExistAudioOrVideoUrl = dict.isExistAudioUrl();

        if(!soundInforList.isEmpty()) {
            soundInforList.clear();
        }
        
        if(dictLanguageType == DictLanguageType.ZHO || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //欧路 汉
                            DictLanguageType.ZHO,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("CHN"), "zho-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"cn"}
                    )
            );
            loadTts(DictLanguageType.ZHO, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.RUS || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //有道 俄语
                            DictLanguageType.RUS,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("RUS"), "rus-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"ru"}
                    )
            );
            soundInforList.add(
                    new SoundInformation(
                            //欧路 俄语
                            DictLanguageType.RUS,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("RUS"), "rus-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"ru"}
                    )
            );
            loadTts(DictLanguageType.RUS, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.KOR || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //有道 朝鲜
                            DictLanguageType.KOR,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("PRK"), "prk-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"ko"}
                    )
            );
            loadTts(DictLanguageType.KOR, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.JPN || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //有道 日
                            DictLanguageType.JPN,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("JPN"), "jpn-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"jap"}
                    )
            );
            soundInforList.add(
                    new SoundInformation(
                            //欧路 日
                            DictLanguageType.JPN,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("JPN"), "jpn-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"jap"}
                    )

            );
            loadTts(DictLanguageType.JPN, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.ENG || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //英语 有道 uk
                            DictLanguageType.ENG,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("GBR"), "eng-GBR-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"en", "1"}
                    )
            );
            soundInforList.add(
                    new SoundInformation(
                            //欧路 英语 uk
                            DictLanguageType.ENG,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("GBR"), "eng-GBR-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"eng", "en_uk_male"}
                    )
            );
            soundInforList.add(
                    new SoundInformation(
                            //有道 英语 us
                            DictLanguageType.ENG,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("USA"), "eng-USA-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"en", "2"}
                    )
            );
            soundInforList.add(
                    new SoundInformation(
                            //欧路 英语 us
                            DictLanguageType.ENG,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("USA"), "eng-USA-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"eng", "en_us_female"}
                    )
            );
            loadTts(DictLanguageType.ENG, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.FRA || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //有道 法
                            DictLanguageType.FRA,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("FRA"), "fra-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"fr"}
                    )
            );
            soundInforList.add(
                    new SoundInformation(
                            //欧路 法
                            DictLanguageType.FRA,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("FRA"), "fra-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"fr"}
                    )
            );
            loadTts(DictLanguageType.FRA, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.DEU || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //欧路 德
                            DictLanguageType.DEU,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("DEU"), "deu-o"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"de"}
                    )
            );
            loadTts(DictLanguageType.DEU, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.SPA || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //欧路 西
                            DictLanguageType.SPA,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("ESP"), "spa-y"),
                            SOUND_SOURCE_EUDICT,
                            new String[] {"es"}
                    )
            );
            loadTts(DictLanguageType.SPA, toLoadTTS);
        }
        if(dictLanguageType == DictLanguageType.THA || dictLanguageType == DictLanguageType.ALL) {
            soundInforList.add(
                    new SoundInformation(
                            //有道 泰
                            DictLanguageType.THA,
                            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("THA"), "tha-y"),
                            SOUND_SOURCE_YOUDAO,
                            new String[] {"th"}
                    )
            );
            loadTts(DictLanguageType.THA, toLoadTTS);
        }

        if(isExistAudioOrVideoUrl) {
            soundInforList.add(
                    new SoundInformation(
                            dictLanguageType,
                            soundName,
                            PronounceManager.SOUND_SOURCE_ONLINE,
                            new String[]{}
                    ));
        }

        String dictSoundNames[] = new String[soundInforList.size()];
        for(int index = 0; index < soundInforList.size(); index++) {
            dictSoundNames[index] = soundInforList.get(index).getSoundName();
        }
        return dictSoundNames;
    }

    private static void loadTts(int dictLanguageType, boolean toLoadTTS) {

        if(dictLanguageType != DictLanguageType.ALL && toLoadTTS) {
            ArrayList<String> available = new ArrayList<>(Arrays.asList(Constant.SUPPORTEDLANGUAGES));
            for(String language : available) {
                String[] temp = language.split("-");
//                Log.i("available language", temp[0]);
//                Log.i("CountryISO3 of Locale", DictLanguageType.getLanguageISO3ByLTId(dictLanguageType));

                if (temp[0].equals(DictLanguageType.getLanguageISO3ByLTId(dictLanguageType))) {
                    soundInforList.add(
                            new SoundInformation(
                                    //欧路 汉
                                    dictLanguageType,
                                    String.format("%s %s", DictLanguageType.getFlagByCountryISO3(temp[1]), language),
                                    SOUND_SOURCE_TTS,
                                    temp
                            ));
                }
            }
        }
    }

    /*
     * 语音信息类
     * 用于保存语音信息：语言、声音名称、声源、locale字符串化
     */
    public static class SoundInformation {
        private int dictLanguageType;
        private String soundName;
        private int soundSourceType;
        private String[] langAndCountry;
        private Locale locale;

        public String[] getLangAndCountry() {
            return langAndCountry;
        }

        public Locale getLocale() {
            return locale;
        }

        public SoundInformation(int dictLanguageType, String soundName, int soundSourceType, String[] langAndCountry) {
            this.dictLanguageType = dictLanguageType;
            this.soundName = soundName;
            this.soundSourceType = soundSourceType;
            this.langAndCountry = langAndCountry;
        }
        public int getDictLanguageType() {
            return dictLanguageType;
        }
        public void setDictLanguageType(int dictLanguageType) {
            this.dictLanguageType = dictLanguageType;
        }

        public String getSoundName() {
            return soundName;
        }

        public int getSoundSourceType() {
            return soundSourceType;
        }
        public boolean isSoundSourceType(int soundSourceType) {
            if(this.soundSourceType == soundSourceType) {
                return true;
            } else {
                return false;
            }
        }

    }
}
