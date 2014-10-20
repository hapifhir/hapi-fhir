















package ca.uhn.fhir.model.dstu.resource;


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;

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
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
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
 * HAPI/FHIR <b>OperationOutcome</b> Resource
 * (Information about the success/failure of an action)
 *
 * <p>
 * <b>Definition:</b>
 * A collection of error, warning or information messages that result from a system action
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/OperationOutcome">http://hl7.org/fhir/profiles/OperationOutcome</a> 
 * </p>
 *
 */
@ResourceDef(name="OperationOutcome", profile="http://hl7.org/fhir/profiles/OperationOutcome", id="operationoutcome")
public class OperationOutcome extends BaseResource implements IResource {


	@Child(name="issue", order=0, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A single issue associated with the action",
		formalDefinition="An error, warning or information message that results from a system action"
	)
	private java.util.List<Issue> myIssue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIssue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIssue);
	}
	

	/**
	 * Gets the value(s) for <b>issue</b> (A single issue associated with the action).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public java.util.List<Issue> getIssue() {  
		if (myIssue == null) {
			myIssue = new java.util.ArrayList<Issue>();
		}
		return myIssue;
	}

	/**
	 * Sets the value(s) for <b>issue</b> (A single issue associated with the action)
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public OperationOutcome setIssue(java.util.List<Issue> theValue) {
		myIssue = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>issue</b> (A single issue associated with the action)
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public Issue addIssue() {
		Issue newType = new Issue();
		getIssue().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>issue</b> (A single issue associated with the action),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public Issue getIssueFirstRep() {
		if (getIssue().isEmpty()) {
			return addIssue();
		}
		return getIssue().get(0); 
	}
  
	/**
	 * Block class for child element: <b>OperationOutcome.issue</b> (A single issue associated with the action)
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	@Block()	
	public static class Issue extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="severity", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="fatal | error | warning | information",
		formalDefinition="Indicates whether the issue indicates a variation from successful processing"
	)
	private BoundCodeDt<IssueSeverityEnum> mySeverity;
	
	@Child(name="type", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Error or warning code",
		formalDefinition="A code indicating the type of error, warning or information message."
	)
	private CodingDt myType;
	
	@Child(name="details", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional description of the issue",
		formalDefinition="Additional description of the issue"
	)
	private StringDt myDetails;
	
	@Child(name="location", type=StringDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="XPath of element(s) related to issue",
		formalDefinition="A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised."
	)
	private java.util.List<StringDt> myLocation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySeverity,  myType,  myDetails,  myLocation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySeverity, myType, myDetails, myLocation);
	}
	

	/**
	 * Gets the value(s) for <b>severity</b> (fatal | error | warning | information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the issue indicates a variation from successful processing
     * </p> 
	 */
	public BoundCodeDt<IssueSeverityEnum> getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<IssueSeverityEnum>(IssueSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (fatal | error | warning | information)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the issue indicates a variation from successful processing
     * </p> 
	 */
	public Issue setSeverity(BoundCodeDt<IssueSeverityEnum> theValue) {
		mySeverity = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (fatal | error | warning | information)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the issue indicates a variation from successful processing
     * </p> 
	 */
	public Issue setSeverity(IssueSeverityEnum theValue) {
		getSeverity().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Error or warning code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating the type of error, warning or information message.
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Error or warning code)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating the type of error, warning or information message.
     * </p> 
	 */
	public Issue setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>details</b> (Additional description of the issue).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description of the issue
     * </p> 
	 */
	public StringDt getDetails() {  
		if (myDetails == null) {
			myDetails = new StringDt();
		}
		return myDetails;
	}

	/**
	 * Sets the value(s) for <b>details</b> (Additional description of the issue)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description of the issue
     * </p> 
	 */
	public Issue setDetails(StringDt theValue) {
		myDetails = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>details</b> (Additional description of the issue)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description of the issue
     * </p> 
	 */
	public Issue setDetails( String theString) {
		myDetails = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>location</b> (XPath of element(s) related to issue).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public java.util.List<StringDt> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<StringDt>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (XPath of element(s) related to issue)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public Issue setLocation(java.util.List<StringDt> theValue) {
		myLocation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>location</b> (XPath of element(s) related to issue)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public StringDt addLocation() {
		StringDt newType = new StringDt();
		getLocation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>location</b> (XPath of element(s) related to issue),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public StringDt getLocationFirstRep() {
		if (getLocation().isEmpty()) {
			return addLocation();
		}
		return getLocation().get(0); 
	}
 	/**
	 * Adds a new value for <b>location</b> (XPath of element(s) related to issue)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Issue addLocation( String theString) {
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<StringDt>();
		}
		myLocation.add(new StringDt(theString));
		return this; 
	}

 

	}




    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.OPERATIONOUTCOME;
    }

}