package ca.uhn.fhir.jpa.graphql;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberOrListParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityOrListParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialOrListParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.graphql.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_FILTER;

public class DaoRegistryGraphQLStorageServices implements IGraphQLStorageServices {

	// the constant hasn't already been defined in org.hl7.fhir.core so we define it here
	static final String SEARCH_ID_PARAM = "search-id";
	static final String SEARCH_OFFSET_PARAM = "search-offset";

	private static final int MAX_SEARCH_SIZE = 500;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;
	@Autowired
	private IPagingProvider myPagingProvider;

	private IFhirResourceDao<? extends IBaseResource> getDao(String theResourceType) {
		RuntimeResourceDefinition typeDef = myContext.getResourceDefinition(theResourceType);
		return myDaoRegistry.getResourceDaoOrNull(typeDef.getImplementingClass());
	}

	private String graphqlArgumentToSearchParam(String name) {
		if (name.startsWith("_")) {
			return name;
		} else {
			return name.replaceAll("_", "-");
		}
	}

	private String searchParamToGraphqlArgument(String name) {
		return name.replaceAll("-", "_");
	}

	private SearchParameterMap buildSearchParams(String theType, List<Argument> theSearchParams) {
		List<Argument> resourceSearchParam = theSearchParams.stream()
			.filter(it -> !PARAM_COUNT.equals(it.getName()))
			.collect(Collectors.toList());

		FhirContext fhirContext = myContext;
		RuntimeResourceDefinition typeDef = fhirContext.getResourceDefinition(theType);

		SearchParameterMap params = new SearchParameterMap();
		ResourceSearchParams searchParams = mySearchParamRegistry.getActiveSearchParams(typeDef.getName());

		for (Argument nextArgument : resourceSearchParam) {

			if (nextArgument.getName().equals(PARAM_FILTER)) {
				String value = nextArgument.getValues().get(0).getValue();
				params.add(PARAM_FILTER, new StringParam(value));
				continue;
			}

			String searchParamName = graphqlArgumentToSearchParam(nextArgument.getName());
			RuntimeSearchParam searchParam = searchParams.get(searchParamName);
			if (searchParam == null) {
				Set<String> graphqlArguments = searchParams.getSearchParamNames().stream()
					.map(this::searchParamToGraphqlArgument)
					.collect(Collectors.toSet());
				String msg = myContext.getLocalizer().getMessageSanitized(DaoRegistryGraphQLStorageServices.class, "invalidGraphqlArgument", nextArgument.getName(), new TreeSet<>(graphqlArguments));
				throw new InvalidRequestException(Msg.code(1275) + msg);
			}

			IQueryParameterOr<?> queryParam;

			switch (searchParam.getParamType()) {
				case NUMBER:
					NumberOrListParam numberOrListParam = new NumberOrListParam();
					for (Value value : nextArgument.getValues()) {
						numberOrListParam.addOr(new NumberParam(value.getValue()));
					}
					queryParam = numberOrListParam;
					break;
				case DATE:
					DateOrListParam dateOrListParam = new DateOrListParam();
					for (Value value : nextArgument.getValues()) {
						dateOrListParam.addOr(new DateParam(value.getValue()));
					}
					queryParam = dateOrListParam;
					break;
				case STRING:
					StringOrListParam stringOrListParam = new StringOrListParam();
					for (Value value : nextArgument.getValues()) {
						stringOrListParam.addOr(new StringParam(value.getValue()));
					}
					queryParam = stringOrListParam;
					break;
				case TOKEN:
					TokenOrListParam tokenOrListParam = new TokenOrListParam();
					for (Value value : nextArgument.getValues()) {
						TokenParam tokenParam = new TokenParam();
						tokenParam.setValueAsQueryToken(fhirContext, searchParamName, null, value.getValue());
						tokenOrListParam.addOr(tokenParam);
					}
					queryParam = tokenOrListParam;
					break;
				case REFERENCE:
					ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
					for (Value value : nextArgument.getValues()) {
						referenceOrListParam.addOr(new ReferenceParam(value.getValue()));
					}
					queryParam = referenceOrListParam;
					break;
				case QUANTITY:
					QuantityOrListParam quantityOrListParam = new QuantityOrListParam();
					for (Value value : nextArgument.getValues()) {
						quantityOrListParam.addOr(new QuantityParam(value.getValue()));
					}
					queryParam = quantityOrListParam;
					break;
				case SPECIAL:
					SpecialOrListParam specialOrListParam = new SpecialOrListParam();
					for (Value value : nextArgument.getValues()) {
						specialOrListParam.addOr(new SpecialParam().setValue(value.getValue()));
					}
					queryParam = specialOrListParam;
					break;
				case COMPOSITE:
				case URI:
				case HAS:
				default:
					throw new InvalidRequestException(Msg.code(1276) + String.format("%s parameters are not yet supported in GraphQL", searchParam.getParamType()));
			}

			params.add(searchParamName, queryParam);
		}

		return params;
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void listResources(Object theAppInfo, String theType, List<Argument> theSearchParams, List<IBaseResource> theMatches) throws FHIRException {
		SearchParameterMap params = buildSearchParams(theType, theSearchParams);
		params.setLoadSynchronousUpTo(MAX_SEARCH_SIZE);

		RequestDetails requestDetails = (RequestDetails) theAppInfo;
		IBundleProvider response = getDao(theType).search(params, requestDetails);
		Integer size = response.size();
		//We set size to null in SearchCoordinatorSvcImpl.executeQuery() if matching results exceeds count
		//so don't throw here
		if ((response.preferredPageSize() != null && size != null && response.preferredPageSize() < size) ||
			size == null) {
			size = response.preferredPageSize();
		}

		Validate.notNull(size, "size is null");
		theMatches.addAll(response.getResources(0, size));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public IBaseResource lookup(Object theAppInfo, String theType, String theId) throws FHIRException {
		IIdType refId = myContext.getVersion().newIdType();
		refId.setValue(theType + "/" + theId);
		return lookup(theAppInfo, refId);
	}

	private IBaseResource lookup(Object theAppInfo, IIdType theRefId) {
		IFhirResourceDao<? extends IBaseResource> dao = getDao(theRefId.getResourceType());
		RequestDetails requestDetails = (RequestDetails) theAppInfo;
		return dao.read(theRefId, requestDetails, false);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ReferenceResolution lookup(Object theAppInfo, IBaseResource theContext, IBaseReference theReference) throws FHIRException {
		IBaseResource outcome = lookup(theAppInfo, theReference.getReferenceElement());
		if (outcome == null) {
			return null;
		}
		return new ReferenceResolution(theContext, outcome);
	}

	private Optional<String> getArgument(List<Argument> params, String name) {
		return params.stream()
			.filter(it -> name.equals(it.getName()))
			.map(it -> it.getValues().get(0).getValue())
			.findAny();
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public IBaseBundle search(Object theAppInfo, String theType, List<Argument> theSearchParams) throws FHIRException {
		RequestDetails requestDetails = (RequestDetails) theAppInfo;

		Optional<String> searchIdArgument = getArgument(theSearchParams, SEARCH_ID_PARAM);
		Optional<String> searchOffsetArgument = getArgument(theSearchParams, SEARCH_OFFSET_PARAM);

		String searchId;
		int searchOffset;
		int pageSize;
		IBundleProvider response;

		if (searchIdArgument.isPresent() && searchOffsetArgument.isPresent()) {
			searchId = searchIdArgument.get();
			searchOffset = Integer.parseInt(searchOffsetArgument.get());

			response = Optional.ofNullable(myPagingProvider.retrieveResultList(requestDetails, searchId)).orElseThrow(()->{
				String msg = myContext.getLocalizer().getMessageSanitized(DaoRegistryGraphQLStorageServices.class, "invalidGraphqlCursorArgument", searchId);
				return new InvalidRequestException(Msg.code(2076) + msg);
			});

			pageSize = Optional.ofNullable(response.preferredPageSize())
				.orElseGet(myPagingProvider::getDefaultPageSize);
		} else {
			pageSize = getArgument(theSearchParams, "_count").map(Integer::parseInt)
				.orElseGet(myPagingProvider::getDefaultPageSize);

			SearchParameterMap params = buildSearchParams(theType, theSearchParams);
			params.setCount(pageSize);

			CacheControlDirective cacheControlDirective = new CacheControlDirective();
			cacheControlDirective.parse(requestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL));

			RequestPartitionId requestPartitionId = myPartitionHelperSvc.determineReadPartitionForRequestForSearchType(requestDetails, theType, params, null);
			response = mySearchCoordinatorSvc.registerSearch(getDao(theType), params, theType, cacheControlDirective, requestDetails, requestPartitionId);

			searchOffset = 0;
			searchId = myPagingProvider.storeResultList(requestDetails, response);
		}


		// response.size() may return {@literal null}, in that case use pageSize
		String serverBase = requestDetails.getFhirServerBase();
		Optional<Integer> numTotalResults = Optional.ofNullable(response.size());
		int numToReturn = numTotalResults.map(integer -> Math.min(pageSize, integer - searchOffset)).orElse(pageSize);

		BundleLinks links = new BundleLinks(requestDetails.getServerBaseForRequest(), null, RestfulServerUtils.prettyPrintResponse(requestDetails.getServer(), requestDetails), BundleTypeEnum.SEARCHSET);

		// RestfulServerUtils.createLinkSelf not suitable here
		String linkFormat = "%s/%s?_format=application/json&search-id=%s&search-offset=%d&_count=%d";

		String linkSelf = String.format(linkFormat, serverBase, theType, searchId, searchOffset, pageSize);
		links.setSelf(linkSelf);

		boolean hasNext = numTotalResults.map(total -> (searchOffset + numToReturn) < total).orElse(true);

		if (hasNext) {
			String linkNext = String.format(linkFormat, serverBase, theType, searchId, searchOffset+numToReturn, pageSize);
			links.setNext(linkNext);
		}

		if (searchOffset > 0) {
			String linkPrev = String.format(linkFormat, serverBase, theType, searchId, Math.max(0, searchOffset-pageSize), pageSize);
			links.setPrev(linkPrev);
		}

		List<IBaseResource> resourceList = response.getResources(searchOffset, numToReturn + searchOffset);

		IVersionSpecificBundleFactory bundleFactory = myContext.newBundleFactory();
		bundleFactory.addRootPropertiesToBundle(response.getUuid(), links, response.size(), response.getPublished());
		bundleFactory.addResourcesToBundle(resourceList, BundleTypeEnum.SEARCHSET, serverBase, null, null);

		IBaseResource result = bundleFactory.getResourceBundle();
		return (IBaseBundle) result;
	}

}
