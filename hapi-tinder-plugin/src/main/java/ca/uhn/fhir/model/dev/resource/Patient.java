















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
 * HAPI/FHIR <b>Patient</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Demographics and other administrative information about a person or animal receiving care or other health-related services
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Tracking patient is the center of the healthcare process
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Patient">http://hl7.org/fhir/profiles/Patient</a> 
 * </p>
 *
 */
@ResourceDef(name="Patient", profile="http://hl7.org/fhir/profiles/Patient", id="patient")
public class Patient 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A patient identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Patient.identifier", description="A patient identifier", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A patient identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Patient.name", description="A portion of either family or given name of the patient", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.family</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="family", path="Patient.name.family", description="A portion of the family name of the patient", type="string"  )
	public static final String SP_FAMILY = "family";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.family</b><br/>
	 * </p>
	 */
	public static final StringClientParam FAMILY = new StringClientParam(SP_FAMILY);

	/**
	 * Search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.given</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="given", path="Patient.name.given", description="A portion of the given name of the patient", type="string"  )
	public static final String SP_GIVEN = "given";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.given</b><br/>
	 * </p>
	 */
	public static final StringClientParam GIVEN = new StringClientParam(SP_GIVEN);

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="phonetic", path="", description="A portion of either family or given name using some kind of phonetic matching algorithm", type="string"  )
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final StringClientParam PHONETIC = new StringClientParam(SP_PHONETIC);

	/**
	 * Search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of telecom details of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.telecom</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="telecom", path="Patient.telecom", description="The value in any kind of telecom details of the patient", type="string"  )
	public static final String SP_TELECOM = "telecom";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of telecom details of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.telecom</b><br/>
	 * </p>
	 */
	public static final StringClientParam TELECOM = new StringClientParam(SP_TELECOM);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.address</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="Patient.address", description="An address in any kind of address/part of the patient", type="string"  )
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.address</b><br/>
	 * </p>
	 */
	public static final StringClientParam ADDRESS = new StringClientParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the patient</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.gender</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="gender", path="Patient.gender", description="Gender of the patient", type="token"  )
	public static final String SP_GENDER = "gender";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the patient</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.gender</b><br/>
	 * </p>
	 */
	public static final TokenClientParam GENDER = new TokenClientParam(SP_GENDER);

	/**
	 * Search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b>Language code (irrespective of use value)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.communication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="language", path="Patient.communication", description="Language code (irrespective of use value)", type="token"  )
	public static final String SP_LANGUAGE = "language";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b>Language code (irrespective of use value)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.communication</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LANGUAGE = new TokenClientParam(SP_LANGUAGE);

	/**
	 * Search parameter constant for <b>birthdate</b>
	 * <p>
	 * Description: <b>The patient's date of birth</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Patient.birthDate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="birthdate", path="Patient.birthDate", description="The patient's date of birth", type="date"  )
	public static final String SP_BIRTHDATE = "birthdate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>birthdate</b>
	 * <p>
	 * Description: <b>The patient's date of birth</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Patient.birthDate</b><br/>
	 * </p>
	 */
	public static final DateClientParam BIRTHDATE = new DateClientParam(SP_BIRTHDATE);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The organization at which this person is a patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.managingOrganization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="Patient.managingOrganization", description="The organization at which this person is a patient", type="reference"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The organization at which this person is a patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.managingOrganization</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORGANIZATION = new ReferenceClientParam(SP_ORGANIZATION);

	/**
	 * Search parameter constant for <b>careprovider</b>
	 * <p>
	 * Description: <b>Patient's nominated care provider, could be a care manager, not the organization that manages the record</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.careProvider</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="careprovider", path="Patient.careProvider", description="Patient's nominated care provider, could be a care manager, not the organization that manages the record", type="reference"  )
	public static final String SP_CAREPROVIDER = "careprovider";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>careprovider</b>
	 * <p>
	 * Description: <b>Patient's nominated care provider, could be a care manager, not the organization that manages the record</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.careProvider</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CAREPROVIDER = new ReferenceClientParam(SP_CAREPROVIDER);

	/**
	 * Search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the patient record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.active</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="active", path="Patient.active", description="Whether the patient record is active", type="token"  )
	public static final String SP_ACTIVE = "active";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the patient record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.active</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTIVE = new TokenClientParam(SP_ACTIVE);

	/**
	 * Search parameter constant for <b>animal-species</b>
	 * <p>
	 * Description: <b>The species for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.species</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="animal-species", path="Patient.animal.species", description="The species for animal patients", type="token"  )
	public static final String SP_ANIMAL_SPECIES = "animal-species";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>animal-species</b>
	 * <p>
	 * Description: <b>The species for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.species</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ANIMAL_SPECIES = new TokenClientParam(SP_ANIMAL_SPECIES);

	/**
	 * Search parameter constant for <b>animal-breed</b>
	 * <p>
	 * Description: <b>The breed for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.breed</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="animal-breed", path="Patient.animal.breed", description="The breed for animal patients", type="token"  )
	public static final String SP_ANIMAL_BREED = "animal-breed";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>animal-breed</b>
	 * <p>
	 * Description: <b>The breed for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.breed</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ANIMAL_BREED = new TokenClientParam(SP_ANIMAL_BREED);

	/**
	 * Search parameter constant for <b>link</b>
	 * <p>
	 * Description: <b>All patients linked to the given patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.link.other</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="link", path="Patient.link.other", description="All patients linked to the given patient", type="reference"  )
	public static final String SP_LINK = "link";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>link</b>
	 * <p>
	 * Description: <b>All patients linked to the given patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.link.other</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LINK = new ReferenceClientParam(SP_LINK);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.active</b>".
	 */
	public static final Include INCLUDE_ACTIVE = new Include("Patient.active");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.address</b>".
	 */
	public static final Include INCLUDE_ADDRESS = new Include("Patient.address");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.animal.breed</b>".
	 */
	public static final Include INCLUDE_ANIMAL_BREED = new Include("Patient.animal.breed");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.animal.species</b>".
	 */
	public static final Include INCLUDE_ANIMAL_SPECIES = new Include("Patient.animal.species");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.birthDate</b>".
	 */
	public static final Include INCLUDE_BIRTHDATE = new Include("Patient.birthDate");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.careProvider</b>".
	 */
	public static final Include INCLUDE_CAREPROVIDER = new Include("Patient.careProvider");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.communication</b>".
	 */
	public static final Include INCLUDE_COMMUNICATION = new Include("Patient.communication");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.gender</b>".
	 */
	public static final Include INCLUDE_GENDER = new Include("Patient.gender");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Patient.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.link.other</b>".
	 */
	public static final Include INCLUDE_LINK_OTHER = new Include("Patient.link.other");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.managingOrganization</b>".
	 */
	public static final Include INCLUDE_MANAGINGORGANIZATION = new Include("Patient.managingOrganization");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Patient.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.name.family</b>".
	 */
	public static final Include INCLUDE_NAME_FAMILY = new Include("Patient.name.family");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.name.given</b>".
	 */
	public static final Include INCLUDE_NAME_GIVEN = new Include("Patient.name.given");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.telecom</b>".
	 */
	public static final Include INCLUDE_TELECOM = new Include("Patient.telecom");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that applies to this person as a patient"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A name associated with the individual."
	)
	private java.util.List<HumanNameDt> myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted."
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="gender", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="AdministrativeGender",
		formalDefinition="Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes."
	)
	private BoundCodeDt<AdministrativeGenderEnum> myGender;
	
	@Child(name="birthDate", type=DateDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date and time of birth for the individual"
	)
	private DateDt myBirthDate;
	
	@Child(name="deceased", order=5, min=0, max=1, type={
		BooleanDt.class, 		DateTimeDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates if the individual is deceased or not"
	)
	private IDatatype myDeceased;
	
	@Child(name="address", type=AddressDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Addresses for the individual"
	)
	private java.util.List<AddressDt> myAddress;
	
	@Child(name="maritalStatus", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="MaritalStatus",
		formalDefinition="This field contains a patient's most recent marital (civil) status."
	)
	private BoundCodeableConceptDt<MaritalStatusCodesEnum> myMaritalStatus;
	
	@Child(name="multipleBirth", order=8, min=0, max=1, type={
		BooleanDt.class, 		IntegerDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates whether the patient is part of a multiple or indicates the actual birth order."
	)
	private IDatatype myMultipleBirth;
	
	@Child(name="photo", type=AttachmentDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Image of the person"
	)
	private java.util.List<AttachmentDt> myPhoto;
	
	@Child(name="contact", order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A contact party (e.g. guardian, partner, friend) for the patient"
	)
	private java.util.List<Contact> myContact;
	
	@Child(name="animal", order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This element has a value if the patient is an animal"
	)
	private Animal myAnimal;
	
	@Child(name="communication", type=CodeableConceptDt.class, order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Language",
		formalDefinition="Languages which may be used to communicate with the patient about his or her health"
	)
	private java.util.List<CodeableConceptDt> myCommunication;
	
	@Child(name="careProvider", order=13, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Patient's nominated care provider"
	)
	private java.util.List<ResourceReferenceDt> myCareProvider;
	
	@Child(name="managingOrganization", order=14, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Organization that is the custodian of the patient record"
	)
	private ResourceReferenceDt myManagingOrganization;
	
	@Child(name="link", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Link to another patient resource that concerns the same actual person"
	)
	private java.util.List<Link> myLink;
	
	@Child(name="active", type=BooleanDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether this patient record is in active use"
	)
	private BooleanDt myActive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myTelecom,  myGender,  myBirthDate,  myDeceased,  myAddress,  myMaritalStatus,  myMultipleBirth,  myPhoto,  myContact,  myAnimal,  myCommunication,  myCareProvider,  myManagingOrganization,  myLink,  myActive);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myTelecom, myGender, myBirthDate, myDeceased, myAddress, myMaritalStatus, myMultipleBirth, myPhoto, myContact, myAnimal, myCommunication, myCareProvider, myManagingOrganization, myLink, myActive);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
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
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public Patient setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
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
     * An identifier that applies to this person as a patient
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
     * A name associated with the individual.
     * </p> 
	 */
	public java.util.List<HumanNameDt> getName() {  
		if (myName == null) {
			myName = new java.util.ArrayList<HumanNameDt>();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public Patient setName(java.util.List<HumanNameDt> theValue) {
		myName = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public HumanNameDt addName() {
		HumanNameDt newType = new HumanNameDt();
		getName().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>name</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public HumanNameDt getNameFirstRep() {
		if (getName().isEmpty()) {
			return addName();
		}
		return getName().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
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
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p> 
	 */
	public Patient setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
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
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p> 
	 */
	public ContactPointDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>gender</b> (AdministrativeGender).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
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
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
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
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Patient setGender(BoundCodeDt<AdministrativeGenderEnum> theValue) {
		myGender = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>gender</b> (AdministrativeGender)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Patient setGender(AdministrativeGenderEnum theValue) {
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
     * The date and time of birth for the individual
     * </p> 
	 */
	public DateDt getBirthDateElement() {  
		if (myBirthDate == null) {
			myBirthDate = new DateDt();
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
     * The date and time of birth for the individual
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
     * The date and time of birth for the individual
     * </p> 
	 */
	public Patient setBirthDate(DateDt theValue) {
		myBirthDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>birthDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public Patient setBirthDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public Patient setBirthDateWithDayPrecision( Date theDate) {
		myBirthDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>deceased[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the individual is deceased or not
     * </p> 
	 */
	public IDatatype getDeceased() {  
		return myDeceased;
	}

	/**
	 * Sets the value(s) for <b>deceased[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the individual is deceased or not
     * </p> 
	 */
	public Patient setDeceased(IDatatype theValue) {
		myDeceased = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>address</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
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
     * Addresses for the individual
     * </p> 
	 */
	public Patient setAddress(java.util.List<AddressDt> theValue) {
		myAddress = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
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
     * Addresses for the individual
     * </p> 
	 */
	public AddressDt getAddressFirstRep() {
		if (getAddress().isEmpty()) {
			return addAddress();
		}
		return getAddress().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>maritalStatus</b> (MaritalStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public BoundCodeableConceptDt<MaritalStatusCodesEnum> getMaritalStatus() {  
		if (myMaritalStatus == null) {
			myMaritalStatus = new BoundCodeableConceptDt<MaritalStatusCodesEnum>(MaritalStatusCodesEnum.VALUESET_BINDER);
		}
		return myMaritalStatus;
	}

	/**
	 * Sets the value(s) for <b>maritalStatus</b> (MaritalStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public Patient setMaritalStatus(BoundCodeableConceptDt<MaritalStatusCodesEnum> theValue) {
		myMaritalStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>maritalStatus</b> (MaritalStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public Patient setMaritalStatus(MaritalStatusCodesEnum theValue) {
		getMaritalStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>multipleBirth[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     * </p> 
	 */
	public IDatatype getMultipleBirth() {  
		return myMultipleBirth;
	}

	/**
	 * Sets the value(s) for <b>multipleBirth[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     * </p> 
	 */
	public Patient setMultipleBirth(IDatatype theValue) {
		myMultipleBirth = theValue;
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
	public Patient setPhoto(java.util.List<AttachmentDt> theValue) {
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
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public java.util.List<Contact> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<Contact>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public Patient setContact(java.util.List<Contact> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public Contact addContact() {
		Contact newType = new Contact();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public Contact getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>animal</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	public Animal getAnimal() {  
		if (myAnimal == null) {
			myAnimal = new Animal();
		}
		return myAnimal;
	}

	/**
	 * Sets the value(s) for <b>animal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	public Patient setAnimal(Animal theValue) {
		myAnimal = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>communication</b> (Language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
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
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public Patient setCommunication(java.util.List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>communication</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
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
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public CodeableConceptDt getCommunicationFirstRep() {
		if (getCommunication().isEmpty()) {
			return addCommunication();
		}
		return getCommunication().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>careProvider</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getCareProvider() {  
		if (myCareProvider == null) {
			myCareProvider = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myCareProvider;
	}

	/**
	 * Sets the value(s) for <b>careProvider</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public Patient setCareProvider(java.util.List<ResourceReferenceDt> theValue) {
		myCareProvider = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>careProvider</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public ResourceReferenceDt addCareProvider() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getCareProvider().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>managingOrganization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that is the custodian of the patient record
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
     * Organization that is the custodian of the patient record
     * </p> 
	 */
	public Patient setManagingOrganization(ResourceReferenceDt theValue) {
		myManagingOrganization = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>link</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public java.util.List<Link> getLink() {  
		if (myLink == null) {
			myLink = new java.util.ArrayList<Link>();
		}
		return myLink;
	}

	/**
	 * Sets the value(s) for <b>link</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public Patient setLink(java.util.List<Link> theValue) {
		myLink = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>link</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public Link addLink() {
		Link newType = new Link();
		getLink().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>link</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public Link getLinkFirstRep() {
		if (getLink().isEmpty()) {
			return addLink();
		}
		return getLink().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>active</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public BooleanDt getActiveElement() {  
		if (myActive == null) {
			myActive = new BooleanDt();
		}
		return myActive;
	}

	
	/**
	 * Gets the value(s) for <b>active</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public Boolean getActive() {  
		return getActiveElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>active</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public Patient setActive(BooleanDt theValue) {
		myActive = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>active</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public Patient setActive( boolean theBoolean) {
		myActive = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Patient.contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	@Block()	
	public static class Contact 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ContactRelationship",
		formalDefinition="The nature of the relationship between the patient and the contact person"
	)
	private java.util.List<CodeableConceptDt> myRelationship;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A name associated with the person"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A contact detail for the person, e.g. a telephone number or an email address."
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Address for the contact person"
	)
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="AdministrativeGender",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeDt<AdministrativeGenderEnum> myGender;
	
	@Child(name="organization", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Organization on behalf of which the contact is acting or for which the contact is working."
	)
	private ResourceReferenceDt myOrganization;
	
	@Child(name="period", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The period during which this person or organization is valid to be contacted relating to this patient"
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRelationship,  myName,  myTelecom,  myAddress,  myGender,  myOrganization,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRelationship, myName, myTelecom, myAddress, myGender, myOrganization, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>relationship</b> (ContactRelationship).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (ContactRelationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public Contact setRelationship(java.util.List<CodeableConceptDt> theValue) {
		myRelationship = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>relationship</b> (ContactRelationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public CodeableConceptDt addRelationship() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRelationship().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relationship</b> (ContactRelationship),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public CodeableConceptDt getRelationshipFirstRep() {
		if (getRelationship().isEmpty()) {
			return addRelationship();
		}
		return getRelationship().get(0); 
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
	public Contact setName(HumanNameDt theValue) {
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
     * A contact detail for the person, e.g. a telephone number or an email address.
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
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public Contact setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
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
     * A contact detail for the person, e.g. a telephone number or an email address.
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
     * Address for the contact person
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
     * Address for the contact person
     * </p> 
	 */
	public Contact setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
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
	public Contact setGender(BoundCodeDt<AdministrativeGenderEnum> theValue) {
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
	public Contact setGender(AdministrativeGenderEnum theValue) {
		getGenderElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>organization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization on behalf of which the contact is acting or for which the contact is working.
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
     * Organization on behalf of which the contact is acting or for which the contact is working.
     * </p> 
	 */
	public Contact setOrganization(ResourceReferenceDt theValue) {
		myOrganization = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which this person or organization is valid to be contacted relating to this patient
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
     * The period during which this person or organization is valid to be contacted relating to this patient
     * </p> 
	 */
	public Contact setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Patient.animal</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	@Block()	
	public static class Animal 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="species", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="AnimalSpecies",
		formalDefinition="Identifies the high level categorization of the kind of animal"
	)
	private BoundCodeableConceptDt<AnimalSpeciesEnum> mySpecies;
	
	@Child(name="breed", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="AnimalBreed",
		formalDefinition="Identifies the detailed categorization of the kind of animal."
	)
	private CodeableConceptDt myBreed;
	
	@Child(name="genderStatus", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="AnimalGenderStatus",
		formalDefinition="Indicates the current state of the animal's reproductive organs"
	)
	private CodeableConceptDt myGenderStatus;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySpecies,  myBreed,  myGenderStatus);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySpecies, myBreed, myGenderStatus);
	}

	/**
	 * Gets the value(s) for <b>species</b> (AnimalSpecies).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the high level categorization of the kind of animal
     * </p> 
	 */
	public BoundCodeableConceptDt<AnimalSpeciesEnum> getSpecies() {  
		if (mySpecies == null) {
			mySpecies = new BoundCodeableConceptDt<AnimalSpeciesEnum>(AnimalSpeciesEnum.VALUESET_BINDER);
		}
		return mySpecies;
	}

	/**
	 * Sets the value(s) for <b>species</b> (AnimalSpecies)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the high level categorization of the kind of animal
     * </p> 
	 */
	public Animal setSpecies(BoundCodeableConceptDt<AnimalSpeciesEnum> theValue) {
		mySpecies = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>species</b> (AnimalSpecies)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the high level categorization of the kind of animal
     * </p> 
	 */
	public Animal setSpecies(AnimalSpeciesEnum theValue) {
		getSpecies().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>breed</b> (AnimalBreed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the detailed categorization of the kind of animal.
     * </p> 
	 */
	public CodeableConceptDt getBreed() {  
		if (myBreed == null) {
			myBreed = new CodeableConceptDt();
		}
		return myBreed;
	}

	/**
	 * Sets the value(s) for <b>breed</b> (AnimalBreed)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the detailed categorization of the kind of animal.
     * </p> 
	 */
	public Animal setBreed(CodeableConceptDt theValue) {
		myBreed = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>genderStatus</b> (AnimalGenderStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the current state of the animal's reproductive organs
     * </p> 
	 */
	public CodeableConceptDt getGenderStatus() {  
		if (myGenderStatus == null) {
			myGenderStatus = new CodeableConceptDt();
		}
		return myGenderStatus;
	}

	/**
	 * Sets the value(s) for <b>genderStatus</b> (AnimalGenderStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the current state of the animal's reproductive organs
     * </p> 
	 */
	public Animal setGenderStatus(CodeableConceptDt theValue) {
		myGenderStatus = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Patient.link</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	@Block()	
	public static class Link 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="other", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The other patient resource that the link refers to"
	)
	private ResourceReferenceDt myOther;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="LinkType",
		formalDefinition="The type of link between this patient resource and another patient resource."
	)
	private BoundCodeDt<LinkTypeEnum> myType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myOther,  myType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myOther, myType);
	}

	/**
	 * Gets the value(s) for <b>other</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The other patient resource that the link refers to
     * </p> 
	 */
	public ResourceReferenceDt getOther() {  
		if (myOther == null) {
			myOther = new ResourceReferenceDt();
		}
		return myOther;
	}

	/**
	 * Sets the value(s) for <b>other</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The other patient resource that the link refers to
     * </p> 
	 */
	public Link setOther(ResourceReferenceDt theValue) {
		myOther = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (LinkType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public BoundCodeDt<LinkTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<LinkTypeEnum>(LinkTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (LinkType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (LinkType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public Link setType(BoundCodeDt<LinkTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (LinkType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public Link setType(LinkTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  

	}




    @Override
    public String getResourceName() {
        return "Patient";
    }

}
