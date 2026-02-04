package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.expunge.PartitionAwareSupplier;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PartitionAwareSupplierTest {

	@Spy
	private MockHapiTransactionService myHapiTransactionService;

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> myBuilderArgumentCaptor;

	private static final String TENANT_A = "TenantA";
	private static final int PARTITION_ID = 10;

	@Test
	void testMethodFindInPartitionedContext_withRequestDetailsHavingTenantId_willExecuteOnSpecifiedPartition() {
		RequestDetails requestDetails = getRequestDetails();

		PartitionAwareSupplier partitionAwareSupplier =
			new PartitionAwareSupplier(myHapiTransactionService, requestDetails, null);
		partitionAwareSupplier.supplyInPartitionedContext(getResourcePersistentIdSupplier());

		assertTransactionServiceWasInvokedWithTenantId(TENANT_A);

	}

	@Test
	void supplyInPartitionedContext_withPartitionIdAndTenantId_partitionIdIsUsed() {
		// setup
		RequestDetails requestDetails = getRequestDetails();
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(PARTITION_ID);

		// execute
		PartitionAwareSupplier partitionAwareSupplier =
			new PartitionAwareSupplier(myHapiTransactionService, requestDetails, requestPartitionId);
		partitionAwareSupplier.supplyInPartitionedContext(getResourcePersistentIdSupplier());

		// verify requestPartitionId is used for transaction
		verify(myHapiTransactionService, times(1)).doExecute(myBuilderArgumentCaptor.capture(), any());
		RequestPartitionId actualPartitionId = myBuilderArgumentCaptor.getValue().getRequestPartitionIdForTesting();
		assertThat(actualPartitionId).isNotNull();
		assertThat(actualPartitionId.getFirstPartitionIdOrNull()).isEqualTo(PARTITION_ID);

		// verify Tenant ID should still present in request details
		String actualTenantId = myBuilderArgumentCaptor.getValue().getRequestDetailsForTesting().getTenantId();
		assertThat(actualTenantId).isEqualTo(TENANT_A);
	}

	private Supplier<List<IResourcePersistentId>> getResourcePersistentIdSupplier() {
		return () -> Collections.emptyList();
	}

	private void assertTransactionServiceWasInvokedWithTenantId(String theExpectedTenantId) {
		verify(myHapiTransactionService, times(1)).doExecute(myBuilderArgumentCaptor.capture(), any());
		HapiTransactionService.ExecutionBuilder methodArgumentExecutionBuilder = myBuilderArgumentCaptor.getValue();

		String requestDetailsTenantId = methodArgumentExecutionBuilder.getRequestDetailsForTesting().getTenantId();

		assertEquals(theExpectedTenantId, requestDetailsTenantId);
	}

	private RequestDetails getRequestDetails() {
		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setTenantId(TENANT_A);
		return requestDetails;
	}

}
