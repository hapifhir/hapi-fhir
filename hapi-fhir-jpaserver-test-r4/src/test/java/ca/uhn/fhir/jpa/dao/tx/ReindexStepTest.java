package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexStep;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReindexStepTest {

	@Mock
	private HapiTransactionService myHapiTransactionService;
	@Mock
	private IJobDataSink<VoidModel> myDataSink;

	@InjectMocks
	private ReindexStep myReindexStep;

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> builderArgumentCaptor;

	@Test
	public void testMethodReindex_withRequestPartitionId_willExecuteWithPartitionId(){
		// given
		Integer expectedPartitionId = 1;
		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		ReindexJobParameters reindexJobParameters = new ReindexJobParameters();
		reindexJobParameters.setRequestPartitionId(RequestPartitionId.fromPartitionId(expectedPartitionId));
		when(myHapiTransactionService.withRequest(any())).thenCallRealMethod();

		// when
		myReindexStep.doReindex(data, myDataSink, "index-id", "chunk-id", reindexJobParameters);

		// then
		assertMethodArgumentRequestPartitionId(expectedPartitionId);

	}

	private void assertMethodArgumentRequestPartitionId(Integer theExpectedPartitionId) {
		verify(myHapiTransactionService, times(1)).doExecute(builderArgumentCaptor.capture(), any());
		HapiTransactionService.ExecutionBuilder methodArgumentExceptionBuilder = builderArgumentCaptor.getValue();
		RequestPartitionId methodArgumentRequestPartitionId = methodArgumentExceptionBuilder.getRequestPartitionIdForTesting();

		assertThat(methodArgumentRequestPartitionId, notNullValue());
		assertThat(methodArgumentRequestPartitionId.getFirstPartitionIdOrNull(), equalTo(theExpectedPartitionId));
	}
}
