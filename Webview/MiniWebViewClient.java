package com.example.forkimjs;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MiniWebViewClient extends WebViewClient {

    /**
     * 웹뷰 안에 컨텐츠 로딩이 끝난 후
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        // 토스트 띄우기
        Toast toast = Toast.makeText(view.getContext(), "로딩완료", Toast.LENGTH_LONG);
        toast.show();

    }
}
