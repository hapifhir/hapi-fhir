package ca.uhn.fhir.jpa.model;

import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Patient;

public class MemberMatchPreHookEvent {
	/**
	 * The patient that is the focus of the member-match action.
	 */
	private Patient myPatient;

	/**
	 * The old/existing coverage.
	 */
	private Coverage myCoverageToMatch;

	/**
	 * The new coverage.
	 */
	private Coverage myCoverageToLink;

	/**
	 * The consent object that will be stored and checked
	 * on the future $export request.
	 */
	private Consent myConsent;

	public Patient getPatient() {
		return myPatient;
	}

	public void setPatient(Patient thePatient) {
		myPatient = thePatient;
	}

	public Coverage getCoverageToMatch() {
		return myCoverageToMatch;
	}

	public void setCoverageToMatch(Coverage theCoverageToMatch) {
		myCoverageToMatch = theCoverageToMatch;
	}

	public Coverage getCoverageToLink() {
		return myCoverageToLink;
	}

	public void setCoverageToLink(Coverage theCoverageToLink) {
		myCoverageToLink = theCoverageToLink;
	}

	public Consent getConsent() {
		return myConsent;
	}

	public void setConsent(Consent theConsent) {
		myConsent = theConsent;
	}
}
