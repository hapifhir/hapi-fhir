package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.api.model.WarmCacheEntry;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FhirResourceDaoR4CacheWarmingTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4CacheWarmingTest.class);

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceServerIdStrategy(new DaoConfig().getResourceServerIdStrategy());

		myDaoConfig.setWarmCacheEntries(new ArrayList<>());
		CacheWarmingSvcImpl cacheWarmingSvc = (CacheWarmingSvcImpl) myCacheWarmingSvc;
		cacheWarmingSvc.initCacheMap();
	}


	@Test
	public void testInvalidCacheEntries() {
		CacheWarmingSvcImpl cacheWarmingSvc = (CacheWarmingSvcImpl) myCacheWarmingSvc;

		myDaoConfig.setWarmCacheEntries(new ArrayList<>());
		myDaoConfig.getWarmCacheEntries().add(
			new WarmCacheEntry()
				.setPeriodMillis(10)
				.setUrl("BadResource?name=smith")
		);
		try {
			cacheWarmingSvc.initCacheMap();
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"BadResource\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}

		myDaoConfig.setWarmCacheEntries(new ArrayList<>());
		myDaoConfig.getWarmCacheEntries().add(
			new WarmCacheEntry()
				.setPeriodMillis(10)
				.setUrl("foo/Patient")
		);
		try {
			cacheWarmingSvc.initCacheMap();
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Invalid warm cache URL (must have ? character)", e.getMessage());
		}


	}

	@Test
	public void testKeepCacheWarm() throws InterruptedException {
		myDaoConfig.setWarmCacheEntries(new ArrayList<>());
		myDaoConfig.getWarmCacheEntries().add(
			new WarmCacheEntry()
				.setPeriodMillis(10)
				.setUrl("Patient?name=smith")
		);
		CacheWarmingSvcImpl cacheWarmingSvc = (CacheWarmingSvcImpl) myCacheWarmingSvc;
		ourLog.info("Have {} tasks", cacheWarmingSvc.initCacheMap().size());

		Patient p1 = new Patient();
		p1.setId("p1");
		p1.setActive(true);
		myPatientDao.update(p1);

		Patient p2 = new Patient();
		p2.setId("p2");
		p2.setActive(true);
		p2.addName().setFamily("Smith");
		myPatientDao.update(p2);

		myCacheWarmingSvc.performWarmingPass();

		Thread.sleep(1000);

		ourLog.info("About to perform search");
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("smith"));
		IBundleProvider result = myPatientDao.search(params);
		assertEquals(PersistedJpaBundleProvider.class, result.getClass());

		PersistedJpaBundleProvider resultCasted = (PersistedJpaBundleProvider) result;
		assertTrue(resultCasted.isCacheHit());
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
