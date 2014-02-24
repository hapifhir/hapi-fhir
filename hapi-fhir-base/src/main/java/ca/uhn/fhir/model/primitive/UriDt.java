package ca.uhn.fhir.model.primitive;

import java.net.URI;
import java.net.URISyntaxException;

import ca.uhn.fhir.model.api.BaseDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "uri")
public class UriDt extends BaseDatatype implements IPrimitiveDatatype<URI> {

	private URI myValue;

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

}
