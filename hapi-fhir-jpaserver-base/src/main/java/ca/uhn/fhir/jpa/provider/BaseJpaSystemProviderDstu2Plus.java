package ca.uhn.fhir.jpa.provider;

import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.util.ParametersUtil;

public abstract class BaseJpaSystemProviderDstu2Plus<T, MT> extends BaseJpaSystemProvider<T, MT> {

	//@formatter:off
	@Operation(name=MARK_ALL_RESOURCES_FOR_REINDEXING, idempotent=true, returnParameters= {
		@OperationParam(name="status")
	})
	//@formatter:on
	public IBaseResource markAllResourcesForReindexing() {
		int count = getDao().markAllResourcesForReindexing();
		
		IBaseParameters retVal = ParametersUtil.newInstance(getContext());
		
		IPrimitiveType<?> string = ParametersUtil.createString(getContext(), "Marked " + count + " resources");
		ParametersUtil.addParameterToParameters(getContext(), retVal, string, "status");
		
		return retVal;
	}

	
}
