package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkHistoryJson;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkRevisionJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmProviderDstu3Plus extends BaseMdmProvider {
	private static final Logger ourLog = getLogger(MdmProviderDstu3Plus.class);

	private final IMdmControllerSvc myMdmControllerSvc;
	private final IMdmSubmitSvc myMdmSubmitSvc;
	private final IMdmSettings myMdmSettings;
	private final MdmControllerHelper myMdmControllerHelper;

	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 100;

	/**
	 * Constructor
	 * <p>
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public MdmProviderDstu3Plus(FhirContext theFhirContext,
										 IMdmControllerSvc theMdmControllerSvc,
										 MdmControllerHelper theMdmHelper,
										 IMdmSubmitSvc theMdmSubmitSvc,
										 IMdmSettings theIMdmSettings
										 ) {
		super(theFhirContext);
		myMdmControllerSvc = theMdmControllerSvc;
		myMdmControllerHelper = theMdmHelper;
		myMdmSubmitSvc = theMdmSubmitSvc;
		myMdmSettings = theIMdmSettings;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, typeName = "Patient")
	public IBaseBundle match(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1, typeName = "Patient") IAnyResource thePatient,
									 RequestDetails theRequestDetails) {
		if (thePatient == null) {
			throw new InvalidRequestException(Msg.code(1498) + "resource may not be null");
		}
		return myMdmControllerHelper.getMatchesAndPossibleMatchesForResource(thePatient, "Patient", theRequestDetails);
	}

	@Operation(name = ProviderConstants.MDM_MATCH)
	public IBaseBundle serverMatch(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1) IAnyResource theResource,
											 @OperationParam(name = ProviderConstants.MDM_RESOURCE_TYPE, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theResourceType,
											 RequestDetails theRequestDetails
	) {
		if (theResource == null) {
			throw new InvalidRequestException(Msg.code(1499) + "resource may not be null");
		}
		return myMdmControllerHelper.getMatchesAndPossibleMatchesForResource(theResource, theResourceType.getValueAsString(), theRequestDetails);
	}

	@Operation(name = ProviderConstants.MDM_MERGE_GOLDEN_RESOURCES)
	public IBaseResource mergeGoldenResources(@OperationParam(name = ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theFromGoldenResourceId,
															@OperationParam(name = ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theToGoldenResourceId,
															@OperationParam(name = ProviderConstants.MDM_MERGE_RESOURCE, max = 1) IAnyResource theMergedResource,
															RequestDetails theRequestDetails) {
		validateMergeParameters(theFromGoldenResourceId, theToGoldenResourceId);

		MdmTransactionContext.OperationType operationType = (theMergedResource == null) ?
			MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES : MdmTransactionContext.OperationType.MANUAL_MERGE_GOLDEN_RESOURCES;
		MdmTransactionContext txContext = createMdmContext(theRequestDetails, operationType,
			getResourceType(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId));
		return myMdmControllerSvc.mergeGoldenResources(theFromGoldenResourceId.getValueAsString(), theToGoldenResourceId.getValueAsString(), theMergedResource, txContext);
	}

	@Operation(name = ProviderConstants.MDM_UPDATE_LINK)
	public IBaseResource updateLink(@OperationParam(name = ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, min = 1, max = 1) IPrimitiveType<String> theGoldenResourceId,
											  @OperationParam(name = ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, min = 1, max = 1) IPrimitiveType<String> theResourceId,
											  @OperationParam(name = ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) IPrimitiveType<String> theMatchResult,
											  ServletRequestDetails theRequestDetails) {
		validateUpdateLinkParameters(theGoldenResourceId, theResourceId, theMatchResult);
		return myMdmControllerSvc.updateLink(theGoldenResourceId.getValueAsString(), theResourceId.getValue(),
			theMatchResult.getValue(), createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.UPDATE_LINK,
				getResourceType(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);
	}

	@Operation(name = ProviderConstants.MDM_CREATE_LINK)
	public IBaseResource createLink(@OperationParam(name = ProviderConstants.MDM_CREATE_LINK_GOLDEN_RESOURCE_ID, min = 1, max = 1) IPrimitiveType<String> theGoldenResourceId,
											  @OperationParam(name = ProviderConstants.MDM_CREATE_LINK_RESOURCE_ID, min = 1, max = 1) IPrimitiveType<String> theResourceId,
											  @OperationParam(name = ProviderConstants.MDM_CREATE_LINK_MATCH_RESULT, min = 0, max = 1) IPrimitiveType<String> theMatchResult,
											  ServletRequestDetails theRequestDetails) {
		validateCreateLinkParameters(theGoldenResourceId, theResourceId, theMatchResult);
		return myMdmControllerSvc.createLink(theGoldenResourceId.getValueAsString(), theResourceId.getValue(), extractStringOrNull(theMatchResult),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.CREATE_LINK,
				getResourceType(ProviderConstants.MDM_CREATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_CLEAR, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, typeName = "decimal")
	})
	public IBaseParameters clearMdmLinks(@OperationParam(name = ProviderConstants.OPERATION_MDM_CLEAR_RESOURCE_NAME, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theResourceNames,
													 @OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
													 ServletRequestDetails theRequestDetails) {

		List<String> resourceNames = new ArrayList<>();

		if (theResourceNames != null) {
			resourceNames.addAll(theResourceNames.stream().map(IPrimitiveType::getValue).collect(Collectors.toList()));
			validateResourceNames(resourceNames);
		} else {
			resourceNames.addAll(myMdmSettings.getMdmRules().getMdmTypes());
		}

		return myMdmControllerSvc.submitMdmClearJob(resourceNames, theBatchSize, theRequestDetails);
	}

	private void validateResourceNames(List<String> theResourceNames) {
		for (String resourceName : theResourceNames) {
			if (!myMdmSettings.isSupportedMdmType(resourceName)) {
				throw new InvalidRequestException(Msg.code(1500) + ProviderConstants.OPERATION_MDM_CLEAR + " does not support resource type: " + resourceName);
			}
		}
	}

	// TODO: search by golden record ID (mulitple IDs)   order by time:  ask Ava >>> MIN 0 NO MAX... repeating
	// TODO: search by target resource ID:  ask Ava >>>> at least one of each.....
	// UNION DISTINCT (squash dupes) of both queries  OR ONLY a single  one at a time
	// OperationParams:  MAX UNLIMITED
	//  >>> drop ALL OTHER SEARCH PARAMS
	// TODO: ONLY ALLOW ONE OF EACH!!!!
	// TODO:  don't bother with paging
	// TODO:  show all MDM link fields
	// TODO:  ask Ava about the "context of investigation"   "all MDM traffic" vs. something more targeted
	// TODO: a single golden patient may have several patient matched
	// TODO:  history of golden resource vs. target resource:  different views.....
	// TODO:  some MDM rules may be VERY BROAD....  LIKE "Smi*"
	// TODO:  how do we want to sort?:   probably default by timestamp

	// Is a set of the OR sufficient ot the contenxt she's investigating?
	@Operation(name = ProviderConstants.MDM_QUERY_LINKS, idempotent = true)
	public IBaseParameters queryLinks(@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theGoldenResourceId,
												 @OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theResourceId,
												 @OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theMatchResult,
												 @OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_LINK_SOURCE, min = 0, max = 1, typeName = "string")
													 IPrimitiveType<String> theLinkSource,

												 @Description(value = "Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
												 @OperationParam(name = PARAM_OFFSET, min = 0, max = 1, typeName = "integer")
														 IPrimitiveType<Integer> theOffset,

												 @Description(value = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
												 @OperationParam(name = Constants.PARAM_COUNT, min = 0, max = 1, typeName = "integer")
														 IPrimitiveType<Integer> theCount,

												 @OperationParam(name = Constants.PARAM_SORT, min = 0, max = 1, typeName = "string")
														 IPrimitiveType<String> theSort,

												 ServletRequestDetails theRequestDetails,
												 @OperationParam(name = ProviderConstants.MDM_RESOURCE_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theResourceType
												 ) {
		MdmPageRequest mdmPageRequest = new MdmPageRequest(theOffset, theCount, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE);
		MdmTransactionContext mdmContext = createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.QUERY_LINKS,
			getResourceType(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId, theResourceType));
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(mdmPageRequest)
				.setGoldenResourceId(extractStringOrNull(theGoldenResourceId))
				.setSourceId(extractStringOrNull(theResourceId))
				.setLinkSource(extractStringOrNull(theLinkSource))
				.setMatchResult(extractStringOrNull(theMatchResult))
				.setResourceType(extractStringOrNull(theResourceType))
				.setSort(extractStringOrNull(theSort));

		Page<MdmLinkJson> mdmLinkJson = myMdmControllerSvc.queryLinks(mdmQuerySearchParameters, mdmContext, theRequestDetails);
		return parametersFromMdmLinks(mdmLinkJson, true, theRequestDetails, mdmPageRequest);
	}

	// TODO: how should this behave if they've turned off envers?
	// TODO:  what does idempotent mean in this case?
	@Operation(name = ProviderConstants.MDM_LINK_HISTORY, idempotent = true)
	public IBaseParameters historyLinks(@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theMdmGoldenResourceIds,
													@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theResourceIds) {
		// TODO: validate that either golden resource or target resource is provided
		ourLog.info("history:  goldenResourceIds: {}, targetResourceIds: {}", theMdmGoldenResourceIds, theResourceIds);
		final List<String> goldenResourceIdsToUse = theMdmGoldenResourceIds == null ? Collections.emptyList() : theMdmGoldenResourceIds.stream().map(this::extractStringOrNull).collect(Collectors.toUnmodifiableList());
		final List<String> resourceIdsToUse = theResourceIds == null ? Collections.emptyList() : theResourceIds.stream().map(this::extractStringOrNull).collect(Collectors.toUnmodifiableList());

		final MdmLinkHistoryJson mdmLinkJson = myMdmControllerSvc.queryLinkHistory(null, null);

		final LocalDateTime march9 = LocalDateTime.of(2023, Month.MARCH, 9, 11, 20, 37);
		final LocalDateTime march8 = LocalDateTime.of(2023, Month.MARCH, 8, 11, 20, 37);
		final LocalDateTime march7 = LocalDateTime.of(2023, Month.MARCH, 7, 11, 20, 37);
		final LocalDateTime march6 = LocalDateTime.of(2023, Month.MARCH, 6, 11, 20, 37);
		final LocalDateTime march5 = LocalDateTime.of(2023, Month.MARCH, 5, 11, 20, 37);
		final LocalDateTime march4 = LocalDateTime.of(2023, Month.MARCH, 4, 11, 20, 37);
		final LocalDateTime march3 = LocalDateTime.of(2023, Month.MARCH, 3, 11, 20, 37);
		final LocalDateTime march2 = LocalDateTime.of(2023, Month.MARCH, 2, 11, 20, 37);
		final LocalDateTime march1 = LocalDateTime.of(2023, Month.MARCH, 1, 11, 20, 37);

		// 2 revisions for MDM link 1:  from MATCH to NO_MATCH
		final MdmLinkRevisionJson mdmLinkRevisionJson1_v1 = buildMdmLinkRevisionJson(1, march1, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.MATCH, "1", "A");
		final MdmLinkRevisionJson mdmLinkRevisionJson1_v2 = buildMdmLinkRevisionJson(2, march2, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.NO_MATCH, "1", "A");

		// 3 revisions for MDM link 2:  each with a different source ID
		final MdmLinkRevisionJson mdmLinkRevisionJson2_v1 = buildMdmLinkRevisionJson(1, march1, MdmLinkSourceEnum.AUTO, MdmMatchResultEnum.MATCH, "2", "B");
		final MdmLinkRevisionJson mdmLinkRevisionJson2_v2 = buildMdmLinkRevisionJson(2, march2, MdmLinkSourceEnum.AUTO, MdmMatchResultEnum.MATCH, "2", "B_1");
		final MdmLinkRevisionJson mdmLinkRevisionJson2_v3 = buildMdmLinkRevisionJson(3, march3, MdmLinkSourceEnum.AUTO, MdmMatchResultEnum.MATCH, "2", "B_2");

		final MdmLinkRevisionJson mdmLinkRevisionJson3 = buildMdmLinkRevisionJson(1, march4, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "3", "C");
		final MdmLinkRevisionJson mdmLinkRevisionJson4 = buildMdmLinkRevisionJson(1, march5, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "3", "D");
		final MdmLinkRevisionJson mdmLinkRevisionJson5 = buildMdmLinkRevisionJson(1, march6, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "3", "E");
		final MdmLinkRevisionJson mdmLinkRevisionJson6 = buildMdmLinkRevisionJson(1, march7, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "3", "F");

		final MdmLinkRevisionJson mdmLinkRevisionJson7 = buildMdmLinkRevisionJson(1, march8, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "4", "G");
		final MdmLinkRevisionJson mdmLinkRevisionJson8 = buildMdmLinkRevisionJson(1, march9, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "4", "H");
		final MdmLinkRevisionJson mdmLinkRevisionJson9 = buildMdmLinkRevisionJson(1, march9, MdmLinkSourceEnum.MANUAL, MdmMatchResultEnum.GOLDEN_RECORD, "4", "I");

		final List<MdmLinkRevisionJson> allMdmLinkRevisions = List.of(mdmLinkRevisionJson1_v1, mdmLinkRevisionJson1_v2, mdmLinkRevisionJson2_v1, mdmLinkRevisionJson2_v2, mdmLinkRevisionJson2_v3, mdmLinkRevisionJson3, mdmLinkRevisionJson4, mdmLinkRevisionJson5, mdmLinkRevisionJson6, mdmLinkRevisionJson7, mdmLinkRevisionJson8, mdmLinkRevisionJson9);

		/**
		 * Search on:
		 *
		 * 1. goldenResourceIds:  1, 2
		 * 2. sourceIds:  C, H
		 * 
		 * Expected results:
		 *
		 * TODO:  expected results
		 */

		final IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);

		final List<MdmLinkRevisionJson> filteredMdmRevisions = allMdmLinkRevisions.stream()
