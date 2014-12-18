















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
 * HAPI/FHIR <b>DeviceObservationReport</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * Describes the data produced by a device at a point in time
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DeviceObservationReport">http://hl7.org/fhir/profiles/DeviceObservationReport</a> 
 * </p>
 *
 */
@ResourceDef(name="DeviceObservationReport", profile="http://hl7.org/fhir/profiles/DeviceObservationReport", id="deviceobservationreport")
public class DeviceObservationReport 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.source</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="DeviceObservationReport.source", description="", type="reference"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.source</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SOURCE = new ReferenceClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The compartment code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="DeviceObservationReport.virtualDevice.code", description="The compartment code", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The compartment code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>channel</b>
	 * <p>
	 * Description: <b>The channel code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="channel", path="DeviceObservationReport.virtualDevice.channel.code", description="The channel code", type="token"  )
	public static final String SP_CHANNEL = "channel";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>channel</b>
	 * <p>
	 * Description: <b>The channel code</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CHANNEL = new TokenClientParam(SP_CHANNEL);

	/**
	 * Search parameter constant for <b>observation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.metric.observation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="observation", path="DeviceObservationReport.virtualDevice.channel.metric.observation", description="", type="reference"  )
	public static final String SP_OBSERVATION = "observation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>observation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.virtualDevice.channel.metric.observation</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam OBSERVATION = new ReferenceClientParam(SP_OBSERVATION);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DeviceObservationReport.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="DeviceObservationReport.subject", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DeviceObservationReport.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.source</b>".
	 */
	public static final Include INCLUDE_SOURCE = new Include("DeviceObservationReport.source");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DeviceObservationReport.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.virtualDevice.channel.code</b>".
	 */
	public static final Include INCLUDE_VIRTUALDEVICE_CHANNEL_CODE = new Include("DeviceObservationReport.virtualDevice.channel.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.virtualDevice.channel.metric.observation</b>".
	 */
	public static final Include INCLUDE_VIRTUALDEVICE_CHANNEL_METRIC_OBSERVATION = new Include("DeviceObservationReport.virtualDevice.channel.metric.observation");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DeviceObservationReport.virtualDevice.code</b>".
	 */
	public static final Include INCLUDE_VIRTUALDEVICE_CODE = new Include("DeviceObservationReport.virtualDevice.code");


	@Child(name="instant", type=InstantDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The point in time that the values are reported"
	)
	private InstantDt myInstant;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier assigned to this observation bu the source device that made the observation"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="source", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identification information for the device that is the source of the data"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="subject", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class, 		ca.uhn.fhir.model.dev.resource.Device.class, 		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The subject of the measurement"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="virtualDevice", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A medical-related subsystem of a medical device"
	)
	private java.util.List<VirtualDevice> myVirtualDevice;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myInstant,  myIdentifier,  mySource,  mySubject,  myVirtualDevice);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myInstant, myIdentifier, mySource, mySubject, myVirtualDevice);
	}

	/**
	 * Gets the value(s) for <b>instant</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public InstantDt getInstantElement() {  
		if (myInstant == null) {
			myInstant = new InstantDt();
		}
		return myInstant;
	}

	
	/**
	 * Gets the value(s) for <b>instant</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public Date getInstant() {  
		return getInstantElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>instant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public DeviceObservationReport setInstant(InstantDt theValue) {
		myInstant = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>instant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public DeviceObservationReport setInstantWithMillisPrecision( Date theDate) {
		myInstant = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>instant</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The point in time that the values are reported
     * </p> 
	 */
	public DeviceObservationReport setInstant( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myInstant = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier assigned to this observation bu the source device that made the observation
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier assigned to this observation bu the source device that made the observation
     * </p> 
	 */
	public DeviceObservationReport setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>source</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification information for the device that is the source of the data
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identification information for the device that is the source of the data
     * </p> 
	 */
	public DeviceObservationReport setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>subject</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the measurement
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the measurement
     * </p> 
	 */
	public DeviceObservationReport setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>virtualDevice</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public java.util.List<VirtualDevice> getVirtualDevice() {  
		if (myVirtualDevice == null) {
			myVirtualDevice = new java.util.ArrayList<VirtualDevice>();
		}
		return myVirtualDevice;
	}

	/**
	 * Sets the value(s) for <b>virtualDevice</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public DeviceObservationReport setVirtualDevice(java.util.List<VirtualDevice> theValue) {
		myVirtualDevice = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>virtualDevice</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public VirtualDevice addVirtualDevice() {
		VirtualDevice newType = new VirtualDevice();
		getVirtualDevice().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>virtualDevice</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	public VirtualDevice getVirtualDeviceFirstRep() {
		if (getVirtualDevice().isEmpty()) {
			return addVirtualDevice();
		}
		return getVirtualDevice().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DeviceObservationReport.virtualDevice</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A medical-related subsystem of a medical device
     * </p> 
	 */
	@Block()	
	public static class VirtualDevice 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="VirtalDeviceKind",
		formalDefinition="Describes the compartment"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="channel", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Groups together physiological measurement data and derived data"
	)
	private java.util.List<VirtualDeviceChannel> myChannel;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myChannel);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myChannel);
	}

	/**
	 * Gets the value(s) for <b>code</b> (VirtalDeviceKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the compartment
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (VirtalDeviceKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the compartment
     * </p> 
	 */
	public VirtualDevice setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>channel</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public java.util.List<VirtualDeviceChannel> getChannel() {  
		if (myChannel == null) {
			myChannel = new java.util.ArrayList<VirtualDeviceChannel>();
		}
		return myChannel;
	}

	/**
	 * Sets the value(s) for <b>channel</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public VirtualDevice setChannel(java.util.List<VirtualDeviceChannel> theValue) {
		myChannel = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>channel</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public VirtualDeviceChannel addChannel() {
		VirtualDeviceChannel newType = new VirtualDeviceChannel();
		getChannel().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>channel</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	public VirtualDeviceChannel getChannelFirstRep() {
		if (getChannel().isEmpty()) {
			return addChannel();
		}
		return getChannel().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>DeviceObservationReport.virtualDevice.channel</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Groups together physiological measurement data and derived data
     * </p> 
	 */
	@Block()	
	public static class VirtualDeviceChannel 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="DeviceChannelKind1",
		formalDefinition="Describes the channel"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="metric", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A piece of measured or derived data that is reported by the machine"
	)
	private java.util.List<VirtualDeviceChannelMetric> myMetric;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myMetric);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myMetric);
	}

	/**
	 * Gets the value(s) for <b>code</b> (DeviceChannelKind1).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the channel
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (DeviceChannelKind1)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the channel
     * </p> 
	 */
	public VirtualDeviceChannel setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>metric</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public java.util.List<VirtualDeviceChannelMetric> getMetric() {  
		if (myMetric == null) {
			myMetric = new java.util.ArrayList<VirtualDeviceChannelMetric>();
		}
		return myMetric;
	}

	/**
	 * Sets the value(s) for <b>metric</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public VirtualDeviceChannel setMetric(java.util.List<VirtualDeviceChannelMetric> theValue) {
		myMetric = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>metric</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public VirtualDeviceChannelMetric addMetric() {
		VirtualDeviceChannelMetric newType = new VirtualDeviceChannelMetric();
		getMetric().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>metric</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	public VirtualDeviceChannelMetric getMetricFirstRep() {
		if (getMetric().isEmpty()) {
			return addMetric();
		}
		return getMetric().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>DeviceObservationReport.virtualDevice.channel.metric</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A piece of measured or derived data that is reported by the machine
     * </p> 
	 */
	@Block()	
	public static class VirtualDeviceChannelMetric 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="observation", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Observation.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The data for the metric"
	)
	private ResourceReferenceDt myObservation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myObservation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myObservation);
	}

	/**
	 * Gets the value(s) for <b>observation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The data for the metric
     * </p> 
	 */
	public ResourceReferenceDt getObservation() {  
		if (myObservation == null) {
			myObservation = new ResourceReferenceDt();
		}
		return myObservation;
	}

	/**
	 * Sets the value(s) for <b>observation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The data for the metric
     * </p> 
	 */
	public VirtualDeviceChannelMetric setObservation(ResourceReferenceDt theValue) {
		myObservation = theValue;
		return this;
	}
	
	

  

	}






    @Override
    public String getResourceName() {
        return "DeviceObservationReport";
    }

}
