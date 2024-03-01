package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.param.HasParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class ResourceProviderR4SearchContained_SOME_NEW_Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4SearchContained_SOME_NEW_Test.class);
	@Autowired
	@Qualifier("myClinicalImpressionDaoR4")
	protected IFhirResourceDao<ClinicalImpression> myClinicalImpressionDao;
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
	}

	@Nested
	class TripleHas {
		private static final String PAT_ID = "pat1";
		private static final String OBSERVATION_ID = "obs1";
		private static final String ENCOUNTER_ID = "enc1";
		private static final String ADVERSE_EVENT_ID = "adv1";
		private static final String CARE_PLAN_ID = "cp1";

		@BeforeEach
		void beforeEach() {
			final Patient patient = new Patient();
			patient.setId(PAT_ID);

			final IIdType patientId = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();

			final Observation observation = new Observation();
			observation.setId(OBSERVATION_ID);
			observation.setSubject(new Reference(patientId.getValue()));

			res2Json(observation);

			final IIdType observationId = myObservationDao.update(observation, mySrd).getId().toUnqualifiedVersionless();

			final Encounter encounter = new Encounter();
			encounter.setId(ENCOUNTER_ID);
			encounter.addReasonReference(new Reference(observationId.getValue()));

			res2Json(encounter);

			final IIdType encounterId = myEncounterDao.update(encounter, mySrd).getId().toUnqualifiedVersionless();

			final CarePlan carePlan = new CarePlan();
			carePlan.setId(CARE_PLAN_ID);
			carePlan.setEncounter(new Reference(encounterId.getValue()));

			res2Json(carePlan);

			myCarePlanDao.update(carePlan, mySrd);

			final List<ResourceLink> all = myResourceLinkDao.findAll();
			all.forEach(link -> ourLog.info("link:{}", link));
		}

		private void res2Json(IBaseResource theResource) {
			final String json = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theResource);

			ourLog.info("\n"+json);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Patient?_has:Observation:subject:_has:Encounter:reason-reference:_id="+ENCOUNTER_ID,
		})
		void doubleHas(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
//			"Patient?_id="+PAT_ID,
//			"CarePlan?_id="+CARE_PLAN_ID,
//			"Observation?subject="+PAT_ID,
//			"Patient?_has:Observation:subject:_id="+OBSERVATION_ID,
//			"Observation?_has:Encounter:reason-reference:_id="+ENCOUNTER_ID,
//			"Patient?_has:Observation:subject:_has:Encounter:reason-reference:_id="+ENCOUNTER_ID,
//			"Observation?_has:Encounter:reason-reference:_has:CarePlan:encounter:_id="+CARE_PLAN_ID,
			"Patient?_has:Observation:subject:_has:Encounter:reason-reference:_has:CarePlan:encounter:_id="+CARE_PLAN_ID
		})
		void tripleHas(String theQueryString) {
			runAndAssert(theQueryString);
		}
	}

	@Nested
	class ComplexQueries {
		private static final String PAT_ID = "pat1";
		private static final String ORG_ID = "org1";
		private static final String ORG_NAME = "myOrg";
		private static final String PRACTITIONER_ID = "pra1";
		private static final String COVERAGE_ID = "cov1";
		private static final String LIST_ID = "list1";
		private static final String EOB_ID = "eob1";

		@BeforeEach
		void beforeEach() {
			myStorageSettings.setIndexOnContainedResources(false);
			final Patient patient = new Patient();
			patient.setId(PAT_ID);

			final IIdType patientId = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();

			final Organization organization = new Organization();
			organization.setId(ORG_ID);
			organization.setName(ORG_NAME);

			final IIdType orgId = myOrganizationDao.update(organization, mySrd).getId().toUnqualifiedVersionless();

			final Practitioner practitioner = new Practitioner();
			practitioner.setId(PRACTITIONER_ID);

			final IIdType practitionerId = myPractitionerDao.update(practitioner, mySrd).getId().toUnqualifiedVersionless();

			final Coverage coverage = new Coverage();
			coverage.setId(COVERAGE_ID);
			coverage.addPayor().setReference(orgId.getValue());

			final IIdType coverageId = myCoverageDao.update(coverage, mySrd).getId().toUnqualifiedVersionless();

			final ListResource list = new ListResource();
			list.setId(LIST_ID);
			list.addEntry().setItem(new Reference(orgId.getValue()));

			myListDao.update(list, mySrd).getId().toUnqualifiedVersionless();

            final ExplanationOfBenefit explanationOfBenefit = new ExplanationOfBenefit();
			explanationOfBenefit.setId(EOB_ID);
			explanationOfBenefit.setPatient(new Reference(patientId.getValue()));
			explanationOfBenefit.setInsurer(new Reference(orgId.getValue()));
			explanationOfBenefit.setProvider(new Reference(orgId.getValue()));
			explanationOfBenefit.addCareTeam().setProvider(new Reference(practitionerId.getValue()));
			explanationOfBenefit.addInsurance().setCoverage(new Reference(coverageId.getValue()));

            myExplanationOfBenefitDao.update(explanationOfBenefit, mySrd).getId().toUnqualifiedVersionless();
        }

		@ParameterizedTest
		@ValueSource(strings = {
//			"Coverage?payor.name="+ORG_NAME,
			"ExplanationOfBenefit?coverage.payor.name="+ORG_NAME,
		})
		void chain(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor="+ORG_ID, // This is the first half
		})
		void hasThenChainSimple(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"ExplanationOfBenefit?coverage.payor:Organization._has:List:item:_id="+LIST_ID
		})
		void chainThenHasSimple(String theQueryString) {
			// LUKETODO:  this workflow is the gold standard on how to handle _has > chain > _has
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Coverage?payor._has:List:item:_id="+LIST_ID, // dot _has
			"Coverage?payor.name="+ORG_NAME,
			"Coverage?payor="+ORG_ID,
			"ExplanationOfBenefit?coverage="+COVERAGE_ID,
			"ExplanationOfBenefit?coverage.payor.name="+ORG_NAME,
			"ExplanationOfBenefit?coverage.payor="+ORG_ID,
			"ExplanationOfBenefit?coverage.payor._has:List:item:_id="+LIST_ID,
			"ExplanationOfBenefit?coverage.payor:Organization._has:List:item:_id="+LIST_ID,
			"Organization?_has:List:item:_id="+LIST_ID, // This the second half of the buggy query
		})
		void complexQueryFromList(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Organization?_has:Coverage:payor:_has:ExplanationOfBenefit:coverage:_id="+EOB_ID, // THIS WORKS!!!!!!!
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor="+ORG_ID, // this works
//			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor.name="+ORG_NAME, // this does not work
			"Practitioner?_has:ExplanationOfBenefit:care-team:_id="+EOB_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage="+COVERAGE_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor="+ORG_ID, // This is the first half
//			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor._has:List:item:_id="+LIST_ID, // this doesn't work ... reomoves Organzation
		})
		void complexQueryFromPractitioner(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor._has:List:item:_id="+LIST_ID,
//			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor:Organization._has:List:item:_id="+LIST_ID  // same thing
		})
		void verySimilarToBug(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@Test
		void searchWithSearchParameterSameAsBug() {
			final SearchParameterMap searchParameterMap = new SearchParameterMap();

			final HasParam hasParam = new HasParam("ExplanationOfBenefit", "care-team", "coverage.payor:Organization._has:List:item:_id", LIST_ID);

			searchParameterMap.add("_has", hasParam);

			final IBundleProvider search = myPractitionerDao.search(searchParameterMap, mySrd);

			assertFalse(search.isEmpty());
		}

		@Test
		void searchWithSearchParameterFirstHalf() {
			final SearchParameterMap searchParameterMap = new SearchParameterMap();

//			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor:Organization="+ORG_ID  // This the first half of the buggy query
			final HasParam hasParam = new HasParam("ExplanationOfBenefit", "care-team", "coverage.payor", ORG_ID);

			searchParameterMap.add("_has", hasParam);

			final IBundleProvider search = myPractitionerDao.search(searchParameterMap, mySrd);

			assertFalse(search.isEmpty());
		}

		@Test
		void searchWithSearchParameterSecondHalf() {
			final SearchParameterMap searchParameterMap = new SearchParameterMap();

//			"Organization?_has:List:item:_id="+LIST_ID, // This the second half of the buggy query
			final HasParam hasParam = new HasParam("List", "item", "_id", LIST_ID);

			searchParameterMap.add("_has", hasParam);

			final IBundleProvider search = myOrganizationDao.search(searchParameterMap, mySrd);

			assertFalse(search.isEmpty());
		}

		@Test
		@Disabled
		void searchWithSearchParameterWithIdealComposition() {
			final SearchParameterMap searchParameterMap = new SearchParameterMap();

//			params.add(PARAM_HAS, new HasParam("Observation", "subject", "_has:DiagnosticReport:result:status", "final"));
//			final HasParam hasParamIdeal = new HasParam("ExplanationOfBenefit", "care-team", "coverage.payor:Organization._has:List:item:_id", LIST_ID);
			final HasParam hasParamIdeal = new HasParam("ExplanationOfBenefit", "care-team", "coverage.payor", "Organization._has:List:item:_id=" +LIST_ID);

			// LUKETODO:  try:
			// patient -> observation -> encounter -> adverseevent

			searchParameterMap.add("_has", hasParamIdeal);

			final IBundleProvider search = myPractitionerDao.search(searchParameterMap, mySrd);

			assertFalse(search.isEmpty());
		}
	}

	@Nested
	class ComplexQueriesWithCustomSearchParam {
		private static final String PAT_ID = "pat1";
		private static final String ORG_ID = "org1";
		private static final String ORG_NAME = "myOrg";
		private static final String PRACTITIONER_ID = "pra1";
		private static final String COVERAGE_ID = "cov1";
		private static final String GROUP_ID = "grp1";
		private static final String EOB_ID = "eob1";

		@BeforeEach
		void beforeEach() {
			myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
			myStorageSettings.setIndexOnContainedResources(false);

			// LUKETODO:  not exactly like production but create SearchParameter first to create the RES_LINK
			// and avoid a call to reindex
			final SearchParameter searchParameterGroupValueReference = new SearchParameter();
			searchParameterGroupValueReference.setId("group-value-reference");
			searchParameterGroupValueReference.setName("group-value-reference");
			searchParameterGroupValueReference.addBase("Group");
			searchParameterGroupValueReference.setStatus(Enumerations.PublicationStatus.ACTIVE);
			searchParameterGroupValueReference.setCode("value-reference");
			searchParameterGroupValueReference.setType(Enumerations.SearchParamType.REFERENCE);
			searchParameterGroupValueReference.setExpression("Group.characteristic.value.as(Reference)");
			searchParameterGroupValueReference.addTarget("Organization");
			searchParameterGroupValueReference.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);

			mySearchParameterDao.update(searchParameterGroupValueReference, mySrd);

			final Patient patient = new Patient();
			patient.setId(PAT_ID);

			final IIdType patientId = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();

			final Organization organization = new Organization();
			organization.setId(ORG_ID);
			organization.setName(ORG_NAME);

			final IIdType orgId = myOrganizationDao.update(organization, mySrd).getId().toUnqualifiedVersionless();

			final Practitioner practitioner = new Practitioner();
			practitioner.setId(PRACTITIONER_ID);

			final IIdType practitionerId = myPractitionerDao.update(practitioner, mySrd).getId().toUnqualifiedVersionless();

			final Coverage coverage = new Coverage();
			coverage.setId(COVERAGE_ID);
			coverage.addPayor().setReference(orgId.getValue());

			final IIdType coverageId = myCoverageDao.update(coverage, mySrd).getId().toUnqualifiedVersionless();

			final Group group = new Group();
			group.setId(GROUP_ID);
			group.addCharacteristic().getValueReference().setReference(orgId.getValue());

			myGroupDao.update(group, mySrd);

			final ExplanationOfBenefit explanationOfBenefit = new ExplanationOfBenefit();
			explanationOfBenefit.setId(EOB_ID);
			explanationOfBenefit.setPatient(new Reference(patientId.getValue()));
			explanationOfBenefit.setInsurer(new Reference(orgId.getValue()));
			explanationOfBenefit.setProvider(new Reference(orgId.getValue()));
			explanationOfBenefit.addCareTeam().setProvider(new Reference(practitionerId.getValue()));
			explanationOfBenefit.addInsurance().setCoverage(new Reference(coverageId.getValue()));

			myExplanationOfBenefitDao.update(explanationOfBenefit, mySrd).getId().toUnqualifiedVersionless();
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Coverage?payor.name="+ORG_NAME,
			"Coverage?payor="+ORG_ID,
			"ExplanationOfBenefit?coverage.payor.name="+ORG_NAME,
			"ExplanationOfBenefit?coverage.payor="+ORG_ID,
			"ExplanationOfBenefit?care-team="+PRACTITIONER_ID,
			"ExplanationOfBenefit?coverage="+COVERAGE_ID,
			"ExplanationOfBenefit?provider="+ORG_ID,
		})
		void complexQueryFromGroup(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Group?value-reference="+ORG_ID,
			"Organization?_has:Group:value-reference:_id="+GROUP_ID, // This the second half of the buggy query
		})
		void useCustomSearchParam(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			// LUKETODO:  this is the exact scenario in the bug
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor:Organization._has:Group:value-reference:_id="+GROUP_ID,
		})
		void bug(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Practitioner?_has:ExplanationOfBenefit:care-team:_id="+EOB_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage="+COVERAGE_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor="+ORG_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor:Organization="+ORG_ID,  // This the first half of the buggy query
//			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor._has:Group:value-reference:_id="+GROUP_ID, // this doesn't work
//			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor:Organization.name="+ORG_NAME // This doesn't work
		})
		void complexQueryFromPractitioner(String theQueryString) {
			runAndAssert(theQueryString);
		}
	}


	private void runAndAssert(String theQueryString) {
		ourLog.info("queryString:\n{}", theQueryString);

		final Bundle outcome = myClient.search()
			.byUrl(theQueryString)
			.returnBundle(Bundle.class)
			.execute();

		assertFalse(outcome.getEntry().isEmpty());
		ourLog.info("result:\n{}", theQueryString);
	}
}
