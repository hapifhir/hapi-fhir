















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
 * HAPI/FHIR <b>Device</b> Resource
 * (An instance of a manufactured thing that is used in the provision of healthcare)
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
	 * Description: <b>The type of the device</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Device.type</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Device.type", description="The type of the device", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the device</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Device.type</b><br>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>The manufacturer of the device</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Device.manufacturer</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Device.manufacturer", description="The manufacturer of the device", type="string"  )
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>The manufacturer of the device</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Device.manufacturer</b><br>
	 * </p>
	 */
	public static final StringClientParam MANUFACTURER = new StringClientParam(SP_MANUFACTURER);

	/**
	 * Search parameter constant for <b>model</b>
	 * <p>
	 * Description: <b>The model of the device</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Device.model</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="model", path="Device.model", description="The model of the device", type="string"  )
	public static final String SP_MODEL = "model";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>model</b>
	 * <p>
	 * Description: <b>The model of the device</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Device.model</b><br>
	 * </p>
	 */
	public static final StringClientParam MODEL = new StringClientParam(SP_MODEL);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The organization responsible for the device</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Device.owner</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="Device.owner", description="The organization responsible for the device", type="reference"  )
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>The organization responsible for the device</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Device.owner</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam ORGANIZATION = new ReferenceClientParam(SP_ORGANIZATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.owner</b>".
	 */
	public static final Include INCLUDE_OWNER = new Include("Device.owner");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Device.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Device.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Device.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>A location, where the resource is found</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Device.location</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Device.location", description="A location, where the resource is found", type="reference"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>A location, where the resource is found</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Device.location</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Device.location");

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient information, if the resource is affixed to a person</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Device.patient</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Device.patient", description="Patient information, if the resource is affixed to a person", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient information, if the resource is affixed to a person</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Device.patient</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Device.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Device.patient");

	/**
	 * Search parameter constant for <b>udi</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Device.udi</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="udi", path="Device.udi", description="", type="string"  )
	public static final String SP_UDI = "udi";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>udi</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Device.udi</b><br>
	 * </p>
	 */
	public static final StringClientParam UDI = new StringClientParam(SP_UDI);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Instance id from manufacturer, owner and others",
		formalDefinition="Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="What kind of device this is",
		formalDefinition="A kind of this device"
	)
	private CodeableConceptDt myType;
	
	@Child(name="manufacturer", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Name of device manufacturer",
		formalDefinition="A name of the manufacturer"
	)
	private StringDt myManufacturer;
	
	@Child(name="model", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Model id assigned by the manufacturer",
		formalDefinition="The \"model\" - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type"
	)
	private StringDt myModel;
	
	@Child(name="version", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Version number (i.e. software)",
		formalDefinition="The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware"
	)
	private StringDt myVersion;
	
	@Child(name="expiry", type=DateDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Date of expiry of this device (if applicable)",
		formalDefinition="Date of expiry of this device (if applicable)"
	)
	private DateDt myExpiry;
	
	@Child(name="udi", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="FDA Mandated Unique Device Identifier",
		formalDefinition="FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm"
	)
	private StringDt myUdi;
	
	@Child(name="lotNumber", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Lot number of manufacture",
		formalDefinition="Lot number assigned by the manufacturer"
	)
	private StringDt myLotNumber;
	
	@Child(name="owner", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Organization responsible for device",
		formalDefinition="An organization that is responsible for the provision and ongoing maintenance of the device"
	)
	private ResourceReferenceDt myOwner;
	
	@Child(name="location", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where the resource is found",
		formalDefinition="The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \"in/with the patient\"), or a coded location"
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="patient", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="If the resource is affixed to a person",
		formalDefinition="Patient information, if the resource is affixed to a person"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="contact", type=ContactDt.class, order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Details for human/organization for support",
		formalDefinition="Contact details for an organization or a particular human that is responsible for the device"
	)
	private java.util.List<ContactDt> myContact;
	
	@Child(name="url", type=UriDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="Network address to contact device",
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
	 * Gets the value(s) for <b>identifier</b> (Instance id from manufacturer, owner and others).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Gets the value(s) for <b>identifier</b> (Instance id from manufacturer, owner and others).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}


	/**
	 * Sets the value(s) for <b>identifier</b> (Instance id from manufacturer, owner and others)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public Device setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Instance id from manufacturer, owner and others)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Instance id from manufacturer, owner and others),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Instance id from manufacturer, owner and others)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Device addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Instance id from manufacturer, owner and others)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this device by various organizations. The most likely organizations to assign identifiers are the manufacturer and the owner, though regulatory agencies may also assign an identifier. The identifiers identify the particular device, not the kind of device
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Device addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (What kind of device this is).
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
	 * Gets the value(s) for <b>type</b> (What kind of device this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A kind of this device
     * </p> 
	 */
	public CodeableConceptDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}


	/**
	 * Sets the value(s) for <b>type</b> (What kind of device this is)
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
	 * Gets the value(s) for <b>manufacturer</b> (Name of device manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name of the manufacturer
     * </p> 
	 */
	public StringDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new StringDt();
		}
		return myManufacturer;
	}


	/**
	 * Gets the value(s) for <b>manufacturer</b> (Name of device manufacturer).
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
	 * Sets the value(s) for <b>manufacturer</b> (Name of device manufacturer)
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
	 * Sets the value for <b>manufacturer</b> (Name of device manufacturer)
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
	 * Gets the value(s) for <b>model</b> (Model id assigned by the manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The \&quot;model\&quot; - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public StringDt getModel() {  
		if (myModel == null) {
			myModel = new StringDt();
		}
		return myModel;
	}


	/**
	 * Gets the value(s) for <b>model</b> (Model id assigned by the manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The \&quot;model\&quot; - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public StringDt getModelElement() {  
		if (myModel == null) {
			myModel = new StringDt();
		}
		return myModel;
	}


	/**
	 * Sets the value(s) for <b>model</b> (Model id assigned by the manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * The \&quot;model\&quot; - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public Device setModel(StringDt theValue) {
		myModel = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>model</b> (Model id assigned by the manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * The \&quot;model\&quot; - an identifier assigned by the manufacturer to identify the product by its type. This number is shared by the all devices sold as the same type
     * </p> 
	 */
	public Device setModel( String theString) {
		myModel = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Version number (i.e. software)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}


	/**
	 * Gets the value(s) for <b>version</b> (Version number (i.e. software)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}


	/**
	 * Sets the value(s) for <b>version</b> (Version number (i.e. software))
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public Device setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>version</b> (Version number (i.e. software))
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the device, if the device has multiple releases under the same model, or if the device is software or carries firmware
     * </p> 
	 */
	public Device setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expiry</b> (Date of expiry of this device (if applicable)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date of expiry of this device (if applicable)
     * </p> 
	 */
	public DateDt getExpiry() {  
		if (myExpiry == null) {
			myExpiry = new DateDt();
		}
		return myExpiry;
	}


	/**
	 * Gets the value(s) for <b>expiry</b> (Date of expiry of this device (if applicable)).
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
	 * Sets the value(s) for <b>expiry</b> (Date of expiry of this device (if applicable))
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
	 * Sets the value for <b>expiry</b> (Date of expiry of this device (if applicable))
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
	 * Sets the value for <b>expiry</b> (Date of expiry of this device (if applicable))
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
	 * Gets the value(s) for <b>udi</b> (FDA Mandated Unique Device Identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * FDA Mandated Unique Device Identifier. Use the human readable information (the content that the user sees, which is sometimes different to the exact syntax represented in the barcode)  - see http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/default.htm
     * </p> 
	 */
	public StringDt getUdi() {  
		if (myUdi == null) {
			myUdi = new StringDt();
		}
		return myUdi;
	}


	/**
	 * Gets the value(s) for <b>udi</b> (FDA Mandated Unique Device Identifier).
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
	 * Sets the value(s) for <b>udi</b> (FDA Mandated Unique Device Identifier)
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
	 * Sets the value for <b>udi</b> (FDA Mandated Unique Device Identifier)
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
	 * Gets the value(s) for <b>lotNumber</b> (Lot number of manufacture).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number assigned by the manufacturer
     * </p> 
	 */
	public StringDt getLotNumber() {  
		if (myLotNumber == null) {
			myLotNumber = new StringDt();
		}
		return myLotNumber;
	}


	/**
	 * Gets the value(s) for <b>lotNumber</b> (Lot number of manufacture).
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
	 * Sets the value(s) for <b>lotNumber</b> (Lot number of manufacture)
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
	 * Sets the value for <b>lotNumber</b> (Lot number of manufacture)
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
	 * Gets the value(s) for <b>owner</b> (Organization responsible for device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device
     * </p> 
	 */
	public ResourceReferenceDt getOwner() {  
		if (myOwner == null) {
			myOwner = new ResourceReferenceDt();
		}
		return myOwner;
	}


	/**
	 * Gets the value(s) for <b>owner</b> (Organization responsible for device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device
     * </p> 
	 */
	public ResourceReferenceDt getOwnerElement() {  
		if (myOwner == null) {
			myOwner = new ResourceReferenceDt();
		}
		return myOwner;
	}


	/**
	 * Sets the value(s) for <b>owner</b> (Organization responsible for device)
	 *
     * <p>
     * <b>Definition:</b>
     * An organization that is responsible for the provision and ongoing maintenance of the device
     * </p> 
	 */
	public Device setOwner(ResourceReferenceDt theValue) {
		myOwner = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (Where the resource is found).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \&quot;in/with the patient\&quot;), or a coded location
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}


	/**
	 * Gets the value(s) for <b>location</b> (Where the resource is found).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \&quot;in/with the patient\&quot;), or a coded location
     * </p> 
	 */
	public ResourceReferenceDt getLocationElement() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}


	/**
	 * Sets the value(s) for <b>location</b> (Where the resource is found)
	 *
     * <p>
     * <b>Definition:</b>
     * The resource may be found in a literal location (i.e. GPS coordinates), a logical place (i.e. \&quot;in/with the patient\&quot;), or a coded location
     * </p> 
	 */
	public Device setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (If the resource is affixed to a person).
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
	 * Gets the value(s) for <b>patient</b> (If the resource is affixed to a person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient information, if the resource is affixed to a person
     * </p> 
	 */
	public ResourceReferenceDt getPatientElement() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}


	/**
	 * Sets the value(s) for <b>patient</b> (If the resource is affixed to a person)
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
	 * Gets the value(s) for <b>contact</b> (Details for human/organization for support).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public java.util.List<ContactDt> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		return myContact;
	}


	/**
	 * Gets the value(s) for <b>contact</b> (Details for human/organization for support).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public java.util.List<ContactDt> getContactElement() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		return myContact;
	}


	/**
	 * Sets the value(s) for <b>contact</b> (Details for human/organization for support)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public Device setContact(java.util.List<ContactDt> theValue) {
		myContact = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>contact</b> (Details for human/organization for support)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public ContactDt addContact() {
		ContactDt newType = new ContactDt();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (Details for human/organization for support),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
	 */
	public ContactDt getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
 	/**
	 * Adds a new value for <b>contact</b> (Details for human/organization for support)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Device addContact( ContactUseEnum theContactUse,  String theValue) {
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		myContact.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>contact</b> (Details for human/organization for support)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for an organization or a particular human that is responsible for the device
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Device addContact( String theValue) {
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		myContact.add(new ContactDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>url</b> (Network address to contact device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A network address on which the device may be contacted directly
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}


	/**
	 * Gets the value(s) for <b>url</b> (Network address to contact device).
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
	 * Sets the value(s) for <b>url</b> (Network address to contact device)
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
	 * Sets the value for <b>url</b> (Network address to contact device)
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

    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU1;
    }

}
