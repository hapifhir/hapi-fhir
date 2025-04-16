/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.jobs.merge.MergeResourceHelper;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static software.amazon.awssdk.utils.StringUtils.isBlank;

public final class JpaSystemProvider<T, MT> extends BaseJpaSystemProvider<T, MT> {
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Description(
			"Marks all currently existing resources of a given type, or all resources of all types, for reindexing.")
	@Operation(
			name = MARK_ALL_RESOURCES_FOR_REINDEXING,
			idempotent = false,
			returnParameters = {@OperationParam(name = "status")})
	/**
	 * @deprecated
	 * @see ReindexProvider#Reindex(List, IPrimitiveType, RequestDetails)
	 */
	@Deprecated
	public IBaseResource markAllResourcesForReindexing(
			@OperationParam(name = "type", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theType) {

		if (theType != null && isNotBlank(theType.getValueAsString())) {
			getResourceReindexingSvc().markAllResourcesForReindexing(theType.getValueAsString());
		} else {
			getResourceReindexingSvc().markAllResourcesForReindexing();
		}

		IBaseParameters retVal = ParametersUtil.newInstance(getContext());

		IPrimitiveType<?> string = ParametersUtil.createString(getContext(), "Marked resources");
		ParametersUtil.addParameterToParameters(getContext(), retVal, "status", string);

		return retVal;
	}

	@Description("Forces a single pass of the resource reindexing processor")
	@Operation(
			name = PERFORM_REINDEXING_PASS,
			idempotent = false,
			returnParameters = {@OperationParam(name = "status")})
	/**
	 * @deprecated
	 * @see ReindexProvider#Reindex(List, IPrimitiveType, RequestDetails)
	 */
	@Deprecated
	public IBaseResource performReindexingPass() {
		Integer count = getResourceReindexingSvc().runReindexingPass();

		IBaseParameters retVal = ParametersUtil.newInstance(getContext());

		IPrimitiveType<?> string;
		if (count == null) {
			string = ParametersUtil.createString(getContext(), "Index pass already proceeding");
		} else {
			string = ParametersUtil.createString(getContext(), "Indexed " + count + " resources");
		}
		ParametersUtil.addParameterToParameters(getContext(), retVal, "status", string);

		return retVal;
	}

	@Operation(name = JpaConstants.OPERATION_GET_RESOURCE_COUNTS, idempotent = true)
	@Description(
			shortDefinition =
					"Provides the number of resources currently stored on the server, broken down by resource type")
	public IBaseParameters getResourceCounts() {
		IBaseParameters retVal = ParametersUtil.newInstance(getContext());

		Map<String, Long> counts = getDao().getResourceCountsFromCache();
		counts = defaultIfNull(counts, Collections.emptyMap());
		counts = new TreeMap<>(counts);
		for (Map.Entry<String, Long> nextEntry : counts.entrySet()) {
			ParametersUtil.addParameterToParametersInteger(
					getContext(),
					retVal,
					nextEntry.getKey(),
					nextEntry.getValue().intValue());
		}

		return retVal;
	}

	@Operation(
			name = ProviderConstants.OPERATION_META,
			idempotent = true,
			returnParameters = {@OperationParam(name = "return", typeName = "Meta")})
	public IBaseParameters meta(RequestDetails theRequestDetails) {
		IBaseParameters retVal = ParametersUtil.newInstance(getContext());
		ParametersUtil.addParameterToParameters(
				getContext(), retVal, "return", getDao().metaGetOperation(theRequestDetails));
		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Transaction
	public IBaseBundle transaction(RequestDetails theRequestDetails, @TransactionParam IBaseBundle theResources) {
		startRequest(((ServletRequestDetails) theRequestDetails).getServletRequest());
		try {
			IFhirSystemDao<T, MT> dao = getDao();
			return (IBaseBundle) dao.transaction(theRequestDetails, (T) theResources);
		} finally {
			endRequest(((ServletRequestDetails) theRequestDetails).getServletRequest());
		}
	}

	@Operation(name = ProviderConstants.OPERATION_REPLACE_REFERENCES, global = true)
	@Description(
			value =
					"This operation searches for all references matching the provided id and updates them to references to the provided target-reference-id.",
			shortDefinition = "Repoints referencing resources to another resources instance")
	public IBaseParameters replaceReferences(
			@OperationParam(
							name = ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID,
							min = 1,
							typeName = "string")
					IPrimitiveType<String> theSourceId,
			@OperationParam(
							name = ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID,
							min = 1,
							typeName = "string")
					IPrimitiveType<String> theTargetId,
			@OperationParam(
							name = ProviderConstants.OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT,
							typeName = "unsignedInt")
					IPrimitiveType<Integer> theResourceLimit,
			ServletRequestDetails theServletRequest) {
		startRequest(theServletRequest);

		try {
			validateReplaceReferencesParams(theSourceId, theTargetId);

			int resourceLimit = MergeResourceHelper.setResourceLimitFromParameter(myStorageSettings, theResourceLimit);

			IdDt sourceId = new IdDt(theSourceId.getValue());
			IdDt targetId = new IdDt(theTargetId.getValue());
			RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
					theServletRequest, ReadPartitionIdRequestDetails.forRead(targetId));
			ReplaceReferencesRequest replaceReferencesRequest =
					new ReplaceReferencesRequest(sourceId, targetId, resourceLimit, partitionId, true);
			IBaseParameters retval =
					getReplaceReferencesSvc().replaceReferences(replaceReferencesRequest, theServletRequest);
			if (ParametersUtil.getNamedParameter(getContext(), retval, OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK)
					.isPresent()) {
				HttpServletResponse response = theServletRequest.getServletResponse();
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
			}
			return retval;
		} finally {
			endRequest(theServletRequest);
		}
	}

	private static void validateReplaceReferencesParams(
			IPrimitiveType<String> theSourceId, IPrimitiveType<String> theTargetId) {
		if (theSourceId == null || isBlank(theSourceId.getValue())) {
			throw new InvalidRequestException(Msg.code(2583) + "Parameter '"
					+ OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID + "' is blank");
		}

		if (theTargetId == null || isBlank(theTargetId.getValue())) {
			throw new InvalidRequestException(Msg.code(2584) + "Parameter '"
					+ OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID + "' is blank");
		}
	}
}
