package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.BackboneElement;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;

@Block
public class _EventMarkerComponent extends BackboneElement implements org.hl7.fhir.instance.model.api.IBaseBackboneElement {

	/**
	 * eventType (extension)
	 */
	@Child(name = FIELD_EVENTTYPE, min = 1, max = 1, type = {Coding.class})
	@Description(shortDefinition = "", formalDefinition = "The type of event marker on an episode of care.")
	@Extension(url = EXTURL_EVENTTYPE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Coding ourEventType;
	public static final String EXTURL_EVENTTYPE = "http://myfhir.dk/x/MyEpisodeOfCare-event-marker/eventType";
	public static final String FIELD_EVENTTYPE = "eventType";
	/**
	 * eventTimestamp (extension)
	 */
	@Child(name = FIELD_EVENTTIMESTAMP, min = 1, max = 1, type = {DateTimeType.class})
	@Description(shortDefinition = "", formalDefinition = "Time in which the event marker was created.")
	@Extension(url = EXTURL_EVENTTIMESTAMP, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.DateTimeType ourEventTimestamp;
	public static final String EXTURL_EVENTTIMESTAMP = "http://myfhir.dk/x/MyEpisodeOfCare-event-marker/eventTimestamp";
	public static final String FIELD_EVENTTIMESTAMP = "eventTimestamp";

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourEventType, ourEventTimestamp);
	}

	@Override
	public _EventMarkerComponent copy() {
		_EventMarkerComponent dst = new _EventMarkerComponent();
		copyValues(dst);
		dst.ourEventType = ourEventType == null ? null : ourEventType.copy();
		dst.ourEventTimestamp = ourEventTimestamp == null ? null : ourEventTimestamp.copy();
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
		if (!(other instanceof _EventMarkerComponent)) {
			return false;
		}
		_EventMarkerComponent that = (_EventMarkerComponent) other;
		return compareDeep(ourEventType, that.ourEventType, true) && compareDeep(ourEventTimestamp, that.ourEventTimestamp, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof _EventMarkerComponent)) {
			return false;
		}
		_EventMarkerComponent that = (_EventMarkerComponent) other;
		return compareValues(ourEventTimestamp, that.ourEventTimestamp, true);
	}

	public org.hl7.fhir.dstu3.model.Coding _getEventType() {
		if (ourEventType == null)
			ourEventType = new org.hl7.fhir.dstu3.model.Coding();
		return ourEventType;
	}

	public _EventMarkerComponent _setEventType(org.hl7.fhir.dstu3.model.Coding theValue) {
		ourEventType = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.DateTimeType _getEventTimestamp() {
		if (ourEventTimestamp == null)
			ourEventTimestamp = new org.hl7.fhir.dstu3.model.DateTimeType();
		return ourEventTimestamp;
	}

	public _EventMarkerComponent _setEventTimestamp(org.hl7.fhir.dstu3.model.DateTimeType theValue) {
		ourEventTimestamp = theValue;
		return this;
	}

}
