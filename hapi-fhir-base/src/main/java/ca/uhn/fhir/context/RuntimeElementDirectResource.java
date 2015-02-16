package ca.uhn.fhir.context;

import org.hl7.fhir.instance.model.IBaseResource;

public class RuntimeElementDirectResource extends BaseRuntimeElementDefinition<IBaseResource> {

	public RuntimeElementDirectResource() {
		super("DirectChildResource", IBaseResource.class);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE;
	}

}
