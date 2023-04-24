package com.mmjang.ankihelper.ui.floating.screenshot;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

//import com.google.android.material.textfield.TextInputEditText;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.domain.FinishActivityManager;
import com.mmjang.ankihelper.ui.intelligence.TessOcr;
import com.mmjang.ankihelper.ui.intelligence.mathpix.MathpixOcr;
import com.mmjang.ankihelper.ui.intelligence.mlkit.MlKitOcr;
import com.mmjang.ankihelper.ui.popup.PopupActivity;
import com.mmjang.ankihelper.ui.share.SharePopupWindow;
import com.mmjang.ankihelper.ui.widget.button.MLLabel;
import com.mmjang.ankihelper.ui.widget.button.MLLabelButton;
import com.mmjang.ankihelper.ui.widget.MathxView;
import com.mmjang.ankihelper.ui.widget.NoteEditText;
import com.mmjang.ankihelper.ui.widget.button.PerformEditButton;
import com.mmjang.ankihelper.util.BuildConfig;
import com.mmjang.ankihelper.util.ColorThemeUtils;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.DialogUtil;
import com.mmjang.ankihelper.util.FileUtils;
import com.mmjang.ankihelper.util.ImageUtils;
import com.mmjang.ankihelper.util.PerformEdit;
import com.mmjang.ankihelper.util.RegexUtil;
import com.mmjang.ankihelper.util.ScreenUtils;
import com.mmjang.ankihelper.util.ShareUtil;
import com.mmjang.ankihelper.util.StorageUtils;
import com.mmjang.ankihelper.util.ToastUtil;
import com.mmjang.ankihelper.util.Trace;
import com.mmjang.ankihelper.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ss on 2022/10/23.
 */

public class CaptureResultActivity extends AppCompatActivity {//implements View.OnTouchListener {
    private ImageView capturedImage;
    private MathxView mathPreview;
    private Bitmap bitmap;
    private ImageView ivMlKit, ivTesseract, ivMathpix, ivSearch, ivCopy, ivShare, ivMode;
    private NoteEditText ocrResult;
    private RelativeLayout ocrResultRL;
    private TextView tilHint;
    private LinearLayout tagArrAWLL, performEditArrLL, containerLl, viewerOnContainerLl, editorOnContainerLl;
    int alpha = 100;
    int lastPickedColor;
    boolean hasContent = false;
    Settings settings;
    Thread mPreThread;
    boolean isRecognized;
    String mText;
    int preMlKitOcrLangCheckedIndex;
    int tesseractOcrModeIndex = 0;
    private int currentModeIndex = 0;
    int heightDpChanged = 0;
    int widthDpChanged = 0;
    private static final int NORMAL_HEIGHT = 2280;


