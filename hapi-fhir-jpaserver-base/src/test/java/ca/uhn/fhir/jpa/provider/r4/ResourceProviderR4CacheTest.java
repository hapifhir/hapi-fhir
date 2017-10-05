package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.util.AopTestUtils;

import java.io.IOException;

import static org.junit.Assert.*;

public class ResourceProviderR4CacheTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CacheTest.class);
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;

	@Override
	@After
	public void after() throws Exception {
		super.after();
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCacheControlNoStoreMaxResultsUpperLimit(new DaoConfig().getCacheControlNoStoreMaxResultsUpperLimit());
	}

	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
	}

	@Test
	public void testCacheNoStore() throws IOException {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		ourClient.create().resource(pt1).execute();

		Bundle results = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.execute();
		assertEquals(1, results.getEntry().size());
		assertEquals(0, mySearchEntityDao.count());

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		ourClient.create().resource(pt2).execute();

		results = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.execute();
		assertEquals(2, results.getEntry().size());
		assertEquals(0, mySearchEntityDao.count());

	}

	@Test
	public void testCacheNoStoreMaxResults() throws IOException {

		for (int i = 0; i < 10; i++) {
			Patient pt1 = new Patient();
			pt1.addName().setFamily("FAM" + i);
			ourClient.create().resource(pt1).execute();
		}

		Bundle results = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true).setMaxResults(5))
			.execute();
		assertEquals(5, results.getEntry().size());
		assertEquals(0, mySearchEntityDao.count());

	}

	@Test
	public void testCacheNoStoreMaxResultsWithIllegalValue() throws IOException {
		myDaoConfig.setCacheControlNoStoreMaxResultsUpperLimit(123);
		try {
			ourClient
				.search()
				.forResource("Patient")
				.where(Patient.FAMILY.matches().value("FAM"))
				.returnBundle(Bundle.class)
				.cacheControl(new CacheControlDirective().setNoStore(true).setMaxResults(5000))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Cache-Control header max-results value must not exceed 123", e.getMessage());
		}
	}

	@Test
	public void testCacheSuppressed() throws IOException {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		ourClient.create().resource(pt1).execute();

		Bundle results = ourClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results.getEntry().size());
		assertEquals(1, mySearchEntityDao.count());

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		ourClient.create().resource(pt2).execute();

		results = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();
		assertEquals(2, results.getEntry().size());
		assertEquals(2, mySearchEntityDao.count());

	}

	@Test
	public void testCacheUsedNormally() throws IOException {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		ourClient.create().resource(pt1).execute();

		Bundle results = ourClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results.getEntry().size());
		assertEquals(1, mySearchEntityDao.count());

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		ourClient.create().resource(pt2).execute();

		results = ourClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results.getEntry().size());
		assertEquals(1, mySearchEntityDao.count());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
