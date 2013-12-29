package com.dagobert_engine.trading.service.rest;


import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.dagobert_engine.trading.model.TradingStatus;
import com.dagobert_engine.trading.service.MtGoxTradeService;

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
@Path(value = "/trading")
@Produces({"application/xml", "application/json"})
@Consumes({"application/xml", "application/json"})
public class TradingRESTService {
	
	@Inject
	private MtGoxTradeService trading;
	
	@GET
	public TradingStatus getStatus() {
		return trading.getStatus();
	}
}
