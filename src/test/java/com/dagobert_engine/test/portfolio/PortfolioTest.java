package com.dagobert_engine.test.portfolio;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.dagobert_engine.config.service.ConfigService;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.portfolio.model.MtGoxPermission;
import com.dagobert_engine.portfolio.model.Transaction;
import com.dagobert_engine.portfolio.model.Wallet;
import com.dagobert_engine.portfolio.service.MtGoxPortfolioService;
import com.dagobert_engine.test.util.AbstractTest;

@RunWith(Arquillian.class)
public class PortfolioTest extends AbstractTest {

	@Inject
	private MtGoxPortfolioService portfolio;

	@Inject
	private ConfigService config;

	@Override
	public void before() throws NamingException, SystemException,
			NotSupportedException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} // sleep to avoid ddos counter measures
		super.before();
	}

	@Test
	public void getPermissions() {
		MtGoxPermission[] perms = portfolio.getApiPermissions();
		assertThat(perms, is(notNullValue()));
		assertThat(perms.length, not(0));
	}

	@Test
	public void getBalance() {

		CurrencyData balance = portfolio
				.getBalance(config.getDefaultCurrency());
		assertThat(balance, is(notNullValue()));
		
		balance = portfolio
				.getBalance(CurrencyType.BTC);
		assertThat(balance, is(notNullValue()));
	}

	@Test
	public void getId() {
		String id = portfolio.getId();
		assertThat(id, is(notNullValue()));
		assertThat(id.equals(""), not(true));
	}

	@Test
	@Ignore // TODO: 47:19,766 SEVERE [com.dagobert_engine.core.service.MtGoxApiAdapter]  HTTP Error: 403, answer: {"result":"error","error":"Identification required to access private API","token":"login_error_invalid_nonce"}
	public void getLink() {
		String link = portfolio.getLink();
		assertThat(link, is(notNullValue()));
		assertThat(link.equals(""), not(true));
	}

	@Test
	public void getJoinDate() {
		Date date = portfolio.getJoinDate();
		assertThat(date, is(notNullValue()));
	}

	@Test
	public void getLastLogin() {
		Date lastLogin = portfolio.getLastLogin();
		assertThat(lastLogin, is(notNullValue()));
	}

	@Test
	public void getLocale() {
		Locale locale = portfolio.getLocale();
		assertThat(locale, is(notNullValue()));
	}

	@Test
	public void getMonthlyVolume() {
		CurrencyData volume = portfolio.getMonthlyVolume();
		assertThat(volume, is(notNullValue()));
	}

	@Test
	public void getFee() {
		double fee = portfolio.getTradeFee();
		assertThat(fee, not(0.0));
	}

	@Test
	public void getTransactions() {
		List<Transaction> transactions = portfolio.getTransactions(config
				.getDefaultCurrency());
		assertThat(transactions, is(notNullValue()));

		transactions = portfolio.getTransactions(CurrencyType.BTC);
		assertThat(transactions, is(notNullValue()));
	}

	@Test
	public void getWallets() {
		Map<CurrencyType, Wallet> wallets = portfolio.getWallets();
		assertThat(wallets, is(notNullValue()));
	}

}
