package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4CacheWarmingTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4CacheWarmingTest.class);

	@AfterEach
	public void afterResetDao() {
		myStorageSettings.setResourceServerIdStrategy(new JpaStorageSettings().getResourceServerIdStrategy());

		myStorageSettings.setWarmCacheEntries(new ArrayList<>());
		CacheWarmingSvcImpl cacheWarmingSvc = (CacheWarmingSvcImpl) myCacheWarmingSvc;
		cacheWarmingSvc.initCacheMap();
	}


	@Test
	public void testInvalidCacheEntries() {
		CacheWarmingSvcImpl cacheWarmingSvc = (CacheWarmingSvcImpl) myCacheWarmingSvc;

		myStorageSettings.setWarmCacheEntries(new ArrayList<>());
		myStorageSettings.getWarmCacheEntries().add(
			new WarmCacheEntry()
				.setPeriodMillis(10)
				.setUrl("BadResource?name=smith")
		);
		try {
			cacheWarmingSvc.initCacheMap();
			fail("");
		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1684) + "Unknown resource name \"BadResource\" (this name is not known in FHIR version \"R4\")");
		}

		myStorageSettings.setWarmCacheEntries(new ArrayList<>());
		myStorageSettings.getWarmCacheEntries().add(
			new WarmCacheEntry()
				.setPeriodMillis(10)
				.setUrl("foo/Patient")
		);
		try {
			cacheWarmingSvc.initCacheMap();
			fail("");
		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1172) + "Invalid warm cache URL (must have ? character)");
		}


	}

	@Test
	public void testKeepCacheWarm() throws InterruptedException {
		myStorageSettings.setWarmCacheEntries(new ArrayList<>());
		myStorageSettings.getWarmCacheEntries().add(
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
		assertThat(result.getClass()).isEqualTo(PersistedJpaBundleProvider.class);

		PersistedJpaBundleProvider resultCasted = (PersistedJpaBundleProvider) result;
		assertThat(resultCasted.getCacheStatus()).isEqualTo(SearchCacheStatusEnum.HIT);
	}

}
