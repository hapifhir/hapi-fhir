/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkExportJobHelper {


	private final IGenericClient myClient;

	public BulkExportJobHelper(IGenericClient theClient) {
		myClient = theClient;
	}

	// FIXME: inline all
	public BulkExportContents fetchJobResults(BulkExportJobResults theResults) {
		Multimap<String, IBaseResource> typeToResources = ArrayListMultimap.create();

		Map<String, List<IBaseResource>> typeToNdJson = convertJobResultsToResources(theResults);
		for (Map.Entry<String, List<IBaseResource>> entry : typeToNdJson.entrySet()) {
			typeToResources.putAll(entry.getKey(), entry.getValue());
		}

		return new BulkExportContents(typeToResources);
	}

	private Map<String, String> convertJobResultsToStringContents(BulkExportJobResults theResults) {
		Map<String, String> typeToResources = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : theResults.getResourceTypeToBinaryIds().entrySet()) {
			typeToResources.put(entry.getKey(), "");
			StringBuilder sb = new StringBuilder();
			List<String> binaryIds = entry.getValue();
			for (String binaryId : binaryIds) {
				String contents = getBinaryContentsAsString(binaryId);
				if (!contents.endsWith("\n")) {
					contents = contents + "\n";
				}
				sb.append(contents);
			}
			typeToResources.put(entry.getKey(), sb.toString());
		}
		return typeToResources;
	}

	private Map<String, List<IBaseResource>> convertJobResultsToResources(BulkExportJobResults theResults) {
		Map<String, String> stringStringMap = convertJobResultsToStringContents(theResults);
		Map<String, List<IBaseResource>> typeToResources = new HashMap<>();
		stringStringMap.forEach((key, value) -> typeToResources.put(key, convertNDJSONToResources(value)));
		return typeToResources;
	}

	private List<IBaseResource> convertNDJSONToResources(String theValue) {
		IParser iParser = myClient.getFhirContext().newJsonParser();
		return theValue.lines()
			.map(iParser::parseResource)
			.toList();
	}

	private String getBinaryContentsAsString(String theBinaryId) {
		IBaseBinary binary = (IBaseBinary) myClient
			.read()
			.resource("Binary")
			.withId(theBinaryId)
			.execute();
		assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
		return new String(binary.getContent(), Constants.CHARSET_UTF8);
	}

	public Map<String, Map<String, Set<String>>> convertJobResultsToResourceVersionMap(BulkExportJobResults theBulkExportJobResults) {
		Map<String, List<IBaseResource>> exportedResourcesByType = convertJobResultsToResources(theBulkExportJobResults);

		Map<String, Map<String, Set<String>>> retVal = new HashMap<>();

		for (Map.Entry<String, List<IBaseResource>> resourcesOfTypeEntry : exportedResourcesByType.entrySet()) {
			retVal.put(
				resourcesOfTypeEntry.getKey(),
				resourcesOfTypeEntry.getValue().stream().collect(Collectors.groupingBy(
					r -> r.getIdElement().toVersionless().getValueAsString(),
					mapping(r -> r.getIdElement().getValueAsString(), Collectors.toSet())
				))
			);
		}

		return retVal;
	}


	public static class BulkExportContents {
		private final Multimap<String, IBaseResource> myTypeToResources;

		BulkExportContents(Multimap<String, IBaseResource> theTypeToResources) {
			myTypeToResources = theTypeToResources;
		}

		public int countResources(String theResourceType) {
			return myTypeToResources.get(theResourceType).size();
		}

		public Set<String> getResourceIdPartsForType(String theResourceType) {
			return myTypeToResources
				.get(theResourceType)
				.stream()
				.map(t -> t.getIdElement().getIdPart())
				.collect(Collectors.toSet());
		}

		public Set<String> getResourceTypes() {
			return myTypeToResources.keySet();
		}
	}
}
