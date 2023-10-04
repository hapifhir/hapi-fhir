package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.jpa.dao.expunge.PartitionAwareFinder;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PartitionAwareFinderTest {

	@Spy
	private MockHapiTransactionService myHapiTransactionService;

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> builderArgumentCaptor;

	private final String expectedTenantId = "TenantA";

	@Test
	public void testMethodFindInPartitionedContext_withRequestDetailsHavingTenantId_willExecuteOnSpecifiedPartition(){
		RequestDetails requestDetails = getRequestDetails();

		PartitionAwareFinder partitionAwareFinder = new PartitionAwareFinder(myHapiTransactionService, requestDetails);
		partitionAwareFinder.supplyInPartitionedContext(getResourcePersistentIdSupplier());

		assertTransactionServiceWasInvokedWithTenantId(expectedTenantId);

	}

	private Supplier<List<IResourcePersistentId>> getResourcePersistentIdSupplier(){
		return () -> Collections.emptyList();
	}

	private void assertTransactionServiceWasInvokedWithTenantId(String theExpectedTenantId) {
		verify(myHapiTransactionService, times(1)).doExecute(builderArgumentCaptor.capture(), any());
		List<HapiTransactionService.ExecutionBuilder> methodArgumentExecutionBuilders = builderArgumentCaptor.getAllValues();

		methodArgumentExecutionBuilders.stream()
			.map(HapiTransactionService.ExecutionBuilder::getRequestDetailsForTesting)
			.map(RequestDetails::getTenantId)
			.allMatch(theExpectedTenantId::equals);
	}

	private RequestDetails getRequestDetails() {
		RequestDetails requestDetails =	new ServletRequestDetails();
		requestDetails.setTenantId(expectedTenantId);
		return requestDetails;
	}

}
