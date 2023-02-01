package com.tzh.myapplication.view

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class GyroscopeManager : SensorEventListener {
    private val list_views: MutableList<XImageView> = ArrayList()
    private var sensorManager: SensorManager? = null
    private var endTimestamp: Long = 0
    private var angleX = 0.0
    private var angleY = 0.0

    //0到π/2
    private val maxAngle = Math.PI / 3
    fun addView(xImageView: XImageView?) {
        if (xImageView != null && !list_views.contains(xImageView)) {
            list_views.add(xImageView)
        }
    }

    fun register(context: Context) {
        if (sensorManager == null) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }
        sensorManager?.let {
            val mSensor: Sensor = it.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            it.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST)
            endTimestamp = 0
            angleX = 0.0
            angleY = 0.0
        }
    }

    fun unregister() {
        sensorManager?.let {
            it.unregisterListener(this)
            sensorManager = null
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (endTimestamp == 0L) {
            endTimestamp = event.timestamp
            return
        }
        angleX += event.values[0] * (event.timestamp - endTimestamp) * NS2S
        angleY += event.values[1] * (event.timestamp - endTimestamp) * NS2S
        if (angleX > maxAngle) {
            angleX = maxAngle
        }
        if (angleX < -maxAngle) {
            angleX = -maxAngle
        }
        if (angleY > maxAngle) {
            angleY = maxAngle
        }
        if (angleY < -maxAngle) {
            angleY = -maxAngle
        }
        for (view in list_views) {
            view.update(angleY / maxAngle, angleX / maxAngle)
        }
        endTimestamp = event.timestamp
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    companion object {
        // 将纳秒转化为秒
        private const val NS2S = 1.0f / 1000000000.0f
    }
}