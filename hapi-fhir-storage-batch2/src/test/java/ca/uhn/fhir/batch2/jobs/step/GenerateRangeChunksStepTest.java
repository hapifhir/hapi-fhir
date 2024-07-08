package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.util.Batch2Utils.BATCH_START_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerateRangeChunksStepTest {

	private final GenerateRangeChunksStep<JobParameters> myStep = new GenerateRangeChunksStep<>();
	@Mock
	private StepExecutionDetails<JobParameters, VoidModel> myStepExecutionDetails;
	@Mock
	private IJobDataSink<ChunkRangeJson> myJobDataSink;
	private static final Date START = BATCH_START_DATE;
	private static final Date END = new Date();

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	public static Stream<Arguments> getReindexParameters() {
		List<RequestPartitionId> threePartitions = List.of(
				RequestPartitionId.fromPartitionId(1),
				RequestPartitionId.fromPartitionId(2),
				RequestPartitionId.fromPartitionId(3)
		);
		List<RequestPartitionId> partition1 = List.of(RequestPartitionId.fromPartitionId(1));

		// the actual values (URLs, partitionId) don't matter, but we add values similar to real hapi-fhir use-cases
		return Stream.of(
				Arguments.of(List.of(), List.of(), false, 1),
				Arguments.of(List.of(), partition1, false, 1),
				Arguments.of(List.of("Observation?"), threePartitions, false, 3),
				Arguments.of(List.of("Observation?"), List.of(), false, 1),
				Arguments.of(List.of("Observation?"), partition1, true, 1),
				Arguments.of(List.of("Observation?", "Patient?"), threePartitions, false, 6),
				Arguments.of(List.of("Observation?", "Patient?", "Practitioner?"), threePartitions, true, 3),
				Arguments.of(List.of("Observation?status=final", "Patient?"), partition1, false, 2),
				Arguments.of(List.of("Observation?status=final"), threePartitions, false, 3)
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getReindexParameters")
	public void run_withParameters_producesExpectedChunks(List<String> theUrls, List<RequestPartitionId> thePartitions,
																			boolean theShouldAssignPartitionToUrl, int theExpectedChunkCount) {
		JobParameters parameters = JobParameters.from(theUrls, thePartitions, theShouldAssignPartitionToUrl);

		when(myStepExecutionDetails.getParameters()).thenReturn(parameters);
		myStep.run(myStepExecutionDetails, myJobDataSink);

		ArgumentCaptor<ChunkRangeJson> captor = ArgumentCaptor.forClass(ChunkRangeJson.class);
		verify(myJobDataSink, times(theExpectedChunkCount)).accept(captor.capture());

		List<ChunkRangeJson> chunkRangeJsonList = getExpectedChunkList(theUrls, thePartitions, theShouldAssignPartitionToUrl, theExpectedChunkCount);

		RequestPartitionId[] actualPartitionIds = captor.getAllValues().stream().map(ChunkRangeJson::getPartitionId).toList().toArray(new RequestPartitionId[0]);
		RequestPartitionId[] expectedPartitionIds = chunkRangeJsonList.stream().map(ChunkRangeJson::getPartitionId).toList().toArray(new RequestPartitionId[0]);
		assertThat(actualPartitionIds).containsExactlyInAnyOrder(expectedPartitionIds);

		String[] actualUrls = captor.getAllValues().stream().map(ChunkRangeJson::getUrl).toList().toArray(new String[0]);
		String[] expectedUrls = chunkRangeJsonList.stream().map(ChunkRangeJson::getUrl).toList().toArray(new String[0]);
		assertThat(actualUrls).containsExactlyInAnyOrder(expectedUrls);
	}

	private List<ChunkRangeJson> getExpectedChunkList(List<String> theUrls, List<RequestPartitionId> thePartitions, 
																	  boolean theShouldAssignPartitionToUrl, int theExpectedChunkCount) {
		List<ChunkRangeJson> chunkRangeJsonList = new ArrayList<>();
		if (theShouldAssignPartitionToUrl) {
			for (int i = 0; i < theExpectedChunkCount; i++) {
				String url = theUrls.get(i);
				RequestPartitionId partition = thePartitions.get(i);
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(START, END).setUrl(url).setPartitionId(partition);
				chunkRangeJsonList.add(chunkRangeJson);
			}
			return chunkRangeJsonList;
		}

		if (theUrls.isEmpty() && thePartitions.isEmpty()) {
			ChunkRangeJson chunkRangeJson = new ChunkRangeJson(START, END);
			chunkRangeJsonList.add(chunkRangeJson);
			return chunkRangeJsonList;
		}


		if (theUrls.isEmpty()) {
			for (RequestPartitionId partition : thePartitions) {
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(START, END).setPartitionId(partition);
				chunkRangeJsonList.add(chunkRangeJson);
			}
			return chunkRangeJsonList;
		}

		if (thePartitions.isEmpty()) {
			for (String url : theUrls) {
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(START, END).setUrl(url);
				chunkRangeJsonList.add(chunkRangeJson);
			}
			return chunkRangeJsonList;
		}

		theUrls.forEach(url -> {
			for (RequestPartitionId partition : thePartitions) {
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(START, END).setUrl(url).setPartitionId(partition);
				chunkRangeJsonList.add(chunkRangeJson);
			}
		});

		return chunkRangeJsonList;
	}
}