















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
 * HAPI/FHIR <b>Coverage</b> Resource
 * (Insurance or medical plan)
 *
 * <p>
 * <b>Definition:</b>
 * Financial instrument by which payment information for health care
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Health care programs and insurers are significant payors of health service costs
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Coverage">http://hl7.org/fhir/profiles/Coverage</a> 
 * </p>
 *
 */
@ResourceDef(name="Coverage", profile="http://hl7.org/fhir/profiles/Coverage", id="coverage")
public class Coverage 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>issuer</b>
	 * <p>
	 * Description: <b>The identity of the insurer</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Coverage.issuer</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="issuer", path="Coverage.issuer", description="The identity of the insurer", type="reference"  )
	public static final String SP_ISSUER = "issuer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>issuer</b>
	 * <p>
	 * Description: <b>The identity of the insurer</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Coverage.issuer</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam ISSUER = new ReferenceClientParam(SP_ISSUER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Coverage.issuer</b>".
	 */
	public static final Include INCLUDE_ISSUER = new Include("Coverage.issuer");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The primary identifier of the insured</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Coverage.identifier", description="The primary identifier of the insured", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The primary identifier of the insured</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The kind of coverage</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.type</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Coverage.type", description="The kind of coverage", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The kind of coverage</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.type</b><br>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>plan</b>
	 * <p>
	 * Description: <b>A plan or policy identifier</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.plan</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="plan", path="Coverage.plan", description="A plan or policy identifier", type="token"  )
	public static final String SP_PLAN = "plan";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>plan</b>
	 * <p>
	 * Description: <b>A plan or policy identifier</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.plan</b><br>
	 * </p>
	 */
	public static final TokenClientParam PLAN = new TokenClientParam(SP_PLAN);

	/**
	 * Search parameter constant for <b>subplan</b>
	 * <p>
	 * Description: <b>Sub-plan identifier</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.subplan</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="subplan", path="Coverage.subplan", description="Sub-plan identifier", type="token"  )
	public static final String SP_SUBPLAN = "subplan";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subplan</b>
	 * <p>
	 * Description: <b>Sub-plan identifier</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.subplan</b><br>
	 * </p>
	 */
	public static final TokenClientParam SUBPLAN = new TokenClientParam(SP_SUBPLAN);

	/**
	 * Search parameter constant for <b>group</b>
	 * <p>
	 * Description: <b>Group identifier</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.group</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="group", path="Coverage.group", description="Group identifier", type="token"  )
	public static final String SP_GROUP = "group";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>group</b>
	 * <p>
	 * Description: <b>Group identifier</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.group</b><br>
	 * </p>
	 */
	public static final TokenClientParam GROUP = new TokenClientParam(SP_GROUP);

	/**
	 * Search parameter constant for <b>dependent</b>
	 * <p>
	 * Description: <b>Dependent number</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.dependent</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="dependent", path="Coverage.dependent", description="Dependent number", type="token"  )
	public static final String SP_DEPENDENT = "dependent";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dependent</b>
	 * <p>
	 * Description: <b>Dependent number</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.dependent</b><br>
	 * </p>
	 */
	public static final TokenClientParam DEPENDENT = new TokenClientParam(SP_DEPENDENT);

	/**
	 * Search parameter constant for <b>sequence</b>
	 * <p>
	 * Description: <b>Sequence number</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.sequence</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="sequence", path="Coverage.sequence", description="Sequence number", type="token"  )
	public static final String SP_SEQUENCE = "sequence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>sequence</b>
	 * <p>
	 * Description: <b>Sequence number</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.sequence</b><br>
	 * </p>
	 */
	public static final TokenClientParam SEQUENCE = new TokenClientParam(SP_SEQUENCE);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the subscriber</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.subscriber.name</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Coverage.subscriber.name", description="The name of the subscriber", type="token"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the subscriber</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Coverage.subscriber.name</b><br>
	 * </p>
	 */
	public static final TokenClientParam NAME = new TokenClientParam(SP_NAME);


	@Child(name="issuer", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="An identifier for the plan issuer",
		formalDefinition="The program or plan underwriter or payor."
	)
	private ResourceReferenceDt myIssuer;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Coverage start and end dates",
		formalDefinition="Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force."
	)
	private PeriodDt myPeriod;
	
	@Child(name="type", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Type of coverage",
		formalDefinition="The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health."
	)
	private CodingDt myType;
	
	@Child(name="identifier", type=IdentifierDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="The primary coverage ID",
		formalDefinition="The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="group", type=IdentifierDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="An identifier for the group",
		formalDefinition="Todo"
	)
	private IdentifierDt myGroup;
	
	@Child(name="plan", type=IdentifierDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="An identifier for the plan",
		formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID."
	)
	private IdentifierDt myPlan;
	
	@Child(name="subplan", type=IdentifierDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="An identifier for the subsection of the plan",
		formalDefinition="Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID."
	)
	private IdentifierDt mySubplan;
	
	@Child(name="dependent", type=IntegerDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="The dependent number",
		formalDefinition="A unique identifier for a dependent under the coverage."
	)
	private IntegerDt myDependent;
	
	@Child(name="sequence", type=IntegerDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="The plan instance or sequence counter",
		formalDefinition="An optional counter for a particular instance of the identified coverage which increments upon each renewal."
	)
	private IntegerDt mySequence;
	
	@Child(name="subscriber", order=9, min=0, max=1)	
	@Description(
		shortDefinition="Planholder information",
		formalDefinition=""
	)
	private Subscriber mySubscriber;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIssuer,  myPeriod,  myType,  myIdentifier,  myGroup,  myPlan,  mySubplan,  myDependent,  mySequence,  mySubscriber);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIssuer, myPeriod, myType, myIdentifier, myGroup, myPlan, mySubplan, myDependent, mySequence, mySubscriber);
	}

	/**
	 * Gets the value(s) for <b>issuer</b> (An identifier for the plan issuer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public ResourceReferenceDt getIssuer() {  
		if (myIssuer == null) {
			myIssuer = new ResourceReferenceDt();
		}
		return myIssuer;
	}


	/**
	 * Gets the value(s) for <b>issuer</b> (An identifier for the plan issuer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public ResourceReferenceDt getIssuerElement() {  
		if (myIssuer == null) {
			myIssuer = new ResourceReferenceDt();
		}
		return myIssuer;
	}


	/**
	 * Sets the value(s) for <b>issuer</b> (An identifier for the plan issuer)
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public Coverage setIssuer(ResourceReferenceDt theValue) {
		myIssuer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Coverage start and end dates).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Gets the value(s) for <b>period</b> (Coverage start and end dates).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     * </p> 
	 */
	public PeriodDt getPeriodElement() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Sets the value(s) for <b>period</b> (Coverage start and end dates)
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     * </p> 
	 */
	public Coverage setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Type of coverage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (Type of coverage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public CodingDt getTypeElement() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (Type of coverage)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public Coverage setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (The primary coverage ID).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (The primary coverage ID).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public IdentifierDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (The primary coverage ID)
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (The primary coverage ID)
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (The primary coverage ID)
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>group</b> (An identifier for the group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public IdentifierDt getGroup() {  
		if (myGroup == null) {
			myGroup = new IdentifierDt();
		}
		return myGroup;
	}


	/**
	 * Gets the value(s) for <b>group</b> (An identifier for the group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public IdentifierDt getGroupElement() {  
		if (myGroup == null) {
			myGroup = new IdentifierDt();
		}
		return myGroup;
	}


	/**
	 * Sets the value(s) for <b>group</b> (An identifier for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Coverage setGroup(IdentifierDt theValue) {
		myGroup = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>group</b> (An identifier for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Coverage setGroup( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myGroup = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>group</b> (An identifier for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Coverage setGroup( String theSystem,  String theValue) {
		myGroup = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>plan</b> (An identifier for the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public IdentifierDt getPlan() {  
		if (myPlan == null) {
			myPlan = new IdentifierDt();
		}
		return myPlan;
	}


	/**
	 * Gets the value(s) for <b>plan</b> (An identifier for the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public IdentifierDt getPlanElement() {  
		if (myPlan == null) {
			myPlan = new IdentifierDt();
		}
		return myPlan;
	}


	/**
	 * Sets the value(s) for <b>plan</b> (An identifier for the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan(IdentifierDt theValue) {
		myPlan = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>plan</b> (An identifier for the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myPlan = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>plan</b> (An identifier for the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan( String theSystem,  String theValue) {
		myPlan = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subplan</b> (An identifier for the subsection of the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public IdentifierDt getSubplan() {  
		if (mySubplan == null) {
			mySubplan = new IdentifierDt();
		}
		return mySubplan;
	}


	/**
	 * Gets the value(s) for <b>subplan</b> (An identifier for the subsection of the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public IdentifierDt getSubplanElement() {  
		if (mySubplan == null) {
			mySubplan = new IdentifierDt();
		}
		return mySubplan;
	}


	/**
	 * Sets the value(s) for <b>subplan</b> (An identifier for the subsection of the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan(IdentifierDt theValue) {
		mySubplan = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>subplan</b> (An identifier for the subsection of the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		mySubplan = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>subplan</b> (An identifier for the subsection of the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan( String theSystem,  String theValue) {
		mySubplan = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dependent</b> (The dependent number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public IntegerDt getDependent() {  
		if (myDependent == null) {
			myDependent = new IntegerDt();
		}
		return myDependent;
	}


	/**
	 * Gets the value(s) for <b>dependent</b> (The dependent number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public IntegerDt getDependentElement() {  
		if (myDependent == null) {
			myDependent = new IntegerDt();
		}
		return myDependent;
	}


	/**
	 * Sets the value(s) for <b>dependent</b> (The dependent number)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Coverage setDependent(IntegerDt theValue) {
		myDependent = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dependent</b> (The dependent number)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Coverage setDependent( int theInteger) {
		myDependent = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sequence</b> (The plan instance or sequence counter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public IntegerDt getSequence() {  
		if (mySequence == null) {
			mySequence = new IntegerDt();
		}
		return mySequence;
	}


	/**
	 * Gets the value(s) for <b>sequence</b> (The plan instance or sequence counter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public IntegerDt getSequenceElement() {  
		if (mySequence == null) {
			mySequence = new IntegerDt();
		}
		return mySequence;
	}


	/**
	 * Sets the value(s) for <b>sequence</b> (The plan instance or sequence counter)
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Coverage setSequence(IntegerDt theValue) {
		mySequence = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sequence</b> (The plan instance or sequence counter)
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Coverage setSequence( int theInteger) {
		mySequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subscriber</b> (Planholder information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Subscriber getSubscriber() {  
		if (mySubscriber == null) {
			mySubscriber = new Subscriber();
		}
		return mySubscriber;
	}


	/**
	 * Gets the value(s) for <b>subscriber</b> (Planholder information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Subscriber getSubscriberElement() {  
		if (mySubscriber == null) {
			mySubscriber = new Subscriber();
		}
		return mySubscriber;
	}


	/**
	 * Sets the value(s) for <b>subscriber</b> (Planholder information)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Coverage setSubscriber(Subscriber theValue) {
		mySubscriber = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>Coverage.subscriber</b> (Planholder information)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Subscriber 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=HumanNameDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="PolicyHolder name",
		formalDefinition="The name of the PolicyHolder"
	)
	private HumanNameDt myName;
	
	@Child(name="address", type=AddressDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="PolicyHolder address",
		formalDefinition="The mailing address, typically home, of the PolicyHolder"
	)
	private AddressDt myAddress;
	
	@Child(name="birthdate", type=DateDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="PolicyHolder date of birth",
		formalDefinition="The date of birth of the PolicyHolder"
	)
	private DateDt myBirthdate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myAddress,  myBirthdate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myAddress, myBirthdate);
	}

	/**
	 * Gets the value(s) for <b>name</b> (PolicyHolder name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}


	/**
	 * Gets the value(s) for <b>name</b> (PolicyHolder name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public HumanNameDt getNameElement() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}


	/**
	 * Sets the value(s) for <b>name</b> (PolicyHolder name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public Subscriber setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>address</b> (PolicyHolder address).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}


	/**
	 * Gets the value(s) for <b>address</b> (PolicyHolder address).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public AddressDt getAddressElement() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}


	/**
	 * Sets the value(s) for <b>address</b> (PolicyHolder address)
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public Subscriber setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthdate</b> (PolicyHolder date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public DateDt getBirthdate() {  
		if (myBirthdate == null) {
			myBirthdate = new DateDt();
		}
		return myBirthdate;
	}


	/**
	 * Gets the value(s) for <b>birthdate</b> (PolicyHolder date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public DateDt getBirthdateElement() {  
		if (myBirthdate == null) {
			myBirthdate = new DateDt();
		}
		return myBirthdate;
	}


	/**
	 * Sets the value(s) for <b>birthdate</b> (PolicyHolder date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public Subscriber setBirthdate(DateDt theValue) {
		myBirthdate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>birthdate</b> (PolicyHolder date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public Subscriber setBirthdate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthdate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthdate</b> (PolicyHolder date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public Subscriber setBirthdateWithDayPrecision( Date theDate) {
		myBirthdate = new DateDt(theDate); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "Coverage";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
