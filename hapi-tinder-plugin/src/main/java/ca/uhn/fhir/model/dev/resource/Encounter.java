















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
 * HAPI/FHIR <b>Encounter</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Encounter">http://hl7.org/fhir/profiles/Encounter</a> 
 * </p>
 *
 */
@ResourceDef(name="Encounter", profile="http://hl7.org/fhir/profiles/Encounter", id="encounter")
public class Encounter 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Encounter.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Encounter.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>A date within the period the Encounter lasted</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Encounter.period", description="A date within the period the Encounter lasted", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>A date within the period the Encounter lasted</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Encounter.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>length</b>
	 * <p>
	 * Description: <b>Length of encounter in days</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Encounter.length</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="length", path="Encounter.length", description="Length of encounter in days", type="number"  )
	public static final String SP_LENGTH = "length";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>length</b>
	 * <p>
	 * Description: <b>Length of encounter in days</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Encounter.length</b><br/>
	 * </p>
	 */
	public static final NumberClientParam LENGTH = new NumberClientParam(SP_LENGTH);

	/**
	 * Search parameter constant for <b>indication</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.indication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="indication", path="Encounter.indication", description="", type="reference"  )
	public static final String SP_INDICATION = "indication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>indication</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.indication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam INDICATION = new ReferenceClientParam(SP_INDICATION);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.location.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Encounter.location.location", description="", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.location.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>location-period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.location.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location-period", path="Encounter.location.period", description="", type="date"  )
	public static final String SP_LOCATION_PERIOD = "location-period";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location-period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.location.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam LOCATION_PERIOD = new DateClientParam(SP_LOCATION_PERIOD);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Encounter.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>special-arrangement</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.hospitalization.specialArrangement</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="special-arrangement", path="Encounter.hospitalization.specialArrangement", description="", type="token"  )
	public static final String SP_SPECIAL_ARRANGEMENT = "special-arrangement";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>special-arrangement</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.hospitalization.specialArrangement</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SPECIAL_ARRANGEMENT = new TokenClientParam(SP_SPECIAL_ARRANGEMENT);

	/**
	 * Search parameter constant for <b>part-of</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.partOf</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="part-of", path="Encounter.partOf", description="", type="reference"  )
	public static final String SP_PART_OF = "part-of";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>part-of</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.partOf</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PART_OF = new ReferenceClientParam(SP_PART_OF);

	/**
	 * Search parameter constant for <b>participant-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.participant.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="participant-type", path="Encounter.participant.type", description="", type="token"  )
	public static final String SP_PARTICIPANT_TYPE = "participant-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>participant-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.participant.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PARTICIPANT_TYPE = new TokenClientParam(SP_PARTICIPANT_TYPE);

	/**
	 * Search parameter constant for <b>episodeofcare</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.episodeOfCare</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="episodeofcare", path="Encounter.episodeOfCare", description="", type="reference"  )
	public static final String SP_EPISODEOFCARE = "episodeofcare";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>episodeofcare</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.episodeOfCare</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam EPISODEOFCARE = new ReferenceClientParam(SP_EPISODEOFCARE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.episodeOfCare</b>".
	 */
	public static final Include INCLUDE_EPISODEOFCARE = new Include("Encounter.episodeOfCare");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.hospitalization.specialArrangement</b>".
	 */
	public static final Include INCLUDE_HOSPITALIZATION_SPECIALARRANGEMENT = new Include("Encounter.hospitalization.specialArrangement");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Encounter.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.indication</b>".
	 */
	public static final Include INCLUDE_INDICATION = new Include("Encounter.indication");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.length</b>".
	 */
	public static final Include INCLUDE_LENGTH = new Include("Encounter.length");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.location.location</b>".
	 */
	public static final Include INCLUDE_LOCATION_LOCATION = new Include("Encounter.location.location");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.location.period</b>".
	 */
	public static final Include INCLUDE_LOCATION_PERIOD = new Include("Encounter.location.period");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.partOf</b>".
	 */
	public static final Include INCLUDE_PARTOF = new Include("Encounter.partOf");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.participant.type</b>".
	 */
	public static final Include INCLUDE_PARTICIPANT_TYPE = new Include("Encounter.participant.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Encounter.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.period</b>".
	 */
	public static final Include INCLUDE_PERIOD = new Include("Encounter.period");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Encounter.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Encounter.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="EncounterState",
		formalDefinition=""
	)
	private BoundCodeDt<EncounterStateEnum> myStatus;
	
	@Child(name="statusHistory", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The current status is always found in the current version of the resource. This status history permits the encounter resource to contain the status history without the needing to read through the historical versions of the resource, or even have the server store them."
	)
	private java.util.List<StatusHistory> myStatusHistory;
	
	@Child(name="class", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="EncounterClass",
		formalDefinition=""
	)
	private BoundCodeDt<EncounterClassEnum> myClassElement;
	
	@Child(name="type", type=CodeableConceptDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="EncounterType",
		formalDefinition="Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)"
	)
	private java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> myType;
	
	@Child(name="patient", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="episodeOfCare", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.EpisodeOfCare.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Where a specific encounter should be classified as a part of a specific episode of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as govt reporting, or issue tracking."
	)
	private ResourceReferenceDt myEpisodeOfCare;
	
	@Child(name="participant", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The main practitioner responsible for providing the service"
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="fulfills", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Appointment.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myFulfills;
	
	@Child(name="period", type=PeriodDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The start and end time of the encounter"
	)
	private PeriodDt myPeriod;
	
	@Child(name="length", type=DurationDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Quantity of time the encounter lasted. This excludes the time during leaves of absence."
	)
	private DurationDt myLength;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="EncounterReason",
		formalDefinition="Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis."
	)
	private BoundCodeableConceptDt<EncounterReasonCodesEnum> myReason;
	
	@Child(name="indication", order=12, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis."
	)
	private java.util.List<ResourceReferenceDt> myIndication;
	
	@Child(name="priority", type=CodeableConceptDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="Priority",
		formalDefinition=""
	)
	private BoundCodeableConceptDt<PriorityCodesEnum> myPriority;
	
	@Child(name="hospitalization", order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Details about an admission to a clinic"
	)
	private Hospitalization myHospitalization;
	
	@Child(name="location", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="List of locations at which the patient has been"
	)
	private java.util.List<Location> myLocation;
	
	@Child(name="serviceProvider", order=16, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myServiceProvider;
	
	@Child(name="partOf", order=17, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Another Encounter of which this encounter is a part of (administratively or in time)."
	)
	private ResourceReferenceDt myPartOf;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myStatusHistory,  myClassElement,  myType,  myPatient,  myEpisodeOfCare,  myParticipant,  myFulfills,  myPeriod,  myLength,  myReason,  myIndication,  myPriority,  myHospitalization,  myLocation,  myServiceProvider,  myPartOf);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myStatusHistory, myClassElement, myType, myPatient, myEpisodeOfCare, myParticipant, myFulfills, myPeriod, myLength, myReason, myIndication, myPriority, myHospitalization, myLocation, myServiceProvider, myPartOf);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public Encounter setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (EncounterState).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterStateEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<EncounterStateEnum>(EncounterStateEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (EncounterState).
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
	 * Sets the value(s) for <b>status</b> (EncounterState)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setStatus(BoundCodeDt<EncounterStateEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (EncounterState)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setStatus(EncounterStateEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>statusHistory</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The current status is always found in the current version of the resource. This status history permits the encounter resource to contain the status history without the needing to read through the historical versions of the resource, or even have the server store them.
     * </p> 
	 */
	public java.util.List<StatusHistory> getStatusHistory() {  
		if (myStatusHistory == null) {
			myStatusHistory = new java.util.ArrayList<StatusHistory>();
		}
		return myStatusHistory;
	}

	/**
	 * Sets the value(s) for <b>statusHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The current status is always found in the current version of the resource. This status history permits the encounter resource to contain the status history without the needing to read through the historical versions of the resource, or even have the server store them.
     * </p> 
	 */
	public Encounter setStatusHistory(java.util.List<StatusHistory> theValue) {
		myStatusHistory = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>statusHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The current status is always found in the current version of the resource. This status history permits the encounter resource to contain the status history without the needing to read through the historical versions of the resource, or even have the server store them.
     * </p> 
	 */
	public StatusHistory addStatusHistory() {
		StatusHistory newType = new StatusHistory();
		getStatusHistory().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>statusHistory</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The current status is always found in the current version of the resource. This status history permits the encounter resource to contain the status history without the needing to read through the historical versions of the resource, or even have the server store them.
     * </p> 
	 */
	public StatusHistory getStatusHistoryFirstRep() {
		if (getStatusHistory().isEmpty()) {
			return addStatusHistory();
		}
		return getStatusHistory().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>class</b> (EncounterClass).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterClassEnum> getClassElementElement() {  
		if (myClassElement == null) {
			myClassElement = new BoundCodeDt<EncounterClassEnum>(EncounterClassEnum.VALUESET_BINDER);
		}
		return myClassElement;
	}

	
	/**
	 * Gets the value(s) for <b>class</b> (EncounterClass).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getClassElement() {  
		return getClassElementElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>class</b> (EncounterClass)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setClassElement(BoundCodeDt<EncounterClassEnum> theValue) {
		myClassElement = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>class</b> (EncounterClass)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setClassElement(EncounterClassEnum theValue) {
		getClassElementElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (EncounterType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<EncounterTypeEnum>>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (EncounterType)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public Encounter setType(java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>type</b> (EncounterType) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterTypeEnum> addType(EncounterTypeEnum theValue) {
		BoundCodeableConceptDt<EncounterTypeEnum> retVal = new BoundCodeableConceptDt<EncounterTypeEnum>(EncounterTypeEnum.VALUESET_BINDER, theValue);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>type</b> (EncounterType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterTypeEnum> getTypeFirstRep() {
		if (getType().size() == 0) {
			addType();
		}
		return getType().get(0);
	}

	/**
	 * Add a value for <b>type</b> (EncounterType)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterTypeEnum> addType() {
		BoundCodeableConceptDt<EncounterTypeEnum> retVal = new BoundCodeableConceptDt<EncounterTypeEnum>(EncounterTypeEnum.VALUESET_BINDER);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> (EncounterType)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public Encounter setType(EncounterTypeEnum theValue) {
		getType().clear();
		addType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>episodeOfCare</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where a specific encounter should be classified as a part of a specific episode of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as govt reporting, or issue tracking.
     * </p> 
	 */
	public ResourceReferenceDt getEpisodeOfCare() {  
		if (myEpisodeOfCare == null) {
			myEpisodeOfCare = new ResourceReferenceDt();
		}
		return myEpisodeOfCare;
	}

	/**
	 * Sets the value(s) for <b>episodeOfCare</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Where a specific encounter should be classified as a part of a specific episode of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as govt reporting, or issue tracking.
     * </p> 
	 */
	public Encounter setEpisodeOfCare(ResourceReferenceDt theValue) {
		myEpisodeOfCare = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>participant</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
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
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public Encounter setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
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
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>fulfills</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getFulfills() {  
		if (myFulfills == null) {
			myFulfills = new ResourceReferenceDt();
		}
		return myFulfills;
	}

	/**
	 * Sets the value(s) for <b>fulfills</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setFulfills(ResourceReferenceDt theValue) {
		myFulfills = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The start and end time of the encounter
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
     * The start and end time of the encounter
     * </p> 
	 */
	public Encounter setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>length</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     * </p> 
	 */
	public DurationDt getLength() {  
		if (myLength == null) {
			myLength = new DurationDt();
		}
		return myLength;
	}

	/**
	 * Sets the value(s) for <b>length</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     * </p> 
	 */
	public Encounter setLength(DurationDt theValue) {
		myLength = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reason</b> (EncounterReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterReasonCodesEnum> getReason() {  
		if (myReason == null) {
			myReason = new BoundCodeableConceptDt<EncounterReasonCodesEnum>(EncounterReasonCodesEnum.VALUESET_BINDER);
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (EncounterReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public Encounter setReason(BoundCodeableConceptDt<EncounterReasonCodesEnum> theValue) {
		myReason = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>reason</b> (EncounterReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public Encounter setReason(EncounterReasonCodesEnum theValue) {
		getReason().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>indication</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getIndication() {  
		if (myIndication == null) {
			myIndication = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myIndication;
	}

	/**
	 * Sets the value(s) for <b>indication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     * </p> 
	 */
	public Encounter setIndication(java.util.List<ResourceReferenceDt> theValue) {
		myIndication = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>indication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     * </p> 
	 */
	public ResourceReferenceDt addIndication() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getIndication().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>priority</b> (Priority).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<PriorityCodesEnum> getPriority() {  
		if (myPriority == null) {
			myPriority = new BoundCodeableConceptDt<PriorityCodesEnum>(PriorityCodesEnum.VALUESET_BINDER);
		}
		return myPriority;
	}

	/**
	 * Sets the value(s) for <b>priority</b> (Priority)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setPriority(BoundCodeableConceptDt<PriorityCodesEnum> theValue) {
		myPriority = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>priority</b> (Priority)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setPriority(PriorityCodesEnum theValue) {
		getPriority().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>hospitalization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	public Hospitalization getHospitalization() {  
		if (myHospitalization == null) {
			myHospitalization = new Hospitalization();
		}
		return myHospitalization;
	}

	/**
	 * Sets the value(s) for <b>hospitalization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	public Encounter setHospitalization(Hospitalization theValue) {
		myHospitalization = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public java.util.List<Location> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<Location>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public Encounter setLocation(java.util.List<Location> theValue) {
		myLocation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public Location addLocation() {
		Location newType = new Location();
		getLocation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>location</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public Location getLocationFirstRep() {
		if (getLocation().isEmpty()) {
			return addLocation();
		}
		return getLocation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>serviceProvider</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getServiceProvider() {  
		if (myServiceProvider == null) {
			myServiceProvider = new ResourceReferenceDt();
		}
		return myServiceProvider;
	}

	/**
	 * Sets the value(s) for <b>serviceProvider</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setServiceProvider(ResourceReferenceDt theValue) {
		myServiceProvider = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>partOf</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Another Encounter of which this encounter is a part of (administratively or in time).
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
     * Another Encounter of which this encounter is a part of (administratively or in time).
     * </p> 
	 */
	public Encounter setPartOf(ResourceReferenceDt theValue) {
		myPartOf = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>Encounter.statusHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The current status is always found in the current version of the resource. This status history permits the encounter resource to contain the status history without the needing to read through the historical versions of the resource, or even have the server store them.
     * </p> 
	 */
	@Block()	
	public static class StatusHistory 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="EncounterState",
		formalDefinition=""
	)
	private BoundCodeDt<EncounterStateEnum> myStatus;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStatus, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>status</b> (EncounterState).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterStateEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<EncounterStateEnum>(EncounterStateEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (EncounterState).
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
	 * Sets the value(s) for <b>status</b> (EncounterState)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StatusHistory setStatus(BoundCodeDt<EncounterStateEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (EncounterState)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StatusHistory setStatus(EncounterStateEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public StatusHistory setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Encounter.participant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	@Block()	
	public static class Participant 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ParticipantType",
		formalDefinition=""
	)
	private java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> myType;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period"
	)
	private PeriodDt myPeriod;
	
	@Child(name="individual", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myIndividual;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myPeriod,  myIndividual);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myPeriod, myIndividual);
	}

	/**
	 * Gets the value(s) for <b>type</b> (ParticipantType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<ParticipantTypeEnum>>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ParticipantType)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setType(java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>type</b> (ParticipantType) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addType(ParticipantTypeEnum theValue) {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER, theValue);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>type</b> (ParticipantType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> getTypeFirstRep() {
		if (getType().size() == 0) {
			addType();
		}
		return getType().get(0);
	}

	/**
	 * Add a value for <b>type</b> (ParticipantType)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addType() {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> (ParticipantType)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setType(ParticipantTypeEnum theValue) {
		getType().clear();
		addType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period
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
     * The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period
     * </p> 
	 */
	public Participant setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>individual</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new ResourceReferenceDt();
		}
		return myIndividual;
	}

	/**
	 * Sets the value(s) for <b>individual</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setIndividual(ResourceReferenceDt theValue) {
		myIndividual = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Encounter.hospitalization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	@Block()	
	public static class Hospitalization 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="preAdmissionIdentifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private IdentifierDt myPreAdmissionIdentifier;
	
	@Child(name="origin", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myOrigin;
	
	@Child(name="admitSource", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="AdmitSource",
		formalDefinition=""
	)
	private BoundCodeableConceptDt<AdmitSourceEnum> myAdmitSource;
	
	@Child(name="diet", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="PatientDiet",
		formalDefinition="Dietary restrictions for the patient"
	)
	private CodeableConceptDt myDiet;
	
	@Child(name="specialCourtesy", type=CodeableConceptDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Courtesies",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> mySpecialCourtesy;
	
	@Child(name="specialArrangement", type=CodeableConceptDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Arrangements",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> mySpecialArrangement;
	
	@Child(name="destination", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myDestination;
	
	@Child(name="dischargeDisposition", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="DischargeDisp",
		formalDefinition=""
	)
	private CodeableConceptDt myDischargeDisposition;
	
	@Child(name="dischargeDiagnosis", order=8, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myDischargeDiagnosis;
	
	@Child(name="reAdmission", type=BooleanDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether this hospitalization is a readmission"
	)
	private BooleanDt myReAdmission;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPreAdmissionIdentifier,  myOrigin,  myAdmitSource,  myDiet,  mySpecialCourtesy,  mySpecialArrangement,  myDestination,  myDischargeDisposition,  myDischargeDiagnosis,  myReAdmission);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPreAdmissionIdentifier, myOrigin, myAdmitSource, myDiet, mySpecialCourtesy, mySpecialArrangement, myDestination, myDischargeDisposition, myDischargeDiagnosis, myReAdmission);
	}

	/**
	 * Gets the value(s) for <b>preAdmissionIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt getPreAdmissionIdentifier() {  
		if (myPreAdmissionIdentifier == null) {
			myPreAdmissionIdentifier = new IdentifierDt();
		}
		return myPreAdmissionIdentifier;
	}

	/**
	 * Sets the value(s) for <b>preAdmissionIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setPreAdmissionIdentifier(IdentifierDt theValue) {
		myPreAdmissionIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>origin</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getOrigin() {  
		if (myOrigin == null) {
			myOrigin = new ResourceReferenceDt();
		}
		return myOrigin;
	}

	/**
	 * Sets the value(s) for <b>origin</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setOrigin(ResourceReferenceDt theValue) {
		myOrigin = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>admitSource</b> (AdmitSource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<AdmitSourceEnum> getAdmitSource() {  
		if (myAdmitSource == null) {
			myAdmitSource = new BoundCodeableConceptDt<AdmitSourceEnum>(AdmitSourceEnum.VALUESET_BINDER);
		}
		return myAdmitSource;
	}

	/**
	 * Sets the value(s) for <b>admitSource</b> (AdmitSource)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setAdmitSource(BoundCodeableConceptDt<AdmitSourceEnum> theValue) {
		myAdmitSource = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>admitSource</b> (AdmitSource)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setAdmitSource(AdmitSourceEnum theValue) {
		getAdmitSource().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>diet</b> (PatientDiet).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Dietary restrictions for the patient
     * </p> 
	 */
	public CodeableConceptDt getDiet() {  
		if (myDiet == null) {
			myDiet = new CodeableConceptDt();
		}
		return myDiet;
	}

	/**
	 * Sets the value(s) for <b>diet</b> (PatientDiet)
	 *
     * <p>
     * <b>Definition:</b>
     * Dietary restrictions for the patient
     * </p> 
	 */
	public Hospitalization setDiet(CodeableConceptDt theValue) {
		myDiet = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specialCourtesy</b> (Courtesies).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSpecialCourtesy() {  
		if (mySpecialCourtesy == null) {
			mySpecialCourtesy = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySpecialCourtesy;
	}

	/**
	 * Sets the value(s) for <b>specialCourtesy</b> (Courtesies)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setSpecialCourtesy(java.util.List<CodeableConceptDt> theValue) {
		mySpecialCourtesy = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>specialCourtesy</b> (Courtesies)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addSpecialCourtesy() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getSpecialCourtesy().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>specialCourtesy</b> (Courtesies),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getSpecialCourtesyFirstRep() {
		if (getSpecialCourtesy().isEmpty()) {
			return addSpecialCourtesy();
		}
		return getSpecialCourtesy().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>specialArrangement</b> (Arrangements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSpecialArrangement() {  
		if (mySpecialArrangement == null) {
			mySpecialArrangement = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySpecialArrangement;
	}

	/**
	 * Sets the value(s) for <b>specialArrangement</b> (Arrangements)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setSpecialArrangement(java.util.List<CodeableConceptDt> theValue) {
		mySpecialArrangement = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>specialArrangement</b> (Arrangements)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addSpecialArrangement() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getSpecialArrangement().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>specialArrangement</b> (Arrangements),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getSpecialArrangementFirstRep() {
		if (getSpecialArrangement().isEmpty()) {
			return addSpecialArrangement();
		}
		return getSpecialArrangement().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>destination</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getDestination() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setDestination(ResourceReferenceDt theValue) {
		myDestination = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dischargeDisposition</b> (DischargeDisp).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getDischargeDisposition() {  
		if (myDischargeDisposition == null) {
			myDischargeDisposition = new CodeableConceptDt();
		}
		return myDischargeDisposition;
	}

	/**
	 * Sets the value(s) for <b>dischargeDisposition</b> (DischargeDisp)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setDischargeDisposition(CodeableConceptDt theValue) {
		myDischargeDisposition = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dischargeDiagnosis</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getDischargeDiagnosis() {  
		if (myDischargeDiagnosis == null) {
			myDischargeDiagnosis = new ResourceReferenceDt();
		}
		return myDischargeDiagnosis;
	}

	/**
	 * Sets the value(s) for <b>dischargeDiagnosis</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setDischargeDiagnosis(ResourceReferenceDt theValue) {
		myDischargeDiagnosis = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reAdmission</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public BooleanDt getReAdmissionElement() {  
		if (myReAdmission == null) {
			myReAdmission = new BooleanDt();
		}
		return myReAdmission;
	}

	
	/**
	 * Gets the value(s) for <b>reAdmission</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public Boolean getReAdmission() {  
		return getReAdmissionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reAdmission</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public Hospitalization setReAdmission(BooleanDt theValue) {
		myReAdmission = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reAdmission</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public Hospitalization setReAdmission( boolean theBoolean) {
		myReAdmission = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Encounter.location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	@Block()	
	public static class Location 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="location", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The location where the encounter takes place"
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="EncounterLocationStatus",
		formalDefinition="The status of the participants presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time"
	)
	private CodeDt myStatus;
	
	@Child(name="period", type=PeriodDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLocation,  myStatus,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLocation, myStatus, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The location where the encounter takes place
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The location where the encounter takes place
     * </p> 
	 */
	public Location setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (EncounterLocationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the participants presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time
     * </p> 
	 */
	public CodeDt getStatusElement() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (EncounterLocationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the participants presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (EncounterLocationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the participants presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time
     * </p> 
	 */
	public Location setStatus(CodeDt theValue) {
		myStatus = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>status</b> (EncounterLocationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the participants presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time
     * </p> 
	 */
	public Location setStatus( String theCode) {
		myStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public Location setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Encounter";
    }

}
