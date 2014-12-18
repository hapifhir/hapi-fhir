















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
 * HAPI/FHIR <b>SecurityEvent</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SecurityEvent">http://hl7.org/fhir/profiles/SecurityEvent</a> 
 * </p>
 *
 */
@ResourceDef(name="SecurityEvent", profile="http://hl7.org/fhir/profiles/SecurityEvent", id="securityevent")
public class SecurityEvent 
    extends  ca.uhn.fhir.model.base.resource.BaseSecurityEvent     implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="SecurityEvent.event.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>action</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.action</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="action", path="SecurityEvent.event.action", description="", type="token"  )
	public static final String SP_ACTION = "action";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>action</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.action</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTION = new TokenClientParam(SP_ACTION);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SecurityEvent.event.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SecurityEvent.event.dateTime", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SecurityEvent.event.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subtype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.subtype</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subtype", path="SecurityEvent.event.subtype", description="", type="token"  )
	public static final String SP_SUBTYPE = "subtype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subtype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.subtype</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SUBTYPE = new TokenClientParam(SP_SUBTYPE);

	/**
	 * Search parameter constant for <b>user</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.userId</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="user", path="SecurityEvent.participant.userId", description="", type="token"  )
	public static final String SP_USER = "user";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>user</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.userId</b><br/>
	 * </p>
	 */
	public static final TokenClientParam USER = new TokenClientParam(SP_USER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.participant.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="SecurityEvent.participant.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.participant.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.network.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="SecurityEvent.participant.network.identifier", description="", type="token"  )
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.network.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ADDRESS = new TokenClientParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="SecurityEvent.source.identifier", description="", type="token"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SOURCE = new TokenClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>site</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.site</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="site", path="SecurityEvent.source.site", description="", type="token"  )
	public static final String SP_SITE = "site";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>site</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.site</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SITE = new TokenClientParam(SP_SITE);

	/**
	 * Search parameter constant for <b>object-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="object-type", path="SecurityEvent.object.type", description="", type="token"  )
	public static final String SP_OBJECT_TYPE = "object-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>object-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam OBJECT_TYPE = new TokenClientParam(SP_OBJECT_TYPE);

	/**
	 * Search parameter constant for <b>identity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identity", path="SecurityEvent.object.identifier", description="", type="token"  )
	public static final String SP_IDENTITY = "identity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTITY = new TokenClientParam(SP_IDENTITY);

	/**
	 * Search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityEvent.object.reference</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reference", path="SecurityEvent.object.reference", description="", type="reference"  )
	public static final String SP_REFERENCE = "reference";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityEvent.object.reference</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REFERENCE = new ReferenceClientParam(SP_REFERENCE);

	/**
	 * Search parameter constant for <b>desc</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.object.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="desc", path="SecurityEvent.object.name", description="", type="string"  )
	public static final String SP_DESC = "desc";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>desc</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.object.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESC = new StringClientParam(SP_DESC);

	/**
	 * Search parameter constant for <b>patientid</b>
	 * <p>
	 * Description: <b>The id of the patient (one of multiple kinds of participations)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patientid", path="", description="The id of the patient (one of multiple kinds of participations)", type="token"  )
	public static final String SP_PATIENTID = "patientid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patientid</b>
	 * <p>
	 * Description: <b>The id of the patient (one of multiple kinds of participations)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final TokenClientParam PATIENTID = new TokenClientParam(SP_PATIENTID);

	/**
	 * Search parameter constant for <b>altid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.altId</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="altid", path="SecurityEvent.participant.altId", description="", type="token"  )
	public static final String SP_ALTID = "altid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>altid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.altId</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ALTID = new TokenClientParam(SP_ALTID);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>A patient that the .object.reference refers to </b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="", description="A patient that the .object.reference refers to ", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>A patient that the .object.reference refers to </b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.event.action</b>".
	 */
	public static final Include INCLUDE_EVENT_ACTION = new Include("SecurityEvent.event.action");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.event.dateTime</b>".
	 */
	public static final Include INCLUDE_EVENT_DATETIME = new Include("SecurityEvent.event.dateTime");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.event.subtype</b>".
	 */
	public static final Include INCLUDE_EVENT_SUBTYPE = new Include("SecurityEvent.event.subtype");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.event.type</b>".
	 */
	public static final Include INCLUDE_EVENT_TYPE = new Include("SecurityEvent.event.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.object.identifier</b>".
	 */
	public static final Include INCLUDE_OBJECT_IDENTIFIER = new Include("SecurityEvent.object.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.object.name</b>".
	 */
	public static final Include INCLUDE_OBJECT_NAME = new Include("SecurityEvent.object.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.object.reference</b>".
	 */
	public static final Include INCLUDE_OBJECT_REFERENCE = new Include("SecurityEvent.object.reference");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.object.type</b>".
	 */
	public static final Include INCLUDE_OBJECT_TYPE = new Include("SecurityEvent.object.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.participant.altId</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_ALTID = new Include("SecurityEvent.participant.altId");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.participant.name</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_NAME = new Include("SecurityEvent.participant.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.participant.network.identifier</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_NETWORK_IDENTIFIER = new Include("SecurityEvent.participant.network.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.participant.userId</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_USERID = new Include("SecurityEvent.participant.userId");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.source.identifier</b>".
	 */
	public static final Include INCLUDE_SOURCE_IDENTIFIER = new Include("SecurityEvent.source.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.source.site</b>".
	 */
	public static final Include INCLUDE_SOURCE_SITE = new Include("SecurityEvent.source.site");


	@Child(name="event", order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the name, action type, time, and disposition of the audited event"
	)
	private Event myEvent;
	
	@Child(name="participant", order=1, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="source", order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private Source mySource;
	
	@Child(name="object", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Specific instances of data or objects that have been accessed"
	)
	private java.util.List<ObjectElement> myObject;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEvent,  myParticipant,  mySource,  myObject);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myEvent, myParticipant, mySource, myObject);
	}

	/**
	 * Gets the value(s) for <b>event</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name, action type, time, and disposition of the audited event
     * </p> 
	 */
	public Event getEvent() {  
		if (myEvent == null) {
			myEvent = new Event();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name, action type, time, and disposition of the audited event
     * </p> 
	 */
	public SecurityEvent setEvent(Event theValue) {
		myEvent = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>participant</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Participant> getParticipant() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}

	/**
	 * Sets the value(s) for <b>participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SecurityEvent setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant addParticipant() {
		Participant newType = new Participant();
		getParticipant().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>participant</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public SecurityEvent setSource(Source theValue) {
		mySource = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>object</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public java.util.List<ObjectElement> getObject() {  
		if (myObject == null) {
			myObject = new java.util.ArrayList<ObjectElement>();
		}
		return myObject;
	}

	/**
	 * Sets the value(s) for <b>object</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public SecurityEvent setObject(java.util.List<ObjectElement> theValue) {
		myObject = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>object</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public ObjectElement addObject() {
		ObjectElement newType = new ObjectElement();
		getObject().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>object</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public ObjectElement getObjectFirstRep() {
		if (getObject().isEmpty()) {
			return addObject();
		}
		return getObject().get(0); 
	}
  
	/**
	 * Block class for child element: <b>SecurityEvent.event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name, action type, time, and disposition of the audited event
     * </p> 
	 */
	@Block()	
	public static class Event 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="SecurityEventType",
		formalDefinition="Identifier for a family of the event"
	)
	private CodeableConceptDt myType;
	
	@Child(name="subtype", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="SecurityEventSubType",
		formalDefinition="Identifier for the category of event"
	)
	private java.util.List<CodeableConceptDt> mySubtype;
	
	@Child(name="action", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventAction",
		formalDefinition="Indicator for type of action performed during the event that generated the audit"
	)
	private BoundCodeDt<SecurityEventActionEnum> myAction;
	
	@Child(name="dateTime", type=InstantDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The time when the event occurred on the source"
	)
	private InstantDt myDateTime;
	
	@Child(name="outcome", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventOutcome",
		formalDefinition="Indicates whether the event succeeded or failed"
	)
	private BoundCodeDt<SecurityEventOutcomeEnum> myOutcome;
	
	@Child(name="outcomeDesc", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text description of the outcome of the event"
	)
	private StringDt myOutcomeDesc;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySubtype,  myAction,  myDateTime,  myOutcome,  myOutcomeDesc);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySubtype, myAction, myDateTime, myOutcome, myOutcomeDesc);
	}

	/**
	 * Gets the value(s) for <b>type</b> (SecurityEventType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a family of the event
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (SecurityEventType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a family of the event
     * </p> 
	 */
	public Event setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subtype</b> (SecurityEventSubType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSubtype() {  
		if (mySubtype == null) {
			mySubtype = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySubtype;
	}

	/**
	 * Sets the value(s) for <b>subtype</b> (SecurityEventSubType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public Event setSubtype(java.util.List<CodeableConceptDt> theValue) {
		mySubtype = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>subtype</b> (SecurityEventSubType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public CodeableConceptDt addSubtype() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getSubtype().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>subtype</b> (SecurityEventSubType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public CodeableConceptDt getSubtypeFirstRep() {
		if (getSubtype().isEmpty()) {
			return addSubtype();
		}
		return getSubtype().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>action</b> (SecurityEventAction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public BoundCodeDt<SecurityEventActionEnum> getActionElement() {  
		if (myAction == null) {
			myAction = new BoundCodeDt<SecurityEventActionEnum>(SecurityEventActionEnum.VALUESET_BINDER);
		}
		return myAction;
	}

	
	/**
	 * Gets the value(s) for <b>action</b> (SecurityEventAction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public String getAction() {  
		return getActionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>action</b> (SecurityEventAction)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public Event setAction(BoundCodeDt<SecurityEventActionEnum> theValue) {
		myAction = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>action</b> (SecurityEventAction)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public Event setAction(SecurityEventActionEnum theValue) {
		getActionElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public InstantDt getDateTimeElement() {  
		if (myDateTime == null) {
			myDateTime = new InstantDt();
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
     * The time when the event occurred on the source
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
     * The time when the event occurred on the source
     * </p> 
	 */
	public Event setDateTime(InstantDt theValue) {
		myDateTime = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public Event setDateTimeWithMillisPrecision( Date theDate) {
		myDateTime = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public Event setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>outcome</b> (SecurityEventOutcome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public BoundCodeDt<SecurityEventOutcomeEnum> getOutcomeElement() {  
		if (myOutcome == null) {
			myOutcome = new BoundCodeDt<SecurityEventOutcomeEnum>(SecurityEventOutcomeEnum.VALUESET_BINDER);
		}
		return myOutcome;
	}

	
	/**
	 * Gets the value(s) for <b>outcome</b> (SecurityEventOutcome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public String getOutcome() {  
		return getOutcomeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (SecurityEventOutcome)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public Event setOutcome(BoundCodeDt<SecurityEventOutcomeEnum> theValue) {
		myOutcome = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>outcome</b> (SecurityEventOutcome)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public Event setOutcome(SecurityEventOutcomeEnum theValue) {
		getOutcomeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>outcomeDesc</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public StringDt getOutcomeDescElement() {  
		if (myOutcomeDesc == null) {
			myOutcomeDesc = new StringDt();
		}
		return myOutcomeDesc;
	}

	
	/**
	 * Gets the value(s) for <b>outcomeDesc</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public String getOutcomeDesc() {  
		return getOutcomeDescElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>outcomeDesc</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public Event setOutcomeDesc(StringDt theValue) {
		myOutcomeDesc = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>outcomeDesc</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public Event setOutcomeDesc( String theString) {
		myOutcomeDesc = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>SecurityEvent.participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Participant 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="role", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="DICOMRoleId",
		formalDefinition="Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context"
	)
	private java.util.List<CodeableConceptDt> myRole;
	
	@Child(name="reference", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Direct reference to a resource that identifies the participant"
	)
	private ResourceReferenceDt myReference;
	
	@Child(name="userId", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Unique identifier for the user actively participating in the event"
	)
	private StringDt myUserId;
	
	@Child(name="altId", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available"
	)
	private StringDt myAltId;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-meaningful name for the user"
	)
	private StringDt myName;
	
	@Child(name="requestor", type=BooleanDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicator that the user is or is not the requestor, or initiator, for the event being audited."
	)
	private BooleanDt myRequestor;
	
	@Child(name="media", type=CodingDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Type of media involved. Used when the event is about exporting/importing onto media"
	)
	private CodingDt myMedia;
	
	@Child(name="network", order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Logical network location for application activity, if the activity has a network location"
	)
	private ParticipantNetwork myNetwork;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myReference,  myUserId,  myAltId,  myName,  myRequestor,  myMedia,  myNetwork);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myReference, myUserId, myAltId, myName, myRequestor, myMedia, myNetwork);
	}

	/**
	 * Gets the value(s) for <b>role</b> (DICOMRoleId).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRole() {  
		if (myRole == null) {
			myRole = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (DICOMRoleId)
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public Participant setRole(java.util.List<CodeableConceptDt> theValue) {
		myRole = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>role</b> (DICOMRoleId)
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public CodeableConceptDt addRole() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRole().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>role</b> (DICOMRoleId),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public CodeableConceptDt getRoleFirstRep() {
		if (getRole().isEmpty()) {
			return addRole();
		}
		return getRole().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Direct reference to a resource that identifies the participant
     * </p> 
	 */
	public ResourceReferenceDt getReference() {  
		if (myReference == null) {
			myReference = new ResourceReferenceDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Direct reference to a resource that identifies the participant
     * </p> 
	 */
	public Participant setReference(ResourceReferenceDt theValue) {
		myReference = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>userId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public StringDt getUserIdElement() {  
		if (myUserId == null) {
			myUserId = new StringDt();
		}
		return myUserId;
	}

	
	/**
	 * Gets the value(s) for <b>userId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public String getUserId() {  
		return getUserIdElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>userId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public Participant setUserId(StringDt theValue) {
		myUserId = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>userId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public Participant setUserId( String theString) {
		myUserId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>altId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public StringDt getAltIdElement() {  
		if (myAltId == null) {
			myAltId = new StringDt();
		}
		return myAltId;
	}

	
	/**
	 * Gets the value(s) for <b>altId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public String getAltId() {  
		return getAltIdElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>altId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public Participant setAltId(StringDt theValue) {
		myAltId = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>altId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public Participant setAltId( String theString) {
		myAltId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-meaningful name for the user
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
     * Human-meaningful name for the user
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
     * Human-meaningful name for the user
     * </p> 
	 */
	public Participant setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-meaningful name for the user
     * </p> 
	 */
	public Participant setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requestor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public BooleanDt getRequestorElement() {  
		if (myRequestor == null) {
			myRequestor = new BooleanDt();
		}
		return myRequestor;
	}

	
	/**
	 * Gets the value(s) for <b>requestor</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public Boolean getRequestor() {  
		return getRequestorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requestor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public Participant setRequestor(BooleanDt theValue) {
		myRequestor = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requestor</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public Participant setRequestor( boolean theBoolean) {
		myRequestor = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>media</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of media involved. Used when the event is about exporting/importing onto media
     * </p> 
	 */
	public CodingDt getMedia() {  
		if (myMedia == null) {
			myMedia = new CodingDt();
		}
		return myMedia;
	}

	/**
	 * Sets the value(s) for <b>media</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Type of media involved. Used when the event is about exporting/importing onto media
     * </p> 
	 */
	public Participant setMedia(CodingDt theValue) {
		myMedia = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>network</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical network location for application activity, if the activity has a network location
     * </p> 
	 */
	public ParticipantNetwork getNetwork() {  
		if (myNetwork == null) {
			myNetwork = new ParticipantNetwork();
		}
		return myNetwork;
	}

	/**
	 * Sets the value(s) for <b>network</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Logical network location for application activity, if the activity has a network location
     * </p> 
	 */
	public Participant setNetwork(ParticipantNetwork theValue) {
		myNetwork = theValue;
		return this;
	}
	
	

  

	}

	/**
	 * Block class for child element: <b>SecurityEvent.participant.network</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Logical network location for application activity, if the activity has a network location
     * </p> 
	 */
	@Block()	
	public static class ParticipantNetwork 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier for the network access point of the user device for the audit event"
	)
	private StringDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventParticipantNetworkType",
		formalDefinition="An identifier for the type of network access point that originated the audit event"
	)
	private BoundCodeDt<SecurityEventParticipantNetworkTypeEnum> myType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the network access point of the user device for the audit event
     * </p> 
	 */
	public StringDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
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
     * An identifier for the network access point of the user device for the audit event
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
     * An identifier for the network access point of the user device for the audit event
     * </p> 
	 */
	public ParticipantNetwork setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the network access point of the user device for the audit event
     * </p> 
	 */
	public ParticipantNetwork setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (SecurityEventParticipantNetworkType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public BoundCodeDt<SecurityEventParticipantNetworkTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<SecurityEventParticipantNetworkTypeEnum>(SecurityEventParticipantNetworkTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (SecurityEventParticipantNetworkType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (SecurityEventParticipantNetworkType)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public ParticipantNetwork setType(BoundCodeDt<SecurityEventParticipantNetworkTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (SecurityEventParticipantNetworkType)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public ParticipantNetwork setType(SecurityEventParticipantNetworkTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  

	}



	/**
	 * Block class for child element: <b>SecurityEvent.source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Source 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="site", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Logical source location within the healthcare enterprise network"
	)
	private StringDt mySite;
	
	@Child(name="identifier", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier of the source where the event originated"
	)
	private StringDt myIdentifier;
	
	@Child(name="type", type=CodingDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="SecurityEventSourceType",
		formalDefinition="Code specifying the type of source where event originated"
	)
	private java.util.List<CodingDt> myType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySite,  myIdentifier,  myType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySite, myIdentifier, myType);
	}

	/**
	 * Gets the value(s) for <b>site</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public StringDt getSiteElement() {  
		if (mySite == null) {
			mySite = new StringDt();
		}
		return mySite;
	}

	
	/**
	 * Gets the value(s) for <b>site</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public String getSite() {  
		return getSiteElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>site</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public Source setSite(StringDt theValue) {
		mySite = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>site</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public Source setSite( String theString) {
		mySite = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the source where the event originated
     * </p> 
	 */
	public StringDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
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
     * Identifier of the source where the event originated
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
     * Identifier of the source where the event originated
     * </p> 
	 */
	public Source setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the source where the event originated
     * </p> 
	 */
	public Source setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (SecurityEventSourceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public java.util.List<CodingDt> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<CodingDt>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (SecurityEventSourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public Source setType(java.util.List<CodingDt> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>type</b> (SecurityEventSourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public CodingDt addType() {
		CodingDt newType = new CodingDt();
		getType().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>type</b> (SecurityEventSourceType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public CodingDt getTypeFirstRep() {
		if (getType().isEmpty()) {
			return addType();
		}
		return getType().get(0); 
	}
  

	}


	/**
	 * Block class for child element: <b>SecurityEvent.object</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	@Block()	
	public static class ObjectElement 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="reference", order=1, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific"
	)
	private ResourceReferenceDt myReference;
	
	@Child(name="type", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventObjectType",
		formalDefinition="Object type being audited"
	)
	private BoundCodeDt<SecurityEventObjectTypeEnum> myType;
	
	@Child(name="role", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventObjectRole",
		formalDefinition="Code representing the functional application role of Participant Object being audited"
	)
	private BoundCodeDt<SecurityEventObjectRoleEnum> myRole;
	
	@Child(name="lifecycle", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventObjectLifecycle",
		formalDefinition="Identifier for the data life-cycle stage for the participant object"
	)
	private BoundCodeDt<SecurityEventObjectLifecycleEnum> myLifecycle;
	
	@Child(name="sensitivity", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="SecurityEventObjectSensitivity",
		formalDefinition="Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics"
	)
	private BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum> mySensitivity;
	
	@Child(name="name", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An instance-specific descriptor of the Participant Object ID audited, such as a person's name"
	)
	private StringDt myName;
	
	@Child(name="description", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Text that describes the object in more detail"
	)
	private StringDt myDescription;
	
	@Child(name="query", type=Base64BinaryDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The actual query for a query-type participant object"
	)
	private Base64BinaryDt myQuery;
	
	@Child(name="detail", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<ObjectDetail> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myReference,  myType,  myRole,  myLifecycle,  mySensitivity,  myName,  myDescription,  myQuery,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myReference, myType, myRole, myLifecycle, mySensitivity, myName, myDescription, myQuery, myDetail);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
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
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public ObjectElement setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public ResourceReferenceDt getReference() {  
		if (myReference == null) {
			myReference = new ResourceReferenceDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public ObjectElement setReference(ResourceReferenceDt theValue) {
		myReference = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (SecurityEventObjectType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public BoundCodeDt<SecurityEventObjectTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<SecurityEventObjectTypeEnum>(SecurityEventObjectTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (SecurityEventObjectType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (SecurityEventObjectType)
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public ObjectElement setType(BoundCodeDt<SecurityEventObjectTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (SecurityEventObjectType)
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public ObjectElement setType(SecurityEventObjectTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>role</b> (SecurityEventObjectRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public BoundCodeDt<SecurityEventObjectRoleEnum> getRoleElement() {  
		if (myRole == null) {
			myRole = new BoundCodeDt<SecurityEventObjectRoleEnum>(SecurityEventObjectRoleEnum.VALUESET_BINDER);
		}
		return myRole;
	}

	
	/**
	 * Gets the value(s) for <b>role</b> (SecurityEventObjectRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public String getRole() {  
		return getRoleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>role</b> (SecurityEventObjectRole)
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public ObjectElement setRole(BoundCodeDt<SecurityEventObjectRoleEnum> theValue) {
		myRole = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>role</b> (SecurityEventObjectRole)
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public ObjectElement setRole(SecurityEventObjectRoleEnum theValue) {
		getRoleElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>lifecycle</b> (SecurityEventObjectLifecycle).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public BoundCodeDt<SecurityEventObjectLifecycleEnum> getLifecycleElement() {  
		if (myLifecycle == null) {
			myLifecycle = new BoundCodeDt<SecurityEventObjectLifecycleEnum>(SecurityEventObjectLifecycleEnum.VALUESET_BINDER);
		}
		return myLifecycle;
	}

	
	/**
	 * Gets the value(s) for <b>lifecycle</b> (SecurityEventObjectLifecycle).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public String getLifecycle() {  
		return getLifecycleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lifecycle</b> (SecurityEventObjectLifecycle)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public ObjectElement setLifecycle(BoundCodeDt<SecurityEventObjectLifecycleEnum> theValue) {
		myLifecycle = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>lifecycle</b> (SecurityEventObjectLifecycle)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public ObjectElement setLifecycle(SecurityEventObjectLifecycleEnum theValue) {
		getLifecycleElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sensitivity</b> (SecurityEventObjectSensitivity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
     * </p> 
	 */
	public BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum> getSensitivity() {  
		if (mySensitivity == null) {
			mySensitivity = new BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum>(SecurityEventObjectSensitivityEnum.VALUESET_BINDER);
		}
		return mySensitivity;
	}

	/**
	 * Sets the value(s) for <b>sensitivity</b> (SecurityEventObjectSensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
     * </p> 
	 */
	public ObjectElement setSensitivity(BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum> theValue) {
		mySensitivity = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>sensitivity</b> (SecurityEventObjectSensitivity)
	 *
     * <p>
     * <b>Definition:</b>
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
     * </p> 
	 */
	public ObjectElement setSensitivity(SecurityEventObjectSensitivityEnum theValue) {
		getSensitivity().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
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
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
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
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
     * </p> 
	 */
	public ObjectElement setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
     * </p> 
	 */
	public ObjectElement setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text that describes the object in more detail
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
     * Text that describes the object in more detail
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
     * Text that describes the object in more detail
     * </p> 
	 */
	public ObjectElement setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Text that describes the object in more detail
     * </p> 
	 */
	public ObjectElement setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>query</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public Base64BinaryDt getQueryElement() {  
		if (myQuery == null) {
			myQuery = new Base64BinaryDt();
		}
		return myQuery;
	}

	
	/**
	 * Gets the value(s) for <b>query</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public byte[] getQuery() {  
		return getQueryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>query</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public ObjectElement setQuery(Base64BinaryDt theValue) {
		myQuery = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>query</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public ObjectElement setQuery( byte[] theBytes) {
		myQuery = new Base64BinaryDt(theBytes); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ObjectDetail> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ObjectDetail>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectElement setDetail(java.util.List<ObjectDetail> theValue) {
		myDetail = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail addDetail() {
		ObjectDetail newType = new ObjectDetail();
		getDetail().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>detail</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail getDetailFirstRep() {
		if (getDetail().isEmpty()) {
			return addDetail();
		}
		return getDetail().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>SecurityEvent.object.detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class ObjectDetail 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myType;
	
	@Child(name="value", type=Base64BinaryDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private Base64BinaryDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myValue);
	}

	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getTypeElement() {  
		if (myType == null) {
			myType = new StringDt();
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setType(StringDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setType( String theString) {
		myType = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Base64BinaryDt getValueElement() {  
		if (myValue == null) {
			myValue = new Base64BinaryDt();
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
     * 
     * </p> 
	 */
	public byte[] getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setValue(Base64BinaryDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setValue( byte[] theBytes) {
		myValue = new Base64BinaryDt(theBytes); 
		return this; 
	}

 

	}





    @Override
    public String getResourceName() {
        return "SecurityEvent";
    }

}
