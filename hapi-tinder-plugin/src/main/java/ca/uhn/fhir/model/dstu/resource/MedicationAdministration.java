















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
 * HAPI/FHIR <b>MedicationAdministration</b> Resource
 * (Administration of medication to a patient)
 *
 * <p>
 * <b>Definition:</b>
 * Describes the event of a patient being given a dose of a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MedicationAdministration">http://hl7.org/fhir/profiles/MedicationAdministration</a> 
 * </p>
 *
 */
@ResourceDef(name="MedicationAdministration", profile="http://hl7.org/fhir/profiles/MedicationAdministration", id="medicationadministration")
public class MedicationAdministration 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.device</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="device", path="MedicationAdministration.device", description="Return administrations with this administration device identity", type="reference"  )
	public static final String SP_DEVICE = "device";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.device</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DEVICE = new ReferenceClientParam(SP_DEVICE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.device</b>".
	 */
	public static final Include INCLUDE_DEVICE = new Include("MedicationAdministration.device");

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return administrations that share this encounter</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="MedicationAdministration.encounter", description="Return administrations that share this encounter", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return administrations that share this encounter</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("MedicationAdministration.encounter");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="MedicationAdministration.identifier", description="Return administrations with this external identity", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Return administrations of this medication</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.medication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="medication", path="MedicationAdministration.medication", description="Return administrations of this medication", type="reference"  )
	public static final String SP_MEDICATION = "medication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Return administrations of this medication</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.medication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MEDICATION = new ReferenceClientParam(SP_MEDICATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.medication</b>".
	 */
	public static final Include INCLUDE_MEDICATION = new Include("MedicationAdministration.medication");

	/**
	 * Search parameter constant for <b>notgiven</b>
	 * <p>
	 * Description: <b>Administrations that were not made</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.wasNotGiven</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="notgiven", path="MedicationAdministration.wasNotGiven", description="Administrations that were not made", type="token"  )
	public static final String SP_NOTGIVEN = "notgiven";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>notgiven</b>
	 * <p>
	 * Description: <b>Administrations that were not made</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.wasNotGiven</b><br/>
	 * </p>
	 */
	public static final TokenClientParam NOTGIVEN = new TokenClientParam(SP_NOTGIVEN);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="MedicationAdministration.patient", description="The identity of a patient to list administrations  for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("MedicationAdministration.patient");

	/**
	 * Search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list administrations from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.prescription</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="prescription", path="MedicationAdministration.prescription", description="The identity of a prescription to list administrations from", type="reference"  )
	public static final String SP_PRESCRIPTION = "prescription";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list administrations from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.prescription</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PRESCRIPTION = new ReferenceClientParam(SP_PRESCRIPTION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationAdministration.prescription</b>".
	 */
	public static final Include INCLUDE_PRESCRIPTION = new Include("MedicationAdministration.prescription");

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="MedicationAdministration.status", description="MedicationAdministration event status (for example one of active/paused/completed/nullified)", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>whengiven</b>
	 * <p>
	 * Description: <b>Date of administration</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationAdministration.whenGiven</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="whengiven", path="MedicationAdministration.whenGiven", description="Date of administration", type="date"  )
	public static final String SP_WHENGIVEN = "whengiven";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>whengiven</b>
	 * <p>
	 * Description: <b>Date of administration</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationAdministration.whenGiven</b><br/>
	 * </p>
	 */
	public static final DateClientParam WHENGIVEN = new DateClientParam(SP_WHENGIVEN);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External identifier",
		formalDefinition="External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="in progress | on hold | completed | entered in error | stopped",
		formalDefinition="Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way."
	)
	private BoundCodeDt<MedicationAdministrationStatusEnum> myStatus;
	
	@Child(name="patient", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who received medication?",
		formalDefinition="The person or animal to whom the medication was given."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="practitioner", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who administered substance?",
		formalDefinition="The individual who was responsible for giving the medication to the patient."
	)
	private ResourceReferenceDt myPractitioner;
	
	@Child(name="encounter", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Encounter administered as part of",
		formalDefinition="The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="prescription", order=5, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.MedicationPrescription.class	})
	@Description(
		shortDefinition="Order administration performed against",
		formalDefinition="The original request, instruction or authority to perform the administration."
	)
	private ResourceReferenceDt myPrescription;
	
	@Child(name="wasNotGiven", type=BooleanDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="True if medication not administered",
		formalDefinition="Set this to true if the record is saying that the medication was NOT administered."
	)
	private BooleanDt myWasNotGiven;
	
	@Child(name="reasonNotGiven", type=CodeableConceptDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Reason administration not performed",
		formalDefinition="A code indicating why the administration was not performed."
	)
	private java.util.List<CodeableConceptDt> myReasonNotGiven;
	
	@Child(name="whenGiven", type=PeriodDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="Start and end time of administration",
		formalDefinition="An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same."
	)
	private PeriodDt myWhenGiven;
	
	@Child(name="medication", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class	})
	@Description(
		shortDefinition="What was administered?",
		formalDefinition="Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="device", order=10, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Device used to administer",
		formalDefinition="The device used in administering the medication to the patient.  E.g. a particular infusion pump"
	)
	private java.util.List<ResourceReferenceDt> myDevice;
	
	@Child(name="dosage", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Medicine administration instructions to the patient/carer",
		formalDefinition="Provides details of how much of the medication was administered"
	)
	private java.util.List<Dosage> myDosage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myPatient,  myPractitioner,  myEncounter,  myPrescription,  myWasNotGiven,  myReasonNotGiven,  myWhenGiven,  myMedication,  myDevice,  myDosage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myPatient, myPractitioner, myEncounter, myPrescription, myWasNotGiven, myReasonNotGiven, myWhenGiven, myMedication, myDevice, myDosage);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public MedicationAdministration setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public MedicationAdministration addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public MedicationAdministration addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public BoundCodeDt<MedicationAdministrationStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationAdministrationStatusEnum>(MedicationAdministrationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Gets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public BoundCodeDt<MedicationAdministrationStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationAdministrationStatusEnum>(MedicationAdministrationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public MedicationAdministration setStatus(BoundCodeDt<MedicationAdministrationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public MedicationAdministration setStatus(MedicationAdministrationStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (Who received medication?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal to whom the medication was given.
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}


	/**
	 * Gets the value(s) for <b>patient</b> (Who received medication?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal to whom the medication was given.
     * </p> 
	 */
	public ResourceReferenceDt getPatientElement() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}


	/**
	 * Sets the value(s) for <b>patient</b> (Who received medication?)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal to whom the medication was given.
     * </p> 
	 */
	public MedicationAdministration setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>practitioner</b> (Who administered substance?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public ResourceReferenceDt getPractitioner() {  
		if (myPractitioner == null) {
			myPractitioner = new ResourceReferenceDt();
		}
		return myPractitioner;
	}


	/**
	 * Gets the value(s) for <b>practitioner</b> (Who administered substance?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public ResourceReferenceDt getPractitionerElement() {  
		if (myPractitioner == null) {
			myPractitioner = new ResourceReferenceDt();
		}
		return myPractitioner;
	}


	/**
	 * Sets the value(s) for <b>practitioner</b> (Who administered substance?)
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public MedicationAdministration setPractitioner(ResourceReferenceDt theValue) {
		myPractitioner = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>encounter</b> (Encounter administered as part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}


	/**
	 * Gets the value(s) for <b>encounter</b> (Encounter administered as part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
     * </p> 
	 */
	public ResourceReferenceDt getEncounterElement() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}


	/**
	 * Sets the value(s) for <b>encounter</b> (Encounter administered as part of)
	 *
     * <p>
     * <b>Definition:</b>
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
     * </p> 
	 */
	public MedicationAdministration setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>prescription</b> (Order administration performed against).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public ResourceReferenceDt getPrescription() {  
		if (myPrescription == null) {
			myPrescription = new ResourceReferenceDt();
		}
		return myPrescription;
	}


	/**
	 * Gets the value(s) for <b>prescription</b> (Order administration performed against).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public ResourceReferenceDt getPrescriptionElement() {  
		if (myPrescription == null) {
			myPrescription = new ResourceReferenceDt();
		}
		return myPrescription;
	}


	/**
	 * Sets the value(s) for <b>prescription</b> (Order administration performed against)
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public MedicationAdministration setPrescription(ResourceReferenceDt theValue) {
		myPrescription = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>wasNotGiven</b> (True if medication not administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public BooleanDt getWasNotGiven() {  
		if (myWasNotGiven == null) {
			myWasNotGiven = new BooleanDt();
		}
		return myWasNotGiven;
	}


	/**
	 * Gets the value(s) for <b>wasNotGiven</b> (True if medication not administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public BooleanDt getWasNotGivenElement() {  
		if (myWasNotGiven == null) {
			myWasNotGiven = new BooleanDt();
		}
		return myWasNotGiven;
	}


	/**
	 * Sets the value(s) for <b>wasNotGiven</b> (True if medication not administered)
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public MedicationAdministration setWasNotGiven(BooleanDt theValue) {
		myWasNotGiven = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>wasNotGiven</b> (True if medication not administered)
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public MedicationAdministration setWasNotGiven( boolean theBoolean) {
		myWasNotGiven = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reasonNotGiven</b> (Reason administration not performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getReasonNotGiven() {  
		if (myReasonNotGiven == null) {
			myReasonNotGiven = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myReasonNotGiven;
	}


	/**
	 * Gets the value(s) for <b>reasonNotGiven</b> (Reason administration not performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getReasonNotGivenElement() {  
		if (myReasonNotGiven == null) {
			myReasonNotGiven = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myReasonNotGiven;
	}


	/**
	 * Sets the value(s) for <b>reasonNotGiven</b> (Reason administration not performed)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public MedicationAdministration setReasonNotGiven(java.util.List<CodeableConceptDt> theValue) {
		myReasonNotGiven = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reasonNotGiven</b> (Reason administration not performed)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public CodeableConceptDt addReasonNotGiven() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getReasonNotGiven().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reasonNotGiven</b> (Reason administration not performed),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public CodeableConceptDt getReasonNotGivenFirstRep() {
		if (getReasonNotGiven().isEmpty()) {
			return addReasonNotGiven();
		}
		return getReasonNotGiven().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>whenGiven</b> (Start and end time of administration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public PeriodDt getWhenGiven() {  
		if (myWhenGiven == null) {
			myWhenGiven = new PeriodDt();
		}
		return myWhenGiven;
	}


	/**
	 * Gets the value(s) for <b>whenGiven</b> (Start and end time of administration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public PeriodDt getWhenGivenElement() {  
		if (myWhenGiven == null) {
			myWhenGiven = new PeriodDt();
		}
		return myWhenGiven;
	}


	/**
	 * Sets the value(s) for <b>whenGiven</b> (Start and end time of administration)
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public MedicationAdministration setWhenGiven(PeriodDt theValue) {
		myWhenGiven = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>medication</b> (What was administered?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedication() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}


	/**
	 * Gets the value(s) for <b>medication</b> (What was administered?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedicationElement() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}


	/**
	 * Sets the value(s) for <b>medication</b> (What was administered?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public MedicationAdministration setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>device</b> (Device used to administer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDevice() {  
		if (myDevice == null) {
			myDevice = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDevice;
	}


	/**
	 * Gets the value(s) for <b>device</b> (Device used to administer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDeviceElement() {  
		if (myDevice == null) {
			myDevice = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDevice;
	}


	/**
	 * Sets the value(s) for <b>device</b> (Device used to administer)
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public MedicationAdministration setDevice(java.util.List<ResourceReferenceDt> theValue) {
		myDevice = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>device</b> (Device used to administer)
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public ResourceReferenceDt addDevice() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDevice().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public java.util.List<Dosage> getDosage() {  
		if (myDosage == null) {
			myDosage = new java.util.ArrayList<Dosage>();
		}
		return myDosage;
	}


	/**
	 * Gets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public java.util.List<Dosage> getDosageElement() {  
		if (myDosage == null) {
			myDosage = new java.util.ArrayList<Dosage>();
		}
		return myDosage;
	}


	/**
	 * Sets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public MedicationAdministration setDosage(java.util.List<Dosage> theValue) {
		myDosage = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public Dosage addDosage() {
		Dosage newType = new Dosage();
		getDosage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dosage</b> (Medicine administration instructions to the patient/carer),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public Dosage getDosageFirstRep() {
		if (getDosage().isEmpty()) {
			return addDosage();
		}
		return getDosage().get(0); 
	}
  
	/**
	 * Block class for child element: <b>MedicationAdministration.dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	@Block()	
	public static class Dosage 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="timing", order=0, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="When dose(s) were given",
		formalDefinition="The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)"
	)
	private IDatatype myTiming;
	
	@Child(name="asNeeded", order=1, min=0, max=1, type={
		BooleanDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="Take \"as needed\" f(or x)",
		formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication"
	)
	private IDatatype myAsNeeded;
	
	@Child(name="site", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Body site administered to",
		formalDefinition="A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\""
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Path of substance into body",
		formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc."
	)
	private CodeableConceptDt myRoute;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="How drug was administered",
		formalDefinition="A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration."
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="quantity", type=QuantityDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Amount administered in one dose",
		formalDefinition="The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection."
	)
	private QuantityDt myQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Dose quantity per unit of time",
		formalDefinition="Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity."
	)
	private RatioDt myRate;
	
	@Child(name="maxDosePerPeriod", type=RatioDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Total dose that was consumed per unit of time",
		formalDefinition="The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours."
	)
	private RatioDt myMaxDosePerPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTiming,  myAsNeeded,  mySite,  myRoute,  myMethod,  myQuantity,  myRate,  myMaxDosePerPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTiming, myAsNeeded, mySite, myRoute, myMethod, myQuantity, myRate, myMaxDosePerPeriod);
	}

	/**
	 * Gets the value(s) for <b>timing[x]</b> (When dose(s) were given).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public IDatatype getTiming() {  
		return myTiming;
	}


	/**
	 * Gets the value(s) for <b>timing[x]</b> (When dose(s) were given).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public IDatatype getTimingElement() {  
		return myTiming;
	}


	/**
	 * Sets the value(s) for <b>timing[x]</b> (When dose(s) were given)
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public Dosage setTiming(IDatatype theValue) {
		myTiming = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>asNeeded[x]</b> (Take \"as needed\" f(or x)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public IDatatype getAsNeeded() {  
		return myAsNeeded;
	}


	/**
	 * Gets the value(s) for <b>asNeeded[x]</b> (Take \"as needed\" f(or x)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public IDatatype getAsNeededElement() {  
		return myAsNeeded;
	}


	/**
	 * Sets the value(s) for <b>asNeeded[x]</b> (Take \"as needed\" f(or x))
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public Dosage setAsNeeded(IDatatype theValue) {
		myAsNeeded = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>site</b> (Body site administered to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}


	/**
	 * Gets the value(s) for <b>site</b> (Body site administered to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public CodeableConceptDt getSiteElement() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}


	/**
	 * Sets the value(s) for <b>site</b> (Body site administered to)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public Dosage setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>route</b> (Path of substance into body).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public CodeableConceptDt getRoute() {  
		if (myRoute == null) {
			myRoute = new CodeableConceptDt();
		}
		return myRoute;
	}


	/**
	 * Gets the value(s) for <b>route</b> (Path of substance into body).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public CodeableConceptDt getRouteElement() {  
		if (myRoute == null) {
			myRoute = new CodeableConceptDt();
		}
		return myRoute;
	}


	/**
	 * Sets the value(s) for <b>route</b> (Path of substance into body)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public Dosage setRoute(CodeableConceptDt theValue) {
		myRoute = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>method</b> (How drug was administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}


	/**
	 * Gets the value(s) for <b>method</b> (How drug was administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public CodeableConceptDt getMethodElement() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}


	/**
	 * Sets the value(s) for <b>method</b> (How drug was administered)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public Dosage setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount administered in one dose).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}


	/**
	 * Gets the value(s) for <b>quantity</b> (Amount administered in one dose).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public QuantityDt getQuantityElement() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}


	/**
	 * Sets the value(s) for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>rate</b> (Dose quantity per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
     * </p> 
	 */
	public RatioDt getRate() {  
		if (myRate == null) {
			myRate = new RatioDt();
		}
		return myRate;
	}


	/**
	 * Gets the value(s) for <b>rate</b> (Dose quantity per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
     * </p> 
	 */
	public RatioDt getRateElement() {  
		if (myRate == null) {
			myRate = new RatioDt();
		}
		return myRate;
	}


	/**
	 * Sets the value(s) for <b>rate</b> (Dose quantity per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
     * </p> 
	 */
	public Dosage setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> (Total dose that was consumed per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriod() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}


	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> (Total dose that was consumed per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriodElement() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}


	/**
	 * Sets the value(s) for <b>maxDosePerPeriod</b> (Total dose that was consumed per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public Dosage setMaxDosePerPeriod(RatioDt theValue) {
		myMaxDosePerPeriod = theValue;
		return this;
	}

  

	}




    @Override
    public String getResourceName() {
        return "MedicationAdministration";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
