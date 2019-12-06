package com.quickrepair.customer.Models;

import android.content.Context;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.AccountDatabase;
import com.quickrepair.customer.Database.Token;

public class LoginAccount {

    private static LoginAccount instance;
    Account account;
    Token token;
    AccountRepository repository;

    public static LoginAccount getInstance(Context context) {
        if (instance == null)
            instance = new LoginAccount(context);
        return instance;
    }

    public Account getAccount() {
        return account;
    }

    public Token getToken() {
        return token;
    }

    private LoginAccount(Context context) {
        repository = new AccountRepository(AccountDatabase.getInstance(context).AccountDao());
        account = new Account();
        token = new Token();
        repository.getAccount(account, token);
    }
}
