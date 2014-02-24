package ca.uhn.fhir.model.primitive;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "base64Binary")
public class Base64BinaryDt extends BasePrimitiveDatatype<byte[]> {

	private byte[] myValue;

	@Override
	public void setValueAsString(String theValue) {
		if (theValue == null) {
			myValue = null;
		} else {
			myValue = Base64.decodeBase64(theValue);
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		} else {
			return Base64.encodeBase64String(myValue);
		}
	}

	@Override
	public void setValue(byte[] theValue) {
		myValue = theValue;
	}

	@Override
	public byte[] getValue() {
		return myValue;
	}

}
