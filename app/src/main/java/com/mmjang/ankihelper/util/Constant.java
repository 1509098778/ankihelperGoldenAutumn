package com.mmjang.ankihelper.util;

import android.os.Environment;

import com.mmjang.ankihelper.ui.widget.button.MLLabel;

/**
 * Created by liao on 2017/4/27.
 */

public class Constant {
    private static final String[] SHARED_EXPORT_ELEMENTS = new String[]{
            "空",
            "摘取例句",
            "摘取例句（加粗）",
            "摘取例句（挖空）",
            "摘取例句（挖空，全c1模式）",
            "笔记",
            "URL",
            "所有释义",
            "句子翻译",
            "🔊|🎞🌐🔗",
            "🔊|🎞🌐▶️<Video>",
            "🔊|🎞🌐▶️[Sound]",
            "🔊|🎞💾🔗",
            "🔊|🎞💾▶️<Video>",
            "🔊|🎞💾▶️[Sound](🍏Remarks)",
            "摘取例句🔊🌐🔗",
            "摘取例句🔊🌐▶️[Sound]",
            "摘取例句🔊💾🔗",
            "摘取例句🔊💾▶️[Sound]",
            //"FBReader跳转链接"
    };

    public static String[] getSharedExportElements() {
        return SHARED_EXPORT_ELEMENTS;
    }

    public static final String[] DEFAULT_FIELDS = new String[]{
            "body",
            "词性",
            Constant.DICT_FIELD_PHONETICS,
            Constant.DICT_FIELD_DEFINITION,
    };
    //
    public static final String DICT_FIELD_WORD = "单词";
    public static final String DICT_FIELD_PHONETICS = "音标";
    public static final String DICT_FIELD_DEFINITION = "释义";
    public static final String DICT_FILED_SENSE = "词性";
    public static final String DICT_FIELD_JS = "js";
    public static final String DICT_FIELD_CSS = "css";
    public static final String DICT_FIELD_COMPLEX_ONLINE = "复合项（在线音频）";
    public static final String DICT_FIELD_COMPLEX_OFFLINE = "复合项（本地音频）";
    public static final String DICT_FIELD_COMPLEX_MUTE = "复合项（无音频）";

    public static String[] getDefaultFields() { return DEFAULT_FIELDS; }

    public static final String[] MLKIT_TEXT_RECONGNITION_LANGS = new String[] {
            "汉语（Chinese）",
            "日本語（Japanese）",
            "한국의（朝鲜语）",
            "Latin（拉丁语系）",
            "संस्कृतम्（梵文）"

    };

    public enum Mode {
        NORMAL_MODE("普通模式"), LATEX_MODE("Latex模式");

