package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJpaSystemProviderDstu2Plus<T, MT> extends BaseJpaSystemProvider<T, MT> {


	@Description("Marks all currently existing resources of a given type, or all resources of all types, for reindexing.")
	@Operation(name = MARK_ALL_RESOURCES_FOR_REINDEXING, idempotent = false, returnParameters = {
		@OperationParam(name = "status")
	})
	/**
	 * @deprecated
	 * @see ReindexProvider#Reindex(List, IPrimitiveType, RequestDetails)
	 */
	@Deprecated
	public IBaseResource markAllResourcesForReindexing(
		@OperationParam(name="type", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theType
	) {

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
	@Operation(name = PERFORM_REINDEXING_PASS, idempotent = false, returnParameters = {
		@OperationParam(name = "status")
	})
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

	/**
	 * $process-message
	 */
	@Description("Accept a FHIR Message Bundle for processing")
	@Operation(name = JpaConstants.OPERATION_PROCESS_MESSAGE, idempotent = false)
	public IBaseBundle processMessage(
		HttpServletRequest theServletRequest,
		RequestDetails theRequestDetails,

		@OperationParam(name = "content", min = 1, max = 1, typeName = "Bundle")
		@Description(formalDefinition = "The message to process (or, if using asynchronous messaging, it may be a response message to accept)")
			IBaseBundle theMessageToProcess
	) {

		startRequest(theServletRequest);
		try {
			return getDao().processMessage(theRequestDetails, theMessageToProcess);
		} finally {
			endRequest(theServletRequest);
		}

	}


}
