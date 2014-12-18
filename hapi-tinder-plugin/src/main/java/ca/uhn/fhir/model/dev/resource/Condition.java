















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
 * HAPI/FHIR <b>Condition</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Condition">http://hl7.org/fhir/profiles/Condition</a> 
 * </p>
 *
 */
@ResourceDef(name="Condition", profile="http://hl7.org/fhir/profiles/Condition", id="condition")
public class Condition 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>Code for the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Condition.code", description="Code for the condition", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>Code for the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Condition.status", description="The status of the condition", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>severity</b>
	 * <p>
	 * Description: <b>The severity of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.severity</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="severity", path="Condition.severity", description="The severity of the condition", type="token"  )
	public static final String SP_SEVERITY = "severity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>severity</b>
	 * <p>
	 * Description: <b>The severity of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.severity</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SEVERITY = new TokenClientParam(SP_SEVERITY);

	/**
	 * Search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b>The category of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.category</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="category", path="Condition.category", description="The category of the condition", type="token"  )
	public static final String SP_CATEGORY = "category";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b>The category of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.category</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CATEGORY = new TokenClientParam(SP_CATEGORY);

	/**
	 * Search parameter constant for <b>onset</b>
	 * <p>
	 * Description: <b>When the Condition started (if started on a date)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.onset[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="onset", path="Condition.onset[x]", description="When the Condition started (if started on a date)", type="date"  )
	public static final String SP_ONSET = "onset";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>onset</b>
	 * <p>
	 * Description: <b>When the Condition started (if started on a date)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.onset[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam ONSET = new DateClientParam(SP_ONSET);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="Condition.encounter", description="", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>asserter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.asserter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="asserter", path="Condition.asserter", description="", type="reference"  )
	public static final String SP_ASSERTER = "asserter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>asserter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.asserter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ASSERTER = new ReferenceClientParam(SP_ASSERTER);

	/**
	 * Search parameter constant for <b>date-asserted</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.dateAsserted</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date-asserted", path="Condition.dateAsserted", description="", type="date"  )
	public static final String SP_DATE_ASSERTED = "date-asserted";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date-asserted</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.dateAsserted</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE_ASSERTED = new DateClientParam(SP_DATE_ASSERTED);

	/**
	 * Search parameter constant for <b>evidence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.evidence.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="evidence", path="Condition.evidence.code", description="", type="token"  )
	public static final String SP_EVIDENCE = "evidence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>evidence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.evidence.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVIDENCE = new TokenClientParam(SP_EVIDENCE);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.location.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Condition.location.code", description="", type="token"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.location.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LOCATION = new TokenClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>stage</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.stage.summary</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="stage", path="Condition.stage.summary", description="", type="token"  )
	public static final String SP_STAGE = "stage";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>stage</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.stage.summary</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STAGE = new TokenClientParam(SP_STAGE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Condition.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Condition.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>dueto-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.dueTo.codeableConcept</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dueto-code", path="Condition.dueTo.codeableConcept", description="", type="token"  )
	public static final String SP_DUETO_CODE = "dueto-code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dueto-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.dueTo.codeableConcept</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DUETO_CODE = new TokenClientParam(SP_DUETO_CODE);

	/**
	 * Search parameter constant for <b>dueto-item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.dueTo.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dueto-item", path="Condition.dueTo.target", description="", type="reference"  )
	public static final String SP_DUETO_ITEM = "dueto-item";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dueto-item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.dueTo.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DUETO_ITEM = new ReferenceClientParam(SP_DUETO_ITEM);

	/**
	 * Search parameter constant for <b>following-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.occurredFollowing.codeableConcept</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="following-code", path="Condition.occurredFollowing.codeableConcept", description="", type="token"  )
	public static final String SP_FOLLOWING_CODE = "following-code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>following-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.occurredFollowing.codeableConcept</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FOLLOWING_CODE = new TokenClientParam(SP_FOLLOWING_CODE);

	/**
	 * Search parameter constant for <b>following-item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.occurredFollowing.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="following-item", path="Condition.occurredFollowing.target", description="", type="reference"  )
	public static final String SP_FOLLOWING_ITEM = "following-item";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>following-item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.occurredFollowing.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam FOLLOWING_ITEM = new ReferenceClientParam(SP_FOLLOWING_ITEM);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.asserter</b>".
	 */
	public static final Include INCLUDE_ASSERTER = new Include("Condition.asserter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.category</b>".
	 */
	public static final Include INCLUDE_CATEGORY = new Include("Condition.category");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("Condition.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.dateAsserted</b>".
	 */
	public static final Include INCLUDE_DATEASSERTED = new Include("Condition.dateAsserted");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.dueTo.codeableConcept</b>".
	 */
	public static final Include INCLUDE_DUETO_CODEABLECONCEPT = new Include("Condition.dueTo.codeableConcept");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.dueTo.target</b>".
	 */
	public static final Include INCLUDE_DUETO_TARGET = new Include("Condition.dueTo.target");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("Condition.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.evidence.code</b>".
	 */
	public static final Include INCLUDE_EVIDENCE_CODE = new Include("Condition.evidence.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.location.code</b>".
	 */
	public static final Include INCLUDE_LOCATION_CODE = new Include("Condition.location.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.occurredFollowing.codeableConcept</b>".
	 */
	public static final Include INCLUDE_OCCURREDFOLLOWING_CODEABLECONCEPT = new Include("Condition.occurredFollowing.codeableConcept");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.occurredFollowing.target</b>".
	 */
	public static final Include INCLUDE_OCCURREDFOLLOWING_TARGET = new Include("Condition.occurredFollowing.target");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.onset[x]</b>".
	 */
	public static final Include INCLUDE_ONSET = new Include("Condition.onset[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.severity</b>".
	 */
	public static final Include INCLUDE_SEVERITY = new Include("Condition.severity");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.stage.summary</b>".
	 */
	public static final Include INCLUDE_STAGE_SUMMARY = new Include("Condition.stage.summary");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Condition.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Condition.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the patient who the condition record is associated with"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="encounter", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Encounter during which the condition was first asserted"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="asserter", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Person who takes responsibility for asserting the existence of the condition as part of the electronic record"
	)
	private ResourceReferenceDt myAsserter;
	
	@Child(name="dateAsserted", type=DateDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Estimated or actual date the condition/problem/diagnosis was first detected/suspected"
	)
	private DateDt myDateAsserted;
	
	@Child(name="code", type=CodeableConceptDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="ConditionKind",
		formalDefinition="Identification of the condition, problem or diagnosis."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="category", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="ConditionCategory",
		formalDefinition="A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis"
	)
	private CodeableConceptDt myCategory;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="ConditionStatus",
		formalDefinition="The clinical status of the condition"
	)
	private BoundCodeDt<ConditionStatusEnum> myStatus;
	
	@Child(name="certainty", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="ConditionCertainty",
		formalDefinition="The degree of confidence that this condition is correct"
	)
	private CodeableConceptDt myCertainty;
	
	@Child(name="severity", type=CodeableConceptDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="ConditionSeverity",
		formalDefinition="A subjective assessment of the severity of the condition as evaluated by the clinician."
	)
	private CodeableConceptDt mySeverity;
	
	@Child(name="onset", order=10, min=0, max=1, type={
		DateTimeDt.class, 		AgeDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Estimated or actual date or date-time  the condition began, in the opinion of the clinician"
	)
	private IDatatype myOnset;
	
	@Child(name="abatement", order=11, min=0, max=1, type={
		DateDt.class, 		AgeDt.class, 		BooleanDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate."
	)
	private IDatatype myAbatement;
	
	@Child(name="stage", order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Clinical stage or grade of a condition. May include formal severity assessments"
	)
	private Stage myStage;
	
	@Child(name="evidence", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed"
	)
	private java.util.List<Evidence> myEvidence;
	
	@Child(name="location", order=14, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The anatomical location where this condition manifests itself"
	)
	private java.util.List<Location> myLocation;
	
	@Child(name="dueTo", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition"
	)
	private java.util.List<DueTo> myDueTo;
	
	@Child(name="occurredFollowing", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition"
	)
	private java.util.List<OccurredFollowing> myOccurredFollowing;
	
	@Child(name="notes", type=StringDt.class, order=17, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis"
	)
	private StringDt myNotes;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myEncounter,  myAsserter,  myDateAsserted,  myCode,  myCategory,  myStatus,  myCertainty,  mySeverity,  myOnset,  myAbatement,  myStage,  myEvidence,  myLocation,  myDueTo,  myOccurredFollowing,  myNotes);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myEncounter, myAsserter, myDateAsserted, myCode, myCategory, myStatus, myCertainty, mySeverity, myOnset, myAbatement, myStage, myEvidence, myLocation, myDueTo, myOccurredFollowing, myNotes);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Condition setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the patient who the condition record is associated with
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
     * Indicates the patient who the condition record is associated with
     * </p> 
	 */
	public Condition setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Encounter during which the condition was first asserted
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
     * Encounter during which the condition was first asserted
     * </p> 
	 */
	public Condition setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>asserter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Person who takes responsibility for asserting the existence of the condition as part of the electronic record
     * </p> 
	 */
	public ResourceReferenceDt getAsserter() {  
		if (myAsserter == null) {
			myAsserter = new ResourceReferenceDt();
		}
		return myAsserter;
	}

	/**
	 * Sets the value(s) for <b>asserter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Person who takes responsibility for asserting the existence of the condition as part of the electronic record
     * </p> 
	 */
	public Condition setAsserter(ResourceReferenceDt theValue) {
		myAsserter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dateAsserted</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public DateDt getDateAssertedElement() {  
		if (myDateAsserted == null) {
			myDateAsserted = new DateDt();
		}
		return myDateAsserted;
	}

	
	/**
	 * Gets the value(s) for <b>dateAsserted</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Date getDateAsserted() {  
		return getDateAssertedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dateAsserted</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Condition setDateAsserted(DateDt theValue) {
		myDateAsserted = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateAsserted</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Condition setDateAsserted( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateAsserted = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateAsserted</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Condition setDateAssertedWithDayPrecision( Date theDate) {
		myDateAsserted = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (ConditionKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the condition, problem or diagnosis.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (ConditionKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the condition, problem or diagnosis.
     * </p> 
	 */
	public Condition setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>category</b> (ConditionCategory).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis
     * </p> 
	 */
	public CodeableConceptDt getCategory() {  
		if (myCategory == null) {
			myCategory = new CodeableConceptDt();
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (ConditionCategory)
	 *
     * <p>
     * <b>Definition:</b>
     * A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis
     * </p> 
	 */
	public Condition setCategory(CodeableConceptDt theValue) {
		myCategory = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (ConditionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public BoundCodeDt<ConditionStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ConditionStatusEnum>(ConditionStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ConditionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ConditionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public Condition setStatus(BoundCodeDt<ConditionStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ConditionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public Condition setStatus(ConditionStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>certainty</b> (ConditionCertainty).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The degree of confidence that this condition is correct
     * </p> 
	 */
	public CodeableConceptDt getCertainty() {  
		if (myCertainty == null) {
			myCertainty = new CodeableConceptDt();
		}
		return myCertainty;
	}

	/**
	 * Sets the value(s) for <b>certainty</b> (ConditionCertainty)
	 *
     * <p>
     * <b>Definition:</b>
     * The degree of confidence that this condition is correct
     * </p> 
	 */
	public Condition setCertainty(CodeableConceptDt theValue) {
		myCertainty = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>severity</b> (ConditionSeverity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     * </p> 
	 */
	public CodeableConceptDt getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new CodeableConceptDt();
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (ConditionSeverity)
	 *
     * <p>
     * <b>Definition:</b>
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     * </p> 
	 */
	public Condition setSeverity(CodeableConceptDt theValue) {
		mySeverity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>onset[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date or date-time  the condition began, in the opinion of the clinician
     * </p> 
	 */
	public IDatatype getOnset() {  
		return myOnset;
	}

	/**
	 * Sets the value(s) for <b>onset[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date or date-time  the condition began, in the opinion of the clinician
     * </p> 
	 */
	public Condition setOnset(IDatatype theValue) {
		myOnset = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>abatement[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.
     * </p> 
	 */
	public IDatatype getAbatement() {  
		return myAbatement;
	}

	/**
	 * Sets the value(s) for <b>abatement[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.
     * </p> 
	 */
	public Condition setAbatement(IDatatype theValue) {
		myAbatement = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>stage</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical stage or grade of a condition. May include formal severity assessments
     * </p> 
	 */
	public Stage getStage() {  
		if (myStage == null) {
			myStage = new Stage();
		}
		return myStage;
	}

	/**
	 * Sets the value(s) for <b>stage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical stage or grade of a condition. May include formal severity assessments
     * </p> 
	 */
	public Condition setStage(Stage theValue) {
		myStage = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>evidence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public java.util.List<Evidence> getEvidence() {  
		if (myEvidence == null) {
			myEvidence = new java.util.ArrayList<Evidence>();
		}
		return myEvidence;
	}

	/**
	 * Sets the value(s) for <b>evidence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public Condition setEvidence(java.util.List<Evidence> theValue) {
		myEvidence = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>evidence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public Evidence addEvidence() {
		Evidence newType = new Evidence();
		getEvidence().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>evidence</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public Evidence getEvidenceFirstRep() {
		if (getEvidence().isEmpty()) {
			return addEvidence();
		}
		return getEvidence().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public java.util.List<Location> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<Location>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public Condition setLocation(java.util.List<Location> theValue) {
		myLocation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public Location addLocation() {
		Location newType = new Location();
		getLocation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>location</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public Location getLocationFirstRep() {
		if (getLocation().isEmpty()) {
			return addLocation();
		}
		return getLocation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>dueTo</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition
     * </p> 
	 */
	public java.util.List<DueTo> getDueTo() {  
		if (myDueTo == null) {
			myDueTo = new java.util.ArrayList<DueTo>();
		}
		return myDueTo;
	}

	/**
	 * Sets the value(s) for <b>dueTo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition
     * </p> 
	 */
	public Condition setDueTo(java.util.List<DueTo> theValue) {
		myDueTo = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dueTo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition
     * </p> 
	 */
	public DueTo addDueTo() {
		DueTo newType = new DueTo();
		getDueTo().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dueTo</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition
     * </p> 
	 */
	public DueTo getDueToFirstRep() {
		if (getDueTo().isEmpty()) {
			return addDueTo();
		}
		return getDueTo().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>occurredFollowing</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition
     * </p> 
	 */
	public java.util.List<OccurredFollowing> getOccurredFollowing() {  
		if (myOccurredFollowing == null) {
			myOccurredFollowing = new java.util.ArrayList<OccurredFollowing>();
		}
		return myOccurredFollowing;
	}

	/**
	 * Sets the value(s) for <b>occurredFollowing</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition
     * </p> 
	 */
	public Condition setOccurredFollowing(java.util.List<OccurredFollowing> theValue) {
		myOccurredFollowing = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>occurredFollowing</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition
     * </p> 
	 */
	public OccurredFollowing addOccurredFollowing() {
		OccurredFollowing newType = new OccurredFollowing();
		getOccurredFollowing().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>occurredFollowing</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition
     * </p> 
	 */
	public OccurredFollowing getOccurredFollowingFirstRep() {
		if (getOccurredFollowing().isEmpty()) {
			return addOccurredFollowing();
		}
		return getOccurredFollowing().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
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
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
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
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
     * </p> 
	 */
	public Condition setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
     * </p> 
	 */
	public Condition setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Condition.stage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical stage or grade of a condition. May include formal severity assessments
     * </p> 
	 */
	@Block()	
	public static class Stage 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="summary", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific"
	)
	private CodeableConceptDt mySummary;
	
	@Child(name="assessment", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Reference to a formal record of the evidence on which the staging assessment is based"
	)
	private java.util.List<ResourceReferenceDt> myAssessment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySummary,  myAssessment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySummary, myAssessment);
	}

	/**
	 * Gets the value(s) for <b>summary</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific
     * </p> 
	 */
	public CodeableConceptDt getSummary() {  
		if (mySummary == null) {
			mySummary = new CodeableConceptDt();
		}
		return mySummary;
	}

	/**
	 * Sets the value(s) for <b>summary</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific
     * </p> 
	 */
	public Stage setSummary(CodeableConceptDt theValue) {
		mySummary = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>assessment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a formal record of the evidence on which the staging assessment is based
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAssessment() {  
		if (myAssessment == null) {
			myAssessment = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAssessment;
	}

	/**
	 * Sets the value(s) for <b>assessment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a formal record of the evidence on which the staging assessment is based
     * </p> 
	 */
	public Stage setAssessment(java.util.List<ResourceReferenceDt> theValue) {
		myAssessment = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>assessment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a formal record of the evidence on which the staging assessment is based
     * </p> 
	 */
	public ResourceReferenceDt addAssessment() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAssessment().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Condition.evidence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	@Block()	
	public static class Evidence 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A manifestation or symptom that led to the recording of this condition"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="detail", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Links to other relevant information, including pathology reports"
	)
	private java.util.List<ResourceReferenceDt> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDetail);
	}

	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A manifestation or symptom that led to the recording of this condition
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A manifestation or symptom that led to the recording of this condition
     * </p> 
	 */
	public Evidence setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Links to other relevant information, including pathology reports
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Links to other relevant information, including pathology reports
     * </p> 
	 */
	public Evidence setDetail(java.util.List<ResourceReferenceDt> theValue) {
		myDetail = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Links to other relevant information, including pathology reports
     * </p> 
	 */
	public ResourceReferenceDt addDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDetail().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Condition.location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	@Block()	
	public static class Location 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Code that identifies the structural location"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="detail", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Detailed anatomical location information"
	)
	private StringDt myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDetail);
	}

	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the structural location
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the structural location
     * </p> 
	 */
	public Location setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public StringDt getDetailElement() {  
		if (myDetail == null) {
			myDetail = new StringDt();
		}
		return myDetail;
	}

	
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public String getDetail() {  
		return getDetailElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public Location setDetail(StringDt theValue) {
		myDetail = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public Location setDetail( String theString) {
		myDetail = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Condition.dueTo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition
     * </p> 
	 */
	@Block()	
	public static class DueTo 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="codeableConcept", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ConditionKind",
		formalDefinition="Code that identifies the target of this relationship. The code takes the place of a detailed instance target"
	)
	private CodeableConceptDt myCodeableConcept;
	
	@Child(name="target", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Condition.class, 		ca.uhn.fhir.model.dev.resource.Procedure.class, 		ca.uhn.fhir.model.dev.resource.MedicationAdministration.class, 		ca.uhn.fhir.model.dev.resource.Immunization.class, 		ca.uhn.fhir.model.dev.resource.MedicationStatement.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Target of the relationship"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCodeableConcept,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCodeableConcept, myTarget);
	}

	/**
	 * Gets the value(s) for <b>codeableConcept</b> (ConditionKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the target of this relationship. The code takes the place of a detailed instance target
     * </p> 
	 */
	public CodeableConceptDt getCodeableConcept() {  
		if (myCodeableConcept == null) {
			myCodeableConcept = new CodeableConceptDt();
		}
		return myCodeableConcept;
	}

	/**
	 * Sets the value(s) for <b>codeableConcept</b> (ConditionKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the target of this relationship. The code takes the place of a detailed instance target
     * </p> 
	 */
	public DueTo setCodeableConcept(CodeableConceptDt theValue) {
		myCodeableConcept = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Target of the relationship
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Target of the relationship
     * </p> 
	 */
	public DueTo setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Condition.occurredFollowing</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition
     * </p> 
	 */
	@Block()	
	public static class OccurredFollowing 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="codeableConcept", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ConditionKind",
		formalDefinition="Code that identifies the target of this relationship. The code takes the place of a detailed instance target"
	)
	private CodeableConceptDt myCodeableConcept;
	
	@Child(name="target", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Condition.class, 		ca.uhn.fhir.model.dev.resource.Procedure.class, 		ca.uhn.fhir.model.dev.resource.MedicationAdministration.class, 		ca.uhn.fhir.model.dev.resource.Immunization.class, 		ca.uhn.fhir.model.dev.resource.MedicationStatement.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Target of the relationship"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCodeableConcept,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCodeableConcept, myTarget);
	}

	/**
	 * Gets the value(s) for <b>codeableConcept</b> (ConditionKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the target of this relationship. The code takes the place of a detailed instance target
     * </p> 
	 */
	public CodeableConceptDt getCodeableConcept() {  
		if (myCodeableConcept == null) {
			myCodeableConcept = new CodeableConceptDt();
		}
		return myCodeableConcept;
	}

	/**
	 * Sets the value(s) for <b>codeableConcept</b> (ConditionKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the target of this relationship. The code takes the place of a detailed instance target
     * </p> 
	 */
	public OccurredFollowing setCodeableConcept(CodeableConceptDt theValue) {
		myCodeableConcept = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Target of the relationship
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Target of the relationship
     * </p> 
	 */
	public OccurredFollowing setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Condition";
    }

}
