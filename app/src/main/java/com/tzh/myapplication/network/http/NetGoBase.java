package com.tzh.myapplication.network.http;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunshine.retrofit.HttpBuilder;
import com.sunshine.retrofit.interfaces.Error;
import com.sunshine.retrofit.interfaces.Success;

import java.util.HashMap;
import java.util.Map;

import com.tzh.myapplication.base.MyApplication;
import com.tzh.myapplication.network.http.InterfaceSet.NetGoCallback;
import com.tzh.mylibrary.util.Util;

/**
 * 接口请求工具类基类，分隔一些基础，免得长期跟基础混淆
 */
public class NetGoBase {
    //根据api的名称获取map
    public static final HashMap<String,String> api_name_to_url_map = new HashMap<String,String>(){
        {
            ///数据统计
            put("tongji", "https://statistic.hatlab.cn/statistic/yuanqi/addActionLog");

            put("qsPrintLog", "/appLog/qsPrintLog");//打印日志
            put("qsUploadWidgetLog", "https://statistic.hatlab.cn/statistic/yuanqi/addAndroidWidgetLog");//小组件打印日志
            put("addAndroidWidgetRefreshLog", "https://statistic.hatlab.cn/statistic/zhuomian/addAndroidWidgetRefreshLog");//小组件work刷新纪录
            put("bindPushMap","/push/bindPushMap");//绑定推送

            put("getUserInfo", "/user/getUserInfo");//获取我的个人数据
            put("setUserInfo", "/user/setUserInfo");//设置我的个人数据

            put("getLastVersion", "/version/getLastVersion");//获取版本信息
            put("qqLogin","/login/qqLogin");//QQ登录
            put("wechatLogin","/login/wechatLogin");//微信登录
            put("qqLogin","/login/qqLogin");//qq登录
            put("youkeLogin","/login/youkeLogin");//微信登录

            put("getThemeListByTag","/theme/getThemeListByTag");//根据tag获取主题列表
            put("getMyThemeListThumbUp","/yuanqiTheme/getMyThemeListThumbUp");//获取我收藏的主题列表
            put("getLocRecomList","/theme/getLocRecomList");//根据地理位置获取附近人使用主题

            put("setThemeIsSelect","/theme/setThemeIsSelect");//点击立即使用主题
            put("getCateList","/wallpaper/getCateList");//获取壁纸分类列表
            put("getWallpaperListByCateId","/wallpaper/getWallpaperListByCateId");//根据分类id获取壁纸详情
            put("getMyWallpaperListThumbUpAction","/wallpaper/getMyWallpaperListThumbUp");//获取我收藏的壁纸列表
            put("getSearchHistory","/wallpaper/getSearchHistory");//获取搜索历史和热词
            put("getThemeSearchHistory","/search/getThemeSearchHistory");//首页搜索获取历史和热词
            put("getWallpaperListBySearch","/wallpaper/getWallpaperListBySearch");//根据关键词和搜索分页获取结果

            //小组件,
            put("getWidgetList","/androidWidget/getWidgetList");//获取tab栏的小组件类表
            put("updateWidget","/androidWidget/updateWidget");//更新小组件

            ///情侣空间
            put("getWidgetInfoByWidgetToken","/androidWidget/getWidgetInfoByWidgetToken");//获取组件信息，比如组件后端提供的图片等等

            put("getThemeInfoByThemeTypeForAndroid","/yuanqiTheme/getThemeInfoByThemeTypeForAndroid");//获取主题信息

            put("getIconFrameListByThemeType","/theme/getIconFrameListByThemeType");//获取图标相框列表
            put("deleteThemeIcon","/theme/deleteThemeIcon");//删除图标

            put("searchThemeByKeywords","/search/searchThemeByKeywords");//首页搜索
            put("getTabbarAdsInfo","/ads/getTabbarAdsInfo");//获取tabbar栏的广告
            put("addAdsLog","/ads/addAdsLog");//广告日志
            put("getThemeWidgetListAdsInfo","/ads/getThemeWidgetListAdsInfo");//获取主题的小组件列表广告



            put("getZhuxiaoStatus","/zhuxiao/getZhuxiaoStatus");//获取注销状态
            put("setZhuxiaoStatus","/zhuxiao/setZhuxiaoStatus");//设置注销状态


            put("getBackgroundCateList","/androidWidget/getBackgroundCateList");//获取背景图网络分类列表
            put("getBackgroundListByCateId","/androidWidget/getBackgroundListByCateId");//根据分类id获取网络背景图列表



            put("getZhitiaoBindInfo","/zhitiao/getZhitiaoBindInfo");//获取纸条小组件的绑定信息
            put("bindZhitiao","/zhitiao/bindZhitiao");//绑定纸条
            put("showPopLog","/zhitiao/showPopLog");//纸条弹窗显示的log

            put("unBindZhitiao","/zhitiao/unBindZhitiao");//纸条解绑
            put("acceptBind","/zhitiao/acceptBind");//纸条接受绑定
            put("refuseBind","/zhitiao/refuseBind");//纸条拒绝绑定
            put("getOtherZhitiaoDataInfo","/zhitiao/getOtherZhitiaoDataInfo");//获取"给Ta递纸条"页面数据接口
            put("setOtherZhitiaoDataInfo","/zhitiao/setOtherZhitiaoDataInfo");//"给Ta递纸条"接口

            put("getCityInfoByLocation","/location/getCityInfoByLocation");//根据经纬度获取城市信息
            //beauty页面
            put("getBeautyInfoByToken","/beauty/getBeautyInfoByToken");//获取精选页面信息
            put("addJizhangLog","/jizhang/addJizhangLog");//账单信息统计


            //会员支付功能
            put("createPayOrder","/pay/createPayOrder");//创建支付订单
            put("checkPayOrder", "/pay/checkPayOrder");//核查支付订单
            put("getVipInfo","/vip/getVipInfo");//获取vip支付信息



            //创建分享组件
            put("createShareWidget","/androidWidget/createShareWidget");
            //根据组件分享码获取组件列表
            put("getShareWidgetList","/androidWidget/getShareWidgetList");


            //获取编辑页面的广告信息
            put("getWidgetEditInfo","/widgetEditAds/getWidgetEditInfo");

            ///主题点赞
            put("setThumbUp","/yuanqiTheme/setThumbUp");
            //壁纸点赞
            put("setWallpaperThumbUp","/wallpaper/setWallpaperThumbUp");
            //回去主题组件安装页面的广告
            put("getThemeWidgetInstallAdsInfo","/ads/getThemeWidgetInstallAdsInfo");

            //灵动岛首页、权限设置页、白名单的广告
            put("getIslandMainPageAds","/island/getIslandMainPageAds");
            put("getIslandAuthAds","/island/getIslandAuthAds");
            put("getIslandWhiteAds","/island/getIslandWhiteAds");

            //登录注册
            put("register","/denglun/zhuCe");//注册
            put("login","/denglun/getUserInfo");//登录
            put("setName","/denglun/setName");//修改昵称

            //签到
            put("getIntegralTaskList","/task/getIntegralTaskList");//签到页面数据
            put("getGoodsList","/task/getGoodsList");//积分兑换商品列表
            put("setExchangeGoods","/task/setExchangeGoods");//兑换商品
        }
    };


