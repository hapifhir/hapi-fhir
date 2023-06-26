package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.*;

import java.util.ArrayList;
import java.util.List;


@ResourceDef(name = MyEpisodeOfCareFHIR.FHIR_RESOURCE_NAME, id = MyEpisodeOfCareFHIR.FHIR_PROFILE_NAME, profile = MyEpisodeOfCareFHIR.FHIR_PROFILE_URI)
public class MyEpisodeOfCareFHIR extends EpisodeOfCare {

	public static final String FHIR_RESOURCE_NAME = "EpisodeOfCare";
	public static final String FHIR_PROFILE_NAME = "MyEpisodeOfCare";
	public static final String FHIR_PROFILE_URI = "http://myfhir.dk/p/MyEpisodeOfCare";
	/**
	 * dischargeTo (extension)
	 */
	@Child(name = FIELD_DISCHARGETO, min = 0, max = 1, type = {StringType.class})
	@Description(shortDefinition = "", formalDefinition = "Discharge to")
	@Extension(url = EXTURL_DISCHARGETO, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.StringType ourDischargeTo;
	public static final String EXTURL_DISCHARGETO = "http://myfhir.dk/x/MyEpisodeOfCare-discharge-to";
	public static final String FIELD_DISCHARGETO = "dischargeTo";
	/**
	 * dischargeDisposition (extension)
	 */
	@Child(name = FIELD_DISCHARGEDISPOSITION, min = 0, max = 1, type = {Coding.class})
	@Description(shortDefinition = "", formalDefinition = "Category or kind of location after discharge.")
	@Extension(url = EXTURL_DISCHARGEDISPOSITION, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Coding ourDischargeDisposition;
	public static final String EXTURL_DISCHARGEDISPOSITION = "http://myfhir.dk/x/MyEpisodeOfCare-discharge-disposition";
	public static final String FIELD_DISCHARGEDISPOSITION = "dischargeDisposition";
	/**
	 * previous (extension)
	 */
	@Child(name = FIELD_PREVIOUS, min = 0, max = 1, type = {_PreviousComponent.class})
	@Description(shortDefinition = "", formalDefinition = "Previous reference between episode of care.")
	@Extension(url = EXTURL_PREVIOUS, definedLocally = false, isModifier = false)
	protected _PreviousComponent ourPrevious;
	public static final String EXTURL_PREVIOUS = "http://myfhir.dk/x/MyEpisodeOfCare-previous";
	public static final String FIELD_PREVIOUS = "previous";
	/**
	 * referralInformation (extension)
	 */
	@Child(name = FIELD_REFERRALINFORMATION, min = 1, max = 1, type = {_MyReferralInformationComponent.class})
	@Description(shortDefinition = "", formalDefinition = "Referral information related to this episode of care.")
	@Extension(url = EXTURL_REFERRALINFORMATION, definedLocally = false, isModifier = false)
	protected _MyReferralInformationComponent ourReferralInformation;
	public static final String EXTURL_REFERRALINFORMATION = "http://myfhir.dk/x/MyEpisodeOfCare-referral-information";
	public static final String FIELD_REFERRALINFORMATION = "referralInformation";
	/**
	 * eventMarker (extension)
	 */
	@Child(name = FIELD_EVENTMARKER, min = 0, max = Child.MAX_UNLIMITED, type = {_EventMarkerComponent.class})
	@Description(shortDefinition = "", formalDefinition = "Marks specific times on an episode of care with clinical or administrative relevance.")
	@Extension(url = EXTURL_EVENTMARKER, definedLocally = false, isModifier = false)
	protected List<_EventMarkerComponent> ourEventMarker;
	public static final String EXTURL_EVENTMARKER = "http://myfhir.dk/x/MyEpisodeOfCare-event-marker";
	public static final String FIELD_EVENTMARKER = "eventMarker";
	/**
	 * payor (extension)
	 */
	@Child(name = FIELD_PAYOR, min = 0, max = Child.MAX_UNLIMITED, type = {_PayorComponent.class})
	@Description(shortDefinition = "", formalDefinition = "Payor information for time periods")
	@Extension(url = EXTURL_PAYOR, definedLocally = false, isModifier = false)
	protected List<_PayorComponent> ourPayor;
	public static final String EXTURL_PAYOR = "http://myfhir.dk/x/MyEpisodeOfCare-payor";
	public static final String FIELD_PAYOR = "payor";
	/**
	 * healthIssue (extension)
	 */
	@Child(name = FIELD_HEALTHISSUE, min = 0, max = 1, type = {Condition.class})
	@Description(shortDefinition = "", formalDefinition = "The health issue this episode of care is related to.")
	@Extension(url = EXTURL_HEALTHISSUE, definedLocally = false, isModifier = false)
	protected org.hl7.fhir.dstu3.model.Reference ourHealthIssue;
	public static final String EXTURL_HEALTHISSUE = "http://myfhir.dk/x/MyEpisodeOfCare-health-issue";
	public static final String FIELD_HEALTHISSUE = "healthIssue";
	/**
	 * identifier
	 */
	@Child(name = FIELD_IDENTIFIER, min = 0, max = Child.MAX_UNLIMITED, order = Child.REPLACE_PARENT, type = {Identifier.class})
	@Description(shortDefinition = "Business Identifier(s) relevant for this EpisodeOfCare", formalDefinition = "Identifiers which the episode of care is known by.")
	protected List<org.hl7.fhir.dstu3.model.Identifier> ourIdentifier;
	public static final String FIELD_IDENTIFIER = "identifier";
	/**
	 * status
	 */
	@Child(name = FIELD_STATUS, min = 1, max = 1, order = Child.REPLACE_PARENT, modifier = true, summary = true, type = {CodeType.class})
	@Description(shortDefinition = "planned | waitlist | active | onhold | finished | cancelled | entered-in-error", formalDefinition = "Status of the episode of care.")
	protected org.hl7.fhir.dstu3.model.Enumeration<EpisodeOfCareStatus> ourStatus;
	public static final String FIELD_STATUS = "status";
	/**
	 * patient
	 */
	@Child(name = FIELD_PATIENT, min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {Patient.class})
	@Description(shortDefinition = "The patient who is the focus of this episode of care", formalDefinition = "The patient who is the subject of this episode of care.")
	protected org.hl7.fhir.dstu3.model.Reference ourPatient;
	public static final String FIELD_PATIENT = "patient";
	/**
	 * managingOrganization
	 */
	@Child(name = FIELD_MANAGINGORGANIZATION, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {Organization.class})
	@Description(shortDefinition = "Organization that assumes care", formalDefinition = "The organization that assumes care.")
	protected org.hl7.fhir.dstu3.model.Reference ourManagingOrganization;
	public static final String FIELD_MANAGINGORGANIZATION = "managingOrganization";
	/**
	 * period
	 */
	@Child(name = FIELD_PERIOD, min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {Period.class})
	@Description(shortDefinition = "Interval during responsibility is assumed", formalDefinition = "The start and end time of the episode of care.")
	protected Period ourPeriod;
	public static final String FIELD_PERIOD = "period";
	/**
	 * careManager
	 */
	@Child(name = FIELD_CAREMANAGER, min = 0, max = 1, order = Child.REPLACE_PARENT, type = {Practitioner.class})
	@Description(shortDefinition = "Care manager/care co-ordinator for the patient", formalDefinition = "Care manager")
	protected org.hl7.fhir.dstu3.model.Reference ourCareManager;
	public static final String FIELD_CAREMANAGER = "careManager";
	/**
	 * 
	 */
	@Child(name = "statusHistory", min = 0, max = 0, order = Child.REPLACE_PARENT)
	@Deprecated
	protected ca.uhn.fhir.model.api.IElement ourStatusHistory;
	/**
	 * 
	 */
	@Child(name = "type", min = 0, max = 0, order = Child.REPLACE_PARENT)
	@Deprecated
	protected ca.uhn.fhir.model.api.IElement ourType;
	/**
	 * 
	 */
	@Child(name = "diagnosis", min = 0, max = 0, order = Child.REPLACE_PARENT)
	@Deprecated
	protected ca.uhn.fhir.model.api.IElement ourDiagnosis;
	/**
	 * 
	 */
	@Child(name = "referralRequest", min = 0, max = 0, order = Child.REPLACE_PARENT)
	@Deprecated
	protected ca.uhn.fhir.model.api.IElement ourReferralRequest;
	/**
	 * 
	 */
	@Child(name = "team", min = 0, max = 0, order = Child.REPLACE_PARENT)
	@Deprecated
	protected ca.uhn.fhir.model.api.IElement ourTeam;
	/**
	 * 
	 */
	@Child(name = "account", min = 0, max = 0, order = Child.REPLACE_PARENT)
	@Deprecated
	protected ca.uhn.fhir.model.api.IElement ourAccount;

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourDischargeTo, ourDischargeDisposition, ourPrevious, ourReferralInformation, ourEventMarker, ourPayor, ourHealthIssue, ourIdentifier, ourStatus, ourPatient, ourManagingOrganization, ourPeriod, ourCareManager);
	}

