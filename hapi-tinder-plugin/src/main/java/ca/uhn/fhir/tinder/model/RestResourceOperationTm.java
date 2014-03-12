package ca.uhn.fhir.tinder.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceOperation;

public class RestResourceOperationTm {

	private String myName;
	private String myDocumentation;

	public RestResourceOperationTm(RestResourceOperation theOp) {
		myName = theOp.getCode().getValue();
		myDocumentation = theOp.getDocumentation().getValue();
	}

	public String getName() {
		return myName;
	}

	public boolean isHasDocumentation() {
		return StringUtils.isNotBlank(myDocumentation);
	}
	
	public String getNameCapitalized() {
		return WordUtils.capitalize(myName);
	}
	
	public String getDocumentation() {
		return myDocumentation;
	}

	public void setName(String theName) {
		myName = theName;
	}
	
}
