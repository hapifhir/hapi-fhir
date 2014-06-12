package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;

public class SearchParameterMap extends HashMap<String, List<List<IQueryParameterType>>> {

	private static final long serialVersionUID = 1L;

	private Set<Include> myIncludes;

	public void add(String theName, IQueryParameterAnd theAnd) {
		if (theAnd == null) {
			return;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<IQueryParameterType>>());
		}

		for (IQueryParameterOr next : theAnd.getValuesAsQueryTokens()) {
			if (next == null) {
				continue;
			}
			get(theName).add(next.getValuesAsQueryTokens());
		}
	}

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

	public Set<Include> getIncludes() {
		if (myIncludes == null) {
			myIncludes = new HashSet<Include>();
		}
		return myIncludes;
	}

	public void setIncludes(Set<Include> theIncludes) {
		myIncludes = theIncludes;
	}

	public void addInclude(Include theInclude) {
		getIncludes().add(theInclude);
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this);
		if (isEmpty() == false) {
			b.append("params", super.toString());
		}
		if (getIncludes().isEmpty() == false) {
			b.append("includes", getIncludes());
		}
		return b.toString();
	}

}
