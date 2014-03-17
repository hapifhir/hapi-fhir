package ca.uhn.fhir.context;

import ca.uhn.fhir.model.dstu.composite.ContainedDt;

public class RuntimeElemContainedResources extends BaseRuntimeElementDefinition<ContainedDt> {

	public RuntimeElemContainedResources() {
		super("contained", ContainedDt.class);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.CONTAINED_RESOURCES;
	}

}
