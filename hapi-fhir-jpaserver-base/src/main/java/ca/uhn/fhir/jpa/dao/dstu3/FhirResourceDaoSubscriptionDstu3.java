package ca.uhn.fhir.jpa.dao.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FhirResourceDaoSubscriptionDstu3 extends FhirResourceDaoDstu3<Subscription> implements IFhirResourceDaoSubscription<Subscription> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoSubscriptionDstu3.class);

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

	protected void validateChannelEndpoint(Subscription theResource) {
		if (isBlank(theResource.getChannel().getEndpoint())) {
			throw new UnprocessableEntityException("Rest-hook subscriptions must have Subscription.channel.endpoint defined");
		}
	}

	protected void validateChannelPayload(Subscription theResource) {
		if (!isBlank(theResource.getChannel().getPayload()) && EncodingEnum.forContentType(theResource.getChannel().getPayload()) == null) {
			throw new UnprocessableEntityException("Invalid value for Subscription.channel.payload: " + theResource.getChannel().getPayload());
		}
	}

	public RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(Subscription theResource) {
		switch (ObjectUtils.defaultIfNull(theResource.getStatus(), SubscriptionStatus.OFF)) {
			case REQUESTED:
			case ACTIVE:
				break;
			case ERROR:
			case OFF:
			case NULL:
				return null;
		}

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

		if (theResource.getChannel().getType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated");
		} else if (theResource.getChannel().getType() == SubscriptionChannelType.RESTHOOK) {
			validateChannelPayload(theResource);
			validateChannelEndpoint(theResource);
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
		if (resDef == null) {
			return;
		}

		IFhirResourceDao<? extends IBaseResource> dao = getDao(resDef.getImplementingClass());
		if (dao == null) {
			throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resDef);
		}

		if (theResource.getChannel().getType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
		}

		SubscriptionStatus status = theResource.getStatus();
		if (status == null) {
			throw new UnprocessableEntityException("Subscription.status must be populated on this server");
		}

	}

}
