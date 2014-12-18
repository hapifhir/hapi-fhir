















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
 * HAPI/FHIR <b>QuestionnaireAnswers</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support structured, hierarchical registration of data gathered using digital forms and other questionnaires.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/QuestionnaireAnswers">http://hl7.org/fhir/profiles/QuestionnaireAnswers</a> 
 * </p>
 *
 */
@ResourceDef(name="QuestionnaireAnswers", profile="http://hl7.org/fhir/profiles/QuestionnaireAnswers", id="questionnaireanswers")
public class QuestionnaireAnswers 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the questionnaire answers</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>QuestionnaireAnswers.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="QuestionnaireAnswers.status", description="The status of the questionnaire answers", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the questionnaire answers</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>QuestionnaireAnswers.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>authored</b>
	 * <p>
	 * Description: <b>When the questionnaire was authored</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>QuestionnaireAnswers.authored</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="authored", path="QuestionnaireAnswers.authored", description="When the questionnaire was authored", type="date"  )
	public static final String SP_AUTHORED = "authored";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>authored</b>
	 * <p>
	 * Description: <b>When the questionnaire was authored</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>QuestionnaireAnswers.authored</b><br/>
	 * </p>
	 */
	public static final DateClientParam AUTHORED = new DateClientParam(SP_AUTHORED);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="QuestionnaireAnswers.subject", description="The subject of the questionnaire", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>The author of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="QuestionnaireAnswers.author", description="The author of the questionnaire", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>The author of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Search parameter constant for <b>questionnaire</b>
	 * <p>
	 * Description: <b>The questionnaire the answers are provided for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.questionnaire</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="questionnaire", path="QuestionnaireAnswers.questionnaire", description="The questionnaire the answers are provided for", type="reference"  )
	public static final String SP_QUESTIONNAIRE = "questionnaire";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>questionnaire</b>
	 * <p>
	 * Description: <b>The questionnaire the answers are provided for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.questionnaire</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam QUESTIONNAIRE = new ReferenceClientParam(SP_QUESTIONNAIRE);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Encounter during which questionnaire was authored</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="QuestionnaireAnswers.encounter", description="Encounter during which questionnaire was authored", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Encounter during which questionnaire was authored</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient that is the subject of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="QuestionnaireAnswers.subject", description="The patient that is the subject of the questionnaire", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient that is the subject of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>QuestionnaireAnswers.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>QuestionnaireAnswers.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("QuestionnaireAnswers.author");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>QuestionnaireAnswers.authored</b>".
	 */
	public static final Include INCLUDE_AUTHORED = new Include("QuestionnaireAnswers.authored");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>QuestionnaireAnswers.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("QuestionnaireAnswers.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>QuestionnaireAnswers.questionnaire</b>".
	 */
	public static final Include INCLUDE_QUESTIONNAIRE = new Include("QuestionnaireAnswers.questionnaire");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>QuestionnaireAnswers.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("QuestionnaireAnswers.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>QuestionnaireAnswers.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("QuestionnaireAnswers.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A business identifier assigned to a particular completed (or partially completed) questionnaire"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="questionnaire", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Questionnaire.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the Questionnaire resource that defines the form for which answers are being provided"
	)
	private ResourceReferenceDt myQuestionnaire;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="QuestionnaireAnswersStatus",
		formalDefinition="The lifecycle status of the questionnaire answers as a whole."
	)
	private BoundCodeDt<QuestionnaireAnswersStatusEnum> myStatus;
	
	@Child(name="subject", order=3, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="authored", type=DateTimeDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date and/or time that this version of the questionnaire answers was authored"
	)
	private DateTimeDt myAuthored;
	
	@Child(name="source", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person who answered the questions about the subject. Only used when this is not the subject him/herself"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="encounter", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="group", order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A group of questions to a possibly similarly grouped set of questions in the questionnaire answers"
	)
	private Group myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myQuestionnaire,  myStatus,  mySubject,  myAuthor,  myAuthored,  mySource,  myEncounter,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myQuestionnaire, myStatus, mySubject, myAuthor, myAuthored, mySource, myEncounter, myGroup);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A business identifier assigned to a particular completed (or partially completed) questionnaire
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
     * A business identifier assigned to a particular completed (or partially completed) questionnaire
     * </p> 
	 */
	public QuestionnaireAnswers setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>questionnaire</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the Questionnaire resource that defines the form for which answers are being provided
     * </p> 
	 */
	public ResourceReferenceDt getQuestionnaire() {  
		if (myQuestionnaire == null) {
			myQuestionnaire = new ResourceReferenceDt();
		}
		return myQuestionnaire;
	}

	/**
	 * Sets the value(s) for <b>questionnaire</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the Questionnaire resource that defines the form for which answers are being provided
     * </p> 
	 */
	public QuestionnaireAnswers setQuestionnaire(ResourceReferenceDt theValue) {
		myQuestionnaire = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (QuestionnaireAnswersStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire answers as a whole.
     * </p> 
	 */
	public BoundCodeDt<QuestionnaireAnswersStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<QuestionnaireAnswersStatusEnum>(QuestionnaireAnswersStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (QuestionnaireAnswersStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire answers as a whole.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (QuestionnaireAnswersStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire answers as a whole.
     * </p> 
	 */
	public QuestionnaireAnswers setStatus(BoundCodeDt<QuestionnaireAnswersStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (QuestionnaireAnswersStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire answers as a whole.
     * </p> 
	 */
	public QuestionnaireAnswers setStatus(QuestionnaireAnswersStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information
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
     * The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information
     * </p> 
	 */
	public QuestionnaireAnswers setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system
     * </p> 
	 */
	public QuestionnaireAnswers setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>authored</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire answers was authored
     * </p> 
	 */
	public DateTimeDt getAuthoredElement() {  
		if (myAuthored == null) {
			myAuthored = new DateTimeDt();
		}
		return myAuthored;
	}

	
	/**
	 * Gets the value(s) for <b>authored</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire answers was authored
     * </p> 
	 */
	public Date getAuthored() {  
		return getAuthoredElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>authored</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire answers was authored
     * </p> 
	 */
	public QuestionnaireAnswers setAuthored(DateTimeDt theValue) {
		myAuthored = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>authored</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire answers was authored
     * </p> 
	 */
	public QuestionnaireAnswers setAuthored( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myAuthored = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>authored</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire answers was authored
     * </p> 
	 */
	public QuestionnaireAnswers setAuthoredWithSecondsPrecision( Date theDate) {
		myAuthored = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who answered the questions about the subject. Only used when this is not the subject him/herself
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The person who answered the questions about the subject. Only used when this is not the subject him/herself
     * </p> 
	 */
	public QuestionnaireAnswers setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.
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
     * Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.
     * </p> 
	 */
	public QuestionnaireAnswers setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire answers
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
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire answers
     * </p> 
	 */
	public QuestionnaireAnswers setGroup(Group theValue) {
		myGroup = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>QuestionnaireAnswers.group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire answers
     * </p> 
	 */
	@Block()	
	public static class Group 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="linkId", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource."
	)
	private StringDt myLinkId;
	
	@Child(name="title", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Text that is displayed above the contents of the group"
	)
	private StringDt myTitle;
	
	@Child(name="text", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional text for the group, used for display purposes"
	)
	private StringDt myText;
	
	@Child(name="subject", order=3, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="group", type=Group.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A sub-group within a group. The ordering of groups within this group is relevant"
	)
	private java.util.List<Group> myGroup;
	
	@Child(name="question", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Set of questions within this group. The order of questions within the group is relevant"
	)
	private java.util.List<GroupQuestion> myQuestion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLinkId,  myTitle,  myText,  mySubject,  myGroup,  myQuestion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLinkId, myTitle, myText, mySubject, myGroup, myQuestion);
	}

	/**
	 * Gets the value(s) for <b>linkId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
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
     * Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
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
     * Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
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
     * Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
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
     * Text that is displayed above the contents of the group
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
     * Text that is displayed above the contents of the group
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
     * Text that is displayed above the contents of the group
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
     * Text that is displayed above the contents of the group
     * </p> 
	 */
	public Group setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
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
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers
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
     * More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers
     * </p> 
	 */
	public Group setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
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
	 * Block class for child element: <b>QuestionnaireAnswers.group.question</b> ()
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
		formalDefinition="Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource."
	)
	private StringDt myLinkId;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myText;
	
	@Child(name="answer", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The respondent's answer(s) to the question"
	)
	private java.util.List<GroupQuestionAnswer> myAnswer;
	
	@Child(name="group", type=Group.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Nested group, containing nested question for this question. The order of groups within the question is relevant"
	)
	private java.util.List<Group> myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLinkId,  myText,  myAnswer,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLinkId, myText, myAnswer, myGroup);
	}

	/**
	 * Gets the value(s) for <b>linkId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
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
     * Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
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
     * Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
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
     * Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
     * </p> 
	 */
	public GroupQuestion setLinkId( String theString) {
		myLinkId = new StringDt(theString); 
		return this; 
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
	 * Gets the value(s) for <b>answer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The respondent's answer(s) to the question
     * </p> 
	 */
	public java.util.List<GroupQuestionAnswer> getAnswer() {  
		if (myAnswer == null) {
			myAnswer = new java.util.ArrayList<GroupQuestionAnswer>();
		}
		return myAnswer;
	}

	/**
	 * Sets the value(s) for <b>answer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The respondent's answer(s) to the question
     * </p> 
	 */
	public GroupQuestion setAnswer(java.util.List<GroupQuestionAnswer> theValue) {
		myAnswer = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>answer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The respondent's answer(s) to the question
     * </p> 
	 */
	public GroupQuestionAnswer addAnswer() {
		GroupQuestionAnswer newType = new GroupQuestionAnswer();
		getAnswer().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>answer</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The respondent's answer(s) to the question
     * </p> 
	 */
	public GroupQuestionAnswer getAnswerFirstRep() {
		if (getAnswer().isEmpty()) {
			return addAnswer();
		}
		return getAnswer().get(0); 
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

	/**
	 * Block class for child element: <b>QuestionnaireAnswers.group.question.answer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The respondent's answer(s) to the question
     * </p> 
	 */
	@Block()	
	public static class GroupQuestionAnswer 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="value", order=0, min=0, max=1, type={
		BooleanDt.class, 		DecimalDt.class, 		IntegerDt.class, 		DateDt.class, 		DateTimeDt.class, 		InstantDt.class, 		TimeDt.class, 		StringDt.class, 		AttachmentDt.class, 		CodingDt.class, 		QuantityDt.class, 		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Single-valued answer to the question"
	)
	private IDatatype myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myValue);
	}

	/**
	 * Gets the value(s) for <b>value[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Single-valued answer to the question
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Single-valued answer to the question
     * </p> 
	 */
	public GroupQuestionAnswer setValue(IDatatype theValue) {
		myValue = theValue;
		return this;
	}
	
	

  

	}






    @Override
    public String getResourceName() {
        return "QuestionnaireAnswers";
    }

}
