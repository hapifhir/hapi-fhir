package ca.uhn.fhir.rest.server.messaging;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ResourceModifiedMessage extends BaseResourceMessage implements IResourceMessage, IModelJson {

	@JsonProperty("resourceId")
	private String myId;
	@JsonProperty("operationType")
	private OperationTypeEnum myOperationType;
	@JsonProperty("payload")
	private String myPayload;
	@JsonProperty("payloadId")
	private String myPayloadId;
	@JsonProperty("parentTransactionGuid")
	private String myParentTransactionGuid;
	@JsonIgnore
	private transient IBaseResource myPayloadDecoded;

	/**
	 * Constructor
	 */
	public ResourceModifiedMessage() {
		super();
	}

	public ResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		this();
		setId(theResource.getIdElement());
		setOperationType(theOperationType);
		if (theOperationType != OperationTypeEnum.DELETE) {
			setNewPayload(theFhirContext, theResource);
		}
	}

	public ResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest) {
		this(theFhirContext, theNewResource, theOperationType);
		if (theRequest != null) {
			setParentTransactionGuid(theRequest.getTransactionGuid());
		}
	}

	@Override
	public String getPayloadId() {
		return myPayloadId;
	}

	public String getId() {
		return myId;
	}

	public IIdType getId(FhirContext theCtx) {
		IIdType retVal = null;
		if (myId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(myId);
		}
		return retVal;
	}

	public IBaseResource getNewPayload(FhirContext theCtx) {
		if (myPayloadDecoded == null && isNotBlank(myPayload)) {
			myPayloadDecoded = theCtx.newJsonParser().parseResource(myPayload);
		}
		return myPayloadDecoded;
	}

	public OperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public void setOperationType(OperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public void setId(IIdType theId) {
		myId = null;
		if (theId != null) {
			myId = theId.getValue();
		}
	}

	public String getParentTransactionGuid() {
		return myParentTransactionGuid;
	}

	public void setParentTransactionGuid(String theParentTransactionGuid) {
		myParentTransactionGuid = theParentTransactionGuid;
	}

	private void setNewPayload(FhirContext theCtx, IBaseResource theNewPayload) {
		/*
		 * References with placeholders would be invalid by the time we get here, and
		 * would be caught before we even get here. This check is basically a last-ditch
		 * effort to make sure nothing has broken in the various safeguards that
		 * should prevent this from happening (hence it only being an assert as
		 * opposed to something executed all the time).
		 */
		assert payloadContainsNoPlaceholderReferences(theCtx, theNewPayload);

		/*
		 * Note: Don't set myPayloadDecoded in here- This is a false optimization since
		 * it doesn't actually get used if anyone is doing subscriptions at any
		 * scale using a queue engine, and not going through the serialize/deserialize
		 * as we would in a queue engine can mask bugs.
		 * -JA
		 */
		myPayload = theCtx.newJsonParser().encodeResourceToString(theNewPayload);
		myPayloadId = theNewPayload.getIdElement().toUnqualified().getValue();
	}

	public enum OperationTypeEnum {
		CREATE,
		UPDATE,
		DELETE,
		MANUALLY_TRIGGERED
	}

	private static boolean payloadContainsNoPlaceholderReferences(FhirContext theCtx, IBaseResource theNewPayload) {
		List<ResourceReferenceInfo> refs = theCtx.newTerser().getAllResourceReferences(theNewPayload);
		for (ResourceReferenceInfo next : refs) {
			String ref = next.getResourceReference().getReferenceElement().getValue();
			if (isBlank(ref)) {
				IBaseResource resource = next.getResourceReference().getResource();
				if (resource != null) {
					ref = resource.getIdElement().getValue();
				}
			}
			if (isNotBlank(ref)) {
				if (ref.startsWith("#")) {
					continue;
				}
				if (ref.startsWith("urn:uuid:")) {
					throw new AssertionError("Reference at " + next.getName() + " is invalid: " + ref);
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myId", myId)
			.append("myOperationType", myOperationType)
//			.append("myPayload", myPayload)
			.append("myPayloadId", myPayloadId)
//			.append("myPayloadDecoded", myPayloadDecoded)
			.toString();
	}
}

