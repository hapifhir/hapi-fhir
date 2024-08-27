package ca.uhn.fhir.cr.repo;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.api.Repository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.cr.repo.RequestDetailsCloner.startWith;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class leverages DaoRegistry from Hapi-fhir to implement CRUD FHIR API operations constrained to provide only the operations necessary for the cql-evaluator modules to function.
 **/
public class HapiFhirRepository implements Repository {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiFhirRepository.class);
	private final DaoRegistry myDaoRegistry;
	private final RequestDetails myRequestDetails;
	private final RestfulServer myRestfulServer;

	public HapiFhirRepository(
			DaoRegistry theDaoRegistry, RequestDetails theRequestDetails, RestfulServer theRestfulServer) {
		myDaoRegistry = theDaoRegistry;
		myRequestDetails = theRequestDetails;
		myRestfulServer = theRestfulServer;
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> T read(
			Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return myDaoRegistry.getResourceDao(theResourceType).read(theId, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T theResource, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return myDaoRegistry.getResourceDao(theResource).create(theResource, details);
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I theId, P thePatchParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		// TODO update FHIR patchType once FHIRPATCH bug has been fixed
		return myDaoRegistry
				.getResourceDao(theId.getResourceType())
				.patch(theId, null, null, null, thePatchParameters, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T theResource, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();

		return myDaoRegistry.getResourceDao(theResource).update(theResource, details);
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();

		return myDaoRegistry.getResourceDao(theResourceType).delete(theId, details);
	}

	@Override
	public <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> theBundleType,
			Class<T> theResourceType,
			Map<String, List<IQueryParameterType>> theSearchParameters,
			Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		SearchConverter converter = new SearchConverter();
		converter.convertParameters(theSearchParameters, fhirContext());
		details.setParameters(converter.resultParameters);
		var bundleProvider =
				myDaoRegistry.getResourceDao(theResourceType).search(converter.searchParameterMap, details);

		if (bundleProvider == null) {
			return null;
		}

		return createBundle(details, bundleProvider, null);
	}

	private <B extends IBaseBundle> B createBundle(
			RequestDetails theRequestDetails, IBundleProvider theBundleProvider, String thePagingAction) {
		var count = RestfulServerUtils.extractCountParameter(theRequestDetails);
		var linkSelf = RestfulServerUtils.createLinkSelf(theRequestDetails.getFhirServerBase(), theRequestDetails);

		Set<Include> includes = new HashSet<>();
		var reqIncludes = theRequestDetails.getParameters().get(Constants.PARAM_INCLUDE);
		if (reqIncludes != null) {
			for (String nextInclude : reqIncludes) {
				includes.add(new Include(nextInclude));
			}
		}

		var offset = RestfulServerUtils.tryToExtractNamedParameter(theRequestDetails, Constants.PARAM_PAGINGOFFSET);
		if (offset == null || offset < 0) {
			offset = 0;
		}
		var start = offset;
		if (theBundleProvider.size() != null) {
			start = Math.max(0, Math.min(offset, theBundleProvider.size()));
		}

		BundleTypeEnum bundleType = null;
		var bundleTypeValues = theRequestDetails.getParameters().get(Constants.PARAM_BUNDLETYPE);
		if (bundleTypeValues != null) {
			bundleType = BundleTypeEnum.VALUESET_BINDER.fromCodeString(bundleTypeValues[0]);
		} else {
			bundleType = BundleTypeEnum.SEARCHSET;
		}

		var responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(
				theRequestDetails, myRestfulServer.getDefaultResponseEncoding());
		var linkEncoding =
				theRequestDetails.getParameters().containsKey(Constants.PARAM_FORMAT) && responseEncoding != null
						? responseEncoding.getEncoding()
						: null;

		return (B) BundleProviderUtil.createBundleFromBundleProvider(
				myRestfulServer,
				theRequestDetails,
				count,
				linkSelf,
				includes,
				theBundleProvider,
				start,
				bundleType,
				linkEncoding,
				thePagingAction);
	}

	// TODO: The main use case for this is paging through Bundles, but I suppose that technically
	// we ought to handle any old link. Maybe this is also an escape hatch for "custom non-FHIR
	// repository action"?
	@Override
	public <B extends IBaseBundle> B link(Class<B> theBundleType, String theUrl, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		var urlParts = UrlUtil.parseUrl(theUrl);
		details.setCompleteUrl(theUrl);
		details.setParameters(UrlUtil.parseQueryStrings(urlParts.getParams()));

		var pagingProvider = myRestfulServer.getPagingProvider();
		if (pagingProvider == null) {
			throw new InvalidRequestException(Msg.code(2312) + "This server does not support paging");
		}

		var thePagingAction = details.getParameters().get(Constants.PARAM_PAGINGACTION)[0];

		IBundleProvider bundleProvider;

		String pageId = null;
		String[] pageIdParams = details.getParameters().get(Constants.PARAM_PAGEID);
		if (pageIdParams != null && pageIdParams.length > 0 && isNotBlank(pageIdParams[0])) {
			pageId = pageIdParams[0];
		}

		if (pageId != null) {
			// This is a page request by Search ID and Page ID
			bundleProvider = pagingProvider.retrieveResultList(details, thePagingAction, pageId);
			validateHaveBundleProvider(thePagingAction, bundleProvider);
		} else {
			// This is a page request by Search ID and Offset
			bundleProvider = pagingProvider.retrieveResultList(details, thePagingAction);
			validateHaveBundleProvider(thePagingAction, bundleProvider);
		}

		return createBundle(details, bundleProvider, thePagingAction);
	}

	private void validateHaveBundleProvider(String thePagingAction, IBundleProvider theBundleProvider) {
		// Return an HTTP 410 if the search is not known
		if (theBundleProvider == null) {
			ourLog.info("Client requested unknown paging ID[{}]", thePagingAction);
			String msg = fhirContext()
					.getLocalizer()
					.getMessage(PageMethodBinding.class, "unknownSearchId", thePagingAction);
			throw new ResourceGoneException(Msg.code(2313) + msg);
		}
	}

	@Override
	public <C extends IBaseConformance> C capabilities(
			Class<C> theCapabilityStatementType, Map<String, String> theHeaders) {
		var method = myRestfulServer.getServerConformanceMethod();
		if (method == null) {
			return null;
		}
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return (C) method.provideCapabilityStatement(myRestfulServer, details);
	}

	@Override
	public <B extends IBaseBundle> B transaction(B theBundle, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails).addHeaders(theHeaders).create();
		return (B) myDaoRegistry.getSystemDao().transaction(details, theBundle);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters> R invoke(
			String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
				.addHeaders(theHeaders)
				.setOperation(theName)
				.setParameters(theParameters)
				.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters> MethodOutcome invoke(
			String theName, P theParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
				.addHeaders(theHeaders)
				.setOperation(theName)
				.setParameters(theParameters)
				.create();

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> theResourceType,
			String theName,
			P theParameters,
			Class<R> theReturnType,
			Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
				.addHeaders(theHeaders)
				.setOperation(theName)
				.setResourceType(theResourceType.getSimpleName())
				.setParameters(theParameters)
				.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
			Class<T> theResourceType, String theName, P theParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
				.addHeaders(theHeaders)
				.setOperation(theName)
				.setResourceType(theResourceType.getSimpleName())
				.setParameters(theParameters)
				.create();

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I theId, String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
				.addHeaders(theHeaders)
				.setOperation(theName)
				.setResourceType(theId.getResourceType())
				.setId(theId)
				.setParameters(theParameters)
				.create();

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(
			I theId, String theName, P theParameters, Map<String, String> theHeaders) {
		var details = startWith(myRequestDetails)
				.addHeaders(theHeaders)
				.setOperation(theName)
				.setResourceType(theId.getResourceType())
				.setId(theId)
				.setParameters(theParameters)
				.create();

		return invoke(details);
	}

	private void notImplemented() {
		throw new NotImplementedOperationException(Msg.code(2314) + "history not yet implemented");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(
			P theParameters, Class<B> theReturnBundleType, Map<String, String> theHeaders) {
		notImplemented();

		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> theResourceType, P theParameters, Class<B> theReturnBundleType, Map<String, String> theHeaders) {
		notImplemented();

		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I theId, P theParameters, Class<B> theReturnBundleType, Map<String, String> theHeaders) {
		notImplemented();

		return null;
	}

	@Override
	public FhirContext fhirContext() {
		return myRestfulServer.getFhirContext();
	}

	protected <R extends Object> R invoke(RequestDetails theDetails) {
		try {
			return (R)
					myRestfulServer.determineResourceMethod(theDetails, null).invokeServer(myRestfulServer, theDetails);
		} catch (IOException e) {
			throw new RuntimeException(Msg.code(2315) + e);
		}
	}
}
