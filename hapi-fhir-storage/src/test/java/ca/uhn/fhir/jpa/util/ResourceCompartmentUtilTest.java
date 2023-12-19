package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceCompartmentUtilTest {

	@Mock
	private RuntimeResourceDefinition myRuntimeResourceDefinition;
	@Mock
	private IBaseResource myResource;
	private List<RuntimeSearchParam> myCompartmentSearchParams;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ISearchParamExtractor mySearchParamExtractor;

	@BeforeEach
	void setUp() {
		myCompartmentSearchParams = getMockSearchParams();
	}

	@Test
	void getResourceCompartment() {
		when(mySearchParamExtractor.getPathValueExtractor(myResource, "Observation.subject"))
			.thenReturn(() -> List.of(new Reference("Patient/A")));

		Optional<String> oCompartment = ResourceCompartmentUtil.getResourceCompartment(
			myResource, myCompartmentSearchParams, mySearchParamExtractor);

		assertTrue(oCompartment.isPresent());
		assertEquals("A", oCompartment.get());
	}

	@Test
	void getPatientCompartmentSearchParams() {
		when(myRuntimeResourceDefinition.getSearchParams()).thenReturn(myCompartmentSearchParams);

		List<RuntimeSearchParam> result = ResourceCompartmentUtil.getPatientCompartmentSearchParams(myRuntimeResourceDefinition);

		assertEquals(2, result.size());
	}

	private List<RuntimeSearchParam> getMockSearchParams() {
		return List.of(
			getMockSearchParam("subject", "Observation.subject"),
			getMockSearchParam("performer", "Observation.performer"));
	}

	private RuntimeSearchParam getMockSearchParam(String theName, String thePath) {
		RuntimeSearchParam rsp = mock(RuntimeSearchParam.class);
		lenient().when(rsp.getParamType()).thenReturn(RestSearchParameterTypeEnum.REFERENCE);
		lenient().when(rsp.getProvidesMembershipInCompartments()).thenReturn(getCompartmentsForParam(theName));
		lenient().when(rsp.getName()).thenReturn(theName);
		lenient().when(rsp.getPath()).thenReturn(thePath);
		return rsp;
	}

	private Set<String> getCompartmentsForParam(String theName) {
        return switch (theName) {
            case "subject" -> Set.of("Device", "Patient");
            case "performer" -> Set.of("Practitioner", "Patient", "RelatedPerson");
            default -> Collections.emptySet();
        };
    }
}
