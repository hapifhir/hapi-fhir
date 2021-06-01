package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.DEFAULT_PARTITION_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultitenantDeleteExpungeR4 extends BaseMultitenantResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MultitenantDeleteExpungeR4.class);

	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Test
	public void testDeleteExpungeOperation() {
		// Create patients

		IIdType idAT = createPatient(withTenant(TENANT_A), withActiveTrue());
		IIdType idAF = createPatient(withTenant(TENANT_A), withActiveFalse());
		IIdType idBT = createPatient(withTenant(TENANT_B), withActiveTrue());
		IIdType idBF = createPatient(withTenant(TENANT_B), withActiveFalse());

		// validate setup
		assertEquals(2, getAllPatientsInTenant(TENANT_A).getTotal());
		assertEquals(2, getAllPatientsInTenant(TENANT_B).getTotal());
		assertEquals(0, getAllPatientsInTenant(DEFAULT_PARTITION_NAME).getTotal());

		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, "/Patient?active=false");

		// execute

		myTenantClientInterceptor.setTenantId(TENANT_B);
		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		myBatchJobHelper.awaitAllBulkJobCompletions(BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME);

		DecimalType jobIdPrimitive = (DecimalType) response.getParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID);
		Long jobId = jobIdPrimitive.getValue().longValue();

		assertEquals(1, myBatchJobHelper.getReadCount(jobId));
		assertEquals(1, myBatchJobHelper.getWriteCount(jobId));

		// validate only the false patient in TENANT_B is removed
		assertEquals(2, getAllPatientsInTenant(TENANT_A).getTotal());
		assertEquals(1, getAllPatientsInTenant(TENANT_B).getTotal());
		assertEquals(0, getAllPatientsInTenant(DEFAULT_PARTITION_NAME).getTotal());
	}

	private Bundle getAllPatientsInTenant(String theTenantId) {
		myTenantClientInterceptor.setTenantId(theTenantId);

		return myClient.search().forResource("Patient").cacheControl(new CacheControlDirective().setNoCache(true)).returnBundle(Bundle.class).execute();
	}
}
