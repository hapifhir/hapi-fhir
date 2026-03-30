package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests that the R4 parser will:
 * - correct enum values that only differ by case (and emit an invalidValue to the error handler)
 * - leave non-correctable values as-is and emit the normal invalidValue message
 */
@ExtendWith(MockitoExtension.class)
public class CaseInsensitiveEnumParsingR4Test {

    private static final FhirContext ourCtx = FhirContext.forR4();

	 @Mock
	 private IParserErrorHandler myMockErrorHandler;

	 @Captor
	 ArgumentCaptor<String> msgCaptor;

	 @Test
    public void testEnumValueCorrectedByCase() {
        // Input with wrong casing which should be corrected (Male -> male)
        String res = "{ \"resourceType\": \"Patient\", \"gender\": \"Male\" }";

        IParser parser = ourCtx.newJsonParser();
        parser.setParserErrorHandler(myMockErrorHandler);

        Patient parsed = parser.parseResource(Patient.class, res);

        // The parser should correct the value and set the canonical form on the primitive
        assertNotNull(parsed.getGenderElement().getValue(), "Enum value should be set");
        assertEquals("male", parsed.getGenderElement().getValueAsString(), "Canonical code should be 'male'");

        // Ensure the error handler was notified of the invalid casing and the original value was passed
        // The parser may or may not notify the error handler about the corrected casing depending on
        // runtime enum factory behavior. We require the value to be corrected; if the handler was
        // invoked at least once, assert that one of the invalidValue messages mentions the correction.
			verify(myMockErrorHandler, atLeastOnce()).invalidValue(any(), eq("Male"), msgCaptor.capture());
			String capturedMsg = msgCaptor.getAllValues().get(0);
			assertEquals("Case discrepancy detected in code value 'Male' (should be 'male')", capturedMsg);
    }

    @Test
    public void testEnumValueNotCorrectable_single() {
        // Input that does not match any enum code even after case normalization
        String res = "{ \"resourceType\": \"Patient\", \"gender\": \"FooBar\" }";

        IParser parser = ourCtx.newJsonParser();
        IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
        parser.setParserErrorHandler(errorHandler);

        Patient parsed = parser.parseResource(Patient.class, res);

        // No enum value should be set, but the raw string should be preserved
        assertNull(parsed.getGenderElement().getValue(), "Enum value should be null for unknown code");
        assertEquals("FooBar", parsed.getGenderElement().getValueAsString(), "Raw string should be preserved");

        // The error handler should be notified of the unknown code (verify it was invoked with original value)
        verify(errorHandler, times(1)).invalidValue(any(), eq("FooBar"), msgCaptor.capture());

        String capturedMsg = msgCaptor.getValue();
        assertEquals("Unknown AdministrativeGender code 'FooBar'", capturedMsg);
    }

    @ParameterizedTest
	 @ValueSource(strings = {"XyZ123", "UNKNOWN_CODE", "123_RANDOM"})
    public void testEnumValueNotCorrectable_multipleExamples(String example) {
        // Additional non-correctable examples to ensure correction attempts are attempted but not applied
			String res = "{ \"resourceType\": \"Patient\", \"gender\": \"" + example + "\" }";

			IParser parser = ourCtx.newJsonParser();
			IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
			parser.setParserErrorHandler(errorHandler);

			Patient parsed = parser.parseResource(Patient.class, res);

			assertNull(parsed.getGenderElement().getValue(), "Enum value should be null for unknown code: " + example);
			assertEquals(example, parsed.getGenderElement().getValueAsString(), "Raw string should be preserved: " + example);

			verify(errorHandler, times(1)).invalidValue(any(), eq(example), msgCaptor.capture());

			String capturedMsg = msgCaptor.getValue();
			assertEquals("Unknown AdministrativeGender code '" + example + "'", capturedMsg);
	  }
}
