package com.tzh.myapplication.network.http;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


/**
 * Created on 2020/11/30. 接口集合类
 */
public class InterfaceSet {


    /**
     * netGo的返回接口
     */
    public interface NetGoCallback {
        void done(String api,String result);
        void error(String api, String response);
    }
}
