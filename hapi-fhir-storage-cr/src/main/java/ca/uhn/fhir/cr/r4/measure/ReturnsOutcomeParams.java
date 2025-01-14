package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.OperationEmbeddedType;
import ca.uhn.fhir.rest.annotation.OperationParam;

import java.util.List;
import java.util.StringJoiner;

@OperationEmbeddedType
public class ReturnsOutcomeParams {
	@OperationParam(name = "oneString")
	// LUKETODO:  do I always need to make it a BooleanType and not a Boolean?
	private String myString;

	@OperationParam(name = "multStrings")
	private List<String> myStrings;

	public String getString() {
		return myString;
	}

	public void setString(String myString) {
		this.myString = myString;
	}

	public List<String> getStrings() {
		return myStrings;
	}

	public void setStrings(List<String> myStrings) {
		this.myStrings = myStrings;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ReturnsOutcomeParams.class.getSimpleName() + "[", "]")
			.add("myString='" + myString + "'")
			.add("myStrings=" + myStrings)
			.toString();
	}
}
