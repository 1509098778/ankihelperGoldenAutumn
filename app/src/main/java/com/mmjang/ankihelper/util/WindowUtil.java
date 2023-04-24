package com.mmjang.ankihelper.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.ui.popup.PopupActivity;

/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.util
 * @ClassName: WindowUtil
 * @Description: java类作用描述
 * @Author: ss
 * @CreateDate: 2022/7/29 1:10 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/7/29 1:10 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WindowUtil {
    public static boolean isStatusBarShown(Activity context) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        int paramsFlag = params.flags & (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return paramsFlag == params.flags;
    }
}
