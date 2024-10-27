/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.packages.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class PackageUtils {

	public static final String LOADER_WITH_CACHE = "loaderWithCache";

	/**
	 * Default install types
	 */
	public static List<String> DEFAULT_INSTALL_TYPES = Collections.unmodifiableList(Lists.newArrayList(
			"NamingSystem",
			"CodeSystem",
			"ValueSet",
			"StructureDefinition",
			"ConceptMap",
			"SearchParameter",
			"Subscription"));
}
