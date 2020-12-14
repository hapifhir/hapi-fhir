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

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
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
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class MdmProviderDstu3Plus extends BaseMdmProvider {
	private final IMdmControllerSvc myMdmControllerSvc;
	private final IMdmMatchFinderSvc myMdmMatchFinderSvc;
	private final IMdmExpungeSvc myMdmExpungeSvc;
	private final IMdmSubmitSvc myMdmSubmitSvc;

	/**
	 * Constructor
	 * <p>
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public MdmProviderDstu3Plus(FhirContext theFhirContext, IMdmControllerSvc theMdmControllerSvc, IMdmMatchFinderSvc theMdmMatchFinderSvc, IMdmExpungeSvc theMdmExpungeSvc, IMdmSubmitSvc theMdmSubmitSvc) {
		super(theFhirContext);
		myMdmControllerSvc = theMdmControllerSvc;
		myMdmMatchFinderSvc = theMdmMatchFinderSvc;
		myMdmExpungeSvc = theMdmExpungeSvc;
		myMdmSubmitSvc = theMdmSubmitSvc;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, typeName = "Patient")
	public IBaseBundle match(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1, typeName = "Patient") IAnyResource thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		return getMatchesAndPossibleMatchesForResource(thePatient, "Patient");
	}

	@Operation(name = ProviderConstants.MDM_MATCH)
	public IBaseBundle serverMatch(@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1) IAnyResource theResource,
											 @OperationParam(name = ProviderConstants.MDM_RESOURCE_TYPE, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theResourceType
	) {
		if (theResource == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		return getMatchesAndPossibleMatchesForResource(theResource, theResourceType.getValueAsString());
	}

	/**
	 * Helper method which will return a bundle of all Matches and Possible Matches.
	 */
	private IBaseBundle getMatchesAndPossibleMatchesForResource(IAnyResource theResource, String theResourceType) {
		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(theResourceType, theResource);
		matches.sort(Comparator.comparing((MatchedTarget m) -> m.getMatchResult().getNormalizedScore()).reversed());

		IBaseBundle retVal = (IBaseBundle) myFhirContext.getResourceDefinition("Bundle").newInstance();
		InvocationUtils.invoke(retVal, "setType", InvocationUtils.getInnerEnumLiteral(retVal, "BundleType", "SEARCHSET"));
		InvocationUtils.invoke(retVal, "setId", UUID.randomUUID().toString());
		Object meta = InvocationUtils.invoke(retVal, "getMeta", null);

		Object now = myFhirContext.getElementDefinition("instant").newInstance();
		InvocationUtils.invoke(meta, "setLastUpdatedElement", now);

		for (MatchedTarget next : matches) {
			boolean shouldKeepThisEntry = next.isMatch() || next.isPossibleMatch();
			if (!shouldKeepThisEntry) {
				continue;
			}

			Object entry = InvocationUtils.newInnerClassInstance(retVal, "BundleEntryComponent");
			InvocationUtils.invoke(entry, "setResource", next.getTarget());
			InvocationUtils.invoke(entry, "setSearch", toBundleEntrySearchComponent(retVal, next));
			InvocationUtils.invoke(retVal, "addEntry", entry);
		}
		return retVal;
	}


	public Object toBundleEntrySearchComponent(IBaseBundle theBundle, MatchedTarget theMatchedTarget) {
		Object searchComponent = InvocationUtils.newInnerClassInstance(theBundle, "BundleEntrySearchComponent");
		Object match = InvocationUtils.getInnerEnumLiteral(theBundle, "SearchEntryMode", "MATCH");
		InvocationUtils.invoke(searchComponent, "setMode", match);
		InvocationUtils.invoke(searchComponent, "setScore", BigDecimal.valueOf(theMatchedTarget.getMatchResult().getNormalizedScore()));

		String matchGrade = getMatchGrade(theBundle, theMatchedTarget);
		Object codeType = myFhirContext.getElementDefinition("code").newInstance(matchGrade);

		InvocationUtils.invoke(searchComponent, "addExtension", MdmConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE, codeType);
		return searchComponent;
	}

	@Operation(name = ProviderConstants.MDM_MERGE_GOLDEN_RESOURCES)
	public IBaseResource mergeGoldenResources(@OperationParam(name = ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theFromGoldenResourceId,
															@OperationParam(name = ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, min = 1, max = 1, typeName = "string") IPrimitiveType<String> theToGoldenResourceId,
															RequestDetails theRequestDetails) {
		validateMergeParameters(theFromGoldenResourceId, theToGoldenResourceId);

		return myMdmControllerSvc.mergeGoldenResources(theFromGoldenResourceId.getValueAsString(), theToGoldenResourceId.getValueAsString(),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES,
				getResourceType(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId))
		);
	}

	@Operation(name = ProviderConstants.MDM_UPDATE_LINK)
	public IBaseResource updateLink(@OperationParam(name = ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, min = 1, max = 1) IPrimitiveType<String> theGoldenResourceId,
											  @OperationParam(name = ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, min = 1, max = 1) IPrimitiveType<String> theResourceId,
											  @OperationParam(name = ProviderConstants.MDM_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) IPrimitiveType<String> theMatchResult,
											  ServletRequestDetails theRequestDetails) {
		validateUpdateLinkParameters(theGoldenResourceId, theResourceId, theMatchResult);
		return myMdmControllerSvc.updateLink(theGoldenResourceId.getValueAsString(), theResourceId.getValue(), theMatchResult.getValue(),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.UPDATE_LINK,
				getResourceType(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);
	}

	@Operation(name = ProviderConstants.MDM_CLEAR, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, typeName = "decimal")
	})
	public IBaseParameters clearMdmLinks(@OperationParam(name = ProviderConstants.MDM_CLEAR_SOURCE_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theSourceType,
													 ServletRequestDetails theRequestDetails) {
		long resetCount;
		if (theSourceType == null || StringUtils.isBlank(theSourceType.getValue())) {
			resetCount = myMdmExpungeSvc.expungeAllMdmLinks(theRequestDetails);
		} else {
			resetCount = myMdmExpungeSvc.expungeAllMdmLinksOfSourceType(theSourceType.getValueAsString(), theRequestDetails);
		}

		IBaseParameters parameters = newParameters();
		setValue(parameters, ProviderConstants.OPERATION_MDM_CLEAR_OUT_PARAM_DELETED_COUNT, "decimal", "" + resetCount);
		return parameters;
	}


	@Operation(name = ProviderConstants.MDM_QUERY_LINKS, idempotent = true)
	public IBaseParameters queryLinks(@OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theGoldenResourceId,
												 @OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theResourceId,
												 @OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theMatchResult,
												 @OperationParam(name = ProviderConstants.MDM_QUERY_LINKS_LINK_SOURCE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theLinkSource,
												 ServletRequestDetails theRequestDetails) {

		Stream<MdmLinkJson> mdmLinkJson = myMdmControllerSvc.queryLinks(extractStringOrNull(theGoldenResourceId),
			extractStringOrNull(theResourceId), extractStringOrNull(theMatchResult), extractStringOrNull(theLinkSource),
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.QUERY_LINKS,
				getResourceType(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId))
		);
		return parametersFromMdmLinks(mdmLinkJson, true);
	}

	@Operation(name = ProviderConstants.MDM_DUPLICATE_GOLDEN_RESOURCES, idempotent = true)
	public IBaseParameters getDuplicateGoldenResources(ServletRequestDetails theRequestDetails) {
		Stream<MdmLinkJson> possibleDuplicates = myMdmControllerSvc.getDuplicateGoldenResources(
			createMdmContext(theRequestDetails, MdmTransactionContext.OperationType.DUPLICATE_GOLDEN_RESOURCES, (String) null)
		);
		return parametersFromMdmLinks(possibleDuplicates, false);
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
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, typeName = "integer")
	})
	public IBaseParameters mdmBatchOnAllSourceResources(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_RESOURCE_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theResourceType,
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theCriteria,
		ServletRequestDetails theRequestDetails) {
		String criteria = convertStringTypeToString(theCriteria);
		String resourceType = convertStringTypeToString(theResourceType);

		long submittedCount;
		if (resourceType != null) {
			submittedCount = myMdmSubmitSvc.submitSourceResourceTypeToMdm(resourceType, criteria);
		} else {
			submittedCount = myMdmSubmitSvc.submitAllSourceTypesToMdm(criteria);
		}
		return buildMdmOutParametersWithCount(submittedCount);
	}

	private String convertStringTypeToString(IPrimitiveType<String> theCriteria) {
		return theCriteria == null ? null : theCriteria.getValueAsString();
	}


	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Patient", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, typeName = "integer")
	})
	public IBaseParameters mdmBatchPatientInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitSourceResourceToMdm(theIdParam);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Patient", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, typeName = "integer")
	})
	public IBaseParameters mdmBatchPatientType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA, typeName = "string") IPrimitiveType<String> theCriteria,
		RequestDetails theRequest) {
		String criteria = convertStringTypeToString(theCriteria);
		long submittedCount = myMdmSubmitSvc.submitPatientTypeToMdm(criteria);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Practitioner", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, typeName = "integer")
	})
	public IBaseParameters mdmBatchPractitionerInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myMdmSubmitSvc.submitSourceResourceToMdm(theIdParam);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_SUBMIT, idempotent = false, typeName = "Practitioner", returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, typeName = "integer")
	})
	public IBaseParameters mdmBatchPractitionerType(
		@OperationParam(name = ProviderConstants.MDM_BATCH_RUN_CRITERIA, typeName = "string") IPrimitiveType<String> theCriteria,
		RequestDetails theRequest) {
		String criteria = convertStringTypeToString(theCriteria);
		long submittedCount = myMdmSubmitSvc.submitPractitionerTypeToMdm(criteria);
		return buildMdmOutParametersWithCount(submittedCount);
	}

	/**
	 * Helper function to build the out-parameters for all batch MDM operations.
	 */
	public IBaseParameters buildMdmOutParametersWithCount(long theCount) {
		IBaseParameters retVal = newParameters();
		setValue(retVal, ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, "decimal", "" + theCount);
		return retVal;
	}

	public IBaseParameters newParameters() {
		RuntimeResourceDefinition paramsResDefn = myFhirContext.getResourceDefinition("Parameters");
		IBaseParameters parameters = (IBaseParameters) paramsResDefn.newInstance();
		return parameters;
	}

	public <T extends IBaseBackboneElement> T setValue(IBaseParameters theParameters, String theName, String theValueType, Object theValue) {
		Object parameter = InvocationUtils.invoke(theParameters, "addParameter");
		InvocationUtils
			.invoke(parameter, "setName", theName);

		BaseRuntimeElementDefinition bred = myFhirContext.getElementDefinition(theValueType);
		InvocationUtils
			.invoke(parameter, "setValue", bred.newInstance(theValue));
		return (T) parameter;
	}

	@Nonnull
	protected String getMatchGrade(IBaseBundle theBundle, MatchedTarget theTheMatchedTarget) {
		String modelPackageName = theBundle.getClass().getPackage().getName();

		String enumLiteral = "PROBABLE";
		if (theTheMatchedTarget.isMatch()) {
			enumLiteral = "CERTAIN";
		} else if (theTheMatchedTarget.isPossibleMatch()) {
			enumLiteral = "POSSIBLE";
		}

		Enum gradeValue = InvocationUtils.getEnumValue(modelPackageName + ".codesystems.MatchGrade", enumLiteral);
		String gradeCode = InvocationUtils.invoke(gradeValue, "toCode");
		return gradeCode;
	}

	private String getResourceType(String theParamName, IPrimitiveType<String> theResourceId) {
		if (theResourceId != null) {
			return getResourceType(theParamName, theResourceId.getValueAsString());
		} else {
			return MdmConstants.UNKNOWN_MDM_TYPES;
		}
	}

	private String getResourceType(String theParamName, String theResourceId) {
		if (StringUtils.isEmpty(theResourceId)) {
			return MdmConstants.UNKNOWN_MDM_TYPES;
		}
		IIdType idType = MdmControllerUtil.getGoldenIdDtOrThrowException(theParamName, theResourceId);
		return idType.getResourceType();
	}
}
