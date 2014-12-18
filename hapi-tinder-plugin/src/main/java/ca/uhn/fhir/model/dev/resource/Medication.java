















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
 * HAPI/FHIR <b>Medication</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Medication">http://hl7.org/fhir/profiles/Medication</a> 
 * </p>
 *
 */
@ResourceDef(name="Medication", profile="http://hl7.org/fhir/profiles/Medication", id="medication")
public class Medication 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Medication.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Medication.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Medication.manufacturer", description="", type="reference"  )
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MANUFACTURER = new ReferenceClientParam(SP_MANUFACTURER);

	/**
	 * Search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="form", path="Medication.product.form", description="", type="token"  )
	public static final String SP_FORM = "form";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FORM = new TokenClientParam(SP_FORM);

	/**
	 * Search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="ingredient", path="Medication.product.ingredient.item", description="", type="reference"  )
	public static final String SP_INGREDIENT = "ingredient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam INGREDIENT = new ReferenceClientParam(SP_INGREDIENT);

	/**
	 * Search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="container", path="Medication.package.container", description="", type="token"  )
	public static final String SP_CONTAINER = "container";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTAINER = new TokenClientParam(SP_CONTAINER);

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="content", path="Medication.package.content.item", description="", type="reference"  )
	public static final String SP_CONTENT = "content";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONTENT = new ReferenceClientParam(SP_CONTENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("Medication.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.manufacturer</b>".
	 */
	public static final Include INCLUDE_MANUFACTURER = new Include("Medication.manufacturer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Medication.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.package.container</b>".
	 */
	public static final Include INCLUDE_PACKAGE_CONTAINER = new Include("Medication.package.container");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.package.content.item</b>".
	 */
	public static final Include INCLUDE_PACKAGE_CONTENT_ITEM = new Include("Medication.package.content.item");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.product.form</b>".
	 */
	public static final Include INCLUDE_PRODUCT_FORM = new Include("Medication.product.form");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.product.ingredient.item</b>".
	 */
	public static final Include INCLUDE_PRODUCT_INGREDIENT_ITEM = new Include("Medication.product.ingredient.item");


	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code"
	)
	private StringDt myName;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="MedicationFormalRepresentation",
		formalDefinition="A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="isBrand", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)"
	)
	private BooleanDt myIsBrand;
	
	@Child(name="manufacturer", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Describes the details of the manufacturer"
	)
	private ResourceReferenceDt myManufacturer;
	
	@Child(name="kind", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="MedicationKind",
		formalDefinition="Medications are either a single administrable product or a package that contains one or more products."
	)
	private BoundCodeDt<MedicationKindEnum> myKind;
	
	@Child(name="product", order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Information that only applies to products (not packages)"
	)
	private Product myProduct;
	
	@Child(name="package", order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Information that only applies to packages (not products)"
	)
	private Package myPackage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myCode,  myIsBrand,  myManufacturer,  myKind,  myProduct,  myPackage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myCode, myIsBrand, myManufacturer, myKind, myProduct, myPackage);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
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
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
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
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public Medication setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public Medication setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (MedicationFormalRepresentation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (MedicationFormalRepresentation)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes
     * </p> 
	 */
	public Medication setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>isBrand</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public BooleanDt getIsBrandElement() {  
		if (myIsBrand == null) {
			myIsBrand = new BooleanDt();
		}
		return myIsBrand;
	}

	
	/**
	 * Gets the value(s) for <b>isBrand</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public Boolean getIsBrand() {  
		return getIsBrandElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>isBrand</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public Medication setIsBrand(BooleanDt theValue) {
		myIsBrand = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>isBrand</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public Medication setIsBrand( boolean theBoolean) {
		myIsBrand = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>manufacturer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the details of the manufacturer
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
     * Describes the details of the manufacturer
     * </p> 
	 */
	public Medication setManufacturer(ResourceReferenceDt theValue) {
		myManufacturer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>kind</b> (MedicationKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public BoundCodeDt<MedicationKindEnum> getKindElement() {  
		if (myKind == null) {
			myKind = new BoundCodeDt<MedicationKindEnum>(MedicationKindEnum.VALUESET_BINDER);
		}
		return myKind;
	}

	
	/**
	 * Gets the value(s) for <b>kind</b> (MedicationKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public String getKind() {  
		return getKindElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>kind</b> (MedicationKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public Medication setKind(BoundCodeDt<MedicationKindEnum> theValue) {
		myKind = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>kind</b> (MedicationKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public Medication setKind(MedicationKindEnum theValue) {
		getKindElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>product</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	public Product getProduct() {  
		if (myProduct == null) {
			myProduct = new Product();
		}
		return myProduct;
	}

	/**
	 * Sets the value(s) for <b>product</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	public Medication setProduct(Product theValue) {
		myProduct = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>package</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public Package getPackage() {  
		if (myPackage == null) {
			myPackage = new Package();
		}
		return myPackage;
	}

	/**
	 * Sets the value(s) for <b>package</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public Medication setPackage(Package theValue) {
		myPackage = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>Medication.product</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	@Block()	
	public static class Product 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="form", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="MedicationForm",
		formalDefinition="Describes the form of the item.  Powder; tables; carton"
	)
	private CodeableConceptDt myForm;
	
	@Child(name="ingredient", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a particular constituent of interest in the product"
	)
	private java.util.List<ProductIngredient> myIngredient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myForm,  myIngredient);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myForm, myIngredient);
	}

	/**
	 * Gets the value(s) for <b>form</b> (MedicationForm).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the form of the item.  Powder; tables; carton
     * </p> 
	 */
	public CodeableConceptDt getForm() {  
		if (myForm == null) {
			myForm = new CodeableConceptDt();
		}
		return myForm;
	}

	/**
	 * Sets the value(s) for <b>form</b> (MedicationForm)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the form of the item.  Powder; tables; carton
     * </p> 
	 */
	public Product setForm(CodeableConceptDt theValue) {
		myForm = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>ingredient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public java.util.List<ProductIngredient> getIngredient() {  
		if (myIngredient == null) {
			myIngredient = new java.util.ArrayList<ProductIngredient>();
		}
		return myIngredient;
	}

	/**
	 * Sets the value(s) for <b>ingredient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public Product setIngredient(java.util.List<ProductIngredient> theValue) {
		myIngredient = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>ingredient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public ProductIngredient addIngredient() {
		ProductIngredient newType = new ProductIngredient();
		getIngredient().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>ingredient</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public ProductIngredient getIngredientFirstRep() {
		if (getIngredient().isEmpty()) {
			return addIngredient();
		}
		return getIngredient().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Medication.product.ingredient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	@Block()	
	public static class ProductIngredient 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="item", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Substance.class, 		ca.uhn.fhir.model.dev.resource.Medication.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication"
	)
	private ResourceReferenceDt myItem;
	
	@Child(name="amount", type=RatioDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet"
	)
	private RatioDt myAmount;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myItem,  myAmount);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myItem, myAmount);
	}

	/**
	 * Gets the value(s) for <b>item</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual ingredient - either a substance (simple ingredient) or another medication
     * </p> 
	 */
	public ResourceReferenceDt getItem() {  
		if (myItem == null) {
			myItem = new ResourceReferenceDt();
		}
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual ingredient - either a substance (simple ingredient) or another medication
     * </p> 
	 */
	public ProductIngredient setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>amount</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet
     * </p> 
	 */
	public RatioDt getAmount() {  
		if (myAmount == null) {
			myAmount = new RatioDt();
		}
		return myAmount;
	}

	/**
	 * Sets the value(s) for <b>amount</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet
     * </p> 
	 */
	public ProductIngredient setAmount(RatioDt theValue) {
		myAmount = theValue;
		return this;
	}
	
	

  

	}



	/**
	 * Block class for child element: <b>Medication.package</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	@Block()	
	public static class Package 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="container", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="MedicationContainer",
		formalDefinition="The kind of container that this package comes as"
	)
	private CodeableConceptDt myContainer;
	
	@Child(name="content", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A set of components that go to make up the described item."
	)
	private java.util.List<PackageContent> myContent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myContainer,  myContent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myContainer, myContent);
	}

	/**
	 * Gets the value(s) for <b>container</b> (MedicationContainer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of container that this package comes as
     * </p> 
	 */
	public CodeableConceptDt getContainer() {  
		if (myContainer == null) {
			myContainer = new CodeableConceptDt();
		}
		return myContainer;
	}

	/**
	 * Sets the value(s) for <b>container</b> (MedicationContainer)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of container that this package comes as
     * </p> 
	 */
	public Package setContainer(CodeableConceptDt theValue) {
		myContainer = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>content</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of components that go to make up the described item.
     * </p> 
	 */
	public java.util.List<PackageContent> getContent() {  
		if (myContent == null) {
			myContent = new java.util.ArrayList<PackageContent>();
		}
		return myContent;
	}

	/**
	 * Sets the value(s) for <b>content</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of components that go to make up the described item.
     * </p> 
	 */
	public Package setContent(java.util.List<PackageContent> theValue) {
		myContent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>content</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of components that go to make up the described item.
     * </p> 
	 */
	public PackageContent addContent() {
		PackageContent newType = new PackageContent();
		getContent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>content</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of components that go to make up the described item.
     * </p> 
	 */
	public PackageContent getContentFirstRep() {
		if (getContent().isEmpty()) {
			return addContent();
		}
		return getContent().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Medication.package.content</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of components that go to make up the described item.
     * </p> 
	 */
	@Block()	
	public static class PackageContent 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="item", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies one of the items in the package"
	)
	private ResourceReferenceDt myItem;
	
	@Child(name="amount", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount of the product that is in the package"
	)
	private QuantityDt myAmount;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myItem,  myAmount);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myItem, myAmount);
	}

	/**
	 * Gets the value(s) for <b>item</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies one of the items in the package
     * </p> 
	 */
	public ResourceReferenceDt getItem() {  
		if (myItem == null) {
			myItem = new ResourceReferenceDt();
		}
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies one of the items in the package
     * </p> 
	 */
	public PackageContent setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>amount</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the product that is in the package
     * </p> 
	 */
	public QuantityDt getAmount() {  
		if (myAmount == null) {
			myAmount = new QuantityDt();
		}
		return myAmount;
	}

	/**
	 * Sets the value(s) for <b>amount</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the product that is in the package
     * </p> 
	 */
	public PackageContent setAmount(QuantityDt theValue) {
		myAmount = theValue;
		return this;
	}
	
	

  

	}





    @Override
    public String getResourceName() {
        return "Medication";
    }

}
