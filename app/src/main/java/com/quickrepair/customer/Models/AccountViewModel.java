package com.quickrepair.customer.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.quickrepair.customer.Database.Account;
import com.quickrepair.customer.Database.Token;

import java.util.List;

public class AccountViewModel extends ViewModel {

	private AccountRepository accountRepository;

	/*private final MutableLiveData<Account> mAccount = new MutableLiveData<>();
	private LiveData<Token> mToken;*/
	private LiveData<Account.Merchant.MerchantService> serviceIfAccountIsMerchant;
	private LiveData<List<Account.Merchant>> merchantList;
	private LiveData<Account.Merchant.MerchantService> serviceOfMerchant;

	public AccountViewModel(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;/*
		mToken = accountRepository.getToken(null);
		if (mAccount.getValue() == null)
			accountRepository.getAccount(mAccount, mToken);*/
//		serviceIfAccountIsMerchant = accountRepository.getMerchantService(mAccount.getValue());
	}

	/*public LiveData<Account> getAccount() {
		//if (mAccount == null)
		return mAccount;
	}*/

	/*public void setAccount(Account account) {
		mAccount.setValue(account);
		accountRepository.refreshAccount(mAccount.getValue());
	}*/

	/*public LiveData<Token> getToken() {
		//if (mToken == null)
		return mToken;
	}*/

	/*public void setToken(Token token) {
		((MutableLiveData)mToken).setValue(token);
	}*/

	public LiveData<List<Account.Merchant>> getMerchantList() {
		merchantList = accountRepository.getMerchants(0, 0);
		return merchantList;
	}

	public LiveData<Account.Merchant.MerchantService> getServiceIfIsMerchant(Account.Merchant  account) {
		//if (serviceIfAccountIsMerchant == null)
		if (account != null)
			return serviceIfAccountIsMerchant;
		else
			return null;
	}

	public LiveData<Account.Merchant.MerchantService> getMerchantService(Account.Merchant merchant) {
		serviceOfMerchant = accountRepository.getMerchantService(merchant);
		return serviceOfMerchant;
	}

	public void getVerification(Account account) {
		accountRepository.getVerification(account);
	}

	public void checkPassword(Account account, Token token) {
		accountRepository.refreshToken(account, token);
	}

	/*public void setMerchantService(Merchant.MerchantService merchantService) {
		((MutableLiveData)serviceIfAccountIsMerchant).setValue(merchantService);
	}*/

	/*private void getAccountIfAvailable() {
		AccountBaseHelper helper = new AccountBaseHelper();
		AccountCursorWrapper cursor = helper.queryAccount();
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
			Account account = cursor.getIsCustomer() ? new Customer() : new Merchant();
			account.setAccountNumber(cursor.getAccount());
			account.setPassword(cursor.getPassword());
			account.setAccountType(cursor.getIsCustomer() ? Account.Type.customer : Account.Type.merchant);
			mAccount = new MutableLiveData<>();
			mAccount.setValue(account);
		}
	}*/

	public static class AccountViewModelFactory extends ViewModelProvider.NewInstanceFactory {
		private AccountRepository accountRepository;

		public AccountViewModelFactory(AccountRepository accountRepository) {
			this.accountRepository = accountRepository;
		}

		@NonNull
		@Override
		public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
			return (T) new AccountViewModel(accountRepository);
		}

		/*public static AccountViewModel getViewModel(Fragment fragment, Context context) {
			return ViewModelProviders.of(
					fragment,
					new AccountViewModel.AccountViewModelFactory(
							new AccountRepository(AccountDatabase.getInstance(context.getApplicationContext()).AccountDao())
					)
			).get(AccountViewModel.class);
		}*/
	}
}
