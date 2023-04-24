package com.mmjang.ankihelper.ui.floating.assist

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import com.mmjang.ankihelper.R
import com.mmjang.ankihelper.data.Settings

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class HierarchyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val strokePaint = Paint()
    private val contentPaint = Paint()
    private var selectedNode: AccessibilityNodeInfo? = null
    private var dragPoint: Point? = null
//    private var themeColor = Color.YELLOW

    constructor(context: Context) : this(context, null)

    init {
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 5F
        contentPaint.color = Color.argb(15, 0, 0, 0)

//        if (Settings.getInstance(context).pinkThemeQ) {
//            context.setTheme(R.style.AppThemePink)
//        } else {
//            context.setTheme(R.style.AppTheme)
//        }
//        val typedValue = TypedValue()
//        val theme: Resources.Theme = context.theme
//        val got: Boolean = theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
//        themeColor = if (got) typedValue.data else Color.YELLOW

    }
    fun getSelectedNode(): AccessibilityNodeInfo? = selectedNode

    fun setSelectedNode(
        selectedNode: AccessibilityNodeInfo?, dragPoint: Point,
    counter: Long) {
        this.selectedNode = selectedNode
        this.dragPoint = dragPoint
        visibility = VISIBLE
        selectedNode?.let {
            if(counter > 400) {
                strokePaint.color = Color.YELLOW
            } else if (AssistUtil.isWechatMsgNode(it)) {
                strokePaint.color = Color.GREEN
            } else {
                strokePaint.color = Color.RED
            }
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        drawWidget(canvas, selectedNode)
    }

    private fun drawWidget(canvas: Canvas, node: AccessibilityNodeInfo?) {
        node?.let {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            val statusBarHeight = getStatusBarHeight()
            bounds.top -= statusBarHeight
            bounds.bottom -= statusBarHeight
            bounds.let {
                canvas.drawRect(it, contentPaint)
                canvas.drawRect(it, strokePaint)
            }
        }
    }

    private fun getStatusBarHeight(): Int {
        val arr = IntArray(2)
        getLocationOnScreen(arr)
        return arr[1]
    }
}