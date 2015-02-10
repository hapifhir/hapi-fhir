















package ca.uhn.fhir.model.dstu.resource;


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;

import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dstu.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.resource.Appointment;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.resource.Availability;
import ca.uhn.fhir.model.dstu.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu.resource.CarePlan;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanGoalStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.CompositionAttestationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConceptMapEquivalenceEnum;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.valueset.ConditionRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.CriticalityEnum;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentReference;
import ca.uhn.fhir.model.dstu.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.resource.FamilyHistory;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.resource.GVFMeta;
import ca.uhn.fhir.model.dstu.resource.Group;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.resource.Immunization;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dstu.resource.ImmunizationRecommendation;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ListModeEnum;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.resource.Media;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dstu.resource.Microarray;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Order;
import ca.uhn.fhir.model.dstu.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.resource.Procedure;
import ca.uhn.fhir.model.dstu.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QueryOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireGroupNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.dstu.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.composite.ScheduleDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityTypeEnum;
import ca.uhn.fhir.model.dstu.resource.SequencingAnalysis;
import ca.uhn.fhir.model.dstu.resource.SequencingLab;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.dstu.resource.Slot;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dstu.resource.Substance;
import ca.uhn.fhir.model.dstu.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyTypeEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.AgeDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IdrefDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>User</b> Resource
 * (A user authorized to use the system)
 *
 * <p>
 * <b>Definition:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/User">http://hl7.org/fhir/profiles/User</a> 
 * </p>
 *
 */
