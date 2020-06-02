package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class LastNParameterHelper {

	public static boolean isLastNParameter(String theParamName, FhirContext theContext) {
		if (theParamName == null) {
			return false;
		}

		FhirVersionEnum version = theContext.getVersion().getVersion();

		if (isR5(version) && isLastNParameterR5(theParamName)) {
			return true;
		} else if (isR4(version) && isLastNParameterR4(theParamName)) {
			return true;
		} else if (isDstu3(version) && isLastNParameterDstu3(theParamName)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isDstu3(FhirVersionEnum theVersion) {
		return (theVersion == FhirVersionEnum.DSTU3);
	}

	private static boolean isR4(FhirVersionEnum theVersion) {
		return (theVersion == FhirVersionEnum.R4);
	}

	private static boolean isR5(FhirVersionEnum theVersion) {
		return (theVersion == FhirVersionEnum.R5);
	}

	private static boolean isLastNParameterDstu3(String theParamName) {
		return (theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_PATIENT)
			|| theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_CODE))
			|| theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_DATE);
	}

	private static boolean isLastNParameterR4(String theParamName) {
		return (theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_PATIENT)
			|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CODE))
			|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_DATE);
	}

	private static boolean isLastNParameterR5(String theParamName) {
		return (theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_PATIENT)
			|| theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_CODE))
			|| theParamName.equals(org.hl7.fhir.r5.model.Observation.SP_DATE);
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
