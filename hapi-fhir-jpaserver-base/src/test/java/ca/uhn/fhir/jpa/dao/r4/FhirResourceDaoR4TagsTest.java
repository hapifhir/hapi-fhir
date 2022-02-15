package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4TagsTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TagsTest.class);

	@Override
	@AfterEach
	public final void after() throws Exception {
		super.after();
		myDaoConfig.setTagStorageMode(DaoConfig.DEFAULT_TAG_STORAGE_MODE);
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_Read() {
		initializeNonVersioned();

		// Read

		Patient patient;
		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_Read() {
		initializeVersioned();

		// Read

		Patient patient;
		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_VRead() {
		initializeVersioned();

		Patient patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));

		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveNonVersionedTags_VRead() {
		initializeNonVersioned();

		Patient patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_History() {
		initializeVersioned();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);

		// Version 1
		Patient patient = (Patient) history.getResources(0, 999).get(1);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));

		// Version 2
		patient = (Patient) history.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_History() {
		initializeNonVersioned();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);

		// Version 1
		Patient patient = (Patient) history.getResources(0, 999).get(1);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

		// Version 2
		patient = (Patient) history.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testStoreAndRetrieveVersionedTags_Search() {
		initializeVersioned();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());

		Patient patient = (Patient) search.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_Search() {
		initializeNonVersioned();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());

		Patient patient = (Patient) search.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testInlineTags_StoreAndRetrieve() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.INLINE);

		// Store a first version
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.getMeta().addSecurity("http://sec1", "vsec1", "dsec1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		runInTransaction(()->{
			assertEquals(0, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
			assertEquals(0, myTagDefinitionDao.count());
		});

		// Read it back
		patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));
		assertThat(toSecurityLabels(patient).toString(), toSecurityLabels(patient), contains("http://sec1|vsec1|dsec1"));

		// Store a second version
		patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile2");
		patient.getMeta().addTag("http://tag2", "vtag2", "dtag2");
		patient.getMeta().addSecurity("http://sec2", "vsec2", "dsec2");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		runInTransaction(()->{
			assertEquals(0, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
			assertEquals(0, myTagDefinitionDao.count());
		});

		// First version should have only the initial tags
		patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));
		assertThat(toSecurityLabels(patient).toString(), toSecurityLabels(patient), contains("http://sec1|vsec1|dsec1"));

		// Second version should have the new set of tags
		// TODO: We could copy these forward like we do for non-inline mode. Perhaps in the future.
		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag2|vtag2|dtag2"));
		assertThat(toSecurityLabels(patient).toString(), toSecurityLabels(patient), containsInAnyOrder("http://sec2|vsec2|dsec2"));

	}


	@Test
	public void testInlineTags_Search_Tag() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = createResourceTagSearchParameter();
		ourLog.info("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_tag").exactly().systemAndCode("http://tag1", "vtag1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder("Patient/A", "Patient/B"));

		validatePatientSearchResultsForInlineTags(outcome);
	}

	@Test
	public void testInlineTags_Search_Profile() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = createSearchParamForInlineResourceProfile();
		ourLog.info("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_profile").exactly().code("http://profile1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder("Patient/A", "Patient/B"));
		validatePatientSearchResultsForInlineTags(outcome);
	}

	@Test
	public void testInlineTags_Search_Security() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-security");
		for (String next : myFhirContext.getResourceTypes().stream().sorted().collect(Collectors.toList())) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setCode("_security");
		searchParameter.setName("Security");
		searchParameter.setExpression("meta.security");
		ourLog.info("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_security").exactly().systemAndCode("http://sec1", "vsec1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder("Patient/A", "Patient/B"));

		validatePatientSearchResultsForInlineTags(outcome);
	}

	private void validatePatientSearchResultsForInlineTags(Bundle outcome) {
		Patient patient;
		patient = (Patient) outcome.getEntry().get(0).getResource();
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));
		assertThat(toSecurityLabels(patient).toString(), toSecurityLabels(patient), contains("http://sec1|vsec1|dsec1"));
		patient = (Patient) outcome.getEntry().get(1).getResource();
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));
		assertThat(toSecurityLabels(patient).toString(), toSecurityLabels(patient), contains("http://sec1|vsec1|dsec1"));
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
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.NON_VERSIONED);

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
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.VERSIONED);

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
	private List<String> toTags(Patient patient) {
		return patient.getMeta().getTag().stream().map(t -> t.getSystem() + "|" + t.getCode() + "|" + t.getDisplay()).collect(Collectors.toList());
	}

	@Nonnull
	private List<String> toSecurityLabels(Patient patient) {
		return patient.getMeta().getSecurity().stream().map(t -> t.getSystem() + "|" + t.getCode() + "|" + t.getDisplay()).collect(Collectors.toList());
	}

	@Nonnull
	private List<String> toProfiles(Patient patient) {
		return patient.getMeta().getProfile().stream().map(t -> t.getValue()).collect(Collectors.toList());
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
