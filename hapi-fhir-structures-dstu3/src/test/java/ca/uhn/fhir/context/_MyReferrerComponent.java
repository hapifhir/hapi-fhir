package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.*;

@Block
public class _MyReferrerComponent extends BackboneElement implements org.hl7.fhir.instance.model.api.IBaseBackboneElement {

	/**
	 * referrerType (extension)
	 */
	@Child(name = FIELD_REFERRERTYPE, min = 1, max = 1, type = {StringType.class})
	@Description(shortDefinition = "", formalDefinition = "Type of the selected referrer")
	@Extension(url = EXTURL_REFERRERTYPE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.StringType ourReferrerType;
	public static final String EXTURL_REFERRERTYPE = "http://myfhir.dk/x/MyReferrer/referrerType";
	public static final String FIELD_REFERRERTYPE = "referrerType";
	/**
	 * hospitalReferrer (extension)
	 */
	@Child(name = FIELD_HOSPITALREFERRER, min = 0, max = 1, type = {CodeType.class})
	@Description(shortDefinition = "", formalDefinition = "Hospital department reference.")
	@Extension(url = EXTURL_HOSPITALREFERRER, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.CodeType ourHospitalReferrer;
	public static final String EXTURL_HOSPITALREFERRER = "http://myfhir.dk/x/MyReferrer/hospitalReferrer";
	public static final String FIELD_HOSPITALREFERRER = "hospitalReferrer";
	/**
	 * doctorReferrer (extension)
	 */
	@Child(name = FIELD_DOCTORREFERRER, min = 0, max = 1, type = {Practitioner.class})
	@Description(shortDefinition = "", formalDefinition = "Reference to a medical practitioner or a specialist doctor.")
	@Extension(url = EXTURL_DOCTORREFERRER, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Reference ourDoctorReferrer;
	public static final String EXTURL_DOCTORREFERRER = "http://myfhir.dk/x/MyReferrer/doctorReferrer";
	public static final String FIELD_DOCTORREFERRER = "doctorReferrer";
	/**
	 * otherReferrer (extension)
	 */
	@Child(name = FIELD_OTHERREFERRER, min = 0, max = 1, type = {_OtherReferrerComponent.class})
	@Description(shortDefinition = "", formalDefinition = "Name, address and phone number of the referrer.")
	@Extension(url = EXTURL_OTHERREFERRER, definedLocally = false, isModifier = false)
	protected _OtherReferrerComponent ourOtherReferrer;
	public static final String EXTURL_OTHERREFERRER = "http://myfhir.dk/x/MyReferrer/otherReferrer";
	public static final String FIELD_OTHERREFERRER = "otherReferrer";

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourReferrerType, ourHospitalReferrer, ourDoctorReferrer, ourOtherReferrer);
	}

	@Override
	public _MyReferrerComponent copy() {
		_MyReferrerComponent dst = new _MyReferrerComponent();
		copyValues(dst);
		dst.ourReferrerType = ourReferrerType == null ? null : ourReferrerType.copy();
		dst.ourHospitalReferrer = ourHospitalReferrer == null ? null : ourHospitalReferrer.copy();
		dst.ourDoctorReferrer = ourDoctorReferrer == null ? null : ourDoctorReferrer.copy();
		dst.ourOtherReferrer = ourOtherReferrer == null ? null : ourOtherReferrer.copy();
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
		if (!(other instanceof _MyReferrerComponent)) {
			return false;
		}
		_MyReferrerComponent that = (_MyReferrerComponent) other;
		return compareDeep(ourReferrerType, that.ourReferrerType, true) && compareDeep(ourHospitalReferrer, that.ourHospitalReferrer, true) && compareDeep(ourDoctorReferrer, that.ourDoctorReferrer, true) && compareDeep(ourOtherReferrer, that.ourOtherReferrer, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof _MyReferrerComponent)) {
			return false;
		}
		_MyReferrerComponent that = (_MyReferrerComponent) other;
		return compareValues(ourReferrerType, that.ourReferrerType, true) && compareValues(ourHospitalReferrer, that.ourHospitalReferrer, true);
	}

	public org.hl7.fhir.dstu3.model.StringType _getReferrerType() {
		if (ourReferrerType == null)
			ourReferrerType = new org.hl7.fhir.dstu3.model.StringType();
		return ourReferrerType;
	}

	public _MyReferrerComponent _setReferrerType(org.hl7.fhir.dstu3.model.StringType theValue) {
		ourReferrerType = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.CodeType _getHospitalReferrer() {
		if (ourHospitalReferrer == null)
			ourHospitalReferrer = new org.hl7.fhir.dstu3.model.CodeType();
		return ourHospitalReferrer;
	}

	public _MyReferrerComponent _setHospitalReferrer(org.hl7.fhir.dstu3.model.CodeType theValue) {
		ourHospitalReferrer = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Reference _getDoctorReferrer() {
		if (ourDoctorReferrer == null)
			ourDoctorReferrer = new org.hl7.fhir.dstu3.model.Reference();
		return ourDoctorReferrer;
	}

	public _MyReferrerComponent _setDoctorReferrer(org.hl7.fhir.dstu3.model.Reference theValue) {
		ourDoctorReferrer = theValue;
		return this;
	}

	public _OtherReferrerComponent _getOtherReferrer() {
		if (ourOtherReferrer == null)
			ourOtherReferrer = new _OtherReferrerComponent();
		return ourOtherReferrer;
	}

	public _MyReferrerComponent _setOtherReferrer(_OtherReferrerComponent theValue) {
		ourOtherReferrer = theValue;
		return this;
	}

}
