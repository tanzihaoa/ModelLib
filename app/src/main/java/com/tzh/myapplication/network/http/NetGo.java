package com.tzh.myapplication.network.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.tzh.myapplication.network.http.InterfaceSet.NetGoCallback;


/**
 * 接口请求工具类
 */
public class NetGo extends NetGoBase {
    /**
     * 统计
     * @param logs
     * @param callback
     */
    public static void tongji(String logs, NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        param.put("logs", logs);
        //请求接口并返回回调
        NetGo.request("tongji", param, callback);
    }


    //传递给后端日志
    public static void qsPrintLog(String logs,NetGoCallback callback){
//        //参数列表
//        JSONObject param = UserDBManager.getInstance().getParam();
//        param.put("logs",logs);
//        //请求接口并返回回调
//        NetGo.request("qsPrintLog",param,callback);
    }

    /**
     * 荣耀手机打印日志
     * @param logs
     */
    public static void qsUploadWidgetLog(String logs){
//        //是不是荣耀手机,荣耀手机才上传日志
//        if(Build.BRAND.toLowerCase().equals("honor")) {
//            //参数列表
//            JSONObject param = UserDBManager.getInstance().getParam();
//            param.put("logs", logs);
//            //请求接口并返回回调
//            NetGo.request("qsUploadWidgetLog", param, null);
//        }
    }
    /**
     * 小组件打印日志
     * @param logs
     * @param callback
     */
    public static void qsUploadWidgetLog(String logs, NetGoCallback callback){
//        //是不是荣耀手机,荣耀手机才上传日志
//        if(Build.BRAND.toLowerCase().equals("honor")) {
//            //参数列表
//            JSONObject param = UserDBManager.getInstance().getParam();
//            param.put("logs", logs);
//            //请求接口并返回回调
//            NetGo.request("qsUploadWidgetLog", param, callback);
//        }
    }



    //用户注册
    public static void register(String account,String password,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //用户名
        param.put("account",account);
        //密码
        param.put("password",password);

        NetGo.request("register",param,callback);
    }

    //用户登录
    public static void login(String account,String password,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //用户名
        param.put("account",account);
        //密码
        param.put("password",password);

        NetGo.request("login",param,callback);
    }



    //积分兑换商品列表
    public static void getGoodsList(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        NetGo.request("getGoodsList",param,callback);
    }

    //兑换商品
    public static void setExchangeGoods(String token,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //兑换的商品token
        param.put("goodsToken",token);
        NetGo.request("setExchangeGoods",param,callback);
    }

    ///游客登录
    public static void youkeLogin(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //请求接口并返回回调
        NetGo.request("youkeLogin",param,callback);
    }

    //根据tag获取主题列表
    public static void getThemeListByTag(String tag,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //标记
        param.put("tag",tag);
        //请求接口并返回回调
        NetGo.request("getThemeListByTag",param,callback);
    }

    ///获取版本信息
    public static void getLastVersion(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        ///新的隐私政策需要
        param.put("isNewPrivacy",1);
        //上次组件强制更新时间
        param.put("lastForceUpdateWidgetModelTime","");

        try {
            ///获取数据库里面小组件数据，如果数据库存在tab数据，不在进行拉去
            param.put("tokenList", "");
        }catch (Exception e){

        }

        //请求接口并返回回调
        NetGo.request("getLastVersion",param,callback);
    }

    /**
     * 根据地理位置获取附近人使用主题
     * @param longitude
     * @param latitude
     * @param callback
     */
    public static void getLocRecomList(double longitude,double latitude,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        ///地理位置
        param.put("longitude",longitude);
        param.put("latitude",latitude);
        //请求接口并返回回调
        NetGo.request("getLocRecomList",param,callback);
    }

    //点击立即使用主题
    public static void setThemeIsSelect(String themeType,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //标记
        param.put("themeType",themeType);
        //请求接口并返回回调
        NetGo.request("setThemeIsSelect",param,callback);
    }


    //获取壁纸分类列表
    public static void getCateList(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //获取壁纸分类列表
        NetGo.request("getCateList",param,callback);
    }

