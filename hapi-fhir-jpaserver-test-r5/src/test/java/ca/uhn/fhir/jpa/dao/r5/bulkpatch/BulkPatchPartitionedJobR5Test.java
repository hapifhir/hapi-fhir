package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.FhirPatchBuilder;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BulkPatchPartitionedJobR5Test extends BaseBulkPatchR5Test {

	@Autowired
	private IBatch2WorkChunkRepository myBatch2WorkChunkRepository;

	@Autowired
	private BulkPatchProvider myBulkPatchProvider = new BulkPatchProvider();

	@Autowired
	private IIdHelperService<JpaPid> myIIdHelperService;

	@RegisterExtension
	private RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(FhirContext.forR5Cached())
		.withServer(server -> {
			assert myBulkPatchProvider != null;
			server.registerProvider(myBulkPatchProvider);
		});

	private IGenericClient myFhirClient;

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myPartitionSettings.setDefaultPartitionId(0);
		myPartitionSettings.setUnnamedPartitionMode(true);

		registerInterceptor(new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry));

		myFhirClient = myRestfulServerExtension.getFhirClient();
	}

	@Test
	void testPartitionedBulkPatch_SpecificPartitionsSpecified() {
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());

		int partitionAId = PatientIdPartitionInterceptor.defaultPartitionAlgorithm("A");
		int partitionCId = PatientIdPartitionInterceptor.defaultPartitionAlgorithm("C");

		Parameters patch = createPatchToAddIdentifierToPatient();

		Parameters request = new Parameters();
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PATCH)
			.setResource(patch);
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_URL)
			.setValue(new StringType("Patient?"));
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PARTITION_ID)
			.setValue(new StringType(Integer.toString(partitionAId)));
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PARTITION_ID)
			.setValue(new StringType(Integer.toString(partitionCId)));

		MethodOutcome response = myFhirClient
			.operation()
			.onServer()
			.named(JpaConstants.OPERATION_BULK_PATCH)
			.withParameters(request)
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.returnMethodOutcome()
			.execute();
		assertEquals(Constants.STATUS_HTTP_202_ACCEPTED, response.getResponseStatusCode());
		String pollLocation = response.getResponseHeaders().get(Constants.HEADER_CONTENT_LOCATION).get(0);
		String prefix = myRestfulServerExtension.getBaseUrl() + "/$hapi.fhir.bulk-patch-status?_jobId=";
		assertThat(pollLocation).startsWith(prefix);
		String jobId = pollLocation.substring(prefix.length());

		myBatch2JobHelper.awaitJobCompletion(jobId);

		// Verify
		// Since we patched partitions for resource A and C, resource B should not have been patched.
		assertEquals("123", readPatient("Patient/A").getIdentifierFirstRep().getValue());
		assertNull(readPatient("Patient/B").getIdentifierFirstRep().getValue());
	}

	@Test
	void testPartitionedBulkPatch_AllPartitionsSpecified() {
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());

		Parameters patch = createPatchToAddIdentifierToPatient();

		Parameters request = new Parameters();
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PATCH)
			.setResource(patch);
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_URL)
			.setValue(new StringType("Patient?"));
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PARTITION_ID)
			.setValue(new StringType(ProviderConstants.ALL_PARTITIONS_TENANT_NAME));

		MethodOutcome response = myFhirClient
			.operation()
			.onServer()
			.named(JpaConstants.OPERATION_BULK_PATCH)
			.withParameters(request)
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.returnMethodOutcome()
			.execute();
		assertEquals(Constants.STATUS_HTTP_202_ACCEPTED, response.getResponseStatusCode());
		String pollLocation = response.getResponseHeaders().get(Constants.HEADER_CONTENT_LOCATION).get(0);
		String prefix = myRestfulServerExtension.getBaseUrl() + "/$hapi.fhir.bulk-patch-status?_jobId=";
		assertThat(pollLocation).startsWith(prefix);
		String jobId = pollLocation.substring(prefix.length());

		myBatch2JobHelper.awaitJobCompletion(jobId);

		// Verify
		assertEquals("123", readPatient("Patient/A").getIdentifierFirstRep().getValue());
		assertEquals("123", readPatient("Patient/B").getIdentifierFirstRep().getValue());
	}

	private Parameters createPatchToAddIdentifierToPatient() {
		FhirPatchBuilder patchBuildr = new FhirPatchBuilder(myFhirContext);
		patchBuildr
			.add()
			.path("Patient")
			.name("identifier")
			.value(new Identifier().setSystem("http://foo").setValue("123"));
		return (Parameters) patchBuildr.build();
	}

}
