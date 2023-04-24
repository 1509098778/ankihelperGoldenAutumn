
package com.mmjang.ankihelper.ui.popup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ichi2.anki.api.NoteInfo;
import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.data.database.ExternalDatabase;
import com.mmjang.ankihelper.data.dict.BingOxford;
import com.mmjang.ankihelper.data.dict.Collins;
import com.mmjang.ankihelper.data.dict.CollinsEnEn;
import com.mmjang.ankihelper.data.dict.CustomDictionary;
import com.mmjang.ankihelper.data.dict.Definition;
import com.mmjang.ankihelper.data.dict.DictLanguageType;
import com.mmjang.ankihelper.data.dict.DictTango;
import com.mmjang.ankihelper.data.dict.DictionaryDotCom;
import com.mmjang.ankihelper.data.dict.DictionaryRegister;
import com.mmjang.ankihelper.data.dict.Dub91Sentence;
import com.mmjang.ankihelper.data.dict.EnglishSentenceSet;
import com.mmjang.ankihelper.data.dict.EudicSentence;
import com.mmjang.ankihelper.data.dict.Getyarn;
import com.mmjang.ankihelper.data.dict.Handian;
import com.mmjang.ankihelper.data.dict.IDictionary;
import com.mmjang.ankihelper.data.dict.IdiomDict;
import com.mmjang.ankihelper.data.dict.Mdict;
import com.mmjang.ankihelper.data.dict.Mnemonic;
import com.mmjang.ankihelper.data.dict.Ode2;
import com.mmjang.ankihelper.data.dict.RenRenCiDianSentence;
import com.mmjang.ankihelper.data.dict.UrbanAutoCompleteAdapter;
import com.mmjang.ankihelper.data.dict.VocabCom;
import com.mmjang.ankihelper.data.dict.WebsterLearners;
import com.mmjang.ankihelper.data.history.HistoryUtil;
import com.mmjang.ankihelper.data.model.UserTag;
import com.mmjang.ankihelper.data.plan.OutputPlanPOJO;
import com.mmjang.ankihelper.domain.CBWatcherService;
import com.mmjang.ankihelper.domain.FinishActivityManager;
import com.mmjang.ankihelper.domain.PlayAudioManager;
import com.mmjang.ankihelper.domain.PronounceManager;
import com.mmjang.ankihelper.ui.plan.ComplexElement;
import com.mmjang.ankihelper.ui.share.SharePopupWindow;
import com.mmjang.ankihelper.ui.tango.OutputLocatorPOJO;
import com.mmjang.ankihelper.ui.translation.TranslateBuilder;
import com.mmjang.ankihelper.ui.widget.BigBangLayout;
import com.mmjang.ankihelper.ui.widget.BigBangLayoutWrapper;
import com.mmjang.ankihelper.ui.widget.MathxView;
import com.mmjang.ankihelper.ui.widget.button.MLLabel;
import com.mmjang.ankihelper.ui.widget.button.MLLabelButton;
import com.mmjang.ankihelper.ui.widget.NoteEditText;
import com.mmjang.ankihelper.ui.widget.button.PerformEditButton;
import com.mmjang.ankihelper.util.BuildConfig;
import com.mmjang.ankihelper.util.ClipboardUtils;
import com.mmjang.ankihelper.util.ColorThemeUtils;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.CrashManager;
import com.mmjang.ankihelper.util.DialogUtil;
import com.mmjang.ankihelper.util.FieldUtil;
import com.mmjang.ankihelper.util.PerformEdit;
import com.mmjang.ankihelper.util.PunctuationUtil;
import com.mmjang.ankihelper.util.RegexUtil;
import com.mmjang.ankihelper.util.ScreenUtils;
import com.mmjang.ankihelper.util.StringUtil;
import com.mmjang.ankihelper.util.SystemUtils;
import com.mmjang.ankihelper.util.TextSplitter;
import com.mmjang.ankihelper.util.ToastUtil;
import com.mmjang.ankihelper.util.Trace;
import com.mmjang.ankihelper.util.Utils;
import com.mmjang.ankihelper.util.StorageUtils;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.litepal.crud.DataSupport;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.mmjang.ankihelper.util.FieldUtil.getBlankSentence;
import static com.mmjang.ankihelper.util.FieldUtil.getBoldSentence;
import static com.mmjang.ankihelper.util.FieldUtil.getNormalSentence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class PopupActivity extends AppCompatActivity implements BigBangLayoutWrapper.ActionListener, TextToSpeech.OnInitListener {
    List<IDictionary> dictionaryList;
    IDictionary currentDictionary;
    List<OutputPlanPOJO> outputPlanList;
    OutputPlanPOJO currentOutputPlan;
    Settings settings;
    String mTextToProcess;
    String mPlanNameFromIntent;
    String mCurrentKeyWord;
    String mNoteEditedByUser = "";
    Set<String> mTagEditedByUser = new HashSet<>();
    //posiblle pre set target word
    String mTargetWord;
    //possible url from dedicated borwser
    String mUrl = "";
    //possible specific note id to update
    Long mUpdateNoteId = 0L;
    //!!!!!!!!!!!important!!! boolean, if the plan spinner is during init, forbid asyncsearch;
    boolean isDuringPlanSpinnerInit = false;
    //update action   replace/append    append is the default action, to prevent data loss;
    String mUpdateAction;
    //possible bookmark id from fbreader
    String mFbReaderBookmarkId;
    //translation
    String mTranslatedResult = "";
    boolean needTranslation = false;
    boolean isRefreshedBigBang  = false;
    int preTranslationEngineIndex = 0;
    //views
    AutoCompleteTextView acTextView;
    Button btnSearch;
    ImageButton btnSetTangoDict;
    ImageButton btnPronounce;
    Spinner planSpinner;
    Spinner pronounceLanguageSpinner;
    private int pickedSentencePronouceLanguageIndex;
    private AnkiDroidHelper mAnkiDroid;
    private Map<Long, String> deckList;
    private long currentDeckId;
    Spinner deckSpinner;

    //RecyclerView recyclerViewDefinitionList;
    CardView mCvPopupToolbar;
    ImageButton mBtnEditNote;
    ImageButton mBtnEditTag;
    ImageButton mBtnTranslation;
    ImageButton mBtnFooterRotateLeft;
    ImageButton mBtnFooterRotateRight;
    ImageButton mBtnFooterScrollTop;
    ImageButton mBtnFooterScrollBottom;
    ImageButton mBtnFooterShare;
    ImageButton mBtnFooterSearch;
    ImageButton mBtnFooterCopy;
    ProgressBar progressBar;
    ProgressBar mMediaProgress;

    CardView mCardViewTranslation;
    EditText mEditTextTranslation;

    //SurfaceView
    SurfaceView mSurfaceView;
    SurfaceHolder surfaceHolder;

    //fab
    //FloatingActionButton mFab;
    ScrollView scrollView;
    //plan b
    LinearLayout viewDefinitionList;
    List<Definition> mDefinitionList;

    //media
//    MediaPlayer mAudioMediaPlayer;
    MediaPlayer mVideoMediaPlayer;

    //tts
    TextToSpeech mTts;
    int mTtsStatus;
    boolean toLoadTTS = false;

    //save thread running.
    Thread mPreThread;

    //cacheAudio
    HttpProxyCacheServer mProxy;

    //downloader
    Fetch fetch;
    FetchListener fetchListener;
    boolean isFetchDownloading = false;
    //async event
    private static final int PROCESS_DEFINITION_LIST = 1;
    private static final int ASYNC_SEARCH_FAILED = 2;
    private static final int TRANSLATION_DONE = 3;
    private static final int TRANSLATIOn_FAILED = 4;
    //view tag
    private static final int TAG_NOTE_ID_LONG = 5;
    //async
    @SuppressLint("HandlerLeak")
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESS_DEFINITION_LIST:
                    showSearchButton();
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    mDefinitionList = (List<Definition>) msg.obj;
                    if(!PopupActivity.this.isDestroyed()) {
                        processDefinitionList(mDefinitionList);
                    }
                    break;
                case ASYNC_SEARCH_FAILED:
                    showSearchButton();
                    Toast.makeText(PopupActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case TRANSLATION_DONE:
                    String result = (String) msg.obj;
                    String[] splitted = result.split("\n");
                    if (splitted.length > 0 && splitted[0].equals("error")) {
                        Toast.makeText(PopupActivity.this, result, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    mEditTextTranslation.setText((result));
                    showTranslateDone();
                    showTranslationCardView(true);
                    break;
                default:
                    showTranslateNormal();
                    Toast.makeText(PopupActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private BigBangLayout bigBangLayout;
    private BigBangLayoutWrapper bigBangLayoutWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ColorThemeUtils.setColorTheme(PopupActivity.this, Constant.StyleBaseTheme.Transparent);
        super.onCreate(savedInstanceState);
        settings = Settings.getInstance(this);
        FinishActivityManager.getManager().addActivity(this);
        //初始化 错误日志系统
        CrashManager.getInstance(this);
        startAnkiDroid();
        setStatusBarColor();
        setContentView(R.layout.activity_popup);
        initAnkiApi();
//        getActionBar().hide();
        //set animation
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        //
        assignViews();
        initBigBangLayout();
        loadData(); //dictionaryList;
        populatePlanSpinner();
        setEventListener();
        initVisible();
        initFetch();

        if (settings.getMoniteClipboardQ()) {
            startCBService();
        }

//        handleIntent();
        handleIntent();
        getIntent().putExtra(Constant.INTENT_ANKIHELPER_ACTION, false);

        //async invoke droid
        asyncInvokeDroid();

        //tts init
        //初始化语音。这是一个异步操作。初始化完成后调用oninitListener(第二个参数)。
        if(mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        mTts = new TextToSpeech(getApplicationContext(), this);
        //cache
        mProxy = new HttpProxyCacheServer.Builder(PopupActivity.this).maxCacheSize(Constant.DEFAULT_MAX_SIZE).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getBooleanExtra(Constant.INTENT_ANKIHELPER_ACTION, false)) {
            handleIntent();
            getIntent().putExtra(Constant.INTENT_ANKIHELPER_ACTION, false);

            processTextFromFxService();
        }
    }

    private void setTransparent(float alpha) {
        Window window=getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha=alpha;//这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
    }

    private boolean isTransparent() {
        Window window=getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        if(wl.alpha > 0.0f) {
            return false;
        }
        return true;
    }

    private void asyncInvokeDroid() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyApplication.getAnkiDroid().getApi().getDeckList();
                        } catch (Exception e) {
                        }
                    }
                }
        ).start();
    }

    //ed

    private void setTargetWord() {
        setTargetWord(true);
    }
    private void setTargetWord(boolean isAutomaticSearch) {

//        String word = "";
//        if (!TextUtils.isEmpty(mTargetWord)) {
//            word = mTargetWord;
//        } else {
//            word = mTextToProcess;
//        }
//
//        for (BigBangLayout.Line line : bigBangLayout.getLines()) {
//            List<BigBangLayout.Item> items = line.getItems();
//            for (BigBangLayout.Item item : items) {
//                if (item.getText().equals(word)) {
//                    item.setSelected(true);
//                }
//            }
//        }
//        acTextView.setText(word);
//        asyncSearch(word);

        if (!TextUtils.isEmpty(mTargetWord)) {
            for (BigBangLayout.Line line : bigBangLayout.getLines()) {
                List<BigBangLayout.Item> items = line.getItems();
                for (BigBangLayout.Item item : items) {
                    if (item.getText().equals(mTargetWord)) {
                        item.setSelected(true);
                    }
                }
            }
            acTextView.setText(mTargetWord);
            if(isAutomaticSearch)
                asyncSearch(mTargetWord);
//        } else if (mTextToProcess.matches("[a-zA-Z\\-]*") || mTextToProcess.length() == 1) {
        } else if (RegexUtil.isEnglish(mTextToProcess) ||
                RegexUtil.isRussian(mTextToProcess) ||
                RegexUtil.isSpecialWord(mTextToProcess) ||
                mTextToProcess.length() == 1) {
            //bug
            for (BigBangLayout.Line line : bigBangLayout.getLines()) {
                List<BigBangLayout.Item> items = line.getItems();
                for (BigBangLayout.Item item : items) {
                    if (item.getText().equals(mTextToProcess)) {
                        item.setSelected(true);
                    }
                }
            }
            acTextView.setText(mTextToProcess);
            if(isAutomaticSearch)
                asyncSearch(mTextToProcess);
        }
    }

    private void setStatusBarColor() {
        int statusBarColor = 0;
        if (Build.VERSION.SDK_INT >= 21) {
            statusBarColor = getWindow().getStatusBarColor();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(statusBarColor);
        }
    }

    private void assignViews() {
        acTextView = (AutoCompleteTextView) findViewById(R.id.edit_text_hwd);
        btnSearch = (Button) findViewById(settings.getLeftHandModeQ() ? R.id.btn_search_left : R.id.btn_search);
        progressBar = (ProgressBar) findViewById(settings.getLeftHandModeQ() ? R.id.progress_bar_left : R.id.progress_bar);
        btnSetTangoDict = (ImageButton) findViewById(R.id.btn_set_tango_dict);
        btnPronounce = ((ImageButton) findViewById(R.id.btn_pronounce));
        planSpinner = (Spinner) findViewById(R.id.plan_spinner);
        deckSpinner = (Spinner) findViewById(R.id.deck_spinner);
        pronounceLanguageSpinner = (Spinner) findViewById(R.id.language_spinner);
        //recyclerViewDefinitionList = (RecyclerView) findViewById(R.id.recycler_view_definition_list);
        viewDefinitionList = (LinearLayout) findViewById(R.id.view_definition_list);
        mCvPopupToolbar = (CardView) findViewById(R.id.cv_popup_toolbar);
        mBtnEditNote = (ImageButton) findViewById(R.id.footer_note);
        mBtnEditTag = (ImageButton) findViewById(R.id.footer_tag);
        bigBangLayout = (BigBangLayout) findViewById(R.id.ocr_mlkit);
        bigBangLayoutWrapper = (BigBangLayoutWrapper) findViewById(R.id.bigbang_wrapper);
        mCardViewTranslation = (CardView) findViewById(R.id.cardview_translation);
        mBtnTranslation = (ImageButton) findViewById(R.id.footer_translate);
        mEditTextTranslation = (EditText) findViewById(R.id.edittext_translation);
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_surface);
        //mFab = (FloatingActionButton) findViewById(R.id.fab);
        mBtnFooterRotateLeft = (ImageButton) findViewById(R.id.footer_rotate_left);
        mBtnFooterRotateRight = (ImageButton) findViewById(R.id.footer_rotate_right);
        mBtnFooterScrollTop = (ImageButton) findViewById(R.id.footer_scroll_top);
        mBtnFooterScrollBottom = (ImageButton) findViewById(R.id.footer_scroll_bottom);
        mBtnFooterShare = (ImageButton) findViewById(R.id.footer_share);
        mBtnFooterSearch = (ImageButton) findViewById(R.id.footer_search);
        mBtnFooterCopy = (ImageButton) findViewById(R.id.footer_copy);
        mMediaProgress = findViewById(R.id.audio_progress);

    }

    private void initVisible() {
        int visible;

        if(settings.get(Settings.POPUP_SWITCH_SCROLL_BOTTOM, false))
            visible = View.VISIBLE;
        else
            visible = View.GONE;
        mBtnFooterScrollBottom.setVisibility(visible);

        if(settings.getPopupToolbarSearchEnable())
            visible = View.VISIBLE;
        else
            visible = View.GONE;
        mBtnFooterSearch.setVisibility(visible);

        if(settings.getPopupToolbarNoteEnable())
            visible = View.VISIBLE;
        else
            visible = View.GONE;
        mBtnEditNote.setVisibility(visible);

        if(settings.getPopupToolbarTagEnable())
            visible = View.VISIBLE;
        else
            visible = View.GONE;
        mBtnEditTag.setVisibility(visible);

        if(settings.get(Settings.POPUP_SWITCH_COPY, false))
            visible = View.VISIBLE;
        else
            visible = View.GONE;
        mBtnFooterCopy.setVisibility(visible);

        if(settings.getLeftHandModeQ())
            btnSearch.setVisibility(View.VISIBLE);


    }
    private void loadData() {
        dictionaryList = DictionaryRegister.getDictionaryObjectList();
        outputPlanList = ExternalDatabase.getInstance().getAllPlan();

        //设置一些控件
        deckSpinner.setEnabled(settings.getPopupSpinnerDeckEnable());
//        已被Mdict.class已调用
//        List<OutputLocatorPOJO> newList = OutputLocatorPOJO.readOutputLocatorPOJOListFromSettings();

        //load tag
        boolean loadQ = settings.getSetAsDefaultTag();
        if (loadQ) {
            mTagEditedByUser = Utils.fromStringToTagSet(settings.getDefaulTag());
        }
        //check if outputPlanList is empty
        if (outputPlanList.size() == 0) {
            //Toast.makeText(this, , Toast.LENGTH_LONG).show();
            Utils.showMessage(this, getResources().getString(R.string.toast_no_available_plan));
        }
    }

    private void populatePlanSpinner() {
        if (outputPlanList.size() == 0) {
            return;
        }
        final String[] planNameArr = new String[outputPlanList.size()];
        for (int i = 0; i < outputPlanList.size(); i++) {
            planNameArr[i] = outputPlanList.get(i).getPlanName();
        }
        ArrayAdapter<String> planSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, planNameArr);
        planSpinner.setAdapter(planSpinnerAdapter);
        planSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set plan to last selected plan
        String lastSelectedPlan = settings.getLastSelectedPlan();
        if (lastSelectedPlan.equals("")) //first use, set default plan to first one if any
        {
            if (outputPlanList.size() > 0) {
                settings.setLastSelectedPlan(outputPlanList.get(0).getPlanName());
                currentOutputPlan = outputPlanList.get(0);
                currentDictionary = getDictionaryFromOutputPlan(currentOutputPlan);
                if (currentDictionary == null) {
                    String message = String.format("方案\"%s\"所选词典\"%s\"不存在，请检查是否需要重新导入自定义词典",
                            currentOutputPlan.getPlanName(),
                            currentOutputPlan.getDictionaryKey());
                    Utils.showMessage(PopupActivity.this, message);
                } else {
                    setActAdapter(currentDictionary);
                }
            } else {
                return;
            }
        }

        ///////////////if user add intent parameter to control which plan to use
        mPlanNameFromIntent = getIntent().getStringExtra(Constant.INTENT_ANKIHELPER_PLAN_NAME);
        if (mPlanNameFromIntent != null && !mPlanNameFromIntent.equals("")) {
            lastSelectedPlan = mPlanNameFromIntent;
        }
        ///////////////
        int i = 0;
        boolean find = false;
        for (OutputPlanPOJO plan : outputPlanList) {
            if (plan.getPlanName().equals(lastSelectedPlan)) {
                isDuringPlanSpinnerInit = true;
                planSpinner.setSelection(i);
                currentOutputPlan = outputPlanList.get(i);
                currentDictionary = getDictionaryFromOutputPlan(currentOutputPlan);
                if (currentDictionary == null) {
                    String message = String.format("方案\"%s\"所选词典\"%s\"不存在，请检查是否需要重新导入自定义词典",
                            currentOutputPlan.getPlanName(),
                            currentOutputPlan.getDictionaryKey());
                    Utils.showMessage(PopupActivity.this, message);
                    break;
                }
                setActAdapter(currentDictionary);
                find = true;
                break;
            }
            //if not equal, compare next
            i++;
        }
        if (!find) //if the saved last plan no longer in the plan list, reset to first one
        {
            if (outputPlanList.size() > 0) {
                settings.setLastSelectedPlan(outputPlanList.get(0).getPlanName());
                currentOutputPlan = outputPlanList.get(0);
                currentDictionary = getDictionaryFromOutputPlan(currentOutputPlan);
                if (currentDictionary == null) {
                    String message = String.format("方案\"%s\"所选词典\"%s\"不存在，请检查是否需要重新导入自定义词典",
                            currentOutputPlan.getPlanName(),
                            currentOutputPlan.getDictionaryKey());
                    Utils.showMessage(PopupActivity.this, message);
                }
                setActAdapter(currentDictionary);
            }
        } else {
            //if find, then current plan and dictionary must have been set above.
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            scrollView.setOnScrollChangeListener(
//                    new View.OnScrollChangeListener() {
//                        @Override
//                        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                            if(i1 > i3){
//                                mFab.hide();
//                            }else{
//                                mFab.show();
//                                //mFab.setAlpha(Constant.FLOAT_ACTION_BUTTON_ALPHA);
//                            }
//                        }
//                    }
//            );
//        }
    }

    private void populateLanguageSpinner() {
        String[] sharedExportElements = Constant.getSharedExportElements();
        for (String exportedFieldKey : currentOutputPlan.getFieldsMap().values()) {
            ComplexElement ce = new ComplexElement(exportedFieldKey);
            if(mUpdateNoteId > 0 && mUpdateAction == null)
                exportedFieldKey = ce.getElementAppending();
            else
                exportedFieldKey = ce.getElementNormal();
            if (exportedFieldKey.equals(sharedExportElements[12]) ||
                    exportedFieldKey.equals(sharedExportElements[13]) ||
                    exportedFieldKey.equals(sharedExportElements[14]) ||
                    exportedFieldKey.equals(sharedExportElements[17]) ||
                    exportedFieldKey.equals(sharedExportElements[18]) ||
                    exportedFieldKey.equals(Constant.DICT_FIELD_COMPLEX_OFFLINE)) {
                if (mTtsStatus == TextToSpeech.SUCCESS) {
                    toLoadTTS = true;
                } else {
                    toLoadTTS = false;
                }
                break;
            } else {
                toLoadTTS = false;
            }
        }

        if(currentDictionary instanceof DictTango) {
            btnSetTangoDict.setVisibility(View.VISIBLE);
            HashMap<String, Boolean> tangoDictCheckerMap;
//            Trace.e("checkMap", String.valueOf(settings.getMdictChckerMap(currentOutputPlan.getPlanName()).size()));
            if(settings.getTangoDictChckerMap(currentOutputPlan.getPlanName()).isEmpty()) {
                tangoDictCheckerMap = new HashMap<>();
                for(OutputLocatorPOJO locator : OutputLocatorPOJO.readOutputLocatorPOJOListFromSettings()) {
//                    tangoDictCheckerMap.put(locator.getDictName(), locator.isChecked());
                    tangoDictCheckerMap.put(locator.getDictName(), false);
                }
                settings.setTangoDictCheckerMap(currentOutputPlan.getPlanName(), tangoDictCheckerMap);
            } else {
                tangoDictCheckerMap = settings.getTangoDictChckerMap(currentOutputPlan.getPlanName());
                List<OutputLocatorPOJO> locatorList = new ArrayList<>();
                OutputLocatorPOJO.readOutputLocatorPOJOListFromSettings().stream().map(a->a.isChecked()?locatorList.add(a):null).collect(Collectors.toList());
                for(OutputLocatorPOJO locator : locatorList) {
                    if(tangoDictCheckerMap.containsKey(locator.getDictName())) {
                        if(locator.isChecked())
                            locator.setChecked(tangoDictCheckerMap.get(locator.getDictName()).booleanValue());
                    } else {
                        tangoDictCheckerMap.put(locator.getDictName(), false);
                        settings.setTangoDictCheckerMap(currentOutputPlan.getPlanName(), tangoDictCheckerMap);
                    }
                }
            }

        } else {
            btnSetTangoDict.setVisibility(View.GONE);
        }
        String[] languages = PronounceManager.getAvailablePronounceLanguage(currentDictionary, toLoadTTS);
        ArrayAdapter<String> languagesSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, languages);
        languagesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pronounceLanguageSpinner.setAdapter(languagesSpinnerAdapter);

        int type = getTypeCurrentDictionary();
        int index = settings.getRestorePronounceSpinnerIndex(type, currentDictionary.isExistAudioUrl(), toLoadTTS);
        if(index == -1) {
            index = toLoadTTS && currentDictionary.isExistAudioUrl() ? languages.length - 1 : PronounceManager.getSoundInforIndexByList(type);
        }
        pronounceLanguageSpinner.setSelection(index);
        pickedSentencePronouceLanguageIndex = index;
    }

    //
    private void initAnkiApi() {
        if (mAnkiDroid == null) {
            mAnkiDroid = new AnkiDroidHelper(this);
        }
        if (!AnkiDroidHelper.isApiAvailable(MyApplication.getContext())) {
            Toast.makeText(this, R.string.api_not_available_message, Toast.LENGTH_LONG).show();
        }

        if (mAnkiDroid.shouldRequestPermission()) {
            mAnkiDroid.requestPermission(this, 0);
        }
    }
    //
    private void loadDecksAndModels() {
        deckList = Utils.hashMap2LinkedHashMap(mAnkiDroid.getApi().getDeckList());
    }

    //
    private void populateDecks() {
        ArrayAdapter<String> deckSpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, Utils.getMapValueArray(deckList));
        deckSpinner.setAdapter(deckSpinnerAdapter);
        if (currentOutputPlan != null) {
            long savedDeckId;
            if(deckSpinner.isEnabled() && settings.getPopupIgnoreDeckScheme() && settings.getLastDeckId() != -1)
                savedDeckId = settings.getLastDeckId();
            else
                savedDeckId = currentOutputPlan.getOutputDeckId();
            //int i = 0;
            long[] deckIdList = Utils.getMapKeyArray(deckList);
            //int deckPos = Arrays.asList(deckIdList).indexOf(savedDeckId);
            int deckPos = Utils.getArrayIndex(deckIdList, savedDeckId);
            if (deckPos == -1) {
                deckPos = 0;
            }

            currentDeckId = deckIdList[deckPos];
            deckSpinner.setSelection(deckPos);
        } else {
            currentDeckId = Utils.getMapKeyArray(deckList)[0];
        }

        deckSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentDeckId = Utils.getMapKeyArray(deckList)[position];
                        settings.setLastDeckId(currentDeckId);
                        currentOutputPlan.setOutputDeckId(currentDeckId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

    }

    private void initBigBangLayout() {
        bigBangLayout.setShowSymbol(true);
        bigBangLayout.setShowSpace(true);
        bigBangLayout.setShowSection(true);
        bigBangLayout.setItemSpace(0);
        bigBangLayout.setLineSpace(0);
        bigBangLayout.setTextPadding(5);
        bigBangLayout.setTextPaddingPort(5);
        bigBangLayoutWrapper.setStickHeader(true);
        bigBangLayoutWrapper.setActionListener(this);

    }

    private void setEventListener() {

        //auto finish
        Button btnCancelBlank = (Button) findViewById(R.id.btn_cancel_blank);
        Button btnCancelBlankAboveCard = (Button) findViewById(R.id.btn_cancel_blank_above_card);
        btnCancelBlank.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        btnCancelBlankAboveCard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        planSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //销毁surfaceView
                        mSurfaceView.setVisibility(View.GONE);
                        LinearLayout preLy = (LinearLayout) mSurfaceView.getParent();
                        if (preLy != null)
                            preLy.removeView(mSurfaceView);

                        currentOutputPlan = outputPlanList.get(position);
                        currentDictionary = getDictionaryFromOutputPlan(currentOutputPlan);
                        setActAdapter(currentDictionary);
                        //memorise last selected plan
                        settings.setLastSelectedPlan(currentOutputPlan.getPlanName());
                        populateLanguageSpinner();
                        loadDecksAndModels();
                        populateDecks();

                        String actContent = acTextView.getText().toString();
                        if (isDuringPlanSpinnerInit) {
                            isDuringPlanSpinnerInit = false;
                        } else {
                            if (!actContent.trim().isEmpty()) {
                                if(bigBangLayoutWrapper.getVisibility() == View.VISIBLE)
                                    asyncSearch(actContent);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        pronounceLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int type = getTypeCurrentDictionary();
                settings.setLastPronounceLanguage(position);
//                settings.setRestorePronounceSpinnerIndex(position, type, currentDictionary.isExistAudioUrl(), toLoadTTS);
                settings.setRestorePronounceSpinnerIndex(position, type, currentDictionary.isExistAudioUrl(), toLoadTTS);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pronounceLanguageSpinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int checkedIndex = pickedSentencePronouceLanguageIndex;
                String[] languages = PronounceManager.getAvailablePronounceLanguage(currentDictionary, toLoadTTS);
                boolean[] isCheckedArr = new boolean[languages.length];

                for(int i = 0; i < isCheckedArr.length; i++) {
                    if(i == checkedIndex)
                        isCheckedArr[i] = true;
                    else
                        isCheckedArr[i] = false;
                }

                androidx.appcompat.app.AlertDialog.Builder multiChoiceDialog = new androidx.appcompat.app.AlertDialog.Builder(PopupActivity.this);
                multiChoiceDialog
                        .setTitle(R.string.tv_choose_language_pronounce_picked_sentence);
//                                .setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
//
//                                });

                multiChoiceDialog.setSingleChoiceItems(languages, checkedIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pickedSentencePronouceLanguageIndex = which;
                            }
                        });
                multiChoiceDialog.show();

                return false;
            }
        });

        btnSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String word = acTextView.getText().toString();
                        if (!word.isEmpty()) {
                            if(mTextToProcess.equals("") || isRefreshedBigBang) {
                                bigBangLayoutWrapper.setVisibility(View.VISIBLE);
                                mTextToProcess = word;
                                populateWordSelectBox();
                                HistoryUtil.savePopupOpen(mTextToProcess);

                                bigBangLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setTargetWord(false);
                                    }
                                });
                            }
                            asyncSearch(word);
                            Utils.hideSoftKeyboard(PopupActivity.this);
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                }
        );

        btnSetTangoDict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    List<OutputLocatorPOJO> locatorList = new ArrayList<>();
                    OutputLocatorPOJO.readOutputLocatorPOJOListFromSettings().stream().map(a->a.isChecked()?locatorList.add(a):null).collect(Collectors.toList());
                    HashMap<String, Boolean> mdictCheckerMap = settings.getTangoDictChckerMap(currentOutputPlan.getPlanName());
                    AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(PopupActivity.this);
                    multiChoiceDialog.setTitle("选择词典");

                    String[] dictNameArr = new String[locatorList.size()];
                    boolean[] isCheckedArr = new boolean[locatorList.size()];

                    for(int i=0; i<locatorList.size(); i++) {
                        dictNameArr[i] = String.format("%s %s", locatorList.get(i).getlangName(), locatorList.get(i).getDictName());
                        boolean isChecked = mdictCheckerMap.get(locatorList.get(i).getDictName()).booleanValue();
                        locatorList.get(i).setChecked(isChecked);
                        isCheckedArr[i] = isChecked;
                    }

                    multiChoiceDialog.setMultiChoiceItems(
                            dictNameArr,
                            isCheckedArr,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    OutputLocatorPOJO locator = locatorList.get(which);
                                    locator.setChecked(isChecked);

                                    mdictCheckerMap.replace(locator.getDictName(), Boolean.valueOf(isChecked));
                                    settings.setTangoDictCheckerMap(currentOutputPlan.getPlanName(), mdictCheckerMap);
                                    populateLanguageSpinner();
                                }
                            });
                    multiChoiceDialog.show();
            }
        });

        btnPronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String word = acTextView.getText().toString();
                int lastPronounceLanguageIndex = Settings.getInstance(PopupActivity.this).getLastPronounceLanguage();
                PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(lastPronounceLanguageIndex);

                StopPlayingAll();
                if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_TTS)
                    playTts(word);
                else {
                    String finalAudioUrl = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, word, currentDictionary.getAudioUrl());
                    String cacheAudioUrl = getCacheAudioUrl(finalAudioUrl);
                    PlayAudioManager.playPronounceSound(PopupActivity.this, cacheAudioUrl);
                    Trace.i("speaks p", word);
                }
            }
        });

        btnPronounce.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String text = mTextToProcess;
                int lastPronounceLanguageIndex = pickedSentencePronouceLanguageIndex;
                PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(lastPronounceLanguageIndex);

                StopPlayingAll();
                if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_TTS)
                    playTts(text, lastPronounceLanguageIndex);
                else {
                    String finalAudioUrl = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, text, currentDictionary.getAudioUrl(), pickedSentencePronouceLanguageIndex);
                    String cacheAudioUrl = getCacheAudioUrl(finalAudioUrl);
                    PlayAudioManager.playPronounceSound(PopupActivity.this, cacheAudioUrl);

                }
                return true;
            }
        });

        mBtnEditNote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupEditNoteDialog();
                    }
                }
        );

        mBtnEditTag.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupEditTagDialog();
                    }
                }
        );

        acTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Trace.d("autocomplete", i + "");
                        acTextView.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        btnSearch.callOnClick();
                                    }
                                }
                        );
                    }
                }
        );

        acTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePronounceToSpinner(acTextView.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mBtnTranslation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEditTextTranslation.setVisibility(View.VISIBLE);
                        int index = settings.getTranslatorCheckedIndex();
                        if (mEditTextTranslation.getText().toString().equals("") ||
                                preTranslationEngineIndex != index) {
                            preTranslationEngineIndex = index;
                            asyncTranslate(mTextToProcess);
                        }
                    }
                }
        );

        mBtnTranslation.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        DialogUtil.selectTranslatorSettingDialog(PopupActivity.this);
                        return true;
                    }
                }
        );
        mBtnFooterRotateRight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mPlanSize = outputPlanList.size();
                        int currentPos = planSpinner.getSelectedItemPosition();
                        if (mPlanSize > 1) {
                            if (currentPos < mPlanSize - 1) {
                                planSpinner.setSelection(currentPos + 1);
                            } else if (currentPos == mPlanSize - 1) {
                                planSpinner.setSelection(0);
                            }
                            //vibarate(Constant.VIBRATE_DURATION);
                            //scrollView.fullScroll(ScrollView.FOCUS_UP);
                        } else {
                            Toast.makeText(PopupActivity.this, R.string.str_only_one_plan_cant_switch, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        mBtnFooterRotateLeft.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mPlanSize = outputPlanList.size();
                        int currentPos = planSpinner.getSelectedItemPosition();
                        if (mPlanSize > 1) {
                            if (currentPos > 0) {
                                planSpinner.setSelection(currentPos - 1);
                            } else if (currentPos == 0) {
                                planSpinner.setSelection(mPlanSize - 1);
                            }
                            //    vibarate(Constant.VIBRATE_DURATION);
                            //scrollView.fullScroll(ScrollView.FOCUS_UP);
                        } else {
                            Toast.makeText(PopupActivity.this, R.string.str_only_one_plan_cant_switch, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        mBtnFooterSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String word = acTextView.getText().toString();
                        if (!word.isEmpty()) {
                            asyncSearch(word);
                            Utils.hideSoftKeyboard(PopupActivity.this);
//                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }
                }
        );

        mBtnFooterCopy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("plans string", mTextToProcess);
                            clipboard.setPrimaryClip(clip);
                            ToastUtil.show("复制成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show("复制失败");
                        }
                    }
                }
        );

        mBtnFooterCopy.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        try {
                            String word = acTextView.getText().toString();
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("plans string", word);
                            clipboard.setPrimaryClip(clip);
                            ToastUtil.show("长按复制成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show("复制失败");
                        }
                        return true;
                    }
                }
        );

        mBtnFooterScrollTop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (scrollView.getScrollY() > 10) {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                }
        );

        mBtnFooterScrollBottom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }
        );

        mBtnFooterShare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        String text = acTextView.getText().toString().trim();
                        Trace.e("acTextView", text);
                        if(text != null && !text.equals("")) {
                            LinearLayout mLayoutRoot;
                            mLayoutRoot = new LinearLayout(view.getContext());

                            //实例化分享窗口
                            SharePopupWindow spw = new SharePopupWindow(PopupActivity.this, text); ;
                            // 显示窗口
                            spw.showAtLocation(mLayoutRoot, Gravity.BOTTOM, 0, 0);
                        }
                    }
                }
        );
    }

    private IDictionary getDictionaryFromOutputPlan(OutputPlanPOJO outputPlan) {
        String dictionaryName = outputPlan.getDictionaryKey();
        for (IDictionary dict : dictionaryList) {
            if (dict.getDictionaryName().equals(dictionaryName)) {
                return dict;
            }
        }
        return null;
    }

    private void processDefinitionList(List<Definition> definitionList) {
        if (definitionList.isEmpty()) {
            viewDefinitionList.removeAllViewsInLayout();
            viewDefinitionList.setMinimumHeight(viewDefinitionList.getMinimumHeight());
            //dw
//            Toast.makeText(this, R.string.definition_not_found, Toast.LENGTH_SHORT).show();
            showSearchButton();
        } else {
//            DefinitionAdapter defAdapter = new DefinitionAdapter(PopupActivity.this, definitionList, mTextSplitter, currentOutputPlan);
//            LinearLayoutManager llm = new LinearLayoutManager(this);
//            //llm.setAutoMeasureEnabled(true);
//            recyclerViewDefinitionList.setLayoutManager(llm);
//            //recyclerViewDefinitionList.getRecycledViewPool().setMaxRecycledViews(0,0);
//            //recyclerViewDefinitionList.setHasFixedSize(true);
//            //recyclerViewDefinitionList.setNestedScrollingEnabled(false);
//            recyclerViewDefinitionList.setAdapter(defAdapter);
            viewDefinitionList.removeAllViewsInLayout();
//            initVideoMediaoPlayer();
            for (Definition def : definitionList) {
                viewDefinitionList.addView(getCardFromDefinition(def));
            }
//            viewDefinitionList.post(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            View childView = scrollView.getChildAt(0);
//                            if(scrollView.getScrollY() > 10) {
//                                scrollView.fullScroll(ScrollView.FOCUS_UP);
//                            }
//                        }
//                    }
//            );
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        processTextFromFxService();
    }

    boolean isFromAndroidQClipboard = false;
    boolean isFromFxService = false;

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType() == null ? "": intent.getType();
        Trace.e("type", type);
        Trace.e("action", action);

        if (intent == null) {
            return;
        }

        //getStringExtra() may return null
        if (Intent.ACTION_SEND.equals(action) && type.equals("text/plain")) {
            String base64 = intent.getStringExtra(Constant.INTENT_ANKIHELPER_BASE64);
            mTextToProcess = intent.getStringExtra(Intent.EXTRA_TEXT);

            if (mTextToProcess != null && mTextToProcess.equals(Constant.USE_FX_SERVICE_CB_FLAG)) {
                mTextToProcess = "";
                isFromFxService = true;
            } else if (mTextToProcess != null && mTextToProcess.equals(Constant.USE_CLIPBOARD_CONTENT_FLAG)) {
                mTextToProcess = "";
                isFromAndroidQClipboard = true;
            } else if (base64 != null && !base64.equals("0")) {
                mTextToProcess = new String(Base64.decode(mTextToProcess, Base64.DEFAULT));
            }

            //mFbReaderBookmarkId = intent.getStringExtra(Constant.INTENT_ANKIHELPER_FBREADER_BOOKMARK_ID);
            String noteEditedByUser = intent.getStringExtra(Constant.INTENT_ANKIHELPER_NOTE);
            if (noteEditedByUser != null) {
                mNoteEditedByUser = noteEditedByUser;
            }
            String updateId = intent.getStringExtra(Constant.INTENT_ANKIHELPER_NOTE_ID);
            mUpdateAction = intent.getStringExtra(Constant.INTENT_ANKIHELPER_UPDATE_ACTION);
            if (updateId != null && !updateId.isEmpty()) {
                try {
                    mUpdateNoteId = Long.parseLong(updateId);
                    if (mUpdateNoteId > 0) {
                        mTagEditedByUser =
                                MyApplication.getAnkiDroid()
                                        .getApi().getNote(mUpdateNoteId)
                                        .getTags();
                    }
                } catch (Exception e) {

                }
            }
            mTargetWord = intent.getStringExtra(Constant.INTENT_ANKIHELPER_TARGET_WORD);
        } else if (Intent.ACTION_PROCESS_TEXT.equals(action) && type.equals("text/plain")) {
            mTextToProcess = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
            mTargetWord = intent.getStringExtra(Constant.INTENT_ANKIHELPER_TARGET_WORD);
        } else if (Intent.ACTION_PROCESS_TEXT.equals(action) && type.equals("text/*")) {
            mTextToProcess = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
            mTargetWord = intent.getStringExtra(Constant.INTENT_ANKIHELPER_TARGET_WORD);
        } else if(Intent.ACTION_VIEW.equals(action)) {
            mTextToProcess = intent.getData().getQueryParameter("content");
            mTargetWord = intent.getData().getQueryParameter("key");
            Trace.i("mTextToProcess", intent.getDataString());
        }

        if (mTextToProcess == null) {
            return;
        }

        mTextToProcess = PunctuationUtil.chinesePunctuationToEnglish(mTextToProcess.trim());
//        mTextToProcess = StringUtil.htmlTagFilter(mTextToProcess);

        //判断分享内容是否带有分享Url协议
        if(RegexUtil.isScrollingToTextFragment(mTextToProcess))
        {
            String text = mTextToProcess;
//            mTextToProcess = text.substring(1, text.lastIndexOf("\n")-1);
//            mTargetWord = intent.getStringExtra(Constant.INTENT_ANKIHELPER_TARGET_WORD);//无用语句，保留以后可能用得上
//            mUrl = text.substring(text.lastIndexOf("\n")+1, text.length()).trim();

            mTextToProcess =  RegexUtil.getTextOfFragment(text);
            mUrl = RegexUtil.getLinkOfFragment(text);
        } else {
//            mTargetWord = intent.getStringExtra(Constant.INTENT_ANKIHELPER_TARGET_WORD);//无用语句，保留以后可能用得上
            String url = intent.getStringExtra(Constant. INTENT_ANKIHELPER_TARGET_URL);
            if(url != null)
                mUrl = url;
        }

        populateWordSelectBox();

        // 是否复制文本
//        if(settings.get(Settings.COPY_MARKED_TEXT, false) && !mTextToProcess.equals("")) {
//            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("plans string", mTextToProcess);
//            clipboard.setPrimaryClip(clip);
//        }

        HistoryUtil.savePopupOpen(mTextToProcess);

        bigBangLayout.post(new Runnable() {
            @Override
            public void run() {
//                if(!mTextToProcess.equals("") && isTransparent())
//                    setTransparent(1.0f);
                setTargetWord();
                if (Utils.containsTranslationField(currentOutputPlan) && settings.getPopupToolbarAutomaticTranslationEnable()) {
                    asyncTranslate(mTextToProcess);
                }
            }
        });
    }

    private void processTextFromFxService() {
        if( mTextToProcess == null || mTextToProcess.equals("")) {
            String text = "";
            mTextToProcess = "";
            if (isFromFxService || isFromAndroidQClipboard) {
                text = ClipboardUtils.getText(getApplicationContext());
                Trace.e("text", text);
                if (!text.equals("")) {
                    mTextToProcess = text;
                }
                //窗口未销毁，从失焦到得焦时，会调用onWindowFocusChanged，所以必须通过mTextToProcess识别
//                if (mTextToProcess.equals(""))
////                finish();
//                    moveTaskToBack(true);

                if (mTextToProcess.equals("")) {
                    isRefreshedBigBang = true;
                    mEditTextTranslation.setText("");
                    mEditTextTranslation.setVisibility(View.GONE);
                    bigBangLayoutWrapper.setVisibility(View.GONE);
                    return;
                } else {
                    mEditTextTranslation.setVisibility(View.VISIBLE);
                    bigBangLayoutWrapper.setVisibility(View.VISIBLE);
                    //判断分享内容是否带有分享Url协议
                    if(RegexUtil.isScrollingToTextFragment(text))
                    {
//                        mTextToProcess = text.substring(1, mTextToProcess.lastIndexOf("\n")-1);
//                        mUrl = text.substring(mTextToProcess.lastIndexOf("\n")+1, mTextToProcess.length()).trim();
                        mTextToProcess =  RegexUtil.getTextOfFragment(text);
                        mUrl = RegexUtil.getLinkOfFragment(text);
                    }
                }

                if (settings.getClearSearchedEnable()) {
                    ClipboardUtils.clearFirstClipboard(getApplicationContext());
                }

                populateWordSelectBox();
                bigBangLayout.post(new Runnable() {
                    @Override
                    public void run() {
//                        if (!mTextToProcess.equals("") && isTransparent())
//                            setTransparent(1.0f);
                        setTargetWord();
                        if (Utils.containsTranslationField(currentOutputPlan) && settings.getPopupToolbarAutomaticTranslationEnable()) {
                            asyncTranslate(mTextToProcess);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    //ed
    private void populateWordSelectBox() {
        List<String> localSegments = TextSplitter.getLocalSegments(mTextToProcess);
        bigBangLayout.removeAllViews();
        for (String localSegment : localSegments) {
            bigBangLayout.addTextItem(localSegment);
        }
        bigBangLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        String currentWord = FieldUtil.getSelectedText(bigBangLayout.getLines());
                        if (!currentWord.equals("") && !currentWord.equals(acTextView.getText().toString())) {
                            mCurrentKeyWord = currentWord;
                            acTextView.setText(currentWord);
                            asyncSearch(currentWord);
                        }
                    }
                }
        );
    }


    private void asyncSearch(final String word) {
        if (word.length() == 0) {
            showPronounce(false);
            return;
        }
        if (currentDictionary == null || currentOutputPlan == null) {
            return;
        }
        showProgressBar();
        progressBar.invalidate();
        showPronounce(true);
        if (mPreThread != null && mPreThread.getState() == Thread.State.RUNNABLE)
            mPreThread.interrupt();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    List<Definition> d = currentDictionary.wordLookup(word);
                    Message message = mHandler.obtainMessage();
                    message.obj = d;
                    message.what = PROCESS_DEFINITION_LIST;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    String error = e.getMessage();
                    Message message = mHandler.obtainMessage();
                    message.obj = error;
                    message.what = ASYNC_SEARCH_FAILED;
                    mHandler.sendMessage(message);
                }
            }
        });
        thread.start();
        mPreThread = thread;
        HistoryUtil.saveWordlookup(mTextToProcess, word);
    }

    private void asyncTranslate(final String text) {
        if (text == null) return;
        if (text.trim().equals("")) return;
        showTranslateLoading();
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result;
                            result = new TranslateBuilder(settings.getTranslatorCheckedIndex()).translate(text, currentDictionary.getLanguageType());
                            Message message = mHandler.obtainMessage();
                            message.obj = result;
                            message.what = TRANSLATION_DONE;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            String error = e.getMessage();
                            Message message = mHandler.obtainMessage();
                            message.obj = error;
                            message.what = TRANSLATIOn_FAILED;
                            mHandler.sendMessage(message);
                        }
                    }
                }
        );
        thread.start();
    }

    private void setActAdapter(IDictionary dict) {
        Object adapter = dict.getAutoCompleteAdapter(PopupActivity.this,
                android.R.layout.simple_spinner_dropdown_item);
        if (adapter != null) {
            if (adapter instanceof SimpleCursorAdapter) {
                acTextView.setAdapter((SimpleCursorAdapter) adapter);
            } else if (adapter instanceof UrbanAutoCompleteAdapter) {
                acTextView.setAdapter((UrbanAutoCompleteAdapter) adapter);
            }
        }
        acTextView.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            if (acTextView.getText().toString().trim().isEmpty()) {
                                return;
                            }
                            acTextView.showDropDown();
                        }
                    }
                }
        );
    }

    //plan B
    private View getCardFromDefinition(final Definition def) {
//        int index = viewDefinitionList.getChildCount();
//        Trace.e("index", String.valueOf(index));

        View view;
        if (settings.getLeftHandModeQ()) {
            view = LayoutInflater.from(PopupActivity.this)
                    .inflate(R.layout.definition_item_left, null);
        } else {
            view = LayoutInflater.from(PopupActivity.this)
                    .inflate(R.layout.definition_item, null);
        }
        //toggle fab with clicks
//        view.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(mFab.getVisibility() == View.VISIBLE){
//                            mFab.hide();
//                        }else{
//                            mFab.show();
//                        }
//                    }
//                }
//        );
        final TextView textViewDefinition = (TextView) view.findViewById(R.id.textview_definition);
        final ImageButton btnAddDefinition = (ImageButton) view.findViewById(R.id.btn_add_definition);
        final LinearLayout btnAddDefinitionLarge = (LinearLayout) view.findViewById(R.id.btn_add_definition_large);
        final ImageView defImage = view.findViewById(R.id.def_img);

        btnAddDefinitionLarge.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnAddDefinition.callOnClick();
                    }
                }
        );
        //final Definition def = mDefinitionList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDefinition.setText(Html.fromHtml(def.getDisplayHtml(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textViewDefinition.setText(Html.fromHtml(def.getDisplayHtml()));

        }

        if (def.getDisplayHtml().isEmpty()) {
            textViewDefinition.setVisibility(View.GONE);
        }

        if (def.getImageUrl() != null && !def.getImageUrl().isEmpty()) {
            Glide.with(this).load(def.getImageUrl()).into(defImage);
            defImage.setVisibility(View.VISIBLE);
        }

        //play audio or video
        if(currentDictionary instanceof Mdict ||
            currentDictionary instanceof DictTango)
            textViewDefinition.setTextIsSelectable(true);
        else
            textViewDefinition.setTextIsSelectable(false);

        textViewDefinition.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        StopPlayingAll();
                        if (!(currentDictionary instanceof Mdict ||
                                currentDictionary instanceof DictTango)) {
                            try {
                                String readContent =cleanKey(def, true); // = def.getExportElement("摘取例句").replaceAll("</?[\\w\\W].*?>", "");
                                String finalAudioUrl = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, def.getAudioUrl());

                                int lastPronounceLanguageIndex = Settings.getInstance(PopupActivity.this).getLastPronounceLanguage();
                                PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(lastPronounceLanguageIndex);

                                // SOUND_SOURCE_ONLINE
                                if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_ONLINE) {
                                    if (finalAudioUrl.isEmpty())
                                        Toast.makeText(PopupActivity.this, R.string.play_no_audio, Toast.LENGTH_SHORT).show();
                                        // mp4，缓存
                                    else if (def.getAudioSuffix().equals(Constant.MP4_SUFFIX)) {
                                        //在线媒体缓存
                                        String cacheAudioUrl = getCacheAudioUrl(finalAudioUrl);
//                                        Trace.e("cacheAudioUrl", cacheAudioUrl);

                                        //移动mSufaceView
                                        LinearLayout ly = (LinearLayout) textViewDefinition.getParent();
                                        LinearLayout preLy = (LinearLayout) mSurfaceView.getParent();
                                        if (mVideoMediaPlayer == null) {
                                            ly.addView(mSurfaceView);
                                            initVideoMediaPlayer();
                                        } else {
                                            if (mSurfaceView != null) {
                                                if (mSurfaceView.getVisibility() == View.VISIBLE) {
                                                    if (!ly.equals(preLy)) {
                                                        preLy.removeView(mSurfaceView);
                                                        ly.addView(mSurfaceView);
                                                        initVideoMediaPlayer();
                                                    }
                                                } else if (mSurfaceView.getVisibility() == View.GONE) {
                                                    if (preLy != null) {
                                                        preLy.removeView(mSurfaceView);
                                                    }
                                                    ly.addView(mSurfaceView);
                                                    initVideoMediaPlayer();
                                                }
                                            }
                                        }

                                        mMediaProgress.setVisibility(View.VISIBLE);
                                        mVideoMediaPlayer.setDataSource(PopupActivity.this, Uri.parse(cacheAudioUrl));
                                        mVideoMediaPlayer.prepareAsync();
                                    }
                                    //mp3，不缓存
                                    else if (def.getAudioSuffix().equals(Constant.MP3_SUFFIX)) {
                                        // 隐藏mSurfaceView
                                        if (mSurfaceView != null && mSurfaceView.getVisibility() == View.VISIBLE)
                                            mSurfaceView.setVisibility(View.GONE);

                                        mMediaProgress.setVisibility(View.VISIBLE);
                                        PlayAudioManager.playPronounceSound(PopupActivity.this, finalAudioUrl);
                                        mMediaProgress.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMediaProgress.setVisibility(View.GONE);
                                            }
                                        }, 500);
                                    }
                                }
                                // SOUND_SOURCE_TTS
                                else if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_TTS) {
                                    // 隐藏mSurfaceView
                                    if (mSurfaceView != null && mSurfaceView.getVisibility() == View.VISIBLE)
                                        mSurfaceView.setVisibility(View.GONE);

                                    mMediaProgress.setVisibility(View.VISIBLE);
                                    playTts(readContent);
                                    mMediaProgress.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mMediaProgress.setVisibility(View.GONE);
                                        }
                                    }, 500);
                                }
                                // SOUND_SOURCE_YOUDAO
                                // SOUND_SOURCE_EUDICT
                                else if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_YOUDAO ||
                                        infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_EUDICT) {
                                    // 隐藏mSurfaceView
                                    if (mSurfaceView != null && mSurfaceView.getVisibility() == View.VISIBLE)
                                        mSurfaceView.setVisibility(View.GONE);

                                    mMediaProgress.setVisibility(View.VISIBLE);
                                    PlayAudioManager.playPronounceSound(PopupActivity.this, finalAudioUrl);
                                    mMediaProgress.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mMediaProgress.setVisibility(View.GONE);
                                        }
                                    }, 500);
                                }
                                currentDictionary.setAudioUrl(def.getAudioUrl());

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(PopupActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                            } catch (IllegalStateException ise) {

                            } catch (Exception e) {

                            }
                        }
                        return true;
                    }
                }
        );
        textViewDefinition.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StopPlayingAll();
                        try {
                            String readContent = cleanKey(def);
                            Trace.i("speaks", readContent);
                            String finalAudioUrl = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, def.getAudioUrl());

                            int lastPronounceLanguageIndex = Settings.getInstance(PopupActivity.this).getLastPronounceLanguage();
                            PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(lastPronounceLanguageIndex);

                            // SOUND_SOURCE_ONLINE
                            if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_ONLINE) {
                                if (finalAudioUrl.isEmpty())
                                    Toast.makeText(PopupActivity.this, R.string.play_no_audio, Toast.LENGTH_SHORT).show();
                                    // mp4，缓存
                                else if (def.getAudioSuffix().equals(Constant.MP4_SUFFIX)) {
                                    //在线媒体缓存
                                    String cacheAudioUrl = getCacheAudioUrl(finalAudioUrl);
//                                    Trace.e("cacheAudioUrl", cacheAudioUrl);

                                    //移动mSufaceView
                                    LinearLayout ly = (LinearLayout) textViewDefinition.getParent();
                                    LinearLayout preLy = (LinearLayout) mSurfaceView.getParent();
                                    if (mVideoMediaPlayer == null) {
                                        ly.addView(mSurfaceView);
                                        initVideoMediaPlayer();
                                    } else {
                                        if (mSurfaceView != null) {
                                            if (mSurfaceView.getVisibility() == View.VISIBLE) {
                                                if (!ly.equals(preLy)) {
                                                    preLy.removeView(mSurfaceView);
                                                    ly.addView(mSurfaceView);
                                                    initVideoMediaPlayer();
                                                }
                                            } else if (mSurfaceView.getVisibility() == View.GONE) {
                                                if (preLy != null) {
                                                    preLy.removeView(mSurfaceView);
                                                }
                                                ly.addView(mSurfaceView);
                                                initVideoMediaPlayer();

                                            }
                                        }
                                    }

                                    mMediaProgress.setVisibility(View.VISIBLE);
                                    mVideoMediaPlayer.setDataSource(PopupActivity.this, Uri.parse(cacheAudioUrl));
                                    mVideoMediaPlayer.prepareAsync();
                                }
                                //mp3，不缓存
                                else if (def.getAudioSuffix().equals(Constant.MP3_SUFFIX)) {
                                    // 隐藏mSurfaceView
                                    if (mSurfaceView != null && mSurfaceView.getVisibility() == View.VISIBLE)
                                        mSurfaceView.setVisibility(View.GONE);

                                    mMediaProgress.setVisibility(View.VISIBLE);
                                    PlayAudioManager.playPronounceSound(PopupActivity.this, finalAudioUrl);
                                    mMediaProgress.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mMediaProgress.setVisibility(View.GONE);
                                        }
                                    }, 500);
                                }
                            }
                            // SOUND_SOURCE_TTS
                            else if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_TTS) {
                                // 隐藏mSurfaceView
                                if (mSurfaceView != null && mSurfaceView.getVisibility() == View.VISIBLE)
                                    mSurfaceView.setVisibility(View.GONE);

                                mMediaProgress.setVisibility(View.VISIBLE);
                                playTts(readContent);
                                mMediaProgress.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMediaProgress.setVisibility(View.GONE);
                                    }
                                }, 500);
                            }
                            // SOUND_SOURCE_YOUDAO
                            // SOUND_SOURCE_EUDICT
                            else if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_YOUDAO ||
                                    infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_EUDICT) {
                                // 隐藏mSurfaceView
                                if (mSurfaceView != null && mSurfaceView.getVisibility() == View.VISIBLE)
                                    mSurfaceView.setVisibility(View.GONE);

                                mMediaProgress.setVisibility(View.VISIBLE);
                                PlayAudioManager.playPronounceSound(PopupActivity.this, finalAudioUrl);
                                mMediaProgress.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMediaProgress.setVisibility(View.GONE);
                                    }
                                }, 500);
                            }
                            currentDictionary.setAudioUrl(def.getAudioUrl());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PopupActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                        } catch (IllegalStateException ise) {

                        } catch(Exception e) {

                        }

                    }
                }
        );

        // mSurfaceView点击事件-播放视频
        mSurfaceView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            StopPlayingAll();
                            String finalAudioUrl = currentDictionary.getAudioUrl();
                            String cacheAudioUrl = getCacheAudioUrl(finalAudioUrl);
                            mMediaProgress.setVisibility(View.VISIBLE);
                            mSurfaceView.setVisibility(View.VISIBLE);
                            mVideoMediaPlayer.setDataSource(PopupActivity.this, Uri.parse(cacheAudioUrl));
                            mVideoMediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PopupActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                        } catch (IllegalStateException e) {

                        }
                    }
                }
        );


        //set custom action for the textView
        makeTextViewSelectAndSearch(textViewDefinition);
        //holder.itemView.setAnimation(AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_in));
        //holder.textViewDefinition.setTextColor(Color.BLACK);
        btnAddDefinition.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //vibarate(Constant.VIBRATE_DURATION);
                        //before add, check if this note is already added by check the attached tag
                        try {
                            AnkiDroidHelper mAnkiDroid = MyApplication.getAnkiDroid();
                            Long noteIdAdded = (Long) btnAddDefinition.getTag(R.id.TAG_NOTE_ID);
                            if (noteIdAdded != null) {
                                if (mUpdateNoteId == 0) {
                                    if (Utils.deleteNote(PopupActivity.this, noteIdAdded.longValue())) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            btnAddDefinition.setBackground(ContextCompat.getDrawable(
                                                    PopupActivity.this,
                                                    Utils.getResIdFromAttribute(PopupActivity.this, R.attr.icon_add)));
                                        }
                                        btnAddDefinition.setTag(R.id.TAG_NOTE_ID, null);
                                        Toast.makeText(PopupActivity.this, R.string.str_cancel_note_add, Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(PopupActivity.this, R.string.error_note_cancel, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(PopupActivity.this, R.string.str_not_cancelable_append_mode, Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                            ///////////////////////////////////



                            //save css
                            if (def.getCssUrl() != null && !def.getCssUrl().isEmpty()) {
                                String saveFile = Constant.AUDIO_MEDIA_DIRECTORY + def.getCssName();
                                final Request request = new Request(def.getCssUrl(), saveFile);
//                                Trace.e("cssUrl", def.getCssUrl());
//                                Trace.e("cssName", saveFile);
                                download(request, saveFile);
                            }

                            //save js
                            if (def.getJsUrl() != null && !def.getJsUrl().isEmpty()) {
                                String saveFile = Constant.AUDIO_MEDIA_DIRECTORY + def.getJsName();
                                final Request request = new Request(def.getJsUrl(), saveFile);
//                                Trace.e("jsUrl", def.getJsUrl());
//                                Trace.e("jsName", saveFile);
                                download(request, saveFile);
                            }

                            //save image
                            if (def.getImageUrl() != null && !def.getImageUrl().isEmpty()) {

                                if (defImage.getDrawable() != null && true) {
                                    BitmapDrawable drawable = (BitmapDrawable) defImage.getDrawable();
                                    Bitmap bm = drawable.getBitmap();

                                    OutputStream fOut = null;
                                    //Uri outputFileUri;
                                    try {
                                        File root = new File(Constant.IMAGE_MEDIA_DIRECTORY);
                                        if (!root.exists()) {
                                            root.mkdirs();
                                        }

                                        File sdImageMainDirectory = new File(root, def.getImageName());
                                        //outputFileUri = Uri.fromFile(sdImageMainDirectory);
                                        fOut = new FileOutputStream(sdImageMainDirectory);
                                    } catch (Exception e) {

                                    }
                                    try {
                                        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            // mp3 下载 start

                            //end

                            String[] sharedExportElements = Constant.getSharedExportElements();
                            String[] exportFields = new String[currentOutputPlan.getFieldsMap().size()];
                            int i = 0;
//                            Map<String, String> map = currentOutputPlan.getFieldsMap();
                            for (String exportedFieldKey : currentOutputPlan.getFieldsMap().values()) {
                                ComplexElement ce = new ComplexElement(exportedFieldKey);
                                if(mUpdateNoteId > 0 && mUpdateAction == null)
                                    exportedFieldKey = ce.getElementAppending();
                                else
                                    exportedFieldKey = ce.getElementNormal();
                                Trace.e("exported", String.valueOf(exportedFieldKey));
                                if (exportedFieldKey.equals(sharedExportElements[0])) {
                                    exportFields[i] = "";
                                    i++;
                                    continue;
                                }

                                if (exportedFieldKey.equals(sharedExportElements[1])) {
                                    exportFields[i] = getNormalSentence(bigBangLayout.getLines());
                                    i++;
                                    continue;
                                }

                                if (exportedFieldKey.equals(sharedExportElements[2])) {
                                    exportFields[i] = getBoldSentence(bigBangLayout.getLines());
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[3])) {
                                    exportFields[i] = getBlankSentence(bigBangLayout.getLines(), true);
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[4])) {
                                    exportFields[i] = getBlankSentence(bigBangLayout.getLines(), false);
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[5])) {
                                    exportFields[i] = mNoteEditedByUser;
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[6])) {
//                                    if(mUrl.contains(":~:text=")) {
//                                        String readContent = cleanKey(def);
//                                        exportFields[i] = StringUtil.appendTagAll(mUrl, readContent, "-,", ",-");
//                                    }
//                                    else
                                        exportFields[i] = mUrl;
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[7])) {
                                    exportFields[i] = Utils.getAllHtmlFromDefinitionList(mDefinitionList);
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[8])) {
                                    exportFields[i] = mEditTextTranslation.getText().toString().replace("\n", "<br/>");
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[9])) {
                                    currentDictionary.setAudioUrl(def.getAudioUrl());
                                    String readContent = cleanKey(def);
                                    String tempUrl = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, currentDictionary.getAudioUrl());
                                    exportFields[i] = !tempUrl.isEmpty() ? tempUrl : "";
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[10]) ||
                                        exportedFieldKey.equals(Constant.DICT_FIELD_COMPLEX_ONLINE)) {
                                    currentDictionary.setAudioUrl(def.getAudioUrl());
                                    String readContent = cleanKey(def);
                                    final int lastPronounceLanguageIndex = settings.getLastPronounceLanguage();
                                    PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(lastPronounceLanguageIndex);
                                    String TPL_HTML_MEDIA_TAG;
                                    if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_ONLINE && def.getAudioSuffix().equals(Constant.MP4_SUFFIX)) {
                                        TPL_HTML_MEDIA_TAG = Constant.TPL_HTML_VIDEO_TAG;
                                    } else {
                                        TPL_HTML_MEDIA_TAG = Constant.TPL_HTML_AUDIO_TAG;
                                    }
                                    final String finalMediaUrlOnline = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, currentDictionary.getAudioUrl());

                                    if (exportFields.equals(sharedExportElements[10]))
                                        exportFields[i] = String.format(TPL_HTML_MEDIA_TAG, finalMediaUrlOnline);
                                    //Constant.DICT_FIELD_COMPLEX_ONLINE
                                    else if (exportedFieldKey.equals(Constant.DICT_FIELD_COMPLEX_ONLINE)) {
                                        exportFields[i] = def.getExportElement(Constant.DICT_FIELD_COMPLEX_ONLINE).
                                                replace(Constant._TPL_HTML_NOTE_TAG_LOCATION_,
                                                        String.format(Constant.TPL_HTML_NOTE_TAG, mNoteEditedByUser)).
                                                replace(Constant._TPL_HTML_MEDIA_TAG_LOCATION_,
                                                        String.format(TPL_HTML_MEDIA_TAG, finalMediaUrlOnline));
                                    }
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[11])) {
                                    currentDictionary.setAudioUrl(def.getAudioUrl());
                                    String readContent = cleanKey(def);
                                    final String finalMediaUrlOnline = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, currentDictionary.getAudioUrl());
                                    exportFields[i] = PlayAudioManager.getSoundTag(PopupActivity.this, finalMediaUrlOnline);

                                    i++;
                                    continue;
                                }

                                // Download mp3, mp4 files at all and save offline-audio in mobile.
                                //下载字段11"🔊|🎞💾▶️" 和 "苹果专用下载字段12 🔊|🎞💾▶️ (Needs Field: Remarks)"
                                if (exportedFieldKey.equals(sharedExportElements[12]) ||
                                        exportedFieldKey.equals(sharedExportElements[13]) ||
                                        exportedFieldKey.equals(sharedExportElements[14]) ||
                                        exportedFieldKey.equals(Constant.DICT_FIELD_COMPLEX_OFFLINE)) {

                                    currentDictionary.setAudioUrl(def.getAudioUrl());

                                    String readContent = cleanKey(def);

                                    final int lastPronounceLanguageIndex = settings.getLastPronounceLanguage();
                                    PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(lastPronounceLanguageIndex);

                                    String SavedAudioOrVideoFileName;
                                    String TPL_HTML_MEDIA_TAG;
                                    if (infor.getSoundSourceType() == PronounceManager.SOUND_SOURCE_ONLINE && def.getAudioSuffix().equals(Constant.MP4_SUFFIX)) {
                                        SavedAudioOrVideoFileName = def.getAudioName() + Constant.MP4_SUFFIX;
                                        TPL_HTML_MEDIA_TAG = Constant.TPL_HTML_VIDEO_TAG;
                                    } else {
                                        SavedAudioOrVideoFileName = def.getAudioName() + Constant.MP3_SUFFIX;
                                        TPL_HTML_MEDIA_TAG = Constant.TPL_HTML_AUDIO_TAG;
                                    }

                                    final String finalMediaUrlOnline = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, currentDictionary.getAudioUrl());
                                    final Request request = new Request(finalMediaUrlOnline, Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioOrVideoFileName);

                                    //判断媒体是否有缓存，有则改名
                                    if (mProxy.isCached(finalMediaUrlOnline)) {
                                        File file = new File(URI.create(mProxy.getProxyUrl(finalMediaUrlOnline)));
                                        String.valueOf(file.renameTo(new File(Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioOrVideoFileName)));
                                        Toast.makeText(PopupActivity.this, R.string.downlaod_completed, Toast.LENGTH_SHORT).show();
                                    }
                                    //在线语音下载
                                    else if (!finalMediaUrlOnline.equals("") && !infor.isSoundSourceType(PronounceManager.SOUND_SOURCE_TTS)) {
                                        Trace.e("url", "download");
                                        download(request, Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioOrVideoFileName);
                                    }

                                    //tts语音下载
                                    if (infor.isSoundSourceType(PronounceManager.SOUND_SOURCE_TTS)) {
                                        File f = new File(Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioOrVideoFileName);
                                        if(!f.exists()) {
                                            restoreTtsVoice(readContent, f.getPath());
                                            Toast.makeText(PopupActivity.this, R.string.downlaod_completed, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //音频字段
                                    //url，无tag
                                    if (exportedFieldKey.equals(sharedExportElements[12])) {
                                        exportFields[i] = SavedAudioOrVideoFileName;
                                    }
                                    //"全video tag
                                    else if (exportedFieldKey.equals(sharedExportElements[13])) {
                                        exportFields[i] = String.format(TPL_HTML_MEDIA_TAG, SavedAudioOrVideoFileName);
                                    }
                                    //"全sound tag，苹果专用下载字段 Remarks字段"
                                    else if (exportedFieldKey.equals(sharedExportElements[14])) {
                                        exportFields[i] = PlayAudioManager.getSoundTag(PopupActivity.this, SavedAudioOrVideoFileName);
                                    }
                                    //Constant.DICT_FIELD_COMPLEX_OFFLINE
                                    else if (exportedFieldKey.equals(Constant.DICT_FIELD_COMPLEX_OFFLINE)) {
                                        exportFields[i] = def.getExportElement(Constant.DICT_FIELD_COMPLEX_OFFLINE).
                                                replace(Constant._TPL_HTML_NOTE_TAG_LOCATION_,
                                                        String.format(Constant.TPL_HTML_NOTE_TAG, mNoteEditedByUser)).
                                                replace(Constant._TPL_HTML_MEDIA_TAG_LOCATION_,
                                                String.format(TPL_HTML_MEDIA_TAG, SavedAudioOrVideoFileName));
                                    }
                                    i++;
                                    continue;
                                }

                                if (exportedFieldKey.equals(sharedExportElements[15])) {
                                    String readContent = StringUtil.htmlTagFilter(mTextToProcess);;
                                    String tempUrl = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, PlayAudioManager.YOUDAO_AUDIO, pickedSentencePronouceLanguageIndex);
                                    exportFields[i] = !tempUrl.isEmpty() ? tempUrl : "";
                                    i++;
                                    continue;
                                }
                                if (exportedFieldKey.equals(sharedExportElements[16])) {
                                    String readContent = StringUtil.htmlTagFilter(mTextToProcess);
                                    final String finalMediaUrlOnline = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, readContent, PlayAudioManager.YOUDAO_AUDIO, pickedSentencePronouceLanguageIndex);
                                    exportFields[i] = PlayAudioManager.getSoundTag(PopupActivity.this, finalMediaUrlOnline);

                                    i++;
                                    continue;
                                }

                                if(exportedFieldKey.equals(sharedExportElements[17]) ||
                                        exportedFieldKey.equals(sharedExportElements[18])) {
                                    String text = StringUtil.htmlTagFilter(mTextToProcess);
                                    PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(pickedSentencePronouceLanguageIndex);
                                    String SavedAudioFileName = def.getAudioName() + "_ps" + Constant.MP3_SUFFIX;
                                    final String finalMediaUrlOnline = PlayAudioManager.getSoundUrlOnline(PopupActivity.this, text, PlayAudioManager.YOUDAO_AUDIO, pickedSentencePronouceLanguageIndex);
                                    final Request request = new Request(finalMediaUrlOnline, Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioFileName);

                                    if (!finalMediaUrlOnline.equals("") && !infor.isSoundSourceType(PronounceManager.SOUND_SOURCE_TTS)) {
                                        Trace.e("url", "download");
                                        download(request, Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioFileName);
                                    }

                                    //tts语音下载
                                    if (infor.isSoundSourceType(PronounceManager.SOUND_SOURCE_TTS)) {
                                        File f = new File(Constant.AUDIO_MEDIA_DIRECTORY + SavedAudioFileName);
                                        if(!f.exists()) {
                                            restoreTtsVoice(text, f.getPath());
                                            Toast.makeText(PopupActivity.this, R.string.downlaod_completed, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //音频字段
                                    //uri，无tag
                                    if (exportedFieldKey.equals(sharedExportElements[17])) {
                                        exportFields[i] = SavedAudioFileName;
                                    }
                                    //"全sound tag，苹果专用下载字段 Remarks字段"
                                    else if (exportedFieldKey.equals(sharedExportElements[18])) {
                                        exportFields[i] = PlayAudioManager.getSoundTag(PopupActivity.this, SavedAudioFileName);
                                    }

                                    i++;
                                    continue;
                                }

                                //其他字段
                                if (def.hasElement(exportedFieldKey)) {
                                    //Constant.DICT_FIELD_DEFINITION 释义
                                    if(currentDictionary instanceof Mdict && exportedFieldKey.equals(Constant.DICT_FIELD_DEFINITION)){
                                        int min = 0;
                                        int max = textViewDefinition.getText().length();
                                        if (textViewDefinition.isFocused()) {
                                            final int selStart = textViewDefinition.getSelectionStart();
                                            final int selEnd = textViewDefinition.getSelectionEnd();

                                            min = Math.max(0, Math.min(selStart, selEnd));
                                            max = Math.max(0, Math.max(selStart, selEnd));
                                        }
                                        // Perform your definition lookup with the selected text
                                        exportFields[i] = textViewDefinition.getText().subSequence(min, max).toString();
                                    }
                                    //Constant.DICT_FIELD_COMPLEX_MUTE
                                    else if(exportedFieldKey.equals(Constant.DICT_FIELD_COMPLEX_MUTE)) {
                                        exportFields[i] = def.getExportElement(Constant.DICT_FIELD_COMPLEX_MUTE).
                                                replace(Constant._TPL_HTML_NOTE_TAG_LOCATION_,
                                                        String.format(Constant.TPL_HTML_NOTE_TAG, mNoteEditedByUser)).
                                                replace(Constant._TPL_HTML_MEDIA_TAG_LOCATION_, "");
                                    } else
                                        exportFields[i] = def.getExportElement(exportedFieldKey).replaceAll("\\s{2,}", " ");

                                    i++;
                                    continue;
                                }

                                exportFields[i] = "";
                                i++;
                            }

                            /////////////////
                            long deckId = currentOutputPlan.getOutputDeckId();
                            long modelId = currentOutputPlan.getOutputModelId();
                            if (mUpdateNoteId == 0) {
                                Long result = mAnkiDroid.getApi().addNote(modelId, deckId, exportFields, mTagEditedByUser);
                                if (result != null) {
                                    Toast.makeText(PopupActivity.this, R.string.str_added, Toast.LENGTH_SHORT).show();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        btnAddDefinition.setBackground(ContextCompat.getDrawable(
                                                PopupActivity.this, Utils.getResIdFromAttribute(PopupActivity.this, R.attr.icon_add_done)));
                                    }
                                    clearBigbangSelection();
                                    mNoteEditedByUser = "";
                                    //attach the noteid to the button
                                    btnAddDefinition.setTag(R.id.TAG_NOTE_ID, result);
                                    //if there is a note id field in the model, update the note
                                    int count = 0;
                                    for (String field : currentOutputPlan.getFieldsMap().keySet()) {
                                        if (field.replace(" ", "").toLowerCase().equals("noteid")) {
                                            exportFields[count] = result.toString();
                                            boolean success = mAnkiDroid.getApi().updateNoteFields(
                                                    result.longValue(),
                                                    exportFields
                                            );
                                            if (!success) {
                                                Toast.makeText(PopupActivity.this, R.string.str_error_noteid, Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        }
                                        count++;
                                    }
                                    //save note add
                                    HistoryUtil.saveNoteAdd("", getBoldSentence(bigBangLayout.getLines()),
                                            currentDictionary.getDictionaryName(),
                                            textViewDefinition.getText().toString(),
                                            mTranslatedResult,
                                            mNoteEditedByUser,
                                            mTagEditedByUser.toString()
                                    );
                                } else {
                                    Toast.makeText(PopupActivity.this, R.string.str_failed_add, Toast.LENGTH_SHORT).show();
                                }
                            } else {//there's note id, so we need to retrieve note first
                                NoteInfo note = mAnkiDroid.getApi().getNote(mUpdateNoteId);
                                String[] original = note.getFields();
                                Set<String> tags = note.getTags();
                                if (original == null || original.length != exportFields.length) {
                                    Toast.makeText(PopupActivity.this, R.string.str_error_notetype_noncompatible, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (mUpdateAction != null && mUpdateAction.equals("replace")) {
                                    //replace
                                    for (int j = 0; j < original.length; j++) {
                                        if (exportFields[j].isEmpty()) {
                                            exportFields[j] = original[j];
                                        }
                                    }

                                } else {
                                    //append
                                    for (int j = 0; j < original.length; j++) {
                                        if (original[j].trim().isEmpty() || exportFields[j].trim().isEmpty()) {
                                            exportFields[j] = original[j] + exportFields[j];
                                        } else {
                                            exportFields[j] = original[j] + "<br/>" + exportFields[j];
                                        }
                                    }
                                }
                                //we need to check the tag used by user is already in the tags, if not, add it
                                tags.addAll(mTagEditedByUser);
                                boolean success = mAnkiDroid.getApi().updateNoteFields(mUpdateNoteId, exportFields);
                                boolean successTag = mAnkiDroid.getApi().updateNoteTags(mUpdateNoteId, tags);
                                if (success && successTag) {
                                    Toast.makeText(PopupActivity.this, R.string.str_note_updated, Toast.LENGTH_SHORT).show();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        btnAddDefinition.setBackground(ContextCompat.getDrawable(
                                                PopupActivity.this, Utils.getResIdFromAttribute(PopupActivity.this, R.attr.icon_add_done)));
                                    }
                                    //btnAddDefinition.setEnabled(false);
                                } else {
                                    Toast.makeText(PopupActivity.this, R.string.str_error_note_update, Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (settings.getAutoCancelPopupQ()) {
                                if (fetch == null) {
                                    finish();
                                } else {
                                    if (!isFetchDownloading) {
                                        finish();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(PopupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }

    private  void setupHtmlTagButton(final EditText edt, final Button btn, String front, String behind) {
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Editable edit = edt.getText();
                        int start = edt.getSelectionStart();
                        int end = edt.getSelectionEnd();
                        Trace.e("start : end", String.format("%d : %d", start, end));
                        if(start < end) {
                            edt.setText(StringUtil.appendTagAll(edit.toString(), edit.toString().substring(start, end), front, behind));
                            Spannable spanText = (Spannable) edt.getText();
                            Selection.setSelection(spanText, end + front.length() + behind.length());
                        } else {
                            edt.setText(edit.toString().substring(0, start) + front + behind + edit.toString().substring(start));
                            Spannable spanText = (Spannable) edt.getText();
                            Selection.setSelection(spanText, start + front.length());
                        }
                        edt.setFocusable(true);
                        edt.setFocusableInTouchMode(true);
                        edt.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) edt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(edt, 0);
                    }
                }
        );
    }
    private void setupEditNoteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PopupActivity.this);
        LayoutInflater inflater = PopupActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_note, null);
        dialogBuilder.setView(dialogView);

        final MathxView tvShow = (MathxView) dialogView.findViewById(R.id.tv_show_note_html);
        tvShow.setText("预览");
//        tvShow.setMaxLines(10);
//        tvShow.setMovementMethod(ScrollingMovementMethod.getInstance());

        final NoteEditText edt = (NoteEditText) dialogView.findViewById(R.id.edit_note);
        edt.setHorizontallyScrolling(false);
        edt.setMaxLines(10);
        edt.setText(mNoteEditedByUser);
        edt.setSelection(mNoteEditedByUser.length());
        edt.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String latex = s.toString().trim();
                        String change = convertInline(latex);
                        if(!tvShow.getText().equals(change)) {
                            latex = change;
//                        ToastUtil.show(mathPreview.getmScrollY() +"");
                            tvShow.setText(latex);
                        }
                    }
                }
        );
        if(!edt.getText().toString().equals(""))
            tvShow.setText(edt.getText().toString());
//            tvShow.setText(Html.fromHtml(edt.getText().toString(), Html.FROM_HTML_MODE_COMPACT));

        int heightDpChanged = ScreenUtils.getAvailableScreenHeight(PopupActivity.this);
        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            heightDpChanged = heightDpChanged / 4;
            edt.setMaxHeight(heightDpChanged);
        } else {
            heightDpChanged = heightDpChanged * 2 / 9;
            edt.setMaxHeight(heightDpChanged);
        }
        tvShow.setMaxHeight(heightDpChanged);
        tvShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((MathxView) v).setmScrollY(v.getScrollY());
                }
                return false;
            }
        });

        LinearLayout tagArrAWLL = dialogView.findViewById(R.id.layout_note_buttons);
        MLLabel[] mixTagArr = ArrayUtils.concat(Constant.latexTagArr, Constant.htmlTagArr);
        for(MLLabel tag : mixTagArr) {
            final MLLabelButton btn =  new MLLabelButton(PopupActivity.this);
            btn.setupHtmlTag(edt, tag);
            Paint p = btn.getPaint();
            p.setTypeface(btn.getTypeface());
            p.setTextSize(btn.getTextSize());
            float needWidth = btn.getPaddingLeft()+btn.getPaddingRight()+p.measureText(btn.getText().toString());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.leftMargin = ScreenUtils.dp2px(getApplicationContext(),2);
            params.rightMargin = ScreenUtils.dp2px(getApplicationContext(),2);
            params.height = ScreenUtils.dp2px(getApplicationContext(),28);
            params.width = ScreenUtils.dp2px(getApplicationContext(),28);
            if(params.width < needWidth)
                params.width = (int) (needWidth + 10);
            tagArrAWLL.addView(btn, params);
        }

        LinearLayout performEditArrLL = dialogView.findViewById(R.id.ll_perform_edit_buttons);
        PerformEdit performEdit = new PerformEdit(edt);
        for(PerformEditButton.ActionEnum action : PerformEditButton.ActionEnum.values()) {
            final PerformEditButton btn = new PerformEditButton(PopupActivity.this);
            btn.setupPerformEditAction(action, performEdit);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.leftMargin = ScreenUtils.dp2px(getApplicationContext(),2);
            params.rightMargin = ScreenUtils.dp2px(getApplicationContext(),2);
            params.height = ScreenUtils.dp2px(getApplicationContext(),28);
            params.width = ScreenUtils.dp2px(getApplicationContext(),28);
            performEditArrLL.addView(btn, params);
        }
