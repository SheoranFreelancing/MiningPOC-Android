package com.drill.activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.drill.adaptors.MerchantsAdapter;
import com.drill.db.MerchantDatabase;
import com.drill.android.R;
import com.drill.responsemodel.Merchant;
import com.drill.sync.RetroHttpManager;
import com.drill.ui.LT_BaseActivity;
import com.drill.utils.Constants;

import java.util.ArrayList;

import retrofit.RetrofitError;

/**
 * Created by groupon on 6/17/15.
 */

public class MerchantListActivity extends LT_BaseActivity {

    private ArrayList<Merchant> merchantList;
    private ListView merchantListView;
    private MerchantsAdapter merchantsAdapter;
    private LinearLayout noMerchantMessageLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_merchant);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setIcon(
                new ColorDrawable(getResources().getColor(R.color.transparent)));
        TextView title = (TextView) findViewById(R.id.activityTitle);
        title.setText(R.string.merchants);
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setVisibility(View.GONE);

        merchantList = MerchantDatabase.getInstance().getAllMerchants();
        merchantListView = (ListView) findViewById(R.id.listViewMain);
        merchantsAdapter = new MerchantsAdapter(this,R.layout.merchant_row, merchantList);
        merchantListView.setAdapter(merchantsAdapter);
        merchantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String merchantId = merchantsAdapter.getItem(i).get_id()    ;
//                Intent intent = new Intent(MerchantListActivity.this, MerchantDetailsActivity.class);
//                intent.putExtra(Constants.MERCHANT_ID, merchantId);
//                startActivity(intent);

            }
        });
        noMerchantMessageLayout = (LinearLayout)findViewById(R.id.no_merchant_message_layout);
        noMerchantMessageLayout.setVisibility(View.INVISIBLE);
        Button addNewMerchant = (Button) findViewById(R.id.add_new_merchant);
        addNewMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MerchantListActivity.this, AddMerchantActivity.class);
//                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
//                String lastLoginUser = settings.getString(Constants.LAST_ACCOUNT_NAME, Constants.INVALID_LOGIN);
//                intent.putExtra(Constants.USER_EMAIL, lastLoginUser);
//                startActivity(intent);
            }
        });
        if (getIntent() != null) {
            handleIntent(getIntent());
        }
        RetroHttpManager.create(this, RetroHttpManager.REQUEST_GET_MERCHANTS).sendRequest("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMerchants();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                merchantsAdapter.getFilter().filter(s);
                return true;
            }
        });

        return true;
    }

    private void handleIntent(Intent intent) {
        // Special processing of the incoming intent only occurs if the if the action specified
        // by the intent is ACTION_SEARCH.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // SearchManager.QUERY is the key that a SearchManager will use to send a query string
            // to an Activity.
            String query = intent.getStringExtra(SearchManager.QUERY);
//            Toast.makeText(this, getResources().getString(R.string.search_for) + query, Toast.LENGTH_SHORT).show();
            merchantsAdapter.getFilter().filter(query.toString());
        }
    }


    @Override
    public void onFailure(RetrofitError error) {
        super.onFailure(error);
        updateMerchants();
    }

    @Override
    public void onSuccessResponse(RetroHttpManager manager) {
        super.onSuccessResponse(manager);
        updateMerchants();
    }

    private void updateMerchants() {
        merchantList.clear();
        merchantList.addAll(MerchantDatabase.getInstance().getAllMerchants());
        merchantsAdapter.notifyDataSetChanged();
        if(merchantList.size() == 0) {
            merchantListView.setVisibility(View.GONE);
            noMerchantMessageLayout.setVisibility(View.VISIBLE);
        } else {
            noMerchantMessageLayout.setVisibility(View.GONE);
            merchantListView.setVisibility(View.VISIBLE);
        }
    }



}