    private void initWindow() {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
        localLayoutParams.width = ((int) (localDisplayMetrics.widthPixels * 0.99D));
        localLayoutParams.gravity = Gravity.CENTER;
        localLayoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(localLayoutParams);
        getWindow().setGravity(17);
        getWindow().getAttributes().windowAnimations = R.anim.anim_scale_in;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ColorThemeUtils.setColorTheme(CaptureResultActivity.this, Constant.StyleBaseTheme.BigBangTheme);
        super.onCreate(savedInstanceState);
        init();
        updateUI(settings.get(Settings.CAPTURE_RESULT_EDIT_MODE, 0));

        FinishActivityManager.getManager().finishActivity(PopupActivity.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(preMlKitOcrLangCheckedIndex != settings.getMlKitOcrLangCheckedIndex())
            isRecognized = false;
        if(hasFocus && !isRecognized) {
            RecognizeTextByMlkitOcr2(settings.getMlKitOcrLangCheckedIndex());
            isRecognized = true;
        }
    }

    @SuppressLint("ResourceType")
    private void init() {
        Trace.d("captureResult",  "init");
        settings = Settings.getInstance(this);
//        if (settings.getPinkThemeQ()) {
//            setTheme(R.style.BigBangThemePink);
//        } else {
//            setTheme(R.style.BigBangTheme);
//        }
        ColorThemeUtils.setColorTheme(CaptureResultActivity.this, Constant.StyleBaseTheme.BigBangTheme);

        alpha = 100;
        lastPickedColor = Color.parseColor("#94a4bb");
        currentModeIndex = settings.get(Settings.CAPTURE_RESULT_EDIT_MODE, 0);

        CardView cardView = new CardView(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_capture_result, null, false);
        cardView.setRadius(ScreenUtils.dp2px(getApplicationContext(), 10));
        cardView.setElevation(ScreenUtils.dp2px(getApplicationContext(), 3));
        cardView.setTranslationZ(ScreenUtils.dp2px(getApplicationContext(), 3));

        cardView.setUseCompatPadding(true);
//        int value = (int) ((alpha / 100.0f) * 255);
//        cardView.setCardBackgroundColor(Color.argb(value, Color.red(lastPickedColor), Color.green(lastPickedColor), Color.blue(lastPickedColor)));

//        //set bg color in theme.
//        int[] attrArr = {R.attr.color_popup_background};
//        TypedArray typedArr = obtainStyledAttributes(attrArr);
//        cardView.setCardBackgroundColor(typedArr.getColor(0,0xFF94a4bb));

        cardView.addView(view);

        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.transparent));
        setContentView(cardView);
        initWindow();

        Intent intent = getIntent();
        String fileName = intent.getStringExtra(ScreenCapture.FILE_NAME);
        if (fileName != null) {
//            Trace.d("captureResult",  "fileName is null");
//            // ToastUtil.show("R.string.screen_capture_fail");
//            finish();
//            return;
            Trace.d("CaptureResultActivity", fileName);
            File capturedFile = new File(fileName);
            if (capturedFile.exists()) {
                bitmap = BitmapFactory.decodeFile(fileName);
            } else {
                Trace.d("captureResult", "captureedFile is not exists.");
                // ToastUtil.show("R.string.screen_capture_fail");
                finish();
                return;
            }
        } else {
            String action=intent.getAction();
            String type=intent.getType();
            Trace.i(action + "\n" + type);
            if(action.equals(Intent.ACTION_SEND) && type.startsWith("image/")){
                Uri uri=intent.getParcelableExtra(Intent.EXTRA_STREAM);
                //接收多张图片
                // ArrayList<Uri> uris=intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if(uri!=null){
                    bitmap = ImageUtils.ImageSizeCompress(this, uri);
                }
            } else {
                finish();
                return;
            }
        }

        isRecognized = false;
        mText = "";
        preMlKitOcrLangCheckedIndex = settings.getMlKitOcrLangCheckedIndex();
        ocrResult = (NoteEditText) findViewById(R.id.ocr_result);
        ocrResult.setBackgroundResource(R.drawable.edit_background);
        ocrResultRL = (RelativeLayout) findViewById(R.id.ocr_result_rl);
        containerLl = (LinearLayout) findViewById(R.id.layout_container);
        viewerOnContainerLl = (LinearLayout) findViewById(R.id.layout_container_view);
        editorOnContainerLl = (LinearLayout) findViewById(R.id.layout_container_edit);
        tilHint = (TextView) findViewById(R.id.ocr_result_key);
        capturedImage = (ImageView) findViewById(R.id.captured_pic);
        ivShare = (ImageView) findViewById(R.id.share);
        ivCopy = (ImageView) findViewById(R.id.copy);
