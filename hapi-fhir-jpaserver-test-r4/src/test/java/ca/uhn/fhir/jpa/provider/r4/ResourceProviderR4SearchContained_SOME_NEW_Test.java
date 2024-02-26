package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

	// LUKETODO:  find another home for this
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
			"Coverage?payor._has:List:item:_id="+LIST_ID,
			"Coverage?payor.name="+ORG_NAME,
			"Coverage?payor="+ORG_ID,
			"ExplanationOfBenefit?coverage.payor.name="+ORG_NAME,
			"ExplanationOfBenefit?coverage.payor="+ORG_ID,
			"ExplanationOfBenefit?coverage.payor._has:List:item:_id="+LIST_ID
		})
		void complexQueryFromList(String theQueryString) {
			runAndAssert(theQueryString);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Practitioner?_has:ExplanationOfBenefit:care-team:_id="+EOB_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage="+COVERAGE_ID,
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor="+ORG_ID
		})
		void complexQueryFromPractitioner(String theQueryString) {
			runAndAssert(theQueryString);
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

	@Nested
	class ComplexQueriesWithCustomSearchParam {
		private static final String PAT_ID = "pat1";
		private static final String ORG_ID = "org1";
		private static final String ORG_NAME = "myOrg";
		private static final String PRACTITIONER_ID = "pra1";
		private static final String COVERAGE_ID = "cov1";
		private static final String LIST_ID = "list1";
		private static final String GROUP_ID = "grp1";
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

			final Group group = new Group();
			group.setId(GROUP_ID);
			group.addCharacteristic().getValueReference().setReference(orgId.getValue());

			final IIdType groupId = myGroupDao.update(group, mySrd).getId().toUnqualifiedVersionless();

			final ExplanationOfBenefit explanationOfBenefit = new ExplanationOfBenefit();
			explanationOfBenefit.setId(EOB_ID);
			explanationOfBenefit.setPatient(new Reference(patientId.getValue()));
			explanationOfBenefit.setInsurer(new Reference(orgId.getValue()));
			explanationOfBenefit.setProvider(new Reference(orgId.getValue()));
			explanationOfBenefit.addCareTeam().setProvider(new Reference(practitionerId.getValue()));
			explanationOfBenefit.addInsurance().setCoverage(new Reference(coverageId.getValue()));

			myExplanationOfBenefitDao.update(explanationOfBenefit, mySrd).getId().toUnqualifiedVersionless();

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

			// LUKETODO: this causes aw reindex and Exception:  org.hibernate.HibernateException: Unable to perform beforeTransactionCompletion callback: Cannot invoke "Object.equals(Object)" because the return value of "org.hibernate.engine.spi.EntityEntry.getVersion()" is null
			mySearchParameterDao.update(searchParameterGroupValueReference, mySrd);
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor:Organization._has:Group:value-reference:_id="+GROUP_ID,
//			"Coverage?payor._has:List:item:_id="+LIST_ID,
//			"Coverage?payor.name="+ORG_NAME,
//			"Coverage?payor="+ORG_ID,
//			"ExplanationOfBenefit?coverage.payor.name="+ORG_NAME,
//			"ExplanationOfBenefit?coverage.payor="+ORG_ID,
//			"ExplanationOfBenefit?coverage.payor._has:List:item:_id="+LIST_ID
		})
		void complexQueryFromList(String theQueryString) {
			runAndAssert(theQueryString);
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

	public List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
		}
		return ids;
	}

}
