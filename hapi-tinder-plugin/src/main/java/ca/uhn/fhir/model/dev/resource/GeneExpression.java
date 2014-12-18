















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
 * HAPI/FHIR <b>GeneExpression</b> Resource
 * ()
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


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneExpression.gene.coordinate</b>".
	 */
	public static final Include INCLUDE_GENE_COORDINATE = new Include("GeneExpression.gene.coordinate");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneExpression.gene.identifier</b>".
	 */
	public static final Include INCLUDE_GENE_IDENTIFIER = new Include("GeneExpression.gene.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneExpression.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("GeneExpression.subject");


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Subject described by the resource"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="gene", order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Gene of study"
	)
	private Gene myGene;
	
	@Child(name="microarray", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Microarray.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Microarray that studies the gene"
	)
	private java.util.List<ResourceReferenceDt> myMicroarray;
	
	@Child(name="rnaSeq", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>subject</b> ().
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
	 * Sets the value(s) for <b>subject</b> ()
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
	 * Gets the value(s) for <b>gene</b> ().
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
	 * Sets the value(s) for <b>gene</b> ()
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
	 * Gets the value(s) for <b>microarray</b> ().
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
	 * Sets the value(s) for <b>microarray</b> ()
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
	 * Adds and returns a new value for <b>microarray</b> ()
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
	 * Gets the value(s) for <b>rnaSeq</b> ().
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
	 * Sets the value(s) for <b>rnaSeq</b> ()
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
	 * Adds and returns a new value for <b>rnaSeq</b> ()
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
	 * Gets the first repetition for <b>rnaSeq</b> (),
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
	 * Block class for child element: <b>GeneExpression.gene</b> ()
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
		shortDefinition="",
		formalDefinition="Identifier of the gene"
	)
	private StringDt myIdentifier;
	
	@Child(name="coordinate", order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>identifier</b> ().
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
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public String getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
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
	 * Sets the value for <b>identifier</b> ()
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
	 * Gets the value(s) for <b>coordinate</b> ().
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
	 * Sets the value(s) for <b>coordinate</b> ()
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
	 * Block class for child element: <b>GeneExpression.gene.coordinate</b> ()
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
		shortDefinition="",
		formalDefinition="Chromosome"
	)
	private StringDt myChromosome;
	
	@Child(name="start", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Start position"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>chromosome</b> ().
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
	 * Gets the value(s) for <b>chromosome</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public String getChromosome() {  
		return getChromosomeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>chromosome</b> ()
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
	 * Sets the value for <b>chromosome</b> ()
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
	 * Gets the value(s) for <b>start</b> ().
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
	 * Gets the value(s) for <b>start</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public Integer getStart() {  
		return getStartElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>start</b> ()
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
	 * Sets the value for <b>start</b> ()
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
	 * Gets the value(s) for <b>end</b> ().
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
	 * Gets the value(s) for <b>end</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public Integer getEnd() {  
		return getEndElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>end</b> ()
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
	 * Sets the value for <b>end</b> ()
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
	 * Block class for child element: <b>GeneExpression.rnaSeq</b> ()
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
		ca.uhn.fhir.model.dev.resource.SequencingLab.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Input lab for the RNA-Seq"
	)
	private ResourceReferenceDt myInputLab;
	
	@Child(name="inputAnalysis", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.SequencingAnalysis.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Input analysis for the RNA-Seq"
	)
	private ResourceReferenceDt myInputAnalysis;
	
	@Child(name="expression", type=DecimalDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Expression level of the gene in RPKM"
	)
	private DecimalDt myExpression;
	
	@Child(name="isoform", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>inputLab</b> ().
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
	 * Sets the value(s) for <b>inputLab</b> ()
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
	 * Gets the value(s) for <b>inputAnalysis</b> ().
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
	 * Sets the value(s) for <b>inputAnalysis</b> ()
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
	 * Gets the value(s) for <b>expression</b> ().
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
	 * Gets the value(s) for <b>expression</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public BigDecimal getExpression() {  
		return getExpressionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>expression</b> ()
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
	 * Sets the value for <b>expression</b> ()
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
	 * Sets the value for <b>expression</b> ()
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
	 * Sets the value for <b>expression</b> ()
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
	 * Gets the value(s) for <b>isoform</b> ().
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
	 * Sets the value(s) for <b>isoform</b> ()
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
	 * Adds and returns a new value for <b>isoform</b> ()
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
	 * Gets the first repetition for <b>isoform</b> (),
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
	 * Block class for child element: <b>GeneExpression.rnaSeq.isoform</b> ()
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
		shortDefinition="",
		formalDefinition="Identifier of the isoform"
	)
	private StringDt myIdentity;
	
	@Child(name="expression", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>identity</b> ().
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
	 * Gets the value(s) for <b>identity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
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
     * Identifier of the isoform
     * </p> 
	 */
	public RnaSeqIsoform setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identity</b> ()
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
	 * Gets the value(s) for <b>expression</b> ().
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
	 * Gets the value(s) for <b>expression</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public BigDecimal getExpression() {  
		return getExpressionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>expression</b> ()
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
	 * Sets the value for <b>expression</b> ()
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
	 * Sets the value for <b>expression</b> ()
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
	 * Sets the value for <b>expression</b> ()
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

}
