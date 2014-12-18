















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
 * HAPI/FHIR <b>Observation</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Measurements and simple assertions made about a patient, device or other subject
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Observations are a key aspect of healthcare.  This resource is used to capture those that do not require more sophisticated mechanisms.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Observation">http://hl7.org/fhir/profiles/Observation</a> 
 * </p>
 *
 */
@ResourceDef(name="Observation", profile="http://hl7.org/fhir/profiles/Observation", id="observation")
public class Observation 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the observation type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Observation.name", description="The name of the observation type", type="token"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the observation type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.name</b><br/>
	 * </p>
	 */
	public static final TokenClientParam NAME = new TokenClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>value-quantity</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br/>
	 * Type: <b>quantity</b><br/>
	 * Path: <b>Observation.valueQuantity</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-quantity", path="Observation.valueQuantity", description="The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)", type="quantity"  )
	public static final String SP_VALUE_QUANTITY = "value-quantity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-quantity</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)</b><br/>
	 * Type: <b>quantity</b><br/>
	 * Path: <b>Observation.valueQuantity</b><br/>
	 * </p>
	 */
	public static final QuantityClientParam VALUE_QUANTITY = new QuantityClientParam(SP_VALUE_QUANTITY);

	/**
	 * Search parameter constant for <b>value-concept</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a CodeableConcept</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.valueCodeableConcept</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-concept", path="Observation.valueCodeableConcept", description="The value of the observation, if the value is a CodeableConcept", type="token"  )
	public static final String SP_VALUE_CONCEPT = "value-concept";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-concept</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a CodeableConcept</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.valueCodeableConcept</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VALUE_CONCEPT = new TokenClientParam(SP_VALUE_CONCEPT);

	/**
	 * Search parameter constant for <b>value-date</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.valueDateTime | Observation.valuePeriod</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-date", path="Observation.valueDateTime | Observation.valuePeriod", description="The value of the observation, if the value is a Period", type="date"  )
	public static final String SP_VALUE_DATE = "value-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-date</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a Period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.valueDateTime | Observation.valuePeriod</b><br/>
	 * </p>
	 */
	public static final DateClientParam VALUE_DATE = new DateClientParam(SP_VALUE_DATE);

	/**
	 * Search parameter constant for <b>value-string</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a string, and also searches in CodeableConcept.text</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Observation.valueString</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value-string", path="Observation.valueString", description="The value of the observation, if the value is a string, and also searches in CodeableConcept.text", type="string"  )
	public static final String SP_VALUE_STRING = "value-string";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value-string</b>
	 * <p>
	 * Description: <b>The value of the observation, if the value is a string, and also searches in CodeableConcept.text</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Observation.valueString</b><br/>
	 * </p>
	 */
	public static final StringClientParam VALUE_STRING = new StringClientParam(SP_VALUE_STRING);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.applies[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Observation.applies[x]", description="Obtained date/time. If the obtained element is a period, a date that falls in the period", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Observation.applies[x]</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Observation.status", description="The status of the observation", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>reliability</b>
	 * <p>
	 * Description: <b>The reliability of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.reliability</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reliability", path="Observation.reliability", description="The reliability of the observation", type="token"  )
	public static final String SP_RELIABILITY = "reliability";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reliability</b>
	 * <p>
	 * Description: <b>The reliability of the observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.reliability</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RELIABILITY = new TokenClientParam(SP_RELIABILITY);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the observation is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Observation.subject", description="The subject that the observation is about", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the observation is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who and/or what performed the observation</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.performer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="Observation.performer", description="Who and/or what performed the observation", type="reference"  )
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who and/or what performed the observation</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.performer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PERFORMER = new ReferenceClientParam(SP_PERFORMER);

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.specimen</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="Observation.specimen", description="", type="reference"  )
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.specimen</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SPECIMEN = new ReferenceClientParam(SP_SPECIMEN);

	/**
	 * Search parameter constant for <b>related-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.related.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-type", path="Observation.related.type", description="", type="token"  )
	public static final String SP_RELATED_TYPE = "related-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.related.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RELATED_TYPE = new TokenClientParam(SP_RELATED_TYPE);

	/**
	 * Search parameter constant for <b>related-target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.related.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-target", path="Observation.related.target", description="", type="reference"  )
	public static final String SP_RELATED_TARGET = "related-target";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.related.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RELATED_TARGET = new ReferenceClientParam(SP_RELATED_TARGET);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Healthcare event related to the observation</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="Observation.encounter", description="Healthcare event related to the observation", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Healthcare event related to the observation</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>data-absent-reason</b>
	 * <p>
	 * Description: <b>The reason why the expected value in the element Observation.value[x] is missing.</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.dataAbsentReason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="data-absent-reason", path="Observation.dataAbsentReason", description="The reason why the expected value in the element Observation.value[x] is missing.", type="token"  )
	public static final String SP_DATA_ABSENT_REASON = "data-absent-reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>data-absent-reason</b>
	 * <p>
	 * Description: <b>The reason why the expected value in the element Observation.value[x] is missing.</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.dataAbsentReason</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DATA_ABSENT_REASON = new TokenClientParam(SP_DATA_ABSENT_REASON);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The subject that the observation is about (if patient)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Observation.subject", description="The subject that the observation is about (if patient)", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The subject that the observation is about (if patient)</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Observation.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The unique Id for a particular observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Observation.identifier", description="The unique Id for a particular observation", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The unique Id for a particular observation</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Observation.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name-value-quantity</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name-value-quantity", path="name & value[x]", description="Both name and one of the value parameters", type="composite"  , compositeOf={  "name",  "value-quantity" }  )
	public static final String SP_NAME_VALUE_QUANTITY = "name-value-quantity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name-value-quantity</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, QuantityClientParam> NAME_VALUE_QUANTITY = new CompositeClientParam<TokenClientParam, QuantityClientParam>(SP_NAME_VALUE_QUANTITY);

	/**
	 * Search parameter constant for <b>name-value-concept</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name-value-concept", path="name & value[x]", description="Both name and one of the value parameters", type="composite"  , compositeOf={  "name",  "value-concept" }  )
	public static final String SP_NAME_VALUE_CONCEPT = "name-value-concept";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name-value-concept</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, TokenClientParam> NAME_VALUE_CONCEPT = new CompositeClientParam<TokenClientParam, TokenClientParam>(SP_NAME_VALUE_CONCEPT);

	/**
	 * Search parameter constant for <b>name-value-date</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name-value-date", path="name & value[x]", description="Both name and one of the value parameters", type="composite"  , compositeOf={  "name",  "value-date" }  )
	public static final String SP_NAME_VALUE_DATE = "name-value-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name-value-date</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, DateClientParam> NAME_VALUE_DATE = new CompositeClientParam<TokenClientParam, DateClientParam>(SP_NAME_VALUE_DATE);

	/**
	 * Search parameter constant for <b>name-value-string</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name-value-string", path="name & value[x]", description="Both name and one of the value parameters", type="composite"  , compositeOf={  "name",  "value-string" }  )
	public static final String SP_NAME_VALUE_STRING = "name-value-string";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name-value-string</b>
	 * <p>
	 * Description: <b>Both name and one of the value parameters</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>name & value[x]</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, StringClientParam> NAME_VALUE_STRING = new CompositeClientParam<TokenClientParam, StringClientParam>(SP_NAME_VALUE_STRING);

	/**
	 * Search parameter constant for <b>related-target-related-type</b>
	 * <p>
	 * Description: <b>Related Observations - search on related-type and related-target together</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>related-target & related-type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-target-related-type", path="related-target & related-type", description="Related Observations - search on related-type and related-target together", type="composite"  , compositeOf={  "related-target",  "related-type" }  )
	public static final String SP_RELATED_TARGET_RELATED_TYPE = "related-target-related-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-target-related-type</b>
	 * <p>
	 * Description: <b>Related Observations - search on related-type and related-target together</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>related-target & related-type</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<ReferenceClientParam, TokenClientParam> RELATED_TARGET_RELATED_TYPE = new CompositeClientParam<ReferenceClientParam, TokenClientParam>(SP_RELATED_TARGET_RELATED_TYPE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.applies[x]</b>".
	 */
	public static final Include INCLUDE_APPLIES = new Include("Observation.applies[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.dataAbsentReason</b>".
	 */
	public static final Include INCLUDE_DATAABSENTREASON = new Include("Observation.dataAbsentReason");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("Observation.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Observation.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Observation.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("Observation.performer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.related.target</b>".
	 */
	public static final Include INCLUDE_RELATED_TARGET = new Include("Observation.related.target");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.related.type</b>".
	 */
	public static final Include INCLUDE_RELATED_TYPE = new Include("Observation.related.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.reliability</b>".
	 */
	public static final Include INCLUDE_RELIABILITY = new Include("Observation.reliability");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.specimen</b>".
	 */
	public static final Include INCLUDE_SPECIMEN = new Include("Observation.specimen");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Observation.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Observation.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.valueCodeableConcept</b>".
	 */
	public static final Include INCLUDE_VALUECODEABLECONCEPT = new Include("Observation.valueCodeableConcept");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.valueDateTime</b>".
	 */
	public static final Include INCLUDE_VALUEDATETIME = new Include("Observation.valueDateTime");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.valuePeriod</b>".
	 */
	public static final Include INCLUDE_VALUEPERIOD = new Include("Observation.valuePeriod");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.valueQuantity</b>".
	 */
	public static final Include INCLUDE_VALUEQUANTITY = new Include("Observation.valueQuantity");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Observation.valueString</b>".
	 */
	public static final Include INCLUDE_VALUESTRING = new Include("Observation.valueString");


	@Child(name="name", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ObservationType",
		formalDefinition="Describes what was observed. Sometimes this is called the observation \"code\""
	)
	private CodeableConceptDt myName;
	
	@Child(name="value", order=1, min=0, max=1, type={
		QuantityDt.class, 		CodeableConceptDt.class, 		AttachmentDt.class, 		RatioDt.class, 		DateTimeDt.class, 		PeriodDt.class, 		SampledDataDt.class, 		StringDt.class, 		TimeDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The information determined as a result of making the observation, if the information has a simple value"
	)
	private IDatatype myValue;
	
	@Child(name="dataAbsentReason", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="DataAbsentReason",
		formalDefinition="Provides a reason why the expected value in the element Observation.value[x] is missing."
	)
	private BoundCodeDt<DataAbsentReasonEnum> myDataAbsentReason;
	
	@Child(name="interpretation", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ObservationInterpretation",
		formalDefinition="The assessment made based on the result of the observation."
	)
	private BoundCodeableConceptDt<ObservationInterpretationCodesEnum> myInterpretation;
	
	@Child(name="comments", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result."
	)
	private StringDt myComments;
	
	@Child(name="applies", order=5, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself"
	)
	private IDatatype myApplies;
	
	@Child(name="issued", type=InstantDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date and time this observation was made available"
	)
	private InstantDt myIssued;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="ObservationStatus",
		formalDefinition="The status of the result value"
	)
	private BoundCodeDt<ObservationStatusEnum> myStatus;
	
	@Child(name="reliability", type=CodeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="ObservationReliability",
		formalDefinition="An estimate of the degree to which quality issues have impacted on the value reported"
	)
	private BoundCodeDt<ObservationReliabilityEnum> myReliability;
	
	@Child(name="bodySite", type=CodeableConceptDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="BodySite",
		formalDefinition="Indicates the site on the subject's body where the observation was made ( i.e. the target site)."
	)
	private CodeableConceptDt myBodySite;
	
	@Child(name="method", type=CodeableConceptDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="ObservationMethod",
		formalDefinition="Indicates the mechanism used to perform the observation"
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="identifier", type=IdentifierDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A unique identifier for the simple observation"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="subject", order=12, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,other observer (for example a relative or EMT), or any observation made about the subject."
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="specimen", order=13, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Specimen.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The specimen that was used when this observation was made"
	)
	private ResourceReferenceDt mySpecimen;
	
	@Child(name="performer", order=14, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who was responsible for asserting the observed value as \"true\""
	)
	private java.util.List<ResourceReferenceDt> myPerformer;
	
	@Child(name="encounter", order=15, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The healthcare event  ( e.g. a patient and healthcare provider interaction ) that relates to this observation"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="referenceRange", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Guidance on how to interpret the value by comparison to a normal or recommended range"
	)
	private java.util.List<ReferenceRange> myReferenceRange;
	
	@Child(name="related", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Related observations - either components, or previous observations, or statements of derivation"
	)
	private java.util.List<Related> myRelated;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myValue,  myDataAbsentReason,  myInterpretation,  myComments,  myApplies,  myIssued,  myStatus,  myReliability,  myBodySite,  myMethod,  myIdentifier,  mySubject,  mySpecimen,  myPerformer,  myEncounter,  myReferenceRange,  myRelated);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myValue, myDataAbsentReason, myInterpretation, myComments, myApplies, myIssued, myStatus, myReliability, myBodySite, myMethod, myIdentifier, mySubject, mySpecimen, myPerformer, myEncounter, myReferenceRange, myRelated);
	}

	/**
	 * Gets the value(s) for <b>name</b> (ObservationType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation \"code\"
     * </p> 
	 */
	public CodeableConceptDt getName() {  
		if (myName == null) {
			myName = new CodeableConceptDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (ObservationType)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes what was observed. Sometimes this is called the observation \"code\"
     * </p> 
	 */
	public Observation setName(CodeableConceptDt theValue) {
		myName = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>value[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The information determined as a result of making the observation, if the information has a simple value
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
     * The information determined as a result of making the observation, if the information has a simple value
     * </p> 
	 */
	public Observation setValue(IDatatype theValue) {
		myValue = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dataAbsentReason</b> (DataAbsentReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     * </p> 
	 */
	public BoundCodeDt<DataAbsentReasonEnum> getDataAbsentReasonElement() {  
		if (myDataAbsentReason == null) {
			myDataAbsentReason = new BoundCodeDt<DataAbsentReasonEnum>(DataAbsentReasonEnum.VALUESET_BINDER);
		}
		return myDataAbsentReason;
	}

	
	/**
	 * Gets the value(s) for <b>dataAbsentReason</b> (DataAbsentReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     * </p> 
	 */
	public String getDataAbsentReason() {  
		return getDataAbsentReasonElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dataAbsentReason</b> (DataAbsentReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     * </p> 
	 */
	public Observation setDataAbsentReason(BoundCodeDt<DataAbsentReasonEnum> theValue) {
		myDataAbsentReason = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>dataAbsentReason</b> (DataAbsentReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     * </p> 
	 */
	public Observation setDataAbsentReason(DataAbsentReasonEnum theValue) {
		getDataAbsentReasonElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>interpretation</b> (ObservationInterpretation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public BoundCodeableConceptDt<ObservationInterpretationCodesEnum> getInterpretation() {  
		if (myInterpretation == null) {
			myInterpretation = new BoundCodeableConceptDt<ObservationInterpretationCodesEnum>(ObservationInterpretationCodesEnum.VALUESET_BINDER);
		}
		return myInterpretation;
	}

	/**
	 * Sets the value(s) for <b>interpretation</b> (ObservationInterpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public Observation setInterpretation(BoundCodeableConceptDt<ObservationInterpretationCodesEnum> theValue) {
		myInterpretation = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>interpretation</b> (ObservationInterpretation)
	 *
     * <p>
     * <b>Definition:</b>
     * The assessment made based on the result of the observation.
     * </p> 
	 */
	public Observation setInterpretation(ObservationInterpretationCodesEnum theValue) {
		getInterpretation().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
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
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
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
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public Observation setComments(StringDt theValue) {
		myComments = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     * </p> 
	 */
	public Observation setComments( String theString) {
		myComments = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>applies[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getApplies() {  
		return myApplies;
	}

	/**
	 * Sets the value(s) for <b>applies[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the \"physiologically relevant time\". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public Observation setApplies(IDatatype theValue) {
		myApplies = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time this observation was made available
     * </p> 
	 */
	public InstantDt getIssuedElement() {  
		if (myIssued == null) {
			myIssued = new InstantDt();
		}
		return myIssued;
	}

	
	/**
	 * Gets the value(s) for <b>issued</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time this observation was made available
     * </p> 
	 */
	public Date getIssued() {  
		return getIssuedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time this observation was made available
     * </p> 
	 */
	public Observation setIssued(InstantDt theValue) {
		myIssued = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time this observation was made available
     * </p> 
	 */
	public Observation setIssuedWithMillisPrecision( Date theDate) {
		myIssued = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>issued</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time this observation was made available
     * </p> 
	 */
	public Observation setIssued( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIssued = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (ObservationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public BoundCodeDt<ObservationStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ObservationStatusEnum>(ObservationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ObservationStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ObservationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public Observation setStatus(BoundCodeDt<ObservationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ObservationStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the result value
     * </p> 
	 */
	public Observation setStatus(ObservationStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reliability</b> (ObservationReliability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public BoundCodeDt<ObservationReliabilityEnum> getReliabilityElement() {  
		if (myReliability == null) {
			myReliability = new BoundCodeDt<ObservationReliabilityEnum>(ObservationReliabilityEnum.VALUESET_BINDER);
		}
		return myReliability;
	}

	
	/**
	 * Gets the value(s) for <b>reliability</b> (ObservationReliability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public String getReliability() {  
		return getReliabilityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reliability</b> (ObservationReliability)
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public Observation setReliability(BoundCodeDt<ObservationReliabilityEnum> theValue) {
		myReliability = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>reliability</b> (ObservationReliability)
	 *
     * <p>
     * <b>Definition:</b>
     * An estimate of the degree to which quality issues have impacted on the value reported
     * </p> 
	 */
	public Observation setReliability(ObservationReliabilityEnum theValue) {
		getReliabilityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>bodySite</b> (BodySite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the site on the subject's body where the observation was made ( i.e. the target site).
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new CodeableConceptDt();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (BodySite)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the site on the subject's body where the observation was made ( i.e. the target site).
     * </p> 
	 */
	public Observation setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>method</b> (ObservationMethod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (ObservationMethod)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the mechanism used to perform the observation
     * </p> 
	 */
	public Observation setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for the simple observation
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
     * A unique identifier for the simple observation
     * </p> 
	 */
	public Observation setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,other observer (for example a relative or EMT), or any observation made about the subject.
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
     * The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,other observer (for example a relative or EMT), or any observation made about the subject.
     * </p> 
	 */
	public Observation setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specimen</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made
     * </p> 
	 */
	public ResourceReferenceDt getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new ResourceReferenceDt();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen that was used when this observation was made
     * </p> 
	 */
	public Observation setSpecimen(ResourceReferenceDt theValue) {
		mySpecimen = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>performer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as \"true\"
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as \"true\"
     * </p> 
	 */
	public Observation setPerformer(java.util.List<ResourceReferenceDt> theValue) {
		myPerformer = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Who was responsible for asserting the observed value as \"true\"
     * </p> 
	 */
	public ResourceReferenceDt addPerformer() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getPerformer().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare event  ( e.g. a patient and healthcare provider interaction ) that relates to this observation
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
     * The healthcare event  ( e.g. a patient and healthcare provider interaction ) that relates to this observation
     * </p> 
	 */
	public Observation setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>referenceRange</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public java.util.List<ReferenceRange> getReferenceRange() {  
		if (myReferenceRange == null) {
			myReferenceRange = new java.util.ArrayList<ReferenceRange>();
		}
		return myReferenceRange;
	}

	/**
	 * Sets the value(s) for <b>referenceRange</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public Observation setReferenceRange(java.util.List<ReferenceRange> theValue) {
		myReferenceRange = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>referenceRange</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public ReferenceRange addReferenceRange() {
		ReferenceRange newType = new ReferenceRange();
		getReferenceRange().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>referenceRange</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	public ReferenceRange getReferenceRangeFirstRep() {
		if (getReferenceRange().isEmpty()) {
			return addReferenceRange();
		}
		return getReferenceRange().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>related</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public java.util.List<Related> getRelated() {  
		if (myRelated == null) {
			myRelated = new java.util.ArrayList<Related>();
		}
		return myRelated;
	}

	/**
	 * Sets the value(s) for <b>related</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public Observation setRelated(java.util.List<Related> theValue) {
		myRelated = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>related</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public Related addRelated() {
		Related newType = new Related();
		getRelated().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>related</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	public Related getRelatedFirstRep() {
		if (getRelated().isEmpty()) {
			return addRelated();
		}
		return getRelated().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Observation.referenceRange</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how to interpret the value by comparison to a normal or recommended range
     * </p> 
	 */
	@Block()	
	public static class ReferenceRange 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="low", type=QuantityDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3"
	)
	private QuantityDt myLow;
	
	@Child(name="high", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5"
	)
	private QuantityDt myHigh;
	
	@Child(name="meaning", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ObservationRangeMeaning",
		formalDefinition="Code for the meaning of the reference range"
	)
	private CodeableConceptDt myMeaning;
	
	@Child(name="age", type=RangeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so"
	)
	private RangeDt myAge;
	
	@Child(name="text", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'."
	)
	private StringDt myText;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLow,  myHigh,  myMeaning,  myAge,  myText);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLow, myHigh, myMeaning, myAge, myText);
	}

	/**
	 * Gets the value(s) for <b>low</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public QuantityDt getLow() {  
		if (myLow == null) {
			myLow = new QuantityDt();
		}
		return myLow;
	}

	/**
	 * Sets the value(s) for <b>low</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the low bound of the reference range. If this is omitted, the low bound of the reference range is assumed to be meaningless. E.g. <2.3
     * </p> 
	 */
	public ReferenceRange setLow(QuantityDt theValue) {
		myLow = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>high</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public QuantityDt getHigh() {  
		if (myHigh == null) {
			myHigh = new QuantityDt();
		}
		return myHigh;
	}

	/**
	 * Sets the value(s) for <b>high</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the high bound of the reference range. If this is omitted, the high bound of the reference range is assumed to be meaningless. E.g. >5
     * </p> 
	 */
	public ReferenceRange setHigh(QuantityDt theValue) {
		myHigh = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>meaning</b> (ObservationRangeMeaning).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code for the meaning of the reference range
     * </p> 
	 */
	public CodeableConceptDt getMeaning() {  
		if (myMeaning == null) {
			myMeaning = new CodeableConceptDt();
		}
		return myMeaning;
	}

	/**
	 * Sets the value(s) for <b>meaning</b> (ObservationRangeMeaning)
	 *
     * <p>
     * <b>Definition:</b>
     * Code for the meaning of the reference range
     * </p> 
	 */
	public ReferenceRange setMeaning(CodeableConceptDt theValue) {
		myMeaning = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>age</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so
     * </p> 
	 */
	public RangeDt getAge() {  
		if (myAge == null) {
			myAge = new RangeDt();
		}
		return myAge;
	}

	/**
	 * Sets the value(s) for <b>age</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so
     * </p> 
	 */
	public ReferenceRange setAge(RangeDt theValue) {
		myAge = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'.
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
     * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'.
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
     * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'.
     * </p> 
	 */
	public ReferenceRange setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of \"Negative\" or a list or table of 'normals'.
     * </p> 
	 */
	public ReferenceRange setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Observation.related</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Related observations - either components, or previous observations, or statements of derivation
     * </p> 
	 */
	@Block()	
	public static class Related 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ObservationRelationshipType",
		formalDefinition="A code specifying the kind of relationship that exists with the target observation"
	)
	private BoundCodeDt<ObservationRelationshipTypeEnum> myType;
	
	@Child(name="target", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Observation.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A reference to the observation that is related to this observation"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myTarget);
	}

	/**
	 * Gets the value(s) for <b>type</b> (ObservationRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public BoundCodeDt<ObservationRelationshipTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<ObservationRelationshipTypeEnum>(ObservationRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (ObservationRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (ObservationRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public Related setType(BoundCodeDt<ObservationRelationshipTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (ObservationRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the kind of relationship that exists with the target observation
     * </p> 
	 */
	public Related setType(ObservationRelationshipTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the observation that is related to this observation
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
     * A reference to the observation that is related to this observation
     * </p> 
	 */
	public Related setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Observation";
    }

}
