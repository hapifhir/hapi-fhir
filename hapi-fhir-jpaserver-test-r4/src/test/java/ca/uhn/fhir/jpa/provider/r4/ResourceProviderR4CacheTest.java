package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4CacheTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4CacheTest.class);
	private CapturingInterceptor myCapturingInterceptor;
	@Autowired
	private ISearchDao mySearchEntityDao;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCacheControlNoStoreMaxResultsUpperLimit(new DaoConfig().getCacheControlNoStoreMaxResultsUpperLimit());

		myClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myCapturingInterceptor = new CapturingInterceptor();
		myClient.registerInterceptor(myCapturingInterceptor);
	}

	@Test
	public void testCacheNoStore() {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		myClient.create().resource(pt1).execute();

		Bundle results = myClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.execute();
		assertEquals(1, results.getEntry().size());
		runInTransaction(()->assertEquals(0, mySearchEntityDao.count()));
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		myClient.create().resource(pt2).execute();

		results = myClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.execute();
		assertEquals(2, results.getEntry().size());
		runInTransaction(()->assertEquals(0, mySearchEntityDao.count()));
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

	}

	@Test
	public void testCacheNoStoreMaxResults() {

		for (int i = 0; i < 10; i++) {
			Patient pt1 = new Patient();
			pt1.addName().setFamily("FAM" + i);
			myClient.create().resource(pt1).execute();
		}

		Bundle results = myClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true).setMaxResults(5))
			.execute();
		assertEquals(5, results.getEntry().size());
		runInTransaction(()->assertEquals(0, mySearchEntityDao.count()));
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

	}

	@Test
	public void testCacheNoStoreMaxResultsWithIllegalValue() {
		myDaoConfig.setCacheControlNoStoreMaxResultsUpperLimit(123);
		try {
			myClient
				.search()
				.forResource("Patient")
				.where(Patient.FAMILY.matches().value("FAM"))
				.returnBundle(Bundle.class)
				.cacheControl(new CacheControlDirective().setNoStore(true).setMaxResults(5000))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1165) + "Cache-Control header max-results value must not exceed 123", e.getMessage());
		}
	}

	@Test
	public void testCacheSuppressed() {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		myClient.create().resource(pt1).execute();

		Bundle results = myClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results.getEntry().size());
		runInTransaction(() -> assertEquals(1, mySearchEntityDao.count()));
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		myClient.create().resource(pt2).execute();

		results = myClient
			.search()
			.forResource("Patient")
			.where(Patient.FAMILY.matches().value("FAM"))
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();
		assertEquals(2, results.getEntry().size());
		runInTransaction(() -> assertEquals(2, mySearchEntityDao.count()));
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());

	}

	@Test
	public void testCacheUsedNormally() {

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAM");
		myClient.create().resource(pt1).execute();

		Date beforeFirst = new Date();

		TestUtil.sleepOneClick();

		Bundle results1 = myClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();

		TestUtil.sleepOneClick();

		assertEquals(1, results1.getEntry().size());
		runInTransaction(() -> assertEquals(1, mySearchEntityDao.count()));
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), empty());
		Date results1Date = TestUtil.getTimestamp(results1).getValue();
		assertThat(results1Date, greaterThan(beforeFirst));
		assertThat(results1Date, lessThan(new Date()));
		assertThat(results1.getId(), not(blankOrNullString()));

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FAM");
		myClient.create().resource(pt2).execute();

		Bundle results2 = myClient.search().forResource("Patient").where(Patient.FAMILY.matches().value("FAM")).returnBundle(Bundle.class).execute();
		assertEquals(1, results2.getEntry().size());
		runInTransaction(() -> assertEquals(1, mySearchEntityDao.count()));
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
		myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		Bundle resp1 = myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("foo"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(2, resp1.getEntry().size());

		myClient.delete().resourceById(new IdType(p1Id)).execute();

		Bundle resp2 = myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("foo"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(resp1.getId(), resp2.getId());

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp2));
		assertEquals(1, resp2.getEntry().size());
	}

	@Test
	public void testParamWithNoValueIsConsideredForCacheResults(){
		// Given: We populate the cache by searching
		myClient
			.search()
			.byUrl("Procedure")
			.returnBundle(Bundle.class)
			.execute();

		// When: We search Procedure?patient=
		BaseServerResponseException exception = assertThrows(BaseServerResponseException.class, () -> {myClient
			.search()
			.byUrl("Procedure?patient=")
			.returnBundle(Bundle.class)
			.execute();});

		// Then: We do not get a cache hit
		assertNotEquals(Constants.STATUS_HTTP_200_OK, exception.getStatusCode());
	}

	@Test
	public void testReturn400ForParameterWithNoValue(){
		// When: We search Procedure?patient=
		BaseServerResponseException exception = assertThrows(BaseServerResponseException.class, () -> {myClient
			.search()
			.byUrl("Procedure?patient=")
			.returnBundle(Bundle.class)
			.execute();});

		// Then: We get a HTTP 400 Bad Request
		assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, exception.getStatusCode());
	}

}
