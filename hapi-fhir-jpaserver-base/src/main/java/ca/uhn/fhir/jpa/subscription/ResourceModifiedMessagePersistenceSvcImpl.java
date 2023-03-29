package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.model.entity.IResourceModifiedPK;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK.with;

public class ResourceModifiedMessagePersistenceSvcImpl implements IResourceModifiedMessagePersistenceSvc {

	private FhirContext myFhirContext;

	private IResourceModifiedDao myResourceModifiedDao;

	private DaoRegistry myDaoRegistry;

	private ObjectMapper myObjectMapper;

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceModifiedMessagePersistenceSvcImpl.class);

	public ResourceModifiedMessagePersistenceSvcImpl(FhirContext theFhirContext, IResourceModifiedDao theResourceModifiedDao, DaoRegistry theDaoRegistry, ObjectMapper theObjectMapper) {
		myFhirContext = theFhirContext;
		myResourceModifiedDao = theResourceModifiedDao;
		myDaoRegistry = theDaoRegistry;
		myObjectMapper = theObjectMapper;
	}

	@Override
	public List<IResourceModifiedPK> findAllPKs() {
		return myResourceModifiedDao
			.findAll().stream()
			.map(ResourceModifiedEntity::getResourceModifiedEntityPK)
			.collect(Collectors.toList());
	}

	@Override
	public IResourceModifiedPK persist(ResourceModifiedMessage theMsg) {
		ResourceModifiedEntity resourceModifiedEntity = createEntityFrom(theMsg);
		return myResourceModifiedDao.save(resourceModifiedEntity).getResourceModifiedEntityPK();
	}

	@Override
	public ResourceModifiedMessage findByPK(IResourceModifiedPK theResourceModifiedPK) {
		Optional<ResourceModifiedEntity> optionalEntity = myResourceModifiedDao.findById((ResourceModifiedEntityPK) theResourceModifiedPK);

		if (optionalEntity.isEmpty()){
			throw new ResourceNotFoundException(String.format("%s - Could not find entity with PK %s/%s",Msg.code(2300),theResourceModifiedPK.getResourcePid(), theResourceModifiedPK.getResourceVersion()));
		}

		return inflateResourceModifiedMessageFromEntity(optionalEntity.get());
	}

	@Override
	public boolean deleteByPK(IResourceModifiedPK theResourceModifiedPK) {
		int removedCount = myResourceModifiedDao.removeById((ResourceModifiedEntityPK) theResourceModifiedPK);

		return removedCount == 1;
	}

	protected ResourceModifiedMessage inflateResourceModifiedMessageFromEntity(ResourceModifiedEntity theResourceModifiedEntity){
		String resourcePid = theResourceModifiedEntity.getResourceModifiedEntityPK().getResourcePid();
		String resourceVersion = theResourceModifiedEntity.getResourceModifiedEntityPK().getResourceVersion();
		String resourceType = theResourceModifiedEntity.getResourceType();
		ResourceModifiedMessage retVal = getPayloadLessMessageFromString(theResourceModifiedEntity.getPartialResourceModifiedMessage());
		RequestPartitionId requestPartitionId = retVal.getPartitionId();

		IdDt resourceIdDt = new IdDt(resourceType, resourcePid, resourceVersion);
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);

		IBaseResource iBaseResource = dao.read(resourceIdDt, requestPartitionId);

		retVal.setNewPayload(myFhirContext, iBaseResource);

		return retVal;
	}

	ResourceModifiedEntity createEntityFrom(ResourceModifiedMessage theMsg) {
		IIdType theMsgId = theMsg.getPayloadId(myFhirContext);

		ResourceModifiedEntity resourceModifiedEntity = new ResourceModifiedEntity();
		resourceModifiedEntity.setResourceModifiedEntityPK(with(theMsgId.getIdPart(), theMsgId.getVersionIdPart()));

		String partialModifiedMessage = getPayloadLessMessageAsString(theMsg);
		resourceModifiedEntity.setPartialResourceModifiedMessage(partialModifiedMessage);
		resourceModifiedEntity.setResourceType(theMsgId.getResourceType());
		resourceModifiedEntity.setCreatedTime(new Date());

		return resourceModifiedEntity;
	}

	private ResourceModifiedMessage getPayloadLessMessageFromString(String thePayloadLessMessage){
		try {
			return myObjectMapper.readValue(thePayloadLessMessage, ResourceModifiedMessage.class);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException("Failed to json deserialize payloadless  message", e);
		}
	}

	private String getPayloadLessMessageAsString(ResourceModifiedMessage theMsg) {
		ResourceModifiedMessage tempMessage = new ResourceModifiedMessage();
		tempMessage.setPayloadId(theMsg.getPayloadId(myFhirContext));
		tempMessage.setSubscriptionId(theMsg.getSubscriptionId());
		tempMessage.setMediaType(theMsg.getMediaType());
		tempMessage.setOperationType(theMsg.getOperationType());
		tempMessage.setPartitionId(theMsg.getPartitionId());
		tempMessage.setTransactionId(theMsg.getTransactionId());
		tempMessage.setMessageKey(theMsg.getMessageKeyOrNull());
		tempMessage.copyAdditionalPropertiesFrom(theMsg);

		try {
			return myObjectMapper.writeValueAsString(tempMessage);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException("Failed to json serialize payloadless  ResourceModifiedMessage", e);
		}
	}

}
