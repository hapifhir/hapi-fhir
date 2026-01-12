package ca.uhn.fhir.jpa.interceptor.auth.fetcher;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DaoRegistryAuthorizationResourceFetcherTest {

	@RegisterExtension
	private final LogbackTestExtension myLogCapture = new LogbackTestExtension(DaoRegistryAuthorizationResourceFetcher.class);

	@InjectMocks
	private DaoRegistryAuthorizationResourceFetcher myFetcher;

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
	void testFetch_withNullResourceId_returnsEmpty() {
		Optional<IBaseResource> result = myFetcher.fetch(null, myRequestDetails);
		assertThat(result).isEmpty();
	}

	@Test
	void testFetch_withResourceIdWithoutType_returnsEmptyAndLogsWarning() {
		IIdType resourceId = new IdType();
		resourceId.setValue("123");

		Optional<IBaseResource> result = myFetcher.fetch(resourceId, myRequestDetails);

		assertThat(result).isEmpty();
		verifyNoInteractions(myDaoRegistry);
		LogbackTestExtensionAssert.assertThat(myLogCapture)
			.hasWarnMessage("Cannot fetch resource for authorization: resource ID 123 does not have a resource type");
	}

	@Test
	void testFetch_withResourceExists_returnsResource() {
		IIdType resourceId = new IdType("Patient/123");
		Patient patient = new Patient();
		patient.setId(resourceId);

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(patient);

		Optional<IBaseResource> result = myFetcher.fetch(resourceId, myRequestDetails);

		assertThat(result).contains(patient);
	}

	@Test
	void testFetch_whenCalledTwiceForSameResource_usesCacheAndOnlyFetchesOnce() {
		IIdType resourceId = new IdType("Patient/123");
		Patient patient = new Patient();
		patient.setId(resourceId);

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(patient);

		// First fetch
		Optional<IBaseResource> result1 = myFetcher.fetch(resourceId, myRequestDetails);
		// Second fetch (should use cache)
		Optional<IBaseResource> result2 = myFetcher.fetch(resourceId, myRequestDetails);

		assertThat(result1).isPresent();
		assertThat(result2).isPresent();
		assertThat(result1).containsSame(result2.get());

		// Verify DAO was only called once
		verify(myPatientDao, times(1)).read(any(), any());
	}

	@Test
	void testFetch_withResourceNotFound_cachesNegativeResult() {
		IIdType resourceId = new IdType("Patient/123");

		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(null);

		// First fetch
		Optional<IBaseResource> result1 = myFetcher.fetch(resourceId, myRequestDetails);
		// Second fetch (should use cached negative result)
		Optional<IBaseResource> result2 = myFetcher.fetch(resourceId, myRequestDetails);

		assertThat(result1).isEmpty();
		assertThat(result2).isEmpty();

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

		Optional<IBaseResource> result1 = myFetcher.fetch(patientId1, myRequestDetails);
		Optional<IBaseResource> result2 = myFetcher.fetch(patientId2, myRequestDetails);

		assertThat(result1).contains(patient1);
		assertThat(result2).contains(patient2);

		verify(myPatientDao, times(2)).read(any(), any());
	}
}
