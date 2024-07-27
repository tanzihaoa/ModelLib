package com.tzh.mylibrary.util.voice;

import android.content.Context;

import com.tzh.mylibrary.util.AppPathManager;

import java.io.File;


public class PathUtil {

    /**
     * 获取老师端录制问答语音路径
     * @param context
     * @return
     */
    public static File getMasterQuestionVoice(Context context) {
        File file = context.getExternalFilesDir("voice/");
        assert file != null;
        AppPathManager.ifFolderExit(file.getAbsolutePath());
        return file;
    }

    /**
     * 获取存储xml文件夹
     * @param context 上下文
     * @return
     */
    public static File getXml(Context context) {
        File file = context.getExternalFilesDir("xml/");
        assert file != null;
        AppPathManager.ifFolderExit(file.getAbsolutePath());
        return file;
    }
}
