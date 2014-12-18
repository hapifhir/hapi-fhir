















package ca.uhn.fhir.model.dev.resource;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;

import ca.uhn.fhir.model.dev.composite.AddressDt;
import ca.uhn.fhir.model.dev.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dev.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskCertaintyEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskCriticalityEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskStatusEnum;
import ca.uhn.fhir.model.dev.valueset.AdverseReactionRiskTypeEnum;
import ca.uhn.fhir.model.dev.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dev.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dev.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dev.valueset.AnswerFormatEnum;
import ca.uhn.fhir.model.dev.resource.Appointment;
import ca.uhn.fhir.model.dev.valueset.AppointmentStatusEnum;
import ca.uhn.fhir.model.dev.composite.AttachmentDt;
import ca.uhn.fhir.model.dev.resource.CarePlan;
import ca.uhn.fhir.model.dev.valueset.CarePlanActivityCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.CarePlanActivityStatusEnum;
import ca.uhn.fhir.model.dev.valueset.CarePlanGoalStatusEnum;
import ca.uhn.fhir.model.dev.valueset.CarePlanStatusEnum;
import ca.uhn.fhir.model.dev.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dev.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.valueset.CompositionAttestationModeEnum;
import ca.uhn.fhir.model.dev.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ConceptMapEquivalenceEnum;
import ca.uhn.fhir.model.dev.resource.Condition;
import ca.uhn.fhir.model.dev.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dev.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dev.composite.ContactPointDt;
import ca.uhn.fhir.model.dev.resource.Contract;
import ca.uhn.fhir.model.dev.valueset.ContractSubtypeCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ContractTermTypeCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ContractTypeCodesEnum;
import ca.uhn.fhir.model.dev.valueset.DataAbsentReasonEnum;
import ca.uhn.fhir.model.dev.resource.Device;
import ca.uhn.fhir.model.dev.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dev.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dev.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dev.resource.DiagnosticReport;
import ca.uhn.fhir.model.dev.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dev.resource.DocumentManifest;
import ca.uhn.fhir.model.dev.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dev.resource.DocumentReference;
import ca.uhn.fhir.model.dev.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dev.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dev.resource.Encounter;
import ca.uhn.fhir.model.dev.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dev.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dev.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dev.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EnteralFormulaAdditiveTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EnteralFormulaTypeEnum;
import ca.uhn.fhir.model.dev.resource.EpisodeOfCare;
import ca.uhn.fhir.model.dev.valueset.ExcludeFoodModifierEnum;
import ca.uhn.fhir.model.dev.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dev.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dev.resource.FamilyHistory;
import ca.uhn.fhir.model.dev.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dev.valueset.FluidConsistencyTypeEnum;
import ca.uhn.fhir.model.dev.valueset.FoodTypeEnum;
import ca.uhn.fhir.model.dev.resource.Group;
import ca.uhn.fhir.model.dev.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dev.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.composite.HumanNameDt;
import ca.uhn.fhir.model.dev.composite.IdentifierDt;
import ca.uhn.fhir.model.dev.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dev.resource.ImagingStudy;
import ca.uhn.fhir.model.dev.resource.Immunization;
import ca.uhn.fhir.model.dev.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dev.resource.ImmunizationRecommendation;
import ca.uhn.fhir.model.dev.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dev.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dev.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dev.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ListModeEnum;
import ca.uhn.fhir.model.dev.resource.Location;
import ca.uhn.fhir.model.dev.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dev.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dev.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dev.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dev.resource.Media;
import ca.uhn.fhir.model.dev.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dev.resource.Medication;
import ca.uhn.fhir.model.dev.resource.MedicationAdministration;
import ca.uhn.fhir.model.dev.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dev.resource.MedicationDispense;
import ca.uhn.fhir.model.dev.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dev.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dev.resource.MedicationPrescription;
import ca.uhn.fhir.model.dev.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dev.resource.MedicationStatement;
import ca.uhn.fhir.model.dev.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dev.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dev.resource.Microarray;
import ca.uhn.fhir.model.dev.valueset.ModalityEnum;
import ca.uhn.fhir.model.dev.resource.Namespace;
import ca.uhn.fhir.model.dev.valueset.NamespaceIdentifierTypeEnum;
import ca.uhn.fhir.model.dev.valueset.NamespaceStatusEnum;
import ca.uhn.fhir.model.dev.valueset.NamespaceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.NutrientModifierEnum;
import ca.uhn.fhir.model.dev.valueset.NutritionOrderStatusEnum;
import ca.uhn.fhir.model.dev.resource.Observation;
import ca.uhn.fhir.model.dev.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dev.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dev.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dev.resource.OperationDefinition;
import ca.uhn.fhir.model.dev.valueset.OperationKindEnum;
import ca.uhn.fhir.model.dev.resource.OperationOutcome;
import ca.uhn.fhir.model.dev.valueset.OperationParameterUseEnum;
import ca.uhn.fhir.model.dev.resource.Order;
import ca.uhn.fhir.model.dev.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.dev.resource.Organization;
import ca.uhn.fhir.model.dev.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipantRequiredEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipantStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ParticipationStatusEnum;
import ca.uhn.fhir.model.dev.resource.Patient;
import ca.uhn.fhir.model.dev.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.composite.PeriodDt;
import ca.uhn.fhir.model.dev.resource.Practitioner;
import ca.uhn.fhir.model.dev.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dev.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dev.valueset.PriorityCodesEnum;
import ca.uhn.fhir.model.dev.resource.Procedure;
import ca.uhn.fhir.model.dev.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.dev.resource.Profile;
import ca.uhn.fhir.model.dev.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.dev.composite.QuantityDt;
import ca.uhn.fhir.model.dev.valueset.QueryOutcomeEnum;
import ca.uhn.fhir.model.dev.resource.Questionnaire;
import ca.uhn.fhir.model.dev.valueset.QuestionnaireAnswersStatusEnum;
import ca.uhn.fhir.model.dev.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.dev.composite.RangeDt;
import ca.uhn.fhir.model.dev.composite.RatioDt;
import ca.uhn.fhir.model.dev.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.dev.resource.ReferralRequest;
import ca.uhn.fhir.model.dev.valueset.ReferralStatusEnum;
import ca.uhn.fhir.model.dev.resource.RelatedPerson;
import ca.uhn.fhir.model.dev.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.dev.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dev.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dev.composite.SampledDataDt;
import ca.uhn.fhir.model.dev.resource.Schedule;
import ca.uhn.fhir.model.dev.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.dev.resource.SecurityGroup;
import ca.uhn.fhir.model.dev.resource.SequencingAnalysis;
import ca.uhn.fhir.model.dev.resource.SequencingLab;
import ca.uhn.fhir.model.dev.resource.Slot;
import ca.uhn.fhir.model.dev.valueset.SlotStatusEnum;
import ca.uhn.fhir.model.dev.resource.Specimen;
import ca.uhn.fhir.model.dev.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dev.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dev.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.dev.resource.Substance;
import ca.uhn.fhir.model.dev.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SupplementTypeEnum;
import ca.uhn.fhir.model.dev.resource.Supply;
import ca.uhn.fhir.model.dev.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dev.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dev.valueset.SystemRestfulInteractionEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dev.valueset.TextureModifierEnum;
import ca.uhn.fhir.model.dev.composite.TimingDt;
import ca.uhn.fhir.model.dev.valueset.TypeRestfulInteractionEnum;
import ca.uhn.fhir.model.dev.resource.ValueSet;
import ca.uhn.fhir.model.dev.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dev.composite.AgeDt;
import ca.uhn.fhir.model.dev.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.DurationDt;
import ca.uhn.fhir.model.dev.composite.MoneyDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.TimeDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>Appointment</b> Resource
 * ()
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
	 * Type: <b>token</b><br/>
	 * Path: <b>Appointment.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Appointment.status", description="The overall status of the appointment", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The overall status of the appointment</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Appointment.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>actor</b>
	 * <p>
	 * Description: <b>Any one of the individuals participating in the appointment</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Appointment.participant.actor</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="actor", path="Appointment.participant.actor", description="Any one of the individuals participating in the appointment", type="reference"  )
	public static final String SP_ACTOR = "actor";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>actor</b>
	 * <p>
	 * Description: <b>Any one of the individuals participating in the appointment</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Appointment.participant.actor</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ACTOR = new ReferenceClientParam(SP_ACTOR);

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

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>One of the individuals of the appointment is this patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Appointment.participant.actor</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Appointment.participant.actor", description="One of the individuals of the appointment is this patient", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>One of the individuals of the appointment is this patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Appointment.participant.actor</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Appointment.participant.actor</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_ACTOR = new Include("Appointment.participant.actor");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Appointment.participant.status</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_STATUS = new Include("Appointment.participant.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Appointment.start</b>".
	 */
	public static final Include INCLUDE_START = new Include("Appointment.start");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Appointment.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Appointment.status");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="priority", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private IntegerDt myPriority;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="AppointmentStatus",
		formalDefinition="The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status"
	)
	private BoundCodeDt<AppointmentStatusEnum> myStatus;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private CodeableConceptDt myType;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="ApptReason",
		formalDefinition=""
	)
	private CodeableConceptDt myReason;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myDescription;
	
	@Child(name="start", type=InstantDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private InstantDt myStart;
	
	@Child(name="end", type=InstantDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private InstantDt myEnd;
	
	@Child(name="slot", order=8, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Slot.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> mySlot;
	
	@Child(name="location", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="comment", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myComment;
	
	@Child(name="order", order=11, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Order.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myOrder;
	
	@Child(name="participant", order=12, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="lastModifiedBy", order=13, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myLastModifiedBy;
	
	@Child(name="lastModified", type=DateTimeDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private DateTimeDt myLastModified;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPriority,  myStatus,  myType,  myReason,  myDescription,  myStart,  myEnd,  mySlot,  myLocation,  myComment,  myOrder,  myParticipant,  myLastModifiedBy,  myLastModified);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPriority, myStatus, myType, myReason, myDescription, myStart, myEnd, mySlot, myLocation, myComment, myOrder, myParticipant, myLastModifiedBy, myLastModified);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
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
	 * Sets the value(s) for <b>identifier</b> ()
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
	 * Adds and returns a new value for <b>identifier</b> ()
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
	 * Gets the first repetition for <b>identifier</b> (),
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
	 * Gets the value(s) for <b>priority</b> ().
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
	 * Gets the value(s) for <b>priority</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Integer getPriority() {  
		return getPriorityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>priority</b> ()
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
	 * Sets the value for <b>priority</b> ()
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
	 * Gets the value(s) for <b>status</b> (AppointmentStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public BoundCodeDt<AppointmentStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<AppointmentStatusEnum>(AppointmentStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (AppointmentStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (AppointmentStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public Appointment setStatus(BoundCodeDt<AppointmentStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (AppointmentStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status
     * </p> 
	 */
	public Appointment setStatus(AppointmentStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reason</b> (ApptReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (ApptReason)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>description</b> ().
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
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> ()
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
	 * Sets the value for <b>description</b> ()
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
	 * Gets the value(s) for <b>start</b> ().
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
	 * Gets the value(s) for <b>start</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Date getStart() {  
		return getStartElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>start</b> ()
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
	 * Sets the value for <b>start</b> ()
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
	 * Sets the value for <b>start</b> ()
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
	 * Gets the value(s) for <b>end</b> ().
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
	 * Gets the value(s) for <b>end</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Date getEnd() {  
		return getEndElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>end</b> ()
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
	 * Sets the value for <b>end</b> ()
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
	 * Sets the value for <b>end</b> ()
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
	 * Gets the value(s) for <b>slot</b> ().
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
	 * Sets the value(s) for <b>slot</b> ()
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
	 * Adds and returns a new value for <b>slot</b> ()
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
	 * Gets the value(s) for <b>location</b> ().
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
	 * Sets the value(s) for <b>location</b> ()
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
	 * Gets the value(s) for <b>comment</b> ().
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
	 * Gets the value(s) for <b>comment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getComment() {  
		return getCommentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comment</b> ()
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
	 * Sets the value for <b>comment</b> ()
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
	 * Gets the value(s) for <b>order</b> ().
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
	 * Sets the value(s) for <b>order</b> ()
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
	 * Gets the value(s) for <b>participant</b> ().
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
	 * Sets the value(s) for <b>participant</b> ()
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
	 * Adds and returns a new value for <b>participant</b> ()
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
	 * Gets the first repetition for <b>participant</b> (),
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
	 * Gets the value(s) for <b>lastModifiedBy</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getLastModifiedBy() {  
		if (myLastModifiedBy == null) {
			myLastModifiedBy = new ResourceReferenceDt();
		}
		return myLastModifiedBy;
	}

	/**
	 * Sets the value(s) for <b>lastModifiedBy</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setLastModifiedBy(ResourceReferenceDt theValue) {
		myLastModifiedBy = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>lastModified</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getLastModifiedElement() {  
		if (myLastModified == null) {
			myLastModified = new DateTimeDt();
		}
		return myLastModified;
	}

	
	/**
	 * Gets the value(s) for <b>lastModified</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Date getLastModified() {  
		return getLastModifiedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lastModified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setLastModified(DateTimeDt theValue) {
		myLastModified = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lastModified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setLastModified( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myLastModified = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>lastModified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Appointment setLastModifiedWithSecondsPrecision( Date theDate) {
		myLastModified = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Appointment.participant</b> ()
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
		shortDefinition="ParticipantType",
		formalDefinition=""
	)
	private java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> myType;
	
	@Child(name="actor", order=1, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myActor;
	
	@Child(name="required", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ParticipantRequired",
		formalDefinition="Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present."
	)
	private BoundCodeDt<ParticipantRequiredEnum> myRequired;
	
	@Child(name="status", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="ParticipationStatus",
		formalDefinition="Participation status of the Patient"
	)
	private BoundCodeDt<ParticipationStatusEnum> myStatus;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myActor,  myRequired,  myStatus);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myActor, myRequired, myStatus);
	}

	/**
	 * Gets the value(s) for <b>type</b> (ParticipantType).
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
	 * Sets the value(s) for <b>type</b> (ParticipantType)
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
	 * Add a value for <b>type</b> (ParticipantType) using an enumerated type. This
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
	 * Gets the first repetition for <b>type</b> (ParticipantType),
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
	 * Add a value for <b>type</b> (ParticipantType)
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
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> (ParticipantType)
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
	 * Gets the value(s) for <b>actor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getActor() {  
		if (myActor == null) {
			myActor = new ResourceReferenceDt();
		}
		return myActor;
	}

	/**
	 * Sets the value(s) for <b>actor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setActor(ResourceReferenceDt theValue) {
		myActor = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>required</b> (ParticipantRequired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public BoundCodeDt<ParticipantRequiredEnum> getRequiredElement() {  
		if (myRequired == null) {
			myRequired = new BoundCodeDt<ParticipantRequiredEnum>(ParticipantRequiredEnum.VALUESET_BINDER);
		}
		return myRequired;
	}

	
	/**
	 * Gets the value(s) for <b>required</b> (ParticipantRequired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public String getRequired() {  
		return getRequiredElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>required</b> (ParticipantRequired)
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public Participant setRequired(BoundCodeDt<ParticipantRequiredEnum> theValue) {
		myRequired = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>required</b> (ParticipantRequired)
	 *
     * <p>
     * <b>Definition:</b>
     * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
     * </p> 
	 */
	public Participant setRequired(ParticipantRequiredEnum theValue) {
		getRequiredElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (ParticipationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public BoundCodeDt<ParticipationStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ParticipationStatusEnum>(ParticipationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ParticipationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ParticipationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public Participant setStatus(BoundCodeDt<ParticipationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ParticipationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Participation status of the Patient
     * </p> 
	 */
	public Participant setStatus(ParticipationStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  

	}




    @Override
    public String getResourceName() {
        return "Appointment";
    }

}
