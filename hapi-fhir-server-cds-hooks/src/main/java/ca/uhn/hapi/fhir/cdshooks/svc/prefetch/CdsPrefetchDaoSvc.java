/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

@Service
public class CdsPrefetchDaoSvc {
	public static final int MAX_RESOURCES_IN_BUNDLE = 1024;
	private final DaoRegistry myDaoRegistry;
	private final MatchUrlService myMatchUrlService;
	private final FhirContext myFhirContext;

	/**
	 * @param theDaoRegistry the registry to prefetch from, or {@code null} when the server has no JPA storage, in
	 *                       which case prefetching through the DAO strategy fails with a {@link ConfigurationException}
	 */
	public CdsPrefetchDaoSvc(
			@Nullable DaoRegistry theDaoRegistry,
			@Nonnull MatchUrlService theMatchUrlService,
			@Nonnull FhirContext theFhirContext) {
		myDaoRegistry = theDaoRegistry;
		myMatchUrlService = theMatchUrlService;
		myFhirContext = theFhirContext;
	}

	public IBaseResource resourceFromUrl(String theUrl) {
		UrlUtil.UrlParts parts = UrlUtil.parseUrl(theUrl);
		String resourceType = parts.getResourceType();
		if (resourceType == null) {
			throw new InvalidRequestException(
					Msg.code(2380) + "Failed to resolve " + theUrl + ". Url does not start with resource type");
		}
		if (myDaoRegistry == null) {
			throw new ConfigurationException(Msg.code(3054) + "Unable to resolve " + theUrl
					+ " using the DAO prefetch strategy: no DaoRegistry is available on this server");
		}
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		if (dao == null) {
			throw new ConfigurationException(Msg.code(2381) + "No dao registered for resource type " + resourceType);
		}

		String resourceId = parts.getResourceId();
		if (resourceId != null) {
			// TODO KHS get a RequestDetails down here
			return dao.read(new IdDt(resourceType, resourceId));
		}

		String matchUrl = parts.getParams();
		if (matchUrl != null) {
			return getBundleFromUrl(resourceType, dao, matchUrl);
		}

		throw new InvalidRequestException(
				Msg.code(2382) + "Unable to translate url " + theUrl + " into a resource or a bundle");
	}

	private IBaseResource getBundleFromUrl(String resourceType, IFhirResourceDao<?> dao, String matchUrl) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(resourceType);
		SearchParameterMap searchParams = myMatchUrlService.translateMatchUrl(matchUrl, resourceDef);
		searchParams.setLoadSynchronous(true);
		// TODO KHS get a RequestDetails down here
		IBundleProvider bundleProvider = dao.search(searchParams);
		IVersionSpecificBundleFactory bundleFactory = myFhirContext.newBundleFactory();
		bundleFactory.addResourcesToBundle(
				bundleProvider.getResources(0, MAX_RESOURCES_IN_BUNDLE), BundleTypeEnum.SEARCHSET, null, null, null);
		return bundleFactory.getResourceBundle();
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}
}
