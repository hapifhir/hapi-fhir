package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class JpaResourceDaoSearchParameter<T extends IBaseResource> extends BaseHapiFhirResourceDao<T> implements IFhirResourceDaoSearchParameter<T> {

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	private SearchParameterDaoValidator mySearchParameterDaoValidator;

	protected void reindexAffectedResources(T theResource, RequestDetails theRequestDetails) {
		// N.B. Don't do this on the canonicalized version
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;

		org.hl7.fhir.r5.model.SearchParameter searchParameter = myVersionCanonicalizer.searchParameterToCanonical(theResource);
		List<String> base = theResource != null ? searchParameter.getBase().stream().map(CodeType::getCode).collect(Collectors.toList()) : null;
		requestReindexForRelatedResources(reindex, base, theRequestDetails);
	}


	@Override
	protected void postPersist(ResourceTable theEntity, T theResource, RequestDetails theRequestDetails) {
		super.postPersist(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void postUpdate(ResourceTable theEntity, T theResource, RequestDetails theRequestDetails) {
		super.postUpdate(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void preDelete(T theResourceToDelete, ResourceTable theEntityToDelete, RequestDetails theRequestDetails) {
		super.preDelete(theResourceToDelete, theEntityToDelete, theRequestDetails);
		reindexAffectedResources(theResourceToDelete, theRequestDetails);
	}

	@Override
	protected void validateResourceForStorage(T theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		validateSearchParam(theResource);
	}

	public void validateSearchParam(IBaseResource theResource) {
		org.hl7.fhir.r5.model.SearchParameter searchParameter = myVersionCanonicalizer.searchParameterToCanonical(theResource);
		mySearchParameterDaoValidator.validate(searchParameter);
	}

	@VisibleForTesting
	void setVersionCanonicalizerForUnitTest(VersionCanonicalizer theVersionCanonicalizer) {
		myVersionCanonicalizer = theVersionCanonicalizer;
	}

	@VisibleForTesting
	public void setSearchParameterDaoValidatorForUnitTest(SearchParameterDaoValidator theSearchParameterDaoValidator){
		mySearchParameterDaoValidator = theSearchParameterDaoValidator;
	}
}
