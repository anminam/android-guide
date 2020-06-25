package com.example.anminamwebview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    // 웹뷰
    WebView view = null;

    // 연결할 URL
    String URL = "http://42.243.134.39:3000/";

    // 자바스크립트 인터페이스를 쓰려면 해당 어노테이션 필요
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        // 뷰를 가져와서 가져오기와서 변수에 등록
        view = (WebView) findViewById(R.id.webView);

        // 웹뷰클라이언트등록 - 웹에서 통신하고 로딩하고 등등 케치할 수있음
        view.setWebViewClient(new MiniWebViewClient());

        // 웹뷰와 인터페이스 실제로 연결 2번째인자는 namespace - window.mini .... 으로 호출 가능
        view.addJavascriptInterface(new MiniViewViewInterface(), "mini");

        // 셋팅 생성
        WebSettings setting = view.getSettings();
        // 콘솔창에서 사용할수있게 인에이블
        setting.setJavaScriptEnabled(true);

        //  url 연결 - 실제 연결이 이루어지며 화면에 해당 URL내용이 보임
        view.loadUrl(URL);

    }
}
