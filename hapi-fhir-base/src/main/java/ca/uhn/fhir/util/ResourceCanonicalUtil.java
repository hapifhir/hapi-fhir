/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import org.apache.commons.lang3.ClassUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Utility methods for version-agnostic canonical resource detection across DSTU3, R4, R4B, and R5.
 * This module has no FHIR structures dependency, so {@link #isCanonicalResource(IBaseResource)} is tested
 * against real resources per version instead:
 * <ul>
 *   <li>DSTU3: {@code ca.uhn.fhir.util.ResourceCanonicalUtilDstu3Test} in hapi-fhir-structures-dstu3</li>
 *   <li>R4: {@code ca.uhn.fhir.util.ResourceCanonicalUtilR4Test} in hapi-fhir-structures-r4</li>
 *   <li>R4B: {@code ca.uhn.fhir.util.ResourceCanonicalUtilR4BTest} in hapi-fhir-structures-r4b</li>
 *   <li>R5: {@code ca.uhn.fhir.util.ResourceCanonicalUtilR5Test} in hapi-fhir-structures-r5</li>
 * </ul>
 */
public final class ResourceCanonicalUtil {

	private ResourceCanonicalUtil() {}

	/**
	 * Returns true if the resource is a canonical/conformance resource, i.e. extends
	 * {@code MetadataResource} (DSTU3/R4/R4B) or {@code CanonicalResource} (R5).
	 */
	public static boolean isCanonicalResource(IBaseResource theResource) {
		return isCanonicalResourceClass(theResource.getClass());
	}

	static boolean isCanonicalResourceClass(Class<?> theClass) {
		return ClassUtils.getAllSuperclasses(theClass).stream()
				.map(Class::getSimpleName)
				.anyMatch(name -> "MetadataResource".equals(name) || "CanonicalResource".equals(name));
	}
}