//        ivOcr = (ImageView) findViewById(R.id.recognize);
//        ivOcr22 = (ImageView) findViewById(R.id.recognize22);
        ivMlKit = (ImageView) findViewById(R.id.ocr_mlkit);
        ivTesseract = (ImageView) findViewById(R.id.ocr_tesseract);
        ivMathpix = (ImageView) findViewById(R.id.ocr_mathpix);
        ivSearch = (ImageView) findViewById(R.id.search);
        ivMode = (ImageView) findViewById(R.id.mode);
        //MathView
        mathPreview = (MathxView) findViewById(R.id.mathPreview);
        mathPreview.setInitialScale(200);
        mathPreview.setClickable(true);
        mathPreview.setVerticalScrollBarEnabled(true);
        mathPreview.setHorizontalScrollBarEnabled(false);
        mathPreview.getSettings().setDisplayZoomControls(true);
        mathPreview.getSettings().setBuiltInZoomControls(false);
        mathPreview.getSettings().setSupportZoom(true);

        ocrResultRL.setVisibility(View.GONE);

        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();

        //获取屏幕的宽高
        heightDpChanged = ScreenUtils.getAvailableScreenHeight(CaptureResultActivity.this);
        widthDpChanged = ScreenUtils.getScreenWidth(CaptureResultActivity.this);

//        ToastUtil.show("height: " + heightDpChanged);
        //根据屏幕旋转状态设置布局参数
        //横屏状态
        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            heightDpChanged = heightDpChanged * 25 / 100 * heightDpChanged / NORMAL_HEIGHT;
            ocrResult.setMaxHeight(heightDpChanged * 2);
//            ocrResult.setMaxHeight(heightDpChanged * 97 / 200);

            LinearLayout.LayoutParams params =  (LinearLayout.LayoutParams) viewerOnContainerLl.getLayoutParams();
            params.width = widthDpChanged / 2;
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            viewerOnContainerLl.setLayoutParams(params);

            containerLl.setOrientation(LinearLayout.HORIZONTAL);
        //竖屏状态
        } else {
            heightDpChanged = heightDpChanged * 15 / 100 * heightDpChanged / NORMAL_HEIGHT;
            ocrResult.setMaxHeight(heightDpChanged * 2);

            containerLl.setOrientation(LinearLayout.VERTICAL);
        }

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
        if (bitmap.getHeight() > localDisplayMetrics.heightPixels * 2 / 3 || 1.0 * bitmap.getHeight() / bitmap.getWidth() >= 1.2) {
//            heightDpChanged = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDpChanged, getResources().getDisplayMetrics());

            viewerOnContainerLl.setOrientation(LinearLayout.HORIZONTAL);

            capturedImage.setMaxWidth(localDisplayMetrics.widthPixels / 2);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) capturedImage.getLayoutParams();
            if (bitmap.getWidth() > localDisplayMetrics.widthPixels / 2) {
                layoutParams.width = bitmap.getWidth() * 2 / 5;
                layoutParams.height = bitmap.getHeight() * 2 / 5;
            } else {
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.height = heightDpChanged * 2;
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            }
            capturedImage.setMaxHeight(heightDpChanged);
            capturedImage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) ocrResultRL.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            ocrResultRL.setLayoutParams(layoutParams);

            mathPreview.setMaxHeight(heightDpChanged*2 + ScreenUtils.dp2px(getApplicationContext(),20));
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mathPreview.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            mathPreview.setMaxHeight(heightDpChanged*2 + ScreenUtils.dp2px(getApplicationContext(),20));
            mathPreview.setLayoutParams(layoutParams);
        }

        //start--标签按钮和和撤回按钮

