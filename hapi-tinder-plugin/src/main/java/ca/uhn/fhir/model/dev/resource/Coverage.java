















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
 * HAPI/FHIR <b>Coverage</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Financial instrument which may be used to pay for or reimburse for health care products and services.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Health care programs and insurers are significant payors of health service costs
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Coverage">http://hl7.org/fhir/profiles/Coverage</a> 
 * </p>
 *
 */
@ResourceDef(name="Coverage", profile="http://hl7.org/fhir/profiles/Coverage", id="coverage")
public class Coverage 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>issuer</b>
	 * <p>
	 * Description: <b>The identity of the insurer</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Coverage.issuer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="issuer", path="Coverage.issuer", description="The identity of the insurer", type="reference"  )
	public static final String SP_ISSUER = "issuer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>issuer</b>
	 * <p>
	 * Description: <b>The identity of the insurer</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Coverage.issuer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ISSUER = new ReferenceClientParam(SP_ISSUER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The primary identifier of the insured</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Coverage.identifier", description="The primary identifier of the insured", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The primary identifier of the insured</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The kind of coverage</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Coverage.type", description="The kind of coverage", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The kind of coverage</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>plan</b>
	 * <p>
	 * Description: <b>A plan or policy identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.plan</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="plan", path="Coverage.plan", description="A plan or policy identifier", type="token"  )
	public static final String SP_PLAN = "plan";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>plan</b>
	 * <p>
	 * Description: <b>A plan or policy identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.plan</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PLAN = new TokenClientParam(SP_PLAN);

	/**
	 * Search parameter constant for <b>subplan</b>
	 * <p>
	 * Description: <b>Sub-plan identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.subplan</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subplan", path="Coverage.subplan", description="Sub-plan identifier", type="token"  )
	public static final String SP_SUBPLAN = "subplan";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subplan</b>
	 * <p>
	 * Description: <b>Sub-plan identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.subplan</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SUBPLAN = new TokenClientParam(SP_SUBPLAN);

	/**
	 * Search parameter constant for <b>group</b>
	 * <p>
	 * Description: <b>Group identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.group</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="group", path="Coverage.group", description="Group identifier", type="token"  )
	public static final String SP_GROUP = "group";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>group</b>
	 * <p>
	 * Description: <b>Group identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.group</b><br/>
	 * </p>
	 */
	public static final TokenClientParam GROUP = new TokenClientParam(SP_GROUP);

	/**
	 * Search parameter constant for <b>dependent</b>
	 * <p>
	 * Description: <b>Dependent number</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.dependent</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dependent", path="Coverage.dependent", description="Dependent number", type="token"  )
	public static final String SP_DEPENDENT = "dependent";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dependent</b>
	 * <p>
	 * Description: <b>Dependent number</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.dependent</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DEPENDENT = new TokenClientParam(SP_DEPENDENT);

	/**
	 * Search parameter constant for <b>sequence</b>
	 * <p>
	 * Description: <b>Sequence number</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.sequence</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="sequence", path="Coverage.sequence", description="Sequence number", type="token"  )
	public static final String SP_SEQUENCE = "sequence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>sequence</b>
	 * <p>
	 * Description: <b>Sequence number</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Coverage.sequence</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SEQUENCE = new TokenClientParam(SP_SEQUENCE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.dependent</b>".
	 */
	public static final Include INCLUDE_DEPENDENT = new Include("Coverage.dependent");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.group</b>".
	 */
	public static final Include INCLUDE_GROUP = new Include("Coverage.group");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Coverage.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.issuer</b>".
	 */
	public static final Include INCLUDE_ISSUER = new Include("Coverage.issuer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.plan</b>".
	 */
	public static final Include INCLUDE_PLAN = new Include("Coverage.plan");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.sequence</b>".
	 */
	public static final Include INCLUDE_SEQUENCE = new Include("Coverage.sequence");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.subplan</b>".
	 */
	public static final Include INCLUDE_SUBPLAN = new Include("Coverage.subplan");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Coverage.type");


	@Child(name="issuer", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The program or plan underwriter or payor."
	)
	private ResourceReferenceDt myIssuer;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force."
	)
	private PeriodDt myPeriod;
	
	@Child(name="type", type=CodingDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="CoverageType",
		formalDefinition="The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health."
	)
	private CodingDt myType;
	
	@Child(name="identifier", type=IdentifierDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="group", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID."
	)
	private StringDt myGroup;
	
	@Child(name="plan", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID."
	)
	private StringDt myPlan;
	
	@Child(name="subplan", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID."
	)
	private StringDt mySubplan;
	
	@Child(name="dependent", type=IntegerDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A unique identifier for a dependent under the coverage."
	)
	private IntegerDt myDependent;
	
	@Child(name="sequence", type=IntegerDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An optional counter for a particular instance of the identified coverage which increments upon each renewal."
	)
	private IntegerDt mySequence;
	
	@Child(name="subscriber", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due."
	)
	private ResourceReferenceDt mySubscriber;
	
	@Child(name="network", type=IdentifierDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier for a community of providers."
	)
	private IdentifierDt myNetwork;
	
	@Child(name="contract", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Contract.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The policy(s) which constitute this insurance coverage."
	)
	private java.util.List<ResourceReferenceDt> myContract;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIssuer,  myPeriod,  myType,  myIdentifier,  myGroup,  myPlan,  mySubplan,  myDependent,  mySequence,  mySubscriber,  myNetwork,  myContract);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIssuer, myPeriod, myType, myIdentifier, myGroup, myPlan, mySubplan, myDependent, mySequence, mySubscriber, myNetwork, myContract);
	}

	/**
	 * Gets the value(s) for <b>issuer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public ResourceReferenceDt getIssuer() {  
		if (myIssuer == null) {
			myIssuer = new ResourceReferenceDt();
		}
		return myIssuer;
	}

	/**
	 * Sets the value(s) for <b>issuer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public Coverage setIssuer(ResourceReferenceDt theValue) {
		myIssuer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
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
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     * </p> 
	 */
	public Coverage setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (CoverageType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (CoverageType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public Coverage setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
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
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
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
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public StringDt getGroupElement() {  
		if (myGroup == null) {
			myGroup = new StringDt();
		}
		return myGroup;
	}

	
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public String getGroup() {  
		return getGroupElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setGroup(StringDt theValue) {
		myGroup = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setGroup( String theString) {
		myGroup = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>plan</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public StringDt getPlanElement() {  
		if (myPlan == null) {
			myPlan = new StringDt();
		}
		return myPlan;
	}

	
	/**
	 * Gets the value(s) for <b>plan</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public String getPlan() {  
		return getPlanElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>plan</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan(StringDt theValue) {
		myPlan = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>plan</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan( String theString) {
		myPlan = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subplan</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public StringDt getSubplanElement() {  
		if (mySubplan == null) {
			mySubplan = new StringDt();
		}
		return mySubplan;
	}

	
	/**
	 * Gets the value(s) for <b>subplan</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public String getSubplan() {  
		return getSubplanElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>subplan</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan(StringDt theValue) {
		mySubplan = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>subplan</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan( String theString) {
		mySubplan = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dependent</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public IntegerDt getDependentElement() {  
		if (myDependent == null) {
			myDependent = new IntegerDt();
		}
		return myDependent;
	}

	
	/**
	 * Gets the value(s) for <b>dependent</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Integer getDependent() {  
		return getDependentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dependent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Coverage setDependent(IntegerDt theValue) {
		myDependent = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dependent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Coverage setDependent( int theInteger) {
		myDependent = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sequence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public IntegerDt getSequenceElement() {  
		if (mySequence == null) {
			mySequence = new IntegerDt();
		}
		return mySequence;
	}

	
	/**
	 * Gets the value(s) for <b>sequence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Integer getSequence() {  
		return getSequenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>sequence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Coverage setSequence(IntegerDt theValue) {
		mySequence = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>sequence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Coverage setSequence( int theInteger) {
		mySequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subscriber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.
     * </p> 
	 */
	public ResourceReferenceDt getSubscriber() {  
		if (mySubscriber == null) {
			mySubscriber = new ResourceReferenceDt();
		}
		return mySubscriber;
	}

	/**
	 * Sets the value(s) for <b>subscriber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.
     * </p> 
	 */
	public Coverage setSubscriber(ResourceReferenceDt theValue) {
		mySubscriber = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>network</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier for a community of providers.
     * </p> 
	 */
	public IdentifierDt getNetwork() {  
		if (myNetwork == null) {
			myNetwork = new IdentifierDt();
		}
		return myNetwork;
	}

	/**
	 * Sets the value(s) for <b>network</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier for a community of providers.
     * </p> 
	 */
	public Coverage setNetwork(IdentifierDt theValue) {
		myNetwork = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>contract</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The policy(s) which constitute this insurance coverage.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getContract() {  
		if (myContract == null) {
			myContract = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myContract;
	}

	/**
	 * Sets the value(s) for <b>contract</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The policy(s) which constitute this insurance coverage.
     * </p> 
	 */
	public Coverage setContract(java.util.List<ResourceReferenceDt> theValue) {
		myContract = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contract</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The policy(s) which constitute this insurance coverage.
     * </p> 
	 */
	public ResourceReferenceDt addContract() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getContract().add(newType);
		return newType; 
	}
  


    @Override
    public String getResourceName() {
        return "Coverage";
    }

}