    ///请求url
    public static void request(String apiName, JSONObject param, NetGoCallback callback) {
        //param参数和systemParam参数
        Map<String,String> map = new HashMap<>();

        //普通参数,param
        String paramEncryption = "";
        if (param != null){
            paramEncryption = param.toString();
        }
        //获取系统信息参数,systemParam
        String systemEncryption = getSystemInfo();

        Log.e("paramEncryption==",paramEncryption);
        //获取参数和系统参数
        map.put("param", paramEncryption);
        map.put("systemParam", systemEncryption);
        doPostContent(apiName, map, callback);
    }

    /**
     * 获取systemInfo
     * @return
     */
    public static String getSystemInfo(){
        JSONObject system = new JSONObject();
        if (system == null){
            system = new JSONObject();
        }
        ///设置手机牌子
        String brand = Build.BRAND;
        try{
            ///添加手机品牌
            String brand_lit = brand.toLowerCase();
            system.put("brand", brand_lit);
        }catch (Exception e){
        }
        //品牌
        system.put("appName", "yuanqi");
        //品牌
        system.put("model", Build.BRAND + Build.MODEL);
        //新的model
        system.put("realmodel", Build.MODEL);

        //获取版本号
        system.put("version", getCurrent_version());


        system.put("deviceId", DeviceUuidUtils.getDeviceId());
        //系统版本
        system.put("systemVersion",android.os.Build.VERSION.RELEASE);
        //设备类型
        system.put("deviceType", "android");
        //渠道,
        system.put("channel","huawei");

        //屏幕的宽和高
        system.put("screenWidth", Util.getPhoneWidth(MyApplication.mContext));
        system.put("screenHeight", Util.getPhoneHeight(MyApplication.mContext));
        //设置一下经纬度
        system.put("longitude",0.0);
        system.put("latitude",0.0);

        //统一系统信息
        String systemEncryption  = system.toString();

        return  systemEncryption;
    }


