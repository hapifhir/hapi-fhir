package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_IDENTIFIER;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_NEW_COVERAGE;

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

public class MemberMatcherR4Helper {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MemberMatcherR4Helper.class);

	private static final String OUT_COVERAGE_IDENTIFIER_CODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v2-0203";
	private static final String OUT_COVERAGE_IDENTIFIER_CODE = "MB";
	private static final String OUT_COVERAGE_IDENTIFIER_TEXT = "Member Number";
	private static final String COVERAGE_TYPE = "Coverage";
	private static final String CONSENT_POLICY_REGULAR_TYPE = "regular";
	private static final String CONSENT_POLICY_SENSITIVE_TYPE = "sensitive";
	public static final String CONSENT_IDENTIFIER_CODE_SYSTEM = "https://smilecdr.com/fhir/ns/member-match-fixme";

	private final FhirContext myFhirContext;
	private final IFhirResourceDao<Coverage> myCoverageDao;
	private final IFhirResourceDao<Patient> myPatientDao;
	private final IFhirResourceDao<Consent> myConsentDao;
	// by default, not provided
	// but if it is, extensions can be added to Consent on $member-match
	@Nullable
	private final IConsentExtensionProvider myIConsentExtensionProvider;

	private boolean myRegularFilterSupported = false;

	public MemberMatcherR4Helper(
		FhirContext theContext,
		IFhirResourceDao<Coverage> theCoverageDao,
		IFhirResourceDao<Patient> thePatientDao,
		IFhirResourceDao<Consent> theConsentDao,
		@Nullable IConsentExtensionProvider theExtensionProvider
	) {
		myFhirContext = theContext;
		myConsentDao = theConsentDao;
		myPatientDao = thePatientDao;
		myCoverageDao = theCoverageDao;
		myIConsentExtensionProvider = theExtensionProvider;
	}

	/**
	 * Find Coverage matching the received member (Patient) by coverage id or by coverage identifier only
	 */
	public Optional<Coverage> findMatchingCoverage(Coverage theCoverageToMatch) {
		// search by received old coverage id
		List<IBaseResource> foundCoverages = findCoverageByCoverageId(theCoverageToMatch);
		if (foundCoverages.size() == 1 && isCoverage(foundCoverages.get(0))) {
			return Optional.of((Coverage) foundCoverages.get(0));
		}

		// search by received old coverage identifier
		foundCoverages = findCoverageByCoverageIdentifier(theCoverageToMatch);
		if (foundCoverages.size() == 1 && isCoverage(foundCoverages.get(0))) {
			return Optional.of((Coverage) foundCoverages.get(0));
		}

		return Optional.empty();
	}


	private List<IBaseResource> findCoverageByCoverageIdentifier(Coverage theCoverageToMatch) {
		TokenOrListParam identifierParam = new TokenOrListParam();
		for (Identifier identifier : theCoverageToMatch.getIdentifier()) {
			identifierParam.add(identifier.getSystem(), identifier.getValue());
		}

		SearchParameterMap paramMap = new SearchParameterMap()
			.add("identifier", identifierParam);
		ca.uhn.fhir.rest.api.server.IBundleProvider retVal = myCoverageDao.search(paramMap);

		return retVal.getAllResources();
	}


	private boolean isCoverage(IBaseResource theIBaseResource) {
		return theIBaseResource.fhirType().equals(COVERAGE_TYPE);
	}


	private List<IBaseResource> findCoverageByCoverageId(Coverage theCoverageToMatch) {
		SearchParameterMap paramMap = new SearchParameterMap()
			.add("_id", new StringParam(theCoverageToMatch.getId()));
		ca.uhn.fhir.rest.api.server.IBundleProvider retVal = myCoverageDao.search(paramMap);

		return retVal.getAllResources();
	}

	public void updateConsentForMemberMatch(Consent theConsent, Patient thePatient, Patient theMemberPatient) {
		addClientIdAsExtensionToConsentIfAvailable(theConsent);
		addIdentifierToConsent(theConsent, theMemberPatient);
		updateConsentPatientAndPerformer(theConsent, thePatient);

		// save the resource
		myConsentDao.create(theConsent);
	}

	public Parameters buildSuccessReturnParameters(Patient theMemberPatient, Coverage theCoverage, Consent theConsent) {
		IBaseParameters parameters = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_MEMBER_PATIENT, theMemberPatient);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_NEW_COVERAGE, theCoverage);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_CONSENT, theConsent);
		ParametersUtil.addParameterToParameters(myFhirContext, parameters, PARAM_MEMBER_IDENTIFIER, getIdentifier(theMemberPatient));
		return (Parameters) parameters;
	}

	private Identifier getIdentifier(Patient theMemberPatient) {
		List<Identifier> memberIdentifiers = theMemberPatient.getIdentifier();
		if (memberIdentifiers != null && memberIdentifiers.size() > 0) {
			for (Identifier memberIdentifier : memberIdentifiers) {
				List<Coding> typeCodings = memberIdentifier.getType().getCoding();
				if (memberIdentifier.getType() != null && typeCodings != null && typeCodings.size() > 0) {
					for (Coding typeCoding : typeCodings) {
						if (typeCoding.getCode().equals("MB")) {
							return memberIdentifier;
						}
					}
				}
			}
		}
		String i18nMessage = myFhirContext.getLocalizer().getMessage(
			"operation.member.match.error.beneficiary.without.identifier");
		throw new UnprocessableEntityException(Msg.code(2219) + i18nMessage);
	}

	public void addMemberIdentifierToMemberPatient(Patient theMemberPatient, Identifier theNewIdentifier) {
		Coding coding = new Coding()
			.setSystem(OUT_COVERAGE_IDENTIFIER_CODE_SYSTEM)
			.setCode(OUT_COVERAGE_IDENTIFIER_CODE)
			.setDisplay(OUT_COVERAGE_IDENTIFIER_TEXT)
			.setUserSelected(false);

		CodeableConcept concept = new CodeableConcept()
			.setCoding(Lists.newArrayList(coding))
			.setText(OUT_COVERAGE_IDENTIFIER_TEXT);

		Identifier newIdentifier = new Identifier()
			.setUse(Identifier.IdentifierUse.USUAL)
			.setType(concept)
			.setSystem(theNewIdentifier.getSystem())
			.setValue(theNewIdentifier.getValue());

		theMemberPatient.addIdentifier(newIdentifier);
	}

	/**
	 * If there is a client id
	 *
	 * @param theConsent - the consent to modify
	 */
	private void addClientIdAsExtensionToConsentIfAvailable(Consent theConsent) {
		if (myIConsentExtensionProvider != null) {
			Collection<IBaseExtension> extensions = myIConsentExtensionProvider.getConsentExtension(theConsent);

			for (IBaseExtension ext : extensions) {
				if (ext instanceof Extension) {
					theConsent.addExtension((Extension) ext);
				} else {
					Extension extR4 = new Extension();
					extR4.setUrl(ext.getUrl());
					extR4.setValue(ext.getValue());
					theConsent.addExtension(extR4);
				}
			}

			ourLog.trace("{} extension(s) added to Consent", extensions.size());
		}
	}

	public Optional<Patient> getBeneficiaryPatient(Coverage theCoverage) {
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

		Patient beneficiary = myPatientDao.read(new IdDt(beneficiaryRef.getReference()));
		return Optional.ofNullable(beneficiary);
	}

	/**
	 * Matching by member patient demographics - family name and birthdate only
	 */
	public boolean validPatientMember(Patient thePatientFromContract, Patient thePatientToMatch) {
		if (thePatientFromContract == null || thePatientFromContract.getIdElement() == null) {
			return false;
		}
		StringOrListParam familyName = new StringOrListParam();
		for (HumanName name : thePatientToMatch.getName()) {
			familyName.addOr(new StringParam(name.getFamily()));
		}
		SearchParameterMap map = new SearchParameterMap()
			.add("family", familyName)
			.add("birthdate", new DateParam(thePatientToMatch.getBirthDateElement().getValueAsString()));
		ca.uhn.fhir.rest.api.server.IBundleProvider bundle = myPatientDao.search(map);
		for (IBaseResource patientResource : bundle.getAllResources()) {
			IIdType patientId = patientResource.getIdElement().toUnqualifiedVersionless();
			if (patientId.getValue().equals(thePatientFromContract.getIdElement().toUnqualifiedVersionless().getValue())) {
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
	 * This is filtering the Consent policy data. The rule is specified in
	 * https://build.fhir.org/ig/HL7/davinci-ehrx/StructureDefinition-hrex-consent.html#notes
	 */
	private boolean validConsentPolicy(String thePolicyUri) {
		String policyTypes = StringUtils.substringAfterLast(thePolicyUri, "#");
		if (policyTypes.equals(CONSENT_POLICY_SENSITIVE_TYPE)) {
			return true;
		}
		if (policyTypes.equals(CONSENT_POLICY_REGULAR_TYPE) && myRegularFilterSupported) {
			return true;
		}
		return false;
	}

	private void addIdentifierToConsent(Consent theConsent, Patient thePatient) {
		String consentId = getIdentifier(thePatient).getValue();
		Identifier consentIdentifier = new Identifier().setSystem(CONSENT_IDENTIFIER_CODE_SYSTEM).setValue(consentId);
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
