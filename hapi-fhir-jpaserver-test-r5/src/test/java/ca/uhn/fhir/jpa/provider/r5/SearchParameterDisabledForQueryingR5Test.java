package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.test.util.ComboSearchParameterTestHelper;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXT_SEARCHPARAM_ENABLED_FOR_SEARCHING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SearchParameterDisabledForQueryingR5Test extends BaseResourceProviderR5Test {

	@Autowired(required = false)
	protected Batch2JobHelper myBatch2JobHelper;
	private ComboSearchParameterTestHelper myComboSearchParameterTestHelper;

	@BeforeEach
	void beforeEach() {
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
		myComboSearchParameterTestHelper = new ComboSearchParameterTestHelper(mySearchParameterDao, mySearchParamRegistry);
	}

	@AfterEach
	void afterEach() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(defaults.isMarkResourcesForReindexingUponSearchParameterChange());
	}

	@ParameterizedTest
	@MethodSource("standardSearchParameters")
	void testIndexAndSearch(TestParameters theParameters) {
		// Setup
		createPatient(withId("A"), withFamily("SIMPSON"), withGiven("HOMER"));

		SearchParameter sp = theParameters.mySearchParameter;
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();

		// Test
		reindexAllPatientsAndWaitForCompletion();

		// Verify
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamString> indexes = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().equals(sp.getCode()))
				.toList();
			assertEquals(1, indexes.size());
			assertEquals(theParameters.myStringIndexValue, indexes.get(0).getValueNormalized());
		});

		// Test
		try {
			Bundle outcome = myClient
				.search()
				.forResource("Patient")
				.where(new StringClientParam(sp.getCode()).matches().value(theParameters.myStringIndexValue))
				.returnBundle(Bundle.class)
				.execute();
			if (theParameters.myExpectedErrorCode == null) {
				assertEquals(1, outcome.getEntry().size());
			} else {
				fail();
			}
		} catch (InvalidRequestException e) {
			if (theParameters.myExpectedErrorCode != null) {
				String expectedErrorMessage = "HAPI-" + theParameters.myExpectedErrorCode + ": Search parameter \"" + sp.getCode() + "\" for resource type \"Patient\" is not active for searching";
				assertThat(e.getMessage()).contains(expectedErrorMessage);

				String expectedValidParams = "Valid search parameters for this search are: [_id, _lastUpdated, _profile, _security, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, part-agree, phone, phonetic, telecom]";
				if (theParameters.mySearchParameter.getCode().equals("family")) {
					expectedValidParams = expectedErrorMessage.replace(", family", "");
				}
				assertThat(e.getMessage()).contains(expectedValidParams);
			} else {
				fail();
			}
		}
	}


	@ParameterizedTest
	@MethodSource("standardSearchParameters")
	void testCapabilityStatement(TestParameters theParameters) {
		// Setup
		SearchParameter sp = theParameters.mySearchParameter;
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();

		// Test
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		// Verify
		CapabilityStatement.CapabilityStatementRestResourceComponent patient = cs.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow();
		Set<String> searchParamNames = patient.getSearchParam().stream().map(CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent::getName).collect(Collectors.toSet());
		if (theParameters.myEnabledForSearching == Boolean.FALSE) {
			assertThat(searchParamNames).doesNotContain(theParameters.mySearchParameter.getCode());
		} else {
			assertThat(searchParamNames).contains(theParameters.mySearchParameter.getCode());
		}
	}


	@ParameterizedTest
	@CsvSource({
		// theEnabledForSearching, theUnique
		"  true                  , true",
		"  false                 , true",
		"                        , true",
		"  true                  , false",
		"  false                 , false",
		"                        , false"
	})
	public void testComboUniqueSearchParameter(Boolean theEnabledForSearching, boolean theUnique) {
		myComboSearchParameterTestHelper.createFamilyAndGenderSps(theUnique, t -> {
			if (theEnabledForSearching != null) {
				t.addExtension(EXT_SEARCHPARAM_ENABLED_FOR_SEARCHING, new BooleanType(theEnabledForSearching));
			}
		});

		createPatient(withId("A"), withFamily("simpson"), withGender("male"));

		logAllDateIndexes();
		logAllTokenIndexes();
		logAllUniqueIndexes();

		// Test
		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add(Patient.SP_FAMILY, new StringParam("simpson"))
			.add(Patient.SP_GENDER, new TokenParam( "male"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		myCaptureQueriesListener.logSelectQueries();

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactly("Patient/A");
		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true);

		if (theEnabledForSearching == Boolean.FALSE) {
			assertThat(sql).contains(ResourceIndexedSearchParamToken.HFJ_SPIDX_TOKEN);
			assertThat(sql).doesNotContain(ResourceIndexedComboStringUnique.HFJ_IDX_CMP_STRING_UNIQ);
			assertThat(sql).doesNotContain(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);
		} else {
			assertThat(sql).doesNotContain(ResourceIndexedSearchParamToken.HFJ_SPIDX_TOKEN);
			assertThat(sql).containsAnyOf(ResourceIndexedComboStringUnique.HFJ_IDX_CMP_STRING_UNIQ, ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);
		}
	}


	private void reindexAllPatientsAndWaitForCompletion() {
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Patient?");

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexUtils.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(res);
	}

	private static List<Arguments> standardSearchParameters() {
		return List.of(
			arguments(named("Custom/EnabledTrue", new TestParameters(createSearchParameterCustom(true), "SIMPSONHOMER", null))),
			arguments(named("Custom/EnabledFalse", new TestParameters(createSearchParameterCustom(false), "SIMPSONHOMER", 2539))),
			arguments(named("Custom/EnabledNull", new TestParameters(createSearchParameterCustom(null), "SIMPSONHOMER", null))),
			arguments(named("BuiltIn/EnabledTrue", new TestParameters(createSearchParameterBuiltIn(true), "SIMPSON", null))),
			arguments(named("BuiltIn/EnabledFalse", new TestParameters(createSearchParameterBuiltIn(false), "SIMPSON", 2540))),
			arguments(named("BuiltIn/EnabledNull", new TestParameters(createSearchParameterBuiltIn(null), "SIMPSON", null)))
		);
	}

	private static @Nonnull SearchParameter createSearchParameterBuiltIn(Boolean theEnabledForSearching) {
		SearchParameter retVal = createSearchParameter(theEnabledForSearching, "family", "Patient.name.family");
		retVal.setId("individual-family");
		retVal.setUrl("http://hl7.org/fhir/SearchParameter/individual-family");
		return retVal;
	}

	private static @Nonnull SearchParameter createSearchParameterCustom(Boolean theEnabledForSearching) {
		return createSearchParameter(theEnabledForSearching, "names", "Patient.name.family + Patient.name.given");
	}

	private static SearchParameter createSearchParameter(Boolean theEnabledForSearching, String code, String expression) {
		SearchParameter sp = new SearchParameter();
		if (theEnabledForSearching != null) {
			sp.addExtension(EXT_SEARCHPARAM_ENABLED_FOR_SEARCHING, new BooleanType(theEnabledForSearching));
		}
		sp.setId(code);
		sp.setName(code);
		sp.setCode(code);
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression(expression);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		return sp;
	}

	private static class TestParameters {
		private final SearchParameter mySearchParameter;
		private final String myStringIndexValue;
		private final Boolean myEnabledForSearching;
		private final Integer myExpectedErrorCode;

		private TestParameters(SearchParameter theSearchParameter, String theStringIndexValue, Integer theExpectedErrorCode) {
			mySearchParameter = theSearchParameter;
			myStringIndexValue = theStringIndexValue;
			myExpectedErrorCode = theExpectedErrorCode;

			Extension ext = mySearchParameter.getExtensionByUrl(EXT_SEARCHPARAM_ENABLED_FOR_SEARCHING);
			if (ext != null) {
				myEnabledForSearching = ext.getValueBooleanType().booleanValue();
			} else {
				myEnabledForSearching = null;
			}
		}
	}

}
