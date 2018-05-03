package ca.uhn.fhir.rest.server.provider.r4;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.hl7.fhir.r4.model.ConceptMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This is a subclass to implement FHIR operations specific to R4 ConceptMap
 * resources. Its superclass is a simple implementation of the resource provider
 * interface that uses a HashMap to store all resources in memory.
 * <p>
 * This subclass currently supports the following FHIR operations:
 * </p>
 * <ul>
 * <li>Search for R4 ConceptMap resources by ConceptMap.url</li>
 * </ul>
 */
public class HashMapResourceProviderConceptMapR4 extends HashMapResourceProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(HashMapResourceProviderConceptMapR4.class);

	@SuppressWarnings("unchecked")
	public HashMapResourceProviderConceptMapR4(FhirContext theFhirContext) {
		super(theFhirContext, ConceptMap.class);

		FhirVersionEnum fhirVersion = theFhirContext.getVersion().getVersion();
		if (fhirVersion != FhirVersionEnum.R4) {
			throw new IllegalStateException("Requires FHIR version R4. Unsupported FHIR version provided: " + fhirVersion);
		}


	}

	@Search
	public List<ConceptMap> search(@RequiredParam(name=ConceptMap.SP_URL) String theConceptMapUrl) {
		List<ConceptMap> retVal = new ArrayList<>();

		// FIXME: TreeMap<Long, T> next is what it looks like in superclass.
		for (TreeMap<Long, ConceptMap> next : myIdToVersionToResourceMap.values()) {
			if (!next.isEmpty()) {
				ConceptMap conceptMap = (ConceptMap) next.lastEntry().getValue();
				if (theConceptMapUrl.equals(conceptMap.getUrl()))
				retVal.add(conceptMap);
				break;
			}
		}

		return retVal;
	}
}
