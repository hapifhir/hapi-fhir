package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.export.BulkExportJobService.JOB_INSTANCE_ID;
import static ca.uhn.test.util.AssertJson.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkExportJobServiceTest {

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private IJobCoordinator myJobCoordinator;
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private JpaStorageSettings myStorageSettings;

	@Test
	void startJob_storesJobInstanceIdInUserData() {
		// setup
		BulkExportJobService svc = new BulkExportJobService(
			myInterceptorBroadcaster, myJobCoordinator,
			myDaoRegistry, myRequestPartitionHelperSvc, myStorageSettings);

		ServletRequestDetails requestDetails = spy(new ServletRequestDetails());
		requestDetails.setServletResponse(mock(HttpServletResponse.class));
		requestDetails.setServletRequest(mock(HttpServletRequest.class));

		RestfulServer server = mock(RestfulServer.class);
		doReturn(server).when(requestDetails).getServer();
		when(server.getServerBaseForRequest(any())).thenReturn("http://localhost");

		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setResourceTypes(List.of("Patient"));
		params.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);

		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId("test-instance-id-123");
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(response);

		// execute
		svc.startJob(requestDetails, params);

		// verify
		String storedId = (String) requestDetails.getUserData()
			.get(JOB_INSTANCE_ID);
		assertThat(storedId).isEqualTo("test-instance-id-123");
	}
}
