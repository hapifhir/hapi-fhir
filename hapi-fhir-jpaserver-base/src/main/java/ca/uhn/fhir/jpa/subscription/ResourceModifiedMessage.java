package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ResourceModifiedMessage {

	private static final long serialVersionUID = 1L;

	@JsonProperty("resourceId")
	private String myId;
	@JsonProperty("operationType")
	private RestOperationTypeEnum myOperationType;
	@JsonProperty("newPayload")
	private String myNewPayloadEncoded;
	@JsonIgnore
	private transient IBaseResource myNewPayload;

	public IIdType getId(FhirContext theCtx) {
		IIdType retVal = null;
		if (myId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(myId);
		}
		return retVal;
	}

	public IBaseResource getNewPayload(FhirContext theCtx) {
		if (myNewPayload == null && myNewPayloadEncoded != null) {
			myNewPayload = theCtx.newJsonParser().parseResource(myNewPayloadEncoded);
		}
		return myNewPayload;
	}

	public RestOperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public void setOperationType(RestOperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public void setId(IIdType theId) {
		myId = null;
		if (theId != null) {
			myId = theId.getValue();
		}
	}

	public void setNewPayload(FhirContext theCtx, IBaseResource theNewPayload) {
		myNewPayload = theNewPayload;
		myNewPayloadEncoded = theCtx.newJsonParser().encodeResourceToString(theNewPayload);
	}
}
