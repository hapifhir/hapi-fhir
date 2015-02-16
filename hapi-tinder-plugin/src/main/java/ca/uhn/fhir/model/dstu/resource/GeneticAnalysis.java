















package ca.uhn.fhir.model.dstu.resource;


import java.util.*;

import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dstu.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.resource.Appointment;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.resource.Availability;
import ca.uhn.fhir.model.dstu.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu.resource.CarePlan;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanActivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanGoalStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CarePlanStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.CompositionAttestationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConceptMapEquivalenceEnum;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.valueset.ConditionRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.CriticalityEnum;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DeviceObservationReport;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.resource.DocumentReference;
import ca.uhn.fhir.model.dstu.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.resource.FamilyHistory;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.resource.GVFMeta;
import ca.uhn.fhir.model.dstu.resource.Group;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.resource.Immunization;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dstu.resource.ImmunizationRecommendation;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ListModeEnum;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.resource.Media;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.valueset.MedicationPrescriptionStatusEnum;
import ca.uhn.fhir.model.dstu.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dstu.resource.Microarray;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Order;
import ca.uhn.fhir.model.dstu.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.resource.Procedure;
import ca.uhn.fhir.model.dstu.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QueryOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireGroupNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.dstu.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.composite.ScheduleDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SensitivityTypeEnum;
import ca.uhn.fhir.model.dstu.resource.SequencingAnalysis;
import ca.uhn.fhir.model.dstu.resource.SequencingLab;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.dstu.resource.Slot;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dstu.resource.Substance;
import ca.uhn.fhir.model.dstu.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyTypeEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.AgeDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IdrefDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>GeneticAnalysis</b> Resource
 * (Analysis of a patient's genetic test)
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
	 * Description: <b>Subject of the analysis</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>GeneticAnalysis.subject</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="GeneticAnalysis.subject", description="Subject of the analysis", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>GeneticAnalysis.subject</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("GeneticAnalysis.subject");

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>Author of the analysis</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>GeneticAnalysis.author</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="GeneticAnalysis.author", description="Author of the analysis", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>Author of the analysis</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>GeneticAnalysis.author</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("GeneticAnalysis.author");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is uploaded</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>GeneticAnalysis.date</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="GeneticAnalysis.date", description="Date when result of the analysis is uploaded", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is uploaded</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>GeneticAnalysis.date</b><br>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject of the analysis",
		formalDefinition="Subject of the analysis"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Author of the analysis",
		formalDefinition="Author of the analysis"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="date", type=DateDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Date when result of the analysis is updated",
		formalDefinition="Date when result of the analysis is updated"
	)
	private DateDt myDate;
	
	@Child(name="geneticAnalysisSummary", order=3, min=1, max=1)	
	@Description(
		shortDefinition="Summary of the analysis",
		formalDefinition="Summary of the analysis"
	)
	private GeneticAnalysisSummary myGeneticAnalysisSummary;
	
	@Child(name="dnaRegionAnalysisTestCoverage", order=4, min=0, max=1)	
	@Description(
		shortDefinition="Coverage of the genetic test",
		formalDefinition="Coverage of the genetic test"
	)
	private DnaRegionAnalysisTestCoverage myDnaRegionAnalysisTestCoverage;
	
	@Child(name="geneticAnalysisDiscreteResult", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Genetic analysis discrete result",
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
	 * Gets the value(s) for <b>subject</b> (Subject of the analysis).
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
	 * Gets the value(s) for <b>subject</b> (Subject of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
     * </p> 
	 */
	public ResourceReferenceDt getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (Subject of the analysis)
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
	 * Gets the value(s) for <b>author</b> (Author of the analysis).
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
	 * Gets the value(s) for <b>author</b> (Author of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Author of the analysis
     * </p> 
	 */
	public ResourceReferenceDt getAuthorElement() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}


	/**
	 * Sets the value(s) for <b>author</b> (Author of the analysis)
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
	 * Gets the value(s) for <b>date</b> (Date when result of the analysis is updated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public DateDt getDate() {  
		if (myDate == null) {
			myDate = new DateDt();
		}
		return myDate;
	}


	/**
	 * Gets the value(s) for <b>date</b> (Date when result of the analysis is updated).
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
	 * Sets the value(s) for <b>date</b> (Date when result of the analysis is updated)
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
	 * Sets the value for <b>date</b> (Date when result of the analysis is updated)
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
	 * Sets the value for <b>date</b> (Date when result of the analysis is updated)
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
	 * Gets the value(s) for <b>geneticAnalysisSummary</b> (Summary of the analysis).
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
	 * Gets the value(s) for <b>geneticAnalysisSummary</b> (Summary of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary getGeneticAnalysisSummaryElement() {  
		if (myGeneticAnalysisSummary == null) {
			myGeneticAnalysisSummary = new GeneticAnalysisSummary();
		}
		return myGeneticAnalysisSummary;
	}


	/**
	 * Sets the value(s) for <b>geneticAnalysisSummary</b> (Summary of the analysis)
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
	 * Gets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test).
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
	 * Gets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverage getDnaRegionAnalysisTestCoverageElement() {  
		if (myDnaRegionAnalysisTestCoverage == null) {
			myDnaRegionAnalysisTestCoverage = new DnaRegionAnalysisTestCoverage();
		}
		return myDnaRegionAnalysisTestCoverage;
	}


	/**
	 * Sets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test)
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
	 * Gets the value(s) for <b>geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result).
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
	 * Gets the value(s) for <b>geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	public GeneticAnalysisDiscreteResult getGeneticAnalysisDiscreteResultElement() {  
		if (myGeneticAnalysisDiscreteResult == null) {
			myGeneticAnalysisDiscreteResult = new GeneticAnalysisDiscreteResult();
		}
		return myGeneticAnalysisDiscreteResult;
	}


	/**
	 * Sets the value(s) for <b>geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result)
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
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisSummary</b> (Summary of the analysis)
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
		shortDefinition="Genetic disease being assesed",
		formalDefinition="Genetic disease being assesed"
	)
	private CodingDt myGeneticDiseaseAssessed;
	
	@Child(name="medicationAssesed", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Medication being assesed",
		formalDefinition="Medication being assesed"
	)
	private CodingDt myMedicationAssesed;
	
	@Child(name="genomicSourceClass", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Class of the source of sample",
		formalDefinition="Class of the source of sample"
	)
	private CodingDt myGenomicSourceClass;
	
	@Child(name="geneticDiseaseAnalysisOverallInterpretation", type=CodingDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Overall interpretation of the patient's genotype on the genetic disease being assesed",
		formalDefinition="Overall interpretation of the patient's genotype on the genetic disease being assesed"
	)
	private CodingDt myGeneticDiseaseAnalysisOverallInterpretation;
	
	@Child(name="geneticDiseaseAnalysisOverallCarrierInterpertation", type=CodingDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Carrier status of the patietn",
		formalDefinition="Carrier status of the patietn"
	)
	private CodingDt myGeneticDiseaseAnalysisOverallCarrierInterpertation;
	
	@Child(name="drugEfficacyAnalysisOverallInterpretation", type=CodingDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Analysis on the efficacy of the drug being assessed",
		formalDefinition="Analysis on the efficacy of the drug being assessed"
	)
	private CodingDt myDrugEfficacyAnalysisOverallInterpretation;
	
	@Child(name="geneticAnalysisSummaryReport", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Summary of the analysis",
		formalDefinition="Summary of the analysis"
	)
	private StringDt myGeneticAnalysisSummaryReport;
	
	@Child(name="reasonForStudyAdditionalNote", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Additional notes",
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
	 * Gets the value(s) for <b>geneticDiseaseAssessed</b> (Genetic disease being assesed).
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
	 * Gets the value(s) for <b>geneticDiseaseAssessed</b> (Genetic disease being assesed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic disease being assesed
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAssessedElement() {  
		if (myGeneticDiseaseAssessed == null) {
			myGeneticDiseaseAssessed = new CodingDt();
		}
		return myGeneticDiseaseAssessed;
	}


	/**
	 * Sets the value(s) for <b>geneticDiseaseAssessed</b> (Genetic disease being assesed)
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
	 * Gets the value(s) for <b>medicationAssesed</b> (Medication being assesed).
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
	 * Gets the value(s) for <b>medicationAssesed</b> (Medication being assesed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medication being assesed
     * </p> 
	 */
	public CodingDt getMedicationAssesedElement() {  
		if (myMedicationAssesed == null) {
			myMedicationAssesed = new CodingDt();
		}
		return myMedicationAssesed;
	}


	/**
	 * Sets the value(s) for <b>medicationAssesed</b> (Medication being assesed)
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
	 * Gets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample).
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
	 * Gets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public CodingDt getGenomicSourceClassElement() {  
		if (myGenomicSourceClass == null) {
			myGenomicSourceClass = new CodingDt();
		}
		return myGenomicSourceClass;
	}


	/**
	 * Sets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample)
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
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> (Overall interpretation of the patient's genotype on the genetic disease being assesed).
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
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> (Overall interpretation of the patient's genotype on the genetic disease being assesed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Overall interpretation of the patient's genotype on the genetic disease being assesed
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAnalysisOverallInterpretationElement() {  
		if (myGeneticDiseaseAnalysisOverallInterpretation == null) {
			myGeneticDiseaseAnalysisOverallInterpretation = new CodingDt();
		}
		return myGeneticDiseaseAnalysisOverallInterpretation;
	}


	/**
	 * Sets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> (Overall interpretation of the patient's genotype on the genetic disease being assesed)
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
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> (Carrier status of the patietn).
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
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> (Carrier status of the patietn).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Carrier status of the patietn
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAnalysisOverallCarrierInterpertationElement() {  
		if (myGeneticDiseaseAnalysisOverallCarrierInterpertation == null) {
			myGeneticDiseaseAnalysisOverallCarrierInterpertation = new CodingDt();
		}
		return myGeneticDiseaseAnalysisOverallCarrierInterpertation;
	}


	/**
	 * Sets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> (Carrier status of the patietn)
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
	 * Gets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> (Analysis on the efficacy of the drug being assessed).
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
	 * Gets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> (Analysis on the efficacy of the drug being assessed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Analysis on the efficacy of the drug being assessed
     * </p> 
	 */
	public CodingDt getDrugEfficacyAnalysisOverallInterpretationElement() {  
		if (myDrugEfficacyAnalysisOverallInterpretation == null) {
			myDrugEfficacyAnalysisOverallInterpretation = new CodingDt();
		}
		return myDrugEfficacyAnalysisOverallInterpretation;
	}


	/**
	 * Sets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> (Analysis on the efficacy of the drug being assessed)
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
	 * Gets the value(s) for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public StringDt getGeneticAnalysisSummaryReport() {  
		if (myGeneticAnalysisSummaryReport == null) {
			myGeneticAnalysisSummaryReport = new StringDt();
		}
		return myGeneticAnalysisSummaryReport;
	}


	/**
	 * Gets the value(s) for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis).
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
	 * Sets the value(s) for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis)
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
	 * Sets the value for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis)
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
	 * Gets the value(s) for <b>reasonForStudyAdditionalNote</b> (Additional notes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public StringDt getReasonForStudyAdditionalNote() {  
		if (myReasonForStudyAdditionalNote == null) {
			myReasonForStudyAdditionalNote = new StringDt();
		}
		return myReasonForStudyAdditionalNote;
	}


	/**
	 * Gets the value(s) for <b>reasonForStudyAdditionalNote</b> (Additional notes).
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
	 * Sets the value(s) for <b>reasonForStudyAdditionalNote</b> (Additional notes)
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
	 * Sets the value for <b>reasonForStudyAdditionalNote</b> (Additional notes)
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
	 * Block class for child element: <b>GeneticAnalysis.dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test)
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
		shortDefinition="DNA studied",
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
	 * Gets the value(s) for <b>dnaRegionOfInterest</b> (DNA studied).
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
	 * Gets the value(s) for <b>dnaRegionOfInterest</b> (DNA studied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> getDnaRegionOfInterestElement() {  
		if (myDnaRegionOfInterest == null) {
			myDnaRegionOfInterest = new java.util.ArrayList<DnaRegionAnalysisTestCoverageDnaRegionOfInterest>();
		}
		return myDnaRegionOfInterest;
	}


	/**
	 * Sets the value(s) for <b>dnaRegionOfInterest</b> (DNA studied)
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
	 * Adds and returns a new value for <b>dnaRegionOfInterest</b> (DNA studied)
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
	 * Gets the first repetition for <b>dnaRegionOfInterest</b> (DNA studied),
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
	 * Block class for child element: <b>GeneticAnalysis.dnaRegionAnalysisTestCoverage.dnaRegionOfInterest</b> (DNA studied)
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
		shortDefinition="Genomic reference sequence identifier",
		formalDefinition="Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGenomicReferenceSequenceIdentifier;
	
	@Child(name="regionOfInterestStart", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Start position of the DNA region of interest",
		formalDefinition="Start position of the DNA region of interest"
	)
	private IntegerDt myRegionOfInterestStart;
	
	@Child(name="regionOfInterestStop", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="End position of the DNA region of interest",
		formalDefinition="End position of the DNA region of interest"
	)
	private IntegerDt myRegionOfInterestStop;
	
	@Child(name="referenceNucleotide", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Genotype of the region in reference genome",
		formalDefinition="Genotype of the region in reference genome"
	)
	private StringDt myReferenceNucleotide;
	
	@Child(name="variableNucleotide", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="The patient's genotype in the region",
		formalDefinition="The patient's genotype in the region"
	)
	private StringDt myVariableNucleotide;
	
	@Child(name="genechipId", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="ID of the genechip",
		formalDefinition="ID of the genechip"
	)
	private StringDt myGenechipId;
	
	@Child(name="genechipManufacturerId", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="ID of manufacturer of the genechip",
		formalDefinition="ID of manufacturer of the genechip"
	)
	private StringDt myGenechipManufacturerId;
	
	@Child(name="genechipVersion", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Version of the genechip",
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
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGenomicReferenceSequenceIdentifier() {  
		if (myGenomicReferenceSequenceIdentifier == null) {
			myGenomicReferenceSequenceIdentifier = new StringDt();
		}
		return myGenomicReferenceSequenceIdentifier;
	}


	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier).
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
	 * Sets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier)
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
	 * Sets the value for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier)
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
	 * Gets the value(s) for <b>regionOfInterestStart</b> (Start position of the DNA region of interest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public IntegerDt getRegionOfInterestStart() {  
		if (myRegionOfInterestStart == null) {
			myRegionOfInterestStart = new IntegerDt();
		}
		return myRegionOfInterestStart;
	}


	/**
	 * Gets the value(s) for <b>regionOfInterestStart</b> (Start position of the DNA region of interest).
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
	 * Sets the value(s) for <b>regionOfInterestStart</b> (Start position of the DNA region of interest)
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
	 * Sets the value for <b>regionOfInterestStart</b> (Start position of the DNA region of interest)
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
	 * Gets the value(s) for <b>regionOfInterestStop</b> (End position of the DNA region of interest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public IntegerDt getRegionOfInterestStop() {  
		if (myRegionOfInterestStop == null) {
			myRegionOfInterestStop = new IntegerDt();
		}
		return myRegionOfInterestStop;
	}


	/**
	 * Gets the value(s) for <b>regionOfInterestStop</b> (End position of the DNA region of interest).
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
	 * Sets the value(s) for <b>regionOfInterestStop</b> (End position of the DNA region of interest)
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
	 * Sets the value for <b>regionOfInterestStop</b> (End position of the DNA region of interest)
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
	 * Gets the value(s) for <b>referenceNucleotide</b> (Genotype of the region in reference genome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public StringDt getReferenceNucleotide() {  
		if (myReferenceNucleotide == null) {
			myReferenceNucleotide = new StringDt();
		}
		return myReferenceNucleotide;
	}


	/**
	 * Gets the value(s) for <b>referenceNucleotide</b> (Genotype of the region in reference genome).
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
	 * Sets the value(s) for <b>referenceNucleotide</b> (Genotype of the region in reference genome)
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
	 * Sets the value for <b>referenceNucleotide</b> (Genotype of the region in reference genome)
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
	 * Gets the value(s) for <b>variableNucleotide</b> (The patient's genotype in the region).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public StringDt getVariableNucleotide() {  
		if (myVariableNucleotide == null) {
			myVariableNucleotide = new StringDt();
		}
		return myVariableNucleotide;
	}


	/**
	 * Gets the value(s) for <b>variableNucleotide</b> (The patient's genotype in the region).
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
	 * Sets the value(s) for <b>variableNucleotide</b> (The patient's genotype in the region)
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
	 * Sets the value for <b>variableNucleotide</b> (The patient's genotype in the region)
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
	 * Gets the value(s) for <b>genechipId</b> (ID of the genechip).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public StringDt getGenechipId() {  
		if (myGenechipId == null) {
			myGenechipId = new StringDt();
		}
		return myGenechipId;
	}


	/**
	 * Gets the value(s) for <b>genechipId</b> (ID of the genechip).
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
	 * Sets the value(s) for <b>genechipId</b> (ID of the genechip)
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
	 * Sets the value for <b>genechipId</b> (ID of the genechip)
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
	 * Gets the value(s) for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public StringDt getGenechipManufacturerId() {  
		if (myGenechipManufacturerId == null) {
			myGenechipManufacturerId = new StringDt();
		}
		return myGenechipManufacturerId;
	}


	/**
	 * Gets the value(s) for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip).
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
	 * Sets the value(s) for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip)
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
	 * Sets the value for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip)
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
	 * Gets the value(s) for <b>genechipVersion</b> (Version of the genechip).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public StringDt getGenechipVersion() {  
		if (myGenechipVersion == null) {
			myGenechipVersion = new StringDt();
		}
		return myGenechipVersion;
	}


	/**
	 * Gets the value(s) for <b>genechipVersion</b> (Version of the genechip).
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
	 * Sets the value(s) for <b>genechipVersion</b> (Version of the genechip)
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
	 * Sets the value for <b>genechipVersion</b> (Version of the genechip)
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
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result)
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
		shortDefinition="DNA analysis discrete sequence variation",
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
	 * Gets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation).
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
	 * Gets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public java.util.List<GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation> getDnaAnalysisDiscreteSequenceVariationElement() {  
		if (myDnaAnalysisDiscreteSequenceVariation == null) {
			myDnaAnalysisDiscreteSequenceVariation = new java.util.ArrayList<GeneticAnalysisDiscreteResultDnaAnalysisDiscreteSequenceVariation>();
		}
		return myDnaAnalysisDiscreteSequenceVariation;
	}


	/**
	 * Sets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation)
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
	 * Adds and returns a new value for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation)
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
	 * Gets the first repetition for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation),
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
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisDiscreteResult.dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation)
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
		shortDefinition="Identifier of the gene",
		formalDefinition="Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGeneIdentifier;
	
	@Child(name="genomicReferenceSequenceIdentifier", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Genomic reference sequence identifier",
		formalDefinition="Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGenomicReferenceSequenceIdentifier;
	
	@Child(name="transcriptReferenceIdentifier", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the transcript reference identifier",
		formalDefinition="Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myTranscriptReferenceIdentifier;
	
	@Child(name="alleleName", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Name of the allele",
		formalDefinition="Name of the allele"
	)
	private StringDt myAlleleName;
	
	@Child(name="dnaSequenceVariationIdentifier", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the DNA variation",
		formalDefinition="Identifier of the DNA variation"
	)
	private StringDt myDnaSequenceVariationIdentifier;
	
	@Child(name="dnaSequenceVariation", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="DNA variation represented in HGVS nomenclature",
		formalDefinition="DNA variation represented in HGVS nomenclature"
	)
	private StringDt myDnaSequenceVariation;
	
	@Child(name="dnaSequenceVariationType", type=CodingDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Type of the variation",
		formalDefinition="Type of the variation"
	)
	private CodingDt myDnaSequenceVariationType;
	
	@Child(name="aminoAcidChange", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Amino acid change represented in HGVS nomenclature",
		formalDefinition="Amino acid change represented in HGVS nomenclature"
	)
	private StringDt myAminoAcidChange;
	
	@Child(name="aminoAcidChangeType", type=CodingDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Type of the amino acid change",
		formalDefinition="Type of the amino acid change"
	)
	private CodingDt myAminoAcidChangeType;
	
	@Child(name="dnaRegionName", type=StringDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Human-readable name of the DNA region",
		formalDefinition="Human-readable name of the DNA region"
	)
	private StringDt myDnaRegionName;
	
	@Child(name="allellicState", type=CodingDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Allelic state",
		formalDefinition="Allelic state"
	)
	private CodingDt myAllellicState;
	
	@Child(name="genomicSourceClass", type=CodingDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="Class of the source of sample",
		formalDefinition="Class of the source of sample"
	)
	private CodingDt myGenomicSourceClass;
	
	@Child(name="dnaSequenceVariationDisplayName", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="Conventional display of the DNA region and its interpretation",
		formalDefinition="Conventional display of the DNA region and its interpretation"
	)
	private StringDt myDnaSequenceVariationDisplayName;
	
	@Child(name="geneticDiseaseSequenceVariationInterpretation", type=CodingDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="Interpretation of the genotype's effect on genetic disease",
		formalDefinition="Interpretation of the genotype's effect on genetic disease"
	)
	private CodingDt myGeneticDiseaseSequenceVariationInterpretation;
	
	@Child(name="drugMetabolismSequenceVariationInterpretatioin", type=CodingDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Interpretation of the genotype's effect on the drug's metabolic effect",
		formalDefinition="Interpretation of the genotype's effect on the drug's metabolic effect"
	)
	private CodingDt myDrugMetabolismSequenceVariationInterpretatioin;
	
	@Child(name="drugEfficacySequenceVariationInterpretation", type=CodingDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="Interpretation of the genotype's effect on the drug's efficacy",
		formalDefinition="Interpretation of the genotype's effect on the drug's efficacy"
	)
	private CodingDt myDrugEfficacySequenceVariationInterpretation;
	
	@Child(name="geneticVariantAssessment", type=CodingDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="Genotyping result on a known set of mutation",
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
	 * Gets the value(s) for <b>geneIdentifier</b> (Identifier of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGeneIdentifier() {  
		if (myGeneIdentifier == null) {
			myGeneIdentifier = new StringDt();
		}
		return myGeneIdentifier;
	}


	/**
	 * Gets the value(s) for <b>geneIdentifier</b> (Identifier of the gene).
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
	 * Sets the value(s) for <b>geneIdentifier</b> (Identifier of the gene)
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
	 * Sets the value for <b>geneIdentifier</b> (Identifier of the gene)
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
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the reference sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGenomicReferenceSequenceIdentifier() {  
		if (myGenomicReferenceSequenceIdentifier == null) {
			myGenomicReferenceSequenceIdentifier = new StringDt();
		}
		return myGenomicReferenceSequenceIdentifier;
	}


	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier).
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
	 * Sets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier)
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
	 * Sets the value for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier)
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
	 * Gets the value(s) for <b>transcriptReferenceIdentifier</b> (Identifier of the transcript reference identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference transcript represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getTranscriptReferenceIdentifier() {  
		if (myTranscriptReferenceIdentifier == null) {
			myTranscriptReferenceIdentifier = new StringDt();
		}
		return myTranscriptReferenceIdentifier;
	}


	/**
	 * Gets the value(s) for <b>transcriptReferenceIdentifier</b> (Identifier of the transcript reference identifier).
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
	 * Sets the value(s) for <b>transcriptReferenceIdentifier</b> (Identifier of the transcript reference identifier)
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
	 * Sets the value for <b>transcriptReferenceIdentifier</b> (Identifier of the transcript reference identifier)
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
	 * Gets the value(s) for <b>alleleName</b> (Name of the allele).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the allele
     * </p> 
	 */
	public StringDt getAlleleName() {  
		if (myAlleleName == null) {
			myAlleleName = new StringDt();
		}
		return myAlleleName;
	}


	/**
	 * Gets the value(s) for <b>alleleName</b> (Name of the allele).
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
	 * Sets the value(s) for <b>alleleName</b> (Name of the allele)
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
	 * Sets the value for <b>alleleName</b> (Name of the allele)
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
	 * Gets the value(s) for <b>dnaSequenceVariationIdentifier</b> (Identifier of the DNA variation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the DNA variation
     * </p> 
	 */
	public StringDt getDnaSequenceVariationIdentifier() {  
		if (myDnaSequenceVariationIdentifier == null) {
			myDnaSequenceVariationIdentifier = new StringDt();
		}
		return myDnaSequenceVariationIdentifier;
	}


	/**
	 * Gets the value(s) for <b>dnaSequenceVariationIdentifier</b> (Identifier of the DNA variation).
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
	 * Sets the value(s) for <b>dnaSequenceVariationIdentifier</b> (Identifier of the DNA variation)
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
	 * Sets the value for <b>dnaSequenceVariationIdentifier</b> (Identifier of the DNA variation)
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
	 * Gets the value(s) for <b>dnaSequenceVariation</b> (DNA variation represented in HGVS nomenclature).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA variation represented in HGVS nomenclature
     * </p> 
	 */
	public StringDt getDnaSequenceVariation() {  
		if (myDnaSequenceVariation == null) {
			myDnaSequenceVariation = new StringDt();
		}
		return myDnaSequenceVariation;
	}


	/**
	 * Gets the value(s) for <b>dnaSequenceVariation</b> (DNA variation represented in HGVS nomenclature).
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
	 * Sets the value(s) for <b>dnaSequenceVariation</b> (DNA variation represented in HGVS nomenclature)
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
	 * Sets the value for <b>dnaSequenceVariation</b> (DNA variation represented in HGVS nomenclature)
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
	 * Gets the value(s) for <b>dnaSequenceVariationType</b> (Type of the variation).
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
	 * Gets the value(s) for <b>dnaSequenceVariationType</b> (Type of the variation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the variation
     * </p> 
	 */
	public CodingDt getDnaSequenceVariationTypeElement() {  
		if (myDnaSequenceVariationType == null) {
			myDnaSequenceVariationType = new CodingDt();
		}
		return myDnaSequenceVariationType;
	}


	/**
	 * Sets the value(s) for <b>dnaSequenceVariationType</b> (Type of the variation)
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
	 * Gets the value(s) for <b>aminoAcidChange</b> (Amino acid change represented in HGVS nomenclature).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Amino acid change represented in HGVS nomenclature
     * </p> 
	 */
	public StringDt getAminoAcidChange() {  
		if (myAminoAcidChange == null) {
			myAminoAcidChange = new StringDt();
		}
		return myAminoAcidChange;
	}


	/**
	 * Gets the value(s) for <b>aminoAcidChange</b> (Amino acid change represented in HGVS nomenclature).
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
	 * Sets the value(s) for <b>aminoAcidChange</b> (Amino acid change represented in HGVS nomenclature)
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
	 * Sets the value for <b>aminoAcidChange</b> (Amino acid change represented in HGVS nomenclature)
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
	 * Gets the value(s) for <b>aminoAcidChangeType</b> (Type of the amino acid change).
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
	 * Gets the value(s) for <b>aminoAcidChangeType</b> (Type of the amino acid change).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the amino acid change
     * </p> 
	 */
	public CodingDt getAminoAcidChangeTypeElement() {  
		if (myAminoAcidChangeType == null) {
			myAminoAcidChangeType = new CodingDt();
		}
		return myAminoAcidChangeType;
	}


	/**
	 * Sets the value(s) for <b>aminoAcidChangeType</b> (Type of the amino acid change)
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
	 * Gets the value(s) for <b>dnaRegionName</b> (Human-readable name of the DNA region).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name of the DNA region
     * </p> 
	 */
	public StringDt getDnaRegionName() {  
		if (myDnaRegionName == null) {
			myDnaRegionName = new StringDt();
		}
		return myDnaRegionName;
	}


	/**
	 * Gets the value(s) for <b>dnaRegionName</b> (Human-readable name of the DNA region).
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
	 * Sets the value(s) for <b>dnaRegionName</b> (Human-readable name of the DNA region)
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
	 * Sets the value for <b>dnaRegionName</b> (Human-readable name of the DNA region)
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
	 * Gets the value(s) for <b>allellicState</b> (Allelic state).
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
	 * Gets the value(s) for <b>allellicState</b> (Allelic state).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allelic state
     * </p> 
	 */
	public CodingDt getAllellicStateElement() {  
		if (myAllellicState == null) {
			myAllellicState = new CodingDt();
		}
		return myAllellicState;
	}


	/**
	 * Sets the value(s) for <b>allellicState</b> (Allelic state)
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
	 * Gets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample).
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
	 * Gets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public CodingDt getGenomicSourceClassElement() {  
		if (myGenomicSourceClass == null) {
			myGenomicSourceClass = new CodingDt();
		}
		return myGenomicSourceClass;
	}


	/**
	 * Sets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample)
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
	 * Gets the value(s) for <b>dnaSequenceVariationDisplayName</b> (Conventional display of the DNA region and its interpretation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conventional display of the DNA region and its interpretation
     * </p> 
	 */
	public StringDt getDnaSequenceVariationDisplayName() {  
		if (myDnaSequenceVariationDisplayName == null) {
			myDnaSequenceVariationDisplayName = new StringDt();
		}
		return myDnaSequenceVariationDisplayName;
	}


	/**
	 * Gets the value(s) for <b>dnaSequenceVariationDisplayName</b> (Conventional display of the DNA region and its interpretation).
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
	 * Sets the value(s) for <b>dnaSequenceVariationDisplayName</b> (Conventional display of the DNA region and its interpretation)
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
	 * Sets the value for <b>dnaSequenceVariationDisplayName</b> (Conventional display of the DNA region and its interpretation)
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
	 * Gets the value(s) for <b>geneticDiseaseSequenceVariationInterpretation</b> (Interpretation of the genotype's effect on genetic disease).
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
	 * Gets the value(s) for <b>geneticDiseaseSequenceVariationInterpretation</b> (Interpretation of the genotype's effect on genetic disease).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on genetic disease
     * </p> 
	 */
	public CodingDt getGeneticDiseaseSequenceVariationInterpretationElement() {  
		if (myGeneticDiseaseSequenceVariationInterpretation == null) {
			myGeneticDiseaseSequenceVariationInterpretation = new CodingDt();
		}
		return myGeneticDiseaseSequenceVariationInterpretation;
	}


	/**
	 * Sets the value(s) for <b>geneticDiseaseSequenceVariationInterpretation</b> (Interpretation of the genotype's effect on genetic disease)
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
	 * Gets the value(s) for <b>drugMetabolismSequenceVariationInterpretatioin</b> (Interpretation of the genotype's effect on the drug's metabolic effect).
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
	 * Gets the value(s) for <b>drugMetabolismSequenceVariationInterpretatioin</b> (Interpretation of the genotype's effect on the drug's metabolic effect).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on the drug's metabolic effect
     * </p> 
	 */
	public CodingDt getDrugMetabolismSequenceVariationInterpretatioinElement() {  
		if (myDrugMetabolismSequenceVariationInterpretatioin == null) {
			myDrugMetabolismSequenceVariationInterpretatioin = new CodingDt();
		}
		return myDrugMetabolismSequenceVariationInterpretatioin;
	}


	/**
	 * Sets the value(s) for <b>drugMetabolismSequenceVariationInterpretatioin</b> (Interpretation of the genotype's effect on the drug's metabolic effect)
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
	 * Gets the value(s) for <b>drugEfficacySequenceVariationInterpretation</b> (Interpretation of the genotype's effect on the drug's efficacy).
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
	 * Gets the value(s) for <b>drugEfficacySequenceVariationInterpretation</b> (Interpretation of the genotype's effect on the drug's efficacy).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Interpretation of the genotype's effect on the drug's efficacy
     * </p> 
	 */
	public CodingDt getDrugEfficacySequenceVariationInterpretationElement() {  
		if (myDrugEfficacySequenceVariationInterpretation == null) {
			myDrugEfficacySequenceVariationInterpretation = new CodingDt();
		}
		return myDrugEfficacySequenceVariationInterpretation;
	}


	/**
	 * Sets the value(s) for <b>drugEfficacySequenceVariationInterpretation</b> (Interpretation of the genotype's effect on the drug's efficacy)
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
	 * Gets the value(s) for <b>geneticVariantAssessment</b> (Genotyping result on a known set of mutation).
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
	 * Gets the value(s) for <b>geneticVariantAssessment</b> (Genotyping result on a known set of mutation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotyping result on a known set of mutation
     * </p> 
	 */
	public CodingDt getGeneticVariantAssessmentElement() {  
		if (myGeneticVariantAssessment == null) {
			myGeneticVariantAssessment = new CodingDt();
		}
		return myGeneticVariantAssessment;
	}


	/**
	 * Sets the value(s) for <b>geneticVariantAssessment</b> (Genotyping result on a known set of mutation)
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

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
