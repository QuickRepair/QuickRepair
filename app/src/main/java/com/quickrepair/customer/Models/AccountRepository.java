package com.quickrepair.customer.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Handler;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.AccountDao;
import com.quickrepair.customer.Database.Token;
import com.quickrepair.customer.NetworkConnection.AsyncConnection;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    private AccountDao accountDao;
    private Handler handler;

    public AccountRepository(AccountDao accountDao) {
        this.accountDao = accountDao;
        handler = new Handler();
    }

    void getAccount(Account account, Token token) {
        new GetAccountAsyncTask(accountDao, account, token).execute();
    }

    /*LiveData<Token> getToken(final Account account) {
        final MutableLiveData<Token> tokenMutableLiveData = new MutableLiveData<>();
        if (account != null) {
            refreshToken(account, tokenMutableLiveData);
        }
        return tokenMutableLiveData;
    }*/

    void refreshToken(final Account account, final Token token) {
        if (account != null) {
            new AsyncConnection().getToken(account, new AsyncConnection.onResponseListener() {
                @Override
                public void onResponse(Response response) {
                    try {
                        final String tokenStr = response.header("token");
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String id = jsonObject.getString("id");
                        account.setOnline(true);
                        account.setId(id);
                        token.setTokenId(tokenStr);
                        new InsertAccountAsyncTask(accountDao, account).execute();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    LiveData<Account.Merchant.MerchantService> getMerchantService(Account.Merchant account) {
        final MutableLiveData<Account.Merchant.MerchantService> serviceMutableLiveData = new MutableLiveData<>();
        final Account.Merchant.MerchantService service = new Account.Merchant.MerchantService();
//        service.setValue();
        new AsyncConnection().getMerchantService(account, new AsyncConnection.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    double distance = jsonObject.getDouble("max_distance");
                    JSONArray appliances = jsonObject.getJSONArray("supported_appliance");
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i != appliances.length(); ++i) {
                        list.add(appliances.getString(i));
                    }
                    service.setDistance(distance);
                    service.setAppliances(list);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            serviceMutableLiveData.setValue(service);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return serviceMutableLiveData;
    }

    LiveData<List<Account.Merchant>> getMerchants(double longtitude, double latitude) {
        final MutableLiveData<List<Account.Merchant>> merchantMutableLiveData = new MutableLiveData<>();
        final List<Account.Merchant> merchantList = new ArrayList<>();
        new AsyncConnection().getMerchantList(longtitude, latitude, new AsyncConnection.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray merchants = jsonObject.getJSONArray("merchants");
                    for (int i = 0; i != merchants.length(); ++i) {
                        String id = merchants.getString(i);
                        Account.Merchant m = new Account.Merchant();
                        m.setId(id);
                        merchantList.add(m);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            merchantMutableLiveData.setValue(merchantList);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return merchantMutableLiveData;
    }

    void getVerification(Account account) {
        new AsyncConnection().getVerification(account, new AsyncConnection.onResponseListener() {
            @Override
            public void onResponse(Response response) {
            }
        });
    }

    private class GetAccountAsyncTask extends AsyncTask<Void, Void, Void> {

        private AccountDao dao;
        private Account loginAccount;
        private Token token;

        GetAccountAsyncTask(AccountDao accountDao, Account account, Token token) {
            this.dao = accountDao;
            this.loginAccount = account;
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Account account = dao.get();
            this.loginAccount.setId(account.getId());
            this.loginAccount.setName(account.getName());
            this.loginAccount.setAccountNumber(account.getAccountNumber());
            this.loginAccount.setPassword(account.getPassword());
            this.loginAccount.setAccountType(account.getAccountType());
            refreshToken(this.loginAccount, this.token);
            return null;
        }
    }

    private class InsertAccountAsyncTask extends AsyncTask<Void, Void, Void> {

        private AccountDao dao;
        private Account account;

        InsertAccountAsyncTask(AccountDao accountDao, Account account) {
            this.dao = accountDao;
            this.account = account;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.insert(account);
            return null;
        }
    }
}
