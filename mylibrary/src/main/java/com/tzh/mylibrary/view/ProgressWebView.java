package com.tzh.mylibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tzh.mylibrary.R;

/**
 * 带进度条的webview
 *
 * @author Jarry
 */
public class ProgressWebView extends WebView {

    private Context context;
    public ProgressBar progressBar;

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void init(final Context context) {
        this.context = context;

        progressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 4, 0, 0);
        progressBar.setLayoutParams(lp);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_web_progress_bar));
        this.addView(progressBar);
        WebSettings webSettings = getSettings();
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 屏幕自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);

        webSettings.setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    final String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

}