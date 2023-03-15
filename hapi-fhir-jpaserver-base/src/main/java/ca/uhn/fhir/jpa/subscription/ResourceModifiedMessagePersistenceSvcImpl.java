package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

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

	public ResourceModifiedMessagePersistenceSvcImpl(FhirContext theFhirContext, IResourceModifiedDao theResourceModifiedDao, DaoRegistry theDaoRegistry, ObjectMapper theObjectMapper) {
		myFhirContext = theFhirContext;
		myResourceModifiedDao = theResourceModifiedDao;
		myDaoRegistry = theDaoRegistry;
		myObjectMapper = theObjectMapper;
	}

	@Override
	public List<ResourceModifiedEntityPK> findAllIds() {
		return myResourceModifiedDao
			.findAll().stream()
			.map(ResourceModifiedEntity::getResourceModifiedEntityPK)
			.collect(Collectors.toList());
	}

	@Override
	public ResourceModifiedEntityPK persist(ResourceModifiedMessage theMsg) {
		ResourceModifiedEntity resourceModifiedEntity = createEntityFrom(theMsg);
		return myResourceModifiedDao.save(resourceModifiedEntity).getResourceModifiedEntityPK();
	}

	@Override
	public Optional<ResourceModifiedMessage> findById(ResourceModifiedEntityPK theResourceModifiedEntityPK) {
		Optional<ResourceModifiedEntity> optionalEntity = myResourceModifiedDao.findById(theResourceModifiedEntityPK);

		if (optionalEntity.isEmpty()){
			return Optional.empty();
		}

		return inflateResourceModifiedMessageFromEntity(optionalEntity.get());
	}

	@Override
	public boolean deleteById(ResourceModifiedEntityPK theResourceModifiedEntityPK) {
		boolean retVal = false;
		if(myResourceModifiedDao.existsById(theResourceModifiedEntityPK)) {
			myResourceModifiedDao.deleteById(theResourceModifiedEntityPK);
			retVal = true;
		}

		return retVal;
	}

	public Optional<ResourceModifiedMessage> inflateResourceModifiedMessageFromEntity(ResourceModifiedEntity theResourceModifiedEntity){
		long resourcePid = theResourceModifiedEntity.getResourceModifiedEntityPK().getResourcePid();
		long resourceVersion = theResourceModifiedEntity.getResourceModifiedEntityPK().getResourceVersion();
		String resourceType = theResourceModifiedEntity.getResourceType();
		ResourceModifiedMessage retVal = getPayloadLessMessageFromString(theResourceModifiedEntity.getPartialResourceModifiedMessage());
		RequestPartitionId partitionId = retVal.getPartitionId();

		org.hl7.fhir.r4.model.IdType resourceId = new org.hl7.fhir.r4.model.IdType(resourceType, Long.toString(resourcePid), Long.toString(resourceVersion));
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceId.getResourceType());

		IBaseResource baseResource = dao.read(resourceId, partitionId);

		retVal.setNewPayload(myFhirContext, baseResource);

		return Optional.of(retVal);
	}

	@Override
	public ResourceModifiedMessage inflateResourceModifiedMessageFromPK(ResourceModifiedEntityPK theResourceModifiedEntityPK) {
		return null;
	}

	ResourceModifiedEntity createEntityFrom(ResourceModifiedMessage theMsg) {
		IIdType theMsgId = theMsg.getPayloadId(myFhirContext);

		ResourceModifiedEntity resourceModifiedEntity = new ResourceModifiedEntity();
		resourceModifiedEntity.setResourceModifiedEntityPK(with(theMsgId.getIdPartAsLong(), theMsgId.getVersionIdPartAsLong()));

		String partialModifiedMessage = getPayloadLessMessageAsString(theMsg);
		resourceModifiedEntity.setPartialResourceModifiedMessage(partialModifiedMessage);
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
			throw new ConfigurationException("Failed to json serialize payloadless", e);
		}
	}

}
