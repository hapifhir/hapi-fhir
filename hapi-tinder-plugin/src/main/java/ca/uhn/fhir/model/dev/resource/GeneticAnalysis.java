















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
 * HAPI/FHIR <b>GeneticAnalysis</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Analysis of a patient's genetic test
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/GeneticAnalysis">http://hl7.org/fhir/profiles/GeneticAnalysis</a> 
 * </p>
 *
 */
@ResourceDef(name="GeneticAnalysis", profile="http://hl7.org/fhir/profiles/GeneticAnalysis", id="geneticanalysis")
public class GeneticAnalysis 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="GeneticAnalysis.subject", description="Subject of the analysis", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>Author of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="GeneticAnalysis.author", description="Author of the analysis", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>Author of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>GeneticAnalysis.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="GeneticAnalysis.date", description="Date when result of the analysis is uploaded", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>GeneticAnalysis.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("GeneticAnalysis.author");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("GeneticAnalysis.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("GeneticAnalysis.subject");


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Subject of the analysis"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Author of the analysis"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="date", type=DateDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date when result of the analysis is updated"
	)
	private DateDt myDate;
	
	@Child(name="geneticAnalysisSummary", order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Summary of the analysis"
	)
	private GeneticAnalysisSummary myGeneticAnalysisSummary;
	
	@Child(name="dnaRegionAnalysisTestCoverage", order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Coverage of the genetic test"
	)
	private DnaRegionAnalysisTestCoverage myDnaRegionAnalysisTestCoverage;
	
	@Child(name="geneticAnalysisDiscreteResult", order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Genetic analysis discrete result"
	)
	private GeneticAnalysisDiscreteResult myGeneticAnalysisDiscreteResult;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myAuthor,  myDate,  myGeneticAnalysisSummary,  myDnaRegionAnalysisTestCoverage,  myGeneticAnalysisDiscreteResult);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myAuthor, myDate, myGeneticAnalysisSummary, myDnaRegionAnalysisTestCoverage, myGeneticAnalysisDiscreteResult);
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
	public GeneticAnalysis setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>author</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Author of the analysis
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
     * Author of the analysis
     * </p> 
	 */
	public GeneticAnalysis setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
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
	public GeneticAnalysis setDate(DateDt theValue) {
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
	public GeneticAnalysis setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
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
	public GeneticAnalysis setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>geneticAnalysisSummary</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary getGeneticAnalysisSummary() {  
		if (myGeneticAnalysisSummary == null) {
			myGeneticAnalysisSummary = new GeneticAnalysisSummary();
		}
		return myGeneticAnalysisSummary;
	}

	/**
	 * Sets the value(s) for <b>geneticAnalysisSummary</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysis setGeneticAnalysisSummary(GeneticAnalysisSummary theValue) {
		myGeneticAnalysisSummary = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverage getDnaRegionAnalysisTestCoverage() {  
		if (myDnaRegionAnalysisTestCoverage == null) {
			myDnaRegionAnalysisTestCoverage = new DnaRegionAnalysisTestCoverage();
		}
		return myDnaRegionAnalysisTestCoverage;
	}

	/**
	 * Sets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	public GeneticAnalysis setDnaRegionAnalysisTestCoverage(DnaRegionAnalysisTestCoverage theValue) {
		myDnaRegionAnalysisTestCoverage = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>geneticAnalysisDiscreteResult</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	public GeneticAnalysisDiscreteResult getGeneticAnalysisDiscreteResult() {  
		if (myGeneticAnalysisDiscreteResult == null) {
			myGeneticAnalysisDiscreteResult = new GeneticAnalysisDiscreteResult();
		}
		return myGeneticAnalysisDiscreteResult;
	}

	/**
	 * Sets the value(s) for <b>geneticAnalysisDiscreteResult</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	public GeneticAnalysis setGeneticAnalysisDiscreteResult(GeneticAnalysisDiscreteResult theValue) {
		myGeneticAnalysisDiscreteResult = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisSummary</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	@Block()	
	public static class GeneticAnalysisSummary 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="geneticDiseaseAssessed", type=CodingDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Genetic disease being assesed"
	)
	private CodingDt myGeneticDiseaseAssessed;
	
	@Child(name="medicationAssesed", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Medication being assesed"
	)
	private CodingDt myMedicationAssesed;
	
	@Child(name="genomicSourceClass", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Class of the source of sample"
	)
	private CodingDt myGenomicSourceClass;
	
	@Child(name="geneticDiseaseAnalysisOverallInterpretation", type=CodingDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Overall interpretation of the patient's genotype on the genetic disease being assesed"
	)
	private CodingDt myGeneticDiseaseAnalysisOverallInterpretation;
	
	@Child(name="geneticDiseaseAnalysisOverallCarrierInterpertation", type=CodingDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Carrier status of the patietn"
	)
	private CodingDt myGeneticDiseaseAnalysisOverallCarrierInterpertation;
	
	@Child(name="drugEfficacyAnalysisOverallInterpretation", type=CodingDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Analysis on the efficacy of the drug being assessed"
	)
	private CodingDt myDrugEfficacyAnalysisOverallInterpretation;
	
	@Child(name="geneticAnalysisSummaryReport", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Summary of the analysis"
	)
	private StringDt myGeneticAnalysisSummaryReport;
	
	@Child(name="reasonForStudyAdditionalNote", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional notes"
	)
	private StringDt myReasonForStudyAdditionalNote;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGeneticDiseaseAssessed,  myMedicationAssesed,  myGenomicSourceClass,  myGeneticDiseaseAnalysisOverallInterpretation,  myGeneticDiseaseAnalysisOverallCarrierInterpertation,  myDrugEfficacyAnalysisOverallInterpretation,  myGeneticAnalysisSummaryReport,  myReasonForStudyAdditionalNote);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGeneticDiseaseAssessed, myMedicationAssesed, myGenomicSourceClass, myGeneticDiseaseAnalysisOverallInterpretation, myGeneticDiseaseAnalysisOverallCarrierInterpertation, myDrugEfficacyAnalysisOverallInterpretation, myGeneticAnalysisSummaryReport, myReasonForStudyAdditionalNote);
	}

	/**
	 * Gets the value(s) for <b>geneticDiseaseAssessed</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic disease being assesed
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAssessed() {  
		if (myGeneticDiseaseAssessed == null) {
			myGeneticDiseaseAssessed = new CodingDt();
		}
		return myGeneticDiseaseAssessed;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseAssessed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic disease being assesed
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticDiseaseAssessed(CodingDt theValue) {
		myGeneticDiseaseAssessed = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>medicationAssesed</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medication being assesed
     * </p> 
	 */
	public CodingDt getMedicationAssesed() {  
		if (myMedicationAssesed == null) {
			myMedicationAssesed = new CodingDt();
		}
		return myMedicationAssesed;
	}

	/**
	 * Sets the value(s) for <b>medicationAssesed</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Medication being assesed
     * </p> 
	 */
	public GeneticAnalysisSummary setMedicationAssesed(CodingDt theValue) {
		myMedicationAssesed = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>genomicSourceClass</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public CodingDt getGenomicSourceClass() {  
		if (myGenomicSourceClass == null) {
			myGenomicSourceClass = new CodingDt();
		}
		return myGenomicSourceClass;
	}

	/**
	 * Sets the value(s) for <b>genomicSourceClass</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public GeneticAnalysisSummary setGenomicSourceClass(CodingDt theValue) {
		myGenomicSourceClass = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Overall interpretation of the patient's genotype on the genetic disease being assesed
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAnalysisOverallInterpretation() {  
		if (myGeneticDiseaseAnalysisOverallInterpretation == null) {
			myGeneticDiseaseAnalysisOverallInterpretation = new CodingDt();
		}
		return myGeneticDiseaseAnalysisOverallInterpretation;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Overall interpretation of the patient's genotype on the genetic disease being assesed
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticDiseaseAnalysisOverallInterpretation(CodingDt theValue) {
		myGeneticDiseaseAnalysisOverallInterpretation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Carrier status of the patietn
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAnalysisOverallCarrierInterpertation() {  
		if (myGeneticDiseaseAnalysisOverallCarrierInterpertation == null) {
			myGeneticDiseaseAnalysisOverallCarrierInterpertation = new CodingDt();
		}
		return myGeneticDiseaseAnalysisOverallCarrierInterpertation;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Carrier status of the patietn
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticDiseaseAnalysisOverallCarrierInterpertation(CodingDt theValue) {
		myGeneticDiseaseAnalysisOverallCarrierInterpertation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Analysis on the efficacy of the drug being assessed
     * </p> 
	 */
	public CodingDt getDrugEfficacyAnalysisOverallInterpretation() {  
		if (myDrugEfficacyAnalysisOverallInterpretation == null) {
			myDrugEfficacyAnalysisOverallInterpretation = new CodingDt();
		}
		return myDrugEfficacyAnalysisOverallInterpretation;
	}

	/**
	 * Sets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Analysis on the efficacy of the drug being assessed
     * </p> 
	 */
	public GeneticAnalysisSummary setDrugEfficacyAnalysisOverallInterpretation(CodingDt theValue) {
		myDrugEfficacyAnalysisOverallInterpretation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>geneticAnalysisSummaryReport</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public StringDt getGeneticAnalysisSummaryReportElement() {  
		if (myGeneticAnalysisSummaryReport == null) {
			myGeneticAnalysisSummaryReport = new StringDt();
		}
		return myGeneticAnalysisSummaryReport;
	}

	
	/**
	 * Gets the value(s) for <b>geneticAnalysisSummaryReport</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public String getGeneticAnalysisSummaryReport() {  
		return getGeneticAnalysisSummaryReportElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>geneticAnalysisSummaryReport</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticAnalysisSummaryReport(StringDt theValue) {
		myGeneticAnalysisSummaryReport = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>geneticAnalysisSummaryReport</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticAnalysisSummaryReport( String theString) {
		myGeneticAnalysisSummaryReport = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reasonForStudyAdditionalNote</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public StringDt getReasonForStudyAdditionalNoteElement() {  
		if (myReasonForStudyAdditionalNote == null) {
			myReasonForStudyAdditionalNote = new StringDt();
		}
		return myReasonForStudyAdditionalNote;
	}

	
	/**
	 * Gets the value(s) for <b>reasonForStudyAdditionalNote</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public String getReasonForStudyAdditionalNote() {  
		return getReasonForStudyAdditionalNoteElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reasonForStudyAdditionalNote</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public GeneticAnalysisSummary setReasonForStudyAdditionalNote(StringDt theValue) {
		myReasonForStudyAdditionalNote = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reasonForStudyAdditionalNote</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public GeneticAnalysisSummary setReasonForStudyAdditionalNote( String theString) {
		myReasonForStudyAdditionalNote = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GeneticAnalysis.dnaRegionAnalysisTestCoverage</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	@Block()	
	public static class DnaRegionAnalysisTestCoverage 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="dnaRegionOfInterest", order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="DNA studied"
	)
	private java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> myDnaRegionOfInterest;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDnaRegionOfInterest);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDnaRegionOfInterest);
	}

	/**
	 * Gets the value(s) for <b>dnaRegionOfInterest</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> getDnaRegionOfInterest() {  
		if (myDnaRegionOfInterest == null) {
			myDnaRegionOfInterest = new java.util.ArrayList<DnaRegionAnalysisTestCoverageDnaRegionOfInterest>();
		}
		return myDnaRegionOfInterest;
	}

	/**
	 * Sets the value(s) for <b>dnaRegionOfInterest</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverage setDnaRegionOfInterest(java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> theValue) {
		myDnaRegionOfInterest = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dnaRegionOfInterest</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest addDnaRegionOfInterest() {
		DnaRegionAnalysisTestCoverageDnaRegionOfInterest newType = new DnaRegionAnalysisTestCoverageDnaRegionOfInterest();
		getDnaRegionOfInterest().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dnaRegionOfInterest</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest getDnaRegionOfInterestFirstRep() {
		if (getDnaRegionOfInterest().isEmpty()) {
			return addDnaRegionOfInterest();
		}
		return getDnaRegionOfInterest().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>GeneticAnalysis.dnaRegionAnalysisTestCoverage.dnaRegionOfInterest</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	@Block()	
	public static class DnaRegionAnalysisTestCoverageDnaRegionOfInterest 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="genomicReferenceSequenceIdentifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGenomicReferenceSequenceIdentifier;
	
	@Child(name="regionOfInterestStart", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Start position of the DNA region of interest"
	)
	private IntegerDt myRegionOfInterestStart;
	
	@Child(name="regionOfInterestStop", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="End position of the DNA region of interest"
	)
	private IntegerDt myRegionOfInterestStop;
	
	@Child(name="referenceNucleotide", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Genotype of the region in reference genome"
	)
	private StringDt myReferenceNucleotide;
	
	@Child(name="variableNucleotide", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The patient's genotype in the region"
	)
	private StringDt myVariableNucleotide;
	
	@Child(name="genechipId", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="ID of the genechip"
	)
	private StringDt myGenechipId;
	
	@Child(name="genechipManufacturerId", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="ID of manufacturer of the genechip"
	)
	private StringDt myGenechipManufacturerId;
	
	@Child(name="genechipVersion", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Version of the genechip"
	)
	private StringDt myGenechipVersion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGenomicReferenceSequenceIdentifier,  myRegionOfInterestStart,  myRegionOfInterestStop,  myReferenceNucleotide,  myVariableNucleotide,  myGenechipId,  myGenechipManufacturerId,  myGenechipVersion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGenomicReferenceSequenceIdentifier, myRegionOfInterestStart, myRegionOfInterestStop, myReferenceNucleotide, myVariableNucleotide, myGenechipId, myGenechipManufacturerId, myGenechipVersion);
	}

	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGenomicReferenceSequenceIdentifierElement() {  
		if (myGenomicReferenceSequenceIdentifier == null) {
			myGenomicReferenceSequenceIdentifier = new StringDt();
		}
		return myGenomicReferenceSequenceIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public String getGenomicReferenceSequenceIdentifier() {  
		return getGenomicReferenceSequenceIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>genomicReferenceSequenceIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenomicReferenceSequenceIdentifier(StringDt theValue) {
		myGenomicReferenceSequenceIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>genomicReferenceSequenceIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenomicReferenceSequenceIdentifier( String theString) {
		myGenomicReferenceSequenceIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>regionOfInterestStart</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public IntegerDt getRegionOfInterestStartElement() {  
		if (myRegionOfInterestStart == null) {
			myRegionOfInterestStart = new IntegerDt();
		}
		return myRegionOfInterestStart;
	}

	
	/**
	 * Gets the value(s) for <b>regionOfInterestStart</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public Integer getRegionOfInterestStart() {  
		return getRegionOfInterestStartElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>regionOfInterestStart</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStart(IntegerDt theValue) {
		myRegionOfInterestStart = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>regionOfInterestStart</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStart( int theInteger) {
		myRegionOfInterestStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>regionOfInterestStop</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public IntegerDt getRegionOfInterestStopElement() {  
		if (myRegionOfInterestStop == null) {
			myRegionOfInterestStop = new IntegerDt();
		}
		return myRegionOfInterestStop;
	}

	
	/**
	 * Gets the value(s) for <b>regionOfInterestStop</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public Integer getRegionOfInterestStop() {  
		return getRegionOfInterestStopElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>regionOfInterestStop</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStop(IntegerDt theValue) {
		myRegionOfInterestStop = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>regionOfInterestStop</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStop( int theInteger) {
		myRegionOfInterestStop = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>referenceNucleotide</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public StringDt getReferenceNucleotideElement() {  
		if (myReferenceNucleotide == null) {
			myReferenceNucleotide = new StringDt();
		}
		return myReferenceNucleotide;
	}

	
	/**
	 * Gets the value(s) for <b>referenceNucleotide</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public String getReferenceNucleotide() {  
		return getReferenceNucleotideElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>referenceNucleotide</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setReferenceNucleotide(StringDt theValue) {
		myReferenceNucleotide = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>referenceNucleotide</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setReferenceNucleotide( String theString) {
		myReferenceNucleotide = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variableNucleotide</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public StringDt getVariableNucleotideElement() {  
		if (myVariableNucleotide == null) {
			myVariableNucleotide = new StringDt();
		}
		return myVariableNucleotide;
	}

	
	/**
	 * Gets the value(s) for <b>variableNucleotide</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public String getVariableNucleotide() {  
		return getVariableNucleotideElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>variableNucleotide</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setVariableNucleotide(StringDt theValue) {
		myVariableNucleotide = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>variableNucleotide</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setVariableNucleotide( String theString) {
		myVariableNucleotide = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genechipId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public StringDt getGenechipIdElement() {  
		if (myGenechipId == null) {
			myGenechipId = new StringDt();
		}
		return myGenechipId;
	}

	
	/**
	 * Gets the value(s) for <b>genechipId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public String getGenechipId() {  
		return getGenechipIdElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>genechipId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipId(StringDt theValue) {
		myGenechipId = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>genechipId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipId( String theString) {
		myGenechipId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genechipManufacturerId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public StringDt getGenechipManufacturerIdElement() {  
		if (myGenechipManufacturerId == null) {
			myGenechipManufacturerId = new StringDt();
		}
		return myGenechipManufacturerId;
	}

	
	/**
	 * Gets the value(s) for <b>genechipManufacturerId</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public String getGenechipManufacturerId() {  
		return getGenechipManufacturerIdElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>genechipManufacturerId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipManufacturerId(StringDt theValue) {
		myGenechipManufacturerId = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>genechipManufacturerId</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipManufacturerId( String theString) {
		myGenechipManufacturerId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genechipVersion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public StringDt getGenechipVersionElement() {  
		if (myGenechipVersion == null) {
			myGenechipVersion = new StringDt();
		}
		return myGenechipVersion;
	}

	
	/**
	 * Gets the value(s) for <b>genechipVersion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public String getGenechipVersion() {  
		return getGenechipVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>genechipVersion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipVersion(StringDt theValue) {
		myGenechipVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>genechipVersion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipVersion( String theString) {
		myGenechipVersion = new StringDt(theString); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisDiscreteResult</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	@Block()	
	public static class GeneticAnalysisDiscreteResult 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="dnaAnalysisDiscreteSequenceVariation", order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="DNA analysis discrete sequence variation"
	)
	private java.util.List<GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation> myDnaAnalysisDiscreteSequenceVariation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDnaAnalysisDiscreteSequenceVariation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDnaAnalysisDiscreteSequenceVariation);
	}

	/**
	 * Gets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public java.util.List<GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation> getDnaAnalysisDiscreteSequenceVariation() {  
		if (myDnaAnalysisDiscreteSequenceVariation == null) {
			myDnaAnalysisDiscreteSequenceVariation = new java.util.ArrayList<GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation>();
		}
		return myDnaAnalysisDiscreteSequenceVariation;
	}

	/**
	 * Sets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResult setDnaAnalysisDiscreteSequenceVariation(java.util.List<GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation> theValue) {
		myDnaAnalysisDiscreteSequenceVariation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dnaAnalysisDiscreteSequenceVariation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation addDnaAnalysisDiscreteSequenceVariation() {
		GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation newType = new GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation();
		getDnaAnalysisDiscreteSequenceVariation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dnaAnalysisDiscreteSequenceVariation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation getDnaAnalysisDiscreteSequenceVariationFirstRep() {
		if (getDnaAnalysisDiscreteSequenceVariation().isEmpty()) {
			return addDnaAnalysisDiscreteSequenceVariation();
		}
		return getDnaAnalysisDiscreteSequenceVariation().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisDiscreteResult.dnaAnalysisDiscreteSequenceVariation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	@Block()	
	public static class GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="geneIdentifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGeneIdentifier;
	
	@Child(name="genomicReferenceSequenceIdentifier", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGenomicReferenceSequenceIdentifier;
	
	@Child(name="transcriptReferenceIdentifier", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myTranscriptReferenceIdentifier;
	
	@Child(name="alleleName", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Name of the allele"
	)
	private StringDt myAlleleName;
	
	@Child(name="dnaSequenceVariationIdentifier", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier of the DNA variation"
	)
	private StringDt myDnaSequenceVariationIdentifier;
	
	@Child(name="dnaSequenceVariation", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="DNA variation represented in HGVS nomenclature"
	)
	private StringDt myDnaSequenceVariation;
	
	@Child(name="dnaSequenceVariationType", type=CodingDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Type of the variation"
	)
	private CodingDt myDnaSequenceVariationType;
	
	@Child(name="aminoAcidChange", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Amino acid change represented in HGVS nomenclature"
	)
	private StringDt myAminoAcidChange;
	
	@Child(name="aminoAcidChangeType", type=CodingDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Type of the amino acid change"
	)
	private CodingDt myAminoAcidChangeType;
	
	@Child(name="dnaRegionName", type=StringDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Human-readable name of the DNA region"
	)
	private StringDt myDnaRegionName;
	
	@Child(name="allellicState", type=CodingDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Allelic state"
	)
	private CodingDt myAllellicState;
	
	@Child(name="genomicSourceClass", type=CodingDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Class of the source of sample"
	)
	private CodingDt myGenomicSourceClass;
	
	@Child(name="dnaSequenceVariationDisplayName", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Conventional display of the DNA region and its interpretation"
	)
	private StringDt myDnaSequenceVariationDisplayName;
	
	@Child(name="geneticDiseaseSequenceVariationInterpretation", type=CodingDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Interpretation of the genotype's effect on genetic disease"
	)
	private CodingDt myGeneticDiseaseSequenceVariationInterpretation;
	
	@Child(name="drugMetabolismSequenceVariationInterpretatioin", type=CodingDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Interpretation of the genotype's effect on the drug's metabolic effect"
	)
	private CodingDt myDrugMetabolismSequenceVariationInterpretatioin;
	
	@Child(name="drugEfficacySequenceVariationInterpretation", type=CodingDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Interpretation of the genotype's effect on the drug's efficacy"
	)
	private CodingDt myDrugEfficacySequenceVariationInterpretation;
	
	@Child(name="geneticVariantAssessment", type=CodingDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Genotyping result on a known set of mutation"
	)
	private CodingDt myGeneticVariantAssessment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGeneIdentifier,  myGenomicReferenceSequenceIdentifier,  myTranscriptReferenceIdentifier,  myAlleleName,  myDnaSequenceVariationIdentifier,  myDnaSequenceVariation,  myDnaSequenceVariationType,  myAminoAcidChange,  myAminoAcidChangeType,  myDnaRegionName,  myAllellicState,  myGenomicSourceClass,  myDnaSequenceVariationDisplayName,  myGeneticDiseaseSequenceVariationInterpretation,  myDrugMetabolismSequenceVariationInterpretatioin,  myDrugEfficacySequenceVariationInterpretation,  myGeneticVariantAssessment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGeneIdentifier, myGenomicReferenceSequenceIdentifier, myTranscriptReferenceIdentifier, myAlleleName, myDnaSequenceVariationIdentifier, myDnaSequenceVariation, myDnaSequenceVariationType, myAminoAcidChange, myAminoAcidChangeType, myDnaRegionName, myAllellicState, myGenomicSourceClass, myDnaSequenceVariationDisplayName, myGeneticDiseaseSequenceVariationInterpretation, myDrugMetabolismSequenceVariationInterpretatioin, myDrugEfficacySequenceVariationInterpretation, myGeneticVariantAssessment);
	}

	/**
	 * Gets the value(s) for <b>geneIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGeneIdentifierElement() {  
		if (myGeneIdentifier == null) {
			myGeneIdentifier = new StringDt();
		}
		return myGeneIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>geneIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public String getGeneIdentifier() {  
		return getGeneIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>geneIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGeneIdentifier(StringDt theValue) {
		myGeneIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>geneIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGeneIdentifier( String theString) {
		myGeneIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGenomicReferenceSequenceIdentifierElement() {  
		if (myGenomicReferenceSequenceIdentifier == null) {
			myGenomicReferenceSequenceIdentifier = new StringDt();
		}
		return myGenomicReferenceSequenceIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public String getGenomicReferenceSequenceIdentifier() {  
		return getGenomicReferenceSequenceIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>genomicReferenceSequenceIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGenomicReferenceSequenceIdentifier(StringDt theValue) {
		myGenomicReferenceSequenceIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>genomicReferenceSequenceIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGenomicReferenceSequenceIdentifier( String theString) {
		myGenomicReferenceSequenceIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>transcriptReferenceIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getTranscriptReferenceIdentifierElement() {  
		if (myTranscriptReferenceIdentifier == null) {
			myTranscriptReferenceIdentifier = new StringDt();
		}
		return myTranscriptReferenceIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>transcriptReferenceIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public String getTranscriptReferenceIdentifier() {  
		return getTranscriptReferenceIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>transcriptReferenceIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setTranscriptReferenceIdentifier(StringDt theValue) {
		myTranscriptReferenceIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>transcriptReferenceIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setTranscriptReferenceIdentifier( String theString) {
		myTranscriptReferenceIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>alleleName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the allele
     * </p> 
	 */
	public StringDt getAlleleNameElement() {  
		if (myAlleleName == null) {
			myAlleleName = new StringDt();
		}
		return myAlleleName;
	}

	
	/**
	 * Gets the value(s) for <b>alleleName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the allele
     * </p> 
	 */
	public String getAlleleName() {  
		return getAlleleNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>alleleName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the allele
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setAlleleName(StringDt theValue) {
		myAlleleName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>alleleName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the allele
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setAlleleName( String theString) {
		myAlleleName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dnaSequenceVariationIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the DNA variation
     * </p> 
	 */
	public StringDt getDnaSequenceVariationIdentifierElement() {  
		if (myDnaSequenceVariationIdentifier == null) {
			myDnaSequenceVariationIdentifier = new StringDt();
		}
		return myDnaSequenceVariationIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>dnaSequenceVariationIdentifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the DNA variation
     * </p> 
	 */
	public String getDnaSequenceVariationIdentifier() {  
		return getDnaSequenceVariationIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dnaSequenceVariationIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the DNA variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariationIdentifier(StringDt theValue) {
		myDnaSequenceVariationIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dnaSequenceVariationIdentifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the DNA variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariationIdentifier( String theString) {
		myDnaSequenceVariationIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dnaSequenceVariation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA variation represented in HGVS nomenclature
     * </p> 
	 */
	public StringDt getDnaSequenceVariationElement() {  
		if (myDnaSequenceVariation == null) {
			myDnaSequenceVariation = new StringDt();
		}
		return myDnaSequenceVariation;
	}

	
	/**
	 * Gets the value(s) for <b>dnaSequenceVariation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA variation represented in HGVS nomenclature
     * </p> 
	 */
	public String getDnaSequenceVariation() {  
		return getDnaSequenceVariationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dnaSequenceVariation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA variation represented in HGVS nomenclature
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariation(StringDt theValue) {
		myDnaSequenceVariation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dnaSequenceVariation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * DNA variation represented in HGVS nomenclature
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariation( String theString) {
		myDnaSequenceVariation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dnaSequenceVariationType</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the variation
     * </p> 
	 */
	public CodingDt getDnaSequenceVariationType() {  
		if (myDnaSequenceVariationType == null) {
			myDnaSequenceVariationType = new CodingDt();
		}
		return myDnaSequenceVariationType;
	}

	/**
	 * Sets the value(s) for <b>dnaSequenceVariationType</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariationType(CodingDt theValue) {
		myDnaSequenceVariationType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>aminoAcidChange</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acid change represented in HGVS nomenclature
     * </p> 
	 */
	public StringDt getAminoAcidChangeElement() {  
		if (myAminoAcidChange == null) {
			myAminoAcidChange = new StringDt();
		}
		return myAminoAcidChange;
	}

	
	/**
	 * Gets the value(s) for <b>aminoAcidChange</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acid change represented in HGVS nomenclature
     * </p> 
	 */
	public String getAminoAcidChange() {  
		return getAminoAcidChangeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>aminoAcidChange</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acid change represented in HGVS nomenclature
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setAminoAcidChange(StringDt theValue) {
		myAminoAcidChange = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>aminoAcidChange</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acid change represented in HGVS nomenclature
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setAminoAcidChange( String theString) {
		myAminoAcidChange = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>aminoAcidChangeType</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the amino acid change
     * </p> 
	 */
	public CodingDt getAminoAcidChangeType() {  
		if (myAminoAcidChangeType == null) {
			myAminoAcidChangeType = new CodingDt();
		}
		return myAminoAcidChangeType;
	}

	/**
	 * Sets the value(s) for <b>aminoAcidChangeType</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the amino acid change
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setAminoAcidChangeType(CodingDt theValue) {
		myAminoAcidChangeType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dnaRegionName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name of the DNA region
     * </p> 
	 */
	public StringDt getDnaRegionNameElement() {  
		if (myDnaRegionName == null) {
			myDnaRegionName = new StringDt();
		}
		return myDnaRegionName;
	}

	
	/**
	 * Gets the value(s) for <b>dnaRegionName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name of the DNA region
     * </p> 
	 */
	public String getDnaRegionName() {  
		return getDnaRegionNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dnaRegionName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name of the DNA region
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaRegionName(StringDt theValue) {
		myDnaRegionName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dnaRegionName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name of the DNA region
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaRegionName( String theString) {
		myDnaRegionName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>allellicState</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allelic state
     * </p> 
	 */
	public CodingDt getAllellicState() {  
		if (myAllellicState == null) {
			myAllellicState = new CodingDt();
		}
		return myAllellicState;
	}

	/**
	 * Sets the value(s) for <b>allellicState</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Allelic state
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setAllellicState(CodingDt theValue) {
		myAllellicState = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>genomicSourceClass</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public CodingDt getGenomicSourceClass() {  
		if (myGenomicSourceClass == null) {
			myGenomicSourceClass = new CodingDt();
		}
		return myGenomicSourceClass;
	}

	/**
	 * Sets the value(s) for <b>genomicSourceClass</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGenomicSourceClass(CodingDt theValue) {
		myGenomicSourceClass = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dnaSequenceVariationDisplayName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conventional display of the DNA region and its interpretation
     * </p> 
	 */
	public StringDt getDnaSequenceVariationDisplayNameElement() {  
		if (myDnaSequenceVariationDisplayName == null) {
			myDnaSequenceVariationDisplayName = new StringDt();
		}
		return myDnaSequenceVariationDisplayName;
	}

	
	/**
	 * Gets the value(s) for <b>dnaSequenceVariationDisplayName</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conventional display of the DNA region and its interpretation
     * </p> 
	 */
	public String getDnaSequenceVariationDisplayName() {  
		return getDnaSequenceVariationDisplayNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dnaSequenceVariationDisplayName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Conventional display of the DNA region and its interpretation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariationDisplayName(StringDt theValue) {
		myDnaSequenceVariationDisplayName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>dnaSequenceVariationDisplayName</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Conventional display of the DNA region and its interpretation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDnaSequenceVariationDisplayName( String theString) {
		myDnaSequenceVariationDisplayName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>geneticDiseaseSequenceVariationInterpretation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on genetic disease
     * </p> 
	 */
	public CodingDt getGeneticDiseaseSequenceVariationInterpretation() {  
		if (myGeneticDiseaseSequenceVariationInterpretation == null) {
			myGeneticDiseaseSequenceVariationInterpretation = new CodingDt();
		}
		return myGeneticDiseaseSequenceVariationInterpretation;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseSequenceVariationInterpretation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on genetic disease
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGeneticDiseaseSequenceVariationInterpretation(CodingDt theValue) {
		myGeneticDiseaseSequenceVariationInterpretation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>drugMetabolismSequenceVariationInterpretatioin</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on the drug's metabolic effect
     * </p> 
	 */
	public CodingDt getDrugMetabolismSequenceVariationInterpretatioin() {  
		if (myDrugMetabolismSequenceVariationInterpretatioin == null) {
			myDrugMetabolismSequenceVariationInterpretatioin = new CodingDt();
		}
		return myDrugMetabolismSequenceVariationInterpretatioin;
	}

	/**
	 * Sets the value(s) for <b>drugMetabolismSequenceVariationInterpretatioin</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on the drug's metabolic effect
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDrugMetabolismSequenceVariationInterpretatioin(CodingDt theValue) {
		myDrugMetabolismSequenceVariationInterpretatioin = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>drugEfficacySequenceVariationInterpretation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on the drug's efficacy
     * </p> 
	 */
	public CodingDt getDrugEfficacySequenceVariationInterpretation() {  
		if (myDrugEfficacySequenceVariationInterpretation == null) {
			myDrugEfficacySequenceVariationInterpretation = new CodingDt();
		}
		return myDrugEfficacySequenceVariationInterpretation;
	}

	/**
	 * Sets the value(s) for <b>drugEfficacySequenceVariationInterpretation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on the drug's efficacy
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setDrugEfficacySequenceVariationInterpretation(CodingDt theValue) {
		myDrugEfficacySequenceVariationInterpretation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>geneticVariantAssessment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotyping result on a known set of mutation
     * </p> 
	 */
	public CodingDt getGeneticVariantAssessment() {  
		if (myGeneticVariantAssessment == null) {
			myGeneticVariantAssessment = new CodingDt();
		}
		return myGeneticVariantAssessment;
	}

	/**
	 * Sets the value(s) for <b>geneticVariantAssessment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Genotyping result on a known set of mutation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation setGeneticVariantAssessment(CodingDt theValue) {
		myGeneticVariantAssessment = theValue;
		return this;
	}
	
	

  

	}





    @Override
    public String getResourceName() {
        return "GeneticAnalysis";
    }

}
