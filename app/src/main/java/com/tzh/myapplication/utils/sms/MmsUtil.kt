package com.tzh.myapplication.utils.sms

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager

object MmsUtil {
    fun sendMms(context: Context, fileUri: Uri, recipient: String,content : String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        val mimeType = "image/*"
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        sendIntent.putExtra("sms_body", content)
        sendIntent.putExtra("address", recipient)
        sendIntent.type = mimeType
        val packageManager = context.packageManager
        val resolvedIntentActivities = packageManager.queryIntentActivities(sendIntent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolvedIntentInfo in resolvedIntentActivities) {
            val packageName = resolvedIntentInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                fileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        context.startActivity(Intent.createChooser(sendIntent, "Send MMS"))
    }
}