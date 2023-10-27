package com.tzh.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tzh.myapplication.base.MyApplication;
import com.tzh.myapplication.utils.WakeLockUtil;
import com.tzh.mylibrary.util.LogUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 收款无障碍服务
 */
public class SkAccessibilityService extends AccessibilityService{
    String TAG = "SkAccessibilityService";

    /**
     * 当服务启动的时候会被调用
     */
    @Override
    public void onServiceConnected(){
        LogUtils.e( TAG,":无障碍服务连接成功");
        //开启wakelock，使CPU处于不休眠的状态，开启后需要重启手机
        WakeLockUtil.acquireWakeLock(MyApplication.mContext,10*60*1000L);
    }
    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 获取事件类型，在对应的事件类型中对相应的节点进行操作
        int eventType = event.getEventType();
        //根据事件回调类型进行处理
        switch (eventType) {
            //当通知栏发生改变时
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                LogUtils.e( TAG,"通知栏发生改变时");
                break;
            //当窗口的状态发生改变时（界面改变）
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                LogUtils.e( TAG,"界面改变");
                break;
            //内容改变
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                LogUtils.e( TAG,"内容改变");
                String className = event.getClassName().toString();
                //获取界面信息
                getUiInfo(className);
                break;
            //滑动变化
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                LogUtils.e( TAG,"滑动变化");
                break;
            default:
                LogUtils.e( TAG,"===="+eventType);
                break;
        }
    }
    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {

    }

    /**
     * 通过控件ID获取节点信息
     * @param id 控件Id
     */
    @SuppressLint("NewApi")
    public String getNodeInfo(String id){
        String result="";
        // 获取当前整个活动窗口的根节点
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        // 获取父节点
        //nodeInfo.getParent();
        // 获取子节点
        //nodeInfo.getChild(0);
        if (nodeInfo != null) {
            // 通过文本找到对应的节点集合
            // List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("");
            // 通过控件ID找到对应的节点集合，如com.tencent.mm:id/gd
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            for (AccessibilityNodeInfo item : list) {
                String str=item.getText().toString();
                if (str.length() != 0){
                    result=str;
                    break;
                }
                //模拟点击
                //item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                //模拟长按
                //item.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                //模拟获取焦点
                //item.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                //模拟粘贴
                //item.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
        }
        return result;
    }

    /**
     * 获取界面信息
     * @param classname 类名
     */
    @SuppressLint("NewApi")
    public void getUiInfo(String classname){
        LogUtils.e( TAG,"无障碍服务窗口状态改变,类名为"+classname);
        //通过类名判断是不是微信
        if(classname.equals("com.tencent.mm.ui.LauncherUI")) {
            LogUtils.e( TAG,"正在使用无障碍服务获取微信收款信息：");
            String title=getNodeInfo("com.tencent.mm:id/fzg");
            if(title.contains("微信支付")||title.contains("微信收款助手")) {
                String content = getNodeInfo("com.tencent.mm:id/e7t");
                //String time = getNodeInfo("com.tencent.mm:id/j0l");
                Map<String,String> postmap= new HashMap<>();
                postmap.put("type","weixin");
                postmap.put("title",title);
                postmap.put("money",extractMoney(content));
                postmap.put("content",content);
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                postmap.put("time",sdf.format(new Date()));
                LogUtils.e( TAG,"获取到的信息集合："+ postmap);
            }
        }
    }

    /**
     * 从字符串里提取金额
     * @param content 提取的字符串
     */
    protected  String extractMoney(String content){
        Pattern pattern = Pattern.compile("(收款|收款￥|向你付款|向您付款|入账|到帐)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元");
        Matcher matcher = pattern.matcher(content);
        List<String> list = new ArrayList<>();
        while(matcher.find()){
            list.add(matcher.group());
        }
        if(list.size()>0){
            String tmp=list.get(list.size()-1);
            Pattern patternNum = Pattern.compile("(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?");
            Matcher matcherNum = patternNum.matcher(tmp);
            if(matcherNum.find())
                return matcherNum.group();
        }
        return null;
    }
}
