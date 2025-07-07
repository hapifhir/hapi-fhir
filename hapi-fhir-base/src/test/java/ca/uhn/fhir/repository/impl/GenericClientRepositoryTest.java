package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;

import static ca.uhn.fhir.rest.api.Constants.HEADER_IF_NONE_MATCH;
import static org.mockito.ArgumentMatchers.any;

@MockitoSettings(
	strictness = Strictness.WARN
)
class GenericClientRepositoryTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	IGenericClient myGenericClient;

	@InjectMocks
	GenericClientRepository myGenericClientRepository;

	@Test
	void testCreate() {
	    // given
		IBaseResource mockResource = org.mockito.Mockito.mock(IBaseResource.class);
		MethodOutcome stubOutcome = new MethodOutcome();
		Mockito.when(myGenericClient.create().resource(mockResource).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		Mockito.when(myGenericClient.create().resource(mockResource).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

	    // when
		MethodOutcome outcome = myGenericClientRepository.create(mockResource, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

	    // then check the stubs call on our myGenericClient
		Mockito.verify(myGenericClient.create().resource(mockResource).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();


	}

}
