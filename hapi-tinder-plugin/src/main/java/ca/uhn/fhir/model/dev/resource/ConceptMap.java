















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
 * HAPI/FHIR <b>ConceptMap</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ConceptMap">http://hl7.org/fhir/profiles/ConceptMap</a> 
 * </p>
 *
 */
@ResourceDef(name="ConceptMap", profile="http://hl7.org/fhir/profiles/ConceptMap", id="conceptmap")
public class ConceptMap 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the concept map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="ConceptMap.identifier", description="The identifier of the concept map", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the concept map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the concept map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.version</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="ConceptMap.version", description="The version identifier of the concept map", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the concept map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.version</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the concept map</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ConceptMap.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="ConceptMap.name", description="Name of the concept map", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the concept map</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ConceptMap.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the concept map</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ConceptMap.publisher</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="ConceptMap.publisher", description="Name of the publisher of the concept map", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the concept map</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ConceptMap.publisher</b><br/>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the concept map</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ConceptMap.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="ConceptMap.description", description="Text search in the description of the concept map", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the concept map</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ConceptMap.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the concept map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="ConceptMap.status", description="Status of the concept map", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the concept map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The concept map publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ConceptMap.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="ConceptMap.date", description="The concept map publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The concept map publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ConceptMap.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b>The system for any concepts mapped by this concept map</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ConceptMap.source[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="ConceptMap.source[x]", description="The system for any concepts mapped by this concept map", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b>The system for any concepts mapped by this concept map</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ConceptMap.source[x]</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ConceptMap.target[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="target", path="ConceptMap.target[x]", description="", type="reference"  )
	public static final String SP_TARGET = "target";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ConceptMap.target[x]</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam TARGET = new ReferenceClientParam(SP_TARGET);

	/**
	 * Search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any destination concepts mapped by this map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.element.map.codeSystem</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="system", path="ConceptMap.element.map.codeSystem", description="The system for any destination concepts mapped by this map", type="token"  )
	public static final String SP_SYSTEM = "system";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any destination concepts mapped by this map</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.element.map.codeSystem</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SYSTEM = new TokenClientParam(SP_SYSTEM);

	/**
	 * Search parameter constant for <b>dependson</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.element.dependsOn.element</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dependson", path="ConceptMap.element.dependsOn.element", description="", type="token"  )
	public static final String SP_DEPENDSON = "dependson";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dependson</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.element.dependsOn.element</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DEPENDSON = new TokenClientParam(SP_DEPENDSON);

	/**
	 * Search parameter constant for <b>product</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.element.map.product.element</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="product", path="ConceptMap.element.map.product.element", description="", type="token"  )
	public static final String SP_PRODUCT = "product";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>product</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ConceptMap.element.map.product.element</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PRODUCT = new TokenClientParam(SP_PRODUCT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("ConceptMap.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.description</b>".
	 */
	public static final Include INCLUDE_DESCRIPTION = new Include("ConceptMap.description");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.element.dependsOn.element</b>".
	 */
	public static final Include INCLUDE_ELEMENT_DEPENDSON_ELEMENT = new Include("ConceptMap.element.dependsOn.element");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.element.map.codeSystem</b>".
	 */
	public static final Include INCLUDE_ELEMENT_MAP_CODESYSTEM = new Include("ConceptMap.element.map.codeSystem");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.element.map.product.element</b>".
	 */
	public static final Include INCLUDE_ELEMENT_MAP_PRODUCT_ELEMENT = new Include("ConceptMap.element.map.product.element");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("ConceptMap.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("ConceptMap.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("ConceptMap.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.source[x]</b>".
	 */
	public static final Include INCLUDE_SOURCE = new Include("ConceptMap.source[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("ConceptMap.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.target[x]</b>".
	 */
	public static final Include INCLUDE_TARGET = new Include("ConceptMap.target[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ConceptMap.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("ConceptMap.version");


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language name describing the concept map"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of the individual or organization that published the concept map"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactPointDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contacts of the publisher to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc."
	)
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A copyright statement relating to the concept map and/or its contents"
	)
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="ValueSetStatus",
		formalDefinition="The status of the concept map"
	)
	private BoundCodeDt<ValueSetStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date that the concept map status was last changed"
	)
	private DateTimeDt myDate;
	
	@Child(name="source", order=10, min=1, max=1, type={
		UriDt.class, 		ValueSet.class, 		Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The source value set that specifies the concepts that are being mapped"
	)
	private IDatatype mySource;
	
	@Child(name="target", order=11, min=1, max=1, type={
		UriDt.class, 		ValueSet.class, 		Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made"
	)
	private IDatatype myTarget;
	
	@Child(name="element", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Mappings for an individual concept in the source to one or more concepts in the target"
	)
	private java.util.List<Element> myElement;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myCopyright,  myStatus,  myExperimental,  myDate,  mySource,  myTarget,  myElement);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myName, myPublisher, myTelecom, myDescription, myCopyright, myStatus, myExperimental, myDate, mySource, myTarget, myElement);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
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
     * The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
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
     * The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public ConceptMap setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this concept map when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public ConceptMap setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
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
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
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
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public ConceptMap setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public ConceptMap setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the concept map
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
     * A free text natural language name describing the concept map
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
     * A free text natural language name describing the concept map
     * </p> 
	 */
	public ConceptMap setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the concept map
     * </p> 
	 */
	public ConceptMap setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the concept map
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
     * The name of the individual or organization that published the concept map
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
     * The name of the individual or organization that published the concept map
     * </p> 
	 */
	public ConceptMap setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the concept map
     * </p> 
	 */
	public ConceptMap setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public java.util.List<ContactPointDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactPointDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ConceptMap setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactPointDt addTelecom() {
		ContactPointDt newType = new ContactPointDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactPointDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
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
     * A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
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
     * A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public ConceptMap setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public ConceptMap setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>copyright</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the concept map and/or its contents
     * </p> 
	 */
	public StringDt getCopyrightElement() {  
		if (myCopyright == null) {
			myCopyright = new StringDt();
		}
		return myCopyright;
	}

	
	/**
	 * Gets the value(s) for <b>copyright</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the concept map and/or its contents
     * </p> 
	 */
	public String getCopyright() {  
		return getCopyrightElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>copyright</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the concept map and/or its contents
     * </p> 
	 */
	public ConceptMap setCopyright(StringDt theValue) {
		myCopyright = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>copyright</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the concept map and/or its contents
     * </p> 
	 */
	public ConceptMap setCopyright( String theString) {
		myCopyright = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (ValueSetStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the concept map
     * </p> 
	 */
	public BoundCodeDt<ValueSetStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ValueSetStatusEnum>(ValueSetStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ValueSetStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the concept map
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ValueSetStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the concept map
     * </p> 
	 */
	public ConceptMap setStatus(BoundCodeDt<ValueSetStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ValueSetStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the concept map
     * </p> 
	 */
	public ConceptMap setStatus(ValueSetStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimentalElement() {  
		if (myExperimental == null) {
			myExperimental = new BooleanDt();
		}
		return myExperimental;
	}

	
	/**
	 * Gets the value(s) for <b>experimental</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Boolean getExperimental() {  
		return getExperimentalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>experimental</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public ConceptMap setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>experimental</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public ConceptMap setExperimental( boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the concept map status was last changed
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
     * The date that the concept map status was last changed
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
     * The date that the concept map status was last changed
     * </p> 
	 */
	public ConceptMap setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the concept map status was last changed
     * </p> 
	 */
	public ConceptMap setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the concept map status was last changed
     * </p> 
	 */
	public ConceptMap setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The source value set that specifies the concepts that are being mapped
     * </p> 
	 */
	public IDatatype getSource() {  
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The source value set that specifies the concepts that are being mapped
     * </p> 
	 */
	public ConceptMap setSource(IDatatype theValue) {
		mySource = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>target[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made
     * </p> 
	 */
	public IDatatype getTarget() {  
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made
     * </p> 
	 */
	public ConceptMap setTarget(IDatatype theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>element</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mappings for an individual concept in the source to one or more concepts in the target
     * </p> 
	 */
	public java.util.List<Element> getElement() {  
		if (myElement == null) {
			myElement = new java.util.ArrayList<Element>();
		}
		return myElement;
	}

	/**
	 * Sets the value(s) for <b>element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Mappings for an individual concept in the source to one or more concepts in the target
     * </p> 
	 */
	public ConceptMap setElement(java.util.List<Element> theValue) {
		myElement = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Mappings for an individual concept in the source to one or more concepts in the target
     * </p> 
	 */
	public Element addElement() {
		Element newType = new Element();
		getElement().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>element</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Mappings for an individual concept in the source to one or more concepts in the target
     * </p> 
	 */
	public Element getElementFirstRep() {
		if (getElement().isEmpty()) {
			return addElement();
		}
		return getElement().get(0); 
	}
  
	/**
	 * Block class for child element: <b>ConceptMap.element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Mappings for an individual concept in the source to one or more concepts in the target
     * </p> 
	 */
	@Block()	
	public static class Element 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="codeSystem", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Code System (if the source is a value value set that crosses more than one code system)"
	)
	private UriDt myCodeSystem;
	
	@Child(name="code", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="!",
		formalDefinition="Identity (code or path) or the element/item being mapped"
	)
	private CodeDt myCode;
	
	@Child(name="dependsOn", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value"
	)
	private java.util.List<ElementDependsOn> myDependsOn;
	
	@Child(name="map", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A concept from the target value set that this concept maps to"
	)
	private java.util.List<ElementMap> myMap;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCodeSystem,  myCode,  myDependsOn,  myMap);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCodeSystem, myCode, myDependsOn, myMap);
	}

	/**
	 * Gets the value(s) for <b>codeSystem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code System (if the source is a value value set that crosses more than one code system)
     * </p> 
	 */
	public UriDt getCodeSystemElement() {  
		if (myCodeSystem == null) {
			myCodeSystem = new UriDt();
		}
		return myCodeSystem;
	}

	
	/**
	 * Gets the value(s) for <b>codeSystem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code System (if the source is a value value set that crosses more than one code system)
     * </p> 
	 */
	public URI getCodeSystem() {  
		return getCodeSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>codeSystem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Code System (if the source is a value value set that crosses more than one code system)
     * </p> 
	 */
	public Element setCodeSystem(UriDt theValue) {
		myCodeSystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>codeSystem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Code System (if the source is a value value set that crosses more than one code system)
     * </p> 
	 */
	public Element setCodeSystem( String theUri) {
		myCodeSystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item being mapped
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item being mapped
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item being mapped
     * </p> 
	 */
	public Element setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item being mapped
     * </p> 
	 */
	public Element setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dependsOn</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value
     * </p> 
	 */
	public java.util.List<ElementDependsOn> getDependsOn() {  
		if (myDependsOn == null) {
			myDependsOn = new java.util.ArrayList<ElementDependsOn>();
		}
		return myDependsOn;
	}

	/**
	 * Sets the value(s) for <b>dependsOn</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value
     * </p> 
	 */
	public Element setDependsOn(java.util.List<ElementDependsOn> theValue) {
		myDependsOn = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dependsOn</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value
     * </p> 
	 */
	public ElementDependsOn addDependsOn() {
		ElementDependsOn newType = new ElementDependsOn();
		getDependsOn().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dependsOn</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value
     * </p> 
	 */
	public ElementDependsOn getDependsOnFirstRep() {
		if (getDependsOn().isEmpty()) {
			return addDependsOn();
		}
		return getDependsOn().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>map</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concept from the target value set that this concept maps to
     * </p> 
	 */
	public java.util.List<ElementMap> getMap() {  
		if (myMap == null) {
			myMap = new java.util.ArrayList<ElementMap>();
		}
		return myMap;
	}

	/**
	 * Sets the value(s) for <b>map</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A concept from the target value set that this concept maps to
     * </p> 
	 */
	public Element setMap(java.util.List<ElementMap> theValue) {
		myMap = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>map</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A concept from the target value set that this concept maps to
     * </p> 
	 */
	public ElementMap addMap() {
		ElementMap newType = new ElementMap();
		getMap().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>map</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A concept from the target value set that this concept maps to
     * </p> 
	 */
	public ElementMap getMapFirstRep() {
		if (getMap().isEmpty()) {
			return addMap();
		}
		return getMap().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>ConceptMap.element.dependsOn</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value
     * </p> 
	 */
	@Block()	
	public static class ElementDependsOn 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="element", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition"
	)
	private UriDt myElement;
	
	@Child(name="codeSystem", type=UriDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The code system of the dependency code (if the source/dependency is a value set that cross code systems)"
	)
	private UriDt myCodeSystem;
	
	@Child(name="code", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identity (code or path) or the element/item that the map depends on / refers to"
	)
	private StringDt myCode;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myElement,  myCodeSystem,  myCode);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myElement, myCodeSystem, myCode);
	}

	/**
	 * Gets the value(s) for <b>element</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition
     * </p> 
	 */
	public UriDt getElementElement() {  
		if (myElement == null) {
			myElement = new UriDt();
		}
		return myElement;
	}

	
	/**
	 * Gets the value(s) for <b>element</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition
     * </p> 
	 */
	public URI getElement() {  
		return getElementElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition
     * </p> 
	 */
	public ElementDependsOn setElement(UriDt theValue) {
		myElement = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition
     * </p> 
	 */
	public ElementDependsOn setElement( String theUri) {
		myElement = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>codeSystem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the dependency code (if the source/dependency is a value set that cross code systems)
     * </p> 
	 */
	public UriDt getCodeSystemElement() {  
		if (myCodeSystem == null) {
			myCodeSystem = new UriDt();
		}
		return myCodeSystem;
	}

	
	/**
	 * Gets the value(s) for <b>codeSystem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the dependency code (if the source/dependency is a value set that cross code systems)
     * </p> 
	 */
	public URI getCodeSystem() {  
		return getCodeSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>codeSystem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the dependency code (if the source/dependency is a value set that cross code systems)
     * </p> 
	 */
	public ElementDependsOn setCodeSystem(UriDt theValue) {
		myCodeSystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>codeSystem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the dependency code (if the source/dependency is a value set that cross code systems)
     * </p> 
	 */
	public ElementDependsOn setCodeSystem( String theUri) {
		myCodeSystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map depends on / refers to
     * </p> 
	 */
	public StringDt getCodeElement() {  
		if (myCode == null) {
			myCode = new StringDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map depends on / refers to
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map depends on / refers to
     * </p> 
	 */
	public ElementDependsOn setCode(StringDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map depends on / refers to
     * </p> 
	 */
	public ElementDependsOn setCode( String theString) {
		myCode = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>ConceptMap.element.map</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A concept from the target value set that this concept maps to
     * </p> 
	 */
	@Block()	
	public static class ElementMap 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="codeSystem", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The code system of the target code (if the target is a value set that cross code systems)"
	)
	private UriDt myCodeSystem;
	
	@Child(name="code", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="!",
		formalDefinition="Identity (code or path) or the element/item that the map refers to"
	)
	private CodeDt myCode;
	
	@Child(name="equivalence", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="ConceptMapEquivalence",
		formalDefinition="The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target"
	)
	private BoundCodeDt<ConceptMapEquivalenceEnum> myEquivalence;
	
	@Child(name="comments", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of status/issues in mapping that conveys additional information not represented in  the structured data"
	)
	private StringDt myComments;
	
	@Child(name="product", type=ElementDependsOn.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on"
	)
	private java.util.List<ElementDependsOn> myProduct;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCodeSystem,  myCode,  myEquivalence,  myComments,  myProduct);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCodeSystem, myCode, myEquivalence, myComments, myProduct);
	}

	/**
	 * Gets the value(s) for <b>codeSystem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the target code (if the target is a value set that cross code systems)
     * </p> 
	 */
	public UriDt getCodeSystemElement() {  
		if (myCodeSystem == null) {
			myCodeSystem = new UriDt();
		}
		return myCodeSystem;
	}

	
	/**
	 * Gets the value(s) for <b>codeSystem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the target code (if the target is a value set that cross code systems)
     * </p> 
	 */
	public URI getCodeSystem() {  
		return getCodeSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>codeSystem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the target code (if the target is a value set that cross code systems)
     * </p> 
	 */
	public ElementMap setCodeSystem(UriDt theValue) {
		myCodeSystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>codeSystem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The code system of the target code (if the target is a value set that cross code systems)
     * </p> 
	 */
	public ElementMap setCodeSystem( String theUri) {
		myCodeSystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map refers to
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map refers to
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map refers to
     * </p> 
	 */
	public ElementMap setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * Identity (code or path) or the element/item that the map refers to
     * </p> 
	 */
	public ElementMap setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>equivalence</b> (ConceptMapEquivalence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target
     * </p> 
	 */
	public BoundCodeDt<ConceptMapEquivalenceEnum> getEquivalenceElement() {  
		if (myEquivalence == null) {
			myEquivalence = new BoundCodeDt<ConceptMapEquivalenceEnum>(ConceptMapEquivalenceEnum.VALUESET_BINDER);
		}
		return myEquivalence;
	}

	
	/**
	 * Gets the value(s) for <b>equivalence</b> (ConceptMapEquivalence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target
     * </p> 
	 */
	public String getEquivalence() {  
		return getEquivalenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>equivalence</b> (ConceptMapEquivalence)
	 *
     * <p>
     * <b>Definition:</b>
     * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target
     * </p> 
	 */
	public ElementMap setEquivalence(BoundCodeDt<ConceptMapEquivalenceEnum> theValue) {
		myEquivalence = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>equivalence</b> (ConceptMapEquivalence)
	 *
     * <p>
     * <b>Definition:</b>
     * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from source to target (e.g. the source is 'wider' than the target
     * </p> 
	 */
	public ElementMap setEquivalence(ConceptMapEquivalenceEnum theValue) {
		getEquivalenceElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of status/issues in mapping that conveys additional information not represented in  the structured data
     * </p> 
	 */
	public StringDt getCommentsElement() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of status/issues in mapping that conveys additional information not represented in  the structured data
     * </p> 
	 */
	public String getComments() {  
		return getCommentsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of status/issues in mapping that conveys additional information not represented in  the structured data
     * </p> 
	 */
	public ElementMap setComments(StringDt theValue) {
		myComments = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of status/issues in mapping that conveys additional information not represented in  the structured data
     * </p> 
	 */
	public ElementMap setComments( String theString) {
		myComments = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>product</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on
     * </p> 
	 */
	public java.util.List<ElementDependsOn> getProduct() {  
		if (myProduct == null) {
			myProduct = new java.util.ArrayList<ElementDependsOn>();
		}
		return myProduct;
	}

	/**
	 * Sets the value(s) for <b>product</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on
     * </p> 
	 */
	public ElementMap setProduct(java.util.List<ElementDependsOn> theValue) {
		myProduct = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>product</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on
     * </p> 
	 */
	public ElementDependsOn addProduct() {
		ElementDependsOn newType = new ElementDependsOn();
		getProduct().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>product</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on
     * </p> 
	 */
	public ElementDependsOn getProductFirstRep() {
		if (getProduct().isEmpty()) {
			return addProduct();
		}
		return getProduct().get(0); 
	}
  

	}





    @Override
    public String getResourceName() {
        return "ConceptMap";
    }

}
