package com.har.quickrepairforandroid.AsyncTransmissions;

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

	public void getMethod(Request request, final AsyncTransmissionTask handler) {
		Call call = mClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				handler.handler(response);
			}
		});
	}

	public void postMethod(Request request, final AsyncTransmissionTask task) {
		Call call = mClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				task.handler(response);
			}
		});
	}
}