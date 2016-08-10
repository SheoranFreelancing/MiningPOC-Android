package com.drill;

import android.app.Application;

import com.drill.db.MerchantDatabase;
import com.drill.utils.LT_Utils;

public class L3Application extends Application {

	public static L3Application appContext;
	private MerchantDatabase merchantDBInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		LT_Utils.appContext = appContext;
		LT_Utils.isReleaseBuild();
		merchantDBInstance = MerchantDatabase.getInstance();
		MerchantDatabase.initializeMerchantDatabaseHandler(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		merchantDBInstance.close();
	}

	public synchronized static L3Application getApplication() {
		return appContext;
	}
}