//        MLLabelButton.HtmlTagEnums[] values = MLLabelButton.HtmlTagEnums.values();
//        for(MLLabelButton.HtmlTagEnums tag : values) {
//            final MLLabelButton hbtn = new MLLabelButton(MyApplication.getContext());
//            hbtn.setupHtmlTag(edt, tag);
//            ly.addView(hbtn);
//        }

        dialogBuilder.setTitle(R.string.dialog_note);
        //dialogBuilder.setMessage("输入笔记");
        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mNoteEditedByUser = edt.getText().toString().replaceAll("\\n|\\r|\\t", "");
            }
        });
//                        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                //pass
//                            }
//                        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void setupEditTagDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PopupActivity.this);
        LayoutInflater inflater = PopupActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_tag, null);
        dialogBuilder.setView(dialogView);
        final AutoCompleteTextView editTag = (AutoCompleteTextView) dialogView.findViewById(R.id.edit_tag);
        final CheckBox checkBoxSetAsDefaultTag = (CheckBox) dialogView.findViewById(R.id.checkbox_as_default_tag);
        final ChipGroup tagChipGroup = (ChipGroup) dialogView.findViewById(R.id.tag_chip_list);
        editTag.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if (mTagEditedByUser.size() > 0) {
            String text = Utils.fromTagSetToString(mTagEditedByUser);
            editTag.setText(text);
            editTag.setSelection(text.length());
        }
        tagChipGroup.setSingleSelection(false);
        final List<UserTag> userTags = DataSupport.findAll(UserTag.class);
        for (UserTag userTag : userTags) {
            final Chip chip = (Chip) inflater.inflate(R.layout.tag_chip_item, null);
            chip.setText(userTag.getTag());
            chip.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                mTagEditedByUser.add(chip.getText().toString());
                            } else {
                                mTagEditedByUser.remove(chip.getText().toString());
                            }
                            //tag1,tag2,tag3
                            String text = Utils.fromTagSetToString(mTagEditedByUser);
                            editTag.setText(text);
                            editTag.setSelection(text.length());
                        }
                    }
            );
            if (mTagEditedByUser.contains(chip.getText().toString())) {
                chip.setChecked(true);
            }
            tagChipGroup.addView(chip);
        }
