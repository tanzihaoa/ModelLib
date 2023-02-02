package com.tzh.mylibrary.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class LoadImageUtil {

    /**
     * 加载资源文件图片
     * @param imageView
     * @param url 路径
     * @param topLeftRounded 左上圆角
     * @param topRightRounded 右上圆角
     * @param bottomRightRounded 右下圆角
     * @param bottomLeftRounded 坐下圆角
     */
    public static void loadImageResources(ImageView imageView, int url, float topLeftRounded, float topRightRounded, float bottomRightRounded, float bottomLeftRounded) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(
                        (DpToUtil.dip2px(imageView.getContext(), topLeftRounded)),
                        DpToUtil.dip2px(imageView.getContext(), topRightRounded),
                        DpToUtil.dip2px(imageView.getContext(), bottomRightRounded),
                        DpToUtil.dip2px(imageView.getContext(), bottomLeftRounded)))))
                .into(imageView);
    }

    /**
     * 加载URL文件图片
     * @param imageView
     * @param url 路径
     * @param topLeftRounded 左上圆角
     * @param topRightRounded 右上圆角
     * @param bottomRightRounded 右下圆角
     * @param bottomLeftRounded 坐下圆角
     */
    public static void loadImageUrl(ImageView imageView, String url, float topLeftRounded, float topRightRounded, float bottomRightRounded, float bottomLeftRounded) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(
                        DpToUtil.dip2px(imageView.getContext(), topLeftRounded),
                        DpToUtil.dip2px(imageView.getContext(), topRightRounded),
                        DpToUtil.dip2px(imageView.getContext(), bottomRightRounded),
                        DpToUtil.dip2px(imageView.getContext(), bottomLeftRounded)))))
                .into(imageView);
    }

    /**
     * 加载URL文件图片
     * @param imageView
     * @param url 路径
     * @param rounded 圆角

     */
    public static void loadImageUrl(ImageView imageView, String url, float rounded) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded)))))
                .into(imageView);
    }

    /**
     * 加载URL文件图片
     * @param imageView
     * @param url 路径
     * @param rounded 圆角

     */
    public static void loadImageUrl(ImageView imageView, int url, float rounded) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded)))))
                .into(imageView);
    }

    @BindingAdapter(value = {"loadHead","rounded"})
    public static void loadHead(ImageView imageView,String url,float rounded){
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded)))))
                .into(imageView);
    }

    @BindingAdapter(value = "loadImageUrl")
    public static void loadImageUrl(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @BindingAdapter(value = "loadImageUrl")
    public static void loadImageUrl(ImageView imageView,int url){
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    public static void loadImageBitmap(ImageView imageView, Bitmap bitmap,float rounded){
        Glide.with(imageView.getContext())
                .load(bitmap)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new GranularRoundedCorners(
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded),
                        DpToUtil.dip2px(imageView.getContext(), rounded)))))
                .into(imageView);
    }

    @BindingAdapter(value = "loadImageDrawable")
    public static void loadImageDrawable(ImageView imageView, Drawable url){
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
