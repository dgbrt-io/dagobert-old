package com.dagobert_engine.rest;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import com.dagobert_engine.model.CurrencyPairStatistics;
import com.dagobert_engine.service.Configuration;
import com.dagobert_engine.service.KrakenService;
import com.xeiam.xchange.currency.CurrencyPair;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Path(value = "/services/kraken")
public class KrakenEndpoint {
	private Logger logger = Logger.getLogger(getClass());
	
	@Inject
	private Configuration config;
	
	@Inject
	private KrakenService service;

	public KrakenEndpoint() {
	}
	

	/**
	 * Default
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getInfo(@Context UriInfo uriInfo) {
		logger.debug("GET " + uriInfo.getPath() + " => getApiStatus(...)");
		return service.getInfo();
	}
	
	@GET
	@Path("/currencyPairs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CurrencyPair> getAllCurrencyPairs(@Context UriInfo uriInfo) {
		return config.getTradedCurrencyPairs();
	}

	@GET
	@Path("/currencyPairs/{pairKey}/statistics")
	@Produces(MediaType.APPLICATION_JSON)
	public CurrencyPairStatistics getCurrencyPairStatistics(@PathParam("pairKey") String pairKey, @Context UriInfo uriInfo) {
		logger.debug("GET " + uriInfo.getPath() + " => getCurrencyPairStatistics(...)");
		CurrencyPair pair = config.getCurrencyPairs().get(pairKey);
		return service.getCurrencyPairStatistics().get(pair);
	}
	
	@GET
	@Path("/currencyPairs/{pairKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public CurrencyPair getCurrencyPair(@PathParam("pairKey") String pairKey, @Context UriInfo uriInfo) {
		logger.debug("GET " + uriInfo.getPath() + " => getCurrencyPair(...)");
		return config.getCurrencyPairs().get(pairKey);
	}
	
	
	
}
