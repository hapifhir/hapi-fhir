package ca.uhn.fhir.empi.rules.metric.matcher;

import org.hl7.fhir.dstu3.model.BaseDateTimeType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.instance.model.api.IBase;

public class HapiDateMatcherDstu3 {
	// TODO KHS code duplication (tried generalizing it with generics, but it got too convoluted)
	public boolean match(IBase theLeftBase, IBase theRightBase) {
		if (theLeftBase instanceof BaseDateTimeType && theRightBase instanceof BaseDateTimeType) {
			BaseDateTimeType leftDate = (BaseDateTimeType) theLeftBase;
			BaseDateTimeType rightDate = (BaseDateTimeType) theRightBase;
			int comparison = leftDate.getPrecision().compareTo(rightDate.getPrecision());
			if (comparison == 0) {
				return leftDate.getValueAsString().equals(rightDate.getValueAsString());
			}
			BaseDateTimeType leftPDate;
			BaseDateTimeType rightPDate;
			if (comparison > 0) {
				leftPDate = leftDate;
				if (rightDate instanceof DateType) {
					rightPDate = new DateType(rightDate.getValue(), leftDate.getPrecision());
				} else {
					rightPDate = new DateTimeType(rightDate.getValue(), leftDate.getPrecision());
				}
			} else {
				rightPDate = rightDate;
				if (leftDate instanceof DateType) {
					leftPDate = new DateType(leftDate.getValue(), rightDate.getPrecision());
				} else {
					leftPDate = new DateTimeType(leftDate.getValue(), rightDate.getPrecision());
				}
			}
			return leftPDate.getValueAsString().equals(rightPDate.getValueAsString());
		}

		return false;
	}
}
