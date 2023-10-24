package com.tzh.myapplication.utils

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock


object WakeLockUtil {
    /**
     * 点亮屏幕
     *
     * @param timeout The timeout after which to release the wake lock, in milliseconds.
     */
    @JvmStatic
    fun acquireWakeLock(context: Context, timeout: Long): WakeLock? {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            ?: return null
        val wakeLock = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ON_AFTER_RELEASE,
            context.javaClass.name
        )
        wakeLock.acquire(timeout)
        return wakeLock
    }

    fun release(wakeLock: WakeLock) {
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}