    //根据分类id获取壁纸详情
    public static void getWallpaperListByCateId(String cateId,int page,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //分类详情
        param.put("cateId",cateId);

        //分页
        JSONObject range = new JSONObject();
        range.put("page",page);
        range.put("pageSize", 20);
        param.put("range",range);

        //获取壁纸分类列表
        NetGo.request("getWallpaperListByCateId",param,callback);
    }
    //获取我收藏的壁纸列表
    public static void getCollectionWallpaper(int page,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //分类详情
      //  param.put("cateId",cateId);
        //分页
        JSONObject range = new JSONObject();
        range.put("page",page);
        range.put("pageSize", 20);
        param.put("range",range);
        //获取我收藏的壁纸列表
        NetGo.request("getMyWallpaperListThumbUpAction",param,callback);
    }
    //获取我收藏的主题列表
    public static void getCollectionThemeList(int page,NetGoCallback callback){
        JSONObject param = new JSONObject();
        //分类详情
        //  param.put("cateId",cateId);
        //分页
        JSONObject range = new JSONObject();
        range.put("page",page);
        range.put("pageSize",20);
        param.put("range",range);
        //请求接口并返回回调
        NetGo.request("getMyThemeListThumbUp",param,callback);
    }

    //获取搜索历史和热词
    public static void getSearchHistory(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //获取壁纸搜索
        NetGo.request("getSearchHistory",param,callback);
    }
    //首页搜索获取历史和热词
    public static void getThemeSearchHistory(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //获取壁纸搜索
        NetGo.request("getThemeSearchHistory",param,callback);
    }

    //根据关键词和搜索分页获取结果
    public static void getWallpaperListBySearch(int type,String keyword,int page,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //搜索信息
        param.put("type",type);
        param.put("keyword",keyword);

        //分页
        JSONObject range = new JSONObject();
        range.put("page",page);
        range.put("pageSize", 20);
        param.put("range",range);
        //获取壁纸搜索
        NetGo.request("getWallpaperListBySearch",param,callback);
    }


    //获取tab栏的小组件类表
    public static void getWidgetList(String tokenList,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //存在的widgetToken列表
        param.put("tokenList",tokenList);

        NetGo.request("getWidgetList",param,callback);
    }


    /**
     * 获取后端该组件的信息
     * @param widgetToken
     * @param callback
     */
    public static void getWidgetInfoByWidgetToken(String widgetToken,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        //标记
        param.put("widgetToken",widgetToken);
        //请求接口并返回回调
        NetGo.request("getWidgetInfoByWidgetToken",param,callback);
    }


    //获取背景图网络分类列表
    public static void getBackgroundCateList(String bundleId,int widgetType,int widgetSizeType,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        param.put("bundleId",bundleId);
        param.put("widgetType",widgetType);
        param.put("widgetSizeType",widgetSizeType);
        //获取背景图分类列表
        NetGo.request("getBackgroundCateList",param,callback);
    }

    //根据分类id获取网络背景图列表
    public static void getBackgroundListByCateId(String bundleId,String cateId,int page,int widgetType,int widgetSizeType,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        ///给128-228-328
        param.put("bundleId",bundleId);
        //分类详情
        param.put("cateId",cateId);
        param.put("widgetType",widgetType);
        param.put("widgetSizeType",widgetSizeType);

        //分页
        JSONObject range = new JSONObject();
        range.put("page",page);
        range.put("pageSize", 20);
        param.put("range",range);

        //获取壁纸分类列表
        NetGo.request("getBackgroundListByCateId",param,callback);
    }



    /**
     * 纸条弹窗显示反馈
     * @param popType
     * @param callback
     */
    public static void showPopLog(int popType,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        ///纸条弹窗的类型
        param.put("popType",popType);
        NetGo.request("showPopLog", param, callback);
    }

    /**
     * 接受绑定
     * @param requestBindCode
     * @param callback
     */
    public static void acceptBind(String requestBindCode,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        ///设置绑定纸条码
        param.put("requestBindCode",requestBindCode);
        NetGo.request("acceptBind", param, callback);
    }

    /**
     * 拒绝绑定
     * @param requestBindCode
     * @param callback
     */
    public static void refuseBind(String requestBindCode,NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        ///设置绑定纸条码
        param.put("requestBindCode",requestBindCode);
        NetGo.request("refuseBind", param, callback);
    }

    /**
     * 解绑
     * @param callback
     */
    public static void unBindZhitiao(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        NetGo.request("unBindZhitiao", param, callback);
    }

