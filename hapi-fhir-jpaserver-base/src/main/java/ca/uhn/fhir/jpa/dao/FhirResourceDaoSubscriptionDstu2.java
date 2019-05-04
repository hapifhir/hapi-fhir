package ca.uhn.fhir.jpa.dao;

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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FhirResourceDaoSubscriptionDstu2 extends FhirResourceDaoDstu2<Subscription> implements IFhirResourceDaoSubscription<Subscription> {

	@Autowired
	private ISubscriptionTableDao mySubscriptionTableDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	private void createSubscriptionTable(ResourceTable theEntity, Subscription theSubscription) {
		SubscriptionTable subscriptionEntity = new SubscriptionTable();
		subscriptionEntity.setCreated(new Date());
		subscriptionEntity.setSubscriptionResource(theEntity);
		myEntityManager.persist(subscriptionEntity);
	}

	@Override
	public Long getSubscriptionTablePidForSubscriptionResource(IIdType theId) {
		ResourceTable entity = readEntityLatestVersion(theId);
		SubscriptionTable table = mySubscriptionTableDao.findOneByResourcePid(entity.getId());
		if (table == null) {
			return null;
		}
		return table.getId();
	}


	@Override
	protected void postPersist(ResourceTable theEntity, Subscription theSubscription) {
		super.postPersist(theEntity, theSubscription);

		createSubscriptionTable(theEntity, theSubscription);
	}


	@Override
	protected ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing, boolean theUpdateVersion,
													 Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		if (theDeletedTimestampOrNull != null) {
			mySubscriptionTableDao.deleteAllForSubscription(theEntity);
		}
		return retVal;
	}

	public RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(Subscription theResource) {
		String query = theResource.getCriteria();
		if (isBlank(query)) {
			throw new UnprocessableEntityException("Subscription.criteria must be populated");
		}

		int sep = query.indexOf('?');
		if (sep <= 1) {
			throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
		}

		String resType = query.substring(0, sep);
		if (resType.contains("/")) {
			throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
		}

		RuntimeResourceDefinition resDef;
		try {
			resDef = getContext().getResourceDefinition(resType);
		} catch (DataFormatException e) {
			throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resType);
		}
		return resDef;
	}

	@Override
	protected void validateResourceForStorage(Subscription theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		RuntimeResourceDefinition resDef = validateCriteriaAndReturnResourceDefinition(theResource);

		IFhirResourceDao<? extends IBaseResource> dao = getDao(resDef.getImplementingClass());
		if (dao == null) {
			throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resDef);
		}

		if (theResource.getChannel().getType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
		}

		SubscriptionStatusEnum status = theResource.getStatusElement().getValueAsEnum();
		if (status == null) {
			throw new UnprocessableEntityException("Subscription.status must be populated on this server");
		}

	}

}
