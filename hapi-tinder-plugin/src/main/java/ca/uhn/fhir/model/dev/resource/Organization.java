















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
 * HAPI/FHIR <b>Organization</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Organization">http://hl7.org/fhir/profiles/Organization</a> 
 * </p>
 *
 */
@ResourceDef(name="Organization", profile="http://hl7.org/fhir/profiles/Organization", id="organization")
public class Organization 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of the organization's name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Organization.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Organization.name", description="A portion of the organization's name", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of the organization's name</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Organization.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="phonetic", path="", description="A portion of the organization's name using some kind of phonetic matching algorithm", type="string"  )
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final StringClientParam PHONETIC = new StringClientParam(SP_PHONETIC);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of organization</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Organization.type", description="A code for the type of organization", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>A code for the type of organization</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Organization.identifier", description="Any identifier for the organization (not the accreditation issuer's identifier)", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>Search all organizations that are part of the given organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Organization.partOf</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partof", path="Organization.partOf", description="Search all organizations that are part of the given organization", type="reference"  )
	public static final String SP_PARTOF = "partof";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partof</b>
	 * <p>
	 * Description: <b>Search all organizations that are part of the given organization</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Organization.partOf</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PARTOF = new ReferenceClientParam(SP_PARTOF);

	/**
	 * Search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the organization's record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.active</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="active", path="Organization.active", description="Whether the organization's record is active", type="token"  )
	public static final String SP_ACTIVE = "active";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the organization's record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Organization.active</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTIVE = new TokenClientParam(SP_ACTIVE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b></b>".
	 */
	public static final Include INCLUDE_ = new Include("");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Organization.active</b>".
	 */
	public static final Include INCLUDE_ACTIVE = new Include("Organization.active");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Organization.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Organization.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Organization.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Organization.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Organization.partOf</b>".
	 */
	public static final Include INCLUDE_PARTOF = new Include("Organization.partOf");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Organization.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Organization.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier for the organization that is used to identify the organization across multiple disparate systems"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A name associated with the organization"
	)
	private StringDt myName;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="OrganizationType",
		formalDefinition="The kind of organization that this is"
	)
	private BoundCodeableConceptDt<OrganizationTypeEnum> myType;
	
	@Child(name="telecom", type=ContactPointDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A contact detail for the organization"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An address for the organization"
	)
	private java.util.List<AddressDt> myAddress;
	
	@Child(name="partOf", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The organization of which this organization forms a part"
	)
	private ResourceReferenceDt myPartOf;
	
	@Child(name="contact", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<Contact> myContact;
	
	@Child(name="location", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Location(s) the organization uses to provide services"
	)
	private java.util.List<ResourceReferenceDt> myLocation;
	
	@Child(name="active", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether the organization's record is still in active use"
	)
	private BooleanDt myActive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myType,  myTelecom,  myAddress,  myPartOf,  myContact,  myLocation,  myActive);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myType, myTelecom, myAddress, myPartOf, myContact, myLocation, myActive);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
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
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
     * </p> 
	 */
	public Organization setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
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
     * Identifier for the organization that is used to identify the organization across multiple disparate systems
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
     * A name associated with the organization
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
     * A name associated with the organization
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
     * A name associated with the organization
     * </p> 
	 */
	public Organization setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the organization
     * </p> 
	 */
	public Organization setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (OrganizationType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public BoundCodeableConceptDt<OrganizationTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<OrganizationTypeEnum>(OrganizationTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (OrganizationType)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public Organization setType(BoundCodeableConceptDt<OrganizationTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (OrganizationType)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of organization that this is
     * </p> 
	 */
	public Organization setType(OrganizationTypeEnum theValue) {
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
     * A contact detail for the organization
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
     * A contact detail for the organization
     * </p> 
	 */
	public Organization setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the organization
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
     * A contact detail for the organization
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
     * An address for the organization
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
     * An address for the organization
     * </p> 
	 */
	public Organization setAddress(java.util.List<AddressDt> theValue) {
		myAddress = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>address</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An address for the organization
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
     * An address for the organization
     * </p> 
	 */
	public AddressDt getAddressFirstRep() {
		if (getAddress().isEmpty()) {
			return addAddress();
		}
		return getAddress().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>partOf</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The organization of which this organization forms a part
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
     * The organization of which this organization forms a part
     * </p> 
	 */
	public Organization setPartOf(ResourceReferenceDt theValue) {
		myPartOf = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public Organization setContact(java.util.List<Contact> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public Contact getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
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
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public Organization setLocation(java.util.List<ResourceReferenceDt> theValue) {
		myLocation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Location(s) the organization uses to provide services
     * </p> 
	 */
	public ResourceReferenceDt addLocation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getLocation().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>active</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
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
     * Whether the organization's record is still in active use
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
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public Organization setActive(BooleanDt theValue) {
		myActive = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>active</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the organization's record is still in active use
     * </p> 
	 */
	public Organization setActive( boolean theBoolean) {
		myActive = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Organization.contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Contact 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="purpose", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ContactPartyType",
		formalDefinition="Indicates a purpose for which the contact can be reached"
	)
	private CodeableConceptDt myPurpose;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A name associated with the contact"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the party may be contacted."
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Visiting or postal addresses for the contact"
	)
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="AdministrativeGender",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeDt<AdministrativeGenderEnum> myGender;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPurpose,  myName,  myTelecom,  myAddress,  myGender);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPurpose, myName, myTelecom, myAddress, myGender);
	}

	/**
	 * Gets the value(s) for <b>purpose</b> (ContactPartyType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates a purpose for which the contact can be reached
     * </p> 
	 */
	public CodeableConceptDt getPurpose() {  
		if (myPurpose == null) {
			myPurpose = new CodeableConceptDt();
		}
		return myPurpose;
	}

	/**
	 * Sets the value(s) for <b>purpose</b> (ContactPartyType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates a purpose for which the contact can be reached
     * </p> 
	 */
	public Contact setPurpose(CodeableConceptDt theValue) {
		myPurpose = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the contact
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
     * A name associated with the contact
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
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
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
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
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
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
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
     * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
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
     * Visiting or postal addresses for the contact
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
     * Visiting or postal addresses for the contact
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

  

	}




    @Override
    public String getResourceName() {
        return "Organization";
    }

}
