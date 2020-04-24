package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ResourceProviderR4CacheTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4CacheTest.class);
	private CapturingInterceptor myCapturingInterceptor;
	@Autowired
	private ISearchDao mySearchEntityDao;

	@Override
	@After
	public void after() throws Exception {
		super.after();
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCacheControlNoStoreMaxResultsUpperLimit(new DaoConfig().getCacheControlNoStoreMaxResultsUpperLimit());

		ourClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myCapturingInterceptor = new CapturingInterceptor();
		ourClient.registerInterceptor(myCapturingInterceptor);
	}

	@Test
	public void testCacheNoStore() {

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
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

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
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

	}

	@Test
	public void testCacheNoStoreMaxResults() {

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
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

	}

	@Test
	public void testCacheNoStoreMaxResultsWithIllegalValue() {
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
	public void testCacheSuppressed() {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		ourClient.create().resource(pt1).execute();

		Bundle results = ourClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results.getEntry().size());
		assertEquals(1, mySearchEntityDao.count());
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

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
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

	}

	@Test
	public void testCacheUsedNormally() {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		ourClient.create().resource(pt1).execute();

		Date beforeFirst = new Date();

		TestUtil.sleepOneClick();

		Bundle results1 = ourClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();

		TestUtil.sleepOneClick();

		assertEquals(1, results1.getEntry().size());
		assertEquals(1, mySearchEntityDao.count());
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());
		Date results1Date = TestUtil.getTimestamp(results1).getValue();
		assertThat(results1Date, greaterThan(beforeFirst));
		assertThat(results1Date, lessThan(new Date()));
		assertThat(results1.getId(), not(blankOrNullString()));

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		ourClient.create().resource(pt2).execute();

		Bundle results2 = ourClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results2.getEntry().size());
		assertEquals(1, mySearchEntityDao.count());
		assertEquals("HIT from " + ourServerBase, myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE).get(0));
		assertEquals(results1.getMeta().getLastUpdated(), results2.getMeta().getLastUpdated());
		assertEquals(results1.getId(), results2.getId());
	}

	@Test
	public void testDeletedSearchResultsNotReturnedFromCache() {
		Patient p = new Patient();
		p.addName().setFamily("Foo");
		String p1Id = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Foo");
		String p2Id = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		Bundle resp1 = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("foo"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(2, resp1.getEntry().size());

		ourClient.delete().resourceById(new IdType(p1Id)).execute();

		Bundle resp2 = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("foo"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(resp1.getId(), resp2.getId());

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp2));
		assertEquals(1, resp2.getEntry().size());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
