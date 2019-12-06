package com.quickrepair.customer.NetworkConnection;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Database.Token;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AsyncConnection {
	public static final MediaType TypeJson = MediaType.parse("application/json; charset=utf-8");
	private static String hostUrl;

	public AsyncConnection() {
		hostUrl = "http://192.168.0.105:45212";
	}

	public void getVerification(Account account, onResponseListener listener) {
		String type = Account.Type.values()[account.getAccountType()] == Account.Type.merchant ? "merchant" : "customer";
		HttpUrl url = HttpUrl
				.parse(hostUrl + "/verification/" + type + "/" + account.getAccountNumber())
				.newBuilder()
				.build();
		HttpConnection.getInstance().call(new Request.Builder().url(url).build(), listener);
	}

	public void getToken(Account account, onResponseListener listener) {
		String type = Account.Type.values()[account.getAccountType()] == Account.Type.merchant ? "merchant" : "customer";
		HttpUrl url = HttpUrl
				.parse(hostUrl + "/token/" + type + "/" + account.getAccountNumber())
				.newBuilder()
				.build();
		Request request = new Request.Builder().url(url).addHeader("password", account.getPassword()).build();
		/*JSONObject json = new JSONObject();
		json.put("type", "login");
		json.put("account", mAccountText.getText().toString());
		json.put("password", mPasswordText.getText().toString());
		json.put("account_type", mLoginTypeSwitch.isChecked() ? "merchant" : "customer");
		RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());*/
		HttpConnection.getInstance().call(request, listener);
	}

	public void getOrderList(Account account, Token token, onResponseListener listener) {
		String type = Account.Type.values()[account.getAccountType()] == Account.Type.merchant ? "merchant" : "customer";
		HttpUrl url = HttpUrl
				.parse(hostUrl + "/order_list/" + type + "/" + account.getId())
				.newBuilder()
				.build();
		Request request = new Request.Builder().url(url).addHeader("token", token.getTokenId()).build();
		HttpConnection.getInstance().call(request, listener);
	}

	public void getMerchantList(double longitude, double latitude, onResponseListener listener) {
		HttpUrl url = HttpUrl.parse(hostUrl + "/merchant_list/" + longitude + "/" + latitude)
				.newBuilder()
				.build();
		HttpConnection.getInstance().call(new Request.Builder().url(url).build(), listener);
	}

	public void getMerchantService(Account merchant, onResponseListener listener) {
		HttpUrl url = HttpUrl.parse(hostUrl + "/supported_service/" + merchant.getId())
				.newBuilder()
				.build();
		HttpConnection.getInstance()
				.call(new Request.Builder()
						.url(url)
						.build(),
						listener
				);
	}

	public void publishOrder(OrderItem order, Token token, onResponseListener listener) {
		try {
			JSONObject json = new JSONObject();
			json.put("receiver", order.getReceiver());
			json.put("committer", order.getCommitter());
			json.put("appliance", order.getApplianceType());
			json.put("detail", order.getDetail());
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpConnection.getInstance()
					.call(new Request.Builder()
							.url(hostUrl + "/order")
							.addHeader("token", token.getTokenId())
							.post(requestBody)
							.build(),
							listener
					);
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	public void updateServices(Account.Merchant.MerchantService service, Account account, Token token, onResponseListener listener) {
		try {
			JSONObject json = new JSONObject();
			json.put("max_distance", service.getDistance());
			JSONArray array = new JSONArray();
			for (String appliance : service.getAppliances())
				array.put(appliance);
			json.put("support_appliance", array);
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpUrl url = HttpUrl.parse(hostUrl + "/supported_service/" + account.getId())
					.newBuilder()
					.build();
			HttpConnection.getInstance()
					.call(new Request.Builder()
							.url(url)
							.addHeader("token", token.getTokenId())
							.put(requestBody)
							.build(),
							listener
					);
		} catch (JSONException je) {
			
		}
	}

	public void getOrderDetail(OrderItem order, Token token, onResponseListener listener) {
		HttpUrl url = HttpUrl.parse(hostUrl + "/supported_service/" + order.getId())
				.newBuilder()
				.build();
		HttpConnection.getInstance()
				.call(new Request.Builder()
						.url(url)
						.addHeader("token", token.getTokenId())
						.build(),
						listener
				);
	}

	public void receiveOrder(OrderItem order, Token token, onResponseListener listener) {
		try {
			HttpUrl url = HttpUrl.parse(hostUrl + "/order/" + order.getId())
					.newBuilder()
					.build();
			JSONObject json = new JSONObject();
			json.put("state", "receive");
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpConnection.getInstance()
					.call(new Request.Builder()
									.url(url)
									.addHeader("token", token.getTokenId())
									.addHeader("account", String.valueOf(order.getReceiver()))
									.put(requestBody)
									.build(),
							listener
					);
		} catch (JSONException je) {

		}
	}

	public void startRepairOrder(OrderItem order, Token token, onResponseListener listener)  {
		try {
			HttpUrl url = HttpUrl.parse(hostUrl + "/order/" + order.getId())
					.newBuilder()
					.build();
			JSONObject json = new JSONObject();
			json.put("state", "start_repair");
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpConnection.getInstance()
					.call(new Request.Builder()
									.url(url)
									.addHeader("token", token.getTokenId())
									.addHeader("account", String.valueOf(order.getReceiver()))
									.put(requestBody)
									.build(),
							listener
					);
		} catch (JSONException je) {

		}
	}

	public void endRepairOrder(OrderItem order, Token token, onResponseListener listener)  {
		try {
			HttpUrl url = HttpUrl.parse(hostUrl + "/order/" + order.getId())
					.newBuilder()
					.build();
			JSONObject json = new JSONObject();
			json.put("state", "end_repair");
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpConnection.getInstance()
					.call(new Request.Builder()
									.url(url)
									.addHeader("token", token.getTokenId())
									.addHeader("account", String.valueOf(order.getReceiver()))
									.put(requestBody)
									.build(),
							listener
					);
		} catch (JSONException je) {

		}
	}

	public void payForOrder(OrderItem order, Token token, onResponseListener listener)  {
		try {
			HttpUrl url = HttpUrl.parse(hostUrl + "/order/" + order.getId())
					.newBuilder()
					.build();
			JSONObject json = new JSONObject();
			json.put("state", "pay");
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpConnection.getInstance()
					.call(new Request.Builder()
									.url(url)
									.addHeader("token", token.getTokenId())
									.addHeader("account", String.valueOf(order.getCommitter()))
									.put(requestBody)
									.build(),
							listener
					);
		} catch (JSONException je) {

		}
	}

	public void rejectOrder(OrderItem order, Token token, onResponseListener listener)  {
		try {
			HttpUrl url = HttpUrl.parse(hostUrl + "/order/" + order.getId())
					.newBuilder()
					.build();
			JSONObject json = new JSONObject();
			json.put("state", "reject");
			RequestBody requestBody = RequestBody.create(AsyncConnection.TypeJson, json.toString());
			HttpConnection.getInstance()
					.call(new Request.Builder()
									.url(url)
									.addHeader("token", token.getTokenId())
									.addHeader("account", String.valueOf(order.getReceiver()))
									.put(requestBody)
									.build(),
							listener
					);
		} catch (JSONException je) {

		}
	}

	public interface onResponseListener {
		public void onResponse(Response response);
	}
}
