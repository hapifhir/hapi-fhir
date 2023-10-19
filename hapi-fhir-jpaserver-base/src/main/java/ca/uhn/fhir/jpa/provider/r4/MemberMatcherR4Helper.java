/*
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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_IDENTIFIER;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_NEW_COVERAGE;

public class MemberMatcherR4Helper {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MemberMatcherR4Helper.class);

	private static final String OUT_COVERAGE_IDENTIFIER_CODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v2-0203";
	private static final String OUT_COVERAGE_IDENTIFIER_CODE = "MB";
	private static final String OUT_COVERAGE_IDENTIFIER_TEXT = "Member Number";
	private static final String COVERAGE_TYPE = "Coverage";
	private static final String CONSENT_POLICY_REGULAR_TYPE = "regular";
	private static final String CONSENT_POLICY_SENSITIVE_TYPE = "sensitive";
	public static final String CONSENT_IDENTIFIER_CODE_SYSTEM =
			"https://smilecdr.com/fhir/ns/member-match-source-client";

	private final FhirContext myFhirContext;
	private final IFhirResourceDao<Coverage> myCoverageDao;
	private final IFhirResourceDao<Patient> myPatientDao;
	private final IFhirResourceDao<Consent> myConsentDao;
	/** A hook to modify the Consent before save */
	private final Consumer<IBaseResource> myConsentModifier;

	private boolean myRegularFilterSupported = false;

	public MemberMatcherR4Helper(
			FhirContext theContext,
			IFhirResourceDao<Coverage> theCoverageDao,
			IFhirResourceDao<Patient> thePatientDao,
			IFhirResourceDao<Consent> theConsentDao,
			@Nullable IMemberMatchConsentHook theConsentModifier) {
		myFhirContext = theContext;
		myConsentDao = theConsentDao;
		myPatientDao = thePatientDao;
		myCoverageDao = theCoverageDao;
		myConsentModifier = (theConsentModifier != null) ? theConsentModifier : noop -> {};
	}

	/**
	 * Find Coverage matching the received member (Patient) by coverage id or by coverage identifier only
	 */
	public Optional<Coverage> findMatchingCoverage(Coverage theCoverageToMatch, RequestDetails theRequestDetails) {
		// search by received old coverage id
		List<IBaseResource> foundCoverages = findCoverageByCoverageId(theCoverageToMatch, theRequestDetails);
		if (foundCoverages.size() == 1 && isCoverage(foundCoverages.get(0))) {
			return Optional.of((Coverage) foundCoverages.get(0));
		}

		// search by received old coverage identifier
		foundCoverages = findCoverageByCoverageIdentifier(theCoverageToMatch, theRequestDetails);
		if (foundCoverages.size() == 1 && isCoverage(foundCoverages.get(0))) {
			return Optional.of((Coverage) foundCoverages.get(0));
		}

		return Optional.empty();
	}

	private List<IBaseResource> findCoverageByCoverageIdentifier(
			Coverage theCoverageToMatch, RequestDetails theRequestDetails) {
		TokenOrListParam identifierParam = new TokenOrListParam();
		for (Identifier identifier : theCoverageToMatch.getIdentifier()) {
			identifierParam.add(identifier.getSystem(), identifier.getValue());
		}

		SearchParameterMap paramMap = new SearchParameterMap().add("identifier", identifierParam);
		ca.uhn.fhir.rest.api.server.IBundleProvider retVal = myCoverageDao.search(paramMap, theRequestDetails);

		return retVal.getAllResources();
	}

	private boolean isCoverage(IBaseResource theIBaseResource) {
		return theIBaseResource.fhirType().equals(COVERAGE_TYPE);
	}

	private List<IBaseResource> findCoverageByCoverageId(
			Coverage theCoverageToMatch, RequestDetails theRequestDetails) {
		SearchParameterMap paramMap = new SearchParameterMap().add("_id", new StringParam(theCoverageToMatch.getId()));
		ca.uhn.fhir.rest.api.server.IBundleProvider retVal = myCoverageDao.search(paramMap, theRequestDetails);

		return retVal.getAllResources();
	}

	public void updateConsentForMemberMatch(
			Consent theConsent, Patient thePatient, Patient theMemberPatient, RequestDetails theRequestDetails) {
		addIdentifierToConsent(theConsent, theMemberPatient);
		updateConsentPatientAndPerformer(theConsent, thePatient);
		myConsentModifier.accept(theConsent);

		// Trust RequestTenantPartitionInterceptor or PatientIdPartitionInterceptor to assign the partition.
		myConsentDao.create(theConsent, theRequestDetails);
	}

	public Parameters buildSuccessReturnParameters(Patient theMemberPatient, Coverage theCoverage, Consent theConsent) {
		IBaseParameters parameters = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_MEMBER_PATIENT, theMemberPatient);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_NEW_COVERAGE, theCoverage);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_CONSENT, theConsent);
		ParametersUtil.addParameterToParameters(
				myFhirContext, parameters, PARAM_MEMBER_IDENTIFIER, getIdentifier(theMemberPatient));
		return (Parameters) parameters;
	}

	private Identifier getIdentifier(Patient theMemberPatient) {
		return theMemberPatient.getIdentifier().stream()
				.filter(this::isTypeMB)
				.findFirst()
				.orElseThrow(() -> {
					String i18nMessage = myFhirContext
							.getLocalizer()
							.getMessage("operation.member.match.error.beneficiary.without.identifier");
					return new UnprocessableEntityException(Msg.code(2219) + i18nMessage);
				});
	}

	private boolean isTypeMB(Identifier theMemberIdentifier) {
		return theMemberIdentifier.getType() != null
				&& theMemberIdentifier.getType().getCoding().stream()
						.anyMatch(typeCoding -> typeCoding.getCode().equals("MB"));
	}

	public void addMemberIdentifierToMemberPatient(Patient theMemberPatient, Identifier theNewIdentifier) {
		Coding coding = new Coding()
				.setSystem(OUT_COVERAGE_IDENTIFIER_CODE_SYSTEM)
				.setCode(OUT_COVERAGE_IDENTIFIER_CODE)
				.setDisplay(OUT_COVERAGE_IDENTIFIER_TEXT)
				.setUserSelected(false);

		CodeableConcept concept =
				new CodeableConcept().setCoding(Lists.newArrayList(coding)).setText(OUT_COVERAGE_IDENTIFIER_TEXT);

		Identifier newIdentifier = new Identifier()
				.setUse(Identifier.IdentifierUse.USUAL)
				.setType(concept)
				.setSystem(theNewIdentifier.getSystem())
				.setValue(theNewIdentifier.getValue());

		theMemberPatient.addIdentifier(newIdentifier);
	}

	public Optional<Patient> getBeneficiaryPatient(Coverage theCoverage, RequestDetails theRequestDetails) {
		if (theCoverage.getBeneficiaryTarget() == null && theCoverage.getBeneficiary() == null) {
			return Optional.empty();
		}

		if (theCoverage.getBeneficiaryTarget() != null
				&& !theCoverage.getBeneficiaryTarget().getIdentifier().isEmpty()) {
			return Optional.of(theCoverage.getBeneficiaryTarget());
		}

		Reference beneficiaryRef = theCoverage.getBeneficiary();
		if (beneficiaryRef == null) {
			return Optional.empty();
		}

		if (beneficiaryRef.getResource() != null) {
			return Optional.of((Patient) beneficiaryRef.getResource());
		}

		if (beneficiaryRef.getReference() == null) {
			return Optional.empty();
		}

		Patient beneficiary = myPatientDao.read(new IdDt(beneficiaryRef.getReference()), theRequestDetails);
		return Optional.ofNullable(beneficiary);
	}

	/**
	 * Matching by member patient demographics - family name and birthdate only
	 */
	public boolean validPatientMember(
			Patient thePatientFromContract, Patient thePatientToMatch, RequestDetails theRequestDetails) {
		if (thePatientFromContract == null
				|| thePatientFromContract.getIdElement() == null
				|| thePatientToMatch == null) {
			return false;
		}
		StringOrListParam familyName = new StringOrListParam();
		for (HumanName name : thePatientToMatch.getName()) {
			familyName.addOr(new StringParam(name.getFamily()));
		}
		SearchParameterMap map = new SearchParameterMap()
				.add("family", familyName)
				.add(
						"birthdate",
						new DateParam(thePatientToMatch.getBirthDateElement().getValueAsString()));
		ca.uhn.fhir.rest.api.server.IBundleProvider bundle = myPatientDao.search(map, theRequestDetails);
		for (IBaseResource patientResource : bundle.getAllResources()) {
			IIdType patientId = patientResource.getIdElement().toUnqualifiedVersionless();
			if (patientId
					.getValue()
					.equals(thePatientFromContract
							.getIdElement()
							.toUnqualifiedVersionless()
							.getValue())) {
				return true;
			}
		}
		return false;
	}

	public boolean validConsentDataAccess(Consent theConsent) {
		if (theConsent.getPolicy().isEmpty()) {
			return false;
		}
		for (Consent.ConsentPolicyComponent policyComponent : theConsent.getPolicy()) {
			if (policyComponent.getUri() == null || !validConsentPolicy(policyComponent.getUri())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * The consent policy rules are
	 * <a href="https://build.fhir.org/ig/HL7/davinci-ehrx/StructureDefinition-hrex-consent.html#notes">
	 *    described here.</a>
	 */
	private boolean validConsentPolicy(String thePolicyUri) {
		String policyTypes = StringUtils.substringAfterLast(thePolicyUri, "#");
		if (policyTypes.equals(CONSENT_POLICY_SENSITIVE_TYPE)) {
			return true;
		}
		return policyTypes.equals(CONSENT_POLICY_REGULAR_TYPE) && myRegularFilterSupported;
	}

	private void addIdentifierToConsent(Consent theConsent, Patient thePatient) {
		String consentId = getIdentifier(thePatient).getValue();
		Identifier consentIdentifier =
				new Identifier().setSystem(CONSENT_IDENTIFIER_CODE_SYSTEM).setValue(consentId);
		theConsent.addIdentifier(consentIdentifier);
	}

	public void setRegularFilterSupported(boolean theRegularFilterSupported) {
		myRegularFilterSupported = theRegularFilterSupported;
	}

	private void updateConsentPatientAndPerformer(Consent theConsent, Patient thePatient) {
		String patientRef = thePatient.getIdElement().toUnqualifiedVersionless().getValue();
		theConsent.getPatient().setReference(patientRef);
		theConsent.getPerformer().set(0, new Reference(patientRef));
	}
}
