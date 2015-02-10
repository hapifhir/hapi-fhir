















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
 * HAPI/FHIR <b>DocumentManifest</b> Resource
 * (A manifest that defines a set of documents)
 *
 * <p>
 * <b>Definition:</b>
 * A manifest that defines a set of documents
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DocumentManifest">http://hl7.org/fhir/profiles/DocumentManifest</a> 
 * </p>
 *
 */
@ResourceDef(name="DocumentManifest", profile="http://hl7.org/fhir/profiles/DocumentManifest", id="documentmanifest")
public class DocumentManifest 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.masterIdentifier | DocumentManifest.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DocumentManifest.masterIdentifier | DocumentManifest.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.masterIdentifier | DocumentManifest.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DocumentManifest.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DocumentManifest.subject");

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="DocumentManifest.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.recipient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="recipient", path="DocumentManifest.recipient", description="", type="reference"  )
	public static final String SP_RECIPIENT = "recipient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.recipient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RECIPIENT = new ReferenceClientParam(SP_RECIPIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.recipient</b>".
	 */
	public static final Include INCLUDE_RECIPIENT = new Include("DocumentManifest.recipient");

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="DocumentManifest.author", description="", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("DocumentManifest.author");

	/**
	 * Search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentManifest.created</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="created", path="DocumentManifest.created", description="", type="date"  )
	public static final String SP_CREATED = "created";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentManifest.created</b><br/>
	 * </p>
	 */
	public static final DateClientParam CREATED = new DateClientParam(SP_CREATED);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DocumentManifest.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>supersedes</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.supercedes</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supersedes", path="DocumentManifest.supercedes", description="", type="reference"  )
	public static final String SP_SUPERSEDES = "supersedes";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>supersedes</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.supercedes</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUPERSEDES = new ReferenceClientParam(SP_SUPERSEDES);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.supercedes</b>".
	 */
	public static final Include INCLUDE_SUPERCEDES = new Include("DocumentManifest.supercedes");

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentManifest.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="DocumentManifest.description", description="", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentManifest.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.confidentiality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="confidentiality", path="DocumentManifest.confidentiality", description="", type="token"  )
	public static final String SP_CONFIDENTIALITY = "confidentiality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.confidentiality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONFIDENTIALITY = new TokenClientParam(SP_CONFIDENTIALITY);

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.content</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="content", path="DocumentManifest.content", description="", type="reference"  )
	public static final String SP_CONTENT = "content";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.content</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONTENT = new ReferenceClientParam(SP_CONTENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentManifest.content</b>".
	 */
	public static final Include INCLUDE_CONTENT = new Include("DocumentManifest.content");


	@Child(name="masterIdentifier", type=IdentifierDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Unique Identifier for the set of documents",
		formalDefinition="A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts"
	)
	private IdentifierDt myMasterIdentifier;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Other identifiers for the manifest",
		formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=2, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="The subject of the set of documents",
		formalDefinition="Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)"
	)
	private java.util.List<ResourceReferenceDt> mySubject;
	
	@Child(name="recipient", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Intended to get notified about this set of documents",
		formalDefinition="A patient, practitioner, or organization for which this set of documents is intended"
	)
	private java.util.List<ResourceReferenceDt> myRecipient;
	
	@Child(name="type", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="What kind of document set this is",
		formalDefinition="Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider"
	)
	private CodeableConceptDt myType;
	
	@Child(name="author", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who and/or what authored the document",
		formalDefinition="Identifies who is responsible for adding the information to the document"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="created", type=DateTimeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="When this document manifest created",
		formalDefinition="When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)"
	)
	private DateTimeDt myCreated;
	
	@Child(name="source", type=UriDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="The source system/application/software",
		formalDefinition="Identifies the source system, application, or software that produced the document manifest"
	)
	private UriDt mySource;
	
	@Child(name="status", type=CodeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="current | superceded | entered in error",
		formalDefinition="The status of this document manifest"
	)
	private BoundCodeDt<DocumentReferenceStatusEnum> myStatus;
	
	@Child(name="supercedes", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.DocumentManifest.class	})
	@Description(
		shortDefinition="If this document manifest replaces another",
		formalDefinition="Whether this document manifest replaces another"
	)
	private ResourceReferenceDt mySupercedes;
	
	@Child(name="description", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Human-readable description (title)",
		formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\""
	)
	private StringDt myDescription;
	
	@Child(name="confidentiality", type=CodeableConceptDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Sensitivity of set of documents",
		formalDefinition="A code specifying the level of confidentiality of this set of Documents"
	)
	private CodeableConceptDt myConfidentiality;
	
	@Child(name="content", order=12, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.DocumentReference.class, 		ca.uhn.fhir.model.dstu.resource.Binary.class, 		ca.uhn.fhir.model.dstu.resource.Media.class	})
	@Description(
		shortDefinition="Contents of this set of documents",
		formalDefinition="The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed"
	)
	private java.util.List<ResourceReferenceDt> myContent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMasterIdentifier,  myIdentifier,  mySubject,  myRecipient,  myType,  myAuthor,  myCreated,  mySource,  myStatus,  mySupercedes,  myDescription,  myConfidentiality,  myContent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMasterIdentifier, myIdentifier, mySubject, myRecipient, myType, myAuthor, myCreated, mySource, myStatus, mySupercedes, myDescription, myConfidentiality, myContent);
	}

	/**
	 * Gets the value(s) for <b>masterIdentifier</b> (Unique Identifier for the set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public IdentifierDt getMasterIdentifier() {  
		if (myMasterIdentifier == null) {
			myMasterIdentifier = new IdentifierDt();
		}
		return myMasterIdentifier;
	}


	/**
	 * Gets the value(s) for <b>masterIdentifier</b> (Unique Identifier for the set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public IdentifierDt getMasterIdentifierElement() {  
		if (myMasterIdentifier == null) {
			myMasterIdentifier = new IdentifierDt();
		}
		return myMasterIdentifier;
	}


	/**
	 * Sets the value(s) for <b>masterIdentifier</b> (Unique Identifier for the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier(IdentifierDt theValue) {
		myMasterIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>masterIdentifier</b> (Unique Identifier for the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myMasterIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>masterIdentifier</b> (Unique Identifier for the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier( String theSystem,  String theValue) {
		myMasterIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (Other identifiers for the manifest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (Other identifiers for the manifest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public DocumentManifest setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Other identifiers for the manifest),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentManifest addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentManifest addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (The subject of the set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSubject() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySubject;
	}


	/**
	 * Gets the value(s) for <b>subject</b> (The subject of the set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSubjectElement() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySubject;
	}


	/**
	 * Sets the value(s) for <b>subject</b> (The subject of the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public DocumentManifest setSubject(java.util.List<ResourceReferenceDt> theValue) {
		mySubject = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>subject</b> (The subject of the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public ResourceReferenceDt addSubject() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSubject().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>recipient</b> (Intended to get notified about this set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRecipient() {  
		if (myRecipient == null) {
			myRecipient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRecipient;
	}


	/**
	 * Gets the value(s) for <b>recipient</b> (Intended to get notified about this set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRecipientElement() {  
		if (myRecipient == null) {
			myRecipient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myRecipient;
	}


	/**
	 * Sets the value(s) for <b>recipient</b> (Intended to get notified about this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public DocumentManifest setRecipient(java.util.List<ResourceReferenceDt> theValue) {
		myRecipient = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>recipient</b> (Intended to get notified about this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public ResourceReferenceDt addRecipient() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getRecipient().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> (What kind of document set this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}


	/**
	 * Gets the value(s) for <b>type</b> (What kind of document set this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public CodeableConceptDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (What kind of document set this is)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public DocumentManifest setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Who and/or what authored the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}


	/**
	 * Gets the value(s) for <b>author</b> (Who and/or what authored the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthorElement() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}


	/**
	 * Sets the value(s) for <b>author</b> (Who and/or what authored the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public DocumentManifest setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>author</b> (Who and/or what authored the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>created</b> (When this document manifest created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DateTimeDt getCreated() {  
		if (myCreated == null) {
			myCreated = new DateTimeDt();
		}
		return myCreated;
	}


	/**
	 * Gets the value(s) for <b>created</b> (When this document manifest created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DateTimeDt getCreatedElement() {  
		if (myCreated == null) {
			myCreated = new DateTimeDt();
		}
		return myCreated;
	}


	/**
	 * Sets the value(s) for <b>created</b> (When this document manifest created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreated(DateTimeDt theValue) {
		myCreated = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>created</b> (When this document manifest created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreated( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myCreated = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>created</b> (When this document manifest created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreatedWithSecondsPrecision( Date theDate) {
		myCreated = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> (The source system/application/software).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public UriDt getSource() {  
		if (mySource == null) {
			mySource = new UriDt();
		}
		return mySource;
	}


	/**
	 * Gets the value(s) for <b>source</b> (The source system/application/software).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public UriDt getSourceElement() {  
		if (mySource == null) {
			mySource = new UriDt();
		}
		return mySource;
	}


	/**
	 * Sets the value(s) for <b>source</b> (The source system/application/software)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public DocumentManifest setSource(UriDt theValue) {
		mySource = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>source</b> (The source system/application/software)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public DocumentManifest setSource( String theUri) {
		mySource = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (current | superceded | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public BoundCodeDt<DocumentReferenceStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DocumentReferenceStatusEnum>(DocumentReferenceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Gets the value(s) for <b>status</b> (current | superceded | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public BoundCodeDt<DocumentReferenceStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DocumentReferenceStatusEnum>(DocumentReferenceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}


	/**
	 * Sets the value(s) for <b>status</b> (current | superceded | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public DocumentManifest setStatus(BoundCodeDt<DocumentReferenceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (current | superceded | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public DocumentManifest setStatus(DocumentReferenceStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>supercedes</b> (If this document manifest replaces another).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public ResourceReferenceDt getSupercedes() {  
		if (mySupercedes == null) {
			mySupercedes = new ResourceReferenceDt();
		}
		return mySupercedes;
	}


	/**
	 * Gets the value(s) for <b>supercedes</b> (If this document manifest replaces another).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public ResourceReferenceDt getSupercedesElement() {  
		if (mySupercedes == null) {
			mySupercedes = new ResourceReferenceDt();
		}
		return mySupercedes;
	}


	/**
	 * Sets the value(s) for <b>supercedes</b> (If this document manifest replaces another)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public DocumentManifest setSupercedes(ResourceReferenceDt theValue) {
		mySupercedes = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Human-readable description (title)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}


	/**
	 * Gets the value(s) for <b>description</b> (Human-readable description (title)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}


	/**
	 * Sets the value(s) for <b>description</b> (Human-readable description (title))
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentManifest setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Human-readable description (title))
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentManifest setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>confidentiality</b> (Sensitivity of set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public CodeableConceptDt getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new CodeableConceptDt();
		}
		return myConfidentiality;
	}


	/**
	 * Gets the value(s) for <b>confidentiality</b> (Sensitivity of set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public CodeableConceptDt getConfidentialityElement() {  
		if (myConfidentiality == null) {
			myConfidentiality = new CodeableConceptDt();
		}
		return myConfidentiality;
	}


	/**
	 * Sets the value(s) for <b>confidentiality</b> (Sensitivity of set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public DocumentManifest setConfidentiality(CodeableConceptDt theValue) {
		myConfidentiality = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>content</b> (Contents of this set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getContent() {  
		if (myContent == null) {
			myContent = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myContent;
	}


	/**
	 * Gets the value(s) for <b>content</b> (Contents of this set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getContentElement() {  
		if (myContent == null) {
			myContent = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myContent;
	}


	/**
	 * Sets the value(s) for <b>content</b> (Contents of this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public DocumentManifest setContent(java.util.List<ResourceReferenceDt> theValue) {
		myContent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>content</b> (Contents of this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public ResourceReferenceDt addContent() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getContent().add(newType);
		return newType; 
	}
  


    @Override
    public String getResourceName() {
        return "DocumentManifest";
    }

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
