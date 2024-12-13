/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.merge.MergeOperationInputParameters;
import ca.uhn.fhir.jpa.provider.merge.MergeOperationOutcome;
import ca.uhn.fhir.jpa.provider.merge.PatientMergeOperationInputParameters;
import ca.uhn.fhir.jpa.provider.merge.ResourceMergeService;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.IdentifierUtil;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJpaResourceProviderPatient<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IReplaceReferencesSvc myReplaceReferencesSvc;

	@Autowired
	private IHapiTransactionService myHapiTransactionService;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	/**
	 * Patient/123/$everything
	 */
	@Operation(
			name = JpaConstants.OPERATION_EVERYTHING,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-everything",
			idempotent = true,
			bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientInstanceEverything(
			jakarta.servlet.http.HttpServletRequest theServletRequest,
			@IdParam IIdType theId,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
					@OperationParam(name = Constants.PARAM_COUNT, typeName = "unsignedInt")
					IPrimitiveType<Integer> theCount,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
					@OperationParam(name = Constants.PARAM_OFFSET, typeName = "unsignedInt")
					IPrimitiveType<Integer> theOffset,
			@Description(
							shortDefinition =
									"Only return resources which were last updated as specified by the given range")
					@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
					DateRangeParam theLastUpdated,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_CONTENT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theContent,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TEXT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theNarrative,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theFilter,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TYPE,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypes,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(name = Constants.PARAM_MDM, min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theMdmExpand,
			@Sort SortSpec theSortSpec,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			PatientEverythingParameters everythingParams = new PatientEverythingParameters();
			everythingParams.setCount(theCount);
			everythingParams.setOffset(theOffset);
			everythingParams.setLastUpdated(theLastUpdated);
			everythingParams.setSort(theSortSpec);
			everythingParams.setContent(toStringAndList(theContent));
			everythingParams.setNarrative(toStringAndList(theNarrative));
			everythingParams.setFilter(toStringAndList(theFilter));
			everythingParams.setTypes(toStringAndList(theTypes));
			everythingParams.setMdmExpand(resolveNullValue(theMdmExpand));

			return ((IFhirResourceDaoPatient<?>) getDao())
					.patientInstanceEverything(theServletRequest, theRequestDetails, everythingParams, theId);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Patient/$everything
	 */
	@Operation(
			name = JpaConstants.OPERATION_EVERYTHING,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-everything",
			idempotent = true,
			bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientTypeEverything(
			jakarta.servlet.http.HttpServletRequest theServletRequest,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
					@OperationParam(name = Constants.PARAM_COUNT, typeName = "unsignedInt")
					IPrimitiveType<Integer> theCount,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
					@OperationParam(name = Constants.PARAM_OFFSET, typeName = "unsignedInt")
					IPrimitiveType<Integer> theOffset,
			@Description(
							shortDefinition =
									"Only return resources which were last updated as specified by the given range")
					@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
					DateRangeParam theLastUpdated,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_CONTENT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theContent,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TEXT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theNarrative,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theFilter,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TYPE,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypes,
			@Description(shortDefinition = "Filter the resources to return based on the patient ids provided.")
					@OperationParam(
							name = Constants.PARAM_ID,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "id")
					List<IIdType> theId,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(name = Constants.PARAM_MDM, min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theMdmExpand,
			@Sort SortSpec theSortSpec,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			PatientEverythingParameters everythingParams = new PatientEverythingParameters();
			everythingParams.setCount(theCount);
			everythingParams.setOffset(theOffset);
			everythingParams.setLastUpdated(theLastUpdated);
			everythingParams.setSort(theSortSpec);
			everythingParams.setContent(toStringAndList(theContent));
			everythingParams.setNarrative(toStringAndList(theNarrative));
			everythingParams.setFilter(toStringAndList(theFilter));
			everythingParams.setTypes(toStringAndList(theTypes));
			everythingParams.setMdmExpand(resolveNullValue(theMdmExpand));

			return ((IFhirResourceDaoPatient<?>) getDao())
					.patientTypeEverything(
							theServletRequest,
							theRequestDetails,
							everythingParams,
							toFlattenedPatientIdTokenParamList(theId));
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Patient/$merge
	 */
	@Operation(
			name = ProviderConstants.OPERATION_MERGE,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-merge")
	public IBaseParameters patientMerge(
			HttpServletRequest theServletRequest,
			HttpServletResponse theServletResponse,
			ServletRequestDetails theRequestDetails,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_SOURCE_PATIENT_IDENTIFIER)
					List<Identifier> theSourcePatientIdentifier,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_TARGET_PATIENT_IDENTIFIER)
					List<Identifier> theTargetPatientIdentifier,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_SOURCE_PATIENT, max = 1)
					IBaseReference theSourcePatient,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_TARGET_PATIENT, max = 1)
					IBaseReference theTargetPatient,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PREVIEW, typeName = "boolean", max = 1)
					IPrimitiveType<Boolean> thePreview,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_DELETE_SOURCE, typeName = "boolean", max = 1)
					IPrimitiveType<Boolean> theDeleteSource,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_RESULT_PATIENT, max = 1)
					IBaseResource theResultPatient,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_BATCH_SIZE, typeName = "unsignedInt")
					IPrimitiveType<Integer> theBatchSize) {

		startRequest(theServletRequest);
		int batchSize = defaultIfNull(
				IPrimitiveType.toValueOrNull(theBatchSize), myStorageSettings.getMaxTransactionEntriesForWrite());
		if (batchSize > myStorageSettings.getMaxTransactionEntriesForWrite()) {
			batchSize = myStorageSettings.getMaxTransactionEntriesForWrite();
		}

		try {
			MergeOperationInputParameters mergeOperationParameters = buildMergeOperationInputParameters(
					theSourcePatientIdentifier,
					theTargetPatientIdentifier,
					theSourcePatient,
					theTargetPatient,
					thePreview,
					theDeleteSource,
					theResultPatient,
					batchSize);

			IFhirResourceDaoPatient<Patient> patientDao = (IFhirResourceDaoPatient<Patient>) getDao();
			IFhirResourceDao<Task> taskDao = myDaoRegistry.getResourceDao(Task.class);
			ResourceMergeService resourceMergeService = new ResourceMergeService(
					patientDao,
					taskDao,
					myReplaceReferencesSvc,
					myHapiTransactionService,
					myRequestPartitionHelperSvc,
					myJobCoordinator);

			FhirContext fhirContext = patientDao.getContext();

			MergeOperationOutcome mergeOutcome =
					resourceMergeService.merge(mergeOperationParameters, theRequestDetails);

			theServletResponse.setStatus(mergeOutcome.getHttpStatusCode());
			return buildMergeOperationOutputParameters(fhirContext, mergeOutcome, theRequestDetails.getResource());
		} finally {
			endRequest(theServletRequest);
		}
	}

	private IBaseParameters buildMergeOperationOutputParameters(
			FhirContext theFhirContext, MergeOperationOutcome theMergeOutcome, IBaseResource theInputParameters) {

		IBaseParameters retVal = ParametersUtil.newInstance(theFhirContext);
		ParametersUtil.addParameterToParameters(
				theFhirContext, retVal, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT, theInputParameters);

		ParametersUtil.addParameterToParameters(
				theFhirContext,
				retVal,
				ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME,
				theMergeOutcome.getOperationOutcome());

		if (theMergeOutcome.getUpdatedTargetResource() != null) {
			ParametersUtil.addParameterToParameters(
					theFhirContext,
					retVal,
					OPERATION_MERGE_OUTPUT_PARAM_RESULT,
					theMergeOutcome.getUpdatedTargetResource());
		}

		if (theMergeOutcome.getTask() != null) {
			ParametersUtil.addParameterToParameters(
					theFhirContext,
					retVal,
					ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK,
					theMergeOutcome.getTask());
		}
		return retVal;
	}

	private MergeOperationInputParameters buildMergeOperationInputParameters(
			List<Identifier> theSourcePatientIdentifier,
			List<Identifier> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultPatient,
			int theBatchSize) {
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(theBatchSize);
		if (theSourcePatientIdentifier != null) {
			List<CanonicalIdentifier> sourceResourceIdentifiers = theSourcePatientIdentifier.stream()
					.map(IdentifierUtil::identifierDtFromIdentifier)
					.collect(Collectors.toList());
			mergeOperationParameters.setSourceResourceIdentifiers(sourceResourceIdentifiers);
		}
		if (theTargetPatientIdentifier != null) {
			List<CanonicalIdentifier> targetResourceIdentifiers = theTargetPatientIdentifier.stream()
					.map(IdentifierUtil::identifierDtFromIdentifier)
					.collect(Collectors.toList());
			mergeOperationParameters.setTargetResourceIdentifiers(targetResourceIdentifiers);
		}
		mergeOperationParameters.setSourceResource(theSourcePatient);
		mergeOperationParameters.setTargetResource(theTargetPatient);
		mergeOperationParameters.setPreview(thePreview != null && thePreview.getValue());
		mergeOperationParameters.setDeleteSource(theDeleteSource != null && theDeleteSource.getValue());

		if (theResultPatient != null) {
			// pass in a copy of the result patient as we don't want it to be modified. It will be
			// returned back to the client as part of the response.
			mergeOperationParameters.setResultResource(((Patient) theResultPatient).copy());
		}

		return mergeOperationParameters;
	}

	/**
	 * Given a list of string types, return only the ID portions of any parameters passed in.
	 */
	private TokenOrListParam toFlattenedPatientIdTokenParamList(List<IIdType> theId) {
		TokenOrListParam retVal = new TokenOrListParam();
		if (theId != null) {
			for (IIdType next : theId) {
				if (isNotBlank(next.getValue())) {
					String[] split = next.getValueAsString().split(",");
					Arrays.stream(split).map(IdDt::new).forEach(id -> {
						retVal.addOr(new TokenParam(id.getIdPart()));
					});
				}
			}
		}

		return retVal.getValuesAsQueryTokens().isEmpty() ? null : retVal;
	}

	private StringAndListParam toStringAndList(List<IPrimitiveType<String>> theNarrative) {
		StringAndListParam retVal = new StringAndListParam();
		if (theNarrative != null) {
			for (IPrimitiveType<String> next : theNarrative) {
				if (isNotBlank(next.getValue())) {
					retVal.addAnd(new StringOrListParam().addOr(new StringParam(next.getValue())));
				}
			}
		}
		if (retVal.getValuesAsQueryTokens().isEmpty()) {
			return null;
		}
		return retVal;
	}

	private boolean resolveNullValue(IPrimitiveType<Boolean> theMdmExpand) {
		return theMdmExpand == null ? Boolean.FALSE : theMdmExpand.getValue();
	}
}
