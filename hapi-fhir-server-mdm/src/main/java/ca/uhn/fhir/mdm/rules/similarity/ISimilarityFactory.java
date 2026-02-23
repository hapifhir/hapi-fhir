/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.rules.similarity;

import java.util.Set;

// Created by claude-opus-4-6
/**
 * Factory interface for retrieving and registering similarity algorithms by name.
 * Implementations pre-populate built-in algorithms from {@link MdmSimilarityEnum}
 * and allow registration of custom algorithms at runtime.
 */
public interface ISimilarityFactory {

	/**
	 * Retrieves the similarity algorithm registered under the given name.
	 *
	 * @param theName the algorithm name (e.g. "JARO_WINKLER", "COSINE", or a custom name)
	 * @return the similarity instance, or {@code null} if no algorithm is registered under that name
	 */
	IMdmFieldSimilarity getSimilarityForName(String theName);

	/**
	 * Registers a custom similarity algorithm under the given name.
	 *
	 * @param theName the algorithm name
	 * @param theSimilarity the similarity implementation
	 * @throws IllegalArgumentException if an algorithm is already registered under that name
	 */
	void register(String theName, IMdmFieldSimilarity theSimilarity);

	/**
	 * @return the set of all registered algorithm names
	 */
	Set<String> getRegisteredNames();
}
