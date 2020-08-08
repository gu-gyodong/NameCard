package com.project.namecard.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.project.namecard.R;

public class CardAddressView extends Activity {

    WebView addressimage;
    Handler handler;
    String addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_address_view);

        init_webView();
        handler = new Handler();
    }

    private void init_webView() {
        // WebView 설정
        addressimage = findViewById(R.id.addressimage);
        // JavaScript 허용
        addressimage.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        addressimage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        addressimage.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        addressimage.setWebChromeClient(new WebChromeClient());
        addressimage.loadUrl("https://rnryehd111.cafe24.com/BusinessCard/AddressApi.php");
    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    addressText = String.format("%s %s", arg2, arg3);
                    init_webView();
                    Intent intent = new Intent();
                    intent.putExtra("addressText", addressText);
                    setResult(201, intent);
                    finish();
                }
            });
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}