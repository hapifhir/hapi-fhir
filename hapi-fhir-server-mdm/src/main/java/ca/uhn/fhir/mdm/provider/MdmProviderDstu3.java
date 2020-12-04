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
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmExpungeSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.codesystems.MatchGrade;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class MdmProviderDstu3 extends BaseMdmProvider {
	private final IMdmControllerSvc myMdmControllerSvc;
	private final IMdmMatchFinderSvc myMdmMatchFinderSvc;
	private final IMdmExpungeSvc myMdmExpungeSvc;
	private final IMdmSubmitSvc myMdmSubmitSvc;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public MdmProviderDstu3(FhirContext theFhirContext, IMdmControllerSvc theMdmControllerSvc, IMdmMatchFinderSvc theMdmMatchFinderSvc, IMdmExpungeSvc theMdmExpungeSvc, IMdmSubmitSvc theMdmSubmitSvc) {
		super(theFhirContext);
		myMdmControllerSvc = theMdmControllerSvc;
		myMdmMatchFinderSvc = theMdmMatchFinderSvc;
		myMdmExpungeSvc = theMdmExpungeSvc;
		myMdmSubmitSvc = theMdmSubmitSvc;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, type = Patient.class)
	public Bundle match(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1) Patient thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}

		return getMatchesAndPossibleMatchesForResource(thePatient, "Patient");
	}

	@Operation(name = ProviderConstants.MDM_MATCH)
	public Bundle serverMatch(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1) IAnyResource theResource,
									  @OperationParam(name = ProviderConstants.MDM_RESOURCE_TYPE, min = 1, max = 1) StringType theResourceType
	) {
		if (theResource == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		return getMatchesAndPossibleMatchesForResource(theResource, theResourceType.getValueNotNull());
	}

	/**
	 * Helper method which will return a bundle of all Matches and Possible Matches.
	 */
	private Bundle getMatchesAndPossibleMatchesForResource(IAnyResource theResource, String theResourceType) {
		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(theResourceType, theResource);
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

		searchComponent.addExtension(MdmConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE, new CodeType(matchGrade.toCode()));
		return searchComponent;
	}

	@Operation(name = ProviderConstants.MDM_MERGE_GOLDEN_RESOURCES)
	public IBaseResource mergeGoldenResources(@OperationParam(name=ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theFromGoldenResourceId,
															@OperationParam(name=ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theToGoldenResourceId,
															RequestDetails theRequestDetails) {
		validateMergeParameters(theFromGoldenResourceId, theToGoldenResourceId);

		return myMdmControllerSvc.mergeGoldenResources(theFromGoldenResourceId.getValue(), theToGoldenResourceId.getValue(),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES,
				getResourceType(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId))
		);
	}

	@Operation(name = ProviderConstants.MDM_UPDATE_LINK)
	public IBaseResource updateLink(@OperationParam(name=ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theGoldenResourceId,
								  @OperationParam(name=ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, min = 1, max = 1) StringType theResourceId,
								  @OperationParam(name=ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) StringType theMatchResult,
								  ServletRequestDetails theRequestDetails) {
		validateUpdateLinkParameters(theGoldenResourceId, theResourceId, theMatchResult);
		return myMdmControllerSvc.updateLink(theGoldenResourceId.getValueNotNull(), theResourceId.getValue(), theMatchResult.getValue(),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.UPDATE_LINK,
				getResourceType(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);
	}

	@Operation(name = ProviderConstants.MDM_CLEAR, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type= DecimalType.class)
	})
	public Parameters clearMdmLinks(@OperationParam(name=ProviderConstants.MDM_CLEAR_TARGET_TYPE, min = 0, max = 1) StringType theTargetType,
											  ServletRequestDetails theRequestDetails) {
		long resetCount;
		if (theTargetType == null || StringUtils.isBlank(theTargetType.getValue())) {
			resetCount = myMdmExpungeSvc.expungeAllMdmLinks(theRequestDetails);
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
										  @OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1) StringType theMatchResult,
										  @OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_LINK_SOURCE, min = 0, max = 1) StringType theLinkSource,
										  ServletRequestDetails theRequestDetails) {

		Stream<MdmLinkJson> mdmLinkJson = myMdmControllerSvc.queryLinks(extractStringOrNull(theGoldenResourceId), extractStringOrNull(theResourceId),
			extractStringOrNull(theMatchResult), extractStringOrNull(theLinkSource),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.QUERY_LINKS,
				getResourceType(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);
		return (Parameters) parametersFromMdmLinks(mdmLinkJson, true);
	}

	@Operation(name = ProviderConstants.MDM_DUPLICATE_GOLDEN_RESOURCES, idempotent = true)
	public Parameters getDuplicateGoldenResources(ServletRequestDetails theRequestDetails) {
		Stream<MdmLinkJson> possibleDuplicates = myMdmControllerSvc.getDuplicateGoldenResources(
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.DUPLICATE_GOLDEN_RESOURCES, (String) null)
		);
		return (Parameters) parametersFromMdmLinks(possibleDuplicates, false);
	}

	@Operation(name = ProviderConstants.MDM_NOT_DUPLICATE)
	public Parameters notDuplicate(@OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 1, max = 1) StringType theGoldenResourceId,
										    @OperationParam(name=ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 1, max = 1) StringType theResourceId,
										    ServletRequestDetails theRequestDetails) {

		validateNotDuplicateParameters(theGoldenResourceId, theResourceId);
		myMdmControllerSvc.notDuplicateGoldenResource(theGoldenResourceId.getValue(), theResourceId.getValue(),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.NOT_DUPLICATE,
				getResourceType(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);

		Parameters retval = (Parameters) ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersBoolean(myFhirContext, retval, "success", true);
		return retval;
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type= IntegerType.class)
	})
	public Parameters mdmBatchOnAllTargets(
		@OperationParam(name= ProviderConstants.MDM_BATCH_RUN_RESOURCE_TYPE, min = 0 , max = 1) StringType theResourceType,
		@OperationParam(name= ProviderConstants.MDM_BATCH_RUN_CRITERIA,min = 0 , max = 1) StringType theCriteria,
		ServletRequestDetails theRequestDetails) {
		String criteria = convertStringTypeToString(theCriteria);
		String resourceType = convertStringTypeToString(theResourceType);

		long submittedCount;
		if (resourceType != null) {
			submittedCount = myMdmSubmitSvc.submitTargetTypeToMdm(resourceType, criteria);
		} else {
			submittedCount = myMdmSubmitSvc.submitAllTargetTypesToMdm(criteria);
		}
		return buildMdmOutParametersWithCount(submittedCount);
	}

	private String convertStringTypeToString(StringType theCriteria) {
		return theCriteria == null ? null : theCriteria.getValueAsString();
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Patient.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters mdmBatchPatientInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitTargetToMdm(theIdParam);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Patient.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters mdmBatchPatientType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA) StringType theCriteria,
		RequestDetails theRequest) {
		String criteria = convertStringTypeToString(theCriteria);
		long submittedCount = myMdmSubmitSvc.submitPatientTypeToMdm(criteria);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Practitioner.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters mdmBatchPractitionerInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitTargetToMdm(theIdParam);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, type = Practitioner.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = IntegerType.class)
	})
	public Parameters mdmBatchPractitionerType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA) StringType theCriteria,
		RequestDetails theRequest) {
		String criteria = convertStringTypeToString(theCriteria);
		long submittedCount = myMdmSubmitSvc.submitPractitionerTypeToMdm(criteria);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	/**
	 * Helper function to build the out-parameters for all batch MDM operations.
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

	private String getResourceType(String theParamName, StringType theResourceId) {
		if (theResourceId != null) {
			IIdType idType = MdmControllerUtil.getGoldenIdDtOrThrowException(theParamName, theResourceId.getValueNotNull());
			return idType.getResourceType();
		} else {
			return MdmConstants.UNKNOWN_MDM_TYPES;
		}
	}
}
