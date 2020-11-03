package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IVersionChangeConsumer {
	void accept(IIdType theResourceId, BaseResourceMessage.OperationTypeEnum theOperationType);
}
