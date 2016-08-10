package com.drill.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drill.android.R;
import com.drill.ui.LT_BaseActivity;
import com.drill.utils.Constants;

public class SelectOptionActivity extends LT_BaseActivity {

    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option);

        //Get references to UI elements in the layout
        LinearLayout searchMerchant = (LinearLayout)findViewById(R.id.layout_search_merchant);
        LinearLayout listMerchant = (LinearLayout)findViewById(R.id.layout_list_merchant);
        LinearLayout nearByLayout = (LinearLayout)findViewById(R.id.layout_nearBy_merchant);
//        nearByLayout.setVisibility(BuildConfig.DEBUG?View.VISIBLE:View.GONE);
        LinearLayout logOutButton = (LinearLayout)findViewById(R.id.layout_logout);
        final String salesName = getIntent().getStringExtra(Constants.USER_EMAIL);
        TextView salesRepName = (TextView) findViewById(R.id.sales_rep_textView);
        salesRepName.setText(salesName);

        searchMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        nearByLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectOptionActivity.this, MapViewActivity.class);
                startActivity(intent);
            }
        });
        listMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectOptionActivity.this, MerchantListActivity.class);
                if (null != salesName && !salesName.isEmpty()) {
                    intent.putExtra(Constants.USER_EMAIL, salesName);
                }
                startActivity(intent);
            }
        });

        settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.edit().clear().commit();
                Intent intent = new Intent(SelectOptionActivity.this, LTLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


}