	@Override
	public MyEpisodeOfCareFHIR copy() {
		MyEpisodeOfCareFHIR dst = new MyEpisodeOfCareFHIR();
		copyValues(dst);
		dst.ourDischargeTo = ourDischargeTo == null ? null : ourDischargeTo.copy();
		dst.ourDischargeDisposition = ourDischargeDisposition == null ? null : ourDischargeDisposition.copy();
		dst.ourPrevious = ourPrevious == null ? null : ourPrevious.copy();
		dst.ourReferralInformation = ourReferralInformation == null ? null : ourReferralInformation.copy();
		if (ourEventMarker != null) {
			dst.ourEventMarker = new ArrayList<_EventMarkerComponent>();
			for (_EventMarkerComponent i : ourEventMarker) {
				dst.ourEventMarker.add(i.copy());
			}
		}
		if (ourPayor != null) {
			dst.ourPayor = new ArrayList<_PayorComponent>();
			for (_PayorComponent i : ourPayor) {
				dst.ourPayor.add(i.copy());
			}
		}
		dst.ourHealthIssue = ourHealthIssue == null ? null : ourHealthIssue.copy();
		if (ourIdentifier != null) {
			dst.ourIdentifier = new ArrayList<org.hl7.fhir.dstu3.model.Identifier>();
			for (org.hl7.fhir.dstu3.model.Identifier i : ourIdentifier) {
				dst.ourIdentifier.add(i.copy());
			}
		}
		dst.ourStatus = ourStatus == null ? null : ourStatus.copy();
		dst.ourPatient = ourPatient == null ? null : ourPatient.copy();
		dst.ourManagingOrganization = ourManagingOrganization == null ? null : ourManagingOrganization.copy();
		dst.ourPeriod = ourPeriod == null ? null : ourPeriod.copy();
		dst.ourCareManager = ourCareManager == null ? null : ourCareManager.copy();
		return dst;
	}

