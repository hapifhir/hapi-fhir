package ca.uhn.fhir.rest.param;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;

public class IdentifierListParam implements IQueryParameterOr {

	private List<IdentifierDt> myIdentifiers = new ArrayList<IdentifierDt>();
	
	/**
	 * Returns all identifiers associated with this list
	 */
	public List<IdentifierDt> getIdentifiers() {
		return myIdentifiers;
	}

	@Override
	public List<String> getValuesAsQueryTokens() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (IdentifierDt next : myIdentifiers) {
			retVal.add(next.getValueAsQueryToken());
		}
		return retVal;
	}

	/**
	 * Returns all identifiers associated with this list
	 */
	public void setIdentifiers(List<IdentifierDt> theIdentifiers) {
		myIdentifiers = theIdentifiers;
	}

	@Override
	public void setValuesAsQueryTokens(List<String> theParameters) {
		for (String string : theParameters) {
			IdentifierDt dt = new IdentifierDt();
			dt.setValueAsQueryToken(string);
			myIdentifiers.add(dt);
		}
	}

}
