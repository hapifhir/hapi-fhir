package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class ValidateMethodBindingDstu2 extends OperationMethodBinding {

	public ValidateMethodBindingDstu2(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
			Validate theAnnotation) {
		super(theReturnResourceType, theReturnTypeFromRp, theMethod, theContext, theProvider, true, Constants.EXTOP_VALIDATE, theAnnotation.type());

		List<IParameter> newParams = new ArrayList<IParameter>();
		int idx = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				if (IBaseResource.class.isAssignableFrom(((ResourceParameter) next).getResourceType())) {
					Class<?> parameterType = theMethod.getParameterTypes()[idx];
					if (String.class.equals(parameterType) || EncodingEnum.class.equals(parameterType)) {
						newParams.add(next);
					} else {
						OperationParameter parameter = new OperationParameter(Constants.EXTOP_VALIDATE, Constants.EXTOP_VALIDATE_RESOURCE, 0, 1);
						parameter.initializeTypes(theMethod, null, null, parameterType);
						newParams.add(parameter);
					}
				} else {
					newParams.add(next);
				}
			} else {
				newParams.add(next);
			}
			idx++;
		}
		setParameters(newParams);

	}

}
