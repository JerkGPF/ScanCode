package com.gpfei.scancode;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.king.zxing.CaptureActivity;

/**
 * H5调用Android原生实现扫一扫
 */

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.custom_webView);
        initWebViewSettings();
        //访问网页
        mWebView.loadUrl("file:///android_asset/test.html");
        mWebView.requestFocusFromTouch();//获取焦点

        mWebView.addJavascriptInterface(new JsInterface(), "android");

    }

    private void initWebViewSettings() {
        //打开页面时， 自适应屏幕：
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        //支持缩放
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
    }

    //扫描回传值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //这是拿到解析扫描到的信息，并转成字符串
            String result = data.getStringExtra(KEY_RESULT);
            Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
            //解析扫到的二维码后就跳转页面
            String method = "javascript:testResult('" + result + "')";
            mWebView.loadUrl(method);
        }
    }

    private class JsInterface {
        // 安卓原生与h5互调方法定义
        @JavascriptInterface //js接口声明
        public void scanCode() {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class); //打开扫一扫
            startActivityForResult(intent, 1);
        }
    }
}