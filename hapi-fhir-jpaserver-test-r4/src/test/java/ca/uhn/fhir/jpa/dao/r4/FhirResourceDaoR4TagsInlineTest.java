package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;

import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TagsTest.toProfiles;
import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TagsTest.toSecurityLabels;
import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TagsTest.toTags;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4TagsInlineTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TagsInlineTest.class);

	@Override
	@AfterEach
	public final void after() throws Exception {
		super.after();
		myStorageSettings.setTagStorageMode(JpaStorageSettings.DEFAULT_TAG_STORAGE_MODE);
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

		SearchParameter searchParameter = createSearchParameterForInlineTag();
		ourLog.debug("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		logAllResources();
		logAllResourceVersions();

		createPatientsForInlineSearchTests();

		logAllTokenIndexes();

		// Perform a search
		Bundle outcome = myClient.search().forResource("Patient").where(new TokenClientParam("_tag").exactly().systemAndCode("http://tag1", "vtag1")).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder("Patient/A", "Patient/B");

		validatePatientSearchResultsForInlineTags(outcome);
	}

	@Test
	public void testInlineTags_Search_Profile() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = createSearchParameterForInlineProfile();
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

		SearchParameter searchParameter = createSearchParameterForInlineSecurity();
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

	@Nonnull
	public static SearchParameter createSearchParameterForInlineTag() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-tag");
		for (String next : FhirContext.forR4Cached().getResourceTypes().stream().sorted().toList()) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setCode("_tag");
		searchParameter.setName("Tag");
		searchParameter.setExpression("meta.tag");
		return searchParameter;
	}

	@Nonnull
	public static SearchParameter createSearchParameterForInlineSecurity() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-security");
		for (String next : FhirContext.forR4Cached().getResourceTypes().stream().sorted().toList()) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setCode("_security");
		searchParameter.setName("Security");
		searchParameter.setExpression("meta.security");
		return searchParameter;
	}

	@Nonnull
	public static SearchParameter createSearchParameterForInlineProfile() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("SearchParameter/resource-profile");
		for (String next : FhirContext.forR4Cached().getResourceTypes().stream().sorted().toList()) {
			searchParameter.addBase(next);
		}
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.URI);
		searchParameter.setCode("_profile");
		searchParameter.setName("Profile");
		searchParameter.setExpression("meta.profile");
		return searchParameter;
	}

}
