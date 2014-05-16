package ca.uhn.fhir.rest.method;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class QualifiedParamList extends ArrayList<String> {

	private static final long serialVersionUID = 1L;
	
	private String myQualifier;

	public QualifiedParamList() {
		super();
	}
	
	public QualifiedParamList(int theCapacity) {
		super(theCapacity);
	}

	public String getQualifier() {
		return myQualifier;
	}

	public void setQualifier(String theQualifier) {
		myQualifier = theQualifier;
	}

	public static QualifiedParamList singleton(String theQualifier, String theNextParam) {
		QualifiedParamList retVal = new QualifiedParamList(1);
		retVal.setQualifier(theQualifier);
		retVal.add(theNextParam);
		return retVal;
	}
	
	
	public static QualifiedParamList splitQueryStringByCommasIgnoreEscape(String theQualifier, String theParams){
		 QualifiedParamList retVal = new QualifiedParamList();
		 retVal.setQualifier(theQualifier);
		
		StringTokenizer tok = new StringTokenizer(theParams,",");
		String prev=null;
		while (tok.hasMoreElements()) {
			String str = tok.nextToken();
			if (prev!=null&&prev.endsWith("\\")) {
				int idx = retVal.size()-1;
				String existing = retVal.get(idx);
				retVal.set(idx, existing.substring(0, existing.length()-1) + "," + str);
			}else {
				retVal.add(str);
			}
			
			prev=str;
		}
		
		return retVal;
	}

	
}
