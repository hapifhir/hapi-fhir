















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
 * HAPI/FHIR <b>SecurityPrincipal</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A human or a software device that uses a software application/service
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SecurityPrincipal">http://hl7.org/fhir/profiles/SecurityPrincipal</a> 
 * </p>
 *
 */
@ResourceDef(name="SecurityPrincipal", profile="http://hl7.org/fhir/profiles/SecurityPrincipal", id="securityprincipal")
public class SecurityPrincipal 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="SecurityPrincipal.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityPrincipal.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="SecurityPrincipal.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityPrincipal.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>ContactPoint</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.contact</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="ContactPoint", path="SecurityPrincipal.contact", description="", type="token"  )
	public static final String SP_CONTACTPOINT = "ContactPoint";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>ContactPoint</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.contact</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTACTPOINT = new TokenClientParam(SP_CONTACTPOINT);

	/**
	 * Search parameter constant for <b>login</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.login</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="login", path="SecurityPrincipal.login", description="", type="token"  )
	public static final String SP_LOGIN = "login";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>login</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.login</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LOGIN = new TokenClientParam(SP_LOGIN);

	/**
	 * Search parameter constant for <b>DomainResource</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityPrincipal.resource</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="DomainResource", path="SecurityPrincipal.resource", description="", type="reference"  )
	public static final String SP_DOMAINRESOURCE = "DomainResource";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>DomainResource</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityPrincipal.resource</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DOMAINRESOURCE = new ReferenceClientParam(SP_DOMAINRESOURCE);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.claim.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="SecurityPrincipal.claim.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.claim.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>claim</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.claim.value</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="claim", path="SecurityPrincipal.claim.value", description="", type="token"  )
	public static final String SP_CLAIM = "claim";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>claim</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityPrincipal.claim.value</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CLAIM = new TokenClientParam(SP_CLAIM);

	/**
	 * Search parameter constant for <b>group</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityPrincipal.group</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="group", path="SecurityPrincipal.group", description="", type="reference"  )
	public static final String SP_GROUP = "group";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>group</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityPrincipal.group</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam GROUP = new ReferenceClientParam(SP_GROUP);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.claim.type</b>".
	 */
	public static final Include INCLUDE_CLAIM_TYPE = new Include("SecurityPrincipal.claim.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.claim.value</b>".
	 */
	public static final Include INCLUDE_CLAIM_VALUE = new Include("SecurityPrincipal.claim.value");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.contact</b>".
	 */
	public static final Include INCLUDE_CONTACT = new Include("SecurityPrincipal.contact");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.group</b>".
	 */
	public static final Include INCLUDE_GROUP = new Include("SecurityPrincipal.group");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("SecurityPrincipal.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.login</b>".
	 */
	public static final Include INCLUDE_LOGIN = new Include("SecurityPrincipal.login");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("SecurityPrincipal.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityPrincipal.resource</b>".
	 */
	public static final Include INCLUDE_RESOURCE = new Include("SecurityPrincipal.resource");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifiers that identify this user on external systems"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Human usable name for this principal"
	)
	private java.util.List<HumanNameDt> myName;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional textual information to futher characterise/identify this user for human use"
	)
	private StringDt myDescription;
	
	@Child(name="photo", type=AttachmentDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A Picture or Photo of the principal, if relevant"
	)
	private AttachmentDt myPhoto;
	
	@Child(name="contact", type=ContactPointDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contact detais by which the human may be contacted"
	)
	private java.util.List<ContactPointDt> myContact;
	
	@Child(name="login", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A login, if the user logs in directly"
	)
	private StringDt myLogin;
	
	@Child(name="passwordHash", type=Base64BinaryDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Password hash, if the user logs in directly"
	)
	private Base64BinaryDt myPasswordHash;
	
	@Child(name="ipMask", type=StringDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="One or more IP v4 or v6 masks. The user is allowed to use the system if they meet any of the masks"
	)
	private java.util.List<StringDt> myIpMask;
	
	@Child(name="sessionLength", type=IntegerDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Time in minutes until user must re-authenticate"
	)
	private IntegerDt mySessionLength;
	
	@Child(name="resource", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.RelatedPerson.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Group.class	})
	@Description(
		shortDefinition="",
		formalDefinition="One of more resources that relate to the same user.  E.g. if the user is a patient, associates the user login record with the patient record. Typically,"
	)
	private java.util.List<ResourceReferenceDt> myResource;
	
	@Child(name="claim", order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Claims (e.g. rights/roles) associated this principal"
	)
	private java.util.List<Claim> myClaim;
	
	@Child(name="group", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.SecurityGroup.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Groups this user gets claims from"
	)
	private java.util.List<ResourceReferenceDt> myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myDescription,  myPhoto,  myContact,  myLogin,  myPasswordHash,  myIpMask,  mySessionLength,  myResource,  myClaim,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myDescription, myPhoto, myContact, myLogin, myPasswordHash, myIpMask, mySessionLength, myResource, myClaim, myGroup);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers that identify this user on external systems
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
     * Identifiers that identify this user on external systems
     * </p> 
	 */
	public SecurityPrincipal setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers that identify this user on external systems
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
     * Identifiers that identify this user on external systems
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
     * Human usable name for this principal
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
     * Human usable name for this principal
     * </p> 
	 */
	public SecurityPrincipal setName(java.util.List<HumanNameDt> theValue) {
		myName = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human usable name for this principal
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
     * Human usable name for this principal
     * </p> 
	 */
	public HumanNameDt getNameFirstRep() {
		if (getName().isEmpty()) {
			return addName();
		}
		return getName().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional textual information to futher characterise/identify this user for human use
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
     * Additional textual information to futher characterise/identify this user for human use
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
     * Additional textual information to futher characterise/identify this user for human use
     * </p> 
	 */
	public SecurityPrincipal setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional textual information to futher characterise/identify this user for human use
     * </p> 
	 */
	public SecurityPrincipal setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>photo</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A Picture or Photo of the principal, if relevant
     * </p> 
	 */
	public AttachmentDt getPhoto() {  
		if (myPhoto == null) {
			myPhoto = new AttachmentDt();
		}
		return myPhoto;
	}

	/**
	 * Sets the value(s) for <b>photo</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A Picture or Photo of the principal, if relevant
     * </p> 
	 */
	public SecurityPrincipal setPhoto(AttachmentDt theValue) {
		myPhoto = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact detais by which the human may be contacted
     * </p> 
	 */
	public java.util.List<ContactPointDt> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactPointDt>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact detais by which the human may be contacted
     * </p> 
	 */
	public SecurityPrincipal setContact(java.util.List<ContactPointDt> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact detais by which the human may be contacted
     * </p> 
	 */
	public ContactPointDt addContact() {
		ContactPointDt newType = new ContactPointDt();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact detais by which the human may be contacted
     * </p> 
	 */
	public ContactPointDt getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>login</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A login, if the user logs in directly
     * </p> 
	 */
	public StringDt getLoginElement() {  
		if (myLogin == null) {
			myLogin = new StringDt();
		}
		return myLogin;
	}

	
	/**
	 * Gets the value(s) for <b>login</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A login, if the user logs in directly
     * </p> 
	 */
	public String getLogin() {  
		return getLoginElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>login</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A login, if the user logs in directly
     * </p> 
	 */
	public SecurityPrincipal setLogin(StringDt theValue) {
		myLogin = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>login</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A login, if the user logs in directly
     * </p> 
	 */
	public SecurityPrincipal setLogin( String theString) {
		myLogin = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>passwordHash</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Password hash, if the user logs in directly
     * </p> 
	 */
	public Base64BinaryDt getPasswordHashElement() {  
		if (myPasswordHash == null) {
			myPasswordHash = new Base64BinaryDt();
		}
		return myPasswordHash;
	}

	
	/**
	 * Gets the value(s) for <b>passwordHash</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Password hash, if the user logs in directly
     * </p> 
	 */
	public byte[] getPasswordHash() {  
		return getPasswordHashElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>passwordHash</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Password hash, if the user logs in directly
     * </p> 
	 */
	public SecurityPrincipal setPasswordHash(Base64BinaryDt theValue) {
		myPasswordHash = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>passwordHash</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Password hash, if the user logs in directly
     * </p> 
	 */
	public SecurityPrincipal setPasswordHash( byte[] theBytes) {
		myPasswordHash = new Base64BinaryDt(theBytes); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>ipMask</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more IP v4 or v6 masks. The user is allowed to use the system if they meet any of the masks
     * </p> 
	 */
	public java.util.List<StringDt> getIpMask() {  
		if (myIpMask == null) {
			myIpMask = new java.util.ArrayList<StringDt>();
		}
		return myIpMask;
	}

	/**
	 * Sets the value(s) for <b>ipMask</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more IP v4 or v6 masks. The user is allowed to use the system if they meet any of the masks
     * </p> 
	 */
	public SecurityPrincipal setIpMask(java.util.List<StringDt> theValue) {
		myIpMask = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>ipMask</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more IP v4 or v6 masks. The user is allowed to use the system if they meet any of the masks
     * </p> 
	 */
	public StringDt addIpMask() {
		StringDt newType = new StringDt();
		getIpMask().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>ipMask</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more IP v4 or v6 masks. The user is allowed to use the system if they meet any of the masks
     * </p> 
	 */
	public StringDt getIpMaskFirstRep() {
		if (getIpMask().isEmpty()) {
			return addIpMask();
		}
		return getIpMask().get(0); 
	}
 	/**
	 * Adds a new value for <b>ipMask</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One or more IP v4 or v6 masks. The user is allowed to use the system if they meet any of the masks
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public SecurityPrincipal addIpMask( String theString) {
		if (myIpMask == null) {
			myIpMask = new java.util.ArrayList<StringDt>();
		}
		myIpMask.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sessionLength</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time in minutes until user must re-authenticate
     * </p> 
	 */
	public IntegerDt getSessionLengthElement() {  
		if (mySessionLength == null) {
			mySessionLength = new IntegerDt();
		}
		return mySessionLength;
	}

	
	/**
	 * Gets the value(s) for <b>sessionLength</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time in minutes until user must re-authenticate
     * </p> 
	 */
	public Integer getSessionLength() {  
		return getSessionLengthElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>sessionLength</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Time in minutes until user must re-authenticate
     * </p> 
	 */
	public SecurityPrincipal setSessionLength(IntegerDt theValue) {
		mySessionLength = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>sessionLength</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Time in minutes until user must re-authenticate
     * </p> 
	 */
	public SecurityPrincipal setSessionLength( int theInteger) {
		mySessionLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>resource</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One of more resources that relate to the same user.  E.g. if the user is a patient, associates the user login record with the patient record. Typically,
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getResource() {  
		if (myResource == null) {
			myResource = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myResource;
	}

	/**
	 * Sets the value(s) for <b>resource</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One of more resources that relate to the same user.  E.g. if the user is a patient, associates the user login record with the patient record. Typically,
     * </p> 
	 */
	public SecurityPrincipal setResource(java.util.List<ResourceReferenceDt> theValue) {
		myResource = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>resource</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * One of more resources that relate to the same user.  E.g. if the user is a patient, associates the user login record with the patient record. Typically,
     * </p> 
	 */
	public ResourceReferenceDt addResource() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getResource().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>claim</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Claims (e.g. rights/roles) associated this principal
     * </p> 
	 */
	public java.util.List<Claim> getClaim() {  
		if (myClaim == null) {
			myClaim = new java.util.ArrayList<Claim>();
		}
		return myClaim;
	}

	/**
	 * Sets the value(s) for <b>claim</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Claims (e.g. rights/roles) associated this principal
     * </p> 
	 */
	public SecurityPrincipal setClaim(java.util.List<Claim> theValue) {
		myClaim = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>claim</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Claims (e.g. rights/roles) associated this principal
     * </p> 
	 */
	public Claim addClaim() {
		Claim newType = new Claim();
		getClaim().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>claim</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Claims (e.g. rights/roles) associated this principal
     * </p> 
	 */
	public Claim getClaimFirstRep() {
		if (getClaim().isEmpty()) {
			return addClaim();
		}
		return getClaim().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>group</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Groups this user gets claims from
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getGroup() {  
		if (myGroup == null) {
			myGroup = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Groups this user gets claims from
     * </p> 
	 */
	public SecurityPrincipal setGroup(java.util.List<ResourceReferenceDt> theValue) {
		myGroup = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>group</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Groups this user gets claims from
     * </p> 
	 */
	public ResourceReferenceDt addGroup() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getGroup().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>SecurityPrincipal.claim</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Claims (e.g. rights/roles) associated this principal
     * </p> 
	 */
	@Block()	
	public static class Claim 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Logical or literal reference to type of claim. See \"Security Claim Types\" for further information"
	)
	private UriDt myType;
	
	@Child(name="value", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Claim value. This is usually a URI, but depends on the type"
	)
	private StringDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myValue);
	}

	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical or literal reference to type of claim. See \"Security Claim Types\" for further information
     * </p> 
	 */
	public UriDt getTypeElement() {  
		if (myType == null) {
			myType = new UriDt();
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical or literal reference to type of claim. See \"Security Claim Types\" for further information
     * </p> 
	 */
	public URI getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Logical or literal reference to type of claim. See \"Security Claim Types\" for further information
     * </p> 
	 */
	public Claim setType(UriDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Logical or literal reference to type of claim. See \"Security Claim Types\" for further information
     * </p> 
	 */
	public Claim setType( String theUri) {
		myType = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Claim value. This is usually a URI, but depends on the type
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
     * Claim value. This is usually a URI, but depends on the type
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
     * Claim value. This is usually a URI, but depends on the type
     * </p> 
	 */
	public Claim setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Claim value. This is usually a URI, but depends on the type
     * </p> 
	 */
	public Claim setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "SecurityPrincipal";
    }

}
