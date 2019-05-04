package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.subscription.SubscriptionActivatingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.Query;

import static org.junit.Assert.*;

public class FhirResourceDaoR4InvalidSubscriptionTest extends BaseJpaR4Test {

	@After
	public void afterResetDao() {
		SubscriptionActivatingInterceptor.setWaitForSubscriptionActivationSynchronouslyForUnitTest(false);
		myDaoConfig.setResourceServerIdStrategy(new DaoConfig().getResourceServerIdStrategy());
		BaseHapiFhirDao.setValidationDisabledForUnitTest(false);
	}

	@Before
	public void before() {
		SubscriptionActivatingInterceptor.setWaitForSubscriptionActivationSynchronouslyForUnitTest(true);
	}

	@Test
	public void testCreateInvalidSubscriptionOkButCanNotActivate() {
		Subscription s = new Subscription();
		s.setStatus(Subscription.SubscriptionStatus.OFF);
		s.setCriteria("FOO");
		IIdType id = mySubscriptionDao.create(s).getId().toUnqualified();

		s = mySubscriptionDao.read(id);
		assertEquals("FOO", s.getCriteria());

		s.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		try {
			mySubscriptionDao.update(s);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Subscription.criteria must be in the form \"{Resource Type}?[params]\"", e.getMessage());
		}
	}

	/**
	 * Make sure that bad data in the database doesn't prevent startup
	 */
	@Test
	public void testSubscriptionMarkedDeleted() {
		BaseHapiFhirDao.setValidationDisabledForUnitTest(true);

		Subscription s = new Subscription();
		s.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		s.getChannel().setEndpoint("http://foo");
		s.getChannel().setPayload("application/fhir+json");
		s.setCriteria("Patient?foo");
		final IIdType id = mySubscriptionDao.create(s).getId().toUnqualifiedVersionless();
		assertNotNull(id.getIdPart());

		BaseHapiFhirDao.setValidationDisabledForUnitTest(false);

		new TransactionTemplate(myTransactionMgr).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Query q = myEntityManager.createNativeQuery("UPDATE HFJ_RESOURCE SET RES_DELETED_AT = RES_UPDATED WHERE RES_ID = " + id.getIdPart());
				q.executeUpdate();
			}
		});

		myEntityManager.clear();
	}

	/**
	 * Make sure that bad data in the database doesn't prevent startup
	 */
	@Test
	public void testSubscriptionWithInvalidCriteria() throws InterruptedException {
		BaseHapiFhirDao.setValidationDisabledForUnitTest(true);

		Subscription s = new Subscription();
		s.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		s.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		s.getChannel().setEndpoint("http://foo");
		s.getChannel().setPayload("application/fhir+json");
		s.setCriteria("BLAH");
		IIdType id = mySubscriptionDao.create(s).getId().toUnqualifiedVersionless();
		assertNotNull(id.getIdPart());

		BaseHapiFhirDao.setValidationDisabledForUnitTest(false);
	}

	/**
	 * Make sure that bad data in the database doesn't prevent startup
	 */
	@Test
	public void testSubscriptionWithNoStatus() {
		BaseHapiFhirDao.setValidationDisabledForUnitTest(true);

		Subscription s = new Subscription();
		s.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		s.getChannel().setEndpoint("http://foo");
		s.getChannel().setPayload("application/fhir+json");
		s.setCriteria("Patient?active=true");
		IIdType id = mySubscriptionDao.create(s).getId().toUnqualifiedVersionless();

		BaseHapiFhirDao.setValidationDisabledForUnitTest(false);
	}

	/**
	 * Make sure that bad data in the database doesn't prevent startup
	 */
	@Test
	public void testSubscriptionWithNoType() {
		BaseHapiFhirDao.setValidationDisabledForUnitTest(true);

		Subscription s = new Subscription();
		s.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		s.getChannel().setEndpoint("http://foo");
		s.getChannel().setPayload("application/fhir+json");
		s.setCriteria("Patient?foo");
		IIdType id = mySubscriptionDao.create(s).getId().toUnqualifiedVersionless();
		assertNotNull(id.getIdPart());

		BaseHapiFhirDao.setValidationDisabledForUnitTest(false);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