//        params.gravity = Gravity.CENTER_HORIZONTAL;
//        params.leftMargin = ScreenUtils.dp2px(getApplicationContext(),2);
//        params.rightMargin = ScreenUtils.dp2px(getApplicationContext(),2);
//        params.height = ScreenUtils.dp2px(getApplicationContext(),28);
//        params.width = ScreenUtils.dp2px(getApplicationContext(),36);

        tagArrAWLL = findViewById(R.id.layout_tag_buttons);
        MLLabel[] mixTagArr = ArrayUtils.concat(Constant.latexTagArr, Constant.htmlTagArr);
        for(MLLabel tag : mixTagArr) {
            final MLLabelButton btn =  new MLLabelButton(CaptureResultActivity.this);
            btn.setupHtmlTag(ocrResult, tag);
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

        performEditArrLL = findViewById(R.id.layout_perform_edit_buttons);
        PerformEdit performEdit = new PerformEdit(ocrResult);
        for(PerformEditButton.ActionEnum action : PerformEditButton.ActionEnum.values()) {
            final PerformEditButton btn = new PerformEditButton(CaptureResultActivity.this);
            btn.setupPerformEditAction(action, performEdit);
//            View.OnClickListener onClickListener = null;
//            switch (labelEnum) {
//                case redo:
//                    btn.setBackground(ContextCompat.getDrawable(
//                            CaptureResultActivity.this,
//                            Utils.getResIdFromAttribute(CaptureResultActivity.this, R.attr.icon_redo)));
//                    onClickListener = v-> performEdit.redo();
//                    break;
//                case undo:
//                    btn.setBackground(ContextCompat.getDrawable(
//                            CaptureResultActivity.this,
//                            Utils.getResIdFromAttribute(CaptureResultActivity.this, R.attr.icon_undo)));
//                    onClickListener = v-> performEdit.undo();
//            }
//            btn.setOnClickListener(onClickListener);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.leftMargin = ScreenUtils.dp2px(getApplicationContext(),2);
            params.rightMargin = ScreenUtils.dp2px(getApplicationContext(),2);
            params.height = ScreenUtils.dp2px(getApplicationContext(),24);
            params.width = ScreenUtils.dp2px(getApplicationContext(),24);
            performEditArrLL.addView(btn, params);
        }
        //end

        capturedImage.setImageBitmap(bitmap);

        capturedImage.setOnClickListener(
                v -> {
                    currentModeIndex = currentModeIndex % Constant.Mode.values().length;
                    switch (Constant.Mode.values()[currentModeIndex]) {
                        case LATEX_MODE:
                            capturedImage.setVisibility(View.GONE);
                            ocrResultRL.setVisibility(View.VISIBLE);
                            break;
//                        case NORMAL_MODE:
//                        default:
//                            RecognizeTextByMlkitOcr2(settings.getMlKitOcrLangCheckedIndex());
                    }
                }
        );

        ocrResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                UrlCountUtil.onEvent(UrlCountUtil.CLICK_CAPTURERESULT_OCRRESULT);
//                if (!TextUtils.isEmpty(ocrResult.getText())) {
//                    Intent intent = new Intent(CaptureResultActivity.this, BigBangActivity.class);
//                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(BigBangActivity.TO_SPLIT_STR, ocrResult.getText().toString());
//                    startActivity(intent);
//                    finish();
//                }
                return true;
            }

        });

        ocrResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String latex = s.toString().trim();
//                if(!latex.equals("")) {
//                    mathPreview.setVisibility(View.VISIBLE);
                    String change = convertInline(latex);
                    if(!mathPreview.getText().equals(change)) {
                        latex = change;
//                        ToastUtil.show(mathPreview.getmScrollY() +"");
                        mathPreview.setText(latex);
                    }
//					latex = String.format("$$%s$$", latex);

//                }
//                else
//                    mathPreview.setVisibility(View.GONE);
            }
        });

        ocrResult.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.hasFocus()) {
//                    mathPreview.setMaxHeight(heightDpChanged * 2);
//                    if(mathPreview.getContentHeight()>=mathPreview.getScrollY()+heightDpChanged)
//                        mathPreview.setmScrollY(mathPreview.getScrollY()+heightDpChanged);
                } else {
//                    mathPreview.setMaxHeight(heightDpChanged * 2);
                    if(mathPreview.getScrollY()-heightDpChanged>=0)
                        mathPreview.setmScrollY(mathPreview.getScrollY()-heightDpChanged);
                }
            }
        });
        mathPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((MathxView) v).setmScrollY(v.getScrollY());
                        
                        float percent =  (((float) v.getScrollY())/ (mathPreview.getContentHeight() * mathPreview.getScale() - v.getHeight()));
                        percent = percent >=1 ? 1 : percent;
