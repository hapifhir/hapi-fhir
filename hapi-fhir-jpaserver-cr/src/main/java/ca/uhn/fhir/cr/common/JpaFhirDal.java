package ca.uhn.fhir.cr.common;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Reasoning
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal;

@SuppressWarnings("unchecked")
public class JpaFhirDal implements FhirDal {

	protected final DaoRegistry daoRegistry;
	protected final RequestDetails requestDetails;

	public JpaFhirDal(DaoRegistry daoRegistry) {
		this(daoRegistry, null);
	}

	public JpaFhirDal(DaoRegistry daoRegistry, RequestDetails requestDetails) {
		this.daoRegistry = daoRegistry;
		this.requestDetails = requestDetails;
	}

	@Override
	public void create(IBaseResource theResource) {
		this.daoRegistry.getResourceDao(theResource.fhirType()).create(theResource, requestDetails);
	}

	@Override
	public IBaseResource read(IIdType theId) {
		return this.daoRegistry.getResourceDao(theId.getResourceType()).read(theId, requestDetails);
	}

	@Override
	public void update(IBaseResource theResource) {
		this.daoRegistry.getResourceDao(theResource.fhirType()).update(theResource, requestDetails);
	}

	@Override
	public void delete(IIdType theId) {
		this.daoRegistry.getResourceDao(theId.getResourceType()).delete(theId, requestDetails);

	}

	// TODO: the search interfaces need some work
	@Override
	public Iterable<IBaseResource> search(String theResourceType) {
		return this.daoRegistry.getResourceDao(theResourceType).search(SearchParameterMap.newSynchronous(), requestDetails)
				.getAllResources();
	}

	@Override
	public Iterable<IBaseResource> searchByUrl(String theResourceType, String theUrl) {
		return this.daoRegistry.getResourceDao(theResourceType)
				.search(SearchParameterMap.newSynchronous().add("url", new UriParam(theUrl)), requestDetails).getAllResources();
	}
}