//			.filter(revision -> theMdmGoldenResourceIds.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toUnmodifiableSet()).contains(revision.getMdmLink().getGoldenResourceId()))
//			.filter(revision -> theResourceIds.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toUnmodifiableSet()).contains(revision.getMdmLink().getSourceId()))
			.filter(revision -> filterMe(revision, goldenResourceIdsToUse, resourceIdsToUse))
			.sorted((revision1, revision2) -> {
				final int timestampCompare = revision2.getRevisionTimestamp().compareTo(revision1.getRevisionTimestamp());
				final int goldenResourceCompare = revision1.getMdmLink().getGoldenResourceId().compareTo(revision2.getMdmLink().getGoldenResourceId());

				if (goldenResourceCompare != 0) {
					return timestampCompare;
				}

				return goldenResourceCompare;
			})
			.collect(Collectors.toUnmodifiableList());

		ourLog.info("filteredMdmRevisions.isEmpty(): {}", filteredMdmRevisions.isEmpty());
		filteredMdmRevisions.forEach(revision -> ourLog.info("revision: {}", revision));

		filteredMdmRevisions.forEach(mdmLinkRevision -> {
				// TODO:  what is valueUri?  why do we need it?
				final IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retVal, "historical link");
				final MdmLinkJson mdmLink = mdmLinkRevision.getMdmLink();

				ParametersUtil.addPartString(myFhirContext, resultPart, "goldenResourceId", mdmLink.getGoldenResourceId());
				ParametersUtil.addPartString(myFhirContext, resultPart, "revisionNumber", mdmLinkRevision.getRevisionNumber().toString());
				ParametersUtil.addPartString(myFhirContext, resultPart, "revisionTimestamp", mdmLinkRevision.getRevisionTimestamp().toString());
				ParametersUtil.addPartString(myFhirContext, resultPart, "goldenResourceId", mdmLink.getGoldenResourceId());
				ParametersUtil.addPartString(myFhirContext, resultPart, "targetResourceId", mdmLink.getSourceId());
				ParametersUtil.addPartString(myFhirContext, resultPart, "matchResult", mdmLink.getMatchResult().name());
	//			ParametersUtil.addPartString(myFhirContext, resultPart, "linkSource", mdmLink.getLinkSource().name());
	//			ParametersUtil.addPartBoolean(myFhirContext, resultPart, "eidMatch", mdmLink.getEidMatch());
	//			ParametersUtil.addPartBoolean(myFhirContext, resultPart, "hadToCreateNewResource", mdmLink.getLinkCreatedNewResource());
	//			ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", mdmLink.getScore());
	//			ParametersUtil.addPartDecimal(myFhirContext, resultPart, "linkCreated", (double) mdmLink.getCreated().getTime());
	//			ParametersUtil.addPartDecimal(myFhirContext, resultPart, "linkUpdated", (double) mdmLink.getUpdated().getTime());
		});

		return retVal;
	}

	private boolean filterMe(MdmLinkRevisionJson mdmLinkRevisionJson, List<String> theMdmGoldenResourceIds, List<String> theResourceIds) {
		final boolean isWithinGoldenResource = theMdmGoldenResourceIds.contains(mdmLinkRevisionJson.getMdmLink().getGoldenResourceId());
		final boolean isWithTargetResource = theResourceIds.contains(mdmLinkRevisionJson.getMdmLink().getSourceId());

		return isWithinGoldenResource || isWithTargetResource;
	}

	// TODO: move this to a unit test
	private static MdmLinkRevisionJson buildMdmLinkRevisionJson(int theRevisionNumber, LocalDateTime theRevisionTimestamp, MdmLinkSourceEnum theMdmLinkSourceEnum, MdmMatchResultEnum theMdmMatchResultEnum, String theGoldenResourceId, String theSourceId) {
		final MdmLinkJson mdmLink = new MdmLinkJson();

		mdmLink.setLinkSource(theMdmLinkSourceEnum);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setGoldenResourceId(theGoldenResourceId);
		mdmLink.setSourceId(theSourceId);
		mdmLink.setCreated(new Date());
		mdmLink.setUpdated(new Date());

		return new MdmLinkRevisionJson(mdmLink, theRevisionNumber, theRevisionTimestamp);
	}

	@Operation(name = ProviderConstants.MDM_DUPLICATE_GOLDEN_RESOURCES, idempotent = true)
	public IBaseParameters getDuplicateGoldenResources(
		@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
		@OperationParam(name = PARAM_OFFSET, min = 0, max = 1, typeName = "integer")
			IPrimitiveType<Integer> theOffset,
		@Description(formalDefinition = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
		@OperationParam(name = Constants.PARAM_COUNT, min = 0, max = 1, typeName = "integer")
			IPrimitiveType<Integer> theCount,
			ServletRequestDetails theRequestDetails,
		@Description(formalDefinition = "This parameter controls the returned resource type.")
		@OperationParam(name = ProviderConstants.MDM_RESOURCE_TYPE, min = 0, max = 1, typeName = "string")
			IPrimitiveType<String> theResourceType) {

		MdmPageRequest mdmPageRequest = new MdmPageRequest(theOffset, theCount, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE);

		Page<MdmLinkJson> possibleDuplicates = myMdmControllerSvc.getDuplicateGoldenResources(createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.DUPLICATE_GOLDEN_RESOURCES, null), mdmPageRequest, theRequestDetails, extractStringOrNull(theResourceType));

		return parametersFromMdmLinks(possibleDuplicates, false, theRequestDetails, mdmPageRequest);
	}

	@Operation(name = ProviderConstants.MDM_NOT_DUPLICATE)
	public IBaseParameters notDuplicate(@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theGoldenResourceId,
													@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theResourceId,
													ServletRequestDetails theRequestDetails) {

		validateNotDuplicateParameters(theGoldenResourceId, theResourceId);
		myMdmControllerSvc.notDuplicateGoldenResource(theGoldenResourceId.getValue(), theResourceId.getValue(),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.NOT_DUPLICATE,
				getResourceType(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);

		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersBoolean(myFhirContext, retval, "success", true);
		return retval;
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_SUBMIT_OUT_PARAM_SUBMITTED, typeName = "integer")
	})
	public IBaseParameters mdmBatchOnAllSourceResources(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_RESOURCE_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theResourceType,
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theCriteria,
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		ServletRequestDetails theRequestDetails) {
		String criteria = convertStringTypeToString(theCriteria);
		String resourceType = convertStringTypeToString(theResourceType);
		long submittedCount;
		if (theRequestDetails.isPreferRespondAsync()) {
			List<String> urls = buildUrlsForJob(criteria, resourceType);
			return myMdmControllerSvc.submitMdmSubmitJob(urls, theBatchSize, theRequestDetails);
		} else {
			if (StringUtils.isNotBlank(resourceType)) {
				submittedCount = myMdmSubmitSvc.submitSourceResourceTypeToMdm(resourceType, criteria, theRequestDetails);
			} else {
				submittedCount = myMdmSubmitSvc.submitAllSourceTypesToMdm(criteria, theRequestDetails);
			}
			return buildMdmOutParametersWithCount(submittedCount);
		}

	}

	@Nonnull
	private List<String> buildUrlsForJob(String criteria, String resourceType) {
		List<String> urls = new ArrayList<>();
		if (StringUtils.isNotBlank(resourceType)) {
			String theUrl = resourceType + "?" + criteria;
			urls.add(theUrl);
		} else {
			myMdmSettings.getMdmRules().getMdmTypes()
				.stream()
				.map(type -> type + "?" + criteria)
				.forEach(urls::add);
		}
		return urls;
	}


	private String convertStringTypeToString(IPrimitiveType<String> theCriteria) {
		return theCriteria == null ? "" : theCriteria.getValueAsString();
	}


	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Patient", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, typeName = "integer")
	})
	public IBaseParameters mdmBatchPatientInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitSourceResourceToMdm(theIdParam, theRequest);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Patient", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, typeName = "integer")
	})
	public IBaseParameters mdmBatchPatientType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA, typeName = "string") IPrimitiveType<String> theCriteria,
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		ServletRequestDetails theRequest) {
		if (theRequest.isPreferRespondAsync()) {
			String theUrl = "Patient?";
			return myMdmControllerSvc.submitMdmSubmitJob(Collections.singletonList(theUrl), theBatchSize, theRequest);
		} else {
			String criteria = convertStringTypeToString(theCriteria);
			long submittedCount = myMdmSubmitSvc.submitPatientTypeToMdm(criteria, theRequest);
			return buildMdmOutParametersWithCount(submittedCount);
		}
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Practitioner", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, typeName = "integer")
	})
	public IBaseParameters mdmBatchPractitionerInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitSourceResourceToMdm(theIdParam, theRequest);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Practitioner", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, typeName = "integer")
	})
	public IBaseParameters mdmBatchPractitionerType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA, typeName = "string") IPrimitiveType<String> theCriteria,
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		ServletRequestDetails theRequest) {
		if (theRequest.isPreferRespondAsync()) {
			String theUrl = "Practitioner?";
			return myMdmControllerSvc.submitMdmSubmitJob(Collections.singletonList(theUrl), theBatchSize,  theRequest);
		} else {
			String criteria = convertStringTypeToString(theCriteria);
			long submittedCount = myMdmSubmitSvc.submitPractitionerTypeToMdm(criteria, theRequest);
			return buildMdmOutParametersWithCount(submittedCount);
		}
	}

	/**
	 * Helper function to build the out-parameters for all batch MDM operations.
	 */
	public IBaseParameters buildMdmOutParametersWithCount(long theCount) {
		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersLong(myFhirContext, retval, ProviderConstants.OPERATION_MDM_SUBMIT_OUT_PARAM_SUBMITTED, theCount);
		return retval;
	}

	private String getResourceType(String theParamName, IPrimitiveType<String> theResourceId) {
		if (theResourceId != null) {
			return getResourceType(theParamName, theResourceId.getValueAsString());
		} else {
			return MdmConstants.UNKNOWN_MDM_TYPES;
		}
	}

	private String getResourceType(String theParamName, IPrimitiveType<String> theResourceId, IPrimitiveType theResourceType) {
		return theResourceType != null ? theResourceType.getValueAsString() : getResourceType(theParamName, theResourceId);
	}

	private String getResourceType(String theParamName, String theResourceId) {
		if (StringUtils.isEmpty(theResourceId)) {
			return MdmConstants.UNKNOWN_MDM_TYPES;
		}
		IIdType idType = MdmControllerUtil.getGoldenIdDtOrThrowException(theParamName, theResourceId);
		return idType.getResourceType();
	}
}
