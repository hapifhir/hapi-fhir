package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceCompartmentUtilTest {

	@Mock
	private RuntimeResourceDefinition myRuntimeResourceDefinition;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IBaseResource myResource;
	@Mock
	private FhirContext myFhirContext;
	private List<RuntimeSearchParam> myCompartmentSearchParams;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ISearchParamExtractor mySearchParamExtractor;

	@Test
	void getResourceCompartment() {
		myCompartmentSearchParams = getMockSearchParams(true);
		when(mySearchParamExtractor.getPathValueExtractor(myResource, "Observation.subject"))
			.thenReturn(() -> List.of(new Reference("Patient/P01")));

		Optional<String> oCompartment = ResourceCompartmentUtil.getResourceCompartment(
			myResource, myCompartmentSearchParams, mySearchParamExtractor);

		assertThat(oCompartment).isPresent();
		assertThat(oCompartment).contains("P01");
	}

	@Test
	void getPatientCompartmentSearchParams() {
		myCompartmentSearchParams = getMockSearchParams(true);
		when(myRuntimeResourceDefinition.getSearchParams()).thenReturn(myCompartmentSearchParams);

		List<RuntimeSearchParam> result = ResourceCompartmentUtil.getPatientCompartmentSearchParams(myRuntimeResourceDefinition);

		assertThat(result).hasSize(2);
	}

	@Nested
	public class TestGetPatientCompartmentIdentity {
		@Test
		void whenNoPatientCompartmentsReturnsEmpty() {
			myCompartmentSearchParams = getMockSearchParams(false);
			when(myFhirContext.getResourceDefinition(myResource)).thenReturn(myRuntimeResourceDefinition);
			when(myRuntimeResourceDefinition.getSearchParams()).thenReturn(myCompartmentSearchParams);

			Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(myResource, myFhirContext, mySearchParamExtractor);

			assertTrue(result.isEmpty());
		}

		@Test
		void whenPatientResource_andNoId_throws() {
			myCompartmentSearchParams = getMockSearchParams(true);
			when(myFhirContext.getResourceDefinition(myResource)).thenReturn(myRuntimeResourceDefinition);
			when(myRuntimeResourceDefinition.getSearchParams()).thenReturn(myCompartmentSearchParams);
			when(myRuntimeResourceDefinition.getName()).thenReturn("Patient");
			when(myResource.getIdElement().getIdPart()).thenReturn(null);

			MethodNotAllowedException thrown = assertThrows(MethodNotAllowedException.class,
				() -> ResourceCompartmentUtil.getPatientCompartmentIdentity(myResource, myFhirContext, mySearchParamExtractor));

			assertEquals(Msg.code(2475) + "Patient resource IDs must be client-assigned in patient compartment mode", thrown.getMessage());
		}

		@Test
		void whenPatientResource_whichHasId_returnsId() {
			myCompartmentSearchParams = getMockSearchParams(true);
			when(myFhirContext.getResourceDefinition(myResource)).thenReturn(myRuntimeResourceDefinition);
			when(myRuntimeResourceDefinition.getSearchParams()).thenReturn(myCompartmentSearchParams);
			when(myRuntimeResourceDefinition.getName()).thenReturn("Patient");
			when(myResource.getIdElement().getIdPart()).thenReturn("Abc");

			Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(myResource, myFhirContext, mySearchParamExtractor);

			assertThat(result).isPresent();
			assertThat(result).contains("Abc");
		}

		@Test
		void whenNoPatientResource_returnsPatientCompartment() {
			// getResourceCompartment is tested independently, so here it is just (static) mocked

			myCompartmentSearchParams = getMockSearchParams(true);
			when(myFhirContext.getResourceDefinition(myResource)).thenReturn(myRuntimeResourceDefinition);
			when(myRuntimeResourceDefinition.getSearchParams()).thenReturn(myCompartmentSearchParams);
			when(myRuntimeResourceDefinition.getName()).thenReturn("Observation");
			when(mySearchParamExtractor.getPathValueExtractor(myResource, "Observation.subject"))
				.thenReturn(() -> List.of(new Reference("Patient/P01")));

//			try (MockedStatic<ResourceCompartmentUtil> mockedUtil = Mockito.mockStatic(ResourceCompartmentUtil.class)) {
//				mockedUtil.when(() -> ResourceCompartmentUtil.getResourceCompartment(
//					myResource, myCompartmentSearchParams, mySearchParamExtractor)).thenReturn(Optional.of("P01"));

				// execute
				Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(myResource, myFhirContext, mySearchParamExtractor);

			assertThat(result).isPresent();
			assertThat(result).contains("P01");
//			}
		}
	}

	private List<RuntimeSearchParam> getMockSearchParams(boolean providePatientCompartment) {
		return List.of(
			getMockSearchParam("subject", "Observation.subject", providePatientCompartment),
			getMockSearchParam("performer", "Observation.performer", providePatientCompartment));
	}

	private RuntimeSearchParam getMockSearchParam(String theName, String thePath, boolean providePatientCompartment) {
		RuntimeSearchParam rsp = mock(RuntimeSearchParam.class);
		lenient().when(rsp.getParamType()).thenReturn(RestSearchParameterTypeEnum.REFERENCE);
		lenient().when(rsp.getProvidesMembershipInCompartments()).thenReturn(getCompartmentsForParam(theName, providePatientCompartment));
		lenient().when(rsp.getName()).thenReturn(theName);
		lenient().when(rsp.getPath()).thenReturn(thePath);
		return rsp;
	}

	private Set<String> getCompartmentsForParam(String theName, boolean theProvidePatientCompartment) {
		if (theProvidePatientCompartment) {
			return switch (theName) {
				case "subject" -> Set.of("Device", "Patient");
				case "performer" -> Set.of("Practitioner", "Patient", "RelatedPerson");
				default -> Collections.emptySet();
			};
		} else {
			return switch (theName) {
				case "subject" -> Set.of("Device");
				case "performer" -> Set.of("Practitioner", "RelatedPerson");
				default -> Collections.emptySet();
			};
		}
    }
}
