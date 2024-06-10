package ca.uhn.fhir.jpa.dao.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PartitionAwareSupplierTest {

	@Spy
	private MockHapiTransactionService myHapiTransactionService;

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> myBuilderArgumentCaptor;

	private static final String ourExpectedTenantId = "TenantA";

	@Test
	public void testMethodFindInPartitionedContext_withRequestDetailsHavingTenantId_willExecuteOnSpecifiedPartition(){
		RequestDetails requestDetails = getRequestDetails();

		PartitionAwareSupplier partitionAwareSupplier = new PartitionAwareSupplier(myHapiTransactionService, requestDetails);
		partitionAwareSupplier.supplyInPartitionedContext(getResourcePersistentIdSupplier());

		assertTransactionServiceWasInvokedWithTenantId(ourExpectedTenantId);

	}

	private Supplier<List<IResourcePersistentId>> getResourcePersistentIdSupplier(){
		return () -> Collections.emptyList();
	}

	private void assertTransactionServiceWasInvokedWithTenantId(String theExpectedTenantId) {
		verify(myHapiTransactionService, times(1)).doExecute(myBuilderArgumentCaptor.capture(), any());
		HapiTransactionService.ExecutionBuilder methodArgumentExecutionBuilder = myBuilderArgumentCaptor.getValue();

		String requestDetailsTenantId = methodArgumentExecutionBuilder.getRequestDetailsForTesting().getTenantId();

		assertEquals(theExpectedTenantId, requestDetailsTenantId);
	}

	private RequestDetails getRequestDetails() {
		RequestDetails requestDetails =	new ServletRequestDetails();
		requestDetails.setTenantId(ourExpectedTenantId);
		return requestDetails;
	}

}
