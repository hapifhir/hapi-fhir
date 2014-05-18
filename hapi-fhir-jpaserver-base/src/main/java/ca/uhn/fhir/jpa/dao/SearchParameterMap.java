package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateRangeParam;

public class SearchParameterMap extends HashMap<String, List<List<IQueryParameterType>>> {

	private static final long serialVersionUID = 1L;

	public void add(String theName, IQueryParameterType theParam) {
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<IQueryParameterType>>());
		}
		ArrayList<IQueryParameterType> list = new ArrayList<IQueryParameterType>();
		list.add(theParam);
		get(theName).add(list);
	}

	public void add(String theName, IQueryParameterAnd theBirthdate) {
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<IQueryParameterType>>());
		}

		for (QualifiedParamList next : theBirthdate.getValuesAsQueryTokens()) {
			next.get
		}
	}

}
