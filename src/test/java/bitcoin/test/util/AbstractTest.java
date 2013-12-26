package bitcoin.test.util;

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
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.After;
import org.junit.Before;

import com.dagobert_engine.core.rest.JaxRsActivator;
import com.dagobert_engine.core.util.Resources;
import com.dagobert_engine.statistics.model.BTCRate;
import com.dagobert_engine.statistics.service.StatisticsService;
import com.dagobert_engine.trading.service.MtGoxTradeService;


public abstract class AbstractTest {

	private static final String COMMONS_MATH = "org.apache.commons:commons-math:2.0";
	private static final String GSON = "com.google.code.gson:gson:2.2.4";
	private static final String SIMPLE = "com.googlecode.json-simple:json-simple:1.1.1";
	private static final String JTA = "javax.transaction:jta:1.1";
	private static final String COMMONS_CODEC = "commons-codec:commons-codec:1.8";

	private UserTransaction trans;

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

	@Deployment
	public static Archive<?> createTestArchive() {

		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addAsLibraries(
						DependencyResolvers.use(MavenDependencyResolver.class)
								.goOffline().artifact(COMMONS_MATH)
								.artifact(GSON).artifact(SIMPLE).artifact(JTA)
								.artifact(COMMONS_CODEC).resolveAsFiles())
				.addPackage(MtGoxTradeService.class.getPackage())
				.addPackage(StatisticsService.class.getPackage())
				.addPackage(BTCRate.class.getPackage())
				.addPackage(Resources.class.getPackage())
				.addPackage(JaxRsActivator.class.getPackage())

				// Tests
				.addPackage(bitcoin.test.util.AbstractTest.class.getPackage())
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
}
