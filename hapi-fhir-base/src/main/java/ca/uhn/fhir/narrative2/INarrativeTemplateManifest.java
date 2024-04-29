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
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public interface INarrativeTemplateManifest {
	List<INarrativeTemplate> getTemplateByResourceName(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull String theResourceName,
			@Nonnull Collection<String> theProfiles);

	List<INarrativeTemplate> getTemplateByName(
			@Nonnull FhirContext theFhirContext, @Nonnull EnumSet<TemplateTypeEnum> theStyles, @Nonnull String theName);

	List<INarrativeTemplate> getTemplateByElement(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull IBase theElementValue);

	List<INarrativeTemplate> getTemplateByFragmentName(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull String theFragmentName);
}
