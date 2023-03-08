package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.ISubscriptionMessagePersistence;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class SubscriptionMessagePersistenceImpl implements ISubscriptionMessagePersistence {

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IResourceModifiedDao myResourceModifiedDao;

	@Override
	public String save(ResourceModifiedMessage theMsg) {

		ResourceModifiedEntity resourceModifiedEntity = createEntityFrom(theMsg);
		Long id = myResourceModifiedDao.save(resourceModifiedEntity).getId();

		return Long.toString(id);

	}

	@Override
	public List<Long> findAllIds() {
		// fix me
		return null;
	}

	@Override
	public ResourceModifiedMessage findById(Long theAnId) {
		// fix me
		return null;
	}

	ResourceModifiedEntity createEntityFrom(ResourceModifiedMessage theMsg){
		IIdType theMsgId = theMsg.getPayloadId(myFhirContext);

		ResourceModifiedEntity resourceModifiedEntity = new ResourceModifiedEntity();
		resourceModifiedEntity.setResourcePid(theMsgId.getIdPartAsLong());
		resourceModifiedEntity.setResourceVersion(theMsgId.getVersionIdPartAsLong());
		resourceModifiedEntity.setResourceTransactionGuid(theMsg.getTransactionId());
		resourceModifiedEntity.setCreatedTime(new Date());
		resourceModifiedEntity.setOperationType(theMsg.getOperationType());

		RequestPartitionId requestPartitionId = ObjectUtils.defaultIfNull(theMsg.getPartitionId(), RequestPartitionId.defaultPartition());

		resourceModifiedEntity.setRequestPartitionId(requestPartitionId.toJson());

		return resourceModifiedEntity;
	}

}
