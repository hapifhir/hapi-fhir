/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

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
		assertThatExceptionOfType(RepeatingChoiceHandledException.class).isThrownBy(() -> {
			parser.parseResource(resourceStr);
		});
	}
}
