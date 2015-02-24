















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
 * HAPI/FHIR <b>DiagnosticReport</b> Resource
 * (A Diagnostic report - a combination of request information, atomic results, images, interpretation, as well as formatted reports)
 *
 * <p>
 * <b>Definition:</b>
 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretation, and formatted representation of diagnostic reports
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support reporting for any diagnostic report into a clinical data repository.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DiagnosticReport">http://hl7.org/fhir/profiles/DiagnosticReport</a> 
 * </p>
 *
 */
@ResourceDef(name="DiagnosticReport", profile="http://hl7.org/fhir/profiles/DiagnosticReport", id="diagnosticreport")
public class DiagnosticReport 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.status</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DiagnosticReport.status", description="The status of the report", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.status</b><br>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>issued</b>
	 * <p>
	 * Description: <b>When the report was issued</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>DiagnosticReport.issued</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="issued", path="DiagnosticReport.issued", description="When the report was issued", type="date"  )
	public static final String SP_ISSUED = "issued";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>issued</b>
	 * <p>
	 * Description: <b>When the report was issued</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>DiagnosticReport.issued</b><br>
	 * </p>
	 */
	public static final DateClientParam ISSUED = new DateClientParam(SP_ISSUED);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the report</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.subject</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DiagnosticReport.subject", description="The subject of the report", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the report</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.subject</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DiagnosticReport.subject");

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who was the source of the report (organization)</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.performer</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="DiagnosticReport.performer", description="Who was the source of the report (organization)", type="reference"  )
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>Who was the source of the report (organization)</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.performer</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam PERFORMER = new ReferenceClientParam(SP_PERFORMER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("DiagnosticReport.performer");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DiagnosticReport.identifier", description="An identifier for the report", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>service</b>
	 * <p>
	 * Description: <b>Which diagnostic discipline/department created the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.serviceCategory</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="service", path="DiagnosticReport.serviceCategory", description="Which diagnostic discipline/department created the report", type="token"  )
	public static final String SP_SERVICE = "service";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>service</b>
	 * <p>
	 * Description: <b>Which diagnostic discipline/department created the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.serviceCategory</b><br>
	 * </p>
	 */
	public static final TokenClientParam SERVICE = new TokenClientParam(SP_SERVICE);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The clinically relevant time of the report</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>DiagnosticReport.diagnostic[x]</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="DiagnosticReport.diagnostic[x]", description="The clinically relevant time of the report", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The clinically relevant time of the report</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>DiagnosticReport.diagnostic[x]</b><br>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>The specimen details</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.specimen</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="DiagnosticReport.specimen", description="The specimen details", type="reference"  )
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>The specimen details</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.specimen</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam SPECIMEN = new ReferenceClientParam(SP_SPECIMEN);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.specimen</b>".
	 */
	public static final Include INCLUDE_SPECIMEN = new Include("DiagnosticReport.specimen");

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.name</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="DiagnosticReport.name", description="The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)", type="token"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.name</b><br>
	 * </p>
	 */
	public static final TokenClientParam NAME = new TokenClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>result</b>
	 * <p>
	 * Description: <b>Link to an atomic result (observation resource)</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.result</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="result", path="DiagnosticReport.result", description="Link to an atomic result (observation resource)", type="reference"  )
	public static final String SP_RESULT = "result";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>result</b>
	 * <p>
	 * Description: <b>Link to an atomic result (observation resource)</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.result</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam RESULT = new ReferenceClientParam(SP_RESULT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.result</b>".
	 */
	public static final Include INCLUDE_RESULT = new Include("DiagnosticReport.result");

	/**
	 * Search parameter constant for <b>diagnosis</b>
	 * <p>
	 * Description: <b>A coded diagnosis on the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.codedDiagnosis</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="diagnosis", path="DiagnosticReport.codedDiagnosis", description="A coded diagnosis on the report", type="token"  )
	public static final String SP_DIAGNOSIS = "diagnosis";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>diagnosis</b>
	 * <p>
	 * Description: <b>A coded diagnosis on the report</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>DiagnosticReport.codedDiagnosis</b><br>
	 * </p>
	 */
	public static final TokenClientParam DIAGNOSIS = new TokenClientParam(SP_DIAGNOSIS);

	/**
	 * Search parameter constant for <b>image</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.image.link</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="image", path="DiagnosticReport.image.link", description="", type="reference"  )
	public static final String SP_IMAGE = "image";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>image</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.image.link</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam IMAGE = new ReferenceClientParam(SP_IMAGE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.image.link</b>".
	 */
	public static final Include INCLUDE_IMAGE_LINK = new Include("DiagnosticReport.image.link");

	/**
	 * Search parameter constant for <b>request</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.requestDetail</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="request", path="DiagnosticReport.requestDetail", description="", type="reference"  )
	public static final String SP_REQUEST = "request";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>request</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>DiagnosticReport.requestDetail</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam REQUEST = new ReferenceClientParam(SP_REQUEST);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticReport.requestDetail</b>".
	 */
	public static final Include INCLUDE_REQUESTDETAIL = new Include("DiagnosticReport.requestDetail");


	@Child(name="name", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name/Code for this diagnostic report",
		formalDefinition="A code or name that describes this diagnostic report"
	)
	private CodeableConceptDt myName;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="registered | partial | final | corrected +",
		formalDefinition="The status of the diagnostic report as a whole"
	)
	private BoundCodeDt<DiagnosticReportStatusEnum> myStatus;
	
	@Child(name="issued", type=DateTimeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Date this version was released",
		formalDefinition="The date and/or time that this version of the report was released from the source diagnostic service"
	)
	private DateTimeDt myIssued;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="The subject of the report, usually, but not always, the patient",
		formalDefinition="The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="performer", order=4, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Responsible Diagnostic Service",
		formalDefinition="The diagnostic service that is responsible for issuing the report"
	)
	private ResourceReferenceDt myPerformer;
	
	@Child(name="identifier", type=IdentifierDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Id for external references to this report",
		formalDefinition="The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="requestDetail", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.class	})
	@Description(
		shortDefinition="What was requested",
		formalDefinition="Details concerning a test requested."
	)
	private java.util.List<ResourceReferenceDt> myRequestDetail;
	
	@Child(name="serviceCategory", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Biochemistry, Hematology etc.",
		formalDefinition="The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI"
	)
	private CodeableConceptDt myServiceCategory;
	
	@Child(name="diagnostic", order=8, min=1, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="Physiologically Relevant time/time-period for report",
		formalDefinition="The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself"
	)
	private IDatatype myDiagnostic;
	
	@Child(name="specimen", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Specimen.class	})
	@Description(
		shortDefinition="Specimens this report is based on",
		formalDefinition="Details about the specimens on which this Disagnostic report is based"
	)
	private java.util.List<ResourceReferenceDt> mySpecimen;
	
	@Child(name="result", order=10, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Observation.class	})
	@Description(
		shortDefinition="Observations - simple, or complex nested groups",
		formalDefinition="Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\")"
	)
	private java.util.List<ResourceReferenceDt> myResult;
	
	@Child(name="imagingStudy", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.ImagingStudy.class	})
	@Description(
		shortDefinition="Reference to full details of imaging associated with the diagnostic report",
		formalDefinition="One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images"
	)
	private java.util.List<ResourceReferenceDt> myImagingStudy;
	
	@Child(name="image", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Key images associated with this report",
		formalDefinition="A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)"
	)
	private java.util.List<Image> myImage;
	
	@Child(name="conclusion", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="Clinical Interpretation of test results",
		formalDefinition="Concise and clinically contextualized narrative interpretation of the diagnostic report"
	)
	private StringDt myConclusion;
	
	@Child(name="codedDiagnosis", type=CodeableConceptDt.class, order=14, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Codes for the conclusion",
		formalDefinition=""
	)
	private java.util.List<CodeableConceptDt> myCodedDiagnosis;
	
	@Child(name="presentedForm", type=AttachmentDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Entire Report as issued",
		formalDefinition="Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent."
	)
	private java.util.List<AttachmentDt> myPresentedForm;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myStatus,  myIssued,  mySubject,  myPerformer,  myIdentifier,  myRequestDetail,  myServiceCategory,  myDiagnostic,  mySpecimen,  myResult,  myImagingStudy,  myImage,  myConclusion,  myCodedDiagnosis,  myPresentedForm);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myStatus, myIssued, mySubject, myPerformer, myIdentifier, myRequestDetail, myServiceCategory, myDiagnostic, mySpecimen, myResult, myImagingStudy, myImage, myConclusion, myCodedDiagnosis, myPresentedForm);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name/Code for this diagnostic report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code or name that describes this diagnostic report
     * </p> 
	 */
	public CodeableConceptDt getName() {  
		if (myName == null) {
			myName = new CodeableConceptDt();
		}
		return myName;
	}


	/**
	 * Gets the value(s) for <b>name</b> (Name/Code for this diagnostic report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code or name that describes this diagnostic report
     * </p> 
	 */
	public CodeableConceptDt getNameElement() {  
		if (myName == null) {
			myName = new CodeableConceptDt();
		}
		return myName;
	}


	/**
	 * Sets the value(s) for <b>name</b> (Name/Code for this diagnostic report)
	 *
     * <p>
     * <b>Definition:</b>
     * A code or name that describes this diagnostic report
     * </p> 
	 */
	public DiagnosticReport setName(CodeableConceptDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (registered | partial | final | corrected +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public BoundCodeDt<DiagnosticReportStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticReportStatusEnum>(DiagnosticReportStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Gets the value(s) for <b>status</b> (registered | partial | final | corrected +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public BoundCodeDt<DiagnosticReportStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticReportStatusEnum>(DiagnosticReportStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Sets the value(s) for <b>status</b> (registered | partial | final | corrected +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public DiagnosticReport setStatus(BoundCodeDt<DiagnosticReportStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (registered | partial | final | corrected +)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the diagnostic report as a whole
     * </p> 
	 */
	public DiagnosticReport setStatus(DiagnosticReportStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>issued</b> (Date this version was released).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DateTimeDt getIssued() {  
		if (myIssued == null) {
			myIssued = new DateTimeDt();
		}
		return myIssued;
	}


	/**
	 * Gets the value(s) for <b>issued</b> (Date this version was released).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DateTimeDt getIssuedElement() {  
		if (myIssued == null) {
			myIssued = new DateTimeDt();
		}
		return myIssued;
	}


	/**
	 * Sets the value(s) for <b>issued</b> (Date this version was released)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DiagnosticReport setIssued(DateTimeDt theValue) {
		myIssued = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>issued</b> (Date this version was released)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DiagnosticReport setIssued( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIssued = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>issued</b> (Date this version was released)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the report was released from the source diagnostic service
     * </p> 
	 */
	public DiagnosticReport setIssuedWithSecondsPrecision( Date theDate) {
		myIssued = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (The subject of the report, usually, but not always, the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Gets the value(s) for <b>subject</b> (The subject of the report, usually, but not always, the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources
     * </p> 
	 */
	public ResourceReferenceDt getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (The subject of the report, usually, but not always, the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources
     * </p> 
	 */
	public DiagnosticReport setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>performer</b> (Responsible Diagnostic Service).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The diagnostic service that is responsible for issuing the report
     * </p> 
	 */
	public ResourceReferenceDt getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new ResourceReferenceDt();
		}
		return myPerformer;
	}


	/**
	 * Gets the value(s) for <b>performer</b> (Responsible Diagnostic Service).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The diagnostic service that is responsible for issuing the report
     * </p> 
	 */
	public ResourceReferenceDt getPerformerElement() {  
		if (myPerformer == null) {
			myPerformer = new ResourceReferenceDt();
		}
		return myPerformer;
	}


	/**
	 * Sets the value(s) for <b>performer</b> (Responsible Diagnostic Service)
	 *
     * <p>
     * <b>Definition:</b>
     * The diagnostic service that is responsible for issuing the report
     * </p> 
	 */
	public DiagnosticReport setPerformer(ResourceReferenceDt theValue) {
		myPerformer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (Id for external references to this report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (Id for external references to this report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public IdentifierDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (Id for external references to this report)
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public DiagnosticReport setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Id for external references to this report)
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public DiagnosticReport setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Id for external references to this report)
	 *
     * <p>
     * <b>Definition:</b>
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider
     * </p> 
	 */
	public DiagnosticReport setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requestDetail</b> (What was requested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRequestDetail() {  
		if (myRequestDetail == null) {
			myRequestDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRequestDetail;
	}


	/**
	 * Gets the value(s) for <b>requestDetail</b> (What was requested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRequestDetailElement() {  
		if (myRequestDetail == null) {
			myRequestDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRequestDetail;
	}


	/**
	 * Sets the value(s) for <b>requestDetail</b> (What was requested)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public DiagnosticReport setRequestDetail(java.util.List<ResourceReferenceDt> theValue) {
		myRequestDetail = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>requestDetail</b> (What was requested)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning a test requested.
     * </p> 
	 */
	public ResourceReferenceDt addRequestDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getRequestDetail().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>serviceCategory</b> (Biochemistry, Hematology etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI
     * </p> 
	 */
	public CodeableConceptDt getServiceCategory() {  
		if (myServiceCategory == null) {
			myServiceCategory = new CodeableConceptDt();
		}
		return myServiceCategory;
	}


	/**
	 * Gets the value(s) for <b>serviceCategory</b> (Biochemistry, Hematology etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI
     * </p> 
	 */
	public CodeableConceptDt getServiceCategoryElement() {  
		if (myServiceCategory == null) {
			myServiceCategory = new CodeableConceptDt();
		}
		return myServiceCategory;
	}


	/**
	 * Sets the value(s) for <b>serviceCategory</b> (Biochemistry, Hematology etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI
     * </p> 
	 */
	public DiagnosticReport setServiceCategory(CodeableConceptDt theValue) {
		myServiceCategory = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>diagnostic[x]</b> (Physiologically Relevant time/time-period for report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getDiagnostic() {  
		return myDiagnostic;
	}


	/**
	 * Gets the value(s) for <b>diagnostic[x]</b> (Physiologically Relevant time/time-period for report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public IDatatype getDiagnosticElement() {  
		return myDiagnostic;
	}


	/**
	 * Sets the value(s) for <b>diagnostic[x]</b> (Physiologically Relevant time/time-period for report)
	 *
     * <p>
     * <b>Definition:</b>
     * The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself
     * </p> 
	 */
	public DiagnosticReport setDiagnostic(IDatatype theValue) {
		myDiagnostic = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specimen</b> (Specimens this report is based on).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about the specimens on which this Disagnostic report is based
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySpecimen;
	}


	/**
	 * Gets the value(s) for <b>specimen</b> (Specimens this report is based on).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about the specimens on which this Disagnostic report is based
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSpecimenElement() {  
		if (mySpecimen == null) {
			mySpecimen = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySpecimen;
	}


	/**
	 * Sets the value(s) for <b>specimen</b> (Specimens this report is based on)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about the specimens on which this Disagnostic report is based
     * </p> 
	 */
	public DiagnosticReport setSpecimen(java.util.List<ResourceReferenceDt> theValue) {
		mySpecimen = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>specimen</b> (Specimens this report is based on)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about the specimens on which this Disagnostic report is based
     * </p> 
	 */
	public ResourceReferenceDt addSpecimen() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSpecimen().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>result</b> (Observations - simple, or complex nested groups).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \&quot;atomic\&quot; results), or they can be grouping observations that include references to other members of the group (e.g. \&quot;panels\&quot;)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getResult() {  
		if (myResult == null) {
			myResult = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myResult;
	}


	/**
	 * Gets the value(s) for <b>result</b> (Observations - simple, or complex nested groups).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \&quot;atomic\&quot; results), or they can be grouping observations that include references to other members of the group (e.g. \&quot;panels\&quot;)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getResultElement() {  
		if (myResult == null) {
			myResult = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myResult;
	}


	/**
	 * Sets the value(s) for <b>result</b> (Observations - simple, or complex nested groups)
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \&quot;atomic\&quot; results), or they can be grouping observations that include references to other members of the group (e.g. \&quot;panels\&quot;)
     * </p> 
	 */
	public DiagnosticReport setResult(java.util.List<ResourceReferenceDt> theValue) {
		myResult = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>result</b> (Observations - simple, or complex nested groups)
	 *
     * <p>
     * <b>Definition:</b>
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \&quot;atomic\&quot; results), or they can be grouping observations that include references to other members of the group (e.g. \&quot;panels\&quot;)
     * </p> 
	 */
	public ResourceReferenceDt addResult() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getResult().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>imagingStudy</b> (Reference to full details of imaging associated with the diagnostic report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getImagingStudy() {  
		if (myImagingStudy == null) {
			myImagingStudy = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myImagingStudy;
	}


	/**
	 * Gets the value(s) for <b>imagingStudy</b> (Reference to full details of imaging associated with the diagnostic report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getImagingStudyElement() {  
		if (myImagingStudy == null) {
			myImagingStudy = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myImagingStudy;
	}


	/**
	 * Sets the value(s) for <b>imagingStudy</b> (Reference to full details of imaging associated with the diagnostic report)
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images
     * </p> 
	 */
	public DiagnosticReport setImagingStudy(java.util.List<ResourceReferenceDt> theValue) {
		myImagingStudy = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>imagingStudy</b> (Reference to full details of imaging associated with the diagnostic report)
	 *
     * <p>
     * <b>Definition:</b>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images
     * </p> 
	 */
	public ResourceReferenceDt addImagingStudy() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getImagingStudy().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>image</b> (Key images associated with this report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)
     * </p> 
	 */
	public java.util.List<Image> getImage() {  
		if (myImage == null) {
			myImage = new java.util.ArrayList<Image>();
		}
		return myImage;
	}


	/**
	 * Gets the value(s) for <b>image</b> (Key images associated with this report).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)
     * </p> 
	 */
	public java.util.List<Image> getImageElement() {  
		if (myImage == null) {
			myImage = new java.util.ArrayList<Image>();
		}
		return myImage;
	}


	/**
	 * Sets the value(s) for <b>image</b> (Key images associated with this report)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)
     * </p> 
	 */
	public DiagnosticReport setImage(java.util.List<Image> theValue) {
		myImage = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>image</b> (Key images associated with this report)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)
     * </p> 
	 */
	public Image addImage() {
		Image newType = new Image();
		getImage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>image</b> (Key images associated with this report),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)
     * </p> 
	 */
	public Image getImageFirstRep() {
		if (getImage().isEmpty()) {
			return addImage();
		}
		return getImage().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>conclusion</b> (Clinical Interpretation of test results).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public StringDt getConclusion() {  
		if (myConclusion == null) {
			myConclusion = new StringDt();
		}
		return myConclusion;
	}


	/**
	 * Gets the value(s) for <b>conclusion</b> (Clinical Interpretation of test results).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public StringDt getConclusionElement() {  
		if (myConclusion == null) {
			myConclusion = new StringDt();
		}
		return myConclusion;
	}


	/**
	 * Sets the value(s) for <b>conclusion</b> (Clinical Interpretation of test results)
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public DiagnosticReport setConclusion(StringDt theValue) {
		myConclusion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>conclusion</b> (Clinical Interpretation of test results)
	 *
     * <p>
     * <b>Definition:</b>
     * Concise and clinically contextualized narrative interpretation of the diagnostic report
     * </p> 
	 */
	public DiagnosticReport setConclusion( String theString) {
		myConclusion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>codedDiagnosis</b> (Codes for the conclusion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCodedDiagnosis() {  
		if (myCodedDiagnosis == null) {
			myCodedDiagnosis = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCodedDiagnosis;
	}


	/**
	 * Gets the value(s) for <b>codedDiagnosis</b> (Codes for the conclusion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCodedDiagnosisElement() {  
		if (myCodedDiagnosis == null) {
			myCodedDiagnosis = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCodedDiagnosis;
	}


	/**
	 * Sets the value(s) for <b>codedDiagnosis</b> (Codes for the conclusion)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DiagnosticReport setCodedDiagnosis(java.util.List<CodeableConceptDt> theValue) {
		myCodedDiagnosis = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>codedDiagnosis</b> (Codes for the conclusion)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt addCodedDiagnosis() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCodedDiagnosis().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>codedDiagnosis</b> (Codes for the conclusion),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getCodedDiagnosisFirstRep() {
		if (getCodedDiagnosis().isEmpty()) {
			return addCodedDiagnosis();
		}
		return getCodedDiagnosis().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>presentedForm</b> (Entire Report as issued).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public java.util.List<AttachmentDt> getPresentedForm() {  
		if (myPresentedForm == null) {
			myPresentedForm = new java.util.ArrayList<AttachmentDt>();
		}
		return myPresentedForm;
	}


	/**
	 * Gets the value(s) for <b>presentedForm</b> (Entire Report as issued).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public java.util.List<AttachmentDt> getPresentedFormElement() {  
		if (myPresentedForm == null) {
			myPresentedForm = new java.util.ArrayList<AttachmentDt>();
		}
		return myPresentedForm;
	}


	/**
	 * Sets the value(s) for <b>presentedForm</b> (Entire Report as issued)
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public DiagnosticReport setPresentedForm(java.util.List<AttachmentDt> theValue) {
		myPresentedForm = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>presentedForm</b> (Entire Report as issued)
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public AttachmentDt addPresentedForm() {
		AttachmentDt newType = new AttachmentDt();
		getPresentedForm().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>presentedForm</b> (Entire Report as issued),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     * </p> 
	 */
	public AttachmentDt getPresentedFormFirstRep() {
		if (getPresentedForm().isEmpty()) {
			return addPresentedForm();
		}
		return getPresentedForm().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DiagnosticReport.image</b> (Key images associated with this report)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)
     * </p> 
	 */
	@Block()	
	public static class Image 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="comment", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Comment about the image (e.g. explanation)",
		formalDefinition="A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features"
	)
	private StringDt myComment;
	
	@Child(name="link", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Media.class	})
	@Description(
		shortDefinition="Reference to the image source",
		formalDefinition=""
	)
	private ResourceReferenceDt myLink;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myComment,  myLink);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myComment, myLink);
	}

	/**
	 * Gets the value(s) for <b>comment</b> (Comment about the image (e.g. explanation)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features
     * </p> 
	 */
	public StringDt getComment() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}


	/**
	 * Gets the value(s) for <b>comment</b> (Comment about the image (e.g. explanation)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features
     * </p> 
	 */
	public StringDt getCommentElement() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}


	/**
	 * Sets the value(s) for <b>comment</b> (Comment about the image (e.g. explanation))
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features
     * </p> 
	 */
	public Image setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>comment</b> (Comment about the image (e.g. explanation))
	 *
     * <p>
     * <b>Definition:</b>
     * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features
     * </p> 
	 */
	public Image setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>link</b> (Reference to the image source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getLink() {  
		if (myLink == null) {
			myLink = new ResourceReferenceDt();
		}
		return myLink;
	}


	/**
	 * Gets the value(s) for <b>link</b> (Reference to the image source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getLinkElement() {  
		if (myLink == null) {
			myLink = new ResourceReferenceDt();
		}
		return myLink;
	}


	/**
	 * Sets the value(s) for <b>link</b> (Reference to the image source)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Image setLink(ResourceReferenceDt theValue) {
		myLink = theValue;
		return this;
	}

  

	}




    @Override
    public String getResourceName() {
        return "DiagnosticReport";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
