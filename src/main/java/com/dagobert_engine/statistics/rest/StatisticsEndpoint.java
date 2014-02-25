package com.dagobert_engine.statistics.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dagobert_engine.core.model.AdvancedCurrencyStatistics;
import com.dagobert_engine.core.model.CurrencyStatistics;
import com.dagobert_engine.statistics.model.Period;


@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StatisticsEndpoint {

	
	@GET
	public CurrencyStatistics getStatistics(@QueryParam("refCurrency") String referenceCurrency, @QueryParam("targetCurrency") String targetCurrency);
	
	@GET
	@Path("/periods")
	public List<Period> getPeriods(@QueryParam("refCurrency") String referenceCurrency, @QueryParam("targetCurrency") String targetCurrency);
	
	@GET
	@Path("/advanced")
	public AdvancedCurrencyStatistics getAdvancedStatistics(@QueryParam("refCurrency") String referenceCurrency, @QueryParam("targetCurrency") String targetCurrency);
	

}
