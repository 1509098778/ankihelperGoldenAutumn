package com.mmjang.ankihelper.ui.popup;


import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.ui.floating.assist.AssistFloatWindow;
import com.mmjang.ankihelper.util.ColorThemeUtils;
import com.mmjang.ankihelper.util.DialogUtil;
import com.mmjang.ankihelper.util.Trace;
import com.mmjang.ankihelper.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.ui.popup
 * @ClassName: PopupSettingActivity
 * @Description: java类作用描述
 * @Author: 唐朝
 * @CreateDate: 2022/5/19 9:57 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/5/19 9:57 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PopupSettingActivity extends AppCompatActivity {
    private SeekBar seekBarFontSize;
    private TextView textViewSeekBar;
    private TextView textViewClearVideoCache;
    private SwitchCompat switchDeck;
    private SwitchCompat switchIgnoreDeck;
    private SwitchCompat switchAutomaticTranslation;
//    private SwitchCompat switchCopyMarked;
    private SwitchCompat switchSymbol;
    private SwitchCompat switchScrollBottom;
    private SwitchCompat switchSearch;
    private SwitchCompat switchNote;
    private SwitchCompat switchTag;
    private SwitchCompat switchCopy;

    int number = 0;
    int alpha = 100;
    private String STR_SET_SIZE;//"BigBang字体大小: %dsp";
    private static final int REQUEST_OVERLAYS_WINDOW = 2;
    private static final int REQUEST_ACCESSIBILITY_SETTINGS = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(Settings.getInstance(this).getPinkThemeQ()){
//            setTheme(R.style.AppThemePink);
//        }else{
//            setTheme(R.style.AppTheme);
//        }
        ColorThemeUtils.setColorTheme(PopupSettingActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewSeekBar = (TextView) findViewById(R.id.tv_bigbing_font_size);
        textViewClearVideoCache = (TextView) findViewById(R.id.tv_video_cache_clear);
        seekBarFontSize = (SeekBar) findViewById(R.id.sb_bing_font_size);
        switchDeck = (SwitchCompat) findViewById(R.id.switch_select_deck);
        switchIgnoreDeck = (SwitchCompat) findViewById(R.id.switch_ignore_deck_scheme);
        switchAutomaticTranslation = (SwitchCompat) findViewById(R.id.switch_automatic_translation);
//        switchCopyMarked = (SwitchCompat) findViewById(R.id.switch_copy_marked);
        switchSymbol = (SwitchCompat) findViewById(R.id.switch_select_symbol);
        switchScrollBottom = (SwitchCompat) findViewById(R.id.switch_scroll_bottom);
        switchSearch = (SwitchCompat) findViewById(R.id.switch_search);
        switchNote = (SwitchCompat) findViewById(R.id.switch_note);
        switchTag = (SwitchCompat) findViewById(R.id.switch_tag);
        switchCopy = (SwitchCompat) findViewById(R.id.switch_copy);

        STR_SET_SIZE = this.getResources().getString(R.string.tv_popup_font_size);


        //设置最大值(设置不了最小值)
        seekBarFontSize.setMax(36);
        //设置初始值
        Settings settings = Settings.getInstance(getApplicationContext());
        alpha  = settings.getFloatingButtonAlpha();
        number = settings.getPopupFontSize();
        boolean isDeckEnable = settings.getPopupSpinnerDeckEnable();
        switchDeck.setChecked(isDeckEnable);
        switchIgnoreDeck.setEnabled(isDeckEnable);
        switchIgnoreDeck.setChecked(settings.getPopupIgnoreDeckScheme());
        switchAutomaticTranslation.setChecked(settings.getPopupToolbarAutomaticTranslationEnable());
//        switchCopyMarked.setChecked(settings.get(Settings.COPY_MARKED_TEXT, false));
        switchSymbol.setChecked(settings.getPopupSwitchSymbolSelection());
        switchScrollBottom.setChecked(settings.get(Settings.POPUP_SWITCH_SCROLL_BOTTOM, false));
        switchSearch.setChecked(settings.getPopupToolbarSearchEnable());
        switchNote.setChecked(settings.getPopupToolbarNoteEnable());
        switchTag.setChecked(settings.getPopupToolbarTagEnable());
        switchCopy.setChecked(settings.get(Settings.POPUP_SWITCH_COPY, false));

        textViewSeekBar.setTextSize(number);
        seekBarFontSize.setProgress(number);
        textViewSeekBar.setText(String.format(STR_SET_SIZE, seekBarFontSize.getProgress()));

        //设置不可滑动
//      seekBarMin.setEnabled(false);
        seekBarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //改变数值
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                number = progress;
                settings.setPopupTextSize(number);
                textViewSeekBar.setTextSize(settings.getPopupFontSize());
                textViewSeekBar.setText(String.format(STR_SET_SIZE, number));

                //如果需要设置最小值，如下 (注：上面设置最大值现对应要减10：seekBar.setMax(70-10);)
//                progress += 10;
//                textView.setText(progress + "");
            }

            //开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(),"从"+number+"开始滑动",Toast.LENGTH_SHORT).show();
            }

            //停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(),"滑动到："+number,Toast.LENGTH_SHORT).show();
            }
        });

        switchDeck.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupSpinnerDeckEnable(isChecked);
                        switchIgnoreDeck.setEnabled(isChecked);
                    }
                }
        );

        switchIgnoreDeck.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupIgnoreDeckScheme(isChecked);
                    }
                }
        );


        switchAutomaticTranslation.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupToolbarAutomaticTranslationEnable(isChecked);
                    }
                }
        );

//        switchCopyMarked.setOnCheckedChangeListener(
//                new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        settings.put(Settings.COPY_MARKED_TEXT, isChecked);
//                    }
//                }
//        );

        switchSymbol.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupSwitchSymbolSelection(isChecked);
                    }
                }
        );

        switchScrollBottom.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.put(Settings.POPUP_SWITCH_SCROLL_BOTTOM, isChecked);
                    }
                }
        );

        switchSearch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupToolbarSearchEnable(isChecked);
                    }

                }
        );

        switchNote.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupToolbarNoteEnable(isChecked);
                    }
                }
        );

        switchTag.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setPopupToolbarTagEnable(isChecked);
                    }
                }
        );

        switchCopy.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.put(Settings.POPUP_SWITCH_COPY, isChecked);
                    }
                }
        );

        textViewClearVideoCache.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Utils.cleanVideoCacheDir(PopupSettingActivity.this);
                            Toast.makeText(PopupSettingActivity.this, "清除完成", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Trace.e(null, "Error cleaning cache", e);
                            Toast.makeText(PopupSettingActivity.this, "Error cleaning cache", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

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
}