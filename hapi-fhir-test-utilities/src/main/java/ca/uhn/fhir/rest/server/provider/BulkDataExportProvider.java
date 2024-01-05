/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.util.Date;

public class BulkDataExportProvider {

	public static final String PARAM_EXPORT_POLL_STATUS_JOB_ID = "_jobId";
	public static final String PARAM_EXPORT_OUTPUT_FORMAT = "_outputFormat";
	public static final String PARAM_EXPORT_TYPE = "_type";
	public static final String PARAM_EXPORT_SINCE = "_since";

	public static final String PARAM_EXPORT_TYPE_FILTER = "_typeFilter";
	public static final String PARAM_EXPORT_MDM = "_mdm";

	@Operation(
			name = ProviderConstants.OPERATION_EXPORT,
			global = false /* set to true once we can handle this */,
			manualResponse = true,
			idempotent = true)
	public void export(
			@OperationParam(name = PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(name = PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theTypeFilter,
			ServletRequestDetails theRequestDetails) {}

	@Operation(name = ProviderConstants.OPERATION_EXPORT, manualResponse = true, idempotent = true, typeName = "Group")
	public void groupExport(
			@IdParam IIdType theIdParam,
			@OperationParam(name = PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(name = PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theTypeFilter,
			@OperationParam(name = PARAM_EXPORT_MDM, min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theMdm,
			ServletRequestDetails theRequestDetails) {}

	@Operation(
			name = ProviderConstants.OPERATION_EXPORT,
			manualResponse = true,
			idempotent = true,
			typeName = "Patient")
	public void patientExport(
			@OperationParam(name = PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(name = PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theTypeFilter,
			ServletRequestDetails theRequestDetails) {}

	@Operation(name = ProviderConstants.OPERATION_EXPORT_POLL_STATUS, manualResponse = true, idempotent = true)
	public void exportPollStatus(
			@OperationParam(name = PARAM_EXPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theJobId,
			ServletRequestDetails theRequestDetails)
			throws IOException {}
}
