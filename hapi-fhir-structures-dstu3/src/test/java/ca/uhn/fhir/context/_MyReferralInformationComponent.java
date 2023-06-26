package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.*;

@Block
public class _MyReferralInformationComponent extends BackboneElement implements org.hl7.fhir.instance.model.api.IBaseBackboneElement {

	/**
	 * referralType (extension)
	 */
	@Child(name = FIELD_REFERRALTYPE, min = 1, max = 1, type = {Coding.class})
	@Description(shortDefinition = "", formalDefinition = "Referral type in referral info.")
	@Extension(url = EXTURL_REFERRALTYPE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Coding ourReferralType;
	public static final String EXTURL_REFERRALTYPE = "http://myfhir.dk/x/MyReferralInformation/referralType";
	public static final String FIELD_REFERRALTYPE = "referralType";
	/**
	 * referringOrganisation (extension)
	 */
	@Child(name = FIELD_REFERRINGORGANISATION, min = 0, max = 1, type = {Organization.class})
	@Description(shortDefinition = "", formalDefinition = "The organization which the referral originates from.")
	@Extension(url = EXTURL_REFERRINGORGANISATION, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Reference ourReferringOrganisation;
	public static final String EXTURL_REFERRINGORGANISATION = "http://myfhir.dk/x/MyReferralInformation/referringOrganisation";
	public static final String FIELD_REFERRINGORGANISATION = "referringOrganisation";
	/**
	 * freeChoice (extension)
	 */
	@Child(name = FIELD_FREECHOICE, min = 1, max = 1, type = {Coding.class})
	@Description(shortDefinition = "", formalDefinition = "Type of free choice in relation to the referral decision.")
	@Extension(url = EXTURL_FREECHOICE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Coding ourFreeChoice;
	public static final String EXTURL_FREECHOICE = "http://myfhir.dk/x/MyReferralInformation/freeChoice";
	public static final String FIELD_FREECHOICE = "freeChoice";
	/**
	 * received (extension)
	 */
	@Child(name = FIELD_RECEIVED, min = 1, max = 1, type = {DateTimeType.class})
	@Description(shortDefinition = "", formalDefinition = "Time in which the referral was received.")
	@Extension(url = EXTURL_RECEIVED, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.DateTimeType ourReceived;
	public static final String EXTURL_RECEIVED = "http://myfhir.dk/x/MyReferralInformation/received";
	public static final String FIELD_RECEIVED = "received";
	/**
	 * referrer (extension)
	 */
	@Child(name = FIELD_REFERRER, min = 0, max = 1, type = {_MyReferrerComponent.class})
	@Description(shortDefinition = "", formalDefinition = "Referring organization, doctor or other.")
	@Extension(url = EXTURL_REFERRER, definedLocally = false, isModifier = false)
	protected _MyReferrerComponent ourReferrer;
	public static final String EXTURL_REFERRER = "http://myfhir.dk/x/MyReferralInformation-referrer";
	public static final String FIELD_REFERRER = "referrer";

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourReferralType, ourReferringOrganisation, ourFreeChoice, ourReceived, ourReferrer);
	}

	@Override
	public _MyReferralInformationComponent copy() {
		_MyReferralInformationComponent dst = new _MyReferralInformationComponent();
		copyValues(dst);
		dst.ourReferralType = ourReferralType == null ? null : ourReferralType.copy();
		dst.ourReferringOrganisation = ourReferringOrganisation == null ? null : ourReferringOrganisation.copy();
		dst.ourFreeChoice = ourFreeChoice == null ? null : ourFreeChoice.copy();
		dst.ourReceived = ourReceived == null ? null : ourReceived.copy();
		dst.ourReferrer = ourReferrer == null ? null : ourReferrer.copy();
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
		if (!(other instanceof _MyReferralInformationComponent)) {
			return false;
		}
		_MyReferralInformationComponent that = (_MyReferralInformationComponent) other;
		return compareDeep(ourReferralType, that.ourReferralType, true) && compareDeep(ourReferringOrganisation, that.ourReferringOrganisation, true) && compareDeep(ourFreeChoice, that.ourFreeChoice, true) && compareDeep(ourReceived, that.ourReceived, true)
				&& compareDeep(ourReferrer, that.ourReferrer, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof _MyReferralInformationComponent)) {
			return false;
		}
		_MyReferralInformationComponent that = (_MyReferralInformationComponent) other;
		return compareValues(ourReceived, that.ourReceived, true);
	}

	public org.hl7.fhir.dstu3.model.Coding _getReferralType() {
		if (ourReferralType == null)
			ourReferralType = new org.hl7.fhir.dstu3.model.Coding();
		return ourReferralType;
	}

	public _MyReferralInformationComponent _setReferralType(org.hl7.fhir.dstu3.model.Coding theValue) {
		ourReferralType = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Reference _getReferringOrganisation() {
		if (ourReferringOrganisation == null)
			ourReferringOrganisation = new org.hl7.fhir.dstu3.model.Reference();
		return ourReferringOrganisation;
	}

	public _MyReferralInformationComponent _setReferringOrganisation(org.hl7.fhir.dstu3.model.Reference theValue) {
		ourReferringOrganisation = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Coding _getFreeChoice() {
		if (ourFreeChoice == null)
			ourFreeChoice = new org.hl7.fhir.dstu3.model.Coding();
		return ourFreeChoice;
	}

	public _MyReferralInformationComponent _setFreeChoice(org.hl7.fhir.dstu3.model.Coding theValue) {
		ourFreeChoice = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.DateTimeType _getReceived() {
		if (ourReceived == null)
			ourReceived = new org.hl7.fhir.dstu3.model.DateTimeType();
		return ourReceived;
	}

	public _MyReferralInformationComponent _setReceived(org.hl7.fhir.dstu3.model.DateTimeType theValue) {
		ourReceived = theValue;
		return this;
	}

	public _MyReferrerComponent _getReferrer() {
		if (ourReferrer == null)
			ourReferrer = new _MyReferrerComponent();
		return ourReferrer;
	}

	public _MyReferralInformationComponent _setReferrer(_MyReferrerComponent theValue) {
		ourReferrer = theValue;
		return this;
	}

}
