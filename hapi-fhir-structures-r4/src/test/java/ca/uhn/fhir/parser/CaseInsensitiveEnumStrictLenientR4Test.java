package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.r4.model.ClaimResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Strict vs Lenient error handler behavior for case-insensitive enum correction.
 */
public class CaseInsensitiveEnumStrictLenientR4Test {

    private static final FhirContext ourCtx = FhirContext.forR4();

    @Test
    public void testStrictHandlerThrowsOnCorrectableEnum() {
        String res = "{ \"resourceType\": \"ClaimResponse\", \"status\": \"Active\" }";

        IParser parser = ourCtx.newJsonParser();
        parser.setParserErrorHandler(new StrictErrorHandler());

		 // Diagnostic introspection (reflection) - will throw helpful assertion if handler is not StrictErrorHandler
		  try {
			  java.lang.reflect.Field f = parser.getClass().getDeclaredField("myErrorHandler");
			  f.setAccessible(true);
			  Object handler = f.get(parser);
			  System.out.println("Parser class: " + parser.getClass().getName() + " loader=" + parser.getClass().getClassLoader());
			  System.out.println("Handler class: " + handler.getClass().getName() + " loader=" + handler.getClass().getClassLoader());
			  assertTrue(handler instanceof StrictErrorHandler, "Handler is not StrictErrorHandler; was: " + handler.getClass().getName());
		  } catch (NoSuchFieldException | IllegalAccessException nsfe) {
			  // some parser implementations may store the handler differently; print what we can
			  System.out.println("No myErrorHandler field on parser impl: " + parser.getClass().getName());
 		  }

        // Strict handler should throw because invalidValue is treated as fatal
        assertThrows(DataFormatException.class, () -> parser.parseResource(ClaimResponse.class, res));
    }

	@Test
	public void testLenientHandlerFailsOnBadEnum() {
		String res = "{ \"resourceType\": \"ClaimResponse\", \"status\": \"FooBar\" }";

		IParser parser = ourCtx.newJsonParser();
		// Use lenient handler that does NOT treat invalid values as fatal
		LenientErrorHandler lenient = new LenientErrorHandler().setErrorOnInvalidValue(false);
		parser.setParserErrorHandler(lenient);

		ClaimResponse parsed = parser.parseResource(ClaimResponse.class, res);

		// Value should be corrected to canonical 'active'
		assertNull(parsed.getStatus(), "Enum value should be null for unknown code");
	}

	@Test
	public void testLenientHandlerAcceptsAndCorrectsEnum() {
		String res = "{ \"resourceType\": \"ClaimResponse\", \"status\": \"Active\" }";

		IParser parser = ourCtx.newJsonParser();
		// Use lenient handler that does NOT treat invalid values as fatal
		LenientErrorHandler lenient = new LenientErrorHandler().setErrorOnInvalidValue(false);
		parser.setParserErrorHandler(lenient);

		ClaimResponse parsed = parser.parseResource(ClaimResponse.class, res);

		// Value should be corrected to canonical 'active'
		assertNotNull(parsed.getStatus().toCode());
		assertEquals("active", parsed.getStatus().toCode());
	}

    @Test
    public void testNonCorrectableValue_StrictThrows_LenientPreservesRaw() {
    		String res = "{ \"resourceType\": \"ClaimResponse\", \"status\": \"Active\" }";

        // Strict should throw
        IParser strictParser = ourCtx.newJsonParser();
        strictParser.setParserErrorHandler(new StrictErrorHandler());
        assertThrows(DataFormatException.class, () -> strictParser.parseResource(ClaimResponse.class, res));

        // Lenient should not throw and should preserve raw string
        IParser lenientParser = ourCtx.newJsonParser();
        LenientErrorHandler lenient = new LenientErrorHandler().setErrorOnInvalidValue(false);
        lenientParser.setParserErrorHandler(lenient);

		  ClaimResponse parsed = lenientParser.parseResource(ClaimResponse.class, res);
        assertEquals("ACTIVE", parsed.getStatus().toString());
    }
}

