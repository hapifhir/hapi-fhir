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
import ca.uhn.fhir.empi.api.EmpiLinkJson;
import ca.uhn.fhir.empi.api.IEmpiControllerSvc;
import ca.uhn.fhir.empi.api.IEmpiExpungeSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiSubmitSvc;
import ca.uhn.fhir.empi.api.MatchedTarget;
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.codesystems.MatchGrade;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class EmpiProviderR4 extends BaseEmpiProvider {
	private final IEmpiControllerSvc myEmpiControllerSvc;
	private final IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	private final IEmpiExpungeSvc myMdmExpungeSvc;
	private final IEmpiSubmitSvc myMdmSubmitSvc;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public EmpiProviderR4(FhirContext theFhirContext, IEmpiControllerSvc theEmpiControllerSvc, IEmpiMatchFinderSvc theEmpiMatchFinderSvc, IEmpiExpungeSvc theMdmExpungeSvc, IEmpiSubmitSvc theMdmSubmitSvc) {
		super(theFhirContext);
		myEmpiControllerSvc = theEmpiControllerSvc;
		myEmpiMatchFinderSvc = theEmpiMatchFinderSvc;
		myMdmExpungeSvc = theMdmExpungeSvc;
		myMdmSubmitSvc = theMdmSubmitSvc;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, type = Patient.class)
	public Bundle match(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1) Patient thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		Bundle retVal = getMatchesAndPossibleMatchesForResource(thePatient, "Patient");

		return retVal;
	}

	@Operation(name = ProviderConstants.MDM_MATCH)
	public Bundle serverMatch(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1) IAnyResource theResource,
									  @OperationParam(name = ProviderConstants.MDM_RESOURCE_TYPE, min = 1, max = 1) StringType theResourceType
	) {
		if (theResource == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		Bundle retVal = getMatchesAndPossibleMatchesForResource(theResource, theResourceType.getValueNotNull());

		return retVal;
	}

	/**
	 * Helper method which will return a bundle of all Matches and Possible Matches.
	 * @param theResource
	 * @param theTheValueNotNull
	 * @return
	 */
	private Bundle getMatchesAndPossibleMatchesForResource(IAnyResource theResource, String theTheValueNotNull) {
		List<MatchedTarget> matches = myEmpiMatchFinderSvc.getMatchedTargets(theTheValueNotNull, theResource);

		matches.sort(Comparator.comparing((MatchedTarget m) -> m.getMatchResult().getNormalizedScore()).reversed());

		Bundle retVal = new Bundle();
		retVal.setType(Bundle.BundleType.SEARCHSET);
		retVal.setId(UUID.randomUUID().toString());
		retVal.getMeta().setLastUpdatedElement(InstantType.now());

		for (MatchedTarget next : matches) {
			boolean shouldKeepThisEntry = next.isMatch() || next.isPossibleMatch();
			if (!shouldKeepThisEntry) {
				continue;
			}

			Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
			entry.setResource((Resource) next.getTarget());
			entry.setSearch(toBundleEntrySearchComponent(next));

			retVal.addEntry(entry);
		}
		return retVal;
	}


	private Bundle.BundleEntrySearchComponent toBundleEntrySearchComponent(MatchedTarget theMatchedTarget) {
		Bundle.BundleEntrySearchComponent searchComponent = new Bundle.BundleEntrySearchComponent();
		searchComponent.setMode(Bundle.SearchEntryMode.MATCH);
		searchComponent.setScore(theMatchedTarget.getMatchResult().getNormalizedScore());

		MatchGrade matchGrade = getMatchGrade(theMatchedTarget);

		searchComponent.addExtension(EmpiConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE, new CodeType(matchGrade.toCode()));
		return searchComponent;
	}

	@Operation(name = ProviderConstants.MDM_MERGE_GOLDEN_RESOURCES)
	public IBaseResource mergeGoldenResources(@OperationParam(name=ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theFromGoldenResourceId,
															@OperationParam(name=ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theToGoldenResourceId,
															RequestDetails theRequestDetails) {
		validateMergeParameters(theFromGoldenResourceId, theToGoldenResourceId);

		return myEmpiControllerSvc.mergeGoldenResources(theFromGoldenResourceId.getValue(), theToGoldenResourceId.getValue(), createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.MERGE_PERSONS));
	}

	@Operation(name = ProviderConstants.MDM_UPDATE_LINK)
	public IBaseResource updateLink(@OperationParam(name=ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theGoldenResourceId,
								  @OperationParam(name=ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, min = 1, max = 1) StringType theResourceId,
								  @OperationParam(name=ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) StringType theMatchResult,
								  ServletRequestDetails theRequestDetails) {

		validateUpdateLinkParameters(theGoldenResourceId, theResourceId, theMatchResult);
		return myEmpiControllerSvc.updateLink(theGoldenResourceId.getValueNotNull(), theResourceId.getValue(), theMatchResult.getValue(), createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.UPDATE_LINK));
	}

	@Operation(name = ProviderConstants.MDM_CLEAR, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type=DecimalType.class)
	})
	public Parameters clearEmpiLinks(@OperationParam(name=ProviderConstants.MDM_CLEAR_TARGET_TYPE, min = 0, max = 1) StringType theTargetType,
												ServletRequestDetails theRequestDetails) {
		long resetCount;
		if (theTargetType == null || StringUtils.isBlank(theTargetType.getValue())) {
			resetCount = myMdmExpungeSvc.expungeAllEmpiLinks(theRequestDetails);
		} else {
			resetCount = myMdmExpungeSvc.expungeAllMdmLinksOfTargetType(theTargetType.getValueNotNull(), theRequestDetails);
		}
		Parameters parameters = new Parameters();
		parameters.addParameter().setName(ProviderConstants.OPERATION_MDM_CLEAR_OUT_PARAM_DELETED_COUNT)
			.setValue(new DecimalType(resetCount));
		return parameters;
	}


	@Operation(name = ProviderConstants.MDM_QUERY_LINKS, idempotent = true)
	public Parameters queryLinks(@OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 0, max = 1) StringType theGoldenResourceId,
									 @OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 0, max = 1) StringType theResourceId,
									 @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1) StringType theMatchResult,
									 @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_LINK_SOURCE, min = 0, max = 1) StringType theLinkSource,
									 ServletRequestDetails theRequestDetails) {

		Stream<EmpiLinkJson> empiLinkJson = myEmpiControllerSvc.queryLinks(extractStringOrNull(theGoldenResourceId), extractStringOrNull(theResourceId), extractStringOrNull(theMatchResult), extractStringOrNull(theLinkSource), createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.QUERY_LINKS));
		return (Parameters) parametersFromEmpiLinks(empiLinkJson, true);
	}

	@Operation(name = ProviderConstants.MDM_DUPLICATE_GOLDEN_RESOURCES, idempotent = true)
	public Parameters getDuplicateGoldenResources(ServletRequestDetails theRequestDetails) {
		Stream<EmpiLinkJson> possibleDuplicates = myEmpiControllerSvc.getDuplicateGoldenResources(createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.DUPLICATE_GOLDEN_RESOURCES));
		return (Parameters) parametersFromEmpiLinks(possibleDuplicates, false);
	}

	@Operation(name = ProviderConstants.MDM_NOT_DUPLICATE)
	public Parameters notDuplicate(@OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theGoldenResourceId,
											 @OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 1, max = 1) StringType theResourceId,
											 ServletRequestDetails theRequestDetails) {

		validateNotDuplicateParameters(theGoldenResourceId, theResourceId);
		myEmpiControllerSvc.notDuplicateGoldenResource(theGoldenResourceId.getValue(), theResourceId.getValue(), createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.NOT_DUPLICATE));

		Parameters retval = (Parameters) ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersBoolean(myFhirContext, retval, "success", true);
		return retval;
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type= IntegerType.class)
	})
	public Parameters empiBatchOnAllTargets(
		//TODO GGG MDM: also have to take it an optional resourceType here, to clarify which resources should have MDM run on them.
		@OperationParam(name= ProviderConstants.MDM_BATCH_RUN_RESOURCE_TYPE, min = 0 , max = 1) StringType theResourceType,
		@OperationParam(name= ProviderConstants.MDM_BATCH_RUN_CRITERIA,min = 0 , max = 1) StringType theCriteria,
		ServletRequestDetails theRequestDetails) {
		String criteria = convertStringTypeToString(theCriteria);
		String resourceType = convertStringTypeToString(theResourceType);

		long submittedCount;
		if (resourceType != null) {
			submittedCount  = myMdmSubmitSvc.submitTargetTypeToEmpi(resourceType, criteria);
		} else {
			submittedCount  = myMdmSubmitSvc.submitAllTargetTypesToEmpi(criteria);

		}
		return buildMdmOutParametersWithCount(submittedCount);
	}

	private String convertStringTypeToString(StringType theCriteria) {
		return theCriteria == null ? null : theCriteria.getValueAsString();
	}


	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Patient.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters empiBatchPatientInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitTargetToMdm(theIdParam);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Patient.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters empiBatchPatientType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA) StringType theCriteria,
		RequestDetails theRequest) {
		String criteria = convertStringTypeToString(theCriteria);
		long submittedCount = myMdmSubmitSvc.submitPatientTypeToMdm(criteria);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Practitioner.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters empiBatchPractitionerInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitTargetToMdm(theIdParam);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Practitioner.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters empiBatchPractitionerType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA) StringType theCriteria,
		RequestDetails theRequest) {
		String criteria = convertStringTypeToString(theCriteria);
		long submittedCount = myMdmSubmitSvc.submitPractitionerTypeToMdm(criteria);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	/**
	 * Helper function to build the out-parameters for all batch EMPI operations.
	 */
	private Parameters buildMdmOutParametersWithCount(long theCount) {
		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName(ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT)
			.setValue(new DecimalType(theCount));
		return parameters;
	}

	@Nonnull
	protected MatchGrade getMatchGrade(MatchedTarget theTheMatchedTarget) {
		MatchGrade matchGrade = MatchGrade.PROBABLE;
		if (theTheMatchedTarget.isMatch()) {
			matchGrade = MatchGrade.CERTAIN;
		} else if (theTheMatchedTarget.isPossibleMatch()) {
			matchGrade = MatchGrade.POSSIBLE;
		}
		return matchGrade;
	}
}
