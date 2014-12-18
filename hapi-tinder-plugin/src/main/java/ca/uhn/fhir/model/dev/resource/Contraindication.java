















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
 * HAPI/FHIR <b>Contraindication</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient.  E.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Contraindication">http://hl7.org/fhir/profiles/Contraindication</a> 
 * </p>
 *
 */
@ResourceDef(name="Contraindication", profile="http://hl7.org/fhir/profiles/Contraindication", id="contraindication")
public class Contraindication 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Contraindication.category</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="category", path="Contraindication.category", description="", type="token"  )
	public static final String SP_CATEGORY = "category";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Contraindication.category</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CATEGORY = new TokenClientParam(SP_CATEGORY);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Contraindication.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Contraindication.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Contraindication.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Contraindication.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Contraindication.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Contraindication.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contraindication.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Contraindication.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contraindication.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>implicated</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contraindication.implicated</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="implicated", path="Contraindication.implicated", description="", type="reference"  )
	public static final String SP_IMPLICATED = "implicated";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>implicated</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Contraindication.implicated</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam IMPLICATED = new ReferenceClientParam(SP_IMPLICATED);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Contraindication.category</b>".
	 */
	public static final Include INCLUDE_CATEGORY = new Include("Contraindication.category");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Contraindication.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Contraindication.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Contraindication.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Contraindication.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Contraindication.implicated</b>".
	 */
	public static final Include INCLUDE_IMPLICATED = new Include("Contraindication.implicated");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Contraindication.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Contraindication.patient");


	@Child(name="patient", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the patient whose record the contraindication is associated with."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="category", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ContraindicationCategory",
		formalDefinition="Identifies the general type of issue identified."
	)
	private CodeableConceptDt myCategory;
	
	@Child(name="severity", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ContraindicationSeverity",
		formalDefinition="Indicates the degree of importance associated with the identified issue based on the potential impact on the patient"
	)
	private CodeDt mySeverity;
	
	@Child(name="implicated", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the resource representing the current activity or proposed activity that"
	)
	private java.util.List<ResourceReferenceDt> myImplicated;
	
	@Child(name="detail", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A textual explanation of the contraindication"
	)
	private StringDt myDetail;
	
	@Child(name="date", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date or date-time when the contraindication was initially identified."
	)
	private DateTimeDt myDate;
	
	@Child(name="author", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the provider or software that identified the"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="identifier", type=IdentifierDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Business identifier associated with the contraindication record"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="reference", type=UriDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified"
	)
	private UriDt myReference;
	
	@Child(name="mitigation", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindicaiton from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action."
	)
	private java.util.List<Mitigation> myMitigation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPatient,  myCategory,  mySeverity,  myImplicated,  myDetail,  myDate,  myAuthor,  myIdentifier,  myReference,  myMitigation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPatient, myCategory, mySeverity, myImplicated, myDetail, myDate, myAuthor, myIdentifier, myReference, myMitigation);
	}

	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the patient whose record the contraindication is associated with.
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
     * Indicates the patient whose record the contraindication is associated with.
     * </p> 
	 */
	public Contraindication setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>category</b> (ContraindicationCategory).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the general type of issue identified.
     * </p> 
	 */
	public CodeableConceptDt getCategory() {  
		if (myCategory == null) {
			myCategory = new CodeableConceptDt();
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (ContraindicationCategory)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the general type of issue identified.
     * </p> 
	 */
	public Contraindication setCategory(CodeableConceptDt theValue) {
		myCategory = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>severity</b> (ContraindicationSeverity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient
     * </p> 
	 */
	public CodeDt getSeverityElement() {  
		if (mySeverity == null) {
			mySeverity = new CodeDt();
		}
		return mySeverity;
	}

	
	/**
	 * Gets the value(s) for <b>severity</b> (ContraindicationSeverity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient
     * </p> 
	 */
	public String getSeverity() {  
		return getSeverityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>severity</b> (ContraindicationSeverity)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient
     * </p> 
	 */
	public Contraindication setSeverity(CodeDt theValue) {
		mySeverity = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>severity</b> (ContraindicationSeverity)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient
     * </p> 
	 */
	public Contraindication setSeverity( String theCode) {
		mySeverity = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>implicated</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the resource representing the current activity or proposed activity that
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getImplicated() {  
		if (myImplicated == null) {
			myImplicated = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myImplicated;
	}

	/**
	 * Sets the value(s) for <b>implicated</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the resource representing the current activity or proposed activity that
     * </p> 
	 */
	public Contraindication setImplicated(java.util.List<ResourceReferenceDt> theValue) {
		myImplicated = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>implicated</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the resource representing the current activity or proposed activity that
     * </p> 
	 */
	public ResourceReferenceDt addImplicated() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getImplicated().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A textual explanation of the contraindication
     * </p> 
	 */
	public StringDt getDetailElement() {  
		if (myDetail == null) {
			myDetail = new StringDt();
		}
		return myDetail;
	}

	
	/**
	 * Gets the value(s) for <b>detail</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A textual explanation of the contraindication
     * </p> 
	 */
	public String getDetail() {  
		return getDetailElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A textual explanation of the contraindication
     * </p> 
	 */
	public Contraindication setDetail(StringDt theValue) {
		myDetail = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>detail</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A textual explanation of the contraindication
     * </p> 
	 */
	public Contraindication setDetail( String theString) {
		myDetail = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date or date-time when the contraindication was initially identified.
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
     * The date or date-time when the contraindication was initially identified.
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
     * The date or date-time when the contraindication was initially identified.
     * </p> 
	 */
	public Contraindication setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date or date-time when the contraindication was initially identified.
     * </p> 
	 */
	public Contraindication setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date or date-time when the contraindication was initially identified.
     * </p> 
	 */
	public Contraindication setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the provider or software that identified the
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the provider or software that identified the
     * </p> 
	 */
	public Contraindication setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Business identifier associated with the contraindication record
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
     * Business identifier associated with the contraindication record
     * </p> 
	 */
	public Contraindication setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified
     * </p> 
	 */
	public UriDt getReferenceElement() {  
		if (myReference == null) {
			myReference = new UriDt();
		}
		return myReference;
	}

	
	/**
	 * Gets the value(s) for <b>reference</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified
     * </p> 
	 */
	public URI getReference() {  
		return getReferenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified
     * </p> 
	 */
	public Contraindication setReference(UriDt theValue) {
		myReference = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reference</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified
     * </p> 
	 */
	public Contraindication setReference( String theUri) {
		myReference = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mitigation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindicaiton from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     * </p> 
	 */
	public java.util.List<Mitigation> getMitigation() {  
		if (myMitigation == null) {
			myMitigation = new java.util.ArrayList<Mitigation>();
		}
		return myMitigation;
	}

	/**
	 * Sets the value(s) for <b>mitigation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindicaiton from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     * </p> 
	 */
	public Contraindication setMitigation(java.util.List<Mitigation> theValue) {
		myMitigation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>mitigation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindicaiton from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     * </p> 
	 */
	public Mitigation addMitigation() {
		Mitigation newType = new Mitigation();
		getMitigation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>mitigation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindicaiton from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     * </p> 
	 */
	public Mitigation getMitigationFirstRep() {
		if (getMitigation().isEmpty()) {
			return addMitigation();
		}
		return getMitigation().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Contraindication.mitigation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindicaiton from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     * </p> 
	 */
	@Block()	
	public static class Mitigation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="action", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ContraindicationMitigationAction",
		formalDefinition="Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication"
	)
	private CodeableConceptDt myAction;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates when the mitigating action was documented"
	)
	private DateTimeDt myDate;
	
	@Child(name="author", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring"
	)
	private ResourceReferenceDt myAuthor;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myAction,  myDate,  myAuthor);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myAction, myDate, myAuthor);
	}

	/**
	 * Gets the value(s) for <b>action</b> (ContraindicationMitigationAction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication
     * </p> 
	 */
	public CodeableConceptDt getAction() {  
		if (myAction == null) {
			myAction = new CodeableConceptDt();
		}
		return myAction;
	}

	/**
	 * Sets the value(s) for <b>action</b> (ContraindicationMitigationAction)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication
     * </p> 
	 */
	public Mitigation setAction(CodeableConceptDt theValue) {
		myAction = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the mitigating action was documented
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
     * Indicates when the mitigating action was documented
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
     * Indicates when the mitigating action was documented
     * </p> 
	 */
	public Mitigation setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the mitigating action was documented
     * </p> 
	 */
	public Mitigation setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates when the mitigating action was documented
     * </p> 
	 */
	public Mitigation setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring
     * </p> 
	 */
	public Mitigation setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Contraindication";
    }

}
