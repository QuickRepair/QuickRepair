package com.quickrepair.customer.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.OrderItem;
import com.quickrepair.customer.Database.Token;

import java.util.List;

public class OrderViewModel extends ViewModel {

    private OrderRepository orderRepository;

    private LiveData<List<OrderItem>> mOrderList;
    private LiveData<OrderItem> mOrder;

    public OrderViewModel(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public LiveData<List<OrderItem>> getOrderList(Account account, Token token) {
        if (account != null && token != null)
            mOrderList = orderRepository.getOrders(account, token);
        return mOrderList;
    }

    public LiveData<OrderItem> getOrder(Account account, Long order) {
        if (account != null)
            mOrder = orderRepository.getOrder(account.getId());
        return mOrder;
    }

    public void newOrder(OrderItem order, Token token) {
        orderRepository.newOrder(order, token);
    }

    public void orderStateUpdate(Token token, OrderItem.State state) {
        orderRepository.updateOrder(mOrder.getValue(), token, state);
    }

    public static class OrderViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private OrderRepository orderRepository;

        public OrderViewModelFactory(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new OrderViewModel(orderRepository);
        }
    }
}
