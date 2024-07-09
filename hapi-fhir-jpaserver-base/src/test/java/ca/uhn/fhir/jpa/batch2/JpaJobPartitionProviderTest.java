package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaJobPartitionProviderTest {
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private IPartitionLookupSvc myPartitionLookupSvc;
	@InjectMocks
	private JpaJobPartitionProvider myJobPartitionProvider;

	@Test
	public void getPartitions_requestSpecificPartition_returnsPartition() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		String operation = ProviderConstants.OPERATION_EXPORT;

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(ArgumentMatchers.eq(requestDetails), ArgumentMatchers.eq(operation))).thenReturn(partitionId);

		// test
		List <RequestPartitionId> partitionIds = myJobPartitionProvider.getPartitions(requestDetails, operation);

		// verify
		Assertions.assertThat(partitionIds).hasSize(1);
		Assertions.assertThat(partitionIds).containsExactlyInAnyOrder(partitionId);
	}

	@Test
	public void getPartitions_requestAllPartitions_returnsListOfAllSpecificPartitions() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		String operation = ProviderConstants.OPERATION_EXPORT;

		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(ArgumentMatchers.eq(requestDetails), ArgumentMatchers.eq(operation)))
				.thenReturn( RequestPartitionId.allPartitions());
		List<RequestPartitionId> partitionIds = List.of(RequestPartitionId.fromPartitionIds(1), RequestPartitionId.fromPartitionIds(2));

		List<PartitionEntity> partitionEntities = new ArrayList<>();
		partitionIds.forEach(partitionId -> {
			PartitionEntity entity = mock(PartitionEntity.class);
			when(entity.toRequestPartitionId()).thenReturn(partitionId);
			partitionEntities.add(entity);
		});
		when(myPartitionLookupSvc.listPartitions()).thenReturn(partitionEntities);
		List<RequestPartitionId> expectedPartitionIds = new ArrayList<>(partitionIds);
		expectedPartitionIds.add(RequestPartitionId.defaultPartition());

		// test
		List<RequestPartitionId> actualPartitionIds = myJobPartitionProvider.getPartitions(requestDetails, operation);

		// verify
		Assertions.assertThat(actualPartitionIds).hasSize(expectedPartitionIds.size());
		Assertions.assertThat(actualPartitionIds).containsExactlyInAnyOrder(expectedPartitionIds.toArray(new RequestPartitionId[0]));
	}
}