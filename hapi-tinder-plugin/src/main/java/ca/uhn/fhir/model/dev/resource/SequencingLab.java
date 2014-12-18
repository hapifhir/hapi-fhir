















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
 * HAPI/FHIR <b>SequencingLab</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A lab for sequencing
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SequencingLab">http://hl7.org/fhir/profiles/SequencingLab</a> 
 * </p>
 *
 */
@ResourceDef(name="SequencingLab", profile="http://hl7.org/fhir/profiles/SequencingLab", id="sequencinglab")
public class SequencingLab 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the lab</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingLab.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="SequencingLab.subject", description="Subject of the lab", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the lab</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingLab.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>Type of the specimen used for the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.specimen.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="SequencingLab.specimen.type", description="Type of the specimen used for the lab", type="string"  )
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>Type of the specimen used for the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.specimen.type</b><br/>
	 * </p>
	 */
	public static final StringClientParam SPECIMEN = new StringClientParam(SP_SPECIMEN);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the lab is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingLab.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SequencingLab.date", description="Date when result of the lab is uploaded", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the lab is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingLab.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>Organization that does the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.organization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="SequencingLab.organization", description="Organization that does the lab", type="string"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>Organization that does the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.organization</b><br/>
	 * </p>
	 */
	public static final StringClientParam ORGANIZATION = new StringClientParam(SP_ORGANIZATION);

	/**
	 * Search parameter constant for <b>system-class</b>
	 * <p>
	 * Description: <b>Class of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.class</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="system-class", path="SequencingLab.system.class", description="Class of the sequencing system", type="string"  )
	public static final String SP_SYSTEM_CLASS = "system-class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system-class</b>
	 * <p>
	 * Description: <b>Class of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.class</b><br/>
	 * </p>
	 */
	public static final StringClientParam SYSTEM_CLASS = new StringClientParam(SP_SYSTEM_CLASS);

	/**
	 * Search parameter constant for <b>system-name</b>
	 * <p>
	 * Description: <b>Name of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="system-name", path="SequencingLab.system.name", description="Name of the sequencing system", type="string"  )
	public static final String SP_SYSTEM_NAME = "system-name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system-name</b>
	 * <p>
	 * Description: <b>Name of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam SYSTEM_NAME = new StringClientParam(SP_SYSTEM_NAME);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("SequencingLab.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.organization</b>".
	 */
	public static final Include INCLUDE_ORGANIZATION = new Include("SequencingLab.organization");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.specimen.type</b>".
	 */
	public static final Include INCLUDE_SPECIMEN_TYPE = new Include("SequencingLab.specimen.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("SequencingLab.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.system.class</b>".
	 */
	public static final Include INCLUDE_SYSTEM_CLASS = new Include("SequencingLab.system.class");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.system.name</b>".
	 */
	public static final Include INCLUDE_SYSTEM_NAME = new Include("SequencingLab.system.name");


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Subject of the sequencing lab"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="organization", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Organization that does the sequencing"
	)
	private StringDt myOrganization;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Name of the lab"
	)
	private StringDt myName;
	
	@Child(name="date", type=DateDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date when the result of the lab is uploaded"
	)
	private DateDt myDate;
	
	@Child(name="type", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="SequencingType",
		formalDefinition="Type of the sequencing lab"
	)
	private CodeDt myType;
	
	@Child(name="system", order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="System of machine used for sequencing"
	)
	private System mySystem;
	
	@Child(name="specimen", order=6, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Specimen of the lab"
	)
	private Specimen mySpecimen;
	
	@Child(name="file", type=AttachmentDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Files uploaded as results of the lab"
	)
	private java.util.List<AttachmentDt> myFile;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myOrganization,  myName,  myDate,  myType,  mySystem,  mySpecimen,  myFile);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myOrganization, myName, myDate, myType, mySystem, mySpecimen, myFile);
	}

	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the sequencing lab
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
     * Subject of the sequencing lab
     * </p> 
	 */
	public SequencingLab setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>organization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public StringDt getOrganizationElement() {  
		if (myOrganization == null) {
			myOrganization = new StringDt();
		}
		return myOrganization;
	}

	
	/**
	 * Gets the value(s) for <b>organization</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public String getOrganization() {  
		return getOrganizationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>organization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public SequencingLab setOrganization(StringDt theValue) {
		myOrganization = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>organization</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public SequencingLab setOrganization( String theString) {
		myOrganization = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the lab
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
     * Name of the lab
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
     * Name of the lab
     * </p> 
	 */
	public SequencingLab setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the lab
     * </p> 
	 */
	public SequencingLab setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public DateDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateDt();
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
     * Date when the result of the lab is uploaded
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
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public SequencingLab setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public SequencingLab setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public SequencingLab setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (SequencingType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public CodeDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (SequencingType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (SequencingType)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public SequencingLab setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> (SequencingType)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public SequencingLab setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * System of machine used for sequencing
     * </p> 
	 */
	public System getSystem() {  
		if (mySystem == null) {
			mySystem = new System();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * System of machine used for sequencing
     * </p> 
	 */
	public SequencingLab setSystem(System theValue) {
		mySystem = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>specimen</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen of the lab
     * </p> 
	 */
	public Specimen getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new Specimen();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen of the lab
     * </p> 
	 */
	public SequencingLab setSpecimen(Specimen theValue) {
		mySpecimen = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>file</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public java.util.List<AttachmentDt> getFile() {  
		if (myFile == null) {
			myFile = new java.util.ArrayList<AttachmentDt>();
		}
		return myFile;
	}

	/**
	 * Sets the value(s) for <b>file</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public SequencingLab setFile(java.util.List<AttachmentDt> theValue) {
		myFile = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>file</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public AttachmentDt addFile() {
		AttachmentDt newType = new AttachmentDt();
		getFile().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>file</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public AttachmentDt getFileFirstRep() {
		if (getFile().isEmpty()) {
			return addFile();
		}
		return getFile().get(0); 
	}
  
	/**
	 * Block class for child element: <b>SequencingLab.system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * System of machine used for sequencing
     * </p> 
	 */
	@Block()	
	public static class System 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="class", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="SequencingSystemClass",
		formalDefinition="Class of sequencing system"
	)
	private CodeDt myClassElement;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Version of sequencing system"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SequencingSystemName",
		formalDefinition="Name of sequencing system"
	)
	private CodeDt myName;
	
	@Child(name="identity", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Id of sequencing system"
	)
	private StringDt myIdentity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myClassElement,  myVersion,  myName,  myIdentity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myClassElement, myVersion, myName, myIdentity);
	}

	/**
	 * Gets the value(s) for <b>class</b> (SequencingSystemClass).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public CodeDt getClassElementElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeDt();
		}
		return myClassElement;
	}

	
	/**
	 * Gets the value(s) for <b>class</b> (SequencingSystemClass).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public String getClassElement() {  
		return getClassElementElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>class</b> (SequencingSystemClass)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public System setClassElement(CodeDt theValue) {
		myClassElement = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>class</b> (SequencingSystemClass)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public System setClassElement( String theCode) {
		myClassElement = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public System setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public System setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (SequencingSystemName).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public CodeDt getNameElement() {  
		if (myName == null) {
			myName = new CodeDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (SequencingSystemName).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (SequencingSystemName)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public System setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (SequencingSystemName)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public System setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public StringDt getIdentityElement() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}

	
	/**
	 * Gets the value(s) for <b>identity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public String getIdentity() {  
		return getIdentityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public System setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public System setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>SequencingLab.specimen</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen of the lab
     * </p> 
	 */
	@Block()	
	public static class Specimen 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="SequencingSpecimenType",
		formalDefinition="Whether the specimen is from germline or somatic cells of the patient"
	)
	private CodeDt myType;
	
	@Child(name="source", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Source of the specimen"
	)
	private CodeableConceptDt mySource;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySource);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySource);
	}

	/**
	 * Gets the value(s) for <b>type</b> (SequencingSpecimenType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public CodeDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (SequencingSpecimenType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (SequencingSpecimenType)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public Specimen setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> (SequencingSpecimenType)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public Specimen setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public CodeableConceptDt getSource() {  
		if (mySource == null) {
			mySource = new CodeableConceptDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public Specimen setSource(CodeableConceptDt theValue) {
		mySource = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "SequencingLab";
    }

}
