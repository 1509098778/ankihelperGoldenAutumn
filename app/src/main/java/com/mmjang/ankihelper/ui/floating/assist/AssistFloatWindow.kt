package com.mmjang.ankihelper.ui.floating.assist

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Point
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnFloatCallbacks
import com.mmjang.ankihelper.MyApplication
import com.mmjang.ankihelper.R
import com.mmjang.ankihelper.data.Settings
import com.mmjang.ankihelper.ui.floating.screenshot.ScreenCaptureActivity
import com.mmjang.ankihelper.ui.popup.PopupActivity
import com.mmjang.ankihelper.util.ColorThemeUtils
import com.mmjang.ankihelper.util.Constant
import com.mmjang.ankihelper.util.Trace

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class AssistFloatWindow private constructor() {
    fun open(floatWindowCallback: FloatWindowCallback) {
        // 以屏幕左上角为原点，设置x、y初始值
        val point: Point = Settings.getInstance(MyApplication.getContext()).floatingButtonPosition
        EasyFloat.with(MyApplication.getContext()).setLayout(R.layout.layout_assist_float_window).setShowPattern(ShowPattern.ALL_TIME)
            .setAnimator(DefaultAnimator())
            .setLocation(point.x, point.y)
            .setSidePattern(SidePattern.RESULT_SIDE)
            .setGravity(Gravity.END, 0, ScreenUtil.getScreenHeightPixels(MyApplication.getContext()) * 2 / 5).setAppFloatAnimator(null)
            .registerCallbacks(object : OnFloatCallbacks {
                override fun createdResult(
                    isCreated: Boolean, msg: String?, view: View?
                ) {
                    val logoView = view!!.findViewById<ImageView>(R.id.ic_logo)

//                    if (Settings.getInstance(MyApplication.getContext()).getPinkThemeQ()) {
//                        logoView?.setBackgroundResource(R.drawable.ic_float_search_pink);
//                    } else {
//                        logoView?.setBackgroundResource(R.drawable.ic_float_search);
//                    }
                    ColorThemeUtils.setFloatingLogo(MyApplication.getContext(), logoView)
                    floatWindowCallback.onLogoWindowCreated(logoView)
                    logoView.setOnClickListener {
                        val intent = Intent()
                        intent.setClass(MyApplication.getContext(), ScreenCaptureActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        MyApplication.getContext().startActivity(intent)
                    }

                    logoView.setOnLongClickListener{
                        performClipboardCheck()
                        return@setOnLongClickListener true
                    }
                }

                override fun show(view: View) {
                    floatWindowCallback.show(view)
                }
                override fun hide(view: View) {
                    floatWindowCallback.hide(view)
                }
                override fun dismiss() {}
                override fun touchEvent(view: View, event: MotionEvent) {
//                    floatWindowCallback.onTouch(view, event)
                }
                override fun drag(view: View, event: MotionEvent) {
                    floatWindowCallback.onDrag(view, event)
                }
                override fun dragEnd(view: View) {
                    floatWindowCallback.onDragEnd(view)
                }

            }).show()
    }

    fun openSelection(callback: SelectionFloatWindowCallback) {
        EasyFloat.with(MyApplication.getContext()).setTag(FLOAT_WINDOW_SELECTION).setLayout(R.layout.view_float_window_hierarchy)
            .setAnimator(DefaultAnimator())
            .setShowPattern(ShowPattern.ALL_TIME).setMatchParent(true, true).setAppFloatAnimator(null)
            .registerCallbacks(object : OnFloatCallbacks {
                override fun createdResult(
                    isCreated: Boolean, msg: String?, view: View?
                ) {
                    view?.apply {
                        val hierarchyView: HierarchyView = findViewById(R.id.hierarchy_view)
                        callback.onSelectionWindowCreated(hierarchyView)
                    }
                }

                override fun show(view: View) {}
                override fun hide(view: View) {}
                override fun dismiss() {}
                override fun touchEvent(view: View, event: MotionEvent) {}
                override fun drag(view: View, event: MotionEvent) {}
                override fun dragEnd(view: View) {}
            }).show()
    }

    fun show(tag: String?) {
        EasyFloat.showAppFloat(tag)
    }

    fun show() {
        EasyFloat.showAppFloat()
    }
    
    fun hide() {
        EasyFloat.hideAppFloat()
    }
    fun dismiss() {
        EasyFloat.dismissAppFloat()
    }

    fun dismiss(tag: String?) {
        EasyFloat.dismissAppFloat(tag)
    }

    fun isShowing(): Boolean {
        return EasyFloat.appFloatIsShow(null)
    }

    fun isShowing(tag: String?): Boolean {
        return EasyFloat.appFloatIsShow(tag)
    }

    companion object {

        const val FLOAT_WINDOW_SELECTION = "AssistSelectionWindow"
        val instance: AssistFloatWindow by lazy {
            AssistFloatWindow()
        }

        var screenshotPermission: Intent? = null
    }

    private fun putTextToPopupWindow(text: String) {
        processing(text)
        Trace.e("AssistDragDelegate", "performClipboardCheck")
    }

    private fun performClipboardCheck() {
        processing(Constant.USE_FX_SERVICE_CB_FLAG)
        Trace.e("AssistDragDelegate", "performClipboardCheck")
    }

    private fun processing(EXTRA_FLAG:String) {
        val vibList = LongArray(1)
        vibList[0] = 10L
        val intent = Intent(MyApplication.getContext(), PopupActivity::class.java)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.putExtra(Intent.EXTRA_TEXT, EXTRA_FLAG)
        intent.putExtra(Constant.INTENT_ANKIHELPER_ACTION, true)
        val pendingIntent =
            PendingIntent.getActivity(MyApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        var mBundle = Bundle()
//        startActivity(MyApplication.getContext(), intent, mBundle)
        try {
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }
    }
}
