/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ReadOnlySearchParamCache {

	// resourceName -> searchParamName -> searchparam
	protected final Map<String, ResourceSearchParams> myResourceNameToSpNameToSp;
	protected final Map<String, RuntimeSearchParam> myUrlToParam;

	/**
	 * Constructor
	 */
	ReadOnlySearchParamCache() {
		myResourceNameToSpNameToSp = new HashMap<>();
		myUrlToParam = new HashMap<>();
	}

	/**
	 * Copy constructor
	 */
	private ReadOnlySearchParamCache(RuntimeSearchParamCache theRuntimeSearchParamCache) {
		myResourceNameToSpNameToSp = theRuntimeSearchParamCache.myResourceNameToSpNameToSp;
		myUrlToParam = theRuntimeSearchParamCache.myUrlToParam;
	}

	public Stream<RuntimeSearchParam> getSearchParamStream() {
		return myResourceNameToSpNameToSp.values().stream().flatMap(entry -> entry.values().stream());
	}

	protected ResourceSearchParams getSearchParamMap(String theResourceName) {
		ResourceSearchParams retval = myResourceNameToSpNameToSp.get(theResourceName);
		if (retval == null) {
			return ResourceSearchParams.empty(theResourceName);
		}
		return retval.readOnly();
	}

	public int size() {
		return myResourceNameToSpNameToSp.size();
	}

	public RuntimeSearchParam getByUrl(String theUrl) {
		return myUrlToParam.get(theUrl);
	}

	public static ReadOnlySearchParamCache fromFhirContext(
			@Nonnull FhirContext theFhirContext, @Nonnull SearchParameterCanonicalizer theCanonicalizer) {
		return fromFhirContext(theFhirContext, theCanonicalizer, null);
	}

	public static ReadOnlySearchParamCache fromFhirContext(
			@Nonnull FhirContext theFhirContext,
			@Nonnull SearchParameterCanonicalizer theCanonicalizer,
			@Nullable Set<String> theSearchParamPatternsToInclude) {
		assert theCanonicalizer != null;

		ReadOnlySearchParamCache retVal = new ReadOnlySearchParamCache();

		Set<String> resourceNames = theFhirContext.getResourceTypes();

		/*
		 * For R4/R4B we include a Bundle of SearchParameters from the core spec in
		 * hapi-fhir-validation-resources-r4/r4b
		 *
		 * For R5 we include the NPM core R5 packages in
		 * hapi-fhir-validation-resources-r5, so we get them from it
		 */
		List<IBaseResource> searchParams = null;
		if (theFhirContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			IBaseBundle allSearchParameterBundle = (IBaseBundle) theFhirContext
					.newJsonParser()
					.parseResource(
							ClasspathUtil.loadResourceAsStream("org/hl7/fhir/r4/model/sp/search-parameters.json"));
			searchParams = BundleUtil.toListOfResources(theFhirContext, allSearchParameterBundle);
		} else if (theFhirContext.getVersion().getVersion() == FhirVersionEnum.R4B) {
			IBaseBundle allSearchParameterBundle = (IBaseBundle) theFhirContext
					.newXmlParser()
					.parseResource(
							ClasspathUtil.loadResourceAsStream("org/hl7/fhir/r4b/model/sp/search-parameters.xml"));
			searchParams = BundleUtil.toListOfResources(theFhirContext, allSearchParameterBundle);
		} else if (theFhirContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			searchParams = FhirContext.forR5Cached().getValidationSupport().fetchAllSearchParameters();
		}

		searchParams = defaultIfNull(searchParams, Collections.emptyList());
		for (IBaseResource next : searchParams) {
			RuntimeSearchParam nextCanonical = theCanonicalizer.canonicalizeSearchParameter(next);

			if (nextCanonical != null) {

				// Force status to ACTIVE - For whatever reason the R5 draft SPs ship with
				// a status of DRAFT which means the server doesn't actually apply them.
				// At least this was the case as of 2021-12-24 - JA
				nextCanonical = new RuntimeSearchParam(
						nextCanonical.getId(),
						nextCanonical.getUri(),
						nextCanonical.getName(),
						nextCanonical.getDescription(),
						nextCanonical.getPath(),
						nextCanonical.getParamType(),
						nextCanonical.getProvidesMembershipInCompartments(),
						nextCanonical.getTargets(),
						RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE,
						nextCanonical.getComboSearchParamType(),
						nextCanonical.getComponents(),
						nextCanonical.getBase());

				Collection<String> base = nextCanonical.getBase();
				if (base.contains("Resource") || base.contains("DomainResource")) {
					base = resourceNames;
				}

				// Add it to our return value if permitted by the pattern parameters
				for (String nextResourceName : base) {
					ResourceSearchParams resourceSearchParams = retVal.myResourceNameToSpNameToSp.computeIfAbsent(
							nextResourceName, t -> new ResourceSearchParams(nextResourceName));
					String nextParamName = nextCanonical.getName();
					if (theSearchParamPatternsToInclude == null
							|| searchParamMatchesAtLeastOnePattern(
									theSearchParamPatternsToInclude, nextResourceName, nextParamName)) {
						resourceSearchParams.addSearchParamIfAbsent(nextParamName, nextCanonical);
					}
				}
			}
		}

		// Now grab all the runtime search parameters from the resource definitions
		for (String resourceName : resourceNames) {
			RuntimeResourceDefinition nextResDef = theFhirContext.getResourceDefinition(resourceName);
			String nextResourceName = nextResDef.getName();

			ResourceSearchParams resourceSearchParams = retVal.myResourceNameToSpNameToSp.computeIfAbsent(
					nextResourceName, t -> new ResourceSearchParams(nextResourceName));
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				String nextParamName = nextSp.getName();
				// Add it to our return value if permitted by the pattern parameters
				if (theSearchParamPatternsToInclude == null
						|| searchParamMatchesAtLeastOnePattern(
								theSearchParamPatternsToInclude, nextResourceName, nextParamName)) {
					resourceSearchParams.addSearchParamIfAbsent(nextParamName, nextSp);
				}
			}
		}
		return retVal;
	}

	public static boolean searchParamMatchesAtLeastOnePattern(
			Set<String> theSearchParamPatterns, String theResourceType, String theSearchParamName) {
		for (String nextPattern : theSearchParamPatterns) {
			if ("*".equals(nextPattern)) {
				return true;
			}
			int colonIdx = nextPattern.indexOf(':');
			Validate.isTrue(colonIdx > 0, "Invalid search param pattern: %s", nextPattern);
			String resourceType = nextPattern.substring(0, colonIdx);
			String searchParamName = nextPattern.substring(colonIdx + 1);
			Validate.notBlank(resourceType, "No resource type specified in pattern: %s", nextPattern);
			Validate.notBlank(searchParamName, "No param name specified in pattern: %s", nextPattern);
			if (!resourceType.equals("*") && !resourceType.equals(theResourceType)) {
				continue;
			}
			if (!searchParamName.equals("*") && !searchParamName.equals(theSearchParamName)) {
				continue;
			}
			return true;
		}

		return false;
	}

	public static ReadOnlySearchParamCache fromRuntimeSearchParamCache(
			RuntimeSearchParamCache theRuntimeSearchParamCache) {
		return new ReadOnlySearchParamCache(theRuntimeSearchParamCache);
	}
}
