package com.dagobert_engine.test.core;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.dagobert_engine.core.model.MtGoxConfiguration;
import com.dagobert_engine.core.service.MtGoxApiAdapter;
import com.dagobert_engine.test.util.AbstractTest;

@RunWith(Arquillian.class)
public class CoreTest extends AbstractTest {
	
	@Inject
	private MtGoxApiAdapter adapter;
	
	@Inject
	private MtGoxConfiguration mtGoxConfig;
	


	
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
	public void setupTest() {
		
		assertThat(mtGoxConfig.getDivisionFactors().size(), not(0));
	}
	
	@Test
	public void getIdKeyTest() {
		assertThat(adapter.getIdKey(), not(nullValue()));
		assertThat(adapter.getIdKey().equals(""), not(true));
	}
	
	@Test
	public void getLagTest() {
		assertThat(adapter.getLag(), not(nullValue()));
	}
}
