package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.rp.r4.CodeSystemResourceProvider;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.model.primitive.StringDt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.uhn.test.util.AssertJson.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceProviderCodeSystemTest {

	@Mock
	private FhirContext myFhirContext;

	@Mock
	private JpaValidationSupportChain myValidationSupportChain;

	@Mock
	private IValidationSupport.CodeValidationResult myCodeValidationResult;

	@InjectMocks
	private CodeSystemResourceProvider myCodeSystemResourceProvider;

	@Captor
	ArgumentCaptor<ValidationSupportContext> myValidationSupportContext;

	@Captor
	ArgumentCaptor<ConceptValidationOptions> myConceptValidationOptions;

	@Captor
	ArgumentCaptor<String> myUrlCaptor;

	@Captor
	ArgumentCaptor<String> myCodeCaptor;

	@Captor
	ArgumentCaptor<String> myDisplayCaptor;

	@Captor
	ArgumentCaptor<String> myValuestUrl;

	@Test
	public void testCodeValidationWhenInputIsCodeSystemUrlAndCode() {
		StringDt expectedUrl = new StringDt("https://example.com/foo/bar");
		StringDt expectedCode = new StringDt("1234");
		StringDt expectedDisplay = new StringDt("Something with lots of syllables");
		StringDt version = new StringDt("1.0.0");

		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		when(myCodeValidationResult.toParameters(any(FhirContext.class))).thenReturn(null);

		when(myValidationSupportChain.validateCode(myValidationSupportContext.capture(), myConceptValidationOptions.capture(),
			myUrlCaptor.capture(), myCodeCaptor.capture(), myDisplayCaptor.capture(), myValuestUrl.capture())).thenReturn(myCodeValidationResult);

		myCodeSystemResourceProvider.validateCode(null, null, expectedUrl, version,
			expectedCode, expectedDisplay, null, null, null);

		assertThat(expectedUrl.getValue()).isEqualTo(myUrlCaptor.getValue());
		assertThat(expectedCode.getValue()).isEqualTo(myCodeCaptor.getValue());
		assertThat(expectedDisplay.getValue()).isEqualTo(myDisplayCaptor.getValue());
	}
}
