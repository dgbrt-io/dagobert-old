package com.dagobert_engine.statistics.rest;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dagobert_engine.core.model.AdvancedCurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyStatistics;
import com.dagobert_engine.core.service.ConfigService;
import com.dagobert_engine.statistics.model.Period;
import com.dagobert_engine.statistics.service.MtGoxStatisticsService;

/**
 * Statistics rest service
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Path(value = "/statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatisticsRESTService {
	
	@Inject
	private ConfigService config;
	
	@Inject
	private MtGoxStatisticsService ratesService;

	@GET
	@Path("periods")
	public List<Period> getPeriods() {
		
		Period lastPeriod = ratesService.getLastPeriod();
		Period currentPeriod = ratesService.getCurrentPeriod();
		return Arrays.asList(currentPeriod, lastPeriod);
	}
	
	@GET
	public CurrencyStatistics getStatistics() {
		
		return ratesService.getStatistics(config.getDefaultCurrency());
	}
	
	@GET
	@Path("advanced")
	public AdvancedCurrencyStatistics getAdvancedStatistics() {
		
		return ratesService.getAdvancedStatistics(config.getDefaultCurrency());
	}
}
