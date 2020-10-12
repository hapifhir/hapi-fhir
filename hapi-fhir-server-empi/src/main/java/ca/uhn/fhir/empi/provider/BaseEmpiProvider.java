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
import ca.uhn.fhir.empi.api.EmpiLinkJson;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.stream.Stream;

public abstract class BaseEmpiProvider {

	protected final FhirContext myFhirContext;

	public BaseEmpiProvider(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	protected void validateMergeParameters(IPrimitiveType<String> theFromPersonId, IPrimitiveType<String> theToPersonId) {
		validateNotNull(ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, theFromPersonId);
		validateNotNull(ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, theToPersonId);
		if (theFromPersonId.getValue().equals(theToPersonId.getValue())) {
			throw new InvalidRequestException("fromPersonId must be different from toPersonId");
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

	protected EmpiTransactionContext createEmpiContext(RequestDetails theRequestDetails, EmpiTransactionContext.OperationType theOperationType) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createFromTransactionGuid(theRequestDetails.getTransactionGuid());
		return new EmpiTransactionContext(transactionLogMessages, theOperationType);
	}

	protected String extractStringOrNull(IPrimitiveType<String> theString) {
		if (theString == null) {
			return null;
		}
		return theString.getValue();
	}

	protected IBaseParameters parametersFromEmpiLinks(Stream<EmpiLinkJson> theEmpiLinkStream, boolean includeResultAndSource) {
		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);

		theEmpiLinkStream.forEach(empiLink -> {
			IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retval, "link");
			ParametersUtil.addPartString(myFhirContext, resultPart, "personId", empiLink.getPersonId());
			ParametersUtil.addPartString(myFhirContext, resultPart, "targetId", empiLink.getTargetId());

			if (includeResultAndSource) {
				ParametersUtil.addPartString(myFhirContext, resultPart, "matchResult", empiLink.getMatchResult().name());
				ParametersUtil.addPartString(myFhirContext, resultPart, "linkSource", empiLink.getLinkSource().name());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "eidMatch", empiLink.getEidMatch());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "newPerson", empiLink.getNewPerson());
				ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", empiLink.getScore());
			}
		});
		return retval;
	}
}
