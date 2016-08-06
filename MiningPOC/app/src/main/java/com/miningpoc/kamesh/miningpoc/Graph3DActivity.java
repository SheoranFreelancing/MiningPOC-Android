package com.miningpoc.kamesh.miningpoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Graph3DActivity extends AppCompatActivity {

    private WebView graph3dView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph3_d);
        graph3dView = (WebView) findViewById(R.id.webView);
        graph3dView.getSettings().setJavaScriptEnabled(true);
        graph3dView.loadUrl("file:///android_asset/html/dot_colors.html");
        final String jsonDataString = getDataAsJsonString();
        graph3dView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                graph3dView.loadUrl("javascript:drawWithData('" + jsonDataString + "')");
            }
        });
    }

    private String getDataAsJsonString() {
        try {
            JSONArray jsonData = DotData.getJSONArrayForData(100);
            return jsonData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
