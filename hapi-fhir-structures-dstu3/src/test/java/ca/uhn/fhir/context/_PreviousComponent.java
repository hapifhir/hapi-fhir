package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.BackboneElement;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.StringType;

@Block
public class _PreviousComponent extends BackboneElement implements org.hl7.fhir.instance.model.api.IBaseBackboneElement {

	/**
	 * previousReference (extension)
	 */
	@Child(name = FIELD_PREVIOUSREFERENCE, min = 1, max = 1, type = {StringType.class})
	@Description(shortDefinition = "", formalDefinition = "Reference to the previous episode of care which may be external.")
	@Extension(url = EXTURL_PREVIOUSREFERENCE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.StringType ourPreviousReference;
	public static final String EXTURL_PREVIOUSREFERENCE = "http://myfhir.dk/x/MyEpisodeOfCare-previous/previousReference";
	public static final String FIELD_PREVIOUSREFERENCE = "previousReference";
	/**
	 * referenceType (extension)
	 */
	@Child(name = FIELD_REFERENCETYPE, min = 1, max = 1, type = {Coding.class})
	@Description(shortDefinition = "", formalDefinition = "The type of reference to a previous episode of care.")
	@Extension(url = EXTURL_REFERENCETYPE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Coding ourReferenceType;
	public static final String EXTURL_REFERENCETYPE = "http://myfhir.dk/x/MyEpisodeOfCare-previous/referenceType";
	public static final String FIELD_REFERENCETYPE = "referenceType";

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourPreviousReference, ourReferenceType);
	}

	@Override
	public _PreviousComponent copy() {
		_PreviousComponent dst = new _PreviousComponent();
		copyValues(dst);
		dst.ourPreviousReference = ourPreviousReference == null ? null : ourPreviousReference.copy();
		dst.ourReferenceType = ourReferenceType == null ? null : ourReferenceType.copy();
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
		if (!(other instanceof _PreviousComponent)) {
			return false;
		}
		_PreviousComponent that = (_PreviousComponent) other;
		return compareDeep(ourPreviousReference, that.ourPreviousReference, true) && compareDeep(ourReferenceType, that.ourReferenceType, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof _PreviousComponent)) {
			return false;
		}
		_PreviousComponent that = (_PreviousComponent) other;
		return compareValues(ourPreviousReference, that.ourPreviousReference, true);
	}

	public org.hl7.fhir.dstu3.model.StringType _getPreviousReference() {
		if (ourPreviousReference == null)
			ourPreviousReference = new org.hl7.fhir.dstu3.model.StringType();
		return ourPreviousReference;
	}

	public _PreviousComponent _setPreviousReference(org.hl7.fhir.dstu3.model.StringType theValue) {
		ourPreviousReference = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Coding _getReferenceType() {
		if (ourReferenceType == null)
			ourReferenceType = new org.hl7.fhir.dstu3.model.Coding();
		return ourReferenceType;
	}

	public _PreviousComponent _setReferenceType(org.hl7.fhir.dstu3.model.Coding theValue) {
		ourReferenceType = theValue;
		return this;
	}

}
