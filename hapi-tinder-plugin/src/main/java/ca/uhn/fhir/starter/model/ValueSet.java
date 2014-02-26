package ca.uhn.fhir.starter.model;

import java.util.ArrayList;
import java.util.List;

public class ValueSet {

	private List<Code> myCodes = new ArrayList<Code>();
	
	public void addConcept(String theCode, String theText, String theDefinition) {
		myCodes.add(new Code(theCode, theText, theDefinition));
	}

	public List<Code> getCodes() {
		return myCodes;
	}

	public static class Code
	{

		private String myCode;
		private String myDefinition;
		private String myText;

		public Code(String theCode, String theText, String theDefinition) {
			myCode =theCode;
			myText = theText;
			myDefinition = theDefinition;
		}

		public String getCode() {
			return myCode;
		}

		public String getDefinition() {
			return myDefinition;
		}

		public String getText() {
			return myText;
		}
		
	}
	
}
