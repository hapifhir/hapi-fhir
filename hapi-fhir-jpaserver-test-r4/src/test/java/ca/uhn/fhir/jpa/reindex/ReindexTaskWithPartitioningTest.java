package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils.JOB_REINDEX;
import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReindexTaskWithPartitioningTest extends BaseJpaR4Test {
	@Autowired
	private IJobCoordinator myJobCoordinator;

	@BeforeEach
	public void before() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("TestPartition1"), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("TestPartition2"), null);

		RequestPartitionId partition1 = RequestPartitionId.fromPartitionId(1);
		RequestPartitionId partition2 = RequestPartitionId.fromPartitionId(2);
		RequestPartitionId defaultPartition = RequestPartitionId.defaultPartition();

		Observation observation1 = buildResource("Observation", withStatus(Observation.ObservationStatus.FINAL.toCode()));
		myObservationDao.create(observation1, new SystemRequestDetails().setRequestPartitionId(partition1));
		Observation observation2 = buildResource("Observation", withStatus(Observation.ObservationStatus.REGISTERED.toCode()));
		myObservationDao.create(observation2, new SystemRequestDetails().setRequestPartitionId(partition1));
		Observation observation3 = buildResource("Observation", withStatus(Observation.ObservationStatus.FINAL.toCode()));
		myObservationDao.create(observation3, new SystemRequestDetails().setRequestPartitionId(partition2));

		Patient patient1 = buildResource("Patient", withActiveTrue());
		myPatientDao.create(patient1, new SystemRequestDetails().setRequestPartitionId(partition1));
		Patient patient2 = buildResource("Patient", withActiveFalse());
		myPatientDao.create(patient2, new SystemRequestDetails().setRequestPartitionId(partition2));
		Patient patient3 = buildResource("Patient", withActiveFalse());
		myPatientDao.create(patient3, new SystemRequestDetails().setRequestPartitionId(defaultPartition));
	}

	@AfterEach
	public void after() {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
	}

	public static Stream<Arguments> getReindexParameters() {
		RequestPartitionId partition1 = RequestPartitionId.fromPartitionId(1);
		RequestPartitionId partition2 = RequestPartitionId.fromPartitionId(2);
		RequestPartitionId allPartitions = RequestPartitionId.allPartitions();
		return Stream.of(
				// 1. includes all resources
				Arguments.of(List.of(), 6),
				// 2. includes all resources from partition 1
				Arguments.of(List.of(new PartitionedUrl().setRequestPartitionId(partition1)), 3),
				// 3. includes all resources in all partitions
				Arguments.of(List.of(new PartitionedUrl().setUrl("").setRequestPartitionId(allPartitions)), 6),
				// 4. includes all Observations in partition 1 and partition 2
				Arguments.of(
						List.of(
							new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(partition1),
							new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(partition2)
						), 3),
				// 5. includes all Observations in all partitions (partition 1, partition 2 and default partition)
				Arguments.of(
						List.of(
							new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(allPartitions)),
						3),
				// 6. includes all Observations in partition 1
				Arguments.of(
						List.of(
								new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(partition1)),
						2),
				// 7. includes all Patients from	 all partitions
				Arguments.of(
						List.of(
								new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(allPartitions)
						), 3),
				// 8. includes Patients and Observations in partitions 1 and 2
				Arguments.of(
						List.of(
								new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(partition1),
								new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(partition2)
						), 3),
				// 9. includes final Observations and Patients from partitions 1 and 2
				Arguments.of(
						List.of(
								new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(partition1),
								new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(partition2),
								new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(partition1),
								new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(partition2)
						), 5),
				// 10. includes final Observations from partition 1 and Patients from partition 2
				Arguments.of(
						List.of(
								new PartitionedUrl().setUrl("Observation?status=final").setRequestPartitionId(partition1),
								new PartitionedUrl().setUrl("Observation?status=final").setRequestPartitionId(partition2),
								new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(partition2)
						), 3),
				// 11. includes final Observations and Patients from partitions 1
				Arguments.of(
						List.of(
								new PartitionedUrl().setUrl("Observation?status=final").setRequestPartitionId(partition1),
								new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(partition1)
						), 2)
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getReindexParameters")
	public void testReindex_withPartitionedUrls_indexesMatchingResources(List<PartitionedUrl> thePartitionedUrls,
																								int theExpectedIndexedResourceCount) {
		PartitionedUrlJobParameters parameters = new PartitionedUrlJobParameters();
		thePartitionedUrls.forEach(parameters::addPartitionedUrl);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(res);

		// verify only resources matching URLs and partitions provided via parameters were re-indexed
		assertThat(jobInstance.getCombinedRecordsProcessed()).isEqualTo(theExpectedIndexedResourceCount);
	}
}
