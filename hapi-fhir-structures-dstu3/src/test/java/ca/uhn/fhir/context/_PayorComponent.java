package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.*;

@Block
public class _PayorComponent extends BackboneElement implements org.hl7.fhir.instance.model.api.IBaseBackboneElement {

	/**
	 * payorCode (extension)
	 */
	@Child(name = FIELD_PAYORCODE, min = 1, max = 1, type = {Coding.class})
	@Description(shortDefinition = "", formalDefinition = "The payor code for the duration")
	@Extension(url = EXTURL_PAYORCODE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Coding ourPayorCode;
	public static final String EXTURL_PAYORCODE = "http://myfhir.dk/x/MyEpisodeOfCare-payor/payorCode";
	public static final String FIELD_PAYORCODE = "payorCode";
	/**
	 * period (extension)
	 */
	@Child(name = FIELD_PERIOD, min = 1, max = 1, type = {Period.class})
	@Description(shortDefinition = "", formalDefinition = "The duration for the responsible payor.")
	@Extension(url = EXTURL_PERIOD, definedLocally = false, isModifier = false)
	protected Period ourPeriod;
	public static final String EXTURL_PERIOD = "http://myfhir.dk/x/MyEpisodeOfCare-payor/period";
	public static final String FIELD_PERIOD = "period";
	/**
	 * userDefined (extension)
	 */
	@Child(name = FIELD_USERDEFINED, min = 1, max = 1, type = {BooleanType.class})
	@Description(shortDefinition = "", formalDefinition = "True if the payor information is defined by a user.")
	@Extension(url = EXTURL_USERDEFINED, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.BooleanType ourUserDefined;
	public static final String EXTURL_USERDEFINED = "http://myfhir.dk/x/MyEpisodeOfCare-payor/userDefined";
	public static final String FIELD_USERDEFINED = "userDefined";

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourPayorCode, ourPeriod, ourUserDefined);
	}

	@Override
	public _PayorComponent copy() {
		_PayorComponent dst = new _PayorComponent();
		copyValues(dst);
		dst.ourPayorCode = ourPayorCode == null ? null : ourPayorCode.copy();
		dst.ourPeriod = ourPeriod == null ? null : ourPeriod.copy();
		dst.ourUserDefined = ourUserDefined == null ? null : ourUserDefined.copy();
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
		if (!(other instanceof _PayorComponent)) {
			return false;
		}
		_PayorComponent that = (_PayorComponent) other;
		return compareDeep(ourPayorCode, that.ourPayorCode, true) && compareDeep(ourPeriod, that.ourPeriod, true) && compareDeep(ourUserDefined, that.ourUserDefined, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof _PayorComponent)) {
			return false;
		}
		_PayorComponent that = (_PayorComponent) other;
		return compareValues(ourUserDefined, that.ourUserDefined, true);
	}

	public org.hl7.fhir.dstu3.model.Coding _getPayorCode() {
		if (ourPayorCode == null)
			ourPayorCode = new org.hl7.fhir.dstu3.model.Coding();
		return ourPayorCode;
	}

	public _PayorComponent _setPayorCode(org.hl7.fhir.dstu3.model.Coding theValue) {
		ourPayorCode = theValue;
		return this;
	}

	public Period _getPeriod() {
		if (ourPeriod == null)
			ourPeriod = new Period();
		return ourPeriod;
	}

	public _PayorComponent _setPeriod(Period theValue) {
		ourPeriod = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.BooleanType _getUserDefined() {
		if (ourUserDefined == null)
			ourUserDefined = new org.hl7.fhir.dstu3.model.BooleanType();
		return ourUserDefined;
	}

	public _PayorComponent _setUserDefined(org.hl7.fhir.dstu3.model.BooleanType theValue) {
		ourUserDefined = theValue;
		return this;
	}

}
