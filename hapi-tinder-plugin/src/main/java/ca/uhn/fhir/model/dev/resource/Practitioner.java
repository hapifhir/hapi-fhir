















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
 * HAPI/FHIR <b>Practitioner</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A person who is directly or indirectly involved in the provisioning of healthcare
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to track doctors, staff, locums etc. for both healthcare practitioners, funders, etc.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Practitioner">http://hl7.org/fhir/profiles/Practitioner</a> 
 * </p>
 *
 */
@ResourceDef(name="Practitioner", profile="http://hl7.org/fhir/profiles/Practitioner", id="practitioner")
public class Practitioner 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A practitioner's Identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Practitioner.identifier", description="A practitioner's Identifier", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A practitioner's Identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Practitioner.name", description="A portion of either family or given name", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="family", path="Practitioner.name", description="A portion of the family name", type="string"  )
	public static final String SP_FAMILY = "family";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam FAMILY = new StringClientParam(SP_FAMILY);

	/**
	 * Search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="given", path="Practitioner.name", description="A portion of the given name", type="string"  )
	public static final String SP_GIVEN = "given";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam GIVEN = new StringClientParam(SP_GIVEN);

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="phonetic", path="Practitioner.name", description="A portion of either family or given name using some kind of phonetic matching algorithm", type="string"  )
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam PHONETIC = new StringClientParam(SP_PHONETIC);

	/**
	 * Search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of contact</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.telecom</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="telecom", path="Practitioner.telecom", description="The value in any kind of contact", type="string"  )
	public static final String SP_TELECOM = "telecom";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of contact</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.telecom</b><br/>
	 * </p>
	 */
	public static final StringClientParam TELECOM = new StringClientParam(SP_TELECOM);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.address</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="Practitioner.address", description="An address in any kind of address/part", type="string"  )
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Practitioner.address</b><br/>
	 * </p>
	 */
	public static final StringClientParam ADDRESS = new StringClientParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the practitioner</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.gender</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="gender", path="Practitioner.gender", description="Gender of the practitioner", type="token"  )
	public static final String SP_GENDER = "gender";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the practitioner</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.gender</b><br/>
	 * </p>
	 */
	public static final TokenClientParam GENDER = new TokenClientParam(SP_GENDER);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Practitioner.organization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="Practitioner.organization", description="The identity of the organization the practitioner represents / acts on behalf of", type="reference"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Practitioner.organization</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORGANIZATION = new ReferenceClientParam(SP_ORGANIZATION);

	/**
	 * Search parameter constant for <b>communication</b>
	 * <p>
	 * Description: <b>One of the languages that the practitioner can communicate with</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.communication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="communication", path="Practitioner.communication", description="One of the languages that the practitioner can communicate with", type="token"  )
	public static final String SP_COMMUNICATION = "communication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>communication</b>
	 * <p>
	 * Description: <b>One of the languages that the practitioner can communicate with</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Practitioner.communication</b><br/>
	 * </p>
	 */
	public static final TokenClientParam COMMUNICATION = new TokenClientParam(SP_COMMUNICATION);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>One of the locations at which this practitioner provides care</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Practitioner.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Practitioner.location", description="One of the locations at which this practitioner provides care", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>One of the locations at which this practitioner provides care</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Practitioner.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.address</b>".
	 */
	public static final Include INCLUDE_ADDRESS = new Include("Practitioner.address");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.communication</b>".
	 */
	public static final Include INCLUDE_COMMUNICATION = new Include("Practitioner.communication");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.gender</b>".
	 */
	public static final Include INCLUDE_GENDER = new Include("Practitioner.gender");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Practitioner.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Practitioner.location");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Practitioner.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.organization</b>".
	 */
	public static final Include INCLUDE_ORGANIZATION = new Include("Practitioner.organization");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Practitioner.telecom</b>".
	 */
	public static final Include INCLUDE_TELECOM = new Include("Practitioner.telecom");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that applies to this person in this role"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A name associated with the person"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A contact detail for the practitioner, e.g. a telephone number or an email address."
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The postal address where the practitioner can be found or visited or to which mail can be delivered"
	)
	private java.util.List<AddressDt> myAddress;
	
	@Child(name="gender", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="AdministrativeGender",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeDt<AdministrativeGenderEnum> myGender;
	
	@Child(name="birthDate", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date and time of birth for the practitioner"
	)
	private DateTimeDt myBirthDate;
	
	@Child(name="photo", type=AttachmentDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Image of the person"
	)
	private java.util.List<AttachmentDt> myPhoto;
	
	@Child(name="organization", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The organization that the practitioner represents"
	)
	private ResourceReferenceDt myOrganization;
	
	@Child(name="role", type=CodeableConceptDt.class, order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="PractitionerRole",
		formalDefinition="Roles which this practitioner is authorized to perform for the organization"
	)
	private java.util.List<BoundCodeableConceptDt<PractitionerRoleEnum>> myRole;
	
	@Child(name="specialty", type=CodeableConceptDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="PractitionerSpecialty",
		formalDefinition="Specific specialty of the practitioner"
	)
	private java.util.List<BoundCodeableConceptDt<PractitionerSpecialtyEnum>> mySpecialty;
	
	@Child(name="period", type=PeriodDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The period during which the person is authorized to act as a practitioner in these role(s) for the organization"
	)
	private PeriodDt myPeriod;
	
	@Child(name="location", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The location(s) at which this practitioner provides care"
	)
	private java.util.List<ResourceReferenceDt> myLocation;
	
	@Child(name="qualification", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<Qualification> myQualification;
	
	@Child(name="communication", type=CodeableConceptDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Language",
		formalDefinition="A language the practitioner is able to use in patient communication"
	)
	private java.util.List<CodeableConceptDt> myCommunication;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myTelecom,  myAddress,  myGender,  myBirthDate,  myPhoto,  myOrganization,  myRole,  mySpecialty,  myPeriod,  myLocation,  myQualification,  myCommunication);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myTelecom, myAddress, myGender, myBirthDate, myPhoto, myOrganization, myRole, mySpecialty, myPeriod, myLocation, myQualification, myCommunication);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
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
     * An identifier that applies to this person in this role
     * </p> 
	 */
	public Practitioner setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person in this role
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
     * An identifier that applies to this person in this role
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
     * A name associated with the person
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public Practitioner setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
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
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * </p> 
	 */
	public Practitioner setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
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
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
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
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public java.util.List<AddressDt> getAddress() {  
		if (myAddress == null) {
			myAddress = new java.util.ArrayList<AddressDt>();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public Practitioner setAddress(java.util.List<AddressDt> theValue) {
		myAddress = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public AddressDt addAddress() {
		AddressDt newType = new AddressDt();
		getAddress().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>address</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The postal address where the practitioner can be found or visited or to which mail can be delivered
     * </p> 
	 */
	public AddressDt getAddressFirstRep() {
		if (getAddress().isEmpty()) {
			return addAddress();
		}
		return getAddress().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>gender</b> (AdministrativeGender).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public BoundCodeDt<AdministrativeGenderEnum> getGenderElement() {  
		if (myGender == null) {
			myGender = new BoundCodeDt<AdministrativeGenderEnum>(AdministrativeGenderEnum.VALUESET_BINDER);
		}
		return myGender;
	}

	
	/**
	 * Gets the value(s) for <b>gender</b> (AdministrativeGender).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public String getGender() {  
		return getGenderElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>gender</b> (AdministrativeGender)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Practitioner setGender(BoundCodeDt<AdministrativeGenderEnum> theValue) {
		myGender = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>gender</b> (AdministrativeGender)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Practitioner setGender(AdministrativeGenderEnum theValue) {
		getGenderElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public DateTimeDt getBirthDateElement() {  
		if (myBirthDate == null) {
			myBirthDate = new DateTimeDt();
		}
		return myBirthDate;
	}

	
	/**
	 * Gets the value(s) for <b>birthDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Date getBirthDate() {  
		return getBirthDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>birthDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Practitioner setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>birthDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Practitioner setBirthDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the practitioner
     * </p> 
	 */
	public Practitioner setBirthDateWithSecondsPrecision( Date theDate) {
		myBirthDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>photo</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public java.util.List<AttachmentDt> getPhoto() {  
		if (myPhoto == null) {
			myPhoto = new java.util.ArrayList<AttachmentDt>();
		}
		return myPhoto;
	}

	/**
	 * Sets the value(s) for <b>photo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public Practitioner setPhoto(java.util.List<AttachmentDt> theValue) {
		myPhoto = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>photo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public AttachmentDt addPhoto() {
		AttachmentDt newType = new AttachmentDt();
		getPhoto().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>photo</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public AttachmentDt getPhotoFirstRep() {
		if (getPhoto().isEmpty()) {
			return addPhoto();
		}
		return getPhoto().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>organization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public ResourceReferenceDt getOrganization() {  
		if (myOrganization == null) {
			myOrganization = new ResourceReferenceDt();
		}
		return myOrganization;
	}

	/**
	 * Sets the value(s) for <b>organization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The organization that the practitioner represents
     * </p> 
	 */
	public Practitioner setOrganization(ResourceReferenceDt theValue) {
		myOrganization = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>role</b> (PractitionerRole).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<PractitionerRoleEnum>> getRole() {  
		if (myRole == null) {
			myRole = new java.util.ArrayList<BoundCodeableConceptDt<PractitionerRoleEnum>>();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (PractitionerRole)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public Practitioner setRole(java.util.List<BoundCodeableConceptDt<PractitionerRoleEnum>> theValue) {
		myRole = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>role</b> (PractitionerRole) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public BoundCodeableConceptDt<PractitionerRoleEnum> addRole(PractitionerRoleEnum theValue) {
		BoundCodeableConceptDt<PractitionerRoleEnum> retVal = new BoundCodeableConceptDt<PractitionerRoleEnum>(PractitionerRoleEnum.VALUESET_BINDER, theValue);
		getRole().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>role</b> (PractitionerRole),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public BoundCodeableConceptDt<PractitionerRoleEnum> getRoleFirstRep() {
		if (getRole().size() == 0) {
			addRole();
		}
		return getRole().get(0);
	}

	/**
	 * Add a value for <b>role</b> (PractitionerRole)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public BoundCodeableConceptDt<PractitionerRoleEnum> addRole() {
		BoundCodeableConceptDt<PractitionerRoleEnum> retVal = new BoundCodeableConceptDt<PractitionerRoleEnum>(PractitionerRoleEnum.VALUESET_BINDER);
		getRole().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>role</b> (PractitionerRole)
	 *
     * <p>
     * <b>Definition:</b>
     * Roles which this practitioner is authorized to perform for the organization
     * </p> 
	 */
	public Practitioner setRole(PractitionerRoleEnum theValue) {
		getRole().clear();
		addRole(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specialty</b> (PractitionerSpecialty).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<PractitionerSpecialtyEnum>> getSpecialty() {  
		if (mySpecialty == null) {
			mySpecialty = new java.util.ArrayList<BoundCodeableConceptDt<PractitionerSpecialtyEnum>>();
		}
		return mySpecialty;
	}

	/**
	 * Sets the value(s) for <b>specialty</b> (PractitionerSpecialty)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public Practitioner setSpecialty(java.util.List<BoundCodeableConceptDt<PractitionerSpecialtyEnum>> theValue) {
		mySpecialty = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>specialty</b> (PractitionerSpecialty) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public BoundCodeableConceptDt<PractitionerSpecialtyEnum> addSpecialty(PractitionerSpecialtyEnum theValue) {
		BoundCodeableConceptDt<PractitionerSpecialtyEnum> retVal = new BoundCodeableConceptDt<PractitionerSpecialtyEnum>(PractitionerSpecialtyEnum.VALUESET_BINDER, theValue);
		getSpecialty().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>specialty</b> (PractitionerSpecialty),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public BoundCodeableConceptDt<PractitionerSpecialtyEnum> getSpecialtyFirstRep() {
		if (getSpecialty().size() == 0) {
			addSpecialty();
		}
		return getSpecialty().get(0);
	}

	/**
	 * Add a value for <b>specialty</b> (PractitionerSpecialty)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public BoundCodeableConceptDt<PractitionerSpecialtyEnum> addSpecialty() {
		BoundCodeableConceptDt<PractitionerSpecialtyEnum> retVal = new BoundCodeableConceptDt<PractitionerSpecialtyEnum>(PractitionerSpecialtyEnum.VALUESET_BINDER);
		getSpecialty().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>specialty</b> (PractitionerSpecialty)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific specialty of the practitioner
     * </p> 
	 */
	public Practitioner setSpecialty(PractitionerSpecialtyEnum theValue) {
		getSpecialty().clear();
		addSpecialty(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
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
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization
     * </p> 
	 */
	public Practitioner setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public Practitioner setLocation(java.util.List<ResourceReferenceDt> theValue) {
		myLocation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The location(s) at which this practitioner provides care
     * </p> 
	 */
	public ResourceReferenceDt addLocation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getLocation().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>qualification</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Qualification> getQualification() {  
		if (myQualification == null) {
			myQualification = new java.util.ArrayList<Qualification>();
		}
		return myQualification;
	}

	/**
	 * Sets the value(s) for <b>qualification</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Practitioner setQualification(java.util.List<Qualification> theValue) {
		myQualification = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>qualification</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Qualification addQualification() {
		Qualification newType = new Qualification();
		getQualification().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>qualification</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Qualification getQualificationFirstRep() {
		if (getQualification().isEmpty()) {
			return addQualification();
		}
		return getQualification().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>communication</b> (Language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCommunication() {  
		if (myCommunication == null) {
			myCommunication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCommunication;
	}

	/**
	 * Sets the value(s) for <b>communication</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public Practitioner setCommunication(java.util.List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>communication</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public CodeableConceptDt addCommunication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCommunication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>communication</b> (Language),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A language the practitioner is able to use in patient communication
     * </p> 
	 */
	public CodeableConceptDt getCommunicationFirstRep() {
		if (getCommunication().isEmpty()) {
			return addCommunication();
		}
		return getCommunication().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Practitioner.qualification</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Qualification 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that applies to this person's qualification in this role"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Qualification",
		formalDefinition=""
	)
	private CodeableConceptDt myCode;
	
	@Child(name="period", type=PeriodDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Period during which the qualification is valid"
	)
	private PeriodDt myPeriod;
	
	@Child(name="issuer", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Organization that regulates and issues the qualification"
	)
	private ResourceReferenceDt myIssuer;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  myPeriod,  myIssuer);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, myPeriod, myIssuer);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person's qualification in this role
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
     * An identifier that applies to this person's qualification in this role
     * </p> 
	 */
	public Qualification setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person's qualification in this role
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
     * An identifier that applies to this person's qualification in this role
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>code</b> (Qualification).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Qualification)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Qualification setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Period during which the qualification is valid
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
     * Period during which the qualification is valid
     * </p> 
	 */
	public Qualification setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>issuer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that regulates and issues the qualification
     * </p> 
	 */
	public ResourceReferenceDt getIssuer() {  
		if (myIssuer == null) {
			myIssuer = new ResourceReferenceDt();
		}
		return myIssuer;
	}

	/**
	 * Sets the value(s) for <b>issuer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that regulates and issues the qualification
     * </p> 
	 */
	public Qualification setIssuer(ResourceReferenceDt theValue) {
		myIssuer = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Practitioner";
    }

}
