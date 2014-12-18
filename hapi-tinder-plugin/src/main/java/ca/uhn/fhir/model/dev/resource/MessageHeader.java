















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
 * HAPI/FHIR <b>MessageHeader</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * The header for a message exchange that is either requesting or responding to an action.  The Reference(s) that are the subject of the action as well as other Information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Many implementations are not prepared to use REST and need a messaging based infrastructure
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MessageHeader">http://hl7.org/fhir/profiles/MessageHeader</a> 
 * </p>
 *
 */
@ResourceDef(name="MessageHeader", profile="http://hl7.org/fhir/profiles/MessageHeader", id="messageheader")
public class MessageHeader 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>src-id</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="src-id", path="MessageHeader.identifier", description="", type="token"  )
	public static final String SP_SRC_ID = "src-id";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>src-id</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SRC_ID = new TokenClientParam(SP_SRC_ID);

	/**
	 * Search parameter constant for <b>timestamp</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MessageHeader.timestamp</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="timestamp", path="MessageHeader.timestamp", description="", type="date"  )
	public static final String SP_TIMESTAMP = "timestamp";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>timestamp</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MessageHeader.timestamp</b><br/>
	 * </p>
	 */
	public static final DateClientParam TIMESTAMP = new DateClientParam(SP_TIMESTAMP);

	/**
	 * Search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.event</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event", path="MessageHeader.event", description="", type="token"  )
	public static final String SP_EVENT = "event";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.event</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVENT = new TokenClientParam(SP_EVENT);

	/**
	 * Search parameter constant for <b>response-id</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.response.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="response-id", path="MessageHeader.response.identifier", description="", type="token"  )
	public static final String SP_RESPONSE_ID = "response-id";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>response-id</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.response.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RESPONSE_ID = new TokenClientParam(SP_RESPONSE_ID);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.response.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="MessageHeader.response.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.response.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>MessageHeader.source.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="MessageHeader.source.name", description="", type="string"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>MessageHeader.source.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam SOURCE = new StringClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>source-uri</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.source.endpoint</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source-uri", path="MessageHeader.source.endpoint", description="", type="token"  )
	public static final String SP_SOURCE_URI = "source-uri";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source-uri</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.source.endpoint</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SOURCE_URI = new TokenClientParam(SP_SOURCE_URI);

	/**
	 * Search parameter constant for <b>destination</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>MessageHeader.destination.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="destination", path="MessageHeader.destination.name", description="", type="string"  )
	public static final String SP_DESTINATION = "destination";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>destination</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>MessageHeader.destination.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESTINATION = new StringClientParam(SP_DESTINATION);

	/**
	 * Search parameter constant for <b>destination-uri</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.destination.endpoint</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="destination-uri", path="MessageHeader.destination.endpoint", description="", type="token"  )
	public static final String SP_DESTINATION_URI = "destination-uri";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>destination-uri</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MessageHeader.destination.endpoint</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DESTINATION_URI = new TokenClientParam(SP_DESTINATION_URI);

	/**
	 * Search parameter constant for <b>data</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MessageHeader.data</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="data", path="MessageHeader.data", description="", type="reference"  )
	public static final String SP_DATA = "data";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>data</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MessageHeader.data</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DATA = new ReferenceClientParam(SP_DATA);

	/**
	 * Search parameter constant for <b>receiver</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MessageHeader.receiver</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="receiver", path="MessageHeader.receiver", description="", type="reference"  )
	public static final String SP_RECEIVER = "receiver";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>receiver</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MessageHeader.receiver</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RECEIVER = new ReferenceClientParam(SP_RECEIVER);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.data</b>".
	 */
	public static final Include INCLUDE_DATA = new Include("MessageHeader.data");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.destination.endpoint</b>".
	 */
	public static final Include INCLUDE_DESTINATION_ENDPOINT = new Include("MessageHeader.destination.endpoint");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.destination.name</b>".
	 */
	public static final Include INCLUDE_DESTINATION_NAME = new Include("MessageHeader.destination.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.event</b>".
	 */
	public static final Include INCLUDE_EVENT = new Include("MessageHeader.event");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("MessageHeader.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.receiver</b>".
	 */
	public static final Include INCLUDE_RECEIVER = new Include("MessageHeader.receiver");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.response.code</b>".
	 */
	public static final Include INCLUDE_RESPONSE_CODE = new Include("MessageHeader.response.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.response.identifier</b>".
	 */
	public static final Include INCLUDE_RESPONSE_IDENTIFIER = new Include("MessageHeader.response.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.source.endpoint</b>".
	 */
	public static final Include INCLUDE_SOURCE_ENDPOINT = new Include("MessageHeader.source.endpoint");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.source.name</b>".
	 */
	public static final Include INCLUDE_SOURCE_NAME = new Include("MessageHeader.source.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MessageHeader.timestamp</b>".
	 */
	public static final Include INCLUDE_TIMESTAMP = new Include("MessageHeader.timestamp");


	@Child(name="identifier", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier of this message"
	)
	private IdDt myIdentifier;
	
	@Child(name="timestamp", type=InstantDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The time that the message was sent"
	)
	private InstantDt myTimestamp;
	
	@Child(name="event", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="MessageEvent",
		formalDefinition="Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-type\""
	)
	private CodingDt myEvent;
	
	@Child(name="response", order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Information about the message that this message is a response to.  Only present if this message is a response."
	)
	private Response myResponse;
	
	@Child(name="source", order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The source application from which this message originated"
	)
	private Source mySource;
	
	@Child(name="destination", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The destination application which the message is intended for"
	)
	private java.util.List<Destination> myDestination;
	
	@Child(name="enterer", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions"
	)
	private ResourceReferenceDt myEnterer;
	
	@Child(name="author", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="receiver", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient."
	)
	private ResourceReferenceDt myReceiver;
	
	@Child(name="responsible", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party"
	)
	private ResourceReferenceDt myResponsible;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="EventReason",
		formalDefinition="Coded indication of the cause for the event - indicates  a reason for the occurance of the event that is a focus of this message"
	)
	private CodeableConceptDt myReason;
	
	@Child(name="data", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The actual data of the message - a reference to the root/focus class of the event."
	)
	private java.util.List<ResourceReferenceDt> myData;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myTimestamp,  myEvent,  myResponse,  mySource,  myDestination,  myEnterer,  myAuthor,  myReceiver,  myResponsible,  myReason,  myData);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myTimestamp, myEvent, myResponse, mySource, myDestination, myEnterer, myAuthor, myReceiver, myResponsible, myReason, myData);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public IdDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new IdDt();
		}
		return myIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public String getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public MessageHeader setIdentifier(IdDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public MessageHeader setIdentifier( String theId) {
		myIdentifier = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>timestamp</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public InstantDt getTimestampElement() {  
		if (myTimestamp == null) {
			myTimestamp = new InstantDt();
		}
		return myTimestamp;
	}

	
	/**
	 * Gets the value(s) for <b>timestamp</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public Date getTimestamp() {  
		return getTimestampElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>timestamp</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public MessageHeader setTimestamp(InstantDt theValue) {
		myTimestamp = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>timestamp</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public MessageHeader setTimestampWithMillisPrecision( Date theDate) {
		myTimestamp = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>timestamp</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public MessageHeader setTimestamp( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTimestamp = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>event</b> (MessageEvent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-type\"
     * </p> 
	 */
	public CodingDt getEvent() {  
		if (myEvent == null) {
			myEvent = new CodingDt();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (MessageEvent)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-type\"
     * </p> 
	 */
	public MessageHeader setEvent(CodingDt theValue) {
		myEvent = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>response</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the message that this message is a response to.  Only present if this message is a response.
     * </p> 
	 */
	public Response getResponse() {  
		if (myResponse == null) {
			myResponse = new Response();
		}
		return myResponse;
	}

	/**
	 * Sets the value(s) for <b>response</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the message that this message is a response to.  Only present if this message is a response.
     * </p> 
	 */
	public MessageHeader setResponse(Response theValue) {
		myResponse = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The source application from which this message originated
     * </p> 
	 */
	public Source getSource() {  
		if (mySource == null) {
			mySource = new Source();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The source application from which this message originated
     * </p> 
	 */
	public MessageHeader setSource(Source theValue) {
		mySource = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>destination</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public java.util.List<Destination> getDestination() {  
		if (myDestination == null) {
			myDestination = new java.util.ArrayList<Destination>();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public MessageHeader setDestination(java.util.List<Destination> theValue) {
		myDestination = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>destination</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public Destination addDestination() {
		Destination newType = new Destination();
		getDestination().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>destination</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public Destination getDestinationFirstRep() {
		if (getDestination().isEmpty()) {
			return addDestination();
		}
		return getDestination().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>enterer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions
     * </p> 
	 */
	public ResourceReferenceDt getEnterer() {  
		if (myEnterer == null) {
			myEnterer = new ResourceReferenceDt();
		}
		return myEnterer;
	}

	/**
	 * Sets the value(s) for <b>enterer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions
     * </p> 
	 */
	public MessageHeader setEnterer(ResourceReferenceDt theValue) {
		myEnterer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions
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
     * The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions
     * </p> 
	 */
	public MessageHeader setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>receiver</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.
     * </p> 
	 */
	public ResourceReferenceDt getReceiver() {  
		if (myReceiver == null) {
			myReceiver = new ResourceReferenceDt();
		}
		return myReceiver;
	}

	/**
	 * Sets the value(s) for <b>receiver</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.
     * </p> 
	 */
	public MessageHeader setReceiver(ResourceReferenceDt theValue) {
		myReceiver = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>responsible</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party
     * </p> 
	 */
	public ResourceReferenceDt getResponsible() {  
		if (myResponsible == null) {
			myResponsible = new ResourceReferenceDt();
		}
		return myResponsible;
	}

	/**
	 * Sets the value(s) for <b>responsible</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party
     * </p> 
	 */
	public MessageHeader setResponsible(ResourceReferenceDt theValue) {
		myResponsible = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reason</b> (EventReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coded indication of the cause for the event - indicates  a reason for the occurance of the event that is a focus of this message
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (EventReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded indication of the cause for the event - indicates  a reason for the occurance of the event that is a focus of this message
     * </p> 
	 */
	public MessageHeader setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>data</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getData() {  
		if (myData == null) {
			myData = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myData;
	}

	/**
	 * Sets the value(s) for <b>data</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p> 
	 */
	public MessageHeader setData(java.util.List<ResourceReferenceDt> theValue) {
		myData = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>data</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p> 
	 */
	public ResourceReferenceDt addData() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getData().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>MessageHeader.response</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the message that this message is a response to.  Only present if this message is a response.
     * </p> 
	 */
	@Block()	
	public static class Response 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The id of the message that this message is a response to"
	)
	private IdDt myIdentifier;
	
	@Child(name="code", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ResponseType",
		formalDefinition="Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not"
	)
	private BoundCodeDt<ResponseTypeEnum> myCode;
	
	@Child(name="details", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.OperationOutcome.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Full details of any issues found in the message"
	)
	private ResourceReferenceDt myDetails;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  myDetails);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, myDetails);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public IdDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new IdDt();
		}
		return myIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public String getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public Response setIdentifier(IdDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public Response setIdentifier( String theId) {
		myIdentifier = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (ResponseType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public BoundCodeDt<ResponseTypeEnum> getCodeElement() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<ResponseTypeEnum>(ResponseTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (ResponseType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (ResponseType)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public Response setCode(BoundCodeDt<ResponseTypeEnum> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>code</b> (ResponseType)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public Response setCode(ResponseTypeEnum theValue) {
		getCodeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>details</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Full details of any issues found in the message
     * </p> 
	 */
	public ResourceReferenceDt getDetails() {  
		if (myDetails == null) {
			myDetails = new ResourceReferenceDt();
		}
		return myDetails;
	}

	/**
	 * Sets the value(s) for <b>details</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Full details of any issues found in the message
     * </p> 
	 */
	public Response setDetails(ResourceReferenceDt theValue) {
		myDetails = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>MessageHeader.source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The source application from which this message originated
     * </p> 
	 */
	@Block()	
	public static class Source 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable name for the source system"
	)
	private StringDt myName;
	
	@Child(name="software", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="May include configuration or other information useful in debugging."
	)
	private StringDt mySoftware;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Can convey versions of multiple systems in situations where a message passes through multiple hands."
	)
	private StringDt myVersion;
	
	@Child(name="contact", type=ContactPointDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An e-mail, phone, website or other contact point to use to resolve issues with message communications."
	)
	private ContactPointDt myContact;
	
	@Child(name="endpoint", type=UriDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the routing target to send acknowledgements to."
	)
	private UriDt myEndpoint;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  mySoftware,  myVersion,  myContact,  myEndpoint);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, mySoftware, myVersion, myContact, myEndpoint);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public Source setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public Source setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>software</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public StringDt getSoftwareElement() {  
		if (mySoftware == null) {
			mySoftware = new StringDt();
		}
		return mySoftware;
	}

	
	/**
	 * Gets the value(s) for <b>software</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public String getSoftware() {  
		return getSoftwareElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>software</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public Source setSoftware(StringDt theValue) {
		mySoftware = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>software</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public Source setSoftware( String theString) {
		mySoftware = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
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
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
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
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
     * </p> 
	 */
	public Source setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
     * </p> 
	 */
	public Source setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
     * </p> 
	 */
	public ContactPointDt getContact() {  
		if (myContact == null) {
			myContact = new ContactPointDt();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
     * </p> 
	 */
	public Source setContact(ContactPointDt theValue) {
		myContact = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>endpoint</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public UriDt getEndpointElement() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	
	/**
	 * Gets the value(s) for <b>endpoint</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public URI getEndpoint() {  
		return getEndpointElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public Source setEndpoint(UriDt theValue) {
		myEndpoint = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>endpoint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public Source setEndpoint( String theUri) {
		myEndpoint = new UriDt(theUri); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>MessageHeader.destination</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	@Block()	
	public static class Destination 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable name for the target system"
	)
	private StringDt myName;
	
	@Child(name="target", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the target end system in situations where the initial message transmission is to an intermediary system."
	)
	private ResourceReferenceDt myTarget;
	
	@Child(name="endpoint", type=UriDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates where the message should be routed to."
	)
	private UriDt myEndpoint;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myTarget,  myEndpoint);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myTarget, myEndpoint);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public Destination setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public Destination setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
     * </p> 
	 */
	public Destination setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>endpoint</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public UriDt getEndpointElement() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	
	/**
	 * Gets the value(s) for <b>endpoint</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public URI getEndpoint() {  
		return getEndpointElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public Destination setEndpoint(UriDt theValue) {
		myEndpoint = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>endpoint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public Destination setEndpoint( String theUri) {
		myEndpoint = new UriDt(theUri); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "MessageHeader";
    }

}
