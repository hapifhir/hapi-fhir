package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestJobStep3InputType implements IModelJson {

	@JsonProperty("data3")
	private String myData3;
	@JsonProperty("data4")
	private String myData4;

	public String getData3() {
		return myData3;
	}

	public TestJobStep3InputType setData3(String theData1) {
		myData3 = theData1;
		return this;
	}

	public String getData4() {
		return myData4;
	}

	public TestJobStep3InputType setData4(String theData2) {
		myData4 = theData2;
		return this;
	}

}
