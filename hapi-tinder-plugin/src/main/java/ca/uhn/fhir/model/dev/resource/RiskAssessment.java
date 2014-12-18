















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
 * HAPI/FHIR <b>RiskAssessment</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/RiskAssessment">http://hl7.org/fhir/profiles/RiskAssessment</a> 
 * </p>
 *
 */
@ResourceDef(name="RiskAssessment", profile="http://hl7.org/fhir/profiles/RiskAssessment", id="riskassessment")
public class RiskAssessment 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>RiskAssessment.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="RiskAssessment.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>RiskAssessment.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>RiskAssessment.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="RiskAssessment.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>RiskAssessment.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.performer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="RiskAssessment.performer", description="", type="reference"  )
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.performer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PERFORMER = new ReferenceClientParam(SP_PERFORMER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="RiskAssessment.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.condition</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="condition", path="RiskAssessment.condition", description="", type="reference"  )
	public static final String SP_CONDITION = "condition";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>condition</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.condition</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONDITION = new ReferenceClientParam(SP_CONDITION);

	/**
	 * Search parameter constant for <b>method</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>RiskAssessment.method</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="method", path="RiskAssessment.method", description="", type="token"  )
	public static final String SP_METHOD = "method";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>method</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>RiskAssessment.method</b><br/>
	 * </p>
	 */
	public static final TokenClientParam METHOD = new TokenClientParam(SP_METHOD);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="RiskAssessment.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>RiskAssessment.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>RiskAssessment.condition</b>".
	 */
	public static final Include INCLUDE_CONDITION = new Include("RiskAssessment.condition");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>RiskAssessment.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("RiskAssessment.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>RiskAssessment.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("RiskAssessment.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>RiskAssessment.method</b>".
	 */
	public static final Include INCLUDE_METHOD = new Include("RiskAssessment.method");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>RiskAssessment.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("RiskAssessment.performer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>RiskAssessment.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("RiskAssessment.subject");


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Group.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The patient or group the risk assessment applies to"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date (and possibly time) the risk assessment was performed"
	)
	private DateTimeDt myDate;
	
	@Child(name="condition", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Condition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="For assessments or prognosis specific to a particular condition, indicates the condition being assessed."
	)
	private ResourceReferenceDt myCondition;
	
	@Child(name="performer", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The provider or software application that performed the assessment"
	)
	private ResourceReferenceDt myPerformer;
	
	@Child(name="identifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Business identifier assigned to the risk assessment"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="method", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="RiskAssessmentMethod",
		formalDefinition="The algorithm, processs or mechanism used to evaluate the risk"
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="basis", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.)"
	)
	private java.util.List<ResourceReferenceDt> myBasis;
	
	@Child(name="prediction", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Describes the expected outcome for the subject"
	)
	private java.util.List<Prediction> myPrediction;
	
	@Child(name="mitigation", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of the steps that might be taken to reduce the identified risk(s)."
	)
	private StringDt myMitigation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myDate,  myCondition,  myPerformer,  myIdentifier,  myMethod,  myBasis,  myPrediction,  myMitigation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myDate, myCondition, myPerformer, myIdentifier, myMethod, myBasis, myPrediction, myMitigation);
	}

	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient or group the risk assessment applies to
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
     * The patient or group the risk assessment applies to
     * </p> 
	 */
	public RiskAssessment setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) the risk assessment was performed
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
     * The date (and possibly time) the risk assessment was performed
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
     * The date (and possibly time) the risk assessment was performed
     * </p> 
	 */
	public RiskAssessment setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) the risk assessment was performed
     * </p> 
	 */
	public RiskAssessment setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) the risk assessment was performed
     * </p> 
	 */
	public RiskAssessment setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>condition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * For assessments or prognosis specific to a particular condition, indicates the condition being assessed.
     * </p> 
	 */
	public ResourceReferenceDt getCondition() {  
		if (myCondition == null) {
			myCondition = new ResourceReferenceDt();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * For assessments or prognosis specific to a particular condition, indicates the condition being assessed.
     * </p> 
	 */
	public RiskAssessment setCondition(ResourceReferenceDt theValue) {
		myCondition = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>performer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The provider or software application that performed the assessment
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
     * The provider or software application that performed the assessment
     * </p> 
	 */
	public RiskAssessment setPerformer(ResourceReferenceDt theValue) {
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
     * Business identifier assigned to the risk assessment
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
     * Business identifier assigned to the risk assessment
     * </p> 
	 */
	public RiskAssessment setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>method</b> (RiskAssessmentMethod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The algorithm, processs or mechanism used to evaluate the risk
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (RiskAssessmentMethod)
	 *
     * <p>
     * <b>Definition:</b>
     * The algorithm, processs or mechanism used to evaluate the risk
     * </p> 
	 */
	public RiskAssessment setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>basis</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getBasis() {  
		if (myBasis == null) {
			myBasis = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myBasis;
	}

	/**
	 * Sets the value(s) for <b>basis</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.)
     * </p> 
	 */
	public RiskAssessment setBasis(java.util.List<ResourceReferenceDt> theValue) {
		myBasis = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>basis</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the source data considered as part of the assessment (FamilyHistory, Observations, Procedures, Conditions, etc.)
     * </p> 
	 */
	public ResourceReferenceDt addBasis() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getBasis().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>prediction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the expected outcome for the subject
     * </p> 
	 */
	public java.util.List<Prediction> getPrediction() {  
		if (myPrediction == null) {
			myPrediction = new java.util.ArrayList<Prediction>();
		}
		return myPrediction;
	}

	/**
	 * Sets the value(s) for <b>prediction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the expected outcome for the subject
     * </p> 
	 */
	public RiskAssessment setPrediction(java.util.List<Prediction> theValue) {
		myPrediction = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>prediction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the expected outcome for the subject
     * </p> 
	 */
	public Prediction addPrediction() {
		Prediction newType = new Prediction();
		getPrediction().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>prediction</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the expected outcome for the subject
     * </p> 
	 */
	public Prediction getPredictionFirstRep() {
		if (getPrediction().isEmpty()) {
			return addPrediction();
		}
		return getPrediction().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>mitigation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the steps that might be taken to reduce the identified risk(s).
     * </p> 
	 */
	public StringDt getMitigationElement() {  
		if (myMitigation == null) {
			myMitigation = new StringDt();
		}
		return myMitigation;
	}

	
	/**
	 * Gets the value(s) for <b>mitigation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the steps that might be taken to reduce the identified risk(s).
     * </p> 
	 */
	public String getMitigation() {  
		return getMitigationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mitigation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the steps that might be taken to reduce the identified risk(s).
     * </p> 
	 */
	public RiskAssessment setMitigation(StringDt theValue) {
		myMitigation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>mitigation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the steps that might be taken to reduce the identified risk(s).
     * </p> 
	 */
	public RiskAssessment setMitigation( String theString) {
		myMitigation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>RiskAssessment.prediction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the expected outcome for the subject
     * </p> 
	 */
	@Block()	
	public static class Prediction 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="outcome", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="RiskAssessmentOutcome",
		formalDefinition="One of the potential outcomes for the patient (e.g. remission, death,  a particular condition)"
	)
	private CodeableConceptDt myOutcome;
	
	@Child(name="probability", order=1, min=0, max=1, type={
		DecimalDt.class, 		RangeDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="RiskAssessmentProbability",
		formalDefinition="How likely is the outcome (in the specified timeframe)"
	)
	private IDatatype myProbability;
	
	@Child(name="relativeRisk", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)"
	)
	private DecimalDt myRelativeRisk;
	
	@Child(name="when", order=3, min=0, max=1, type={
		PeriodDt.class, 		RangeDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the period of time or age range of the subject to which the specified probability applies"
	)
	private IDatatype myWhen;
	
	@Child(name="rationale", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional information explaining the basis for the prediction"
	)
	private StringDt myRationale;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myOutcome,  myProbability,  myRelativeRisk,  myWhen,  myRationale);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myOutcome, myProbability, myRelativeRisk, myWhen, myRationale);
	}

	/**
	 * Gets the value(s) for <b>outcome</b> (RiskAssessmentOutcome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One of the potential outcomes for the patient (e.g. remission, death,  a particular condition)
     * </p> 
	 */
	public CodeableConceptDt getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new CodeableConceptDt();
		}
		return myOutcome;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (RiskAssessmentOutcome)
	 *
     * <p>
     * <b>Definition:</b>
     * One of the potential outcomes for the patient (e.g. remission, death,  a particular condition)
     * </p> 
	 */
	public Prediction setOutcome(CodeableConceptDt theValue) {
		myOutcome = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>probability[x]</b> (RiskAssessmentProbability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How likely is the outcome (in the specified timeframe)
     * </p> 
	 */
	public IDatatype getProbability() {  
		return myProbability;
	}

	/**
	 * Sets the value(s) for <b>probability[x]</b> (RiskAssessmentProbability)
	 *
     * <p>
     * <b>Definition:</b>
     * How likely is the outcome (in the specified timeframe)
     * </p> 
	 */
	public Prediction setProbability(IDatatype theValue) {
		myProbability = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>relativeRisk</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)
     * </p> 
	 */
	public DecimalDt getRelativeRiskElement() {  
		if (myRelativeRisk == null) {
			myRelativeRisk = new DecimalDt();
		}
		return myRelativeRisk;
	}

	
	/**
	 * Gets the value(s) for <b>relativeRisk</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)
     * </p> 
	 */
	public BigDecimal getRelativeRisk() {  
		return getRelativeRiskElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>relativeRisk</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)
     * </p> 
	 */
	public Prediction setRelativeRisk(DecimalDt theValue) {
		myRelativeRisk = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>relativeRisk</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)
     * </p> 
	 */
	public Prediction setRelativeRisk( long theValue) {
		myRelativeRisk = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>relativeRisk</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)
     * </p> 
	 */
	public Prediction setRelativeRisk( double theValue) {
		myRelativeRisk = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>relativeRisk</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the risk for this particular subject (with their specific characteristics) divided by the risk of the population in general.  (Numbers greater than 1 = higher risk than the population, numbers less than 1 = lower risk.)
     * </p> 
	 */
	public Prediction setRelativeRisk( java.math.BigDecimal theValue) {
		myRelativeRisk = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>when[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time or age range of the subject to which the specified probability applies
     * </p> 
	 */
	public IDatatype getWhen() {  
		return myWhen;
	}

	/**
	 * Sets the value(s) for <b>when[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time or age range of the subject to which the specified probability applies
     * </p> 
	 */
	public Prediction setWhen(IDatatype theValue) {
		myWhen = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>rationale</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information explaining the basis for the prediction
     * </p> 
	 */
	public StringDt getRationaleElement() {  
		if (myRationale == null) {
			myRationale = new StringDt();
		}
		return myRationale;
	}

	
	/**
	 * Gets the value(s) for <b>rationale</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information explaining the basis for the prediction
     * </p> 
	 */
	public String getRationale() {  
		return getRationaleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>rationale</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information explaining the basis for the prediction
     * </p> 
	 */
	public Prediction setRationale(StringDt theValue) {
		myRationale = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>rationale</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information explaining the basis for the prediction
     * </p> 
	 */
	public Prediction setRationale( String theString) {
		myRationale = new StringDt(theString); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "RiskAssessment";
    }

}
