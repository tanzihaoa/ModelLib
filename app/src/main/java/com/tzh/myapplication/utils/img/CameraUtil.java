package com.tzh.myapplication.utils.img;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.tzh.myapplication.utils.ToastUtil;
import com.tzh.mylibrary.util.GsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CameraUtil {
    /**
     * 创建相册
     *
     * @param activity     上下文
     *                     true：会保证宽高数据的正确性，返回速度慢，耗时，尤其在华为mate30上，可能点击完成后会加载三四秒才能返回。
     *                     false:有宽高数据但不保证正确性，点击完成后秒回，但可能有因旋转问题导致的宽高相反的情况，以及极少数的宽高为0情况。
     * @param num 选择的图片数量
     */
    public static void createAlbum(AppCompatActivity activity,int num,onSelectCallback callback) {
        createAlbumP(activity, num, callback);
    }

    private static void createAlbumP(AppCompatActivity activity,int num,onSelectCallback callback){
        EasyPhotos.createAlbum(activity, true, false, GlideEngine.getInstance())
                .setFileProviderAuthority("com.tzh.myapplication.fileprovider")
                .setCount(num>0?num:1)
//                .complexSelector(true,1,num>0?num:1)//参数说明：是否只能选择单类型，视频数，图片数。
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                        Log.e("CameraUtil====", GsonUtil.GsonString(photos));
                        LuBanList(activity, photos, new onLuBanListCallback() {
                            @Override
                            public void onResult(List<ImageDTO> photos) {
                                callback.onResult(photos);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        callback.onCancel();
                    }
                });
    }

    /**
     * 复杂选择器  可以选择视频
     * @param activity
     * @param num
     * @param callback
     */
    public static void createAlbumComplex(AppCompatActivity activity,int num,onSelectCallback callback){
        EasyPhotos.createAlbum(activity, true, false, GlideEngine.getInstance())
                .setFileProviderAuthority("com.tzh.myapplication.fileprovider")
//                .setCount(num>0?num:1)
                .complexSelector(true,1,num>0?num:1)//参数说明：是否只能选择单类型，视频数，图片数。
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                        Log.e("CameraUtil====", GsonUtil.GsonString(photos));
                        if(photos.size() == 1 && photos.get(0).duration > 0){
                            Photo photo = photos.get(0);
                            ImageDTO dto = new ImageDTO(new File(photo.path));
                            dto.type = 2;
                            List<ImageDTO> luBanList = new ArrayList<>();
                            luBanList.add(dto);
                            callback.onResult(luBanList);
                        }else{
                            LuBanList(activity, photos, new onLuBanListCallback() {
                                @Override
                                public void onResult(List<ImageDTO> photos) {
                                    callback.onResult(photos);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {
                        callback.onCancel();
                    }
                });
    }


    public static void LuBan(Activity activity,String path,onLuBanCallback callback){
        Log.e("Luban====", path);
        Luban.with(activity)
                .load(path)
                .ignoreBy(100)
                .setTargetDir(getPath(activity))
                .filter(path1 -> !(TextUtils.isEmpty(path1) || path1.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        Log.e("Luban====", "onStart");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.e("Luban====onSuccess", file.getAbsolutePath());
                        Log.e("Luban====", "onSuccess");
                        callback.onResult(new ImageDTO(file));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Luban====", "onError"+e.getMessage());
                        ToastUtil.showLong(e.getMessage());
                        // TODO 当压缩过程出现问题时调用
                        callback.onError();
                    }
                }).launch();
    }

    public static void LuBanList(Activity activity,ArrayList<Photo> photos,onLuBanListCallback callback){
        List<File> list = new ArrayList<>();
        for (Photo photo:photos) {
            list.add(new File(photo.path));
        }

        List<ImageDTO> dtoList = new ArrayList<>();
        Luban.with(activity)
                .load(list)
                .ignoreBy(100)
                .setTargetDir(getPath(activity))
                .filter(path1 -> !(TextUtils.isEmpty(path1) || path1.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.e("Luban====", "onStart");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.e("Luban====onSuccess", file.getAbsolutePath());
                        Log.e("Luban====", "onSuccess");
                        dtoList.add(new ImageDTO(file));
                        if(dtoList.size() == list.size()){
                            callback.onResult(dtoList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Luban====", "onError"+e.getMessage());
                        ToastUtil.showLong(e.getMessage());
                        callback.onError();
                    }
                }).launch();
    }


    public interface onLuBanListCallback{
        void onResult(List<ImageDTO> photos);
        void onError();
    }

    public interface onSelectCallback{
        void onResult(List<ImageDTO> photos);
        void onCancel();
    }

    public interface onLuBanCallback{
        void onResult(ImageDTO file);
        void onError();
    }


    private static String getPath(Context context) {

        return KtFileUtil.INSTANCE.getImageCacheFolder(context).getAbsolutePath();
    }
}
