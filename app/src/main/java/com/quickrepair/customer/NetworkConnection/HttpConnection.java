package com.quickrepair.customer.NetworkConnection;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpConnection {

	private static OkHttpClient mClient = new OkHttpClient();
	private static HttpConnection mConnection = new HttpConnection();

	public static HttpConnection getInstance() {
		return mConnection;
	}

	public void call(Request request, final AsyncConnection.onResponseListener handler) {
		Call call = mClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Response response) {
				handler.onResponse(response);
			}
		});
	}
}