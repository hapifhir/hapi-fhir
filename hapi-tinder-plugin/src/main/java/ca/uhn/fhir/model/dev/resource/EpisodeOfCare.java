















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
 * HAPI/FHIR <b>EpisodeOfCare</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/EpisodeOfCare">http://hl7.org/fhir/profiles/EpisodeOfCare</a> 
 * </p>
 *
 */
@ResourceDef(name="EpisodeOfCare", profile="http://hl7.org/fhir/profiles/EpisodeOfCare", id="episodeofcare")
public class EpisodeOfCare 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="EpisodeOfCare.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>EpisodeOfCare.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="EpisodeOfCare.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>EpisodeOfCare.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>EpisodeOfCare.currentStatus</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="EpisodeOfCare.currentStatus", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>EpisodeOfCare.currentStatus</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.managingOrganization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="EpisodeOfCare.managingOrganization", description="", type="reference"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.managingOrganization</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORGANIZATION = new ReferenceClientParam(SP_ORGANIZATION);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>EpisodeOfCare.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="EpisodeOfCare.period", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>EpisodeOfCare.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>EpisodeOfCare.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="EpisodeOfCare.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>EpisodeOfCare.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.condition</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="condition", path="EpisodeOfCare.condition", description="", type="reference"  )
	public static final String SP_CONDITION = "condition";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.condition</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONDITION = new ReferenceClientParam(SP_CONDITION);

	/**
	 * Search parameter constant for <b>referral</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.referralRequest</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="referral", path="EpisodeOfCare.referralRequest", description="", type="reference"  )
	public static final String SP_REFERRAL = "referral";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>referral</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.referralRequest</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REFERRAL = new ReferenceClientParam(SP_REFERRAL);

	/**
	 * Search parameter constant for <b>care-manager</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.careManager</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="care-manager", path="EpisodeOfCare.careManager", description="", type="reference"  )
	public static final String SP_CARE_MANAGER = "care-manager";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>care-manager</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>EpisodeOfCare.careManager</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CARE_MANAGER = new ReferenceClientParam(SP_CARE_MANAGER);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.careManager</b>".
	 */
	public static final Include INCLUDE_CAREMANAGER = new Include("EpisodeOfCare.careManager");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.condition</b>".
	 */
	public static final Include INCLUDE_CONDITION = new Include("EpisodeOfCare.condition");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.currentStatus</b>".
	 */
	public static final Include INCLUDE_CURRENTSTATUS = new Include("EpisodeOfCare.currentStatus");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("EpisodeOfCare.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.managingOrganization</b>".
	 */
	public static final Include INCLUDE_MANAGINGORGANIZATION = new Include("EpisodeOfCare.managingOrganization");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("EpisodeOfCare.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.period</b>".
	 */
	public static final Include INCLUDE_PERIOD = new Include("EpisodeOfCare.period");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.referralRequest</b>".
	 */
	public static final Include INCLUDE_REFERRALREQUEST = new Include("EpisodeOfCare.referralRequest");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>EpisodeOfCare.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("EpisodeOfCare.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="currentStatus", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="EpisodeOfCareStatus",
		formalDefinition=""
	)
	private CodeDt myCurrentStatus;
	
	@Child(name="statusHistory", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<StatusHistory> myStatusHistory;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The type can be very important in processing as this could be used in determining if the episodeofcare is relevant to specific government reporting, or other types of classifications"
	)
	private java.util.List<CodeableConceptDt> myType;
	
	@Child(name="patient", order=4, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="managingOrganization", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myManagingOrganization;
	
	@Child(name="period", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	
	@Child(name="condition", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Condition.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myCondition;
	
	@Child(name="referralRequest", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.ReferralRequest.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myReferralRequest;
	
	@Child(name="careManager", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myCareManager;
	
	@Child(name="careTeam", order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<CareTeam> myCareTeam;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCurrentStatus,  myStatusHistory,  myType,  myPatient,  myManagingOrganization,  myPeriod,  myCondition,  myReferralRequest,  myCareManager,  myCareTeam);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCurrentStatus, myStatusHistory, myType, myPatient, myManagingOrganization, myPeriod, myCondition, myReferralRequest, myCareManager, myCareTeam);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public EpisodeOfCare setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>currentStatus</b> (EpisodeOfCareStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCurrentStatusElement() {  
		if (myCurrentStatus == null) {
			myCurrentStatus = new CodeDt();
		}
		return myCurrentStatus;
	}

	
	/**
	 * Gets the value(s) for <b>currentStatus</b> (EpisodeOfCareStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getCurrentStatus() {  
		return getCurrentStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>currentStatus</b> (EpisodeOfCareStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setCurrentStatus(CodeDt theValue) {
		myCurrentStatus = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>currentStatus</b> (EpisodeOfCareStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setCurrentStatus( String theCode) {
		myCurrentStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>statusHistory</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<StatusHistory> getStatusHistory() {  
		if (myStatusHistory == null) {
			myStatusHistory = new java.util.ArrayList<StatusHistory>();
		}
		return myStatusHistory;
	}

	/**
	 * Sets the value(s) for <b>statusHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setStatusHistory(java.util.List<StatusHistory> theValue) {
		myStatusHistory = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>statusHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StatusHistory addStatusHistory() {
		StatusHistory newType = new StatusHistory();
		getStatusHistory().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>statusHistory</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StatusHistory getStatusHistoryFirstRep() {
		if (getStatusHistory().isEmpty()) {
			return addStatusHistory();
		}
		return getStatusHistory().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type can be very important in processing as this could be used in determining if the episodeofcare is relevant to specific government reporting, or other types of classifications
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type can be very important in processing as this could be used in determining if the episodeofcare is relevant to specific government reporting, or other types of classifications
     * </p> 
	 */
	public EpisodeOfCare setType(java.util.List<CodeableConceptDt> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type can be very important in processing as this could be used in determining if the episodeofcare is relevant to specific government reporting, or other types of classifications
     * </p> 
	 */
	public CodeableConceptDt addType() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getType().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>type</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The type can be very important in processing as this could be used in determining if the episodeofcare is relevant to specific government reporting, or other types of classifications
     * </p> 
	 */
	public CodeableConceptDt getTypeFirstRep() {
		if (getType().isEmpty()) {
			return addType();
		}
		return getType().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public EpisodeOfCare setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>managingOrganization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getManagingOrganization() {  
		if (myManagingOrganization == null) {
			myManagingOrganization = new ResourceReferenceDt();
		}
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for <b>managingOrganization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setManagingOrganization(ResourceReferenceDt theValue) {
		myManagingOrganization = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public EpisodeOfCare setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>condition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getCondition() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setCondition(java.util.List<ResourceReferenceDt> theValue) {
		myCondition = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>condition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addCondition() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getCondition().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>referralRequest</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getReferralRequest() {  
		if (myReferralRequest == null) {
			myReferralRequest = new ResourceReferenceDt();
		}
		return myReferralRequest;
	}

	/**
	 * Sets the value(s) for <b>referralRequest</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setReferralRequest(ResourceReferenceDt theValue) {
		myReferralRequest = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>careManager</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getCareManager() {  
		if (myCareManager == null) {
			myCareManager = new ResourceReferenceDt();
		}
		return myCareManager;
	}

	/**
	 * Sets the value(s) for <b>careManager</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setCareManager(ResourceReferenceDt theValue) {
		myCareManager = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>careTeam</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CareTeam> getCareTeam() {  
		if (myCareTeam == null) {
			myCareTeam = new java.util.ArrayList<CareTeam>();
		}
		return myCareTeam;
	}

	/**
	 * Sets the value(s) for <b>careTeam</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public EpisodeOfCare setCareTeam(java.util.List<CareTeam> theValue) {
		myCareTeam = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>careTeam</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CareTeam addCareTeam() {
		CareTeam newType = new CareTeam();
		getCareTeam().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>careTeam</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CareTeam getCareTeamFirstRep() {
		if (getCareTeam().isEmpty()) {
			return addCareTeam();
		}
		return getCareTeam().get(0); 
	}
  
	/**
	 * Block class for child element: <b>EpisodeOfCare.statusHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class StatusHistory 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="EpisodeOfCareStatus",
		formalDefinition=""
	)
	private CodeDt myStatus;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStatus, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>status</b> (EpisodeOfCareStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getStatusElement() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (EpisodeOfCareStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (EpisodeOfCareStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StatusHistory setStatus(CodeDt theValue) {
		myStatus = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>status</b> (EpisodeOfCareStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StatusHistory setStatus( String theCode) {
		myStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public StatusHistory setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>EpisodeOfCare.careTeam</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class CareTeam 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="member", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myMember;
	
	@Child(name="role", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> myRole;
	
	@Child(name="period", type=PeriodDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMember,  myRole,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMember, myRole, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>member</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public CareTeam setMember(ResourceReferenceDt theValue) {
		myMember = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>role</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRole() {  
		if (myRole == null) {
			myRole = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CareTeam setRole(java.util.List<CodeableConceptDt> theValue) {
		myRole = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>role</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addRole() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRole().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>role</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getRoleFirstRep() {
		if (getRole().isEmpty()) {
			return addRole();
		}
		return getRole().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public CareTeam setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "EpisodeOfCare";
    }

}
