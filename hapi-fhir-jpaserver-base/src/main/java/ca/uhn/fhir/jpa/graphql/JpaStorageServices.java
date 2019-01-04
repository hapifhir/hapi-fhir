package ca.uhn.fhir.jpa.graphql;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.utils.GraphQLEngine;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class JpaStorageServices extends BaseHapiFhirDao<IBaseResource> implements GraphQLEngine.IGraphQLStorageServices {


	private IFhirResourceDao<? extends IBaseResource> getDao(String theResourceType) {
		RuntimeResourceDefinition typeDef = getContext().getResourceDefinition(theResourceType);
		return getDao(typeDef.getImplementingClass());
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void listResources(Object theAppInfo, String theType, List<Argument> theSearchParams, List<Resource> theMatches) throws FHIRException {

		RuntimeResourceDefinition typeDef = getContext().getResourceDefinition(theType);
		IFhirResourceDao<? extends IBaseResource> dao = getDao(typeDef.getImplementingClass());

		SearchParameterMap params = new SearchParameterMap();

		for (Argument nextArgument : theSearchParams) {

			RuntimeSearchParam searchParam = mySearchParamRegistry.getSearchParamByName(typeDef, nextArgument.getName());

			for (Value nextValue : nextArgument.getValues()) {
				String value = nextValue.getValue();

				IQueryParameterType param = null;
				switch (searchParam.getParamType()) {
					case NUMBER:
						param = new NumberParam(value);
						break;
					case DATE:
						param = new DateParam(value);
						break;
					case STRING:
						param = new StringParam(value);
						break;
					case TOKEN:
						param = new TokenParam(null, value);
						break;
					case REFERENCE:
						param = new ReferenceParam(value);
						break;
					case COMPOSITE:
						throw new InvalidRequestException("Composite parameters are not yet supported in GraphQL");
					case QUANTITY:
						param = new QuantityParam(value);
						break;
					case URI:
						break;
					case HAS:
						break;
				}

				params.add(nextArgument.getName(), param);
			}
		}

		IBundleProvider response = dao.search(params);
		int size = response.size();
		if (response.preferredPageSize() != null && response.preferredPageSize() < size) {
			size = response.preferredPageSize();
		}

		for (IBaseResource next : response.getResources(0, size)) {
			theMatches.add((Resource) next);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Resource lookup(Object theAppInfo, String theType, String theId) throws FHIRException {
		IIdType refId = getContext().getVersion().newIdType();
		refId.setValue(theType + "/" + theId);
		IFhirResourceDao<? extends IBaseResource> dao = getDao(theType);
		BaseHasResource id = dao.readEntity(refId);

		return (Resource) toResource(id, false);
	}

	@Override
	public ReferenceResolution lookup(Object appInfo, Resource context, Reference reference) throws FHIRException {
		return null;
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public Bundle search(Object theAppInfo, String theType, List<Argument> theSearchParams) throws FHIRException {
		throw new NotImplementedOperationException("Not yet able to handle this GraphQL request");
	}
}
