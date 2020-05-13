package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public abstract class BaseEmpiProvider {

	private final FhirContext myFhirContext;
	private final IResourceLoader myResourceLoader;

	public BaseEmpiProvider(FhirContext theFhirContext, IResourceLoader theResourceLoader) {
		myFhirContext = theFhirContext;
		myResourceLoader = theResourceLoader;
	}

	protected IAnyResource getPersonFromId(String theId, String theParamName) {
		IdDt personId = new IdDt(theId);
		if (!"Person".equals(personId.getResourceType()) ||
			personId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Person/<id> where <id> is the id of the person");
		}
		return loadResource(personId);
	}

	protected IAnyResource getTargetFromId(String theId, String theParamName) {
		IdDt targetId = new IdDt(theId);
		String resourceType = targetId.getResourceType();
		if (!EmpiUtil.supportedTargetType(resourceType) ||
			targetId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource");
		}
		return loadResource(targetId);
	}

	protected IAnyResource loadResource(IdDt theResourceId) {
		Class<? extends IBaseResource> resourceClass = myFhirContext.getResourceDefinition(theResourceId.getResourceType()).getImplementingClass();
		return (IAnyResource) myResourceLoader.load(resourceClass, theResourceId);
	}

	protected void validateMergeParameters(IPrimitiveType<String> thePersonIdToDelete, IPrimitiveType<String> thePersonIdToKeep) {
		if (thePersonIdToDelete == null) {
			throw new InvalidRequestException("personToDelete cannot be null");
		}
		if (thePersonIdToKeep == null) {
			throw new InvalidRequestException("personToKeep cannot be null");
		}
		if (thePersonIdToDelete.getValue().equals(thePersonIdToKeep.getValue())) {
			throw new InvalidRequestException("personIdToDelete must be different from personToKeep");
		}
 	}

	protected void validateUpdateLinkParameters(IPrimitiveType<String> thePersonId, IPrimitiveType<String> theTargetId, IPrimitiveType<String> theMatchResult) {
		if (thePersonId == null) {
			// FIXME KHS these should all use constants
			throw new InvalidRequestException("personId cannot be null");
		}
		if (theTargetId == null) {
			throw new InvalidRequestException("targetId cannot be null");
		}
		if (theMatchResult == null) {
			throw new InvalidRequestException("matchResult cannot be null");
		}
	}

	protected EmpiTransactionContext createEmpiContext(RequestDetails theRequestDetails) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createFromTransactionGuid(theRequestDetails.getTransactionGuid());
		return new EmpiTransactionContext(transactionLogMessages, EmpiTransactionContext.OperationType.MERGE_PERSONS);
	}
}
