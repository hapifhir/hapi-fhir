package ca.uhn.fhir.rest.server.messaging;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceModifiedSubscriptionMessage extends ResourceModifiedMessage {

    /**
     * This will only be set if the resource is being triggered for a specific
     * subscription
     */
    @JsonProperty(value = "subscriptionId", required = false)
    private String mySubscriptionId;

    public ResourceModifiedSubscriptionMessage() {
    }

    public ResourceModifiedSubscriptionMessage(FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
        super(theFhirContext, theResource, theOperationType);
    }

    public ResourceModifiedSubscriptionMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest) {
        super(theFhirContext, theNewResource, theOperationType, theRequest);
    }

    public String getSubscriptionId() {
        return mySubscriptionId;
    }

    public void setSubscriptionId(String theSubscriptionId) {
        mySubscriptionId = theSubscriptionId;
    }

}
