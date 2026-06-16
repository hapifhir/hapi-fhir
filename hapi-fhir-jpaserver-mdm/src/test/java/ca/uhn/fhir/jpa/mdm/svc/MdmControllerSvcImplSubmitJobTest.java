package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearJobParameters;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitJobParameters;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by claude-opus-4-6
@ExtendWith(MockitoExtension.class)
class MdmControllerSvcImplSubmitJobTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	private IJobCoordinator myJobCoordinator;
	@Mock
	private IJobPartitionProvider myJobPartitionProvider;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private ServletRequestDetails myRequestDetails;

	@InjectMocks
	private MdmControllerSvcImpl mySvc;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@Test
	void submitMdmSubmitJob_delegatesToJobPartitionProvider() {
		// setup
		List<String> urls = List.of("Patient?", "Practitioner?");
		PartitionedUrl pu1 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1));
		PartitionedUrl pu2 = new PartitionedUrl().setUrl("Practitioner?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(urls))).thenReturn(List.of(pu1, pu2));
		Batch2JobStartResponse jobResponse = new Batch2JobStartResponse();
		jobResponse.setInstanceId("test-job-id");
		when(myJobCoordinator.startInstance(any(ServletRequestDetails.class), any())).thenReturn(jobResponse);

		// execute
		mySvc.submitMdmSubmitJob(urls, null, myRequestDetails);

		// verify
		verify(myJobPartitionProvider).getPartitionedUrls(myRequestDetails, urls);
		verify(myJobCoordinator).startInstance(eq(myRequestDetails), myStartRequestCaptor.capture());

		MdmSubmitJobParameters params = myStartRequestCaptor.getValue().getParameters(MdmSubmitJobParameters.class);
		assertThat(params.getPartitionedUrls()).containsExactly(pu1, pu2);
	}

	@Test
	void submitMdmClearJob_delegatesToJobPartitionProvider() {
		// setup
		List<String> resourceNames = List.of("Patient", "Practitioner");
		List<String> expectedUrls = List.of("Patient?", "Practitioner?");
		PartitionedUrl pu1 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1));
		PartitionedUrl pu2 = new PartitionedUrl().setUrl("Practitioner?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(expectedUrls))).thenReturn(List.of(pu1, pu2));
		Batch2JobStartResponse jobResponse = new Batch2JobStartResponse();
		jobResponse.setInstanceId("test-job-id");
		when(myJobCoordinator.startInstance(any(ServletRequestDetails.class), any())).thenReturn(jobResponse);

		// execute
		mySvc.submitMdmClearJob(resourceNames, null, myRequestDetails);

		// verify
		verify(myJobPartitionProvider).getPartitionedUrls(myRequestDetails, expectedUrls);
		verify(myJobCoordinator).startInstance(eq(myRequestDetails), myStartRequestCaptor.capture());

		JobInstanceStartRequest startRequest = myStartRequestCaptor.getValue();
		assertThat(startRequest.getJobDefinitionId()).isEqualTo(MdmClearAppCtx.JOB_MDM_CLEAR);
		MdmClearJobParameters params = startRequest.getParameters(MdmClearJobParameters.class);
		assertThat(params.getPartitionedUrls()).containsExactly(pu1, pu2);
		assertThat(params.getResourceNames()).containsExactly("Patient", "Practitioner");
	}

	@Test
	void submitMdmClearJob_allPartitionsExpansion_passedThrough() {
		// setup — simulate provider expanding allPartitions into per-partition URLs
		List<String> resourceNames = List.of("Patient");
		List<String> expectedUrls = List.of("Patient?");
		PartitionedUrl pu1 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1));
		PartitionedUrl pu2 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2));
		PartitionedUrl pu3 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(3));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(expectedUrls))).thenReturn(List.of(pu1, pu2, pu3));
		Batch2JobStartResponse jobResponse = new Batch2JobStartResponse();
		jobResponse.setInstanceId("test-job-id");
		when(myJobCoordinator.startInstance(any(ServletRequestDetails.class), any())).thenReturn(jobResponse);

		// execute
		mySvc.submitMdmClearJob(resourceNames, null, myRequestDetails);

		// verify
		verify(myJobCoordinator).startInstance(eq(myRequestDetails), myStartRequestCaptor.capture());
		MdmClearJobParameters params = myStartRequestCaptor.getValue().getParameters(MdmClearJobParameters.class);
		assertThat(params.getPartitionedUrls()).containsExactly(pu1, pu2, pu3);
	}

	@Test
	void submitMdmSubmitJob_allPartitionsExpansion_passedThrough() {
		// setup — simulate provider expanding allPartitions into two concrete partitions
		List<String> urls = List.of("Patient?");
		PartitionedUrl pu1 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1));
		PartitionedUrl pu2 = new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(2));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(urls))).thenReturn(List.of(pu1, pu2));
		Batch2JobStartResponse jobResponse = new Batch2JobStartResponse();
		jobResponse.setInstanceId("test-job-id");
		when(myJobCoordinator.startInstance(any(ServletRequestDetails.class), any())).thenReturn(jobResponse);

		// execute
		mySvc.submitMdmSubmitJob(urls, null, myRequestDetails);

		// verify
		verify(myJobCoordinator).startInstance(eq(myRequestDetails), myStartRequestCaptor.capture());
		MdmSubmitJobParameters params = myStartRequestCaptor.getValue().getParameters(MdmSubmitJobParameters.class);
		assertThat(params.getPartitionedUrls()).containsExactly(pu1, pu2);
	}
}
