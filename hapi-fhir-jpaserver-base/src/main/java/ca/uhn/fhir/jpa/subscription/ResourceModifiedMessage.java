package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.Serializable;

public class ResourceModifiedMessage implements Serializable {

	private static final long serialVersionUID = 0L;

	private IIdType myId;
	private RestOperationTypeEnum myOperationType;
	private IBaseResource myNewPayload;

	public IIdType getId() {
		return myId;
	}

	public void setId(IIdType theId) {
		myId = theId;
	}


	public RestOperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public void setOperationType(RestOperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public IBaseResource getNewPayload() {
		return myNewPayload;
	}

	public void setNewPayload(IBaseResource theNewPayload) {
		myNewPayload = theNewPayload;
	}
}
