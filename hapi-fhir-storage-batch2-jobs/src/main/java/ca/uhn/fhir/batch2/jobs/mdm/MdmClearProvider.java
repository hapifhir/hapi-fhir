package ca.uhn.fhir.batch2.jobs.mdm;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.math.BigDecimal;
import java.util.List;

public class MdmClearProvider {
	// WIP KHS implement

	private final FhirContext myFhirContext;
	private final IJobCoordinator myJobCoordinator;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final MdmSettings myMdmSettings;

	/**
	 * Constructor
	 */
	public MdmClearProvider(FhirContext theFhirContext, IJobCoordinator theJobCoordinator, IRequestPartitionHelperSvc theRequestPartitionHelperSvc, MdmSettings theMdmSettings) {
		myFhirContext = theFhirContext;
		myJobCoordinator = theJobCoordinator;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myMdmSettings = theMdmSettings;
	}

	@Operation(name = ProviderConstants.OPERATION_MDM_CLEAR, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, typeName = "decimal")
	})
	public IBaseParameters clearMdmLinks(@OperationParam(name = ProviderConstants.OPERATION_MDM_CLEAR_RESOURCE_NAME, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theResourceNames,
													 @OperationParam(name = ProviderConstants.OPERATION_MDM_CLEAR_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
													 ServletRequestDetails theRequestDetails) {

		MdmClearJobParameters params = new MdmClearJobParameters();
		if (theResourceNames == null) {
			myMdmSettings.getMdmRules().getMdmTypes()
				.forEach(params::addResourceType);
		} else {
			theResourceNames
				.stream()
				.map(IPrimitiveType::getValue)
				.filter(StringUtils::isNotBlank)
				.forEach(params::addResourceType);
		}

		ReadPartitionIdRequestDetails details= new ReadPartitionIdRequestDetails(null, RestOperationTypeEnum.EXTENDED_OPERATION_SERVER, null, null, null);
		RequestPartitionId requestPartition = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, null, details);
		params.setRequestPartitionId(requestPartition);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(MdmClearAppCtx.JOB_MDM_CLEAR);
		request.setParameters(params);
		String id = myJobCoordinator.startInstance(request);

		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersString(myFhirContext, retVal, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, id);
		return retVal;
	}


}
