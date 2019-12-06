package com.quickrepair.customer.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

@Entity
public class Account implements Serializable {
    public enum Type {merchant, customer}

    @NonNull
    @PrimaryKey
    private String id = "-1";
    private String name;
    private String accountNumber;
    private String password;
    private int accountType;
    @Ignore
    private boolean online;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public static class Customer extends Account {
    }

    public static class Merchant extends Account {

        private MerchantService service;

        public MerchantService getService() {
            return service;
        }

        public void setService(MerchantService service) {
            this.service = service;
        }

        public static class MerchantService implements Serializable {

            private double mDistance;
            private List<String> mAppliances;

            public double getDistance() {
                return mDistance;
            }

            public List<String> getAppliances() {
                return mAppliances;
            }

            public void setAppliances(List<String> appliances) {
                mAppliances = appliances;
            }

            public void setDistance(double distance) {
                mDistance = distance;
            }
        }
    }
}
