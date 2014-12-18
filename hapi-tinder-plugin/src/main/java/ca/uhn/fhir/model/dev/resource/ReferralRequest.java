















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
 * HAPI/FHIR <b>ReferralRequest</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organisation
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ReferralRequest">http://hl7.org/fhir/profiles/ReferralRequest</a> 
 * </p>
 *
 */
@ResourceDef(name="ReferralRequest", profile="http://hl7.org/fhir/profiles/ReferralRequest", id="referralrequest")
public class ReferralRequest 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the referral</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="ReferralRequest.status", description="The status of the referral", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the referral</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the referral</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="ReferralRequest.type", description="The type of the referral", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the referral</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>specialty</b>
	 * <p>
	 * Description: <b>The specialty that the referral is for</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.specialty</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specialty", path="ReferralRequest.specialty", description="The specialty that the referral is for", type="token"  )
	public static final String SP_SPECIALTY = "specialty";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specialty</b>
	 * <p>
	 * Description: <b>The specialty that the referral is for</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.specialty</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SPECIALTY = new TokenClientParam(SP_SPECIALTY);

	/**
	 * Search parameter constant for <b>priority</b>
	 * <p>
	 * Description: <b>The priority assigned to the referral</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.priority</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="priority", path="ReferralRequest.priority", description="The priority assigned to the referral", type="token"  )
	public static final String SP_PRIORITY = "priority";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>priority</b>
	 * <p>
	 * Description: <b>The priority assigned to the referral</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ReferralRequest.priority</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PRIORITY = new TokenClientParam(SP_PRIORITY);

	/**
	 * Search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b>The person that the referral was sent to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ReferralRequest.recipient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="recipient", path="ReferralRequest.recipient", description="The person that the referral was sent to", type="reference"  )
	public static final String SP_RECIPIENT = "recipient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b>The person that the referral was sent to</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ReferralRequest.recipient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RECIPIENT = new ReferenceClientParam(SP_RECIPIENT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Who the referral is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ReferralRequest.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="ReferralRequest.patient", description="Who the referral is about", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Who the referral is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ReferralRequest.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ReferralRequest.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("ReferralRequest.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ReferralRequest.priority</b>".
	 */
	public static final Include INCLUDE_PRIORITY = new Include("ReferralRequest.priority");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ReferralRequest.recipient</b>".
	 */
	public static final Include INCLUDE_RECIPIENT = new Include("ReferralRequest.recipient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ReferralRequest.specialty</b>".
	 */
	public static final Include INCLUDE_SPECIALTY = new Include("ReferralRequest.specialty");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ReferralRequest.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("ReferralRequest.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ReferralRequest.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("ReferralRequest.type");


	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ReferralStatus",
		formalDefinition="The workflow status of the referral or transfer of care request"
	)
	private BoundCodeDt<ReferralStatusEnum> myStatus;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Business Id that uniquely identifies the referral/care transfer request instance"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An indication of the type of referral (or where applicable the type of transfer of care) request"
	)
	private CodeableConceptDt myType;
	
	@Child(name="specialty", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indication of the clinical domain or discipline to which the referral or transfer of care request is sent"
	)
	private CodeableConceptDt mySpecialty;
	
	@Child(name="priority", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An indication of the urgency of referral (or where applicable the type of transfer of care) request"
	)
	private CodeableConceptDt myPriority;
	
	@Child(name="patient", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The patient who is the subject of a referral or transfer of care request"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="requester", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral)"
	)
	private ResourceReferenceDt myRequester;
	
	@Child(name="recipient", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request"
	)
	private java.util.List<ResourceReferenceDt> myRecipient;
	
	@Child(name="encounter", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The encounter at which the request for referral or transfer of care is initiated"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="dateSent", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date/DateTime the request for referral or transfer of care is sent by the author"
	)
	private DateTimeDt myDateSent;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Description of clinical condition indicating why referral/transfer of care is requested"
	)
	private CodeableConceptDt myReason;
	
	@Child(name="description", type=StringDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary"
	)
	private StringDt myDescription;
	
	@Child(name="serviceRequested", type=CodeableConceptDt.class, order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The service(s) that is/are requested to be provided to the patient"
	)
	private java.util.List<CodeableConceptDt> myServiceRequested;
	
	@Child(name="supportingInformation", order=13, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care"
	)
	private java.util.List<ResourceReferenceDt> mySupportingInformation;
	
	@Child(name="fulfillmentTime", type=PeriodDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The period of time within which the services identified in the referral/transfer of care is specified or required to occur"
	)
	private PeriodDt myFulfillmentTime;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus,  myIdentifier,  myType,  mySpecialty,  myPriority,  myPatient,  myRequester,  myRecipient,  myEncounter,  myDateSent,  myReason,  myDescription,  myServiceRequested,  mySupportingInformation,  myFulfillmentTime);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStatus, myIdentifier, myType, mySpecialty, myPriority, myPatient, myRequester, myRecipient, myEncounter, myDateSent, myReason, myDescription, myServiceRequested, mySupportingInformation, myFulfillmentTime);
	}

	/**
	 * Gets the value(s) for <b>status</b> (ReferralStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the referral or transfer of care request
     * </p> 
	 */
	public BoundCodeDt<ReferralStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ReferralStatusEnum>(ReferralStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ReferralStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the referral or transfer of care request
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ReferralStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the referral or transfer of care request
     * </p> 
	 */
	public ReferralRequest setStatus(BoundCodeDt<ReferralStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ReferralStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The workflow status of the referral or transfer of care request
     * </p> 
	 */
	public ReferralRequest setStatus(ReferralStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Business Id that uniquely identifies the referral/care transfer request instance
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
     * Business Id that uniquely identifies the referral/care transfer request instance
     * </p> 
	 */
	public ReferralRequest setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Business Id that uniquely identifies the referral/care transfer request instance
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
     * Business Id that uniquely identifies the referral/care transfer request instance
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An indication of the type of referral (or where applicable the type of transfer of care) request
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An indication of the type of referral (or where applicable the type of transfer of care) request
     * </p> 
	 */
	public ReferralRequest setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specialty</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indication of the clinical domain or discipline to which the referral or transfer of care request is sent
     * </p> 
	 */
	public CodeableConceptDt getSpecialty() {  
		if (mySpecialty == null) {
			mySpecialty = new CodeableConceptDt();
		}
		return mySpecialty;
	}

	/**
	 * Sets the value(s) for <b>specialty</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indication of the clinical domain or discipline to which the referral or transfer of care request is sent
     * </p> 
	 */
	public ReferralRequest setSpecialty(CodeableConceptDt theValue) {
		mySpecialty = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>priority</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An indication of the urgency of referral (or where applicable the type of transfer of care) request
     * </p> 
	 */
	public CodeableConceptDt getPriority() {  
		if (myPriority == null) {
			myPriority = new CodeableConceptDt();
		}
		return myPriority;
	}

	/**
	 * Sets the value(s) for <b>priority</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An indication of the urgency of referral (or where applicable the type of transfer of care) request
     * </p> 
	 */
	public ReferralRequest setPriority(CodeableConceptDt theValue) {
		myPriority = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who is the subject of a referral or transfer of care request
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who is the subject of a referral or transfer of care request
     * </p> 
	 */
	public ReferralRequest setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>requester</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral)
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
     * The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral)
     * </p> 
	 */
	public ReferralRequest setRequester(ResourceReferenceDt theValue) {
		myRequester = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>recipient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRecipient() {  
		if (myRecipient == null) {
			myRecipient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRecipient;
	}

	/**
	 * Sets the value(s) for <b>recipient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request
     * </p> 
	 */
	public ReferralRequest setRecipient(java.util.List<ResourceReferenceDt> theValue) {
		myRecipient = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>recipient</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request
     * </p> 
	 */
	public ResourceReferenceDt addRecipient() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getRecipient().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The encounter at which the request for referral or transfer of care is initiated
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
     * The encounter at which the request for referral or transfer of care is initiated
     * </p> 
	 */
	public ReferralRequest setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dateSent</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date/DateTime the request for referral or transfer of care is sent by the author
     * </p> 
	 */
	public DateTimeDt getDateSentElement() {  
		if (myDateSent == null) {
			myDateSent = new DateTimeDt();
		}
		return myDateSent;
	}

	
	/**
	 * Gets the value(s) for <b>dateSent</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date/DateTime the request for referral or transfer of care is sent by the author
     * </p> 
	 */
	public Date getDateSent() {  
		return getDateSentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dateSent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date/DateTime the request for referral or transfer of care is sent by the author
     * </p> 
	 */
	public ReferralRequest setDateSent(DateTimeDt theValue) {
		myDateSent = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateSent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date/DateTime the request for referral or transfer of care is sent by the author
     * </p> 
	 */
	public ReferralRequest setDateSent( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateSent = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateSent</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date/DateTime the request for referral or transfer of care is sent by the author
     * </p> 
	 */
	public ReferralRequest setDateSentWithSecondsPrecision( Date theDate) {
		myDateSent = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reason</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of clinical condition indicating why referral/transfer of care is requested
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Description of clinical condition indicating why referral/transfer of care is requested
     * </p> 
	 */
	public ReferralRequest setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary
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
     * The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary
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
     * The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary
     * </p> 
	 */
	public ReferralRequest setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary
     * </p> 
	 */
	public ReferralRequest setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>serviceRequested</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The service(s) that is/are requested to be provided to the patient
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getServiceRequested() {  
		if (myServiceRequested == null) {
			myServiceRequested = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myServiceRequested;
	}

	/**
	 * Sets the value(s) for <b>serviceRequested</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The service(s) that is/are requested to be provided to the patient
     * </p> 
	 */
	public ReferralRequest setServiceRequested(java.util.List<CodeableConceptDt> theValue) {
		myServiceRequested = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>serviceRequested</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The service(s) that is/are requested to be provided to the patient
     * </p> 
	 */
	public CodeableConceptDt addServiceRequested() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getServiceRequested().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>serviceRequested</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The service(s) that is/are requested to be provided to the patient
     * </p> 
	 */
	public CodeableConceptDt getServiceRequestedFirstRep() {
		if (getServiceRequested().isEmpty()) {
			return addServiceRequested();
		}
		return getServiceRequested().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>supportingInformation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSupportingInformation() {  
		if (mySupportingInformation == null) {
			mySupportingInformation = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySupportingInformation;
	}

	/**
	 * Sets the value(s) for <b>supportingInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care
     * </p> 
	 */
	public ReferralRequest setSupportingInformation(java.util.List<ResourceReferenceDt> theValue) {
		mySupportingInformation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>supportingInformation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care
     * </p> 
	 */
	public ResourceReferenceDt addSupportingInformation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSupportingInformation().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>fulfillmentTime</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period of time within which the services identified in the referral/transfer of care is specified or required to occur
     * </p> 
	 */
	public PeriodDt getFulfillmentTime() {  
		if (myFulfillmentTime == null) {
			myFulfillmentTime = new PeriodDt();
		}
		return myFulfillmentTime;
	}

	/**
	 * Sets the value(s) for <b>fulfillmentTime</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The period of time within which the services identified in the referral/transfer of care is specified or required to occur
     * </p> 
	 */
	public ReferralRequest setFulfillmentTime(PeriodDt theValue) {
		myFulfillmentTime = theValue;
		return this;
	}
	
	

  


    @Override
    public String getResourceName() {
        return "ReferralRequest";
    }

}
