package ca.uhn.fhir.model.api;

import java.util.HashMap;
import java.util.Map;

public abstract class ValueSetEnumeration {

	private static final Map<Class<? extends ValueSetEnumeration>, CodeMap> myClassToCodeMap = new HashMap<Class<? extends ValueSetEnumeration>, ValueSetEnumeration.CodeMap>();

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

	public String getCode() {
		return myCode;
	}

	public int getOrdinal() {
		return myOrdinal;
	}

	private static class CodeMap {
		private Map<String, ValueSetEnumeration> myCodeMap = new HashMap<String, ValueSetEnumeration>();
		private int myNextOrdinal = 0;
		private String myValueSetIdentifier;

		public CodeMap(String theValueSetIdentifier) {
			myValueSetIdentifier = theValueSetIdentifier;
		}

		public void addCode(ValueSetEnumeration theValueSetEnumeration) {
			myCodeMap.put(theValueSetEnumeration.getCode(), theValueSetEnumeration);
		}

		public int nextOrdinal() {
			return myNextOrdinal++;
		}

	}

}
