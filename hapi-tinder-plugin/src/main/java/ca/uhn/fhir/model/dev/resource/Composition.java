















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
 * HAPI/FHIR <b>Composition</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support documents, and also to capture the EN13606 notion of an attested commit to the patient EHR, and to allow a set of disparate resources at the information/engineering level to be gathered into a clinical statement
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Composition">http://hl7.org/fhir/profiles/Composition</a> 
 * </p>
 *
 */
@ResourceDef(name="Composition", profile="http://hl7.org/fhir/profiles/Composition", id="composition")
public class Composition 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Composition.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.class</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="class", path="Composition.class", description="", type="token"  )
	public static final String SP_CLASS = "class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.class</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CLASS = new TokenClientParam(SP_CLASS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Composition.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Composition.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Composition.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Composition.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="Composition.author", description="", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Search parameter constant for <b>attester</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.attester.party</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="attester", path="Composition.attester.party", description="", type="reference"  )
	public static final String SP_ATTESTER = "attester";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>attester</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.attester.party</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ATTESTER = new ReferenceClientParam(SP_ATTESTER);

	/**
	 * Search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.event.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="context", path="Composition.event.code", description="", type="token"  )
	public static final String SP_CONTEXT = "context";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.event.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTEXT = new TokenClientParam(SP_CONTEXT);

	/**
	 * Search parameter constant for <b>section</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.section.content</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="section", path="Composition.section.content", description="", type="reference"  )
	public static final String SP_SECTION = "section";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>section</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.section.content</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SECTION = new ReferenceClientParam(SP_SECTION);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Composition.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>title</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Composition.title</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="title", path="Composition.title", description="", type="string"  )
	public static final String SP_TITLE = "title";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>title</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Composition.title</b><br/>
	 * </p>
	 */
	public static final StringClientParam TITLE = new StringClientParam(SP_TITLE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Composition.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.confidentiality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="confidentiality", path="Composition.confidentiality", description="", type="token"  )
	public static final String SP_CONFIDENTIALITY = "confidentiality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.confidentiality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONFIDENTIALITY = new TokenClientParam(SP_CONFIDENTIALITY);

	/**
	 * Search parameter constant for <b>period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Composition.event.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="period", path="Composition.event.period", description="", type="date"  )
	public static final String SP_PERIOD = "period";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Composition.event.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam PERIOD = new DateClientParam(SP_PERIOD);

	/**
	 * Search parameter constant for <b>section-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.section.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="section-code", path="Composition.section.code", description="", type="token"  )
	public static final String SP_SECTION_CODE = "section-code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>section-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Composition.section.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SECTION_CODE = new TokenClientParam(SP_SECTION_CODE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Composition.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Composition.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.attester.party</b>".
	 */
	public static final Include INCLUDE_ATTESTER_PARTY = new Include("Composition.attester.party");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("Composition.author");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.class</b>".
	 */
	public static final Include INCLUDE_CLASS = new Include("Composition.class");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.confidentiality</b>".
	 */
	public static final Include INCLUDE_CONFIDENTIALITY = new Include("Composition.confidentiality");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Composition.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.event.code</b>".
	 */
	public static final Include INCLUDE_EVENT_CODE = new Include("Composition.event.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.event.period</b>".
	 */
	public static final Include INCLUDE_EVENT_PERIOD = new Include("Composition.event.period");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Composition.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.section.code</b>".
	 */
	public static final Include INCLUDE_SECTION_CODE = new Include("Composition.section.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.section.content</b>".
	 */
	public static final Include INCLUDE_SECTION_CONTENT = new Include("Composition.section.content");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Composition.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Composition.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.title</b>".
	 */
	public static final Include INCLUDE_TITLE = new Include("Composition.title");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Composition.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Composition.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The composition editing time, when the composition was last logically changed by the author"
	)
	private DateTimeDt myDate;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="DocumentType",
		formalDefinition="Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition"
	)
	private CodeableConceptDt myType;
	
	@Child(name="class", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="DocumentClass",
		formalDefinition="A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type"
	)
	private CodeableConceptDt myClassElement;
	
	@Child(name="title", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Official human-readable label for the composition"
	)
	private StringDt myTitle;
	
	@Child(name="status", type=CodeDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="CompositionStatus",
		formalDefinition="The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document"
	)
	private BoundCodeDt<CompositionStatusEnum> myStatus;
	
	@Child(name="confidentiality", type=CodingDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="DocumentConfidentiality",
		formalDefinition="The code specifying the level of confidentiality of the Composition"
	)
	private CodingDt myConfidentiality;
	
	@Child(name="subject", order=7, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=8, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="attester", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A participant who has attested to the accuracy of the composition/document"
	)
	private java.util.List<Attester> myAttester;
	
	@Child(name="custodian", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information"
	)
	private ResourceReferenceDt myCustodian;
	
	@Child(name="event", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The clinical service, such as a colonoscopy or an appendectomy, being documented"
	)
	private java.util.List<Event> myEvent;
	
	@Child(name="encounter", order=12, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Describes the clinical encounter or type of care this documentation is associated with."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="section", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The root of the sections that make up the composition"
	)
	private java.util.List<Section> mySection;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  myType,  myClassElement,  myTitle,  myStatus,  myConfidentiality,  mySubject,  myAuthor,  myAttester,  myCustodian,  myEvent,  myEncounter,  mySection);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, myType, myClassElement, myTitle, myStatus, myConfidentiality, mySubject, myAuthor, myAttester, myCustodian, myEvent, myEncounter, mySection);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time
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
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time
     * </p> 
	 */
	public Composition setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
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
     * The composition editing time, when the composition was last logically changed by the author
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
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public Composition setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public Composition setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The composition editing time, when the composition was last logically changed by the author
     * </p> 
	 */
	public Composition setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (DocumentType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (DocumentType)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition
     * </p> 
	 */
	public Composition setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>class</b> (DocumentClass).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type
     * </p> 
	 */
	public CodeableConceptDt getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeableConceptDt();
		}
		return myClassElement;
	}

	/**
	 * Sets the value(s) for <b>class</b> (DocumentClass)
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type
     * </p> 
	 */
	public Composition setClassElement(CodeableConceptDt theValue) {
		myClassElement = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Official human-readable label for the composition
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
     * Official human-readable label for the composition
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
     * Official human-readable label for the composition
     * </p> 
	 */
	public Composition setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Official human-readable label for the composition
     * </p> 
	 */
	public Composition setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (CompositionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public BoundCodeDt<CompositionStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<CompositionStatusEnum>(CompositionStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (CompositionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (CompositionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public Composition setStatus(BoundCodeDt<CompositionStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (CompositionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document
     * </p> 
	 */
	public Composition setStatus(CompositionStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>confidentiality</b> (DocumentConfidentiality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code specifying the level of confidentiality of the Composition
     * </p> 
	 */
	public CodingDt getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new CodingDt();
		}
		return myConfidentiality;
	}

	/**
	 * Sets the value(s) for <b>confidentiality</b> (DocumentConfidentiality)
	 *
     * <p>
     * <b>Definition:</b>
     * The code specifying the level of confidentiality of the Composition
     * </p> 
	 */
	public Composition setConfidentiality(CodingDt theValue) {
		myConfidentiality = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)
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
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)
     * </p> 
	 */
	public Composition setSubject(ResourceReferenceDt theValue) {
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
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)
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
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)
     * </p> 
	 */
	public Composition setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>attester</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public java.util.List<Attester> getAttester() {  
		if (myAttester == null) {
			myAttester = new java.util.ArrayList<Attester>();
		}
		return myAttester;
	}

	/**
	 * Sets the value(s) for <b>attester</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public Composition setAttester(java.util.List<Attester> theValue) {
		myAttester = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>attester</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public Attester addAttester() {
		Attester newType = new Attester();
		getAttester().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>attester</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	public Attester getAttesterFirstRep() {
		if (getAttester().isEmpty()) {
			return addAttester();
		}
		return getAttester().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>custodian</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information
     * </p> 
	 */
	public ResourceReferenceDt getCustodian() {  
		if (myCustodian == null) {
			myCustodian = new ResourceReferenceDt();
		}
		return myCustodian;
	}

	/**
	 * Sets the value(s) for <b>custodian</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information
     * </p> 
	 */
	public Composition setCustodian(ResourceReferenceDt theValue) {
		myCustodian = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>event</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	public java.util.List<Event> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<Event>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	public Composition setEvent(java.util.List<Event> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	public Event addEvent() {
		Event newType = new Event();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	public Event getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the clinical encounter or type of care this documentation is associated with.
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
     * Describes the clinical encounter or type of care this documentation is associated with.
     * </p> 
	 */
	public Composition setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>section</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public java.util.List<Section> getSection() {  
		if (mySection == null) {
			mySection = new java.util.ArrayList<Section>();
		}
		return mySection;
	}

	/**
	 * Sets the value(s) for <b>section</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public Composition setSection(java.util.List<Section> theValue) {
		mySection = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>section</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public Section addSection() {
		Section newType = new Section();
		getSection().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>section</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	public Section getSectionFirstRep() {
		if (getSection().isEmpty()) {
			return addSection();
		}
		return getSection().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Composition.attester</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A participant who has attested to the accuracy of the composition/document
     * </p> 
	 */
	@Block()	
	public static class Attester 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="CompositionAttestationMode",
		formalDefinition="The type of attestation the authenticator offers"
	)
	private java.util.List<BoundCodeDt<CompositionAttestationModeEnum>> myMode;
	
	@Child(name="time", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When composition was attested by the party"
	)
	private DateTimeDt myTime;
	
	@Child(name="party", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who attested the composition in the specified way"
	)
	private ResourceReferenceDt myParty;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myTime,  myParty);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMode, myTime, myParty);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (CompositionAttestationMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public java.util.List<BoundCodeDt<CompositionAttestationModeEnum>> getMode() {  
		if (myMode == null) {
			myMode = new java.util.ArrayList<BoundCodeDt<CompositionAttestationModeEnum>>();
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (CompositionAttestationMode)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public Attester setMode(java.util.List<BoundCodeDt<CompositionAttestationModeEnum>> theValue) {
		myMode = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>mode</b> (CompositionAttestationMode) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public BoundCodeDt<CompositionAttestationModeEnum> addMode(CompositionAttestationModeEnum theValue) {
		BoundCodeDt<CompositionAttestationModeEnum> retVal = new BoundCodeDt<CompositionAttestationModeEnum>(CompositionAttestationModeEnum.VALUESET_BINDER, theValue);
		getMode().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>mode</b> (CompositionAttestationMode),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public BoundCodeDt<CompositionAttestationModeEnum> getModeFirstRep() {
		if (getMode().size() == 0) {
			addMode();
		}
		return getMode().get(0);
	}

	/**
	 * Add a value for <b>mode</b> (CompositionAttestationMode)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public BoundCodeDt<CompositionAttestationModeEnum> addMode() {
		BoundCodeDt<CompositionAttestationModeEnum> retVal = new BoundCodeDt<CompositionAttestationModeEnum>(CompositionAttestationModeEnum.VALUESET_BINDER);
		getMode().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>mode</b> (CompositionAttestationMode)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of attestation the authenticator offers
     * </p> 
	 */
	public Attester setMode(CompositionAttestationModeEnum theValue) {
		getMode().clear();
		addMode(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>time</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public DateTimeDt getTimeElement() {  
		if (myTime == null) {
			myTime = new DateTimeDt();
		}
		return myTime;
	}

	
	/**
	 * Gets the value(s) for <b>time</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Date getTime() {  
		return getTimeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>time</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Attester setTime(DateTimeDt theValue) {
		myTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>time</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Attester setTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>time</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When composition was attested by the party
     * </p> 
	 */
	public Attester setTimeWithSecondsPrecision( Date theDate) {
		myTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>party</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who attested the composition in the specified way
     * </p> 
	 */
	public ResourceReferenceDt getParty() {  
		if (myParty == null) {
			myParty = new ResourceReferenceDt();
		}
		return myParty;
	}

	/**
	 * Sets the value(s) for <b>party</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who attested the composition in the specified way
     * </p> 
	 */
	public Attester setParty(ResourceReferenceDt theValue) {
		myParty = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Composition.event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented
     * </p> 
	 */
	@Block()	
	public static class Event 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="DocumentEventType",
		formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act."
	)
	private java.util.List<CodeableConceptDt> myCode;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time"
	)
	private PeriodDt myPeriod;
	
	@Child(name="detail", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Full details for the event(s) the composition/documentation consents"
	)
	private java.util.List<ResourceReferenceDt> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myPeriod,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myPeriod, myDetail);
	}

	/**
	 * Gets the value(s) for <b>code</b> (DocumentEventType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (DocumentEventType)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public Event setCode(java.util.List<CodeableConceptDt> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>code</b> (DocumentEventType)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public CodeableConceptDt addCode() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCode().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>code</b> (DocumentEventType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.
     * </p> 
	 */
	public CodeableConceptDt getCodeFirstRep() {
		if (getCode().isEmpty()) {
			return addCode();
		}
		return getCode().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time
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
     * The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time
     * </p> 
	 */
	public Event setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Full details for the event(s) the composition/documentation consents
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Full details for the event(s) the composition/documentation consents
     * </p> 
	 */
	public Event setDetail(java.util.List<ResourceReferenceDt> theValue) {
		myDetail = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Full details for the event(s) the composition/documentation consents
     * </p> 
	 */
	public ResourceReferenceDt addDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDetail().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Composition.section</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The root of the sections that make up the composition
     * </p> 
	 */
	@Block()	
	public static class Section 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="title", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents"
	)
	private StringDt myTitle;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="CompositionSectionType",
		formalDefinition="A code identifying the kind of content contained within the section. This must be consistent with the section title"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="section", type=Section.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A nested sub-section within this section"
	)
	private java.util.List<Section> mySection;
	
	@Child(name="content", order=3, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The content (narrative and data) associated with the section"
	)
	private ResourceReferenceDt myContent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTitle,  myCode,  mySection,  myContent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTitle, myCode, mySection, myContent);
	}

	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents
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
     * The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents
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
     * The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents
     * </p> 
	 */
	public Section setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents
     * </p> 
	 */
	public Section setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (CompositionSectionType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code identifying the kind of content contained within the section. This must be consistent with the section title
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (CompositionSectionType)
	 *
     * <p>
     * <b>Definition:</b>
     * A code identifying the kind of content contained within the section. This must be consistent with the section title
     * </p> 
	 */
	public Section setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>section</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public java.util.List<Section> getSection() {  
		if (mySection == null) {
			mySection = new java.util.ArrayList<Section>();
		}
		return mySection;
	}

	/**
	 * Sets the value(s) for <b>section</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public Section setSection(java.util.List<Section> theValue) {
		mySection = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>section</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public Section addSection() {
		Section newType = new Section();
		getSection().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>section</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A nested sub-section within this section
     * </p> 
	 */
	public Section getSectionFirstRep() {
		if (getSection().isEmpty()) {
			return addSection();
		}
		return getSection().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>content</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The content (narrative and data) associated with the section
     * </p> 
	 */
	public ResourceReferenceDt getContent() {  
		if (myContent == null) {
			myContent = new ResourceReferenceDt();
		}
		return myContent;
	}

	/**
	 * Sets the value(s) for <b>content</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The content (narrative and data) associated with the section
     * </p> 
	 */
	public Section setContent(ResourceReferenceDt theValue) {
		myContent = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Composition";
    }

}
