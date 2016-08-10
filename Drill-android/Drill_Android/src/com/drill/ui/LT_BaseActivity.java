package com.drill.ui;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drill.android.R;
import com.drill.sync.RetroHttpManager;
import com.drill.utils.LTLog;
import com.drill.ws.parsers.GenericParser;

import retrofit.RetrofitError;

public class LT_BaseActivity extends Activity implements LT_ResponseListener {

    public static final int FULLSCREEN_PROGRESS = 1;
    public static final int BOTTOM_PROGRESS = 2;

	private RelativeLayout rlProgress = null;
	private RelativeLayout rlFullScreenProgress = null;

	public void onFailure(Exception error) {
		LTLog.e("LT_BaseActivity", error.getMessage() != null ? error.getMessage() : error.toString());
		Toast.makeText(this, error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_SHORT).show();
		hideTaskProgress();
	}



    public void showProgressView() {
        showProgressView(FULLSCREEN_PROGRESS);
    }

    public void showProgressView(int type) {
        showProgressView(FULLSCREEN_PROGRESS, "Refreshing... Please wait.");
    }
    public void showProgressView(int type, String loaderMessage) {
        switch (type) {
            case FULLSCREEN_PROGRESS: showFullScreenProgress(loaderMessage); break;
            case BOTTOM_PROGRESS: showDefaultProgress(loaderMessage); break;
        }
    }

	protected void showDefaultProgress(final String loaderMessage) {
		if (null == rlProgress) {
			rlProgress = new RelativeLayout(LT_BaseActivity.this);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rlProgress.inflate(LT_BaseActivity.this,
					R.layout.list_loader_layout, rlProgress);
			LinearLayout llProgressHolder = (LinearLayout) rlProgress
					.findViewById(R.id.llListLoader);
			llProgressHolder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hideAllTaskNotification();
				}
			});
			LT_BaseActivity.this.addContentView(rlProgress, rlp);
		}
		if (null != loaderMessage) {
			TextView msg = (TextView) rlProgress.findViewById(R.id.loadingMsg);
			if (null != msg) {
				msg.setText(loaderMessage);
			}
		}
		rlProgress.setVisibility(View.VISIBLE);
	}

	protected void showFullScreenProgress(final String loaderMessage) {
		runOnUiThread(new Runnable() {

			public void run() {
				if (null == rlFullScreenProgress) {
					rlFullScreenProgress = new RelativeLayout(
							LT_BaseActivity.this);
					RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
					rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					rlFullScreenProgress.inflate(LT_BaseActivity.this,
							R.layout.fullscreen_loader_layout,
							rlFullScreenProgress);
//					rlFullScreenProgress
//							.setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									hideAllTaskNotification();
//											LT_BaseActivity.this.finish();
//								}
//							});
					LT_BaseActivity.this.addContentView(rlFullScreenProgress,
							rlp);
				}
				if (null != loaderMessage) {
					TextView msg = (TextView) rlFullScreenProgress
							.findViewById(R.id.loadingMsg);
					if (null != msg) {
						msg.setText(loaderMessage);
					}
				}

				if (null != rlProgress) {
					rlProgress.setVisibility(View.GONE);
				}
				rlFullScreenProgress.setVisibility(View.VISIBLE);
			}
		});
	}

	public void hideAllTaskNotification() {
		hideTaskProgress();
	}

	public void hideTaskProgress() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (null != rlProgress) {
					rlProgress.setVisibility(View.GONE);
				}
				if (null != rlFullScreenProgress) {
					rlFullScreenProgress.setVisibility(View.GONE);
				}
			}
		});
	}

    public void hideKeyboard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboard();
    }

    @Override
    public void finish() {
        super.finish();
        hideKeyboard();
    }

	public void onFailure(RetrofitError error) {
		LTLog.e("LT_BaseActivity", error.getMessage());
		Toast.makeText(this, error.getCause() +"-->" +error.getMessage(), Toast.LENGTH_LONG).show();
		hideTaskProgress();
	}



	@Override
	public void onSuccessResponse(RetroHttpManager manager) {
		hideTaskProgress();
	}

	@Override
	public void onSuccessResponse(RetroHttpManager manager, Object object) {
		hideTaskProgress();
	}

	@Override
	public void onSuccessResponse(RetroHttpManager manager, int requestType, Object object) {
		hideTaskProgress();
	}

	@Override
	public void onSuccessResponse(RetroHttpManager manager, GenericParser parser, int requestType, Object object) {
		hideTaskProgress();
	}
}
