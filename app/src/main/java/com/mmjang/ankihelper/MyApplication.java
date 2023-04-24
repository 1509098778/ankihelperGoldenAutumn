package com.mmjang.ankihelper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.data.dict.DictLanguageType;
import com.mmjang.ankihelper.ui.floating.screenshot.ScreenCaptureActivity;
import com.mmjang.ankihelper.util.DarkModeUtils;
import com.mmjang.ankihelper.util.DialogUtil;
//import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import okhttp3.OkHttpClient;

/**
 * Created by liao on 2017/4/27.
 */

public class MyApplication extends MultiDexApplication {
    private static Context context;
    private static Application application;
    private static AnkiDroidHelper mAnkiDroid;
    private static OkHttpClient okHttpClient;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = this;
        LitePal.initialize(context);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
        DarkModeUtils.init(this);
//        CrashReport.initCrashReport(getApplicationContext(), "398dc6145b", false);
        AndroidThreeTen.init(this);


    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication(){
        return application;
    }

    public static AnkiDroidHelper getAnkiDroid() {
        if (mAnkiDroid == null) {
            mAnkiDroid = new AnkiDroidHelper(getApplication());
        }
        return mAnkiDroid;
    }

    private static void getAnkiDroidPermission(Activity activity) {

    }

    public static OkHttpClient getOkHttpClient(){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void initRunningAndroid() {

    }

}
