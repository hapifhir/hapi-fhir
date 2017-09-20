package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.Serializable;

public class ResourceModifiedMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String myId;
	private RestOperationTypeEnum myOperationType;
	private String myNewPayloadEncoded;
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
