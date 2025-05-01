/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestAuthorizationJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CdsPrefetchFhirClientSvc {
	private final FhirContext myFhirContext;

	public CdsPrefetchFhirClientSvc(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public IBaseResource resourceFromUrl(CdsServiceRequestJson theCdsServiceRequestJson, String theUrl) {
		IGenericClient client = buildClient(theCdsServiceRequestJson);
		UrlUtil.UrlParts parts = UrlUtil.parseUrl(theUrl);
		String resourceType = parts.getResourceType();
		if (StringUtils.isEmpty(resourceType)) {
			throw new InvalidRequestException(
					Msg.code(2383) + "Failed to resolve " + theUrl + ". Url does not start with a resource type.");
		}

		String resourceId = parts.getResourceId();
		String matchUrl = parts.getParams();
		if (resourceId != null) {
			return client.read().resource(resourceType).withId(resourceId).execute();
		} else if (matchUrl != null) {
			return collectResourcesFromUrlFollowingNextLinks(client, theUrl);
		} else {
			throw new InvalidRequestException(
					Msg.code(2384) + "Unable to translate url " + theUrl + " into a resource or a bundle.");
		}
	}

	/**
	 * Collect resources from a URL that returns a bundle. If the bundle has more than one page,
	 * this paginates and collect all resources into a single bundle
	 */
	private IBaseBundle collectResourcesFromUrlFollowingNextLinks(IGenericClient theClient, String theUrl) {
		IBaseBundle bundle = theClient.search().byUrl(theUrl).execute();

		boolean hasNext = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, "next") != null;
		if (!hasNext) {
			return bundle;
		}

		// bundle has more than one page, paginate and collect all resources into a single bundle
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.setType(BundleUtil.getBundleType(myFhirContext, bundle));
		addAllResourcesOfBundleToBundleBuilder(bundleBuilder, bundle);

		while (hasNext) {
			bundle = theClient.loadPage().next(bundle).execute();
			addAllResourcesOfBundleToBundleBuilder(bundleBuilder, bundle);
			hasNext = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, "next") != null;
		}

		return bundleBuilder.getBundle();
	}

	private void addAllResourcesOfBundleToBundleBuilder(BundleBuilder theBundleBuilder, IBaseBundle theBundle) {
		List<SearchBundleEntryParts> entries = BundleUtil.getSearchBundleEntryParts(myFhirContext, theBundle);

		for (SearchBundleEntryParts currentEntry : entries) {
			IBase newEntry = theBundleBuilder.addEntry();
			theBundleBuilder.addFullUrl(newEntry, currentEntry.getFullUrl());
			theBundleBuilder.addToEntry(newEntry, "resource", currentEntry.getResource());
			boolean hasSearchMode = currentEntry.getSearchMode() != null;
			boolean hasSearchScore = currentEntry.getSearchScore() != null;
			if (hasSearchMode || hasSearchScore) {
				IBaseBackboneElement search = theBundleBuilder.addSearch(newEntry);
				if (hasSearchMode) {
					theBundleBuilder.setSearchField(
							search, "mode", currentEntry.getSearchMode().getCode());
				}
				if (hasSearchScore) {
					theBundleBuilder.setSearchField(
							search, "score", currentEntry.getSearchScore().toString());
				}
			}
		}
	}

	private IGenericClient buildClient(CdsServiceRequestJson theCdsServiceRequestJson) {
		String fhirServerBase = theCdsServiceRequestJson.getFhirServer();
		CdsServiceRequestAuthorizationJson serviceRequestAuthorization =
				theCdsServiceRequestJson.getServiceRequestAuthorizationJson();

		IGenericClient client = myFhirContext.newRestfulGenericClient(fhirServerBase);
		if (serviceRequestAuthorization != null && serviceRequestAuthorization.getAccessToken() != null) {
			IClientInterceptor authInterceptor =
					new BearerTokenAuthInterceptor(serviceRequestAuthorization.getAccessToken());
			client.registerInterceptor(authInterceptor);
		}
		return client;
	}
}
