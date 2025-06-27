package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import org.apache.commons.lang3.StringUtils;
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
			assertEquals(9, myResourceTagDao.count());
			assertEquals(9, myResourceHistoryTagDao.count());
			assertEquals(9, myTagDefinitionDao.count());
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
			assertEquals(9, myResourceTagDao.count());
			assertEquals(9, myResourceHistoryTagDao.count());
			assertEquals(9, myTagDefinitionDao.count());
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


	@Test
	public void testInlineTags_Search_LargeSecurity() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		mySearchParameterDao.update(createSearchParameterForInlineTag(), mySrd);
		mySearchParameterDao.update(createSearchParameterForInlineSecurity(), mySrd);
		mySearchParamRegistry.forceRefresh();

		myCaptureQueriesListener.clear();
		Bundle outcome = myClient
			.search()
			.byUrl("PractitionerRole?_count=59&_security=22R9,15B9,94R9,69G9,28P9,49G9,55A9,9499,39B9,46O9,1899,3299,58B9,13B9,63I9,94E9,25E9,16B9,92P9,31H9,34P9,51N9,42B9,29B9,27Y2,56B9,39K9,1299,61B9,96P9,67J9,91B9,55I9,59K9,59H9,31I9,67B9,65B9,39B9,56M9,72A9,34W9,5899,69B9,94B9,42K9,18K9,52M9,71A9,63B9,4799,11B9,39H9,14B9,19K9,35B9,97B9,27B9,37B9,1499,47B9,49A9,17B9,79J9,3899,36N9,96B9,93U9,69B9,23B9,16K9,46B9,54C9,94K9,97N9,69I9,52S9,99B9,19G9,94P9,36B9,11N9,59I9,5999,35P9,15J9,38B9,71E9,2699,33B9,26B9,33P2,57A9,94U9,22B9,92I9,98B9,61J9,62A9,2399,28J9,94T9,25M9,54A9,29M9,94S9,91H9,93J9,72F9,1999,3799,32P9,12B9&_tag=9H917-2-91~9,9H917-2-91~F,9H916-3-92~9,9H916-3-92~F,9H916-2-91~9,9H916-2-91~F,9H916-3-91~9,9H916-3-91~F,9H929-2-91~9,9H929-2-91~F,9H919-2-91~9,9H919-2-91~F,9H919-2-91~C,9H919-2-91~X,9H991-6-97~9,9H991-6-97~F,9H991-6-91~9,9H991-6-91~F,9H946-5-93~9,9H946-5-93~F,9H946-5-92~9,9H946-5-92~F,9H946-2-91~9,9H946-2-91~F,9H946-5-91~9,9H946-5-91~F,9H946-4-92~9,9H946-4-92~F,9H949-2-91~9,9H949-2-91~F,9H944-3-97~9,9H944-3-97~F,9H931-3-91~9,9H931-3-91~F,9H991-6-17~9,9H991-6-17~F,9H918-2-91~9,9H918-2-91~F,9H999-3-91~9,9H999-3-91~F,9H911-2-91~9,9H911-2-91~F,9H919-2-91~9,9H919-2-91~F,9H945-4-91~9,9H945-4-91~F,9H991-4-94~9,9H991-4-94~F,9H991-6-12~9,9H991-6-12~F,9H991-6-19~9,9H991-6-19~F,9H991-6-11~9,9H991-6-11~F,9H991-3-93~9,9H991-3-93~F,9H991-6-16~9,9H991-6-16~F,9H991-6-14~9,9H991-6-14~F,9H991-6-13~9,9H991-6-13~F,9H991-6-15~9,9H991-6-15~F,9H921-3-91~9,9H921-3-91~F,9H922-3-91~9,9H922-3-91~F,9H923-2-91~9,9H923-2-91~F,9H991-6-92~9,9H991-6-92~F,9H924-3-91~9,9H924-3-91~F,9H927-4-91~9,9H927-4-91~F,9H926-3-91~9,9H926-3-91~F,9H925-3-91~9,9H925-3-91~F,9H928-2-91~9,9H928-2-91~F,9H945-4-99~9,9H945-4-99~F,9H932-3-91~9,9H932-3-91~F,9H933-2-91~9,9H933-2-91~F,9H929-2-91~9,9H929-2-91~F,9H991-6-94~9,9H991-6-94~F,9H939-3-91~9,9H939-3-91~F,9H945-4-93~9,9H945-4-93~F,9H991-6-99~9,9H991-6-99~F,9H992-4-91~9,9H992-4-91~F,9H992-5-91~9,9H992-5-91~F,9H991-6-26~9,9H991-6-26~F,9H991-6-23~9,9H991-6-23~F,9H991-6-24~9,9H991-6-24~F,9H991-5-14~9,9H991-5-14~F,9H991-6-25~9,9H991-6-25~F,9H944-3-98~9,9H944-3-98~F,9H944-3-99~9,9H944-3-99~F,9H945-4-96~9,9H945-4-96~F,9H993-4-91~9,9H993-4-91~F,9H993-4-94~9,9H993-4-94~F,9H944-3-93~9,9H944-3-93~F,9H944-4-92~9,9H944-4-92~F,9H944-3-92~9,9H944-3-92~F,9H944-4-93~9,9H944-4-93~F,9H944-4-91~9,9H944-4-91~F,9H944-4-94~9,9H944-4-94~F,9H996-3-91~9,9H996-3-91~F,9H995-3-92~9,9H995-3-92~F,9H912-4-91~9,9H912-4-91~F,9H935-3-91~9,9H935-3-91~F,9H939-3-91~9,9H939-3-91~F,9H999-3-92~9,9H999-3-92~F,9H942-3-93~9,9H942-3-93~F,9H945-4-92~9,9H945-4-92~F,9H993-4-96~9,9H993-4-96~F,9H991-6-19~9,9H991-6-19~F,9H936-3-91~9,9H936-3-91~F,9H998-5-92~9,9H998-5-92~F,9H998-2-91~9,9H998-2-91~F,9H998-4-91~9,9H998-4-91~F,9H998-4-92~9,9H998-4-92~F,9H998-5-91~9,9H998-5-91~F,9H944-3-91~9,9H944-3-91~F,9H949-2-91~9,9H949-2-91~F&_tag:not=9H991-92-OSA")
//			.byUrl("PractitionerRole?_count=59&_tag=LH917-2-91~L")
//			.byUrl("PractitionerRole?_count=59&_tag=LH917-2-91~L,LH917-2-91~F,LH916-3-92~L,LH916-3-92~F,LH916-2-91~L&_tag:not=LH991-92-OSA")
			.returnBundle(Bundle.class)
			.cacheControl(CacheControlDirective.noCache().setNoStore(true))
			.execute();
		myCaptureQueriesListener.logSelectQueries(true, true);

		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true);
		int times = org.apache.commons.lang3.StringUtils.countMatches(sql, "HFJ_SPIDX_TOKEN");
		assertEquals(3, times);

	}


}
