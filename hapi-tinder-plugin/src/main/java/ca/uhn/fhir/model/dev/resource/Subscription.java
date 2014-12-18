















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
 * HAPI/FHIR <b>Subscription</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Todo
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Subscription">http://hl7.org/fhir/profiles/Subscription</a> 
 * </p>
 *
 */
@ResourceDef(name="Subscription", profile="http://hl7.org/fhir/profiles/Subscription", id="subscription")
public class Subscription 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Subscription.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Subscription.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Subscription.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Subscription.channel.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Subscription.channel.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Subscription.channel.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.channel.url</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="url", path="Subscription.channel.url", description="", type="string"  )
	public static final String SP_URL = "url";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.channel.url</b><br/>
	 * </p>
	 */
	public static final StringClientParam URL = new StringClientParam(SP_URL);

	/**
	 * Search parameter constant for <b>criteria</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.criteria</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="criteria", path="Subscription.criteria", description="", type="string"  )
	public static final String SP_CRITERIA = "criteria";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>criteria</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.criteria</b><br/>
	 * </p>
	 */
	public static final StringClientParam CRITERIA = new StringClientParam(SP_CRITERIA);

	/**
	 * Search parameter constant for <b>payload</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.channel.payload</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="payload", path="Subscription.channel.payload", description="", type="string"  )
	public static final String SP_PAYLOAD = "payload";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>payload</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.channel.payload</b><br/>
	 * </p>
	 */
	public static final StringClientParam PAYLOAD = new StringClientParam(SP_PAYLOAD);

	/**
	 * Search parameter constant for <b>contact</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Subscription.contact</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="contact", path="Subscription.contact", description="", type="token"  )
	public static final String SP_CONTACT = "contact";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>contact</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Subscription.contact</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTACT = new TokenClientParam(SP_CONTACT);

	/**
	 * Search parameter constant for <b>tag</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.tag.term</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="tag", path="Subscription.tag.term", description="", type="string"  )
	public static final String SP_TAG = "tag";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>tag</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Subscription.tag.term</b><br/>
	 * </p>
	 */
	public static final StringClientParam TAG = new StringClientParam(SP_TAG);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.channel.payload</b>".
	 */
	public static final Include INCLUDE_CHANNEL_PAYLOAD = new Include("Subscription.channel.payload");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.channel.type</b>".
	 */
	public static final Include INCLUDE_CHANNEL_TYPE = new Include("Subscription.channel.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.channel.url</b>".
	 */
	public static final Include INCLUDE_CHANNEL_URL = new Include("Subscription.channel.url");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.contact</b>".
	 */
	public static final Include INCLUDE_CONTACT = new Include("Subscription.contact");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.criteria</b>".
	 */
	public static final Include INCLUDE_CRITERIA = new Include("Subscription.criteria");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Subscription.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Subscription.tag.term</b>".
	 */
	public static final Include INCLUDE_TAG_TERM = new Include("Subscription.tag.term");


	@Child(name="criteria", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private StringDt myCriteria;
	
	@Child(name="contact", type=ContactPointDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private java.util.List<ContactPointDt> myContact;
	
	@Child(name="reason", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private StringDt myReason;
	
	@Child(name="status", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="SubscriptionStatus",
		formalDefinition="Todo"
	)
	private BoundCodeDt<SubscriptionStatusEnum> myStatus;
	
	@Child(name="error", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private StringDt myError;
	
	@Child(name="channel", order=5, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private Channel myChannel;
	
	@Child(name="end", type=InstantDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private InstantDt myEnd;
	
	@Child(name="tag", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private java.util.List<Tag> myTag;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCriteria,  myContact,  myReason,  myStatus,  myError,  myChannel,  myEnd,  myTag);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCriteria, myContact, myReason, myStatus, myError, myChannel, myEnd, myTag);
	}

	/**
	 * Gets the value(s) for <b>criteria</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public StringDt getCriteriaElement() {  
		if (myCriteria == null) {
			myCriteria = new StringDt();
		}
		return myCriteria;
	}

	
	/**
	 * Gets the value(s) for <b>criteria</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public String getCriteria() {  
		return getCriteriaElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>criteria</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setCriteria(StringDt theValue) {
		myCriteria = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>criteria</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setCriteria( String theString) {
		myCriteria = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
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
     * Todo
     * </p> 
	 */
	public Subscription setContact(java.util.List<ContactPointDt> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
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
     * Todo
     * </p> 
	 */
	public ContactPointDt getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>reason</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public StringDt getReasonElement() {  
		if (myReason == null) {
			myReason = new StringDt();
		}
		return myReason;
	}

	
	/**
	 * Gets the value(s) for <b>reason</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public String getReason() {  
		return getReasonElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reason</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setReason(StringDt theValue) {
		myReason = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reason</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setReason( String theString) {
		myReason = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (SubscriptionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public BoundCodeDt<SubscriptionStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SubscriptionStatusEnum>(SubscriptionStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (SubscriptionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (SubscriptionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setStatus(BoundCodeDt<SubscriptionStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (SubscriptionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setStatus(SubscriptionStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>error</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public StringDt getErrorElement() {  
		if (myError == null) {
			myError = new StringDt();
		}
		return myError;
	}

	
	/**
	 * Gets the value(s) for <b>error</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public String getError() {  
		return getErrorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>error</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setError(StringDt theValue) {
		myError = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>error</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setError( String theString) {
		myError = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>channel</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Channel getChannel() {  
		if (myChannel == null) {
			myChannel = new Channel();
		}
		return myChannel;
	}

	/**
	 * Sets the value(s) for <b>channel</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setChannel(Channel theValue) {
		myChannel = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>end</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public InstantDt getEndElement() {  
		if (myEnd == null) {
			myEnd = new InstantDt();
		}
		return myEnd;
	}

	
	/**
	 * Gets the value(s) for <b>end</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Date getEnd() {  
		return getEndElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>end</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setEnd(InstantDt theValue) {
		myEnd = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>end</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setEndWithMillisPrecision( Date theDate) {
		myEnd = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>tag</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public java.util.List<Tag> getTag() {  
		if (myTag == null) {
			myTag = new java.util.ArrayList<Tag>();
		}
		return myTag;
	}

	/**
	 * Sets the value(s) for <b>tag</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Subscription setTag(java.util.List<Tag> theValue) {
		myTag = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>tag</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag addTag() {
		Tag newType = new Tag();
		getTag().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>tag</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag getTagFirstRep() {
		if (getTag().isEmpty()) {
			return addTag();
		}
		return getTag().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Subscription.channel</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	@Block()	
	public static class Channel 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="SubscriptionChannelType",
		formalDefinition="Todo"
	)
	private BoundCodeDt<SubscriptionChannelTypeEnum> myType;
	
	@Child(name="url", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private UriDt myUrl;
	
	@Child(name="payload", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="ToDo"
	)
	private StringDt myPayload;
	
	@Child(name="header", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myHeader;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myUrl,  myPayload,  myHeader);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myUrl, myPayload, myHeader);
	}

	/**
	 * Gets the value(s) for <b>type</b> (SubscriptionChannelType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public BoundCodeDt<SubscriptionChannelTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<SubscriptionChannelTypeEnum>(SubscriptionChannelTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (SubscriptionChannelType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (SubscriptionChannelType)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Channel setType(BoundCodeDt<SubscriptionChannelTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (SubscriptionChannelType)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Channel setType(SubscriptionChannelTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Channel setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Channel setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>payload</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ToDo
     * </p> 
	 */
	public StringDt getPayloadElement() {  
		if (myPayload == null) {
			myPayload = new StringDt();
		}
		return myPayload;
	}

	
	/**
	 * Gets the value(s) for <b>payload</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ToDo
     * </p> 
	 */
	public String getPayload() {  
		return getPayloadElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>payload</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * ToDo
     * </p> 
	 */
	public Channel setPayload(StringDt theValue) {
		myPayload = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>payload</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * ToDo
     * </p> 
	 */
	public Channel setPayload( String theString) {
		myPayload = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>header</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getHeaderElement() {  
		if (myHeader == null) {
			myHeader = new StringDt();
		}
		return myHeader;
	}

	
	/**
	 * Gets the value(s) for <b>header</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getHeader() {  
		return getHeaderElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>header</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Channel setHeader(StringDt theValue) {
		myHeader = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>header</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Channel setHeader( String theString) {
		myHeader = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Subscription.tag</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	@Block()	
	public static class Tag 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="term", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private UriDt myTerm;
	
	@Child(name="scheme", type=UriDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private UriDt myScheme;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Todo"
	)
	private StringDt myDescription;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTerm,  myScheme,  myDescription);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTerm, myScheme, myDescription);
	}

	/**
	 * Gets the value(s) for <b>term</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public UriDt getTermElement() {  
		if (myTerm == null) {
			myTerm = new UriDt();
		}
		return myTerm;
	}

	
	/**
	 * Gets the value(s) for <b>term</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public URI getTerm() {  
		return getTermElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>term</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag setTerm(UriDt theValue) {
		myTerm = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>term</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag setTerm( String theUri) {
		myTerm = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>scheme</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public UriDt getSchemeElement() {  
		if (myScheme == null) {
			myScheme = new UriDt();
		}
		return myScheme;
	}

	
	/**
	 * Gets the value(s) for <b>scheme</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public URI getScheme() {  
		return getSchemeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>scheme</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag setScheme(UriDt theValue) {
		myScheme = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>scheme</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag setScheme( String theUri) {
		myScheme = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
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
     * Todo
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
     * Todo
     * </p> 
	 */
	public Tag setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Tag setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "Subscription";
    }

}
