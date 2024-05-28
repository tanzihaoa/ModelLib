package com.tzh.mylibrary.util.picture

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


object PictureSelectorHelper {

    /**
     * 举报用户
     */
    @JvmStatic
    fun showPictureSelectorToReport(activity: AppCompatActivity, datas: List<LocalMedia>?, listener: OnResultCallbackListener<LocalMedia>) {
        onPictureSelector(activity, datas, 3,false, 9, 16, 600, 600, listener)
    }

    /**
     * 选择用户头像
     */
    @JvmStatic
    fun showPictureToInfoHead(activity: AppCompatActivity, listener: OnResultCallbackListener<LocalMedia>) {
        onPictureSelector(activity, null, 1, true, 1, 1, 600, 600, listener)
    }

    /**
     * 预览 activity
     *
     */
    fun onPicturePreview(activity: Activity, position: Int, images: MutableList<LocalMedia>) {
        PictureSelector.create(activity)
            .openPreview()
            .setImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
            .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                override fun onPreviewDelete(position: Int) {

                }

                override fun onLongPressDownload(context: Context?, media: LocalMedia?): Boolean {
                    return false
                }
            }).startActivityPreview(position, false, images as ArrayList<LocalMedia>)
    }


    /**
     * 选择器总处理
     */
    fun onPictureSelector(
        activity: Activity,
        selectImages: List<LocalMedia>? = null,
        maxSelectNum: Int = 1,
        isCropCircle: Boolean = false,
        ratioW: Int = 1,
        ratioH: Int = 1,
        cropWidth: Int = 600,
        cropHeight: Int = 600,
        listener: OnResultCallbackListener<LocalMedia>? = null
    ) {
        val selectionModel = PictureSelector.create(activity)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setSelectedData(selectImages)
            //图片最大选择数量
            .setMaxSelectNum(maxSelectNum)
            //图片最小选择数量
            .setMinSelectNum(1)
            //是否开启点击音效
            .isOpenClickSound(false)
            //是否显示gif文件
            .isGif(false)

        //裁剪
        selectionModel.setCropEngine { fragment, srcUri, destinationUri, dataSource, requestCode -> // 注意* 如果你实现自己的裁剪库，需要在Activity的.setResult();
            // Intent中需要给MediaStore.EXTRA_OUTPUT，塞入裁剪后的路径；如果有额外数据也可以通过CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA字段存入；
            val uCrop = UCrop.of(srcUri, destinationUri, dataSource)
                .withAspectRatio(ratioW.toFloat(), ratioH.toFloat())


            uCrop.setImageEngine(object : UCropImageEngine {
                override fun loadImage(context: Context, url: String?, imageView: ImageView) {
                    Glide.with(context).load(url).into(imageView)
                }

                override fun loadImage(context: Context, url: Uri, maxWidth: Int, maxHeight: Int, call: UCropImageEngine.OnCallbackListener<Bitmap>) {

                }
            })
            uCrop.withOptions(UCrop.Options().also { options ->
                options.setCircleDimmedLayer(isCropCircle)

            })
            fragment.activity?.let {
                uCrop.start(it, fragment, requestCode)
            }
        };
        //压缩
        selectionModel.setCompressEngine(CompressFileEngine { context, source, call ->
            Luban.with(context).load(source).ignoreBy(100)
                .setCompressListener(object : OnNewCompressListener {
                    override fun onStart() {
                    }

                    override fun onSuccess(source: String?, compressFile: File?) {
                        call?.onCallback(source, compressFile?.absolutePath)
                    }

                    override fun onError(source: String?, e: Throwable?) {
                        call?.onCallback(source, null)
                    }
                }).launch()
        })

        selectionModel.setSandboxFileEngine { context, srcPath, mineType, call ->
            if (call != null) {
                val sandboxPath =
                    SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                call.onCallback(srcPath, sandboxPath);
            }
        };

        selectionModel.forResult(listener)
    }

    /**
     * 选择器总处理
     */
    @JvmStatic
    fun onPictureSelector(activity: Activity, maxSelectNum: Int = 1, listener: OnResultCallbackListener<LocalMedia>? = null,chooseMode : Int = SelectMimeType.ofImage()) {
        val selectionModel = PictureSelector.create(activity)
            .openGallery(chooseMode)//SelectMimeType.ofVideo()
            .setImageEngine(GlideEngine.createGlideEngine())
            //图片最大选择数量
            .setMaxSelectNum(maxSelectNum)
//            .setMaxVideoSelectNum(1)
            //图片最小选择数量
            .setMinSelectNum(1)
            //是否开启点击音效
            .isOpenClickSound(false)
            //是否显示gif文件
            .isGif(false)
        //压缩
        selectionModel.setCompressEngine(CompressFileEngine { context, source, call ->
            Luban.with(context).load(source).ignoreBy(100)
                .setCompressListener(object : OnNewCompressListener {
                    override fun onStart() {
                    }

                    override fun onSuccess(source: String?, compressFile: File?) {
                        call?.onCallback(source, compressFile?.absolutePath)
                    }

                    override fun onError(source: String?, e: Throwable?) {
                        call?.onCallback(source, null)
                    }
                }).launch()
        })


        selectionModel.forResult(listener)
    }



    /**
     * 选择器总处理
     */
    @JvmStatic
    fun onPictureSelector(activity: Activity, selectImages: List<LocalMedia>? = null, maxSelectNum: Int = 1, listener: OnResultCallbackListener<LocalMedia>? = null,chooseMode : Int = SelectMimeType.ofImage(),isCamera : Boolean = true) {
        val selectionModel = PictureSelector.create(activity)
            .openGallery(chooseMode)//SelectMimeType.ofVideo()
            .setImageEngine(GlideEngine.createGlideEngine())
            .setSelectedData(selectImages)
            //图片最大选择数量
            .setMaxSelectNum(maxSelectNum)
//            .setMaxVideoSelectNum(1)
            //图片最小选择数量
            .setMinSelectNum(1)
            //是否显示拍照
            .isDisplayCamera(isCamera)
            //是否开启点击音效
            .isOpenClickSound(false)
            //是否显示gif文件
            .isGif(false)
        //压缩
        selectionModel.setCompressEngine(CompressFileEngine { context, source, call ->
            Luban.with(context).load(source).ignoreBy(100)
                .setCompressListener(object : OnNewCompressListener {
                    override fun onStart() {
                    }

                    override fun onSuccess(source: String?, compressFile: File?) {
                        call?.onCallback(source, compressFile?.absolutePath)
                    }

                    override fun onError(source: String?, e: Throwable?) {
                        call?.onCallback(source, null)
                    }
                }).launch()
        })


        selectionModel.forResult(listener)
    }
}
