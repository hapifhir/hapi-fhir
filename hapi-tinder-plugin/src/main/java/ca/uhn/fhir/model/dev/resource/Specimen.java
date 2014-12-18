















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
 * HAPI/FHIR <b>Specimen</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Sample for analysis
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Specimen">http://hl7.org/fhir/profiles/Specimen</a> 
 * </p>
 *
 */
@ResourceDef(name="Specimen", profile="http://hl7.org/fhir/profiles/Specimen", id="specimen")
public class Specimen 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Specimen.subject", description="The subject of the specimen", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient the specimen comes from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Specimen.subject", description="The patient the specimen comes from", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient the specimen comes from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The unique identifier associated with the specimen</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Specimen.identifier", description="The unique identifier associated with the specimen", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The unique identifier associated with the specimen</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The specimen type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Specimen.type", description="The specimen type", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The specimen type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b>The parent of the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.source.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="Specimen.source.target", description="The parent of the specimen", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b>The parent of the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.source.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>accession</b>
	 * <p>
	 * Description: <b>The accession number associated with the specimen</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.accessionIdentifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="accession", path="Specimen.accessionIdentifier", description="The accession number associated with the specimen", type="token"  )
	public static final String SP_ACCESSION = "accession";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>accession</b>
	 * <p>
	 * Description: <b>The accession number associated with the specimen</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.accessionIdentifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACCESSION = new TokenClientParam(SP_ACCESSION);

	/**
	 * Search parameter constant for <b>collected</b>
	 * <p>
	 * Description: <b>The date the specimen was collected</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Specimen.collection.collected[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="collected", path="Specimen.collection.collected[x]", description="The date the specimen was collected", type="date"  )
	public static final String SP_COLLECTED = "collected";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>collected</b>
	 * <p>
	 * Description: <b>The date the specimen was collected</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Specimen.collection.collected[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam COLLECTED = new DateClientParam(SP_COLLECTED);

	/**
	 * Search parameter constant for <b>collector</b>
	 * <p>
	 * Description: <b>Who collected the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.collection.collector</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="collector", path="Specimen.collection.collector", description="Who collected the specimen", type="reference"  )
	public static final String SP_COLLECTOR = "collector";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>collector</b>
	 * <p>
	 * Description: <b>Who collected the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.collection.collector</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam COLLECTOR = new ReferenceClientParam(SP_COLLECTOR);

	/**
	 * Search parameter constant for <b>site</b>
	 * <p>
	 * Description: <b>The source or body site from where the specimen came</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.collection.sourceSite</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="site", path="Specimen.collection.sourceSite", description="The source or body site from where the specimen came", type="token"  )
	public static final String SP_SITE = "site";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>site</b>
	 * <p>
	 * Description: <b>The source or body site from where the specimen came</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.collection.sourceSite</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SITE = new TokenClientParam(SP_SITE);

	/**
	 * Search parameter constant for <b>containerid</b>
	 * <p>
	 * Description: <b>The unique identifier associated with the specimen container</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.container.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="containerid", path="Specimen.container.identifier", description="The unique identifier associated with the specimen container", type="token"  )
	public static final String SP_CONTAINERID = "containerid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>containerid</b>
	 * <p>
	 * Description: <b>The unique identifier associated with the specimen container</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.container.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTAINERID = new TokenClientParam(SP_CONTAINERID);

	/**
	 * Search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b>The kind of specimen container</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.container.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="container", path="Specimen.container.type", description="The kind of specimen container", type="token"  )
	public static final String SP_CONTAINER = "container";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b>The kind of specimen container</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Specimen.container.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTAINER = new TokenClientParam(SP_CONTAINER);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.accessionIdentifier</b>".
	 */
	public static final Include INCLUDE_ACCESSIONIDENTIFIER = new Include("Specimen.accessionIdentifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.collection.collected[x]</b>".
	 */
	public static final Include INCLUDE_COLLECTION_COLLECTED = new Include("Specimen.collection.collected[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.collection.collector</b>".
	 */
	public static final Include INCLUDE_COLLECTION_COLLECTOR = new Include("Specimen.collection.collector");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.collection.sourceSite</b>".
	 */
	public static final Include INCLUDE_COLLECTION_SOURCESITE = new Include("Specimen.collection.sourceSite");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.container.identifier</b>".
	 */
	public static final Include INCLUDE_CONTAINER_IDENTIFIER = new Include("Specimen.container.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.container.type</b>".
	 */
	public static final Include INCLUDE_CONTAINER_TYPE = new Include("Specimen.container.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Specimen.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.source.target</b>".
	 */
	public static final Include INCLUDE_SOURCE_TARGET = new Include("Specimen.source.target");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Specimen.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Specimen.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Specimen.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Id for specimen"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="SpecimenType",
		formalDefinition="Kind of material that forms the specimen"
	)
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Parent specimen from which the focal specimen was a component"
	)
	private java.util.List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Substance.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures."
	)
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Time when specimen was received for processing or testing"
	)
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Details concerning the specimen collection"
	)
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Details concerning treatment and processing steps for the specimen"
	)
	private java.util.List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here."
	)
	private java.util.List<Container> myContainer;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  mySource,  mySubject,  myAccessionIdentifier,  myReceivedTime,  myCollection,  myTreatment,  myContainer);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, mySource, mySubject, myAccessionIdentifier, myReceivedTime, myCollection, myTreatment, myContainer);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
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
     * Id for specimen
     * </p> 
	 */
	public Specimen setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
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
     * Id for specimen
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> (SpecimenType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (SpecimenType)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public Specimen setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public java.util.List<Source> getSource() {  
		if (mySource == null) {
			mySource = new java.util.ArrayList<Source>();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public Specimen setSource(java.util.List<Source> theValue) {
		mySource = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public Source addSource() {
		Source newType = new Source();
		getSource().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>source</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public Source getSourceFirstRep() {
		if (getSource().isEmpty()) {
			return addSource();
		}
		return getSource().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public Specimen setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>accessionIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {  
		if (myAccessionIdentifier == null) {
			myAccessionIdentifier = new IdentifierDt();
		}
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for <b>accessionIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     * </p> 
	 */
	public Specimen setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>receivedTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTimeElement() {  
		if (myReceivedTime == null) {
			myReceivedTime = new DateTimeDt();
		}
		return myReceivedTime;
	}

	
	/**
	 * Gets the value(s) for <b>receivedTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Date getReceivedTime() {  
		return getReceivedTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>receivedTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Specimen setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>receivedTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Specimen setReceivedTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myReceivedTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>receivedTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Specimen setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>collection</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public Collection getCollection() {  
		if (myCollection == null) {
			myCollection = new Collection();
		}
		return myCollection;
	}

	/**
	 * Sets the value(s) for <b>collection</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public Specimen setCollection(Collection theValue) {
		myCollection = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>treatment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public java.util.List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new java.util.ArrayList<Treatment>();
		}
		return myTreatment;
	}

	/**
	 * Sets the value(s) for <b>treatment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public Specimen setTreatment(java.util.List<Treatment> theValue) {
		myTreatment = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>treatment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public Treatment addTreatment() {
		Treatment newType = new Treatment();
		getTreatment().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>treatment</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public Treatment getTreatmentFirstRep() {
		if (getTreatment().isEmpty()) {
			return addTreatment();
		}
		return getTreatment().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>container</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public java.util.List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new java.util.ArrayList<Container>();
		}
		return myContainer;
	}

	/**
	 * Sets the value(s) for <b>container</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public Specimen setContainer(java.util.List<Container> theValue) {
		myContainer = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>container</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public Container addContainer() {
		Container newType = new Container();
		getContainer().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>container</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public Container getContainerFirstRep() {
		if (getContainer().isEmpty()) {
			return addContainer();
		}
		return getContainer().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Specimen.source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	@Block()	
	public static class Source 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="relationship", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="HierarchicalRelationshipType",
		formalDefinition="Whether this relationship is to a parent or to a child"
	)
	private BoundCodeDt<HierarchicalRelationshipTypeEnum> myRelationship;
	
	@Child(name="target", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Specimen.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The specimen resource that is the target of this relationship"
	)
	private java.util.List<ResourceReferenceDt> myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRelationship,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRelationship, myTarget);
	}

	/**
	 * Gets the value(s) for <b>relationship</b> (HierarchicalRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public BoundCodeDt<HierarchicalRelationshipTypeEnum> getRelationshipElement() {  
		if (myRelationship == null) {
			myRelationship = new BoundCodeDt<HierarchicalRelationshipTypeEnum>(HierarchicalRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myRelationship;
	}

	
	/**
	 * Gets the value(s) for <b>relationship</b> (HierarchicalRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public String getRelationship() {  
		return getRelationshipElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (HierarchicalRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public Source setRelationship(BoundCodeDt<HierarchicalRelationshipTypeEnum> theValue) {
		myRelationship = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>relationship</b> (HierarchicalRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public Source setRelationship(HierarchicalRelationshipTypeEnum theValue) {
		getRelationshipElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen resource that is the target of this relationship
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getTarget() {  
		if (myTarget == null) {
			myTarget = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen resource that is the target of this relationship
     * </p> 
	 */
	public Source setTarget(java.util.List<ResourceReferenceDt> theValue) {
		myTarget = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen resource that is the target of this relationship
     * </p> 
	 */
	public ResourceReferenceDt addTarget() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getTarget().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Specimen.collection</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	@Block()	
	public static class Collection 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="collector", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Person who collected the specimen"
	)
	private ResourceReferenceDt myCollector;
	
	@Child(name="comment", type=StringDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="To communicate any details or issues encountered during the specimen collection procedure."
	)
	private java.util.List<StringDt> myComment;
	
	@Child(name="collected", order=2, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Time when specimen was collected from subject - the physiologically relevant time"
	)
	private IDatatype myCollected;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample."
	)
	private QuantityDt myQuantity;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="SpecimenCollectionMethod",
		formalDefinition="A coded value specifying the technique that is used to perform the procedure"
	)
	private BoundCodeableConceptDt<SpecimenCollectionMethodEnum> myMethod;
	
	@Child(name="sourceSite", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="BodySite",
		formalDefinition="Anatomical location from which the specimen was collected (if subject is a patient). This element is not used for environmental specimens."
	)
	private CodeableConceptDt mySourceSite;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCollector,  myComment,  myCollected,  myQuantity,  myMethod,  mySourceSite);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCollector, myComment, myCollected, myQuantity, myMethod, mySourceSite);
	}

	/**
	 * Gets the value(s) for <b>collector</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Person who collected the specimen
     * </p> 
	 */
	public ResourceReferenceDt getCollector() {  
		if (myCollector == null) {
			myCollector = new ResourceReferenceDt();
		}
		return myCollector;
	}

	/**
	 * Sets the value(s) for <b>collector</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Person who collected the specimen
     * </p> 
	 */
	public Collection setCollector(ResourceReferenceDt theValue) {
		myCollector = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>comment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public java.util.List<StringDt> getComment() {  
		if (myComment == null) {
			myComment = new java.util.ArrayList<StringDt>();
		}
		return myComment;
	}

	/**
	 * Sets the value(s) for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public Collection setComment(java.util.List<StringDt> theValue) {
		myComment = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public StringDt addComment() {
		StringDt newType = new StringDt();
		getComment().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>comment</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public StringDt getCommentFirstRep() {
		if (getComment().isEmpty()) {
			return addComment();
		}
		return getComment().get(0); 
	}
 	/**
	 * Adds a new value for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Collection addComment( String theString) {
		if (myComment == null) {
			myComment = new java.util.ArrayList<StringDt>();
		}
		myComment.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>collected[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was collected from subject - the physiologically relevant time
     * </p> 
	 */
	public IDatatype getCollected() {  
		return myCollected;
	}

	/**
	 * Sets the value(s) for <b>collected[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was collected from subject - the physiologically relevant time
     * </p> 
	 */
	public Collection setCollected(IDatatype theValue) {
		myCollected = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.
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
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.
     * </p> 
	 */
	public Collection setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>method</b> (SpecimenCollectionMethod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the technique that is used to perform the procedure
     * </p> 
	 */
	public BoundCodeableConceptDt<SpecimenCollectionMethodEnum> getMethod() {  
		if (myMethod == null) {
			myMethod = new BoundCodeableConceptDt<SpecimenCollectionMethodEnum>(SpecimenCollectionMethodEnum.VALUESET_BINDER);
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (SpecimenCollectionMethod)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the technique that is used to perform the procedure
     * </p> 
	 */
	public Collection setMethod(BoundCodeableConceptDt<SpecimenCollectionMethodEnum> theValue) {
		myMethod = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>method</b> (SpecimenCollectionMethod)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the technique that is used to perform the procedure
     * </p> 
	 */
	public Collection setMethod(SpecimenCollectionMethodEnum theValue) {
		getMethod().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sourceSite</b> (BodySite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location from which the specimen was collected (if subject is a patient). This element is not used for environmental specimens.
     * </p> 
	 */
	public CodeableConceptDt getSourceSite() {  
		if (mySourceSite == null) {
			mySourceSite = new CodeableConceptDt();
		}
		return mySourceSite;
	}

	/**
	 * Sets the value(s) for <b>sourceSite</b> (BodySite)
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location from which the specimen was collected (if subject is a patient). This element is not used for environmental specimens.
     * </p> 
	 */
	public Collection setSourceSite(CodeableConceptDt theValue) {
		mySourceSite = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Specimen.treatment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	@Block()	
	public static class Treatment 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myDescription;
	
	@Child(name="procedure", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="SpecimenTreatmentProcedure",
		formalDefinition="A coded value specifying the procedure used to process the specimen"
	)
	private BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum> myProcedure;
	
	@Child(name="additive", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Substance.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myAdditive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDescription,  myProcedure,  myAdditive);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDescription, myProcedure, myAdditive);
	}

	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
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
     * 
     * </p> 
	 */
	public Treatment setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Treatment setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>procedure</b> (SpecimenTreatmentProcedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the procedure used to process the specimen
     * </p> 
	 */
	public BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum> getProcedure() {  
		if (myProcedure == null) {
			myProcedure = new BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum>(SpecimenTreatmentProcedureEnum.VALUESET_BINDER);
		}
		return myProcedure;
	}

	/**
	 * Sets the value(s) for <b>procedure</b> (SpecimenTreatmentProcedure)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the procedure used to process the specimen
     * </p> 
	 */
	public Treatment setProcedure(BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum> theValue) {
		myProcedure = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>procedure</b> (SpecimenTreatmentProcedure)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the procedure used to process the specimen
     * </p> 
	 */
	public Treatment setProcedure(SpecimenTreatmentProcedureEnum theValue) {
		getProcedure().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>additive</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAdditive() {  
		if (myAdditive == null) {
			myAdditive = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAdditive;
	}

	/**
	 * Sets the value(s) for <b>additive</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Treatment setAdditive(java.util.List<ResourceReferenceDt> theValue) {
		myAdditive = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>additive</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addAdditive() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAdditive().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Specimen.container</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	@Block()	
	public static class Container 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myDescription;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SpecimenContainerType",
		formalDefinition="The type of container associated with the specimen (e.g. slide, aliquot, etc)"
	)
	private CodeableConceptDt myType;
	
	@Child(name="capacity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The capacity (volume or other measure) the container may contain."
	)
	private QuantityDt myCapacity;
	
	@Child(name="specimenQuantity", type=QuantityDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type."
	)
	private QuantityDt mySpecimenQuantity;
	
	@Child(name="additive", order=5, min=0, max=1, type={
		CodeableConceptDt.class, 		Substance.class	})
	@Description(
		shortDefinition="SpecimenContainerAdditive",
		formalDefinition="Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA"
	)
	private IDatatype myAdditive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDescription,  myType,  myCapacity,  mySpecimenQuantity,  myAdditive);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDescription, myType, myCapacity, mySpecimenQuantity, myAdditive);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.
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
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.
     * </p> 
	 */
	public Container setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.
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
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
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
     * 
     * </p> 
	 */
	public Container setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Container setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (SpecimenContainerType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of container associated with the specimen (e.g. slide, aliquot, etc)
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (SpecimenContainerType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of container associated with the specimen (e.g. slide, aliquot, etc)
     * </p> 
	 */
	public Container setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>capacity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public QuantityDt getCapacity() {  
		if (myCapacity == null) {
			myCapacity = new QuantityDt();
		}
		return myCapacity;
	}

	/**
	 * Sets the value(s) for <b>capacity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public Container setCapacity(QuantityDt theValue) {
		myCapacity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specimenQuantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.
     * </p> 
	 */
	public QuantityDt getSpecimenQuantity() {  
		if (mySpecimenQuantity == null) {
			mySpecimenQuantity = new QuantityDt();
		}
		return mySpecimenQuantity;
	}

	/**
	 * Sets the value(s) for <b>specimenQuantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.
     * </p> 
	 */
	public Container setSpecimenQuantity(QuantityDt theValue) {
		mySpecimenQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>additive[x]</b> (SpecimenContainerAdditive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA
     * </p> 
	 */
	public IDatatype getAdditive() {  
		return myAdditive;
	}

	/**
	 * Sets the value(s) for <b>additive[x]</b> (SpecimenContainerAdditive)
	 *
     * <p>
     * <b>Definition:</b>
     * Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA
     * </p> 
	 */
	public Container setAdditive(IDatatype theValue) {
		myAdditive = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Specimen";
    }

}
