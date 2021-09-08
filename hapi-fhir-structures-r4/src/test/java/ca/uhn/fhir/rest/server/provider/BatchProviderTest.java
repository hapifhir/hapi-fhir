package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.api.server.storage.IReindexJobSubmitter;
import ca.uhn.fhir.rest.server.BaseR4ServerTest;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.hapi.rest.server.helper.BatchHelperR4;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BatchProviderTest extends BaseR4ServerTest {
	public static final long TEST_JOB_ID = 123L;
	public static final String TEST_JOB_NAME = "jobName";
	private static final Logger ourLog = LoggerFactory.getLogger(BatchProviderTest.class);
	private final MyMultiUrlJobSubmitter myDeleteExpungeJobSubmitter = new MyMultiUrlJobSubmitter(ProviderConstants.OPERATION_DELETE_EXPUNGE);
	private final MyMultiUrlJobSubmitter myReindexJobSubmitter = new MyMultiUrlJobSubmitter(ProviderConstants.OPERATION_REINDEX);
	private Parameters myReturnParameters;

	@BeforeEach
	public void reset() {
		myReturnParameters = new Parameters();
		myReturnParameters.addParameter("success", true);
		myDeleteExpungeJobSubmitter.reset();
		myReindexJobSubmitter.reset();
	}

	@Test
	public void testDeleteExpunge() throws Exception {
		// setup
		Parameters input = new Parameters();
		String url1 = "Observation?status=active";
		String url2 = "Patient?active=false";
		Integer batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url1);
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url2);
		input.addParameter(ProviderConstants.OPERATION_DELETE_BATCH_SIZE, new DecimalType(batchSize));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		DeleteExpungeProvider provider = new DeleteExpungeProvider(myCtx, myDeleteExpungeJobSubmitter);
		startServer(provider);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals(TEST_JOB_ID, BatchHelperR4.jobIdFromParameters(response));
		assertThat(myDeleteExpungeJobSubmitter.calledWithUrls, hasSize(2));
		assertEquals(url1, myDeleteExpungeJobSubmitter.calledWithUrls.get(0));
		assertEquals(url2, myDeleteExpungeJobSubmitter.calledWithUrls.get(1));
		assertEquals(batchSize, myDeleteExpungeJobSubmitter.calledWithBatchSize);
		assertNotNull(myDeleteExpungeJobSubmitter.calledWithRequestDetails);
		assertFalse(myDeleteExpungeJobSubmitter.everything);
	}

	@Test
	public void testReindex() throws Exception {
		// setup
		Parameters input = new Parameters();
		String url1 = "Observation?status=active";
		String url2 = "Patient?active=false";
		Integer batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_URL, url1);
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_URL, url2);
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new DecimalType(batchSize));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		ReindexProvider provider = new ReindexProvider(myCtx, myReindexJobSubmitter);
		startServer(provider);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withParameters(input)
			.execute();

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		DecimalType jobId = (DecimalType) response.getParameter(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID);
		assertEquals(TEST_JOB_ID, jobId.getValue().longValue());
		assertThat(myReindexJobSubmitter.calledWithUrls, hasSize(2));
		assertEquals(url1, myReindexJobSubmitter.calledWithUrls.get(0));
		assertEquals(url2, myReindexJobSubmitter.calledWithUrls.get(1));
		assertEquals(batchSize, myReindexJobSubmitter.calledWithBatchSize);
		assertNotNull(myReindexJobSubmitter.calledWithRequestDetails);
		assertFalse(myReindexJobSubmitter.everything);

		// bad params
		input = new Parameters();
		batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new DecimalType(batchSize));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));
		try {
			response = myClient
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_REINDEX)
				.withParameters(input)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $reindex must specify either everything=true or provide at least one value for url", e.getMessage());
		}
	}

	@Test
	public void testReindexEverything() throws Exception {
		// setup
		Parameters input = new Parameters();
		Integer batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new DecimalType(batchSize));
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_EVERYTHING, new BooleanType(true));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		ReindexProvider provider = new ReindexProvider(myCtx, myReindexJobSubmitter);
		startServer(provider);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withParameters(input)
			.execute();

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		DecimalType jobId = (DecimalType) response.getParameter(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID);
		assertEquals(TEST_JOB_ID, jobId.getValue().longValue());
		assertThat(myReindexJobSubmitter.calledWithUrls, hasSize(0));
		assertEquals(batchSize, myReindexJobSubmitter.calledWithBatchSize);
		assertNotNull(myReindexJobSubmitter.calledWithRequestDetails);
		assertTrue(myReindexJobSubmitter.everything);
	}

	private class MyMultiUrlJobSubmitter implements IReindexJobSubmitter, IDeleteExpungeJobSubmitter {
		public final String operationName;
		public Integer calledWithBatchSize;
		public RequestDetails calledWithRequestDetails;
		public List<String> calledWithUrls;
		public boolean everything;

		public MyMultiUrlJobSubmitter(String theOperationName) {
			operationName = theOperationName;
		}

		@Override
		public JobExecution submitJob(Integer theBatchSize, List<String> theUrlsToProcess, RequestDetails theRequestDetails) {
			calledWithBatchSize = theBatchSize;
			calledWithRequestDetails = theRequestDetails;
			calledWithUrls = theUrlsToProcess;
			everything = false;
			return buildJobExecution();
		}

		@Nonnull
		private JobExecution buildJobExecution() {
			JobInstance instance = new JobInstance(TEST_JOB_ID, TEST_JOB_NAME);
			return new JobExecution(instance, new JobParameters());
		}

		public void reset() {
			calledWithUrls = new ArrayList<>();
		}

		@Override
		public JobExecution submitEverythingJob(Integer theBatchSize, RequestDetails theRequestDetails) throws JobParametersInvalidException {
			calledWithBatchSize = theBatchSize;
			calledWithRequestDetails = theRequestDetails;
			everything = true;
			return buildJobExecution();
		}
	}
}
