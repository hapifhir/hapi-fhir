















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
 * HAPI/FHIR <b>ImagingStudy</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Representation of the content produced in a DICOM imaging study. A study comprises a set of Series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A Series is of only one modality (e.g., X-ray, CT, MR, ultrasound), but a Study may have multiple Series of different modalities.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ImagingStudy">http://hl7.org/fhir/profiles/ImagingStudy</a> 
 * </p>
 *
 */
@ResourceDef(name="ImagingStudy", profile="http://hl7.org/fhir/profiles/ImagingStudy", id="imagingstudy")
public class ImagingStudy 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Who the study is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImagingStudy.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="ImagingStudy.patient", description="Who the study is about", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Who the study is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImagingStudy.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>started</b>
	 * <p>
	 * Description: <b>When the study was started</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImagingStudy.started</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="started", path="ImagingStudy.started", description="When the study was started", type="date"  )
	public static final String SP_STARTED = "started";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>started</b>
	 * <p>
	 * Description: <b>When the study was started</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImagingStudy.started</b><br/>
	 * </p>
	 */
	public static final DateClientParam STARTED = new DateClientParam(SP_STARTED);

	/**
	 * Search parameter constant for <b>accession</b>
	 * <p>
	 * Description: <b>The accession id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.accession</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="accession", path="ImagingStudy.accession", description="The accession id for the image", type="token"  )
	public static final String SP_ACCESSION = "accession";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>accession</b>
	 * <p>
	 * Description: <b>The accession id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.accession</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACCESSION = new TokenClientParam(SP_ACCESSION);

	/**
	 * Search parameter constant for <b>study</b>
	 * <p>
	 * Description: <b>The study id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.uid</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="study", path="ImagingStudy.uid", description="The study id for the image", type="token"  )
	public static final String SP_STUDY = "study";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>study</b>
	 * <p>
	 * Description: <b>The study id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.uid</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STUDY = new TokenClientParam(SP_STUDY);

	/**
	 * Search parameter constant for <b>series</b>
	 * <p>
	 * Description: <b>The series id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.uid</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="series", path="ImagingStudy.series.uid", description="The series id for the image", type="token"  )
	public static final String SP_SERIES = "series";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>series</b>
	 * <p>
	 * Description: <b>The series id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.uid</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SERIES = new TokenClientParam(SP_SERIES);

	/**
	 * Search parameter constant for <b>modality</b>
	 * <p>
	 * Description: <b>The modality of the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.modality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="modality", path="ImagingStudy.series.modality", description="The modality of the image", type="token"  )
	public static final String SP_MODALITY = "modality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>modality</b>
	 * <p>
	 * Description: <b>The modality of the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.modality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam MODALITY = new TokenClientParam(SP_MODALITY);

	/**
	 * Search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b>The size of the image in MB - may include > or < in the value</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="size", path="", description="The size of the image in MB - may include > or < in the value", type="number"  )
	public static final String SP_SIZE = "size";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b>The size of the image in MB - may include > or < in the value</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final NumberClientParam SIZE = new NumberClientParam(SP_SIZE);

	/**
	 * Search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.bodySite</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="bodysite", path="ImagingStudy.series.bodySite", description="", type="token"  )
	public static final String SP_BODYSITE = "bodysite";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.bodySite</b><br/>
	 * </p>
	 */
	public static final TokenClientParam BODYSITE = new TokenClientParam(SP_BODYSITE);

	/**
	 * Search parameter constant for <b>uid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.uid</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="uid", path="ImagingStudy.series.instance.uid", description="", type="token"  )
	public static final String SP_UID = "uid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>uid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.uid</b><br/>
	 * </p>
	 */
	public static final TokenClientParam UID = new TokenClientParam(SP_UID);

	/**
	 * Search parameter constant for <b>dicom-class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.sopclass</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dicom-class", path="ImagingStudy.series.instance.sopclass", description="", type="token"  )
	public static final String SP_DICOM_CLASS = "dicom-class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dicom-class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.sopclass</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DICOM_CLASS = new TokenClientParam(SP_DICOM_CLASS);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.accession</b>".
	 */
	public static final Include INCLUDE_ACCESSION = new Include("ImagingStudy.accession");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("ImagingStudy.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.series.bodySite</b>".
	 */
	public static final Include INCLUDE_SERIES_BODYSITE = new Include("ImagingStudy.series.bodySite");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.series.instance.sopclass</b>".
	 */
	public static final Include INCLUDE_SERIES_INSTANCE_SOPCLASS = new Include("ImagingStudy.series.instance.sopclass");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.series.instance.uid</b>".
	 */
	public static final Include INCLUDE_SERIES_INSTANCE_UID = new Include("ImagingStudy.series.instance.uid");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.series.modality</b>".
	 */
	public static final Include INCLUDE_SERIES_MODALITY = new Include("ImagingStudy.series.modality");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.series.uid</b>".
	 */
	public static final Include INCLUDE_SERIES_UID = new Include("ImagingStudy.series.uid");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.started</b>".
	 */
	public static final Include INCLUDE_STARTED = new Include("ImagingStudy.started");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.uid</b>".
	 */
	public static final Include INCLUDE_UID = new Include("ImagingStudy.uid");


	@Child(name="started", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date and Time the study started."
	)
	private DateTimeDt myStarted;
	
	@Child(name="patient", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The patient for whom the images are of"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="uid", type=OidDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Formal identifier for the study"
	)
	private OidDt myUid;
	
	@Child(name="accession", type=IdentifierDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Accession Number"
	)
	private IdentifierDt myAccession;
	
	@Child(name="identifier", type=IdentifierDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Other identifiers for the study"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="order", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.DiagnosticOrder.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A list of the diagnostic orders that resulted in this imaging study being performed"
	)
	private java.util.List<ResourceReferenceDt> myOrder;
	
	@Child(name="modalityList", type=CodeDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ImagingModality",
		formalDefinition="A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)"
	)
	private java.util.List<BoundCodeDt<ImagingModalityEnum>> myModalityList;
	
	@Child(name="referrer", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The requesting/referring physician"
	)
	private ResourceReferenceDt myReferrer;
	
	@Child(name="availability", type=CodeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="InstanceAvailability",
		formalDefinition="Availability of study (online, offline or nearline)"
	)
	private BoundCodeDt<InstanceAvailabilityEnum> myAvailability;
	
	@Child(name="url", type=UriDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="WADO-RS URI where Study is available"
	)
	private UriDt myUrl;
	
	@Child(name="numberOfSeries", type=IntegerDt.class, order=10, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Number of Series in Study"
	)
	private IntegerDt myNumberOfSeries;
	
	@Child(name="numberOfInstances", type=IntegerDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Number of SOP Instances in Study"
	)
	private IntegerDt myNumberOfInstances;
	
	@Child(name="clinicalInformation", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Diagnoses etc provided with request"
	)
	private StringDt myClinicalInformation;
	
	@Child(name="procedure", type=CodingDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Type of procedure performed"
	)
	private java.util.List<CodingDt> myProcedure;
	
	@Child(name="interpreter", order=14, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who read study and interpreted the images"
	)
	private ResourceReferenceDt myInterpreter;
	
	@Child(name="description", type=StringDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Institution-generated description or classification of the Study (component) performed"
	)
	private StringDt myDescription;
	
	@Child(name="series", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Each study has one or more series of image instances"
	)
	private java.util.List<Series> mySeries;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStarted,  myPatient,  myUid,  myAccession,  myIdentifier,  myOrder,  myModalityList,  myReferrer,  myAvailability,  myUrl,  myNumberOfSeries,  myNumberOfInstances,  myClinicalInformation,  myProcedure,  myInterpreter,  myDescription,  mySeries);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStarted, myPatient, myUid, myAccession, myIdentifier, myOrder, myModalityList, myReferrer, myAvailability, myUrl, myNumberOfSeries, myNumberOfInstances, myClinicalInformation, myProcedure, myInterpreter, myDescription, mySeries);
	}

	/**
	 * Gets the value(s) for <b>started</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study started.
     * </p> 
	 */
	public DateTimeDt getStartedElement() {  
		if (myStarted == null) {
			myStarted = new DateTimeDt();
		}
		return myStarted;
	}

	
	/**
	 * Gets the value(s) for <b>started</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study started.
     * </p> 
	 */
	public Date getStarted() {  
		return getStartedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>started</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study started.
     * </p> 
	 */
	public ImagingStudy setStarted(DateTimeDt theValue) {
		myStarted = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>started</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study started.
     * </p> 
	 */
	public ImagingStudy setStarted( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStarted = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>started</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study started.
     * </p> 
	 */
	public ImagingStudy setStartedWithSecondsPrecision( Date theDate) {
		myStarted = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient for whom the images are of
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
     * The patient for whom the images are of
     * </p> 
	 */
	public ImagingStudy setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>uid</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for the study
     * </p> 
	 */
	public OidDt getUidElement() {  
		if (myUid == null) {
			myUid = new OidDt();
		}
		return myUid;
	}

	
	/**
	 * Gets the value(s) for <b>uid</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for the study
     * </p> 
	 */
	public URI getUid() {  
		return getUidElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>uid</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for the study
     * </p> 
	 */
	public ImagingStudy setUid(OidDt theValue) {
		myUid = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>accession</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Accession Number
     * </p> 
	 */
	public IdentifierDt getAccession() {  
		if (myAccession == null) {
			myAccession = new IdentifierDt();
		}
		return myAccession;
	}

	/**
	 * Sets the value(s) for <b>accession</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Accession Number
     * </p> 
	 */
	public ImagingStudy setAccession(IdentifierDt theValue) {
		myAccession = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
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
     * Other identifiers for the study
     * </p> 
	 */
	public ImagingStudy setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
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
     * Other identifiers for the study
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>order</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the diagnostic orders that resulted in this imaging study being performed
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getOrder() {  
		if (myOrder == null) {
			myOrder = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myOrder;
	}

	/**
	 * Sets the value(s) for <b>order</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the diagnostic orders that resulted in this imaging study being performed
     * </p> 
	 */
	public ImagingStudy setOrder(java.util.List<ResourceReferenceDt> theValue) {
		myOrder = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>order</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the diagnostic orders that resulted in this imaging study being performed
     * </p> 
	 */
	public ResourceReferenceDt addOrder() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getOrder().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>modalityList</b> (ImagingModality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public java.util.List<BoundCodeDt<ImagingModalityEnum>> getModalityList() {  
		if (myModalityList == null) {
			myModalityList = new java.util.ArrayList<BoundCodeDt<ImagingModalityEnum>>();
		}
		return myModalityList;
	}

	/**
	 * Sets the value(s) for <b>modalityList</b> (ImagingModality)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public ImagingStudy setModalityList(java.util.List<BoundCodeDt<ImagingModalityEnum>> theValue) {
		myModalityList = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>modalityList</b> (ImagingModality) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public BoundCodeDt<ImagingModalityEnum> addModalityList(ImagingModalityEnum theValue) {
		BoundCodeDt<ImagingModalityEnum> retVal = new BoundCodeDt<ImagingModalityEnum>(ImagingModalityEnum.VALUESET_BINDER, theValue);
		getModalityList().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>modalityList</b> (ImagingModality),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public BoundCodeDt<ImagingModalityEnum> getModalityListFirstRep() {
		if (getModalityList().size() == 0) {
			addModalityList();
		}
		return getModalityList().get(0);
	}

	/**
	 * Add a value for <b>modalityList</b> (ImagingModality)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public BoundCodeDt<ImagingModalityEnum> addModalityList() {
		BoundCodeDt<ImagingModalityEnum> retVal = new BoundCodeDt<ImagingModalityEnum>(ImagingModalityEnum.VALUESET_BINDER);
		getModalityList().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>modalityList</b> (ImagingModality)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public ImagingStudy setModalityList(ImagingModalityEnum theValue) {
		getModalityList().clear();
		addModalityList(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>referrer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The requesting/referring physician
     * </p> 
	 */
	public ResourceReferenceDt getReferrer() {  
		if (myReferrer == null) {
			myReferrer = new ResourceReferenceDt();
		}
		return myReferrer;
	}

	/**
	 * Sets the value(s) for <b>referrer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The requesting/referring physician
     * </p> 
	 */
	public ImagingStudy setReferrer(ResourceReferenceDt theValue) {
		myReferrer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>availability</b> (InstanceAvailability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public BoundCodeDt<InstanceAvailabilityEnum> getAvailabilityElement() {  
		if (myAvailability == null) {
			myAvailability = new BoundCodeDt<InstanceAvailabilityEnum>(InstanceAvailabilityEnum.VALUESET_BINDER);
		}
		return myAvailability;
	}

	
	/**
	 * Gets the value(s) for <b>availability</b> (InstanceAvailability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public String getAvailability() {  
		return getAvailabilityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>availability</b> (InstanceAvailability)
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public ImagingStudy setAvailability(BoundCodeDt<InstanceAvailabilityEnum> theValue) {
		myAvailability = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>availability</b> (InstanceAvailability)
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public ImagingStudy setAvailability(InstanceAvailabilityEnum theValue) {
		getAvailabilityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public ImagingStudy setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public ImagingStudy setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>numberOfSeries</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public IntegerDt getNumberOfSeriesElement() {  
		if (myNumberOfSeries == null) {
			myNumberOfSeries = new IntegerDt();
		}
		return myNumberOfSeries;
	}

	
	/**
	 * Gets the value(s) for <b>numberOfSeries</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public Integer getNumberOfSeries() {  
		return getNumberOfSeriesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>numberOfSeries</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfSeries(IntegerDt theValue) {
		myNumberOfSeries = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>numberOfSeries</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfSeries( int theInteger) {
		myNumberOfSeries = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>numberOfInstances</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public IntegerDt getNumberOfInstancesElement() {  
		if (myNumberOfInstances == null) {
			myNumberOfInstances = new IntegerDt();
		}
		return myNumberOfInstances;
	}

	
	/**
	 * Gets the value(s) for <b>numberOfInstances</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public Integer getNumberOfInstances() {  
		return getNumberOfInstancesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>numberOfInstances</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfInstances(IntegerDt theValue) {
		myNumberOfInstances = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>numberOfInstances</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfInstances( int theInteger) {
		myNumberOfInstances = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>clinicalInformation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public StringDt getClinicalInformationElement() {  
		if (myClinicalInformation == null) {
			myClinicalInformation = new StringDt();
		}
		return myClinicalInformation;
	}

	
	/**
	 * Gets the value(s) for <b>clinicalInformation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public String getClinicalInformation() {  
		return getClinicalInformationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>clinicalInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public ImagingStudy setClinicalInformation(StringDt theValue) {
		myClinicalInformation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>clinicalInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public ImagingStudy setClinicalInformation( String theString) {
		myClinicalInformation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>procedure</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public java.util.List<CodingDt> getProcedure() {  
		if (myProcedure == null) {
			myProcedure = new java.util.ArrayList<CodingDt>();
		}
		return myProcedure;
	}

	/**
	 * Sets the value(s) for <b>procedure</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public ImagingStudy setProcedure(java.util.List<CodingDt> theValue) {
		myProcedure = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>procedure</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public CodingDt addProcedure() {
		CodingDt newType = new CodingDt();
		getProcedure().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>procedure</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public CodingDt getProcedureFirstRep() {
		if (getProcedure().isEmpty()) {
			return addProcedure();
		}
		return getProcedure().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>interpreter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who read study and interpreted the images
     * </p> 
	 */
	public ResourceReferenceDt getInterpreter() {  
		if (myInterpreter == null) {
			myInterpreter = new ResourceReferenceDt();
		}
		return myInterpreter;
	}

	/**
	 * Sets the value(s) for <b>interpreter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who read study and interpreted the images
     * </p> 
	 */
	public ImagingStudy setInterpreter(ResourceReferenceDt theValue) {
		myInterpreter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Institution-generated description or classification of the Study (component) performed
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
     * Institution-generated description or classification of the Study (component) performed
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
     * Institution-generated description or classification of the Study (component) performed
     * </p> 
	 */
	public ImagingStudy setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Institution-generated description or classification of the Study (component) performed
     * </p> 
	 */
	public ImagingStudy setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>series</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public java.util.List<Series> getSeries() {  
		if (mySeries == null) {
			mySeries = new java.util.ArrayList<Series>();
		}
		return mySeries;
	}

	/**
	 * Sets the value(s) for <b>series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public ImagingStudy setSeries(java.util.List<Series> theValue) {
		mySeries = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public Series addSeries() {
		Series newType = new Series();
		getSeries().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>series</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public Series getSeriesFirstRep() {
		if (getSeries().isEmpty()) {
			return addSeries();
		}
		return getSeries().get(0); 
	}
  
	/**
	 * Block class for child element: <b>ImagingStudy.series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	@Block()	
	public static class Series 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="number", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The Numeric identifier of this series in the study."
	)
	private IntegerDt myNumber;
	
	@Child(name="modality", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Modality",
		formalDefinition="The modality of this series sequence"
	)
	private BoundCodeDt<ModalityEnum> myModality;
	
	@Child(name="uid", type=OidDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Formal identifier for this series"
	)
	private OidDt myUid;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of the series"
	)
	private StringDt myDescription;
	
	@Child(name="numberOfInstances", type=IntegerDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Sequence that contains attributes from the"
	)
	private IntegerDt myNumberOfInstances;
	
	@Child(name="availability", type=CodeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="InstanceAvailability",
		formalDefinition="Availability of series (online, offline or nearline)"
	)
	private BoundCodeDt<InstanceAvailabilityEnum> myAvailability;
	
	@Child(name="url", type=UriDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="WADO-RS URI where Series is available"
	)
	private UriDt myUrl;
	
	@Child(name="bodySite", type=CodingDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="BodySite",
		formalDefinition="Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed"
	)
	private CodingDt myBodySite;
	
	@Child(name="dateTime", type=DateTimeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date when the series was started"
	)
	private DateTimeDt myDateTime;
	
	@Child(name="instance", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A single image taken from a patient"
	)
	private java.util.List<SeriesInstance> myInstance;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myNumber,  myModality,  myUid,  myDescription,  myNumberOfInstances,  myAvailability,  myUrl,  myBodySite,  myDateTime,  myInstance);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myNumber, myModality, myUid, myDescription, myNumberOfInstances, myAvailability, myUrl, myBodySite, myDateTime, myInstance);
	}

	/**
	 * Gets the value(s) for <b>number</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Numeric identifier of this series in the study.
     * </p> 
	 */
	public IntegerDt getNumberElement() {  
		if (myNumber == null) {
			myNumber = new IntegerDt();
		}
		return myNumber;
	}

	
	/**
	 * Gets the value(s) for <b>number</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Numeric identifier of this series in the study.
     * </p> 
	 */
	public Integer getNumber() {  
		return getNumberElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>number</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Numeric identifier of this series in the study.
     * </p> 
	 */
	public Series setNumber(IntegerDt theValue) {
		myNumber = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>number</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Numeric identifier of this series in the study.
     * </p> 
	 */
	public Series setNumber( int theInteger) {
		myNumber = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>modality</b> (Modality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public BoundCodeDt<ModalityEnum> getModalityElement() {  
		if (myModality == null) {
			myModality = new BoundCodeDt<ModalityEnum>(ModalityEnum.VALUESET_BINDER);
		}
		return myModality;
	}

	
	/**
	 * Gets the value(s) for <b>modality</b> (Modality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public String getModality() {  
		return getModalityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>modality</b> (Modality)
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public Series setModality(BoundCodeDt<ModalityEnum> theValue) {
		myModality = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>modality</b> (Modality)
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public Series setModality(ModalityEnum theValue) {
		getModalityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>uid</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this series
     * </p> 
	 */
	public OidDt getUidElement() {  
		if (myUid == null) {
			myUid = new OidDt();
		}
		return myUid;
	}

	
	/**
	 * Gets the value(s) for <b>uid</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this series
     * </p> 
	 */
	public URI getUid() {  
		return getUidElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>uid</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this series
     * </p> 
	 */
	public Series setUid(OidDt theValue) {
		myUid = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the series
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
     * A description of the series
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
     * A description of the series
     * </p> 
	 */
	public Series setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the series
     * </p> 
	 */
	public Series setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>numberOfInstances</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public IntegerDt getNumberOfInstancesElement() {  
		if (myNumberOfInstances == null) {
			myNumberOfInstances = new IntegerDt();
		}
		return myNumberOfInstances;
	}

	
	/**
	 * Gets the value(s) for <b>numberOfInstances</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public Integer getNumberOfInstances() {  
		return getNumberOfInstancesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>numberOfInstances</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public Series setNumberOfInstances(IntegerDt theValue) {
		myNumberOfInstances = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>numberOfInstances</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public Series setNumberOfInstances( int theInteger) {
		myNumberOfInstances = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>availability</b> (InstanceAvailability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public BoundCodeDt<InstanceAvailabilityEnum> getAvailabilityElement() {  
		if (myAvailability == null) {
			myAvailability = new BoundCodeDt<InstanceAvailabilityEnum>(InstanceAvailabilityEnum.VALUESET_BINDER);
		}
		return myAvailability;
	}

	
	/**
	 * Gets the value(s) for <b>availability</b> (InstanceAvailability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public String getAvailability() {  
		return getAvailabilityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>availability</b> (InstanceAvailability)
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public Series setAvailability(BoundCodeDt<InstanceAvailabilityEnum> theValue) {
		myAvailability = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>availability</b> (InstanceAvailability)
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public Series setAvailability(InstanceAvailabilityEnum theValue) {
		getAvailabilityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public Series setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public Series setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>bodySite</b> (BodySite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed
     * </p> 
	 */
	public CodingDt getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new CodingDt();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (BodySite)
	 *
     * <p>
     * <b>Definition:</b>
     * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed
     * </p> 
	 */
	public Series setBodySite(CodingDt theValue) {
		myBodySite = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the series was started
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
     * The date when the series was started
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
     * The date when the series was started
     * </p> 
	 */
	public Series setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the series was started
     * </p> 
	 */
	public Series setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the series was started
     * </p> 
	 */
	public Series setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>instance</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public java.util.List<SeriesInstance> getInstance() {  
		if (myInstance == null) {
			myInstance = new java.util.ArrayList<SeriesInstance>();
		}
		return myInstance;
	}

	/**
	 * Sets the value(s) for <b>instance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public Series setInstance(java.util.List<SeriesInstance> theValue) {
		myInstance = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>instance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public SeriesInstance addInstance() {
		SeriesInstance newType = new SeriesInstance();
		getInstance().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>instance</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public SeriesInstance getInstanceFirstRep() {
		if (getInstance().isEmpty()) {
			return addInstance();
		}
		return getInstance().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>ImagingStudy.series.instance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	@Block()	
	public static class SeriesInstance 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="number", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The number of this image in the series"
	)
	private IntegerDt myNumber;
	
	@Child(name="uid", type=OidDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Formal identifier for this image"
	)
	private OidDt myUid;
	
	@Child(name="sopclass", type=OidDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="DICOM Image type"
	)
	private OidDt mySopclass;
	
	@Child(name="type", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The type of the instance"
	)
	private StringDt myType;
	
	@Child(name="title", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The description of the instance"
	)
	private StringDt myTitle;
	
	@Child(name="url", type=UriDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="WADO-RS url where image is available"
	)
	private UriDt myUrl;
	
	@Child(name="attachment", order=6, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A FHIR resource with content for this instance"
	)
	private ResourceReferenceDt myAttachment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myNumber,  myUid,  mySopclass,  myType,  myTitle,  myUrl,  myAttachment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myNumber, myUid, mySopclass, myType, myTitle, myUrl, myAttachment);
	}

	/**
	 * Gets the value(s) for <b>number</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public IntegerDt getNumberElement() {  
		if (myNumber == null) {
			myNumber = new IntegerDt();
		}
		return myNumber;
	}

	
	/**
	 * Gets the value(s) for <b>number</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public Integer getNumber() {  
		return getNumberElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>number</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public SeriesInstance setNumber(IntegerDt theValue) {
		myNumber = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>number</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public SeriesInstance setNumber( int theInteger) {
		myNumber = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>uid</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this image
     * </p> 
	 */
	public OidDt getUidElement() {  
		if (myUid == null) {
			myUid = new OidDt();
		}
		return myUid;
	}

	
	/**
	 * Gets the value(s) for <b>uid</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this image
     * </p> 
	 */
	public URI getUid() {  
		return getUidElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>uid</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this image
     * </p> 
	 */
	public SeriesInstance setUid(OidDt theValue) {
		myUid = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>sopclass</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DICOM Image type
     * </p> 
	 */
	public OidDt getSopclassElement() {  
		if (mySopclass == null) {
			mySopclass = new OidDt();
		}
		return mySopclass;
	}

	
	/**
	 * Gets the value(s) for <b>sopclass</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DICOM Image type
     * </p> 
	 */
	public URI getSopclass() {  
		return getSopclassElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>sopclass</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DICOM Image type
     * </p> 
	 */
	public SeriesInstance setSopclass(OidDt theValue) {
		mySopclass = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type </b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the instance
     * </p> 
	 */
	public StringDt getTypeElement() {  
		if (myType == null) {
			myType = new StringDt();
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type </b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the instance
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type </b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the instance
     * </p> 
	 */
	public SeriesInstance setType(StringDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type </b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the instance
     * </p> 
	 */
	public SeriesInstance setType( String theString) {
		myType = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The description of the instance
     * </p> 
	 */
	public StringDt getTitleElement() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	
	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The description of the instance
     * </p> 
	 */
	public String getTitle() {  
		return getTitleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The description of the instance
     * </p> 
	 */
	public SeriesInstance setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The description of the instance
     * </p> 
	 */
	public SeriesInstance setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public SeriesInstance setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public SeriesInstance setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>attachment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A FHIR resource with content for this instance
     * </p> 
	 */
	public ResourceReferenceDt getAttachment() {  
		if (myAttachment == null) {
			myAttachment = new ResourceReferenceDt();
		}
		return myAttachment;
	}

	/**
	 * Sets the value(s) for <b>attachment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A FHIR resource with content for this instance
     * </p> 
	 */
	public SeriesInstance setAttachment(ResourceReferenceDt theValue) {
		myAttachment = theValue;
		return this;
	}
	
	

  

	}





    @Override
    public String getResourceName() {
        return "ImagingStudy";
    }

}
