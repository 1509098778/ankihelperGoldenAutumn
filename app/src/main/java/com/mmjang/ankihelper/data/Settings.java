package com.mmjang.ankihelper.data;

/**
 * Created by liao on 2017/4/13.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mmjang.ankihelper.data.dict.DictLanguageType;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 单例，getInstance()得到实例
 */
public class Settings {

    private static Settings settings = null;

    private final static String PREFER_NAME = "settings";    //应用设置名称
    private final static String MODEL_ID = "model_id";       //应用设置项 模版id
    private final static String DECK_ID = "deck_id";         //应用设置项 牌组id
    private final static String DEFAULT_MODEL_ID = "default_model_id"; //默认模版id，如果此选项存在，则已写入配套模版
    private final static String FIELDS_MAP = "fields_map";   //字段映射
    private final static String MONITE_CLIPBOARD_Q = "show_clipboard_notification_q";   //是否监听剪切板
    private final static String AUTO_CANCEL_POPUP_Q = "auto_cancel_popup";              //点加号后是否退出
    private final static String DEFAULT_PLAN = "default_plan";
    private final static String LAST_SELECTED_PLAN = "last_selected_plan";
    private final static String DEFAULT_TAG = "default_tag";
    private final static String SET_AS_DEFAULT_TAG = "set_as_default_tag";
    private final static String LAST_PRONOUNCE_LANGUAGE = "last_pronounce_language";
    private final static String LEFT_HAND_MODE_Q = "left_hand_mode_q";
    private final static String PINK_THEME_Q = "pink_theme_q";
    private final static String OLD_DATA_MIGRATED = "old_data_migrated";
    private final static String SHOW_CONTENT_ALREADY_READ = "show_content_already_read";
    private final static String FIRST_TIME_RUNNING_READER = "first_time_running_reader";

    public final static String USER_YOUDAO_APP_ID = "user_youdao_app_id";
    public final static String USER_YOUDAO_APP_KEY = "user_youdao_app_key";

    private final static String USER_CAIYUN_APP_SECRET_KEY = "user_caiyun_app_secret_key";

    private final static String USER_MICROSOFT_APP_ID = "user_microsoft_translate_app_id";

    private final static String USER_BAIDUFANYI_APP_ID = "user_baidu_fanyi_app_id";
    private final static String USER_BAIDUFANYI_APP_KEY = "user_baidu_fanyi_app_key";

    private final static String DICTTANGO_ONLINE_URL = "mdictionary_online_url";

    private final static String CURRENT_DICTIONARY_LANGUAGE_TYPE = "current_dictionary_language_type";
    private final static String CURRENT_DICTIONARY_NAME = "current_dictionary_name";

    private final static String LOCATORS_STRING = "locators_string";

    private  final static String MDICT_CHECKER = "mdict_checker_";

    private  final static String OCR_TESSERACT_TRAINEDDATA_CHECKBOX_MAP = "ocr_tess_checker_lang_traineddata";

    private final static  String OCR_MLKIT_CHECKED_INDEX = "ocr_mlkit_checked_index";

    public final static String OCR_MATHPIX_ID = "ocr_mathpix_id";
    public final static String OCR_MATHPIX_KEY = "ocr_mathpix_key";
    public final static  String OCR_MATHPIX_CHECKED_INDEX = "ocr_mathpix_checked_index";

    private final static String TRANSLATOR_CHECKED_INDEX = "translator_checked_index";

    private final static String ORC_SELECTED_LANG = "orc_selected_lang";

    private final static String FLOAT_BALL_ENABLE = "float_ball_enable";

    public final static String COPY_MARKED_TEXT = "copy_marked_text";

    public final static String POPUP_SWITCH_SCROLL_BOTTOM = "popup_switch_scroll_bottom";

    public final static String POPUP_SWITCH_COPY = "popup_switch_copy";

    public final static String CAPTURE_RESULT_EDIT_MODE = "capture_result_mode";

    public final static String DARK_MODE_INDEX = "dark_modea_index";

    public final static String THEME_COLOR_INDEX = "theme_color_index";

    private final static String CLEAR_SEARCHED_ENABLE =  "clear_searched_enable";

    private final static String POPUP_TOOLBAR_AUTOMATIC_TRANSLATION_ENABLE = "pop_toolbar_automatic_translation_enable";

    private final static String POPUP_TOOLBAR_SEARCH_ENABLE = "popup_toolbar_search_enable";

