















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
 * HAPI/FHIR <b>DiagnosticOrder</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A request for a diagnostic investigation service to be performed
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DiagnosticOrder">http://hl7.org/fhir/profiles/DiagnosticOrder</a> 
 * </p>
 *
 */
@ResourceDef(name="DiagnosticOrder", profile="http://hl7.org/fhir/profiles/DiagnosticOrder", id="diagnosticorder")
public class DiagnosticOrder 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>actor</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.event.actor | DiagnosticOrder.item.event.actor</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="actor", path="DiagnosticOrder.event.actor | DiagnosticOrder.item.event.actor", description="", type="reference"  )
	public static final String SP_ACTOR = "actor";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>actor</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.event.actor | DiagnosticOrder.item.event.actor</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ACTOR = new ReferenceClientParam(SP_ACTOR);

	/**
	 * Search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.bodySite</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="bodysite", path="DiagnosticOrder.item.bodySite", description="", type="token"  )
	public static final String SP_BODYSITE = "bodysite";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.bodySite</b><br/>
	 * </p>
	 */
	public static final TokenClientParam BODYSITE = new TokenClientParam(SP_BODYSITE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="DiagnosticOrder.item.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>event-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.event.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event-date", path="DiagnosticOrder.event.dateTime", description="", type="date"  )
	public static final String SP_EVENT_DATE = "event-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.event.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam EVENT_DATE = new DateClientParam(SP_EVENT_DATE);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="DiagnosticOrder.encounter", description="", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DiagnosticOrder.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>item-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-date", path="DiagnosticOrder.item.event.dateTime", description="", type="date"  )
	public static final String SP_ITEM_DATE = "item-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam ITEM_DATE = new DateClientParam(SP_ITEM_DATE);

	/**
	 * Search parameter constant for <b>item-past-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-past-status", path="DiagnosticOrder.item.event.status", description="", type="token"  )
	public static final String SP_ITEM_PAST_STATUS = "item-past-status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-past-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ITEM_PAST_STATUS = new TokenClientParam(SP_ITEM_PAST_STATUS);

	/**
	 * Search parameter constant for <b>item-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-status", path="DiagnosticOrder.item.status", description="", type="token"  )
	public static final String SP_ITEM_STATUS = "item-status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ITEM_STATUS = new TokenClientParam(SP_ITEM_STATUS);

	/**
	 * Search parameter constant for <b>orderer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.orderer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="orderer", path="DiagnosticOrder.orderer", description="", type="reference"  )
	public static final String SP_ORDERER = "orderer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>orderer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.orderer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORDERER = new ReferenceClientParam(SP_ORDERER);

	/**
	 * Search parameter constant for <b>event-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.event.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event-status", path="DiagnosticOrder.event.status", description="", type="token"  )
	public static final String SP_EVENT_STATUS = "event-status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.event.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVENT_STATUS = new TokenClientParam(SP_EVENT_STATUS);

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.specimen | DiagnosticOrder.item.specimen</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="DiagnosticOrder.specimen | DiagnosticOrder.item.specimen", description="", type="reference"  )
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.specimen | DiagnosticOrder.item.specimen</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SPECIMEN = new ReferenceClientParam(SP_SPECIMEN);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DiagnosticOrder.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DiagnosticOrder.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="DiagnosticOrder.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>item-past-status-item-date</b>
	 * <p>
	 * Description: <b>A combination of item-past-status and item-date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>item-past-status & item-date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-past-status-item-date", path="item-past-status & item-date", description="A combination of item-past-status and item-date", type="composite"  , compositeOf={  "item-past-status",  "item-date" }  )
	public static final String SP_ITEM_PAST_STATUS_ITEM_DATE = "item-past-status-item-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-past-status-item-date</b>
	 * <p>
	 * Description: <b>A combination of item-past-status and item-date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>item-past-status & item-date</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, DateClientParam> ITEM_PAST_STATUS_ITEM_DATE = new CompositeClientParam<TokenClientParam, DateClientParam>(SP_ITEM_PAST_STATUS_ITEM_DATE);

	/**
	 * Search parameter constant for <b>event-status-event-date</b>
	 * <p>
	 * Description: <b>A combination of past-status and date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>event-status & event-date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event-status-event-date", path="event-status & event-date", description="A combination of past-status and date", type="composite"  , compositeOf={  "event-status",  "event-date" }  )
	public static final String SP_EVENT_STATUS_EVENT_DATE = "event-status-event-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event-status-event-date</b>
	 * <p>
	 * Description: <b>A combination of past-status and date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>event-status & event-date</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, DateClientParam> EVENT_STATUS_EVENT_DATE = new CompositeClientParam<TokenClientParam, DateClientParam>(SP_EVENT_STATUS_EVENT_DATE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("DiagnosticOrder.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.event.actor</b>".
	 */
	public static final Include INCLUDE_EVENT_ACTOR = new Include("DiagnosticOrder.event.actor");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.event.dateTime</b>".
	 */
	public static final Include INCLUDE_EVENT_DATETIME = new Include("DiagnosticOrder.event.dateTime");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.event.status</b>".
	 */
	public static final Include INCLUDE_EVENT_STATUS = new Include("DiagnosticOrder.event.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("DiagnosticOrder.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.bodySite</b>".
	 */
	public static final Include INCLUDE_ITEM_BODYSITE = new Include("DiagnosticOrder.item.bodySite");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.code</b>".
	 */
	public static final Include INCLUDE_ITEM_CODE = new Include("DiagnosticOrder.item.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.event.actor</b>".
	 */
	public static final Include INCLUDE_ITEM_EVENT_ACTOR = new Include("DiagnosticOrder.item.event.actor");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.event.dateTime</b>".
	 */
	public static final Include INCLUDE_ITEM_EVENT_DATETIME = new Include("DiagnosticOrder.item.event.dateTime");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.event.status</b>".
	 */
	public static final Include INCLUDE_ITEM_EVENT_STATUS = new Include("DiagnosticOrder.item.event.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.specimen</b>".
	 */
	public static final Include INCLUDE_ITEM_SPECIMEN = new Include("DiagnosticOrder.item.specimen");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.status</b>".
	 */
	public static final Include INCLUDE_ITEM_STATUS = new Include("DiagnosticOrder.item.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.orderer</b>".
	 */
	public static final Include INCLUDE_ORDERER = new Include("DiagnosticOrder.orderer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.specimen</b>".
	 */
	public static final Include INCLUDE_SPECIMEN = new Include("DiagnosticOrder.specimen");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("DiagnosticOrder.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DiagnosticOrder.subject");


	@Child(name="subject", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Location.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="orderer", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The practitioner that holds legal responsibility for ordering the investigation"
	)
	private ResourceReferenceDt myOrderer;
	
	@Child(name="identifier", type=IdentifierDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifiers assigned to this order by the order or by the receiver"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="encounter", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="An encounter that provides additional information about the healthcare context in which this request is made"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="clinicalNotes", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An explanation or justification for why this diagnostic investigation is being requested"
	)
	private StringDt myClinicalNotes;
	
	@Child(name="supportingInformation", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Observation.class, 		ca.uhn.fhir.model.dev.resource.Condition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Additional clinical information about the patient or specimen that may influence test interpretations."
	)
	private java.util.List<ResourceReferenceDt> mySupportingInformation;
	
	@Child(name="specimen", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Specimen.class	})
	@Description(
		shortDefinition="",
		formalDefinition="One or more specimens that the diagnostic investigation is about"
	)
	private java.util.List<ResourceReferenceDt> mySpecimen;
	
	@Child(name="status", type=CodeDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="DiagnosticOrderStatus",
		formalDefinition="The status of the order"
	)
	private BoundCodeDt<DiagnosticOrderStatusEnum> myStatus;
	
	@Child(name="priority", type=CodeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="DiagnosticOrderPriority",
		formalDefinition="The clinical priority associated with this order"
	)
	private BoundCodeDt<DiagnosticOrderPriorityEnum> myPriority;
	
	@Child(name="event", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed"
	)
	private java.util.List<Event> myEvent;
	
	@Child(name="item", order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested"
	)
	private java.util.List<Item> myItem;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myOrderer,  myIdentifier,  myEncounter,  myClinicalNotes,  mySupportingInformation,  mySpecimen,  myStatus,  myPriority,  myEvent,  myItem);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myOrderer, myIdentifier, myEncounter, myClinicalNotes, mySupportingInformation, mySpecimen, myStatus, myPriority, myEvent, myItem);
	}

	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)
     * </p> 
	 */
	public DiagnosticOrder setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>orderer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner that holds legal responsibility for ordering the investigation
     * </p> 
	 */
	public ResourceReferenceDt getOrderer() {  
		if (myOrderer == null) {
			myOrderer = new ResourceReferenceDt();
		}
		return myOrderer;
	}

	/**
	 * Sets the value(s) for <b>orderer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner that holds legal responsibility for ordering the investigation
     * </p> 
	 */
	public DiagnosticOrder setOrderer(ResourceReferenceDt theValue) {
		myOrderer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
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
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
	 */
	public DiagnosticOrder setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
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
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An encounter that provides additional information about the healthcare context in which this request is made
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An encounter that provides additional information about the healthcare context in which this request is made
     * </p> 
	 */
	public DiagnosticOrder setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>clinicalNotes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public StringDt getClinicalNotesElement() {  
		if (myClinicalNotes == null) {
			myClinicalNotes = new StringDt();
		}
		return myClinicalNotes;
	}

	
	/**
	 * Gets the value(s) for <b>clinicalNotes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public String getClinicalNotes() {  
		return getClinicalNotesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>clinicalNotes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public DiagnosticOrder setClinicalNotes(StringDt theValue) {
		myClinicalNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>clinicalNotes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public DiagnosticOrder setClinicalNotes( String theString) {
		myClinicalNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>supportingInformation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional clinical information about the patient or specimen that may influence test interpretations.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSupportingInformation() {  
		if (mySupportingInformation == null) {
			mySupportingInformation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySupportingInformation;
	}

	/**
	 * Sets the value(s) for <b>supportingInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional clinical information about the patient or specimen that may influence test interpretations.
     * </p> 
	 */
	public DiagnosticOrder setSupportingInformation(java.util.List<ResourceReferenceDt> theValue) {
		mySupportingInformation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>supportingInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional clinical information about the patient or specimen that may influence test interpretations.
     * </p> 
	 */
	public ResourceReferenceDt addSupportingInformation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSupportingInformation().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>specimen</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more specimens that the diagnostic investigation is about
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more specimens that the diagnostic investigation is about
     * </p> 
	 */
	public DiagnosticOrder setSpecimen(java.util.List<ResourceReferenceDt> theValue) {
		mySpecimen = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more specimens that the diagnostic investigation is about
     * </p> 
	 */
	public ResourceReferenceDt addSpecimen() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSpecimen().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticOrderStatusEnum>(DiagnosticOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public DiagnosticOrder setStatus(BoundCodeDt<DiagnosticOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public DiagnosticOrder setStatus(DiagnosticOrderStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>priority</b> (DiagnosticOrderPriority).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderPriorityEnum> getPriorityElement() {  
		if (myPriority == null) {
			myPriority = new BoundCodeDt<DiagnosticOrderPriorityEnum>(DiagnosticOrderPriorityEnum.VALUESET_BINDER);
		}
		return myPriority;
	}

	
	/**
	 * Gets the value(s) for <b>priority</b> (DiagnosticOrderPriority).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public String getPriority() {  
		return getPriorityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>priority</b> (DiagnosticOrderPriority)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public DiagnosticOrder setPriority(BoundCodeDt<DiagnosticOrderPriorityEnum> theValue) {
		myPriority = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>priority</b> (DiagnosticOrderPriority)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public DiagnosticOrder setPriority(DiagnosticOrderPriorityEnum theValue) {
		getPriorityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>event</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public java.util.List<Event> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<Event>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public DiagnosticOrder setEvent(java.util.List<Event> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public Event addEvent() {
		Event newType = new Event();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public Event getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>item</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public java.util.List<Item> getItem() {  
		if (myItem == null) {
			myItem = new java.util.ArrayList<Item>();
		}
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public DiagnosticOrder setItem(java.util.List<Item> theValue) {
		myItem = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public Item addItem() {
		Item newType = new Item();
		getItem().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>item</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public Item getItemFirstRep() {
		if (getItem().isEmpty()) {
			return addItem();
		}
		return getItem().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DiagnosticOrder.event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	@Block()	
	public static class Event 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="DiagnosticOrderStatus",
		formalDefinition="The status for the event"
	)
	private BoundCodeDt<DiagnosticOrderStatusEnum> myStatus;
	
	@Child(name="description", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="DiagnosticOrderEventDescription",
		formalDefinition="Additional information about the event that occurred - e.g. if the status remained unchanged"
	)
	private CodeableConceptDt myDescription;
	
	@Child(name="dateTime", type=DateTimeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date/time at which the event occurred"
	)
	private DateTimeDt myDateTime;
	
	@Child(name="actor", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person who was responsible for performing or recording the action"
	)
	private ResourceReferenceDt myActor;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus,  myDescription,  myDateTime,  myActor);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStatus, myDescription, myDateTime, myActor);
	}

	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticOrderStatusEnum>(DiagnosticOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public Event setStatus(BoundCodeDt<DiagnosticOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public Event setStatus(DiagnosticOrderStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (DiagnosticOrderEventDescription).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the event that occurred - e.g. if the status remained unchanged
     * </p> 
	 */
	public CodeableConceptDt getDescription() {  
		if (myDescription == null) {
			myDescription = new CodeableConceptDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (DiagnosticOrderEventDescription)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the event that occurred - e.g. if the status remained unchanged
     * </p> 
	 */
	public Event setDescription(CodeableConceptDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public DateTimeDt getDateTimeElement() {  
		if (myDateTime == null) {
			myDateTime = new DateTimeDt();
		}
		return myDateTime;
	}

	
	/**
	 * Gets the value(s) for <b>dateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Date getDateTime() {  
		return getDateTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Event setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Event setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Event setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>actor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who was responsible for performing or recording the action
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
     * The person who was responsible for performing or recording the action
     * </p> 
	 */
	public Event setActor(ResourceReferenceDt theValue) {
		myActor = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>DiagnosticOrder.item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	@Block()	
	public static class Item 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="DiagnosticRequests",
		formalDefinition="A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="specimen", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Specimen.class	})
	@Description(
		shortDefinition="",
		formalDefinition="If the item is related to a specific specimen"
	)
	private java.util.List<ResourceReferenceDt> mySpecimen;
	
	@Child(name="bodySite", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="BodySite",
		formalDefinition="Anatomical location where the request test should be performed"
	)
	private CodeableConceptDt myBodySite;
	
	@Child(name="status", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="DiagnosticOrderStatus",
		formalDefinition="The status of this individual item within the order"
	)
	private BoundCodeDt<DiagnosticOrderStatusEnum> myStatus;
	
	@Child(name="event", type=Event.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A summary of the events of interest that have occurred as this item of the request is processed"
	)
	private java.util.List<Event> myEvent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  mySpecimen,  myBodySite,  myStatus,  myEvent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, mySpecimen, myBodySite, myStatus, myEvent);
	}

	/**
	 * Gets the value(s) for <b>code</b> (DiagnosticRequests).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (DiagnosticRequests)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested
     * </p> 
	 */
	public Item setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specimen</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the item is related to a specific specimen
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If the item is related to a specific specimen
     * </p> 
	 */
	public Item setSpecimen(java.util.List<ResourceReferenceDt> theValue) {
		mySpecimen = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If the item is related to a specific specimen
     * </p> 
	 */
	public ResourceReferenceDt addSpecimen() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSpecimen().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>bodySite</b> (BodySite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location where the request test should be performed
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new CodeableConceptDt();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (BodySite)
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location where the request test should be performed
     * </p> 
	 */
	public Item setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticOrderStatusEnum>(DiagnosticOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public Item setStatus(BoundCodeDt<DiagnosticOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public Item setStatus(DiagnosticOrderStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>event</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public java.util.List<Event> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<Event>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public Item setEvent(java.util.List<Event> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public Event addEvent() {
		Event newType = new Event();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public Event getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  

	}




    @Override
    public String getResourceName() {
        return "DiagnosticOrder";
    }

}
