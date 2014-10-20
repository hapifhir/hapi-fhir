















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
 * HAPI/FHIR <b>Query</b> Resource
 * (A description of a query with a set of parameters)
 *
 * <p>
 * <b>Definition:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Query">http://hl7.org/fhir/profiles/Query</a> 
 * </p>
 *
 */
@ResourceDef(name="Query", profile="http://hl7.org/fhir/profiles/Query", id="query")
public class Query extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Query.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Query.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Query.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>response</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Query.response.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="response", path="Query.response.identifier", description="", type="token"  )
	public static final String SP_RESPONSE = "response";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>response</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Query.response.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RESPONSE = new TokenClientParam(SP_RESPONSE);


	@Child(name="identifier", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Links query and its response(s)",
		formalDefinition=""
	)
	private UriDt myIdentifier;
	
	@Child(name="parameter", type=ExtensionDt.class, order=1, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Set of query parameters with values",
		formalDefinition=""
	)
	private java.util.List<ExtensionDt> myParameter;
	
	@Child(name="response", order=2, min=0, max=1)	
	@Description(
		shortDefinition="If this is a response to a query",
		formalDefinition=""
	)
	private Response myResponse;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myParameter,  myResponse);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myParameter, myResponse);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (Links query and its response(s)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new UriDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Links query and its response(s))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Query setIdentifier(UriDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Links query and its response(s))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Query setIdentifier( String theUri) {
		myIdentifier = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (Set of query parameters with values).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExtensionDt> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<ExtensionDt>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Set of query parameters with values)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Query setParameter(java.util.List<ExtensionDt> theValue) {
		myParameter = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>parameter</b> (Set of query parameters with values)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt addParameter() {
		ExtensionDt newType = new ExtensionDt();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (Set of query parameters with values),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>response</b> (If this is a response to a query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response getResponse() {  
		if (myResponse == null) {
			myResponse = new Response();
		}
		return myResponse;
	}

	/**
	 * Sets the value(s) for <b>response</b> (If this is a response to a query)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Query setResponse(Response theValue) {
		myResponse = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>Query.response</b> (If this is a response to a query)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Response extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Links response to source query",
		formalDefinition=""
	)
	private UriDt myIdentifier;
	
	@Child(name="outcome", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ok | limited | refused | error",
		formalDefinition="Outcome of processing the query"
	)
	private BoundCodeDt<QueryOutcomeEnum> myOutcome;
	
	@Child(name="total", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Total number of matching records",
		formalDefinition=""
	)
	private IntegerDt myTotal;
	
	@Child(name="parameter", type=ExtensionDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Parameters server used",
		formalDefinition=""
	)
	private java.util.List<ExtensionDt> myParameter;
	
	@Child(name="first", type=ExtensionDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="To get first page (if paged)",
		formalDefinition=""
	)
	private java.util.List<ExtensionDt> myFirst;
	
	@Child(name="previous", type=ExtensionDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="To get previous page (if paged)",
		formalDefinition=""
	)
	private java.util.List<ExtensionDt> myPrevious;
	
	@Child(name="next", type=ExtensionDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="To get next page (if paged)",
		formalDefinition=""
	)
	private java.util.List<ExtensionDt> myNext;
	
	@Child(name="last", type=ExtensionDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="To get last page (if paged)",
		formalDefinition=""
	)
	private java.util.List<ExtensionDt> myLast;
	
	@Child(name="reference", order=8, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Resources that are the results of the search",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myReference;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myOutcome,  myTotal,  myParameter,  myFirst,  myPrevious,  myNext,  myLast,  myReference);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myOutcome, myTotal, myParameter, myFirst, myPrevious, myNext, myLast, myReference);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (Links response to source query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new UriDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Links response to source query)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setIdentifier(UriDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Links response to source query)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setIdentifier( String theUri) {
		myIdentifier = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>outcome</b> (ok | limited | refused | error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Outcome of processing the query
     * </p> 
	 */
	public BoundCodeDt<QueryOutcomeEnum> getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new BoundCodeDt<QueryOutcomeEnum>(QueryOutcomeEnum.VALUESET_BINDER);
		}
		return myOutcome;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (ok | limited | refused | error)
	 *
     * <p>
     * <b>Definition:</b>
     * Outcome of processing the query
     * </p> 
	 */
	public Response setOutcome(BoundCodeDt<QueryOutcomeEnum> theValue) {
		myOutcome = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (ok | limited | refused | error)
	 *
     * <p>
     * <b>Definition:</b>
     * Outcome of processing the query
     * </p> 
	 */
	public Response setOutcome(QueryOutcomeEnum theValue) {
		getOutcome().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>total</b> (Total number of matching records).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getTotal() {  
		if (myTotal == null) {
			myTotal = new IntegerDt();
		}
		return myTotal;
	}

	/**
	 * Sets the value(s) for <b>total</b> (Total number of matching records)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setTotal(IntegerDt theValue) {
		myTotal = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>total</b> (Total number of matching records)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setTotal( int theInteger) {
		myTotal = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (Parameters server used).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExtensionDt> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<ExtensionDt>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Parameters server used)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setParameter(java.util.List<ExtensionDt> theValue) {
		myParameter = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>parameter</b> (Parameters server used)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt addParameter() {
		ExtensionDt newType = new ExtensionDt();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (Parameters server used),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>first</b> (To get first page (if paged)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExtensionDt> getFirst() {  
		if (myFirst == null) {
			myFirst = new java.util.ArrayList<ExtensionDt>();
		}
		return myFirst;
	}

	/**
	 * Sets the value(s) for <b>first</b> (To get first page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setFirst(java.util.List<ExtensionDt> theValue) {
		myFirst = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>first</b> (To get first page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt addFirst() {
		ExtensionDt newType = new ExtensionDt();
		getFirst().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>first</b> (To get first page (if paged)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt getFirstFirstRep() {
		if (getFirst().isEmpty()) {
			return addFirst();
		}
		return getFirst().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>previous</b> (To get previous page (if paged)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExtensionDt> getPrevious() {  
		if (myPrevious == null) {
			myPrevious = new java.util.ArrayList<ExtensionDt>();
		}
		return myPrevious;
	}

	/**
	 * Sets the value(s) for <b>previous</b> (To get previous page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setPrevious(java.util.List<ExtensionDt> theValue) {
		myPrevious = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>previous</b> (To get previous page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt addPrevious() {
		ExtensionDt newType = new ExtensionDt();
		getPrevious().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>previous</b> (To get previous page (if paged)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt getPreviousFirstRep() {
		if (getPrevious().isEmpty()) {
			return addPrevious();
		}
		return getPrevious().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>next</b> (To get next page (if paged)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExtensionDt> getNext() {  
		if (myNext == null) {
			myNext = new java.util.ArrayList<ExtensionDt>();
		}
		return myNext;
	}

	/**
	 * Sets the value(s) for <b>next</b> (To get next page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setNext(java.util.List<ExtensionDt> theValue) {
		myNext = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>next</b> (To get next page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt addNext() {
		ExtensionDt newType = new ExtensionDt();
		getNext().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>next</b> (To get next page (if paged)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt getNextFirstRep() {
		if (getNext().isEmpty()) {
			return addNext();
		}
		return getNext().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>last</b> (To get last page (if paged)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExtensionDt> getLast() {  
		if (myLast == null) {
			myLast = new java.util.ArrayList<ExtensionDt>();
		}
		return myLast;
	}

	/**
	 * Sets the value(s) for <b>last</b> (To get last page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setLast(java.util.List<ExtensionDt> theValue) {
		myLast = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>last</b> (To get last page (if paged))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt addLast() {
		ExtensionDt newType = new ExtensionDt();
		getLast().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>last</b> (To get last page (if paged)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExtensionDt getLastFirstRep() {
		if (getLast().isEmpty()) {
			return addLast();
		}
		return getLast().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>reference</b> (Resources that are the results of the search).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReference() {  
		if (myReference == null) {
			myReference = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Resources that are the results of the search)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Response setReference(java.util.List<ResourceReferenceDt> theValue) {
		myReference = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reference</b> (Resources that are the results of the search)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addReference() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReference().add(newType);
		return newType; 
	}
  

	}




    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.QUERY;
    }

}