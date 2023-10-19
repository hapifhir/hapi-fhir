/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;

import java.util.Optional;
import javax.annotation.Nullable;

public class MemberMatchR4ResourceProvider {

	private final MemberMatcherR4Helper myMemberMatcherR4Helper;
	private final FhirContext myFhirContext;

	public MemberMatchR4ResourceProvider(FhirContext theFhirContext, MemberMatcherR4Helper theMemberMatcherR4Helper) {
		myFhirContext = theFhirContext;
		myMemberMatcherR4Helper = theMemberMatcherR4Helper;
	}

	/**
	 * /Patient/$member-match operation
	 * Basic implementation matching by coverage id or by coverage identifier. Matching by
	 * Beneficiary (Patient) demographics on family name and birthdate in this version
	 */
	@Operation(
			name = ProviderConstants.OPERATION_MEMBER_MATCH,
			typeName = "Patient",
			canonicalUrl = "http://hl7.org/fhir/us/davinci-hrex/OperationDefinition/member-match",
			idempotent = false,
			returnParameters = {@OperationParam(name = "MemberIdentifier", typeName = "string")})
	public Parameters patientMemberMatch(
			javax.servlet.http.HttpServletRequest theServletRequest,
			@Description(
							shortDefinition =
									"The target of the operation. Will be returned with Identifier for matched coverage added.")
					@OperationParam(name = Constants.PARAM_MEMBER_PATIENT, min = 1, max = 1)
					Patient theMemberPatient,
			@Description(shortDefinition = "Old coverage information as extracted from beneficiary's card.")
					@OperationParam(name = Constants.PARAM_OLD_COVERAGE, min = 1, max = 1)
					Coverage oldCoverage,
			@Description(
							shortDefinition =
									"New Coverage information. Provided as a reference. Optionally returned unmodified.")
					@OperationParam(name = Constants.PARAM_NEW_COVERAGE, min = 1, max = 1)
					Coverage newCoverage,
			@Description(
							shortDefinition =
									"Consent information. Consent held by the system seeking the match that grants permission to access the patient information.")
					@OperationParam(name = Constants.PARAM_CONSENT, min = 1, max = 1)
					Consent theConsent,
			RequestDetails theRequestDetails) {
		return doMemberMatchOperation(theMemberPatient, oldCoverage, newCoverage, theConsent, theRequestDetails);
	}

	private Parameters doMemberMatchOperation(
			Patient theMemberPatient,
			Coverage theCoverageToMatch,
			Coverage theCoverageToLink,
			Consent theConsent,
			RequestDetails theRequestDetails) {

		validateParams(theMemberPatient, theCoverageToMatch, theCoverageToLink, theConsent);

		Optional<Coverage> coverageOpt =
				myMemberMatcherR4Helper.findMatchingCoverage(theCoverageToMatch, theRequestDetails);
		if (coverageOpt.isEmpty()) {
			String i18nMessage =
					myFhirContext.getLocalizer().getMessage("operation.member.match.error.coverage.not.found");
			throw new UnprocessableEntityException(Msg.code(1155) + i18nMessage);
		}
		Coverage coverage = coverageOpt.get();

		Optional<Patient> patientOpt = myMemberMatcherR4Helper.getBeneficiaryPatient(coverage, theRequestDetails);
		if (patientOpt.isEmpty()) {
			String i18nMessage =
					myFhirContext.getLocalizer().getMessage("operation.member.match.error.beneficiary.not.found");
			throw new UnprocessableEntityException(Msg.code(1156) + i18nMessage);
		}

		Patient patient = patientOpt.get();
		if (!myMemberMatcherR4Helper.validPatientMember(patient, theMemberPatient, theRequestDetails)) {
			String i18nMessage =
					myFhirContext.getLocalizer().getMessage("operation.member.match.error.patient.not.found");
			throw new UnprocessableEntityException(Msg.code(2146) + i18nMessage);
		}

		if (patient.getIdentifier().isEmpty()) {
			String i18nMessage = myFhirContext
					.getLocalizer()
					.getMessage("operation.member.match.error.beneficiary.without.identifier");
			throw new UnprocessableEntityException(Msg.code(1157) + i18nMessage);
		}

		if (!myMemberMatcherR4Helper.validConsentDataAccess(theConsent)) {
			String i18nMessage = myFhirContext
					.getLocalizer()
					.getMessage("operation.member.match.error.consent.release.data.mismatch");
			throw new UnprocessableEntityException(Msg.code(2147) + i18nMessage);
		}

		myMemberMatcherR4Helper.addMemberIdentifierToMemberPatient(theMemberPatient, patient.getIdentifierFirstRep());
		myMemberMatcherR4Helper.updateConsentForMemberMatch(theConsent, patient, theMemberPatient, theRequestDetails);
		return myMemberMatcherR4Helper.buildSuccessReturnParameters(theMemberPatient, theCoverageToLink, theConsent);
	}

	private void validateParams(
			Patient theMemberPatient, Coverage theOldCoverage, Coverage theNewCoverage, Consent theConsent) {
		validateParam(theMemberPatient, Constants.PARAM_MEMBER_PATIENT);
		validateParam(theOldCoverage, Constants.PARAM_OLD_COVERAGE);
		validateParam(theNewCoverage, Constants.PARAM_NEW_COVERAGE);
		validateParam(theConsent, Constants.PARAM_CONSENT);
		validateMemberPatientParam(theMemberPatient);
		validateConsentParam(theConsent);
	}

	private void validateParam(@Nullable Object theParam, String theParamName) {
		if (theParam == null) {
			String i18nMessage = myFhirContext
					.getLocalizer()
					.getMessage("operation.member.match.error.missing.parameter", theParamName);
			throw new UnprocessableEntityException(Msg.code(1158) + i18nMessage);
		}
	}

	private void validateMemberPatientParam(Patient theMemberPatient) {
		if (theMemberPatient.getName().isEmpty()) {
			validateParam(null, Constants.PARAM_MEMBER_PATIENT_NAME);
		}

		validateParam(theMemberPatient.getName().get(0).getFamily(), Constants.PARAM_MEMBER_PATIENT_NAME);
		validateParam(theMemberPatient.getBirthDate(), Constants.PARAM_MEMBER_PATIENT_BIRTHDATE);
	}

	private void validateConsentParam(Consent theConsent) {
		if (theConsent.getPatient().isEmpty()) {
			validateParam(null, Constants.PARAM_CONSENT_PATIENT_REFERENCE);
		}
		if (theConsent.getPerformer().isEmpty()) {
			validateParam(null, Constants.PARAM_CONSENT_PERFORMER_REFERENCE);
		}
	}
}
