package com.mmjang.ankihelper.ui.floating;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.ui.floating.assist.AssistFloatWindow;
import com.mmjang.ankihelper.util.Trace;

/**
 * @ProjectName: ankihelper
 * @Package: com.mmjang.ankihelper.ui.floating
 * @ClassName: QuickTileService
 * @Description: java类作用描述
 * @Author: ss
 * @CreateDate: 2022/7/31 8:32 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/7/31 8:32 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class QuickTileService extends TileService {
    private static final String TAG = QuickTileService.class.getSimpleName();
    private Settings setting = Settings.getInstance(MyApplication.getContext());
    Icon icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_ali_search);
    @Override
    public void onClick() {
        Trace.d(TAG, "onClick");
        // 进行业务处理
        refresh();
    }
    /**
     * 处理点击逻辑，并刷新按钮显示效果
     */
    public void refresh() {
        // 获取当前开关状态
        int state = getQsTile().getState();
        // 改变开关状态
        state = (state == Tile.STATE_ACTIVE)?Tile.STATE_INACTIVE:Tile.STATE_ACTIVE;
        Trace.d(TAG, "refresh:" + state);
        switch(state) {
            case Tile.STATE_ACTIVE:
                AssistFloatWindow.Companion.getInstance().show();
                setting.setFloatBallEnable(true);
                break;
            case Tile.STATE_INACTIVE:
                AssistFloatWindow.Companion.getInstance().hide();
                setting.setFloatBallEnable(false);
                break;
        }
        getQsTile().setState(state);
        getQsTile().setIcon(icon);
        // 刷新显示
        getQsTile().updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        int state;
        if(setting.getFloatBallEnable())
            state = Tile.STATE_ACTIVE;
        else
            state = Tile.STATE_INACTIVE;
        getQsTile().setState(state);
        getQsTile().setIcon(icon);
        // 刷新显示
        getQsTile().updateTile();
    }
}
