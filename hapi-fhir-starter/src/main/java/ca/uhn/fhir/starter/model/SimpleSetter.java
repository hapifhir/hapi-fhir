package ca.uhn.fhir.starter.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleSetter {

	private List<Parameter> myParameters = new ArrayList<Parameter>();
	private String myDatatype;

	public String getDatatype() {
		return myDatatype;
	}

	public void setDatatype(String theDatatype) {
		myDatatype = theDatatype;
	}

	public List<Parameter> getParameters() {
		return myParameters;
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
