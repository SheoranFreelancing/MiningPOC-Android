package com.drill.ui;

import com.drill.sync.RetroHttpManager;
import com.drill.ws.parsers.GenericParser;

import retrofit.RetrofitError;

public interface LT_ResponseListener {
	public void onFailure(RetrofitError error);
	public void onSuccessResponse(RetroHttpManager manager);
	public void onSuccessResponse(RetroHttpManager manager, Object object);
	public void onSuccessResponse(RetroHttpManager manager, int requestType, Object object);
	public void onSuccessResponse(RetroHttpManager manager, GenericParser parser, int requestType, Object object);

}
