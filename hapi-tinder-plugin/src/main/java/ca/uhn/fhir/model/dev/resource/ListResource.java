















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
 * HAPI/FHIR <b>List</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A set of information summarized from a list of other resources
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/List">http://hl7.org/fhir/profiles/List</a> 
 * </p>
 *
 */
@ResourceDef(name="List", profile="http://hl7.org/fhir/profiles/List", id="list")
public class ListResource 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.source</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="List.source", description="", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.source</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.entry.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item", path="List.entry.item", description="", type="reference"  )
	public static final String SP_ITEM = "item";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.entry.item</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ITEM = new ReferenceClientParam(SP_ITEM);

	/**
	 * Search parameter constant for <b>empty-reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.emptyReason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="empty-reason", path="List.emptyReason", description="", type="token"  )
	public static final String SP_EMPTY_REASON = "empty-reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>empty-reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.emptyReason</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EMPTY_REASON = new TokenClientParam(SP_EMPTY_REASON);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>List.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="List.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>List.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="List.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="List.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="List.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("List.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("List.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.emptyReason</b>".
	 */
	public static final Include INCLUDE_EMPTYREASON = new Include("List.emptyReason");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.entry.item</b>".
	 */
	public static final Include INCLUDE_ENTRY_ITEM = new Include("List.entry.item");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.source</b>".
	 */
	public static final Include INCLUDE_SOURCE = new Include("List.source");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("List.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier for the List assigned for business purposes outside the context of FHIR."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ListPurpose",
		formalDefinition="This code defines the purpose of the list - why it was created"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Group.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The common subject (or patient) of the resources that are in the list, if there is one"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="source", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The entity responsible for deciding what the contents of the list were"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="date", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date that the list was prepared"
	)
	private DateTimeDt myDate;
	
	@Child(name="ordered", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether items in the list have a meaningful order"
	)
	private BooleanDt myOrdered;
	
	@Child(name="mode", type=CodeDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="ListMode",
		formalDefinition="How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted"
	)
	private BoundCodeDt<ListModeEnum> myMode;
	
	@Child(name="entry", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Entries in this list"
	)
	private java.util.List<Entry> myEntry;
	
	@Child(name="emptyReason", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="ListEmptyReason",
		formalDefinition="If the list is empty, why the list is empty"
	)
	private CodeableConceptDt myEmptyReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  mySubject,  mySource,  myDate,  myOrdered,  myMode,  myEntry,  myEmptyReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, mySubject, mySource, myDate, myOrdered, myMode, myEntry, myEmptyReason);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
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
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public ListResource setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
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
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>code</b> (ListPurpose).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (ListPurpose)
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public ListResource setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common subject (or patient) of the resources that are in the list, if there is one
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
     * The common subject (or patient) of the resources that are in the list, if there is one
     * </p> 
	 */
	public ListResource setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ListResource setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
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
     * The date that the list was prepared
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
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>ordered</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public BooleanDt getOrderedElement() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}

	
	/**
	 * Gets the value(s) for <b>ordered</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public Boolean getOrdered() {  
		return getOrderedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>ordered</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public ListResource setOrdered(BooleanDt theValue) {
		myOrdered = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>ordered</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public ListResource setOrdered( boolean theBoolean) {
		myOrdered = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mode</b> (ListMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public BoundCodeDt<ListModeEnum> getModeElement() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ListModeEnum>(ListModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	
	/**
	 * Gets the value(s) for <b>mode</b> (ListMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public String getMode() {  
		return getModeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mode</b> (ListMode)
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public ListResource setMode(BoundCodeDt<ListModeEnum> theValue) {
		myMode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>mode</b> (ListMode)
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public ListResource setMode(ListModeEnum theValue) {
		getModeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>entry</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public java.util.List<Entry> getEntry() {  
		if (myEntry == null) {
			myEntry = new java.util.ArrayList<Entry>();
		}
		return myEntry;
	}

	/**
	 * Sets the value(s) for <b>entry</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public ListResource setEntry(java.util.List<Entry> theValue) {
		myEntry = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>entry</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public Entry addEntry() {
		Entry newType = new Entry();
		getEntry().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>entry</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public Entry getEntryFirstRep() {
		if (getEntry().isEmpty()) {
			return addEntry();
		}
		return getEntry().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>emptyReason</b> (ListEmptyReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public CodeableConceptDt getEmptyReason() {  
		if (myEmptyReason == null) {
			myEmptyReason = new CodeableConceptDt();
		}
		return myEmptyReason;
	}

	/**
	 * Sets the value(s) for <b>emptyReason</b> (ListEmptyReason)
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public ListResource setEmptyReason(CodeableConceptDt theValue) {
		myEmptyReason = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>List.entry</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	@Block()	
	public static class Entry 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="flag", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ListItemFlag",
		formalDefinition="The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list"
	)
	private java.util.List<CodeableConceptDt> myFlag;
	
	@Child(name="deleted", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="True if this item is marked as deleted in the list."
	)
	private BooleanDt myDeleted;
	
	@Child(name="date", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="When this item was added to the list"
	)
	private DateTimeDt myDate;
	
	@Child(name="item", order=3, min=1, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A reference to the actual resource from which data was derived"
	)
	private ResourceReferenceDt myItem;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myFlag,  myDeleted,  myDate,  myItem);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myFlag, myDeleted, myDate, myItem);
	}

	/**
	 * Gets the value(s) for <b>flag</b> (ListItemFlag).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getFlag() {  
		if (myFlag == null) {
			myFlag = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myFlag;
	}

	/**
	 * Sets the value(s) for <b>flag</b> (ListItemFlag)
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public Entry setFlag(java.util.List<CodeableConceptDt> theValue) {
		myFlag = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>flag</b> (ListItemFlag)
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public CodeableConceptDt addFlag() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getFlag().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>flag</b> (ListItemFlag),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public CodeableConceptDt getFlagFirstRep() {
		if (getFlag().isEmpty()) {
			return addFlag();
		}
		return getFlag().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>deleted</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public BooleanDt getDeletedElement() {  
		if (myDeleted == null) {
			myDeleted = new BooleanDt();
		}
		return myDeleted;
	}

	
	/**
	 * Gets the value(s) for <b>deleted</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Boolean getDeleted() {  
		return getDeletedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>deleted</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Entry setDeleted(BooleanDt theValue) {
		myDeleted = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>deleted</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Entry setDeleted( boolean theBoolean) {
		myDeleted = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
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
     * When this item was added to the list
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
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>item</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the actual resource from which data was derived
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
     * A reference to the actual resource from which data was derived
     * </p> 
	 */
	public Entry setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "List";
    }

}
