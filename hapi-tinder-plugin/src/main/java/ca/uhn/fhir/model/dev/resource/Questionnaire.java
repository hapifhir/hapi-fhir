















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
 * HAPI/FHIR <b>Questionnaire</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support structured, hierarchical registration of data gathered using digital forms and other questionnaires.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Questionnaire">http://hl7.org/fhir/profiles/Questionnaire</a> 
 * </p>
 *
 */
@ResourceDef(name="Questionnaire", profile="http://hl7.org/fhir/profiles/Questionnaire", id="questionnaire")
public class Questionnaire 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Questionnaire.status", description="The status of the questionnaire", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>When the questionnaire was last changed</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Questionnaire.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Questionnaire.date", description="When the questionnaire was last changed", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>When the questionnaire was last changed</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Questionnaire.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>The author of the questionnaire</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Questionnaire.publisher</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="Questionnaire.publisher", description="The author of the questionnaire", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>The author of the questionnaire</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Questionnaire.publisher</b><br/>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Questionnaire.identifier", description="An identifier for the questionnaire", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The business version of the questionnaire</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Questionnaire.version</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="Questionnaire.version", description="The business version of the questionnaire", type="string"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The business version of the questionnaire</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Questionnaire.version</b><br/>
	 * </p>
	 */
	public static final StringClientParam VERSION = new StringClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code that corresponds to the questionnaire or one of its groups</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.group.concept</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Questionnaire.group.concept", description="A code that corresponds to the questionnaire or one of its groups", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code that corresponds to the questionnaire or one of its groups</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.group.concept</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>title</b>
	 * <p>
	 * Description: <b>All or part of the name of the questionnaire (title for the root group of the questionnaire)</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="title", path="", description="All or part of the name of the questionnaire (title for the root group of the questionnaire)", type="string"  )
	public static final String SP_TITLE = "title";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>title</b>
	 * <p>
	 * Description: <b>All or part of the name of the questionnaire (title for the root group of the questionnaire)</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final StringClientParam TITLE = new StringClientParam(SP_TITLE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Questionnaire.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.group.concept</b>".
	 */
	public static final Include INCLUDE_GROUP_CONCEPT = new Include("Questionnaire.group.concept");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Questionnaire.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("Questionnaire.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Questionnaire.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("Questionnaire.version");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated"
	)
	private StringDt myVersion;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="QuestionnaireStatus",
		formalDefinition="The lifecycle status of the questionnaire as a whole."
	)
	private BoundCodeDt<QuestionnaireStatusEnum> myStatus;
	
	@Child(name="date", type=DateTimeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date that this questionnaire was last changed"
	)
	private DateTimeDt myDate;
	
	@Child(name="publisher", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Organization responsible for developing and maintaining the questionnaire"
	)
	private StringDt myPublisher;
	
	@Child(name="group", order=5, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A collection of related questions (or further groupings of questions)"
	)
	private Group myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myStatus,  myDate,  myPublisher,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myStatus, myDate, myPublisher, myGroup);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Questionnaire setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated
     * </p> 
	 */
	public Questionnaire setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated
     * </p> 
	 */
	public Questionnaire setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (QuestionnaireStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public BoundCodeDt<QuestionnaireStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<QuestionnaireStatusEnum>(QuestionnaireStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (QuestionnaireStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (QuestionnaireStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public Questionnaire setStatus(BoundCodeDt<QuestionnaireStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (QuestionnaireStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public Questionnaire setStatus(QuestionnaireStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this questionnaire was last changed
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
     * The date that this questionnaire was last changed
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
     * The date that this questionnaire was last changed
     * </p> 
	 */
	public Questionnaire setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this questionnaire was last changed
     * </p> 
	 */
	public Questionnaire setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this questionnaire was last changed
     * </p> 
	 */
	public Questionnaire setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization responsible for developing and maintaining the questionnaire
     * </p> 
	 */
	public StringDt getPublisherElement() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	
	/**
	 * Gets the value(s) for <b>publisher</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization responsible for developing and maintaining the questionnaire
     * </p> 
	 */
	public String getPublisher() {  
		return getPublisherElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>publisher</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Organization responsible for developing and maintaining the questionnaire
     * </p> 
	 */
	public Questionnaire setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Organization responsible for developing and maintaining the questionnaire
     * </p> 
	 */
	public Questionnaire setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A collection of related questions (or further groupings of questions)
     * </p> 
	 */
	public Group getGroup() {  
		if (myGroup == null) {
			myGroup = new Group();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A collection of related questions (or further groupings of questions)
     * </p> 
	 */
	public Questionnaire setGroup(Group theValue) {
		myGroup = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>Questionnaire.group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A collection of related questions (or further groupings of questions)
     * </p> 
	 */
	@Block()	
	public static class Group 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="linkId", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireAnswers resource."
	)
	private StringDt myLinkId;
	
	@Child(name="title", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The human-readable name for this section of the questionnaire"
	)
	private StringDt myTitle;
	
	@Child(name="concept", type=CodingDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="QuestionnaireGroupConcept",
		formalDefinition="Identifies a how this group of questions is known in a particular terminology such as LOINC."
	)
	private java.util.List<CodingDt> myConcept;
	
	@Child(name="text", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional text for the group, used for display purposes"
	)
	private StringDt myText;
	
	@Child(name="required", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire."
	)
	private BooleanDt myRequired;
	
	@Child(name="repeats", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether the group may occur multiple times in the instance, containing multiple sets of answers"
	)
	private BooleanDt myRepeats;
	
	@Child(name="group", type=Group.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A sub-group within a group. The ordering of groups within this group is relevant"
	)
	private java.util.List<Group> myGroup;
	
	@Child(name="question", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Set of questions within this group. The order of questions within the group is relevant"
	)
	private java.util.List<GroupQuestion> myQuestion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLinkId,  myTitle,  myConcept,  myText,  myRequired,  myRepeats,  myGroup,  myQuestion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLinkId, myTitle, myConcept, myText, myRequired, myRepeats, myGroup, myQuestion);
	}

	/**
	 * Gets the value(s) for <b>linkId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireAnswers resource.
     * </p> 
	 */
	public StringDt getLinkIdElement() {  
		if (myLinkId == null) {
			myLinkId = new StringDt();
		}
		return myLinkId;
	}

	
	/**
	 * Gets the value(s) for <b>linkId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireAnswers resource.
     * </p> 
	 */
	public String getLinkId() {  
		return getLinkIdElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>linkId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireAnswers resource.
     * </p> 
	 */
	public Group setLinkId(StringDt theValue) {
		myLinkId = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>linkId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireAnswers resource.
     * </p> 
	 */
	public Group setLinkId( String theString) {
		myLinkId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The human-readable name for this section of the questionnaire
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
     * The human-readable name for this section of the questionnaire
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
     * The human-readable name for this section of the questionnaire
     * </p> 
	 */
	public Group setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The human-readable name for this section of the questionnaire
     * </p> 
	 */
	public Group setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> (QuestionnaireGroupConcept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this group of questions is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public java.util.List<CodingDt> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<CodingDt>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> (QuestionnaireGroupConcept)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this group of questions is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public Group setConcept(java.util.List<CodingDt> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> (QuestionnaireGroupConcept)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this group of questions is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public CodingDt addConcept() {
		CodingDt newType = new CodingDt();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>concept</b> (QuestionnaireGroupConcept),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this group of questions is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public CodingDt getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public Group setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public Group setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>required</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public BooleanDt getRequiredElement() {  
		if (myRequired == null) {
			myRequired = new BooleanDt();
		}
		return myRequired;
	}

	
	/**
	 * Gets the value(s) for <b>required</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public Boolean getRequired() {  
		return getRequiredElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>required</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public Group setRequired(BooleanDt theValue) {
		myRequired = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>required</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public Group setRequired( boolean theBoolean) {
		myRequired = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>repeats</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public BooleanDt getRepeatsElement() {  
		if (myRepeats == null) {
			myRepeats = new BooleanDt();
		}
		return myRepeats;
	}

	
	/**
	 * Gets the value(s) for <b>repeats</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public Boolean getRepeats() {  
		return getRepeatsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>repeats</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public Group setRepeats(BooleanDt theValue) {
		myRepeats = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>repeats</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public Group setRepeats( boolean theBoolean) {
		myRepeats = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public java.util.List<Group> getGroup() {  
		if (myGroup == null) {
			myGroup = new java.util.ArrayList<Group>();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public Group setGroup(java.util.List<Group> theValue) {
		myGroup = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public Group addGroup() {
		Group newType = new Group();
		getGroup().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>group</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public Group getGroupFirstRep() {
		if (getGroup().isEmpty()) {
			return addGroup();
		}
		return getGroup().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>question</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public java.util.List<GroupQuestion> getQuestion() {  
		if (myQuestion == null) {
			myQuestion = new java.util.ArrayList<GroupQuestion>();
		}
		return myQuestion;
	}

	/**
	 * Sets the value(s) for <b>question</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public Group setQuestion(java.util.List<GroupQuestion> theValue) {
		myQuestion = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>question</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public GroupQuestion addQuestion() {
		GroupQuestion newType = new GroupQuestion();
		getQuestion().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>question</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public GroupQuestion getQuestionFirstRep() {
		if (getQuestion().isEmpty()) {
			return addQuestion();
		}
		return getQuestion().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Questionnaire.group.question</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	@Block()	
	public static class GroupQuestion 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="linkId", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireAnswers]]] resource."
	)
	private StringDt myLinkId;
	
	@Child(name="concept", type=CodingDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a how this question is known in a particular terminology such as LOINC."
	)
	private java.util.List<CodingDt> myConcept;
	
	@Child(name="text", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myText;
	
	@Child(name="type", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="AnswerFormat",
		formalDefinition="The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected"
	)
	private BoundCodeDt<AnswerFormatEnum> myType;
	
	@Child(name="required", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire."
	)
	private BooleanDt myRequired;
	
	@Child(name="repeats", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether the group may occur multiple times in the instance, containing multiple sets of answers"
	)
	private BooleanDt myRepeats;
	
	@Child(name="options", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.ValueSet.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Reference to a valueset containing the possible options"
	)
	private ResourceReferenceDt myOptions;
	
	@Child(name="group", type=Group.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Nested group, containing nested question for this question. The order of groups within the question is relevant"
	)
	private java.util.List<Group> myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLinkId,  myConcept,  myText,  myType,  myRequired,  myRepeats,  myOptions,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLinkId, myConcept, myText, myType, myRequired, myRepeats, myOptions, myGroup);
	}

	/**
	 * Gets the value(s) for <b>linkId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireAnswers]]] resource.
     * </p> 
	 */
	public StringDt getLinkIdElement() {  
		if (myLinkId == null) {
			myLinkId = new StringDt();
		}
		return myLinkId;
	}

	
	/**
	 * Gets the value(s) for <b>linkId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireAnswers]]] resource.
     * </p> 
	 */
	public String getLinkId() {  
		return getLinkIdElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>linkId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireAnswers]]] resource.
     * </p> 
	 */
	public GroupQuestion setLinkId(StringDt theValue) {
		myLinkId = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>linkId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireAnswers]]] resource.
     * </p> 
	 */
	public GroupQuestion setLinkId( String theString) {
		myLinkId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this question is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public java.util.List<CodingDt> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<CodingDt>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this question is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public GroupQuestion setConcept(java.util.List<CodingDt> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this question is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public CodingDt addConcept() {
		CodingDt newType = new CodingDt();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>concept</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a how this question is known in a particular terminology such as LOINC.
     * </p> 
	 */
	public CodingDt getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public GroupQuestion setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public GroupQuestion setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (AnswerFormat).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected
     * </p> 
	 */
	public BoundCodeDt<AnswerFormatEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<AnswerFormatEnum>(AnswerFormatEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (AnswerFormat).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (AnswerFormat)
	 *
     * <p>
     * <b>Definition:</b>
     * The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected
     * </p> 
	 */
	public GroupQuestion setType(BoundCodeDt<AnswerFormatEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (AnswerFormat)
	 *
     * <p>
     * <b>Definition:</b>
     * The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected
     * </p> 
	 */
	public GroupQuestion setType(AnswerFormatEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>required</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public BooleanDt getRequiredElement() {  
		if (myRequired == null) {
			myRequired = new BooleanDt();
		}
		return myRequired;
	}

	
	/**
	 * Gets the value(s) for <b>required</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public Boolean getRequired() {  
		return getRequiredElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>required</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public GroupQuestion setRequired(BooleanDt theValue) {
		myRequired = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>required</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
     * </p> 
	 */
	public GroupQuestion setRequired( boolean theBoolean) {
		myRequired = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>repeats</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public BooleanDt getRepeatsElement() {  
		if (myRepeats == null) {
			myRepeats = new BooleanDt();
		}
		return myRepeats;
	}

	
	/**
	 * Gets the value(s) for <b>repeats</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public Boolean getRepeats() {  
		return getRepeatsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>repeats</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public GroupQuestion setRepeats(BooleanDt theValue) {
		myRepeats = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>repeats</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the group may occur multiple times in the instance, containing multiple sets of answers
     * </p> 
	 */
	public GroupQuestion setRepeats( boolean theBoolean) {
		myRepeats = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>options</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a valueset containing the possible options
     * </p> 
	 */
	public ResourceReferenceDt getOptions() {  
		if (myOptions == null) {
			myOptions = new ResourceReferenceDt();
		}
		return myOptions;
	}

	/**
	 * Sets the value(s) for <b>options</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a valueset containing the possible options
     * </p> 
	 */
	public GroupQuestion setOptions(ResourceReferenceDt theValue) {
		myOptions = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public java.util.List<Group> getGroup() {  
		if (myGroup == null) {
			myGroup = new java.util.ArrayList<Group>();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public GroupQuestion setGroup(java.util.List<Group> theValue) {
		myGroup = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public Group addGroup() {
		Group newType = new Group();
		getGroup().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>group</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public Group getGroupFirstRep() {
		if (getGroup().isEmpty()) {
			return addGroup();
		}
		return getGroup().get(0); 
	}
  

	}





    @Override
    public String getResourceName() {
        return "Questionnaire";
    }

}