    //获取systemInfo
    public static JSONObject getSystemInfo_jsonObject(boolean isForceGetDeviceId){
        JSONObject system = new JSONObject();
        if (system == null){
            system = new JSONObject();
        }
        ///设置手机牌子
        String brand = Build.BRAND;
        try{
            ///添加手机品牌
            String brand_lit = brand.toLowerCase();
            system.put("brand", brand_lit);
        }catch (Exception e){
        }
        //品牌
        system.put("appName", "yuanqi");
        //品牌
        system.put("model", Build.BRAND + Build.MODEL);
        //新的model
        system.put("realmodel", Build.MODEL);

        //获取版本号
        system.put("version", getCurrent_version());


        system.put("deviceId", DeviceUuidUtils.getDeviceId());
        //系统版本
        system.put("systemVersion", android.os.Build.VERSION.RELEASE);
        //设备类型
        system.put("deviceType", "android");
        //渠道,
        system.put("channel", "huawei");

        //屏幕的宽和高
        system.put("screenWidth", Util.getPhoneWidth(MyApplication.mContext));
        system.put("screenHeight", Util.getPhoneHeight(MyApplication.mContext));
        //设置一下经纬度
        system.put("longitude",0.0);
        system.put("latitude",0.0);

        return  system;
    }
    /**
     * post请求数据
     */
    public static void doPostContent(final String apiName, final Map<String, String> map, final NetGoCallback callback) {
//        final long startTime = System.currentTimeMillis(); // 接口开始请求的时间
        ///根据apiName获取apiUrl
        String apiUrl = null;
        if(apiName != null) {
            apiUrl = api_name_to_url_map.get(apiName);
        }
        if(apiUrl == null){
            System.out.println("数据输入为空");
            return;
        }

        ///获取url
        String url;
        if(apiUrl.startsWith("https://")){
            url = apiUrl;
        }else {
            url = getBaseUrl() + apiUrl;
        }




        String apiTotal = "";
        try{
            apiTotal = url+"?param="+map.get("param")+"&systemParam="+map.get("systemParam");
            //接口访问
            System.out.println("接口访问:"+apiTotal);
        }catch (Exception e){

        }

        //发起接口请求
        HttpBuilder mHttpBuilder = new HttpBuilder(url);
        if (map != null && !map.isEmpty()){
            mHttpBuilder.params(map);
        }

        //获取接口请求之前的时间戳
        final long startTime = System.currentTimeMillis();
        final String apiUrl_final = apiUrl;
        final String apiTotal_final = apiTotal;

        //处理接口数据
        mHttpBuilder.tag(null).success(new Success() {
            /////接口访问成功
            public void Success(final String model) {
                //网络请求异步处理
                ThreadQsDeal.getInstance().set_netGoDeal_executors(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("开始解析数据:"+model);
                        ///解析加密数据
                        String currentModel;
                        //////是否加密数据
                        currentModel = model;
                        try {
                            /////返回数据为空
                            if (TextUtils.isEmpty(currentModel) && callback != null){
                                callback.error(apiName,"");
                                return;
                            }
                            //解析后端数据
                            BaseBean baseBean = JSON.parseObject(currentModel, BaseBean.class);
                            //获取status_code
                            Log.e(apiName+"解析数据结果===",currentModel);
                            int code = baseBean.status.status_code;
                            ///status_code == 0 正确的数据
                            if (code == 0 && callback != null){
                                /////返回成功，status_code为0
                                callback.done(apiName, baseBean.result);
                            } else if (callback != null){
                                //统计
                                netGo_tongji("失败,status_code:"+code,startTime,apiUrl_final,apiTotal_final);

                                ///其他失败情况
                                System.out.println("baseBean.status:"+JSON.toJSONString(baseBean.status));
                                callback.error(apiName, currentModel);
                            }

                        } catch (Exception exception) {
                            //统计
                            netGo_tongji("失败,接口返回后解析异常,exception:"+exception,startTime,apiUrl_final,apiTotal_final);

                            Log.e("解析数据结果===","失败,接口返回后解析异常"+exception);
                            if (currentModel.contains("网络请求超时")){
                            }
//                            //接口请求出错,并且不是日志接口本身
//                            if(!apiName.contains("addActionLog")) {
//                                AliLog.getInstance().addLog("netGoBase request exception", currentModel + "#######" + e.toString());
//                            }
                            if(callback != null) {
                                callback.error(apiName, currentModel);
                            }
                        }
                    }
                });
            }
        }).error(new Error() {
            ////接口访问失败
            public void Error(Object... values) {
                //统计
                netGo_tongji("网络请求异常,values:"+values,startTime,apiUrl_final,apiTotal_final);

                if (callback != null){
                    callback.error(apiName, "网络超时");
                }
            }
        }).post();
    }




    /**
     * 获取当前版本
     * @return
     */
    public static String getCurrent_version(){
        return "1.7.1";
    }


    /**
     * 统计接口状态
     * @param status
     * @param startTime
     * @param apiUrl
     * @param apiTotal
     */
    private static void netGo_tongji(String status,long startTime,String apiUrl,String apiTotal){

    }

    /**
     * 服务器列表
     */
    public static final String[] BASE_URL_LIST = new String[]{
            "https://yuanqiback.hatlab.cn/yuanqi142",//正式
            "https://newfront.hatlab.cn/yuanqi142",//青岛
            "https://backhd.photosave.cn/yuanqi142",//杭州
            "https://xibu.geili.ai/yuanqi142",//成都
            "https://newshenzhen.geili.ai/yuanqi142",//深圳
            "https://newfront.hatlab.cn/llfTest",//开发测试
    };
    /**
     * 获取接口的服务器
     * @return
     */
    public static String getBaseUrl(){
        String curren_Base_url = NetGoBase.BASE_URL_LIST[0];
//        String curren_Base_url = NetGoBase.BASE_URL_LIST[5];

        return curren_Base_url;
    }
}
