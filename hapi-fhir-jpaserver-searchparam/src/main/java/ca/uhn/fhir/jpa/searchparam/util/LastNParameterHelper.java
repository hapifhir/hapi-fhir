package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class LastNParameterHelper {

	public static boolean isLastNParameter(String theParamName, FhirContext theContext) {
		if (theParamName == null) {
			return false;
		}
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			if (theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_PATIENT)
				|| theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_CODE)) {
				return true;
			} else {
				return false;
			}
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			if (theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_PATIENT)
				|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CODE)) {
				return true;
			} else {
				return false;
			}
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			if (theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_PATIENT)
				|| theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_CODE)) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new InvalidRequestException("$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}
}
