















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
 * HAPI/FHIR <b>Microarray</b> Resource
 * (Microarray)
 *
 * <p>
 * <b>Definition:</b>
 * A resource that displays result of a  microarray
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Microarray">http://hl7.org/fhir/profiles/Microarray</a> 
 * </p>
 *
 */
@ResourceDef(name="Microarray", profile="http://hl7.org/fhir/profiles/Microarray", id="microarray")
public class Microarray 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient described by the microarray</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Microarray.subject.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Microarray.subject.patient", description="Patient described by the microarray", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient described by the microarray</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Microarray.subject.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Microarray.subject.patient</b>".
	 */
	public static final Include INCLUDE_SUBJECT_PATIENT = new Include("Microarray.subject.patient");

	/**
	 * Search parameter constant for <b>gene</b>
	 * <p>
	 * Description: <b>Gene studied in the microarray</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Microarray.sample.gene.identity</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="gene", path="Microarray.sample.gene.identity", description="Gene studied in the microarray", type="string"  )
	public static final String SP_GENE = "gene";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>gene</b>
	 * <p>
	 * Description: <b>Gene studied in the microarray</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Microarray.sample.gene.identity</b><br/>
	 * </p>
	 */
	public static final StringClientParam GENE = new StringClientParam(SP_GENE);

	/**
	 * Search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Microarray.sample.gene.coordinate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="coordinate", path="Microarray.sample.gene.coordinate", description="Coordinate of the gene", type="string"  )
	public static final String SP_COORDINATE = "coordinate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Microarray.sample.gene.coordinate</b><br/>
	 * </p>
	 */
	public static final StringClientParam COORDINATE = new StringClientParam(SP_COORDINATE);


	@Child(name="subject", order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Subject of the microarray",
		formalDefinition="Subject of the microarray"
	)
	private java.util.List<Subject> mySubject;
	
	@Child(name="organization", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Organization",
		formalDefinition="Organization that does the microarray"
	)
	private ResourceReferenceDt myOrganization;
	
	@Child(name="date", type=DateDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Date",
		formalDefinition="Date when result of the microarray is updated"
	)
	private DateDt myDate;
	
	@Child(name="scanner", order=3, min=1, max=1)	
	@Description(
		shortDefinition="Scanner",
		formalDefinition="Scanner used in the microarray"
	)
	private Scanner myScanner;
	
	@Child(name="sample", order=4, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Sample",
		formalDefinition="Sample of a grid on the chip"
	)
	private java.util.List<Sample> mySample;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myOrganization,  myDate,  myScanner,  mySample);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myOrganization, myDate, myScanner, mySample);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject of the microarray).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the microarray
     * </p> 
	 */
	public java.util.List<Subject> getSubject() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<Subject>();
		}
		return mySubject;
	}


	/**
	 * Gets the value(s) for <b>subject</b> (Subject of the microarray).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the microarray
     * </p> 
	 */
	public java.util.List<Subject> getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<Subject>();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (Subject of the microarray)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the microarray
     * </p> 
	 */
	public Microarray setSubject(java.util.List<Subject> theValue) {
		mySubject = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>subject</b> (Subject of the microarray)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the microarray
     * </p> 
	 */
	public Subject addSubject() {
		Subject newType = new Subject();
		getSubject().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>subject</b> (Subject of the microarray),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the microarray
     * </p> 
	 */
	public Subject getSubjectFirstRep() {
		if (getSubject().isEmpty()) {
			return addSubject();
		}
		return getSubject().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>organization</b> (Organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the microarray
     * </p> 
	 */
	public ResourceReferenceDt getOrganization() {  
		if (myOrganization == null) {
			myOrganization = new ResourceReferenceDt();
		}
		return myOrganization;
	}


	/**
	 * Gets the value(s) for <b>organization</b> (Organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the microarray
     * </p> 
	 */
	public ResourceReferenceDt getOrganizationElement() {  
		if (myOrganization == null) {
			myOrganization = new ResourceReferenceDt();
		}
		return myOrganization;
	}


	/**
	 * Sets the value(s) for <b>organization</b> (Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the microarray
     * </p> 
	 */
	public Microarray setOrganization(ResourceReferenceDt theValue) {
		myOrganization = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the microarray is updated
     * </p> 
	 */
	public DateDt getDate() {  
		if (myDate == null) {
			myDate = new DateDt();
		}
		return myDate;
	}


	/**
	 * Gets the value(s) for <b>date</b> (Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the microarray is updated
     * </p> 
	 */
	public DateDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateDt();
		}
		return myDate;
	}


	/**
	 * Sets the value(s) for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the microarray is updated
     * </p> 
	 */
	public Microarray setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the microarray is updated
     * </p> 
	 */
	public Microarray setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the microarray is updated
     * </p> 
	 */
	public Microarray setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>scanner</b> (Scanner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Scanner used in the microarray
     * </p> 
	 */
	public Scanner getScanner() {  
		if (myScanner == null) {
			myScanner = new Scanner();
		}
		return myScanner;
	}


	/**
	 * Gets the value(s) for <b>scanner</b> (Scanner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Scanner used in the microarray
     * </p> 
	 */
	public Scanner getScannerElement() {  
		if (myScanner == null) {
			myScanner = new Scanner();
		}
		return myScanner;
	}


	/**
	 * Sets the value(s) for <b>scanner</b> (Scanner)
	 *
     * <p>
     * <b>Definition:</b>
     * Scanner used in the microarray
     * </p> 
	 */
	public Microarray setScanner(Scanner theValue) {
		myScanner = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sample</b> (Sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sample of a grid on the chip
     * </p> 
	 */
	public java.util.List<Sample> getSample() {  
		if (mySample == null) {
			mySample = new java.util.ArrayList<Sample>();
		}
		return mySample;
	}


	/**
	 * Gets the value(s) for <b>sample</b> (Sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sample of a grid on the chip
     * </p> 
	 */
	public java.util.List<Sample> getSampleElement() {  
		if (mySample == null) {
			mySample = new java.util.ArrayList<Sample>();
		}
		return mySample;
	}


	/**
	 * Sets the value(s) for <b>sample</b> (Sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Sample of a grid on the chip
     * </p> 
	 */
	public Microarray setSample(java.util.List<Sample> theValue) {
		mySample = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>sample</b> (Sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Sample of a grid on the chip
     * </p> 
	 */
	public Sample addSample() {
		Sample newType = new Sample();
		getSample().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>sample</b> (Sample),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Sample of a grid on the chip
     * </p> 
	 */
	public Sample getSampleFirstRep() {
		if (getSample().isEmpty()) {
			return addSample();
		}
		return getSample().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Microarray.subject</b> (Subject of the microarray)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the microarray
     * </p> 
	 */
	@Block()	
	public static class Subject 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="patient", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Resource that corresponds to the subject",
		formalDefinition="Resource that corresponds to the subject"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="sampleId", type=StringDt.class, order=1, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Id of the sample that belongs to the subject",
		formalDefinition="Id of the sample that belongs to the subject"
	)
	private java.util.List<StringDt> mySampleId;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPatient,  mySampleId);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPatient, mySampleId);
	}

	/**
	 * Gets the value(s) for <b>patient</b> (Resource that corresponds to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Resource that corresponds to the subject
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}


	/**
	 * Gets the value(s) for <b>patient</b> (Resource that corresponds to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Resource that corresponds to the subject
     * </p> 
	 */
	public ResourceReferenceDt getPatientElement() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}


	/**
	 * Sets the value(s) for <b>patient</b> (Resource that corresponds to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Resource that corresponds to the subject
     * </p> 
	 */
	public Subject setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sampleId</b> (Id of the sample that belongs to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample that belongs to the subject
     * </p> 
	 */
	public java.util.List<StringDt> getSampleId() {  
		if (mySampleId == null) {
			mySampleId = new java.util.ArrayList<StringDt>();
		}
		return mySampleId;
	}


	/**
	 * Gets the value(s) for <b>sampleId</b> (Id of the sample that belongs to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample that belongs to the subject
     * </p> 
	 */
	public java.util.List<StringDt> getSampleIdElement() {  
		if (mySampleId == null) {
			mySampleId = new java.util.ArrayList<StringDt>();
		}
		return mySampleId;
	}


	/**
	 * Sets the value(s) for <b>sampleId</b> (Id of the sample that belongs to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample that belongs to the subject
     * </p> 
	 */
	public Subject setSampleId(java.util.List<StringDt> theValue) {
		mySampleId = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>sampleId</b> (Id of the sample that belongs to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample that belongs to the subject
     * </p> 
	 */
	public StringDt addSampleId() {
		StringDt newType = new StringDt();
		getSampleId().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>sampleId</b> (Id of the sample that belongs to the subject),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample that belongs to the subject
     * </p> 
	 */
	public StringDt getSampleIdFirstRep() {
		if (getSampleId().isEmpty()) {
			return addSampleId();
		}
		return getSampleId().get(0); 
	}
 	/**
	 * Adds a new value for <b>sampleId</b> (Id of the sample that belongs to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample that belongs to the subject
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Subject addSampleId( String theString) {
		if (mySampleId == null) {
			mySampleId = new java.util.ArrayList<StringDt>();
		}
		mySampleId.add(new StringDt(theString));
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Microarray.scanner</b> (Scanner)
	 *
     * <p>
     * <b>Definition:</b>
     * Scanner used in the microarray
     * </p> 
	 */
	@Block()	
	public static class Scanner 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="manufacturer", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Scanner manufacturer",
		formalDefinition="Manufactuerer of the scanner"
	)
	private ResourceReferenceDt myManufacturer;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Scanner name",
		formalDefinition="Name of scanner model"
	)
	private StringDt myName;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Scanner version",
		formalDefinition="Version of the model"
	)
	private StringDt myVersion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myManufacturer,  myName,  myVersion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myManufacturer, myName, myVersion);
	}

	/**
	 * Gets the value(s) for <b>manufacturer</b> (Scanner manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Manufactuerer of the scanner
     * </p> 
	 */
	public ResourceReferenceDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReferenceDt();
		}
		return myManufacturer;
	}


	/**
	 * Gets the value(s) for <b>manufacturer</b> (Scanner manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Manufactuerer of the scanner
     * </p> 
	 */
	public ResourceReferenceDt getManufacturerElement() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReferenceDt();
		}
		return myManufacturer;
	}


	/**
	 * Sets the value(s) for <b>manufacturer</b> (Scanner manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * Manufactuerer of the scanner
     * </p> 
	 */
	public Scanner setManufacturer(ResourceReferenceDt theValue) {
		myManufacturer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (Scanner name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of scanner model
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}


	/**
	 * Gets the value(s) for <b>name</b> (Scanner name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of scanner model
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}


	/**
	 * Sets the value(s) for <b>name</b> (Scanner name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of scanner model
     * </p> 
	 */
	public Scanner setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Scanner name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of scanner model
     * </p> 
	 */
	public Scanner setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Scanner version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the model
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}


	/**
	 * Gets the value(s) for <b>version</b> (Scanner version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the model
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}


	/**
	 * Sets the value(s) for <b>version</b> (Scanner version)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the model
     * </p> 
	 */
	public Scanner setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>version</b> (Scanner version)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the model
     * </p> 
	 */
	public Scanner setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Microarray.sample</b> (Sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Sample of a grid on the chip
     * </p> 
	 */
	@Block()	
	public static class Sample 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Id of the sample",
		formalDefinition="Id of the sample"
	)
	private StringDt myIdentity;
	
	@Child(name="organism", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Sample",
		formalDefinition="Organism that the sample belong s to"
	)
	private CodeableConceptDt myOrganism;
	
	@Child(name="specimen", order=2, min=0, max=1)	
	@Description(
		shortDefinition="Organism",
		formalDefinition="Specimen used on the grid"
	)
	private SampleSpecimen mySpecimen;
	
	@Child(name="gene", order=3, min=1, max=1)	
	@Description(
		shortDefinition="Gene of study",
		formalDefinition="Gene of study"
	)
	private SampleGene myGene;
	
	@Child(name="intensity", type=DecimalDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Intensity",
		formalDefinition="Intensity(expression) of the gene"
	)
	private DecimalDt myIntensity;
	
	@Child(name="isControl", type=BooleanDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Control",
		formalDefinition="Whether the grid is a control in the experiment"
	)
	private BooleanDt myIsControl;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myOrganism,  mySpecimen,  myGene,  myIntensity,  myIsControl);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myOrganism, mySpecimen, myGene, myIntensity, myIsControl);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (Id of the sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}


	/**
	 * Gets the value(s) for <b>identity</b> (Id of the sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample
     * </p> 
	 */
	public StringDt getIdentityElement() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}


	/**
	 * Sets the value(s) for <b>identity</b> (Id of the sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample
     * </p> 
	 */
	public Sample setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Id of the sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the sample
     * </p> 
	 */
	public Sample setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>organism</b> (Sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organism that the sample belong s to
     * </p> 
	 */
	public CodeableConceptDt getOrganism() {  
		if (myOrganism == null) {
			myOrganism = new CodeableConceptDt();
		}
		return myOrganism;
	}


	/**
	 * Gets the value(s) for <b>organism</b> (Sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organism that the sample belong s to
     * </p> 
	 */
	public CodeableConceptDt getOrganismElement() {  
		if (myOrganism == null) {
			myOrganism = new CodeableConceptDt();
		}
		return myOrganism;
	}


	/**
	 * Sets the value(s) for <b>organism</b> (Sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Organism that the sample belong s to
     * </p> 
	 */
	public Sample setOrganism(CodeableConceptDt theValue) {
		myOrganism = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specimen</b> (Organism).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen used on the grid
     * </p> 
	 */
	public SampleSpecimen getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new SampleSpecimen();
		}
		return mySpecimen;
	}


	/**
	 * Gets the value(s) for <b>specimen</b> (Organism).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen used on the grid
     * </p> 
	 */
	public SampleSpecimen getSpecimenElement() {  
		if (mySpecimen == null) {
			mySpecimen = new SampleSpecimen();
		}
		return mySpecimen;
	}


	/**
	 * Sets the value(s) for <b>specimen</b> (Organism)
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen used on the grid
     * </p> 
	 */
	public Sample setSpecimen(SampleSpecimen theValue) {
		mySpecimen = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>gene</b> (Gene of study).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public SampleGene getGene() {  
		if (myGene == null) {
			myGene = new SampleGene();
		}
		return myGene;
	}


	/**
	 * Gets the value(s) for <b>gene</b> (Gene of study).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public SampleGene getGeneElement() {  
		if (myGene == null) {
			myGene = new SampleGene();
		}
		return myGene;
	}


	/**
	 * Sets the value(s) for <b>gene</b> (Gene of study)
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public Sample setGene(SampleGene theValue) {
		myGene = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>intensity</b> (Intensity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Intensity(expression) of the gene
     * </p> 
	 */
	public DecimalDt getIntensity() {  
		if (myIntensity == null) {
			myIntensity = new DecimalDt();
		}
		return myIntensity;
	}


	/**
	 * Gets the value(s) for <b>intensity</b> (Intensity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Intensity(expression) of the gene
     * </p> 
	 */
	public DecimalDt getIntensityElement() {  
		if (myIntensity == null) {
			myIntensity = new DecimalDt();
		}
		return myIntensity;
	}


	/**
	 * Sets the value(s) for <b>intensity</b> (Intensity)
	 *
     * <p>
     * <b>Definition:</b>
     * Intensity(expression) of the gene
     * </p> 
	 */
	public Sample setIntensity(DecimalDt theValue) {
		myIntensity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>intensity</b> (Intensity)
	 *
     * <p>
     * <b>Definition:</b>
     * Intensity(expression) of the gene
     * </p> 
	 */
	public Sample setIntensity( long theValue) {
		myIntensity = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>intensity</b> (Intensity)
	 *
     * <p>
     * <b>Definition:</b>
     * Intensity(expression) of the gene
     * </p> 
	 */
	public Sample setIntensity( double theValue) {
		myIntensity = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>intensity</b> (Intensity)
	 *
     * <p>
     * <b>Definition:</b>
     * Intensity(expression) of the gene
     * </p> 
	 */
	public Sample setIntensity( java.math.BigDecimal theValue) {
		myIntensity = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isControl</b> (Control).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the grid is a control in the experiment
     * </p> 
	 */
	public BooleanDt getIsControl() {  
		if (myIsControl == null) {
			myIsControl = new BooleanDt();
		}
		return myIsControl;
	}


	/**
	 * Gets the value(s) for <b>isControl</b> (Control).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the grid is a control in the experiment
     * </p> 
	 */
	public BooleanDt getIsControlElement() {  
		if (myIsControl == null) {
			myIsControl = new BooleanDt();
		}
		return myIsControl;
	}


	/**
	 * Sets the value(s) for <b>isControl</b> (Control)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the grid is a control in the experiment
     * </p> 
	 */
	public Sample setIsControl(BooleanDt theValue) {
		myIsControl = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>isControl</b> (Control)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the grid is a control in the experiment
     * </p> 
	 */
	public Sample setIsControl( boolean theBoolean) {
		myIsControl = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}

	/**
	 * Block class for child element: <b>Microarray.sample.specimen</b> (Organism)
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen used on the grid
     * </p> 
	 */
	@Block()	
	public static class SampleSpecimen 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Specimen type",
		formalDefinition="Type of the specimen"
	)
	private StringDt myType;
	
	@Child(name="source", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Specimen source",
		formalDefinition="Source of the specimen"
	)
	private CodeableConceptDt mySource;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySource);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySource);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Specimen type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the specimen
     * </p> 
	 */
	public StringDt getType() {  
		if (myType == null) {
			myType = new StringDt();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (Specimen type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the specimen
     * </p> 
	 */
	public StringDt getTypeElement() {  
		if (myType == null) {
			myType = new StringDt();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (Specimen type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the specimen
     * </p> 
	 */
	public SampleSpecimen setType(StringDt theValue) {
		myType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>type</b> (Specimen type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the specimen
     * </p> 
	 */
	public SampleSpecimen setType( String theString) {
		myType = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> (Specimen source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public CodeableConceptDt getSource() {  
		if (mySource == null) {
			mySource = new CodeableConceptDt();
		}
		return mySource;
	}


	/**
	 * Gets the value(s) for <b>source</b> (Specimen source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public CodeableConceptDt getSourceElement() {  
		if (mySource == null) {
			mySource = new CodeableConceptDt();
		}
		return mySource;
	}


	/**
	 * Sets the value(s) for <b>source</b> (Specimen source)
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public SampleSpecimen setSource(CodeableConceptDt theValue) {
		mySource = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Microarray.sample.gene</b> (Gene of study)
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	@Block()	
	public static class SampleGene 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the gene",
		formalDefinition="Identifier of the gene"
	)
	private StringDt myIdentity;
	
	@Child(name="coordinate", order=1, min=0, max=1)	
	@Description(
		shortDefinition="Coordinate of the gene",
		formalDefinition="Coordinate of the gene"
	)
	private SampleGeneCoordinate myCoordinate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myCoordinate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myCoordinate);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (Identifier of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}


	/**
	 * Gets the value(s) for <b>identity</b> (Identifier of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public StringDt getIdentityElement() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}


	/**
	 * Sets the value(s) for <b>identity</b> (Identifier of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public SampleGene setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Identifier of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public SampleGene setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>coordinate</b> (Coordinate of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public SampleGeneCoordinate getCoordinate() {  
		if (myCoordinate == null) {
			myCoordinate = new SampleGeneCoordinate();
		}
		return myCoordinate;
	}


	/**
	 * Gets the value(s) for <b>coordinate</b> (Coordinate of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public SampleGeneCoordinate getCoordinateElement() {  
		if (myCoordinate == null) {
			myCoordinate = new SampleGeneCoordinate();
		}
		return myCoordinate;
	}


	/**
	 * Sets the value(s) for <b>coordinate</b> (Coordinate of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public SampleGene setCoordinate(SampleGeneCoordinate theValue) {
		myCoordinate = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>Microarray.sample.gene.coordinate</b> (Coordinate of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	@Block()	
	public static class SampleGeneCoordinate 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="chromosome", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Chromosome",
		formalDefinition="Chromosome"
	)
	private StringDt myChromosome;
	
	@Child(name="start", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Start position",
		formalDefinition="Start position"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="End position",
		formalDefinition="End position"
	)
	private IntegerDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myChromosome,  myStart,  myEnd);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myChromosome, myStart, myEnd);
	}

	/**
	 * Gets the value(s) for <b>chromosome</b> (Chromosome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public StringDt getChromosome() {  
		if (myChromosome == null) {
			myChromosome = new StringDt();
		}
		return myChromosome;
	}


	/**
	 * Gets the value(s) for <b>chromosome</b> (Chromosome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public StringDt getChromosomeElement() {  
		if (myChromosome == null) {
			myChromosome = new StringDt();
		}
		return myChromosome;
	}


	/**
	 * Sets the value(s) for <b>chromosome</b> (Chromosome)
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public SampleGeneCoordinate setChromosome(StringDt theValue) {
		myChromosome = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>chromosome</b> (Chromosome)
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public SampleGeneCoordinate setChromosome( String theString) {
		myChromosome = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}


	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStartElement() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}


	/**
	 * Sets the value(s) for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public SampleGeneCoordinate setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public SampleGeneCoordinate setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}


	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEndElement() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}


	/**
	 * Sets the value(s) for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public SampleGeneCoordinate setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public SampleGeneCoordinate setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 

	}






    @Override
    public String getResourceName() {
        return "Microarray";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
