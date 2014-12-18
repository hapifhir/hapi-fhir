















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
 * HAPI/FHIR <b>Account</b> Resource
 * (Account)
 *
 * <p>
 * <b>Definition:</b>
 * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centres, etc.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Account">http://hl7.org/fhir/profiles/Account</a> 
 * </p>
 *
 */
@ResourceDef(name="Account", profile="http://hl7.org/fhir/profiles/Account", id="account")
public class Account 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Account.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Account.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Account.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Account.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Account.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Account.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Account.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Account.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Account.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Account.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Account.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Account.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>balance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Account.balance</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="balance", path="Account.balance", description="", type="number"  )
	public static final String SP_BALANCE = "balance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>balance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Account.balance</b><br/>
	 * </p>
	 */
	public static final NumberClientParam BALANCE = new NumberClientParam(SP_BALANCE);

	/**
	 * Search parameter constant for <b>coveragePeriod</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Account.coveragePeriod</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="coveragePeriod", path="Account.coveragePeriod", description="", type="date"  )
	public static final String SP_COVERAGEPERIOD = "coveragePeriod";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>coveragePeriod</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Account.coveragePeriod</b><br/>
	 * </p>
	 */
	public static final DateClientParam COVERAGEPERIOD = new DateClientParam(SP_COVERAGEPERIOD);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Account.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Account.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Account.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>owner</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Account.owner</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="owner", path="Account.owner", description="", type="reference"  )
	public static final String SP_OWNER = "owner";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>owner</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Account.owner</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam OWNER = new ReferenceClientParam(SP_OWNER);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.balance</b>".
	 */
	public static final Include INCLUDE_BALANCE = new Include("Account.balance");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.coveragePeriod</b>".
	 */
	public static final Include INCLUDE_COVERAGEPERIOD = new Include("Account.coveragePeriod");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Account.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Account.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.owner</b>".
	 */
	public static final Include INCLUDE_OWNER = new Include("Account.owner");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Account.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Account.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Account.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Account.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Account.identifier",
		formalDefinition="Unique identifier used to reference the account.  May or may not be intended for human use.  (E.g. credit card number)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Account.name",
		formalDefinition="Name used for the account when displaying it to humans in reports, etc."
	)
	private StringDt myName;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Account.type",
		formalDefinition="Categorizes the account for reporting and searching purposes"
	)
	private CodeableConceptDt myType;
	
	@Child(name="status", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Account.status",
		formalDefinition="Indicates whether the account is presently used/useable or not"
	)
	private CodeDt myStatus;
	
	@Child(name="activePeriod", type=PeriodDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Account.activePeriod",
		formalDefinition="Indicates the period of time over which the account is allowed"
	)
	private PeriodDt myActivePeriod;
	
	@Child(name="currency", type=CodeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Account.currency",
		formalDefinition="Identifies the currency to which transactions must be converted when crediting or debiting the account."
	)
	private CodeDt myCurrency;
	
	@Child(name="balance", type=MoneyDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Account.balance",
		formalDefinition="Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative."
	)
	private MoneyDt myBalance;
	
	@Child(name="coveragePeriod", type=PeriodDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Account.coveragePeriod",
		formalDefinition="Identifies the period of time the account applies to.  E.g. accounts created per fiscal year, quarter, etc."
	)
	private PeriodDt myCoveragePeriod;
	
	@Child(name="subject", order=8, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="Account.subject",
		formalDefinition="Identifies the patient, device, practitioner, location or other object the account is associated with"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="owner", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="Account.owner",
		formalDefinition="Indicates the organization, department, etc. with responsibility for the account."
	)
	private ResourceReferenceDt myOwner;
	
	@Child(name="description", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Account.description",
		formalDefinition="Provides additional information about what the account tracks and how it is used"
	)
	private StringDt myDescription;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myType,  myStatus,  myActivePeriod,  myCurrency,  myBalance,  myCoveragePeriod,  mySubject,  myOwner,  myDescription);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myType, myStatus, myActivePeriod, myCurrency, myBalance, myCoveragePeriod, mySubject, myOwner, myDescription);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Account.identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier used to reference the account.  May or may not be intended for human use.  (E.g. credit card number)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Account.identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier used to reference the account.  May or may not be intended for human use.  (E.g. credit card number)
     * </p> 
	 */
	public Account setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> (Account.identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier used to reference the account.  May or may not be intended for human use.  (E.g. credit card number)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Account.identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier used to reference the account.  May or may not be intended for human use.  (E.g. credit card number)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>name</b> (Account.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name used for the account when displaying it to humans in reports, etc.
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (Account.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name used for the account when displaying it to humans in reports, etc.
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (Account.name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name used for the account when displaying it to humans in reports, etc.
     * </p> 
	 */
	public Account setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (Account.name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name used for the account when displaying it to humans in reports, etc.
     * </p> 
	 */
	public Account setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Account.type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Categorizes the account for reporting and searching purposes
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Account.type)
	 *
     * <p>
     * <b>Definition:</b>
     * Categorizes the account for reporting and searching purposes
     * </p> 
	 */
	public Account setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (Account.status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the account is presently used/useable or not
     * </p> 
	 */
	public CodeDt getStatusElement() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (Account.status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the account is presently used/useable or not
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (Account.status)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the account is presently used/useable or not
     * </p> 
	 */
	public Account setStatus(CodeDt theValue) {
		myStatus = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>status</b> (Account.status)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the account is presently used/useable or not
     * </p> 
	 */
	public Account setStatus( String theCode) {
		myStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>activePeriod</b> (Account.activePeriod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time over which the account is allowed
     * </p> 
	 */
	public PeriodDt getActivePeriod() {  
		if (myActivePeriod == null) {
			myActivePeriod = new PeriodDt();
		}
		return myActivePeriod;
	}

	/**
	 * Sets the value(s) for <b>activePeriod</b> (Account.activePeriod)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time over which the account is allowed
     * </p> 
	 */
	public Account setActivePeriod(PeriodDt theValue) {
		myActivePeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>currency</b> (Account.currency).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the currency to which transactions must be converted when crediting or debiting the account.
     * </p> 
	 */
	public CodeDt getCurrencyElement() {  
		if (myCurrency == null) {
			myCurrency = new CodeDt();
		}
		return myCurrency;
	}

	
	/**
	 * Gets the value(s) for <b>currency</b> (Account.currency).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the currency to which transactions must be converted when crediting or debiting the account.
     * </p> 
	 */
	public String getCurrency() {  
		return getCurrencyElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>currency</b> (Account.currency)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the currency to which transactions must be converted when crediting or debiting the account.
     * </p> 
	 */
	public Account setCurrency(CodeDt theValue) {
		myCurrency = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>currency</b> (Account.currency)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the currency to which transactions must be converted when crediting or debiting the account.
     * </p> 
	 */
	public Account setCurrency( String theCode) {
		myCurrency = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>balance</b> (Account.balance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative.
     * </p> 
	 */
	public MoneyDt getBalance() {  
		if (myBalance == null) {
			myBalance = new MoneyDt();
		}
		return myBalance;
	}

	/**
	 * Sets the value(s) for <b>balance</b> (Account.balance)
	 *
     * <p>
     * <b>Definition:</b>
     * Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative.
     * </p> 
	 */
	public Account setBalance(MoneyDt theValue) {
		myBalance = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>coveragePeriod</b> (Account.coveragePeriod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the period of time the account applies to.  E.g. accounts created per fiscal year, quarter, etc.
     * </p> 
	 */
	public PeriodDt getCoveragePeriod() {  
		if (myCoveragePeriod == null) {
			myCoveragePeriod = new PeriodDt();
		}
		return myCoveragePeriod;
	}

	/**
	 * Sets the value(s) for <b>coveragePeriod</b> (Account.coveragePeriod)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the period of time the account applies to.  E.g. accounts created per fiscal year, quarter, etc.
     * </p> 
	 */
	public Account setCoveragePeriod(PeriodDt theValue) {
		myCoveragePeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> (Account.subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient, device, practitioner, location or other object the account is associated with
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Account.subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient, device, practitioner, location or other object the account is associated with
     * </p> 
	 */
	public Account setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>owner</b> (Account.owner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the organization, department, etc. with responsibility for the account.
     * </p> 
	 */
	public ResourceReferenceDt getOwner() {  
		if (myOwner == null) {
			myOwner = new ResourceReferenceDt();
		}
		return myOwner;
	}

	/**
	 * Sets the value(s) for <b>owner</b> (Account.owner)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the organization, department, etc. with responsibility for the account.
     * </p> 
	 */
	public Account setOwner(ResourceReferenceDt theValue) {
		myOwner = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>description</b> (Account.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides additional information about what the account tracks and how it is used
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> (Account.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides additional information about what the account tracks and how it is used
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> (Account.description)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides additional information about what the account tracks and how it is used
     * </p> 
	 */
	public Account setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> (Account.description)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides additional information about what the account tracks and how it is used
     * </p> 
	 */
	public Account setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 


    @Override
    public String getResourceName() {
        return "Account";
    }

}
