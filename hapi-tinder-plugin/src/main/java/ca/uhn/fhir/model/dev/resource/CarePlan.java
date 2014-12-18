















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
 * HAPI/FHIR <b>CarePlan</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/CarePlan">http://hl7.org/fhir/profiles/CarePlan</a> 
 * </p>
 *
 */
@ResourceDef(name="CarePlan", profile="http://hl7.org/fhir/profiles/CarePlan", id="careplan")
public class CarePlan 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="CarePlan.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.concern</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="condition", path="CarePlan.concern", description="", type="reference"  )
	public static final String SP_CONDITION = "condition";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.concern</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONDITION = new ReferenceClientParam(SP_CONDITION);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="CarePlan.period", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>participant</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.participant.member</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="participant", path="CarePlan.participant.member", description="", type="reference"  )
	public static final String SP_PARTICIPANT = "participant";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>participant</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.participant.member</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PARTICIPANT = new ReferenceClientParam(SP_PARTICIPANT);

	/**
	 * Search parameter constant for <b>activitycode</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>CarePlan.activity.simple.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="activitycode", path="CarePlan.activity.simple.code", description="", type="token"  )
	public static final String SP_ACTIVITYCODE = "activitycode";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>activitycode</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>CarePlan.activity.simple.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTIVITYCODE = new TokenClientParam(SP_ACTIVITYCODE);

	/**
	 * Search parameter constant for <b>activitydate</b>
	 * <p>
	 * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.activity.simple.scheduled[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="activitydate", path="CarePlan.activity.simple.scheduled[x]", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date"  )
	public static final String SP_ACTIVITYDATE = "activitydate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>activitydate</b>
	 * <p>
	 * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>CarePlan.activity.simple.scheduled[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam ACTIVITYDATE = new DateClientParam(SP_ACTIVITYDATE);

	/**
	 * Search parameter constant for <b>activitydetail</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.activity.detail</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="activitydetail", path="CarePlan.activity.detail", description="", type="reference"  )
	public static final String SP_ACTIVITYDETAIL = "activitydetail";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>activitydetail</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>CarePlan.activity.detail</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ACTIVITYDETAIL = new ReferenceClientParam(SP_ACTIVITYDETAIL);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.activity.detail</b>".
	 */
	public static final Include INCLUDE_ACTIVITY_DETAIL = new Include("CarePlan.activity.detail");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.activity.simple.code</b>".
	 */
	public static final Include INCLUDE_ACTIVITY_SIMPLE_CODE = new Include("CarePlan.activity.simple.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.activity.simple.scheduled[x]</b>".
	 */
	public static final Include INCLUDE_ACTIVITY_SIMPLE_SCHEDULED = new Include("CarePlan.activity.simple.scheduled[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.concern</b>".
	 */
	public static final Include INCLUDE_CONCERN = new Include("CarePlan.concern");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.participant.member</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_MEMBER = new Include("CarePlan.participant.member");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("CarePlan.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>CarePlan.period</b>".
	 */
	public static final Include INCLUDE_PERIOD = new Include("CarePlan.period");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="patient", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the patient/subject whose intended care is described by the plan."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="CarePlanStatus",
		formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record."
	)
	private BoundCodeDt<CarePlanStatusEnum> myStatus;
	
	@Child(name="period", type=PeriodDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates when the plan did (or is intended to) come into effect and end."
	)
	private PeriodDt myPeriod;
	
	@Child(name="modified", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the most recent date on which the plan has been revised."
	)
	private DateTimeDt myModified;
	
	@Child(name="concern", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Condition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan."
	)
	private java.util.List<ResourceReferenceDt> myConcern;
	
	@Child(name="participant", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan."
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="goal", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Describes the intended objective(s) of carrying out the Care Plan."
	)
	private java.util.List<Goal> myGoal;
	
	@Child(name="activity", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc."
	)
	private java.util.List<Activity> myActivity;
	
	@Child(name="notes", type=StringDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="General notes about the care plan not covered elsewhere"
	)
	private StringDt myNotes;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPatient,  myStatus,  myPeriod,  myModified,  myConcern,  myParticipant,  myGoal,  myActivity,  myNotes);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPatient, myStatus, myPeriod, myModified, myConcern, myParticipant, myGoal, myActivity, myNotes);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public CarePlan setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient/subject whose intended care is described by the plan.
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient/subject whose intended care is described by the plan.
     * </p> 
	 */
	public CarePlan setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (CarePlanStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public BoundCodeDt<CarePlanStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CarePlanStatusEnum>(CarePlanStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (CarePlanStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (CarePlanStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public CarePlan setStatus(BoundCodeDt<CarePlanStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (CarePlanStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     * </p> 
	 */
	public CarePlan setStatus(CarePlanStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the plan did (or is intended to) come into effect and end.
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the plan did (or is intended to) come into effect and end.
     * </p> 
	 */
	public CarePlan setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>modified</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public DateTimeDt getModifiedElement() {  
		if (myModified == null) {
			myModified = new DateTimeDt();
		}
		return myModified;
	}

	
	/**
	 * Gets the value(s) for <b>modified</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public Date getModified() {  
		return getModifiedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>modified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public CarePlan setModified(DateTimeDt theValue) {
		myModified = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>modified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public CarePlan setModified( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myModified = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>modified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the most recent date on which the plan has been revised.
     * </p> 
	 */
	public CarePlan setModifiedWithSecondsPrecision( Date theDate) {
		myModified = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concern</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getConcern() {  
		if (myConcern == null) {
			myConcern = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myConcern;
	}

	/**
	 * Sets the value(s) for <b>concern</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * </p> 
	 */
	public CarePlan setConcern(java.util.List<ResourceReferenceDt> theValue) {
		myConcern = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concern</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * </p> 
	 */
	public ResourceReferenceDt addConcern() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getConcern().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>participant</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
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
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	public CarePlan setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
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
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>goal</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public java.util.List<Goal> getGoal() {  
		if (myGoal == null) {
			myGoal = new java.util.ArrayList<Goal>();
		}
		return myGoal;
	}

	/**
	 * Sets the value(s) for <b>goal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public CarePlan setGoal(java.util.List<Goal> theValue) {
		myGoal = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>goal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public Goal addGoal() {
		Goal newType = new Goal();
		getGoal().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>goal</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	public Goal getGoalFirstRep() {
		if (getGoal().isEmpty()) {
			return addGoal();
		}
		return getGoal().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>activity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public java.util.List<Activity> getActivity() {  
		if (myActivity == null) {
			myActivity = new java.util.ArrayList<Activity>();
		}
		return myActivity;
	}

	/**
	 * Sets the value(s) for <b>activity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public CarePlan setActivity(java.util.List<Activity> theValue) {
		myActivity = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>activity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public Activity addActivity() {
		Activity newType = new Activity();
		getActivity().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>activity</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	public Activity getActivityFirstRep() {
		if (getActivity().isEmpty()) {
			return addActivity();
		}
		return getActivity().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public StringDt getNotesElement() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public String getNotes() {  
		return getNotesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public CarePlan setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * General notes about the care plan not covered elsewhere
     * </p> 
	 */
	public CarePlan setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>CarePlan.participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p> 
	 */
	@Block()	
	public static class Participant 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="role", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="CarePlanParticipantRole",
		formalDefinition="Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc."
	)
	private CodeableConceptDt myRole;
	
	@Child(name="member", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The specific person or organization who is participating/expected to participate in the care plan."
	)
	private ResourceReferenceDt myMember;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myMember);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myMember);
	}

	/**
	 * Gets the value(s) for <b>role</b> (CarePlanParticipantRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.
     * </p> 
	 */
	public CodeableConceptDt getRole() {  
		if (myRole == null) {
			myRole = new CodeableConceptDt();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (CarePlanParticipantRole)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.
     * </p> 
	 */
	public Participant setRole(CodeableConceptDt theValue) {
		myRole = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>member</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific person or organization who is participating/expected to participate in the care plan.
     * </p> 
	 */
	public ResourceReferenceDt getMember() {  
		if (myMember == null) {
			myMember = new ResourceReferenceDt();
		}
		return myMember;
	}

	/**
	 * Sets the value(s) for <b>member</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specific person or organization who is participating/expected to participate in the care plan.
     * </p> 
	 */
	public Participant setMember(ResourceReferenceDt theValue) {
		myMember = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>CarePlan.goal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended objective(s) of carrying out the Care Plan.
     * </p> 
	 */
	@Block()	
	public static class Goal 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable description of a specific desired objective of the care plan."
	)
	private StringDt myDescription;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="CarePlanGoalStatus",
		formalDefinition="Indicates whether the goal has been reached and is still considered relevant"
	)
	private BoundCodeDt<CarePlanGoalStatusEnum> myStatus;
	
	@Child(name="notes", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Any comments related to the goal"
	)
	private StringDt myNotes;
	
	@Child(name="concern", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Condition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address"
	)
	private java.util.List<ResourceReferenceDt> myConcern;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDescription,  myStatus,  myNotes,  myConcern);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDescription, myStatus, myNotes, myConcern);
	}

	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of a specific desired objective of the care plan.
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
     * Human-readable description of a specific desired objective of the care plan.
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
     * Human-readable description of a specific desired objective of the care plan.
     * </p> 
	 */
	public Goal setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of a specific desired objective of the care plan.
     * </p> 
	 */
	public Goal setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (CarePlanGoalStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public BoundCodeDt<CarePlanGoalStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CarePlanGoalStatusEnum>(CarePlanGoalStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (CarePlanGoalStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (CarePlanGoalStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public Goal setStatus(BoundCodeDt<CarePlanGoalStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (CarePlanGoalStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the goal has been reached and is still considered relevant
     * </p> 
	 */
	public Goal setStatus(CarePlanGoalStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public StringDt getNotesElement() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public String getNotes() {  
		return getNotesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public Goal setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any comments related to the goal
     * </p> 
	 */
	public Goal setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concern</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getConcern() {  
		if (myConcern == null) {
			myConcern = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myConcern;
	}

	/**
	 * Sets the value(s) for <b>concern</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address
     * </p> 
	 */
	public Goal setConcern(java.util.List<ResourceReferenceDt> theValue) {
		myConcern = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concern</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address
     * </p> 
	 */
	public ResourceReferenceDt addConcern() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getConcern().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>CarePlan.activity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     * </p> 
	 */
	@Block()	
	public static class Activity 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="goal", type=UriDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting"
	)
	private java.util.List<UriDt> myGoal;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="CarePlanActivityStatus",
		formalDefinition="Identifies what progress is being made for the specific activity."
	)
	private BoundCodeDt<CarePlanActivityStatusEnum> myStatus;
	
	@Child(name="prohibited", type=BooleanDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, indicates that the described activity is one that must NOT be engaged in when following the plan."
	)
	private BooleanDt myProhibited;
	
	@Child(name="actionResulting", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc."
	)
	private java.util.List<ResourceReferenceDt> myActionResulting;
	
	@Child(name="notes", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Notes about the execution of the activity"
	)
	private StringDt myNotes;
	
	@Child(name="detail", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Procedure.class, 		ca.uhn.fhir.model.dev.resource.MedicationPrescription.class, 		ca.uhn.fhir.model.dev.resource.DiagnosticOrder.class, 		ca.uhn.fhir.model.dev.resource.Encounter.class, 		ca.uhn.fhir.model.dev.resource.Supply.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The details of the proposed activity represented in a specific resource"
	)
	private ResourceReferenceDt myDetail;
	
	@Child(name="simple", order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc"
	)
	private ActivitySimple mySimple;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGoal,  myStatus,  myProhibited,  myActionResulting,  myNotes,  myDetail,  mySimple);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGoal, myStatus, myProhibited, myActionResulting, myNotes, myDetail, mySimple);
	}

	/**
	 * Gets the value(s) for <b>goal</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public java.util.List<UriDt> getGoal() {  
		if (myGoal == null) {
			myGoal = new java.util.ArrayList<UriDt>();
		}
		return myGoal;
	}

	/**
	 * Sets the value(s) for <b>goal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public Activity setGoal(java.util.List<UriDt> theValue) {
		myGoal = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>goal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public UriDt addGoal() {
		UriDt newType = new UriDt();
		getGoal().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>goal</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
	 */
	public UriDt getGoalFirstRep() {
		if (getGoal().isEmpty()) {
			return addGoal();
		}
		return getGoal().get(0); 
	}
 	/**
	 * Adds a new value for <b>goal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Activity addGoal( String theUri) {
		if (myGoal == null) {
			myGoal = new java.util.ArrayList<UriDt>();
		}
		myGoal.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (CarePlanActivityStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public BoundCodeDt<CarePlanActivityStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CarePlanActivityStatusEnum>(CarePlanActivityStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (CarePlanActivityStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (CarePlanActivityStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public Activity setStatus(BoundCodeDt<CarePlanActivityStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (CarePlanActivityStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies what progress is being made for the specific activity.
     * </p> 
	 */
	public Activity setStatus(CarePlanActivityStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>prohibited</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public BooleanDt getProhibitedElement() {  
		if (myProhibited == null) {
			myProhibited = new BooleanDt();
		}
		return myProhibited;
	}

	
	/**
	 * Gets the value(s) for <b>prohibited</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public Boolean getProhibited() {  
		return getProhibitedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>prohibited</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public Activity setProhibited(BooleanDt theValue) {
		myProhibited = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>prohibited</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     * </p> 
	 */
	public Activity setProhibited( boolean theBoolean) {
		myProhibited = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>actionResulting</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getActionResulting() {  
		if (myActionResulting == null) {
			myActionResulting = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myActionResulting;
	}

	/**
	 * Sets the value(s) for <b>actionResulting</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     * </p> 
	 */
	public Activity setActionResulting(java.util.List<ResourceReferenceDt> theValue) {
		myActionResulting = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>actionResulting</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     * </p> 
	 */
	public ResourceReferenceDt addActionResulting() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getActionResulting().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public StringDt getNotesElement() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public String getNotes() {  
		return getNotesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public Activity setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Notes about the execution of the activity
     * </p> 
	 */
	public Activity setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The details of the proposed activity represented in a specific resource
     * </p> 
	 */
	public ResourceReferenceDt getDetail() {  
		if (myDetail == null) {
			myDetail = new ResourceReferenceDt();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The details of the proposed activity represented in a specific resource
     * </p> 
	 */
	public Activity setDetail(ResourceReferenceDt theValue) {
		myDetail = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>simple</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc
     * </p> 
	 */
	public ActivitySimple getSimple() {  
		if (mySimple == null) {
			mySimple = new ActivitySimple();
		}
		return mySimple;
	}

	/**
	 * Sets the value(s) for <b>simple</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc
     * </p> 
	 */
	public Activity setSimple(ActivitySimple theValue) {
		mySimple = theValue;
		return this;
	}
	
	

  

	}

	/**
	 * Block class for child element: <b>CarePlan.activity.simple</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc
     * </p> 
	 */
	@Block()	
	public static class ActivitySimple 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="category", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="CarePlanActivityCategory",
		formalDefinition="High-level categorization of the type of activity in a care plan."
	)
	private BoundCodeDt<CarePlanActivityCategoryEnum> myCategory;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="CarePlanActivityType",
		formalDefinition="Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="scheduled", order=2, min=0, max=1, type={
		TimingDt.class, 		PeriodDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The period, timing or frequency upon which the described activity is to occur."
	)
	private IDatatype myScheduled;
	
	@Child(name="location", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc."
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="performer", order=4, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies who's expected to be involved in the activity."
	)
	private java.util.List<ResourceReferenceDt> myPerformer;
	
	@Child(name="product", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class, 		ca.uhn.fhir.model.dev.resource.Substance.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the food, drug or other product being consumed or supplied in the activity."
	)
	private ResourceReferenceDt myProduct;
	
	@Child(name="dailyAmount", type=QuantityDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the quantity expected to be consumed in a given day."
	)
	private QuantityDt myDailyAmount;
	
	@Child(name="quantity", type=QuantityDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the quantity expected to be supplied."
	)
	private QuantityDt myQuantity;
	
	@Child(name="details", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc."
	)
	private StringDt myDetails;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCategory,  myCode,  myScheduled,  myLocation,  myPerformer,  myProduct,  myDailyAmount,  myQuantity,  myDetails);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCategory, myCode, myScheduled, myLocation, myPerformer, myProduct, myDailyAmount, myQuantity, myDetails);
	}

	/**
	 * Gets the value(s) for <b>category</b> (CarePlanActivityCategory).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public BoundCodeDt<CarePlanActivityCategoryEnum> getCategoryElement() {  
		if (myCategory == null) {
			myCategory = new BoundCodeDt<CarePlanActivityCategoryEnum>(CarePlanActivityCategoryEnum.VALUESET_BINDER);
		}
		return myCategory;
	}

	
	/**
	 * Gets the value(s) for <b>category</b> (CarePlanActivityCategory).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public String getCategory() {  
		return getCategoryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>category</b> (CarePlanActivityCategory)
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public ActivitySimple setCategory(BoundCodeDt<CarePlanActivityCategoryEnum> theValue) {
		myCategory = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>category</b> (CarePlanActivityCategory)
	 *
     * <p>
     * <b>Definition:</b>
     * High-level categorization of the type of activity in a care plan.
     * </p> 
	 */
	public ActivitySimple setCategory(CarePlanActivityCategoryEnum theValue) {
		getCategoryElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>code</b> (CarePlanActivityType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (CarePlanActivityType)
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.
     * </p> 
	 */
	public ActivitySimple setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>scheduled[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period, timing or frequency upon which the described activity is to occur.
     * </p> 
	 */
	public IDatatype getScheduled() {  
		return myScheduled;
	}

	/**
	 * Sets the value(s) for <b>scheduled[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The period, timing or frequency upon which the described activity is to occur.
     * </p> 
	 */
	public ActivitySimple setScheduled(IDatatype theValue) {
		myScheduled = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.
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
     * Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.
     * </p> 
	 */
	public ActivitySimple setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>performer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who's expected to be involved in the activity.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who's expected to be involved in the activity.
     * </p> 
	 */
	public ActivitySimple setPerformer(java.util.List<ResourceReferenceDt> theValue) {
		myPerformer = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who's expected to be involved in the activity.
     * </p> 
	 */
	public ResourceReferenceDt addPerformer() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getPerformer().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>product</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the food, drug or other product being consumed or supplied in the activity.
     * </p> 
	 */
	public ResourceReferenceDt getProduct() {  
		if (myProduct == null) {
			myProduct = new ResourceReferenceDt();
		}
		return myProduct;
	}

	/**
	 * Sets the value(s) for <b>product</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the food, drug or other product being consumed or supplied in the activity.
     * </p> 
	 */
	public ActivitySimple setProduct(ResourceReferenceDt theValue) {
		myProduct = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dailyAmount</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public QuantityDt getDailyAmount() {  
		if (myDailyAmount == null) {
			myDailyAmount = new QuantityDt();
		}
		return myDailyAmount;
	}

	/**
	 * Sets the value(s) for <b>dailyAmount</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be consumed in a given day.
     * </p> 
	 */
	public ActivitySimple setDailyAmount(QuantityDt theValue) {
		myDailyAmount = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the quantity expected to be supplied.
     * </p> 
	 */
	public ActivitySimple setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>details</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public StringDt getDetailsElement() {  
		if (myDetails == null) {
			myDetails = new StringDt();
		}
		return myDetails;
	}

	
	/**
	 * Gets the value(s) for <b>details</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public String getDetails() {  
		return getDetailsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>details</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public ActivitySimple setDetails(StringDt theValue) {
		myDetails = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>details</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
     * </p> 
	 */
	public ActivitySimple setDetails( String theString) {
		myDetails = new StringDt(theString); 
		return this; 
	}

 

	}





    @Override
    public String getResourceName() {
        return "CarePlan";
    }

}
