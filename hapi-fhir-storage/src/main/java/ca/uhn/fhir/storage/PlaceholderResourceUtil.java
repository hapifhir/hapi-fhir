/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TerserUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;

/**
 * Assembles auto-created placeholder reference targets so that every code path producing one — the
 * link resolver's auto-create path and the transaction bundle normalizer's synthetic conditional
 * creates — builds the same resource shape.
 */
// Created by Claude Fable 5
public final class PlaceholderResourceUtil {

	private PlaceholderResourceUtil() {}

	/**
	 * Builds an unpersisted placeholder resource of the given type: stamps the
	 * {@link HapiExtensions#EXT_RESOURCE_PLACEHOLDER} marker (when the type supports extensions) and adds
	 * one identifier per supplied entry.
	 */
	public static IBaseResource buildPlaceholderResource(
			@Nonnull FhirContext theFhirContext,
			@Nonnull RuntimeResourceDefinition theResourceDef,
			@Nonnull List<CanonicalIdentifier> theIdentifiers) {
		IBaseResource placeholder = theResourceDef.newInstance();
		ExtensionUtil.addExtensionIfSupported(
				theFhirContext, placeholder, EXT_RESOURCE_PLACEHOLDER, "boolean", Boolean.TRUE);
		for (CanonicalIdentifier identifier : theIdentifiers) {
			TerserUtil.addIdentifierToResource(
					theFhirContext,
					placeholder,
					identifier.getSystemElement().getValueAsString(),
					identifier.getValueElement().getValueAsString());
		}
		return placeholder;
	}

	/**
	 * Return true if the given resource is a placeholder resource, as identified by a specific extension
	 * @param theResource the {@link IBaseResource} to check
	 * @return whether or not this resource is a placeholder.
	 */
	public static boolean isPlaceholderResource(IBaseResource theResource) {
		if (!(theResource instanceof IBaseHasExtensions)) {
			// if it can't have extensions, we can't check
			// the extension to verify it is a Placeholder Resource
			// (so it probably isn't one)
			return false;
		}

		IBaseExtension<?, ?> extension = ExtensionUtil.getExtensionByUrl(theResource, EXT_RESOURCE_PLACEHOLDER);
		if (extension == null) {
			return false;
		}
		IBaseDatatype type = extension.getValue();
		if (type instanceof IBaseBooleanDatatype bt) {
			return ObjectUtils.getIfNull(bt.getValue(), false);
		}
		return false;
	}
}
