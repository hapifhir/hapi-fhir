package ca.uhn.fhir.jpa.dao.tx;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PartitionAwareSupplierTest {

	@Spy
	private MockHapiTransactionService myHapiTransactionService;

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> builderArgumentCaptor;

	private final String expectedTenantId = "TenantA";

	@Test
	public void testMethodFindInPartitionedContext_withRequestDetailsHavingTenantId_willExecuteOnSpecifiedPartition(){
		RequestDetails requestDetails = getRequestDetails();

		PartitionAwareSupplier partitionAwareSupplier = new PartitionAwareSupplier(myHapiTransactionService, requestDetails);
		partitionAwareSupplier.supplyInPartitionedContext(getResourcePersistentIdSupplier());

		assertTransactionServiceWasInvokedWithTenantId(expectedTenantId);

	}

	private Supplier<List<IResourcePersistentId>> getResourcePersistentIdSupplier(){
		return () -> Collections.emptyList();
	}

	private void assertTransactionServiceWasInvokedWithTenantId(String theExpectedTenantId) {
		verify(myHapiTransactionService, times(1)).doExecute(builderArgumentCaptor.capture(), any());
		HapiTransactionService.ExecutionBuilder methodArgumentExecutionBuilder = builderArgumentCaptor.getValue();

		String requestDetailsTenantId = methodArgumentExecutionBuilder.getRequestDetailsForTesting().getTenantId();

		assertThat(requestDetailsTenantId, is(equalTo(theExpectedTenantId)));
	}

	private RequestDetails getRequestDetails() {
		RequestDetails requestDetails =	new ServletRequestDetails();
		requestDetails.setTenantId(expectedTenantId);
		return requestDetails;
	}

}
