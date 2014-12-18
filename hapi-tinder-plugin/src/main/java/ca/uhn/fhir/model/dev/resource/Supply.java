















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
 * HAPI/FHIR <b>Supply</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * A supply - a  request for something, and provision of what is supplied
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Supply">http://hl7.org/fhir/profiles/Supply</a> 
 * </p>
 *
 */
@ResourceDef(name="Supply", profile="http://hl7.org/fhir/profiles/Supply", id="supply")
public class Supply 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.kind</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="kind", path="Supply.kind", description="", type="token"  )
	public static final String SP_KIND = "kind";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.kind</b><br/>
	 * </p>
	 */
	public static final TokenClientParam KIND = new TokenClientParam(SP_KIND);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Supply.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Supply.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Supply.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Search parameter constant for <b>supplier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.dispense.supplier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supplier", path="Supply.dispense.supplier", description="", type="reference"  )
	public static final String SP_SUPPLIER = "supplier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>supplier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.dispense.supplier</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUPPLIER = new ReferenceClientParam(SP_SUPPLIER);

	/**
	 * Search parameter constant for <b>dispenseid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispenseid", path="Supply.dispense.identifier", description="", type="token"  )
	public static final String SP_DISPENSEID = "dispenseid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dispenseid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DISPENSEID = new TokenClientParam(SP_DISPENSEID);

	/**
	 * Search parameter constant for <b>dispensestatus</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispensestatus", path="Supply.dispense.status", description="", type="token"  )
	public static final String SP_DISPENSESTATUS = "dispensestatus";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dispensestatus</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DISPENSESTATUS = new TokenClientParam(SP_DISPENSESTATUS);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.dispense.identifier</b>".
	 */
	public static final Include INCLUDE_DISPENSE_IDENTIFIER = new Include("Supply.dispense.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.dispense.status</b>".
	 */
	public static final Include INCLUDE_DISPENSE_STATUS = new Include("Supply.dispense.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.dispense.supplier</b>".
	 */
	public static final Include INCLUDE_DISPENSE_SUPPLIER = new Include("Supply.dispense.supplier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Supply.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.kind</b>".
	 */
	public static final Include INCLUDE_KIND = new Include("Supply.kind");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Supply.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Supply.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Supply.status");


	@Child(name="kind", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="SupplyKind",
		formalDefinition="Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process"
	)
	private CodeableConceptDt myKind;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Unique identifier for this supply request"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SupplyStatus",
		formalDefinition="Status of the supply request"
	)
	private BoundCodeDt<SupplyStatusEnum> myStatus;
	
	@Child(name="orderedItem", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class, 		ca.uhn.fhir.model.dev.resource.Substance.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The item that is requested to be supplied"
	)
	private ResourceReferenceDt myOrderedItem;
	
	@Child(name="patient", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A link to a resource representing the person whom the ordered item is for"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="dispense", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed."
	)
	private java.util.List<Dispense> myDispense;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myKind,  myIdentifier,  myStatus,  myOrderedItem,  myPatient,  myDispense);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myKind, myIdentifier, myStatus, myOrderedItem, myPatient, myDispense);
	}

	/**
	 * Gets the value(s) for <b>kind</b> (SupplyKind).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public CodeableConceptDt getKind() {  
		if (myKind == null) {
			myKind = new CodeableConceptDt();
		}
		return myKind;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (SupplyKind)
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public Supply setKind(CodeableConceptDt theValue) {
		myKind = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
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
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (SupplyStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public BoundCodeDt<SupplyStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SupplyStatusEnum>(SupplyStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (SupplyStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (SupplyStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public Supply setStatus(BoundCodeDt<SupplyStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (SupplyStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public Supply setStatus(SupplyStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>orderedItem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The item that is requested to be supplied
     * </p> 
	 */
	public ResourceReferenceDt getOrderedItem() {  
		if (myOrderedItem == null) {
			myOrderedItem = new ResourceReferenceDt();
		}
		return myOrderedItem;
	}

	/**
	 * Sets the value(s) for <b>orderedItem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The item that is requested to be supplied
     * </p> 
	 */
	public Supply setOrderedItem(ResourceReferenceDt theValue) {
		myOrderedItem = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person whom the ordered item is for
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
     * A link to a resource representing the person whom the ordered item is for
     * </p> 
	 */
	public Supply setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>dispense</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public java.util.List<Dispense> getDispense() {  
		if (myDispense == null) {
			myDispense = new java.util.ArrayList<Dispense>();
		}
		return myDispense;
	}

	/**
	 * Sets the value(s) for <b>dispense</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Supply setDispense(java.util.List<Dispense> theValue) {
		myDispense = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>dispense</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Dispense addDispense() {
		Dispense newType = new Dispense();
		getDispense().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dispense</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Dispense getDispenseFirstRep() {
		if (getDispense().isEmpty()) {
			return addDispense();
		}
		return getDispense().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Supply.dispense</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	@Block()	
	public static class Dispense 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifier assigned by the dispensing facility when the item(s) is dispensed."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="SupplyDispenseStatus",
		formalDefinition="A code specifying the state of the dispense event."
	)
	private BoundCodeDt<SupplyDispenseStatusEnum> myStatus;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SupplyItemType",
		formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc."
	)
	private BoundCodeableConceptDt<SupplyItemTypeEnum> myType;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The amount of supply that has been dispensed. Includes unit of measure."
	)
	private QuantityDt myQuantity;
	
	@Child(name="suppliedItem", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Medication.class, 		ca.uhn.fhir.model.dev.resource.Substance.class, 		ca.uhn.fhir.model.dev.resource.Device.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list."
	)
	private ResourceReferenceDt mySuppliedItem;
	
	@Child(name="supplier", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The individual responsible for dispensing the medication, supplier or device."
	)
	private ResourceReferenceDt mySupplier;
	
	@Child(name="whenPrepared", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The time the dispense event occurred."
	)
	private PeriodDt myWhenPrepared;
	
	@Child(name="whenHandedOver", type=PeriodDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The time the dispensed item was sent or handed to the patient (or agent)."
	)
	private PeriodDt myWhenHandedOver;
	
	@Child(name="destination", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Location.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identification of the facility/location where the Supply was shipped to, as part of the dispense event."
	)
	private ResourceReferenceDt myDestination;
	
	@Child(name="receiver", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the person who picked up the Supply."
	)
	private java.util.List<ResourceReferenceDt> myReceiver;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myType,  myQuantity,  mySuppliedItem,  mySupplier,  myWhenPrepared,  myWhenHandedOver,  myDestination,  myReceiver);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myType, myQuantity, mySuppliedItem, mySupplier, myWhenPrepared, myWhenHandedOver, myDestination, myReceiver);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the item(s) is dispensed.
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
     * Identifier assigned by the dispensing facility when the item(s) is dispensed.
     * </p> 
	 */
	public Dispense setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>status</b> (SupplyDispenseStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public BoundCodeDt<SupplyDispenseStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SupplyDispenseStatusEnum>(SupplyDispenseStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (SupplyDispenseStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (SupplyDispenseStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(BoundCodeDt<SupplyDispenseStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (SupplyDispenseStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(SupplyDispenseStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (SupplyItemType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public BoundCodeableConceptDt<SupplyItemTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<SupplyItemTypeEnum>(SupplyItemTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (SupplyItemType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(BoundCodeableConceptDt<SupplyItemTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (SupplyItemType)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(SupplyItemTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>suppliedItem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.
     * </p> 
	 */
	public ResourceReferenceDt getSuppliedItem() {  
		if (mySuppliedItem == null) {
			mySuppliedItem = new ResourceReferenceDt();
		}
		return mySuppliedItem;
	}

	/**
	 * Sets the value(s) for <b>suppliedItem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.
     * </p> 
	 */
	public Dispense setSuppliedItem(ResourceReferenceDt theValue) {
		mySuppliedItem = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>supplier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication, supplier or device.
     * </p> 
	 */
	public ResourceReferenceDt getSupplier() {  
		if (mySupplier == null) {
			mySupplier = new ResourceReferenceDt();
		}
		return mySupplier;
	}

	/**
	 * Sets the value(s) for <b>supplier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication, supplier or device.
     * </p> 
	 */
	public Dispense setSupplier(ResourceReferenceDt theValue) {
		mySupplier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>whenPrepared</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispense event occurred.
     * </p> 
	 */
	public PeriodDt getWhenPrepared() {  
		if (myWhenPrepared == null) {
			myWhenPrepared = new PeriodDt();
		}
		return myWhenPrepared;
	}

	/**
	 * Sets the value(s) for <b>whenPrepared</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispense event occurred.
     * </p> 
	 */
	public Dispense setWhenPrepared(PeriodDt theValue) {
		myWhenPrepared = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>whenHandedOver</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed item was sent or handed to the patient (or agent).
     * </p> 
	 */
	public PeriodDt getWhenHandedOver() {  
		if (myWhenHandedOver == null) {
			myWhenHandedOver = new PeriodDt();
		}
		return myWhenHandedOver;
	}

	/**
	 * Sets the value(s) for <b>whenHandedOver</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed item was sent or handed to the patient (or agent).
     * </p> 
	 */
	public Dispense setWhenHandedOver(PeriodDt theValue) {
		myWhenHandedOver = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>destination</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * </p> 
	 */
	public ResourceReferenceDt getDestination() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * </p> 
	 */
	public Dispense setDestination(ResourceReferenceDt theValue) {
		myDestination = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>receiver</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReceiver() {  
		if (myReceiver == null) {
			myReceiver = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReceiver;
	}

	/**
	 * Sets the value(s) for <b>receiver</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public Dispense setReceiver(java.util.List<ResourceReferenceDt> theValue) {
		myReceiver = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>receiver</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public ResourceReferenceDt addReceiver() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReceiver().add(newType);
		return newType; 
	}
  

	}




    @Override
    public String getResourceName() {
        return "Supply";
    }

}
