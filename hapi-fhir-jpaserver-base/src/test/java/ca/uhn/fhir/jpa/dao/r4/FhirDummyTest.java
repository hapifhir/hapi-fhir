package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@SuppressWarnings({"unchecked", "deprecation", "Duplicates"})
public class FhirDummyTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirDummyTest.class);

	@AfterEach
	public final void after() {
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setTreatReferencesAsLogical(new DaoConfig().getTreatReferencesAsLogical());
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(new DaoConfig().isEnforceReferentialIntegrityOnDelete());
		myDaoConfig.setEnforceReferenceTargetTypes(new DaoConfig().isEnforceReferenceTargetTypes());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		myDaoConfig.setInternalSynchronousSearchSize(new DaoConfig().getInternalSynchronousSearchSize());
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
		myDaoConfig.setHistoryCountMode(DaoConfig.DEFAULT_HISTORY_COUNT_MODE);
	}

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}


	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testConditionalCreateDoesntMakeContainedResource() {
		String patientString = "{\n" +
			"\"resourceType\": \"Patient\",\n" +
			"\"identifier\": [\n" +
			"{\n" +
			"\"system\": \"https://example.org/fhir/memberidentifier\",\n" +
			"\"value\": \"12345670\"\n" +
			"},\n" +
			"{\n" +
			"\"system\": \"https://example.org/fhir/memberuniqueidentifier\",\n" +
			"\"value\": \"12345670TT\"\n" +
			"}\n" +
			"]\n" +
			"}";

		Patient patient = myFhirCtx.newJsonParser().parseResource(Patient.class, patientString);
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(patient);
		String patientId = daoMethodOutcome.getResource().getIdElement().toVersionless().toString();

		String bundleString = "{\n" +
			"  \"resourceType\": \"Bundle\",\n" +
			"  \"type\": \"transaction\",\n" +
			"  \"entry\": [\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:1f3b9e25-fd45-4342-a82b-7ca5755923bb\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Task\",\n" +
			"        \"language\": \"en-US\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"https://example.org/fhir/taskidentifier\",\n" +
			"            \"value\": \"101019\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"basedOn\": [\n" +
			"          {\n" +
			"            \"reference\": \"urn:uuid:47c6d106-3441-41c0-8a2c-054ad9897ced\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Task?identifier\\u003dhttps%3A%2F%2Fexample.org%2Ffhir%2Ftaskidentifier|101019\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:47c6d106-3441-41c0-8a2c-054ad9897ced\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Patient\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"https://example.org/fhir/memberidentifier\",\n" +
			"            \"value\": \"12345670\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"system\": \"https://example.org/fhir/memberuniqueidentifier\",\n" +
			"            \"value\": \"12345670TT\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"POST\",\n" +
			"        \"url\": \"Patient\",\n" +
			"        \"ifNoneExist\": \"identifier\\u003dhttps%3A%2F%2Fexample.org%2Ffhir%2Fmemberuniqueidentifier|12345670TT\"\n" +
			"      }\n" +
			"    }\n" +
			"  ]\n" +
			"}";
		Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, bundleString);
		Bundle transaction2 = mySystemDao.transaction(new SystemRequestDetails(), bundle);
		String taskId2 = transaction2.getEntry().stream()
			.filter(entry -> entry.getResponse().getLocation().contains("Task"))
			.map(entry -> entry.getResponse().getLocation())
			.findFirst().orElse(null);

		Task read2 = myTaskDao.read(new IdType(taskId2));
		assertThat(read2.getBasedOn().get(0).getReference(), is(equalTo(patientId)));
	}
	@Test
	public void testConditionalUpdateDoesntMakeContainedResource() {
		String patientString = "{\n" +
			"\"resourceType\": \"Patient\",\n" +
			"\"identifier\": [\n" +
			"{\n" +
			"\"system\": \"https://example.org/fhir/memberidentifier\",\n" +
			"\"value\": \"12345670\"\n" +
			"},\n" +
			"{\n" +
			"\"system\": \"https://example.org/fhir/memberuniqueidentifier\",\n" +
			"\"value\": \"12345670TT\"\n" +
			"}\n" +
			"]\n" +
			"}";

		Patient patient = myFhirCtx.newJsonParser().parseResource(Patient.class, patientString);
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(patient);
		String patientId = daoMethodOutcome.getResource().getIdElement().toVersionless().toString();

		String bundleString = "{\n" +
			"  \"resourceType\": \"Bundle\",\n" +
			"  \"type\": \"transaction\",\n" +
			"  \"entry\": [\n" +
			"    {\n" +
//			"      \"fullUrl\": \"urn:uuid:1f3b9e25-fd45-4342-a82b-7ca5755923bb\",\n" +
			"      \"resource\": {\n" +
			"        \"id\": \"abc123\",\n" +
			"        \"resourceType\": \"Task\",\n" +
			"        \"language\": \"en-US\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"https://example.org/fhir/taskidentifier\",\n" +
			"            \"value\": \"101019\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"basedOn\": [\n" +
			"          {\n" +
			"            \"reference\": \"urn:uuid:47c6d106-3441-41c0-8a2c-054ad9897ced\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Task?identifier\\u003dhttps%3A%2F%2Fexample.org%2Ffhir%2Ftaskidentifier|101019\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:47c6d106-3441-41c0-8a2c-054ad9897ced\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Patient\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"https://example.org/fhir/memberidentifier\",\n" +
			"            \"value\": \"12345670\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"system\": \"https://example.org/fhir/memberuniqueidentifier\",\n" +
			"            \"value\": \"12345670TT\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Patient?identifier\\u003dhttps%3A%2F%2Fexample.org%2Ffhir%2Fmemberuniqueidentifier|12345670TT\"\n" +
			"      }\n" +
			"    }\n" +
			"  ]\n" +
			"}";
		Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, bundleString);
		Bundle transaction2 = mySystemDao.transaction(new SystemRequestDetails(), bundle);
		String taskId2 = transaction2.getEntry().stream()
			.filter(entry -> entry.getResponse().getLocation().contains("Task"))
			.map(entry -> entry.getResponse().getLocation())
			.findFirst().orElse(null);

		Task read2 = myTaskDao.read(new IdType(taskId2));
		assertThat(read2.getBasedOn().get(0).getReference(), is(equalTo(patientId)));
	}
}