@ResourceDef(name="User", profile="http://hl7.org/fhir/profiles/User", id="user")
public class User 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="User.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.provider</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="provider", path="User.provider", description="", type="token"  )
	public static final String SP_PROVIDER = "provider";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.provider</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PROVIDER = new TokenClientParam(SP_PROVIDER);

	/**
	 * Search parameter constant for <b>login</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.login</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="login", path="User.login", description="", type="string"  )
	public static final String SP_LOGIN = "login";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>login</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.login</b><br/>
	 * </p>
	 */
	public static final StringClientParam LOGIN = new StringClientParam(SP_LOGIN);

	/**
	 * Search parameter constant for <b>level</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.level</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="level", path="User.level", description="", type="token"  )
	public static final String SP_LEVEL = "level";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>level</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.level</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LEVEL = new TokenClientParam(SP_LEVEL);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>User.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="User.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>User.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>User.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("User.patient");


	@Child(name="name", type=HumanNameDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The name of this user",
		formalDefinition=""
	)
	private HumanNameDt myName;
	
	@Child(name="provider", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Which system authenticates the user. Blanks = internally authenticated",
		formalDefinition=""
	)
	private UriDt myProvider;
	
	@Child(name="login", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="The login by which this user is known",
		formalDefinition=""
	)
	private StringDt myLogin;
	
	@Child(name="password", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="If internal login, the password hash (SHA 256, Hex, lowercase)",
		formalDefinition=""
	)
	private StringDt myPassword;
	
	@Child(name="level", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="The level access for this user",
		formalDefinition=""
	)
	private CodeDt myLevel;
	
	@Child(name="sessionLength", type=IntegerDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="How long a session lasts for",
		formalDefinition=""
	)
	private IntegerDt mySessionLength;
	
	@Child(name="contact", type=ContactDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact details for the user",
		formalDefinition=""
	)
	private java.util.List<ContactDt> myContact;
	
	@Child(name="patient", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient compartments the user has access to (if level is patient/family)",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myPatient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myProvider,  myLogin,  myPassword,  myLevel,  mySessionLength,  myContact,  myPatient);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myProvider, myLogin, myPassword, myLevel, mySessionLength, myContact, myPatient);
	}

	/**
	 * Gets the value(s) for <b>name</b> (The name of this user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}


	/**
	 * Gets the value(s) for <b>name</b> (The name of this user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HumanNameDt getNameElement() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}


	/**
	 * Sets the value(s) for <b>name</b> (The name of this user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getProvider() {  
		if (myProvider == null) {
			myProvider = new UriDt();
		}
		return myProvider;
	}


	/**
	 * Gets the value(s) for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getProviderElement() {  
		if (myProvider == null) {
			myProvider = new UriDt();
		}
		return myProvider;
	}


	/**
	 * Sets the value(s) for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setProvider(UriDt theValue) {
		myProvider = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setProvider( String theUri) {
		myProvider = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>login</b> (The login by which this user is known).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getLogin() {  
		if (myLogin == null) {
			myLogin = new StringDt();
		}
		return myLogin;
	}


	/**
	 * Gets the value(s) for <b>login</b> (The login by which this user is known).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getLoginElement() {  
		if (myLogin == null) {
			myLogin = new StringDt();
		}
		return myLogin;
	}


	/**
	 * Sets the value(s) for <b>login</b> (The login by which this user is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLogin(StringDt theValue) {
		myLogin = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>login</b> (The login by which this user is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLogin( String theString) {
		myLogin = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getPassword() {  
		if (myPassword == null) {
			myPassword = new StringDt();
		}
		return myPassword;
	}


	/**
	 * Gets the value(s) for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getPasswordElement() {  
		if (myPassword == null) {
			myPassword = new StringDt();
		}
		return myPassword;
	}


	/**
	 * Sets the value(s) for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setPassword(StringDt theValue) {
		myPassword = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setPassword( String theString) {
		myPassword = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>level</b> (The level access for this user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getLevel() {  
		if (myLevel == null) {
			myLevel = new CodeDt();
		}
		return myLevel;
	}


	/**
	 * Gets the value(s) for <b>level</b> (The level access for this user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getLevelElement() {  
		if (myLevel == null) {
			myLevel = new CodeDt();
		}
		return myLevel;
	}


	/**
	 * Sets the value(s) for <b>level</b> (The level access for this user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLevel(CodeDt theValue) {
		myLevel = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>level</b> (The level access for this user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLevel( String theCode) {
		myLevel = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sessionLength</b> (How long a session lasts for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getSessionLength() {  
		if (mySessionLength == null) {
			mySessionLength = new IntegerDt();
		}
		return mySessionLength;
	}


	/**
	 * Gets the value(s) for <b>sessionLength</b> (How long a session lasts for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getSessionLengthElement() {  
		if (mySessionLength == null) {
			mySessionLength = new IntegerDt();
		}
		return mySessionLength;
	}


	/**
	 * Sets the value(s) for <b>sessionLength</b> (How long a session lasts for)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setSessionLength(IntegerDt theValue) {
		mySessionLength = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sessionLength</b> (How long a session lasts for)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setSessionLength( int theInteger) {
		mySessionLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> (Contact details for the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ContactDt> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		return myContact;
	}


	/**
	 * Gets the value(s) for <b>contact</b> (Contact details for the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ContactDt> getContactElement() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		return myContact;
	}


	/**
	 * Sets the value(s) for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setContact(java.util.List<ContactDt> theValue) {
		myContact = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ContactDt addContact() {
		ContactDt newType = new ContactDt();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (Contact details for the user),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ContactDt getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
 	/**
	 * Adds a new value for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public User addContact( ContactUseEnum theContactUse,  String theValue) {
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		myContact.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public User addContact( String theValue) {
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		myContact.add(new ContactDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>patient</b> (Patient compartments the user has access to (if level is patient/family)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPatient() {  
		if (myPatient == null) {
			myPatient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPatient;
	}


	/**
	 * Gets the value(s) for <b>patient</b> (Patient compartments the user has access to (if level is patient/family)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPatientElement() {  
		if (myPatient == null) {
			myPatient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPatient;
	}


	/**
	 * Sets the value(s) for <b>patient</b> (Patient compartments the user has access to (if level is patient/family))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setPatient(java.util.List<ResourceReferenceDt> theValue) {
		myPatient = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>patient</b> (Patient compartments the user has access to (if level is patient/family))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addPatient() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getPatient().add(newType);
		return newType; 
	}
  


    @Override
    public String getResourceName() {
        return "User";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
