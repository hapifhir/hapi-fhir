package ca.uhn.fhir.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
/**
 * Defines FHIR version independent tests for testing parser error handling. In version dependent
 * projects, the sub-types {@link AbstractXmlParserErrorHandlerTest}, {@link
 * AbstractJsonParserErrorHandlerTest} can be sub-classed to create a complete test.
 */
public abstract sealed class AbstractParserErrorHandlerTest
    permits AbstractXmlParserErrorHandlerTest, AbstractJsonParserErrorHandlerTest {

  protected abstract IParser createParser();

  protected abstract String createResourceWithRepeatingChoice();

	@Test
	public void testRepeatingChoiceHandled() {

		// Let error handler throw custom exception on unexpectedRepeatingElement
		@SuppressWarnings("serial")
		class RepeatingChoiceHandledException extends RuntimeException {}
		IParserErrorHandler errorHandler = new ErrorHandlerAdapter() {
			@Override
			public void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName) {
			  throw new RepeatingChoiceHandledException();
			}
		};

		IParser parser = createParser();
		parser.setParserErrorHandler(errorHandler);

		String resourceStr = createResourceWithRepeatingChoice();
		assertThrows(
			RepeatingChoiceHandledException.class,
			() -> {
				parser.parseResource(resourceStr);
		});
	}
}
