package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

public class FhirResourceDaoR4MetaTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4MetaTest.class);

	// TODO testConcurrentAddTag() can deadlock if we don't increase this
	@BeforeAll
	public static void beforeAll() {
		System.setProperty("unlimited_db_connection", "true");
	}

	@AfterAll
	public static void afterAll() {
		System.clearProperty("unlimited_db_connection");
	}

	/**
	 * See #1731
	 */
	@Test
	public void testMetaExtensionsPreserved() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().addExtension("http://foo", new StringType("hello"));
		IIdType id = myPatientDao.create(patient).getId();

		patient = myPatientDao.read(id);
		assertTrue(patient.getActive());
		assertEquals(1, patient.getMeta().getExtensionsByUrl("http://foo").size());
		assertEquals("hello", patient.getMeta().getExtensionByUrl("http://foo").getValueAsPrimitive().getValueAsString());
	}

	/**
	 * See #1731
	 */
	@Test
	public void testBundleInnerResourceMetaIsPreserved() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().setLastUpdatedElement(new InstantType("2011-01-01T12:12:12Z"));
		patient.getMeta().setVersionId("22");
		patient.getMeta().addProfile("http://foo");
		patient.getMeta().addTag("http://tag", "value", "the tag");
		patient.getMeta().addSecurity("http://tag", "security", "the tag");
		patient.getMeta().addExtension("http://foo", new StringType("hello"));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.COLLECTION);
		bundle.addEntry().setResource(patient);
		IIdType id = myBundleDao.create(bundle).getId();

		bundle = myBundleDao.read(id);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		patient = (Patient) bundle.getEntryFirstRep().getResource();
		assertTrue(patient.getActive());
		assertEquals(1, patient.getMeta().getExtensionsByUrl("http://foo").size());
		assertEquals("22", patient.getMeta().getVersionId());
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("hello", patient.getMeta().getExtensionByUrl("http://foo").getValueAsPrimitive().getValueAsString());
	}

	/**
	 * See #1731
	 */
	@Test
	public void testMetaValuesNotStoredAfterDeletion() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().addProfile("http://foo");
		patient.getMeta().addTag("http://tag", "value", "the tag");
		patient.getMeta().addSecurity("http://tag", "security", "the tag");
		IIdType id = myPatientDao.create(patient).getId();

		Meta meta = new Meta();
		meta.addProfile("http://foo");
		meta.addTag("http://tag", "value", "the tag");
		meta.addSecurity("http://tag", "security", "the tag");
		myPatientDao.metaDeleteOperation(id, meta, mySrd);

		patient = myPatientDao.read(id);
		assertThat(patient.getMeta().getProfile(), empty());
		assertThat(patient.getMeta().getTag(), empty());
		assertThat(patient.getMeta().getSecurity(), empty());
	}

	@Test
	public void testAddTagAndSecurityLabelWithSameValues() {

		Patient patient1 = new Patient();
		patient1.getMeta().addTag().setSystem("http://foo").setCode("bar");
		patient1.setActive(true);
		IIdType pid1 = myPatientDao.create(patient1).getId();

		Patient patient2 = new Patient();
		patient2.getMeta().addSecurity().setSystem("http://foo").setCode("bar");
		patient2.setActive(true);
		IIdType pid2 = myPatientDao.create(patient2).getId();

		patient1 = myPatientDao.read(pid1);
		assertEquals(1, patient1.getMeta().getTag().size());
		assertEquals(0, patient1.getMeta().getSecurity().size());
		assertEquals("http://foo", patient1.getMeta().getTagFirstRep().getSystem());
		assertEquals("bar", patient1.getMeta().getTagFirstRep().getCode());

		patient2 = myPatientDao.read(pid2);
		assertEquals(0, patient2.getMeta().getTag().size());
		assertEquals(1, patient2.getMeta().getSecurity().size());
		assertEquals("http://foo", patient2.getMeta().getSecurityFirstRep().getSystem());
		assertEquals("bar", patient2.getMeta().getSecurityFirstRep().getCode());
	}

	@Test
	public void testAddTagWithVersionAndUserSelected() {
		final List<ResourceHistoryTag> resourceHistoryTags1 = myResourceHistoryTagDao.findAll();
		// TODO:  make this work
		// TODO: test different configs fo dao_config.tag_storage_mode is set to NON_VERSIONED or VERSIONED or others?
		// TODO: test history tag
		final String expectedSystem1 = "http://foo";
		final String expectedCode1 = "code1";
		final String testVersion1 = "testVersion1";
		final String expectedSystem2 = "http://another.system";
		final String expectedCode2 = "code2";
		final String testVersion2 = "testVersion2";

		final Patient savedPatient = new Patient();
		final Coding tag = savedPatient.getMeta().addTag().setSystem(expectedSystem1).setCode(expectedCode1);
		assertFalse(tag.getUserSelected());
		tag.setVersion(testVersion1).setUserSelected(true);
		savedPatient.setActive(true);
		final IIdType pid1 = myPatientDao.create(savedPatient).getId();

		final List<ResourceHistoryTag> resourceHistoryTags2 = myResourceHistoryTagDao.findAll();

		final Patient retrievedPatient = myPatientDao.read(pid1);
		assertAll(
			() -> assertEquals(1, retrievedPatient.getMeta().getTag().size()),
			() -> assertEquals(0, retrievedPatient.getMeta().getSecurity().size()),
			() -> assertEquals(expectedSystem1, retrievedPatient.getMeta().getTagFirstRep().getSystem()),
			() -> assertTrue(retrievedPatient.getMeta().getTagFirstRep().getUserSelected()),
			() -> assertEquals(expectedCode1, retrievedPatient.getMeta().getTagFirstRep().getCode())
			// TODO:  why isn't this working?
			//	,
			//	() -> assertEquals(testVersion1, retrievedPatient.getMeta().getTagFirstRep().getVersion())
		);

		// Update the patient to create a ResourceHistoryTag record
		final List<Coding> tagsFromDbPatient = retrievedPatient.getMeta().getTag();
		assertEquals(1, tagsFromDbPatient.size());

		tagsFromDbPatient.get(0)
			.setCode("code2")
			.setSystem("http://newsystem")
			.setVersion("testVersion2")
			.setUserSelected(false);

		myPatientDao.update(retrievedPatient);
		final Patient retrievedUpdatedPatient = myPatientDao.read(pid1);

//		final DateRangeParam dateRange =
//			new DateRangeParam(Date.from(LocalDateTime.now().minusSeconds(10).atZone(ZoneId.systemDefault()).toInstant()),
//									 Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
//		final HistorySearchDateRangeParam historyDetails = new HistorySearchDateRangeParam(Map.of(), dateRange, 5);
//		final RequestDetails requestDetailsForHistory = mock(RequestDetails.class);
//		final IBundleProvider history = myPatientDao.history(pid1, historyDetails, requestDetailsForHistory);
//		ourLog.info("history: {}", history);

		// TODO:  why do we got from 1 ResourceHistoryTag to 3?
		final List<ResourceHistoryTag> resourceHistoryTags3 = myResourceHistoryTagDao.findAll();

		ourLog.info("resourceHistoryTags: {}", resourceHistoryTags3);

		// TODO:  why do we have tagId
		resourceHistoryTags3.forEach(historyTag -> ourLog.info("tagId: {}, resourceId: {}, userSelected: {}", historyTag.getTagId(), historyTag.getResourceId(), historyTag.getUserSelected()));
		// TODO:  myPatientDao.update(retrievedPatient)
		// TODO:  similar assertAll() post update
		// TODO:  how retrieve resource tag history?

		myCaptureQueriesListener.getInsertQueriesForCurrentThread()
			.stream()
			.map(query -> query.getSql(false, false))
			.forEach(sql -> ourLog.info("sql: {}", sql));

		/*
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))", sql);
		 */
	}


	@Disabled // TODO JA: This test fails regularly, need to get a dedicated connection pool for tag creation
	@Test
	public void testConcurrentAddTag() throws ExecutionException, InterruptedException {

		ExecutorService pool = Executors.newFixedThreadPool(10);

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			Runnable task = () -> {
				Patient patient = new Patient();
				patient.getMeta().addTag().setSystem("http://foo").setCode("bar");
				patient.setActive(true);
				ourLog.info("creating patient {}", index);
				myPatientDao.create(patient);
			};
			ourLog.info("Submitting task {}...", index);
			Future<?> future = pool.submit(task);
			ourLog.info("...done submitting task {}", index);
			futures.add(future);
		}

		// Wait for the tasks to complete, should not throw any exception
		int count = 0;
		for (Future<?> next : futures) {
			try {
				ourLog.info("Getting future {}", count);
				next.get();
			} catch (Exception e) {
				ourLog.error("Failure", e);
				fail(e.toString());
			}
		}

		runInTransaction(() -> {
			ourLog.info("Tag definitions:\n * {}", myTagDefinitionDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		IBundleProvider bundle = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(10, bundle.sizeOrThrowNpe());
		IBundleProvider tagBundle = myPatientDao.search(SearchParameterMap.newSynchronous(PARAM_TAG, new TokenParam("http://foo", "bar")));
		assertEquals(10, tagBundle.sizeOrThrowNpe());

	}
}