//                        ToastUtil.show(String.format(
//                                "v.getScrollY() %d\nmathPreview.getContentHeight() %f\npercent %f",
//                                v.getScrollY(),
//                                mathPreview.getContentHeight() * mathPreview.getScale() - v.getHeight(),
//                                percent));
                        ocrResult.scrollTo(0, (int) (percent * ocrResult.getLineCount() * ocrResult.getLineHeight()));
                }
                return false;
            }
        });

        //mlkit按钮
        ivMlKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ocr自动识别
                RecognizeTextByMlkitOcr2(settings.getMlKitOcrLangCheckedIndex());
            }

        });

        ivMlKit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogUtil.showMlKitOcrSettingDialog(CaptureResultActivity.this);
                return false;
            }
        });

        //tesseract按钮
        ivTesseract.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ocr自动识别
//                        capturedImage.setImageBitmap(bitmap);
                        switch(tesseractOcrModeIndex%3) {
                            case 0:
                                RecognizeTextByTesseractOcr(settings.getOcrSelectedLang());
                                break;
                            case 1:
                                RecognizeTextByTesseractOcr2(settings.getOcrSelectedLang());
                                break;
                            case 2:
                                RecognizeTextByTesseractOcr3(settings.getOcrSelectedLang());
                                break;
                        }
                        tesseractOcrModeIndex++;
                    }
                }
        );

        ivTesseract.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        DialogUtil.showTesseractSettingDialog(CaptureResultActivity.this);
                        return false;
                    }
                }
        );

        //编辑模式切换
        ivMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });

        ivMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogUtil.captureEditModeSettingDialog(CaptureResultActivity.this);
                return true;
            }
        });

        ivMathpix.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(CaptureResultActivity.this)
                                .setTitle(R.string.confirm_use_mathpix)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        recognizeLatexByMathpix();

                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                }
        );
        //popup按钮
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ocrResult.getText().toString().equals("")) {
                    ShareUtil.shareText(getApplicationContext(), ocrResult.getText().toString());
                    finish();
                }

            }
        });

        //复制按钮
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text = ocrResult.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("plans string", text);
                    clipboard.setPrimaryClip(clip);
                    ToastUtil.show("复制成功");
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show("复制失败");
                }
            }
        });

        //分享按钮
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/", format.format(new Date()) + ".jpg");
                    File file = createCacheFile();
                    file.getParentFile().mkdirs();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                    shareMsg("分享给", "截图", "来自bigbang的截图", file.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        ivShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String text = ocrResult.getText().toString();
                if(!text.equals("")) {
                    LinearLayout mLayoutRoot;
                    mLayoutRoot = new LinearLayout(view.getContext());
                    //实例化分享窗口
                    SharePopupWindow spw = new SharePopupWindow(CaptureResultActivity.this, text);
                    ;
                    // 显示窗口
                    spw.showAtLocation(mLayoutRoot, Gravity.BOTTOM, 0, 0);
                }
                return true;
            }
        });