    private final static String POPUP_TOOLBAR_NOTE_ENABLE = "popup_toolbar_note_enable";

    private final static String POPUP_TOOLBAR_TAG_ENABLE = "popup_toolbar_tag_enable";

    public static final String FLOATING_BUTTON_POSITION = "floating_button_position";

    private  final static String POPUP_TEXT_SIZE= "popup_text_size";

    private final static String FLOATING_BUTTON_ALPHA = "floating_button_alpha";

    private  final static String POPUP_SWITCH_SYMBOL_SELECTION = "popup_switch_symbol_selection";

    private final static String POPUP_SPINNER_DECK_ENABLE = "popup_spinner_deck_enable";

    private final static String POPUP_IGNORE_DECK_SCHEME = "popup_ignore_deck_scheme";

    private final static String LAST_MODEL_ID = "last_model_id";       //应用设置项 模版id

    private final static String FILE_PICKER_HISTORY_PATH = "file_picker_history_path";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    private Settings(Context context) {
        sp = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 获得单例
     *
     * @return
     */
    public static Settings getInstance(Context context) {
        if (settings == null) {
            settings = new Settings(context);
        }
        return settings;
    }

    public SharedPreferences getSharedPreferences() {
        return sp;
    }
    /*************/

    Long getModelId() {
        return sp.getLong(MODEL_ID, 0);
    }

    void setModelId(Long modelId) {
        editor.putLong(MODEL_ID, modelId);
        editor.commit();
    }

    /**************/

    Long getDeckId() {
        return sp.getLong(DECK_ID, 0);
    }

    void setDeckId(Long deckId) {
        editor.putLong(DECK_ID, deckId);
        editor.commit();
    }

    /**************/

    Long getDefaultModelId() {
        return sp.getLong(DEFAULT_MODEL_ID, 0);
    }

    void setDefaultModelId(Long defaultModelId) {
        editor.putLong(DEFAULT_MODEL_ID, defaultModelId);
        editor.commit();
    }

    /**************/

    String getFieldsMap() {
        return sp.getString(FIELDS_MAP, "");
    }

    void setFieldsMap(String filedsMap) {
        editor.putString(FIELDS_MAP, filedsMap);
        editor.commit();
    }

    /**************/

    public boolean getMoniteClipboardQ() {
        return sp.getBoolean(MONITE_CLIPBOARD_Q, false);
    }

    public void setMoniteClipboardQ(boolean moniteClipboardQ) {
        editor.putBoolean(MONITE_CLIPBOARD_Q, moniteClipboardQ);
        editor.commit();
    }

    /**************/

    public boolean getAutoCancelPopupQ() {
        return sp.getBoolean(AUTO_CANCEL_POPUP_Q, false);
    }

    public void setAutoCancelPopupQ(boolean autoCancelPopupQ) {
        editor.putBoolean(AUTO_CANCEL_POPUP_Q, autoCancelPopupQ);
        editor.commit();
    }

    /**************/
    public String getDefaultPlan() {
        return sp.getString(DEFAULT_PLAN, "");
    }

    public void setDefaultPlan(String defaultPlan) {
        editor.putString(DEFAULT_PLAN, defaultPlan);
        editor.commit();
    }

    /******************/

    public String getLastSelectedPlan() {
        return sp.getString(LAST_SELECTED_PLAN, "");
    }

    public void setLastSelectedPlan(String lastSelectedPlan) {
        editor.putString(LAST_SELECTED_PLAN, lastSelectedPlan);
        editor.commit();
    }

    /*****************/
    public String getDefaulTag() {
        return sp.getString(DEFAULT_TAG, "");
    }

    public void setDefaultTag(String defaultTag) {
        editor.putString(DEFAULT_TAG, defaultTag);
        editor.commit();
    }

    /****************/
    public boolean getSetAsDefaultTag() {
        return sp.getBoolean(SET_AS_DEFAULT_TAG, false);
    }

    public void setSetAsDefaultTag(boolean setAsDefaultTag) {
        editor.putBoolean(SET_AS_DEFAULT_TAG, setAsDefaultTag);
        editor.commit();
    }

    public boolean getLeftHandModeQ() {
        return sp.getBoolean(LEFT_HAND_MODE_Q, false);
    }

    public void setLeftHandModeQ(boolean leftHandModeQ) {
        editor.putBoolean(LEFT_HAND_MODE_Q, leftHandModeQ);
        editor.commit();
    }

    public boolean getPinkThemeQ() {
        return sp.getBoolean(PINK_THEME_Q, false);
    }

    public void setPinkThemeQ(boolean pinkThemeQ) {
        editor.putBoolean(PINK_THEME_Q, pinkThemeQ);
        editor.commit();
    }

    public boolean getOldDataMigrated() {
        return sp.getBoolean(OLD_DATA_MIGRATED, false);
    }

    public void setOldDataMigrated(boolean oldDataMigrated) {
        editor.putBoolean(OLD_DATA_MIGRATED, oldDataMigrated);
        editor.commit();
    }

    public boolean getShowContentAlreadyRead() {
        return sp.getBoolean(SHOW_CONTENT_ALREADY_READ, false);
    }

    public void setShowContentAlreadyRead(boolean showContentAlreadyRead) {
        editor.putBoolean(SHOW_CONTENT_ALREADY_READ, showContentAlreadyRead);
        editor.commit();
    }

    public boolean getFirstTimeRunningReader() {
        return sp.getBoolean(FIRST_TIME_RUNNING_READER, true);
    }

    public void setFirstTimeRunningReader(boolean firstTimeRunningReader) {
        editor.putBoolean(FIRST_TIME_RUNNING_READER, firstTimeRunningReader);
        editor.commit();
    }

    public int getPopupFontSize() {
        return sp.getInt(POPUP_TEXT_SIZE, 14);
    }

    public void setPopupTextSize(int size) {
        editor.putInt(POPUP_TEXT_SIZE, size);
        editor.commit();
    }

    public int getFloatingButtonAlpha() {
        return sp.getInt(FLOATING_BUTTON_ALPHA, 100);
    }

    public void setFloatingButtonAlpha(int size) {
        editor.putInt(FLOATING_BUTTON_ALPHA, size);
        editor.commit();
    }


    public boolean getFloatBallEnable() {
        return sp.getBoolean(FLOAT_BALL_ENABLE, true);
    }

    public void setFloatBallEnable(boolean status) {
        editor.putBoolean(FLOAT_BALL_ENABLE, status);
        editor.commit();
    }

    public boolean getClearSearchedEnable() {
        return sp.getBoolean(CLEAR_SEARCHED_ENABLE, false);
    }

    public void setClearSearchedEnable(boolean status) {
        editor.putBoolean(CLEAR_SEARCHED_ENABLE, status);
        editor.commit();
    }

    public boolean getPopupSwitchSymbolSelection() {
        return sp.getBoolean(POPUP_SWITCH_SYMBOL_SELECTION, false);
    }


    public void setPopupSwitchSymbolSelection(boolean status) {
        editor.putBoolean(POPUP_SWITCH_SYMBOL_SELECTION, status);
        editor.commit();
    }

    public boolean getPopupToolbarAutomaticTranslationEnable() {
        return sp.getBoolean(POPUP_TOOLBAR_AUTOMATIC_TRANSLATION_ENABLE, false);
    }

    public void setPopupToolbarAutomaticTranslationEnable(boolean status) {
        editor.putBoolean(POPUP_TOOLBAR_AUTOMATIC_TRANSLATION_ENABLE, status);
        editor.commit();
    }

    public boolean getPopupToolbarSearchEnable() {
        return sp.getBoolean(POPUP_TOOLBAR_SEARCH_ENABLE, true);
    }

    public void setPopupToolbarSearchEnable(boolean status) {
        editor.putBoolean(POPUP_TOOLBAR_SEARCH_ENABLE, status);
        editor.commit();
    }

    public boolean getPopupToolbarNoteEnable() {
        return sp.getBoolean(POPUP_TOOLBAR_NOTE_ENABLE, true);
    }

    public void setPopupToolbarNoteEnable(boolean status) {
        editor.putBoolean(POPUP_TOOLBAR_NOTE_ENABLE, status);
        editor.commit();
    }

    public boolean getPopupToolbarTagEnable() {
        return sp.getBoolean(POPUP_TOOLBAR_TAG_ENABLE, true);
    }

    public void setPopupToolbarTagEnable(boolean status) {
        editor.putBoolean(POPUP_TOOLBAR_TAG_ENABLE, status);
        editor.commit();
    }

    public void setFloatingButtonPosition(Point point) {
        editor.putInt(FLOATING_BUTTON_POSITION + "_x", point.x);
        editor.putInt(FLOATING_BUTTON_POSITION + "_y", point.y);
        editor.commit();
    }

    public Point getFloatingButtonPosition() {
        return new Point(
                sp.getInt(FLOATING_BUTTON_POSITION + "_x", 200),
                sp.getInt(FLOATING_BUTTON_POSITION + "_y", 200));
    }

    public boolean getPopupSpinnerDeckEnable() {
        return sp.getBoolean(POPUP_SPINNER_DECK_ENABLE, false);
    }

    public void setPopupSpinnerDeckEnable(boolean status) {
        editor.putBoolean(POPUP_SPINNER_DECK_ENABLE, status);
        editor.commit();
    }

    public boolean getPopupIgnoreDeckScheme() {
        return sp.getBoolean(POPUP_IGNORE_DECK_SCHEME, false);
    }

    public void setPopupIgnoreDeckScheme(boolean status) {
        editor.putBoolean(POPUP_IGNORE_DECK_SCHEME, status);
        editor.commit();
    }

    public Long getLastDeckId() {
        return sp.getLong(LAST_MODEL_ID, -1);
    }

    public void setLastDeckId(Long deckId) {
        editor.putLong(LAST_MODEL_ID, deckId);
        editor.commit();
    }

    public String getUserMicrosoftAppId() {
        return sp.getString(USER_MICROSOFT_APP_ID, "");
    }

    public void setUserMicrosoftAppId(String userMicrosoftAppId) {
        editor.putString(USER_MICROSOFT_APP_ID, userMicrosoftAppId);
        editor.commit();
    }

    public String getUserBaidufanyiAppId() {
        return sp.getString(USER_BAIDUFANYI_APP_ID, "");
    }

    public void setUserBaidufanyiAppId(String userBaidufanyiAppId) {
        editor.putString(USER_BAIDUFANYI_APP_ID, userBaidufanyiAppId);
        editor.commit();
    }

    public String getUserBaidufanyiAppKey() {
        return sp.getString(USER_BAIDUFANYI_APP_KEY, "");
    }

    public void setUserBaidufanyiAppKey(String userBaidufanyiAppKey) {
        editor.putString(USER_BAIDUFANYI_APP_KEY, userBaidufanyiAppKey);
        editor.commit();
    }

    public String getDictTangoOnlineUrl() {
        return sp.getString(DICTTANGO_ONLINE_URL, "http://127.0.0.1:1688");
    }

    public void setDictTangoOnlineUrl(String url) {
        editor.putString(DICTTANGO_ONLINE_URL, url);
        editor.commit();
    }

    public int getLastPronounceLanguage() {
        return sp.getInt(LAST_PRONOUNCE_LANGUAGE, 0);//PronounceManager.LANGUAGE_ENGLISH_UK_YOUDAO_INDEX);
    }

    public void setLastPronounceLanguage(int lastPronounceLanguageIndex) {
        editor.putInt(LAST_PRONOUNCE_LANGUAGE, lastPronounceLanguageIndex);
        editor.commit();
    }


    public void setRestorePronounceSpinnerIndex(int index, int languageType, boolean isExistAudioUrl, boolean isloadedTTS) {
        String PRONOUNCE_SPINNER_INDEX_NAME =
                String.format("%s_%s_%s",
                        DictLanguageType.getLanguageISO3ByLTId(languageType),
                    String.valueOf(isExistAudioUrl),
                    String.valueOf(isloadedTTS));
        editor.putInt(PRONOUNCE_SPINNER_INDEX_NAME, index);
        editor.commit();
//        Log.e("Settings", String.format("(%d)set %s is %d", languageType, PRONOUNCE_SPINNER_INDEX_NAME, index));
    }

    public int getRestorePronounceSpinnerIndex(int languageType, boolean isExistAudioUrl, boolean isloadedTTS) {
        String PRONOUNCE_SPINNER_INDEX_NAME =
                String.format("%s_%s_%s",
                        DictLanguageType.getLanguageISO3ByLTId(languageType),
                        String.valueOf(isExistAudioUrl),
                        String.valueOf(isloadedTTS));
        int index = sp.getInt(PRONOUNCE_SPINNER_INDEX_NAME, -1);
//        Log.e("Settings", String.format("(%d)get %d from %s", languageType, index, PRONOUNCE_SPINNER_INDEX_NAME));
        return index;
    }

    public HashMap<String, Boolean> getTangoDictChckerMap(String planName) {
        Gson gson = new Gson();
        Type type= new TypeToken<HashMap<String, Boolean>>(){}.getType();
        String jsonStr = sp.getString(MDICT_CHECKER +planName, "");
        if(jsonStr.equals("")) {
            return new HashMap<>();
        }
        return gson.fromJson(jsonStr, type);
    }

    public boolean setTangoDictCheckerMap(String planName, HashMap<String, Boolean> dictCheckerMap) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(dictCheckerMap);
        editor.putString(MDICT_CHECKER +planName, jsonStr);
        editor.commit();
        return true;
    }

