package com.tzh.mylibrary.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public class AppPathManager {
    /**
     * 初始话文件管理类
     */
    public static void initPathManager(Context context) {
        //设置app默认文件路径
        AppPathManager.setAppPath(context);
    }

    /**
     * 方法说明:返回多个sd卡的该应用私有数据区的files目录
     * 方法名称:getExternalRootFilesCachePath
     * return：/storage/sdcard0 or sdcard1/Android/data/<包名>/files
     * 返回值:StringBuffer
     */
    public static StringBuffer getExternalRootFilesCachePath(Context context) {
        if (context.getExternalCacheDir() == null) {
            return new StringBuffer(context.getCacheDir()
                    .getAbsolutePath()).append("/");
        }
        return new StringBuffer(context.getExternalFilesDir(
                Environment.MEDIA_MOUNTED).getAbsolutePath()).append("/");
    }

    /**
     * 方法说明:返回多个sd卡下该应用私有数据库的缓存目录
     * 方法名称:getExternalRootCachePath
     * return：/storage/sdcard0 or sdcard1/Android/data/<包名>/caches
     * 返回值:StringBuffer
     */
    public static StringBuffer getExternalRootCachePath(Context context) {
        if (context.getExternalCacheDir() == null) {
            return new StringBuffer(context.getCacheDir()
                    .getAbsolutePath()).append("/");
        }
        return new StringBuffer(context.getExternalCacheDir()
                .getAbsolutePath()).append("/");
    }


    /**
     * 设置app的初始目录
     */
    private static void setAppPath(Context context) {
        File fileAppPath = null;
        try {
            fileAppPath = new File(getExternalRootFilesCachePath(context).toString());
            if (!fileAppPath.exists()) {
                fileAppPath.mkdirs();
            }
            fileAppPath = new File(getExternalRootCachePath(context).toString());
            if (!fileAppPath.exists()) {
                fileAppPath.mkdirs();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * return
     * 方法说明:清除缓存
     * 方法名称:ManualClearCache
     * 返回值:boolean
     */
    public static void ManualClearCache(Context context) {
        File file = new File(getExternalRootCachePath(context).toString());
        DeleteFileAndDir(file);
    }

    /**
     * 方法说明:清除appfiles
     * 方法名称:ManualClearFiles
     * 返回值:void
     */
    public static void ManualClearFiles(Context context) {
        File file = new File(getExternalRootFilesCachePath(context).toString());
        DeleteFileAndDir(file);
    }

    /**
     * 方法说明:递归删除文件和文件夹
     * 方法名称:DeleteFileAndDir
     * 返回值:void
     */
    public static void DeleteFileAndDir(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFileAndDir(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 方法说明:递归删除文件 不删除任何文件夹
     * 方法名称:DeleteFile
     * 返回值:void
     */
    public static void DeleteFileNoDir(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFileNoDir(f);
                }
            }
        }
    }

    /**
     * 方法说明:递归删除文件 不删除当前文件夹
     * 方法名称:DeleteFile
     * 返回值:void
     */
    public static void DeleteFileNoThis(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFileAndDir(f);
                }
            }
        }
    }


    /**
     * @param filePath 删除某个文件
     */
    public static void deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            deleteFile(new File(filePath));
        }
    }

    /**
     * @param fileDirectoryPath 删除某个文件夹内所有文件
     */
    public static void deleteFiles(String fileDirectoryPath) {
        if (!TextUtils.isEmpty(fileDirectoryPath)) {
            File fileDirectory = new File(fileDirectoryPath);
            if (fileDirectory.exists()) {
                if (fileDirectory.isDirectory()) {
                    for (File f : fileDirectory.listFiles()) {
                        deleteFile(f);
                    }
                }
            }
        }
    }

    public static void deleteFile(File file) {
        if (file != null) {

            if (file.exists()) {
                file.delete();
            }
        }
    }


    /**
     * 判断文件夹是否存在，
     * 不存在则创建
     * <p>
     * return
     */
    public static Boolean ifFolderExit(String filePath) {
        File fileAppPath;
        try {
            fileAppPath = new File(filePath);
            if (!fileAppPath.exists()) {
                fileAppPath.mkdirs();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
