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

	public static String getSubjectParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r5.model.Observation.SP_SUBJECT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_SUBJECT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_SUBJECT;
		} else {
			throw new InvalidRequestException("$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getPatientParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r5.model.Observation.SP_PATIENT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_PATIENT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_PATIENT;
		} else {
			throw new InvalidRequestException("$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getEffectiveParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r5.model.Observation.SP_DATE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_DATE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_DATE;
		} else {
			throw new InvalidRequestException("$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getCategoryParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r5.model.Observation.SP_CATEGORY;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_CATEGORY;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_CATEGORY;
		} else {
			throw new InvalidRequestException("$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getCodeParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r5.model.Observation.SP_CODE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_CODE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_CODE;
		} else {
			throw new InvalidRequestException("$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}


}
