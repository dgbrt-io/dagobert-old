package com.dagobert_engine.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import com.dagobert_engine.rest.util.MapBuilder;
import com.dagobert_engine.service.Dagobert;
import com.dagobert_engine.util.Constants;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Path(value = "/")
public class DagobertEndpoint {
	private Logger logger = Logger.getLogger(getClass());
	
	@Inject
	private Dagobert dagobert;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getInfo(@Context UriInfo uriInfo) {
		logger.debug("GET " + uriInfo.getPath() + " => getInfo(...)");
		
		return new MapBuilder<String, Object>().create()
				.put("name", Constants.NAME)
				.put("version", Constants.VERSION)
				.build();
	}	
	
	@GET
	@Path("/services")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> getServices(@Context UriInfo uriInfo) {
		logger.debug("GET " + uriInfo.getPath() + " => getServices(...)");
		
		final List<Map<String, Object>> output = new ArrayList<>();
		for (String serviceName : dagobert.getServices().keySet()) {
			output.add(dagobert.getServices().get(serviceName).getInfo());
		}
		
		return output;
	}
}
