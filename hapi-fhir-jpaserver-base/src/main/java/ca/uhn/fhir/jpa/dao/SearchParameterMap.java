package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;

public class SearchParameterMap extends HashMap<String, List<List<IQueryParameterType>>> {

	private static final long serialVersionUID = 1L;

	public void add(String theName, IQueryParameterType theParam) {
		if (theParam == null) {
			return;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<IQueryParameterType>>());
		}
		ArrayList<IQueryParameterType> list = new ArrayList<IQueryParameterType>();
		list.add(theParam);
		get(theName).add(list);
	}

	public void add(String theName, IQueryParameterAnd theAnd) {
		if (theAnd==null) {
			return;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<IQueryParameterType>>());
		}

		for (IQueryParameterOr next : theAnd.getValuesAsQueryTokens()) {
			if (next==null) {
				continue;
			}
			get(theName).add(next.getValuesAsQueryTokens());
		}
	}

}
