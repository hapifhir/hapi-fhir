package ca.uhn.fhir.rest.param;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.dstu.composite.CodingDt;

public class CodingListParam implements IQueryParameterOr, Iterable<CodingDt> {

	private List<CodingDt> myCodings = new ArrayList<CodingDt>();
	
	/**
	 * Returns all Codings associated with this list
	 */
	public List<CodingDt> getCodings() {
		if (myCodings == null) {
			myCodings = new ArrayList<CodingDt>();
		}
		return myCodings;
	}

	@Override
	public List<String> getValuesAsQueryTokens() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (CodingDt next : myCodings) {
			retVal.add(next.getValueAsQueryToken());
		}
		return retVal;
	}

	/**
	 * Returns all Codings associated with this list
	 */
	public void setCodings(List<CodingDt> theCodings) {
		myCodings = theCodings;
	}

	@Override
	public void setValuesAsQueryTokens(List<String> theParameters) {
		getCodings().clear();
		for (String string : theParameters) {
			CodingDt dt = new CodingDt();
			dt.setValueAsQueryToken(string);
			myCodings.add(dt);
		}
	}

	public void add(CodingDt theCodingDt) {
		myCodings.add(theCodingDt);
	}

	@Override
	public Iterator<CodingDt> iterator() {
		return getCodings().iterator();
	}

}
