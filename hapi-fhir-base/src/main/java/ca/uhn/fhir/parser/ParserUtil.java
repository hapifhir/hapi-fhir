/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.parser.path.EncodeContextPath;
import jakarta.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserUtil {

	/** Non instantiable */
	private ParserUtil() {}

	public static @Nullable Set<String> determineApplicableResourceTypesForTerserPaths(
			@Nullable List<EncodeContextPath> encodeElements) {
		Set<String> encodeElementsAppliesToResourceTypes = null;
		if (encodeElements != null) {
			encodeElementsAppliesToResourceTypes = new HashSet<>();
			for (String next : encodeElements.stream()
					.map(t -> t.getPath().get(0).getName())
					.collect(Collectors.toList())) {
				if (next.startsWith("*")) {
					encodeElementsAppliesToResourceTypes = null;
					break;
				}
				int dotIdx = next.indexOf('.');
				if (dotIdx == -1) {
					encodeElementsAppliesToResourceTypes.add(next);
				} else {
					encodeElementsAppliesToResourceTypes.add(next.substring(0, dotIdx));
				}
			}
		}
		return encodeElementsAppliesToResourceTypes;
	}
}
