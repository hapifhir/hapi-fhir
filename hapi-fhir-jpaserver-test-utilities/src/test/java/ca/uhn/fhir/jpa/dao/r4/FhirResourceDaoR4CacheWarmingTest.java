package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.WarmCacheEntry;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4CacheWarmingTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4CacheWarmingTest.class);

	@AfterEach
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
			assertEquals(Msg.code(1684) + "Unknown resource name \"BadResource\" (this name is not known in FHIR version \"R4\")", e.getMessage());
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
			assertEquals(Msg.code(1172) + "Invalid warm cache URL (must have ? character)", e.getMessage());
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
		assertEquals(SearchCacheStatusEnum.HIT, resultCasted.getCacheStatus());
	}

}
