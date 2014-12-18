















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
 * HAPI/FHIR <b>DocumentReference</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A reference to a document
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DocumentReference">http://hl7.org/fhir/profiles/DocumentReference</a> 
 * </p>
 *
 */
@ResourceDef(name="DocumentReference", profile="http://hl7.org/fhir/profiles/DocumentReference", id="documentreference")
public class DocumentReference 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.masterIdentifier | DocumentReference.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DocumentReference.masterIdentifier | DocumentReference.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.masterIdentifier | DocumentReference.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DocumentReference.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="DocumentReference.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.class</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="class", path="DocumentReference.class", description="", type="token"  )
	public static final String SP_CLASS = "class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.class</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CLASS = new TokenClientParam(SP_CLASS);

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="DocumentReference.author", description="", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Search parameter constant for <b>custodian</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.custodian</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="custodian", path="DocumentReference.custodian", description="", type="reference"  )
	public static final String SP_CUSTODIAN = "custodian";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>custodian</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.custodian</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CUSTODIAN = new ReferenceClientParam(SP_CUSTODIAN);

	/**
	 * Search parameter constant for <b>authenticator</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.authenticator</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="authenticator", path="DocumentReference.authenticator", description="", type="reference"  )
	public static final String SP_AUTHENTICATOR = "authenticator";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>authenticator</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.authenticator</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHENTICATOR = new ReferenceClientParam(SP_AUTHENTICATOR);

	/**
	 * Search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.created</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="created", path="DocumentReference.created", description="", type="date"  )
	public static final String SP_CREATED = "created";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.created</b><br/>
	 * </p>
	 */
	public static final DateClientParam CREATED = new DateClientParam(SP_CREATED);

	/**
	 * Search parameter constant for <b>indexed</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.indexed</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="indexed", path="DocumentReference.indexed", description="", type="date"  )
	public static final String SP_INDEXED = "indexed";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>indexed</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.indexed</b><br/>
	 * </p>
	 */
	public static final DateClientParam INDEXED = new DateClientParam(SP_INDEXED);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DocumentReference.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>relatesto</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.relatesTo.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="relatesto", path="DocumentReference.relatesTo.target", description="", type="reference"  )
	public static final String SP_RELATESTO = "relatesto";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>relatesto</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.relatesTo.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RELATESTO = new ReferenceClientParam(SP_RELATESTO);

	/**
	 * Search parameter constant for <b>relation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.relatesTo.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="relation", path="DocumentReference.relatesTo.code", description="", type="token"  )
	public static final String SP_RELATION = "relation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>relation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.relatesTo.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RELATION = new TokenClientParam(SP_RELATION);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="DocumentReference.description", description="", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.confidentiality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="confidentiality", path="DocumentReference.confidentiality", description="", type="token"  )
	public static final String SP_CONFIDENTIALITY = "confidentiality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.confidentiality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONFIDENTIALITY = new TokenClientParam(SP_CONFIDENTIALITY);

	/**
	 * Search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.primaryLanguage</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="language", path="DocumentReference.primaryLanguage", description="", type="token"  )
	public static final String SP_LANGUAGE = "language";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.primaryLanguage</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LANGUAGE = new TokenClientParam(SP_LANGUAGE);

	/**
	 * Search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.format</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="format", path="DocumentReference.format", description="", type="token"  )
	public static final String SP_FORMAT = "format";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.format</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FORMAT = new TokenClientParam(SP_FORMAT);

	/**
	 * Search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>DocumentReference.size</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="size", path="DocumentReference.size", description="", type="number"  )
	public static final String SP_SIZE = "size";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>DocumentReference.size</b><br/>
	 * </p>
	 */
	public static final NumberClientParam SIZE = new NumberClientParam(SP_SIZE);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="DocumentReference.location", description="", type="string"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.location</b><br/>
	 * </p>
	 */
	public static final StringClientParam LOCATION = new StringClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.event</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event", path="DocumentReference.context.event", description="", type="token"  )
	public static final String SP_EVENT = "event";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.event</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVENT = new TokenClientParam(SP_EVENT);

	/**
	 * Search parameter constant for <b>period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.context.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="period", path="DocumentReference.context.period", description="", type="date"  )
	public static final String SP_PERIOD = "period";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.context.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam PERIOD = new DateClientParam(SP_PERIOD);

	/**
	 * Search parameter constant for <b>facility</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.facilityType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="facility", path="DocumentReference.context.facilityType", description="", type="token"  )
	public static final String SP_FACILITY = "facility";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>facility</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.facilityType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FACILITY = new TokenClientParam(SP_FACILITY);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="DocumentReference.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>relatesto-relation</b>
	 * <p>
	 * Description: <b>Combination of relation and relatesTo</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>relatesto & relation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="relatesto-relation", path="relatesto & relation", description="Combination of relation and relatesTo", type="composite"  , compositeOf={  "relatesto",  "relation" }  )
	public static final String SP_RELATESTO_RELATION = "relatesto-relation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>relatesto-relation</b>
	 * <p>
	 * Description: <b>Combination of relation and relatesTo</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>relatesto & relation</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<ReferenceClientParam, TokenClientParam> RELATESTO_RELATION = new CompositeClientParam<ReferenceClientParam, TokenClientParam>(SP_RELATESTO_RELATION);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.authenticator</b>".
	 */
	public static final Include INCLUDE_AUTHENTICATOR = new Include("DocumentReference.authenticator");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("DocumentReference.author");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.class</b>".
	 */
	public static final Include INCLUDE_CLASS = new Include("DocumentReference.class");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.confidentiality</b>".
	 */
	public static final Include INCLUDE_CONFIDENTIALITY = new Include("DocumentReference.confidentiality");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.context.event</b>".
	 */
	public static final Include INCLUDE_CONTEXT_EVENT = new Include("DocumentReference.context.event");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.context.facilityType</b>".
	 */
	public static final Include INCLUDE_CONTEXT_FACILITYTYPE = new Include("DocumentReference.context.facilityType");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.context.period</b>".
	 */
	public static final Include INCLUDE_CONTEXT_PERIOD = new Include("DocumentReference.context.period");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.created</b>".
	 */
	public static final Include INCLUDE_CREATED = new Include("DocumentReference.created");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.custodian</b>".
	 */
	public static final Include INCLUDE_CUSTODIAN = new Include("DocumentReference.custodian");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.description</b>".
	 */
	public static final Include INCLUDE_DESCRIPTION = new Include("DocumentReference.description");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.format</b>".
	 */
	public static final Include INCLUDE_FORMAT = new Include("DocumentReference.format");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("DocumentReference.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.indexed</b>".
	 */
	public static final Include INCLUDE_INDEXED = new Include("DocumentReference.indexed");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("DocumentReference.location");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.masterIdentifier</b>".
	 */
	public static final Include INCLUDE_MASTERIDENTIFIER = new Include("DocumentReference.masterIdentifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.primaryLanguage</b>".
	 */
	public static final Include INCLUDE_PRIMARYLANGUAGE = new Include("DocumentReference.primaryLanguage");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.relatesTo.code</b>".
	 */
	public static final Include INCLUDE_RELATESTO_CODE = new Include("DocumentReference.relatesTo.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.relatesTo.target</b>".
	 */
	public static final Include INCLUDE_RELATESTO_TARGET = new Include("DocumentReference.relatesTo.target");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.size</b>".
	 */
	public static final Include INCLUDE_SIZE = new Include("DocumentReference.size");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("DocumentReference.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DocumentReference.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("DocumentReference.type");


	@Child(name="masterIdentifier", type=IdentifierDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document"
	)
	private IdentifierDt myMasterIdentifier;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="DocumentType",
		formalDefinition="Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)"
	)
	private CodeableConceptDt myType;
	
	@Child(name="class", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="DocumentClass",
		formalDefinition="A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type"
	)
	private CodeableConceptDt myClassElement;
	
	@Child(name="author", order=5, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies who is responsible for adding the information to the document"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="custodian", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the document"
	)
	private ResourceReferenceDt myCustodian;
	
	@Child(name="policyManager", type=UriDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A reference to a domain or server that manages policies under which the document is accessed and/or made available"
	)
	private UriDt myPolicyManager;
	
	@Child(name="authenticator", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Which person or organization authenticates that this document is valid"
	)
	private ResourceReferenceDt myAuthenticator;
	
	@Child(name="created", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When the document was created"
	)
	private DateTimeDt myCreated;
	
	@Child(name="indexed", type=InstantDt.class, order=10, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When the document reference was created"
	)
	private InstantDt myIndexed;
	
	@Child(name="status", type=CodeDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="DocumentReferenceStatus",
		formalDefinition="The status of this document reference"
	)
	private BoundCodeDt<DocumentReferenceStatusEnum> myStatus;
	
	@Child(name="docStatus", type=CodeableConceptDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="ReferredDocumentStatus",
		formalDefinition="The status of the underlying document"
	)
	private CodeableConceptDt myDocStatus;
	
	@Child(name="relatesTo", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Relationships that this document has with other document references that already exist"
	)
	private java.util.List<RelatesTo> myRelatesTo;
	
	@Child(name="description", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\""
	)
	private StringDt myDescription;
	
	@Child(name="confidentiality", type=CodeableConceptDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="DocumentConfidentiality",
		formalDefinition="A code specifying the level of confidentiality of the XDS Document"
	)
	private java.util.List<CodeableConceptDt> myConfidentiality;
	
	@Child(name="primaryLanguage", type=CodeDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="Language",
		formalDefinition="The primary language in which the source document is written"
	)
	private CodeDt myPrimaryLanguage;
	
	@Child(name="mimeType", type=CodeDt.class, order=17, min=1, max=1)	
	@Description(
		shortDefinition="MimeType",
		formalDefinition="The mime type of the source document"
	)
	private CodeDt myMimeType;
	
	@Child(name="format", type=UriDt.class, order=18, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType"
	)
	private java.util.List<UriDt> myFormat;
	
	@Child(name="size", type=IntegerDt.class, order=19, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The size of the source document this reference refers to in bytes"
	)
	private IntegerDt mySize;
	
	@Child(name="hash", type=Base64BinaryDt.class, order=20, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A hash of the source document to ensure that changes have not occurred"
	)
	private Base64BinaryDt myHash;
	
	@Child(name="location", type=UriDt.class, order=21, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A url at which the document can be accessed"
	)
	private UriDt myLocation;
	
	@Child(name="service", order=22, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of a service call that can be used to retrieve the document"
	)
	private Service myService;
	
	@Child(name="context", order=23, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The clinical context in which the document was prepared"
	)
	private Context myContext;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMasterIdentifier,  myIdentifier,  mySubject,  myType,  myClassElement,  myAuthor,  myCustodian,  myPolicyManager,  myAuthenticator,  myCreated,  myIndexed,  myStatus,  myDocStatus,  myRelatesTo,  myDescription,  myConfidentiality,  myPrimaryLanguage,  myMimeType,  myFormat,  mySize,  myHash,  myLocation,  myService,  myContext);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMasterIdentifier, myIdentifier, mySubject, myType, myClassElement, myAuthor, myCustodian, myPolicyManager, myAuthenticator, myCreated, myIndexed, myStatus, myDocStatus, myRelatesTo, myDescription, myConfidentiality, myPrimaryLanguage, myMimeType, myFormat, mySize, myHash, myLocation, myService, myContext);
	}

	/**
	 * Gets the value(s) for <b>masterIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document
     * </p> 
	 */
	public IdentifierDt getMasterIdentifier() {  
		if (myMasterIdentifier == null) {
			myMasterIdentifier = new IdentifierDt();
		}
		return myMasterIdentifier;
	}

	/**
	 * Sets the value(s) for <b>masterIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document
     * </p> 
	 */
	public DocumentReference setMasterIdentifier(IdentifierDt theValue) {
		myMasterIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
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
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public DocumentReference setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
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
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)
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
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)
     * </p> 
	 */
	public DocumentReference setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> (DocumentType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (DocumentType)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)
     * </p> 
	 */
	public DocumentReference setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>class</b> (DocumentClass).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type
     * </p> 
	 */
	public CodeableConceptDt getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeableConceptDt();
		}
		return myClassElement;
	}

	/**
	 * Sets the value(s) for <b>class</b> (DocumentClass)
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type
     * </p> 
	 */
	public DocumentReference setClassElement(CodeableConceptDt theValue) {
		myClassElement = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public DocumentReference setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>custodian</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document
     * </p> 
	 */
	public ResourceReferenceDt getCustodian() {  
		if (myCustodian == null) {
			myCustodian = new ResourceReferenceDt();
		}
		return myCustodian;
	}

	/**
	 * Sets the value(s) for <b>custodian</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document
     * </p> 
	 */
	public DocumentReference setCustodian(ResourceReferenceDt theValue) {
		myCustodian = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>policyManager</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public UriDt getPolicyManagerElement() {  
		if (myPolicyManager == null) {
			myPolicyManager = new UriDt();
		}
		return myPolicyManager;
	}

	
	/**
	 * Gets the value(s) for <b>policyManager</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public URI getPolicyManager() {  
		return getPolicyManagerElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>policyManager</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public DocumentReference setPolicyManager(UriDt theValue) {
		myPolicyManager = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>policyManager</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public DocumentReference setPolicyManager( String theUri) {
		myPolicyManager = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>authenticator</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Which person or organization authenticates that this document is valid
     * </p> 
	 */
	public ResourceReferenceDt getAuthenticator() {  
		if (myAuthenticator == null) {
			myAuthenticator = new ResourceReferenceDt();
		}
		return myAuthenticator;
	}

	/**
	 * Sets the value(s) for <b>authenticator</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Which person or organization authenticates that this document is valid
     * </p> 
	 */
	public DocumentReference setAuthenticator(ResourceReferenceDt theValue) {
		myAuthenticator = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>created</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DateTimeDt getCreatedElement() {  
		if (myCreated == null) {
			myCreated = new DateTimeDt();
		}
		return myCreated;
	}

	
	/**
	 * Gets the value(s) for <b>created</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public Date getCreated() {  
		return getCreatedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>created</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DocumentReference setCreated(DateTimeDt theValue) {
		myCreated = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>created</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DocumentReference setCreated( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myCreated = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>created</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DocumentReference setCreatedWithSecondsPrecision( Date theDate) {
		myCreated = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>indexed</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public InstantDt getIndexedElement() {  
		if (myIndexed == null) {
			myIndexed = new InstantDt();
		}
		return myIndexed;
	}

	
	/**
	 * Gets the value(s) for <b>indexed</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public Date getIndexed() {  
		return getIndexedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>indexed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public DocumentReference setIndexed(InstantDt theValue) {
		myIndexed = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>indexed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public DocumentReference setIndexedWithMillisPrecision( Date theDate) {
		myIndexed = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>indexed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public DocumentReference setIndexed( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIndexed = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (DocumentReferenceStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public BoundCodeDt<DocumentReferenceStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DocumentReferenceStatusEnum>(DocumentReferenceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (DocumentReferenceStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (DocumentReferenceStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public DocumentReference setStatus(BoundCodeDt<DocumentReferenceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (DocumentReferenceStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public DocumentReference setStatus(DocumentReferenceStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>docStatus</b> (ReferredDocumentStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the underlying document
     * </p> 
	 */
	public CodeableConceptDt getDocStatus() {  
		if (myDocStatus == null) {
			myDocStatus = new CodeableConceptDt();
		}
		return myDocStatus;
	}

	/**
	 * Sets the value(s) for <b>docStatus</b> (ReferredDocumentStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the underlying document
     * </p> 
	 */
	public DocumentReference setDocStatus(CodeableConceptDt theValue) {
		myDocStatus = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>relatesTo</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public java.util.List<RelatesTo> getRelatesTo() {  
		if (myRelatesTo == null) {
			myRelatesTo = new java.util.ArrayList<RelatesTo>();
		}
		return myRelatesTo;
	}

	/**
	 * Sets the value(s) for <b>relatesTo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public DocumentReference setRelatesTo(java.util.List<RelatesTo> theValue) {
		myRelatesTo = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>relatesTo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public RelatesTo addRelatesTo() {
		RelatesTo newType = new RelatesTo();
		getRelatesTo().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relatesTo</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public RelatesTo getRelatesToFirstRep() {
		if (getRelatesTo().isEmpty()) {
			return addRelatesTo();
		}
		return getRelatesTo().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
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
     * Human-readable description of the source document. This is sometimes known as the \"title\"
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
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentReference setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentReference setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>confidentiality</b> (DocumentConfidentiality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myConfidentiality;
	}

	/**
	 * Sets the value(s) for <b>confidentiality</b> (DocumentConfidentiality)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public DocumentReference setConfidentiality(java.util.List<CodeableConceptDt> theValue) {
		myConfidentiality = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>confidentiality</b> (DocumentConfidentiality)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public CodeableConceptDt addConfidentiality() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getConfidentiality().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>confidentiality</b> (DocumentConfidentiality),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public CodeableConceptDt getConfidentialityFirstRep() {
		if (getConfidentiality().isEmpty()) {
			return addConfidentiality();
		}
		return getConfidentiality().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>primaryLanguage</b> (Language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public CodeDt getPrimaryLanguageElement() {  
		if (myPrimaryLanguage == null) {
			myPrimaryLanguage = new CodeDt();
		}
		return myPrimaryLanguage;
	}

	
	/**
	 * Gets the value(s) for <b>primaryLanguage</b> (Language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public String getPrimaryLanguage() {  
		return getPrimaryLanguageElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>primaryLanguage</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public DocumentReference setPrimaryLanguage(CodeDt theValue) {
		myPrimaryLanguage = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>primaryLanguage</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public DocumentReference setPrimaryLanguage( String theCode) {
		myPrimaryLanguage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mimeType</b> (MimeType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public CodeDt getMimeTypeElement() {  
		if (myMimeType == null) {
			myMimeType = new CodeDt();
		}
		return myMimeType;
	}

	
	/**
	 * Gets the value(s) for <b>mimeType</b> (MimeType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public String getMimeType() {  
		return getMimeTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mimeType</b> (MimeType)
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public DocumentReference setMimeType(CodeDt theValue) {
		myMimeType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>mimeType</b> (MimeType)
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public DocumentReference setMimeType( String theCode) {
		myMimeType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>format</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public java.util.List<UriDt> getFormat() {  
		if (myFormat == null) {
			myFormat = new java.util.ArrayList<UriDt>();
		}
		return myFormat;
	}

	/**
	 * Sets the value(s) for <b>format</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public DocumentReference setFormat(java.util.List<UriDt> theValue) {
		myFormat = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>format</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public UriDt addFormat() {
		UriDt newType = new UriDt();
		getFormat().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>format</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public UriDt getFormatFirstRep() {
		if (getFormat().isEmpty()) {
			return addFormat();
		}
		return getFormat().get(0); 
	}
 	/**
	 * Adds a new value for <b>format</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentReference addFormat( String theUri) {
		if (myFormat == null) {
			myFormat = new java.util.ArrayList<UriDt>();
		}
		myFormat.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>size</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public IntegerDt getSizeElement() {  
		if (mySize == null) {
			mySize = new IntegerDt();
		}
		return mySize;
	}

	
	/**
	 * Gets the value(s) for <b>size</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public Integer getSize() {  
		return getSizeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>size</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public DocumentReference setSize(IntegerDt theValue) {
		mySize = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>size</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public DocumentReference setSize( int theInteger) {
		mySize = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>hash</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public Base64BinaryDt getHashElement() {  
		if (myHash == null) {
			myHash = new Base64BinaryDt();
		}
		return myHash;
	}

	
	/**
	 * Gets the value(s) for <b>hash</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public byte[] getHash() {  
		return getHashElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>hash</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public DocumentReference setHash(Base64BinaryDt theValue) {
		myHash = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>hash</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public DocumentReference setHash( byte[] theBytes) {
		myHash = new Base64BinaryDt(theBytes); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public UriDt getLocationElement() {  
		if (myLocation == null) {
			myLocation = new UriDt();
		}
		return myLocation;
	}

	
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public URI getLocation() {  
		return getLocationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public DocumentReference setLocation(UriDt theValue) {
		myLocation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public DocumentReference setLocation( String theUri) {
		myLocation = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of a service call that can be used to retrieve the document
     * </p> 
	 */
	public Service getService() {  
		if (myService == null) {
			myService = new Service();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of a service call that can be used to retrieve the document
     * </p> 
	 */
	public DocumentReference setService(Service theValue) {
		myService = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>context</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical context in which the document was prepared
     * </p> 
	 */
	public Context getContext() {  
		if (myContext == null) {
			myContext = new Context();
		}
		return myContext;
	}

	/**
	 * Sets the value(s) for <b>context</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical context in which the document was prepared
     * </p> 
	 */
	public DocumentReference setContext(Context theValue) {
		myContext = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>DocumentReference.relatesTo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	@Block()	
	public static class RelatesTo 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="DocumentRelationshipType",
		formalDefinition="The type of relationship that this document has with anther document"
	)
	private BoundCodeDt<DocumentRelationshipTypeEnum> myCode;
	
	@Child(name="target", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.DocumentReference.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The target document of this relationship"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myTarget);
	}

	/**
	 * Gets the value(s) for <b>code</b> (DocumentRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public BoundCodeDt<DocumentRelationshipTypeEnum> getCodeElement() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<DocumentRelationshipTypeEnum>(DocumentRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (DocumentRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (DocumentRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public RelatesTo setCode(BoundCodeDt<DocumentRelationshipTypeEnum> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>code</b> (DocumentRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public RelatesTo setCode(DocumentRelationshipTypeEnum theValue) {
		getCodeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The target document of this relationship
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
     * The target document of this relationship
     * </p> 
	 */
	public RelatesTo setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>DocumentReference.service</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of a service call that can be used to retrieve the document
     * </p> 
	 */
	@Block()	
	public static class Service 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="DocumentServiceType",
		formalDefinition="The type of the service that can be used to access the documents"
	)
	private CodeableConceptDt myType;
	
	@Child(name="address", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Where the service end-point is located"
	)
	private StringDt myAddress;
	
	@Child(name="parameter", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A list of named parameters that is used in the service call"
	)
	private java.util.List<ServiceParameter> myParameter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myAddress,  myParameter);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myAddress, myParameter);
	}

	/**
	 * Gets the value(s) for <b>type</b> (DocumentServiceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the service that can be used to access the documents
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (DocumentServiceType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the service that can be used to access the documents
     * </p> 
	 */
	public Service setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>address</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public StringDt getAddressElement() {  
		if (myAddress == null) {
			myAddress = new StringDt();
		}
		return myAddress;
	}

	
	/**
	 * Gets the value(s) for <b>address</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public String getAddress() {  
		return getAddressElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public Service setAddress(StringDt theValue) {
		myAddress = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public Service setAddress( String theString) {
		myAddress = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public java.util.List<ServiceParameter> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<ServiceParameter>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public Service setParameter(java.util.List<ServiceParameter> theValue) {
		myParameter = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>parameter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public ServiceParameter addParameter() {
		ServiceParameter newType = new ServiceParameter();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public ServiceParameter getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>DocumentReference.service.parameter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	@Block()	
	public static class ServiceParameter 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of a parameter"
	)
	private StringDt myName;
	
	@Child(name="value", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The value of the named parameter"
	)
	private StringDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myValue);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a parameter
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
     * The name of a parameter
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
     * The name of a parameter
     * </p> 
	 */
	public ServiceParameter setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a parameter
     * </p> 
	 */
	public ServiceParameter setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the named parameter
     * </p> 
	 */
	public StringDt getValueElement() {  
		if (myValue == null) {
			myValue = new StringDt();
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
     * The value of the named parameter
     * </p> 
	 */
	public String getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the named parameter
     * </p> 
	 */
	public ServiceParameter setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the named parameter
     * </p> 
	 */
	public ServiceParameter setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>DocumentReference.context</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical context in which the document was prepared
     * </p> 
	 */
	@Block()	
	public static class Context 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="event", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="DocumentEventType",
		formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act"
	)
	private java.util.List<CodeableConceptDt> myEvent;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The time period over which the service that is described by the document was provided"
	)
	private PeriodDt myPeriod;
	
	@Child(name="facilityType", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="DocumentFacilityType",
		formalDefinition="The kind of facility where the patient was seen"
	)
	private CodeableConceptDt myFacilityType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEvent,  myPeriod,  myFacilityType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myEvent, myPeriod, myFacilityType);
	}

	/**
	 * Gets the value(s) for <b>event</b> (DocumentEventType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (DocumentEventType)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public Context setEvent(java.util.List<CodeableConceptDt> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> (DocumentEventType)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public CodeableConceptDt addEvent() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (DocumentEventType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public CodeableConceptDt getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time period over which the service that is described by the document was provided
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
     * The time period over which the service that is described by the document was provided
     * </p> 
	 */
	public Context setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>facilityType</b> (DocumentFacilityType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of facility where the patient was seen
     * </p> 
	 */
	public CodeableConceptDt getFacilityType() {  
		if (myFacilityType == null) {
			myFacilityType = new CodeableConceptDt();
		}
		return myFacilityType;
	}

	/**
	 * Sets the value(s) for <b>facilityType</b> (DocumentFacilityType)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of facility where the patient was seen
     * </p> 
	 */
	public Context setFacilityType(CodeableConceptDt theValue) {
		myFacilityType = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "DocumentReference";
    }

}
