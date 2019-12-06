package com.quickrepair.customer.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM Account")
    Account get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Account account);

    @Delete
    void delete(Account account);
}
