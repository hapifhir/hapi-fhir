















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
 * HAPI/FHIR <b>FamilyHistory</b> Resource
 * (Information about patient's relatives, relevant for patient)
 *
 * <p>
 * <b>Definition:</b>
 * Significant health events and conditions for people related to the subject relevant in the context of care for the subject
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/FamilyHistory">http://hl7.org/fhir/profiles/FamilyHistory</a> 
 * </p>
 *
 */
@ResourceDef(name="FamilyHistory", profile="http://hl7.org/fhir/profiles/FamilyHistory", id="familyhistory")
public class FamilyHistory 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of a subject to list family history items for</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>FamilyHistory.subject</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="FamilyHistory.subject", description="The identity of a subject to list family history items for", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of a subject to list family history items for</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>FamilyHistory.subject</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>FamilyHistory.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("FamilyHistory.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Id(s) for this record",
		formalDefinition="This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient history is about",
		formalDefinition="The person who this history concerns"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="note", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional details not covered elsewhere",
		formalDefinition="Conveys information about family history not specific to individual relations."
	)
	private StringDt myNote;
	
	@Child(name="relation", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Relative described by history",
		formalDefinition="The related person. Each FamilyHistory resource contains the entire family history for a single person."
	)
	private java.util.List<Relation> myRelation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myNote,  myRelation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myNote, myRelation);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Id(s) for this record).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (External Id(s) for this record).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public FamilyHistory setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Id(s) for this record),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public FamilyHistory addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public FamilyHistory addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Patient history is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this history concerns
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Gets the value(s) for <b>subject</b> (Patient history is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this history concerns
     * </p> 
	 */
	public ResourceReferenceDt getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (Patient history is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this history concerns
     * </p> 
	 */
	public FamilyHistory setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (Additional details not covered elsewhere).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}


	/**
	 * Gets the value(s) for <b>note</b> (Additional details not covered elsewhere).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public StringDt getNoteElement() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}


	/**
	 * Sets the value(s) for <b>note</b> (Additional details not covered elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public FamilyHistory setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (Additional details not covered elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public FamilyHistory setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relation</b> (Relative described by history).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public java.util.List<Relation> getRelation() {  
		if (myRelation == null) {
			myRelation = new java.util.ArrayList<Relation>();
		}
		return myRelation;
	}


	/**
	 * Gets the value(s) for <b>relation</b> (Relative described by history).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public java.util.List<Relation> getRelationElement() {  
		if (myRelation == null) {
			myRelation = new java.util.ArrayList<Relation>();
		}
		return myRelation;
	}


	/**
	 * Sets the value(s) for <b>relation</b> (Relative described by history)
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public FamilyHistory setRelation(java.util.List<Relation> theValue) {
		myRelation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>relation</b> (Relative described by history)
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public Relation addRelation() {
		Relation newType = new Relation();
		getRelation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relation</b> (Relative described by history),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public Relation getRelationFirstRep() {
		if (getRelation().isEmpty()) {
			return addRelation();
		}
		return getRelation().get(0); 
	}
  
	/**
	 * Block class for child element: <b>FamilyHistory.relation</b> (Relative described by history)
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	@Block()	
	public static class Relation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The family member described",
		formalDefinition="This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\""
	)
	private StringDt myName;
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Relationship to the subject",
		formalDefinition="The type of relationship this person has to the patient (father, mother, brother etc.)"
	)
	private CodeableConceptDt myRelationship;
	
	@Child(name="born", order=2, min=0, max=1, type={
		PeriodDt.class, 		DateDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="(approximate) date of birth",
		formalDefinition="The actual or approximate date of birth of the relative"
	)
	private IDatatype myBorn;
	
	@Child(name="deceased", order=3, min=0, max=1, type={
		BooleanDt.class, 		AgeDt.class, 		RangeDt.class, 		DateDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="Dead? How old/when?",
		formalDefinition="If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set."
	)
	private IDatatype myDeceased;
	
	@Child(name="note", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="General note about related person",
		formalDefinition="This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible."
	)
	private StringDt myNote;
	
	@Child(name="condition", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Condition that the related person had",
		formalDefinition="The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition."
	)
	private java.util.List<RelationCondition> myCondition;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myRelationship,  myBorn,  myDeceased,  myNote,  myCondition);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myRelationship, myBorn, myDeceased, myNote, myCondition);
	}

	/**
	 * Gets the value(s) for <b>name</b> (The family member described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \&quot;Aunt Susan\&quot;, \&quot;my cousin with the red hair\&quot;
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}


	/**
	 * Gets the value(s) for <b>name</b> (The family member described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \&quot;Aunt Susan\&quot;, \&quot;my cousin with the red hair\&quot;
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}


	/**
	 * Sets the value(s) for <b>name</b> (The family member described)
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \&quot;Aunt Susan\&quot;, \&quot;my cousin with the red hair\&quot;
     * </p> 
	 */
	public Relation setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (The family member described)
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \&quot;Aunt Susan\&quot;, \&quot;my cousin with the red hair\&quot;
     * </p> 
	 */
	public Relation setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relationship</b> (Relationship to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public CodeableConceptDt getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new CodeableConceptDt();
		}
		return myRelationship;
	}


	/**
	 * Gets the value(s) for <b>relationship</b> (Relationship to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public CodeableConceptDt getRelationshipElement() {  
		if (myRelationship == null) {
			myRelationship = new CodeableConceptDt();
		}
		return myRelationship;
	}


	/**
	 * Sets the value(s) for <b>relationship</b> (Relationship to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public Relation setRelationship(CodeableConceptDt theValue) {
		myRelationship = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>born[x]</b> ((approximate) date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public IDatatype getBorn() {  
		return myBorn;
	}


	/**
	 * Gets the value(s) for <b>born[x]</b> ((approximate) date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public IDatatype getBornElement() {  
		return myBorn;
	}


	/**
	 * Sets the value(s) for <b>born[x]</b> ((approximate) date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public Relation setBorn(IDatatype theValue) {
		myBorn = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>deceased[x]</b> (Dead? How old/when?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public IDatatype getDeceased() {  
		return myDeceased;
	}


	/**
	 * Gets the value(s) for <b>deceased[x]</b> (Dead? How old/when?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public IDatatype getDeceasedElement() {  
		return myDeceased;
	}


	/**
	 * Sets the value(s) for <b>deceased[x]</b> (Dead? How old/when?)
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public Relation setDeceased(IDatatype theValue) {
		myDeceased = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (General note about related person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}


	/**
	 * Gets the value(s) for <b>note</b> (General note about related person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public StringDt getNoteElement() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}


	/**
	 * Sets the value(s) for <b>note</b> (General note about related person)
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public Relation setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (General note about related person)
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public Relation setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>condition</b> (Condition that the related person had).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public java.util.List<RelationCondition> getCondition() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<RelationCondition>();
		}
		return myCondition;
	}


	/**
	 * Gets the value(s) for <b>condition</b> (Condition that the related person had).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public java.util.List<RelationCondition> getConditionElement() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<RelationCondition>();
		}
		return myCondition;
	}


	/**
	 * Sets the value(s) for <b>condition</b> (Condition that the related person had)
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public Relation setCondition(java.util.List<RelationCondition> theValue) {
		myCondition = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>condition</b> (Condition that the related person had)
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public RelationCondition addCondition() {
		RelationCondition newType = new RelationCondition();
		getCondition().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>condition</b> (Condition that the related person had),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public RelationCondition getConditionFirstRep() {
		if (getCondition().isEmpty()) {
			return addCondition();
		}
		return getCondition().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>FamilyHistory.relation.condition</b> (Condition that the related person had)
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	@Block()	
	public static class RelationCondition 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Condition suffered by relation",
		formalDefinition="The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system"
	)
	private CodeableConceptDt myType;
	
	@Child(name="outcome", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="deceased | permanent disability | etc.",
		formalDefinition="Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation."
	)
	private CodeableConceptDt myOutcome;
	
	@Child(name="onset", order=2, min=0, max=1, type={
		AgeDt.class, 		RangeDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="When condition first manifested",
		formalDefinition="Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence."
	)
	private IDatatype myOnset;
	
	@Child(name="note", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Extra information about condition",
		formalDefinition="An area where general notes can be placed about this specific condition."
	)
	private StringDt myNote;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myOutcome,  myOnset,  myNote);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myOutcome, myOnset, myNote);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Condition suffered by relation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (Condition suffered by relation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
     * </p> 
	 */
	public CodeableConceptDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (Condition suffered by relation)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
     * </p> 
	 */
	public RelationCondition setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>outcome</b> (deceased | permanent disability | etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public CodeableConceptDt getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new CodeableConceptDt();
		}
		return myOutcome;
	}


	/**
	 * Gets the value(s) for <b>outcome</b> (deceased | permanent disability | etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public CodeableConceptDt getOutcomeElement() {  
		if (myOutcome == null) {
			myOutcome = new CodeableConceptDt();
		}
		return myOutcome;
	}


	/**
	 * Sets the value(s) for <b>outcome</b> (deceased | permanent disability | etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public RelationCondition setOutcome(CodeableConceptDt theValue) {
		myOutcome = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>onset[x]</b> (When condition first manifested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public IDatatype getOnset() {  
		return myOnset;
	}


	/**
	 * Gets the value(s) for <b>onset[x]</b> (When condition first manifested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public IDatatype getOnsetElement() {  
		return myOnset;
	}


	/**
	 * Sets the value(s) for <b>onset[x]</b> (When condition first manifested)
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public RelationCondition setOnset(IDatatype theValue) {
		myOnset = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (Extra information about condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}


	/**
	 * Gets the value(s) for <b>note</b> (Extra information about condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public StringDt getNoteElement() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}


	/**
	 * Sets the value(s) for <b>note</b> (Extra information about condition)
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public RelationCondition setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (Extra information about condition)
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public RelationCondition setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 

	}





    @Override
    public String getResourceName() {
        return "FamilyHistory";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
