package com.dagobert_engine.test;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Before;


public abstract class AbstractTest {

	private UserTransaction trans;
	@Deployment
	public static Archive<?> createTestArchive() {
		return ArchiveService.INSTANCE.getArchive();
	}
	
	

	@Before
	public void before() throws NamingException, SystemException,
			NotSupportedException {
		trans = (UserTransaction) new InitialContext()
				.lookup("java:jboss/UserTransaction");

		if (trans.getStatus() != Status.STATUS_ACTIVE) {
			trans.begin();
		}
	}

	@After
	public void after() throws SecurityException, IllegalStateException,
			SystemException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException {

		if (trans.getStatus() == Status.STATUS_ACTIVE) {
			trans.commit();
		}
	}
}
