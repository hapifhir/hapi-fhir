















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
 * HAPI/FHIR <b>Group</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized.  I.e. A collection of entities that isn't an Organization
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Group">http://hl7.org/fhir/profiles/Group</a> 
 * </p>
 *
 */
@ResourceDef(name="Group", profile="http://hl7.org/fhir/profiles/Group", id="group")
public class Group 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of resources the group contains</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Group.type", description="The type of resources the group contains", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of resources the group contains</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The kind of resources contained</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Group.code", description="The kind of resources contained", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The kind of resources contained</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>actual</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.actual</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="actual", path="Group.actual", description="", type="token"  )
	public static final String SP_ACTUAL = "actual";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>actual</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.actual</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTUAL = new TokenClientParam(SP_ACTUAL);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Group.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>member</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Group.member</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="member", path="Group.member", description="", type="reference"  )
	public static final String SP_MEMBER = "member";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>member</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Group.member</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MEMBER = new ReferenceClientParam(SP_MEMBER);

	/**
	 * Search parameter constant for <b>characteristic</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="characteristic", path="Group.characteristic.code", description="", type="token"  )
	public static final String SP_CHARACTERISTIC = "characteristic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>characteristic</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CHARACTERISTIC = new TokenClientParam(SP_CHARACTERISTIC);

	/**
	 * Search parameter constant for <b>value</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value", path="Group.characteristic.value[x]", description="", type="token"  )
	public static final String SP_VALUE = "value";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.value[x]</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VALUE = new TokenClientParam(SP_VALUE);

	/**
	 * Search parameter constant for <b>exclude</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.exclude</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="exclude", path="Group.characteristic.exclude", description="", type="token"  )
	public static final String SP_EXCLUDE = "exclude";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>exclude</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.exclude</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EXCLUDE = new TokenClientParam(SP_EXCLUDE);

	/**
	 * Search parameter constant for <b>characteristic-value</b>
	 * <p>
	 * Description: <b>A composite of both characteristic and value</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>characteristic & value</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="characteristic-value", path="characteristic & value", description="A composite of both characteristic and value", type="composite"  , compositeOf={  "characteristic",  "value" }  )
	public static final String SP_CHARACTERISTIC_VALUE = "characteristic-value";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>characteristic-value</b>
	 * <p>
	 * Description: <b>A composite of both characteristic and value</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>characteristic & value</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, TokenClientParam> CHARACTERISTIC_VALUE = new CompositeClientParam<TokenClientParam, TokenClientParam>(SP_CHARACTERISTIC_VALUE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.actual</b>".
	 */
	public static final Include INCLUDE_ACTUAL = new Include("Group.actual");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.characteristic.code</b>".
	 */
	public static final Include INCLUDE_CHARACTERISTIC_CODE = new Include("Group.characteristic.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.characteristic.exclude</b>".
	 */
	public static final Include INCLUDE_CHARACTERISTIC_EXCLUDE = new Include("Group.characteristic.exclude");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.characteristic.value[x]</b>".
	 */
	public static final Include INCLUDE_CHARACTERISTIC_VALUE = new Include("Group.characteristic.value[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("Group.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Group.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.member</b>".
	 */
	public static final Include INCLUDE_MEMBER = new Include("Group.member");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Group.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A unique business identifier for this group"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="GroupType",
		formalDefinition="Identifies the broad classification of the kind of resources the group includes"
	)
	private BoundCodeDt<GroupTypeEnum> myType;
	
	@Child(name="actual", type=BooleanDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals"
	)
	private BooleanDt myActual;
	
	@Child(name="code", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="GroupKind",
		formalDefinition="Provides a specific type of resource the group includes.  E.g. \"cow\", \"syringe\", etc."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A label assigned to the group for human identification and communication"
	)
	private StringDt myName;
	
	@Child(name="quantity", type=IntegerDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A count of the number of resource instances that are part of the group"
	)
	private IntegerDt myQuantity;
	
	@Child(name="characteristic", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the traits shared by members of the group"
	)
	private java.util.List<Characteristic> myCharacteristic;
	
	@Child(name="member", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Medication.class, 		ca.uhn.fhir.model.dev.resource.Substance.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the resource instances that are members of the group."
	)
	private java.util.List<ResourceReferenceDt> myMember;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  myActual,  myCode,  myName,  myQuantity,  myCharacteristic,  myMember);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, myActual, myCode, myName, myQuantity, myCharacteristic, myMember);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
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
     * A unique business identifier for this group
     * </p> 
	 */
	public Group setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (GroupType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public BoundCodeDt<GroupTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<GroupTypeEnum>(GroupTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (GroupType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (GroupType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public Group setType(BoundCodeDt<GroupTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (GroupType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public Group setType(GroupTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>actual</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public BooleanDt getActualElement() {  
		if (myActual == null) {
			myActual = new BooleanDt();
		}
		return myActual;
	}

	
	/**
	 * Gets the value(s) for <b>actual</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public Boolean getActual() {  
		return getActualElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>actual</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public Group setActual(BooleanDt theValue) {
		myActual = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>actual</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public Group setActual( boolean theBoolean) {
		myActual = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (GroupKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a specific type of resource the group includes.  E.g. \"cow\", \"syringe\", etc.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (GroupKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a specific type of resource the group includes.  E.g. \"cow\", \"syringe\", etc.
     * </p> 
	 */
	public Group setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
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
     * A label assigned to the group for human identification and communication
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
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public Group setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public Group setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public IntegerDt getQuantityElement() {  
		if (myQuantity == null) {
			myQuantity = new IntegerDt();
		}
		return myQuantity;
	}

	
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public Integer getQuantity() {  
		return getQuantityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public Group setQuantity(IntegerDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public Group setQuantity( int theInteger) {
		myQuantity = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>characteristic</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public java.util.List<Characteristic> getCharacteristic() {  
		if (myCharacteristic == null) {
			myCharacteristic = new java.util.ArrayList<Characteristic>();
		}
		return myCharacteristic;
	}

	/**
	 * Sets the value(s) for <b>characteristic</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public Group setCharacteristic(java.util.List<Characteristic> theValue) {
		myCharacteristic = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>characteristic</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public Characteristic addCharacteristic() {
		Characteristic newType = new Characteristic();
		getCharacteristic().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>characteristic</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public Characteristic getCharacteristicFirstRep() {
		if (getCharacteristic().isEmpty()) {
			return addCharacteristic();
		}
		return getCharacteristic().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>member</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getMember() {  
		if (myMember == null) {
			myMember = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myMember;
	}

	/**
	 * Sets the value(s) for <b>member</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public Group setMember(java.util.List<ResourceReferenceDt> theValue) {
		myMember = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>member</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public ResourceReferenceDt addMember() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getMember().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>Group.characteristic</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	@Block()	
	public static class Characteristic 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="GroupCharacteristicKind",
		formalDefinition="A code that identifies the kind of trait being asserted"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="value", order=1, min=1, max=1, type={
		CodeableConceptDt.class, 		BooleanDt.class, 		QuantityDt.class, 		RangeDt.class	})
	@Description(
		shortDefinition="GroupCharacteristicValue",
		formalDefinition="The value of the trait that holds (or does not hold - see 'exclude') for members of the group"
	)
	private IDatatype myValue;
	
	@Child(name="exclude", type=BooleanDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, indicates the characteristic is one that is NOT held by members of the group"
	)
	private BooleanDt myExclude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myValue,  myExclude);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myValue, myExclude);
	}

	/**
	 * Gets the value(s) for <b>code</b> (GroupCharacteristicKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies the kind of trait being asserted
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (GroupCharacteristicKind)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies the kind of trait being asserted
     * </p> 
	 */
	public Characteristic setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>value[x]</b> (GroupCharacteristicValue).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the trait that holds (or does not hold - see 'exclude') for members of the group
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> (GroupCharacteristicValue)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the trait that holds (or does not hold - see 'exclude') for members of the group
     * </p> 
	 */
	public Characteristic setValue(IDatatype theValue) {
		myValue = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>exclude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public BooleanDt getExcludeElement() {  
		if (myExclude == null) {
			myExclude = new BooleanDt();
		}
		return myExclude;
	}

	
	/**
	 * Gets the value(s) for <b>exclude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public Boolean getExclude() {  
		return getExcludeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>exclude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public Characteristic setExclude(BooleanDt theValue) {
		myExclude = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>exclude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public Characteristic setExclude( boolean theBoolean) {
		myExclude = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "Group";
    }

}
