package ca.uhn.fhir.jpa.subscription.model;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.rest.server.messaging.IResourceMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("WeakerAccess")
public class ResourceDeliveryMessage extends BaseResourceMessage implements IResourceMessage {

	@JsonProperty("canonicalSubscription")
	private CanonicalSubscription mySubscription;
	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;
	@JsonProperty("payload")
	private String myPayloadString;
	@JsonProperty("payloadId")
	private String myPayloadId;
	@JsonIgnore
	private transient IBaseResource myPayloadDecoded;

	/**
	 * Constructor
	 */
	public ResourceDeliveryMessage() {
		super();
		myPartitionId = RequestPartitionId.defaultPartition();
	}

	public IBaseResource getPayload(FhirContext theCtx) {
		IBaseResource retVal = myPayloadDecoded;
		if (retVal == null && isNotBlank(myPayloadString)) {
			IParser parser = EncodingEnum.detectEncoding(myPayloadString).newParser(theCtx);
			retVal = parser.parseResource(myPayloadString);
			myPayloadDecoded = retVal;
		}
		return retVal;
	}

	public String getPayloadString() {
		if (this.myPayloadString != null) {
			return this.myPayloadString;
		}

		return "";
	}

	public IIdType getPayloadId(FhirContext theCtx) {
		IIdType retVal = null;
		if (myPayloadId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(myPayloadId);
		}
		return retVal;
	}

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public void setSubscription(CanonicalSubscription theSubscription) {
		mySubscription = theSubscription;
	}

	public void setPayload(FhirContext theCtx, IBaseResource thePayload, EncodingEnum theEncoding) {
		/*
		 * Note that we populate the raw string but don't keep the parsed resource around when we set this. This
		 * has two reasons:
		 *  - If we build up a big queue of these on an in-memory queue, we aren't taking up double the memory
		 *  - If use a serializing queue, we aren't behaving differently (and therefore possibly missing things
		 *    in tests)
		 */
		myPayloadString = theEncoding.newParser(theCtx).encodeResourceToString(thePayload);
		myPayloadId = thePayload.getIdElement().toUnqualified().getValue();
	}

	@Override
	public String getPayloadId() {
		return myPayloadId;
	}

	public void setPayloadId(IIdType thePayloadId) {
		myPayloadId = null;
		if (thePayloadId != null) {
			myPayloadId = thePayloadId.getValue();
		}
	}

	public RequestPartitionId getRequestPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("mySubscription", mySubscription)
			.append("myPayloadString", myPayloadString)
			.append("myPayload", myPayloadDecoded)
			.append("myPayloadId", myPayloadId)
			.append("myPartitionId", myPartitionId)
			.append("myOperationType", getOperationType())
			.toString();
	}

	/**
	 * Helper method to fetch the subscription ID
	 */
	public String getSubscriptionId(FhirContext theFhirContext) {
		String retVal = null;
		if (getSubscription() != null) {
			IIdType idElement = getSubscription().getIdElement(theFhirContext);
			if (idElement != null) {
				retVal = idElement.getValue();
			}
		}
		return retVal;
	}
}
