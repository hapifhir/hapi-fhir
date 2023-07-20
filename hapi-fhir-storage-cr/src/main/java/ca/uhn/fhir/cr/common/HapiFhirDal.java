/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal;

@SuppressWarnings("unchecked")
/**
 * This class leverages DaoRegistry from Hapi-fhir to implement CRUD FHIR API operations constrained to provide only the operations necessary for the cql-evaluator modules to function.
 **/
public class HapiFhirDal implements FhirDal {

	protected final DaoRegistry myDaoRegistry;
	protected final RequestDetails myRequestDetails;

	public HapiFhirDal(DaoRegistry theDaoRegistry) {
		this(theDaoRegistry, null);
	}

	public HapiFhirDal(DaoRegistry theDaoRegistry, RequestDetails theRequestDetails) {
		this.myDaoRegistry = theDaoRegistry;
		this.myRequestDetails = theRequestDetails;
	}

	@Override
	public void create(IBaseResource theResource) {
		this.myDaoRegistry.getResourceDao(theResource.fhirType()).create(theResource, myRequestDetails);
	}

	@Override
	public IBaseResource read(IIdType theId) {
		return this.myDaoRegistry.getResourceDao(theId.getResourceType()).read(theId, myRequestDetails);
	}

	@Override
	public void update(IBaseResource theResource) {
		this.myDaoRegistry.getResourceDao(theResource.fhirType()).update(theResource, myRequestDetails);
	}

	@Override
	public void delete(IIdType theId) {
		this.myDaoRegistry.getResourceDao(theId.getResourceType()).delete(theId, myRequestDetails);
	}

	// TODO: the search interfaces need some work
	@Override
	public Iterable<IBaseResource> search(String theResourceType) {
		var b = this.myDaoRegistry.getResourceDao(theResourceType).search(new SearchParameterMap(), myRequestDetails);
		return new BundleIterable(myRequestDetails, b);
	}

	@Override
	public Iterable<IBaseResource> searchByUrl(String theResourceType, String theUrl) {
		// version example "http://content.smilecdr.com/fhir/dqm/Library/ImmunizationStatusRoutine|2.0.1"
		if (theUrl.contains("|")) {
			String[] urlSplit = theUrl.split("\\|");
			String urlBase = urlSplit[0];
			String urlVersion = urlSplit[1];

			IBundleProvider versionResource = this.myDaoRegistry
					.getResourceDao(theResourceType)
					.search(
							SearchParameterMap.newSynchronous()
									.add("url", new UriParam(urlBase))
									.add("version", new TokenParam(urlVersion)),
							new SystemRequestDetails());
			return new BundleIterable(myRequestDetails, versionResource);
		} else {
			// standard example "http://content.smilecdr.com/fhir/dqm/Library/ImmunizationStatusRoutine"
			IBundleProvider standardResource = this.myDaoRegistry
					.getResourceDao(theResourceType)
					.search(
							SearchParameterMap.newSynchronous().add("url", new UriParam(theUrl)),
							new SystemRequestDetails());
			return new BundleIterable(myRequestDetails, standardResource);
		}
	}
}
