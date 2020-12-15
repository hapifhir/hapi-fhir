package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.stream.Stream;

public abstract class BaseMdmProvider {

	protected final FhirContext myFhirContext;

	public BaseMdmProvider(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	protected void validateMergeParameters(IPrimitiveType<String> theFromGoldenResourceId, IPrimitiveType<String> theToGoldenResourceId) {
		validateNotNull(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToGoldenResourceId);
		if (theFromGoldenResourceId.getValue().equals(theToGoldenResourceId.getValue())) {
			throw new InvalidRequestException("fromGoldenResourceId must be different from toGoldenResourceId");
		}
	}

	private void validateNotNull(String theName, IPrimitiveType<String> theString) {
		if (theString == null || theString.getValue() == null) {
			throw new InvalidRequestException(theName + " cannot be null");
		}
	}

	protected void validateUpdateLinkParameters(IPrimitiveType<String> theGoldenResourceId, IPrimitiveType<String> theResourceId, IPrimitiveType<String> theMatchResult) {
		// TODO NG - Add validation to check that types are the same? - This is done deeper in the code, perhaps move here?
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theResourceId);
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT, theMatchResult);
		MdmMatchResultEnum matchResult = MdmMatchResultEnum.valueOf(theMatchResult.getValue());
		switch (matchResult) {
			case NO_MATCH:
			case MATCH:
				break;
			default:
				throw new InvalidRequestException(ProviderConstants.MDM_UPDATE_LINK + " illegal " + ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT +
					" value '" + matchResult + "'.  Must be " + MdmMatchResultEnum.NO_MATCH + " or " + MdmMatchResultEnum.MATCH);
		}
	}

	protected void validateNotDuplicateParameters(IPrimitiveType<String> theGoldenResourceId, IPrimitiveType<String> theResourceId) {
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theResourceId);
	}


	protected MdmTransactionContext createMdmContext(RequestDetails theRequestDetails, MdmTransactionContext.OperationType theOperationType, String theResourceType) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createFromTransactionGuid(theRequestDetails.getTransactionGuid());
		MdmTransactionContext mdmTransactionContext = new MdmTransactionContext(transactionLogMessages, theOperationType);
		mdmTransactionContext.setResourceType(theResourceType);
		return mdmTransactionContext;
	}

	protected String extractStringOrNull(IPrimitiveType<String> theString) {
		if (theString == null) {
			return null;
		}
		return theString.getValue();
	}

	protected IBaseParameters parametersFromMdmLinks(Stream<MdmLinkJson> theMdmLinkStream, boolean includeResultAndSource) {
		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);

		theMdmLinkStream.forEach(mdmLink -> {
			IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retval, "link");
			ParametersUtil.addPartString(myFhirContext, resultPart, "goldenResourceId", mdmLink.getGoldenResourceId());
			ParametersUtil.addPartString(myFhirContext, resultPart, "sourceResourceId", mdmLink.getSourceId());

			if (includeResultAndSource) {
				ParametersUtil.addPartString(myFhirContext, resultPart, "matchResult", mdmLink.getMatchResult().name());
				ParametersUtil.addPartString(myFhirContext, resultPart, "linkSource", mdmLink.getLinkSource().name());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "eidMatch", mdmLink.getEidMatch());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "hadToCreateNewResource", mdmLink.getLinkCreatedNewResource());
				ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", mdmLink.getScore());
			}
		});
		return retval;
	}

}
