package com.miningpoc.kamesh.miningpoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Graph3DActivity extends AppCompatActivity {

    private WebView graph3dView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph3_d);
        graph3dView = (WebView) findViewById(R.id.webView);
        graph3dView.getSettings().setJavaScriptEnabled(true);
        graph3dView.loadUrl("file:///android_asset/html/dot_colors.html");
    }
}
