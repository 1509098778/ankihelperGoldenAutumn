package com.mmjang.ankihelper.util;

/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.util
 * @ClassName: StorageUtils
 * @Description: java类作用描述
 * @Author: 唐朝
 * @CreateDate: 2022/4/17 11:22 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/4/17 11:22 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

final public class StorageUtils {
    private static final String TAG = "StorageUtils";
    private static final String INDIVIDUAL_DIR_NAME = "video-cache";

    StorageUtils() {
    }

    public static File getIndividualCacheDirectory(Context context) {
        File cacheDir = getCacheDirectory(context, true);
        return new File(cacheDir, "video-cache");
    }

    public static File getAnkihelperDirectory() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + Constant.EXTERNAL_STORAGE_DIRECTORY);
    }

    public static File getIndividualTesseractDirectory() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + Constant.EXTERNAL_STORAGE_DIRECTORY + File.separator +
                Constant.EXTERNAL_STORAGE_TESSERACT_SUBDIRECTORY);
    }

    private static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;

        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException var5) {
            externalStorageState = "";
        }

        if (preferExternal && "mounted".equals(externalStorageState)) {
            appCacheDir = getExternalCacheDir(context);
        }

        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }

        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            Log.w(TAG, "Can't define system cache directory! '" + cacheDirPath + "%s' will be used.");
            appCacheDir = new File(cacheDirPath);
        }

        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Log.w(TAG, "Unable to create external cache directory");
            return null;
        } else {
            return appCacheDir;
        }
    }
}