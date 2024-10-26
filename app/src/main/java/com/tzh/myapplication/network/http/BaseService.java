package com.tzh.myapplication.network.http;

import com.alibaba.fastjson.JSON;
import com.sunshine.retrofit.HttpBuilder;
import com.sunshine.retrofit.interfaces.Error;
import com.sunshine.retrofit.interfaces.Progress;
import com.sunshine.retrofit.interfaces.Success;

/**
 * Created on 2017/10/23 0023.
 */
public class BaseService {
    /**
     * 下载
     * @param url
     * @param what
     * @param callback
     */
    public static void doDownLoad(String url, String path, String tag, final int what, final DownLoadResponseCallBack callback) {
        HttpBuilder mHttpBuilder = new HttpBuilder(url).path(path);
        mHttpBuilder.tag(tag).progress(new Progress() {
            public void progress(float p) {
                callback.progress("下载进度", what, p);
            }
        }).success(new Success() {
            public void Success(String path) {
                callback.done("下载成功", what, path);///model就是地址
            }
        }).error(new Error() {
            public void Error(Object... values) {
                callback.error("下载失败", what, JSON.toJSONString(values));
            }
        }).download();///开始下载
    }
}
