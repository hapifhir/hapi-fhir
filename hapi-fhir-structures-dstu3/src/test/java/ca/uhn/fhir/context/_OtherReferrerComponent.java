package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.BackboneElement;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.StringType;

@Block
public class _OtherReferrerComponent extends BackboneElement implements org.hl7.fhir.instance.model.api.IBaseBackboneElement {

	/**
	 * name (extension)
	 */
	@Child(name = FIELD_NAME, min = 0, max = 1, type = {StringType.class})
	@Description(shortDefinition = "", formalDefinition = "Name of the referrer")
	@Extension(url = EXTURL_NAME, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.StringType ourName;
	public static final String EXTURL_NAME = "http://myfhir.dk/x/MyReferrer/otherReferrer/name";
	public static final String FIELD_NAME = "name";
	/**
	 * address (extension)
	 */
	@Child(name = FIELD_ADDRESS, min = 0, max = 1, type = {Address.class})
	@Description(shortDefinition = "", formalDefinition = "Address of the referrer")
	@Extension(url = EXTURL_ADDRESS, definedLocally = false, isModifier = false)
	protected Address ourAddress;
	public static final String EXTURL_ADDRESS = "http://myfhir.dk/x/MyReferrer/otherReferrer/address";
	public static final String FIELD_ADDRESS = "address";
	/**
	 * phoneNumber (extension)
	 */
	@Child(name = FIELD_PHONENUMBER, min = 0, max = 1, type = {StringType.class})
	@Description(shortDefinition = "", formalDefinition = "Phone number of the referrer")
	@Extension(url = EXTURL_PHONENUMBER, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.StringType ourPhoneNumber;
	public static final String EXTURL_PHONENUMBER = "http://myfhir.dk/x/MyReferrer/otherReferrer/phoneNumber";
	public static final String FIELD_PHONENUMBER = "phoneNumber";

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourName, ourAddress, ourPhoneNumber);
	}

	@Override
	public _OtherReferrerComponent copy() {
		_OtherReferrerComponent dst = new _OtherReferrerComponent();
		copyValues(dst);
		dst.ourName = ourName == null ? null : ourName.copy();
		dst.ourAddress = ourAddress == null ? null : ourAddress.copy();
		dst.ourPhoneNumber = ourPhoneNumber == null ? null : ourPhoneNumber.copy();
		return dst;
	}

	@Override
	public boolean equalsDeep(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsDeep(other)) {
			return false;
		}
		if (!(other instanceof _OtherReferrerComponent)) {
			return false;
		}
		_OtherReferrerComponent that = (_OtherReferrerComponent) other;
		return compareDeep(ourName, that.ourName, true) && compareDeep(ourAddress, that.ourAddress, true) && compareDeep(ourPhoneNumber, that.ourPhoneNumber, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof _OtherReferrerComponent)) {
			return false;
		}
		_OtherReferrerComponent that = (_OtherReferrerComponent) other;
		return compareValues(ourName, that.ourName, true) && compareValues(ourPhoneNumber, that.ourPhoneNumber, true);
	}

	public org.hl7.fhir.dstu3.model.StringType _getName() {
		if (ourName == null)
			ourName = new org.hl7.fhir.dstu3.model.StringType();
		return ourName;
	}

	public _OtherReferrerComponent _setName(org.hl7.fhir.dstu3.model.StringType theValue) {
		ourName = theValue;
		return this;
	}

	public Address _getAddress() {
		if (ourAddress == null)
			ourAddress = new Address();
		return ourAddress;
	}

	public _OtherReferrerComponent _setAddress(Address theValue) {
		ourAddress = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.StringType _getPhoneNumber() {
		if (ourPhoneNumber == null)
			ourPhoneNumber = new org.hl7.fhir.dstu3.model.StringType();
		return ourPhoneNumber;
	}

	public _OtherReferrerComponent _setPhoneNumber(org.hl7.fhir.dstu3.model.StringType theValue) {
		ourPhoneNumber = theValue;
		return this;
	}

}
