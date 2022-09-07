package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
