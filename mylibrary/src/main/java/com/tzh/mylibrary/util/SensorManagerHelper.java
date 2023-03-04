package com.tzh.mylibrary.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorManagerHelper implements SensorEventListener {

    // 传感器管理器
    private SensorManager sensorManager;
    // 重力感应监听器
    private OnShakeListener onShakeListener;
    // 上下文对象context
    private final Context context;
    //检测的时间间隔
    static final int UPDATE_INTERVAL = 100;
    //上一次检测的时间
    long mLastUpdateTime;
    //上一次检测时，加速度在x、y、z方向上的分量，用于和当前加速度比较求差。
    float mLastX, mLastY, mLastZ;
    //摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感。
    public int shakeThreshold = 500;
    //是否出发了摇一摇
    private int mShakeNum = 0;

    public SensorManagerHelper(Context context) {
        this.context = context;
        init();
    }

    /**
     * 开始检测
     */
    private void init() {
        // 获得传感器管理器
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        startListen();
    }

    //启动监听
    public void startListen() {
        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            if (sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    /**
     * 停止检测
     */
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    /**
     * 设置重力感应监听器
     */
    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    /**
     * 设置位置监听器
     */
    OnLocationListener onLocationListener;
    public void setOnLocationListener(OnLocationListener listener) {
        onLocationListener = listener;
    }

    /**
     * 设置位置监听器
     */
    OnSensorEventListener onSensorEventListener;
    public void setOnSensorEventListener(OnSensorEventListener listener) {
        onSensorEventListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;

        //位置回调
        if(onSensorEventListener != null){
            onSensorEventListener.onShake(event);
        }

        //坐标回调
        if(onLocationListener != null){
            //若传感器类型为加速度传感器（重力感应器）
            onLocationListener.onShake(x,y,z);
        }

        //计算移动距离判断是否为一次甩动
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;
        if (diffTime < UPDATE_INTERVAL) {
            return;
        }
        mLastUpdateTime = currentTime;
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        float delta = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / diffTime * 10000);
        // 当加速度的差值大于指定的阈值，认为这是一个摇晃
        if (delta > shakeThreshold && onShakeListener != null) {
            mShakeNum++;
            if (mShakeNum > 3) {
                mShakeNum = 0;
                onShakeListener.onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    /**
     * 摇晃监听接口
     */
    public interface OnShakeListener {
        void onShake();
    }

    /**
     * 位置监听接口
     */
    public interface OnSensorEventListener {
        void onShake(SensorEvent event);
    }

    /**
     * 位置监听接口
     */
    public interface OnLocationListener {
        void onShake(float x,float y,float z);
    }
}