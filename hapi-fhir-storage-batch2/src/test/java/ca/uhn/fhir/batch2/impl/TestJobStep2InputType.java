package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestJobStep2InputType implements IModelJson {

	/**
	 * Constructor
	 */
	public TestJobStep2InputType() {
	}

	/**
	 * Constructor
	 */
	public TestJobStep2InputType(String theData1, String theData2) {
		myData1 = theData1;
		myData2 = theData2;
	}

	@JsonProperty("data1")
	private String myData1;
	@JsonProperty("data2")
	private String myData2;

	public String getData1() {
		return myData1;
	}

	public void setData1(String theData1) {
		myData1 = theData1;
	}

	public String getData2() {
		return myData2;
	}

	public void setData2(String theData2) {
		myData2 = theData2;
	}

}
