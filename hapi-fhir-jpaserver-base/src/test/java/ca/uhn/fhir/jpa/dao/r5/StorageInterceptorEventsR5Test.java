package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SuppressWarnings({"Duplicates"})
public class StorageInterceptorEventsR5Test extends BaseJpaR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StorageInterceptorEventsR5Test.class);

	@Test
	public void before() {
	}

	@Test
	public void testPreShowEventsDontIncludeExpungedResources_AsyncSearch() {
		when(mySrd.getServer().getPagingProvider()).thenReturn(new DatabaseBackedPagingProvider());

		Patient p0 = new Patient();
		p0.setId("P0");
		p0.addIdentifier().setValue("P0");
		myPatientDao.update(p0);

		Patient p1 = new Patient();
		p1.setId("P1");
		p1.addIdentifier().setValue("P1");
		myPatientDao.update(p1);

		Patient p2 = new Patient();
		p2.setId("P2");
		p2.addIdentifier().setValue("P2");
		myPatientDao.update(p2);

		AtomicInteger showedCounter = new AtomicInteger(0);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, (thePointcut, theArgs) -> {
			int showedCountThisPass = theArgs.get(IPreResourceShowDetails.class).size();
			showedCounter.addAndGet(showedCountThisPass);
		});

		// Initial search returns all
		SearchParameterMap params = new SearchParameterMap();
		IBundleProvider search = myPatientDao.search(params, mySrd);
		assertTrue(search instanceof PersistedJpaBundleProvider, search.getClass().toString());
		List<IBaseResource> found = search.getResources(0, 100);
		assertEquals(3, found.size());
		assertEquals(3, showedCounter.get());

		// Delete and expunge one
		myPatientDao.delete(new IdType("Patient/P1"));
		mySystemDao.expunge(new ExpungeOptions().setExpungeDeletedResources(true), mySrd);
		showedCounter.set(0);

		// Next search should return only the non-expunged ones
		params = new SearchParameterMap();
		found = myPatientDao.search(params, mySrd).getResources(0, 100);
		assertEquals(2, found.size());
		assertEquals(2, showedCounter.get());
	}

	@Test
	public void testPreShowEventsDontIncludeExpungedResources_SyncSearch() {
		Patient p0 = new Patient();
		p0.setId("P0");
		p0.addIdentifier().setValue("P0");
		myPatientDao.update(p0);

		Patient p1 = new Patient();
		p1.setId("P1");
		p1.addIdentifier().setValue("P1");
		myPatientDao.update(p1);

		Patient p2 = new Patient();
		p2.setId("P2");
		p2.addIdentifier().setValue("P2");
		myPatientDao.update(p2);

		AtomicInteger showedCounter = new AtomicInteger(0);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, (thePointcut, theArgs) -> {
			int showedCountThisPass = theArgs.get(IPreResourceShowDetails.class).size();
			showedCounter.addAndGet(showedCountThisPass);
		});

		// Initial search returns all
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		IBundleProvider search = myPatientDao.search(params, mySrd);
		assertTrue(search instanceof SimpleBundleProvider, search.getClass().toString());
		List<IBaseResource> found = search.getResources(0, 100);
		assertEquals(3, found.size());
		assertEquals(3, showedCounter.get());

		// Delete and expunge one
		myPatientDao.delete(new IdType("Patient/P1"));
		mySystemDao.expunge(new ExpungeOptions().setExpungeDeletedResources(true), mySrd);
		showedCounter.set(0);

		// Next search should return only the non-expunged ones
		params = new SearchParameterMap();
		found = myPatientDao.search(params, mySrd).getResources(0, 100);
		assertEquals(2, found.size());
		assertEquals(2, showedCounter.get());
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}


}
