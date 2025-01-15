package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4TagsTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TagsTest.class);

	@Override
	@AfterEach
	public final void after() throws Exception {
		super.after();
		myStorageSettings.setTagStorageMode(JpaStorageSettings.DEFAULT_TAG_STORAGE_MODE);
	}


	/**
	 * Make sure tags are preserved
	 */
	@Test
	public void testDeleteResourceWithTags_NonVersionedTags() {
		initializeNonVersioned();

		// Delete

		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));
		IIdType outcomeId = myPatientDao.delete(new IdType("Patient/A"), mySrd).getId();
		assertEquals("3", outcomeId.getVersionIdPart());
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));

		// Make sure $meta-get can fetch the tags of the deleted resource

		Meta meta = myPatientDao.metaGetOperation(Meta.class, new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(meta)).as(toProfiles(meta).toString()).containsExactly("http://profile2");
		assertThat(toTags(meta)).as(toTags(meta).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
		assertEquals("3", meta.getVersionId());

		// Revive and verify

		Patient patient = new Patient();
		patient.setId("A");
		patient.getMeta().addProfile("http://profile3");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		patient = (Patient) myPatientDao.update(patient, mySrd).getResource();
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
		myCaptureQueriesListener.logAllQueries();
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));

		// Read it back

		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	/**
	 * Make sure tags are preserved
	 */
	@Test
	public void testDeleteResourceWithTags_VersionedTags() {
		initializeVersioned();

		// Delete

		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));
		myPatientDao.delete(new IdType("Patient/A"), mySrd);
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));

		// Make sure $meta-get can fetch the tags of the deleted resource

		Meta meta = myPatientDao.metaGetOperation(Meta.class, new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(meta)).as(toProfiles(meta).toString()).containsExactly("http://profile2");
		assertThat(toTags(meta)).as(toTags(meta).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

		// Revive and verify

		Patient patient = new Patient();
		patient.setId("A");
		patient.getMeta().addProfile("http://profile3");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		patient = (Patient) myPatientDao.update(patient, mySrd).getResource();
		myCaptureQueriesListener.logAllQueries();
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

		// Read it back

		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	/**
	 * Make sure tags are preserved
	 */
	@Test
	public void testDeleteResourceWithTags_NonVersionedTags_InTransaction() {
		initializeNonVersioned();
		when(mySrd.getHeader(eq(Constants.HEADER_PREFER))).thenReturn("return=representation");
		Bundle input, output;

		// Delete

		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));
		input = new BundleBuilder(myFhirContext)
			.addTransactionDeleteEntry(new IdType("Patient/A"))
			.andThen()
			.getBundleTyped();
		output = mySystemDao.transaction(mySrd, input);
		IIdType outcomeId = new IdType(output.getEntry().get(0).getResponse().getLocation());
		assertEquals("3", outcomeId.getVersionIdPart());
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));

		// Make sure $meta-get can fetch the tags of the deleted resource

		Meta meta = myPatientDao.metaGetOperation(Meta.class, new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(meta)).as(toProfiles(meta).toString()).containsExactly("http://profile2");
		assertThat(toTags(meta)).as(toTags(meta).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
		assertEquals("3", meta.getVersionId());

		// Revive and verify

		Patient patient = new Patient();
		patient.setId("A");
		patient.getMeta().addProfile("http://profile3");
		patient.setActive(true);

		myCaptureQueriesListener.clear();

		input = new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(patient)
			.andThen()
			.getBundleTyped();
		output = mySystemDao.transaction(mySrd, input);
		patient = (Patient) output.getEntry().get(0).getResource();
		assert patient != null;

		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
		myCaptureQueriesListener.logAllQueries();
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));

		// Read it back

		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	/**
	 * Make sure tags are preserved
	 */
	@Test
	public void testDeleteResourceWithTags_VersionedTags_InTransaction() {
		initializeVersioned();
		when(mySrd.getHeader(eq(Constants.HEADER_PREFER))).thenReturn("return=representation");
		Bundle input, output;

		// Delete

		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));
		input = new BundleBuilder(myFhirContext)
			.addTransactionDeleteEntry(new IdType("Patient/A"))
			.andThen()
			.getBundleTyped();
		output = mySystemDao.transaction(mySrd, input);
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));

		// Make sure $meta-get can fetch the tags of the deleted resource

		Meta meta = myPatientDao.metaGetOperation(Meta.class, new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(meta)).as(toProfiles(meta).toString()).containsExactly("http://profile2");
		assertThat(toTags(meta)).as(toTags(meta).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

		// Revive and verify

		Patient patient = new Patient();
		patient.setId("A");
		patient.getMeta().addProfile("http://profile3");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		input = new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(patient)
			.andThen()
			.getBundleTyped();
		output = mySystemDao.transaction(mySrd, input);
		patient = (Patient) output.getEntry().get(0).getResource();
		assert patient != null;
		myCaptureQueriesListener.logAllQueries();
		runInTransaction(() -> assertEquals(3, myResourceTagDao.count()));
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

		// Read it back

		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactlyInAnyOrder("http://profile3");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	@Test
	public void testStoreAndRetrieveNonVersionedTags_Read() {
		initializeNonVersioned();

		// Read

		Patient patient;
		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_Read() {
		initializeVersioned();

		// Read

		Patient patient;
		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_VRead() {
		initializeVersioned();

		Patient patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile1");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactly("http://tag1|vtag1|dtag1");

		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	@Test
	public void testStoreAndRetrieveNonVersionedTags_VRead() {
		initializeNonVersioned();

		Patient patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_History() {
		initializeVersioned();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);

		// Version 1
		Patient patient = (Patient) history.getResources(0, 999).get(1);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile1");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactly("http://tag1|vtag1|dtag1");

		// Version 2
		patient = (Patient) history.getResources(0, 999).get(0);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_History() {
		initializeNonVersioned();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);

		// Version 1
		Patient patient = (Patient) history.getResources(0, 999).get(1);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");

		// Version 2
		patient = (Patient) history.getResources(0, 999).get(0);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
	}


	@Test
	public void testStoreAndRetrieveVersionedTags_Search() {
		initializeVersioned();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());

		Patient patient = (Patient) search.getResources(0, 999).get(0);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_Search() {
		initializeNonVersioned();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());

		Patient patient = (Patient) search.getResources(0, 999).get(0);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2");
	}


	@Test
	public void testInlineTags_StoreAndRetrieve() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		// Store a first version
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.getMeta().addSecurity("http://sec1", "vsec1", "dsec1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		runInTransaction(() -> {
			assertEquals(0, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
			assertEquals(0, myTagDefinitionDao.count());
		});

		// Read it back
		patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile1");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactly("http://tag1|vtag1|dtag1");
		assertThat(toSecurityLabels(patient)).as(toSecurityLabels(patient).toString()).containsExactly("http://sec1|vsec1|dsec1");

		// Store a second version
		patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile2");
		patient.getMeta().addTag("http://tag2", "vtag2", "dtag2");
		patient.getMeta().addSecurity("http://sec2", "vsec2", "dsec2");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		runInTransaction(() -> {
			assertEquals(0, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
			assertEquals(0, myTagDefinitionDao.count());
		});

		// First version should have only the initial tags
		patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile1");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactly("http://tag1|vtag1|dtag1");
		assertThat(toSecurityLabels(patient)).as(toSecurityLabels(patient).toString()).containsExactly("http://sec1|vsec1|dsec1");

		// Second version should have the new set of tags
		// TODO: We could copy these forward like we do for non-inline mode. Perhaps in the future.
		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile2");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactlyInAnyOrder("http://tag2|vtag2|dtag2");
		assertThat(toSecurityLabels(patient)).as(toSecurityLabels(patient).toString()).containsExactlyInAnyOrder("http://sec2|vsec2|dsec2");

	}


	@Test
	public void testInlineTags_Search_Tag() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = createResourceTagSearchParameter();
		ourLog.debug("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_tag").exactly().systemAndCode("http://tag1", "vtag1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder("Patient/A", "Patient/B");

		validatePatientSearchResultsForInlineTags(outcome);
	}

	@Test
	public void testMetaDelete_TagStorageModeNonVersioned_ShouldShowRemainingTagsInGetAllResources() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		Patient pt = new Patient();
		Meta pMeta = new Meta();
		pMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		pMeta.addTag().setSystem("urn:system2").setCode("urn:code2");
		pt.setMeta(pMeta);
		IIdType id = myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		Meta meta = myClient.meta().get(Meta.class).fromResource(id).execute();
		assertThat(meta.getTag()).hasSize(2);

		Meta inMeta = new Meta();
		inMeta.addTag().setSystem("urn:system2").setCode("urn:code2");
		meta = myClient.meta().delete().onResource(id).meta(inMeta).execute();
		assertThat(meta.getTag()).hasSize(1);

		Bundle patientBundle = myClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
		Patient patient = (Patient) patientBundle.getEntry().get(0).getResource();
		assertThat(patient.getMeta().getTag()).hasSize(1);
	}

	@Test
	public void testMetaDelete_TagStorageModeVersioned_ShouldShowRemainingTagsInGetAllResources() {
		Patient pt = new Patient();
		Meta pMeta = new Meta();
		pMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		pMeta.addTag().setSystem("urn:system2").setCode("urn:code2");
		pt.setMeta(pMeta);
		IIdType id = myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		Meta meta = myClient.meta().get(Meta.class).fromResource(id).execute();
		assertThat(meta.getTag()).hasSize(2);

		Meta inMeta = new Meta();
		inMeta.addTag().setSystem("urn:system2").setCode("urn:code2");
		meta = myClient.meta().delete().onResource(id).meta(inMeta).execute();
		assertThat(meta.getTag()).hasSize(1);

		Bundle patientBundle = myClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
		Patient patient = (Patient) patientBundle.getEntry().get(0).getResource();
		assertThat(patient.getMeta().getTag()).hasSize(1);
	}

	@Test
	public void testInlineTags_Search_Profile() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = createSearchParamForInlineResourceProfile();
		ourLog.debug("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_profile").exactly().code("http://profile1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder("Patient/A", "Patient/B");
		validatePatientSearchResultsForInlineTags(outcome);
	}

	@Test
	public void testInlineTags_Search_Security() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		FhirContext fhirContext = myFhirContext;
		SearchParameter searchParameter = createSecuritySearchParameter(fhirContext);
		ourLog.debug("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_security").exactly().systemAndCode("http://sec1", "vsec1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder("Patient/A", "Patient/B");

		validatePatientSearchResultsForInlineTags(outcome);
	}

	@Nonnull
	public static SearchParameter createSecuritySearchParameter(FhirContext fhirContext) {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-security");
		for (String next : fhirContext.getResourceTypes().stream().sorted().collect(Collectors.toList())) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setCode("_security");
		searchParameter.setName("Security");
		searchParameter.setExpression("meta.security");
		return searchParameter;
	}

	private void validatePatientSearchResultsForInlineTags(Bundle outcome) {
		Patient patient;
		patient = (Patient) outcome.getEntry().get(0).getResource();
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile1");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactly("http://tag1|vtag1|dtag1");
		assertThat(toSecurityLabels(patient)).as(toSecurityLabels(patient).toString()).containsExactly("http://sec1|vsec1|dsec1");
		patient = (Patient) outcome.getEntry().get(1).getResource();
		assertThat(toProfiles(patient)).as(toProfiles(patient).toString()).containsExactly("http://profile1");
		assertThat(toTags(patient)).as(toTags(patient).toString()).containsExactly("http://tag1|vtag1|dtag1");
		assertThat(toSecurityLabels(patient)).as(toSecurityLabels(patient).toString()).containsExactly("http://sec1|vsec1|dsec1");
	}

	private void createPatientsForInlineSearchTests() {
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.getMeta().addSecurity("http://sec1", "vsec1", "dsec1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("Patient/B");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.getMeta().addSecurity("http://sec1", "vsec1", "dsec1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("Patient/NO");
		patient.getMeta().addProfile("http://profile99");
		patient.getMeta().addTag("http://tag99", "vtag99", "dtag99");
		patient.getMeta().addSecurity("http://sec99", "vsec99", "dsec99");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);
	}

	private void initializeNonVersioned() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile2");
		patient.getMeta().addTag("http://tag2", "vtag2", "dtag2");
		patient.setActive(false);
		assertEquals("2", myPatientDao.update(patient, mySrd).getId().getVersionIdPart());
	}

	private void initializeVersioned() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.VERSIONED);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile2");
		patient.getMeta().addTag("http://tag2", "vtag2", "dtag2");
		patient.setActive(false);
		assertEquals("2", myPatientDao.update(patient, mySrd).getId().getVersionIdPart());
	}

	@Nonnull
	static List<String> toTags(Patient patient) {
		return toTags(patient.getMeta());
	}

	@Nonnull
	static List<String> toSecurityLabels(Patient patient) {
		return toSecurityLabels(patient.getMeta());
	}

	@Nonnull
	static List<String> toProfiles(Patient patient) {
		return toProfiles(patient.getMeta());
	}

	@Nonnull
	static List<String> toTags(Meta meta) {
		return meta.getTag().stream().map(t -> t.getSystem() + "|" + t.getCode() + "|" + t.getDisplay()).collect(Collectors.toList());
	}

	@Nonnull
	static List<String> toSecurityLabels(Meta meta) {
		return meta.getSecurity().stream().map(t -> t.getSystem() + "|" + t.getCode() + "|" + t.getDisplay()).collect(Collectors.toList());
	}

	@Nonnull
	static List<String> toProfiles(Meta meta) {
		return meta.getProfile().stream().map(t -> t.getValue()).collect(Collectors.toList());
	}

	@Nonnull
	public static SearchParameter createSearchParamForInlineResourceProfile() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-profile");
		for (String next : FhirContext.forR4Cached().getResourceTypes().stream().sorted().collect(Collectors.toList())) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.URI);
		searchParameter.setCode("_profile");
		searchParameter.setName("Profile");
		searchParameter.setExpression("meta.profile");
		return searchParameter;
	}

	@Nonnull
	public static SearchParameter createResourceTagSearchParameter() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-tag");
		for (String next : FhirContext.forR4Cached().getResourceTypes().stream().sorted().collect(Collectors.toList())) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setCode("_tag");
		searchParameter.setName("Tag");
		searchParameter.setExpression("meta.tag");
		return searchParameter;
	}

}
