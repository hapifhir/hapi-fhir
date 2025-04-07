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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestAuthorizationJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CdsPrefetchFhirClientSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(CdsPrefetchFhirClientSvc.class);
	private int myNumberOfEntriesThresholdForPerfWarning = 1000;
	private final FhirContext myFhirContext;

	@Nullable
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public CdsPrefetchFhirClientSvc(
			FhirContext theFhirContext, @Nullable IInterceptorBroadcaster theInterceptorBroadcaster) {
		myFhirContext = theFhirContext;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	/**
	 * Given a URL to fetch for a Cds Hooks, this method will attempt to fetch the URL into a resource or a bundle.
	 * @param theCdsServiceRequestJson the CDS Service hook request
	 * @param theUrl the url to fetch
	 * @param theMaxPages if the URL is for a search, this specifies the maximum number of pages to fetch. Any non-positive
	 *                    number is treated as nolimit. If the limit is reached, a bundle the is returned containing the pages fetched so far
	 *                    and a warning is logged.
	 * @return the resource or bundle
	 */
	public IBaseResource resourceFromUrl(
			CdsServiceRequestJson theCdsServiceRequestJson, String theUrl, Integer theMaxPages) {
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
			return collectResourcesFromUrlFollowingNextLinks(client, theUrl, theMaxPages);
		} else {
			throw new InvalidRequestException(
					Msg.code(2384) + "Unable to translate url " + theUrl + " into a resource or a bundle.");
		}
	}

	/**
	 * Collect resources from a URL that returns a bundle. If the bundle has more than one page,
	 * this paginates and collect all resources into a single bundle, up to the specified maximum number of pages.
	 */
	private IBaseBundle collectResourcesFromUrlFollowingNextLinks(
			IGenericClient theClient, String theUrl, Integer theMaxPages) {
		IBaseBundle bundle = theClient.search().byUrl(theUrl).execute();

		boolean hasNext = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, "next") != null;
		if (!hasNext) {
			return bundle;
		}

		// bundle has more than one page, paginate and collect all resources into a single bundle
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.setType(BundleUtil.getBundleType(myFhirContext, bundle));
		int numberOfEntriesProcessed = addAllResourcesOfBundleToBundleBuilder(bundleBuilder, bundle);
		int numberOfPagesProcessed = 1;
		boolean isPerformanceTraceWarningHookCalled = false;

		while (hasNext) {
			if (numberOfPagesProcessed == theMaxPages) {
				ourLog.warn(
						"The limit of {} pages has been reached for retrieving the CDS Hooks prefetch url '{}', will not fetch any more pages for this query.",
						theMaxPages,
						theUrl);
				break;
			}
			if (!isPerformanceTraceWarningHookCalled
					&& numberOfEntriesProcessed >= myNumberOfEntriesThresholdForPerfWarning) {
				String msg = String.format(
						"CDS Hooks prefetch url '%s' returned %d entries after %d pages and there are more pages to fetch. This may be a performance issue. If possible, consider modifying the query to return only the resources needed.",
						theUrl, numberOfEntriesProcessed, numberOfPagesProcessed);
				ourLog.warn(msg);
				callPerfTraceWarningHook(msg);
				isPerformanceTraceWarningHookCalled = true;
			}
			bundle = theClient.loadPage().next(bundle).execute();
			numberOfEntriesProcessed += addAllResourcesOfBundleToBundleBuilder(bundleBuilder, bundle);
			hasNext = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, "next") != null;
			numberOfPagesProcessed++;
		}

		return bundleBuilder.getBundle();
	}

	private void callPerfTraceWarningHook(String msg) {
		if (myInterceptorBroadcaster != null && myInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_WARNING)) {
			// Interceptor broadcast: JPA_PERFTRACE_WARNING
			HookParams params = new HookParams()
					.add(RequestDetails.class, null)
					.add(ServletRequestDetails.class, null)
					.add(StorageProcessingMessage.class, new StorageProcessingMessage().setMessage(msg));
			myInterceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_WARNING, params);
		}
	}

	private int addAllResourcesOfBundleToBundleBuilder(BundleBuilder theBundleBuilder, IBaseBundle theBundle) {
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
		return entries.size();
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

	/**
	 * Set the threshold for the number of entries that will trigger a call ot performance trace warning call.
	 */
	public void setNumberOfEntriesThresholdForPerfWarning(int theThreshold) {
		this.myNumberOfEntriesThresholdForPerfWarning = theThreshold;
	}
}
