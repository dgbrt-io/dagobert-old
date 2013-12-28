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
@Path(value = "/" + Period.PERIODS)
@Produces("text/xml")
@Consumes("text/xml")
public class StatisticsRESTService {
	
	@Inject
	private MtGoxStatisticsService ratesService;

	@GET
	public List<Period> getPeriods() {
		
		Period lastPeriod = ratesService.getLastPeriod();
		Period currentPeriod = ratesService.getCurrentPeriod();
		return Arrays.asList(currentPeriod, lastPeriod);
	}
}
