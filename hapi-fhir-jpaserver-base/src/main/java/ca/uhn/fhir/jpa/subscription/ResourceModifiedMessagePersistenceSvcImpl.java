package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessagePK;
import ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.async.AsyncResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK.with;

/**
 * This implementer provides the capability to persist subscription messages for asynchronous submission
 * to the subscription processing pipeline with the purpose of offering a retry mechanism
 * upon submission failure (see @link {@link AsyncResourceModifiedSubmitterSvc}).
 */
public class ResourceModifiedMessagePersistenceSvcImpl implements IResourceModifiedMessagePersistenceSvc {

	private final FhirContext myFhirContext;

	private final IResourceModifiedDao myResourceModifiedDao;

	private final DaoRegistry myDaoRegistry;

	private final ObjectMapper myObjectMapper;

	private final HapiTransactionService myHapiTransactionService;

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceModifiedMessagePersistenceSvcImpl.class);

	public ResourceModifiedMessagePersistenceSvcImpl(
			FhirContext theFhirContext,
			IResourceModifiedDao theResourceModifiedDao,
			DaoRegistry theDaoRegistry,
			HapiTransactionService theHapiTransactionService) {
		myFhirContext = theFhirContext;
		myResourceModifiedDao = theResourceModifiedDao;
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myObjectMapper = new ObjectMapper();
	}

	@Override
	public Page<IPersistedResourceModifiedMessage> findAllOrderedByCreatedTime(Pageable thePageable) {
		return myHapiTransactionService.withSystemRequest().execute(() -> {
			return myResourceModifiedDao.findAllOrderedByCreatedTime(thePageable);
		});
	}

	@Override
	public IPersistedResourceModifiedMessage persist(ResourceModifiedMessage theMsg) {
		ResourceModifiedEntity resourceModifiedEntity = createEntityFrom(theMsg);
		return myResourceModifiedDao.save(resourceModifiedEntity);
	}

	@Override
	public ResourceModifiedMessage inflatePersistedResourceModifiedMessage(
			ResourceModifiedMessage theResourceModifiedMessage) {

		return inflateResourceModifiedMessageFromEntity(createEntityFrom(theResourceModifiedMessage));
	}

	@Override
	public Optional<ResourceModifiedMessage> inflatePersistedResourceModifiedMessageOrNull(
			ResourceModifiedMessage theResourceModifiedMessage) {
		ResourceModifiedMessage inflatedResourceModifiedMessage = null;

		try {
			inflatedResourceModifiedMessage = inflatePersistedResourceModifiedMessage(theResourceModifiedMessage);
		} catch (ResourceNotFoundException e) {
			IdDt idDt = new IdDt(
					theResourceModifiedMessage.getResourceType(myFhirContext),
					theResourceModifiedMessage.getPayloadId(),
					theResourceModifiedMessage.getPayloadVersion());

			ourLog.warn("Scheduled submission will be ignored since resource {} cannot be found", idDt.getIdPart(), e);
		} catch (Exception ex) {
			ourLog.error("Unknown error encountered on inflation of resources.", ex);
		}

		return Optional.ofNullable(inflatedResourceModifiedMessage);
	}

	@Override
	public ResourceModifiedMessage createResourceModifiedMessageFromEntityWithoutInflation(
			IPersistedResourceModifiedMessage thePersistedResourceModifiedMessage) {
		ResourceModifiedMessage resourceModifiedMessage = getPayloadLessMessageFromString(
				((ResourceModifiedEntity) thePersistedResourceModifiedMessage).getSummaryResourceModifiedMessage());

		IdDt resourceId =
				createIdDtFromResourceModifiedEntity((ResourceModifiedEntity) thePersistedResourceModifiedMessage);
		resourceModifiedMessage.setPayloadId(resourceId);

		return resourceModifiedMessage;
	}

	@Override
	public long getMessagePersistedCount() {
		return myResourceModifiedDao.count();
	}

	@Override
	public boolean deleteByPK(IPersistedResourceModifiedMessagePK theResourceModifiedPK) {
		int removedCount =
				myResourceModifiedDao.removeById((PersistedResourceModifiedMessageEntityPK) theResourceModifiedPK);

		return removedCount == 1;
	}

	protected ResourceModifiedMessage inflateResourceModifiedMessageFromEntity(
			ResourceModifiedEntity theResourceModifiedEntity) {
		String resourceType = theResourceModifiedEntity.getResourceType();
		ResourceModifiedMessage retVal =
				getPayloadLessMessageFromString(theResourceModifiedEntity.getSummaryResourceModifiedMessage());
		SystemRequestDetails systemRequestDetails =
				new SystemRequestDetails().setRequestPartitionId(retVal.getPartitionId());
		// Consent has already been checked when the resource was created/updated, so bypass it this time
		ConsentInterceptor.skipAllConsentForRequest(systemRequestDetails);

		IdDt resourceIdDt = createIdDtFromResourceModifiedEntity(theResourceModifiedEntity);
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

		IBaseResource iBaseResource = dao.read(resourceIdDt, systemRequestDetails, true);

		retVal.setNewPayload(myFhirContext, iBaseResource);

		return retVal;
	}

	ResourceModifiedEntity createEntityFrom(ResourceModifiedMessage theMsg) {
		IIdType theMsgId = theMsg.getPayloadId(myFhirContext);

		ResourceModifiedEntity resourceModifiedEntity = new ResourceModifiedEntity();
		resourceModifiedEntity.setResourceModifiedEntityPK(
				with(theMsgId.getIdPart(), theMsgId.getVersionIdPart(), theMsgId.getResourceType()));

		String partialModifiedMessage = getPayloadLessMessageAsString(theMsg);
		resourceModifiedEntity.setSummaryResourceModifiedMessage(partialModifiedMessage);
		resourceModifiedEntity.setCreatedTime(new Date());

		return resourceModifiedEntity;
	}

	private ResourceModifiedMessage getPayloadLessMessageFromString(String thePayloadLessMessage) {
		try {
			return myObjectMapper.readValue(thePayloadLessMessage, ResourceModifiedMessage.class);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException(Msg.code(2334) + "Failed to json deserialize payloadless  message", e);
		}
	}

	private String getPayloadLessMessageAsString(ResourceModifiedMessage theMsg) {
		ResourceModifiedMessage tempMessage = new PayloadLessResourceModifiedMessage(theMsg);

		try {
			return myObjectMapper.writeValueAsString(tempMessage);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException(Msg.code(2335) + "Failed to serialize empty ResourceModifiedMessage", e);
		}
	}

	private IdDt createIdDtFromResourceModifiedEntity(ResourceModifiedEntity theResourceModifiedEntity) {
		String resourcePid =
				theResourceModifiedEntity.getResourceModifiedEntityPK().getResourcePid();
		String resourceVersion =
				theResourceModifiedEntity.getResourceModifiedEntityPK().getResourceVersion();
		String resourceType = theResourceModifiedEntity.getResourceType();

		return new IdDt(resourceType, resourcePid, resourceVersion);
	}

	private static class PayloadLessResourceModifiedMessage extends ResourceModifiedMessage {

		public PayloadLessResourceModifiedMessage(ResourceModifiedMessage theMsg) {
			this.myPayloadId = theMsg.getPayloadId();
			this.myPayloadVersion = theMsg.getPayloadVersion();
			setSubscriptionId(theMsg.getSubscriptionId());
			setMediaType(theMsg.getMediaType());
			setOperationType(theMsg.getOperationType());
			setPartitionId(theMsg.getPartitionId());
			setTransactionId(theMsg.getTransactionId());
			setPayloadMessageKey(theMsg.getPayloadMessageKey());
			copyAdditionalPropertiesFrom(theMsg);
		}
	}
}