//        String[] arr = new String[userTags.size()];
//        for (int i = 0; i < userTags.size(); i++) {
//            arr[i] = userTags.get(i).getTag();
//        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PopupActivity.this,
//                R.layout.support_simple_spinner_dropdown_item, arr);
//        editTag.setAdapter(arrayAdapter);
//        editTag.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (editTag.getText().toString().isEmpty()) {
//                    editTag.showDropDown();
//                }
//                return false;
//            }
//        });
        boolean setDefaultQ = settings.getSetAsDefaultTag();
        checkBoxSetAsDefaultTag.setChecked(setDefaultQ);
        dialogBuilder.setTitle(R.string.dialog_tag);
        //dialogBuilder.setMessage("输入笔记");
        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String tag = editTag.getText().toString().trim();
                if (tag.isEmpty()) {
                    if (checkBoxSetAsDefaultTag.isChecked()) {
                        mTagEditedByUser.clear();
                        Toast.makeText(PopupActivity.this, R.string.tag_cant_be_blank, Toast.LENGTH_LONG).show();
                    } else {
                        settings.setSetAsDefaultTag(false);
                        mTagEditedByUser.clear();
                    }
                    return;
                } else {
                    mTagEditedByUser = Utils.fromStringToTagSet(editTag.getText().toString());
                    settings.setSetAsDefaultTag(checkBoxSetAsDefaultTag.isChecked());
                    settings.setDefaultTag(editTag.getText().toString());
                    for (String t : mTagEditedByUser) {
                        if (!userTags.contains(t)) { //add new tag
                            UserTag userTag = new UserTag(t);
                            userTag.save();
                        }
                    }
                }

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void initFetch() {
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(3)
                .build();
        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        fetchListener = new FetchListener() {
            @Override
            public void onAdded(@NotNull Download download) {

            }

            @Override
            public void onQueued(@NotNull Download download, boolean b) {
            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {
            }

            @Override
            public void onCompleted(@NotNull Download download) {
                if (BuildConfig.isDebug)
                    Toast.makeText(PopupActivity.this, download.getFile() + "下载成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PopupActivity.this, R.string.downlaod_completed, Toast.LENGTH_SHORT).show();
                mMediaProgress.setVisibility(View.GONE);

                isFetchDownloading = false;
                if (settings.getAutoCancelPopupQ()) {
                    finish();
                }
            }

            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
                Toast.makeText(PopupActivity.this, "Download Failed!", Toast.LENGTH_SHORT).show();
                mMediaProgress.setVisibility(View.GONE);
                isFetchDownloading = false;

                if (settings.getAutoCancelPopupQ()) {
                    finish();
                }
            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {
            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
            }

            @Override
            public void onProgress(@NotNull Download download, long l, long l1) {
            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };
        fetch.addListener(fetchListener);
    }

    //cancel auto completetextview focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof AutoCompleteTextView) {
            View currentFocus = getCurrentFocus();
            int screenCoords[] = new int[2];
            currentFocus.getLocationOnScreen(screenCoords);
            float x = event.getRawX() + currentFocus.getLeft() - screenCoords[0];
            float y = event.getRawY() + currentFocus.getTop() - screenCoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < currentFocus.getLeft() ||
                    x >= currentFocus.getRight() ||
                    y < currentFocus.getTop() ||
                    y > currentFocus.getBottom())) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                v.clearFocus();
            }
        }
        return ret;
    }

    private void initVideoMediaPlayer() {

//        if (mVideoMediaPlayer == null) {
        mVideoMediaPlayer = new MediaPlayer();
//        Trace.e("new MediaPlayer", String.valueOf(mSurfaceView.getVisibility()));


        //安卓6.0以后
        if (Build.VERSION.SDK_INT >= 23) {
            //配置播放器
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            mVideoMediaPlayer.setAudioAttributes(aa);

        } else {
            mVideoMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        mVideoMediaPlayer.setOnVideoSizeChangedListener(
                new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        changeVideoSize();
                    }
                }
        );

        mVideoMediaPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        surfaceHolder = mSurfaceView.getHolder();
                        surfaceHolder.addCallback(
                                new SurfaceHolder.Callback() {
                                    int currentPosition = 0;

                                    @Override
                                    public void surfaceCreated(SurfaceHolder holder) {
                                        mVideoMediaPlayer.setDisplay(holder);
                                    }

                                    @Override
                                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                                            mVideoMediaPlayer.setDisplay(holder);
                                    }

                                    @Override
                                    public void surfaceDestroyed(SurfaceHolder holder) {
//                    mVideoMediaPlayer.release();
                                        if (mVideoMediaPlayer != null && mVideoMediaPlayer.isPlaying()) {
                                            currentPosition = mVideoMediaPlayer.getCurrentPosition();
                                            mVideoMediaPlayer.stop();
                                        }
                                    }

                                });
                        mSurfaceView.setVisibility(View.VISIBLE);
