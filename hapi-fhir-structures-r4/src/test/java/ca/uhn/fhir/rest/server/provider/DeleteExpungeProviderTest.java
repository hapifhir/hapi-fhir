package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.BaseR4ServerTest;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteExpungeProviderTest extends BaseR4ServerTest {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProviderTest.class);
	private final MyDeleteExpungeJobSubmitter mySvc = new MyDeleteExpungeJobSubmitter();
	private Parameters myReturnParameters;

	@BeforeEach
	public void reset() {
		myReturnParameters = new Parameters();
		myReturnParameters.addParameter("success", true);
		mySvc.reset();
	}

	@Test
	public void testDeleteExpunge() throws Exception {
		// setup
		Parameters input = new Parameters();
		String url1 = "Patient?active=false";
		String url2 = "Patient?active=false";
		Integer batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url1);
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url2);
		input.addParameter(ProviderConstants.OPERATION_DELETE_BATCH_SIZE, new DecimalType(batchSize));

		DeleteExpungeProvider provider = new DeleteExpungeProvider(myCtx, mySvc);
		startServer(provider);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		DecimalType jobId = (DecimalType) response.getParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID);
		assertEquals(123L, jobId.getValue().longValue());
		assertThat(mySvc.calledWithUrls, hasSize(2));
		assertEquals(url1, mySvc.calledWithUrls.get(0));
		assertEquals(url2, mySvc.calledWithUrls.get(1));
		assertEquals(batchSize, mySvc.calledWithBatchSize);
		assertEquals(null, mySvc.calledWithRequestDetails);
	}

	private class MyDeleteExpungeJobSubmitter implements IDeleteExpungeJobSubmitter {
		public Integer calledWithBatchSize;
		public RequestDetails calledWithRequestDetails;
		public List<String> calledWithUrls;

		@Override
		public JobExecution submitJob(Integer theBatchSize, RequestDetails theRequestDetails, List<String> theUrlsToExpungeDelete) {
			calledWithBatchSize = theBatchSize;
			calledWithRequestDetails = theRequestDetails;
			calledWithUrls = theUrlsToExpungeDelete;
			JobInstance instance = new JobInstance(123L, "jobName");
			return new JobExecution(instance, new JobParameters());
		}

		public void reset() {
			calledWithUrls = new ArrayList<>();
		}
	}
}
