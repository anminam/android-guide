package com.example.anminamwebview;

import android.os.Environment;
import android.webkit.JavascriptInterface;

public class MiniViewViewInterface {

    /**
     * window.mini.getNumber 하면 호출가능(비동기는 안해봄)
     * @return
     */
    @JavascriptInterface
    public int getNumber(){
//        String path = "android.resource://" + this.getPackageName() + "/" + R.raw.video1;
        return 1;

    }

}
