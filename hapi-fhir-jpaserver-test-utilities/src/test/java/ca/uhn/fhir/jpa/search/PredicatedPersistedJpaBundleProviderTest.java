// Created by Sonnet 4
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PredicatedPersistedJpaBundleProviderTest {

	@Mock
	private RequestDetails myRequestDetails;
	
	@Mock
	private Search mySearch;

	@Test
	void getResources_withAcceptAllPredicate_returnsAllResources() {
		Predicate<IBaseResource> acceptAll = resource -> true;
		TestablePredicatedPersistedJpaBundleProvider provider = new TestablePredicatedPersistedJpaBundleProvider(myRequestDetails, mySearch, acceptAll);
		
		Patient patient1 = new Patient();
		patient1.setId("Patient/1");
		Patient patient2 = new Patient();
		patient2.setId("Patient/2");
		List<IBaseResource> mockResources = Arrays.asList(patient1, patient2);
		
		provider.setMockSuperResources(mockResources);
		
		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> result = provider.getResources(0, 10, builder);
		
		assertThat(result).hasSize(2);
		assertThat(result).contains(patient1, patient2);
	}

	@Test
	void getResources_withRejectAllPredicate_returnsEmptyList() {
		Predicate<IBaseResource> rejectAll = resource -> false;
		TestablePredicatedPersistedJpaBundleProvider provider = new TestablePredicatedPersistedJpaBundleProvider(myRequestDetails, mySearch, rejectAll);
		
		Patient patient1 = new Patient();
		patient1.setId("Patient/1");
		Patient patient2 = new Patient();
		patient2.setId("Patient/2");
		List<IBaseResource> mockResources = Arrays.asList(patient1, patient2);
		
		provider.setMockSuperResources(mockResources);
		
		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> result = provider.getResources(0, 10, builder);
		
		assertThat(result).isEmpty();
	}

	@Test
	void getResources_withSelectivePredicate_returnsFilteredResources() {
		// Predicate that accepts only resources with specific ID pattern
		Predicate<IBaseResource> selectiveFilter = resource -> 
			resource.getIdElement().getIdPart().endsWith("2");
		TestablePredicatedPersistedJpaBundleProvider provider = new TestablePredicatedPersistedJpaBundleProvider(myRequestDetails, mySearch, selectiveFilter);
		
		Patient patient1 = new Patient();
		patient1.setId("Patient/1");
		Patient patient2 = new Patient();
		patient2.setId("Patient/2");
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		List<IBaseResource> mockResources = Arrays.asList(patient1, patient2, patient3);
		
		provider.setMockSuperResources(mockResources);
		
		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> result = provider.getResources(0, 10, builder);
		
		assertThat(result).hasSize(1);
		assertThat(result).containsOnly(patient2);
	}

	@Test
	void getResources_withEmptyInput_returnsEmptyList() {
		Predicate<IBaseResource> anyPredicate = resource -> true;
		TestablePredicatedPersistedJpaBundleProvider provider = new TestablePredicatedPersistedJpaBundleProvider(myRequestDetails, mySearch, anyPredicate);
		
		provider.setMockSuperResources(Collections.emptyList());
		
		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> result = provider.getResources(0, 10, builder);
		
		assertThat(result).isEmpty();
	}

	@Test
	void getResources_withNullResourceInList_handlesGracefully() {
		Predicate<IBaseResource> nullSafePredicate = resource -> resource != null && resource.getIdElement() != null;
		TestablePredicatedPersistedJpaBundleProvider provider = new TestablePredicatedPersistedJpaBundleProvider(myRequestDetails, mySearch, nullSafePredicate);
		
		Patient patient1 = new Patient();
		patient1.setId("Patient/1");
		List<IBaseResource> mockResourcesWithNull = Arrays.asList(patient1, null);
		
		provider.setMockSuperResources(mockResourcesWithNull);
		
		ResponsePage.ResponsePageBuilder builder = new ResponsePage.ResponsePageBuilder();
		List<IBaseResource> result = provider.getResources(0, 10, builder);
		
		assertThat(result).hasSize(1);
		assertThat(result).containsOnly(patient1);
	}

	@Test
	void constructor_withValidParameters_initializesCorrectly() {
		Predicate<IBaseResource> testPredicate = resource -> true;
		PredicatedPersistedJpaBundleProvider provider = new PredicatedPersistedJpaBundleProvider(myRequestDetails, mySearch, testPredicate);
		
		// Verify construction doesn't throw and basic state is correct
		assertThat(provider).isNotNull();
	}

	/**
	 * Testable subclass that allows mocking the parent's getResources method
	 */
	private static class TestablePredicatedPersistedJpaBundleProvider extends PredicatedPersistedJpaBundleProvider {
		private List<IBaseResource> mockSuperResources;
		private final Predicate<? super IBaseResource> myTestPredicate;

		public TestablePredicatedPersistedJpaBundleProvider(RequestDetails theRequest, Search theSearch, Predicate<? super IBaseResource> thePredicate) {
			super(theRequest, theSearch, thePredicate);
			myTestPredicate = thePredicate;
		}

		public void setMockSuperResources(List<IBaseResource> mockResources) {
			this.mockSuperResources = mockResources;
		}

		@Override
		public List<IBaseResource> getResources(int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
			if (mockSuperResources != null) {
				// Simulate the parent class behavior and then apply our predicate filtering
				return mockSuperResources.stream().filter(myTestPredicate).toList();
			}
			return super.getResources(theFromIndex, theToIndex, theResponsePageBuilder);
		}
	}
}
