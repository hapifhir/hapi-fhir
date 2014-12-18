















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
 * HAPI/FHIR <b>DocumentManifest</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A manifest that defines a set of documents
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DocumentManifest">http://hl7.org/fhir/profiles/DocumentManifest</a> 
 * </p>
 *
 */
@ResourceDef(name="DocumentManifest", profile="http://hl7.org/fhir/profiles/DocumentManifest", id="documentmanifest")
public class DocumentManifest 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.masterIdentifier | DocumentManifest.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DocumentManifest.masterIdentifier | DocumentManifest.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.masterIdentifier | DocumentManifest.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DocumentManifest.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="DocumentManifest.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.recipient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="recipient", path="DocumentManifest.recipient", description="", type="reference"  )
	public static final String SP_RECIPIENT = "recipient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.recipient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RECIPIENT = new ReferenceClientParam(SP_RECIPIENT);

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="DocumentManifest.author", description="", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentManifest.created</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="created", path="DocumentManifest.created", description="", type="date"  )
	public static final String SP_CREATED = "created";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentManifest.created</b><br/>
	 * </p>
	 */
	public static final DateClientParam CREATED = new DateClientParam(SP_CREATED);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DocumentManifest.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>supersedes</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.supercedes</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supersedes", path="DocumentManifest.supercedes", description="", type="reference"  )
	public static final String SP_SUPERSEDES = "supersedes";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>supersedes</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.supercedes</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUPERSEDES = new ReferenceClientParam(SP_SUPERSEDES);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentManifest.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="DocumentManifest.description", description="", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentManifest.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.confidentiality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="confidentiality", path="DocumentManifest.confidentiality", description="", type="token"  )
	public static final String SP_CONFIDENTIALITY = "confidentiality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.confidentiality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONFIDENTIALITY = new TokenClientParam(SP_CONFIDENTIALITY);

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.content</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="content", path="DocumentManifest.content", description="", type="reference"  )
	public static final String SP_CONTENT = "content";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.content</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONTENT = new ReferenceClientParam(SP_CONTENT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="DocumentManifest.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("DocumentManifest.author");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.confidentiality</b>".
	 */
	public static final Include INCLUDE_CONFIDENTIALITY = new Include("DocumentManifest.confidentiality");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.content</b>".
	 */
	public static final Include INCLUDE_CONTENT = new Include("DocumentManifest.content");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.created</b>".
	 */
	public static final Include INCLUDE_CREATED = new Include("DocumentManifest.created");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.description</b>".
	 */
	public static final Include INCLUDE_DESCRIPTION = new Include("DocumentManifest.description");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("DocumentManifest.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.masterIdentifier</b>".
	 */
	public static final Include INCLUDE_MASTERIDENTIFIER = new Include("DocumentManifest.masterIdentifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.recipient</b>".
	 */
	public static final Include INCLUDE_RECIPIENT = new Include("DocumentManifest.recipient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("DocumentManifest.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DocumentManifest.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.supercedes</b>".
	 */
	public static final Include INCLUDE_SUPERCEDES = new Include("DocumentManifest.supercedes");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("DocumentManifest.type");


	@Child(name="masterIdentifier", type=IdentifierDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts"
	)
	private IdentifierDt myMasterIdentifier;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=2, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)"
	)
	private java.util.List<ResourceReferenceDt> mySubject;
	
	@Child(name="recipient", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A patient, practitioner, or organization for which this set of documents is intended"
	)
	private java.util.List<ResourceReferenceDt> myRecipient;
	
	@Child(name="type", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="DocumentSetType",
		formalDefinition="Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider"
	)
	private CodeableConceptDt myType;
	
	@Child(name="author", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies who is responsible for adding the information to the document"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="created", type=DateTimeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)"
	)
	private DateTimeDt myCreated;
	
	@Child(name="source", type=UriDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the source system, application, or software that produced the document manifest"
	)
	private UriDt mySource;
	
	@Child(name="status", type=CodeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="DocumentReferenceStatus",
		formalDefinition="The status of this document manifest"
	)
	private BoundCodeDt<DocumentReferenceStatusEnum> myStatus;
	
	@Child(name="supercedes", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.DocumentManifest.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Whether this document manifest replaces another"
	)
	private ResourceReferenceDt mySupercedes;
	
	@Child(name="description", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\""
	)
	private StringDt myDescription;
	
	@Child(name="confidentiality", type=CodeableConceptDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="DocumentConfidentiality",
		formalDefinition="A code specifying the level of confidentiality of this set of Documents"
	)
	private CodeableConceptDt myConfidentiality;
	
	@Child(name="content", order=12, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.DocumentReference.class, 		ca.uhn.fhir.model.dstu.resource.Binary.class, 		ca.uhn.fhir.model.dev.resource.Media.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed"
	)
	private java.util.List<ResourceReferenceDt> myContent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMasterIdentifier,  myIdentifier,  mySubject,  myRecipient,  myType,  myAuthor,  myCreated,  mySource,  myStatus,  mySupercedes,  myDescription,  myConfidentiality,  myContent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMasterIdentifier, myIdentifier, mySubject, myRecipient, myType, myAuthor, myCreated, mySource, myStatus, mySupercedes, myDescription, myConfidentiality, myContent);
	}

	/**
	 * Gets the value(s) for <b>masterIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public IdentifierDt getMasterIdentifier() {  
		if (myMasterIdentifier == null) {
			myMasterIdentifier = new IdentifierDt();
		}
		return myMasterIdentifier;
	}

	/**
	 * Sets the value(s) for <b>masterIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier(IdentifierDt theValue) {
		myMasterIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
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
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public DocumentManifest setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
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
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
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
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSubject() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public DocumentManifest setSubject(java.util.List<ResourceReferenceDt> theValue) {
		mySubject = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public ResourceReferenceDt addSubject() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSubject().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>recipient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRecipient() {  
		if (myRecipient == null) {
			myRecipient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRecipient;
	}

	/**
	 * Sets the value(s) for <b>recipient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public DocumentManifest setRecipient(java.util.List<ResourceReferenceDt> theValue) {
		myRecipient = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>recipient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public ResourceReferenceDt addRecipient() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getRecipient().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> (DocumentSetType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (DocumentSetType)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public DocumentManifest setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public DocumentManifest setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>created</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DateTimeDt getCreatedElement() {  
		if (myCreated == null) {
			myCreated = new DateTimeDt();
		}
		return myCreated;
	}

	
	/**
	 * Gets the value(s) for <b>created</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public Date getCreated() {  
		return getCreatedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>created</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreated(DateTimeDt theValue) {
		myCreated = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>created</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreated( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myCreated = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>created</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreatedWithSecondsPrecision( Date theDate) {
		myCreated = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public UriDt getSourceElement() {  
		if (mySource == null) {
			mySource = new UriDt();
		}
		return mySource;
	}

	
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public URI getSource() {  
		return getSourceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public DocumentManifest setSource(UriDt theValue) {
		mySource = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public DocumentManifest setSource( String theUri) {
		mySource = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (DocumentReferenceStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public BoundCodeDt<DocumentReferenceStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DocumentReferenceStatusEnum>(DocumentReferenceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (DocumentReferenceStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (DocumentReferenceStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public DocumentManifest setStatus(BoundCodeDt<DocumentReferenceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (DocumentReferenceStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public DocumentManifest setStatus(DocumentReferenceStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>supercedes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public ResourceReferenceDt getSupercedes() {  
		if (mySupercedes == null) {
			mySupercedes = new ResourceReferenceDt();
		}
		return mySupercedes;
	}

	/**
	 * Sets the value(s) for <b>supercedes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public DocumentManifest setSupercedes(ResourceReferenceDt theValue) {
		mySupercedes = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
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
     * Human-readable description of the source document. This is sometimes known as the \"title\"
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
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentManifest setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentManifest setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>confidentiality</b> (DocumentConfidentiality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public CodeableConceptDt getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new CodeableConceptDt();
		}
		return myConfidentiality;
	}

	/**
	 * Sets the value(s) for <b>confidentiality</b> (DocumentConfidentiality)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public DocumentManifest setConfidentiality(CodeableConceptDt theValue) {
		myConfidentiality = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>content</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getContent() {  
		if (myContent == null) {
			myContent = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myContent;
	}

	/**
	 * Sets the value(s) for <b>content</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public DocumentManifest setContent(java.util.List<ResourceReferenceDt> theValue) {
		myContent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>content</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public ResourceReferenceDt addContent() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getContent().add(newType);
		return newType; 
	}
  


    @Override
    public String getResourceName() {
        return "DocumentManifest";
    }

}
