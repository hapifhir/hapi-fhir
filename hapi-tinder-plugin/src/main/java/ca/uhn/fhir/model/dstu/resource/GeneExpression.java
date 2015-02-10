















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
 * HAPI/FHIR <b>GeneExpression</b> Resource
 * (Resource that records the patient's expression of a gene)
 *
 * <p>
 * <b>Definition:</b>
 * Resource that records the patient's expression of a gene
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/GeneExpression">http://hl7.org/fhir/profiles/GeneExpression</a> 
 * </p>
 *
 */
@ResourceDef(name="GeneExpression", profile="http://hl7.org/fhir/profiles/GeneExpression", id="geneexpression")
public class GeneExpression 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>subject being described by the resource</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneExpression.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="GeneExpression.subject", description="subject being described by the resource", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>subject being described by the resource</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneExpression.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneExpression.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("GeneExpression.subject");

	/**
	 * Search parameter constant for <b>gene</b>
	 * <p>
	 * Description: <b>Id of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="gene", path="GeneExpression.gene.identifier", description="Id of the gene", type="string"  )
	public static final String SP_GENE = "gene";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>gene</b>
	 * <p>
	 * Description: <b>Id of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.identifier</b><br/>
	 * </p>
	 */
	public static final StringClientParam GENE = new StringClientParam(SP_GENE);

	/**
	 * Search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.coordinate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="coordinate", path="GeneExpression.gene.coordinate", description="Coordinate of the gene", type="string"  )
	public static final String SP_COORDINATE = "coordinate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.coordinate</b><br/>
	 * </p>
	 */
	public static final StringClientParam COORDINATE = new StringClientParam(SP_COORDINATE);


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject described by the resource",
		formalDefinition="Subject described by the resource"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="gene", order=1, min=1, max=1)	
	@Description(
		shortDefinition="Gene of study",
		formalDefinition="Gene of study"
	)
	private Gene myGene;
	
	@Child(name="microarray", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Microarray.class	})
	@Description(
		shortDefinition="Microarray that studies the gene",
		formalDefinition="Microarray that studies the gene"
	)
	private java.util.List<ResourceReferenceDt> myMicroarray;
	
	@Child(name="rnaSeq", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="RNA-Seq that studies the gene",
		formalDefinition="RNA-Seq that studies the gene"
	)
	private java.util.List<RnaSeq> myRnaSeq;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myGene,  myMicroarray,  myRnaSeq);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myGene, myMicroarray, myRnaSeq);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject described by the resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by the resource
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Gets the value(s) for <b>subject</b> (Subject described by the resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by the resource
     * </p> 
	 */
	public ResourceReferenceDt getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (Subject described by the resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by the resource
     * </p> 
	 */
	public GeneExpression setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>gene</b> (Gene of study).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public Gene getGene() {  
		if (myGene == null) {
			myGene = new Gene();
		}
		return myGene;
	}


	/**
	 * Gets the value(s) for <b>gene</b> (Gene of study).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public Gene getGeneElement() {  
		if (myGene == null) {
			myGene = new Gene();
		}
		return myGene;
	}


	/**
	 * Sets the value(s) for <b>gene</b> (Gene of study)
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public GeneExpression setGene(Gene theValue) {
		myGene = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>microarray</b> (Microarray that studies the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getMicroarray() {  
		if (myMicroarray == null) {
			myMicroarray = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myMicroarray;
	}


	/**
	 * Gets the value(s) for <b>microarray</b> (Microarray that studies the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getMicroarrayElement() {  
		if (myMicroarray == null) {
			myMicroarray = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myMicroarray;
	}


	/**
	 * Sets the value(s) for <b>microarray</b> (Microarray that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public GeneExpression setMicroarray(java.util.List<ResourceReferenceDt> theValue) {
		myMicroarray = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>microarray</b> (Microarray that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public ResourceReferenceDt addMicroarray() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getMicroarray().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>rnaSeq</b> (RNA-Seq that studies the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public java.util.List<RnaSeq> getRnaSeq() {  
		if (myRnaSeq == null) {
			myRnaSeq = new java.util.ArrayList<RnaSeq>();
		}
		return myRnaSeq;
	}


	/**
	 * Gets the value(s) for <b>rnaSeq</b> (RNA-Seq that studies the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public java.util.List<RnaSeq> getRnaSeqElement() {  
		if (myRnaSeq == null) {
			myRnaSeq = new java.util.ArrayList<RnaSeq>();
		}
		return myRnaSeq;
	}


	/**
	 * Sets the value(s) for <b>rnaSeq</b> (RNA-Seq that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public GeneExpression setRnaSeq(java.util.List<RnaSeq> theValue) {
		myRnaSeq = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>rnaSeq</b> (RNA-Seq that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public RnaSeq addRnaSeq() {
		RnaSeq newType = new RnaSeq();
		getRnaSeq().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>rnaSeq</b> (RNA-Seq that studies the gene),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public RnaSeq getRnaSeqFirstRep() {
		if (getRnaSeq().isEmpty()) {
			return addRnaSeq();
		}
		return getRnaSeq().get(0); 
	}
  
	/**
	 * Block class for child element: <b>GeneExpression.gene</b> (Gene of study)
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	@Block()	
	public static class Gene 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the gene",
		formalDefinition="Identifier of the gene"
	)
	private StringDt myIdentifier;
	
	@Child(name="coordinate", order=1, min=0, max=1)	
	@Description(
		shortDefinition="Coordinate of the gene",
		formalDefinition="Coordinate of the gene"
	)
	private GeneCoordinate myCoordinate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCoordinate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCoordinate);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public StringDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public Gene setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Identifier of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public Gene setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>coordinate</b> (Coordinate of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public GeneCoordinate getCoordinate() {  
		if (myCoordinate == null) {
			myCoordinate = new GeneCoordinate();
		}
		return myCoordinate;
	}


	/**
	 * Gets the value(s) for <b>coordinate</b> (Coordinate of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public GeneCoordinate getCoordinateElement() {  
		if (myCoordinate == null) {
			myCoordinate = new GeneCoordinate();
		}
		return myCoordinate;
	}


	/**
	 * Sets the value(s) for <b>coordinate</b> (Coordinate of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public Gene setCoordinate(GeneCoordinate theValue) {
		myCoordinate = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>GeneExpression.gene.coordinate</b> (Coordinate of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	@Block()	
	public static class GeneCoordinate 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="chromosome", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Chromosome",
		formalDefinition="Chromosome"
	)
	private StringDt myChromosome;
	
	@Child(name="start", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Start position",
		formalDefinition="Start position"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="End position",
		formalDefinition="End position"
	)
	private IntegerDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myChromosome,  myStart,  myEnd);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myChromosome, myStart, myEnd);
	}

	/**
	 * Gets the value(s) for <b>chromosome</b> (Chromosome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public StringDt getChromosome() {  
		if (myChromosome == null) {
			myChromosome = new StringDt();
		}
		return myChromosome;
	}


	/**
	 * Gets the value(s) for <b>chromosome</b> (Chromosome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public StringDt getChromosomeElement() {  
		if (myChromosome == null) {
			myChromosome = new StringDt();
		}
		return myChromosome;
	}


	/**
	 * Sets the value(s) for <b>chromosome</b> (Chromosome)
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public GeneCoordinate setChromosome(StringDt theValue) {
		myChromosome = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>chromosome</b> (Chromosome)
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public GeneCoordinate setChromosome( String theString) {
		myChromosome = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}


	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStartElement() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}


	/**
	 * Sets the value(s) for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public GeneCoordinate setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public GeneCoordinate setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}


	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEndElement() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}


	/**
	 * Sets the value(s) for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public GeneCoordinate setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public GeneCoordinate setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>GeneExpression.rnaSeq</b> (RNA-Seq that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	@Block()	
	public static class RnaSeq 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="inputLab", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.SequencingLab.class	})
	@Description(
		shortDefinition="Input lab for the RNA-Seq",
		formalDefinition="Input lab for the RNA-Seq"
	)
	private ResourceReferenceDt myInputLab;
	
	@Child(name="inputAnalysis", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.SequencingAnalysis.class	})
	@Description(
		shortDefinition="Input analysis for the RNA-Seq",
		formalDefinition="Input analysis for the RNA-Seq"
	)
	private ResourceReferenceDt myInputAnalysis;
	
	@Child(name="expression", type=DecimalDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Expression level of the gene in RPKM",
		formalDefinition="Expression level of the gene in RPKM"
	)
	private DecimalDt myExpression;
	
	@Child(name="isoform", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Isoform of the gene",
		formalDefinition="Isoform of the gene"
	)
	private java.util.List<RnaSeqIsoform> myIsoform;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myInputLab,  myInputAnalysis,  myExpression,  myIsoform);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myInputLab, myInputAnalysis, myExpression, myIsoform);
	}

	/**
	 * Gets the value(s) for <b>inputLab</b> (Input lab for the RNA-Seq).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Input lab for the RNA-Seq
     * </p> 
	 */
	public ResourceReferenceDt getInputLab() {  
		if (myInputLab == null) {
			myInputLab = new ResourceReferenceDt();
		}
		return myInputLab;
	}


	/**
	 * Gets the value(s) for <b>inputLab</b> (Input lab for the RNA-Seq).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Input lab for the RNA-Seq
     * </p> 
	 */
	public ResourceReferenceDt getInputLabElement() {  
		if (myInputLab == null) {
			myInputLab = new ResourceReferenceDt();
		}
		return myInputLab;
	}


	/**
	 * Sets the value(s) for <b>inputLab</b> (Input lab for the RNA-Seq)
	 *
     * <p>
     * <b>Definition:</b>
     * Input lab for the RNA-Seq
     * </p> 
	 */
	public RnaSeq setInputLab(ResourceReferenceDt theValue) {
		myInputLab = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>inputAnalysis</b> (Input analysis for the RNA-Seq).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Input analysis for the RNA-Seq
     * </p> 
	 */
	public ResourceReferenceDt getInputAnalysis() {  
		if (myInputAnalysis == null) {
			myInputAnalysis = new ResourceReferenceDt();
		}
		return myInputAnalysis;
	}


	/**
	 * Gets the value(s) for <b>inputAnalysis</b> (Input analysis for the RNA-Seq).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Input analysis for the RNA-Seq
     * </p> 
	 */
	public ResourceReferenceDt getInputAnalysisElement() {  
		if (myInputAnalysis == null) {
			myInputAnalysis = new ResourceReferenceDt();
		}
		return myInputAnalysis;
	}


	/**
	 * Sets the value(s) for <b>inputAnalysis</b> (Input analysis for the RNA-Seq)
	 *
     * <p>
     * <b>Definition:</b>
     * Input analysis for the RNA-Seq
     * </p> 
	 */
	public RnaSeq setInputAnalysis(ResourceReferenceDt theValue) {
		myInputAnalysis = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>expression</b> (Expression level of the gene in RPKM).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public DecimalDt getExpression() {  
		if (myExpression == null) {
			myExpression = new DecimalDt();
		}
		return myExpression;
	}


	/**
	 * Gets the value(s) for <b>expression</b> (Expression level of the gene in RPKM).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public DecimalDt getExpressionElement() {  
		if (myExpression == null) {
			myExpression = new DecimalDt();
		}
		return myExpression;
	}


	/**
	 * Sets the value(s) for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression(DecimalDt theValue) {
		myExpression = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression( long theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression( double theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression( java.math.BigDecimal theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isoform</b> (Isoform of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public java.util.List<RnaSeqIsoform> getIsoform() {  
		if (myIsoform == null) {
			myIsoform = new java.util.ArrayList<RnaSeqIsoform>();
		}
		return myIsoform;
	}


	/**
	 * Gets the value(s) for <b>isoform</b> (Isoform of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public java.util.List<RnaSeqIsoform> getIsoformElement() {  
		if (myIsoform == null) {
			myIsoform = new java.util.ArrayList<RnaSeqIsoform>();
		}
		return myIsoform;
	}


	/**
	 * Sets the value(s) for <b>isoform</b> (Isoform of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public RnaSeq setIsoform(java.util.List<RnaSeqIsoform> theValue) {
		myIsoform = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>isoform</b> (Isoform of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public RnaSeqIsoform addIsoform() {
		RnaSeqIsoform newType = new RnaSeqIsoform();
		getIsoform().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>isoform</b> (Isoform of the gene),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public RnaSeqIsoform getIsoformFirstRep() {
		if (getIsoform().isEmpty()) {
			return addIsoform();
		}
		return getIsoform().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>GeneExpression.rnaSeq.isoform</b> (Isoform of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	@Block()	
	public static class RnaSeqIsoform 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Identifier of the isoform",
		formalDefinition="Identifier of the isoform"
	)
	private StringDt myIdentity;
	
	@Child(name="expression", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Expression level of the isoform in RPKM",
		formalDefinition="Expression level of the isoform in RPKM"
	)
	private DecimalDt myExpression;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myExpression);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myExpression);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (Identifier of the isoform).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}


	/**
	 * Gets the value(s) for <b>identity</b> (Identifier of the isoform).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public StringDt getIdentityElement() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}


	/**
	 * Sets the value(s) for <b>identity</b> (Identifier of the isoform)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public RnaSeqIsoform setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Identifier of the isoform)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public RnaSeqIsoform setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expression</b> (Expression level of the isoform in RPKM).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public DecimalDt getExpression() {  
		if (myExpression == null) {
			myExpression = new DecimalDt();
		}
		return myExpression;
	}


	/**
	 * Gets the value(s) for <b>expression</b> (Expression level of the isoform in RPKM).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public DecimalDt getExpressionElement() {  
		if (myExpression == null) {
			myExpression = new DecimalDt();
		}
		return myExpression;
	}


	/**
	 * Sets the value(s) for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression(DecimalDt theValue) {
		myExpression = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression( long theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression( double theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression( java.math.BigDecimal theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

 

	}





    @Override
    public String getResourceName() {
        return "GeneExpression";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
