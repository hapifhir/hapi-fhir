















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
 * HAPI/FHIR <b>FamilyHistory</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Significant health events and conditions for people related to the subject relevant in the context of care for the subject
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/FamilyHistory">http://hl7.org/fhir/profiles/FamilyHistory</a> 
 * </p>
 *
 */
@ResourceDef(name="FamilyHistory", profile="http://hl7.org/fhir/profiles/FamilyHistory", id="familyhistory")
public class FamilyHistory 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>FamilyHistory.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="FamilyHistory.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>FamilyHistory.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a subject to list family history items for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>FamilyHistory.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="FamilyHistory.patient", description="The identity of a subject to list family history items for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a subject to list family history items for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>FamilyHistory.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>FamilyHistory.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("FamilyHistory.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>FamilyHistory.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("FamilyHistory.patient");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="patient", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person who this history concerns"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="date", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date (and possibly time) when the family history was taken"
	)
	private DateTimeDt myDate;
	
	@Child(name="note", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Conveys information about family history not specific to individual relations."
	)
	private StringDt myNote;
	
	@Child(name="relation", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The related person. Each FamilyHistory resource contains the entire family history for a single person."
	)
	private java.util.List<Relation> myRelation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPatient,  myDate,  myNote,  myRelation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPatient, myDate, myNote, myRelation);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public FamilyHistory setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this history concerns
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
     * The person who this history concerns
     * </p> 
	 */
	public FamilyHistory setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the family history was taken
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
     * The date (and possibly time) when the family history was taken
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
     * The date (and possibly time) when the family history was taken
     * </p> 
	 */
	public FamilyHistory setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the family history was taken
     * </p> 
	 */
	public FamilyHistory setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the family history was taken
     * </p> 
	 */
	public FamilyHistory setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>note</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public StringDt getNoteElement() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	
	/**
	 * Gets the value(s) for <b>note</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public String getNote() {  
		return getNoteElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>note</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public FamilyHistory setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>note</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public FamilyHistory setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public java.util.List<Relation> getRelation() {  
		if (myRelation == null) {
			myRelation = new java.util.ArrayList<Relation>();
		}
		return myRelation;
	}

	/**
	 * Sets the value(s) for <b>relation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public FamilyHistory setRelation(java.util.List<Relation> theValue) {
		myRelation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>relation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public Relation addRelation() {
		Relation newType = new Relation();
		getRelation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public Relation getRelationFirstRep() {
		if (getRelation().isEmpty()) {
			return addRelation();
		}
		return getRelation().get(0); 
	}
  
	/**
	 * Block class for child element: <b>FamilyHistory.relation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	@Block()	
	public static class Relation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\""
	)
	private StringDt myName;
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="FamilialRelationship",
		formalDefinition="The type of relationship this person has to the patient (father, mother, brother etc.)"
	)
	private CodeableConceptDt myRelationship;
	
	@Child(name="born", order=2, min=0, max=1, type={
		PeriodDt.class, 		DateDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The actual or approximate date of birth of the relative"
	)
	private IDatatype myBorn;
	
	@Child(name="age", order=3, min=0, max=1, type={
		AgeDt.class, 		RangeDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The actual or approximate age of the relative at the time the family history is recorded"
	)
	private IDatatype myAge;
	
	@Child(name="deceased", order=4, min=0, max=1, type={
		BooleanDt.class, 		AgeDt.class, 		RangeDt.class, 		DateDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set."
	)
	private IDatatype myDeceased;
	
	@Child(name="note", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible."
	)
	private StringDt myNote;
	
	@Child(name="condition", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition."
	)
	private java.util.List<RelationCondition> myCondition;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myRelationship,  myBorn,  myAge,  myDeceased,  myNote,  myCondition);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myRelationship, myBorn, myAge, myDeceased, myNote, myCondition);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
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
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
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
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
     * </p> 
	 */
	public Relation setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
     * </p> 
	 */
	public Relation setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relationship</b> (FamilialRelationship).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public CodeableConceptDt getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new CodeableConceptDt();
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (FamilialRelationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public Relation setRelationship(CodeableConceptDt theValue) {
		myRelationship = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>born[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public IDatatype getBorn() {  
		return myBorn;
	}

	/**
	 * Sets the value(s) for <b>born[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public Relation setBorn(IDatatype theValue) {
		myBorn = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>age[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate age of the relative at the time the family history is recorded
     * </p> 
	 */
	public IDatatype getAge() {  
		return myAge;
	}

	/**
	 * Sets the value(s) for <b>age[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate age of the relative at the time the family history is recorded
     * </p> 
	 */
	public Relation setAge(IDatatype theValue) {
		myAge = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>deceased[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public IDatatype getDeceased() {  
		return myDeceased;
	}

	/**
	 * Sets the value(s) for <b>deceased[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public Relation setDeceased(IDatatype theValue) {
		myDeceased = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>note</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public StringDt getNoteElement() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	
	/**
	 * Gets the value(s) for <b>note</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public String getNote() {  
		return getNoteElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>note</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public Relation setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>note</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public Relation setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>condition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public java.util.List<RelationCondition> getCondition() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<RelationCondition>();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public Relation setCondition(java.util.List<RelationCondition> theValue) {
		myCondition = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>condition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public RelationCondition addCondition() {
		RelationCondition newType = new RelationCondition();
		getCondition().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>condition</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public RelationCondition getConditionFirstRep() {
		if (getCondition().isEmpty()) {
			return addCondition();
		}
		return getCondition().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>FamilyHistory.relation.condition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	@Block()	
	public static class RelationCondition 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system"
	)
	private CodeableConceptDt myType;
	
	@Child(name="outcome", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ConditionOutcome",
		formalDefinition="Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation."
	)
	private CodeableConceptDt myOutcome;
	
	@Child(name="onset", order=2, min=0, max=1, type={
		AgeDt.class, 		RangeDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence."
	)
	private IDatatype myOnset;
	
	@Child(name="note", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An area where general notes can be placed about this specific condition."
	)
	private StringDt myNote;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myOutcome,  myOnset,  myNote);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myOutcome, myOnset, myNote);
	}

	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
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
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
     * </p> 
	 */
	public RelationCondition setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>outcome</b> (ConditionOutcome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public CodeableConceptDt getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new CodeableConceptDt();
		}
		return myOutcome;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (ConditionOutcome)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public RelationCondition setOutcome(CodeableConceptDt theValue) {
		myOutcome = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>onset[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public IDatatype getOnset() {  
		return myOnset;
	}

	/**
	 * Sets the value(s) for <b>onset[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public RelationCondition setOnset(IDatatype theValue) {
		myOnset = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>note</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public StringDt getNoteElement() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	
	/**
	 * Gets the value(s) for <b>note</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public String getNote() {  
		return getNoteElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>note</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public RelationCondition setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>note</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public RelationCondition setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 

	}





    @Override
    public String getResourceName() {
        return "FamilyHistory";
    }

}
