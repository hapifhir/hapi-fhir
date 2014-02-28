package ca.uhn.fhir.model.api;

import java.util.HashMap;
import java.util.Map;


public abstract class ValueSetEnumeration {

	private static final Map<Class<? extends ValueSetEnumeration>, CodeMap> myClassToCodeMap = new HashMap<>();
	
	private final String myCode;
	private final int myOrdinal;
	
	public ValueSetEnumeration(String theCode, String theValueSetIdentifier) {
		myCode = theCode;
		
		CodeMap codeMap = myClassToCodeMap.get(getClass());
		if (codeMap == null) {
			codeMap = new CodeMap(theValueSetIdentifier);
			myClassToCodeMap.put(getClass(), codeMap);
		}
		
		myOrdinal = codeMap.nextOrdinal(); 
		codeMap.addCode(this);
		
	}
	
	private static class CodeMap
	{
		private Map<String, ValueSetEnumeration> myCodeMap = new HashMap<String, ValueSetEnumeration>();
		private String myValueSetIdentifier;
		private int myNextOrdinal = 0;

		public CodeMap(String theValueSetIdentifier) {
			myValueSetIdentifier = theValueSetIdentifier;
		}

		public int getNextOrdinal() {
			return myNextOrdinal;
		}

		public int nextOrdinal() {
			return myNextOrdinal++;
		}

		public void addCode(ValueSetEnumeration theValueSetEnumeration) {
			myCodeMap.put(theValueSetEnumeration.getCode(), theValueSetEnumeration);
		}
		
	}

	public String getCode() {
		return myCode;
	}
	
}
