package ca.uhn.fhir.jpa.dao.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeOperation;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpungeOperationTest {

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> myBuilderArgumentCaptor;
	@Spy
	private MockHapiTransactionService myHapiTransactionService;
	private JpaStorageSettings myStorageSettings;
	@Mock
	private IResourceExpungeService myIResourceExpungeService;
	private static final String ourExpectedTenantId = "TenantA";

	@BeforeEach
	public void beforeEach(){
		myStorageSettings = new JpaStorageSettings();
	}

	@Test
	public void testExpunge_onSpecificTenant_willPerformExpungeOnSpecificTenant(){
		// given
		when(myIResourceExpungeService.findHistoricalVersionsOfDeletedResources(any(), any(), anyInt())).thenReturn(List.of(JpaPid.fromId(1l)));
		when(myIResourceExpungeService.findHistoricalVersionsOfNonDeletedResources(any(), any(), anyInt())).thenReturn(List.of(JpaPid.fromId(1l)));
		myStorageSettings.setExpungeBatchSize(5);

		RequestDetails requestDetails = getRequestDetails();
		ExpungeOptions expungeOptions = new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true);

		ExpungeOperation expungeOperation = new ExpungeOperation("Patient", null, expungeOptions, requestDetails);

		expungeOperation.setHapiTransactionServiceForTesting(myHapiTransactionService);
		expungeOperation.setStorageSettingsForTesting(myStorageSettings);
		expungeOperation.setExpungeDaoServiceForTesting(myIResourceExpungeService);

		expungeOperation.call();

		// then
		assertTransactionServiceWasInvokedWithTenantId(ourExpectedTenantId);
	}

	private void assertTransactionServiceWasInvokedWithTenantId(String theExpectedTenantId) {
		// we have set the expungeOptions to setExpungeDeletedResources and SetExpungeOldVersions to true.
		// as a result, we will be making 5 trips to the db.  let's make sure that each trip was done with
		// the hapiTransaction service and that the tenantId was specified.
		verify(myHapiTransactionService, times(5)).doExecute(myBuilderArgumentCaptor.capture(), any());
		List<HapiTransactionService.ExecutionBuilder> methodArgumentExecutionBuilders = myBuilderArgumentCaptor.getAllValues();

		boolean allMatching = methodArgumentExecutionBuilders.stream()
			.map(HapiTransactionService.ExecutionBuilder::getRequestDetailsForTesting)
			.map(RequestDetails::getTenantId)
			.allMatch(theExpectedTenantId::equals);

		assertEquals(true, allMatching);
	}

	private RequestDetails getRequestDetails() {
		RequestDetails requestDetails =	new ServletRequestDetails();
		requestDetails.setTenantId(ourExpectedTenantId);
		return requestDetails;
	}

}
