package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleSetter {

	private String myDatatype;
	private List<Parameter> myParameters = new ArrayList<Parameter>();
	private String mySuffix;

	public String getDatatype() {
		return myDatatype;
	}

	public List<Parameter> getParameters() {
		return myParameters;
	}

	public String getSuffix() {
		return mySuffix;
	}

	public void setDatatype(String theDatatype) {
		myDatatype = theDatatype;
	}

	public void setSuffix(String theSuffix) {
		mySuffix=theSuffix;
	}

	public static class Parameter {

		private String myDatatype;
		private String myParameter;

		public String getDatatype() {
			return myDatatype;
		}

		public String getParameter() {
			return myParameter;
		}

		public void setDatatype(String theDatatype) {
			myDatatype = theDatatype;
		}

		public void setParameter(String theParameter) {
			myParameter = theParameter;
		}

	}

}
