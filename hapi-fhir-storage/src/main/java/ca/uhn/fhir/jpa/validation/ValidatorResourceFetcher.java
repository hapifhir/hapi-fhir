/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.elementmodel.JsonParser;
import org.hl7.fhir.r5.model.CanonicalResource;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.hl7.fhir.utilities.CanonicalPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ValidatorResourceFetcher implements IValidatorResourceFetcher {

	private static final Logger ourLog = LoggerFactory.getLogger(ValidatorResourceFetcher.class);

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final VersionSpecificWorkerContextWrapper myVersionSpecificContextWrapper;

	public ValidatorResourceFetcher(
			FhirContext theFhirContext, IValidationSupport theValidationSupport, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myVersionSpecificContextWrapper =
				VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(theValidationSupport);
	}

	@Override
	public Element fetch(IResourceValidator iResourceValidator, Object appContext, String theUrl) throws FHIRException {
		IdType id = new IdType(theUrl);
		String resourceType = id.getResourceType();
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		IBaseResource target;
		try {
			target = dao.read(id, (RequestDetails) appContext);
		} catch (ResourceNotFoundException e) {
			ourLog.info("Failed to resolve local reference: {}", theUrl);
			try {
				target = fetchByUrl(theUrl, dao, (RequestDetails) appContext);
			} catch (ResourceNotFoundException e2) {
				ourLog.info("Failed to find resource by URL: {}", theUrl);
				return null;
			}
		}
		try {
			return new JsonParser(myVersionSpecificContextWrapper)
					.parse(myFhirContext.newJsonParser().encodeResourceToString(target), resourceType);
		} catch (Exception e) {
			throw new FHIRException(Msg.code(576) + e);
		}
	}

	private IBaseResource fetchByUrl(String url, IFhirResourceDao<?> dao, RequestDetails requestDetails)
			throws ResourceNotFoundException {
		CanonicalPair pair = new CanonicalPair(url);
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.add("url", new UriParam(pair.getUrl()));
		String version = pair.getVersion();
		if (version != null && !version.isEmpty()) {
			searchParameterMap.add("version", new TokenParam(version));
		}
		List<IBaseResource> results = null;
		try {
			results = dao.search(searchParameterMap, requestDetails).getAllResources();
		} catch (InvalidRequestException e) {
			ourLog.info("Resource does not support 'url' or 'version' Search Parameters");
		}
		if (results != null && results.size() > 0) {
			if (results.size() > 1) {
				ourLog.warn(
						String.format("Multiple results found for URL '%s', only the first will be considered.", url));
			}
			return results.get(0);
		} else {
			throw new ResourceNotFoundException(Msg.code(2444) + "Failed to find resource by URL: " + url);
		}
	}

	@Override
	public boolean resolveURL(
			IResourceValidator iResourceValidator, Object o, String s, String s1, String s2, boolean isCanonical) {
		return true;
	}

	@Override
	public byte[] fetchRaw(IResourceValidator iResourceValidator, String s) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(Msg.code(577));
	}

	@Override
	public IValidatorResourceFetcher setLocale(Locale locale) {
		// ignore
		return this;
	}

	@Override
	public CanonicalResource fetchCanonicalResource(IResourceValidator validator, Object appContext, String url)
			throws URISyntaxException {
		return null;
	}

	@Override
	public boolean fetchesCanonicalResource(IResourceValidator iResourceValidator, String s) {
		return false;
	}

	@Override
	public Set<String> fetchCanonicalResourceVersions(IResourceValidator validator, Object appContext, String url) {
		return Collections.emptySet();
	}
}
