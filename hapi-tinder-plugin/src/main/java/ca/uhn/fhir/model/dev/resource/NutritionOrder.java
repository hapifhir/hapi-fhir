















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
 * HAPI/FHIR <b>NutritionOrder</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/NutritionOrder">http://hl7.org/fhir/profiles/NutritionOrder</a> 
 * </p>
 *
 */
@ResourceDef(name="NutritionOrder", profile="http://hl7.org/fhir/profiles/NutritionOrder", id="nutritionorder")
public class NutritionOrder 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of the person who requires the diet, formula or nutritional supplement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="NutritionOrder.subject", description="The identity of the person who requires the diet, formula or nutritional supplement", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of the person who requires the diet, formula or nutritional supplement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b>The identify of the provider who placed the nutrition order</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.orderer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="provider", path="NutritionOrder.orderer", description="The identify of the provider who placed the nutrition order", type="reference"  )
	public static final String SP_PROVIDER = "provider";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b>The identify of the provider who placed the nutrition order</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.orderer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PROVIDER = new ReferenceClientParam(SP_PROVIDER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return nutrition orders with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="NutritionOrder.identifier", description="Return nutrition orders with this external identity", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return nutrition orders with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return nutrition orders with this encounter identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="NutritionOrder.encounter", description="Return nutrition orders with this encounter identity", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return nutrition orders with this encounter identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>datetime</b>
	 * <p>
	 * Description: <b>Return nutrition orders requested on this date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>NutritionOrder.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="datetime", path="NutritionOrder.dateTime", description="Return nutrition orders requested on this date", type="date"  )
	public static final String SP_DATETIME = "datetime";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>datetime</b>
	 * <p>
	 * Description: <b>Return nutrition orders requested on this date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>NutritionOrder.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATETIME = new DateClientParam(SP_DATETIME);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the nutrition order.</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="NutritionOrder.status", description="Status of the nutrition order.", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the nutrition order.</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>oraldiet</b>
	 * <p>
	 * Description: <b>Type of diet that can be consumed orally (i.e., take via the mouth).</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.oralDiet.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="oraldiet", path="NutritionOrder.item.oralDiet.type", description="Type of diet that can be consumed orally (i.e., take via the mouth).", type="token"  )
	public static final String SP_ORALDIET = "oraldiet";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>oraldiet</b>
	 * <p>
	 * Description: <b>Type of diet that can be consumed orally (i.e., take via the mouth).</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.oralDiet.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ORALDIET = new TokenClientParam(SP_ORALDIET);

	/**
	 * Search parameter constant for <b>supplement</b>
	 * <p>
	 * Description: <b>Type of supplement product requested</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.supplement.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supplement", path="NutritionOrder.item.supplement.type", description="Type of supplement product requested", type="token"  )
	public static final String SP_SUPPLEMENT = "supplement";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>supplement</b>
	 * <p>
	 * Description: <b>Type of supplement product requested</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.supplement.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SUPPLEMENT = new TokenClientParam(SP_SUPPLEMENT);

	/**
	 * Search parameter constant for <b>formula</b>
	 * <p>
	 * Description: <b>Type of enteral or infant formula</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.enteralFormula.baseFormulaType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="formula", path="NutritionOrder.item.enteralFormula.baseFormulaType", description="Type of enteral or infant formula", type="token"  )
	public static final String SP_FORMULA = "formula";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>formula</b>
	 * <p>
	 * Description: <b>Type of enteral or infant formula</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.enteralFormula.baseFormulaType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FORMULA = new TokenClientParam(SP_FORMULA);

	/**
	 * Search parameter constant for <b>additive</b>
	 * <p>
	 * Description: <b>Type of module component to add to the feeding</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.enteralFormula.additiveType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="additive", path="NutritionOrder.item.enteralFormula.additiveType", description="Type of module component to add to the feeding", type="token"  )
	public static final String SP_ADDITIVE = "additive";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>additive</b>
	 * <p>
	 * Description: <b>Type of module component to add to the feeding</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>NutritionOrder.item.enteralFormula.additiveType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ADDITIVE = new TokenClientParam(SP_ADDITIVE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of the person who requires the diet, formula or nutritional supplement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="NutritionOrder.subject", description="The identity of the person who requires the diet, formula or nutritional supplement", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of the person who requires the diet, formula or nutritional supplement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>NutritionOrder.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.dateTime</b>".
	 */
	public static final Include INCLUDE_DATETIME = new Include("NutritionOrder.dateTime");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("NutritionOrder.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("NutritionOrder.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.item.enteralFormula.additiveType</b>".
	 */
	public static final Include INCLUDE_ITEM_ENTERALFORMULA_ADDITIVETYPE = new Include("NutritionOrder.item.enteralFormula.additiveType");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.item.enteralFormula.baseFormulaType</b>".
	 */
	public static final Include INCLUDE_ITEM_ENTERALFORMULA_BASEFORMULATYPE = new Include("NutritionOrder.item.enteralFormula.baseFormulaType");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.item.oralDiet.type</b>".
	 */
	public static final Include INCLUDE_ITEM_ORALDIET_TYPE = new Include("NutritionOrder.item.oralDiet.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.item.supplement.type</b>".
	 */
	public static final Include INCLUDE_ITEM_SUPPLEMENT_TYPE = new Include("NutritionOrder.item.supplement.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.orderer</b>".
	 */
	public static final Include INCLUDE_ORDERER = new Include("NutritionOrder.orderer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("NutritionOrder.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>NutritionOrder.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("NutritionOrder.subject");


	@Child(name="subject", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding."
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="orderer", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings."
	)
	private ResourceReferenceDt myOrderer;
	
	@Child(name="identifier", type=IdentifierDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifiers assigned to this order by the order sender or by the order receiver"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="encounter", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="An encounter that provides additional information about the healthcare context in which this request is made"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="dateTime", type=DateTimeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date and time that this nutrition order was requested."
	)
	private DateTimeDt myDateTime;
	
	@Child(name="allergyIntolerance", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.AllergyIntolerance.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order"
	)
	private java.util.List<ResourceReferenceDt> myAllergyIntolerance;
	
	@Child(name="foodPreferenceModifier", type=CodeableConceptDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="PatientDiet",
		formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings."
	)
	private java.util.List<CodeableConceptDt> myFoodPreferenceModifier;
	
	@Child(name="excludeFoodModifier", type=CodeableConceptDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ExcludeFoodModifier",
		formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings."
	)
	private java.util.List<BoundCodeableConceptDt<ExcludeFoodModifierEnum>> myExcludeFoodModifier;
	
	@Child(name="item", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order."
	)
	private java.util.List<Item> myItem;
	
	@Child(name="status", type=CodeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="NutritionOrderStatus",
		formalDefinition="The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended."
	)
	private BoundCodeDt<NutritionOrderStatusEnum> myStatus;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myOrderer,  myIdentifier,  myEncounter,  myDateTime,  myAllergyIntolerance,  myFoodPreferenceModifier,  myExcludeFoodModifier,  myItem,  myStatus);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myOrderer, myIdentifier, myEncounter, myDateTime, myAllergyIntolerance, myFoodPreferenceModifier, myExcludeFoodModifier, myItem, myStatus);
	}

	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.
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
     * The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.
     * </p> 
	 */
	public NutritionOrder setSubject(ResourceReferenceDt theValue) {
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
     * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
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
     * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
     * </p> 
	 */
	public NutritionOrder setOrderer(ResourceReferenceDt theValue) {
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
     * Identifiers assigned to this order by the order sender or by the order receiver
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
     * Identifiers assigned to this order by the order sender or by the order receiver
     * </p> 
	 */
	public NutritionOrder setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order sender or by the order receiver
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
     * Identifiers assigned to this order by the order sender or by the order receiver
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
	public NutritionOrder setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time that this nutrition order was requested.
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
     * The date and time that this nutrition order was requested.
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
     * The date and time that this nutrition order was requested.
     * </p> 
	 */
	public NutritionOrder setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time that this nutrition order was requested.
     * </p> 
	 */
	public NutritionOrder setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time that this nutrition order was requested.
     * </p> 
	 */
	public NutritionOrder setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>allergyIntolerance</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAllergyIntolerance() {  
		if (myAllergyIntolerance == null) {
			myAllergyIntolerance = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAllergyIntolerance;
	}

	/**
	 * Sets the value(s) for <b>allergyIntolerance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order
     * </p> 
	 */
	public NutritionOrder setAllergyIntolerance(java.util.List<ResourceReferenceDt> theValue) {
		myAllergyIntolerance = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>allergyIntolerance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order
     * </p> 
	 */
	public ResourceReferenceDt addAllergyIntolerance() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAllergyIntolerance().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>foodPreferenceModifier</b> (PatientDiet).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getFoodPreferenceModifier() {  
		if (myFoodPreferenceModifier == null) {
			myFoodPreferenceModifier = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myFoodPreferenceModifier;
	}

	/**
	 * Sets the value(s) for <b>foodPreferenceModifier</b> (PatientDiet)
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public NutritionOrder setFoodPreferenceModifier(java.util.List<CodeableConceptDt> theValue) {
		myFoodPreferenceModifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>foodPreferenceModifier</b> (PatientDiet)
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public CodeableConceptDt addFoodPreferenceModifier() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getFoodPreferenceModifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>foodPreferenceModifier</b> (PatientDiet),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public CodeableConceptDt getFoodPreferenceModifierFirstRep() {
		if (getFoodPreferenceModifier().isEmpty()) {
			return addFoodPreferenceModifier();
		}
		return getFoodPreferenceModifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>excludeFoodModifier</b> (ExcludeFoodModifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ExcludeFoodModifierEnum>> getExcludeFoodModifier() {  
		if (myExcludeFoodModifier == null) {
			myExcludeFoodModifier = new java.util.ArrayList<BoundCodeableConceptDt<ExcludeFoodModifierEnum>>();
		}
		return myExcludeFoodModifier;
	}

	/**
	 * Sets the value(s) for <b>excludeFoodModifier</b> (ExcludeFoodModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public NutritionOrder setExcludeFoodModifier(java.util.List<BoundCodeableConceptDt<ExcludeFoodModifierEnum>> theValue) {
		myExcludeFoodModifier = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>excludeFoodModifier</b> (ExcludeFoodModifier) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public BoundCodeableConceptDt<ExcludeFoodModifierEnum> addExcludeFoodModifier(ExcludeFoodModifierEnum theValue) {
		BoundCodeableConceptDt<ExcludeFoodModifierEnum> retVal = new BoundCodeableConceptDt<ExcludeFoodModifierEnum>(ExcludeFoodModifierEnum.VALUESET_BINDER, theValue);
		getExcludeFoodModifier().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>excludeFoodModifier</b> (ExcludeFoodModifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public BoundCodeableConceptDt<ExcludeFoodModifierEnum> getExcludeFoodModifierFirstRep() {
		if (getExcludeFoodModifier().size() == 0) {
			addExcludeFoodModifier();
		}
		return getExcludeFoodModifier().get(0);
	}

	/**
	 * Add a value for <b>excludeFoodModifier</b> (ExcludeFoodModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public BoundCodeableConceptDt<ExcludeFoodModifierEnum> addExcludeFoodModifier() {
		BoundCodeableConceptDt<ExcludeFoodModifierEnum> retVal = new BoundCodeableConceptDt<ExcludeFoodModifierEnum>(ExcludeFoodModifierEnum.VALUESET_BINDER);
		getExcludeFoodModifier().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>excludeFoodModifier</b> (ExcludeFoodModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p> 
	 */
	public NutritionOrder setExcludeFoodModifier(ExcludeFoodModifierEnum theValue) {
		getExcludeFoodModifier().clear();
		addExcludeFoodModifier(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>item</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.
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
     * Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.
     * </p> 
	 */
	public NutritionOrder setItem(java.util.List<Item> theValue) {
		myItem = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.
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
     * Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.
     * </p> 
	 */
	public Item getItemFirstRep() {
		if (getItem().isEmpty()) {
			return addItem();
		}
		return getItem().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (NutritionOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     * </p> 
	 */
	public BoundCodeDt<NutritionOrderStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<NutritionOrderStatusEnum>(NutritionOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (NutritionOrderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (NutritionOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     * </p> 
	 */
	public NutritionOrder setStatus(BoundCodeDt<NutritionOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (NutritionOrderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     * </p> 
	 */
	public NutritionOrder setStatus(NutritionOrderStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Block class for child element: <b>NutritionOrder.item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.
     * </p> 
	 */
	@Block()	
	public static class Item 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="scheduled", order=0, min=0, max=1, type={
		TimingDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The frequency at which the diet, oral supplement or enteral formula should be given"
	)
	private IDatatype myScheduled;
	
	@Child(name="isInEffect", type=BooleanDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates whether the nutrition item is  currently in effect for the patient."
	)
	private BooleanDt myIsInEffect;
	
	@Child(name="oralDiet", order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Class that defines the components of an oral diet order for the patient."
	)
	private ItemOralDiet myOralDiet;
	
	@Child(name="supplement", order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Class that defines the components of a supplement order for the patient."
	)
	private ItemSupplement mySupplement;
	
	@Child(name="enteralFormula", order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Class that defines the components of an enteral formula order for the patient."
	)
	private ItemEnteralFormula myEnteralFormula;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myScheduled,  myIsInEffect,  myOralDiet,  mySupplement,  myEnteralFormula);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myScheduled, myIsInEffect, myOralDiet, mySupplement, myEnteralFormula);
	}

	/**
	 * Gets the value(s) for <b>scheduled[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The frequency at which the diet, oral supplement or enteral formula should be given
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
     * The frequency at which the diet, oral supplement or enteral formula should be given
     * </p> 
	 */
	public Item setScheduled(IDatatype theValue) {
		myScheduled = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>isInEffect</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the nutrition item is  currently in effect for the patient.
     * </p> 
	 */
	public BooleanDt getIsInEffectElement() {  
		if (myIsInEffect == null) {
			myIsInEffect = new BooleanDt();
		}
		return myIsInEffect;
	}

	
	/**
	 * Gets the value(s) for <b>isInEffect</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the nutrition item is  currently in effect for the patient.
     * </p> 
	 */
	public Boolean getIsInEffect() {  
		return getIsInEffectElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>isInEffect</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the nutrition item is  currently in effect for the patient.
     * </p> 
	 */
	public Item setIsInEffect(BooleanDt theValue) {
		myIsInEffect = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>isInEffect</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the nutrition item is  currently in effect for the patient.
     * </p> 
	 */
	public Item setIsInEffect( boolean theBoolean) {
		myIsInEffect = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>oralDiet</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of an oral diet order for the patient.
     * </p> 
	 */
	public ItemOralDiet getOralDiet() {  
		if (myOralDiet == null) {
			myOralDiet = new ItemOralDiet();
		}
		return myOralDiet;
	}

	/**
	 * Sets the value(s) for <b>oralDiet</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of an oral diet order for the patient.
     * </p> 
	 */
	public Item setOralDiet(ItemOralDiet theValue) {
		myOralDiet = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>supplement</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of a supplement order for the patient.
     * </p> 
	 */
	public ItemSupplement getSupplement() {  
		if (mySupplement == null) {
			mySupplement = new ItemSupplement();
		}
		return mySupplement;
	}

	/**
	 * Sets the value(s) for <b>supplement</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of a supplement order for the patient.
     * </p> 
	 */
	public Item setSupplement(ItemSupplement theValue) {
		mySupplement = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>enteralFormula</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of an enteral formula order for the patient.
     * </p> 
	 */
	public ItemEnteralFormula getEnteralFormula() {  
		if (myEnteralFormula == null) {
			myEnteralFormula = new ItemEnteralFormula();
		}
		return myEnteralFormula;
	}

	/**
	 * Sets the value(s) for <b>enteralFormula</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of an enteral formula order for the patient.
     * </p> 
	 */
	public Item setEnteralFormula(ItemEnteralFormula theValue) {
		myEnteralFormula = theValue;
		return this;
	}
	
	

  

	}

	/**
	 * Block class for child element: <b>NutritionOrder.item.oralDiet</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of an oral diet order for the patient.
     * </p> 
	 */
	@Block()	
	public static class ItemOralDiet 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="OralDiet",
		formalDefinition="Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth)."
	)
	private java.util.List<CodeableConceptDt> myType;
	
	@Child(name="nutrients", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Class that defines the details of any nutrient modifications required for the oral diet"
	)
	private java.util.List<ItemOralDietNutrients> myNutrients;
	
	@Child(name="texture", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Class that describes any texture modifications required for the patient to safely consume various types of solid foods."
	)
	private java.util.List<ItemOralDietTexture> myTexture;
	
	@Child(name="fluidConsistencyType", type=CodeableConceptDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="FluidConsistencyType",
		formalDefinition="Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient."
	)
	private java.util.List<BoundCodeableConceptDt<FluidConsistencyTypeEnum>> myFluidConsistencyType;
	
	@Child(name="instruction", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional instructions or information pertaining to the oral diet."
	)
	private StringDt myInstruction;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myNutrients,  myTexture,  myFluidConsistencyType,  myInstruction);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myNutrients, myTexture, myFluidConsistencyType, myInstruction);
	}

	/**
	 * Gets the value(s) for <b>type</b> (OralDiet).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (OralDiet)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).
     * </p> 
	 */
	public ItemOralDiet setType(java.util.List<CodeableConceptDt> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>type</b> (OralDiet)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).
     * </p> 
	 */
	public CodeableConceptDt addType() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getType().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>type</b> (OralDiet),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).
     * </p> 
	 */
	public CodeableConceptDt getTypeFirstRep() {
		if (getType().isEmpty()) {
			return addType();
		}
		return getType().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>nutrients</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the details of any nutrient modifications required for the oral diet
     * </p> 
	 */
	public java.util.List<ItemOralDietNutrients> getNutrients() {  
		if (myNutrients == null) {
			myNutrients = new java.util.ArrayList<ItemOralDietNutrients>();
		}
		return myNutrients;
	}

	/**
	 * Sets the value(s) for <b>nutrients</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the details of any nutrient modifications required for the oral diet
     * </p> 
	 */
	public ItemOralDiet setNutrients(java.util.List<ItemOralDietNutrients> theValue) {
		myNutrients = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>nutrients</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the details of any nutrient modifications required for the oral diet
     * </p> 
	 */
	public ItemOralDietNutrients addNutrients() {
		ItemOralDietNutrients newType = new ItemOralDietNutrients();
		getNutrients().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>nutrients</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the details of any nutrient modifications required for the oral diet
     * </p> 
	 */
	public ItemOralDietNutrients getNutrientsFirstRep() {
		if (getNutrients().isEmpty()) {
			return addNutrients();
		}
		return getNutrients().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>texture</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
     * </p> 
	 */
	public java.util.List<ItemOralDietTexture> getTexture() {  
		if (myTexture == null) {
			myTexture = new java.util.ArrayList<ItemOralDietTexture>();
		}
		return myTexture;
	}

	/**
	 * Sets the value(s) for <b>texture</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
     * </p> 
	 */
	public ItemOralDiet setTexture(java.util.List<ItemOralDietTexture> theValue) {
		myTexture = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>texture</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
     * </p> 
	 */
	public ItemOralDietTexture addTexture() {
		ItemOralDietTexture newType = new ItemOralDietTexture();
		getTexture().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>texture</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
     * </p> 
	 */
	public ItemOralDietTexture getTextureFirstRep() {
		if (getTexture().isEmpty()) {
			return addTexture();
		}
		return getTexture().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>fluidConsistencyType</b> (FluidConsistencyType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<FluidConsistencyTypeEnum>> getFluidConsistencyType() {  
		if (myFluidConsistencyType == null) {
			myFluidConsistencyType = new java.util.ArrayList<BoundCodeableConceptDt<FluidConsistencyTypeEnum>>();
		}
		return myFluidConsistencyType;
	}

	/**
	 * Sets the value(s) for <b>fluidConsistencyType</b> (FluidConsistencyType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
     * </p> 
	 */
	public ItemOralDiet setFluidConsistencyType(java.util.List<BoundCodeableConceptDt<FluidConsistencyTypeEnum>> theValue) {
		myFluidConsistencyType = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>fluidConsistencyType</b> (FluidConsistencyType) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
     * </p> 
	 */
	public BoundCodeableConceptDt<FluidConsistencyTypeEnum> addFluidConsistencyType(FluidConsistencyTypeEnum theValue) {
		BoundCodeableConceptDt<FluidConsistencyTypeEnum> retVal = new BoundCodeableConceptDt<FluidConsistencyTypeEnum>(FluidConsistencyTypeEnum.VALUESET_BINDER, theValue);
		getFluidConsistencyType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>fluidConsistencyType</b> (FluidConsistencyType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
     * </p> 
	 */
	public BoundCodeableConceptDt<FluidConsistencyTypeEnum> getFluidConsistencyTypeFirstRep() {
		if (getFluidConsistencyType().size() == 0) {
			addFluidConsistencyType();
		}
		return getFluidConsistencyType().get(0);
	}

	/**
	 * Add a value for <b>fluidConsistencyType</b> (FluidConsistencyType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
     * </p> 
	 */
	public BoundCodeableConceptDt<FluidConsistencyTypeEnum> addFluidConsistencyType() {
		BoundCodeableConceptDt<FluidConsistencyTypeEnum> retVal = new BoundCodeableConceptDt<FluidConsistencyTypeEnum>(FluidConsistencyTypeEnum.VALUESET_BINDER);
		getFluidConsistencyType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>fluidConsistencyType</b> (FluidConsistencyType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
     * </p> 
	 */
	public ItemOralDiet setFluidConsistencyType(FluidConsistencyTypeEnum theValue) {
		getFluidConsistencyType().clear();
		addFluidConsistencyType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>instruction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions or information pertaining to the oral diet.
     * </p> 
	 */
	public StringDt getInstructionElement() {  
		if (myInstruction == null) {
			myInstruction = new StringDt();
		}
		return myInstruction;
	}

	
	/**
	 * Gets the value(s) for <b>instruction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions or information pertaining to the oral diet.
     * </p> 
	 */
	public String getInstruction() {  
		return getInstructionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>instruction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions or information pertaining to the oral diet.
     * </p> 
	 */
	public ItemOralDiet setInstruction(StringDt theValue) {
		myInstruction = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>instruction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions or information pertaining to the oral diet.
     * </p> 
	 */
	public ItemOralDiet setInstruction( String theString) {
		myInstruction = new StringDt(theString); 
		return this; 
	}

 

	}

	/**
	 * Block class for child element: <b>NutritionOrder.item.oralDiet.nutrients</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the details of any nutrient modifications required for the oral diet
     * </p> 
	 */
	@Block()	
	public static class ItemOralDietNutrients 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="modifier", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="NutrientModifier",
		formalDefinition="Identifies the type of nutrient that is being modified such as carbohydrate or sodium."
	)
	private BoundCodeableConceptDt<NutrientModifierEnum> myModifier;
	
	@Child(name="amount", order=1, min=0, max=1, type={
		QuantityDt.class, 		RangeDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The quantity or range of the specified nutrient to supply."
	)
	private IDatatype myAmount;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myModifier,  myAmount);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myModifier, myAmount);
	}

	/**
	 * Gets the value(s) for <b>modifier</b> (NutrientModifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of nutrient that is being modified such as carbohydrate or sodium.
     * </p> 
	 */
	public BoundCodeableConceptDt<NutrientModifierEnum> getModifier() {  
		if (myModifier == null) {
			myModifier = new BoundCodeableConceptDt<NutrientModifierEnum>(NutrientModifierEnum.VALUESET_BINDER);
		}
		return myModifier;
	}

	/**
	 * Sets the value(s) for <b>modifier</b> (NutrientModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of nutrient that is being modified such as carbohydrate or sodium.
     * </p> 
	 */
	public ItemOralDietNutrients setModifier(BoundCodeableConceptDt<NutrientModifierEnum> theValue) {
		myModifier = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>modifier</b> (NutrientModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of nutrient that is being modified such as carbohydrate or sodium.
     * </p> 
	 */
	public ItemOralDietNutrients setModifier(NutrientModifierEnum theValue) {
		getModifier().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>amount[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity or range of the specified nutrient to supply.
     * </p> 
	 */
	public IDatatype getAmount() {  
		return myAmount;
	}

	/**
	 * Sets the value(s) for <b>amount[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity or range of the specified nutrient to supply.
     * </p> 
	 */
	public ItemOralDietNutrients setAmount(IDatatype theValue) {
		myAmount = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>NutritionOrder.item.oralDiet.texture</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
     * </p> 
	 */
	@Block()	
	public static class ItemOralDietTexture 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="modifier", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="TextureModifier",
		formalDefinition="Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed."
	)
	private BoundCodeableConceptDt<TextureModifierEnum> myModifier;
	
	@Child(name="foodType", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="FoodType",
		formalDefinition="Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet"
	)
	private BoundCodeableConceptDt<FoodTypeEnum> myFoodType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myModifier,  myFoodType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myModifier, myFoodType);
	}

	/**
	 * Gets the value(s) for <b>modifier</b> (TextureModifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
     * </p> 
	 */
	public BoundCodeableConceptDt<TextureModifierEnum> getModifier() {  
		if (myModifier == null) {
			myModifier = new BoundCodeableConceptDt<TextureModifierEnum>(TextureModifierEnum.VALUESET_BINDER);
		}
		return myModifier;
	}

	/**
	 * Sets the value(s) for <b>modifier</b> (TextureModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
     * </p> 
	 */
	public ItemOralDietTexture setModifier(BoundCodeableConceptDt<TextureModifierEnum> theValue) {
		myModifier = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>modifier</b> (TextureModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
     * </p> 
	 */
	public ItemOralDietTexture setModifier(TextureModifierEnum theValue) {
		getModifier().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>foodType</b> (FoodType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet
     * </p> 
	 */
	public BoundCodeableConceptDt<FoodTypeEnum> getFoodType() {  
		if (myFoodType == null) {
			myFoodType = new BoundCodeableConceptDt<FoodTypeEnum>(FoodTypeEnum.VALUESET_BINDER);
		}
		return myFoodType;
	}

	/**
	 * Sets the value(s) for <b>foodType</b> (FoodType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet
     * </p> 
	 */
	public ItemOralDietTexture setFoodType(BoundCodeableConceptDt<FoodTypeEnum> theValue) {
		myFoodType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>foodType</b> (FoodType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet
     * </p> 
	 */
	public ItemOralDietTexture setFoodType(FoodTypeEnum theValue) {
		getFoodType().setValueAsEnum(theValue);
		return this;
	}

  

	}



	/**
	 * Block class for child element: <b>NutritionOrder.item.supplement</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of a supplement order for the patient.
     * </p> 
	 */
	@Block()	
	public static class ItemSupplement 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="SupplementType",
		formalDefinition="Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement"
	)
	private BoundCodeableConceptDt<SupplementTypeEnum> myType;
	
	@Child(name="quantity", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount of the nutritional supplement product to provide to the patient."
	)
	private QuantityDt myQuantity;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The product or brand name of the nutritional supplement product to be provided to the patient."
	)
	private StringDt myName;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myQuantity,  myName);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myQuantity, myName);
	}

	/**
	 * Gets the value(s) for <b>type</b> (SupplementType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement
     * </p> 
	 */
	public BoundCodeableConceptDt<SupplementTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<SupplementTypeEnum>(SupplementTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (SupplementType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement
     * </p> 
	 */
	public ItemSupplement setType(BoundCodeableConceptDt<SupplementTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (SupplementType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement
     * </p> 
	 */
	public ItemSupplement setType(SupplementTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the nutritional supplement product to provide to the patient.
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
     * The amount of the nutritional supplement product to provide to the patient.
     * </p> 
	 */
	public ItemSupplement setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the nutritional supplement product to be provided to the patient.
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the nutritional supplement product to be provided to the patient.
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the nutritional supplement product to be provided to the patient.
     * </p> 
	 */
	public ItemSupplement setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the nutritional supplement product to be provided to the patient.
     * </p> 
	 */
	public ItemSupplement setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>NutritionOrder.item.enteralFormula</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class that defines the components of an enteral formula order for the patient.
     * </p> 
	 */
	@Block()	
	public static class ItemEnteralFormula 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="administrationInstructions", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Free text formula administration or feeding instructions for cases where the instructions are too complex to code."
	)
	private StringDt myAdministrationInstructions;
	
	@Child(name="baseFormulaType", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="EnteralFormulaType",
		formalDefinition="Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula."
	)
	private BoundCodeableConceptDt<EnteralFormulaTypeEnum> myBaseFormulaType;
	
	@Child(name="baseFormulaName", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The product or brand name of the enteral or infant formula product to be provided to the patient."
	)
	private StringDt myBaseFormulaName;
	
	@Child(name="additiveType", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="EnteralFormulaAdditiveType",
		formalDefinition="Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula."
	)
	private BoundCodeableConceptDt<EnteralFormulaAdditiveTypeEnum> myAdditiveType;
	
	@Child(name="additiveName", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The product or brand name of the type of modular component to be added to the formula."
	)
	private StringDt myAdditiveName;
	
	@Child(name="caloricDensity", type=QuantityDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL."
	)
	private QuantityDt myCaloricDensity;
	
	@Child(name="routeofAdministration", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="EnteralRouteOfAdministration",
		formalDefinition="A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube."
	)
	private CodeableConceptDt myRouteofAdministration;
	
	@Child(name="quantity", type=QuantityDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The volume of formula to provide to the patient per the specified administration schedule."
	)
	private QuantityDt myQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule."
	)
	private RatioDt myRate;
	
	@Child(name="rateAdjustment", type=QuantityDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours."
	)
	private QuantityDt myRateAdjustment;
	
	@Child(name="maxVolumeToDeliver", type=QuantityDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours."
	)
	private QuantityDt myMaxVolumeToDeliver;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myAdministrationInstructions,  myBaseFormulaType,  myBaseFormulaName,  myAdditiveType,  myAdditiveName,  myCaloricDensity,  myRouteofAdministration,  myQuantity,  myRate,  myRateAdjustment,  myMaxVolumeToDeliver);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myAdministrationInstructions, myBaseFormulaType, myBaseFormulaName, myAdditiveType, myAdditiveName, myCaloricDensity, myRouteofAdministration, myQuantity, myRate, myRateAdjustment, myMaxVolumeToDeliver);
	}

	/**
	 * Gets the value(s) for <b>administrationInstructions</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public StringDt getAdministrationInstructionsElement() {  
		if (myAdministrationInstructions == null) {
			myAdministrationInstructions = new StringDt();
		}
		return myAdministrationInstructions;
	}

	
	/**
	 * Gets the value(s) for <b>administrationInstructions</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public String getAdministrationInstructions() {  
		return getAdministrationInstructionsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>administrationInstructions</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public ItemEnteralFormula setAdministrationInstructions(StringDt theValue) {
		myAdministrationInstructions = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>administrationInstructions</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public ItemEnteralFormula setAdministrationInstructions( String theString) {
		myAdministrationInstructions = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>baseFormulaType</b> (EnteralFormulaType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.
     * </p> 
	 */
	public BoundCodeableConceptDt<EnteralFormulaTypeEnum> getBaseFormulaType() {  
		if (myBaseFormulaType == null) {
			myBaseFormulaType = new BoundCodeableConceptDt<EnteralFormulaTypeEnum>(EnteralFormulaTypeEnum.VALUESET_BINDER);
		}
		return myBaseFormulaType;
	}

	/**
	 * Sets the value(s) for <b>baseFormulaType</b> (EnteralFormulaType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.
     * </p> 
	 */
	public ItemEnteralFormula setBaseFormulaType(BoundCodeableConceptDt<EnteralFormulaTypeEnum> theValue) {
		myBaseFormulaType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>baseFormulaType</b> (EnteralFormulaType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.
     * </p> 
	 */
	public ItemEnteralFormula setBaseFormulaType(EnteralFormulaTypeEnum theValue) {
		getBaseFormulaType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>baseFormulaName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the enteral or infant formula product to be provided to the patient.
     * </p> 
	 */
	public StringDt getBaseFormulaNameElement() {  
		if (myBaseFormulaName == null) {
			myBaseFormulaName = new StringDt();
		}
		return myBaseFormulaName;
	}

	
	/**
	 * Gets the value(s) for <b>baseFormulaName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the enteral or infant formula product to be provided to the patient.
     * </p> 
	 */
	public String getBaseFormulaName() {  
		return getBaseFormulaNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>baseFormulaName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the enteral or infant formula product to be provided to the patient.
     * </p> 
	 */
	public ItemEnteralFormula setBaseFormulaName(StringDt theValue) {
		myBaseFormulaName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>baseFormulaName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the enteral or infant formula product to be provided to the patient.
     * </p> 
	 */
	public ItemEnteralFormula setBaseFormulaName( String theString) {
		myBaseFormulaName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>additiveType</b> (EnteralFormulaAdditiveType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.
     * </p> 
	 */
	public BoundCodeableConceptDt<EnteralFormulaAdditiveTypeEnum> getAdditiveType() {  
		if (myAdditiveType == null) {
			myAdditiveType = new BoundCodeableConceptDt<EnteralFormulaAdditiveTypeEnum>(EnteralFormulaAdditiveTypeEnum.VALUESET_BINDER);
		}
		return myAdditiveType;
	}

	/**
	 * Sets the value(s) for <b>additiveType</b> (EnteralFormulaAdditiveType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.
     * </p> 
	 */
	public ItemEnteralFormula setAdditiveType(BoundCodeableConceptDt<EnteralFormulaAdditiveTypeEnum> theValue) {
		myAdditiveType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>additiveType</b> (EnteralFormulaAdditiveType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.
     * </p> 
	 */
	public ItemEnteralFormula setAdditiveType(EnteralFormulaAdditiveTypeEnum theValue) {
		getAdditiveType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>additiveName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the type of modular component to be added to the formula.
     * </p> 
	 */
	public StringDt getAdditiveNameElement() {  
		if (myAdditiveName == null) {
			myAdditiveName = new StringDt();
		}
		return myAdditiveName;
	}

	
	/**
	 * Gets the value(s) for <b>additiveName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the type of modular component to be added to the formula.
     * </p> 
	 */
	public String getAdditiveName() {  
		return getAdditiveNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>additiveName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the type of modular component to be added to the formula.
     * </p> 
	 */
	public ItemEnteralFormula setAdditiveName(StringDt theValue) {
		myAdditiveName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>additiveName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The product or brand name of the type of modular component to be added to the formula.
     * </p> 
	 */
	public ItemEnteralFormula setAdditiveName( String theString) {
		myAdditiveName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>caloricDensity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL.
     * </p> 
	 */
	public QuantityDt getCaloricDensity() {  
		if (myCaloricDensity == null) {
			myCaloricDensity = new QuantityDt();
		}
		return myCaloricDensity;
	}

	/**
	 * Sets the value(s) for <b>caloricDensity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL.
     * </p> 
	 */
	public ItemEnteralFormula setCaloricDensity(QuantityDt theValue) {
		myCaloricDensity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>routeofAdministration</b> (EnteralRouteOfAdministration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube.
     * </p> 
	 */
	public CodeableConceptDt getRouteofAdministration() {  
		if (myRouteofAdministration == null) {
			myRouteofAdministration = new CodeableConceptDt();
		}
		return myRouteofAdministration;
	}

	/**
	 * Sets the value(s) for <b>routeofAdministration</b> (EnteralRouteOfAdministration)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube.
     * </p> 
	 */
	public ItemEnteralFormula setRouteofAdministration(CodeableConceptDt theValue) {
		myRouteofAdministration = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The volume of formula to provide to the patient per the specified administration schedule.
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
     * The volume of formula to provide to the patient per the specified administration schedule.
     * </p> 
	 */
	public ItemEnteralFormula setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>rate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule.
     * </p> 
	 */
	public RatioDt getRate() {  
		if (myRate == null) {
			myRate = new RatioDt();
		}
		return myRate;
	}

	/**
	 * Sets the value(s) for <b>rate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule.
     * </p> 
	 */
	public ItemEnteralFormula setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>rateAdjustment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours.
     * </p> 
	 */
	public QuantityDt getRateAdjustment() {  
		if (myRateAdjustment == null) {
			myRateAdjustment = new QuantityDt();
		}
		return myRateAdjustment;
	}

	/**
	 * Sets the value(s) for <b>rateAdjustment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours.
     * </p> 
	 */
	public ItemEnteralFormula setRateAdjustment(QuantityDt theValue) {
		myRateAdjustment = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxVolumeToDeliver</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours.
     * </p> 
	 */
	public QuantityDt getMaxVolumeToDeliver() {  
		if (myMaxVolumeToDeliver == null) {
			myMaxVolumeToDeliver = new QuantityDt();
		}
		return myMaxVolumeToDeliver;
	}

	/**
	 * Sets the value(s) for <b>maxVolumeToDeliver</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours.
     * </p> 
	 */
	public ItemEnteralFormula setMaxVolumeToDeliver(QuantityDt theValue) {
		myMaxVolumeToDeliver = theValue;
		return this;
	}
	
	

  

	}





    @Override
    public String getResourceName() {
        return "NutritionOrder";
    }

}
