package ca.uhn.fhir.cli;

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
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import com.google.common.base.Charsets;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.r4.model.ConceptMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This is a subclass to implement FHIR operations specific to R4 ConceptMap
 * resources. Its superclass, {@link HashMapResourceProvider}, is a simple
 * implementation of the resource provider interface that uses a HashMap to
 * store all resources in memory.
 * <p>
 * This subclass currently supports the following FHIR operations:
 * </p>
 * <ul>
 * <li>Search for R4 ConceptMap resources by ConceptMap.url</li>
 * <li>Conditional update for R4 ConceptMap resources by ConceptMap.url</li>
 * </ul>
 */
public class HashMapResourceProviderConceptMapR4 extends HashMapResourceProvider<ConceptMap> {
	@SuppressWarnings("unchecked")
	public HashMapResourceProviderConceptMapR4(FhirContext theFhirContext) {
		super(theFhirContext, ConceptMap.class);

		FhirVersionEnum fhirVersion = theFhirContext.getVersion().getVersion();
		if (fhirVersion != FhirVersionEnum.R4) {
			throw new IllegalStateException("Requires FHIR version R4. Unsupported FHIR version provided: " + fhirVersion);
		}


	}

	@Search
	public List<ConceptMap> searchByUrl(
		@RequiredParam(name=ConceptMap.SP_URL) String theConceptMapUrl) {

		List<ConceptMap> retVal = new ArrayList<>();

		for (TreeMap<Long, ConceptMap> next : myIdToVersionToResourceMap.values()) {
			if (!next.isEmpty()) {
				ConceptMap conceptMap = next.lastEntry().getValue();
				if (theConceptMapUrl.equals(conceptMap.getUrl()))
				retVal.add(conceptMap);
				break;
			}
		}

		return retVal;
	}

	@Override
	@Update
	public MethodOutcome update(
		@ResourceParam ConceptMap theConceptMap,
		@ConditionalUrlParam String theConditional,
		RequestDetails theRequestDetails) {

		MethodOutcome methodOutcome = new MethodOutcome();

		if (theConditional != null) {
			String url = null;

			try {
				List<NameValuePair> params = URLEncodedUtils.parse(new URI(theConditional), Charsets.UTF_8);
				for (NameValuePair param : params) {
					if (param.getName().equalsIgnoreCase("url")) {
						url = param.getValue();
						break;
					}
				}
			} catch (URISyntaxException urise) {
				throw new InvalidRequestException(urise);
			}

			if (isNotBlank(url)) {
				List<ConceptMap> conceptMaps = searchByUrl(url);

				if (!conceptMaps.isEmpty()) {
					methodOutcome = super.update(conceptMaps.get(0), null, theRequestDetails);
				} else {
					methodOutcome = create(theConceptMap, theRequestDetails);
				}
			}

		} else {
			methodOutcome = super.update(theConceptMap, null, theRequestDetails);
		}

		return methodOutcome;
	}
}
