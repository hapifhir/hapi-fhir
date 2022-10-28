package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestPartitionHelperSvcTest {
	static final Integer PARTITION_ID = 2401;
	static final String PARTITION_NAME = "JIMMY";
	static final PartitionEntity ourPartitionEntity = new PartitionEntity().setName(PARTITION_NAME);

	@Mock
	PartitionSettings myPartitionSettings;
	@Mock
	IPartitionLookupSvc myPartitionLookupSvc;
	@Mock
	FhirContext myFhirContext;
	@Mock
	IInterceptorBroadcaster myInterceptorBroadcaster;

	@InjectMocks
	RequestPartitionHelperSvc mySvc = new RequestPartitionHelperSvc();

	@Test
	public void determineReadPartitionForSystemRequest() {
		// setup
		SystemRequestDetails srd = new SystemRequestDetails();
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(PARTITION_ID);
		srd.setRequestPartitionId(requestPartitionId);
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);
		when(myPartitionLookupSvc.getPartitionById(PARTITION_ID)).thenReturn(ourPartitionEntity);

		// execute
		RequestPartitionId result = mySvc.determineReadPartitionForRequestForRead(srd, "Patient", new IdType("Patient/123"));

		// verify
		assertEquals(PARTITION_ID, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME, result.getFirstPartitionNameOrNull());
	}

	@Test
	public void determineCreatePartitionForSystemRequest() {
		// setup
		SystemRequestDetails srd = new SystemRequestDetails();
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(PARTITION_ID);
		srd.setRequestPartitionId(requestPartitionId);
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);
		when(myPartitionLookupSvc.getPartitionById(PARTITION_ID)).thenReturn(ourPartitionEntity);
		Patient resource = new Patient();
		when(myFhirContext.getResourceType(resource)).thenReturn("Patient");

		// execute
		RequestPartitionId result = mySvc.determineCreatePartitionForRequest(srd, resource, "Patient");

		// verify
		assertEquals(PARTITION_ID, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME, result.getFirstPartitionNameOrNull());
	}

}
