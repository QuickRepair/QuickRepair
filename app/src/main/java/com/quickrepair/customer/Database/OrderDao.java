package com.quickrepair.customer.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM OrderItem")
    List<OrderItem> getAll();

    @Insert
    void insert(OrderItem order);

    @Delete
    void delete(OrderItem order);
}
