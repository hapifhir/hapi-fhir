package ca.uhn.fhir.rest.server.messaging;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
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

public abstract class BaseResourceModifiedMessage extends BaseResourceMessage implements IResourceMessage, IModelJson {

	@JsonProperty("resourceId")
	protected String myId;
	@JsonProperty("payload")
	protected String myPayload;
	@JsonProperty("payloadId")
	protected String myPayloadId;
	@JsonIgnore
	protected transient IBaseResource myPayloadDecoded;

	/**
	 * Constructor
	 */
	public BaseResourceModifiedMessage() {
		super();
	}

	public BaseResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		this();
		setId(theResource.getIdElement());
		setOperationType(theOperationType);
		if (theOperationType != OperationTypeEnum.DELETE) {
			setNewPayload(theFhirContext, theResource);
		}
	}

	public BaseResourceModifiedMessage(FhirContext theFhirContext, IBaseResource theNewResource, OperationTypeEnum theOperationType, RequestDetails theRequest) {
		this(theFhirContext, theNewResource, theOperationType);
		if (theRequest != null) {
			setTransactionId(theRequest.getTransactionGuid());
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

	public IBaseResource getPayload(FhirContext theCtx) {
		IBaseResource retVal = myPayloadDecoded;
		if (retVal == null && isNotBlank(myPayload)) {
			IParser parser = EncodingEnum.detectEncoding(myPayload).newParser(theCtx);
			retVal = parser.parseResource(myPayload);
			myPayloadDecoded = retVal;
		}
		return retVal;
	}

	public String getPayloadString() {
		if (this.myPayload != null) {
			return this.myPayload;
		}

		return "";
	}

	public void setId(IIdType theId) {
		myId = null;
		if (theId != null) {
			myId = theId.getValue();
		}
	}

	protected void setNewPayload(FhirContext theCtx, IBaseResource theNewPayload) {
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

	protected static boolean payloadContainsNoPlaceholderReferences(FhirContext theCtx, IBaseResource theNewPayload) {
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

