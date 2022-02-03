package ca.uhn.fhir.parser;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class ErrorHandlerTest {

	@Test
	public void testAdapterMethods() {
		new ErrorHandlerAdapter().unexpectedRepeatingElement(null, null);
		new ErrorHandlerAdapter().unknownAttribute(null, null);
		new ErrorHandlerAdapter().unknownElement(null, null);
		new ErrorHandlerAdapter().containedResourceWithNoId(null);
		new ErrorHandlerAdapter().unknownReference(null, null);
		new ErrorHandlerAdapter().missingRequiredElement(null, null);
		new ErrorHandlerAdapter().incorrectJsonType(null, null, null, null, null, null);
		new ErrorHandlerAdapter().invalidValue(null, null, null);
	}

	@Test
	public void testLenientMethods() {
		new LenientErrorHandler().unexpectedRepeatingElement(null, null);
		new LenientErrorHandler().unknownAttribute(null, null);
		new LenientErrorHandler().unknownElement(null, null);
		new LenientErrorHandler().containedResourceWithNoId(null);
		new LenientErrorHandler().unknownReference(null, null);
		new LenientErrorHandler().incorrectJsonType(null, null, ValueType.ARRAY, null, ValueType.SCALAR, null);
		new LenientErrorHandler().setErrorOnInvalidValue(false).invalidValue(null, "FOO", "");
		new LenientErrorHandler().invalidValue(null, null, "");
		try {
			new LenientErrorHandler().invalidValue(null, "FOO", "");
			fail();
		} catch (DataFormatException e) {
			// good, this one method defaults to causing an error
		}
	}

	@Test
	public void testStrictMethods1() {
		try {
			new StrictErrorHandler().unexpectedRepeatingElement(null, null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1823) + "Multiple repetitions of non-repeatable element 'null' found during parse", e.getMessage());
		}
	}

	@Test
	public void testStrictMethods2() {
		try {
			new StrictErrorHandler().unknownAttribute(null, null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1824) + "Unknown attribute 'null' found during parse", e.getMessage());
		}
	}

	@Test
	public void testStrictMethods3() {
		try {
			new StrictErrorHandler().unknownElement(null, null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1825) + "Unknown element 'null' found during parse", e.getMessage());
		}
	}

	@Test
	public void testStrictMethods4() {
		try {
			new StrictErrorHandler().containedResourceWithNoId(null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1819) + "Resource has contained child resource with no ID", e.getMessage());
		}
	}

	@Test
	public void testStrictMethods5() {
		try {
			new StrictErrorHandler().unknownReference(null, null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1826) + "Resource has invalid reference: null", e.getMessage());
		}
	}

	@Test
	public void testStrictMethods6() {
		try {
			new StrictErrorHandler().incorrectJsonType(null, null, ValueType.ARRAY, null, ValueType.SCALAR, null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1820) + "Found incorrect type for element null - Expected ARRAY and found SCALAR", e.getMessage());
		}
	}

	@Test
	public void testStrictMethods7() {
		try {
			new StrictErrorHandler().invalidValue(null, null, null);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1821) + "Invalid attribute value \"null\": null", e.getMessage());
		}
	}

}
