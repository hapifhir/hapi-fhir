package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.jobs.BaseR4ServerTest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.hapi.rest.server.helper.BatchHelperR4;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeleteExpungeProviderTest extends BaseR4ServerTest {
	public static final String TEST_JOB_ID = "test-job-id";
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProviderTest.class);

	private Parameters myReturnParameters;
	private MyDeleteExpungeJobSubmitter myDeleteExpungeJobSubmitter = new MyDeleteExpungeJobSubmitter();

	@BeforeEach
	public void reset() {
		myReturnParameters = new Parameters();
		myReturnParameters.addParameter("success", true);
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

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		DeleteExpungeProvider provider = new DeleteExpungeProvider(myCtx, myDeleteExpungeJobSubmitter);
		startServer(provider);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals(TEST_JOB_ID, BatchHelperR4.jobIdFromBatch2Parameters(response));
		assertThat(myDeleteExpungeJobSubmitter.calledWithUrls, hasSize(2));
		assertEquals(url1, myDeleteExpungeJobSubmitter.calledWithUrls.get(0));
		assertEquals(url2, myDeleteExpungeJobSubmitter.calledWithUrls.get(1));
		assertEquals(batchSize, myDeleteExpungeJobSubmitter.calledWithBatchSize);
		assertNotNull(myDeleteExpungeJobSubmitter.calledWithRequestDetails);
	}

	private class MyDeleteExpungeJobSubmitter implements IDeleteExpungeJobSubmitter {
		Integer calledWithBatchSize;
		List<String> calledWithUrls;
		RequestDetails calledWithRequestDetails;

		@Override
		public String submitJob(Integer theBatchSize, List<String> theUrlsToProcess, RequestDetails theRequest) {
			calledWithBatchSize = theBatchSize;
			calledWithUrls = theUrlsToProcess;
			calledWithRequestDetails = theRequest;
			return TEST_JOB_ID;
		}
	}
}
