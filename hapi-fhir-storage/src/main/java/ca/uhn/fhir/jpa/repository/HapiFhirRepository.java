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
package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.repository.searchparam.SearchParameterMapRepositoryRestQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.impl.MultiMapRepositoryRestQueryBuilder;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static ca.uhn.fhir.jpa.repository.RequestDetailsCloner.startWith;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class leverages DaoRegistry from Hapi-fhir JPA server to implement IRepository.
 * This does not implement history.
 **/
@SuppressWarnings("squid:S1135")
public class HapiFhirRepository implements IRepository {
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

	/**
	 * Constructs the RequestDetails that will be used for any given call, by
	 * starting with the RequestDetails that were fed in in the constructor.
	 *
	 * This is protected to allow extenders to alter the details if default
	 * implementation is not sufficient.
	 *
	 * @param theConsumer - consumer to build up default implementations
	 * @return RequestDetails to use for this request
	 */
	protected RequestDetails createRequestDetails(Consumer<RequestDetailsCloner.DetailsBuilder> theConsumer) {
		RequestDetailsCloner.DetailsBuilder cloner = startWith(myRequestDetails);
		theConsumer.accept(cloner);
		return cloner.create();
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> T read(
			Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails((builder) -> {
			builder.addHeaders(theHeaders);
			builder.setAction(RestOperationTypeEnum.READ);
		});
		return myDaoRegistry.getResourceDao(theResourceType).read(theId, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T theResource, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setAction(RestOperationTypeEnum.CREATE);
			builder.addHeaders(theHeaders);
		});

		return myDaoRegistry.getResourceDao(theResource).create(theResource, details);
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I theId, P thePatchParameters, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setAction(RestOperationTypeEnum.PATCH);
			builder.addHeaders(theHeaders);
		});

		return myDaoRegistry
				.getResourceDao(theId.getResourceType())
				.patch(theId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, thePatchParameters, details);
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T theResource, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.addHeaders(theHeaders);
			builder.setAction(RestOperationTypeEnum.UPDATE);
		});

		DaoMethodOutcome update = myDaoRegistry.getResourceDao(theResource).update(theResource, details);
		boolean created = update.getCreated() != null && update.getCreated();
		if (created) {
			update.setResponseStatusCode(Constants.STATUS_HTTP_201_CREATED);
		} else {
			update.setResponseStatusCode(Constants.STATUS_HTTP_200_OK);
		}
		return update;
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> theResourceType, I theId, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setAction(RestOperationTypeEnum.DELETE);
			builder.addHeaders(theHeaders);
		});

		return myDaoRegistry.getResourceDao(theResourceType).delete(theId, details);
	}

	@Override
	public <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> theBundleType,
			Class<T> theResourceType,
			IRepositoryRestQueryContributor theQueryContributor,
			Map<String, String> theHeaders) {

		SearchParameterMap searchParameterMap =
				SearchParameterMapRepositoryRestQueryBuilder.buildFromQueryContributor(theQueryContributor);

		RequestDetails details = createRequestDetails(builder -> {
			builder.setAction(RestOperationTypeEnum.SEARCH_TYPE);
			builder.addHeaders(theHeaders);
			builder.setParameters(MultiMapRepositoryRestQueryBuilder.toFlatMap(searchParameterMap));
		});

		details.setResourceName(myDaoRegistry.getFhirContext().getResourceType(theResourceType));
		IBundleProvider bundleProvider =
				myDaoRegistry.getResourceDao(theResourceType).search(searchParameterMap, details);

		if (bundleProvider == null) {
			return null;
		}

		return createBundle(details, bundleProvider, null);
	}

	protected <B extends IBaseBundle> B createBundle(
			RequestDetails theRequestDetails, @Nonnull IBundleProvider theBundleProvider, String thePagingAction) {
		Integer count = RestfulServerUtils.extractCountParameter(theRequestDetails);
		String linkSelf = RestfulServerUtils.createLinkSelf(theRequestDetails.getFhirServerBase(), theRequestDetails);

		Set<Include> includes = new HashSet<>();
		String[] reqIncludes = theRequestDetails.getParameters().get(Constants.PARAM_INCLUDE);
		if (reqIncludes != null) {
			for (String nextInclude : reqIncludes) {
				includes.add(new Include(nextInclude));
			}
		}

		Integer offset = RestfulServerUtils.tryToExtractNamedParameter(theRequestDetails, Constants.PARAM_PAGINGOFFSET);
		if (offset == null || offset < 0) {
			offset = 0;
		}
		int start = offset;
		Integer size = theBundleProvider.size();
		if (size != null) {
			start = Math.max(0, Math.min(offset, size));
		}

		BundleTypeEnum bundleType;
		String[] bundleTypeValues = theRequestDetails.getParameters().get(Constants.PARAM_BUNDLETYPE);
		if (bundleTypeValues != null) {
			bundleType = BundleTypeEnum.VALUESET_BINDER.fromCodeString(bundleTypeValues[0]);
		} else {
			bundleType = BundleTypeEnum.SEARCHSET;
		}

		return unsafeCast(BundleProviderUtil.createBundleFromBundleProvider(
				myRestfulServer,
				theRequestDetails,
				count,
				linkSelf,
				includes,
				theBundleProvider,
				start,
				bundleType,
				thePagingAction
		));
	}

	// TODO: The main use case for this is paging through Bundles, but I suppose that technically
	// we ought to handle any old link. Maybe this is also an escape hatch for "custom non-FHIR
	// repository action"?
	@Override
	public <B extends IBaseBundle> B link(Class<B> theBundleType, String theUrl, Map<String, String> theHeaders) {
		UrlUtil.UrlParts urlParts = UrlUtil.parseUrl(theUrl);

		RequestDetails details = createRequestDetails(builder -> {
			builder.addHeaders(theHeaders);
			builder.setAction(RestOperationTypeEnum.GET_PAGE);
			builder.withCompleteUrl(theUrl);

			Map<String, String[]> existing = myRequestDetails.getParameters();
			Map<String, String[]> parsed = UrlUtil.parseQueryStrings(urlParts.getParams());
			// we are overwriting parameters here
			// but implementers can override so they can choose what happens if they want
			Map<String, String[]> newParams = new HashMap<>();
			newParams.putAll(existing);
			newParams.putAll(parsed);

			builder.setParameters(newParams);
		});

		IPagingProvider pagingProvider = myRestfulServer.getPagingProvider();
		if (pagingProvider == null) {
			throw new InvalidRequestException(Msg.code(2638) + "This server does not support paging");
		}

		String pagingAction = details.getParameters().get(Constants.PARAM_PAGINGACTION)[0];

		IBundleProvider bundleProvider;

		String pageId = null;
		String[] pageIdParams = details.getParameters().get(Constants.PARAM_PAGEID);
		if (pageIdParams != null && pageIdParams.length > 0 && isNotBlank(pageIdParams[0])) {
			pageId = pageIdParams[0];
		}

		if (pageId != null) {
			// This is a page request by Search ID and Page ID
			bundleProvider = pagingProvider.retrieveResultList(details, pagingAction, pageId);
			validateHaveBundleProvider(pagingAction, bundleProvider);
		} else {
			// This is a page request by Search ID and Offset
			bundleProvider = pagingProvider.retrieveResultList(details, pagingAction);
			validateHaveBundleProvider(pagingAction, bundleProvider);
		}

		return createBundle(details, bundleProvider, pagingAction);
	}

	private void validateHaveBundleProvider(String thePagingAction, IBundleProvider theBundleProvider) {
		// Return an HTTP 410 if the search is not known
		if (theBundleProvider == null) {
			ourLog.info("Client requested unknown paging ID[{}]", thePagingAction);
			String msg = fhirContext()
					.getLocalizer()
					.getMessage(PageMethodBinding.class, "unknownSearchId", thePagingAction);
			throw new ResourceGoneException(Msg.code(2639) + msg);
		}
	}

	@Override
	public <C extends IBaseConformance> C capabilities(
			Class<C> theCapabilityStatementType, Map<String, String> theHeaders) {
		ConformanceMethodBinding method = myRestfulServer.getServerConformanceMethod();
		if (method == null) {
			return null;
		}
		RequestDetails details = createRequestDetails(builder -> {
			builder.setAction(RestOperationTypeEnum.METADATA);
			builder.addHeaders(theHeaders);
		});

		return unsafeCast(method.provideCapabilityStatement(myRestfulServer, details));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <B extends IBaseBundle> B transaction(B theBundle, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.addHeaders(theHeaders);
			builder.setAction(RestOperationTypeEnum.TRANSACTION);
		});

		return unsafeCast(myDaoRegistry.getSystemDao().transaction(details, theBundle));
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters> R invoke(
			String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setRequestType(RequestTypeEnum.POST);
			builder.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
			builder.addHeaders(theHeaders);
			builder.setOperation(theName);
			builder.setRequestContents(theParameters);
		});

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters> MethodOutcome invoke(
			String theName, P theParameters, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setRequestType(RequestTypeEnum.POST);
			builder.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
			builder.addHeaders(theHeaders);
			builder.setOperation(theName);
			builder.setRequestContents(theParameters);
		});

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> theResourceType,
			String theName,
			P theParameters,
			Class<R> theReturnType,
			Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setRequestType(RequestTypeEnum.POST);
			builder.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
			builder.setOperation(theName);
			builder.setResourceType(theResourceType.getSimpleName());
			builder.setRequestContents(theParameters);
			builder.addHeaders(theHeaders);
		});

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, T extends IBaseResource> MethodOutcome invoke(
			Class<T> theResourceType, String theName, P theParameters, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setRequestType(RequestTypeEnum.POST);
			builder.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
			builder.setResourceType(theResourceType.getSimpleName());
			builder.setRequestContents(theParameters);
			builder.setOperation(theName);
		});

		return invoke(details);
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I theId, String theName, P theParameters, Class<R> theReturnType, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setRequestType(RequestTypeEnum.POST);
			builder.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
			builder.addHeaders(theHeaders);
			builder.setOperation(theName);
			builder.setResourceType(theId.getResourceType());
			builder.setId(theId);
			builder.setRequestContents(theParameters);
		});

		return invoke(details);
	}

	@Override
	public <P extends IBaseParameters, I extends IIdType> MethodOutcome invoke(
			I theId, String theName, P theParameters, Map<String, String> theHeaders) {
		RequestDetails details = createRequestDetails(builder -> {
			builder.setRequestType(RequestTypeEnum.POST);
			builder.setAction(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
			builder.addHeaders(theHeaders);
			builder.setOperation(theName);
			builder.setResourceType(theId.getResourceType());
			builder.setId(theId);
			builder.setRequestContents(theParameters);
		});

		return invoke(details);
	}

	private void notImplemented() {
		throw new NotImplementedOperationException(Msg.code(2640) + "history not yet implemented");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(
			P theParameters, Class<B> theBundleType, Map<String, String> theHeaders) {
		notImplemented();

		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> theResourceType, P theParameters, Class<B> theBundleType, Map<String, String> theHeaders) {
		notImplemented();

		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I theId, P theParameters, Class<B> theBundleType, Map<String, String> theHeaders) {
		notImplemented();

		return null;
	}

	@Override
	public @Nonnull FhirContext fhirContext() {
		return myDaoRegistry.getFhirContext();
	}

	protected <R> R invoke(RequestDetails theDetails) {
		try {
			return unsafeCast(myRestfulServer
					.determineResourceMethod(theDetails, null)
					.invokeServer(myRestfulServer, theDetails));
		} catch (IOException exception) {
			throw new InternalErrorException(Msg.code(2641) + exception);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
