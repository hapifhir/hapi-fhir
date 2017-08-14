package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.Serializable;

public class ResourceDeliveryMessage implements Serializable {

	private static final long serialVersionUID = 0L;

	private IBaseResource mySubscription;
	private IBaseResource myPayoad;
	private RestOperationTypeEnum myOperationType;

	public RestOperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public void setOperationType(RestOperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public IBaseResource getPayoad() {
		return myPayoad;
	}

	public void setPayoad(IBaseResource thePayoad) {
		myPayoad = thePayoad;
	}

	public IBaseResource getSubscription() {
		return mySubscription;
	}

	public void setSubscription(IBaseResource theSubscription) {
		mySubscription = theSubscription;
	}

}
