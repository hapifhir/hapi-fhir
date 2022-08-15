package ca.uhn.fhir.jpa.partition;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.partition.PartitionLookupSvcImpl.validatePartitionIdSupplied;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.instance.model.api.IPrimitiveType.toValueOrNull;

/**
 * This HAPI FHIR Server Plain Provider class provides the following operations:
 * <ul>
 *    <li><code>partition-management-create-partition</code></li>
 *    <li><code>partition-management-update-partition</code></li>
 *    <li><code>partition-management-delete-partition</code></li>
 *    <li><code>partition-management-read-partition</code></li>
 *    <li><code>partition-management-list-partitions</code></li>
 * </ul>
 */
public class PartitionManagementProvider {

	@Autowired
	private FhirContext myCtx;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-create-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_CREATE_PARTITION)
	public IBaseParameters addPartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, min = 1, max = 1, typeName = "code") IPrimitiveType<String> thePartitionName,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, min = 0, max = 1, typeName = "string") IPrimitiveType<String> thePartitionDescription
	) {
		validatePartitionIdSupplied(myCtx, toValueOrNull(thePartitionId));

		PartitionEntity input = parseInput(thePartitionId, thePartitionName, thePartitionDescription);

		// Note: Input validation happens inside IPartitionLookupSvc
		PartitionEntity output = myPartitionLookupSvc.createPartition(input);

		IBaseParameters retVal = prepareOutput(output);

		return retVal;
	}

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-read-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_READ_PARTITION, idempotent = true)
	public IBaseParameters addPartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId
	) {
		validatePartitionIdSupplied(myCtx, toValueOrNull(thePartitionId));

		// Note: Input validation happens inside IPartitionLookupSvc
		PartitionEntity output = myPartitionLookupSvc.getPartitionById(thePartitionId.getValue());

		return prepareOutput(output);
	}

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-update-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_UPDATE_PARTITION)
	public IBaseParameters updatePartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, min = 1, max = 1, typeName = "code") IPrimitiveType<String> thePartitionName,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, min = 0, max = 1, typeName = "string") IPrimitiveType<String> thePartitionDescription
	) {
		validatePartitionIdSupplied(myCtx, toValueOrNull(thePartitionId));

		PartitionEntity input = parseInput(thePartitionId, thePartitionName, thePartitionDescription);

		// Note: Input validation happens inside IPartitionLookupSvc
		PartitionEntity output = myPartitionLookupSvc.updatePartition(input);

		IBaseParameters retVal = prepareOutput(output);

		return retVal;
	}

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-delete-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_DELETE_PARTITION)
	public IBaseParameters updatePartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId
	) {
		validatePartitionIdSupplied(myCtx, toValueOrNull(thePartitionId));
		
		myPartitionLookupSvc.deletePartition(thePartitionId.getValue());

		IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
		ParametersUtil.addParameterToParametersString(myCtx, retVal, "message", "Success");

		return retVal;
	}

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-list-partitions
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_LIST_PARTITIONS, idempotent = true)
	public IBaseParameters addPartitions(
		@ResourceParam IBaseParameters theRequest
	) {
		List<PartitionEntity> output = myPartitionLookupSvc.listPartitions();
		return prepareOutputList(output);
	}

	private IBaseParameters prepareOutput(PartitionEntity theOutput) {
		IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
		ParametersUtil.addParameterToParametersInteger(myCtx, retVal, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, theOutput.getId());
		ParametersUtil.addParameterToParametersCode(myCtx, retVal, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, theOutput.getName());
		if (isNotBlank(theOutput.getDescription())) {
			ParametersUtil.addParameterToParametersString(myCtx, retVal, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, theOutput.getDescription());
		}
		return retVal;
	}

	private IBaseParameters prepareOutputList(List<PartitionEntity> theOutput) {
		IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
		for (PartitionEntity partitionEntity : theOutput) {
			IBase resultPart = ParametersUtil.addParameterToParameters(myCtx, retVal, "partition");
			ParametersUtil.addPartInteger(myCtx, resultPart, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, partitionEntity.getId());
			ParametersUtil.addPartCode(myCtx, resultPart, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, partitionEntity.getName());
			if (isNotBlank(partitionEntity.getDescription())) {
				ParametersUtil.addPartString(myCtx, resultPart, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, partitionEntity.getDescription());
			}
		}
		return retVal;
	}

	@Nonnull
	private PartitionEntity parseInput(@OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId, @OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, min = 1, max = 1, typeName = "code") IPrimitiveType<String> thePartitionName, @OperationParam(name = ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, min = 0, max = 1, typeName = "string") IPrimitiveType<String> thePartitionDescription) {
		PartitionEntity input = new PartitionEntity();
		if (thePartitionId != null) {
			input.setId(thePartitionId.getValue());
		}
		if (thePartitionName != null) {
			input.setName(thePartitionName.getValue());
		}
		if (thePartitionDescription != null) {
			input.setDescription(thePartitionDescription.getValue());
		}
		return input;
	}

}
