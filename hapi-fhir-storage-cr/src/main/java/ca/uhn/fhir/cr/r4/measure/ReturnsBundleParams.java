package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.OperationEmbeddedType;
import ca.uhn.fhir.rest.annotation.OperationParam;

import java.util.StringJoiner;

@OperationEmbeddedType
public class ReturnsBundleParams {
	@OperationParam(name = "string")
	// LUKETODO:  do I always need to make it a BooleanType and not a Boolean?
	private String myString;

	public ReturnsBundleParams(String myString) {
		this.myString = myString;
	}

	public String getString() {
		return myString;
	}

	public void setString(String myString) {
		this.myString = myString;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ReturnsBundleParams.class.getSimpleName() + "[", "]")
			.add("myString='" + myString + "'")
			.toString();
	}
}
