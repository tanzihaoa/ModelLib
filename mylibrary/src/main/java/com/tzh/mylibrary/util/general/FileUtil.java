package com.tzh.mylibrary.util.general;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhoutianxing on 2017/11/9.
 */

public class FileUtil {

    /**
     * create parent dir by file path
     *
     * @param filePath file path
     * @return true mean create parent dir succeed
     */
    public static final boolean createFileParentDir(String filePath) {
        File file = new File(filePath);
        if (file != null) {
            if (file.exists()) {
                return true;// parent dir exist
            } else {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    if (parentFile.exists()) {
                        return true;// parent dir exist
                    } else {
                        return parentFile.mkdirs();// create parent dir
                    }
                }
            }
        }
        return false;
    }

    /**
     * get file suffix by file path
     *
     * @param filePath file path
     * @return file suffix,return null means failed
     */
    public static String getFileSuffix(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            int start = filePath.lastIndexOf(".");
            if (start != -1) {
                return filePath.substring(start + 1);
            }
        }
        return null;
    }

    /**
     * whether the path is file path
     *
     * @param path file path
     * @return true means the path is file path
     */
    public static boolean isFilePath(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        if (path.startsWith(File.separator)) {
            return true;
        }
        return false;
    }

    /**
     * whether the file path can write
     *
     * @param path file path
     * @return true means can write to
     */
    public static final boolean canWrite(String path) {
        // if sdcard,needs the permission:  android.permission.WRITE_EXTERNAL_STORAGE
        if (isSDCardPath(path)) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                // TODO write bytes for test
                return true;
            }
        } else {
            // TODO write bytes for test
            return true;
        }
        return false;
    }

    /**
     * whether the file path is sdcard path
     *
     * @param path file path
     * @return true means the file path is sdcard path
     */
    public static final boolean isSDCardPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (path.startsWith(sdRootPath)) {
            return true;
        }
        return false;
    }

    /**
     * get available space(free space can use) by fileDirPath
     *
     * @param fileDirPath file dir path
     * @return available space,-1 means failed
     */
    public static long getAvailableSpace(String fileDirPath) {
        try {
            File file = new File(fileDirPath);
            if (!file.exists()) {
                file.mkdirs();// create to make sure it is not error below
            }
            final StatFs stats = new StatFs(fileDirPath);
            long result = (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * copy LabelEditPresenter file to another one
     *
     * @param fromFile
     * @param toFile
     * @param forceOverwrite
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File fromFile, File toFile, boolean forceOverwrite) {

        if (fromFile == null || !fromFile.exists() || toFile == null) {
            return false;
        }

        if (toFile.exists() && !forceOverwrite) {
            return false;
        }

        try {
            InputStream fosFrom = new FileInputStream(fromFile);
            OutputStream fosTo = new FileOutputStream(toFile);
            byte bytes[] = new byte[1024];

            int writeSize = 0;
            int startIndex = 0;
            int readCount = 0;

            while ((readCount = fosFrom.read(bytes)) != -1) {
                fosTo.write(bytes, startIndex, readCount);
                writeSize += (readCount - startIndex);
            }
            fosFrom.close();
            fosTo.close();

            if (writeSize > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * whether the file exist
     *
     * @param filePath the check file path
     * @return true means exist
     */
    public static boolean isFileExist(String filePath) {

        if (!isFilePath(filePath)) {
            return false;
        }

        File file = new File(filePath);
        if (file != null && file.exists() && file.isFile()) {
            return true;
        }

        return false;
    }
    /** *//**文件重命名
     * @param path 文件目录
     * @param oldname  原来的文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path,String oldname,String newname){
        if(!oldname.equals(newname)){
            File oldfile=new File(path+"/"+oldname);
            File newfile=new File(path+"/"+newname);
            oldfile.renameTo(newfile);

        }
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dst
     * @throws Exception
     */
    public static void copy(File src, File dst) throws Exception {
        int BUFFER_SIZE = 4096;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
    }

    /**
     * @复制文件，支持把源文件内容追加到目标文件末尾
     * @param src
     * @param dst
     * @param append
     * @throws Exception
     */
    public static void copy(File src, File dst, boolean append) throws Exception {
        int BUFFER_SIZE = 4096;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst, append), BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
    }

}