	@Override
	public boolean equalsDeep(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsDeep(other)) {
			return false;
		}
		if (!(other instanceof MyEpisodeOfCareFHIR)) {
			return false;
		}
		MyEpisodeOfCareFHIR that = (MyEpisodeOfCareFHIR) other;
		return compareDeep(ourDischargeTo, that.ourDischargeTo, true) && compareDeep(ourDischargeDisposition, that.ourDischargeDisposition, true) && compareDeep(ourPrevious, that.ourPrevious, true) && compareDeep(ourReferralInformation, that.ourReferralInformation, true)
				&& compareDeep(ourEventMarker, that.ourEventMarker, true) && compareDeep(ourPayor, that.ourPayor, true) && compareDeep(ourHealthIssue, that.ourHealthIssue, true) && compareDeep(ourIdentifier, that.ourIdentifier, true) && compareDeep(ourStatus, that.ourStatus, true)
				&& compareDeep(ourPatient, that.ourPatient, true) && compareDeep(ourManagingOrganization, that.ourManagingOrganization, true) && compareDeep(ourPeriod, that.ourPeriod, true) && compareDeep(ourCareManager, that.ourCareManager, true);
	}

	@Override
	public boolean equalsShallow(Base other) {
		if (this == other) {
			return true;
		}
		if (!super.equalsShallow(other)) {
			return false;
		}
		if (!(other instanceof MyEpisodeOfCareFHIR)) {
			return false;
		}
		MyEpisodeOfCareFHIR that = (MyEpisodeOfCareFHIR) other;
		return compareValues(ourDischargeTo, that.ourDischargeTo, true) && compareValues(ourStatus, that.ourStatus, true);
	}

	public org.hl7.fhir.dstu3.model.StringType _getDischargeTo() {
		if (ourDischargeTo == null)
			ourDischargeTo = new org.hl7.fhir.dstu3.model.StringType();
		return ourDischargeTo;
	}

	public MyEpisodeOfCareFHIR _setDischargeTo(org.hl7.fhir.dstu3.model.StringType theValue) {
		ourDischargeTo = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Coding _getDischargeDisposition() {
		if (ourDischargeDisposition == null)
			ourDischargeDisposition = new org.hl7.fhir.dstu3.model.Coding();
		return ourDischargeDisposition;
	}

	public MyEpisodeOfCareFHIR _setDischargeDisposition(org.hl7.fhir.dstu3.model.Coding theValue) {
		ourDischargeDisposition = theValue;
		return this;
	}

	public _PreviousComponent _getPrevious() {
		if (ourPrevious == null)
			ourPrevious = new _PreviousComponent();
		return ourPrevious;
	}

	public MyEpisodeOfCareFHIR _setPrevious(_PreviousComponent theValue) {
		ourPrevious = theValue;
		return this;
	}

	public _MyReferralInformationComponent _getReferralInformation() {
		if (ourReferralInformation == null)
			ourReferralInformation = new _MyReferralInformationComponent();
		return ourReferralInformation;
	}

	public MyEpisodeOfCareFHIR _setReferralInformation(_MyReferralInformationComponent theValue) {
		ourReferralInformation = theValue;
		return this;
	}

	public List<_EventMarkerComponent> _getEventMarker() {
		if (ourEventMarker == null)
			ourEventMarker = new ArrayList<>();
		return ourEventMarker;
	}

	public MyEpisodeOfCareFHIR _setEventMarker(List<_EventMarkerComponent> theValue) {
		ourEventMarker = theValue;
		return this;
	}

	public List<_PayorComponent> _getPayor() {
		if (ourPayor == null)
			ourPayor = new ArrayList<>();
		return ourPayor;
	}

	public MyEpisodeOfCareFHIR _setPayor(List<_PayorComponent> theValue) {
		ourPayor = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Reference _getHealthIssue() {
		if (ourHealthIssue == null)
			ourHealthIssue = new org.hl7.fhir.dstu3.model.Reference();
		return ourHealthIssue;
	}

	public MyEpisodeOfCareFHIR _setHealthIssue(org.hl7.fhir.dstu3.model.Reference theValue) {
		ourHealthIssue = theValue;
		return this;
	}

	public List<org.hl7.fhir.dstu3.model.Identifier> _getIdentifier() {
		if (ourIdentifier == null)
			ourIdentifier = new ArrayList<>();
		return ourIdentifier;
	}

	public MyEpisodeOfCareFHIR _setIdentifier(List<org.hl7.fhir.dstu3.model.Identifier> theValue) {
		ourIdentifier = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Enumeration<EpisodeOfCareStatus> _getStatus() {
		if (ourStatus == null)
			ourStatus = new org.hl7.fhir.dstu3.model.Enumeration<EpisodeOfCareStatus>(new EpisodeOfCareStatusEnumFactory());
		return ourStatus;
	}

	public MyEpisodeOfCareFHIR _setStatus(org.hl7.fhir.dstu3.model.Enumeration<EpisodeOfCareStatus> theValue) {
		ourStatus = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Reference _getPatient() {
		if (ourPatient == null)
			ourPatient = new org.hl7.fhir.dstu3.model.Reference();
		return ourPatient;
	}

	public MyEpisodeOfCareFHIR _setPatient(org.hl7.fhir.dstu3.model.Reference theValue) {
		ourPatient = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Reference _getManagingOrganization() {
		if (ourManagingOrganization == null)
			ourManagingOrganization = new org.hl7.fhir.dstu3.model.Reference();
		return ourManagingOrganization;
	}

	public MyEpisodeOfCareFHIR _setManagingOrganization(org.hl7.fhir.dstu3.model.Reference theValue) {
		ourManagingOrganization = theValue;
		return this;
	}

	public Period _getPeriod() {
		if (ourPeriod == null)
			ourPeriod = new Period();
		return ourPeriod;
	}

	public MyEpisodeOfCareFHIR _setPeriod(Period theValue) {
		ourPeriod = theValue;
		return this;
	}

	public org.hl7.fhir.dstu3.model.Reference _getCareManager() {
		if (ourCareManager == null)
			ourCareManager = new org.hl7.fhir.dstu3.model.Reference();
		return ourCareManager;
	}

	public MyEpisodeOfCareFHIR _setCareManager(org.hl7.fhir.dstu3.model.Reference theValue) {
		ourCareManager = theValue;
		return this;
	}

	@Override
	@Deprecated
	public void listChildren(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasAccount() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasCareManager() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasDiagnosis() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasIdentifier() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasManagingOrganization() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasPatient() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasPeriod() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasReferralRequest() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasStatus() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasStatusElement() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasStatusHistory() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasTeam() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public boolean hasType() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public String fhirType() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public String[] getTypesForProperty(int p0, String p1) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getAccount() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getAccountTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getDiagnosis() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getIdentifier() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getReferralRequest() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getReferralRequestTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getStatusHistory() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getTeam() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getTeamTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public List getType() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Account addAccountTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Base addChild(String p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Base makeProperty(int p0, String p1) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Base setProperty(int p0, String p1, Base p2) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Base setProperty(String p0, Base p1) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Base[] getProperty(int p0, String p1, boolean p2) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public CareTeam addTeamTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public CodeableConcept addType() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public CodeableConcept getTypeFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Enumeration getStatusElement() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addAccount(Reference p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addDiagnosis(DiagnosisComponent p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addIdentifier(Identifier p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addReferralRequest(Reference p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addStatusHistory(EpisodeOfCareStatusHistoryComponent p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addTeam(Reference p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare addType(CodeableConcept p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setAccount(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setCareManager(Reference p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setCareManagerTarget(Practitioner p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setDiagnosis(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setIdentifier(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setManagingOrganization(Reference p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setManagingOrganizationTarget(Organization p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setPatient(Reference p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setPatientTarget(Patient p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setPeriod(Period p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setReferralRequest(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setStatus(EpisodeOfCareStatus p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setStatusElement(Enumeration p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setStatusHistory(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setTeam(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCare setType(List p0) {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public DiagnosisComponent addDiagnosis() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public DiagnosisComponent getDiagnosisFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCareStatus getStatus() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCareStatusHistoryComponent addStatusHistory() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public EpisodeOfCareStatusHistoryComponent getStatusHistoryFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Identifier addIdentifier() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Identifier getIdentifierFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Organization getManagingOrganizationTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Patient getPatientTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Period getPeriod() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Practitioner getCareManagerTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference addAccount() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference addReferralRequest() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference addTeam() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference getAccountFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference getCareManager() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference getManagingOrganization() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference getPatient() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference getReferralRequestFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public Reference getTeamFirstRep() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public ReferralRequest addReferralRequestTarget() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	@Deprecated
	public ResourceType getResourceType() {
		throw new UnsupportedOperationException("Deprecated method");
	}

	@Override
	protected MyEpisodeOfCareFHIR typedCopy() {
		return copy();
	}
}
