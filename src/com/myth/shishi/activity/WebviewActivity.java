package com.myth.shishi.activity;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;

public class WebviewActivity extends BaseActivity
{

    private WebView webView;

    final String url = "http://wapbaike.baidu.com/search?submit=%E8%BF%9B%E5%85%A5%E8%AF%8D%E6%9D%A1&uid=bk_1345472299_718&ssid=&st=1&bd_page_type=1&bk_fr=srch";

    private ArrayList<String> loadHistoryUrls = new ArrayList<String>();

    private String originUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        setBottomGone();

        webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings(); // webView:
        // 类WebView的实例
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {

                loadHistoryUrls.add(url);
                webView.loadUrl(url);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                handler.proceed(); // 接受所有网站的证书
            }
        });

        if (getIntent().hasExtra("string"))
        {
            String s = getIntent().getStringExtra("string");
            originUrl = getUrl(s);
        }
        else
        {
            originUrl = getHelpUrl();
        }

        loadHistoryUrls.add(originUrl);
        webView.loadUrl(originUrl);

    }

    private String getUrl(String s)
    {
        return url + "&word=" + URLEncoder.encode(s);
    }

    private String getHelpUrl()
    {
        webView.setBackgroundColor(0);

        return "file:///android_asset/intro.html";
    }


}
