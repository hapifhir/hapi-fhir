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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.log.Logs;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// Created by claude-opus-4-6
/**
 * Default implementation of {@link ISimilarityFactory} that pre-populates built-in
 * similarity algorithms from {@link MdmSimilarityEnum} and supports registration
 * of custom algorithms at runtime.
 */
public class MdmSimilarityFactory implements ISimilarityFactory {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	private final Map<String, IMdmFieldSimilarity> mySimilarities = new LinkedHashMap<>();
	private final Set<String> myBuiltInNames;

	public MdmSimilarityFactory() {
		for (MdmSimilarityEnum enumConstant : MdmSimilarityEnum.values()) {
			mySimilarities.put(enumConstant.name(), enumConstant.getSimilarity());
		}
		myBuiltInNames = Set.copyOf(mySimilarities.keySet());
	}

	@Override
	public IMdmFieldSimilarity getSimilarityForName(String theName) {
		IMdmFieldSimilarity similarity = mySimilarities.get(theName);
		if (similarity == null) {
			ourLog.warn("Unrecognized similarity type {}. Returning null", theName);
		}
		return similarity;
	}

	@Override
	public void register(String theName, IMdmFieldSimilarity theSimilarity) {
		if (mySimilarities.containsKey(theName)) {
			throw new IllegalArgumentException(
					Msg.code(2851) + "A similarity is already registered under the name: " + theName);
		}
		mySimilarities.put(theName, theSimilarity);
	}

	@Override
	public void unregister(String theName) {
		if (myBuiltInNames.contains(theName)) {
			throw new IllegalArgumentException(Msg.code(2854) + "Cannot unregister built-in similarity: " + theName);
		}
		mySimilarities.remove(theName);
	}

	@Override
	public Set<String> getRegisteredNames() {
		return Set.copyOf(mySimilarities.keySet());
	}
}
