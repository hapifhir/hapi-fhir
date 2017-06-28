package ca.uhn.fhir.rest.client.method;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TransactionParameter implements IParameter {

	// private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionParameter.class);
	private FhirContext myContext;
	private ParamStyle myParamStyle;

	public TransactionParameter(FhirContext theContext) {
		myContext = theContext;
	}

	private String createParameterTypeError(Method theMethod) {
		return "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName()
				+ " but is not of type List<" + IResource.class.getCanonicalName() + "> or Bundle";
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @"
					+ TransactionParam.class.getName() + " but can not be a collection of collections");
		}
		if (theParameterType.equals(Bundle.class)) {
			myParamStyle = ParamStyle.DSTU1_BUNDLE;
			if (theInnerCollectionType != null) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
		} else if (Modifier.isInterface(theParameterType.getModifiers()) == false && IBaseResource.class.isAssignableFrom(theParameterType)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> parameterType = (Class<? extends IBaseResource>) theParameterType;
			RuntimeResourceDefinition def = myContext.getResourceDefinition(parameterType);
			if ("Bundle".equals(def.getName())) {
				myParamStyle = ParamStyle.RESOURCE_BUNDLE;
			} else {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
		} else {
			if (theInnerCollectionType.equals(List.class) == false) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
			if (theParameterType.equals(IResource.class) == false) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
			myParamStyle = ParamStyle.RESOURCE_LIST;
		}
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource)
			throws InternalErrorException {
		// nothing

	}

	public ParamStyle getParamStyle() {
		return myParamStyle;
	}

	public enum ParamStyle {
		/** Old style bundle (defined in hapi-fhir-base) */
		DSTU1_BUNDLE,
		/** New style bundle (defined in hapi-fhir-structures-* as a resource definition itself */
		RESOURCE_BUNDLE,
		/** List of resources */
		RESOURCE_LIST
	}

}
