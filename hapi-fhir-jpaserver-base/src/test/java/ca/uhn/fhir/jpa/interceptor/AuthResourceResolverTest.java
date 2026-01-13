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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
	}

	@Nested
	class ResolveResourceByIdTest {

		@BeforeEach
		void beforeEach() {
			lenient().when(myRequestPartitionHelperSvc.determineReadPartitionForRequest(any(), any()))
				.thenReturn(RequestPartitionId.allPartitions());
			lenient().when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		}

		@Test
		void withResourceExists_returnsResource() {
			IIdType resourceId = new IdType("Patient/123");
			Patient patient = new Patient();
			patient.setId(resourceId);

			when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(patient);

			IBaseResource result = myResourceResolver.resolveResourceById(resourceId, myRequestDetails);
			assertThat(result).isEqualTo(patient);
		}

		@Test
		void whenCalledTwiceForSameResource_usesCacheAndOnlyFetchesOnce() {
			IIdType resourceId = new IdType("Patient/123");
			Patient patient = new Patient();
			patient.setId(resourceId);

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
		void withResourceNotFound_cachesNegativeResult() {
			IIdType resourceId = new IdType("Patient/123");

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
		void withDifferentResources_fetchesBothAndCachesSeparately() {
			Patient patient1 = new Patient();
			IIdType patientId1 = new IdType("Patient/123");
			patient1.setId(patientId1);

			Patient patient2 = new Patient();
			IIdType patientId2 = new IdType("Patient/456");
			patient2.setId(patientId2);

			when(myPatientDao.read(eq(patientId1), any(SystemRequestDetails.class))).thenReturn(patient1);
			when(myPatientDao.read(eq(patientId2), any(SystemRequestDetails.class))).thenReturn(patient2);

			IBaseResource result1 = myResourceResolver.resolveResourceById(patientId1, myRequestDetails);
			IBaseResource result2 = myResourceResolver.resolveResourceById(patientId2, myRequestDetails);

			assertThat(result1).isEqualTo(patient1);
			assertThat(result2).isEqualTo(patient2);

			verify(myPatientDao, times(2)).read(any(), any());
		}
	}

	@Nested
	class ResolveResourcesByIdsTest {
		@BeforeEach
		void beforeEach() {
			lenient().when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), eq("Patient"), any()))
				.thenReturn(RequestPartitionId.allPartitions());
			lenient().when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		}

		@Test
		void withResourceExists_returnsResource() {
			Patient patient1 = new Patient();
			patient1.setId("Patient/123");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient1));

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				List.of("Patient/123"), "Patient", myRequestDetails);

			assertThat(results).containsExactlyInAnyOrder(patient1);
		}

		@Test
		void withResourcesExist_returnsResources() {
			Patient patient1 = new Patient();
			patient1.setId("Patient/123");
			Patient patient2 = new Patient();
			patient2.setId("Patient/456");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient1, patient2));

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				List.of("Patient/123", "Patient/456"), "Patient", myRequestDetails);

			assertThat(results).containsExactlyInAnyOrder(patient1, patient2);
		}

		@Test
		void withNoResourcesFound_returnsEmptyList() {
			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of());

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				List.of("Patient/123"), "Patient", myRequestDetails);

			assertThat(results).isEmpty();
		}

		@Test
		void whenCalledTwiceForSameResources_usesCacheAndOnlySearchesOnce() {
			Patient patient1 = new Patient();
			patient1.setId("Patient/123");
			Patient patient2 = new Patient();
			patient2.setId("Patient/456");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient1, patient2));

			// First call
			List<IBaseResource> results1 = myResourceResolver.resolveResourcesByIds(
				List.of("Patient/123", "Patient/456"), "Patient", myRequestDetails);
			// Second call (should use cache)
			List<IBaseResource> results2 = myResourceResolver.resolveResourcesByIds(
				List.of("Patient/123", "Patient/456"), "Patient", myRequestDetails);

			assertThat(results1).containsExactlyInAnyOrder(patient1, patient2);
			assertThat(results2).containsExactlyInAnyOrder(patient1, patient2);

			// Verify search was only called once
			verify(myPatientDao, times(1)).searchForResources(any(), any());
		}

		@Test
		void withPartialCache_onlyFetchesMissingResources() {
			Patient patient1 = new Patient();
			patient1.setId("Patient/123");
			Patient patient2 = new Patient();
			patient2.setId("Patient/456");

			// First call fetches patient1
			when(myPatientDao.searchForResources(any(), any()))
				.thenReturn(List.of(patient1))
				.thenReturn(List.of(patient2));

			// First call - fetch patient1
			myResourceResolver.resolveResourcesByIds(List.of("Patient/123"), "Patient", myRequestDetails);
			// Second call - patient1 cached, only fetch patient2
			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				List.of("Patient/123", "Patient/456"), "Patient", myRequestDetails);

			assertThat(results).containsExactlyInAnyOrder(patient1, patient2);

			// Verify first search was for Patient/123
			verify(myPatientDao).searchForResources(
				argThat(params -> params.toNormalizedQueryString().equals("?_id=Patient/123")),
				any());

			// Verify second search was for Patient/456 only (Patient/123 was cached)
			verify(myPatientDao).searchForResources(
				argThat(params -> params.toNormalizedQueryString().equals("?_id=Patient/456")),
				any());
		}
	}
}
