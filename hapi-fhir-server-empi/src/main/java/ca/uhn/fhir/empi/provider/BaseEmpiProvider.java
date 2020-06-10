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
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public abstract class BaseEmpiProvider {

	protected final FhirContext myFhirContext;
	private final IResourceLoader myResourceLoader;

	public BaseEmpiProvider(FhirContext theFhirContext, IResourceLoader theResourceLoader) {
		myFhirContext = theFhirContext;
		myResourceLoader = theResourceLoader;
	}

	protected IAnyResource getLatestPersonFromIdOrThrowException(String theParamName, String theId) {
		IdDt personId = getPersonIdDtOrThrowException(theParamName, theId);
		return loadResource(personId.toUnqualifiedVersionless());
	}

	private IdDt getPersonIdDtOrThrowException(String theParamName, String theId) {
		IdDt personId = new IdDt(theId);
		if (!"Person".equals(personId.getResourceType()) ||
			personId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Person/<id> where <id> is the id of the person");
		}
		return personId;
	}

	protected IAnyResource getLatestTargetFromIdOrThrowException(String theParamName, String theId) {
		IIdType targetId = getTargetIdDtOrThrowException(theParamName, theId);
		return loadResource(targetId.toUnqualifiedVersionless());
	}

	protected IIdType getTargetIdDtOrThrowException(String theParamName, String theId) {
		IdDt targetId = new IdDt(theId);
		String resourceType = targetId.getResourceType();
		if (!EmpiUtil.supportedTargetType(resourceType) ||
			targetId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource");
		}
		return targetId;
	}

	protected IAnyResource loadResource(IIdType theResourceId) {
		Class<? extends IBaseResource> resourceClass = myFhirContext.getResourceDefinition(theResourceId.getResourceType()).getImplementingClass();
		return (IAnyResource) myResourceLoader.load(resourceClass, theResourceId);
	}

	protected void validateMergeParameters(IPrimitiveType<String> theFromPersonId, IPrimitiveType<String> theToPersonId) {
		validateNotNull(ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, theFromPersonId);
		validateNotNull(ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, theToPersonId);
		if (theFromPersonId.getValue().equals(theToPersonId.getValue())) {
			throw new InvalidRequestException("fromPersonId must be different from toPersonId");
		}
 	}

 	protected void validateMergeResources(IAnyResource theFromPerson, IAnyResource theToPerson) {
		validateIsEmpiManaged(ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, theFromPerson);
		validateIsEmpiManaged(ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, theToPerson);
	}

	private void validateIsEmpiManaged(String theName, IAnyResource thePerson) {
		if (!"Person".equals(myFhirContext.getResourceType(thePerson))) {
			throw new InvalidRequestException("Only Person resources can be merged.  The " + theName + " points to a " + myFhirContext.getResourceType(thePerson));
		}
		if (!EmpiUtil.isEmpiManaged(thePerson)) {
			throw new InvalidRequestException("Only EMPI managed resources can be merged.  Empi managed resource have the " + EmpiConstants.CODE_HAPI_EMPI_MANAGED + " tag.");
		}
	}

	private void validateNotNull(String theName, IPrimitiveType<String> theString) {
		if (theString == null || theString.getValue() == null) {
			throw new InvalidRequestException(theName + " cannot be null");
		}
	}

	protected void validateUpdateLinkParameters(IPrimitiveType<String> thePersonId, IPrimitiveType<String> theTargetId, IPrimitiveType<String> theMatchResult) {
		validateNotNull(ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, thePersonId);
		validateNotNull(ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, theTargetId);
		validateNotNull(ProviderConstants.EMPI_UPDATE_LINK_MATCH_RESULT, theMatchResult);
		EmpiMatchResultEnum matchResult = EmpiMatchResultEnum.valueOf(theMatchResult.getValue());
		switch (matchResult) {
			case NO_MATCH:
			case MATCH:
				break;
			default:
				throw new InvalidRequestException(ProviderConstants.EMPI_UPDATE_LINK + " illegal " + ProviderConstants.EMPI_UPDATE_LINK_MATCH_RESULT +
					" value '" + matchResult + "'.  Must be " + EmpiMatchResultEnum.NO_MATCH + " or " + EmpiMatchResultEnum.MATCH);
		}
	}

	protected void validateNotDuplicateParameters(IPrimitiveType<String> thePersonId, IPrimitiveType<String> theTargetId) {
		validateNotNull(ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, thePersonId);
		validateNotNull(ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, theTargetId);
	}

	protected EmpiTransactionContext createEmpiContext(RequestDetails theRequestDetails) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createFromTransactionGuid(theRequestDetails.getTransactionGuid());
		return new EmpiTransactionContext(transactionLogMessages, EmpiTransactionContext.OperationType.MERGE_PERSONS);
	}

	protected EmpiMatchResultEnum extractMatchResultOrNull(IPrimitiveType<String> theMatchResult) {
		String matchResult = extractStringNull(theMatchResult);
		if (matchResult == null) {
			return null;
		}
		return EmpiMatchResultEnum.valueOf(matchResult);
	}

	protected EmpiLinkSourceEnum extractLinkSourceOrNull(IPrimitiveType<String> theLinkSource) {
		String linkSource = extractStringNull(theLinkSource);
		if (linkSource == null) {
			return null;
		}
		return EmpiLinkSourceEnum.valueOf(linkSource);
	}

	private String extractStringNull(IPrimitiveType<String> theString) {
		if (theString == null) {
			return null;
		}
		return theString.getValue();
	}

	protected IIdType extractPersonIdDtOrNull(String theName, IPrimitiveType<String> thePersonId) {
		String personId = extractStringNull(thePersonId);
		if (personId == null) {
			return null;
		}
		return getPersonIdDtOrThrowException(theName, personId);
	}

	protected IIdType extractTargetIdDtOrNull(String theName, IPrimitiveType<String> theTargetId) {
		String targetId = extractStringNull(theTargetId);
		if (targetId == null) {
			return null;
		}
		return getTargetIdDtOrThrowException(theName, targetId);
	}

	protected void validateSameVersion(IAnyResource theResource, IPrimitiveType<String> theResourceId) {
		String storedId = theResource.getIdElement().getValue();
		String requestedId = theResourceId.getValue();
		if (hasVersionIdPart(requestedId) && !storedId.equals(requestedId)) {
			throw new ResourceVersionConflictException("Requested resource " + requestedId + " is not the latest version.  Latest version is " + storedId);
		}
	}

	private boolean hasVersionIdPart(String theId) {
		return new IdDt(theId).hasVersionIdPart();
	}
}
