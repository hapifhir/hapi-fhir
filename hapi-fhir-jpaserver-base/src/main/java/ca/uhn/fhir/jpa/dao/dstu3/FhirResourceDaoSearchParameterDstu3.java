package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoSearchParameterR4;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.SearchParameter;
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

public class FhirResourceDaoSearchParameterDstu3 extends BaseHapiFhirResourceDao<SearchParameter> implements IFhirResourceDaoSearchParameter<SearchParameter> {

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	protected void reindexAffectedResources(SearchParameter theResource, RequestDetails theRequestDetails) {
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;
		String expression = theResource != null ? theResource.getExpression() : null;
		List<String> base = theResource != null ? theResource.getBase().stream().map(PrimitiveType::asStringValue).collect(Collectors.toList()) : null;
		requestReindexForRelatedResources(reindex, base, theRequestDetails);
	}


	@Override
	protected void postPersist(ResourceTable theEntity, SearchParameter theResource, RequestDetails theRequestDetails) {
		super.postPersist(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void postUpdate(ResourceTable theEntity, SearchParameter theResource, RequestDetails theRequestDetails) {
		super.postUpdate(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void preDelete(SearchParameter theResourceToDelete, ResourceTable theEntityToDelete, RequestDetails theRequestDetails) {
		super.preDelete(theResourceToDelete, theEntityToDelete, theRequestDetails);
		reindexAffectedResources(theResourceToDelete, theRequestDetails);
	}

	@Override
	protected void validateResourceForStorage(SearchParameter theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		org.hl7.fhir.r4.model.SearchParameter resource = (org.hl7.fhir.r4.model.SearchParameter) VersionConvertorFactory_30_40.convertResource(theResource, new BaseAdvisor_30_40(false));

		FhirResourceDaoSearchParameterR4.validateSearchParam(
			resource,
			getContext(), getConfig(), mySearchParamRegistry, mySearchParamExtractor);
	}

}
