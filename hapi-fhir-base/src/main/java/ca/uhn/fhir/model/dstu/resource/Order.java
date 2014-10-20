















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
 * HAPI/FHIR <b>Order</b> Resource
 * (A request to perform an action)
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
 * <a href="http://hl7.org/fhir/profiles/Order">http://hl7.org/fhir/profiles/Order</a> 
 * </p>
 *
 */
@ResourceDef(name="Order", profile="http://hl7.org/fhir/profiles/Order", id="order")
public class Order extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Order.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Order.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Order.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Order.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Order.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Order.subject");

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.source</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="Order.source", description="", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.source</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Order.source</b>".
	 */
	public static final Include INCLUDE_SOURCE = new Include("Order.source");

	/**
	 * Search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="target", path="Order.target", description="", type="reference"  )
	public static final String SP_TARGET = "target";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam TARGET = new ReferenceClientParam(SP_TARGET);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Order.target</b>".
	 */
	public static final Include INCLUDE_TARGET = new Include("Order.target");

	/**
	 * Search parameter constant for <b>authority</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.authority</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="authority", path="Order.authority", description="", type="reference"  )
	public static final String SP_AUTHORITY = "authority";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>authority</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.authority</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHORITY = new ReferenceClientParam(SP_AUTHORITY);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Order.authority</b>".
	 */
	public static final Include INCLUDE_AUTHORITY = new Include("Order.authority");

	/**
	 * Search parameter constant for <b>when_code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Order.when.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="when_code", path="Order.when.code", description="", type="token"  )
	public static final String SP_WHEN_CODE = "when_code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>when_code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Order.when.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam WHEN_CODE = new TokenClientParam(SP_WHEN_CODE);

	/**
	 * Search parameter constant for <b>when</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Order.when.schedule</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="when", path="Order.when.schedule", description="", type="date"  )
	public static final String SP_WHEN = "when";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>when</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Order.when.schedule</b><br/>
	 * </p>
	 */
	public static final DateClientParam WHEN = new DateClientParam(SP_WHEN);

	/**
	 * Search parameter constant for <b>detail</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.detail</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="detail", path="Order.detail", description="", type="reference"  )
	public static final String SP_DETAIL = "detail";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>detail</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Order.detail</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DETAIL = new ReferenceClientParam(SP_DETAIL);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Order.detail</b>".
	 */
	public static final Include INCLUDE_DETAIL = new Include("Order.detail");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Identifiers assigned to this order by the orderer or by the receiver",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When the order was made",
		formalDefinition=""
	)
	private DateTimeDt myDate;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient this order is about",
		formalDefinition=""
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="source", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who initiated the order",
		formalDefinition=""
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="target", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who is intended to fulfill the order",
		formalDefinition=""
	)
	private ResourceReferenceDt myTarget;
	
	@Child(name="reason", order=5, min=0, max=1, type={
		CodeableConceptDt.class, 		IResource.class	})
	@Description(
		shortDefinition="Text - why the order was made",
		formalDefinition=""
	)
	private IDatatype myReason;
	
	@Child(name="authority", order=6, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="If required by policy",
		formalDefinition=""
	)
	private ResourceReferenceDt myAuthority;
	
	@Child(name="when", order=7, min=0, max=1)	
	@Description(
		shortDefinition="When order should be fulfilled",
		formalDefinition=""
	)
	private When myWhen;
	
	@Child(name="detail", order=8, min=1, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="What action is being ordered",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  mySubject,  mySource,  myTarget,  myReason,  myAuthority,  myWhen,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, mySubject, mySource, myTarget, myReason, myAuthority, myWhen, myDetail);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Order addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Order addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (When the order was made).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the order was made)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When the order was made)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the order was made)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Patient this order is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Patient this order is about)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>source</b> (Who initiated the order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Who initiated the order)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> (Who is intended to fulfill the order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Who is intended to fulfill the order)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reason[x]</b> (Text - why the order was made).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IDatatype getReason() {  
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason[x]</b> (Text - why the order was made)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setReason(IDatatype theValue) {
		myReason = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>authority</b> (If required by policy).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getAuthority() {  
		if (myAuthority == null) {
			myAuthority = new ResourceReferenceDt();
		}
		return myAuthority;
	}

	/**
	 * Sets the value(s) for <b>authority</b> (If required by policy)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setAuthority(ResourceReferenceDt theValue) {
		myAuthority = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>when</b> (When order should be fulfilled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public When getWhen() {  
		if (myWhen == null) {
			myWhen = new When();
		}
		return myWhen;
	}

	/**
	 * Sets the value(s) for <b>when</b> (When order should be fulfilled)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setWhen(When theValue) {
		myWhen = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>detail</b> (What action is being ordered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (What action is being ordered)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Order setDetail(java.util.List<ResourceReferenceDt> theValue) {
		myDetail = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>detail</b> (What action is being ordered)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDetail().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>Order.when</b> (When order should be fulfilled)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class When extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Code specifies when request should be done. The code may simply be a priority code",
		formalDefinition=""
	)
	private CodeableConceptDt myCode;
	
	@Child(name="schedule", type=ScheduleDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="A formal schedule",
		formalDefinition=""
	)
	private ScheduleDt mySchedule;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  mySchedule);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, mySchedule);
	}
	

	/**
	 * Gets the value(s) for <b>code</b> (Code specifies when request should be done. The code may simply be a priority code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code specifies when request should be done. The code may simply be a priority code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public When setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>schedule</b> (A formal schedule).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ScheduleDt getSchedule() {  
		if (mySchedule == null) {
			mySchedule = new ScheduleDt();
		}
		return mySchedule;
	}

	/**
	 * Sets the value(s) for <b>schedule</b> (A formal schedule)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public When setSchedule(ScheduleDt theValue) {
		mySchedule = theValue;
		return this;
	}

  

	}




    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.ORDER;
    }

}