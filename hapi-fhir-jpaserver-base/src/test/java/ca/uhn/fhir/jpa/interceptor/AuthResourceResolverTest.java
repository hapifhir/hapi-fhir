package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthResourceResolverTest {

	@InjectMocks
	private AuthResourceResolver myResourceResolver;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Mock
	private IFhirResourceDao<Patient> myPatientDao;

	private RequestDetails myRequestDetails;

	@BeforeEach
	void beforeEach() {
		myRequestDetails = new SystemRequestDetails();
		lenient().when(myRequestPartitionHelperSvc.determineReadPartitionForRequest(any(), any()))
			.thenReturn(RequestPartitionId.allPartitions());
	}

	@Test
	void testFetch_withResourceExists_returnsResource() {
		IIdType resourceId = new IdType("Patient/123");
		Patient patient = new Patient();
		patient.setId(resourceId);

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(patient);

		IBaseResource result = myResourceResolver.resolveResourceById(resourceId, myRequestDetails);

		assertThat(result).isEqualTo(patient);
	}

	@Test
	void testFetch_whenCalledTwiceForSameResource_usesCacheAndOnlyFetchesOnce() {
		IIdType resourceId = new IdType("Patient/123");
		Patient patient = new Patient();
		patient.setId(resourceId);

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(patient);

		// First fetch
		IBaseResource result1 = myResourceResolver.resolveResourceById(resourceId, myRequestDetails);
		// Second fetch (should use cache)
		IBaseResource result2 = myResourceResolver.resolveResourceById(resourceId, myRequestDetails);

		assertThat(result1).isNotNull();
		assertThat(result2).isNotNull();
		assertThat(result1).isEqualTo(result2);

		// Verify DAO was only called once
		verify(myPatientDao, times(1)).read(any(), any());
	}

	@Test
	void testFetch_withResourceNotFound_cachesNegativeResult() {
		IIdType resourceId = new IdType("Patient/123");

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(null);

		// First fetch
		IBaseResource result1 = myResourceResolver.resolveResourceById(resourceId, myRequestDetails);
		// Second fetch (should use cached negative result)
		IBaseResource result2 = myResourceResolver.resolveResourceById(resourceId, myRequestDetails);

		assertThat(result1).isNull();
		assertThat(result2).isNull();

		// Verify DAO was only called once
		verify(myPatientDao, times(1)).read(any(), any());
	}

	@Test
	void testFetch_withDifferentResources_fetchesBothAndCachesSeparately() {
		Patient patient1 = new Patient();
		IIdType patientId1 = new IdType("Patient/123");
		patient1.setId(patientId1);

		Patient patient2 = new Patient();
		IIdType patientId2 = new IdType("Patient/456");
		patient2.setId(patientId2);

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(patientId1), any(SystemRequestDetails.class))).thenReturn(patient1);
		when(myPatientDao.read(eq(patientId2), any(SystemRequestDetails.class))).thenReturn(patient2);

		IBaseResource result1 = myResourceResolver.resolveResourceById(patientId1, myRequestDetails);
		IBaseResource result2 = myResourceResolver.resolveResourceById(patientId2, myRequestDetails);

		assertThat(result1).isEqualTo(patient1);
		assertThat(result2).isEqualTo(patient2);

		verify(myPatientDao, times(2)).read(any(), any());
	}
}
