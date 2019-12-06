package com.quickrepair.customer.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.OrderDao;
import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Database.Token;
import com.quickrepair.customer.NetworkConnection.AsyncConnection;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private OrderDao orderDao;
    private Handler handler;

    public OrderRepository(OrderDao orderDao) {
        this.orderDao = orderDao;
        this.handler = new Handler();
    }

    LiveData<List<OrderItem>> getOrders(Account account, Token token) {
        final MutableLiveData<List<OrderItem>> order = new MutableLiveData<>();
        final List<OrderItem> orderItems = new ArrayList<OrderItem>();
        new AsyncConnection().getOrderList(account, token, new AsyncConnection.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray orderArray = jsonObject.getJSONArray("order_list");
                    for (int i = 0; i != orderArray.length(); ++i) {
                        JSONObject item = orderArray.getJSONObject(i);
                        String date = item.getString("create_date");
                        String type = item.getString("appliance");
                        int id = item.getInt("id");
                        orderItems.add(new OrderItem(id, date, type));
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            order.setValue(orderItems);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return order;
    }

    LiveData<OrderItem> getOrder(String orderId) {
        MutableLiveData<OrderItem> order = new MutableLiveData<>();
        order.setValue(new OrderItem());
        return order;
    }

    void newOrder(OrderItem orderItem, Token token) {
        new AsyncConnection().publishOrder(orderItem, token, new AsyncConnection.onResponseListener() {
            @Override
            public void onResponse(Response response) {

            }
        });
    }

    void updateOrder(OrderItem orderItem, Token token, OrderItem.State state) {
        switch (state) {
            case unreceived:
                break;
            case received:
                new AsyncConnection().receiveOrder(orderItem, token, new AsyncConnection.onResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                    }
                });
                break;
            case repairing:
                new AsyncConnection().startRepairOrder(orderItem, token, new AsyncConnection.onResponseListener() {
                    @Override
                    public void onResponse(Response response) {

                    }
                });
                break;
            case paying:
                new AsyncConnection().endRepairOrder(orderItem, token, new AsyncConnection.onResponseListener() {
                    @Override
                    public void onResponse(Response response) {

                    }
                });
                break;
            case finished:
                new AsyncConnection().payForOrder(orderItem, token, new AsyncConnection.onResponseListener() {
                    @Override
                    public void onResponse(Response response) {

                    }
                });
                break;
            case reject:
                new AsyncConnection().rejectOrder(orderItem, token, new AsyncConnection.onResponseListener() {
                    @Override
                    public void onResponse(Response response) {

                    }
                });
                break;
        }
    }
}
