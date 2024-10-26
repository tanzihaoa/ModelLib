package com.tzh.myapplication.ui.activity.wallper;

import java.io.Serializable;
import java.util.List;

/**
 * 单个壁纸详情
 */
public class WallpaperInfo implements Serializable{
    //图片id
    public String picId;
    //分类
    public String cateId;
    //名称
    public String title;
    //链接
    public String thumbnail;
    //链接
    public String thumbnail600;
    //链接
    public String imageUrl;
    //宽和高
    public int width;
    public int height;
    public String videoUrl;
    //链接
    public String nickName;
    //链接
    public String headimgUrl;
    //类型
    public int type;
    //是否点赞 0未点赞 1已点赞
    public int isThumbUp;
    //分享字段
}
