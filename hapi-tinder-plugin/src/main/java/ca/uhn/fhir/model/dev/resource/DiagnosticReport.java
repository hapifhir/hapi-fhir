















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
 * HAPI/FHIR <b>DiagnosticReport</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretation, and formatted representation of diagnostic reports.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support reporting for any diagnostic report into a clinical data repository.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DiagnosticReport">http://hl7.org/fhir/profiles/DiagnosticReport</a> 
 * </p>
 *
 */
@ResourceDef(name="DiagnosticReport", profile="http://hl7.org/fhir/profiles/DiagnosticReport", id="diagnosticreport")
public class DiagnosticReport 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DiagnosticReport.status", description="The status of the report", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>issued</b>
	 * <p>
	 * Description: <b>When the report was issued</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticReport.issued</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="issued", path="DiagnosticReport.issued", description="When the report was issued", type="date"  )
	public static final String SP_ISSUED = "issued";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>issued</b>
	 * <p>
	 * Description: <b>When the report was issued</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticReport.issued</b><br/>
	 * </p>
	 */
	public static final DateClientParam ISSUED = new DateClientParam(SP_ISSUED);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the report</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DiagnosticReport.subject", description="The subject of the report", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the report</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who was the source of the report (organization)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.performer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="DiagnosticReport.performer", description="Who was the source of the report (organization)", type="reference"  )
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who was the source of the report (organization)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.performer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PERFORMER = new ReferenceClientParam(SP_PERFORMER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DiagnosticReport.identifier", description="An identifier for the report", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>service</b>
	 * <p>
	 * Description: <b>Which diagnostic discipline/department created the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.serviceCategory</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="service", path="DiagnosticReport.serviceCategory", description="Which diagnostic discipline/department created the report", type="token"  )
	public static final String SP_SERVICE = "service";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>service</b>
	 * <p>
	 * Description: <b>Which diagnostic discipline/department created the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.serviceCategory</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SERVICE = new TokenClientParam(SP_SERVICE);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The clinically relevant time of the report</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticReport.diagnostic[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="DiagnosticReport.diagnostic[x]", description="The clinically relevant time of the report", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The clinically relevant time of the report</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticReport.diagnostic[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>The specimen details</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.specimen</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="DiagnosticReport.specimen", description="The specimen details", type="reference"  )
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>The specimen details</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.specimen</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SPECIMEN = new ReferenceClientParam(SP_SPECIMEN);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="DiagnosticReport.name", description="The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)", type="token"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.name</b><br/>
	 * </p>
	 */
	public static final TokenClientParam NAME = new TokenClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>result</b>
	 * <p>
	 * Description: <b>Link to an atomic result (observation resource)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.result</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="result", path="DiagnosticReport.result", description="Link to an atomic result (observation resource)", type="reference"  )
	public static final String SP_RESULT = "result";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>result</b>
	 * <p>
	 * Description: <b>Link to an atomic result (observation resource)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.result</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RESULT = new ReferenceClientParam(SP_RESULT);

	/**
	 * Search parameter constant for <b>diagnosis</b>
	 * <p>
	 * Description: <b>A coded diagnosis on the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.codedDiagnosis</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="diagnosis", path="DiagnosticReport.codedDiagnosis", description="A coded diagnosis on the report", type="token"  )
	public static final String SP_DIAGNOSIS = "diagnosis";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>diagnosis</b>
	 * <p>
	 * Description: <b>A coded diagnosis on the report</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticReport.codedDiagnosis</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DIAGNOSIS = new TokenClientParam(SP_DIAGNOSIS);

	/**
	 * Search parameter constant for <b>image</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.image.link</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="image", path="DiagnosticReport.image.link", description="", type="reference"  )
	public static final String SP_IMAGE = "image";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>image</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.image.link</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam IMAGE = new ReferenceClientParam(SP_IMAGE);

	/**
	 * Search parameter constant for <b>request</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.requestDetail</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="request", path="DiagnosticReport.requestDetail", description="", type="reference"  )
	public static final String SP_REQUEST = "request";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>request</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.requestDetail</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REQUEST = new ReferenceClientParam(SP_REQUEST);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The subject of the report if a patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="DiagnosticReport.subject", description="The subject of the report if a patient", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The subject of the report if a patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticReport.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.codedDiagnosis</b>".
	 */
	public static final Include INCLUDE_CODEDDIAGNOSIS = new Include("DiagnosticReport.codedDiagnosis");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.diagnostic[x]</b>".
	 */
	public static final Include INCLUDE_DIAGNOSTIC = new Include("DiagnosticReport.diagnostic[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("DiagnosticReport.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.image.link</b>".
	 */
	public static final Include INCLUDE_IMAGE_LINK = new Include("DiagnosticReport.image.link");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.issued</b>".
	 */
	public static final Include INCLUDE_ISSUED = new Include("DiagnosticReport.issued");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("DiagnosticReport.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("DiagnosticReport.performer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.requestDetail</b>".
	 */
	public static final Include INCLUDE_REQUESTDETAIL = new Include("DiagnosticReport.requestDetail");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.result</b>".
	 */
	public static final Include INCLUDE_RESULT = new Include("DiagnosticReport.result");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.serviceCategory</b>".
	 */
	public static final Include INCLUDE_SERVICECATEGORY = new Include("DiagnosticReport.serviceCategory");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.specimen</b>".
	 */
	public static final Include INCLUDE_SPECIMEN = new Include("DiagnosticReport.specimen");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("DiagnosticReport.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DiagnosticReport.subject");


	@Child(name="name", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="DiagnosticReportNames",
		formalDefinition="A code or name that describes this diagnostic report"
	)
	private CodeableConceptDt myName;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="DiagnosticReportStatus",
		formalDefinition="The status of the diagnostic report as a whole"
	)
	private BoundCodeDt<DiagnosticReportStatusEnum> myStatus;
	
	@Child(name="issued", type=DateTimeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date and/or time that this version of the report was released from the source diagnostic service"
	)
	private DateTimeDt myIssued;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources."
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="performer", order=4, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The diagnostic service that is responsible for issuing the report"
	)
	private ResourceReferenceDt myPerformer;
	
	@Child(name="identifier", type=IdentifierDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="requestDetail", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.DiagnosticOrder.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Details concerning a test requested."
	)
	private java.util.List<ResourceReferenceDt> myRequestDetail;
	
	@Child(name="serviceCategory", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="DiagnosticServiceSection",
		formalDefinition="The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI"
	)
	private CodeableConceptDt myServiceCategory;
	
	@Child(name="diagnostic", order=8, min=1, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself."
	)
	private IDatatype myDiagnostic;
	
	@Child(name="specimen", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Specimen.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Details about the specimens on which this diagnostic report is based"
	)
	private java.util.List<ResourceReferenceDt> mySpecimen;
	
	@Child(name="result", order=10, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Observation.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\")."
	)
	private java.util.List<ResourceReferenceDt> myResult;
	
	@Child(name="imagingStudy", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.ImagingStudy.class	})
	@Description(
		shortDefinition="",
		formalDefinition="One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images."
	)
	private java.util.List<ResourceReferenceDt> myImagingStudy;
	
	@Child(name="image", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)."
	)
	private java.util.List<Image> myImage;
	
	@Child(name="conclusion", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Concise and clinically contextualized narrative interpretation of the diagnostic report"
	)
	private StringDt myConclusion;
	
	@Child(name="codedDiagnosis", type=CodeableConceptDt.class, order=14, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="AdjunctDiagnosis",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> myCodedDiagnosis;
	
	@Child(name="presentedForm", type=AttachmentDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent."
	)
	private java.util.List<AttachmentDt> myPresentedForm;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myStatus,  myIssued,  mySubject,  myPerformer,  myIdentifier,  myRequestDetail,  myServiceCategory,  myDiagnostic,  mySpecimen,  myResult,  myImagingStudy,  myImage,  myConclusion,  myCodedDiagnosis,  myPresentedForm);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myStatus, myIssued, mySubject, myPerformer, myIdentifier, myRequestDetail, myServiceCategory, myDiagnostic, mySpecimen, myResult, myImagingStudy, myImage, myConclusion, myCodedDiagnosis, myPresentedForm);
	}

	/**
	 * Gets the value(s) for <b>name</b> (DiagnosticReportNames).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code or name that describes this diagnostic report
     * </p> 
	 */
	public CodeableConceptDt getName() {  
		if (myName == null) {
			myName = new CodeableConceptDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (DiagnosticReportNames)
	 *
     * <p>
     * <b>Definition:</b>
     * A code or name that describes this diagnostic report
     * </p> 
	 */
	public DiagnosticReport setName(CodeableConceptDt theValue) {
		myName = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticReportStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public BoundCodeDt<DiagnosticReportStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticReportStatusEnum>(DiagnosticReportStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (DiagnosticReportStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticReportStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public DiagnosticReport setStatus(BoundCodeDt<DiagnosticReportStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (DiagnosticReportStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public DiagnosticReport setStatus(DiagnosticReportStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DateTimeDt getIssuedElement() {  
		if (myIssued == null) {
			myIssued = new DateTimeDt();
		}
		return myIssued;
	}

	
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public Date getIssued() {  
		return getIssuedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DiagnosticReport setIssued(DateTimeDt theValue) {
		myIssued = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DiagnosticReport setIssued( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIssued = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DiagnosticReport setIssuedWithSecondsPrecision( Date theDate) {
		myIssued = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.
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
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.
     * </p> 
	 */
	public DiagnosticReport setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>performer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The diagnostic service that is responsible for issuing the report
     * </p> 
	 */
	public ResourceReferenceDt getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new ResourceReferenceDt();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The diagnostic service that is responsible for issuing the report
     * </p> 
	 */
	public DiagnosticReport setPerformer(ResourceReferenceDt theValue) {
		myPerformer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public DiagnosticReport setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>requestDetail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRequestDetail() {  
		if (myRequestDetail == null) {
			myRequestDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRequestDetail;
	}

	/**
	 * Sets the value(s) for <b>requestDetail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public DiagnosticReport setRequestDetail(java.util.List<ResourceReferenceDt> theValue) {
		myRequestDetail = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>requestDetail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public ResourceReferenceDt addRequestDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getRequestDetail().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>serviceCategory</b> (DiagnosticServiceSection).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI
     * </p> 
	 */
	public CodeableConceptDt getServiceCategory() {  
		if (myServiceCategory == null) {
			myServiceCategory = new CodeableConceptDt();
		}
		return myServiceCategory;
	}

	/**
	 * Sets the value(s) for <b>serviceCategory</b> (DiagnosticServiceSection)
	 *
     * <p>
     * <b>Definition:</b>
     * The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI
     * </p> 
	 */
	public DiagnosticReport setServiceCategory(CodeableConceptDt theValue) {
		myServiceCategory = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>diagnostic[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.
     * </p> 
	 */
	public IDatatype getDiagnostic() {  
		return myDiagnostic;
	}

	/**
	 * Sets the value(s) for <b>diagnostic[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.
     * </p> 
	 */
	public DiagnosticReport setDiagnostic(IDatatype theValue) {
		myDiagnostic = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specimen</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about the specimens on which this diagnostic report is based
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
     * Details about the specimens on which this diagnostic report is based
     * </p> 
	 */
	public DiagnosticReport setSpecimen(java.util.List<ResourceReferenceDt> theValue) {
		mySpecimen = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details about the specimens on which this diagnostic report is based
     * </p> 
	 */
	public ResourceReferenceDt addSpecimen() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSpecimen().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>result</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\").
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getResult() {  
		if (myResult == null) {
			myResult = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myResult;
	}

	/**
	 * Sets the value(s) for <b>result</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\").
     * </p> 
	 */
	public DiagnosticReport setResult(java.util.List<ResourceReferenceDt> theValue) {
		myResult = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>result</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\").
     * </p> 
	 */
	public ResourceReferenceDt addResult() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getResult().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>imagingStudy</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getImagingStudy() {  
		if (myImagingStudy == null) {
			myImagingStudy = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myImagingStudy;
	}

	/**
	 * Sets the value(s) for <b>imagingStudy</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.
     * </p> 
	 */
	public DiagnosticReport setImagingStudy(java.util.List<ResourceReferenceDt> theValue) {
		myImagingStudy = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>imagingStudy</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.
     * </p> 
	 */
	public ResourceReferenceDt addImagingStudy() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getImagingStudy().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>image</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p> 
	 */
	public java.util.List<Image> getImage() {  
		if (myImage == null) {
			myImage = new java.util.ArrayList<Image>();
		}
		return myImage;
	}

	/**
	 * Sets the value(s) for <b>image</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p> 
	 */
	public DiagnosticReport setImage(java.util.List<Image> theValue) {
		myImage = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>image</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p> 
	 */
	public Image addImage() {
		Image newType = new Image();
		getImage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>image</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p> 
	 */
	public Image getImageFirstRep() {
		if (getImage().isEmpty()) {
			return addImage();
		}
		return getImage().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>conclusion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public StringDt getConclusionElement() {  
		if (myConclusion == null) {
			myConclusion = new StringDt();
		}
		return myConclusion;
	}

	
	/**
	 * Gets the value(s) for <b>conclusion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public String getConclusion() {  
		return getConclusionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>conclusion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public DiagnosticReport setConclusion(StringDt theValue) {
		myConclusion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>conclusion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public DiagnosticReport setConclusion( String theString) {
		myConclusion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>codedDiagnosis</b> (AdjunctDiagnosis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCodedDiagnosis() {  
		if (myCodedDiagnosis == null) {
			myCodedDiagnosis = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCodedDiagnosis;
	}

	/**
	 * Sets the value(s) for <b>codedDiagnosis</b> (AdjunctDiagnosis)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DiagnosticReport setCodedDiagnosis(java.util.List<CodeableConceptDt> theValue) {
		myCodedDiagnosis = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>codedDiagnosis</b> (AdjunctDiagnosis)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addCodedDiagnosis() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCodedDiagnosis().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>codedDiagnosis</b> (AdjunctDiagnosis),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getCodedDiagnosisFirstRep() {
		if (getCodedDiagnosis().isEmpty()) {
			return addCodedDiagnosis();
		}
		return getCodedDiagnosis().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>presentedForm</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public java.util.List<AttachmentDt> getPresentedForm() {  
		if (myPresentedForm == null) {
			myPresentedForm = new java.util.ArrayList<AttachmentDt>();
		}
		return myPresentedForm;
	}

	/**
	 * Sets the value(s) for <b>presentedForm</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public DiagnosticReport setPresentedForm(java.util.List<AttachmentDt> theValue) {
		myPresentedForm = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>presentedForm</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public AttachmentDt addPresentedForm() {
		AttachmentDt newType = new AttachmentDt();
		getPresentedForm().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>presentedForm</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public AttachmentDt getPresentedFormFirstRep() {
		if (getPresentedForm().isEmpty()) {
			return addPresentedForm();
		}
		return getPresentedForm().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DiagnosticReport.image</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p> 
	 */
	@Block()	
	public static class Image 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="comment", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features."
	)
	private StringDt myComment;
	
	@Child(name="link", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Media.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myLink;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myComment,  myLink);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myComment, myLink);
	}

	/**
	 * Gets the value(s) for <b>comment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
     * </p> 
	 */
	public StringDt getCommentElement() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}

	
	/**
	 * Gets the value(s) for <b>comment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
     * </p> 
	 */
	public String getComment() {  
		return getCommentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
     * </p> 
	 */
	public Image setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
     * </p> 
	 */
	public Image setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>link</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getLink() {  
		if (myLink == null) {
			myLink = new ResourceReferenceDt();
		}
		return myLink;
	}

	/**
	 * Sets the value(s) for <b>link</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Image setLink(ResourceReferenceDt theValue) {
		myLink = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "DiagnosticReport";
    }

}
