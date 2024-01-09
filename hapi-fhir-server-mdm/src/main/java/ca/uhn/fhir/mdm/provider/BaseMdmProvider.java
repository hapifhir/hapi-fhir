/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageLinkBuilder;
import ca.uhn.fhir.mdm.api.paging.MdmPageLinkTuple;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseMdmProvider {

	protected final FhirContext myFhirContext;

	public BaseMdmProvider(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	protected void validateMergeParameters(
			IPrimitiveType<String> theFromGoldenResourceId, IPrimitiveType<String> theToGoldenResourceId) {
		validateNotNull(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToGoldenResourceId);
		if (theFromGoldenResourceId.getValue().equals(theToGoldenResourceId.getValue())) {
			throw new InvalidRequestException(
					Msg.code(1493) + "fromGoldenResourceId must be different from toGoldenResourceId");
		}
	}

	private void validateNotNull(String theName, IPrimitiveType<String> theString) {
		if (theString == null || theString.getValue() == null) {
			throw new InvalidRequestException(Msg.code(1494) + theName + " cannot be null");
		}
	}

	protected void validateMdmLinkHistoryParameters(
			List<IPrimitiveType<String>> theGoldenResourceIds, List<IPrimitiveType<String>> theSourceIds) {
		validateBothCannotBeNullOrEmpty(
				ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID,
				theGoldenResourceIds,
				ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID,
				theSourceIds);
	}

	private void validateBothCannotBeNullOrEmpty(
			String theFirstName,
			List<IPrimitiveType<String>> theFirstList,
			String theSecondName,
			List<IPrimitiveType<String>> theSecondList) {
		if ((theFirstList == null || theFirstList.isEmpty()) && (theSecondList == null || theSecondList.isEmpty())) {
			throw new InvalidRequestException(Msg.code(2292) + "Please include either [" + theFirstName + "]s, ["
					+ theSecondName + "]s, or both in your search inputs.");
		}
	}

	protected void validateUpdateLinkParameters(
			IPrimitiveType<String> theGoldenResourceId,
			IPrimitiveType<String> theResourceId,
			IPrimitiveType<String> theMatchResult) {
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theResourceId);
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT, theMatchResult);
		MdmMatchResultEnum matchResult = MdmMatchResultEnum.valueOf(theMatchResult.getValue());
		switch (matchResult) {
			case NO_MATCH:
			case MATCH:
				break;
			default:
				throw new InvalidRequestException(Msg.code(1495) + ProviderConstants.MDM_UPDATE_LINK + " illegal "
						+ ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT + " value '" + matchResult + "'.  Must be "
						+ MdmMatchResultEnum.NO_MATCH + " or " + MdmMatchResultEnum.MATCH);
		}
	}

	protected void validateNotDuplicateParameters(
			IPrimitiveType<String> theGoldenResourceId, IPrimitiveType<String> theResourceId) {
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theResourceId);
	}

	protected void validateCreateLinkParameters(
			IPrimitiveType<String> theGoldenResourceId,
			IPrimitiveType<String> theResourceId,
			@Nullable IPrimitiveType<String> theMatchResult) {
		validateNotNull(ProviderConstants.MDM_CREATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		validateNotNull(ProviderConstants.MDM_CREATE_LINK_RESOURCE_ID, theResourceId);
		if (theMatchResult != null) {
			MdmMatchResultEnum matchResult = MdmMatchResultEnum.valueOf(theMatchResult.getValue());
			switch (matchResult) {
				case NO_MATCH:
				case POSSIBLE_MATCH:
				case MATCH:
					break;
				default:
					throw new InvalidRequestException(Msg.code(1496) + ProviderConstants.MDM_CREATE_LINK + " illegal "
							+ ProviderConstants.MDM_CREATE_LINK_MATCH_RESULT + " value '" + matchResult + "'.  Must be "
							+ MdmMatchResultEnum.NO_MATCH + ", " + MdmMatchResultEnum.MATCH + " or "
							+ MdmMatchResultEnum.POSSIBLE_MATCH);
			}
		}
	}

	protected MdmTransactionContext createMdmContext(
			RequestDetails theRequestDetails,
			MdmTransactionContext.OperationType theOperationType,
			String theResourceType) {
		TransactionLogMessages transactionLogMessages =
				TransactionLogMessages.createFromTransactionGuid(theRequestDetails.getTransactionGuid());
		MdmTransactionContext mdmTransactionContext =
				new MdmTransactionContext(transactionLogMessages, theOperationType);
		mdmTransactionContext.setResourceType(theResourceType);
		return mdmTransactionContext;
	}

	@Nonnull
	protected List<String> convertToStringsIncludingCommaDelimitedIfNotNull(
			List<IPrimitiveType<String>> thePrimitiveTypeStrings) {
		if (thePrimitiveTypeStrings == null) {
			return Collections.emptyList();
		}

		return thePrimitiveTypeStrings.stream()
				.map(this::extractStringOrNull)
				.filter(Objects::nonNull)
				.map(input -> Arrays.asList(input.split(",")))
				.flatMap(Collection::stream)
				.collect(Collectors.toUnmodifiableList());
	}

	protected String extractStringOrNull(IPrimitiveType<String> theString) {
		if (theString == null) {
			return null;
		}
		return theString.getValue();
	}

	protected IBaseParameters parametersFromMdmLinks(
			Page<MdmLinkJson> theMdmLinkStream,
			boolean theIncludeResultAndSource,
			ServletRequestDetails theServletRequestDetails,
			MdmPageRequest thePageRequest) {
		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
		addPagingParameters(retval, theMdmLinkStream, theServletRequestDetails, thePageRequest);

		long numDuplicates = theMdmLinkStream.getTotalElements();
		ParametersUtil.addParameterToParametersLong(myFhirContext, retval, "total", numDuplicates);

		theMdmLinkStream.getContent().forEach(mdmLink -> {
			IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retval, "link");
			ParametersUtil.addPartString(myFhirContext, resultPart, "goldenResourceId", mdmLink.getGoldenResourceId());
			ParametersUtil.addPartString(myFhirContext, resultPart, "sourceResourceId", mdmLink.getSourceId());

			if (theIncludeResultAndSource) {
				ParametersUtil.addPartString(
						myFhirContext,
						resultPart,
						"matchResult",
						mdmLink.getMatchResult().name());
				ParametersUtil.addPartString(
						myFhirContext,
						resultPart,
						"linkSource",
						mdmLink.getLinkSource().name());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "eidMatch", mdmLink.getEidMatch());
				ParametersUtil.addPartBoolean(
						myFhirContext, resultPart, "hadToCreateNewResource", mdmLink.getLinkCreatedNewResource());
				ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", mdmLink.getScore());
				ParametersUtil.addPartDecimal(myFhirContext, resultPart, "linkCreated", (double)
						mdmLink.getCreated().getTime());
				ParametersUtil.addPartDecimal(myFhirContext, resultPart, "linkUpdated", (double)
						mdmLink.getUpdated().getTime());
			}
		});
		return retval;
	}

	protected void parametersFromMdmLinkRevisions(
			IBaseParameters theRetVal, List<MdmLinkWithRevisionJson> theMdmLinkRevisions) {
		if (theMdmLinkRevisions.isEmpty()) {
			final IBase resultPart = ParametersUtil.addParameterToParameters(
					myFhirContext, theRetVal, "historical links not found for query parameters");

			ParametersUtil.addPartString(
					myFhirContext, resultPart, "theResults", "historical links not found for query parameters");
		}

		theMdmLinkRevisions.forEach(mdmLinkRevision -> parametersFromMdmLinkRevision(theRetVal, mdmLinkRevision));
	}

	private void parametersFromMdmLinkRevision(IBaseParameters retVal, MdmLinkWithRevisionJson mdmLinkRevision) {
		final IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retVal, "historical link");
		final MdmLinkJson mdmLink = mdmLinkRevision.getMdmLink();

		ParametersUtil.addPartString(myFhirContext, resultPart, "goldenResourceId", mdmLink.getGoldenResourceId());
		ParametersUtil.addPartString(
				myFhirContext,
				resultPart,
				"revisionTimestamp",
				mdmLinkRevision.getRevisionTimestamp().toString());
		ParametersUtil.addPartString(myFhirContext, resultPart, "sourceResourceId", mdmLink.getSourceId());
		ParametersUtil.addPartString(
				myFhirContext,
				resultPart,
				"matchResult",
				mdmLink.getMatchResult().name());
		ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", mdmLink.getScore());
		ParametersUtil.addPartString(
				myFhirContext, resultPart, "linkSource", mdmLink.getLinkSource().name());
		ParametersUtil.addPartBoolean(myFhirContext, resultPart, "eidMatch", mdmLink.getEidMatch());
		ParametersUtil.addPartBoolean(
				myFhirContext, resultPart, "hadToCreateNewResource", mdmLink.getLinkCreatedNewResource());
		ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", mdmLink.getScore());
		ParametersUtil.addPartDecimal(myFhirContext, resultPart, "linkCreated", (double)
				mdmLink.getCreated().getTime());
		ParametersUtil.addPartDecimal(myFhirContext, resultPart, "linkUpdated", (double)
				mdmLink.getUpdated().getTime());
	}

	protected void addPagingParameters(
			IBaseParameters theParameters,
			Page<MdmLinkJson> theCurrentPage,
			ServletRequestDetails theServletRequestDetails,
			MdmPageRequest thePageRequest) {
		MdmPageLinkTuple mdmPageLinkTuple =
				MdmPageLinkBuilder.buildMdmPageLinks(theServletRequestDetails, theCurrentPage, thePageRequest);

		if (mdmPageLinkTuple.getPreviousLink().isPresent()) {
			ParametersUtil.addParameterToParametersUri(
					myFhirContext,
					theParameters,
					"prev",
					mdmPageLinkTuple.getPreviousLink().get());
		}

		ParametersUtil.addParameterToParametersUri(
				myFhirContext, theParameters, "self", mdmPageLinkTuple.getSelfLink());

		if (mdmPageLinkTuple.getNextLink().isPresent()) {
			ParametersUtil.addParameterToParametersUri(
					myFhirContext,
					theParameters,
					"next",
					mdmPageLinkTuple.getNextLink().get());
		}
	}
}
