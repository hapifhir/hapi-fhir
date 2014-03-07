package ca.uhn.fhir.model.primitive;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "uri")
public class UriDt extends BasePrimitive<URI> {

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
			return myValue.toASCIIString();
		}
	}

	@Override
	public String toString() {
		return getValueAsString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	/**
	 * Compares the given string to the string representation of this URI. In many cases it is preferable
	 * to use this instead of the standard {@link #equals(Object)} method, since that method returns <code>false</code>
	 * unless it is passed an instance of {@link UriDt}
	 */
	public boolean equals(String theString) {
		return StringUtils.equals(getValueAsString(), theString);
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
