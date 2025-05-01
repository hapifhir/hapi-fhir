/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TerserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
		FhirVersionEnum fhirVersionEnum = theBaseBundle.getStructureFhirVersionEnum();
		FhirContext ctx = FhirContext.forCached(fhirVersionEnum);
		List<Pair<String, IBaseResource>> entryResources =
				BundleUtil.getBundleEntryUrlsAndResources(ctx, theBaseBundle);
		return entryResources.stream()
				.map(Pair::getValue)
				.filter(Objects::nonNull)
				.anyMatch(t -> ctx.getResourceType(t).equals(theResourceType));
	}

	/**
	 * Returns if the bundle contains an entry resource whose `code` property contains a matching code system and code.
	 *
	 * @param theBundle the bundle to inspect
	 * @param theResourceType the resource type to look for
	 * @param theCodeSystem the code system to find
	 * @param theCode the code to find
	 * @return returns true if bundle has a resource that with matching code/code system
	 */
	public boolean bundleHasEntriesWithCode(
			IBaseBundle theBundle, String theResourceType, String theCodeSystem, String theCode) {
		FhirVersionEnum fhirVersionEnum = theBundle.getStructureFhirVersionEnum();
		FhirContext ctx = FhirContext.forCached(fhirVersionEnum);

		return getEntryResources(ctx, theBundle, theResourceType).anyMatch(t -> {
			List<IBase> codeList = TerserUtil.getFieldByFhirPath(ctx, "code.coding", t);
			return codeList.stream().anyMatch(m -> {
				IBaseCoding coding = (IBaseCoding) m;
				return StringUtils.equals(coding.getSystem(), theCodeSystem)
						&& StringUtils.equals(coding.getCode(), theCode);
			});
		});
	}

	/**
	 * Gets a boolean indicating if at least one bundle entry resource's `code` property does NOT contain the
	 * code system/code specified.
	 *
	 * @param theBundle the bundle to inspect
	 * @param theResourceType the resource type to find
	 * @param theCodeSystem the code system to find
	 * @param theCode the code to find
	 * @return Returns true if one entry of resource type requested does not contain the specified code/system
	 */
	public boolean bundleHasEntriesWithoutCode(
			IBaseBundle theBundle, String theResourceType, String theCodeSystem, String theCode) {

		FhirVersionEnum fhirVersionEnum = theBundle.getStructureFhirVersionEnum();
		FhirContext ctx = FhirContext.forCached(fhirVersionEnum);

		return getEntryResources(ctx, theBundle, theResourceType).anyMatch(t -> {
			List<IBase> codeList = TerserUtil.getFieldByFhirPath(ctx, "code.coding", t);
			return codeList.stream().allMatch(m -> {
				IBaseCoding coding = (IBaseCoding) m;
				return !(StringUtils.equals(coding.getSystem(), theCodeSystem)
						&& StringUtils.equals(coding.getCode(), theCode));
			});
		});
	}

	private Stream<IBaseResource> getEntryResources(
			FhirContext theContext, IBaseBundle theBundle, String theResourceType) {
		List<Pair<String, IBaseResource>> entryResources =
				BundleUtil.getBundleEntryUrlsAndResources(theContext, theBundle);
		return entryResources.stream()
				.map(Pair::getValue)
				.filter(Objects::nonNull)
				.filter(t -> theContext.getResourceType(t).equals(theResourceType));
	}
}
