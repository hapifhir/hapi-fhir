package ca.uhn.fhir.jpa.graphql;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
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
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.PARAM_FILTER;

public class JpaStorageServices extends BaseHapiFhirDao<IBaseResource> implements IGraphQLStorageServices {

	private static final int MAX_SEARCH_SIZE = 500;
	private static final Logger ourLog = LoggerFactory.getLogger(JpaStorageServices.class);

	private IFhirResourceDao<? extends IBaseResource> getDao(String theResourceType) {
		RuntimeResourceDefinition typeDef = getContext().getResourceDefinition(theResourceType);
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

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void listResources(Object theAppInfo, String theType, List<Argument> theSearchParams, List<IBaseResource> theMatches) throws FHIRException {

		RuntimeResourceDefinition typeDef = getContext().getResourceDefinition(theType);
		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDaoOrNull(typeDef.getImplementingClass());

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(MAX_SEARCH_SIZE);

		Map<String, RuntimeSearchParam> searchParams = mySearchParamRegistry.getActiveSearchParams(typeDef.getName());

		for (Argument nextArgument : theSearchParams) {

			if (nextArgument.getName().equals(PARAM_FILTER)) {
				String value = nextArgument.getValues().get(0).getValue();
				params.add(PARAM_FILTER, new StringParam(value));
				continue;
			}

			String searchParamName = graphqlArgumentToSearchParam(nextArgument.getName());
			RuntimeSearchParam searchParam = searchParams.get(searchParamName);
			if (searchParam == null) {
				Set<String> graphqlArguments = searchParams.keySet().stream()
					.map(this::searchParamToGraphqlArgument)
					.collect(Collectors.toSet());
				String msg = getContext().getLocalizer().getMessageSanitized(JpaStorageServices.class, "invalidGraphqlArgument", nextArgument.getName(), new TreeSet<>(graphqlArguments));
				throw new InvalidRequestException(msg);
			}

			IQueryParameterOr<?> queryParam;

			switch (searchParam.getParamType()) {
				case NUMBER:
					NumberOrListParam numberOrListParam = new NumberOrListParam();
					for (Value value: nextArgument.getValues()) {
						numberOrListParam.addOr(new NumberParam(value.getValue()));
					}
					queryParam = numberOrListParam;
					break;
				case DATE:
					DateOrListParam dateOrListParam = new DateOrListParam();
					for (Value value: nextArgument.getValues()) {
						dateOrListParam.addOr(new DateParam(value.getValue()));
					}
					queryParam = dateOrListParam;
					break;
				case STRING:
					StringOrListParam stringOrListParam = new StringOrListParam();
					for (Value value: nextArgument.getValues()) {
						stringOrListParam.addOr(new StringParam(value.getValue()));
					}
					queryParam = stringOrListParam;
					break;
				case TOKEN:
					TokenOrListParam tokenOrListParam = new TokenOrListParam();
					for (Value value: nextArgument.getValues()) {
						tokenOrListParam.addOr(new TokenParam(value.getValue()));
					}
					queryParam = tokenOrListParam;
					break;
				case REFERENCE:
					ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
					for (Value value: nextArgument.getValues()) {
						referenceOrListParam.addOr(new ReferenceParam(value.getValue()));
					}
					queryParam = referenceOrListParam;
					break;
				case QUANTITY:
					QuantityOrListParam quantityOrListParam = new QuantityOrListParam();
					for (Value value: nextArgument.getValues()) {
						quantityOrListParam.addOr(new QuantityParam(value.getValue()));
					}
					queryParam = quantityOrListParam;
					break;
				case SPECIAL:
					SpecialOrListParam specialOrListParam = new SpecialOrListParam();
					for (Value value: nextArgument.getValues()) {
						specialOrListParam.addOr(new SpecialParam().setValue(value.getValue()));
					}
					queryParam = specialOrListParam;
					break;
				default:
					throw new InvalidRequestException(String.format("%s parameters are not yet supported in GraphQL", searchParam.getParamType()));
			}

			params.add(searchParamName, queryParam);
		}

		RequestDetails requestDetails = (RequestDetails) theAppInfo;
		IBundleProvider response = dao.search(params, requestDetails);
		int size = response.size();
		if (response.preferredPageSize() != null && response.preferredPageSize() < size) {
			size = response.preferredPageSize();
		}

		theMatches.addAll(response.getResources(0, size));

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public IBaseResource lookup(Object theAppInfo, String theType, String theId) throws FHIRException {
		IIdType refId = getContext().getVersion().newIdType();
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

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public IBaseBundle search(Object theAppInfo, String theType, List<Argument> theSearchParams) throws FHIRException {
		throw new NotImplementedOperationException("Not yet able to handle this GraphQL request");
	}

	@Nullable
	@Override
	protected String getResourceName() {
		return null;
	}
}