    /**
     * 获取"给Ta递纸条"页面数据接口
     * @param callback
     */
    public static void getOtherZhitiaoDataInfo(NetGoCallback callback){
        //参数列表
        JSONObject param = new JSONObject();
        NetGo.request("getOtherZhitiaoDataInfo", param, callback);
    }

    /**
     * 加载小纸条的消息
     */
    public static void getZhitiaoJson(NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //设置时间戳
        param.put("timestamp",System.currentTimeMillis());
        NetGo.request("getZhitiaoJson", param, callback);
    }

    /**
     * 根据经纬度获取城市信息
     * @param longitude
     * @param latitude
     * @param callback
     */
    public static void getCityInfoByLocation(double longitude,double latitude,NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //设置经纬度
        param.put("longitude",longitude);
        param.put("latitude",latitude);

        NetGo.request("getCityInfoByLocation", param, callback);
    }

    /**
     * 获取精选页面信息
     * @param token
     * @param callback
     */
    public static void getBeautyInfoByToken(String token,NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //获取精选页面信息
        param.put("token",token);

        NetGo.request("getBeautyInfoByToken", param, callback);
    }

    /**
     * 创建支付订单
     * @param vipType
     * @param payType
     * @param title
     * @param amount
     * @param callback
     */
    public static void createPayOrder(int payType,String vipType,String title,double amount, NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //存储信息
        param.put("payType",payType);
        param.put("vipType",vipType);
        param.put("title",title);
        param.put("amount",amount);

        NetGo.request("createPayOrder", param, callback);
    }

    /**
     * 双重验证，检测订单是否成功
     * @param orderNo
     * @param callback
     */
    public static void checkPayOrder(String orderNo, NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //存储信息
        param.put("orderNo",orderNo);

        NetGo.request("checkPayOrder", param, callback);
    }

    /**
     * 获取vip信息
     * @param callback
     */
    public static void getVipInfo(NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();

        NetGo.request("getVipInfo", param, callback);
    }

    /**
     * 根据组件分享码获取组件信息
     * @param widgetShareCode
     * @param callback
     */
    public static void getShareWidgetList(String widgetShareCode,NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //组件分享码
        param.put("widgetShareCode", widgetShareCode);


        NetGo.request("getShareWidgetList", param, callback);
    }


    /**
     * 获取编辑页面的广告信息
     * @param callback
     */
    public static void getWidgetEditInfo(boolean isDeskClick,NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //是否是左面点击进入的
        param.put("isDeskClick", (isDeskClick ? 1 : 0));

        NetGo.request("getWidgetEditInfo", param, callback);
    }

    /**
     * 主题点赞
     * @param themeType
     * @param isThumbUp     isThumbUp 0 为取消点赞， 1为点赞
     * @param callback
     */
    public static void setThumbUp(String themeType,int isThumbUp,NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //主题类型
        param.put("themeType", themeType);
        //点赞还是取消点赞
        param.put("isThumbUp", isThumbUp);

        NetGo.request("setThumbUp", param, callback);
    }
    /**
     * 壁纸点赞
     * @param picId  图片id
     * @param isThumbUp     isThumbUp 0 为取消点赞， 1为点赞
     * @param callback
     */
    public static void setbzLike(String picId,int isThumbUp,NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();
        //主题类型
        param.put("picId", picId);
        //点赞还是取消点赞
        param.put("isThumbUp", isThumbUp);
        NetGo.request("setWallpaperThumbUp", param, callback);
    }
    /**
     * 主题点赞
     * @param callback
     */
    public static void getThemeWidgetInstallAdsInfo(NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();

        NetGo.request("getThemeWidgetInstallAdsInfo", param, callback);
    }

    /**
     * 灵动岛首页广告
     * @param callback
     */
    public static void getIslandMainPageAds(NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();

        NetGo.request("getIslandMainPageAds", param, callback);
    }
    /**
     * 灵动岛权限设置广告
     * @param callback
     */
    public static void getIslandAuthAds(NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();

        NetGo.request("getIslandAuthAds", param, callback);
    }
    /**
     * 灵动岛权限设置广告
     * @param callback
     */
    public static void getIslandWhiteAds(NetGoCallback callback){
        //参数信息
        JSONObject param = new JSONObject();

        NetGo.request("getIslandWhiteAds", param, callback);
    }
}
