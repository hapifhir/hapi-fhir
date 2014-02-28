package ca.uhn.fhir.starter.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;

public class ValueSetTm {

	private String myClassName;
	private List<Code> myCodes = new ArrayList<Code>();
	private String myDescription;
	private String myId;
	private String myName;

	public void addConcept(String theCode, String theText, String theDefinition) {
		myCodes.add(new Code(theCode, theText, theDefinition));
	}

	public String getClassName() {
		return myClassName;
	}

	public List<Code> getCodes() {
		return myCodes;
	}

	public String getDescription() {
		return StringUtils.defaultString(myDescription);
	}

	public String getId() {
		return myId;
	}

	public String getName() {
		return StringUtils.defaultString(myName);
	}

	public void setClassName(String theClassName) {
		myClassName = theClassName;
	}

	public void setCodes(List<Code> theCodes) {
		myCodes = theCodes;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}
	
	public void setId(String theId) {
		myId = theId;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public static class Code
	{

		private String myCode;
		private String myDefinition;
		private String myDisplay;

		public Code(String theCode, String theDisplay, String theDefinition) {
			myCode = theCode.trim();
			myDisplay = theDisplay;
			myDefinition = theDefinition;
		}

		public String getCode() {
			return myCode;
		}

		public String getCodeEnumValue() {
			String retVal = myCode.toUpperCase().replace(' ', '_').replace('-', '_').replace('/', '_').replace('.', '_');
			if ("=".equals(retVal)) {
				retVal = "EQUALS";
			}
			if ("<=".equals(retVal)) {
				retVal = "LESSTHAN_OR_EQUALS";
			}
			if ("<".equals(retVal)) {
				retVal = "LESSTHAN";
			}
			if (">=".equals(retVal)) {
				retVal = "GREATERTHAN_OR_EQUALS";
			}
			if (">".equals(retVal)) {
				retVal = "GREATERTHAN";
			}

			if (!Character.isJavaIdentifierStart(retVal.charAt(0))) {
				retVal = '_' + retVal;
			}

			return retVal;
		}

		public String getDefinition() {
			return myDefinition;
		}

		public String getDisplay() {
			return myDisplay;
		}
		
	}
	
}
