package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.util.ProviderConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PartitionManagementProvider {

	@Autowired
	private FhirContext myCtx;
	@Autowired
	private IPartitionConfigSvc myPartitionConfigSvc;

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-add-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_ADD_PARTITION)
	public IBaseParameters addPartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, min = 1, max = 1, typeName = "code") IPrimitiveType<String> thePartitionName,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, min = 0, max = 1, typeName = "string") IPrimitiveType<String> thePartitionDescription
	) {

		PartitionEntity input = parseInput(thePartitionId, thePartitionName, thePartitionDescription);
		PartitionEntity output = myPartitionConfigSvc.createPartition(input);
		IBaseParameters retVal = prepareOutput(output);

		return retVal;
	}

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-update-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_ADD_PARTITION)
	public IBaseParameters updatePartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, min = 1, max = 1, typeName = "code") IPrimitiveType<String> thePartitionName,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, min = 0, max = 1, typeName = "string") IPrimitiveType<String> thePartitionDescription
	) {

		PartitionEntity input = parseInput(thePartitionId, thePartitionName, thePartitionDescription);
		PartitionEntity output = myPartitionConfigSvc.updatePartition(input);
		IBaseParameters retVal = prepareOutput(output);

		return retVal;
	}

	/**
	 * Add Partition:
	 * <code>
	 * $partition-management-delete-partition
	 * </code>
	 */
	@Operation(name = ProviderConstants.PARTITION_MANAGEMENT_ADD_PARTITION)
	public IBaseParameters updatePartition(
		@ResourceParam IBaseParameters theRequest,
		@OperationParam(name=ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, min = 1, max = 1, typeName = "integer") IPrimitiveType<Integer> thePartitionId
	) {

		myPartitionConfigSvc.deletePartition(thePartitionId.getValue());

		IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
		ParametersUtil.addParameterToParametersString(myCtx, retVal, "message", "Success");

		return retVal;
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

	@NotNull
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
