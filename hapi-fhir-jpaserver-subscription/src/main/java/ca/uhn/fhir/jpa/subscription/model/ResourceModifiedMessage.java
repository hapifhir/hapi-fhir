package ca.uhn.fhir.jpa.subscription.model;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
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
	/**
	 * This will only be set if the resource is being triggered for a specific
	 * subscription
	 */
	@JsonProperty(value = "subscriptionId", required = false)
	private String mySubscriptionId;
	@JsonProperty("payload")
	private String myPayload;
	@JsonProperty("payloadId")
	private String myPayloadId;
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

	@Override
	public String getPayloadId() {
		return myPayloadId;
	}

	public String getSubscriptionId() {
		return mySubscriptionId;
	}

	public void setSubscriptionId(String theSubscriptionId) {
		mySubscriptionId = theSubscriptionId;
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
			.append("mySubscriptionId", mySubscriptionId)
//			.append("myPayload", myPayload)
			.append("myPayloadId", myPayloadId)
//			.append("myPayloadDecoded", myPayloadDecoded)
			.toString();
	}
}
