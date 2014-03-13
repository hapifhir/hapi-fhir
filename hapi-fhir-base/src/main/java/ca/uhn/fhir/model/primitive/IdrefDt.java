package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.parser.DataFormatException;

public class IdrefDt extends BasePrimitive<String> {

	private IElement myTarget;
	private String myValue;

	public IElement getTarget() {
		return myTarget;
	}

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() throws DataFormatException {
		return myValue;
	}

	public void setTarget(IElement theTarget) {
		myTarget = theTarget;
	}

	@Override
	public void setValue(String theValue) throws DataFormatException {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		myValue = theValue;
	}

}
