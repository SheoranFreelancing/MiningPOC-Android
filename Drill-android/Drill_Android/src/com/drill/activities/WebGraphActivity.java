package com.drill.activities;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.drill.adaptors.ObservableWebView;
import com.drill.android.R;
import com.drill.data.DotData;
import com.drill.sync.RetroHttpManager;
import com.drill.ui.LT_BaseActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.RetrofitError;

import static com.drill.utils.Constants.LATLNG_LIST;

public class WebGraphActivity extends LT_BaseActivity {

    private ObservableWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_graph);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setIcon(
                new ColorDrawable(getResources().getColor(R.color.transparent)));
        TextView title = (TextView) findViewById(R.id.activityTitle);
        title.setText("3D Graph");
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button editButton = (Button) findViewById(R.id.saveButton);
        editButton.setText(R.string.edit);
        editButton.setVisibility(View.INVISIBLE);

        webview = (ObservableWebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/html/plotly.html");
        final String jsonDataString = getDataAsJsonString();
        android.util.Log.e("Data:", ">" + jsonDataString);
        webview.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webview.loadUrl("javascript:drawWithData('" + jsonDataString + "')");
            }
        });
    }

    private String getDataAsJsonString() {
        try {
            DisplayMetrics metrics = getApplication().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            ArrayList<LatLng> latlangList =  (ArrayList<LatLng>)getIntent().getSerializableExtra(LATLNG_LIST);
            JSONObject jsonData = DotData.getJSONArrayForData(latlangList);
            jsonData.put("chartWidth", width);
            jsonData.put("chartHeight", height);
            return jsonData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onFailure(RetrofitError error) {
        super.onFailure(error);
    }

    @Override
    public void onSuccessResponse(RetroHttpManager manager) {
        super.onSuccessResponse(manager);
    }
}
