















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
 * HAPI/FHIR <b>MedicationAdministration</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Describes the event of a patient being given a dose of a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MedicationAdministration">http://hl7.org/fhir/profiles/MedicationAdministration</a> 
 * </p>
 *
 */
@ResourceDef(name="MedicationAdministration", profile="http://hl7.org/fhir/profiles/MedicationAdministration", id="medicationadministration")
public class MedicationAdministration 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.device</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="device", path="MedicationAdministration.device", description="Return administrations with this administration device identity", type="reference"  )
	public static final String SP_DEVICE = "device";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.device</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DEVICE = new ReferenceClientParam(SP_DEVICE);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return administrations that share this encounter</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="MedicationAdministration.encounter", description="Return administrations that share this encounter", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return administrations that share this encounter</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="MedicationAdministration.identifier", description="Return administrations with this external identity", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Return administrations of this medication</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.medication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="medication", path="MedicationAdministration.medication", description="Return administrations of this medication", type="reference"  )
	public static final String SP_MEDICATION = "medication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Return administrations of this medication</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.medication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MEDICATION = new ReferenceClientParam(SP_MEDICATION);

	/**
	 * Search parameter constant for <b>notgiven</b>
	 * <p>
	 * Description: <b>Administrations that were not made</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.wasNotGiven</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="notgiven", path="MedicationAdministration.wasNotGiven", description="Administrations that were not made", type="token"  )
	public static final String SP_NOTGIVEN = "notgiven";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>notgiven</b>
	 * <p>
	 * Description: <b>Administrations that were not made</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.wasNotGiven</b><br/>
	 * </p>
	 */
	public static final TokenClientParam NOTGIVEN = new TokenClientParam(SP_NOTGIVEN);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="MedicationAdministration.patient", description="The identity of a patient to list administrations  for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list administrations from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.prescription</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="prescription", path="MedicationAdministration.prescription", description="The identity of a prescription to list administrations from", type="reference"  )
	public static final String SP_PRESCRIPTION = "prescription";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list administrations from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.prescription</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PRESCRIPTION = new ReferenceClientParam(SP_PRESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="MedicationAdministration.status", description="MedicationAdministration event status (for example one of active/paused/completed/nullified)", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>effectivetime</b>
	 * <p>
	 * Description: <b>Date administration happened (or did not happen)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationAdministration.effectiveTime[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="effectivetime", path="MedicationAdministration.effectiveTime[x]", description="Date administration happened (or did not happen)", type="date"  )
	public static final String SP_EFFECTIVETIME = "effectivetime";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>effectivetime</b>
	 * <p>
	 * Description: <b>Date administration happened (or did not happen)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationAdministration.effectiveTime[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam EFFECTIVETIME = new DateClientParam(SP_EFFECTIVETIME);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.device</b>".
	 */
	public static final Include INCLUDE_DEVICE = new Include("MedicationAdministration.device");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.effectiveTime[x]</b>".
	 */
	public static final Include INCLUDE_EFFECTIVETIME = new Include("MedicationAdministration.effectiveTime[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("MedicationAdministration.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("MedicationAdministration.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.medication</b>".
	 */
	public static final Include INCLUDE_MEDICATION = new Include("MedicationAdministration.medication");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("MedicationAdministration.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.prescription</b>".
	 */
	public static final Include INCLUDE_PRESCRIPTION = new Include("MedicationAdministration.prescription");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("MedicationAdministration.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.wasNotGiven</b>".
	 */
	public static final Include INCLUDE_WASNOTGIVEN = new Include("MedicationAdministration.wasNotGiven");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="MedicationAdministrationStatus",
		formalDefinition="Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way."
	)
	private BoundCodeDt<MedicationAdministrationStatusEnum> myStatus;
	
	@Child(name="patient", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person or animal to whom the medication was given."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="practitioner", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The individual who was responsible for giving the medication to the patient."
	)
	private ResourceReferenceDt myPractitioner;
	
	@Child(name="encounter", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="prescription", order=5, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.MedicationPrescription.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The original request, instruction or authority to perform the administration."
	)
	private ResourceReferenceDt myPrescription;
	
	@Child(name="wasNotGiven", type=BooleanDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Set this to true if the record is saying that the medication was NOT administered."
	)
	private BooleanDt myWasNotGiven;
	
	@Child(name="reasonNotGiven", type=CodeableConceptDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="MedicationAdministrationNegationReason",
		formalDefinition="A code indicating why the administration was not performed."
	)
	private java.util.List<CodeableConceptDt> myReasonNotGiven;
	
	@Child(name="effectiveTime", order=8, min=1, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same."
	)
	private IDatatype myEffectiveTime;
	
	@Child(name="medication", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="device", order=10, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The device used in administering the medication to the patient.  E.g. a particular infusion pump"
	)
	private java.util.List<ResourceReferenceDt> myDevice;
	
	@Child(name="dosage", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Provides details of how much of the medication was administered"
	)
	private java.util.List<Dosage> myDosage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myPatient,  myPractitioner,  myEncounter,  myPrescription,  myWasNotGiven,  myReasonNotGiven,  myEffectiveTime,  myMedication,  myDevice,  myDosage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myPatient, myPractitioner, myEncounter, myPrescription, myWasNotGiven, myReasonNotGiven, myEffectiveTime, myMedication, myDevice, myDosage);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
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
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public MedicationAdministration setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
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
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (MedicationAdministrationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public BoundCodeDt<MedicationAdministrationStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationAdministrationStatusEnum>(MedicationAdministrationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (MedicationAdministrationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (MedicationAdministrationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public MedicationAdministration setStatus(BoundCodeDt<MedicationAdministrationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (MedicationAdministrationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public MedicationAdministration setStatus(MedicationAdministrationStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal to whom the medication was given.
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
     * The person or animal to whom the medication was given.
     * </p> 
	 */
	public MedicationAdministration setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>practitioner</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public ResourceReferenceDt getPractitioner() {  
		if (myPractitioner == null) {
			myPractitioner = new ResourceReferenceDt();
		}
		return myPractitioner;
	}

	/**
	 * Sets the value(s) for <b>practitioner</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public MedicationAdministration setPractitioner(ResourceReferenceDt theValue) {
		myPractitioner = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
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
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
     * </p> 
	 */
	public MedicationAdministration setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>prescription</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public ResourceReferenceDt getPrescription() {  
		if (myPrescription == null) {
			myPrescription = new ResourceReferenceDt();
		}
		return myPrescription;
	}

	/**
	 * Sets the value(s) for <b>prescription</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public MedicationAdministration setPrescription(ResourceReferenceDt theValue) {
		myPrescription = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>wasNotGiven</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public BooleanDt getWasNotGivenElement() {  
		if (myWasNotGiven == null) {
			myWasNotGiven = new BooleanDt();
		}
		return myWasNotGiven;
	}

	
	/**
	 * Gets the value(s) for <b>wasNotGiven</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public Boolean getWasNotGiven() {  
		return getWasNotGivenElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>wasNotGiven</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public MedicationAdministration setWasNotGiven(BooleanDt theValue) {
		myWasNotGiven = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>wasNotGiven</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public MedicationAdministration setWasNotGiven( boolean theBoolean) {
		myWasNotGiven = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reasonNotGiven</b> (MedicationAdministrationNegationReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getReasonNotGiven() {  
		if (myReasonNotGiven == null) {
			myReasonNotGiven = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myReasonNotGiven;
	}

	/**
	 * Sets the value(s) for <b>reasonNotGiven</b> (MedicationAdministrationNegationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public MedicationAdministration setReasonNotGiven(java.util.List<CodeableConceptDt> theValue) {
		myReasonNotGiven = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>reasonNotGiven</b> (MedicationAdministrationNegationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public CodeableConceptDt addReasonNotGiven() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getReasonNotGiven().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reasonNotGiven</b> (MedicationAdministrationNegationReason),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public CodeableConceptDt getReasonNotGivenFirstRep() {
		if (getReasonNotGiven().isEmpty()) {
			return addReasonNotGiven();
		}
		return getReasonNotGiven().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>effectiveTime[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public IDatatype getEffectiveTime() {  
		return myEffectiveTime;
	}

	/**
	 * Sets the value(s) for <b>effectiveTime[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public MedicationAdministration setEffectiveTime(IDatatype theValue) {
		myEffectiveTime = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>medication</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedication() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}

	/**
	 * Sets the value(s) for <b>medication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public MedicationAdministration setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>device</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDevice() {  
		if (myDevice == null) {
			myDevice = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDevice;
	}

	/**
	 * Sets the value(s) for <b>device</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public MedicationAdministration setDevice(java.util.List<ResourceReferenceDt> theValue) {
		myDevice = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>device</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public ResourceReferenceDt addDevice() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDevice().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>dosage</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public java.util.List<Dosage> getDosage() {  
		if (myDosage == null) {
			myDosage = new java.util.ArrayList<Dosage>();
		}
		return myDosage;
	}

	/**
	 * Sets the value(s) for <b>dosage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public MedicationAdministration setDosage(java.util.List<Dosage> theValue) {
		myDosage = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dosage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public Dosage addDosage() {
		Dosage newType = new Dosage();
		getDosage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dosage</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public Dosage getDosageFirstRep() {
		if (getDosage().isEmpty()) {
			return addDosage();
		}
		return getDosage().get(0); 
	}
  
	/**
	 * Block class for child element: <b>MedicationAdministration.dosage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	@Block()	
	public static class Dosage 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="timing", order=0, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)"
	)
	private IDatatype myTiming;
	
	@Child(name="asNeeded", order=1, min=0, max=1, type={
		BooleanDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="MedicationAsNeededReason",
		formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication"
	)
	private IDatatype myAsNeeded;
	
	@Child(name="site", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="MedicationAdministrationSite",
		formalDefinition="A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\""
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="RouteOfAdministration",
		formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc."
	)
	private CodeableConceptDt myRoute;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="MedicationAdministrationMethod",
		formalDefinition="A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration."
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="quantity", type=QuantityDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection."
	)
	private QuantityDt myQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity."
	)
	private RatioDt myRate;
	
	@Child(name="maxDosePerPeriod", type=RatioDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours."
	)
	private RatioDt myMaxDosePerPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTiming,  myAsNeeded,  mySite,  myRoute,  myMethod,  myQuantity,  myRate,  myMaxDosePerPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTiming, myAsNeeded, mySite, myRoute, myMethod, myQuantity, myRate, myMaxDosePerPeriod);
	}

	/**
	 * Gets the value(s) for <b>timing[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public IDatatype getTiming() {  
		return myTiming;
	}

	/**
	 * Sets the value(s) for <b>timing[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public Dosage setTiming(IDatatype theValue) {
		myTiming = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>asNeeded[x]</b> (MedicationAsNeededReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public IDatatype getAsNeeded() {  
		return myAsNeeded;
	}

	/**
	 * Sets the value(s) for <b>asNeeded[x]</b> (MedicationAsNeededReason)
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public Dosage setAsNeeded(IDatatype theValue) {
		myAsNeeded = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>site</b> (MedicationAdministrationSite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (MedicationAdministrationSite)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public Dosage setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>route</b> (RouteOfAdministration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public CodeableConceptDt getRoute() {  
		if (myRoute == null) {
			myRoute = new CodeableConceptDt();
		}
		return myRoute;
	}

	/**
	 * Sets the value(s) for <b>route</b> (RouteOfAdministration)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public Dosage setRoute(CodeableConceptDt theValue) {
		myRoute = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>method</b> (MedicationAdministrationMethod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (MedicationAdministrationMethod)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public Dosage setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
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
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity(QuantityDt theValue) {
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
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
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
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
     * </p> 
	 */
	public Dosage setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriod() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}

	/**
	 * Sets the value(s) for <b>maxDosePerPeriod</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public Dosage setMaxDosePerPeriod(RatioDt theValue) {
		myMaxDosePerPeriod = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "MedicationAdministration";
    }

}
