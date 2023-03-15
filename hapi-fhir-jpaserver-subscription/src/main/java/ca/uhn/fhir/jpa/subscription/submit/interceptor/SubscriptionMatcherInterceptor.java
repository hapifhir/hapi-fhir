package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

@Interceptor
public class SubscriptionMatcherInterceptor {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private StorageSettings myStorageSettings;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	private ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;
	@Autowired
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource, RequestDetails theRequest) {

		processResourceModifiedWithAsyncRetries(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource, RequestDetails theRequest) {

		processResourceModifiedWithAsyncRetries(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest) {
		boolean dontTriggerSubsWhenVersionsAreTheSame = !myStorageSettings.isTriggerSubscriptionsForNonVersioningChanges();
		boolean resourceVersionsAreTheSame = isSameResourceVersion(theOldResource, theNewResource);

		if (dontTriggerSubsWhenVersionsAreTheSame && resourceVersionsAreTheSame) {
			return;
		}

		processResourceModifiedWithAsyncRetries(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE, theRequest);
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	protected void processResourceModifiedWithAsyncRetries(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType, RequestDetails theRequest) {

		ResourceModifiedMessage msg = createResourceModifiedMessage(theNewResource, theOperationType, theRequest);

		// Interceptor call: SUBSCRIPTION_RESOURCE_MODIFIED
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, msg);
		boolean outcome = CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED, params);

		if (!outcome) {
			return;
		}

		ResourceModifiedEntityPK resourceModifiedEntityPK = myResourceModifiedMessagePersistenceSvc.persist(msg);

		schedulePostCommitMessageDelivery(resourceModifiedEntityPK);

	}

	private void schedulePostCommitMessageDelivery(ResourceModifiedEntityPK thePersistedResourceModifiedEntityPK) {

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public int getOrder() {
				return 0;
			}

			@Override
			public void afterCommit() {
				myResourceModifiedSubmitterSvc.processResourceModified(thePersistedResourceModifiedEntityPK);
			}
		});
	}

	private ResourceModifiedMessage createResourceModifiedMessage(IBaseResource theNewResource, BaseResourceMessage.OperationTypeEnum theOperationType, RequestDetails theRequest) {
		// Even though the resource is being written, the subscription will be interacting with it by effectively "reading" it so we set the RequestPartitionId as a read request
		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(theRequest, theNewResource.getIdElement().getResourceType(), theNewResource.getIdElement());
		return new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType, theRequest, requestPartitionId);
	}

	private boolean isSameResourceVersion(IBaseResource theOldResource, IBaseResource theNewResource) {
		if(isNull(theOldResource) || isNull(theNewResource)){
			return false;
		}

		String oldVersion = theOldResource.getIdElement().getVersionIdPart();
		String newVersion = theNewResource.getIdElement().getVersionIdPart();

		if (isBlank(oldVersion) || isBlank(newVersion)) {
			return false;
		}

		return oldVersion.equals(newVersion);

	}
	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

}
