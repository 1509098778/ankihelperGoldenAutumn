package com.mmjang.ankihelper.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.ui.floating.assist.AssistFloatWindow;

/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.util
 * @ClassName: ThemeUtils
 * @Description: java类作用描述
 * @Author: ss
 * @CreateDate: 2022/10/26 10:42 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/10/26 10:42 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ColorThemeUtils {

    public static void setColorTheme(Context activityContext) {
        setColorTheme(activityContext, Constant.StyleBaseTheme.AppTheme);
    }
    public static void setColorTheme(Context activityContext, Constant.StyleBaseTheme style) {
        Settings settings = Settings.getInstance(activityContext);
        int index = settings.get(Settings.THEME_COLOR_INDEX, 0);
        Constant.ThemeColor color = Constant.ThemeColor.values()[index];

        switch (style) {
            case AppTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        activityContext.setTheme(R.style.AppTheme);
                        break;
                    case THEME_COLOR_BLUE:
                        activityContext.setTheme(R.style.AppThemeBlue);
                        break;
                    case THEME_COLOR_PINK:
                        activityContext.setTheme(R.style.AppThemePink);
                        break;
                }
                break;
            case Transparent:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        activityContext.setTheme(R.style.Transparent);
                        break;
                    case THEME_COLOR_BLUE:
                        activityContext.setTheme(R.style.TransparentBlue);
                        break;
                    case THEME_COLOR_PINK:
                        activityContext.setTheme(R.style.TransparentPink);
                        break;
                }
                break;
            case BigBangTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        activityContext.setTheme(R.style.BigBangTheme);
                        break;
                    case THEME_COLOR_BLUE:
                        activityContext.setTheme(R.style.BigBangThemeBlue);
                        break;
                    case THEME_COLOR_PINK:
                        activityContext.setTheme(R.style.BigBangThemePink);
                        break;
                }
                break;
            case ScreenCaptureTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        activityContext.setTheme(R.style.ScreenCaptureTheme);
                        break;
                    case THEME_COLOR_BLUE:
                        activityContext.setTheme(R.style.ScreenCaptureThemeBlue);
                        break;
                    case THEME_COLOR_PINK:
                        activityContext.setTheme(R.style.ScreenCaptureThemePink);
                        break;
                }
                break;
            case CustomDicTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        activityContext.setTheme(R.style.CustomDicTheme);
                        break;
                    case THEME_COLOR_BLUE:
                        activityContext.setTheme(R.style.CustomDicThemeBlue);
                        break;
                    case THEME_COLOR_PINK:
                        activityContext.setTheme(R.style.CustomDicThemePink);
                        break;
                }
                break;

        }
    }

    public static int getColorTheme(Context activityContext, Constant.StyleBaseTheme style) {
        Settings settings = Settings.getInstance(activityContext);
        int index = settings.get(Settings.THEME_COLOR_INDEX, 0);
        Constant.ThemeColor color = Constant.ThemeColor.values()[index];

        switch (style) {
            case AppTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        return (R.style.AppTheme);
                    case THEME_COLOR_BLUE:
                        return (R.style.AppThemeBlue);
                    case THEME_COLOR_PINK:
                        return (R.style.AppThemePink);
                }
                break;
            case Transparent:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        return (R.style.Transparent);

                    case THEME_COLOR_BLUE:
                        return (R.style.TransparentBlue);

                    case THEME_COLOR_PINK:
                        return (R.style.TransparentPink);

                }
                break;
            case BigBangTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        return (R.style.BigBangTheme);

                    case THEME_COLOR_BLUE:
                        return (R.style.BigBangThemeBlue);

                    case THEME_COLOR_PINK:
                        return (R.style.BigBangThemePink);

                }
                break;
            case ScreenCaptureTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        return (R.style.ScreenCaptureTheme);

                    case THEME_COLOR_BLUE:
                        return (R.style.ScreenCaptureThemeBlue);

                    case THEME_COLOR_PINK:
                        return (R.style.ScreenCaptureThemePink);

                }
                break;
            case CustomDicTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        return (R.style.CustomDicTheme);

                    case THEME_COLOR_BLUE:
                        return (R.style.CustomDicThemeBlue);

                    case THEME_COLOR_PINK:
                        return (R.style.CustomDicThemePink);
                }
                break;
            case AndroidFilePickerTheme:
                switch (color) {
                    case THEME_COLOR_GREEN:
                        return (R.style.AndroidFilePickerTheme);

                    case THEME_COLOR_BLUE:
                        return (R.style.AndroidFilePickerThemeBlue);

                    case THEME_COLOR_PINK:
                        return (R.style.AndroidFilePickerThemePink);
                }
                break;
        }

        return 0;
    }

    public static void setFloatingLogo(Context activityContext, View v) {
        Settings settings = Settings.getInstance(activityContext);
        int index = settings.get(Settings.THEME_COLOR_INDEX, 0);
        Constant.ThemeColor color = Constant.ThemeColor.values()[index];

        switch(color) {
            case THEME_COLOR_GREEN:
//                v.setBackgroundResource(R.drawable.ic_float_search);
                v.setBackgroundResource(R.drawable.floating);
                break;
            case THEME_COLOR_BLUE:
                v.setBackgroundResource(R.drawable.floating_blue);
                break;
            case THEME_COLOR_PINK:
                v.setBackgroundResource(R.drawable.ic_float_search_pink);
                break;
        }
    }

    public static void themeSettingDialog(Context activityContext) {
//        LinkedHashMap<String, Integer> themeMap = new LinkedHashMap<>();
//        themeMap.put("MODE_NIGHT_FOLLOW_SYSTEM", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        themeMap.put("MODE_NIGHT_NO", AppCompatDelegate.MODE_NIGHT_NO);
//        themeMap.put("MODE_NIGHT_FOLLOW_SYSTEM", AppCompatDelegate.MODE_NIGHT_YES);

        Settings settings = Settings.getInstance(activityContext);
        int checkedIndex = settings.get(Settings.THEME_COLOR_INDEX, 0);

        String[] colorNameArr = new String[Constant.ThemeColor.values().length];
        for(int index=0; index < Constant.ThemeColor.values().length; index++) {
            colorNameArr[index] = Constant.ThemeColor.values()[index].getName();
        }

        boolean[] isCheckedArr = new boolean[colorNameArr.length];

        for(int i = 0; i < isCheckedArr.length; i++) {
            if(i == checkedIndex)
                isCheckedArr[i] = true;
            else
                isCheckedArr[i] = false;
        }

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(activityContext);
        multiChoiceDialog.setTitle(R.string.str_color_theme_q);
        multiChoiceDialog.setSingleChoiceItems(colorNameArr, checkedIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.ThemeColor color = Constant.ThemeColor.values()[which];
                        switch(color) {
                            case THEME_COLOR_GREEN:
                                activityContext.setTheme(R.style.AppTheme);
                                break;
                            case THEME_COLOR_BLUE:
                                activityContext.setTheme(R.style.AppThemeBlue);
                                break;
                            case THEME_COLOR_PINK:
                                activityContext.setTheme(R.style.AppThemePink);
                                break;
                        }
                        settings.put(Settings.THEME_COLOR_INDEX, which);
                        ((Activity)activityContext).recreate();
                        if(settings.getFloatBallEnable() && android.provider.Settings.canDrawOverlays(activityContext.getApplicationContext())) {
                            AssistFloatWindow.Companion.getInstance().show();
                        }
                        dialog.dismiss();
                    }
                });
        multiChoiceDialog.show();
    }

}

