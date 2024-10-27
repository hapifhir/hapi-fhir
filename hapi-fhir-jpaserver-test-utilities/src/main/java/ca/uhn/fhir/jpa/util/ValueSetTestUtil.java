/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class ValueSetTestUtil {

	private final VersionCanonicalizer myCanonicalizer;

	public ValueSetTestUtil(FhirVersionEnum theFhirVersion) {
		myCanonicalizer = new VersionCanonicalizer(theFhirVersion);
	}

	public String extractExpansionMessage(IBaseResource theValueSet) {
		ValueSet outcome = myCanonicalizer.valueSetToCanonical(theValueSet);
		List<Extension> extensions = outcome.getMeta().getExtensionsByUrl(EXT_VALUESET_EXPANSION_MESSAGE);
		assertThat(extensions).hasSize(1);
		String expansionMessage = extensions.get(0).getValueAsPrimitive().getValueAsString();
		return expansionMessage;
	}

	@Nonnull
	public List<String> toCodes(IBaseResource theExpandedValueSet) {
		ValueSet outcome = myCanonicalizer.valueSetToCanonical(theExpandedValueSet);
		return outcome.getExpansion().getContains().stream()
				.map(t -> t.getCode())
				.collect(Collectors.toList());
	}
}
