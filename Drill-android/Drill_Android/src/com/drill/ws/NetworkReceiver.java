package com.drill.ws;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.drill.utils.LTLog;

public class NetworkReceiver extends BroadcastReceiver {
	public static final String TAG = "NetworkReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		boolean isNetworkDown = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

		// ConnectivityManager connectivityManager = (ConnectivityManager)
		// context
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo activeNetInfo = connectivityManager
		// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// boolean isConnected = activeNetInfo != null
		// && activeNetInfo.isConnected();

		if (isNetworkDown) {
			LTLog.d(TAG, "onReceive: NOT connected");
			// context.stopService(new Intent(context, UpdaterService.class));
		} else {
			LTLog.d(TAG, "onReceive: connected");
			// context.startService(new Intent(context, UpdaterService.class));
//			if (LoginData.getInstance().isLoggedIn()) {
//				if ((PostsData.getInstance().getDeferredReqCount() > 0)
//						&& ((!PostsData.MAKING_DEFERRED_REQUEST_NOW))) {
//					PostsData.getInstance().makePersistedRequests();
//				}
//			}
		}
	}

}
