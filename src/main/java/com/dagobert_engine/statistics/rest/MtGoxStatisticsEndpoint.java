package com.dagobert_engine.statistics.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;

import com.dagobert_engine.core.model.AdvancedCurrencyStatistics;
import com.dagobert_engine.core.model.Configuration;
import com.dagobert_engine.core.model.CurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyType;
import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.statistics.service.MtGoxStatisticsService;

/**
 * End point for MtGox
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Path(value = "/mtgox/statistics")
public class MtGoxStatisticsEndpoint implements StatisticsEndpoint {
	
	@Inject
	private Configuration config;
	
	@Inject
	private MtGoxStatisticsService ratesService;

	
	@Override
	public List<Period> getPeriods(String refCurrency, String targetCurrency) {
		
		// TODO: also support USD
		if (refCurrency != null && !CurrencyType.USD.name().equals(refCurrency)) {
			throw new CurrencyNotSupportedException("Reference currency: Only USD supported by end point at the moment.");
		}
		
		if (refCurrency != null && !CurrencyType.BTC.name().equals(targetCurrency)) {
			throw new CurrencyNotSupportedException("Reference currency: Only USD supported by end point at the moment.");
		}
		
		Period lastPeriod = ratesService.getLastPeriod();
		Period currentPeriod = ratesService.getCurrentPeriod();
		return Arrays.asList(currentPeriod, lastPeriod);
	}

	@Override
	public CurrencyStatistics getStatistics(String refCurrency, String targetCurrency) {
		
		return ratesService.getStatistics(config.getDefaultCurrency());
	}

	@Override
	public AdvancedCurrencyStatistics getAdvancedStatistics(String refCurrency, String targetCurrency) {
		return ratesService.getAdvancedStatistics(config.getDefaultCurrency());
	}
}
