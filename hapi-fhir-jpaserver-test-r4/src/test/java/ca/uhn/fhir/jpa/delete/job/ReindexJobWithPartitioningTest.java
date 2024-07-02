package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
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

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReindexJobWithPartitioningTest extends BaseJpaR4Test {
	@Autowired
	private IJobCoordinator myJobCoordinator;
	private final RequestTenantPartitionInterceptor myPartitionInterceptor = new RequestTenantPartitionInterceptor();

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);
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
		myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
	}

	public static Stream<Arguments> getReindexParameters() {
		List<RequestPartitionId> twoPartitions = List.of(RequestPartitionId.fromPartitionId(1), RequestPartitionId.fromPartitionId(2));
		List<RequestPartitionId> partition1 = List.of(RequestPartitionId.fromPartitionId(1));
		List<RequestPartitionId> allPartitions = List.of(RequestPartitionId.allPartitions());
		return Stream.of(
				// includes all resources from all partitions - partition 1, partition 2 and default partition
				Arguments.of(List.of(), List.of(), false, 6),
				// includes all Observations
				Arguments.of(List.of("Observation?"), twoPartitions, false, 3),
				// includes all Observations
				Arguments.of(List.of("Observation?"), allPartitions, false, 3),
				Arguments.of(List.of("Observation?"), List.of(), false, 0),
				// includes Observations in partition 1
				Arguments.of(List.of("Observation?"), partition1, true, 2),
				// includes all Patients from all partitions - partition 1, partition 2 and default partition
				Arguments.of(List.of("Patient?"), allPartitions, false, 3),
				// includes Patients and Observations in partitions 1 and 2
				Arguments.of(List.of("Observation?", "Patient?"), twoPartitions, false, 5),
				// includes Observations from partition 1 and Patients from partition 2
				Arguments.of(List.of("Observation?", "Patient?"), twoPartitions, true, 3),
				// includes final Observations and Patients from partitions 1 and 2
				Arguments.of(List.of("Observation?status=final", "Patient?"), twoPartitions, false, 4),
				// includes final Observations from partition 1 and Patients from partition 2
				Arguments.of(List.of("Observation?status=final", "Patient?"), twoPartitions, true, 2),
				// includes final Observations and Patients from partitions 1
				Arguments.of(List.of("Observation?status=final", "Patient?"), partition1, false, 2)
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getReindexParameters")
	public void testReindex_byMultipleUrlsAndPartitions_indexesMatchingResources(List<String> theUrls,
																										  List<RequestPartitionId> thePartitions,
																										  boolean theShouldAssignPartitionToUrl,
																										  int theExpectedIndexedResourceCount) {

		JobParameters parameters = JobParameters.from(theUrls, thePartitions, theShouldAssignPartitionToUrl);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(res);

		// verify only resources matching URLs and partitions provided via parameters were re-indexed
		assertThat(jobInstance.getCombinedRecordsProcessed()).isEqualTo(theExpectedIndexedResourceCount);
	}
}
