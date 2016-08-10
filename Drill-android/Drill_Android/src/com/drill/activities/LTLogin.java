package com.drill.activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.drill.android.R;
import com.drill.sync.RetroHttpManager;
import com.drill.ui.LT_BaseActivity;
import com.drill.utils.Constants;

public class LTLogin extends LT_BaseActivity {

    private final int PICK_ACCOUNT_REQUEST_CODE = 1;
    private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        long lastLoginTime = settings.getLong(Constants.LAST_LOGIN_TIME, 0);
        String lastLoginUser = settings.getString(Constants.LAST_ACCOUNT_NAME,Constants.INVALID_LOGIN);
        long difference = System.currentTimeMillis() - lastLoginTime;
        Log.i("difference : ",""+difference/1000);
        if(difference < Constants.LOGIN_DURATION && !lastLoginUser.equalsIgnoreCase(Constants.INVALID_LOGIN)) {
            Intent intent = new Intent(LTLogin.this, SelectOptionActivity.class);
            intent.putExtra(Constants.USER_EMAIL, lastLoginUser);
            startActivity(intent);
            finish();
        }
        btnLogin = (Button) findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                startMapActivity();
            }
        });
        RetroHttpManager.setBaseServiceUrl(getResources().getString(R.string.base_sarvice_url));
    }
	
	private void getGoogleAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, PICK_ACCOUNT_REQUEST_CODE);
	}

    private void startMapActivity() {
        Intent intent = new Intent(LTLogin.this, MapViewActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String[] emailName  = accountName.split("@");
            if(emailName[1].equalsIgnoreCase(Constants.EMAIL_GROUPON)) {
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Constants.LAST_ACCOUNT_NAME, accountName).commit();
                editor.putLong(Constants.LAST_LOGIN_TIME, System.currentTimeMillis()).commit();
                Intent intent = new Intent(LTLogin.this, SelectOptionActivity.class);
                intent.putExtra(Constants.USER_EMAIL, accountName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,R.string.invalid_login_error,Toast.LENGTH_SHORT).show();
            }
        }
    }
}