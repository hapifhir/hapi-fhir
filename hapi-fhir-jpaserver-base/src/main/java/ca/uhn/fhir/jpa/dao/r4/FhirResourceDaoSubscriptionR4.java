package ca.uhn.fhir.jpa.dao.r4;

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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class FhirResourceDaoSubscriptionR4 extends BaseHapiFhirResourceDao<Subscription> implements IFhirResourceDaoSubscription<Subscription> {

	@Autowired
	private ISubscriptionTableDao mySubscriptionTableDao;

	private void createSubscriptionTable(ResourceTable theEntity, Subscription theSubscription) {
		SubscriptionTable subscriptionEntity = new SubscriptionTable();
		subscriptionEntity.setCreated(new Date());
		subscriptionEntity.setSubscriptionResource(theEntity);
		myEntityManager.persist(subscriptionEntity);
	}

	@Override
	public Long getSubscriptionTablePidForSubscriptionResource(IIdType theId, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		ResourceTable entity = readEntityLatestVersion(theId, theRequest, theTransactionDetails);
		SubscriptionTable table = mySubscriptionTableDao.findOneByResourcePid(entity.getId());
		if (table == null) {
			return null;
		}
		return table.getId();
	}


	@Override
	protected void postPersist(ResourceTable theEntity, Subscription theSubscription, RequestDetails theRequestDetails) {
		super.postPersist(theEntity, theSubscription, theRequestDetails);

		createSubscriptionTable(theEntity, theSubscription);
	}


	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing, boolean theUpdateVersion,
												 TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theTransactionDetails, theForceUpdate, theCreateNewHistoryEntry);

		if (theDeletedTimestampOrNull != null) {
			Long subscriptionId = getSubscriptionTablePidForSubscriptionResource(theEntity.getIdDt(), theRequest, theTransactionDetails);
			if (subscriptionId != null) {
				mySubscriptionTableDao.deleteAllForSubscription(retVal);
			}
		}

		return retVal;
	}

}
