package com.mmjang.ankihelper.ui.floating;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.ui.LauncherActivity;
import com.mmjang.ankihelper.ui.popup.PopupActivity;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.Trace;

/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.util
 * @ClassName: FxService
 * @Description: 悬浮球，Deprecated Class
 * @Author: ss
 * @CreateDate: 2022/10/26 10:42 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/10/26 10:42 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FxService extends Service {

    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    ImageView mFloatView;
    Settings settings;
    private static final String TAG = "FxService";
    private boolean longClick = false;

    private int mSlop;//触发移动事件的最小距离
    private int downX;//手指放下去的x坐标
    private int downY;//手指放下去的Y坐标

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        settings = Settings.getInstance(getApplicationContext());
        Trace.d(TAG, "oncreat");
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void createFloatView() {
//        ViewConfiguration vc = ViewConfiguration.get(getApplicationContext());
//        mSlop = vc.getScaledTouchSlop();
//
//        //获取WindowManagerImpl.CompatModeWrapper
//        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            wmParams = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//        } else {
//            wmParams = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_PHONE,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//        }
//
//        //调整悬浮窗显示的停靠位置为左侧置顶
//        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
//
//        // 以屏幕左上角为原点，设置x、y初始值
//        Point point = settings.getFloatingButtonPosition();
//        wmParams.x = point.x;
//        wmParams.y = point.y;
//
//        LayoutInflater inflater = LayoutInflater.from(getApplication());
//        //获取浮动窗口视图所在布局
//        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
//        //添加mFloatLayout
//        mWindowManager.addView(mFloatLayout, wmParams);
//
//        Trace.d(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
//        Trace.d(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
//        Trace.d(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
//        Trace.d(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());
//
//        //浮动窗口按钮
//        mFloatView = (ImageView) mFloatLayout.findViewById(R.id.float_id);
//        if (Settings.getInstance(this).getPinkThemeQ()) {
//            mFloatView.setImageResource(R.drawable.ic_float_search_pink);
//        } else {
//            mFloatView.setImageResource(R.drawable.ic_float_search);
//        }
//        startShakeByPropertyAnim(mFloatView, 0.8f, 1.0f, 10f, 1000);
//        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
//                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        Trace.d(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
//        Trace.d(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);
//
//
//        //设置监听浮动窗口的触摸移动
//        mFloatView.setOnTouchListener(new OnTouchListener() {
//            int lastMoveX;
//            int lastMoveY;
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (true) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            downX = (int) event.getRawX();
//                            downY = (int) event.getRawY();
//                            lastMoveX = downX;
//                            lastMoveY = downY;
//                            Trace.e("down x,y", String.format("%d %d", downX, downY));
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            int moveX = (int) event.getRawX();
//                            int moveY = (int) event.getRawY();
//                            if (Math.pow(Math.abs(moveX - downX), 2) + Math.pow(Math.abs(moveY - downY), 2) > Math.pow(mSlop, 2)) {
//                                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
//                                wmParams.x = (int) (wmParams.x + moveX - lastMoveX);
//                                wmParams.y = (int) (wmParams.y + moveY -  lastMoveY);
//                                //刷新
//                                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
//                                lastMoveX = moveX;
//                                lastMoveY = moveY;
//                            }
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            settings.setFloatingButtonPosition(new Point(wmParams.x, wmParams.y));
//                            longClick = false;
//                    }
//                }
//                return false;
//            }
//        });
//        mFloatView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Trace.d(TAG, "长按，预留功能");
//                longClick = true;
//                return true;
//            }
//        });
//        mFloatView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Trace.d(TAG, "点击");
////                longClick = false;
//                if(!longClick)
//                    performClipboardCheck();
//                startShakeByPropertyAnim(mFloatView, 0.8f, 1.0f, 10f, 1000);
//            }
//        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
//                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
//                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

//        //先往左再往右
//        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION_Y,
//                Keyframe.ofFloat(0f, 0f),
//                Keyframe.ofFloat(0.1f, -shakeDegrees),
//                Keyframe.ofFloat(0.2f, -shakeDegrees),
//                Keyframe.ofFloat(0.3f, -shakeDegrees),
//                Keyframe.ofFloat(0.4f, shakeDegrees),
//                Keyframe.ofFloat(0.5f, shakeDegrees),
//                Keyframe.ofFloat(0.6f, shakeDegrees),
//                Keyframe.ofFloat(0.7f, -shakeDegrees),
//                Keyframe.ofFloat(0.8f, shakeDegrees),
//                Keyframe.ofFloat(0.9f, -shakeDegrees),
//                Keyframe.ofFloat(1.0f, 0f)
//        );
//        ObjectAnimator objectAnimator;
//        if (Settings.getInstance(this).getPinkThemeQ()) {
//            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, rotateValuesHolder);
//        } else {
//            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder);
//        }
//        objectAnimator.setDuration(duration);
//        objectAnimator.start();
    }

    private void performClipboardCheck() {

        long[] vibList = new long[1];
        vibList[0] = 10L;
        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Intent.EXTRA_TEXT, Constant.USE_FX_SERVICE_CB_FLAG);
        intent.putExtra(Constant.INTENT_ANKIHELPER_ACTION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startActivity(intent);
        try{
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        Trace.e("Fxservice", "performClipboardCheck");
    }

    public static void startFxService(Context context) {
        Intent intent = new Intent(context, FxService.class);
        context.startService(intent);
    }

    public static void stopFxService(Context context) {
        Intent intent = new Intent(context, FxService.class);
        context.stopService(intent);
    }
}