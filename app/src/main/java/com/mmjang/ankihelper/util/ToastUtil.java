package com.mmjang.ankihelper.util;

import android.content.Context;
import android.widget.Toast;

import com.mmjang.ankihelper.MyApplication;


/**
 * Created by l4656_000 on 2015/12/27.
 */
public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public static void showLong(String msg){
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG).show();
    }
    public static void show(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(int rid){
        Toast.makeText(MyApplication.getContext(), rid, Toast.LENGTH_SHORT).show();
    }
    public static void showLong(int rid){
        Toast.makeText(MyApplication.getContext(), rid, Toast.LENGTH_LONG).show();
    }
}