        private String name;
        Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum DarkMode {
        MODE_NIGHT_FOLLOW_SYSTEM("系统"),
        MODE_NIGHT_NO("亮色"),
        MODE_NIGHT_YES("暗色");

        private String name;
        DarkMode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum ThemeColor {
        THEME_COLOR_GREEN("绿色"),
        THEME_COLOR_BLUE("蓝色"),
        THEME_COLOR_PINK("粉色");

        private String name;
        ThemeColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum StyleBaseTheme{
        AppTheme, Transparent, BigBangTheme, ScreenCaptureTheme, CustomDicTheme, AndroidFilePickerTheme;
    }

    //HtmlTagArr
    public static MLLabel[] htmlTagArr = new MLLabel[] {
            new MLLabel("p", "<p>", "</p>"),
            new MLLabel("b", "<b>", "</b>"),
            new MLLabel("i", "<i>", "</i>"),
            new MLLabel("br", "<br/>", ""),
            new MLLabel("h1", "<h1>", "</h1>"),
            new MLLabel("h2", "<h2>", "</h2>"),
            new MLLabel("h3", "<h3>", "</h3>"),
            new MLLabel("a",  "<a href=\"\">","</a>")
    };

    public static MLLabel[] latexTagArr = new MLLabel[] {
            new MLLabel("\\(...\\)", "\\(", "\\)"),
            new MLLabel("\\[...\\]", "\\[", "\\]"),
            new MLLabel("()", "(", ")"),
            new MLLabel("{}", "{", "}"),
            new MLLabel("\\", "\\",""),
            new MLLabel("+", "+", ""),
            new MLLabel("!", "!", ""),
            new MLLabel("^", "^", ""),
            new MLLabel("x^2", "x^", ""),
            new MLLabel("2^x", "", "^x")
    };

    public static final String INTENT_ANKIHELPER_TARGET_WORD = "com.mmjang.ankihelper.target_word";
    public static final String INTENT_ANKIHELPER_TARGET_URL = "com.mmjang.ankihelper.url";
    public static final String INTENT_ANKIHELPER_NOTE_ID = "com.mmjang.ankihelper.note_id";
    public static final String INTENT_ANKIHELPER_UPDATE_ACTION = "com.mmjang.ankihelper.note_update_action";//replace;append;
    public static final String INTENT_ANKIHELPER_BASE64 = "com.mmjang.ankihelper.base64";
    public static final String INTENT_ANKIHELPER_PLAN_NAME = "com.mmjang.ankihelper.plan_name";
    public static final String INTENT_ANKIHELPER_FBREADER_BOOKMARK_ID = "com.mmjang.ankihelper.fbreader.bookmark.id";
    public static final String ANKI_PACKAGE_NAME = "com.ichi2.anki";
    public static final String LATEX_EDITOR_NAME = "com.viclab.ocr";
    public static final String ANKIHELPER_PACKAGE_NAME = "com.mmjang.ankihelper";
    public static final String FBREADER_URL_TMPL = "<a href=\"intent:#Intent;action=android.fbreader.action.VIEW;category=android.intent.category.DEFAULT;type=text/plain;component=org.geometerplus.zlibrary.ui.android/org.geometerplus.android.fbreader.FBReader;S.fbreader.bookmarkid.from.external=%s;end;\">查看原文</a>";
    static final public String INTENT_ANKIHELPER_NOTE = "com.mmjang.ankihelper.note";
    static final public String ASSIST_SERVICE_INFO_ID = "com.mmjang.ankihelper/.ui.floating.assist.AssistService";
//    public static final int VIBRATE_DURATION = 10;
//
//    public static final float FLOAT_ACTION_BUTTON_ALPHA = 0.3f;

    public static final String EXTERNAL_STORAGE_DIRECTORY = "ankihelper";
    public static final String EXTERNAL_STORAGE_CONTENT_SUBDIRECTORY = "content";
    public static final String EXTERNAL_STORAGE_TESSERACT_SUBDIRECTORY = "tesseract";
    public static final String EXTERNAL_STORAGE_TESSDATA_SUBDIRECTORY = "tessdata";
    public static final String LEFT_BOLD_SUBSTITUDE = "☾";
    public static final String RIGHT_BOLD_SUBSTITUDE = "☽";

    public static final String INTENT_ANKIHELPER_CONTENT_INDEX = "com.mmjang.ankihelper.content_index";
    public static final String INTENT_ANKIHELPER_MDICT_ORDER = "com.mmjang.ankihelper.mdict_order";
    public static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";

//    public static final String IMAGE_SUB_DIRECTORY = "ankihelper_image";
//    public static final String AUDIO_SUB_DIRECTORY = "ankihelper_audio";

    public static final String IMAGE_MEDIA_DIRECTORY = Environment.getExternalStorageDirectory()
            + "/AnkiDroid/collection.media/"; // /AnkiDroid/collection.media/ankihelper_image/";

    public static final String AUDIO_MEDIA_DIRECTORY = Environment.getExternalStorageDirectory()
            + "/AnkiDroid/collection.media/"; // /AnkiDroid/collection.media/ankihelper_audio/";

//    public static final String MDICT_DIRECTORY = Environment.getExternalStorageDirectory()
//            + "/eudb_en/en/";

    public static final String USE_CLIPBOARD_CONTENT_FLAG = "use_clipboard_content_flag";
    public static final String USE_FX_SERVICE_CB_FLAG = "use_fx_service_cb_flag";
//    public static final String FINISH_POPUP_WINDOW_FLAG = "finish_popup_window_flag";

    public static final String MP3_SUFFIX = ".mp3";
    public static final String MP4_SUFFIX = ".mp4";
    public static final long DEFAULT_MAX_SIZE = 512 * 1024 * 1024;
    public static final String TPL_HTML_VIDEO_TAG = "<video id=\"video\" width=\"100%%\" src=\"%s\" controlList=\"nodownload\" controls=\"controls\"></video>";
    public static final String TPL_HTML_AUDIO_TAG = "<audio id=\"audio\" src=\"%s\" controlList=\"nodownload\" controls=\"controls\"></audio>";
    public static final String TPL_HTML_NOTE_TAG = "<div class=\"note\">%s</div>";
    public static final String _TPL_HTML_NOTE_TAG_LOCATION_ = "_tpl_html_note_tag_location_";
    public static final String _TPL_HTML_MEDIA_TAG_LOCATION_ = "_tpl_html_media_tag_location_";
    public static final String TPL_HTML_IMG_TAG = "<img src=\"%s\" />";
    public static final String TPL_COMPLEX_DICT_WORD_TAG =
            "<div class=\"complex\">" +
                    "<div class=\"source\"><font color = \"#ba400d\">%s</font> %s</div>" +
            "<div class=\"word_or_phrase_content\">"+
            "<div class=\"word_or_phrase\">%s</div>" +
            "<div class=\"phonetics\">%s</div>" +
            "<div class=\"sense\">%s</div>" +
            "<div class=\"defintion\">%s</div></div>" +
                    _TPL_HTML_NOTE_TAG_LOCATION_ + _TPL_HTML_MEDIA_TAG_LOCATION_ + "</div>";
    public static final String TPL_COMPLEX_DICT_SENTENCE_TAG =
            "<div class=\"complex\">" +
            "<div class=\"source\"><font color = \"#ba400d\">%s</font> %s</div>" +
            "<div class=\"sentence_content\"><div class=\"original_text\">%s</div>" +
            "<div class=\"translation\">%s</div></div>" +
                    _TPL_HTML_NOTE_TAG_LOCATION_ + _TPL_HTML_MEDIA_TAG_LOCATION_ + "</div>";
    public static final String TPL_COMPLEX_DICT_CLOZE_TAG =
            "<div class=\"complex\">" +
                    "<div class=\"source\"><font color = \"#ba400d\">%s</font> %s</div>" +
                    _TPL_HTML_NOTE_TAG_LOCATION_ + _TPL_HTML_MEDIA_TAG_LOCATION_ + "</div>";

    public static final String AUDIO_INDICATOR_MP3 = "<span class=\"indicator\"><font color=\"#227D51\">mp3</font></span>";
    public static final String VIDEO_INDICATOR_MP4 = "<span class=\"indicator\"><font color=\"#227D51\">mp4</font></span>";

    public static final int REQUEST_MEDIA_PROJECTION = 1;

    public final static String[] SUPPORTEDLANGUAGES = {"zho-CHN", "zho-HKG", "zho-TWN", "jpn-JPN", "kor-KOR", "ara-EGY", "ara-SAU", "bul-BGR", "cat-ESP", "ces-CZE", "cym-GBR", "dan-DNK", "deu-AUT", "deu-CHE", "deu-DEU", "ell-GRC", "eng-AUS", "eng-CAN", "eng-HKG", "eng-IRL", "eng-IND", "eng-NZL", "eng-PHL", "eng-SGP", "eng-USA", "eng-ZAF", "spa-ARG", "spa-COL", "spa-ESP", "spa-MEX", "spa-USA", "est-EST", "fin-FIN", "fra-BEL", "fra-CAN", "fra-CHE", "fra-FRA", "gle-IRL", "guj-IND", "heb-ISR", "hin-IND", "hrv-HRV", "hun-HUN", "ind-IDN", "ita-ITA", "lit-LTU", "lav-LVA", "mar-IND", "msa-MYS", "mlt-MLT", "nob-NOR", "nld-BEL", "nld-NLD", "pol-POL", "por-BRA", "por-PRT", "ron-ROU", "rus-RUS", "slk-SVK", "slv-SVN", "swe-SWE", "swa-KEN", "tam-IND", "tel-IND", "tha-THA", "tur-TUR", "ukr-UKR", "urd-PAK", "vie-VNM"};

    public static String INTENT_ANKIHELPER_ACTION = "intent_ankihelper_action";

    //mathpix
    public static String MATHPIX_BASE_URL = "https://api.mathpix.com/v3/text";
}
