package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class ConditionalParamBinder implements IParameter {

	private RestOperationTypeEnum myOperationType;
	private boolean mySupportsMultiple;

	ConditionalParamBinder(RestOperationTypeEnum theOperationType, boolean theSupportsMultiple) {
		Validate.notNull(theOperationType, "theOperationType can not be null");
		myOperationType = theOperationType;
		mySupportsMultiple = theSupportsMultiple;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null || theInnerCollectionType != null || theParameterType.equals(String.class) == false) {
			throw new ConfigurationException("Parameters annotated with @" + ConditionalUrlParam.class.getSimpleName()  + " must be of type String, found incorrect parameteter in method \"" + theMethod + "\"");
		}
	}

	public boolean isSupportsMultiple() {
		return mySupportsMultiple;
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		throw new UnsupportedOperationException("Can not use @" + getClass().getName() + " annotated parameters in client");
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		return theRequest.getConditionalUrl(myOperationType);
	}

}