    public boolean removeMdictCheckerMap(String planName) {
        editor.remove(MDICT_CHECKER +planName);
        editor.commit();
        return true;
    }

    public int getTranslatorCheckedIndex() {
        return sp.getInt(TRANSLATOR_CHECKED_INDEX, 0);
    }

    public void setTranslatorCheckedIndex(int index) {
        editor.putInt(TRANSLATOR_CHECKED_INDEX, index);
        editor.commit();
    }

    public int getMlKitOcrLangCheckedIndex() {
        return sp.getInt(OCR_MLKIT_CHECKED_INDEX, 0);
    }

    public void setMlKitOcrCheckedIndex(int index) {
        editor.putInt(OCR_MLKIT_CHECKED_INDEX, index);
        editor.commit();
    }

    public HashMap<String, Boolean> getTesseractOcrTraineddataCheckBoxMap() {
        Gson gson = new Gson();
        Type type= new TypeToken<HashMap<String, Boolean>>(){}.getType();
        String jsonStr = sp.getString(OCR_TESSERACT_TRAINEDDATA_CHECKBOX_MAP, "");
        if(jsonStr.equals("")) {
            return new HashMap<>();
        }
        return gson.fromJson(jsonStr, type);
    }

    public boolean setTesseractOcrTraineddataCheckBoxMap(HashMap<String, Boolean> dictCheckerMap) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(dictCheckerMap);
        editor.putString(OCR_TESSERACT_TRAINEDDATA_CHECKBOX_MAP, jsonStr);
        editor.commit();
        return true;
    }

    public String getOcrSelectedLang() {
        return sp.getString(ORC_SELECTED_LANG, "");
    }

    public void setOrcSelectedLang(String lang) {
        editor.putString(ORC_SELECTED_LANG, lang);
        editor.commit();
    }

    public String getLocatorsString() {
        return sp.getString(LOCATORS_STRING, "\n");
    }

    public void setLocatorsString(String locatorsString) {
        editor.putString(LOCATORS_STRING, locatorsString);
        editor.commit();
    }

    public String getFilePickerHistoryPath() { return sp.getString(FILE_PICKER_HISTORY_PATH, ""); }

    public void setFilePickerHistoryPath(String path) {
        editor.putString(FILE_PICKER_HISTORY_PATH, path);
        editor.commit();
    }

