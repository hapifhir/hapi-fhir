















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
 * HAPI/FHIR <b>MedicationPrescription</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MedicationPrescription">http://hl7.org/fhir/profiles/MedicationPrescription</a> 
 * </p>
 *
 */
@ResourceDef(name="MedicationPrescription", profile="http://hl7.org/fhir/profiles/MedicationPrescription", id="medicationprescription")
public class MedicationPrescription 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>datewritten</b>
	 * <p>
	 * Description: <b>Return prescriptions written on this date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationPrescription.dateWritten</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="datewritten", path="MedicationPrescription.dateWritten", description="Return prescriptions written on this date", type="date"  )
	public static final String SP_DATEWRITTEN = "datewritten";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>datewritten</b>
	 * <p>
	 * Description: <b>Return prescriptions written on this date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationPrescription.dateWritten</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATEWRITTEN = new DateClientParam(SP_DATEWRITTEN);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return prescriptions with this encounter identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationPrescription.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="MedicationPrescription.encounter", description="Return prescriptions with this encounter identity", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return prescriptions with this encounter identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationPrescription.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return prescriptions with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationPrescription.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="MedicationPrescription.identifier", description="Return prescriptions with this external identity", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return prescriptions with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationPrescription.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Code for medicine or text in medicine name</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationPrescription.medication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="medication", path="MedicationPrescription.medication", description="Code for medicine or text in medicine name", type="reference"  )
	public static final String SP_MEDICATION = "medication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Code for medicine or text in medicine name</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationPrescription.medication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MEDICATION = new ReferenceClientParam(SP_MEDICATION);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list dispenses  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationPrescription.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="MedicationPrescription.patient", description="The identity of a patient to list dispenses  for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list dispenses  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationPrescription.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the prescription</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationPrescription.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="MedicationPrescription.status", description="Status of the prescription", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the prescription</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationPrescription.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationPrescription.dateWritten</b>".
	 */
	public static final Include INCLUDE_DATEWRITTEN = new Include("MedicationPrescription.dateWritten");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationPrescription.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("MedicationPrescription.encounter");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationPrescription.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("MedicationPrescription.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationPrescription.medication</b>".
	 */
	public static final Include INCLUDE_MEDICATION = new Include("MedicationPrescription.medication");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationPrescription.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("MedicationPrescription.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationPrescription.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("MedicationPrescription.status");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="dateWritten", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date (and perhaps time) when the prescription was written"
	)
	private DateTimeDt myDateWritten;
	
	@Child(name="status", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="MedicationPrescriptionStatus",
		formalDefinition="A code specifying the state of the order.  Generally this will be active or completed state"
	)
	private BoundCodeDt<MedicationPrescriptionStatusEnum> myStatus;
	
	@Child(name="patient", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A link to a resource representing the person to whom the medication will be given."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="prescriber", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The healthcare professional responsible for authorizing the prescription"
	)
	private ResourceReferenceDt myPrescriber;
	
	@Child(name="encounter", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A link to a resource that identifies the particular occurrence of contact between patient and health care provider."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="reason", order=6, min=0, max=1, type={
		CodeableConceptDt.class, 		Condition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Can be the reason or the indication for writing the prescription."
	)
	private IDatatype myReason;
	
	@Child(name="medication", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="dosageInstruction", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates how the medication is to be used by the patient"
	)
	private java.util.List<DosageInstruction> myDosageInstruction;
	
	@Child(name="dispense", order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Deals with details of the dispense part of the order"
	)
	private Dispense myDispense;
	
	@Child(name="substitution", order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done."
	)
	private Substitution mySubstitution;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDateWritten,  myStatus,  myPatient,  myPrescriber,  myEncounter,  myReason,  myMedication,  myDosageInstruction,  myDispense,  mySubstitution);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDateWritten, myStatus, myPatient, myPrescriber, myEncounter, myReason, myMedication, myDosageInstruction, myDispense, mySubstitution);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.
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
     * External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.
     * </p> 
	 */
	public MedicationPrescription setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.
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
     * External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>dateWritten</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and perhaps time) when the prescription was written
     * </p> 
	 */
	public DateTimeDt getDateWrittenElement() {  
		if (myDateWritten == null) {
			myDateWritten = new DateTimeDt();
		}
		return myDateWritten;
	}

	
	/**
	 * Gets the value(s) for <b>dateWritten</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and perhaps time) when the prescription was written
     * </p> 
	 */
	public Date getDateWritten() {  
		return getDateWrittenElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dateWritten</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and perhaps time) when the prescription was written
     * </p> 
	 */
	public MedicationPrescription setDateWritten(DateTimeDt theValue) {
		myDateWritten = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dateWritten</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and perhaps time) when the prescription was written
     * </p> 
	 */
	public MedicationPrescription setDateWritten( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateWritten = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateWritten</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and perhaps time) when the prescription was written
     * </p> 
	 */
	public MedicationPrescription setDateWrittenWithSecondsPrecision( Date theDate) {
		myDateWritten = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (MedicationPrescriptionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the order.  Generally this will be active or completed state
     * </p> 
	 */
	public BoundCodeDt<MedicationPrescriptionStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationPrescriptionStatusEnum>(MedicationPrescriptionStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (MedicationPrescriptionStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the order.  Generally this will be active or completed state
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (MedicationPrescriptionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the order.  Generally this will be active or completed state
     * </p> 
	 */
	public MedicationPrescription setStatus(BoundCodeDt<MedicationPrescriptionStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (MedicationPrescriptionStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the order.  Generally this will be active or completed state
     * </p> 
	 */
	public MedicationPrescription setStatus(MedicationPrescriptionStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person to whom the medication will be given.
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
     * A link to a resource representing the person to whom the medication will be given.
     * </p> 
	 */
	public MedicationPrescription setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>prescriber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare professional responsible for authorizing the prescription
     * </p> 
	 */
	public ResourceReferenceDt getPrescriber() {  
		if (myPrescriber == null) {
			myPrescriber = new ResourceReferenceDt();
		}
		return myPrescriber;
	}

	/**
	 * Sets the value(s) for <b>prescriber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The healthcare professional responsible for authorizing the prescription
     * </p> 
	 */
	public MedicationPrescription setPrescriber(ResourceReferenceDt theValue) {
		myPrescriber = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource that identifies the particular occurrence of contact between patient and health care provider.
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
     * A link to a resource that identifies the particular occurrence of contact between patient and health care provider.
     * </p> 
	 */
	public MedicationPrescription setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reason[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Can be the reason or the indication for writing the prescription.
     * </p> 
	 */
	public IDatatype getReason() {  
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Can be the reason or the indication for writing the prescription.
     * </p> 
	 */
	public MedicationPrescription setReason(IDatatype theValue) {
		myReason = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>medication</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedication() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}

	/**
	 * Sets the value(s) for <b>medication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public MedicationPrescription setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dosageInstruction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public java.util.List<DosageInstruction> getDosageInstruction() {  
		if (myDosageInstruction == null) {
			myDosageInstruction = new java.util.ArrayList<DosageInstruction>();
		}
		return myDosageInstruction;
	}

	/**
	 * Sets the value(s) for <b>dosageInstruction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public MedicationPrescription setDosageInstruction(java.util.List<DosageInstruction> theValue) {
		myDosageInstruction = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dosageInstruction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public DosageInstruction addDosageInstruction() {
		DosageInstruction newType = new DosageInstruction();
		getDosageInstruction().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dosageInstruction</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public DosageInstruction getDosageInstructionFirstRep() {
		if (getDosageInstruction().isEmpty()) {
			return addDosageInstruction();
		}
		return getDosageInstruction().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>dispense</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Deals with details of the dispense part of the order
     * </p> 
	 */
	public Dispense getDispense() {  
		if (myDispense == null) {
			myDispense = new Dispense();
		}
		return myDispense;
	}

	/**
	 * Sets the value(s) for <b>dispense</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Deals with details of the dispense part of the order
     * </p> 
	 */
	public MedicationPrescription setDispense(Dispense theValue) {
		myDispense = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>substitution</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.
     * </p> 
	 */
	public Substitution getSubstitution() {  
		if (mySubstitution == null) {
			mySubstitution = new Substitution();
		}
		return mySubstitution;
	}

	/**
	 * Sets the value(s) for <b>substitution</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.
     * </p> 
	 */
	public MedicationPrescription setSubstitution(Substitution theValue) {
		mySubstitution = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>MedicationPrescription.dosageInstruction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	@Block()	
	public static class DosageInstruction 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="text", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Free text dosage instructions for cases where the instructions are too complex to code."
	)
	private StringDt myText;
	
	@Child(name="additionalInstructions", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded."
	)
	private CodeableConceptDt myAdditionalInstructions;
	
	@Child(name="scheduled", order=2, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class, 		TimingDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\""
	)
	private IDatatype myScheduled;
	
	@Child(name="asNeeded", order=3, min=0, max=1, type={
		BooleanDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="MedicationAsNeededReason",
		formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication"
	)
	private IDatatype myAsNeeded;
	
	@Child(name="site", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="MedicationAdministrationSite",
		formalDefinition="A coded specification of the anatomic site where the medication first enters the body"
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="RouteOfAdministration",
		formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient."
	)
	private CodeableConceptDt myRoute;
	
	@Child(name="method", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="MedicationAdministrationMethod",
		formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration."
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="doseQuantity", type=QuantityDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount of therapeutic or other substance given at one administration event."
	)
	private QuantityDt myDoseQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours."
	)
	private RatioDt myRate;
	
	@Child(name="maxDosePerPeriod", type=RatioDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours."
	)
	private RatioDt myMaxDosePerPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myText,  myAdditionalInstructions,  myScheduled,  myAsNeeded,  mySite,  myRoute,  myMethod,  myDoseQuantity,  myRate,  myMaxDosePerPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myText, myAdditionalInstructions, myScheduled, myAsNeeded, mySite, myRoute, myMethod, myDoseQuantity, myRate, myMaxDosePerPeriod);
	}

	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Free text dosage instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Free text dosage instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Free text dosage instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public DosageInstruction setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Free text dosage instructions for cases where the instructions are too complex to code.
     * </p> 
	 */
	public DosageInstruction setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>additionalInstructions</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded.
     * </p> 
	 */
	public CodeableConceptDt getAdditionalInstructions() {  
		if (myAdditionalInstructions == null) {
			myAdditionalInstructions = new CodeableConceptDt();
		}
		return myAdditionalInstructions;
	}

	/**
	 * Sets the value(s) for <b>additionalInstructions</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded.
     * </p> 
	 */
	public DosageInstruction setAdditionalInstructions(CodeableConceptDt theValue) {
		myAdditionalInstructions = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>scheduled[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"
     * </p> 
	 */
	public IDatatype getScheduled() {  
		return myScheduled;
	}

	/**
	 * Sets the value(s) for <b>scheduled[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"
     * </p> 
	 */
	public DosageInstruction setScheduled(IDatatype theValue) {
		myScheduled = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>asNeeded[x]</b> (MedicationAsNeededReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public IDatatype getAsNeeded() {  
		return myAsNeeded;
	}

	/**
	 * Sets the value(s) for <b>asNeeded[x]</b> (MedicationAsNeededReason)
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public DosageInstruction setAsNeeded(IDatatype theValue) {
		myAsNeeded = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>site</b> (MedicationAdministrationSite).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first enters the body
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (MedicationAdministrationSite)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first enters the body
     * </p> 
	 */
	public DosageInstruction setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>route</b> (RouteOfAdministration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient.
     * </p> 
	 */
	public CodeableConceptDt getRoute() {  
		if (myRoute == null) {
			myRoute = new CodeableConceptDt();
		}
		return myRoute;
	}

	/**
	 * Sets the value(s) for <b>route</b> (RouteOfAdministration)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient.
     * </p> 
	 */
	public DosageInstruction setRoute(CodeableConceptDt theValue) {
		myRoute = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>method</b> (MedicationAdministrationMethod).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (MedicationAdministrationMethod)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public DosageInstruction setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>doseQuantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
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
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DosageInstruction setDoseQuantity(QuantityDt theValue) {
		myDoseQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>rate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
     * </p> 
	 */
	public RatioDt getRate() {  
		if (myRate == null) {
			myRate = new RatioDt();
		}
		return myRate;
	}

	/**
	 * Sets the value(s) for <b>rate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
     * </p> 
	 */
	public DosageInstruction setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriod() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}

	/**
	 * Sets the value(s) for <b>maxDosePerPeriod</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public DosageInstruction setMaxDosePerPeriod(RatioDt theValue) {
		myMaxDosePerPeriod = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>MedicationPrescription.dispense</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Deals with details of the dispense part of the order
     * </p> 
	 */
	@Block()	
	public static class Dispense 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="medication", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the medication that is to be dispensed.  This may be a more specifically defined than the medicationPrescription.medication . This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="validityPeriod", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) It reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. Rationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription."
	)
	private PeriodDt myValidityPeriod;
	
	@Child(name="numberOfRepeatsAllowed", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill."
	)
	private IntegerDt myNumberOfRepeatsAllowed;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount that is to be dispensed."
	)
	private QuantityDt myQuantity;
	
	@Child(name="expectedSupplyDuration", type=DurationDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. In some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors."
	)
	private DurationDt myExpectedSupplyDuration;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMedication,  myValidityPeriod,  myNumberOfRepeatsAllowed,  myQuantity,  myExpectedSupplyDuration);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMedication, myValidityPeriod, myNumberOfRepeatsAllowed, myQuantity, myExpectedSupplyDuration);
	}

	/**
	 * Gets the value(s) for <b>medication</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that is to be dispensed.  This may be a more specifically defined than the medicationPrescription.medication . This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedication() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}

	/**
	 * Sets the value(s) for <b>medication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that is to be dispensed.  This may be a more specifically defined than the medicationPrescription.medication . This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public Dispense setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>validityPeriod</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) It reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. Rationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription.
     * </p> 
	 */
	public PeriodDt getValidityPeriod() {  
		if (myValidityPeriod == null) {
			myValidityPeriod = new PeriodDt();
		}
		return myValidityPeriod;
	}

	/**
	 * Sets the value(s) for <b>validityPeriod</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) It reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. Rationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription.
     * </p> 
	 */
	public Dispense setValidityPeriod(PeriodDt theValue) {
		myValidityPeriod = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>numberOfRepeatsAllowed</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
     * </p> 
	 */
	public IntegerDt getNumberOfRepeatsAllowedElement() {  
		if (myNumberOfRepeatsAllowed == null) {
			myNumberOfRepeatsAllowed = new IntegerDt();
		}
		return myNumberOfRepeatsAllowed;
	}

	
	/**
	 * Gets the value(s) for <b>numberOfRepeatsAllowed</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
     * </p> 
	 */
	public Integer getNumberOfRepeatsAllowed() {  
		return getNumberOfRepeatsAllowedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>numberOfRepeatsAllowed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
     * </p> 
	 */
	public Dispense setNumberOfRepeatsAllowed(IntegerDt theValue) {
		myNumberOfRepeatsAllowed = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>numberOfRepeatsAllowed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
     * </p> 
	 */
	public Dispense setNumberOfRepeatsAllowed( int theInteger) {
		myNumberOfRepeatsAllowed = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount that is to be dispensed.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The amount that is to be dispensed.
     * </p> 
	 */
	public Dispense setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>expectedSupplyDuration</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. In some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors.
     * </p> 
	 */
	public DurationDt getExpectedSupplyDuration() {  
		if (myExpectedSupplyDuration == null) {
			myExpectedSupplyDuration = new DurationDt();
		}
		return myExpectedSupplyDuration;
	}

	/**
	 * Sets the value(s) for <b>expectedSupplyDuration</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. In some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors.
     * </p> 
	 */
	public Dispense setExpectedSupplyDuration(DurationDt theValue) {
		myExpectedSupplyDuration = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>MedicationPrescription.substitution</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.
     * </p> 
	 */
	@Block()	
	public static class Substitution 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="MedicationIntendedSubstitutionType",
		formalDefinition="A code signifying whether a different drug should be dispensed from what was prescribed."
	)
	private CodeableConceptDt myType;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="MedicationIntendedSubstitutionReason",
		formalDefinition="Indicates the reason for the substitution, or why substitution must or must not be performed."
	)
	private CodeableConceptDt myReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myReason);
	}

	/**
	 * Gets the value(s) for <b>type</b> (MedicationIntendedSubstitutionType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code signifying whether a different drug should be dispensed from what was prescribed.
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (MedicationIntendedSubstitutionType)
	 *
     * <p>
     * <b>Definition:</b>
     * A code signifying whether a different drug should be dispensed from what was prescribed.
     * </p> 
	 */
	public Substitution setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reason</b> (MedicationIntendedSubstitutionReason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the reason for the substitution, or why substitution must or must not be performed.
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (MedicationIntendedSubstitutionReason)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the reason for the substitution, or why substitution must or must not be performed.
     * </p> 
	 */
	public Substitution setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "MedicationPrescription";
    }

}
