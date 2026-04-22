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
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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

	@Mock
	private IFhirResourceDao<Observation> myObservationDao;

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
			Patient patient = createPatient("Patient/123");

			when(myPatientDao.read(eq(patient.getIdElement()), any(SystemRequestDetails.class))).thenReturn(patient);

			IBaseResource result = myResourceResolver.resolveResourceById(myRequestDetails, patient.getIdElement());
			assertThat(result).isEqualTo(patient);
		}

		@Test
		void whenCalledTwiceForSameResource_usesCacheAndOnlyFetchesOnce() {
			Patient patient = createPatient("Patient/123");

			when(myPatientDao.read(eq(patient.getIdElement()), any(SystemRequestDetails.class))).thenReturn(patient);

			// First fetch
			IBaseResource result1 = myResourceResolver.resolveResourceById(myRequestDetails, patient.getIdElement());
			// Second fetch (should use cache)
			IBaseResource result2 = myResourceResolver.resolveResourceById(myRequestDetails, patient.getIdElement());

			assertThat(result1).isEqualTo(patient);
			assertThat(result2).isEqualTo(patient);

			// Verify DAO was only called once
			verify(myPatientDao, times(1)).read(any(), any());
		}

		@Test
		void withResourceNotFound_cachesNegativeResult() {
			IIdType resourceId = new IdType("Patient/123");

			when(myPatientDao.read(eq(resourceId), any(SystemRequestDetails.class))).thenReturn(null);

			// First fetch
			IBaseResource result1 = myResourceResolver.resolveResourceById(myRequestDetails, resourceId);
			// Second fetch (should use cached negative result)
			IBaseResource result2 = myResourceResolver.resolveResourceById(myRequestDetails, resourceId);

			assertThat(result1).isNull();
			assertThat(result2).isNull();

			// Verify DAO was only called once
			verify(myPatientDao, times(1)).read(any(), any());
		}

		@Test
		void withDifferentResources_fetchesBothAndCachesSeparately() {
			Patient patient1 = createPatient("Patient/123");
			Patient patient2 = createPatient("Patient/456");

			when(myPatientDao.read(eq(patient1.getIdElement()), any(SystemRequestDetails.class))).thenReturn(patient1);
			when(myPatientDao.read(eq(patient2.getIdElement()), any(SystemRequestDetails.class))).thenReturn(patient2);

			IBaseResource result1 = myResourceResolver.resolveResourceById(myRequestDetails, patient1.getIdElement());
			IBaseResource result2 = myResourceResolver.resolveResourceById(myRequestDetails, patient2.getIdElement());

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
			lenient().when(myDaoRegistry.getResourceDao("Observation")).thenReturn(myObservationDao);
		}

		@Test
		void withResourceExists_returnsResource() {
			Patient patient = createPatient("Patient/123");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient));

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, idListFromResources(patient));

			assertThat(results).containsExactlyInAnyOrder(patient);
		}

		@Test
		void withResourcesExist_returnsResources() {
			Patient patient1 = createPatient("Patient/123");
			Patient patient2 = createPatient("Patient/456");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient1, patient2));

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, idListFromResources(patient1, patient2));

			assertThat(results).containsExactlyInAnyOrder(patient1, patient2);
		}

		@Test
		void withMultiplesTypesOfResourcesExist_returnsResources() {
			Patient patient1 = createPatient("Patient/p1");
			Patient patient2 = createPatient("Patient/p2");
			Observation observation1 = createObservation("Observation/o1");
			Observation observation2 = createObservation("Observation/o2");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient1, patient2));

			when(myObservationDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(observation1, observation2));

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, idListFromResources(patient1, patient2, observation1, observation2));

			assertThat(results).containsExactlyInAnyOrder(patient1, patient2, observation1, observation2);
		}

		@Test
		void withNoResourcesFound_returnsEmptyList() {
			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of());

			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, List.of(new IdType("Patient", "123'")));

			assertThat(results).isEmpty();
		}

		@Test
		void whenCalledTwiceForSameResources_usesCacheAndOnlySearchesOnce() {
			Patient patient1 = createPatient("Patient/123");
			Patient patient2 = createPatient("Patient/456");

			when(myPatientDao.searchForResources(any(), any(SystemRequestDetails.class)))
				.thenReturn(List.of(patient1, patient2));

			// First call
			List<IBaseResource> results1 = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, idListFromResources(patient1, patient2));
			// Second call (should use cache)
			List<IBaseResource> results2 = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, idListFromResources(patient1, patient2));

			assertThat(results1).containsExactlyInAnyOrder(patient1, patient2);
			assertThat(results2).containsExactlyInAnyOrder(patient1, patient2);

			// Verify search was only called once
			verify(myPatientDao, times(1)).searchForResources(any(), any());
		}

		@Test
		void withPartialCache_onlyFetchesMissingResources() {
			Patient patient1 = createPatient("Patient/123");
			Patient patient2 = createPatient("Patient/456");

			// First call fetches patient1, second call patient2
			when(myPatientDao.searchForResources(any(), any()))
				.thenReturn(List.of(patient1))
				.thenReturn(List.of(patient2));

			// First call - fetch patient1
			myResourceResolver.resolveResourcesByIds(myRequestDetails, idListFromResources(patient1));
			// Second call - patient1 cached, only fetch patient2
			List<IBaseResource> results = myResourceResolver.resolveResourcesByIds(
				myRequestDetails, idListFromResources(patient1, patient2));

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

	private Patient createPatient(String theId){
		Patient patient = new Patient();
		patient.setId(theId);
		return patient;
	}

	private Observation createObservation(String theId){
		Observation observation = new Observation();
		observation.setId(theId);
		return observation;
	}

	private List<IIdType> idListFromResources(IBaseResource... theResources){
		return Arrays.stream(theResources).map(IBaseResource::getIdElement).toList();
	}
}
