















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
 * HAPI/FHIR <b>Appointment</b> Resource
 * ((informative) A scheduled appointment for a patient and/or practitioner(s) where a service may take place)
 *
 * <p>
 * <b>Definition:</b>
 * A scheduled appointment for a patient and/or practitioner(s) where a service may take place.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Appointment">http://hl7.org/fhir/profiles/Appointment</a> 
 * </p>
 *
 */
@ResourceDef(name="Appointment", profile="http://hl7.org/fhir/profiles/Appointment", id="appointment")
public class Appointment 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Appointment date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Appointment.start</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Appointment.start", description="Appointment date/time.", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Appointment date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Appointment.start</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The overall status of the appointment</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Appointment.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Appointment.status", description="The overall status of the appointment", type="string"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The overall status of the appointment</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Appointment.status</b><br/>
	 * </p>
	 */
	public static final StringClientParam STATUS = new StringClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Appointment.participant.individual</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Appointment.participant.individual", description="The subject that the sensitivity is about", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Appointment.participant.individual</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Appointment.participant.individual</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_INDIVIDUAL = new Include("Appointment.participant.individual");

	/**
	 * Search parameter constant for <b>partstatus</b>
	 * <p>
	 * Description: <b>The Participation status of the subject, or other participant on the appointment </b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Appointment.participant.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partstatus", path="Appointment.participant.status", description="The Participation status of the subject, or other participant on the appointment ", type="token"  )
	public static final String SP_PARTSTATUS = "partstatus";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partstatus</b>
	 * <p>
	 * Description: <b>The Participation status of the subject, or other participant on the appointment </b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Appointment.participant.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PARTSTATUS = new TokenClientParam(SP_PARTSTATUS);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this item",
		formalDefinition="This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="priority", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept)",
		formalDefinition=""
	)
	private IntegerDt myPriority;
	
	@Child(name="status", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="The overall status of the Appointment",
		formalDefinition="Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status"
	)
	private CodeDt myStatus;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field",
		formalDefinition=""
	)
	private StringDt myDescription;
	
	@Child(name="start", type=InstantDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Date/Time that the appointment is to take place",
		formalDefinition=""
	)
	private InstantDt myStart;
	
	@Child(name="end", type=InstantDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Date/Time that the appointment is to conclude",
		formalDefinition=""
	)
	private InstantDt myEnd;
	
	@Child(name="schedule", type=ScheduleDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment",
		formalDefinition=""
	)
	private ScheduleDt mySchedule;
	
	@Child(name="timezone", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry",
		formalDefinition="The timezone should be a value referenced from a timezone database"
	)
	private StringDt myTimezone;
	
	@Child(name="slot", order=8, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Slot.class	})
	@Description(
		shortDefinition="The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> mySlot;
	
	@Child(name="location", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="The primary location that this appointment is to take place",
		formalDefinition=""
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="comment", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Additional comments about the appointment",
		formalDefinition=""
	)
	private StringDt myComment;
	
	@Child(name="order", order=11, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Order.class	})
	@Description(
		shortDefinition="An Order that lead to the creation of this appointment",
		formalDefinition=""
	)
	private ResourceReferenceDt myOrder;
	
	@Child(name="participant", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="List of participants involved in the appointment",
		formalDefinition=""
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="recorder", order=13, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who recorded the appointment",
		formalDefinition=""
	)
	private ResourceReferenceDt myRecorder;
	
	@Child(name="recordedDate", type=DateTimeDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Date when the sensitivity was recorded",
		formalDefinition=""
	)
	private DateTimeDt myRecordedDate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPriority,  myStatus,  myDescription,  myStart,  myEnd,  mySchedule,  myTimezone,  mySlot,  myLocation,  myComment,  myOrder,  myParticipant,  myRecorder,  myRecordedDate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPriority, myStatus, myDescription, myStart, myEnd, mySchedule, myTimezone, mySlot, myLocation, myComment, myOrder, myParticipant, myRecorder, myRecordedDate);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Appointment setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this item),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Appointment addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Appointment addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>priority</b> (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getPriority() {  
		if (myPriority == null) {
			myPriority = new IntegerDt();
		}
		return myPriority;
	}


	/**
	 * Gets the value(s) for <b>priority</b> (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getPriorityElement() {  
		if (myPriority == null) {
			myPriority = new IntegerDt();
		}
		return myPriority;
	}


	/**
	 * Sets the value(s) for <b>priority</b> (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setPriority(IntegerDt theValue) {
		myPriority = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>priority</b> (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setPriority( int theInteger) {
		myPriority = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (The overall status of the Appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public CodeDt getStatus() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}


	/**
	 * Gets the value(s) for <b>status</b> (The overall status of the Appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public CodeDt getStatusElement() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}


	/**
	 * Sets the value(s) for <b>status</b> (The overall status of the Appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public Appointment setStatus(CodeDt theValue) {
		myStatus = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>status</b> (The overall status of the Appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public Appointment setStatus( String theCode) {
		myStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}


	/**
	 * Gets the value(s) for <b>description</b> (The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}


	/**
	 * Sets the value(s) for <b>description</b> (The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Date/Time that the appointment is to take place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getStart() {  
		if (myStart == null) {
			myStart = new InstantDt();
		}
		return myStart;
	}


	/**
	 * Gets the value(s) for <b>start</b> (Date/Time that the appointment is to take place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getStartElement() {  
		if (myStart == null) {
			myStart = new InstantDt();
		}
		return myStart;
	}


	/**
	 * Sets the value(s) for <b>start</b> (Date/Time that the appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setStart(InstantDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Date/Time that the appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setStartWithMillisPrecision( Date theDate) {
		myStart = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>start</b> (Date/Time that the appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setStart( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStart = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (Date/Time that the appointment is to conclude).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getEnd() {  
		if (myEnd == null) {
			myEnd = new InstantDt();
		}
		return myEnd;
	}


	/**
	 * Gets the value(s) for <b>end</b> (Date/Time that the appointment is to conclude).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getEndElement() {  
		if (myEnd == null) {
			myEnd = new InstantDt();
		}
		return myEnd;
	}


	/**
	 * Sets the value(s) for <b>end</b> (Date/Time that the appointment is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setEnd(InstantDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (Date/Time that the appointment is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setEndWithMillisPrecision( Date theDate) {
		myEnd = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> (Date/Time that the appointment is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>schedule</b> (The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ScheduleDt getSchedule() {  
		if (mySchedule == null) {
			mySchedule = new ScheduleDt();
		}
		return mySchedule;
	}


	/**
	 * Gets the value(s) for <b>schedule</b> (The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ScheduleDt getScheduleElement() {  
		if (mySchedule == null) {
			mySchedule = new ScheduleDt();
		}
		return mySchedule;
	}


	/**
	 * Sets the value(s) for <b>schedule</b> (The recurrence schedule for the appointment. The end date in the schedule marks the end of the recurrence(s), not the end of an individual appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setSchedule(ScheduleDt theValue) {
		mySchedule = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public StringDt getTimezone() {  
		if (myTimezone == null) {
			myTimezone = new StringDt();
		}
		return myTimezone;
	}


	/**
	 * Gets the value(s) for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public StringDt getTimezoneElement() {  
		if (myTimezone == null) {
			myTimezone = new StringDt();
		}
		return myTimezone;
	}


	/**
	 * Sets the value(s) for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry)
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public Appointment setTimezone(StringDt theValue) {
		myTimezone = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>timezone</b> (The timezone that the times are to be converted to. Required for recurring appointments to remain accurate where the schedule makes the appointment cross a daylight saving boundry)
	 *
     * <p>
     * <b>Definition:</b>
     * The timezone should be a value referenced from a timezone database
     * </p> 
	 */
	public Appointment setTimezone( String theString) {
		myTimezone = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>slot</b> (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSlot() {  
		if (mySlot == null) {
			mySlot = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySlot;
	}


	/**
	 * Gets the value(s) for <b>slot</b> (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSlotElement() {  
		if (mySlot == null) {
			mySlot = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySlot;
	}


	/**
	 * Sets the value(s) for <b>slot</b> (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setSlot(java.util.List<ResourceReferenceDt> theValue) {
		mySlot = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>slot</b> (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addSlot() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSlot().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>location</b> (The primary location that this appointment is to take place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}


	/**
	 * Gets the value(s) for <b>location</b> (The primary location that this appointment is to take place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getLocationElement() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}


	/**
	 * Sets the value(s) for <b>location</b> (The primary location that this appointment is to take place)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>comment</b> (Additional comments about the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getComment() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}


	/**
	 * Gets the value(s) for <b>comment</b> (Additional comments about the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getCommentElement() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}


	/**
	 * Sets the value(s) for <b>comment</b> (Additional comments about the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>comment</b> (Additional comments about the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>order</b> (An Order that lead to the creation of this appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getOrder() {  
		if (myOrder == null) {
			myOrder = new ResourceReferenceDt();
		}
		return myOrder;
	}


	/**
	 * Gets the value(s) for <b>order</b> (An Order that lead to the creation of this appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getOrderElement() {  
		if (myOrder == null) {
			myOrder = new ResourceReferenceDt();
		}
		return myOrder;
	}


	/**
	 * Sets the value(s) for <b>order</b> (An Order that lead to the creation of this appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setOrder(ResourceReferenceDt theValue) {
		myOrder = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>participant</b> (List of participants involved in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Participant> getParticipant() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}


	/**
	 * Gets the value(s) for <b>participant</b> (List of participants involved in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Participant> getParticipantElement() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}


	/**
	 * Sets the value(s) for <b>participant</b> (List of participants involved in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>participant</b> (List of participants involved in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant addParticipant() {
		Participant newType = new Participant();
		getParticipant().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>participant</b> (List of participants involved in the appointment),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getRecorder() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}


	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getRecorderElement() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}


	/**
	 * Sets the value(s) for <b>recorder</b> (Who recorded the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setRecorder(ResourceReferenceDt theValue) {
		myRecorder = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>recordedDate</b> (Date when the sensitivity was recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getRecordedDate() {  
		if (myRecordedDate == null) {
			myRecordedDate = new DateTimeDt();
		}
		return myRecordedDate;
	}


	/**
	 * Gets the value(s) for <b>recordedDate</b> (Date when the sensitivity was recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getRecordedDateElement() {  
		if (myRecordedDate == null) {
			myRecordedDate = new DateTimeDt();
		}
		return myRecordedDate;
	}


	/**
	 * Sets the value(s) for <b>recordedDate</b> (Date when the sensitivity was recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setRecordedDate(DateTimeDt theValue) {
		myRecordedDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>recordedDate</b> (Date when the sensitivity was recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setRecordedDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRecordedDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>recordedDate</b> (Date when the sensitivity was recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setRecordedDateWithSecondsPrecision( Date theDate) {
		myRecordedDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Appointment.participant</b> (List of participants involved in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Participant 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Role of participant in the appointment",
		formalDefinition=""
	)
	private java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> myType;
	
	@Child(name="individual", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="A Person of device that is participating in the appointment",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myIndividual;
	
	@Child(name="required", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="required | optional | information-only",
		formalDefinition="Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present."
	)
	private CodeDt myRequired;
	
	@Child(name="status", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="accepted | declined | tentative | in-process | completed | needs-action",
		formalDefinition="Participation status of the Patient"
	)
	private CodeDt myStatus;
	
	@Child(name="observation", order=4, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Observation.class	})
	@Description(
		shortDefinition="Observations that lead to the creation of this appointment. (Is this 80%)",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myObservation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myIndividual,  myRequired,  myStatus,  myObservation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myIndividual, myRequired, myStatus, myObservation);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Role of participant in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<ParticipantTypeEnum>>();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (Role of participant in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> getTypeElement() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<ParticipantTypeEnum>>();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (Role of participant in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setType(java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Add a value for <b>type</b> (Role of participant in the appointment) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addType(ParticipantTypeEnum theValue) {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER, theValue);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>type</b> (Role of participant in the appointment),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> getTypeFirstRep() {
		if (getType().size() == 0) {
			addType();
		}
		return getType().get(0);
	}

	/**
	 * Add a value for <b>type</b> (Role of participant in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addType() {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> (Role of participant in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setType(ParticipantTypeEnum theValue) {
		getType().clear();
		addType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>individual</b> (A Person of device that is participating in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myIndividual;
	}


	/**
	 * Gets the value(s) for <b>individual</b> (A Person of device that is participating in the appointment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getIndividualElement() {  
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myIndividual;
	}


	/**
	 * Sets the value(s) for <b>individual</b> (A Person of device that is participating in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setIndividual(java.util.List<ResourceReferenceDt> theValue) {
		myIndividual = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>individual</b> (A Person of device that is participating in the appointment)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addIndividual() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getIndividual().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>required</b> (required | optional | information-only).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public CodeDt getRequired() {  
		if (myRequired == null) {
			myRequired = new CodeDt();
		}
		return myRequired;
	}


	/**
	 * Gets the value(s) for <b>required</b> (required | optional | information-only).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public CodeDt getRequiredElement() {  
		if (myRequired == null) {
			myRequired = new CodeDt();
		}
		return myRequired;
	}


	/**
	 * Sets the value(s) for <b>required</b> (required | optional | information-only)
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public Participant setRequired(CodeDt theValue) {
		myRequired = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>required</b> (required | optional | information-only)
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public Participant setRequired( String theCode) {
		myRequired = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (accepted | declined | tentative | in-process | completed | needs-action).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public CodeDt getStatus() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}


	/**
	 * Gets the value(s) for <b>status</b> (accepted | declined | tentative | in-process | completed | needs-action).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public CodeDt getStatusElement() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}


	/**
	 * Sets the value(s) for <b>status</b> (accepted | declined | tentative | in-process | completed | needs-action)
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public Participant setStatus(CodeDt theValue) {
		myStatus = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>status</b> (accepted | declined | tentative | in-process | completed | needs-action)
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public Participant setStatus( String theCode) {
		myStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>observation</b> (Observations that lead to the creation of this appointment. (Is this 80%)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getObservation() {  
		if (myObservation == null) {
			myObservation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myObservation;
	}


	/**
	 * Gets the value(s) for <b>observation</b> (Observations that lead to the creation of this appointment. (Is this 80%)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getObservationElement() {  
		if (myObservation == null) {
			myObservation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myObservation;
	}


	/**
	 * Sets the value(s) for <b>observation</b> (Observations that lead to the creation of this appointment. (Is this 80%))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setObservation(java.util.List<ResourceReferenceDt> theValue) {
		myObservation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>observation</b> (Observations that lead to the creation of this appointment. (Is this 80%))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addObservation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getObservation().add(newType);
		return newType; 
	}
  

	}




    @Override
    public String getResourceName() {
        return "Appointment";
    }

}
