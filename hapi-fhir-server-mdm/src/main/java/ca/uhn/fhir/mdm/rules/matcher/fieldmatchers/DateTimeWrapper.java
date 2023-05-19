package ca.uhn.fhir.mdm.rules.matcher.fieldmatchers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;

/**
 * A wrapper class for datetimes of ambiguous fhir version
 */
public class DateTimeWrapper {

	/**
	 * The precision of this datetime object
	 */
	private final TemporalPrecisionEnum myPrecision;

	/**
	 * The string value with current precision
	 */
	private final String myValueAsString;

	public DateTimeWrapper(FhirContext theFhirContext, IBase theDate) {
		if (theDate instanceof org.hl7.fhir.dstu3.model.BaseDateTimeType) {
			myPrecision = ((org.hl7.fhir.dstu3.model.BaseDateTimeType) theDate).getPrecision();
			myValueAsString = ((org.hl7.fhir.dstu3.model.BaseDateTimeType) theDate).getValueAsString();
		} else if (theDate instanceof org.hl7.fhir.r4.model.BaseDateTimeType) {
			myPrecision = ((org.hl7.fhir.r4.model.BaseDateTimeType) theDate).getPrecision();
			myValueAsString = ((org.hl7.fhir.r4.model.BaseDateTimeType) theDate).getValueAsString();
		}

		// we should consider changing this error so we don't need the fhir context at all
		throw new UnsupportedOperationException(Msg.code(1520) + "Version not supported: " + theFhirContext.getVersion().getVersion());
	}

	public TemporalPrecisionEnum getPrecision() {
		return myPrecision;
	}

	public String getValueAsStringWithPrecision(TemporalPrecisionEnum thePrecision) {
		// we are using an R4 DateTypes because all datetime strings are the same for all types
		// (and so it won't matter here)
		switch (thePrecision) {
			case YEAR:
			case MONTH:
			case DAY:
				DateType dateType = new DateType(myValueAsString);
				dateType.setPrecision(thePrecision);
				return dateType.getValueAsString();
			case MINUTE:
			case SECOND:
			case MILLI:
				DateTimeType dateTimeType = new DateTimeType(myValueAsString);
				dateTimeType.setPrecision(thePrecision);
				return dateTimeType.getValueAsString();
		}
		// why would we fall out here?
		return myValueAsString;
	}

	public String getValueAsString() {
		return myValueAsString;
	}
}
