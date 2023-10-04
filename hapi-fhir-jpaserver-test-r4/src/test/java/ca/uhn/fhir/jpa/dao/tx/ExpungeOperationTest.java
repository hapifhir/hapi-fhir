package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeOperation;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpungeOperationTest {

	@Captor
	private ArgumentCaptor<HapiTransactionService.ExecutionBuilder> builderArgumentCaptor;
	@Spy
	private MockHapiTransactionService myHapiTransactionService;
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	@Mock
	private IResourceExpungeService myIResourceExpungeService;
	private final String expectedTenantId = "TenantA";

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
		assertTransactionServiceWasInvokedWithTenantId(expectedTenantId);
	}

	private void assertTransactionServiceWasInvokedWithTenantId(String theExpectedTenantId) {
		// we have set the expungeOptions to setExpungeDeletedResources and SetExpungeOldVersions to true.
		// as a result, we will be making 5 trips to the db.  let's make sure that each trip was done with
		// the hapiTransaction service and that the tenantId was specified.
		verify(myHapiTransactionService, times(5)).doExecute(builderArgumentCaptor.capture(), any());
		List<HapiTransactionService.ExecutionBuilder> methodArgumentExecutionBuilders = builderArgumentCaptor.getAllValues();

		boolean allMatching = methodArgumentExecutionBuilders.stream()
			.map(HapiTransactionService.ExecutionBuilder::getRequestDetailsForTesting)
			.map(RequestDetails::getTenantId)
			.allMatch(theExpectedTenantId::equals);


		assertThat(allMatching, is(equalTo(true)));
	}

	private RequestDetails getRequestDetails() {
		RequestDetails requestDetails =	new ServletRequestDetails();
		requestDetails.setTenantId(expectedTenantId);
		return requestDetails;
	}

}
