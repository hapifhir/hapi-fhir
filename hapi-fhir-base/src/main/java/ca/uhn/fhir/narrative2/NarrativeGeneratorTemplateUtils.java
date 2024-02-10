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
package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Objects;

/**
 * An instance of this class is added to the Thymeleaf context as a variable with
 * name <code>"narrativeUtil"</code> and can be accessed from narrative templates.
 *
 * @since 7.0.0
 */
public class NarrativeGeneratorTemplateUtils {

	public static final NarrativeGeneratorTemplateUtils INSTANCE = new NarrativeGeneratorTemplateUtils();

	/**
	 * Given a Bundle as input, are any entries present with a given resource type
	 */
	public boolean bundleHasEntriesWithResourceType(IBaseBundle theBaseBundle, String theResourceType) {
		FhirContext ctx = theBaseBundle.getStructureFhirVersionEnum().newContextCached();
		List<Pair<String, IBaseResource>> entryResources =
				BundleUtil.getBundleEntryUrlsAndResources(ctx, theBaseBundle);
		return entryResources.stream()
				.map(Pair::getValue)
				.filter(Objects::nonNull)
				.anyMatch(t -> ctx.getResourceType(t).equals(theResourceType));
	}
}
