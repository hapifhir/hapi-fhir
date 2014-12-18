















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
 * HAPI/FHIR <b>Device</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * This resource identifies an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Allows institutions to track their devices. 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Device">http://hl7.org/fhir/profiles/Device</a> 
 * </p>
 *
 */
@ResourceDef(name="Device", profile="http://hl7.org/fhir/profiles/Device", id="device")
public class Device 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the device</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Device.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Device.type", description="The type of the device", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the device</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Device.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>The manufacturer of the device</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Device.manufacturer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Device.manufacturer", description="The manufacturer of the device", type="string"  )
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>The manufacturer of the device</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Device.manufacturer</b><br/>
	 * </p>
	 */
	public static final StringClientParam MANUFACTURER = new StringClientParam(SP_MANUFACTURER);

	/**
	 * Search parameter constant for <b>model</b>
	 * <p>
	 * Description: <b>The model of the device</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Device.model</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="model", path="Device.model", description="The model of the device", type="string"  )
	public static final String SP_MODEL = "model";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>model</b>
	 * <p>
	 * Description: <b>The model of the device</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Device.model</b><br/>
	 * </p>
	 */
	public static final StringClientParam MODEL = new StringClientParam(SP_MODEL);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The organization responsible for the device</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Device.owner</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="Device.owner", description="The organization responsible for the device", type="reference"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The organization responsible for the device</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Device.owner</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORGANIZATION = new ReferenceClientParam(SP_ORGANIZATION);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Device.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Device.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Device.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>A location, where the resource is found</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Device.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Device.location", description="A location, where the resource is found", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>A location, where the resource is found</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Device.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient information, if the resource is affixed to a person</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Device.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Device.patient", description="Patient information, if the resource is affixed to a person", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient information, if the resource is affixed to a person</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Device.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>udi</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Device.udi</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="udi", path="Device.udi", description="", type="string"  )
	public static final String SP_UDI = "udi";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>udi</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Device.udi</b><br/>
	 * </p>
	 */
	public static final StringClientParam UDI = new StringClientParam(SP_UDI);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Device.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Device.location");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.manufacturer</b>".
	 */
	public static final Include INCLUDE_MANUFACTURER = new Include("Device.manufacturer");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.model</b>".
	 */
	public static final Include INCLUDE_MODEL = new Include("Device.model");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.owner</b>".
	 */
	public static final Include INCLUDE_OWNER = new Include("Device.owner");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Device.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Device.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.udi</b>".
	 */
	public static final Include INCLUDE_UDI = new Include("Device.udi");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="DeviceKind",
		formalDefinition="A kind of this device"
	)
	private CodeableConceptDt myType;
	
	@Child(name="manufacturer", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A name of the manufacturer"
	)
	private StringDt myManufacturer;
	
	@Child(name="model", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The \"model\" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type."
	)
	private StringDt myModel;
	
	@Child(name="version", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware."
	)
	private StringDt myVersion;
	
	@Child(name="expiry", type=DateDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date of expiry of this device (if applicable)"
	)
	private DateDt myExpiry;
	
	@Child(name="udi", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm"
	)
	private StringDt myUdi;
	
	@Child(name="lotNumber", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Lot number assigned by the manufacturer"
	)
	private StringDt myLotNumber;
	
	@Child(name="owner", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="",
		formalDefinition="An organization that is responsible for the provision and ongoing maintenance of the device."
	)
	private ResourceReferenceDt myOwner;
	
	@Child(name="location", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \"in/with the patient\"), or a coded location."
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="patient", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Patient information, if the resource is affixed to a person"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="contact", type=ContactPointDt.class, order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contact details for an organization or a particular human that is responsible for the device"
	)
	private java.util.List<ContactPointDt> myContact;
	
	@Child(name="url", type=UriDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A network address on which the device may be contacted directly"
	)
	private UriDt myUrl;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  myManufacturer,  myModel,  myVersion,  myExpiry,  myUdi,  myLotNumber,  myOwner,  myLocation,  myPatient,  myContact,  myUrl);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, myManufacturer, myModel, myVersion, myExpiry, myUdi, myLotNumber, myOwner, myLocation, myPatient, myContact, myUrl);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device.
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
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device.
     * </p> 
	 */
	public Device setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device.
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
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> (DeviceKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A kind of this device
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (DeviceKind)
	 *
     * <p>
     * <b>Definition:</b>
     * A kind of this device
     * </p> 
	 */
	public Device setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>manufacturer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public StringDt getManufacturerElement() {  
		if (myManufacturer == null) {
			myManufacturer = new StringDt();
		}
		return myManufacturer;
	}

	
	/**
	 * Gets the value(s) for <b>manufacturer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public String getManufacturer() {  
		return getManufacturerElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>manufacturer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public Device setManufacturer(StringDt theValue) {
		myManufacturer = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>manufacturer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public Device setManufacturer( String theString) {
		myManufacturer = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>model</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The \"model\" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     * </p> 
	 */
	public StringDt getModelElement() {  
		if (myModel == null) {
			myModel = new StringDt();
		}
		return myModel;
	}

	
	/**
	 * Gets the value(s) for <b>model</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The \"model\" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     * </p> 
	 */
	public String getModel() {  
		return getModelElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>model</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The \"model\" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     * </p> 
	 */
	public Device setModel(StringDt theValue) {
		myModel = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>model</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The \"model\" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type.
     * </p> 
	 */
	public Device setModel( String theString) {
		myModel = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     * </p> 
	 */
	public Device setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware.
     * </p> 
	 */
	public Device setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expiry</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public DateDt getExpiryElement() {  
		if (myExpiry == null) {
			myExpiry = new DateDt();
		}
		return myExpiry;
	}

	
	/**
	 * Gets the value(s) for <b>expiry</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public Date getExpiry() {  
		return getExpiryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>expiry</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public Device setExpiry(DateDt theValue) {
		myExpiry = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>expiry</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public Device setExpiry( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myExpiry = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>expiry</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public Device setExpiryWithDayPrecision( Date theDate) {
		myExpiry = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>udi</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public StringDt getUdiElement() {  
		if (myUdi == null) {
			myUdi = new StringDt();
		}
		return myUdi;
	}

	
	/**
	 * Gets the value(s) for <b>udi</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public String getUdi() {  
		return getUdiElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>udi</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public Device setUdi(StringDt theValue) {
		myUdi = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>udi</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public Device setUdi( String theString) {
		myUdi = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>lotNumber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public StringDt getLotNumberElement() {  
		if (myLotNumber == null) {
			myLotNumber = new StringDt();
		}
		return myLotNumber;
	}

	
	/**
	 * Gets the value(s) for <b>lotNumber</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public String getLotNumber() {  
		return getLotNumberElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lotNumber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public Device setLotNumber(StringDt theValue) {
		myLotNumber = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lotNumber</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public Device setLotNumber( String theString) {
		myLotNumber = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>owner</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     * </p> 
	 */
	public ResourceReferenceDt getOwner() {  
		if (myOwner == null) {
			myOwner = new ResourceReferenceDt();
		}
		return myOwner;
	}

	/**
	 * Sets the value(s) for <b>owner</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     * </p> 
	 */
	public Device setOwner(ResourceReferenceDt theValue) {
		myOwner = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>location</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \"in/with the patient\"), or a coded location.
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \"in/with the patient\"), or a coded location.
     * </p> 
	 */
	public Device setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient information, if the resource is affixed to a person
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
     * Patient information, if the resource is affixed to a person
     * </p> 
	 */
	public Device setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public java.util.List<ContactPointDt> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactPointDt>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public Device setContact(java.util.List<ContactPointDt> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public ContactPointDt addContact() {
		ContactPointDt newType = new ContactPointDt();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public ContactPointDt getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public Device setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public Device setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 


    @Override
    public String getResourceName() {
        return "Device";
    }

}
