















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
 * HAPI/FHIR <b>AdverseReactionRisk</b> Resource
 * (Adverse reaction risk)
 *
 * <p>
 * <b>Definition:</b>
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To record a clinical assessment of a propensity, or potential risk to an individual, of an adverse reaction upon future exposure to the specified substance, or class of substance.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/AdverseReactionRisk">http://hl7.org/fhir/profiles/AdverseReactionRisk</a> 
 * </p>
 *
 */
@ResourceDef(name="AdverseReactionRisk", profile="http://hl7.org/fhir/profiles/AdverseReactionRisk", id="adversereactionrisk")
public class AdverseReactionRisk 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.substance | AdverseReactionRisk.event.substance</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="substance", path="AdverseReactionRisk.substance | AdverseReactionRisk.event.substance", description="", type="token"  )
	public static final String SP_SUBSTANCE = "substance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.substance | AdverseReactionRisk.event.substance</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SUBSTANCE = new TokenClientParam(SP_SUBSTANCE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="AdverseReactionRisk.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>criticality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.criticality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="criticality", path="AdverseReactionRisk.criticality", description="", type="token"  )
	public static final String SP_CRITICALITY = "criticality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>criticality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.criticality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CRITICALITY = new TokenClientParam(SP_CRITICALITY);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="AdverseReactionRisk.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.category</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="category", path="AdverseReactionRisk.category", description="", type="token"  )
	public static final String SP_CATEGORY = "category";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.category</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CATEGORY = new TokenClientParam(SP_CATEGORY);

	/**
	 * Search parameter constant for <b>last-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReactionRisk.lastOccurence</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="last-date", path="AdverseReactionRisk.lastOccurence", description="", type="date"  )
	public static final String SP_LAST_DATE = "last-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>last-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReactionRisk.lastOccurence</b><br/>
	 * </p>
	 */
	public static final DateClientParam LAST_DATE = new DateClientParam(SP_LAST_DATE);

	/**
	 * Search parameter constant for <b>manifestation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.event.manifestation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manifestation", path="AdverseReactionRisk.event.manifestation", description="", type="token"  )
	public static final String SP_MANIFESTATION = "manifestation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manifestation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.event.manifestation</b><br/>
	 * </p>
	 */
	public static final TokenClientParam MANIFESTATION = new TokenClientParam(SP_MANIFESTATION);

	/**
	 * Search parameter constant for <b>onset</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReactionRisk.event.onset</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="onset", path="AdverseReactionRisk.event.onset", description="", type="date"  )
	public static final String SP_ONSET = "onset";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>onset</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReactionRisk.event.onset</b><br/>
	 * </p>
	 */
	public static final DateClientParam ONSET = new DateClientParam(SP_ONSET);

	/**
	 * Search parameter constant for <b>duration</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>quantity</b><br/>
	 * Path: <b>AdverseReactionRisk.event.duration</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="duration", path="AdverseReactionRisk.event.duration", description="", type="quantity"  )
	public static final String SP_DURATION = "duration";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>duration</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>quantity</b><br/>
	 * Path: <b>AdverseReactionRisk.event.duration</b><br/>
	 * </p>
	 */
	public static final QuantityClientParam DURATION = new QuantityClientParam(SP_DURATION);

	/**
	 * Search parameter constant for <b>severity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.event.severity</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="severity", path="AdverseReactionRisk.event.severity", description="", type="token"  )
	public static final String SP_SEVERITY = "severity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>severity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.event.severity</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SEVERITY = new TokenClientParam(SP_SEVERITY);

	/**
	 * Search parameter constant for <b>route</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.event.exposureRoute</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="route", path="AdverseReactionRisk.event.exposureRoute", description="", type="token"  )
	public static final String SP_ROUTE = "route";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>route</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.event.exposureRoute</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ROUTE = new TokenClientParam(SP_ROUTE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="AdverseReactionRisk.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReactionRisk.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReactionRisk.recordedDate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="AdverseReactionRisk.recordedDate", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReactionRisk.recordedDate</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>recorder</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReactionRisk.recorder</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="recorder", path="AdverseReactionRisk.recorder", description="", type="reference"  )
	public static final String SP_RECORDER = "recorder";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>recorder</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReactionRisk.recorder</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RECORDER = new ReferenceClientParam(SP_RECORDER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReactionRisk.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="AdverseReactionRisk.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReactionRisk.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.category</b>".
	 */
	public static final Include INCLUDE_CATEGORY = new Include("AdverseReactionRisk.category");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.criticality</b>".
	 */
	public static final Include INCLUDE_CRITICALITY = new Include("AdverseReactionRisk.criticality");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.event.duration</b>".
	 */
	public static final Include INCLUDE_EVENT_DURATION = new Include("AdverseReactionRisk.event.duration");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.event.exposureRoute</b>".
	 */
	public static final Include INCLUDE_EVENT_EXPOSUREROUTE = new Include("AdverseReactionRisk.event.exposureRoute");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.event.manifestation</b>".
	 */
	public static final Include INCLUDE_EVENT_MANIFESTATION = new Include("AdverseReactionRisk.event.manifestation");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.event.onset</b>".
	 */
	public static final Include INCLUDE_EVENT_ONSET = new Include("AdverseReactionRisk.event.onset");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.event.severity</b>".
	 */
	public static final Include INCLUDE_EVENT_SEVERITY = new Include("AdverseReactionRisk.event.severity");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.event.substance</b>".
	 */
	public static final Include INCLUDE_EVENT_SUBSTANCE = new Include("AdverseReactionRisk.event.substance");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("AdverseReactionRisk.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.lastOccurence</b>".
	 */
	public static final Include INCLUDE_LASTOCCURENCE = new Include("AdverseReactionRisk.lastOccurence");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.recordedDate</b>".
	 */
	public static final Include INCLUDE_RECORDEDDATE = new Include("AdverseReactionRisk.recordedDate");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.recorder</b>".
	 */
	public static final Include INCLUDE_RECORDER = new Include("AdverseReactionRisk.recorder");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("AdverseReactionRisk.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("AdverseReactionRisk.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.substance</b>".
	 */
	public static final Include INCLUDE_SUBSTANCE = new Include("AdverseReactionRisk.substance");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReactionRisk.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("AdverseReactionRisk.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this item",
		formalDefinition="This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="recordedDate", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When recorded",
		formalDefinition="Date when the sensitivity was recorded"
	)
	private DateTimeDt myRecordedDate;
	
	@Child(name="recorder", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="Who recorded the sensitivity",
		formalDefinition="Indicates who has responsibility for the record"
	)
	private ResourceReferenceDt myRecorder;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="Who the sensitivity is for",
		formalDefinition="The patient who has the allergy or intolerance"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="substance", type=CodeableConceptDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Ssubstance, (or class) considered to be responsible for risk",
		formalDefinition="Identification of a substance, or a class of substances, that is considered to be responsible for the Adverse reaction risk"
	)
	private BoundCodeableConceptDt<SubstanceTypeEnum> mySubstance;
	
	@Child(name="status", type=CodeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="unconfirmed | confirmed | resolved | refuted",
		formalDefinition="Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance"
	)
	private BoundCodeDt<AdverseReactionRiskStatusEnum> myStatus;
	
	@Child(name="criticality", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="low | high - Estimated potential clinical harm",
		formalDefinition="Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance"
	)
	private BoundCodeDt<AdverseReactionRiskCriticalityEnum> myCriticality;
	
	@Child(name="type", type=CodeDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="immune | non-immune - Underlying mechanism (if known)",
		formalDefinition="Identification of the underlying physiological mechanism for the Reaction Risk"
	)
	private BoundCodeDt<AdverseReactionRiskTypeEnum> myType;
	
	@Child(name="category", type=CodeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="food | medication | environment - Category of Substance",
		formalDefinition="Category of the identified Substance"
	)
	private BoundCodeDt<AdverseReactionRiskCategoryEnum> myCategory;
	
	@Child(name="lastOccurence", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Date(/time) of last known occurence of a reaction",
		formalDefinition="Represents the date and/or time of the last known occurence of a reaction event"
	)
	private DateTimeDt myLastOccurence;
	
	@Child(name="comment", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Additional text not captured in other fields",
		formalDefinition="Additional narrative about the propensity for the Adverse Reaction, not captured in other fields."
	)
	private StringDt myComment;
	
	@Child(name="event", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Adverse Reaction Events linked to exposure to substance",
		formalDefinition="Details about each Adverse Reaction Event linked to exposure to the identified Substance"
	)
	private java.util.List<Event> myEvent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myRecordedDate,  myRecorder,  mySubject,  mySubstance,  myStatus,  myCriticality,  myType,  myCategory,  myLastOccurence,  myComment,  myEvent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myRecordedDate, myRecorder, mySubject, mySubstance, myStatus, myCriticality, myType, myCategory, myLastOccurence, myComment, myEvent);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
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
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public AdverseReactionRisk setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>recordedDate</b> (When recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public DateTimeDt getRecordedDateElement() {  
		if (myRecordedDate == null) {
			myRecordedDate = new DateTimeDt();
		}
		return myRecordedDate;
	}

	
	/**
	 * Gets the value(s) for <b>recordedDate</b> (When recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public Date getRecordedDate() {  
		return getRecordedDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>recordedDate</b> (When recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public AdverseReactionRisk setRecordedDate(DateTimeDt theValue) {
		myRecordedDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>recordedDate</b> (When recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public AdverseReactionRisk setRecordedDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRecordedDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>recordedDate</b> (When recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the sensitivity was recorded
     * </p> 
	 */
	public AdverseReactionRisk setRecordedDateWithSecondsPrecision( Date theDate) {
		myRecordedDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the sensitivity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates who has responsibility for the record
     * </p> 
	 */
	public ResourceReferenceDt getRecorder() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}

	/**
	 * Sets the value(s) for <b>recorder</b> (Who recorded the sensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates who has responsibility for the record
     * </p> 
	 */
	public AdverseReactionRisk setRecorder(ResourceReferenceDt theValue) {
		myRecorder = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> (Who the sensitivity is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who has the allergy or intolerance
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who the sensitivity is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who has the allergy or intolerance
     * </p> 
	 */
	public AdverseReactionRisk setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>substance</b> (Ssubstance, (or class) considered to be responsible for risk).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of a substance, or a class of substances, that is considered to be responsible for the Adverse reaction risk
     * </p> 
	 */
	public BoundCodeableConceptDt<SubstanceTypeEnum> getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new BoundCodeableConceptDt<SubstanceTypeEnum>(SubstanceTypeEnum.VALUESET_BINDER);
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (Ssubstance, (or class) considered to be responsible for risk)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of a substance, or a class of substances, that is considered to be responsible for the Adverse reaction risk
     * </p> 
	 */
	public AdverseReactionRisk setSubstance(BoundCodeableConceptDt<SubstanceTypeEnum> theValue) {
		mySubstance = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>substance</b> (Ssubstance, (or class) considered to be responsible for risk)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of a substance, or a class of substances, that is considered to be responsible for the Adverse reaction risk
     * </p> 
	 */
	public AdverseReactionRisk setSubstance(SubstanceTypeEnum theValue) {
		getSubstance().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (unconfirmed | confirmed | resolved | refuted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance
     * </p> 
	 */
	public BoundCodeDt<AdverseReactionRiskStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<AdverseReactionRiskStatusEnum>(AdverseReactionRiskStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (unconfirmed | confirmed | resolved | refuted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (unconfirmed | confirmed | resolved | refuted)
	 *
     * <p>
     * <b>Definition:</b>
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setStatus(BoundCodeDt<AdverseReactionRiskStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (unconfirmed | confirmed | resolved | refuted)
	 *
     * <p>
     * <b>Definition:</b>
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setStatus(AdverseReactionRiskStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>criticality</b> (low | high - Estimated potential clinical harm).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance
     * </p> 
	 */
	public BoundCodeDt<AdverseReactionRiskCriticalityEnum> getCriticalityElement() {  
		if (myCriticality == null) {
			myCriticality = new BoundCodeDt<AdverseReactionRiskCriticalityEnum>(AdverseReactionRiskCriticalityEnum.VALUESET_BINDER);
		}
		return myCriticality;
	}

	
	/**
	 * Gets the value(s) for <b>criticality</b> (low | high - Estimated potential clinical harm).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance
     * </p> 
	 */
	public String getCriticality() {  
		return getCriticalityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>criticality</b> (low | high - Estimated potential clinical harm)
	 *
     * <p>
     * <b>Definition:</b>
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setCriticality(BoundCodeDt<AdverseReactionRiskCriticalityEnum> theValue) {
		myCriticality = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>criticality</b> (low | high - Estimated potential clinical harm)
	 *
     * <p>
     * <b>Definition:</b>
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setCriticality(AdverseReactionRiskCriticalityEnum theValue) {
		getCriticalityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (immune | non-immune - Underlying mechanism (if known)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the underlying physiological mechanism for the Reaction Risk
     * </p> 
	 */
	public BoundCodeDt<AdverseReactionRiskTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<AdverseReactionRiskTypeEnum>(AdverseReactionRiskTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (immune | non-immune - Underlying mechanism (if known)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the underlying physiological mechanism for the Reaction Risk
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (immune | non-immune - Underlying mechanism (if known))
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the underlying physiological mechanism for the Reaction Risk
     * </p> 
	 */
	public AdverseReactionRisk setType(BoundCodeDt<AdverseReactionRiskTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (immune | non-immune - Underlying mechanism (if known))
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the underlying physiological mechanism for the Reaction Risk
     * </p> 
	 */
	public AdverseReactionRisk setType(AdverseReactionRiskTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>category</b> (food | medication | environment - Category of Substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Category of the identified Substance
     * </p> 
	 */
	public BoundCodeDt<AdverseReactionRiskCategoryEnum> getCategoryElement() {  
		if (myCategory == null) {
			myCategory = new BoundCodeDt<AdverseReactionRiskCategoryEnum>(AdverseReactionRiskCategoryEnum.VALUESET_BINDER);
		}
		return myCategory;
	}

	
	/**
	 * Gets the value(s) for <b>category</b> (food | medication | environment - Category of Substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Category of the identified Substance
     * </p> 
	 */
	public String getCategory() {  
		return getCategoryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>category</b> (food | medication | environment - Category of Substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Category of the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setCategory(BoundCodeDt<AdverseReactionRiskCategoryEnum> theValue) {
		myCategory = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>category</b> (food | medication | environment - Category of Substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Category of the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setCategory(AdverseReactionRiskCategoryEnum theValue) {
		getCategoryElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>lastOccurence</b> (Date(/time) of last known occurence of a reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the date and/or time of the last known occurence of a reaction event
     * </p> 
	 */
	public DateTimeDt getLastOccurenceElement() {  
		if (myLastOccurence == null) {
			myLastOccurence = new DateTimeDt();
		}
		return myLastOccurence;
	}

	
	/**
	 * Gets the value(s) for <b>lastOccurence</b> (Date(/time) of last known occurence of a reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the date and/or time of the last known occurence of a reaction event
     * </p> 
	 */
	public Date getLastOccurence() {  
		return getLastOccurenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lastOccurence</b> (Date(/time) of last known occurence of a reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the date and/or time of the last known occurence of a reaction event
     * </p> 
	 */
	public AdverseReactionRisk setLastOccurence(DateTimeDt theValue) {
		myLastOccurence = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lastOccurence</b> (Date(/time) of last known occurence of a reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the date and/or time of the last known occurence of a reaction event
     * </p> 
	 */
	public AdverseReactionRisk setLastOccurence( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myLastOccurence = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>lastOccurence</b> (Date(/time) of last known occurence of a reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the date and/or time of the last known occurence of a reaction event
     * </p> 
	 */
	public AdverseReactionRisk setLastOccurenceWithSecondsPrecision( Date theDate) {
		myLastOccurence = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comment</b> (Additional text not captured in other fields).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     * </p> 
	 */
	public StringDt getCommentElement() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}

	
	/**
	 * Gets the value(s) for <b>comment</b> (Additional text not captured in other fields).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     * </p> 
	 */
	public String getComment() {  
		return getCommentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comment</b> (Additional text not captured in other fields)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     * </p> 
	 */
	public AdverseReactionRisk setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comment</b> (Additional text not captured in other fields)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     * </p> 
	 */
	public AdverseReactionRisk setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>event</b> (Adverse Reaction Events linked to exposure to substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about each Adverse Reaction Event linked to exposure to the identified Substance
     * </p> 
	 */
	public java.util.List<Event> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<Event>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (Adverse Reaction Events linked to exposure to substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about each Adverse Reaction Event linked to exposure to the identified Substance
     * </p> 
	 */
	public AdverseReactionRisk setEvent(java.util.List<Event> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> (Adverse Reaction Events linked to exposure to substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about each Adverse Reaction Event linked to exposure to the identified Substance
     * </p> 
	 */
	public Event addEvent() {
		Event newType = new Event();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (Adverse Reaction Events linked to exposure to substance),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about each Adverse Reaction Event linked to exposure to the identified Substance
     * </p> 
	 */
	public Event getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Block class for child element: <b>AdverseReactionRisk.event</b> (Adverse Reaction Events linked to exposure to substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about each Adverse Reaction Event linked to exposure to the identified Substance
     * </p> 
	 */
	@Block()	
	public static class Event 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="substance", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Specific substance considered to be responsible for event",
		formalDefinition="Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AdverseReactionRisk.substance and ignore the AdverseReactionRisk.event.substance"
	)
	private BoundCodeableConceptDt<SubstanceTypeEnum> mySubstance;
	
	@Child(name="certainty", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="unlikely | likely | confirmed - clinical certainty about the specific substance",
		formalDefinition="Statement about the degree of clinical certainty that the Specific Substance was the cause of the Manifestation in this reaction event"
	)
	private BoundCodeDt<AdverseReactionRiskCertaintyEnum> myCertainty;
	
	@Child(name="manifestation", type=CodeableConceptDt.class, order=2, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Clinical symptoms/signs associated with the Event",
		formalDefinition="Clinical symptoms and/or signs that are observed or associated with the Adverse Reaction Event"
	)
	private java.util.List<CodeableConceptDt> myManifestation;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Description of the event as a whole",
		formalDefinition="Text description about the Reaction as a whole, including details of the manifestation if required"
	)
	private StringDt myDescription;
	
	@Child(name="onset", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Date(/time) when manifestations showed",
		formalDefinition="Record of the date and/or time of the onset of the Reaction"
	)
	private DateTimeDt myOnset;
	
	@Child(name="duration", type=DurationDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="How long Manifestations persisted",
		formalDefinition="The amount of time that the Adverse Reaction persisted"
	)
	private DurationDt myDuration;
	
	@Child(name="severity", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="mild | moderate | severe (of event as a whole)",
		formalDefinition="Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations"
	)
	private BoundCodeDt<AdverseReactionRiskSeverityEnum> mySeverity;
	
	@Child(name="exposureRoute", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="How the subject was exposed to the substance",
		formalDefinition="Identification of the route by which the subject was exposed to the substance."
	)
	private CodeableConceptDt myExposureRoute;
	
	@Child(name="comment", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Text about event not captured in other fields",
		formalDefinition="Additional text about the Adverse Reaction event not captured in other fields"
	)
	private StringDt myComment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubstance,  myCertainty,  myManifestation,  myDescription,  myOnset,  myDuration,  mySeverity,  myExposureRoute,  myComment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubstance, myCertainty, myManifestation, myDescription, myOnset, myDuration, mySeverity, myExposureRoute, myComment);
	}

	/**
	 * Gets the value(s) for <b>substance</b> (Specific substance considered to be responsible for event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AdverseReactionRisk.substance and ignore the AdverseReactionRisk.event.substance
     * </p> 
	 */
	public BoundCodeableConceptDt<SubstanceTypeEnum> getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new BoundCodeableConceptDt<SubstanceTypeEnum>(SubstanceTypeEnum.VALUESET_BINDER);
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (Specific substance considered to be responsible for event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AdverseReactionRisk.substance and ignore the AdverseReactionRisk.event.substance
     * </p> 
	 */
	public Event setSubstance(BoundCodeableConceptDt<SubstanceTypeEnum> theValue) {
		mySubstance = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>substance</b> (Specific substance considered to be responsible for event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AdverseReactionRisk.substance and ignore the AdverseReactionRisk.event.substance
     * </p> 
	 */
	public Event setSubstance(SubstanceTypeEnum theValue) {
		getSubstance().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>certainty</b> (unlikely | likely | confirmed - clinical certainty about the specific substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Statement about the degree of clinical certainty that the Specific Substance was the cause of the Manifestation in this reaction event
     * </p> 
	 */
	public BoundCodeDt<AdverseReactionRiskCertaintyEnum> getCertaintyElement() {  
		if (myCertainty == null) {
			myCertainty = new BoundCodeDt<AdverseReactionRiskCertaintyEnum>(AdverseReactionRiskCertaintyEnum.VALUESET_BINDER);
		}
		return myCertainty;
	}

	
	/**
	 * Gets the value(s) for <b>certainty</b> (unlikely | likely | confirmed - clinical certainty about the specific substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Statement about the degree of clinical certainty that the Specific Substance was the cause of the Manifestation in this reaction event
     * </p> 
	 */
	public String getCertainty() {  
		return getCertaintyElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>certainty</b> (unlikely | likely | confirmed - clinical certainty about the specific substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Statement about the degree of clinical certainty that the Specific Substance was the cause of the Manifestation in this reaction event
     * </p> 
	 */
	public Event setCertainty(BoundCodeDt<AdverseReactionRiskCertaintyEnum> theValue) {
		myCertainty = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>certainty</b> (unlikely | likely | confirmed - clinical certainty about the specific substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Statement about the degree of clinical certainty that the Specific Substance was the cause of the Manifestation in this reaction event
     * </p> 
	 */
	public Event setCertainty(AdverseReactionRiskCertaintyEnum theValue) {
		getCertaintyElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>manifestation</b> (Clinical symptoms/signs associated with the Event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical symptoms and/or signs that are observed or associated with the Adverse Reaction Event
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getManifestation() {  
		if (myManifestation == null) {
			myManifestation = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myManifestation;
	}

	/**
	 * Sets the value(s) for <b>manifestation</b> (Clinical symptoms/signs associated with the Event)
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical symptoms and/or signs that are observed or associated with the Adverse Reaction Event
     * </p> 
	 */
	public Event setManifestation(java.util.List<CodeableConceptDt> theValue) {
		myManifestation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>manifestation</b> (Clinical symptoms/signs associated with the Event)
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical symptoms and/or signs that are observed or associated with the Adverse Reaction Event
     * </p> 
	 */
	public CodeableConceptDt addManifestation() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getManifestation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>manifestation</b> (Clinical symptoms/signs associated with the Event),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical symptoms and/or signs that are observed or associated with the Adverse Reaction Event
     * </p> 
	 */
	public CodeableConceptDt getManifestationFirstRep() {
		if (getManifestation().isEmpty()) {
			return addManifestation();
		}
		return getManifestation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> (Description of the event as a whole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text description about the Reaction as a whole, including details of the manifestation if required
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> (Description of the event as a whole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text description about the Reaction as a whole, including details of the manifestation if required
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> (Description of the event as a whole)
	 *
     * <p>
     * <b>Definition:</b>
     * Text description about the Reaction as a whole, including details of the manifestation if required
     * </p> 
	 */
	public Event setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> (Description of the event as a whole)
	 *
     * <p>
     * <b>Definition:</b>
     * Text description about the Reaction as a whole, including details of the manifestation if required
     * </p> 
	 */
	public Event setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>onset</b> (Date(/time) when manifestations showed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Record of the date and/or time of the onset of the Reaction
     * </p> 
	 */
	public DateTimeDt getOnsetElement() {  
		if (myOnset == null) {
			myOnset = new DateTimeDt();
		}
		return myOnset;
	}

	
	/**
	 * Gets the value(s) for <b>onset</b> (Date(/time) when manifestations showed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Record of the date and/or time of the onset of the Reaction
     * </p> 
	 */
	public Date getOnset() {  
		return getOnsetElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>onset</b> (Date(/time) when manifestations showed)
	 *
     * <p>
     * <b>Definition:</b>
     * Record of the date and/or time of the onset of the Reaction
     * </p> 
	 */
	public Event setOnset(DateTimeDt theValue) {
		myOnset = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>onset</b> (Date(/time) when manifestations showed)
	 *
     * <p>
     * <b>Definition:</b>
     * Record of the date and/or time of the onset of the Reaction
     * </p> 
	 */
	public Event setOnset( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myOnset = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>onset</b> (Date(/time) when manifestations showed)
	 *
     * <p>
     * <b>Definition:</b>
     * Record of the date and/or time of the onset of the Reaction
     * </p> 
	 */
	public Event setOnsetWithSecondsPrecision( Date theDate) {
		myOnset = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>duration</b> (How long Manifestations persisted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of time that the Adverse Reaction persisted
     * </p> 
	 */
	public DurationDt getDuration() {  
		if (myDuration == null) {
			myDuration = new DurationDt();
		}
		return myDuration;
	}

	/**
	 * Sets the value(s) for <b>duration</b> (How long Manifestations persisted)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of time that the Adverse Reaction persisted
     * </p> 
	 */
	public Event setDuration(DurationDt theValue) {
		myDuration = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>severity</b> (mild | moderate | severe (of event as a whole)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations
     * </p> 
	 */
	public BoundCodeDt<AdverseReactionRiskSeverityEnum> getSeverityElement() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<AdverseReactionRiskSeverityEnum>(AdverseReactionRiskSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	
	/**
	 * Gets the value(s) for <b>severity</b> (mild | moderate | severe (of event as a whole)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations
     * </p> 
	 */
	public String getSeverity() {  
		return getSeverityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>severity</b> (mild | moderate | severe (of event as a whole))
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations
     * </p> 
	 */
	public Event setSeverity(BoundCodeDt<AdverseReactionRiskSeverityEnum> theValue) {
		mySeverity = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>severity</b> (mild | moderate | severe (of event as a whole))
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations
     * </p> 
	 */
	public Event setSeverity(AdverseReactionRiskSeverityEnum theValue) {
		getSeverityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>exposureRoute</b> (How the subject was exposed to the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the route by which the subject was exposed to the substance.
     * </p> 
	 */
	public CodeableConceptDt getExposureRoute() {  
		if (myExposureRoute == null) {
			myExposureRoute = new CodeableConceptDt();
		}
		return myExposureRoute;
	}

	/**
	 * Sets the value(s) for <b>exposureRoute</b> (How the subject was exposed to the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the route by which the subject was exposed to the substance.
     * </p> 
	 */
	public Event setExposureRoute(CodeableConceptDt theValue) {
		myExposureRoute = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>comment</b> (Text about event not captured in other fields).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text about the Adverse Reaction event not captured in other fields
     * </p> 
	 */
	public StringDt getCommentElement() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}

	
	/**
	 * Gets the value(s) for <b>comment</b> (Text about event not captured in other fields).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text about the Adverse Reaction event not captured in other fields
     * </p> 
	 */
	public String getComment() {  
		return getCommentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comment</b> (Text about event not captured in other fields)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text about the Adverse Reaction event not captured in other fields
     * </p> 
	 */
	public Event setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comment</b> (Text about event not captured in other fields)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text about the Adverse Reaction event not captured in other fields
     * </p> 
	 */
	public Event setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "AdverseReactionRisk";
    }

}
