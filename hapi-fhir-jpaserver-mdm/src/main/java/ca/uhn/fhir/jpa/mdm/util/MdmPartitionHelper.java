package ca.uhn.fhir.jpa.mdm.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmPartitionHelper {
	@Autowired
	MessageHelper myMessageHelper;

	public void validateResourcesInSamePartition(IAnyResource theFromResource, IAnyResource theToResource){
		RequestPartitionId fromGoldenResourcePartitionId = (RequestPartitionId) theFromResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		RequestPartitionId toGoldenPartitionId = (RequestPartitionId) theToResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (fromGoldenResourcePartitionId != null && toGoldenPartitionId != null && fromGoldenResourcePartitionId.hasPartitionIds() && toGoldenPartitionId.hasPartitionIds() &&
			!fromGoldenResourcePartitionId.hasPartitionId(toGoldenPartitionId.getFirstPartitionIdOrNull())) {
			throw new InvalidRequestException(Msg.code(2075) + myMessageHelper.getMessageForMismatchPartition(theFromResource, theToResource));
		}
	}
}
