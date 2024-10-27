package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlJobParameters;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerateRangeChunksStepTest {

	private final GenerateRangeChunksStep<PartitionedUrlJobParameters> myStep = new GenerateRangeChunksStep<>();
	@Mock
	private StepExecutionDetails<PartitionedUrlJobParameters, VoidModel> myStepExecutionDetails;
	@Mock
	private IJobDataSink<ChunkRangeJson> myJobDataSink;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	public static Stream<Arguments> getReindexParameters() {
		RequestPartitionId partition1 = RequestPartitionId.fromPartitionId(1);
		RequestPartitionId partition2 = RequestPartitionId.fromPartitionId(2);

		return Stream.of(
				Arguments.of(List.of()),
				Arguments.of(List.of(new PartitionedUrl())),
				Arguments.of(List.of(new PartitionedUrl().setUrl("url").setRequestPartitionId(partition1)),
				Arguments.of(List.of(
						new PartitionedUrl().setUrl("url1").setRequestPartitionId(partition1),
						new PartitionedUrl().setUrl("url2").setRequestPartitionId(partition2)))
				)
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getReindexParameters")
	public void run_withParameters_producesExpectedChunks(List<PartitionedUrl> thePartitionedUrls) {
		PartitionedUrlJobParameters parameters = new PartitionedUrlJobParameters();
		thePartitionedUrls.forEach(parameters::addPartitionedUrl);

		when(myStepExecutionDetails.getParameters()).thenReturn(parameters);
		myStep.run(myStepExecutionDetails, myJobDataSink);

		ArgumentCaptor<ChunkRangeJson> captor = ArgumentCaptor.forClass(ChunkRangeJson.class);
		int expectedChunkCount = !thePartitionedUrls.isEmpty() ? thePartitionedUrls.size() : 1;
		verify(myJobDataSink, times(expectedChunkCount)).accept(captor.capture());

		if (thePartitionedUrls.isEmpty()) {
			ChunkRangeJson chunkRangeJson = captor.getValue();
			assertThat(chunkRangeJson.getUrl()).isNull();
			assertThat(chunkRangeJson.getPartitionId()).isNull();
		} else {
			List<ChunkRangeJson> chunks = captor.getAllValues();
			assertThat(chunks).hasSize(thePartitionedUrls.size());
			for (int i = 0; i < thePartitionedUrls.size(); i++) {
				PartitionedUrl partitionedUrl = thePartitionedUrls.get(i);
				ChunkRangeJson chunkRangeJson = captor.getAllValues().get(i);
				assertThat(chunkRangeJson.getUrl()).isEqualTo(partitionedUrl.getUrl());
				assertThat(chunkRangeJson.getPartitionId()).isEqualTo(partitionedUrl.getRequestPartitionId());
			}
		}
	}
}