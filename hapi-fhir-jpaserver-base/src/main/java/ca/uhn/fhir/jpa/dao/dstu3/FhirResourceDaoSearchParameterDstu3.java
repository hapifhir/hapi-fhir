package ca.uhn.fhir.jpa.dao.dstu3;

import static org.apache.commons.lang3.StringUtils.isBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.jpa.dao.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ElementUtil;

public class FhirResourceDaoSearchParameterDstu3 extends FhirResourceDaoDstu3<SearchParameter> implements IFhirResourceDaoSearchParameter<SearchParameter> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoSearchParameterDstu3.class);

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	protected void markAffectedResources(SearchParameter theResource) {
		if (theResource != null) {
			String expression = theResource.getExpression();
			final String resourceType = expression.substring(0, expression.indexOf('.'));
			ourLog.info("Marking all resources of type {} for reindexing due to updated search parameter with path: {}", expression);

			TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			int updatedCount = txTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus theStatus) {
					return myResourceTableDao.markResourcesOfTypeAsRequiringReindexing(resourceType);
				}
			});

			ourLog.info("Marked {} resources for reindexing", updatedCount);
		}

		mySearchParamRegistry.forceRefresh();
	}

	/**
	 * This method is called once per minute to perform any required re-indexing. During most passes this will
	 * just check and find that there are no resources requiring re-indexing. In that case the method just returns
	 * immediately. If the search finds that some resources require reindexing, the system will do multiple
	 * reindexing passes and then return.
	 */
	@Override
	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_MINUTE)
	public void performReindexingPass() {
		if (getConfig().isSchedulingDisabled()) {
			return;
		}

		Integer count = mySystemDao.performReindexingPass(100);
		for (int i = 0; i < 50 && count != null && count != 0; i++) {
			count = mySystemDao.performReindexingPass(100);
			try {
				Thread.sleep(DateUtils.MILLIS_PER_SECOND);
			} catch (InterruptedException e) {
				break;
			}
		}

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

		if (theResource.getStatus() == null) {
			throw new UnprocessableEntityException("SearchParameter.status is missing or invalid: " + theResource.getStatusElement().getValueAsString());
		}

		String expression = theResource.getExpression();
		if (isBlank(expression)) {
			throw new UnprocessableEntityException("SearchParameter.expression is missing");
		}

		if (ElementUtil.isEmpty(theResource.getBase())) {
			throw new UnprocessableEntityException("SearchParameter.base is missing");
		}

		expression = expression.trim();
		theResource.setExpression(expression);

		String[] expressionSplit = BaseSearchParamExtractor.SPLIT.split(expression);
		String allResourceName = null;
		for (String nextPath : expressionSplit) {
			nextPath = nextPath.trim();

			int dotIdx = nextPath.indexOf('.');
			if (dotIdx == -1) {
				throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + nextPath + "\". Must start with a resource name");
			}

			String resourceName = nextPath.substring(0, dotIdx);
			try {
				getContext().getResourceDefinition(resourceName);
			} catch (DataFormatException e) {
				throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + nextPath + "\": " + e.getMessage());
			}

			if (allResourceName == null) {
				allResourceName = resourceName;
			} else {
				if (!allResourceName.equals(resourceName)) {
					throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + nextPath + "\". All paths in a single SearchParameter must match the same resource type");
				}
			}

		}

	}

}
