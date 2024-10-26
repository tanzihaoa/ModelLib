package com.tzh.mylibrary.activity.web

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.gyf.immersionbar.ImmersionBar
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityWebViewLibBinding
import com.tzh.mylibrary.dialog.HintDialog
import com.tzh.mylibrary.util.OnPermissionCallBackListener
import com.tzh.mylibrary.util.PermissionXUtil
import com.tzh.mylibrary.util.general.PermissionDetectionUtil
import com.tzh.mylibrary.util.toDefault


class WebLibActivity : XBaseBindingActivity<ActivityWebViewLibBinding>(R.layout.activity_web_view_lib) {

    companion object {
        @JvmStatic
        fun start(context: Context,url : String,title : String = "") {
            if (!url.contains("http://") && !url.contains("https://")) {
                return
            }

            val intent = Intent(context, WebLibActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }

        var filePathCallback: ValueCallback<Array<Uri>>? = null
        const val REQUEST_SELECT_FILE = 100
    }


    private val mUrl by lazy {
        intent?.getStringExtra("url").toDefault("")
    }

    private val mTitle by lazy {
        intent?.getStringExtra("title").toDefault("")
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()
        binding.titleBar.setTitleTxt(mTitle)
        binding.webBrowser.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webBrowser.setBackgroundColor(Color.parseColor("#ffffff")) //ok 不会闪黑屏

        binding.webBrowser.webChromeClient = MyWebChromeClient(this,binding.webBrowser.progressBar)

        val settings = binding.webBrowser.settings

        settings.loadsImagesAutomatically = true // 支持自动加载图片

        settings.useWideViewPort = true // 设置webView推荐使用的窗口，使html界面自适应屏幕

        settings.setSupportZoom(true) // 支持缩放

        //允许webview对文件的操作
        settings.allowFileAccessFromFileURLs = true

        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.allowUniversalAccessFromFileURLs = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        binding.webBrowser.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        binding.webBrowser.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                if(mTitle.isEmpty()){
                    val webTitle: String = binding.webBrowser.title.toDefault("")
                    binding.titleBar.setTitleTxt(webTitle)
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (!isLoadUrl) {
                    isLoadUrl = true
                    view?.loadUrl(url.toDefault(""))
                }
            }
        }
        binding.webBrowser.isLongClickable = true
        binding.webBrowser.isScrollbarFadingEnabled = true
        binding.webBrowser.isDrawingCacheEnabled = true

        binding.webBrowser.loadUrl(mUrl)

    }

    override fun initData() {

    }

    override fun onCloseActivity() {

    }

    var isLoadUrl = false

    class MyWebChromeClient(val activity : WebLibActivity, val progressBar : ProgressBar) : WebChromeClient() {
        // 配置权限（同样在WebChromeClient中实现）
        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback) {
            callback.invoke(origin, true, false)
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar.progress = newProgress
            if (newProgress == 100) {
                progressBar.postDelayed({ progressBar.visibility = View.GONE }, 1000)
            } else {
                if (progressBar.visibility != View.VISIBLE) {
                    progressBar.visibility = View.VISIBLE
                }
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
            Companion.filePathCallback = filePathCallback
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                HintDialog(activity, object : HintDialog.HintDialogListener{
                    override fun cancel() {

                    }

                    override fun ok() {
                        //权限未授予，需要向用户请求权限
                        PermissionDetectionUtil.getPermission(activity,object : PermissionDetectionUtil.DetectionListener{
                            override fun ok() {

                            }

                            override fun cancel() {

                            }
                        })
                    }
                }).show("即将申请读取权限用于发送照片或文件，是否确定申请?")
                return false
            } else {
                //权限已授予，可以进行文件读取操作
                val intent = fileChooserParams!!.createIntent()
                try {
                    activity.startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (ex: ActivityNotFoundException) {
                    filePathCallback!!.onReceiveValue(null)
                    return false
                }
            }
            return true
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
            activity.runOnUiThread {
                if (request?.origin.toString().startsWith("https://") || request?.origin.toString().startsWith("http://")) {
                    if(request?.resources?.indexOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE).toDefault(-1) >= 0){
                        PermissionXUtil.requestAnyPermission(activity, Manifest.permission.RECORD_AUDIO,object : OnPermissionCallBackListener {
                            override fun onAgree() {
                                request?.grant(request.resources)
                            }

                            override fun onDisAgree() {

                            }
                        })
                    }else{
                        request?.grant(request.resources)
                    }
                }
            }
        }

        override fun onExceededDatabaseQuota(url: String?, databaseIdentifier: String?, quota: Long,
            estimatedDatabaseSize: Long, totalQuota: Long, quotaUpdater: WebStorage.QuotaUpdater?) {
            quotaUpdater?.updateQuota(estimatedDatabaseSize * 2);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 选择图片或文件回调
        if (requestCode == REQUEST_SELECT_FILE) {
            if (filePathCallback == null) {
                return
            }
            if (data == null || resultCode != RESULT_OK) {
                filePathCallback?.onReceiveValue(null)
                return
            }
            val uris = arrayOf(data.data!!)
            filePathCallback?.onReceiveValue(uris)
            filePathCallback = null
        }
    }
}