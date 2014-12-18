















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
 * HAPI/FHIR <b>Encounter</b> Resource
 * (An interaction during which services are provided to the patient)
 *
 * <p>
 * <b>Definition:</b>
 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Encounter">http://hl7.org/fhir/profiles/Encounter</a> 
 * </p>
 *
 */
@ResourceDef(name="Encounter", profile="http://hl7.org/fhir/profiles/Encounter", id="encounter")
public class Encounter 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Encounter.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Encounter.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Encounter.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>A date within the period the Encounter lasted</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Encounter.period", description="A date within the period the Encounter lasted", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>A date within the period the Encounter lasted</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Encounter.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Encounter.subject");

	/**
	 * Search parameter constant for <b>length</b>
	 * <p>
	 * Description: <b>Length of encounter in days</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Encounter.length</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="length", path="Encounter.length", description="Length of encounter in days", type="number"  )
	public static final String SP_LENGTH = "length";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>length</b>
	 * <p>
	 * Description: <b>Length of encounter in days</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Encounter.length</b><br/>
	 * </p>
	 */
	public static final NumberClientParam LENGTH = new NumberClientParam(SP_LENGTH);

	/**
	 * Search parameter constant for <b>indication</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.indication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="indication", path="Encounter.indication", description="", type="reference"  )
	public static final String SP_INDICATION = "indication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>indication</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.indication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam INDICATION = new ReferenceClientParam(SP_INDICATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.indication</b>".
	 */
	public static final Include INCLUDE_INDICATION = new Include("Encounter.indication");

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.location.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Encounter.location.location", description="", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Encounter.location.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Encounter.location.location</b>".
	 */
	public static final Include INCLUDE_LOCATION_LOCATION = new Include("Encounter.location.location");

	/**
	 * Search parameter constant for <b>location-period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.location.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location-period", path="Encounter.location.period", description="", type="date"  )
	public static final String SP_LOCATION_PERIOD = "location-period";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location-period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Encounter.location.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam LOCATION_PERIOD = new DateClientParam(SP_LOCATION_PERIOD);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Identifier(s) by which this encounter is known",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="planned | in progress | onleave | finished | cancelled",
		formalDefinition=""
	)
	private BoundCodeDt<EncounterStateEnum> myStatus;
	
	@Child(name="class", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="inpatient | outpatient | ambulatory | emergency +",
		formalDefinition=""
	)
	private BoundCodeDt<EncounterClassEnum> myClassElement;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Specific type of encounter",
		formalDefinition="Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)"
	)
	private java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> myType;
	
	@Child(name="subject", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="The patient present at the encounter",
		formalDefinition=""
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="participant", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="List of participants involved in the encounter",
		formalDefinition="The main practitioner responsible for providing the service"
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="period", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="The start and end time of the encounter",
		formalDefinition="The start and end time of the encounter"
	)
	private PeriodDt myPeriod;
	
	@Child(name="length", type=DurationDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Quantity of time the encounter lasted",
		formalDefinition="Quantity of time the encounter lasted. This excludes the time during leaves of absence."
	)
	private DurationDt myLength;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Reason the encounter takes place (code)",
		formalDefinition="Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis."
	)
	private BoundCodeableConceptDt<EncounterReasonCodesEnum> myReason;
	
	@Child(name="indication", order=9, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="Reason the encounter takes place (resource)",
		formalDefinition="Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis."
	)
	private ResourceReferenceDt myIndication;
	
	@Child(name="priority", type=CodeableConceptDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Indicates the urgency of the encounter",
		formalDefinition=""
	)
	private CodeableConceptDt myPriority;
	
	@Child(name="hospitalization", order=11, min=0, max=1)	
	@Description(
		shortDefinition="Details about an admission to a clinic",
		formalDefinition="Details about an admission to a clinic"
	)
	private Hospitalization myHospitalization;
	
	@Child(name="location", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="List of locations the patient has been at",
		formalDefinition="List of locations at which the patient has been"
	)
	private java.util.List<Location> myLocation;
	
	@Child(name="serviceProvider", order=13, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Department or team providing care",
		formalDefinition=""
	)
	private ResourceReferenceDt myServiceProvider;
	
	@Child(name="partOf", order=14, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Another Encounter this encounter is part of",
		formalDefinition="Another Encounter of which this encounter is a part of (administratively or in time)."
	)
	private ResourceReferenceDt myPartOf;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myClassElement,  myType,  mySubject,  myParticipant,  myPeriod,  myLength,  myReason,  myIndication,  myPriority,  myHospitalization,  myLocation,  myServiceProvider,  myPartOf);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myClassElement, myType, mySubject, myParticipant, myPeriod, myLength, myReason, myIndication, myPriority, myHospitalization, myLocation, myServiceProvider, myPartOf);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier(s) by which this encounter is known).
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
	 * Gets the value(s) for <b>identifier</b> (Identifier(s) by which this encounter is known).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier(s) by which this encounter is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Identifier(s) by which this encounter is known)
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
	 * Gets the first repetition for <b>identifier</b> (Identifier(s) by which this encounter is known),
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
	 * Adds a new value for <b>identifier</b> (Identifier(s) by which this encounter is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Encounter addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Identifier(s) by which this encounter is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Encounter addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (planned | in progress | onleave | finished | cancelled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterStateEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<EncounterStateEnum>(EncounterStateEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Gets the value(s) for <b>status</b> (planned | in progress | onleave | finished | cancelled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterStateEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<EncounterStateEnum>(EncounterStateEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Sets the value(s) for <b>status</b> (planned | in progress | onleave | finished | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setStatus(BoundCodeDt<EncounterStateEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (planned | in progress | onleave | finished | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setStatus(EncounterStateEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>class</b> (inpatient | outpatient | ambulatory | emergency +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterClassEnum> getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new BoundCodeDt<EncounterClassEnum>(EncounterClassEnum.VALUESET_BINDER);
		}
		return myClassElement;
	}


	/**
	 * Gets the value(s) for <b>class</b> (inpatient | outpatient | ambulatory | emergency +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<EncounterClassEnum> getClassElementElement() {  
		if (myClassElement == null) {
			myClassElement = new BoundCodeDt<EncounterClassEnum>(EncounterClassEnum.VALUESET_BINDER);
		}
		return myClassElement;
	}


	/**
	 * Sets the value(s) for <b>class</b> (inpatient | outpatient | ambulatory | emergency +)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setClassElement(BoundCodeDt<EncounterClassEnum> theValue) {
		myClassElement = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>class</b> (inpatient | outpatient | ambulatory | emergency +)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setClassElement(EncounterClassEnum theValue) {
		getClassElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Specific type of encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<EncounterTypeEnum>>();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (Specific type of encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> getTypeElement() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<EncounterTypeEnum>>();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (Specific type of encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public Encounter setType(java.util.List<BoundCodeableConceptDt<EncounterTypeEnum>> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Add a value for <b>type</b> (Specific type of encounter) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterTypeEnum> addType(EncounterTypeEnum theValue) {
		BoundCodeableConceptDt<EncounterTypeEnum> retVal = new BoundCodeableConceptDt<EncounterTypeEnum>(EncounterTypeEnum.VALUESET_BINDER, theValue);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>type</b> (Specific type of encounter),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterTypeEnum> getTypeFirstRep() {
		if (getType().size() == 0) {
			addType();
		}
		return getType().get(0);
	}

	/**
	 * Add a value for <b>type</b> (Specific type of encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterTypeEnum> addType() {
		BoundCodeableConceptDt<EncounterTypeEnum> retVal = new BoundCodeableConceptDt<EncounterTypeEnum>(EncounterTypeEnum.VALUESET_BINDER);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> (Specific type of encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)
     * </p> 
	 */
	public Encounter setType(EncounterTypeEnum theValue) {
		getType().clear();
		addType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (The patient present at the encounter).
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
	 * Gets the value(s) for <b>subject</b> (The patient present at the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (The patient present at the encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>participant</b> (List of participants involved in the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public java.util.List<Participant> getParticipant() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}


	/**
	 * Gets the value(s) for <b>participant</b> (List of participants involved in the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public java.util.List<Participant> getParticipantElement() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}


	/**
	 * Sets the value(s) for <b>participant</b> (List of participants involved in the encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public Encounter setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>participant</b> (List of participants involved in the encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public Participant addParticipant() {
		Participant newType = new Participant();
		getParticipant().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>participant</b> (List of participants involved in the encounter),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> (The start and end time of the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The start and end time of the encounter
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Gets the value(s) for <b>period</b> (The start and end time of the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The start and end time of the encounter
     * </p> 
	 */
	public PeriodDt getPeriodElement() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Sets the value(s) for <b>period</b> (The start and end time of the encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * The start and end time of the encounter
     * </p> 
	 */
	public Encounter setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>length</b> (Quantity of time the encounter lasted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     * </p> 
	 */
	public DurationDt getLength() {  
		if (myLength == null) {
			myLength = new DurationDt();
		}
		return myLength;
	}


	/**
	 * Gets the value(s) for <b>length</b> (Quantity of time the encounter lasted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     * </p> 
	 */
	public DurationDt getLengthElement() {  
		if (myLength == null) {
			myLength = new DurationDt();
		}
		return myLength;
	}


	/**
	 * Sets the value(s) for <b>length</b> (Quantity of time the encounter lasted)
	 *
     * <p>
     * <b>Definition:</b>
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     * </p> 
	 */
	public Encounter setLength(DurationDt theValue) {
		myLength = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reason</b> (Reason the encounter takes place (code)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterReasonCodesEnum> getReason() {  
		if (myReason == null) {
			myReason = new BoundCodeableConceptDt<EncounterReasonCodesEnum>(EncounterReasonCodesEnum.VALUESET_BINDER);
		}
		return myReason;
	}


	/**
	 * Gets the value(s) for <b>reason</b> (Reason the encounter takes place (code)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public BoundCodeableConceptDt<EncounterReasonCodesEnum> getReasonElement() {  
		if (myReason == null) {
			myReason = new BoundCodeableConceptDt<EncounterReasonCodesEnum>(EncounterReasonCodesEnum.VALUESET_BINDER);
		}
		return myReason;
	}


	/**
	 * Sets the value(s) for <b>reason</b> (Reason the encounter takes place (code))
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public Encounter setReason(BoundCodeableConceptDt<EncounterReasonCodesEnum> theValue) {
		myReason = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (Reason the encounter takes place (code))
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     * </p> 
	 */
	public Encounter setReason(EncounterReasonCodesEnum theValue) {
		getReason().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>indication</b> (Reason the encounter takes place (resource)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     * </p> 
	 */
	public ResourceReferenceDt getIndication() {  
		if (myIndication == null) {
			myIndication = new ResourceReferenceDt();
		}
		return myIndication;
	}


	/**
	 * Gets the value(s) for <b>indication</b> (Reason the encounter takes place (resource)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     * </p> 
	 */
	public ResourceReferenceDt getIndicationElement() {  
		if (myIndication == null) {
			myIndication = new ResourceReferenceDt();
		}
		return myIndication;
	}


	/**
	 * Sets the value(s) for <b>indication</b> (Reason the encounter takes place (resource))
	 *
     * <p>
     * <b>Definition:</b>
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     * </p> 
	 */
	public Encounter setIndication(ResourceReferenceDt theValue) {
		myIndication = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>priority</b> (Indicates the urgency of the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getPriority() {  
		if (myPriority == null) {
			myPriority = new CodeableConceptDt();
		}
		return myPriority;
	}


	/**
	 * Gets the value(s) for <b>priority</b> (Indicates the urgency of the encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getPriorityElement() {  
		if (myPriority == null) {
			myPriority = new CodeableConceptDt();
		}
		return myPriority;
	}


	/**
	 * Sets the value(s) for <b>priority</b> (Indicates the urgency of the encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setPriority(CodeableConceptDt theValue) {
		myPriority = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>hospitalization</b> (Details about an admission to a clinic).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	public Hospitalization getHospitalization() {  
		if (myHospitalization == null) {
			myHospitalization = new Hospitalization();
		}
		return myHospitalization;
	}


	/**
	 * Gets the value(s) for <b>hospitalization</b> (Details about an admission to a clinic).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	public Hospitalization getHospitalizationElement() {  
		if (myHospitalization == null) {
			myHospitalization = new Hospitalization();
		}
		return myHospitalization;
	}


	/**
	 * Sets the value(s) for <b>hospitalization</b> (Details about an admission to a clinic)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	public Encounter setHospitalization(Hospitalization theValue) {
		myHospitalization = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (List of locations the patient has been at).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public java.util.List<Location> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<Location>();
		}
		return myLocation;
	}


	/**
	 * Gets the value(s) for <b>location</b> (List of locations the patient has been at).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public java.util.List<Location> getLocationElement() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<Location>();
		}
		return myLocation;
	}


	/**
	 * Sets the value(s) for <b>location</b> (List of locations the patient has been at)
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public Encounter setLocation(java.util.List<Location> theValue) {
		myLocation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>location</b> (List of locations the patient has been at)
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public Location addLocation() {
		Location newType = new Location();
		getLocation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>location</b> (List of locations the patient has been at),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	public Location getLocationFirstRep() {
		if (getLocation().isEmpty()) {
			return addLocation();
		}
		return getLocation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>serviceProvider</b> (Department or team providing care).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getServiceProvider() {  
		if (myServiceProvider == null) {
			myServiceProvider = new ResourceReferenceDt();
		}
		return myServiceProvider;
	}


	/**
	 * Gets the value(s) for <b>serviceProvider</b> (Department or team providing care).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getServiceProviderElement() {  
		if (myServiceProvider == null) {
			myServiceProvider = new ResourceReferenceDt();
		}
		return myServiceProvider;
	}


	/**
	 * Sets the value(s) for <b>serviceProvider</b> (Department or team providing care)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Encounter setServiceProvider(ResourceReferenceDt theValue) {
		myServiceProvider = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>partOf</b> (Another Encounter this encounter is part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Another Encounter of which this encounter is a part of (administratively or in time).
     * </p> 
	 */
	public ResourceReferenceDt getPartOf() {  
		if (myPartOf == null) {
			myPartOf = new ResourceReferenceDt();
		}
		return myPartOf;
	}


	/**
	 * Gets the value(s) for <b>partOf</b> (Another Encounter this encounter is part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Another Encounter of which this encounter is a part of (administratively or in time).
     * </p> 
	 */
	public ResourceReferenceDt getPartOfElement() {  
		if (myPartOf == null) {
			myPartOf = new ResourceReferenceDt();
		}
		return myPartOf;
	}


	/**
	 * Sets the value(s) for <b>partOf</b> (Another Encounter this encounter is part of)
	 *
     * <p>
     * <b>Definition:</b>
     * Another Encounter of which this encounter is a part of (administratively or in time).
     * </p> 
	 */
	public Encounter setPartOf(ResourceReferenceDt theValue) {
		myPartOf = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>Encounter.participant</b> (List of participants involved in the encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * The main practitioner responsible for providing the service
     * </p> 
	 */
	@Block()	
	public static class Participant 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Role of participant in encounter",
		formalDefinition=""
	)
	private java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> myType;
	
	@Child(name="individual", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Persons involved in the encounter other than the patient",
		formalDefinition=""
	)
	private ResourceReferenceDt myIndividual;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myIndividual);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myIndividual);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Role of participant in encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<ParticipantTypeEnum>>();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (Role of participant in encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> getTypeElement() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeableConceptDt<ParticipantTypeEnum>>();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (Role of participant in encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setType(java.util.List<BoundCodeableConceptDt<ParticipantTypeEnum>> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Add a value for <b>type</b> (Role of participant in encounter) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addType(ParticipantTypeEnum theValue) {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER, theValue);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>type</b> (Role of participant in encounter),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> getTypeFirstRep() {
		if (getType().size() == 0) {
			addType();
		}
		return getType().get(0);
	}

	/**
	 * Add a value for <b>type</b> (Role of participant in encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<ParticipantTypeEnum> addType() {
		BoundCodeableConceptDt<ParticipantTypeEnum> retVal = new BoundCodeableConceptDt<ParticipantTypeEnum>(ParticipantTypeEnum.VALUESET_BINDER);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> (Role of participant in encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setType(ParticipantTypeEnum theValue) {
		getType().clear();
		addType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>individual</b> (Persons involved in the encounter other than the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new ResourceReferenceDt();
		}
		return myIndividual;
	}


	/**
	 * Gets the value(s) for <b>individual</b> (Persons involved in the encounter other than the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getIndividualElement() {  
		if (myIndividual == null) {
			myIndividual = new ResourceReferenceDt();
		}
		return myIndividual;
	}


	/**
	 * Sets the value(s) for <b>individual</b> (Persons involved in the encounter other than the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant setIndividual(ResourceReferenceDt theValue) {
		myIndividual = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Encounter.hospitalization</b> (Details about an admission to a clinic)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about an admission to a clinic
     * </p> 
	 */
	@Block()	
	public static class Hospitalization 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="preAdmissionIdentifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Pre-admission identifier",
		formalDefinition=""
	)
	private IdentifierDt myPreAdmissionIdentifier;
	
	@Child(name="origin", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="The location from which the patient came before admission",
		formalDefinition=""
	)
	private ResourceReferenceDt myOrigin;
	
	@Child(name="admitSource", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="From where patient was admitted (physician referral, transfer)",
		formalDefinition=""
	)
	private BoundCodeableConceptDt<AdmitSourceEnum> myAdmitSource;
	
	@Child(name="period", type=PeriodDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Period during which the patient was admitted",
		formalDefinition="Period during which the patient was admitted"
	)
	private PeriodDt myPeriod;
	
	@Child(name="accomodation", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Where the patient stays during this encounter",
		formalDefinition=""
	)
	private java.util.List<HospitalizationAccomodation> myAccomodation;
	
	@Child(name="diet", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Dietary restrictions for the patient",
		formalDefinition="Dietary restrictions for the patient"
	)
	private CodeableConceptDt myDiet;
	
	@Child(name="specialCourtesy", type=CodeableConceptDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Special courtesies (VIP, board member)",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> mySpecialCourtesy;
	
	@Child(name="specialArrangement", type=CodeableConceptDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Wheelchair, translator, stretcher, etc",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> mySpecialArrangement;
	
	@Child(name="destination", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Location to which the patient is discharged",
		formalDefinition=""
	)
	private ResourceReferenceDt myDestination;
	
	@Child(name="dischargeDisposition", type=CodeableConceptDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Category or kind of location after discharge",
		formalDefinition=""
	)
	private CodeableConceptDt myDischargeDisposition;
	
	@Child(name="dischargeDiagnosis", order=10, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete",
		formalDefinition=""
	)
	private ResourceReferenceDt myDischargeDiagnosis;
	
	@Child(name="reAdmission", type=BooleanDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Is this hospitalization a readmission?",
		formalDefinition="Whether this hospitalization is a readmission"
	)
	private BooleanDt myReAdmission;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPreAdmissionIdentifier,  myOrigin,  myAdmitSource,  myPeriod,  myAccomodation,  myDiet,  mySpecialCourtesy,  mySpecialArrangement,  myDestination,  myDischargeDisposition,  myDischargeDiagnosis,  myReAdmission);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPreAdmissionIdentifier, myOrigin, myAdmitSource, myPeriod, myAccomodation, myDiet, mySpecialCourtesy, mySpecialArrangement, myDestination, myDischargeDisposition, myDischargeDiagnosis, myReAdmission);
	}

	/**
	 * Gets the value(s) for <b>preAdmissionIdentifier</b> (Pre-admission identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt getPreAdmissionIdentifier() {  
		if (myPreAdmissionIdentifier == null) {
			myPreAdmissionIdentifier = new IdentifierDt();
		}
		return myPreAdmissionIdentifier;
	}


	/**
	 * Gets the value(s) for <b>preAdmissionIdentifier</b> (Pre-admission identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt getPreAdmissionIdentifierElement() {  
		if (myPreAdmissionIdentifier == null) {
			myPreAdmissionIdentifier = new IdentifierDt();
		}
		return myPreAdmissionIdentifier;
	}


	/**
	 * Sets the value(s) for <b>preAdmissionIdentifier</b> (Pre-admission identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setPreAdmissionIdentifier(IdentifierDt theValue) {
		myPreAdmissionIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>preAdmissionIdentifier</b> (Pre-admission identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setPreAdmissionIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myPreAdmissionIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>preAdmissionIdentifier</b> (Pre-admission identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setPreAdmissionIdentifier( String theSystem,  String theValue) {
		myPreAdmissionIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>origin</b> (The location from which the patient came before admission).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getOrigin() {  
		if (myOrigin == null) {
			myOrigin = new ResourceReferenceDt();
		}
		return myOrigin;
	}


	/**
	 * Gets the value(s) for <b>origin</b> (The location from which the patient came before admission).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getOriginElement() {  
		if (myOrigin == null) {
			myOrigin = new ResourceReferenceDt();
		}
		return myOrigin;
	}


	/**
	 * Sets the value(s) for <b>origin</b> (The location from which the patient came before admission)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setOrigin(ResourceReferenceDt theValue) {
		myOrigin = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>admitSource</b> (From where patient was admitted (physician referral, transfer)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<AdmitSourceEnum> getAdmitSource() {  
		if (myAdmitSource == null) {
			myAdmitSource = new BoundCodeableConceptDt<AdmitSourceEnum>(AdmitSourceEnum.VALUESET_BINDER);
		}
		return myAdmitSource;
	}


	/**
	 * Gets the value(s) for <b>admitSource</b> (From where patient was admitted (physician referral, transfer)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeableConceptDt<AdmitSourceEnum> getAdmitSourceElement() {  
		if (myAdmitSource == null) {
			myAdmitSource = new BoundCodeableConceptDt<AdmitSourceEnum>(AdmitSourceEnum.VALUESET_BINDER);
		}
		return myAdmitSource;
	}


	/**
	 * Sets the value(s) for <b>admitSource</b> (From where patient was admitted (physician referral, transfer))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setAdmitSource(BoundCodeableConceptDt<AdmitSourceEnum> theValue) {
		myAdmitSource = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>admitSource</b> (From where patient was admitted (physician referral, transfer))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setAdmitSource(AdmitSourceEnum theValue) {
		getAdmitSource().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Period during which the patient was admitted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Period during which the patient was admitted
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Gets the value(s) for <b>period</b> (Period during which the patient was admitted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Period during which the patient was admitted
     * </p> 
	 */
	public PeriodDt getPeriodElement() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Sets the value(s) for <b>period</b> (Period during which the patient was admitted)
	 *
     * <p>
     * <b>Definition:</b>
     * Period during which the patient was admitted
     * </p> 
	 */
	public Hospitalization setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>accomodation</b> (Where the patient stays during this encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<HospitalizationAccomodation> getAccomodation() {  
		if (myAccomodation == null) {
			myAccomodation = new java.util.ArrayList<HospitalizationAccomodation>();
		}
		return myAccomodation;
	}


	/**
	 * Gets the value(s) for <b>accomodation</b> (Where the patient stays during this encounter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<HospitalizationAccomodation> getAccomodationElement() {  
		if (myAccomodation == null) {
			myAccomodation = new java.util.ArrayList<HospitalizationAccomodation>();
		}
		return myAccomodation;
	}


	/**
	 * Sets the value(s) for <b>accomodation</b> (Where the patient stays during this encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setAccomodation(java.util.List<HospitalizationAccomodation> theValue) {
		myAccomodation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>accomodation</b> (Where the patient stays during this encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HospitalizationAccomodation addAccomodation() {
		HospitalizationAccomodation newType = new HospitalizationAccomodation();
		getAccomodation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>accomodation</b> (Where the patient stays during this encounter),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HospitalizationAccomodation getAccomodationFirstRep() {
		if (getAccomodation().isEmpty()) {
			return addAccomodation();
		}
		return getAccomodation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>diet</b> (Dietary restrictions for the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Dietary restrictions for the patient
     * </p> 
	 */
	public CodeableConceptDt getDiet() {  
		if (myDiet == null) {
			myDiet = new CodeableConceptDt();
		}
		return myDiet;
	}


	/**
	 * Gets the value(s) for <b>diet</b> (Dietary restrictions for the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Dietary restrictions for the patient
     * </p> 
	 */
	public CodeableConceptDt getDietElement() {  
		if (myDiet == null) {
			myDiet = new CodeableConceptDt();
		}
		return myDiet;
	}


	/**
	 * Sets the value(s) for <b>diet</b> (Dietary restrictions for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * Dietary restrictions for the patient
     * </p> 
	 */
	public Hospitalization setDiet(CodeableConceptDt theValue) {
		myDiet = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specialCourtesy</b> (Special courtesies (VIP, board member)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSpecialCourtesy() {  
		if (mySpecialCourtesy == null) {
			mySpecialCourtesy = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySpecialCourtesy;
	}


	/**
	 * Gets the value(s) for <b>specialCourtesy</b> (Special courtesies (VIP, board member)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSpecialCourtesyElement() {  
		if (mySpecialCourtesy == null) {
			mySpecialCourtesy = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySpecialCourtesy;
	}


	/**
	 * Sets the value(s) for <b>specialCourtesy</b> (Special courtesies (VIP, board member))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setSpecialCourtesy(java.util.List<CodeableConceptDt> theValue) {
		mySpecialCourtesy = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>specialCourtesy</b> (Special courtesies (VIP, board member))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addSpecialCourtesy() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getSpecialCourtesy().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>specialCourtesy</b> (Special courtesies (VIP, board member)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getSpecialCourtesyFirstRep() {
		if (getSpecialCourtesy().isEmpty()) {
			return addSpecialCourtesy();
		}
		return getSpecialCourtesy().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>specialArrangement</b> (Wheelchair, translator, stretcher, etc).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSpecialArrangement() {  
		if (mySpecialArrangement == null) {
			mySpecialArrangement = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySpecialArrangement;
	}


	/**
	 * Gets the value(s) for <b>specialArrangement</b> (Wheelchair, translator, stretcher, etc).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSpecialArrangementElement() {  
		if (mySpecialArrangement == null) {
			mySpecialArrangement = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySpecialArrangement;
	}


	/**
	 * Sets the value(s) for <b>specialArrangement</b> (Wheelchair, translator, stretcher, etc)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setSpecialArrangement(java.util.List<CodeableConceptDt> theValue) {
		mySpecialArrangement = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>specialArrangement</b> (Wheelchair, translator, stretcher, etc)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addSpecialArrangement() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getSpecialArrangement().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>specialArrangement</b> (Wheelchair, translator, stretcher, etc),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getSpecialArrangementFirstRep() {
		if (getSpecialArrangement().isEmpty()) {
			return addSpecialArrangement();
		}
		return getSpecialArrangement().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>destination</b> (Location to which the patient is discharged).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getDestination() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}


	/**
	 * Gets the value(s) for <b>destination</b> (Location to which the patient is discharged).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getDestinationElement() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}


	/**
	 * Sets the value(s) for <b>destination</b> (Location to which the patient is discharged)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setDestination(ResourceReferenceDt theValue) {
		myDestination = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dischargeDisposition</b> (Category or kind of location after discharge).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getDischargeDisposition() {  
		if (myDischargeDisposition == null) {
			myDischargeDisposition = new CodeableConceptDt();
		}
		return myDischargeDisposition;
	}


	/**
	 * Gets the value(s) for <b>dischargeDisposition</b> (Category or kind of location after discharge).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getDischargeDispositionElement() {  
		if (myDischargeDisposition == null) {
			myDischargeDisposition = new CodeableConceptDt();
		}
		return myDischargeDisposition;
	}


	/**
	 * Sets the value(s) for <b>dischargeDisposition</b> (Category or kind of location after discharge)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setDischargeDisposition(CodeableConceptDt theValue) {
		myDischargeDisposition = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dischargeDiagnosis</b> (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getDischargeDiagnosis() {  
		if (myDischargeDiagnosis == null) {
			myDischargeDiagnosis = new ResourceReferenceDt();
		}
		return myDischargeDiagnosis;
	}


	/**
	 * Gets the value(s) for <b>dischargeDiagnosis</b> (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getDischargeDiagnosisElement() {  
		if (myDischargeDiagnosis == null) {
			myDischargeDiagnosis = new ResourceReferenceDt();
		}
		return myDischargeDiagnosis;
	}


	/**
	 * Sets the value(s) for <b>dischargeDiagnosis</b> (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Hospitalization setDischargeDiagnosis(ResourceReferenceDt theValue) {
		myDischargeDiagnosis = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reAdmission</b> (Is this hospitalization a readmission?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public BooleanDt getReAdmission() {  
		if (myReAdmission == null) {
			myReAdmission = new BooleanDt();
		}
		return myReAdmission;
	}


	/**
	 * Gets the value(s) for <b>reAdmission</b> (Is this hospitalization a readmission?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public BooleanDt getReAdmissionElement() {  
		if (myReAdmission == null) {
			myReAdmission = new BooleanDt();
		}
		return myReAdmission;
	}


	/**
	 * Sets the value(s) for <b>reAdmission</b> (Is this hospitalization a readmission?)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public Hospitalization setReAdmission(BooleanDt theValue) {
		myReAdmission = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reAdmission</b> (Is this hospitalization a readmission?)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this hospitalization is a readmission
     * </p> 
	 */
	public Hospitalization setReAdmission( boolean theBoolean) {
		myReAdmission = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}

	/**
	 * Block class for child element: <b>Encounter.hospitalization.accomodation</b> (Where the patient stays during this encounter)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class HospitalizationAccomodation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="bed", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="The bed that is assigned to the patient",
		formalDefinition=""
	)
	private ResourceReferenceDt myBed;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Period during which the patient was assigned the bed",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myBed,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myBed, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>bed</b> (The bed that is assigned to the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getBed() {  
		if (myBed == null) {
			myBed = new ResourceReferenceDt();
		}
		return myBed;
	}


	/**
	 * Gets the value(s) for <b>bed</b> (The bed that is assigned to the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getBedElement() {  
		if (myBed == null) {
			myBed = new ResourceReferenceDt();
		}
		return myBed;
	}


	/**
	 * Sets the value(s) for <b>bed</b> (The bed that is assigned to the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HospitalizationAccomodation setBed(ResourceReferenceDt theValue) {
		myBed = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Period during which the patient was assigned the bed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Gets the value(s) for <b>period</b> (Period during which the patient was assigned the bed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public PeriodDt getPeriodElement() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Sets the value(s) for <b>period</b> (Period during which the patient was assigned the bed)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HospitalizationAccomodation setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  

	}



	/**
	 * Block class for child element: <b>Encounter.location</b> (List of locations the patient has been at)
	 *
     * <p>
     * <b>Definition:</b>
     * List of locations at which the patient has been
     * </p> 
	 */
	@Block()	
	public static class Location 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="location", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Location the encounter takes place",
		formalDefinition="The location where the encounter takes place"
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Time period during which the patient was present at the location",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLocation,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLocation, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>location</b> (Location the encounter takes place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The location where the encounter takes place
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}


	/**
	 * Gets the value(s) for <b>location</b> (Location the encounter takes place).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The location where the encounter takes place
     * </p> 
	 */
	public ResourceReferenceDt getLocationElement() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}


	/**
	 * Sets the value(s) for <b>location</b> (Location the encounter takes place)
	 *
     * <p>
     * <b>Definition:</b>
     * The location where the encounter takes place
     * </p> 
	 */
	public Location setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Time period during which the patient was present at the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Gets the value(s) for <b>period</b> (Time period during which the patient was present at the location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public PeriodDt getPeriodElement() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}


	/**
	 * Sets the value(s) for <b>period</b> (Time period during which the patient was present at the location)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Location setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  

	}




    @Override
    public String getResourceName() {
        return "Encounter";
    }

}
