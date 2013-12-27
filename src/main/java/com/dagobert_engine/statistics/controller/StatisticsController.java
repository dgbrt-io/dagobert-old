package com.dagobert_engine.statistics.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.dagobert_engine.statistics.model.Period;

/**
 * Statistics controller TODO
 * 
 * @author Michael Kunzmann (mail@michaelkunzmann.com)
 * @version 0.1-ALPHA
 *
 * License http://www.apache.org/licenses/LICENSE-2.0
 *
 */
@Named("statistics")
@SessionScoped
public class StatisticsController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3410669867905143094L;

	//@Inject
	//private GeneralStatisticsService statsService;
	
	private List<Period> periods = new ArrayList<>();
	
	
	@PostConstruct
	public void refresh() {
		//periods = statsService.getPeriods();
	}
	

	public List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}
	
	
	
}
