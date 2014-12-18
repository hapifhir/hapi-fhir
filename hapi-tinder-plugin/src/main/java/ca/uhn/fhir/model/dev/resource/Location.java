















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
 * HAPI/FHIR <b>Location</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Details and position information for a physical place where services are provided  and resources and participants may be stored, found, contained or accommodated
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Location">http://hl7.org/fhir/profiles/Location</a> 
 * </p>
 *
 */
@ResourceDef(name="Location", profile="http://hl7.org/fhir/profiles/Location", id="location")
public class Location 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Location.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A (portion of the) name of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Location.name", description="A (portion of the) name of the location", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A (portion of the) name of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of location</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Location.type", description="A code for the type of location", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of location</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>A (part of the) address of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.address</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="Location.address", description="A (part of the) address of the location", type="string"  )
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>A (part of the) address of the location</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Location.address</b><br/>
	 * </p>
	 */
	public static final StringClientParam ADDRESS = new StringClientParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Searches for locations with a specific kind of status</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Location.status", description="Searches for locations with a specific kind of status", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Searches for locations with a specific kind of status</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Location.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>The location of which this location is a part</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Location.partOf</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partof", path="Location.partOf", description="The location of which this location is a part", type="reference"  )
	public static final String SP_PARTOF = "partof";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>The location of which this location is a part</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Location.partOf</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PARTOF = new ReferenceClientParam(SP_PARTOF);

	/**
	 * Search parameter constant for <b>near</b>
	 * <p>
	 * Description: <b>The coordinates expressed as [lat],[long] (using KML, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="near", path="", description="The coordinates expressed as [lat],[long] (using KML, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)", type="token"  )
	public static final String SP_NEAR = "near";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>near</b>
	 * <p>
	 * Description: <b>The coordinates expressed as [lat],[long] (using KML, see notes) to find locations near to (servers may search using a square rather than a circle for efficiency)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final TokenClientParam NEAR = new TokenClientParam(SP_NEAR);

	/**
	 * Search parameter constant for <b>near-distance</b>
	 * <p>
	 * Description: <b>A distance quantity to limit the near search to locations within a specific distance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="near-distance", path="", description="A distance quantity to limit the near search to locations within a specific distance", type="token"  )
	public static final String SP_NEAR_DISTANCE = "near-distance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>near-distance</b>
	 * <p>
	 * Description: <b>A distance quantity to limit the near search to locations within a specific distance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final TokenClientParam NEAR_DISTANCE = new TokenClientParam(SP_NEAR_DISTANCE);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>Searches for locations that are managed by the provided organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Location.managingOrganization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="Location.managingOrganization", description="Searches for locations that are managed by the provided organization", type="reference"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>Searches for locations that are managed by the provided organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Location.managingOrganization</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORGANIZATION = new ReferenceClientParam(SP_ORGANIZATION);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.address</b>".
	 */
	public static final Include INCLUDE_ADDRESS = new Include("Location.address");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Location.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.managingOrganization</b>".
	 */
	public static final Include INCLUDE_MANAGINGORGANIZATION = new Include("Location.managingOrganization");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Location.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.partOf</b>".
	 */
	public static final Include INCLUDE_PARTOF = new Include("Location.partOf");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Location.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Location.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Location.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Unique code or number identifying the location to its users"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Name of the location as used by humans. Does not need to be unique."
	)
	private StringDt myName;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Description of the Location, which helps in finding or referencing the place"
	)
	private StringDt myDescription;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="LocationType",
		formalDefinition="Indicates the type of function performed at the location"
	)
	private BoundCodeableConceptDt<LocationTypeEnum> myType;
	
	@Child(name="telecom", type=ContactPointDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private AddressDt myAddress;
	
	@Child(name="physicalType", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="PhysicalType",
		formalDefinition="Physical form of the location, e.g. building, room, vehicle, road"
	)
	private CodeableConceptDt myPhysicalType;
	
	@Child(name="position", order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)"
	)
	private Position myPosition;
	
	@Child(name="managingOrganization", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myManagingOrganization;
	
	@Child(name="status", type=CodeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="LocationStatus",
		formalDefinition=""
	)
	private BoundCodeDt<LocationStatusEnum> myStatus;
	
	@Child(name="partOf", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myPartOf;
	
	@Child(name="mode", type=CodeDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="LocationMode",
		formalDefinition="Indicates whether a resource instance represents a specific location or a class of locations"
	)
	private BoundCodeDt<LocationModeEnum> myMode;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myDescription,  myType,  myTelecom,  myAddress,  myPhysicalType,  myPosition,  myManagingOrganization,  myStatus,  myPartOf,  myMode);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myDescription, myType, myTelecom, myAddress, myPhysicalType, myPosition, myManagingOrganization, myStatus, myPartOf, myMode);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
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
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public Location setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Unique code or number identifying the location to its users
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
     * Unique code or number identifying the location to its users
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
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
     * Name of the location as used by humans. Does not need to be unique.
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
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public Location setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the location as used by humans. Does not need to be unique.
     * </p> 
	 */
	public Location setName( String theString) {
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
     * Description of the Location, which helps in finding or referencing the place
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
     * Description of the Location, which helps in finding or referencing the place
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
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public Location setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the Location, which helps in finding or referencing the place
     * </p> 
	 */
	public Location setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (LocationType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public BoundCodeableConceptDt<LocationTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<LocationTypeEnum>(LocationTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (LocationType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public Location setType(BoundCodeableConceptDt<LocationTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (LocationType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of function performed at the location
     * </p> 
	 */
	public Location setType(LocationTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
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
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public Location setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
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
     * The contact details of communication devices available at the location. This can include phone numbers, fax numbers, mobile numbers, email addresses and web sites
     * </p> 
	 */
	public ContactPointDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>address</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>physicalType</b> (PhysicalType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public CodeableConceptDt getPhysicalType() {  
		if (myPhysicalType == null) {
			myPhysicalType = new CodeableConceptDt();
		}
		return myPhysicalType;
	}

	/**
	 * Sets the value(s) for <b>physicalType</b> (PhysicalType)
	 *
     * <p>
     * <b>Definition:</b>
     * Physical form of the location, e.g. building, room, vehicle, road
     * </p> 
	 */
	public Location setPhysicalType(CodeableConceptDt theValue) {
		myPhysicalType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>position</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public Position getPosition() {  
		if (myPosition == null) {
			myPosition = new Position();
		}
		return myPosition;
	}

	/**
	 * Sets the value(s) for <b>position</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	public Location setPosition(Position theValue) {
		myPosition = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>managingOrganization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getManagingOrganization() {  
		if (myManagingOrganization == null) {
			myManagingOrganization = new ResourceReferenceDt();
		}
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for <b>managingOrganization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setManagingOrganization(ResourceReferenceDt theValue) {
		myManagingOrganization = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (LocationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<LocationStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<LocationStatusEnum>(LocationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (LocationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (LocationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setStatus(BoundCodeDt<LocationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (LocationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setStatus(LocationStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>partOf</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getPartOf() {  
		if (myPartOf == null) {
			myPartOf = new ResourceReferenceDt();
		}
		return myPartOf;
	}

	/**
	 * Sets the value(s) for <b>partOf</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setPartOf(ResourceReferenceDt theValue) {
		myPartOf = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>mode</b> (LocationMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public BoundCodeDt<LocationModeEnum> getModeElement() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<LocationModeEnum>(LocationModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	
	/**
	 * Gets the value(s) for <b>mode</b> (LocationMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public String getMode() {  
		return getModeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mode</b> (LocationMode)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public Location setMode(BoundCodeDt<LocationModeEnum> theValue) {
		myMode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>mode</b> (LocationMode)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether a resource instance represents a specific location or a class of locations
     * </p> 
	 */
	public Location setMode(LocationModeEnum theValue) {
		getModeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Block class for child element: <b>Location.position</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The absolute geographic location of the Location, expressed in a KML compatible manner (see notes below for KML)
     * </p> 
	 */
	@Block()	
	public static class Position 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="longitude", type=DecimalDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)"
	)
	private DecimalDt myLongitude;
	
	@Child(name="latitude", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)"
	)
	private DecimalDt myLatitude;
	
	@Child(name="altitude", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)"
	)
	private DecimalDt myAltitude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLongitude,  myLatitude,  myAltitude);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLongitude, myLatitude, myAltitude);
	}

	/**
	 * Gets the value(s) for <b>longitude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getLongitudeElement() {  
		if (myLongitude == null) {
			myLongitude = new DecimalDt();
		}
		return myLongitude;
	}

	
	/**
	 * Gets the value(s) for <b>longitude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public BigDecimal getLongitude() {  
		return getLongitudeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>longitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude(DecimalDt theValue) {
		myLongitude = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>longitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude( long theValue) {
		myLongitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>longitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude( double theValue) {
		myLongitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>longitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Longitude. The value domain and the interpretation are the same as for the text of the longitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLongitude( java.math.BigDecimal theValue) {
		myLongitude = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>latitude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getLatitudeElement() {  
		if (myLatitude == null) {
			myLatitude = new DecimalDt();
		}
		return myLatitude;
	}

	
	/**
	 * Gets the value(s) for <b>latitude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public BigDecimal getLatitude() {  
		return getLatitudeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>latitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude(DecimalDt theValue) {
		myLatitude = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>latitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude( long theValue) {
		myLatitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>latitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude( double theValue) {
		myLatitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>latitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Latitude. The value domain and the interpretation are the same as for the text of the latitude element in KML (see notes below)
     * </p> 
	 */
	public Position setLatitude( java.math.BigDecimal theValue) {
		myLatitude = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>altitude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public DecimalDt getAltitudeElement() {  
		if (myAltitude == null) {
			myAltitude = new DecimalDt();
		}
		return myAltitude;
	}

	
	/**
	 * Gets the value(s) for <b>altitude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public BigDecimal getAltitude() {  
		return getAltitudeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>altitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude(DecimalDt theValue) {
		myAltitude = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>altitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude( long theValue) {
		myAltitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>altitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude( double theValue) {
		myAltitude = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>altitude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Altitude. The value domain and the interpretation are the same as for the text of the altitude element in KML (see notes below)
     * </p> 
	 */
	public Position setAltitude( java.math.BigDecimal theValue) {
		myAltitude = new DecimalDt(theValue); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "Location";
    }

}
