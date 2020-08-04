package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4InvalidSubscriptionTest extends BaseJpaR4Test {

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@AfterEach
	public void afterResetDao() {
		myDaoConfig.setResourceServerIdStrategy(new DaoConfig().getResourceServerIdStrategy());
		BaseHapiFhirDao.setValidationDisabledForUnitTest(false);
	}

	@AfterEach
	public void afterUnregisterRestHookListener() {
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerRestHookInterceptor();
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

}
