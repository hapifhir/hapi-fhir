















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
 * HAPI/FHIR <b>Procedure</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * An action that is performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Procedure">http://hl7.org/fhir/profiles/Procedure</a> 
 * </p>
 *
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/profiles/Procedure", id="procedure")
public class Procedure 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Type of procedure</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Procedure.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Procedure.type", description="Type of procedure", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Type of procedure</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Procedure.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date the procedure was performed on</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Procedure.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Procedure.date", description="The date the procedure was performed on", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date the procedure was performed on</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Procedure.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list procedures  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Procedure.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Procedure.patient", description="The identity of a patient to list procedures  for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list procedures  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Procedure.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Procedure.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Procedure.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Procedure.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("Procedure.patient");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Procedure.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Procedure.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="patient", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The person on whom the procedure was performed"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded"
	)
	private CodeableConceptDt myType;
	
	@Child(name="bodySite", type=CodeableConceptDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion"
	)
	private java.util.List<CodeableConceptDt> myBodySite;
	
	@Child(name="indication", type=CodeableConceptDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text"
	)
	private java.util.List<CodeableConceptDt> myIndication;
	
	@Child(name="performer", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Limited to 'real' people rather than equipment"
	)
	private java.util.List<Performer> myPerformer;
	
	@Child(name="date", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured."
	)
	private PeriodDt myDate;
	
	@Child(name="encounter", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Encounter.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The encounter during which the procedure was performed"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="outcome", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="What was the outcome of the procedure - did it resolve reasons why the procedure was performed?"
	)
	private StringDt myOutcome;
	
	@Child(name="report", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.DiagnosticReport.class	})
	@Description(
		shortDefinition="",
		formalDefinition="This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies"
	)
	private java.util.List<ResourceReferenceDt> myReport;
	
	@Child(name="complication", type=CodeableConceptDt.class, order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues"
	)
	private java.util.List<CodeableConceptDt> myComplication;
	
	@Child(name="followUp", type=StringDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used"
	)
	private StringDt myFollowUp;
	
	@Child(name="relatedItem", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure"
	)
	private java.util.List<RelatedItem> myRelatedItem;
	
	@Child(name="notes", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Any other notes about the procedure - e.g. the operative notes"
	)
	private StringDt myNotes;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPatient,  myType,  myBodySite,  myIndication,  myPerformer,  myDate,  myEncounter,  myOutcome,  myReport,  myComplication,  myFollowUp,  myRelatedItem,  myNotes);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPatient, myType, myBodySite, myIndication, myPerformer, myDate, myEncounter, myOutcome, myReport, myComplication, myFollowUp, myRelatedItem, myNotes);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Procedure setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
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
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>patient</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person on whom the procedure was performed
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
     * The person on whom the procedure was performed
     * </p> 
	 */
	public Procedure setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded
     * </p> 
	 */
	public Procedure setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>bodySite</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public Procedure setBodySite(java.util.List<CodeableConceptDt> theValue) {
		myBodySite = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>bodySite</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public CodeableConceptDt addBodySite() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getBodySite().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>bodySite</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public CodeableConceptDt getBodySiteFirstRep() {
		if (getBodySite().isEmpty()) {
			return addBodySite();
		}
		return getBodySite().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>indication</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getIndication() {  
		if (myIndication == null) {
			myIndication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myIndication;
	}

	/**
	 * Sets the value(s) for <b>indication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public Procedure setIndication(java.util.List<CodeableConceptDt> theValue) {
		myIndication = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>indication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public CodeableConceptDt addIndication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getIndication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>indication</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public CodeableConceptDt getIndicationFirstRep() {
		if (getIndication().isEmpty()) {
			return addIndication();
		}
		return getIndication().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>performer</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public java.util.List<Performer> getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new java.util.ArrayList<Performer>();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public Procedure setPerformer(java.util.List<Performer> theValue) {
		myPerformer = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public Performer addPerformer() {
		Performer newType = new Performer();
		getPerformer().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>performer</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public Performer getPerformerFirstRep() {
		if (getPerformer().isEmpty()) {
			return addPerformer();
		}
		return getPerformer().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     * </p> 
	 */
	public PeriodDt getDate() {  
		if (myDate == null) {
			myDate = new PeriodDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     * </p> 
	 */
	public Procedure setDate(PeriodDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>encounter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The encounter during which the procedure was performed
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The encounter during which the procedure was performed
     * </p> 
	 */
	public Procedure setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>outcome</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public StringDt getOutcomeElement() {  
		if (myOutcome == null) {
			myOutcome = new StringDt();
		}
		return myOutcome;
	}

	
	/**
	 * Gets the value(s) for <b>outcome</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public String getOutcome() {  
		return getOutcomeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>outcome</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public Procedure setOutcome(StringDt theValue) {
		myOutcome = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>outcome</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public Procedure setOutcome( String theString) {
		myOutcome = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>report</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReport() {  
		if (myReport == null) {
			myReport = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReport;
	}

	/**
	 * Sets the value(s) for <b>report</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies
     * </p> 
	 */
	public Procedure setReport(java.util.List<ResourceReferenceDt> theValue) {
		myReport = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>report</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies
     * </p> 
	 */
	public ResourceReferenceDt addReport() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReport().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>complication</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getComplication() {  
		if (myComplication == null) {
			myComplication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myComplication;
	}

	/**
	 * Sets the value(s) for <b>complication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public Procedure setComplication(java.util.List<CodeableConceptDt> theValue) {
		myComplication = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>complication</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public CodeableConceptDt addComplication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getComplication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>complication</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public CodeableConceptDt getComplicationFirstRep() {
		if (getComplication().isEmpty()) {
			return addComplication();
		}
		return getComplication().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>followUp</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public StringDt getFollowUpElement() {  
		if (myFollowUp == null) {
			myFollowUp = new StringDt();
		}
		return myFollowUp;
	}

	
	/**
	 * Gets the value(s) for <b>followUp</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public String getFollowUp() {  
		return getFollowUpElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>followUp</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public Procedure setFollowUp(StringDt theValue) {
		myFollowUp = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>followUp</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public Procedure setFollowUp( String theString) {
		myFollowUp = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relatedItem</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public java.util.List<RelatedItem> getRelatedItem() {  
		if (myRelatedItem == null) {
			myRelatedItem = new java.util.ArrayList<RelatedItem>();
		}
		return myRelatedItem;
	}

	/**
	 * Sets the value(s) for <b>relatedItem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public Procedure setRelatedItem(java.util.List<RelatedItem> theValue) {
		myRelatedItem = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>relatedItem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public RelatedItem addRelatedItem() {
		RelatedItem newType = new RelatedItem();
		getRelatedItem().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relatedItem</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public RelatedItem getRelatedItemFirstRep() {
		if (getRelatedItem().isEmpty()) {
			return addRelatedItem();
		}
		return getRelatedItem().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public StringDt getNotesElement() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public String getNotes() {  
		return getNotesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public Procedure setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public Procedure setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Procedure.performer</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	@Block()	
	public static class Performer 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="person", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class	})
	@Description(
		shortDefinition="",
		formalDefinition="The practitioner who was involved in the procedure"
	)
	private ResourceReferenceDt myPerson;
	
	@Child(name="role", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="E.g. surgeon, anaethetist, endoscopist"
	)
	private CodeableConceptDt myRole;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPerson,  myRole);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPerson, myRole);
	}

	/**
	 * Gets the value(s) for <b>person</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner who was involved in the procedure
     * </p> 
	 */
	public ResourceReferenceDt getPerson() {  
		if (myPerson == null) {
			myPerson = new ResourceReferenceDt();
		}
		return myPerson;
	}

	/**
	 * Sets the value(s) for <b>person</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner who was involved in the procedure
     * </p> 
	 */
	public Performer setPerson(ResourceReferenceDt theValue) {
		myPerson = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>role</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * E.g. surgeon, anaethetist, endoscopist
     * </p> 
	 */
	public CodeableConceptDt getRole() {  
		if (myRole == null) {
			myRole = new CodeableConceptDt();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * E.g. surgeon, anaethetist, endoscopist
     * </p> 
	 */
	public Performer setRole(CodeableConceptDt theValue) {
		myRole = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Procedure.relatedItem</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	@Block()	
	public static class RelatedItem 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ProcedureRelationshipType",
		formalDefinition="The nature of the relationship"
	)
	private BoundCodeDt<ProcedureRelationshipTypeEnum> myType;
	
	@Child(name="target", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.AllergyIntolerance.class, 		ca.uhn.fhir.model.dev.resource.CarePlan.class, 		ca.uhn.fhir.model.dev.resource.Condition.class, 		ca.uhn.fhir.model.dev.resource.DiagnosticReport.class, 		ca.uhn.fhir.model.dev.resource.FamilyHistory.class, 		ca.uhn.fhir.model.dev.resource.ImagingStudy.class, 		ca.uhn.fhir.model.dev.resource.Immunization.class, 		ca.uhn.fhir.model.dev.resource.ImmunizationRecommendation.class, 		ca.uhn.fhir.model.dev.resource.MedicationAdministration.class, 		ca.uhn.fhir.model.dev.resource.MedicationDispense.class, 		ca.uhn.fhir.model.dev.resource.MedicationPrescription.class, 		ca.uhn.fhir.model.dev.resource.MedicationStatement.class, 		ca.uhn.fhir.model.dev.resource.Observation.class, 		ca.uhn.fhir.model.dev.resource.Procedure.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myTarget);
	}

	/**
	 * Gets the value(s) for <b>type</b> (ProcedureRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public BoundCodeDt<ProcedureRelationshipTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<ProcedureRelationshipTypeEnum>(ProcedureRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (ProcedureRelationshipType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (ProcedureRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public RelatedItem setType(BoundCodeDt<ProcedureRelationshipTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (ProcedureRelationshipType)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public RelatedItem setType(ProcedureRelationshipTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> ().
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
	 * Sets the value(s) for <b>target</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public RelatedItem setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Procedure";
    }

}
