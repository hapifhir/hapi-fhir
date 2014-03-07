package ca.uhn.fhir.model.primitive;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "base64Binary")
public class Base64BinaryDt extends BasePrimitive<byte[]> {

	private byte[] myValue;

	/**
	 * Constructor
	 */
	public Base64BinaryDt() {
		super();
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public Base64BinaryDt(@SimpleSetter.Parameter(name="theBytes") byte[] theBytes) {
		setValue(theBytes);
	}

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
