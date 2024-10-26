package com.tzh.myapplication.network.http;

/**
 * Created on 2017/11/23 0023.
 */
public interface DownLoadResponseCallBack {
    void progress(String msg, int what, float progress);
    void done(String msg, int what, String path);
    void error(String msg, int what, String response);
}
