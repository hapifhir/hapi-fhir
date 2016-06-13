package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.defaultString;

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.util.ReflectionUtil;

final class FhirPrimitiveBinder extends BaseJavaPrimitiveBinder<IPrimitiveType<?>> {
	
	private Class<IPrimitiveType<?>> myType;

	FhirPrimitiveBinder(Class<IPrimitiveType<?>> theType) {
		myType = theType;
	}

	@Override
	protected String doEncode(IPrimitiveType<?> theString) {
		return theString.getValueAsString();
	}

	@Override
	protected IPrimitiveType<?> doParse(String theString) {
		IPrimitiveType<?> instance = ReflectionUtil.newInstance(myType);
		instance.setValueAsString(theString);
		return instance;
	}


}
