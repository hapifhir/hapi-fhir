















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
 * HAPI/FHIR <b>SequencingAnalysis</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Computational analysis on a patient's genetic raw file
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SequencingAnalysis">http://hl7.org/fhir/profiles/SequencingAnalysis</a> 
 * </p>
 *
 */
@ResourceDef(name="SequencingAnalysis", profile="http://hl7.org/fhir/profiles/SequencingAnalysis", id="sequencinganalysis")
public class SequencingAnalysis 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingAnalysis.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="SequencingAnalysis.subject", description="Subject of the analysis", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingAnalysis.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is updated</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingAnalysis.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SequencingAnalysis.date", description="Date when result of the analysis is updated", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is updated</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingAnalysis.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>genome</b>
	 * <p>
	 * Description: <b>Name of the reference genome used in the analysis</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingAnalysis.genome.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="genome", path="SequencingAnalysis.genome.name", description="Name of the reference genome used in the analysis", type="string"  )
	public static final String SP_GENOME = "genome";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>genome</b>
	 * <p>
	 * Description: <b>Name of the reference genome used in the analysis</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingAnalysis.genome.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam GENOME = new StringClientParam(SP_GENOME);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingAnalysis.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("SequencingAnalysis.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingAnalysis.genome.name</b>".
	 */
	public static final Include INCLUDE_GENOME_NAME = new Include("SequencingAnalysis.genome.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingAnalysis.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("SequencingAnalysis.subject");


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Subject of the analysis"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="date", type=DateDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date when result of the analysis is updated"
	)
	private DateDt myDate;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Name of the analysis"
	)
	private StringDt myName;
	
	@Child(name="genome", order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Reference genome used in the analysis"
	)
	private Genome myGenome;
	
	@Child(name="file", type=AttachmentDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Files uploaded as result of the analysis"
	)
	private java.util.List<AttachmentDt> myFile;
	
	@Child(name="inputLab", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.SequencingLab.class	})
	@Description(
		shortDefinition="",
		formalDefinition="SequencingLab taken into account of the analysis"
	)
	private java.util.List<ResourceReferenceDt> myInputLab;
	
	@Child(name="inputAnalysis", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.SequencingAnalysis.class	})
	@Description(
		shortDefinition="",
		formalDefinition="SequencingAnalysis taken into account of the analysis"
	)
	private java.util.List<ResourceReferenceDt> myInputAnalysis;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myDate,  myName,  myGenome,  myFile,  myInputLab,  myInputAnalysis);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myDate, myName, myGenome, myFile, myInputLab, myInputAnalysis);
	}

	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
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
     * Subject of the analysis
     * </p> 
	 */
	public SequencingAnalysis setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
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
     * Date when result of the analysis is updated
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
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
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
     * Name of the analysis
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
     * Name of the analysis
     * </p> 
	 */
	public SequencingAnalysis setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
     * </p> 
	 */
	public SequencingAnalysis setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genome</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	public Genome getGenome() {  
		if (myGenome == null) {
			myGenome = new Genome();
		}
		return myGenome;
	}

	/**
	 * Sets the value(s) for <b>genome</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	public SequencingAnalysis setGenome(Genome theValue) {
		myGenome = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>file</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
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
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public SequencingAnalysis setFile(java.util.List<AttachmentDt> theValue) {
		myFile = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>file</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
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
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public AttachmentDt getFileFirstRep() {
		if (getFile().isEmpty()) {
			return addFile();
		}
		return getFile().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>inputLab </b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getInputLab() {  
		if (myInputLab == null) {
			myInputLab = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myInputLab;
	}

	/**
	 * Sets the value(s) for <b>inputLab </b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public SequencingAnalysis setInputLab(java.util.List<ResourceReferenceDt> theValue) {
		myInputLab = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>inputLab </b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public ResourceReferenceDt addInputLab() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getInputLab().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>inputAnalysis </b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getInputAnalysis() {  
		if (myInputAnalysis == null) {
			myInputAnalysis = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myInputAnalysis;
	}

	/**
	 * Sets the value(s) for <b>inputAnalysis </b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public SequencingAnalysis setInputAnalysis(java.util.List<ResourceReferenceDt> theValue) {
		myInputAnalysis = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>inputAnalysis </b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public ResourceReferenceDt addInputAnalysis() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getInputAnalysis().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>SequencingAnalysis.genome</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	@Block()	
	public static class Genome 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ReferenceGenome",
		formalDefinition="Name of the reference genome"
	)
	private CodeDt myName;
	
	@Child(name="build", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Build number of the refernece genome"
	)
	private StringDt myBuild;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myBuild);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myBuild);
	}

	/**
	 * Gets the value(s) for <b>name</b> (ReferenceGenome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public CodeDt getNameElement() {  
		if (myName == null) {
			myName = new CodeDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ReferenceGenome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ReferenceGenome)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public Genome setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ReferenceGenome)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public Genome setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>build</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public StringDt getBuildElement() {  
		if (myBuild == null) {
			myBuild = new StringDt();
		}
		return myBuild;
	}

	
	/**
	 * Gets the value(s) for <b>build</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public String getBuild() {  
		return getBuildElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>build</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public Genome setBuild(StringDt theValue) {
		myBuild = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>build</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public Genome setBuild( String theString) {
		myBuild = new StringDt(theString); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "SequencingAnalysis";
    }

}
