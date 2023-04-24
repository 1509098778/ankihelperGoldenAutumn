package com.mmjang.ankihelper.ui.floating.assist

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import com.lzf.easyfloat.permission.PermissionUtils
import com.mmjang.ankihelper.MyApplication
import com.mmjang.ankihelper.data.Settings
import com.mmjang.ankihelper.domain.ScreenListener
import com.mmjang.ankihelper.domain.ScreenListener.ScreenStateListener
import com.mmjang.ankihelper.util.CrashManager
import com.mmjang.ankihelper.util.Trace


/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class AssistService : AccessibilityService(), FloatWindowCallback {
    private var mAssistDragDelegate: AssistDragDelegate? = null
    private var mScreenListener: ScreenListener = ScreenListener(this)

    override fun onCreate() {
        super.onCreate()
        //初始化 错误日志系统
        CrashManager.getInstance(this)
        mScreenListener.begin(object : ScreenStateListener {
            override fun onScreenOn() {
                Trace.e("AssistService", "屏幕已打开了")
            }

            override fun onScreenOff() {
                Trace.e("AssistService", "屏幕已关闭了")
                mAssistDragDelegate?.onStopCapture()
            }

            override fun onUserPresent() {
                Trace.e("AssistService", "屏幕已解锁了")
            }
        })
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        mAssistDragDelegate = AssistDragDelegate.newInstance(this)

        Trace.e("Assis", "connected")
        if (PermissionUtils.checkPermission(MyApplication.getContext())) {
            if (!AssistFloatWindow.instance.isShowing()) {
                AssistFloatWindow.instance.open(this)
                AssistFloatWindow.instance.show()
                if(!Settings.getInstance(MyApplication.getContext()).floatBallEnable)
                    AssistFloatWindow.instance.hide()
            }
        }

    }

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (TextUtils.isEmpty(event.packageName)) {
            return;
        }
        Trace.e("eventType", event.eventType.toString())
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> mAssistDragDelegate?.onTypeWindowStateChanged(event)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        //test accessibility service. 2023.1.23
//        mAssistDragDelegate = null
        if (PermissionUtils.checkPermission(MyApplication.getContext())) {
            AssistFloatWindow.instance.dismiss()
            AssistFloatWindow.instance.dismiss(AssistFloatWindow.FLOAT_WINDOW_SELECTION)
        }
        Trace.e("AssistService", "onUnbind!")
        return super.onUnbind(intent)
    }

    override fun hide(view: View) {
        mAssistDragDelegate?.hide(view)
    }
    override fun show(view: View) {
        Trace.e("Assis", "show")
        mAssistDragDelegate?.show(view)
        mAssistDragDelegate?.change(view)
    }
    override fun onDrag(view: View, event: MotionEvent) {
        mAssistDragDelegate?.onDrag(view)
    }

//    override fun onTouch(view: View, event: MotionEvent) {
//        mAssistDragDelegate?.onTouch(view, event)
//    }

    override fun onDragEnd(view: View) {
        mAssistDragDelegate?.onDragEnd(view)
    }

    override fun onLogoWindowCreated(logoView: View) {
        mAssistDragDelegate?.onLogoWindowCreated(logoView)
    }

//    //暂时
//    fun stopService() {
//        mAssistDragDelegate?.onStopCapture()
//        val intent = Intent(MyApplication.getContext(), AssistService::class.java)
//        MyApplication.getContext().stopService(intent)
//    }
//
//    fun startService() {
//        val intent = Intent(MyApplication.getContext(), AssistService::class.java)
//        MyApplication.getContext().startService(intent)
//    }
}
