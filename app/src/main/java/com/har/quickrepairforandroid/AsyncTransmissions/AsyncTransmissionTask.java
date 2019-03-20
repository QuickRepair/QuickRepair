package com.har.quickrepairforandroid.AsyncTransmissions;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public interface AsyncTransmissionTask {
	public static final MediaType TypeJson = MediaType.parse("application/json; charset=utf-8");
	public void execute();
	public Request makeRequest();
	public void handler(Response response);
}
