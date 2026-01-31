/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.test.junit;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class StringToIntegerListArgumentConverter
	extends TypedArgumentConverter<String, List<Integer>> {

	@SuppressWarnings("unchecked")
	public StringToIntegerListArgumentConverter() {
		super(String.class, (Class) List.class);
	}

	@Override
	protected List<Integer> convert(String source) throws ArgumentConversionException {
		List<Integer> retVal = new ArrayList<>();

		StringBuilder buffer = new StringBuilder();
		char[] charArray = source.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			boolean digit = Character.isDigit(c);
			boolean lastChar = i == charArray.length - 1;

			if (digit) {
				buffer.append(c);
			}

			if ((lastChar || !digit) && !buffer.isEmpty()) {
				retVal.add(Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
			}
		}

		return retVal;
	}

}
