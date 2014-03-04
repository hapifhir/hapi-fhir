package ca.uhn.fhir.model.primitive;

import java.net.URI;
import java.net.URISyntaxException;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "uri")
public class UriDt  extends BaseElement implements IPrimitiveDatatype<URI> {

	private URI myValue;

	/**
	 * Create a new String
	 */
	public UriDt() {
		// nothing
	}
	
	/**
	 * Create a new String
	 */
	@SimpleSetter
	public UriDt(@SimpleSetter.Parameter(name="theUri") String theValue) {
		setValueAsString(theValue);
	}

	public URI getValue() {
		return myValue;
	}

	public void setValue(URI theValue) {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue==null) {
			myValue=null;
		}else {
			try {
				myValue = new URI(theValue);
			} catch (URISyntaxException e) {
				throw new DataFormatException("Unable to parse URI value", e);
			}
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		} else {
			return myValue.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UriDt other = (UriDt) obj;
		if (myValue == null) {
			if (other.myValue != null)
				return false;
		} else if (!myValue.equals(other.myValue))
			return false;
		return true;
	}

}
