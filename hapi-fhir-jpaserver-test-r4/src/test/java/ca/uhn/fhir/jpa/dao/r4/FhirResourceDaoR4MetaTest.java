package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.system.HapiTestSystemProperties;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class FhirResourceDaoR4MetaTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4MetaTest.class);

	// TODO testConcurrentAddTag() can deadlock if we don't increase this
	@BeforeAll
	public static void beforeAll() {
		HapiTestSystemProperties.enableUnlimitedDbConnections();
	}

	@AfterAll
	public static void afterAll() {
		HapiTestSystemProperties.disableUnlimitedDbConnections();
	}

	/**
	 * See #1731
	 */
	@Test
	public void testMetaExtensionsPreserved() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().addExtension("http://foo", new StringType("hello"));
		IIdType id = myPatientDao.create(patient, mySrd).getId();

		patient = myPatientDao.read(id, mySrd);
		assertTrue(patient.getActive());
		assertThat(patient.getMeta().getExtensionsByUrl("http://foo")).hasSize(1);
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
		IIdType id = myBundleDao.create(bundle, mySrd).getId();

		bundle = myBundleDao.read(id, mySrd);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		patient = (Patient) bundle.getEntryFirstRep().getResource();
		assertTrue(patient.getActive());
		assertThat(patient.getMeta().getExtensionsByUrl("http://foo")).hasSize(1);
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
		IIdType id = myPatientDao.create(patient, mySrd).getId();

		Meta meta = new Meta();
		meta.addProfile("http://foo");
		meta.addTag("http://tag", "value", "the tag");
		meta.addSecurity("http://tag", "security", "the tag");
		myPatientDao.metaDeleteOperation(id, meta, mySrd);

		patient = myPatientDao.read(id, mySrd);
		assertThat(patient.getMeta().getProfile()).isEmpty();
		assertThat(patient.getMeta().getTag()).isEmpty();
		assertThat(patient.getMeta().getSecurity()).isEmpty();
	}

	@Test
	public void testAddTagAndSecurityLabelWithSameValues() {

		Patient patient1 = new Patient();
		patient1.getMeta().addTag().setSystem("http://foo").setCode("bar");
		patient1.setActive(true);
		IIdType pid1 = myPatientDao.create(patient1, mySrd).getId();

		Patient patient2 = new Patient();
		patient2.getMeta().addSecurity().setSystem("http://foo").setCode("bar");
		patient2.setActive(true);
		IIdType pid2 = myPatientDao.create(patient2, mySrd).getId();

		patient1 = myPatientDao.read(pid1, mySrd);
		assertThat(patient1.getMeta().getTag()).hasSize(1);
		assertThat(patient1.getMeta().getSecurity()).isEmpty();
		assertEquals("http://foo", patient1.getMeta().getTagFirstRep().getSystem());
		assertEquals("bar", patient1.getMeta().getTagFirstRep().getCode());

		patient2 = myPatientDao.read(pid2, mySrd);
		assertThat(patient2.getMeta().getTag()).isEmpty();
		assertThat(patient2.getMeta().getSecurity()).hasSize(1);
		assertEquals("http://foo", patient2.getMeta().getSecurityFirstRep().getSystem());
		assertEquals("bar", patient2.getMeta().getSecurityFirstRep().getCode());
	}

	/**
	 * Make sure round-trips with tags don't add a userSelected property.
	 * Verify https://github.com/hapifhir/hapi-fhir/issues/4819
	 */
	@Test
	void testAddTag_userSelectedStaysNull() {
	    // given
		Patient patient1 = new Patient();
		patient1.getMeta().addTag().setSystem("http://foo").setCode("bar");
		patient1.setActive(true);
		IIdType pid1 = myPatientDao.create(patient1, mySrd).getId();

		// when
		var patientReadback = myPatientDao.read(pid1, mySrd);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patientReadback));

		// then
		var tag = patientReadback.getMeta().getTag().get(0);
		assertNotNull(tag);
		assertNull(tag.getUserSelectedElement().asStringValue());
	}


	@Nested
	public class TestTagWithVersionAndUserSelected {

		private static final String expectedSystem1 = "http://foo";
		private static final String expectedCode1 = "code1";
		private static final String expectedVersion1 = "testVersion1";
		private static final String expectedDisplay1 = "Test1";
		private static final boolean expectedUserSelected1 = true;

		private static final String expectedSystem2 = "http://another.system";
		private static final String expectedCode2 = "code2";
		private static final String expectedVersion2 = "testVersion2";
		private static final String expectedDisplay2 = "Test2";
		private static final boolean expectedUserSelected2 = false;

		private static final class TagQtyExpectations {
			public int theTagQty;
			public int theTagV1Qty;
			public int theTagV2Qty;

			public TagQtyExpectations(int theTheTagV1Qty, int theTheTagV2Qty) {
				theTagQty = theTheTagV1Qty + theTheTagV2Qty;
				theTagV1Qty = theTheTagV1Qty;
				theTagV2Qty = theTheTagV2Qty;
			}
		}

		private static final Map<JpaStorageSettings.TagStorageModeEnum, TagQtyExpectations> expectedTagCountMap = Map.of(
			JpaStorageSettings.TagStorageModeEnum.INLINE, 			new TagQtyExpectations(0, 1),
			JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED, 	new TagQtyExpectations(1, 1),
			JpaStorageSettings.TagStorageModeEnum.VERSIONED, 		new TagQtyExpectations(1, 1)
		);

		private static final Map<JpaStorageSettings.TagStorageModeEnum, TagQtyExpectations> expectedHistoryTagCountMap = Map.of(
			JpaStorageSettings.TagStorageModeEnum.INLINE, 			new TagQtyExpectations(0, 0),
			JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED, 	new TagQtyExpectations(0, 0),
			JpaStorageSettings.TagStorageModeEnum.VERSIONED, 		new TagQtyExpectations(2, 1)
		);

		@ParameterizedTest
		@EnumSource(JpaStorageSettings.TagStorageModeEnum.class)
		public void testAddTagWithVersionAndUserSelected(JpaStorageSettings.TagStorageModeEnum theTagStorageModeEnum) {

			myStorageSettings.setTagStorageMode(theTagStorageModeEnum);

			final Patient savedPatient = new Patient();
			final Coding newTag = savedPatient.getMeta()
				.addTag()
				.setSystem(expectedSystem1)
				.setCode(expectedCode1)
				.setDisplay(expectedDisplay1);
			assertNull(newTag.getUserSelectedElement().asStringValue());
			assertFalse(newTag.getUserSelected());
			newTag.setVersion(expectedVersion1)
				.setUserSelected(expectedUserSelected1);
			savedPatient.setActive(true);
			final IIdType pid1 = myPatientDao.create(savedPatient, new SystemRequestDetails()).getId().toVersionless();

			final Patient retrievedPatient = myPatientDao.read(pid1, new SystemRequestDetails());
			validateSavedPatientTags(retrievedPatient);

			// Update the patient to create a ResourceHistoryTag record
			final List<Coding> tagsFromDbPatient = retrievedPatient.getMeta().getTag();
			assertThat(tagsFromDbPatient).hasSize(1);

			tagsFromDbPatient.get(0)
				.setCode(expectedCode2)
				.setSystem(expectedSystem2)
				.setVersion(expectedVersion2)
				.setDisplay(expectedDisplay2)
				.setUserSelected(expectedUserSelected2);

			myPatientDao.update(retrievedPatient, new SystemRequestDetails());
			final Patient retrievedUpdatedPatient = myPatientDao.read(pid1, new SystemRequestDetails());

			final Meta meta = retrievedUpdatedPatient.getMeta();
			final List<Coding> tags = meta.getTag();
			tags.forEach(innerTag -> ourLog.info("TAGS: version: {}, userSelected: {}, code: {}, display: {}, system: {}",
				innerTag.getVersion(), innerTag.getUserSelected(), innerTag.getCode(), innerTag.getDisplay(), innerTag.getSystem()));
			final Coding tagFirstRep = meta.getTagFirstRep();
			ourLog.info("TAG FIRST REP: version: {}, userSelected: {}, code: {}, display: {}, system: {}",
				tagFirstRep.getVersion(), tagFirstRep.getUserSelected(), tagFirstRep.getCode(), tagFirstRep.getDisplay(), tagFirstRep.getSystem());

			TagQtyExpectations expectedCounts = expectedTagCountMap.get(theTagStorageModeEnum);
			validateUpdatedPatientTags(expectedCounts, tags);

			final List<ResourceHistoryTag> resourceHistoryTags2 = myResourceHistoryTagDao.findAll();

			resourceHistoryTags2.forEach(historyTag -> {
				final TagDefinition tag = historyTag.getTag();
				ourLog.info("tagId: {}, resourceId: {}, version: {}, userSelected: {}, system: {}, code: {}, display: {}",
					historyTag.getTagId(), historyTag.getResourceId(), /*tag.getVersion()*/ null, /*tag.getUserSelected()*/ null, tag.getSystem(), tag.getCode(), tag.getDisplay());
			});

			TagQtyExpectations expectedHistoryCounts = expectedHistoryTagCountMap.get(theTagStorageModeEnum);
			validateHistoryTags(expectedHistoryCounts, resourceHistoryTags2);
		}


		private  void validateSavedPatientTags(Patient thePatient) {
			assertAll(
				() -> assertEquals(1, thePatient.getMeta().getTag().size()),
				() -> assertEquals(0, thePatient.getMeta().getSecurity().size()),
				() -> assertEquals(0, thePatient.getMeta().getProfile().size()),

				() -> assertEquals(expectedSystem1, thePatient.getMeta().getTagFirstRep().getSystem()),
				() -> assertTrue(thePatient.getMeta().getTagFirstRep().getUserSelected()),
				() -> assertEquals(expectedCode1, thePatient.getMeta().getTagFirstRep().getCode()),
				() -> assertEquals(expectedVersion1, thePatient.getMeta().getTagFirstRep().getVersion()),
				() -> assertEquals(expectedUserSelected1, thePatient.getMeta().getTagFirstRep().getUserSelected())
			);
		}


		private void validateUpdatedPatientTags(TagQtyExpectations theExpectedCounts, List<Coding> tags) {
			assertAll(
				() -> assertEquals(theExpectedCounts.theTagQty, tags.size()),
				() -> assertEquals(theExpectedCounts.theTagV1Qty, tags.stream()
					.filter(tag -> expectedSystem1.equals(tag.getSystem()))
					.filter(tag -> expectedCode1.equals(tag.getCode()))
					.filter(tag -> expectedDisplay1.equals(tag.getDisplay()))
					.filter(tag -> expectedVersion1.equals(tag.getVersion()))
					.filter(tag -> expectedUserSelected1 == tag.getUserSelected())
					.count()),
				() -> assertEquals(theExpectedCounts.theTagV2Qty, tags.stream()
					.filter(tag -> expectedSystem2.equals(tag.getSystem()))
					.filter(tag -> expectedCode2.equals(tag.getCode()))
					.filter(tag -> expectedDisplay2.equals(tag.getDisplay()))
					.filter(tag -> expectedVersion2.equals(tag.getVersion()))
					.filter(tag -> expectedUserSelected2 == tag.getUserSelected())
					.count())
			);
		}

		private void validateHistoryTags(TagQtyExpectations theExpectedHistoryCounts, List<ResourceHistoryTag> theHistoryTags) {
			// validating this way because tags are a set so can be in any order
			assertAll(
				() -> assertEquals(theExpectedHistoryCounts.theTagQty, theHistoryTags.size()),
				() -> assertEquals(theExpectedHistoryCounts.theTagV1Qty, theHistoryTags.stream()
					.filter(resourceHistoryTag -> expectedSystem1.equals(resourceHistoryTag.getTag().getSystem()))
					.filter(resourceHistoryTag -> expectedCode1.equals(resourceHistoryTag.getTag().getCode()))
					.filter(resourceHistoryTag -> expectedDisplay1.equals(resourceHistoryTag.getTag().getDisplay()))
					.filter(resourceHistoryTag -> expectedVersion1.equals(resourceHistoryTag.getTag().getVersion()))
					.filter(resourceHistoryTag -> expectedUserSelected1 == resourceHistoryTag.getTag().getUserSelected())
					.count()),
				() -> assertEquals(theExpectedHistoryCounts.theTagV2Qty, theHistoryTags.stream()
					.filter(resourceHistoryTag -> expectedSystem2.equals(resourceHistoryTag.getTag().getSystem()))
					.filter(resourceHistoryTag -> expectedCode2.equals(resourceHistoryTag.getTag().getCode()))
					.filter(resourceHistoryTag -> expectedDisplay2.equals(resourceHistoryTag.getTag().getDisplay()))
					.filter(resourceHistoryTag -> expectedVersion2.equals(resourceHistoryTag.getTag().getVersion()))
					.filter(resourceHistoryTag -> expectedUserSelected2 == resourceHistoryTag.getTag().getUserSelected())
					.count())
			);
		}


	}


	@Disabled("This test fails regularly, need to get a dedicated connection pool for tag creation") // TODO JA:
	@Test
	public void testConcurrentAddTag() {

		ExecutorService pool = Executors.newFixedThreadPool(10);

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			Runnable task = () -> {
				Patient patient = new Patient();
				patient.getMeta().addTag().setSystem("http://foo").setCode("bar");
				patient.setActive(true);
				ourLog.info("creating patient {}", index);
				myPatientDao.create(patient, mySrd);
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
				next.get(5, TimeUnit.SECONDS);
			} catch (Exception e) {
				ourLog.error("Failure", e);
				fail(e.toString());
			}
		}

		runInTransaction(() -> ourLog.info("Tag definitions:\n * {}", myTagDefinitionDao.findAll().stream().map(TagDefinition::toString).collect(Collectors.joining("\n * "))));

		IBundleProvider bundle = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertEquals(10, bundle.sizeOrThrowNpe());
		IBundleProvider tagBundle = myPatientDao.search(SearchParameterMap.newSynchronous(PARAM_TAG, new TokenParam("http://foo", "bar")), mySrd);
		assertEquals(10, tagBundle.sizeOrThrowNpe());

	}
}
