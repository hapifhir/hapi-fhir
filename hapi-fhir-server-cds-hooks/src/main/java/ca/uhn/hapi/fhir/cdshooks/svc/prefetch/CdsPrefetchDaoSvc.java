/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

@Service
public class CdsPrefetchDaoSvc {
	public static final int MAX_RESOURCES_IN_BUNDLE = 1024;
	private final DaoRegistry myDaoRegistry;
	private final MatchUrlService myMatchUrlService;
	private final FhirContext myFhirContext;

	public CdsPrefetchDaoSvc(DaoRegistry theDaoRegistry, MatchUrlService theMatchUrlService, FhirContext theFhirContext) {
		myDaoRegistry = theDaoRegistry;
		myMatchUrlService = theMatchUrlService;
		myFhirContext = theFhirContext;
	}

	public IBaseResource resourceFromUrl(String theUrl) {
		UrlUtil.UrlParts parts = UrlUtil.parseUrl(theUrl);
		String resourceType = parts.getResourceType();
		if (resourceType == null) {
			throw new InvalidRequestException("Failed to resolve " + theUrl + ". Url does not start with resource type");
		}
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);
		if (dao == null) {
			throw new ConfigurationException("No dao registered for resource type " + resourceType);
		}

		String resourceId = parts.getResourceId();
		if (resourceId != null) {
			return dao.read(new IdDt(resourceType, resourceId));
		}

		String matchUrl = parts.getParams();
		if (matchUrl != null) {
			return getBundleFromUrl(resourceType, dao, matchUrl);
		}

		throw new InvalidRequestException("Unable to translate url " + theUrl + " into a resource or a bundle");
	}

	private IBaseResource getBundleFromUrl(String resourceType, IFhirResourceDao dao, String matchUrl) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(resourceType);
		SearchParameterMap searchParams = myMatchUrlService.translateMatchUrl(matchUrl, resourceDef);
		searchParams.setLoadSynchronous(true);
		IBundleProvider bundleProvider = dao.search(searchParams);
		IVersionSpecificBundleFactory bundleFactory = myFhirContext.newBundleFactory();
		bundleFactory.addResourcesToBundle(bundleProvider.getResources(0, MAX_RESOURCES_IN_BUNDLE), BundleTypeEnum.SEARCHSET, null, null, null);
		return bundleFactory.getResourceBundle();
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}
}
