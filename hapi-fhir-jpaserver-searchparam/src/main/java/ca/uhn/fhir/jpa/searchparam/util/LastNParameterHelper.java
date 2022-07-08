package ca.uhn.fhir.jpa.searchparam.util;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
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
			|| theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CODE))
			|| theParamName.equals(org.hl7.fhir.dstu3.model.Observation.SP_DATE);
	}

	private static boolean isLastNParameterR4(String theParamName) {
		return (theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_PATIENT)
			|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CODE))
			|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_DATE);
	}

	private static boolean isLastNParameterR5(String theParamName) {
		return (theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_SUBJECT) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_PATIENT)
			|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CATEGORY) || theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_CODE))
			|| theParamName.equals(org.hl7.fhir.r4.model.Observation.SP_DATE);
	}

	public static String getSubjectParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r4.model.Observation.SP_SUBJECT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_SUBJECT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_SUBJECT;
		} else {
			throw new InvalidRequestException(Msg.code(489) + "$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getPatientParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r4.model.Observation.SP_PATIENT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_PATIENT;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_PATIENT;
		} else {
			throw new InvalidRequestException(Msg.code(490) + "$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getEffectiveParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r4.model.Observation.SP_DATE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_DATE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_DATE;
		} else {
			throw new InvalidRequestException(Msg.code(491) + "$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getCategoryParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r4.model.Observation.SP_CATEGORY;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_CATEGORY;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_CATEGORY;
		} else {
			throw new InvalidRequestException(Msg.code(492) + "$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}

	public static String getCodeParamName(FhirContext theContext) {
		if (theContext.getVersion().getVersion() == FhirVersionEnum.R5) {
			return org.hl7.fhir.r4.model.Observation.SP_CODE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.R4) {
			return org.hl7.fhir.r4.model.Observation.SP_CODE;
		} else if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			return org.hl7.fhir.dstu3.model.Observation.SP_CODE;
		} else {
			throw new InvalidRequestException(Msg.code(493) + "$lastn operation is not implemented for FHIR Version " + theContext.getVersion().getVersion().getFhirVersionString());
		}
	}


}
