package ca.uhn.fhir.model.api;

import java.util.HashMap;
import java.util.Map;

public abstract class ValueSetRegistry {

//	private Map<String, Class<? extends Enum>> 
	
//	private static final Map<Class<? extends ValueSetRegistry>, CodeMap> myClassToCodeMap = new HashMap<Class<? extends ValueSetRegistry>, ValueSetRegistry.CodeMap>();
//
//	public static final ValueSetRegistry OTHER = new OtherCode();
//	
//	private final String myCode;
//	private final int myOrdinal;
//
//	public ValueSetRegistry(String theCode, String theValueSetIdentifier) {
//		myCode = theCode;
//		myOrdinal = ourNextOrdinal++;
//
//		CodeMap codeMap = myClassToCodeMap.get(getClass());
//		if (codeMap == null) {
//			codeMap = new CodeMap(theValueSetIdentifier);
//			myClassToCodeMap.put(getClass(), codeMap);
//		}
//
//		codeMap.addCode(this);
//
//	}
//
//	public ValueSetRegistry() {
//		// TODO Auto-generated constructor stub
//	}
//
//	public ValueSetRegistry getCode(Class<? extends ValueSetRegistry> theType, String theCode) {
//		CodeMap codeMap = myClassToCodeMap.get(theType);
//		if (codeMap == null) {
//			
//		}
//	}
//	
//	public String getCode() {
//		return myCode;
//	}
//
//	public int getOrdinal() {
//		return myOrdinal;
//	}
//
//	private static final class OtherCode extends ValueSetRegistry {
//		private OtherCode() {
//			super();
//		}
//	}
//
//	private static final class OtherInstance extends ValueSetRegistry {
//		private OtherInstance(String theCode) {
//			super();
//			myCode = theCode;
//		}
//	}
//
//	private static class CodeMap {
//		private Map<String, ValueSetRegistry> myCodeMap = new HashMap<String, ValueSetRegistry>();
//		private String myValueSetIdentifier;
//		private int myNextOrdinal = 0;
//
//		public CodeMap(String theValueSetIdentifier) {
//			myValueSetIdentifier = theValueSetIdentifier;
//		}
//
//		public void addCode(ValueSetRegistry theValueSetEnumeration) {
//			myCodeMap.put(theValueSetEnumeration.getCode(), theValueSetEnumeration);
//		}
//
//		public int nextOrdinal() {
//			return myNextOrdinal++;
//		}
//	}

}