//        ivOcr.setOnClickListener(v -> {
//            //ocr自动识别
//            capturedImage.setImageBitmap(bitmap);
//            RecognizeTextByTesseractOcr(settings.getOcrSelectedLang());
//        });
//
//        ivOcr.setOnLongClickListener(
//                v -> {
//                    RecognizeTextByTesseractOcr2(settings.getOcrSelectedLang());
//                    return true;
//                });
//
//        ivOcr22.setOnClickListener(
//                v -> RecognizeTextByTesseractOcr2(settings.getOcrSelectedLang())
//        );
    }

    /**
     * 分享功能
     *
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
            intent.putExtra(Constant.INTENT_ANKIHELPER_ACTION, true);
        } else {
            File file = new File(imgPath);
            if (file != null && file.exists() && file.isFile()) {
                intent.setType("image/jpg");
                Uri uri = FileProvider.getUriForFile(MyApplication.getContext(), getPackageName() + ".provider", file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
//        if ((view.getId() == R.id.ocr_result && canVerticalScroll(ocrResult)) ||
//                (view.getId() == R.id.mathPreview && canVerticalScroll(mathPreview))) {
//            view.getParent().requestDisallowInterceptTouchEvent(true);
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                view.getParent().requestDisallowInterceptTouchEvent(false);
//            }
//        }
//
//        return false;
//    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private boolean canVerticalScroll(WebView webView) {
        //滚动的距离
        int scrollY = webView.getScrollY();
        //控件内容的总高度
        int scrollRange = webView.getHeight();
        //控件实际显示的高度
        int scrollExtent = webView.getHeight() - webView.getPaddingTop() -webView.getPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private File createCacheFile() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        File cacheDir = StorageUtils.getIndividualCacheDirectory(MyApplication.getContext());
        String fileName = format.format(new Date()) + ".jpg";
        return new File(cacheDir, fileName);
    }

    private void RecognizeTextByTesseractOcr2(String lang) {
        if(lang.equals(""))
            return;

        TessOcr ocr = new TessOcr(this);
        ocr.setLanguageText(lang);

        if (mPreThread != null && mPreThread.getState() == Thread.State.RUNNABLE) {
            mPreThread.interrupt();
        }
        mPreThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ocrHint(true);
                Bitmap xBitmap = bitmap;
                mText = textFormat(ocr.getText(xBitmap));

                Bitmap finalXBitmap = xBitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ocrHint(false);
                        ocrResult.setText(mText);
                        if(BuildConfig.isDebug)
                            capturedImage.setImageBitmap(finalXBitmap);
                    }
                });
            }
        });
        mPreThread.start();

    }


    private void RecognizeTextByTesseractOcr(String lang) {
        if(lang.equals(""))
            return;

        TessOcr ocr = new TessOcr(this);
        ocr.setLanguageText(lang);

        if (mPreThread != null && mPreThread.getState() == Thread.State.RUNNABLE) {
            mPreThread.interrupt();
        }
        mPreThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ocrHint(true);
                Bitmap xBitmap = bitmap;
                switch(1) {
                    case 1:
                        xBitmap = FileUtils.binaryzation(bitmap, 240);
                        xBitmap = FileUtils.convertGray(xBitmap);
                        mText = ocr.getText(xBitmap);
//                        ocr.getText(xBitmap, s->{ocrResult.setText(s); return s;});
                        if (!mText.equals(""))
                            break;
                    case 2:
                        xBitmap = FileUtils.convertGray(bitmap);
                        xBitmap = FileUtils.binaryzation(xBitmap, 110);

                        mText = ocr.getText(xBitmap);
//                        ocr.getText(xBitmap, s->{ocrResult.setText(s); return s;});
                }
                mText = textFormat(mText);

                Bitmap finalXBitmap = xBitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ocrHint(false);
                        ocrResult.setText(mText);
//                        ocrResultRL.setVisibility(View.VISIBLE);
                        if(BuildConfig.isDebug)
                            capturedImage.setImageBitmap(finalXBitmap);
                    }
                });

            }
        });
        mPreThread.start();

    }


    private void RecognizeTextByTesseractOcr3(String lang) {
        if(!hasContent)
            return;

        if(lang.equals(""))
            return;

        TessOcr ocr = new TessOcr(this);
        ocr.setLanguageText(lang);

        if (mPreThread != null && mPreThread.getState() == Thread.State.RUNNABLE)
            mPreThread.interrupt();
        mPreThread = new Thread(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void run() {
                        ocrHint(true);
                        Bitmap xBitmap = bitmap;
                        xBitmap = FileUtils.convertGray(xBitmap);
                        xBitmap = FileUtils.binaryzation(xBitmap, 100);
//                        xBitmap = FileUtils.navieRemoveNoise(xBitmap);
//                        xBitmap = FileUtils.convertGray(xBitmap);
                        xBitmap = ImageUtils.removedLines(xBitmap);
                        mText = ocr.getText(xBitmap);
                        mText = textFormat(mText);
                        Bitmap finalXBitmap = xBitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ocr.getText(bitmap, s->{ ocrResult.setText(s);return s;});
                                ocrHint(false);
                                ocrResult.setText(mText);
//                                ocrResultRL.setVisibility(View.VISIBLE);
                                if(BuildConfig.isDebug)
                                    capturedImage.setImageBitmap(finalXBitmap);
                            }
                        });
                    }
                }
        );
        mPreThread.start();
    }
//
//    private void asynTessOcrBinaryesszationAndGray() {
//        //ocr自动识别
//        Thread thread = new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        Bitmap xBitmap = null;
//                        String text = "";
//
//                        switch(1) {
//                            case 1:
//                                xBitmap = FileUtils.binaryzation(bitmap, 240);
//                                xBitmap = FileUtils.convertGray(xBitmap);
//                                text = processOcr(xBitmap);
//                                if (!text.equals(""))
//                                    break;
//                            case 2:
//                                xBitmap = bitmap;
//                                text = processOcr(xBitmap);
//                        }
//                        ocrResult.setText(text);
//                        ocrResultRL.setVisibility(View.VISIBLE);
//                        capturedImage.setImageBitmap(xBitmap);
//                    }
//                }
//        );
//        thread.run();
//    }


    private void RecognizeTextByMlkitOcr2(int index) {
        MlKitOcr ocr = new MlKitOcr(this);
        ocr.setLanguageText(MlKitOcr.LanguageText.values()[index].name());

        if (mPreThread != null && mPreThread.getState() == Thread.State.RUNNABLE)
            mPreThread.interrupt();
        mPreThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        ocrHint(true);
                        ocr.getText(bitmap, s -> {
                            s = textFormat(s);
                            ocrResult.setText(s);
                            if(s != null && !s.equals(""))
                                hasContent = true;
                            else
                                hasContent = false;
                            ocrHint(false);
//                            ocrResultRL.setVisibility(View.VISIBLE);
                            if(BuildConfig.isDebug)
                                capturedImage.setImageBitmap(bitmap);
                            return s;
                        });
                    }
                }
            );
        mPreThread.start();
    }


    private void recognizeLatexByMathpix() {
        MathpixOcr ocr = new MathpixOcr(this);

        if (mPreThread != null && mPreThread.getState() == Thread.State.RUNNABLE)
            mPreThread.interrupt();
        mPreThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        ocrHint(true);
                        ocr.getText(bitmap, s -> {
                            s = textFormat(s);
                            ocrResult.setText(s);
                            if(s != null && !s.equals(""))
                                hasContent = true;
                            else
                                hasContent = false;
                            ocrHint(false);
//                            ocrResultRL.setVisibility(View.VISIBLE);
                            if(BuildConfig.isDebug)
                                capturedImage.setImageBitmap(bitmap);
                            return s;
                        });
                    }
                }
        );
        mPreThread.start();
    }
//    private void RecognizeTextByMlkitOcr(int index) {
//        // [START get_detector_default]
////        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//        TextRecognizer recognizer;
//        switch (index) {
//            case 0:
//                recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
//                break;
//            case 1:
//                recognizer = TextRecognition.getClient(new TextRecognizerOptions.Builder().build());
//                break;
//            case 2:
//                recognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
//                break;
//            case 3:
//                recognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());
//                break;
//            case 4:
//                recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
//                break;
//            default:
//                recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
//        }
//        // [START run_detector]
//        InputImage inputImg = InputImage.fromBitmap(bitmap, 0);
//
//        Task<Text> result =
//                recognizer.process(inputImg)
//                        .addOnSuccessListener(new OnSuccessListener<Text>() {
//                            @Override
//                            public void onSuccess(Text visionText) {
//                                ocrResult.setText(visionText.getText());
//                                ocrResultRL.setVisibility(View.VISIBLE);
//                                capturedImage.setImageBitmap(bitmap);
//                                recognizer.close();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Task failed with an exception
//                                        // ...
//                                        recognizer.close();
//                                    }
//                                });
//        // [END run_detector]
//    }

    static int counter = 0;
    private void ocrHint(boolean isProcessing) {
        if(isProcessing)
            counter += 1;
        else
            counter -= 1;

        if(counter > 0)
            tilHint.setHint(R.string.til_ocr_hint_processing);
        else
            tilHint.setHint(R.string.til_ocr_hint_result);
    }
//
//    private String textFormat(String s) {
//
//        // latin
//        if (RegexUtil.isSpecialWord(s.substring(0, 1))) {
//            s = s.replaceAll("([a-zA-ZÀ-ÿ])(\n)([a-zA-ZÀ-ÿ])","$1 $3");
//            return s;
//        }
//        //俄语
//        else if (RegexUtil.isRussian(s.substring(0, 1))) {
//            s = s.replaceAll("([\u0400-\u052f])(\n)([\u0400-\u052f])","$1 $3");
//            return s;
//        }
//        // 泰
//        else if (RegexUtil.isThai(s.substring(0, 1))) {
//            s = s.replaceAll("([\u0e00-\u0e7f])(\n)([\u0e00-\u0e7f])","$1 $3");
//            return s;
//        } else {
//            //中文、朝鲜、日等
//            s = s.replaceAll("([^。?!.?!])(\n)([^。?!.?!])","$1$3");
//            return s;
//        }
//    }

    private  String textFormat(String s) {
        String regex = "\\w\n+\\w";
        try {
            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(s);
            StringBuffer sb = new StringBuffer();
//            for (int i = 0; m.find(); i++) {
            while(m.find()) {
                String word = s.substring(m.start(), m.end());
                String c = word.substring(0, 1);
                // latin
                if (RegexUtil.isSpecialWord(c)) {
                    word = word.replaceAll("([a-zA-ZÀ-ÿ])(\n+)([a-zA-ZÀ-ÿ])","$1 $3");
                }
                //俄语
                else if (RegexUtil.isRussian(c)) {
                    word = word.replaceAll("([\u0400-\u052f])(\n+)([\u0400-\u052f])","$1 $3");
                }
                // 泰
                else if (RegexUtil.isThai(c)) {
                    word = word.replaceAll("([\u0e00-\u0e7f])(\n+)([\u0e00-\u0e7f])","$1 $3");
                } else {
                    //中文、朝鲜、日等
                    word = word.replaceAll("([^。?!.?!])(\n+)([^。?!.?!])","$1$3");
                }

                m.appendReplacement(sb, word);
            }
            return m.appendTail(sb).toString();
        } catch (Exception e) {
            return s;
        }
    }

    //切换模式后刷新界面
    private void updateUI(int modeIndex) {
        switch (Constant.Mode.values()[modeIndex]) {
            case LATEX_MODE:
                ivMlKit.setVisibility(View.GONE);
                ivTesseract.setVisibility(View.GONE);
                ivMathpix.setVisibility(View.VISIBLE);
                ocrResultRL.setVisibility(View.VISIBLE);
                tagArrAWLL.setVisibility(View.VISIBLE);
                break;
            case NORMAL_MODE:
            default:
                ivMlKit.setVisibility(View.VISIBLE);
                ivTesseract.setVisibility(View.VISIBLE);
                ivMathpix.setVisibility(View.GONE);
                ocrResultRL.setVisibility(View.GONE);
                capturedImage.setVisibility(View.VISIBLE);
                tagArrAWLL.setVisibility(View.GONE);
        }

    }

    private void switchMode() {
        currentModeIndex = ++currentModeIndex % Constant.Mode.values().length;
        updateUI(currentModeIndex);
    }

    private String convertInline(String text) {
        String regex = "(\\${1,2})(.+?)(\\${1,2})";
        String latex = text.replaceAll(regex, "\\\\($2\\\\)");
        return latex;
    }
}
