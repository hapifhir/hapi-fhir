package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Extract search params for advanced lucene indexing.
 *
 * This class re-uses the extracted JPA entities to build an ExtendedLuceneIndexData instance.
 */
public class ExtendedLuceneIndexExtractor {

	private final FhirContext myContext;
	private final Map<String, RuntimeSearchParam> myParams;

	public ExtendedLuceneIndexExtractor(FhirContext theContext, Map<String, RuntimeSearchParam> theActiveParams) {
		myContext = theContext;
		myParams = theActiveParams;
	}

	@NotNull
	public ExtendedLuceneIndexData extract(ResourceIndexedSearchParams theNewParams) {
		ExtendedLuceneIndexData retVal = new ExtendedLuceneIndexData(myContext);

		theNewParams.myStringParams.forEach(nextParam ->
			retVal.addStringIndexData(nextParam.getParamName(), nextParam.getValueExact()));

		theNewParams.myTokenParams.forEach(nextParam ->
			retVal.addTokenIndexData(nextParam.getParamName(), nextParam.getSystem(), nextParam.getValue()));

		if (!theNewParams.myLinks.isEmpty()) {

			// awkwardly, links are indexed by jsonpath, not by search param.
			// so we re-build the linkage.
			Map<String, List<String>> linkPathToParamName = new HashMap<>();
			for (String nextParamName : theNewParams.getPopulatedResourceLinkParameters()) {
				RuntimeSearchParam sp = myParams.get(nextParamName);
				List<String> pathsSplit = sp.getPathsSplit();
				for (String nextPath : pathsSplit) {
					// we want case-insensitive matching
					nextPath = nextPath.toLowerCase(Locale.ROOT);

					linkPathToParamName
						.computeIfAbsent(nextPath, (p) -> new ArrayList<>())
						.add(nextParamName);
				}
			}

			for (ResourceLink nextLink : theNewParams.getResourceLinks()) {
				String insensitivePath = nextLink.getSourcePath().toLowerCase(Locale.ROOT);
				List<String> paramNames = linkPathToParamName.getOrDefault(insensitivePath, Collections.emptyList());
				for (String nextParamName : paramNames) {
					String qualifiedTargetResourceId = nextLink.getTargetResourceType() + "/" + nextLink.getTargetResourceId();
					retVal.addResourceLinkIndexData(nextParamName, qualifiedTargetResourceId);
				}
			}
		}
		return retVal;
	}
}
