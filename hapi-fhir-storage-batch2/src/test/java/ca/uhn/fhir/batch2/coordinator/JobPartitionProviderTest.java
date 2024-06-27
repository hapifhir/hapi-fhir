package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobPartitionProviderTest {
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@InjectMocks
	private JobPartitionProvider myJobPartitionProvider;

	@Test
	public void getPartitions_requestSpecificPartition_returnsPartition() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		String operation = ProviderConstants.OPERATION_EXPORT;

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(eq(requestDetails), eq(operation))).thenReturn(partitionId);

		// test
		List <RequestPartitionId> partitionIds = myJobPartitionProvider.getPartitions(requestDetails, operation);

		// verify
		assertThat(partitionIds).hasSize(1);
		assertThat(partitionIds).containsExactlyInAnyOrder(partitionId);
	}
}