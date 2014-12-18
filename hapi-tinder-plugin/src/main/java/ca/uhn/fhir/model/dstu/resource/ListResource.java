















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
 * HAPI/FHIR <b>List</b> Resource
 * (Information summarized from a list of other resources)
 *
 * <p>
 * <b>Definition:</b>
 * A set of information summarized from a list of other resources
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/List">http://hl7.org/fhir/profiles/List</a> 
 * </p>
 *
 */
@ResourceDef(name="List", profile="http://hl7.org/fhir/profiles/List", id="list")
public class ListResource 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.source</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="List.source", description="", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.source</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.source</b>".
	 */
	public static final Include INCLUDE_SOURCE = new Include("List.source");

	/**
	 * Search parameter constant for <b>item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.entry.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item", path="List.entry.item", description="", type="reference"  )
	public static final String SP_ITEM = "item";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.entry.item</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ITEM = new ReferenceClientParam(SP_ITEM);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.entry.item</b>".
	 */
	public static final Include INCLUDE_ENTRY_ITEM = new Include("List.entry.item");

	/**
	 * Search parameter constant for <b>empty-reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.emptyReason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="empty-reason", path="List.emptyReason", description="", type="token"  )
	public static final String SP_EMPTY_REASON = "empty-reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>empty-reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.emptyReason</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EMPTY_REASON = new TokenClientParam(SP_EMPTY_REASON);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>List.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="List.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>List.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="List.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="List.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>List.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("List.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Business identifier",
		formalDefinition="Identifier for the List assigned for business purposes outside the context of FHIR."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="What the purpose of this list is",
		formalDefinition="This code defines the purpose of the list - why it was created"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="If all resources have the same subject",
		formalDefinition="The common subject (or patient) of the resources that are in the list, if there is one"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="source", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Who and/or what defined the list contents",
		formalDefinition="The entity responsible for deciding what the contents of the list were"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="date", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="When the list was prepared",
		formalDefinition="The date that the list was prepared"
	)
	private DateTimeDt myDate;
	
	@Child(name="ordered", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Whether items in the list have a meaningful order",
		formalDefinition="Whether items in the list have a meaningful order"
	)
	private BooleanDt myOrdered;
	
	@Child(name="mode", type=CodeDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="working | snapshot | changes",
		formalDefinition="How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted"
	)
	private BoundCodeDt<ListModeEnum> myMode;
	
	@Child(name="entry", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Entries in the list",
		formalDefinition="Entries in this list"
	)
	private java.util.List<Entry> myEntry;
	
	@Child(name="emptyReason", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Why list is empty",
		formalDefinition="If the list is empty, why the list is empty"
	)
	private CodeableConceptDt myEmptyReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  mySubject,  mySource,  myDate,  myOrdered,  myMode,  myEntry,  myEmptyReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, mySubject, mySource, myDate, myOrdered, myMode, myEntry, myEmptyReason);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public ListResource setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Business identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ListResource addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ListResource addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (What the purpose of this list is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}


	/**
	 * Gets the value(s) for <b>code</b> (What the purpose of this list is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public CodeableConceptDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}


	/**
	 * Sets the value(s) for <b>code</b> (What the purpose of this list is)
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public ListResource setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (If all resources have the same subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common subject (or patient) of the resources that are in the list, if there is one
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Gets the value(s) for <b>subject</b> (If all resources have the same subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common subject (or patient) of the resources that are in the list, if there is one
     * </p> 
	 */
	public ResourceReferenceDt getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (If all resources have the same subject)
	 *
     * <p>
     * <b>Definition:</b>
     * The common subject (or patient) of the resources that are in the list, if there is one
     * </p> 
	 */
	public ListResource setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>source</b> (Who and/or what defined the list contents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}


	/**
	 * Gets the value(s) for <b>source</b> (Who and/or what defined the list contents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ResourceReferenceDt getSourceElement() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}


	/**
	 * Sets the value(s) for <b>source</b> (Who and/or what defined the list contents)
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ListResource setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (When the list was prepared).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}


	/**
	 * Gets the value(s) for <b>date</b> (When the list was prepared).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}


	/**
	 * Sets the value(s) for <b>date</b> (When the list was prepared)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When the list was prepared)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the list was prepared)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>ordered</b> (Whether items in the list have a meaningful order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public BooleanDt getOrdered() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}


	/**
	 * Gets the value(s) for <b>ordered</b> (Whether items in the list have a meaningful order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public BooleanDt getOrderedElement() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}


	/**
	 * Sets the value(s) for <b>ordered</b> (Whether items in the list have a meaningful order)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public ListResource setOrdered(BooleanDt theValue) {
		myOrdered = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>ordered</b> (Whether items in the list have a meaningful order)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public ListResource setOrdered( boolean theBoolean) {
		myOrdered = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mode</b> (working | snapshot | changes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public BoundCodeDt<ListModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ListModeEnum>(ListModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}


	/**
	 * Gets the value(s) for <b>mode</b> (working | snapshot | changes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public BoundCodeDt<ListModeEnum> getModeElement() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ListModeEnum>(ListModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}


	/**
	 * Sets the value(s) for <b>mode</b> (working | snapshot | changes)
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public ListResource setMode(BoundCodeDt<ListModeEnum> theValue) {
		myMode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (working | snapshot | changes)
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public ListResource setMode(ListModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>entry</b> (Entries in the list).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public java.util.List<Entry> getEntry() {  
		if (myEntry == null) {
			myEntry = new java.util.ArrayList<Entry>();
		}
		return myEntry;
	}


	/**
	 * Gets the value(s) for <b>entry</b> (Entries in the list).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public java.util.List<Entry> getEntryElement() {  
		if (myEntry == null) {
			myEntry = new java.util.ArrayList<Entry>();
		}
		return myEntry;
	}


	/**
	 * Sets the value(s) for <b>entry</b> (Entries in the list)
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public ListResource setEntry(java.util.List<Entry> theValue) {
		myEntry = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>entry</b> (Entries in the list)
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public Entry addEntry() {
		Entry newType = new Entry();
		getEntry().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>entry</b> (Entries in the list),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public Entry getEntryFirstRep() {
		if (getEntry().isEmpty()) {
			return addEntry();
		}
		return getEntry().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>emptyReason</b> (Why list is empty).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public CodeableConceptDt getEmptyReason() {  
		if (myEmptyReason == null) {
			myEmptyReason = new CodeableConceptDt();
		}
		return myEmptyReason;
	}


	/**
	 * Gets the value(s) for <b>emptyReason</b> (Why list is empty).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public CodeableConceptDt getEmptyReasonElement() {  
		if (myEmptyReason == null) {
			myEmptyReason = new CodeableConceptDt();
		}
		return myEmptyReason;
	}


	/**
	 * Sets the value(s) for <b>emptyReason</b> (Why list is empty)
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public ListResource setEmptyReason(CodeableConceptDt theValue) {
		myEmptyReason = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>List.entry</b> (Entries in the list)
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	@Block()	
	public static class Entry 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="flag", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Workflow information about this item",
		formalDefinition="The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list"
	)
	private java.util.List<CodeableConceptDt> myFlag;
	
	@Child(name="deleted", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="If this item is actually marked as deleted",
		formalDefinition="True if this item is marked as deleted in the list."
	)
	private BooleanDt myDeleted;
	
	@Child(name="date", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="When item added to list",
		formalDefinition="When this item was added to the list"
	)
	private DateTimeDt myDate;
	
	@Child(name="item", order=3, min=1, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="Actual entry",
		formalDefinition="A reference to the actual resource from which data was derived"
	)
	private ResourceReferenceDt myItem;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myFlag,  myDeleted,  myDate,  myItem);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myFlag, myDeleted, myDate, myItem);
	}

	/**
	 * Gets the value(s) for <b>flag</b> (Workflow information about this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getFlag() {  
		if (myFlag == null) {
			myFlag = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myFlag;
	}


	/**
	 * Gets the value(s) for <b>flag</b> (Workflow information about this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getFlagElement() {  
		if (myFlag == null) {
			myFlag = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myFlag;
	}


	/**
	 * Sets the value(s) for <b>flag</b> (Workflow information about this item)
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public Entry setFlag(java.util.List<CodeableConceptDt> theValue) {
		myFlag = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>flag</b> (Workflow information about this item)
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public CodeableConceptDt addFlag() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getFlag().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>flag</b> (Workflow information about this item),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public CodeableConceptDt getFlagFirstRep() {
		if (getFlag().isEmpty()) {
			return addFlag();
		}
		return getFlag().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>deleted</b> (If this item is actually marked as deleted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public BooleanDt getDeleted() {  
		if (myDeleted == null) {
			myDeleted = new BooleanDt();
		}
		return myDeleted;
	}


	/**
	 * Gets the value(s) for <b>deleted</b> (If this item is actually marked as deleted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public BooleanDt getDeletedElement() {  
		if (myDeleted == null) {
			myDeleted = new BooleanDt();
		}
		return myDeleted;
	}


	/**
	 * Sets the value(s) for <b>deleted</b> (If this item is actually marked as deleted)
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Entry setDeleted(BooleanDt theValue) {
		myDeleted = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>deleted</b> (If this item is actually marked as deleted)
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Entry setDeleted( boolean theBoolean) {
		myDeleted = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (When item added to list).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}


	/**
	 * Gets the value(s) for <b>date</b> (When item added to list).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}


	/**
	 * Sets the value(s) for <b>date</b> (When item added to list)
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When item added to list)
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When item added to list)
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>item</b> (Actual entry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the actual resource from which data was derived
     * </p> 
	 */
	public ResourceReferenceDt getItem() {  
		if (myItem == null) {
			myItem = new ResourceReferenceDt();
		}
		return myItem;
	}


	/**
	 * Gets the value(s) for <b>item</b> (Actual entry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the actual resource from which data was derived
     * </p> 
	 */
	public ResourceReferenceDt getItemElement() {  
		if (myItem == null) {
			myItem = new ResourceReferenceDt();
		}
		return myItem;
	}


	/**
	 * Sets the value(s) for <b>item</b> (Actual entry)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the actual resource from which data was derived
     * </p> 
	 */
	public Entry setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
	}

  

	}




    @Override
    public String getResourceName() {
        return "List";
    }

}
