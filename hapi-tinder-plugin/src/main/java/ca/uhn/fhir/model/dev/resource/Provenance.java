















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
 * HAPI/FHIR <b>Provenance</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Provenance information that describes the activity that led to the creation of a set of resources. This information can be used to help determine their reliability or trace where the information in them came from. The focus of the provenance resource is record keeping, audit and traceability, and not explicit statements of clinical significance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Provenance">http://hl7.org/fhir/profiles/Provenance</a> 
 * </p>
 *
 */
@ResourceDef(name="Provenance", profile="http://hl7.org/fhir/profiles/Provenance", id="provenance")
public class Provenance 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="target", path="Provenance.target", description="", type="reference"  )
	public static final String SP_TARGET = "target";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam TARGET = new ReferenceClientParam(SP_TARGET);

	/**
	 * Search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.start</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="start", path="Provenance.period.start", description="", type="date"  )
	public static final String SP_START = "start";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.start</b><br/>
	 * </p>
	 */
	public static final DateClientParam START = new DateClientParam(SP_START);

	/**
	 * Search parameter constant for <b>end</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.end</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="end", path="Provenance.period.end", description="", type="date"  )
	public static final String SP_END = "end";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>end</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.end</b><br/>
	 * </p>
	 */
	public static final DateClientParam END = new DateClientParam(SP_END);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Provenance.location", description="", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>party</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.reference</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="party", path="Provenance.agent.reference", description="", type="token"  )
	public static final String SP_PARTY = "party";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>party</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.reference</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PARTY = new TokenClientParam(SP_PARTY);

	/**
	 * Search parameter constant for <b>partytype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partytype", path="Provenance.agent.type", description="", type="token"  )
	public static final String SP_PARTYTYPE = "partytype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partytype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PARTYTYPE = new TokenClientParam(SP_PARTYTYPE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>A patient that the target resource(s) refer to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="", description="A patient that the target resource(s) refer to", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>A patient that the target resource(s) refer to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.agent.reference</b>".
	 */
	public static final Include INCLUDE_AGENT_REFERENCE = new Include("Provenance.agent.reference");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.agent.type</b>".
	 */
	public static final Include INCLUDE_AGENT_TYPE = new Include("Provenance.agent.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Provenance.location");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.period.end</b>".
	 */
	public static final Include INCLUDE_PERIOD_END = new Include("Provenance.period.end");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.period.start</b>".
	 */
	public static final Include INCLUDE_PERIOD_START = new Include("Provenance.period.start");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.target</b>".
	 */
	public static final Include INCLUDE_TARGET = new Include("Provenance.target");


	@Child(name="target", order=0, min=1, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The Reference(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity"
	)
	private java.util.List<ResourceReferenceDt> myTarget;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The period during which the activity occurred"
	)
	private PeriodDt myPeriod;
	
	@Child(name="recorded", type=InstantDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The instant of time at which the activity was recorded"
	)
	private InstantDt myRecorded;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The reason that the activity was taking place"
	)
	private CodeableConceptDt myReason;
	
	@Child(name="location", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Where the activity occurred, if relevant"
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="policy", type=UriDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc."
	)
	private java.util.List<UriDt> myPolicy;
	
	@Child(name="agent", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility"
	)
	private java.util.List<Agent> myAgent;
	
	@Child(name="entity", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An entity used in this activity"
	)
	private java.util.List<Entity> myEntity;
	
	@Child(name="integritySignature", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A digital signature on the target Reference(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation."
	)
	private StringDt myIntegritySignature;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTarget,  myPeriod,  myRecorded,  myReason,  myLocation,  myPolicy,  myAgent,  myEntity,  myIntegritySignature);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTarget, myPeriod, myRecorded, myReason, myLocation, myPolicy, myAgent, myEntity, myIntegritySignature);
	}

	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Reference(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getTarget() {  
		if (myTarget == null) {
			myTarget = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Reference(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity
     * </p> 
	 */
	public Provenance setTarget(java.util.List<ResourceReferenceDt> theValue) {
		myTarget = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Reference(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity
     * </p> 
	 */
	public ResourceReferenceDt addTarget() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getTarget().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the activity occurred
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
     * The period during which the activity occurred
     * </p> 
	 */
	public Provenance setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>recorded</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public InstantDt getRecordedElement() {  
		if (myRecorded == null) {
			myRecorded = new InstantDt();
		}
		return myRecorded;
	}

	
	/**
	 * Gets the value(s) for <b>recorded</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Date getRecorded() {  
		return getRecordedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>recorded</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Provenance setRecorded(InstantDt theValue) {
		myRecorded = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>recorded</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Provenance setRecordedWithMillisPrecision( Date theDate) {
		myRecorded = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>recorded</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Provenance setRecorded( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRecorded = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reason</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason that the activity was taking place
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The reason that the activity was taking place
     * </p> 
	 */
	public Provenance setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where the activity occurred, if relevant
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
     * Where the activity occurred, if relevant
     * </p> 
	 */
	public Provenance setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>policy</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public java.util.List<UriDt> getPolicy() {  
		if (myPolicy == null) {
			myPolicy = new java.util.ArrayList<UriDt>();
		}
		return myPolicy;
	}

	/**
	 * Sets the value(s) for <b>policy</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public Provenance setPolicy(java.util.List<UriDt> theValue) {
		myPolicy = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>policy</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public UriDt addPolicy() {
		UriDt newType = new UriDt();
		getPolicy().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>policy</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public UriDt getPolicyFirstRep() {
		if (getPolicy().isEmpty()) {
			return addPolicy();
		}
		return getPolicy().get(0); 
	}
 	/**
	 * Adds a new value for <b>policy</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Provenance addPolicy( String theUri) {
		if (myPolicy == null) {
			myPolicy = new java.util.ArrayList<UriDt>();
		}
		myPolicy.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>agent</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public java.util.List<Agent> getAgent() {  
		if (myAgent == null) {
			myAgent = new java.util.ArrayList<Agent>();
		}
		return myAgent;
	}

	/**
	 * Sets the value(s) for <b>agent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public Provenance setAgent(java.util.List<Agent> theValue) {
		myAgent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>agent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public Agent addAgent() {
		Agent newType = new Agent();
		getAgent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>agent</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public Agent getAgentFirstRep() {
		if (getAgent().isEmpty()) {
			return addAgent();
		}
		return getAgent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>entity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public java.util.List<Entity> getEntity() {  
		if (myEntity == null) {
			myEntity = new java.util.ArrayList<Entity>();
		}
		return myEntity;
	}

	/**
	 * Sets the value(s) for <b>entity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public Provenance setEntity(java.util.List<Entity> theValue) {
		myEntity = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>entity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public Entity addEntity() {
		Entity newType = new Entity();
		getEntity().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>entity</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public Entity getEntityFirstRep() {
		if (getEntity().isEmpty()) {
			return addEntity();
		}
		return getEntity().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>integritySignature</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target Reference(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public StringDt getIntegritySignatureElement() {  
		if (myIntegritySignature == null) {
			myIntegritySignature = new StringDt();
		}
		return myIntegritySignature;
	}

	
	/**
	 * Gets the value(s) for <b>integritySignature</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target Reference(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public String getIntegritySignature() {  
		return getIntegritySignatureElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>integritySignature</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target Reference(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public Provenance setIntegritySignature(StringDt theValue) {
		myIntegritySignature = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>integritySignature</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target Reference(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public Provenance setIntegritySignature( String theString) {
		myIntegritySignature = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Provenance.agent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	@Block()	
	public static class Agent 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="role", type=CodingDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ProvenanceAgentRole",
		formalDefinition="The role that the participant played"
	)
	private CodingDt myRole;
	
	@Child(name="type", type=CodingDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ProvenanceAgentType",
		formalDefinition="The type of the participant"
	)
	private CodingDt myType;
	
	@Child(name="reference", type=UriDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identity of participant. May be a logical or physical uri and maybe absolute or relative"
	)
	private UriDt myReference;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable description of the participant"
	)
	private StringDt myDisplay;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myType,  myReference,  myDisplay);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myType, myReference, myDisplay);
	}

	/**
	 * Gets the value(s) for <b>role</b> (ProvenanceAgentRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The role that the participant played
     * </p> 
	 */
	public CodingDt getRole() {  
		if (myRole == null) {
			myRole = new CodingDt();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (ProvenanceAgentRole)
	 *
     * <p>
     * <b>Definition:</b>
     * The role that the participant played
     * </p> 
	 */
	public Agent setRole(CodingDt theValue) {
		myRole = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (ProvenanceAgentType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the participant
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ProvenanceAgentType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the participant
     * </p> 
	 */
	public Agent setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public UriDt getReferenceElement() {  
		if (myReference == null) {
			myReference = new UriDt();
		}
		return myReference;
	}

	
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public URI getReference() {  
		return getReferenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Agent setReference(UriDt theValue) {
		myReference = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Agent setReference( String theUri) {
		myReference = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public Agent setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public Agent setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Provenance.entity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	@Block()	
	public static class Entity 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="role", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ProvenanceEntityRole",
		formalDefinition="How the entity was used during the activity"
	)
	private BoundCodeDt<ProvenanceEntityRoleEnum> myRole;
	
	@Child(name="type", type=CodingDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ProvenanceEntityType",
		formalDefinition="The type of the entity. If the entity is a resource, then this is a resource type"
	)
	private CodingDt myType;
	
	@Child(name="reference", type=UriDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identity of participant. May be a logical or physical uri and maybe absolute or relative"
	)
	private UriDt myReference;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable description of the entity"
	)
	private StringDt myDisplay;
	
	@Child(name="agent", type=Agent.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity"
	)
	private Agent myAgent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myType,  myReference,  myDisplay,  myAgent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myType, myReference, myDisplay, myAgent);
	}

	/**
	 * Gets the value(s) for <b>role</b> (ProvenanceEntityRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public BoundCodeDt<ProvenanceEntityRoleEnum> getRoleElement() {  
		if (myRole == null) {
			myRole = new BoundCodeDt<ProvenanceEntityRoleEnum>(ProvenanceEntityRoleEnum.VALUESET_BINDER);
		}
		return myRole;
	}

	
	/**
	 * Gets the value(s) for <b>role</b> (ProvenanceEntityRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public String getRole() {  
		return getRoleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>role</b> (ProvenanceEntityRole)
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public Entity setRole(BoundCodeDt<ProvenanceEntityRoleEnum> theValue) {
		myRole = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>role</b> (ProvenanceEntityRole)
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public Entity setRole(ProvenanceEntityRoleEnum theValue) {
		getRoleElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (ProvenanceEntityType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the entity. If the entity is a resource, then this is a resource type
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ProvenanceEntityType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the entity. If the entity is a resource, then this is a resource type
     * </p> 
	 */
	public Entity setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public UriDt getReferenceElement() {  
		if (myReference == null) {
			myReference = new UriDt();
		}
		return myReference;
	}

	
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public URI getReference() {  
		return getReferenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Entity setReference(UriDt theValue) {
		myReference = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Entity setReference( String theUri) {
		myReference = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public Entity setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public Entity setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>agent</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity
     * </p> 
	 */
	public Agent getAgent() {  
		if (myAgent == null) {
			myAgent = new Agent();
		}
		return myAgent;
	}

	/**
	 * Sets the value(s) for <b>agent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity
     * </p> 
	 */
	public Entity setAgent(Agent theValue) {
		myAgent = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Provenance";
    }

}
