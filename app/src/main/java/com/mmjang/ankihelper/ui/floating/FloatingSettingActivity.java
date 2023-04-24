package com.mmjang.ankihelper.ui.floating;


import static com.mmjang.ankihelper.util.Constant.ASSIST_SERVICE_INFO_ID;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.quicksettings.TileService;
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
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.ui.customdict.CustomDictionaryActivity;
import com.mmjang.ankihelper.ui.floating.assist.AssistFloatWindow;
import com.mmjang.ankihelper.util.ColorThemeUtils;
import com.mmjang.ankihelper.util.SystemUtils;
import com.mmjang.ankihelper.util.ToastUtil;
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
 * @Author: ss
 * @CreateDate: 2022/5/19 9:57 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/5/19 9:57 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FloatingSettingActivity extends AppCompatActivity {
    private SeekBar seekBarFloatingAlpha;
    private TextView textViewFloatBallAccessibility;
    private TextView textViewFloatBallOverlays;
    private SwitchCompat switchFloatBall;
    private SwitchCompat switchClearSearched;
    private Settings settings = Settings.getInstance(MyApplication.getContext());
    private SharedPreferences.OnSharedPreferenceChangeListener callbackSwitchFloatBall;


    int number = 0;
    int alpha = 100;
    private static final int REQUEST_OVERLAYS_WINDOW = 2;
    private static final int REQUEST_ACCESSIBILITY_SETTINGS = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(Settings.getInstance(this).getPinkThemeQ()){
//            setTheme(R.style.AppThemePink);
//        }else{
//            setTheme(R.style.AppTheme);
//        }
        ColorThemeUtils.setColorTheme(FloatingSettingActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_settings);
        checkAndRequestPermissions();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewFloatBallAccessibility = (TextView) findViewById(R.id.tv_open_float_ball_accessibility);
        textViewFloatBallOverlays = (TextView) findViewById(R.id.tv_open_float_ball_overlays);
        seekBarFloatingAlpha = (SeekBar) findViewById(R.id.sb_floating_alpha);
        switchFloatBall = (SwitchCompat) findViewById(R.id.switch_float_ball);
        switchClearSearched = (SwitchCompat) findViewById(R.id.switch_clear_searched);
        callbackSwitchFloatBall = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switchFloatBall.setChecked(settings.getFloatBallEnable());
            }
        };
        settings.getSharedPreferences().registerOnSharedPreferenceChangeListener(callbackSwitchFloatBall);

        //设置最大值(设置不了最小值)
        seekBarFloatingAlpha.setMax(100);
        //设置初始值
        Settings settings = Settings.getInstance(getApplicationContext());
        switchFloatBall.setChecked(settings.getFloatBallEnable());
        seekBarFloatingAlpha.setEnabled(switchFloatBall.isChecked());
        switchClearSearched.setEnabled(switchFloatBall.isChecked());
        switchClearSearched.setChecked(settings.getClearSearchedEnable());
        alpha  = settings.getFloatingButtonAlpha();
        number = settings.getPopupFontSize();
        seekBarFloatingAlpha.setProgress(alpha);

        seekBarFloatingAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha = progress;
                settings.setFloatingButtonAlpha(alpha);
                AssistFloatWindow.Companion.getInstance().show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textViewFloatBallAccessibility.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(requestAccessibility()) {
                            Toast.makeText(FloatingSettingActivity.this, "辅助服务已打开", Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );

        textViewFloatBallOverlays.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(requestDrawOverLays()) {
                            Toast.makeText(FloatingSettingActivity.this, "悬浮权限已打开", Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );

        switchFloatBall.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked) {
                            AssistFloatWindow.Companion.getInstance().show();
                        } else {
                            AssistFloatWindow.Companion.getInstance().hide();
                        }
                        settings.setFloatBallEnable(isChecked);
                        switchFloatBall.setChecked(isChecked);
                        seekBarFloatingAlpha.setEnabled(switchFloatBall.isChecked());
                        switchClearSearched.setEnabled(switchFloatBall.isChecked());
                    }
                }
        );

        switchClearSearched.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setClearSearchedEnable(isChecked);
                    }
                }
        );

//        startQSTileService();
        if(getIntent().getAction() == TileService.ACTION_QS_TILE_PREFERENCES){
            ComponentName componentName = (ComponentName) getIntent().getExtras().get(Intent.EXTRA_COMPONENT_NAME);
            if(componentName != null) {
                try {
                    Object t = Class.forName(componentName.getClassName()).newInstance();
                    if (t instanceof QuickStartTileService)
                        requestAccessibility();

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startQSTileService() {
//        if(!QuickStartTileService.Companion.isStarted()) {
            Intent i = new Intent(this, QuickStartTileService.class);
            startService(i);
//        }
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

    public boolean requestDrawOverLays() {
        if (!android.provider.Settings.canDrawOverlays(getApplicationContext())) {
            Toast.makeText(this, "您还没有打开悬浮窗权限", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_ask_permission_overlays)
                    .setPositiveButton(R.string.dialog_btn_go_to_permission_window, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //跳转到相应软件的设置页面
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplicationContext().getPackageName()));
                            startActivityForResult(intent, REQUEST_OVERLAYS_WINDOW);
                        }
                    }).show();
            return false;
        } else
            return true;
    }

    public boolean requestAccessibility() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : serviceInfos) {
            String id = info.getId();
            if (id.contains(ASSIST_SERVICE_INFO_ID)) {
                return true;
            }
        }
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, REQUEST_ACCESSIBILITY_SETTINGS);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_OVERLAYS_WINDOW:
                if (!android.provider.Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "悬浮授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "悬浮授权成功", Toast.LENGTH_SHORT).show();
//                    Settings.getInstance(this).setFloatBallEnable(true);
//                    switchFloatBall.setChecked(true);
//                    switchClearSearched.setEnabled(switchFloatBall.isChecked());
                }
                break;
            case REQUEST_ACCESSIBILITY_SETTINGS:
                AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
                List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
                for (AccessibilityServiceInfo info : serviceInfos) {
                    String id = info.getId();
                    Trace.e("all -->" + id);
                    if (id.contains(ASSIST_SERVICE_INFO_ID)) {
                        Toast.makeText(this, "辅助服务打开成功", Toast.LENGTH_SHORT).show();
                        Trace.e("Assis", "hide");
                        AssistFloatWindow.Companion.getInstance().hide();
                        if (Settings.getInstance(MyApplication.getContext()).getFloatBallEnable())
                            AssistFloatWindow.Companion.getInstance().show();
                        if (!MyApplication.getAnkiDroid().isAnkiDroidRunning())
                            MyApplication.getAnkiDroid().startAnkiDroid();
                        return;
                    }
                }
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkAndRequestPermissions() {
        if(requestDrawOverLays()) {
            Toast.makeText(FloatingSettingActivity.this, "悬浮权限已打开", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        settings.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(callbackSwitchFloatBall);
        super.onDestroy();
    }
}