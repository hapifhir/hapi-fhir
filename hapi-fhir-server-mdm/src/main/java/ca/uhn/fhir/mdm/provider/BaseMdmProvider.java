/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageLinkBuilder;
import ca.uhn.fhir.mdm.api.paging.MdmPageLinkTuple;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseMdmProvider {

	protected final FhirContext myFhirContext;
	protected final IMdmControllerSvc myMdmControllerSvc;

	public BaseMdmProvider(FhirContext theFhirContext, IMdmControllerSvc theMdmControllerSvc) {
		myFhirContext = theFhirContext;
		myMdmControllerSvc = theMdmControllerSvc;
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
			IBaseParameters theRetVal,
			List<MdmLinkWithRevisionJson> theMdmLinkRevisions,
			ServletRequestDetails theRequestDetails) {
		if (theMdmLinkRevisions.isEmpty()) {
			final IBase resultPart = ParametersUtil.addParameterToParameters(
					myFhirContext, theRetVal, "historical links not found for query parameters");

			ParametersUtil.addPartString(
					myFhirContext, resultPart, "theResults", "historical links not found for query parameters");
		}

		theMdmLinkRevisions.forEach(mdmLinkRevision -> parametersFromMdmLinkRevision(
				theRetVal,
				mdmLinkRevision,
				findInitialMatchResult(theMdmLinkRevisions, mdmLinkRevision, theRequestDetails)));
	}

	private MdmMatchResultEnum findInitialMatchResult(
			List<MdmLinkWithRevisionJson> theRevisionList,
			MdmLinkWithRevisionJson theToMatch,
			ServletRequestDetails theRequestDetails) {
		String sourceId = theToMatch.getMdmLink().getSourceId();
		String goldenId = theToMatch.getMdmLink().getGoldenResourceId();

		// In the REDIRECT case, both the goldenResourceId and sourceResourceId fields are actually both
		// golden resources. Because of this, based on our history query, it's possible not all links
		// involving that golden resource will show up in the results (eg. query for goldenResourceId = GR/1
		// but sourceResourceId = GR/1 in the link history). Hence, we should re-query to find the initial
		// match result.
		if (theToMatch.getMdmLink().getMatchResult() == MdmMatchResultEnum.REDIRECT) {
			MdmHistorySearchParameters params = new MdmHistorySearchParameters()
					.setSourceIds(List.of(sourceId, goldenId))
					.setGoldenResourceIds(List.of(sourceId, goldenId));

			List<MdmLinkWithRevisionJson> result = myMdmControllerSvc.queryLinkHistory(params, theRequestDetails);
			// If there is a POSSIBLE_DUPLICATE, a user merged two resources with *pre-existing* POSSIBLE_DUPLICATE link
			// so the initial match result is POSSIBLE_DUPLICATE
			// If no POSSIBLE_DUPLICATE, a user merged two *unlinked* GRs, so the initial match result is REDIRECT
			return containsPossibleDuplicate(result)
					? MdmMatchResultEnum.POSSIBLE_DUPLICATE
					: MdmMatchResultEnum.REDIRECT;
		}

		// Get first match result with given source and golden ID
		Optional<MdmLinkWithRevisionJson> theEarliestRevision = theRevisionList.stream()
				.filter(revision -> revision.getMdmLink().getSourceId().equals(sourceId))
				.filter(revision -> revision.getMdmLink().getGoldenResourceId().equals(goldenId))
				.min(Comparator.comparing(MdmLinkWithRevisionJson::getRevisionNumber));

		return theEarliestRevision.isPresent()
				? theEarliestRevision.get().getMdmLink().getMatchResult()
				: theToMatch.getMdmLink().getMatchResult();
	}

	private static boolean containsPossibleDuplicate(List<MdmLinkWithRevisionJson> result) {
		return result.stream().anyMatch(t -> t.getMdmLink().getMatchResult() == MdmMatchResultEnum.POSSIBLE_DUPLICATE);
	}

	private void parametersFromMdmLinkRevision(
			IBaseParameters theRetVal,
			MdmLinkWithRevisionJson theMdmLinkRevision,
			MdmMatchResultEnum theInitialAutoResult) {
		final IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, theRetVal, "historical link");
		final MdmLinkJson mdmLink = theMdmLinkRevision.getMdmLink();

		ParametersUtil.addPartString(myFhirContext, resultPart, "goldenResourceId", mdmLink.getGoldenResourceId());
		ParametersUtil.addPartString(
				myFhirContext,
				resultPart,
				"revisionTimestamp",
				theMdmLinkRevision.getRevisionTimestamp().toString());
		ParametersUtil.addPartString(myFhirContext, resultPart, "sourceResourceId", mdmLink.getSourceId());
		ParametersUtil.addPartString(
				myFhirContext,
				resultPart,
				"matchResult",
				mdmLink.getMatchResult().name());
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

		IBase matchResultMapSubpart = ParametersUtil.createPart(myFhirContext, resultPart, "matchResultMap");

		IBase matchedRulesSubpart = ParametersUtil.createPart(myFhirContext, matchResultMapSubpart, "matchedRules");
		for (Map.Entry<String, MdmMatchResultEnum> entry : mdmLink.getRule()) {
			ParametersUtil.addPartString(myFhirContext, matchedRulesSubpart, "rule", entry.getKey());
		}

		ParametersUtil.addPartString(
				myFhirContext, matchResultMapSubpart, "initialMatchResult", theInitialAutoResult.name());
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
