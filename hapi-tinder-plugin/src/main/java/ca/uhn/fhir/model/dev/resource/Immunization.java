















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
 * HAPI/FHIR <b>Immunization</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Immunization event information
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Immunization">http://hl7.org/fhir/profiles/Immunization</a> 
 * </p>
 *
 */
@ResourceDef(name="Immunization", profile="http://hl7.org/fhir/profiles/Immunization", id="immunization")
public class Immunization 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Vaccination  Administration / Refusal Date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Immunization.date", description="Vaccination  Administration / Refusal Date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Vaccination  Administration / Refusal Date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Immunization.vaccinationProtocol.doseSequence</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dose-sequence", path="Immunization.vaccinationProtocol.doseSequence", description="", type="number"  )
	public static final String SP_DOSE_SEQUENCE = "dose-sequence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Immunization.vaccinationProtocol.doseSequence</b><br/>
	 * </p>
	 */
	public static final NumberClientParam DOSE_SEQUENCE = new NumberClientParam(SP_DOSE_SEQUENCE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Immunization.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>The service delivery location or facility in which the vaccine was / was to be administered</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Immunization.location", description="The service delivery location or facility in which the vaccine was / was to be administered", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>The service delivery location or facility in which the vaccine was / was to be administered</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>lot-number</b>
	 * <p>
	 * Description: <b>Vaccine Lot Number</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Immunization.lotNumber</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="lot-number", path="Immunization.lotNumber", description="Vaccine Lot Number", type="string"  )
	public static final String SP_LOT_NUMBER = "lot-number";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>lot-number</b>
	 * <p>
	 * Description: <b>Vaccine Lot Number</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Immunization.lotNumber</b><br/>
	 * </p>
	 */
	public static final StringClientParam LOT_NUMBER = new StringClientParam(SP_LOT_NUMBER);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>Vaccine Manufacturer</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.manufacturer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Immunization.manufacturer", description="Vaccine Manufacturer", type="reference"  )
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>Vaccine Manufacturer</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.manufacturer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MANUFACTURER = new ReferenceClientParam(SP_MANUFACTURER);

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>The practitioner who administered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.performer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="Immunization.performer", description="The practitioner who administered the vaccination", type="reference"  )
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>The practitioner who administered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.performer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PERFORMER = new ReferenceClientParam(SP_PERFORMER);

	/**
	 * Search parameter constant for <b>reaction</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.reaction.detail</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reaction", path="Immunization.reaction.detail", description="", type="reference"  )
	public static final String SP_REACTION = "reaction";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reaction</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.reaction.detail</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REACTION = new ReferenceClientParam(SP_REACTION);

	/**
	 * Search parameter constant for <b>reaction-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.reaction.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reaction-date", path="Immunization.reaction.date", description="", type="date"  )
	public static final String SP_REACTION_DATE = "reaction-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reaction-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.reaction.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam REACTION_DATE = new DateClientParam(SP_REACTION_DATE);

	/**
	 * Search parameter constant for <b>reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.reason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reason", path="Immunization.explanation.reason", description="", type="token"  )
	public static final String SP_REASON = "reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.reason</b><br/>
	 * </p>
	 */
	public static final TokenClientParam REASON = new TokenClientParam(SP_REASON);

	/**
	 * Search parameter constant for <b>refusal-reason</b>
	 * <p>
	 * Description: <b>Explanation of refusal / exemption</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.refusalReason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="refusal-reason", path="Immunization.explanation.refusalReason", description="Explanation of refusal / exemption", type="token"  )
	public static final String SP_REFUSAL_REASON = "refusal-reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>refusal-reason</b>
	 * <p>
	 * Description: <b>Explanation of refusal / exemption</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.refusalReason</b><br/>
	 * </p>
	 */
	public static final TokenClientParam REFUSAL_REASON = new TokenClientParam(SP_REFUSAL_REASON);

	/**
	 * Search parameter constant for <b>refused</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.refusedIndicator</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="refused", path="Immunization.refusedIndicator", description="", type="token"  )
	public static final String SP_REFUSED = "refused";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>refused</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.refusedIndicator</b><br/>
	 * </p>
	 */
	public static final TokenClientParam REFUSED = new TokenClientParam(SP_REFUSED);

	/**
	 * Search parameter constant for <b>requester</b>
	 * <p>
	 * Description: <b>The practitioner who ordered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.requester</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="requester", path="Immunization.requester", description="The practitioner who ordered the vaccination", type="reference"  )
	public static final String SP_REQUESTER = "requester";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>requester</b>
	 * <p>
	 * Description: <b>The practitioner who ordered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.requester</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REQUESTER = new ReferenceClientParam(SP_REQUESTER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the vaccination event / refusal</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Immunization.subject", description="The subject of the vaccination event / refusal", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the vaccination event / refusal</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b>Vaccine Product Type Administered</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.vaccineType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="vaccine-type", path="Immunization.vaccineType", description="Vaccine Product Type Administered", type="token"  )
	public static final String SP_VACCINE_TYPE = "vaccine-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b>Vaccine Product Type Administered</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.vaccineType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VACCINE_TYPE = new TokenClientParam(SP_VACCINE_TYPE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient for the vaccination event / refusal</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Immunization.subject", description="The patient for the vaccination event / refusal", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The patient for the vaccination event / refusal</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Immunization.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.explanation.reason</b>".
	 */
	public static final Include INCLUDE_EXPLANATION_REASON = new Include("Immunization.explanation.reason");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.explanation.refusalReason</b>".
	 */
	public static final Include INCLUDE_EXPLANATION_REFUSALREASON = new Include("Immunization.explanation.refusalReason");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Immunization.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Immunization.location");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.lotNumber</b>".
	 */
	public static final Include INCLUDE_LOTNUMBER = new Include("Immunization.lotNumber");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.manufacturer</b>".
	 */
	public static final Include INCLUDE_MANUFACTURER = new Include("Immunization.manufacturer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("Immunization.performer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.reaction.date</b>".
	 */
	public static final Include INCLUDE_REACTION_DATE = new Include("Immunization.reaction.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.reaction.detail</b>".
	 */
	public static final Include INCLUDE_REACTION_DETAIL = new Include("Immunization.reaction.detail");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.refusedIndicator</b>".
	 */
	public static final Include INCLUDE_REFUSEDINDICATOR = new Include("Immunization.refusedIndicator");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.requester</b>".
	 */
	public static final Include INCLUDE_REQUESTER = new Include("Immunization.requester");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Immunization.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.vaccinationProtocol.doseSequence</b>".
	 */
	public static final Include INCLUDE_VACCINATIONPROTOCOL_DOSESEQUENCE = new Include("Immunization.vaccinationProtocol.doseSequence");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.vaccineType</b>".
	 */
	public static final Include INCLUDE_VACCINETYPE = new Include("Immunization.vaccineType");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A unique identifier assigned to this adverse reaction record."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date vaccine administered or was to be administered"
	)
	private DateTimeDt myDate;
	
	@Child(name="vaccineType", type=CodeableConceptDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="VaccineType",
		formalDefinition="Vaccine that was administered or was to be administered"
	)
	private CodeableConceptDt myVaccineType;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The patient to whom the vaccine was to be administered"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="refusedIndicator", type=BooleanDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates if the vaccination was refused."
	)
	private BooleanDt myRefusedIndicator;
	
	@Child(name="reported", type=BooleanDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="True if this administration was reported rather than directly administered."
	)
	private BooleanDt myReported;
	
	@Child(name="performer", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Clinician who administered the vaccine"
	)
	private ResourceReferenceDt myPerformer;
	
	@Child(name="requester", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Clinician who ordered the vaccination"
	)
	private ResourceReferenceDt myRequester;
	
	@Child(name="manufacturer", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Name of vaccine manufacturer"
	)
	private ResourceReferenceDt myManufacturer;
	
	@Child(name="location", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The service delivery location where the vaccine administration occurred."
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="lotNumber", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Lot number of the  vaccine product"
	)
	private StringDt myLotNumber;
	
	@Child(name="expirationDate", type=DateDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date vaccine batch expires"
	)
	private DateDt myExpirationDate;
	
	@Child(name="site", type=CodeableConceptDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="ImmunizationSite",
		formalDefinition="Body site where vaccine was administered"
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="ImmunizationRoute",
		formalDefinition="The path by which the vaccine product is taken into the body."
	)
	private BoundCodeableConceptDt<ImmunizationRouteCodesEnum> myRoute;
	
	@Child(name="doseQuantity", type=QuantityDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The quantity of vaccine product that was administered"
	)
	private QuantityDt myDoseQuantity;
	
	@Child(name="explanation", order=15, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Reasons why a vaccine was administered or refused"
	)
	private Explanation myExplanation;
	
	@Child(name="reaction", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Categorical data indicating that an adverse event is associated in time to an immunization"
	)
	private java.util.List<Reaction> myReaction;
	
	@Child(name="vaccinationProtocol", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contains information about the protocol(s) under which the vaccine was administered"
	)
	private java.util.List<VaccinationProtocol> myVaccinationProtocol;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  myVaccineType,  mySubject,  myRefusedIndicator,  myReported,  myPerformer,  myRequester,  myManufacturer,  myLocation,  myLotNumber,  myExpirationDate,  mySite,  myRoute,  myDoseQuantity,  myExplanation,  myReaction,  myVaccinationProtocol);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, myVaccineType, mySubject, myRefusedIndicator, myReported, myPerformer, myRequester, myManufacturer, myLocation, myLotNumber, myExpirationDate, mySite, myRoute, myDoseQuantity, myExplanation, myReaction, myVaccinationProtocol);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
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
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
	 */
	public Immunization setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
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
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
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
     * Date vaccine administered or was to be administered
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
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public Immunization setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public Immunization setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public Immunization setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>vaccineType</b> (VaccineType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that was administered or was to be administered
     * </p> 
	 */
	public CodeableConceptDt getVaccineType() {  
		if (myVaccineType == null) {
			myVaccineType = new CodeableConceptDt();
		}
		return myVaccineType;
	}

	/**
	 * Sets the value(s) for <b>vaccineType</b> (VaccineType)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that was administered or was to be administered
     * </p> 
	 */
	public Immunization setVaccineType(CodeableConceptDt theValue) {
		myVaccineType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient to whom the vaccine was to be administered
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
     * The patient to whom the vaccine was to be administered
     * </p> 
	 */
	public Immunization setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>refusedIndicator</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public BooleanDt getRefusedIndicatorElement() {  
		if (myRefusedIndicator == null) {
			myRefusedIndicator = new BooleanDt();
		}
		return myRefusedIndicator;
	}

	
	/**
	 * Gets the value(s) for <b>refusedIndicator</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public Boolean getRefusedIndicator() {  
		return getRefusedIndicatorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>refusedIndicator</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public Immunization setRefusedIndicator(BooleanDt theValue) {
		myRefusedIndicator = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>refusedIndicator</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public Immunization setRefusedIndicator( boolean theBoolean) {
		myRefusedIndicator = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reported</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public BooleanDt getReportedElement() {  
		if (myReported == null) {
			myReported = new BooleanDt();
		}
		return myReported;
	}

	
	/**
	 * Gets the value(s) for <b>reported</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public Boolean getReported() {  
		return getReportedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reported</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public Immunization setReported(BooleanDt theValue) {
		myReported = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reported</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public Immunization setReported( boolean theBoolean) {
		myReported = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>performer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who administered the vaccine
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
     * Clinician who administered the vaccine
     * </p> 
	 */
	public Immunization setPerformer(ResourceReferenceDt theValue) {
		myPerformer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>requester</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who ordered the vaccination
     * </p> 
	 */
	public ResourceReferenceDt getRequester() {  
		if (myRequester == null) {
			myRequester = new ResourceReferenceDt();
		}
		return myRequester;
	}

	/**
	 * Sets the value(s) for <b>requester</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who ordered the vaccination
     * </p> 
	 */
	public Immunization setRequester(ResourceReferenceDt theValue) {
		myRequester = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>manufacturer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of vaccine manufacturer
     * </p> 
	 */
	public ResourceReferenceDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReferenceDt();
		}
		return myManufacturer;
	}

	/**
	 * Sets the value(s) for <b>manufacturer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of vaccine manufacturer
     * </p> 
	 */
	public Immunization setManufacturer(ResourceReferenceDt theValue) {
		myManufacturer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The service delivery location where the vaccine administration occurred.
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
     * The service delivery location where the vaccine administration occurred.
     * </p> 
	 */
	public Immunization setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>lotNumber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public StringDt getLotNumberElement() {  
		if (myLotNumber == null) {
			myLotNumber = new StringDt();
		}
		return myLotNumber;
	}

	
	/**
	 * Gets the value(s) for <b>lotNumber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public String getLotNumber() {  
		return getLotNumberElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lotNumber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public Immunization setLotNumber(StringDt theValue) {
		myLotNumber = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lotNumber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public Immunization setLotNumber( String theString) {
		myLotNumber = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expirationDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public DateDt getExpirationDateElement() {  
		if (myExpirationDate == null) {
			myExpirationDate = new DateDt();
		}
		return myExpirationDate;
	}

	
	/**
	 * Gets the value(s) for <b>expirationDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Date getExpirationDate() {  
		return getExpirationDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>expirationDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Immunization setExpirationDate(DateDt theValue) {
		myExpirationDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>expirationDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Immunization setExpirationDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myExpirationDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>expirationDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Immunization setExpirationDateWithDayPrecision( Date theDate) {
		myExpirationDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>site</b> (ImmunizationSite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Body site where vaccine was administered
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (ImmunizationSite)
	 *
     * <p>
     * <b>Definition:</b>
     * Body site where vaccine was administered
     * </p> 
	 */
	public Immunization setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>route</b> (ImmunizationRoute).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The path by which the vaccine product is taken into the body.
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationRouteCodesEnum> getRoute() {  
		if (myRoute == null) {
			myRoute = new BoundCodeableConceptDt<ImmunizationRouteCodesEnum>(ImmunizationRouteCodesEnum.VALUESET_BINDER);
		}
		return myRoute;
	}

	/**
	 * Sets the value(s) for <b>route</b> (ImmunizationRoute)
	 *
     * <p>
     * <b>Definition:</b>
     * The path by which the vaccine product is taken into the body.
     * </p> 
	 */
	public Immunization setRoute(BoundCodeableConceptDt<ImmunizationRouteCodesEnum> theValue) {
		myRoute = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>route</b> (ImmunizationRoute)
	 *
     * <p>
     * <b>Definition:</b>
     * The path by which the vaccine product is taken into the body.
     * </p> 
	 */
	public Immunization setRoute(ImmunizationRouteCodesEnum theValue) {
		getRoute().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>doseQuantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public QuantityDt getDoseQuantity() {  
		if (myDoseQuantity == null) {
			myDoseQuantity = new QuantityDt();
		}
		return myDoseQuantity;
	}

	/**
	 * Sets the value(s) for <b>doseQuantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity(QuantityDt theValue) {
		myDoseQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>explanation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered or refused
     * </p> 
	 */
	public Explanation getExplanation() {  
		if (myExplanation == null) {
			myExplanation = new Explanation();
		}
		return myExplanation;
	}

	/**
	 * Sets the value(s) for <b>explanation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered or refused
     * </p> 
	 */
	public Immunization setExplanation(Explanation theValue) {
		myExplanation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reaction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public java.util.List<Reaction> getReaction() {  
		if (myReaction == null) {
			myReaction = new java.util.ArrayList<Reaction>();
		}
		return myReaction;
	}

	/**
	 * Sets the value(s) for <b>reaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public Immunization setReaction(java.util.List<Reaction> theValue) {
		myReaction = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>reaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public Reaction addReaction() {
		Reaction newType = new Reaction();
		getReaction().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reaction</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public Reaction getReactionFirstRep() {
		if (getReaction().isEmpty()) {
			return addReaction();
		}
		return getReaction().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>vaccinationProtocol</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public java.util.List<VaccinationProtocol> getVaccinationProtocol() {  
		if (myVaccinationProtocol == null) {
			myVaccinationProtocol = new java.util.ArrayList<VaccinationProtocol>();
		}
		return myVaccinationProtocol;
	}

	/**
	 * Sets the value(s) for <b>vaccinationProtocol</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public Immunization setVaccinationProtocol(java.util.List<VaccinationProtocol> theValue) {
		myVaccinationProtocol = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>vaccinationProtocol</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol addVaccinationProtocol() {
		VaccinationProtocol newType = new VaccinationProtocol();
		getVaccinationProtocol().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>vaccinationProtocol</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol getVaccinationProtocolFirstRep() {
		if (getVaccinationProtocol().isEmpty()) {
			return addVaccinationProtocol();
		}
		return getVaccinationProtocol().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Immunization.explanation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered or refused
     * </p> 
	 */
	@Block()	
	public static class Explanation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="reason", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ImmunizationReason",
		formalDefinition="Reasons why a vaccine was administered"
	)
	private java.util.List<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>> myReason;
	
	@Child(name="refusalReason", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="NoImmunizationReason",
		formalDefinition="Refusal or exemption reasons"
	)
	private java.util.List<CodeableConceptDt> myRefusalReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myReason,  myRefusalReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myReason, myRefusalReason);
	}

	/**
	 * Gets the value(s) for <b>reason</b> (ImmunizationReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>> getReason() {  
		if (myReason == null) {
			myReason = new java.util.ArrayList<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>>();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (ImmunizationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public Explanation setReason(java.util.List<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>> theValue) {
		myReason = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>reason</b> (ImmunizationReason) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationReasonCodesEnum> addReason(ImmunizationReasonCodesEnum theValue) {
		BoundCodeableConceptDt<ImmunizationReasonCodesEnum> retVal = new BoundCodeableConceptDt<ImmunizationReasonCodesEnum>(ImmunizationReasonCodesEnum.VALUESET_BINDER, theValue);
		getReason().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>reason</b> (ImmunizationReason),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationReasonCodesEnum> getReasonFirstRep() {
		if (getReason().size() == 0) {
			addReason();
		}
		return getReason().get(0);
	}

	/**
	 * Add a value for <b>reason</b> (ImmunizationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationReasonCodesEnum> addReason() {
		BoundCodeableConceptDt<ImmunizationReasonCodesEnum> retVal = new BoundCodeableConceptDt<ImmunizationReasonCodesEnum>(ImmunizationReasonCodesEnum.VALUESET_BINDER);
		getReason().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>reason</b> (ImmunizationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public Explanation setReason(ImmunizationReasonCodesEnum theValue) {
		getReason().clear();
		addReason(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>refusalReason</b> (NoImmunizationReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRefusalReason() {  
		if (myRefusalReason == null) {
			myRefusalReason = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRefusalReason;
	}

	/**
	 * Sets the value(s) for <b>refusalReason</b> (NoImmunizationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public Explanation setRefusalReason(java.util.List<CodeableConceptDt> theValue) {
		myRefusalReason = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>refusalReason</b> (NoImmunizationReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public CodeableConceptDt addRefusalReason() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRefusalReason().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>refusalReason</b> (NoImmunizationReason),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public CodeableConceptDt getRefusalReasonFirstRep() {
		if (getRefusalReason().isEmpty()) {
			return addRefusalReason();
		}
		return getRefusalReason().get(0); 
	}
  

	}


	/**
	 * Block class for child element: <b>Immunization.reaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	@Block()	
	public static class Reaction 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="date", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date of reaction to the immunization"
	)
	private DateTimeDt myDate;
	
	@Child(name="detail", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Observation.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Details of the reaction"
	)
	private ResourceReferenceDt myDetail;
	
	@Child(name="reported", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Self-reported indicator"
	)
	private BooleanDt myReported;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDate,  myDetail,  myReported);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDate, myDetail, myReported);
	}

	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
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
     * Date of reaction to the immunization
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
     * Date of reaction to the immunization
     * </p> 
	 */
	public Reaction setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
     * </p> 
	 */
	public Reaction setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
     * </p> 
	 */
	public Reaction setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the reaction
     * </p> 
	 */
	public ResourceReferenceDt getDetail() {  
		if (myDetail == null) {
			myDetail = new ResourceReferenceDt();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the reaction
     * </p> 
	 */
	public Reaction setDetail(ResourceReferenceDt theValue) {
		myDetail = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reported</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public BooleanDt getReportedElement() {  
		if (myReported == null) {
			myReported = new BooleanDt();
		}
		return myReported;
	}

	
	/**
	 * Gets the value(s) for <b>reported</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public Boolean getReported() {  
		return getReportedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reported</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public Reaction setReported(BooleanDt theValue) {
		myReported = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reported</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public Reaction setReported( boolean theBoolean) {
		myReported = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Immunization.vaccinationProtocol</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	@Block()	
	public static class VaccinationProtocol 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="doseSequence", type=IntegerDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Nominal position in a series"
	)
	private IntegerDt myDoseSequence;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Contains the description about the protocol under which the vaccine was administered"
	)
	private StringDt myDescription;
	
	@Child(name="authority", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the authority who published the protocol?  E.g. ACIP"
	)
	private ResourceReferenceDt myAuthority;
	
	@Child(name="series", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority"
	)
	private StringDt mySeries;
	
	@Child(name="seriesDoses", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The recommended number of doses to achieve immunity."
	)
	private IntegerDt mySeriesDoses;
	
	@Child(name="doseTarget", type=CodeableConceptDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="VaccinationProtocolDoseTarget",
		formalDefinition="The targeted disease"
	)
	private CodeableConceptDt myDoseTarget;
	
	@Child(name="doseStatus", type=CodeableConceptDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="VaccinationProtocolDoseStatus",
		formalDefinition="Indicates if the immunization event should \"count\" against  the protocol."
	)
	private CodeableConceptDt myDoseStatus;
	
	@Child(name="doseStatusReason", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="VaccinationProtocolDoseStatusReason",
		formalDefinition="Provides an explanation as to why a immunization event should or should not count against the protocol."
	)
	private CodeableConceptDt myDoseStatusReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDoseSequence,  myDescription,  myAuthority,  mySeries,  mySeriesDoses,  myDoseTarget,  myDoseStatus,  myDoseStatusReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDoseSequence, myDescription, myAuthority, mySeries, mySeriesDoses, myDoseTarget, myDoseStatus, myDoseStatusReason);
	}

	/**
	 * Gets the value(s) for <b>doseSequence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public IntegerDt getDoseSequenceElement() {  
		if (myDoseSequence == null) {
			myDoseSequence = new IntegerDt();
		}
		return myDoseSequence;
	}

	
	/**
	 * Gets the value(s) for <b>doseSequence</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public Integer getDoseSequence() {  
		return getDoseSequenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>doseSequence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public VaccinationProtocol setDoseSequence(IntegerDt theValue) {
		myDoseSequence = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>doseSequence</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public VaccinationProtocol setDoseSequence( int theInteger) {
		myDoseSequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
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
     * Contains the description about the protocol under which the vaccine was administered
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
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>authority</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the authority who published the protocol?  E.g. ACIP
     * </p> 
	 */
	public ResourceReferenceDt getAuthority() {  
		if (myAuthority == null) {
			myAuthority = new ResourceReferenceDt();
		}
		return myAuthority;
	}

	/**
	 * Sets the value(s) for <b>authority</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the authority who published the protocol?  E.g. ACIP
     * </p> 
	 */
	public VaccinationProtocol setAuthority(ResourceReferenceDt theValue) {
		myAuthority = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>series</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public StringDt getSeriesElement() {  
		if (mySeries == null) {
			mySeries = new StringDt();
		}
		return mySeries;
	}

	
	/**
	 * Gets the value(s) for <b>series</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public String getSeries() {  
		return getSeriesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public VaccinationProtocol setSeries(StringDt theValue) {
		mySeries = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>series</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public VaccinationProtocol setSeries( String theString) {
		mySeries = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>seriesDoses</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public IntegerDt getSeriesDosesElement() {  
		if (mySeriesDoses == null) {
			mySeriesDoses = new IntegerDt();
		}
		return mySeriesDoses;
	}

	
	/**
	 * Gets the value(s) for <b>seriesDoses</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public Integer getSeriesDoses() {  
		return getSeriesDosesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>seriesDoses</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public VaccinationProtocol setSeriesDoses(IntegerDt theValue) {
		mySeriesDoses = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>seriesDoses</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public VaccinationProtocol setSeriesDoses( int theInteger) {
		mySeriesDoses = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>doseTarget</b> (VaccinationProtocolDoseTarget).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The targeted disease
     * </p> 
	 */
	public CodeableConceptDt getDoseTarget() {  
		if (myDoseTarget == null) {
			myDoseTarget = new CodeableConceptDt();
		}
		return myDoseTarget;
	}

	/**
	 * Sets the value(s) for <b>doseTarget</b> (VaccinationProtocolDoseTarget)
	 *
     * <p>
     * <b>Definition:</b>
     * The targeted disease
     * </p> 
	 */
	public VaccinationProtocol setDoseTarget(CodeableConceptDt theValue) {
		myDoseTarget = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>doseStatus</b> (VaccinationProtocolDoseStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the immunization event should \"count\" against  the protocol.
     * </p> 
	 */
	public CodeableConceptDt getDoseStatus() {  
		if (myDoseStatus == null) {
			myDoseStatus = new CodeableConceptDt();
		}
		return myDoseStatus;
	}

	/**
	 * Sets the value(s) for <b>doseStatus</b> (VaccinationProtocolDoseStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the immunization event should \"count\" against  the protocol.
     * </p> 
	 */
	public VaccinationProtocol setDoseStatus(CodeableConceptDt theValue) {
		myDoseStatus = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>doseStatusReason</b> (VaccinationProtocolDoseStatusReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides an explanation as to why a immunization event should or should not count against the protocol.
     * </p> 
	 */
	public CodeableConceptDt getDoseStatusReason() {  
		if (myDoseStatusReason == null) {
			myDoseStatusReason = new CodeableConceptDt();
		}
		return myDoseStatusReason;
	}

	/**
	 * Sets the value(s) for <b>doseStatusReason</b> (VaccinationProtocolDoseStatusReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides an explanation as to why a immunization event should or should not count against the protocol.
     * </p> 
	 */
	public VaccinationProtocol setDoseStatusReason(CodeableConceptDt theValue) {
		myDoseStatusReason = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Immunization";
    }

}