//    //checkd locator
//    public String getLocatorsCheckedString() {
//        return sp.getString(LOCATORS_CHECKED_STRING, "\n");
//    }
//
//    public void setLocatorsCheckedString(String locatorsString) {
//        editor.putString(LOCATORS_CHECKED_STRING, locatorsString);
//        editor.commit();
//    }


    boolean hasKey(String key) {
        return sp.contains(key);
    }


    public String getUserCaiyunAppSecretKey() {
        return sp.getString(USER_CAIYUN_APP_SECRET_KEY, "");
    }

    public void setUserCaiyunAppSecretKey(String key) {
        editor.putString(USER_CAIYUN_APP_SECRET_KEY, key);
        editor.commit();
    }

    public boolean put(String TAG, String value) {
        editor.putString(TAG, value);
        editor.commit();
        return true;
    }

    public boolean put(String TAG, boolean value) {
        editor.putBoolean(TAG, value);
        editor.commit();
        return true;
    }

    public boolean put(String TAG, int value) {
        editor.putInt(TAG, value);
        editor.commit();
        return true;
    }

    public String get(String TAG, String defaultValue) {
        return sp.getString(TAG, defaultValue);
    }

    public boolean get(String TAG, boolean defaultValue) {
        return sp.getBoolean(TAG, defaultValue);
    }

    public int get(String TAG, int defaultValue) {
        return sp.getInt(TAG, defaultValue);
    }

}