//                            mVideoMediaPlayer.setDisplay(surfaceHolder);
                        mMediaProgress.setVisibility(View.GONE);
                        mVideoMediaPlayer.start();
//                        Trace.e("start", String.valueOf(mVideoMediaPlayer.isPlaying()));
                    }
                }
        );

        mVideoMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mSurfaceView.setVisibility(View.VISIBLE);
                mMediaProgress.setVisibility(View.GONE);
            }
        });

        mVideoMediaPlayer.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mp.reset();
                        Toast.makeText(PopupActivity.this, "Failed to play audio, check your connection.", Toast.LENGTH_SHORT);
                        mSurfaceView.setVisibility(View.GONE);
                        mMediaProgress.setVisibility(View.GONE);
                        return false;
                    }
                }
        );
//        }

        try {
            mMediaProgress.setVisibility(View.GONE);
            mVideoMediaPlayer.reset();
        } catch (IllegalStateException e) {
        }
    }

    private void killVideoMediaPlayer() {
        if (mVideoMediaPlayer != null) {
            try {
                mVideoMediaPlayer.reset();
                mVideoMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //change surfaceView size by lisenting mplayer
    private void changeVideoSize() {
        // 首先取得video的宽和高
        int videoWidth = mVideoMediaPlayer.getVideoWidth();
        int videoHeight = mVideoMediaPlayer.getVideoHeight();
        Trace.e("w, h", String.format("%d, %d", videoWidth, videoHeight));

        float max;
        Display dis = getWindowManager().getDefaultDisplay();

        Point point = new Point();
        dis.getSize(point);
        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            max = (float) 0.8 * point.x / (float) videoWidth / 2;
        } else {
            max = (float) 0.8 * point.x / (float) videoWidth;
//            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
        int nVideoWidth = (int) Math.ceil((float) videoWidth * max);
        int nVideoHeight = (int) Math.ceil((float) videoHeight * max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(nVideoWidth, nVideoHeight));

        //surfaceView始终在屏幕内完整显示
        int[] svPos = new int[2];
        int[] cvToolobarPos = new int[2];
        mSurfaceView.getLocationOnScreen(svPos);
        int svY = svPos[1];
        mCvPopupToolbar.getLocationOnScreen(cvToolobarPos);
        int cvY = cvToolobarPos[1];

        int deviation =svY + nVideoHeight - cvY;

        if (deviation > 0) {
            scrollView.scrollTo(mSurfaceView.getScrollX(), scrollView.getScrollY() + deviation);
        }
    }

    private void StopPlayingAll() {
        PlayAudioManager.stop();
        if (mVideoMediaPlayer != null)
            mVideoMediaPlayer.reset();
        if (mTts != null & mTts.isSpeaking())
            mTts.stop();
    }

    @Override
    public void onDestroy() {
        PlayAudioManager.killAll();
        killVideoMediaPlayer();
//        killTts();
//        if(mTts != null) {
//            mTts.stop();
//            mTts.shutdown();
//        }
        fetch.removeListener(fetchListener);
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    private void startCBService() {
        Intent intent = new Intent(this, CBWatcherService.class);
        startService(intent);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.GONE);
    }

    private void showSearchButton() {
        progressBar.setVisibility(View.GONE);
        btnSearch.setVisibility(View.VISIBLE);
    }

    private void showSurfaceView(boolean show) {

        mSurfaceView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showPronounce(boolean shouldShow) {
        btnPronounce.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    private void showTranslateNormal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtnTranslation.setImageResource(Utils.getResIdFromAttribute(this, R.attr.icon_translate_normal));
        }
    }

    private void showTranslateLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtnTranslation.setImageResource(Utils.getResIdFromAttribute(this, R.attr.icon_translate_wait));
        }
    }

    private void showTranslateDone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtnTranslation.setImageResource(Utils.getResIdFromAttribute(this, R.attr.icon_translate_done));
        }
    }

    private void showTranslationCardView(boolean show) {
        mCardViewTranslation.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSelected(String text) {
        String currentWord = FieldUtil.getSelectedText(bigBangLayout.getLines());
        if (!currentWord.equals(acTextView.getText().toString())) {
            mCurrentKeyWord = currentWord;
            acTextView.setText(currentWord);
            asyncSearch(currentWord);
        }
    }

    @Override
    public void onSearch(String text) {

    }

    @Override
    public void onShare(String text) {

    }

    @Override
    public void onCopy(String text) {

    }

    @Override
    public void onTrans(String text) {

    }

    @Override
    public void onDrag() {

    }

    @Override
    public void onSwitchType(boolean isLocal) {

    }

    @Override
    public void onSwitchSymbol(boolean isShow) {

    }

    @Override
    public void onSwitchSection(boolean isShow) {

    }

    @Override
    public void onDragSelection() {

    }

    @Override
    public void onCancel() {
        acTextView.setText("");
        asyncSearch("");
    }

    void vibarate(int ms) {
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(ms);
    }

    void clearBigbangSelection() {
        for (BigBangLayout.Line line : bigBangLayout.getLines()) {
            List<BigBangLayout.Item> items = line.getItems();
            for (BigBangLayout.Item item : items) {
                if (item.getText().equals(mTargetWord)) {
                    item.setSelected(false);
                }
            }
        }
    }

    private void makeTextViewSelectAndSearch(final TextView textView) {
        textView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Remove the "copy all" option
                menu.removeItem(android.R.id.cut);
                //menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when action mode is first created. The menu supplied
                // will be used to generate action buttons for the action mode

                // Here is an example MenuItem
                menu.add(0, 1, 0, "Definition").setIcon(R.drawable.ic_ali_search);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Called when an action mode is about to be exited and
                // destroyed
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        int min = 0;
                        int max = textView.getText().length();
                        if (textView.isFocused()) {
                            final int selStart = textView.getSelectionStart();
                            final int selEnd = textView.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }
                        // Perform your definition lookup with the selected text
                        final String selectedText = textView.getText().subSequence(min, max).toString();
                        // Finish and close the ActionMode
                        mode.finish();
                        acTextView.setText(selectedText);
                        asyncSearch(selectedText);
                        return true;
                    default:
                        break;
                }
                return false;
            }

        });
    }

    @Override
    public void onInit(int status) {
        mTtsStatus = status;
        if (status == TextToSpeech.SUCCESS) {

            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
                    super.onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);

                }

                @Override
                public void onDone(String utteranceId) {
                }

                @Override
                public void onError(String utteranceId) {
                    Toast.makeText(PopupActivity.this, R.string.play_pronounciation_failed, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStop(String utteranceId, boolean interrupted) {
                    super.onStop(utteranceId, interrupted);
                    mTts.stop();
                    mTts.shutdown();
                }
            });
        } else {
            Trace.e("tts status OnInit", "TextToSpeech is failed!");
        }
    }

    private void playTts(String text, int pronounceIndex) {
        //TextToSpeech的speak方法有两个重载。
        // 执行朗读的方法
        //speak(CharSequence text,int queueMode,Bundle params,String utteranceId);
        // 将朗读的的声音记录成音频文件
        //synthesizeToFile(CharSequence text,Bundle params,File file,String utteranceId);
        //第二个参数queueMode用于指定发音队列模式，两种模式选择
        //（1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
        //（2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，
        //等前面的语音任务执行完了才会执行新的语音任务
        try {
            PronounceManager.SoundInformation infor = PronounceManager.getDictInformationByIndex(pronounceIndex);
//            Trace.w("current locale:", Arrays.asList(infor.getLangAndCountry()).toString());

            String[] temp = infor.getLangAndCountry();
            Locale locale = new Locale(temp[0], temp[1]);
//            Trace.i("playTts()", locale.toString());
            int result = mTts.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Trace.e("error", "This Language is not supported");
//                Trace.e("current Locale", infor.getLocale().toString());
                Toast.makeText(PopupActivity.this, "暂不支持该语言" + infor.getLocale().toString(), Toast.LENGTH_SHORT).show();
            } else {
                mTts.setPitch(1.0f);
                mTts.setSpeechRate(1.0f);
                mTts.speak(text,
                        TextToSpeech.QUEUE_FLUSH,
                        null);
            }
        } catch (Exception e) {
            Trace.e("tts speak()", e.getMessage());
        }
    }

    private void playTts(String text) {
        int lastPronounceLanguageIndex = Settings.getInstance(PopupActivity.this).getLastPronounceLanguage();
        playTts(text, lastPronounceLanguageIndex);
    }

    private void killTts() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
    }

    private String getCacheAudioUrl(String url) {
        try {
//            Trace.e("download url", url);
            //判断是否存在download临时缓存文件，有则删除
            File file = getCacheFile(url);
//            Trace.e("Existing cached File", file.getAbsolutePath() + file.getName());
            if (file.exists()) {
                file.delete();
                Trace.e("mProxy", "is deleted.");
            }

            return mProxy.getProxyUrl(url, true);
        } catch (Exception e) {
            return url;
        }
    }

    private File getCacheFile(String url) {
        File cacheDir = StorageUtils.getIndividualCacheDirectory(PopupActivity.this);
        String fileName = new Md5FileNameGenerator().generate(url) + ".download";
        return new File(cacheDir, fileName);
    }

    private void restoreTtsVoice(String text, String pathOfFile) {
        if (!text.equals("")) {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "record");
            mTts.synthesizeToFile(text, null, new File(pathOfFile), text);
//            Trace.w("tts", "restored mp3 file.");
        }
    }

    private void download(Request request, String saveFilePath) {
        Trace.e("url", request.getUrl());
        Trace.e("downloading", saveFilePath);
        if(!isConnect()) {
            Toast.makeText(this, "无网络，下载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(new File(saveFilePath).exists())) {
            request.setPriority(Priority.HIGH);
            request.setNetworkType(NetworkType.ALL);

            isFetchDownloading = true;
            fetch.enqueue(request,
                    new Func<Request>() {
                        @Override
                        public void call(@NotNull Request result) {
                            mMediaProgress.setVisibility(View.VISIBLE);
                            isFetchDownloading = true;
                        }
                    },
                    new Func<Error>() {
                        @Override
                        public void call(@NotNull Error result) {
                            isFetchDownloading = false;
                        }
                    }
            );
        }
    }


    private String cleanKey(final Definition def) {
        return cleanKey(def, false);
    }

    private String cleanKey(final Definition def, boolean isTranslationIncluded) {
        String content = def.getSpell();
        if(content.equals("")) {
            if (currentDictionary instanceof EudicSentence ||
                    currentDictionary instanceof RenRenCiDianSentence ||
                    currentDictionary instanceof Dub91Sentence ||
                    currentDictionary instanceof EnglishSentenceSet) {
                content = def.getExportElement("英文例句");
            } else if (currentDictionary instanceof Getyarn) {
                content = def.getExportElement("外文例句");
            } else if (currentDictionary instanceof Handian) {
                content = def.getExportElement("字词");
            } else {
                content = def.getExportElementByIndex(0) + ". ";
//            return def.getExportElement("单词").replaceAll("</?[\\w\\W].*?>", "") + ". " + translation;
            }
        }

        if(isTranslationIncluded) {
            if (currentDictionary instanceof EudicSentence ||
                    currentDictionary instanceof RenRenCiDianSentence ||
                    currentDictionary instanceof Dub91Sentence ||
                    currentDictionary instanceof EnglishSentenceSet) {
                content = content + "\n" + def.getExportElement("例句中文");
            } else if(currentDictionary instanceof VocabCom ||
                    currentDictionary instanceof Mnemonic ||
                    currentDictionary instanceof CollinsEnEn ||
                    currentDictionary instanceof DictionaryDotCom ||
                    currentDictionary instanceof BingOxford ||
                    currentDictionary instanceof Collins ||
                    currentDictionary instanceof IdiomDict ||
                    currentDictionary instanceof Ode2 ||
                    currentDictionary instanceof WebsterLearners ||
                    currentDictionary instanceof CustomDictionary)
                content = content + "\n" + def.getExportElement("释义");
        }
        return StringUtil.htmlTagFilter(content);
    }

    private void changePronounceToSpinner(String word) {
        if(word.isEmpty())
            return;

        int type = getTypeCurrentDictionary();
        int index = settings.getRestorePronounceSpinnerIndex(type, currentDictionary.isExistAudioUrl(), toLoadTTS);

        //根据文字判断语言并切换发音spinner
        if (!(currentDictionary.isExistAudioUrl() &&
                (pronounceLanguageSpinner.getSelectedItemPosition() == PronounceManager.getDictInformationSize() - 1))) {

            if(index == -1) {
                index = PronounceManager.getSoundInforIndexByList(type);
            }
            pronounceLanguageSpinner.setSelection(index);
            pickedSentencePronouceLanguageIndex = index;
        }
//        Trace.e("changePronounce",
//                String.format("%s is in %s",
//                        word, (String) pronounceLanguageSpinner.getSelectedItem()));
    }

    private int getTypeCurrentDictionary() {
        int type = currentDictionary.getLanguageType();

        if(acTextView.getText().toString().equals("")) {
            return type;
        }
        if(currentDictionary instanceof DictTango) {
            HashMap<String, Boolean> tangoDictCheckerMap = settings.getTangoDictChckerMap(currentOutputPlan.getPlanName());
            if(!tangoDictCheckerMap.isEmpty()) {
                int resultType =  DictLanguageType.NAN;
                for(String dictName : tangoDictCheckerMap.keySet()) {
                    if(tangoDictCheckerMap.get(dictName)) {
                        int checkedType = (int) Math.pow(2, OutputLocatorPOJO.getOutputlocatorMap().get(dictName).getLangIndex());
                        int wordType = DictLanguageType.getLTIdByWord(acTextView.getText().toString());
                        resultType = checkedType;
                        if(checkedType == wordType) {
                            break;
                        }
                    }
                }
                type = resultType;
            } else {
                type = DictLanguageType.getLTIdByWord(acTextView.getText().toString());
            }
        } else if(currentDictionary.getLanguageType() == DictLanguageType.ALL)
            type = DictLanguageType.getLTIdByWord(acTextView.getText().toString());

        return currentDictionary.getLanguageType() | type;
    }

    private boolean isConnect() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null&& info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        // TODO: handle exception
            Trace.e("isConnect",e.toString());
        }
        return false;
    }

    private void startAnkiDroid() {
        if (AnkiDroidHelper.isApiAvailable(MyApplication.getContext()) && !MyApplication.getAnkiDroid().isAnkiDroidRunning()) {
            MyApplication.getAnkiDroid().startAnkiDroid();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(SystemUtils.isBackActivity(PopupActivity.this, Constant.ANKIHELPER_PACKAGE_NAME)) {
                                            Intent intent = PopupActivity.this.getIntent();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            handler.removeCallbacks(this);
                                        }
                                        else {
                                            handler.postDelayed(this, 200);
                                        }
                                    }
                                },
                    200);
        }
    }

    private String convertInline(String text) {
        String regex = "(\\${1,2})(.+?)(\\${1,2})";
        String latex = text.replaceAll(regex, "\\\\($2\\\\)");
        return latex;
    }
}
