package com.dagobert_engine.test.statistics;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.dagobert_engine.core.model.AdvancedCurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyData;
import com.dagobert_engine.core.model.CurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.statistics.service.MtGoxStatisticsService;
import com.dagobert_engine.test.util.AbstractTest;

@RunWith(Arquillian.class)
public class StatisticsTest extends AbstractTest {
	
	@Inject
	private MtGoxStatisticsService statistics;
	
	
	private static final CurrencyType DEFAULT_CURRENCY = CurrencyType.USD;
	


	
	@Override
	public void before() throws NamingException, SystemException, NotSupportedException {
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
	public void getAdvancedStats() {
		AdvancedCurrencyStatistics stats = statistics.getAdvancedStatistics(DEFAULT_CURRENCY);
		assertThat(stats, is(notNullValue()));
		assertThat(stats.getAvg(), is(notNullValue()));
		assertThat(stats.getBuy(), is(notNullValue()));
		assertThat(stats.getSell(), is(notNullValue()));
		assertThat(stats.getHigh(), is(notNullValue()));
		assertThat(stats.getLow(), is(notNullValue()));
		assertThat(stats.getTime(), is(notNullValue()));
		assertThat(stats.getLastTradeAnyCurrency(), is(notNullValue()));
		assertThat(stats.getLastTradeConvertedToDefault(), is(notNullValue()));
		assertThat(stats.getLastTradeDefaultCurrency(), is(notNullValue()));
		assertThat(stats.getTimestampMicroSecs(), not(0L));
		assertThat(stats.getVolume(), is(notNullValue()));
		assertThat(stats.getVolumeWeightenedAvg(), is(notNullValue()));
	}
	
	@Test
	public void getStats() {
		CurrencyStatistics stats = statistics.getStatistics(DEFAULT_CURRENCY);
		assertThat(stats, is(notNullValue()));
		assertThat(stats.getBuy(), is(notNullValue()));
		assertThat(stats.getSell(), is(notNullValue()));
		assertThat(stats.getTime(), is(notNullValue()));
		assertThat(stats.getLastTradeAnyCurrency(), is(notNullValue()));
		assertThat(stats.getLastTradeConvertedToDefault(), is(notNullValue()));
		assertThat(stats.getLastTradeDefaultCurrency(), is(notNullValue()));
		assertThat(stats.getTimestampMicroSecs(), not(0L));
	}
	

	@Test
	public void getLastPrice() {
		CurrencyData lastPrice = statistics.getLastPrice(DEFAULT_CURRENCY);
		assertThat(lastPrice, is(notNullValue()));
	}
	
}
