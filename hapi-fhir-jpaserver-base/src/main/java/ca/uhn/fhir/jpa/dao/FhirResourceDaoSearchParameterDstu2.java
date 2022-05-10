package ca.uhn.fhir.jpa.dao;

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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoSearchParameterR4;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_40;
import org.springframework.beans.factory.annotation.Autowired;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirResourceDaoSearchParameterDstu2 extends BaseHapiFhirResourceDao<SearchParameter> implements IFhirResourceDaoSearchParameter<SearchParameter> {

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	private FhirContext myDstu2Hl7OrgContext = FhirContext.forDstu2Hl7Org();

	protected void markAffectedResources(SearchParameter theResource) {
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;
		String expression = theResource != null ? theResource.getXpath() : null;
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

		String encoded = getContext().newJsonParser().encodeResourceToString(theResource);
		org.hl7.fhir.dstu2.model.SearchParameter hl7Org = myDstu2Hl7OrgContext.newJsonParser().parseResource(org.hl7.fhir.dstu2.model.SearchParameter.class, encoded);

		org.hl7.fhir.r4.model.SearchParameter convertedSp = (org.hl7.fhir.r4.model.SearchParameter) VersionConvertorFactory_10_40.convertResource(hl7Org, new BaseAdvisor_10_40(false));
		if (isBlank(convertedSp.getExpression()) && isNotBlank(hl7Org.getXpath())) {
			convertedSp.setExpression(hl7Org.getXpath());
		}

		FhirResourceDaoSearchParameterR4.validateSearchParam(convertedSp, getContext(), getConfig(), mySearchParamRegistry, mySearchParamExtractor);

	}


}
