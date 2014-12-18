















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
 * HAPI/FHIR <b>ImmunizationRecommendation</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A patient's point-of-time immunization status and recommendation with optional supporting justification
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ImmunizationRecommendation">http://hl7.org/fhir/profiles/ImmunizationRecommendation</a> 
 * </p>
 *
 */
@ResourceDef(name="ImmunizationRecommendation", profile="http://hl7.org/fhir/profiles/ImmunizationRecommendation", id="immunizationrecommendation")
public class ImmunizationRecommendation 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="ImmunizationRecommendation.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.vaccineType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="vaccine-type", path="ImmunizationRecommendation.recommendation.vaccineType", description="", type="token"  )
	public static final String SP_VACCINE_TYPE = "vaccine-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.vaccineType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VACCINE_TYPE = new TokenClientParam(SP_VACCINE_TYPE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="ImmunizationRecommendation.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="ImmunizationRecommendation.recommendation.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>dose-number</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.doseNumber</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dose-number", path="ImmunizationRecommendation.recommendation.doseNumber", description="", type="number"  )
	public static final String SP_DOSE_NUMBER = "dose-number";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dose-number</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.doseNumber</b><br/>
	 * </p>
	 */
	public static final NumberClientParam DOSE_NUMBER = new NumberClientParam(SP_DOSE_NUMBER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.forecastStatus</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="ImmunizationRecommendation.recommendation.forecastStatus", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.forecastStatus</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dose-sequence", path="ImmunizationRecommendation.recommendation.protocol.doseSequence", description="", type="token"  )
	public static final String SP_DOSE_SEQUENCE = "dose-sequence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DOSE_SEQUENCE = new TokenClientParam(SP_DOSE_SEQUENCE);

	/**
	 * Search parameter constant for <b>support</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingImmunization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="support", path="ImmunizationRecommendation.recommendation.supportingImmunization", description="", type="reference"  )
	public static final String SP_SUPPORT = "support";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>support</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingImmunization</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUPPORT = new ReferenceClientParam(SP_SUPPORT);

	/**
	 * Search parameter constant for <b>information</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="information", path="ImmunizationRecommendation.recommendation.supportingPatientInformation", description="", type="reference"  )
	public static final String SP_INFORMATION = "information";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>information</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam INFORMATION = new ReferenceClientParam(SP_INFORMATION);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="ImmunizationRecommendation.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("ImmunizationRecommendation.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.date</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_DATE = new Include("ImmunizationRecommendation.recommendation.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.doseNumber</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_DOSENUMBER = new Include("ImmunizationRecommendation.recommendation.doseNumber");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.forecastStatus</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_FORECASTSTATUS = new Include("ImmunizationRecommendation.recommendation.forecastStatus");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_PROTOCOL_DOSESEQUENCE = new Include("ImmunizationRecommendation.recommendation.protocol.doseSequence");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.supportingImmunization</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_SUPPORTINGIMMUNIZATION = new Include("ImmunizationRecommendation.recommendation.supportingImmunization");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_SUPPORTINGPATIENTINFORMATION = new Include("ImmunizationRecommendation.recommendation.supportingPatientInformation");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.vaccineType</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_VACCINETYPE = new Include("ImmunizationRecommendation.recommendation.vaccineType");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("ImmunizationRecommendation.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A unique identifier assigned to this particular recommendation record."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The patient who is the subject of the profile"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="recommendation", order=2, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Vaccine administration recommendations"
	)
	private java.util.List<Recommendation> myRecommendation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myRecommendation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myRecommendation);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this particular recommendation record.
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
     * A unique identifier assigned to this particular recommendation record.
     * </p> 
	 */
	public ImmunizationRecommendation setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this particular recommendation record.
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
     * A unique identifier assigned to this particular recommendation record.
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
     * The patient who is the subject of the profile
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
     * The patient who is the subject of the profile
     * </p> 
	 */
	public ImmunizationRecommendation setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>recommendation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public java.util.List<Recommendation> getRecommendation() {  
		if (myRecommendation == null) {
			myRecommendation = new java.util.ArrayList<Recommendation>();
		}
		return myRecommendation;
	}

	/**
	 * Sets the value(s) for <b>recommendation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public ImmunizationRecommendation setRecommendation(java.util.List<Recommendation> theValue) {
		myRecommendation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>recommendation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public Recommendation addRecommendation() {
		Recommendation newType = new Recommendation();
		getRecommendation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>recommendation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public Recommendation getRecommendationFirstRep() {
		if (getRecommendation().isEmpty()) {
			return addRecommendation();
		}
		return getRecommendation().get(0); 
	}
  
	/**
	 * Block class for child element: <b>ImmunizationRecommendation.recommendation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	@Block()	
	public static class Recommendation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="date", type=DateTimeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date the immunization recommendation was created."
	)
	private DateTimeDt myDate;
	
	@Child(name="vaccineType", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="VaccineType",
		formalDefinition="Vaccine that pertains to the recommendation"
	)
	private CodeableConceptDt myVaccineType;
	
	@Child(name="doseNumber", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose)."
	)
	private IntegerDt myDoseNumber;
	
	@Child(name="forecastStatus", type=CodeableConceptDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="ImmunizationRecommendationStatus",
		formalDefinition="Vaccine administration status"
	)
	private BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum> myForecastStatus;
	
	@Child(name="dateCriterion", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc."
	)
	private java.util.List<RecommendationDateCriterion> myDateCriterion;
	
	@Child(name="protocol", order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Contains information about the protocol under which the vaccine was administered"
	)
	private RecommendationProtocol myProtocol;
	
	@Child(name="supportingImmunization", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Immunization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Immunization event history that supports the status and recommendation"
	)
	private java.util.List<ResourceReferenceDt> mySupportingImmunization;
	
	@Child(name="supportingPatientInformation", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Observation.class, 		ca.uhn.fhir.model.dev.resource.AllergyIntolerance.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information."
	)
	private java.util.List<ResourceReferenceDt> mySupportingPatientInformation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDate,  myVaccineType,  myDoseNumber,  myForecastStatus,  myDateCriterion,  myProtocol,  mySupportingImmunization,  mySupportingPatientInformation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDate, myVaccineType, myDoseNumber, myForecastStatus, myDateCriterion, myProtocol, mySupportingImmunization, mySupportingPatientInformation);
	}

	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Date getDate() {  
		return getDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Recommendation setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Recommendation setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Recommendation setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>vaccineType</b> (VaccineType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that pertains to the recommendation
     * </p> 
	 */
	public CodeableConceptDt getVaccineType() {  
		if (myVaccineType == null) {
			myVaccineType = new CodeableConceptDt();
		}
		return myVaccineType;
	}

	/**
	 * Sets the value(s) for <b>vaccineType</b> (VaccineType)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that pertains to the recommendation
     * </p> 
	 */
	public Recommendation setVaccineType(CodeableConceptDt theValue) {
		myVaccineType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>doseNumber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public IntegerDt getDoseNumberElement() {  
		if (myDoseNumber == null) {
			myDoseNumber = new IntegerDt();
		}
		return myDoseNumber;
	}

	
	/**
	 * Gets the value(s) for <b>doseNumber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public Integer getDoseNumber() {  
		return getDoseNumberElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>doseNumber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public Recommendation setDoseNumber(IntegerDt theValue) {
		myDoseNumber = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>doseNumber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public Recommendation setDoseNumber( int theInteger) {
		myDoseNumber = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>forecastStatus</b> (ImmunizationRecommendationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration status
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum> getForecastStatus() {  
		if (myForecastStatus == null) {
			myForecastStatus = new BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum>(ImmunizationRecommendationStatusCodesEnum.VALUESET_BINDER);
		}
		return myForecastStatus;
	}

	/**
	 * Sets the value(s) for <b>forecastStatus</b> (ImmunizationRecommendationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration status
     * </p> 
	 */
	public Recommendation setForecastStatus(BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum> theValue) {
		myForecastStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>forecastStatus</b> (ImmunizationRecommendationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration status
     * </p> 
	 */
	public Recommendation setForecastStatus(ImmunizationRecommendationStatusCodesEnum theValue) {
		getForecastStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateCriterion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public java.util.List<RecommendationDateCriterion> getDateCriterion() {  
		if (myDateCriterion == null) {
			myDateCriterion = new java.util.ArrayList<RecommendationDateCriterion>();
		}
		return myDateCriterion;
	}

	/**
	 * Sets the value(s) for <b>dateCriterion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public Recommendation setDateCriterion(java.util.List<RecommendationDateCriterion> theValue) {
		myDateCriterion = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dateCriterion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public RecommendationDateCriterion addDateCriterion() {
		RecommendationDateCriterion newType = new RecommendationDateCriterion();
		getDateCriterion().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dateCriterion</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public RecommendationDateCriterion getDateCriterionFirstRep() {
		if (getDateCriterion().isEmpty()) {
			return addDateCriterion();
		}
		return getDateCriterion().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>protocol</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol under which the vaccine was administered
     * </p> 
	 */
	public RecommendationProtocol getProtocol() {  
		if (myProtocol == null) {
			myProtocol = new RecommendationProtocol();
		}
		return myProtocol;
	}

	/**
	 * Sets the value(s) for <b>protocol</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol under which the vaccine was administered
     * </p> 
	 */
	public Recommendation setProtocol(RecommendationProtocol theValue) {
		myProtocol = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>supportingImmunization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Immunization event history that supports the status and recommendation
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSupportingImmunization() {  
		if (mySupportingImmunization == null) {
			mySupportingImmunization = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySupportingImmunization;
	}

	/**
	 * Sets the value(s) for <b>supportingImmunization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Immunization event history that supports the status and recommendation
     * </p> 
	 */
	public Recommendation setSupportingImmunization(java.util.List<ResourceReferenceDt> theValue) {
		mySupportingImmunization = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>supportingImmunization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Immunization event history that supports the status and recommendation
     * </p> 
	 */
	public ResourceReferenceDt addSupportingImmunization() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSupportingImmunization().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>supportingPatientInformation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSupportingPatientInformation() {  
		if (mySupportingPatientInformation == null) {
			mySupportingPatientInformation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySupportingPatientInformation;
	}

	/**
	 * Sets the value(s) for <b>supportingPatientInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
     * </p> 
	 */
	public Recommendation setSupportingPatientInformation(java.util.List<ResourceReferenceDt> theValue) {
		mySupportingPatientInformation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>supportingPatientInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
     * </p> 
	 */
	public ResourceReferenceDt addSupportingPatientInformation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSupportingPatientInformation().add(newType);
		return newType; 
	}
  

	}

	/**
	 * Block class for child element: <b>ImmunizationRecommendation.recommendation.dateCriterion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	@Block()	
	public static class RecommendationDateCriterion 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ImmunizationRecommendationDateCriterion",
		formalDefinition="Date classification of recommendation - e.g. earliest date to give, latest date to give, etc."
	)
	private BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum> myCode;
	
	@Child(name="value", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date recommendation"
	)
	private DateTimeDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myValue);
	}

	/**
	 * Gets the value(s) for <b>code</b> (ImmunizationRecommendationDateCriterion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum>(ImmunizationRecommendationDateCriterionCodesEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (ImmunizationRecommendationDateCriterion)
	 *
     * <p>
     * <b>Definition:</b>
     * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
     * </p> 
	 */
	public RecommendationDateCriterion setCode(BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>code</b> (ImmunizationRecommendationDateCriterion)
	 *
     * <p>
     * <b>Definition:</b>
     * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
     * </p> 
	 */
	public RecommendationDateCriterion setCode(ImmunizationRecommendationDateCriterionCodesEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>value</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public DateTimeDt getValueElement() {  
		if (myValue == null) {
			myValue = new DateTimeDt();
		}
		return myValue;
	}

	
	/**
	 * Gets the value(s) for <b>value</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public Date getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public RecommendationDateCriterion setValue(DateTimeDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public RecommendationDateCriterion setValue( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myValue = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public RecommendationDateCriterion setValueWithSecondsPrecision( Date theDate) {
		myValue = new DateTimeDt(theDate); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>ImmunizationRecommendation.recommendation.protocol</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol under which the vaccine was administered
     * </p> 
	 */
	@Block()	
	public static class RecommendationProtocol 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="doseSequence", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol."
	)
	private IntegerDt myDoseSequence;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Contains the description about the protocol under which the vaccine was administered"
	)
	private StringDt myDescription;
	
	@Child(name="authority", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the authority who published the protocol?  E.g. ACIP"
	)
	private ResourceReferenceDt myAuthority;
	
	@Child(name="series", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority"
	)
	private StringDt mySeries;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDoseSequence,  myDescription,  myAuthority,  mySeries);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDoseSequence, myDescription, myAuthority, mySeries);
	}

	/**
	 * Gets the value(s) for <b>doseSequence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public IntegerDt getDoseSequenceElement() {  
		if (myDoseSequence == null) {
			myDoseSequence = new IntegerDt();
		}
		return myDoseSequence;
	}

	
	/**
	 * Gets the value(s) for <b>doseSequence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public Integer getDoseSequence() {  
		return getDoseSequenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>doseSequence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public RecommendationProtocol setDoseSequence(IntegerDt theValue) {
		myDoseSequence = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>doseSequence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public RecommendationProtocol setDoseSequence( int theInteger) {
		myDoseSequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
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
     * Contains the description about the protocol under which the vaccine was administered
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
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public RecommendationProtocol setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public RecommendationProtocol setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>authority</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the authority who published the protocol?  E.g. ACIP
     * </p> 
	 */
	public ResourceReferenceDt getAuthority() {  
		if (myAuthority == null) {
			myAuthority = new ResourceReferenceDt();
		}
		return myAuthority;
	}

	/**
	 * Sets the value(s) for <b>authority</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the authority who published the protocol?  E.g. ACIP
     * </p> 
	 */
	public RecommendationProtocol setAuthority(ResourceReferenceDt theValue) {
		myAuthority = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>series</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public StringDt getSeriesElement() {  
		if (mySeries == null) {
			mySeries = new StringDt();
		}
		return mySeries;
	}

	
	/**
	 * Gets the value(s) for <b>series</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public String getSeries() {  
		return getSeriesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public RecommendationProtocol setSeries(StringDt theValue) {
		mySeries = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public RecommendationProtocol setSeries( String theString) {
		mySeries = new StringDt(theString); 
		return this; 
	}

 

	}





    @Override
    public String getResourceName() {
        return "ImmunizationRecommendation";
    }

}
