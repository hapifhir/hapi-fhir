package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.delete.job.ReindexTestHelper;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.DEFAULT_PARTITION_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultitenantBatchOperationR4Test extends BaseMultitenantResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MultitenantBatchOperationR4Test.class);

	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setExpungeEnabled(true);
		myDaoConfig.setDeleteExpungeEnabled(true);
	}

	@AfterEach
	@Override
	public void after() throws Exception {
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
		myDaoConfig.setDeleteExpungeEnabled(new DaoConfig().isDeleteExpungeEnabled());
		super.after();
	}

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

		MyInterceptor interceptor = new MyInterceptor();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PARTITION_SELECTED, interceptor);
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
		assertThat(interceptor.requestPartitionIds, hasSize(1));
		RequestPartitionId partitionId = interceptor.requestPartitionIds.get(0);
		assertEquals(TENANT_B_ID, partitionId.getFirstPartitionIdOrNull());
		assertEquals(TENANT_B, partitionId.getFirstPartitionNameOrNull());
		assertThat(interceptor.requestDetails.get(0), isA(ServletRequestDetails.class));
		assertEquals("Patient", interceptor.resourceDefs.get(0).getName());
		myInterceptorRegistry.unregisterInterceptor(interceptor);

		DecimalType jobIdPrimitive = (DecimalType) response.getParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID);
		Long jobId = jobIdPrimitive.getValue().longValue();

		assertEquals(1, myBatchJobHelper.getReadCount(jobId));
		assertEquals(1, myBatchJobHelper.getWriteCount(jobId));

		// validate only the false patient in TENANT_B is removed
		assertEquals(2, getAllPatientsInTenant(TENANT_A).getTotal());
		assertEquals(1, getAllPatientsInTenant(TENANT_B).getTotal());
		assertEquals(0, getAllPatientsInTenant(DEFAULT_PARTITION_NAME).getTotal());

	}

	@Test
	public void testReindexEverything() {
		ReindexTestHelper reindexTestHelper = new ReindexTestHelper(myFhirCtx, myDaoRegistry, mySearchParamRegistry);
		myTenantClientInterceptor.setTenantId(TENANT_A);
		IIdType obsFinalA = doCreateResource(reindexTestHelper.buildObservationWithAlleleExtension());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		IIdType obsFinalB = doCreateResource(reindexTestHelper.buildObservationWithAlleleExtension());

		reindexTestHelper.createAlleleSearchParameter();

		// The searchparam value is on the observation, but it hasn't been indexed yet
		myTenantClientInterceptor.setTenantId(TENANT_A);
		assertThat(reindexTestHelper.getAlleleObservationIds(myClient), hasSize(0));
		myTenantClientInterceptor.setTenantId(TENANT_B);
		assertThat(reindexTestHelper.getAlleleObservationIds(myClient), hasSize(0));
		// setup
		Parameters input = new Parameters();
		Integer batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new DecimalType(batchSize));
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_EVERYTHING, new BooleanType(true));

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		myTenantClientInterceptor.setTenantId(TENANT_A);
		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withParameters(input)
			.execute();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		DecimalType jobId = (DecimalType) response.getParameter(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID);

		myBatchJobHelper.awaitJobExecution(jobId.getValueAsNumber().longValue());

		// validate
		List<String> alleleObservationIds = reindexTestHelper.getAlleleObservationIds(myClient);
		// Only the one in the first tenant should be indexed
		myTenantClientInterceptor.setTenantId(TENANT_A);
		assertThat(reindexTestHelper.getAlleleObservationIds(myClient), hasSize(1));
		assertEquals(obsFinalA.getIdPart(), alleleObservationIds.get(0));
		myTenantClientInterceptor.setTenantId(TENANT_B);
		assertThat(reindexTestHelper.getAlleleObservationIds(myClient), hasSize(0));
	}
	// FIXME KHS test reindex_urls

	private Bundle getAllPatientsInTenant(String theTenantId) {
		myTenantClientInterceptor.setTenantId(theTenantId);

		return myClient.search().forResource("Patient").cacheControl(new CacheControlDirective().setNoCache(true)).returnBundle(Bundle.class).execute();
	}

	private static class MyInterceptor implements IAnonymousInterceptor {
		public List<RequestPartitionId> requestPartitionIds = new ArrayList<>();
		public List<RequestDetails> requestDetails = new ArrayList<>();
		public List<RuntimeResourceDefinition> resourceDefs = new ArrayList<>();

		@Override
		public void invoke(IPointcut thePointcut, HookParams theArgs) {
			requestPartitionIds.add(theArgs.get(RequestPartitionId.class));
			requestDetails.add(theArgs.get(RequestDetails.class));
			resourceDefs.add(theArgs.get(RuntimeResourceDefinition.class));
		}
	}
}
