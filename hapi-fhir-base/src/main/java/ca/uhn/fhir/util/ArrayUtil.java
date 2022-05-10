package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public class ArrayUtil {

	/** Non instantiable */
	private ArrayUtil() {}

	/**
	 * Takes in a list like "foo, bar,, baz" and returns a set containing only ["foo", "bar", "baz"]
	 */
	public static Set<String> commaSeparatedListToCleanSet(String theValueAsString) {
		Set<String> resourceTypes;
		resourceTypes = Arrays.stream(split(theValueAsString, ","))
			.map(t->trim(t))
			.filter(t->isNotBlank(t))
			.collect(Collectors.toSet());
		return resourceTypes;
	}
}
