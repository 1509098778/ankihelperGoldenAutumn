package com.mmjang.ankihelper.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.ui.floating.screenshot.CaptureResultActivity;
import com.mmjang.ankihelper.ui.translation.TranslateBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gao on 2017/6/27.
 */

public class DialogUtil {

    public static void showStartAnkiDialog(Context activityContext) {
        AlertDialog dialog = new AlertDialog.Builder(activityContext)
                .setMessage(activityContext.getString(R.string.plan_anki_not_started))
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                MyApplication.getAnkiDroid().startAnkiDroid();
                            }
                        })
                .create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    public static void showTesseractSettingDialog(Context activityContext) {
        Settings settings = Settings.getInstance(activityContext);
        HashMap<String, Boolean> dataCheckerMap = settings.getTesseractOcrTraineddataCheckBoxMap();
//                        HashMap<String, Boolean> dataCheckerMap = new HashMap<>();
        File[] files = new File(StorageUtils.getIndividualTesseractDirectory(),
                Constant.EXTERNAL_STORAGE_TESSDATA_SUBDIRECTORY).listFiles();
        List<File> list;

        list = new ArrayList<>();
        if(files != null) {
            Arrays.asList(files).stream().map(a->a.getName().endsWith("traineddata")?list.add(a):null).collect(Collectors.toList());
        }

//        if(dataCheckerMap.size() != files.length) {
//            for(int i=0; i<list.size(); i++) {
//                dataCheckerMap.put(list.get(i).getName(), false);
//            }
//        }
        for(File file : list) {
            if(!dataCheckerMap.containsKey(file.getName()))
                dataCheckerMap.put(file.getName(), false);
        }


        String[] dataNameArr = new String[list.size()];
        boolean[] isCheckedArr = new boolean[list.size()];

        for(int i=0; i<list.size(); i++) {
            dataNameArr[i] = String.format("%s", list.get(i).getName());
            Boolean value = dataCheckerMap.get(list.get(i).getName());
            if(value != null) {
                isCheckedArr[i] = value.booleanValue();
            }else
                isCheckedArr[i] = false;
        }

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(activityContext);
        multiChoiceDialog
                .setTitle(R.string.tv_choose_language_name);
//                                .setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
//
//                                });

        multiChoiceDialog.setMultiChoiceItems(
                dataNameArr,
                isCheckedArr,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        File file = list.get(which);
                        dataCheckerMap.replace(file.getName(), Boolean.valueOf(isChecked));
                        isCheckedArr[which] = isChecked;

                        List<String> selectedDataList = new ArrayList<>();
                        for(String key : dataCheckerMap.keySet()) {
                            if(dataCheckerMap.get(key)) {
//                                                Trace.e("selectedData", );
                                selectedDataList.add(key.split("\\.")[0]);
                            }
                        }

                        if(selectedDataList.size()<3) {
                            String lang = "";
                            switch (selectedDataList.size()) {
                                case 1:
                                    lang = selectedDataList.get(0);
                                    break;
                                case 2:
                                    lang = TextUtils.join("+", selectedDataList.toArray());
                                    break;
                            }
                            settings.setTesseractOcrTraineddataCheckBoxMap(dataCheckerMap);
                            settings.setOrcSelectedLang(lang);
                        } else {
                            ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                            dataCheckerMap.replace(file.getName(), Boolean.valueOf(false));
                            isCheckedArr[which] = false;
                        }
                    }
                });
        multiChoiceDialog.show();
    }

    public static void showMlKitOcrSettingDialog(Context activityContext) {
        Settings settings = Settings.getInstance(activityContext);
        int checkedIndex = settings.getMlKitOcrLangCheckedIndex();
        String[] dataNameArr = Constant.MLKIT_TEXT_RECONGNITION_LANGS;
        boolean[] isCheckedArr = new boolean[Constant.MLKIT_TEXT_RECONGNITION_LANGS.length];

        for(int i = 0; i < isCheckedArr.length; i++) {
            if(i == checkedIndex)
                isCheckedArr[i] = true;
            else
                isCheckedArr[i] = false;
        }

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(activityContext);
        multiChoiceDialog
                .setTitle(R.string.tv_choose_script_charactor);
//                                .setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
//
//                                });

        multiChoiceDialog.setSingleChoiceItems(dataNameArr, checkedIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settings.setMlKitOcrCheckedIndex(which);
                    }
                });
        multiChoiceDialog.show();
    }

    public static void selectTranslatorSettingDialog(Context activityContext) {
        Settings settings = Settings.getInstance(activityContext);
        int checkedIndex = settings.getTranslatorCheckedIndex();
        String[] dataNameArr = TranslateBuilder.getNameArr();
        boolean[] isCheckedArr = new boolean[dataNameArr.length];

        for(int i = 0; i < isCheckedArr.length; i++) {
            if(i == checkedIndex)
                isCheckedArr[i] = true;
            else
                isCheckedArr[i] = false;
        }

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(activityContext);
        multiChoiceDialog.setTitle(R.string.tv_translation_engine);
        multiChoiceDialog.setSingleChoiceItems(dataNameArr, checkedIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settings.setTranslatorCheckedIndex(which);
                    }
                });
        multiChoiceDialog.show();
    }

    public static void captureEditModeSettingDialog(Context activityContext) {
        Settings settings = Settings.getInstance(activityContext);
        int checkedIndex = settings.get(Settings.CAPTURE_RESULT_EDIT_MODE, 0);
        String[] modeNameArr = new String[Constant.Mode.values().length];
        for(int index=0; index < Constant.Mode.values().length; index++) {
            modeNameArr[index] = Constant.Mode.values()[index].getName();
        }

        boolean[] isCheckedArr = new boolean[modeNameArr.length];

        for(int i = 0; i < isCheckedArr.length; i++) {
            if(i == checkedIndex)
                isCheckedArr[i] = true;
            else
                isCheckedArr[i] = false;
        }

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(activityContext);
        multiChoiceDialog.setTitle(R.string.tv_capture_result_edit_mode);
        multiChoiceDialog.setSingleChoiceItems(modeNameArr, checkedIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settings.put(Settings.CAPTURE_RESULT_EDIT_MODE, which);
                    }
                });
        multiChoiceDialog.show();
    }
}
