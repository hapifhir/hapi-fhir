/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.mdm.rules.matcher.fieldmatchers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.DateTimeType;

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
			org.hl7.fhir.dstu3.model.BaseDateTimeType dstu3Date = (org.hl7.fhir.dstu3.model.BaseDateTimeType) theDate;
			myPrecision = dstu3Date.getPrecision();
			myValueAsString = dstu3Date.getValueAsString();
		} else if (theDate instanceof org.hl7.fhir.r4.model.BaseDateTimeType) {
			org.hl7.fhir.r4.model.BaseDateTimeType r4Date = (org.hl7.fhir.r4.model.BaseDateTimeType) theDate;
			myPrecision = r4Date.getPrecision();
			myValueAsString = r4Date.getValueAsString();
		} else if (theDate instanceof org.hl7.fhir.r5.model.BaseDateTimeType) {
			org.hl7.fhir.r5.model.BaseDateTimeType r5Date = (org.hl7.fhir.r5.model.BaseDateTimeType) theDate;
			myPrecision = r5Date.getPrecision();
			myValueAsString = r5Date.getValueAsString();
		} else {
			// we should consider changing this error so we don't need the fhir context at all
			throw new UnsupportedOperationException(Msg.code(1520) + "Version not supported: "
					+ theFhirContext.getVersion().getVersion());
		}
	}

	public TemporalPrecisionEnum getPrecision() {
		return myPrecision;
	}

	public String getValueAsStringWithPrecision(TemporalPrecisionEnum thePrecision) {
		// we are using an R4 DateTypes because all datetime strings are the same for all fhir versions
		// (and so it won't matter here)
		// we just want the string at a specific precision
		DateTimeType dateTimeType = new DateTimeType(myValueAsString);
		dateTimeType.setPrecision(thePrecision);
		return dateTimeType.getValueAsString();
	}

	public String getValueAsString() {
		return myValueAsString;
	}
}
