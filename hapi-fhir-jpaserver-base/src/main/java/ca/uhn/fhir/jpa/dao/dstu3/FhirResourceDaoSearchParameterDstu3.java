package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoSearchParameterR4;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.dstu3.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class FhirResourceDaoSearchParameterDstu3 extends FhirResourceDaoDstu3<SearchParameter> implements IFhirResourceDaoSearchParameter<SearchParameter> {

	protected void markAffectedResources(SearchParameter theResource) {
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;
		String expression = theResource != null ? theResource.getExpression() : null;
		markResourcesMatchingExpressionAsNeedingReindexing(reindex, expression);
	}


	@Override
	protected void postPersist(ResourceTable theEntity, SearchParameter theResource) {
		super.postPersist(theEntity, theResource);
		markAffectedResources(theResource);
	}

	@Override
	protected void postUpdate(ResourceTable theEntity, SearchParameter theResource) {
		super.postUpdate(theEntity, theResource);
		markAffectedResources(theResource);
	}

	@Override
	protected void preDelete(SearchParameter theResourceToDelete, ResourceTable theEntityToDelete) {
		super.preDelete(theResourceToDelete, theEntityToDelete);
		markAffectedResources(theResourceToDelete);
	}

	@Override
	protected void validateResourceForStorage(SearchParameter theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		Enum<?> status = theResource.getStatus();
		List<CodeType> base = theResource.getBase();
		String expression = theResource.getExpression();
		FhirContext context = getContext();
		Enumerations.SearchParamType type = theResource.getType();

		FhirResourceDaoSearchParameterR4.validateSearchParam(type, status, base, expression, context, getConfig());
	}

}
