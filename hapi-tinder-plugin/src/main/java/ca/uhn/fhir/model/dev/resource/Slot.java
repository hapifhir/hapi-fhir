















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
 * HAPI/FHIR <b>Slot</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Slot">http://hl7.org/fhir/profiles/Slot</a> 
 * </p>
 *
 */
@ResourceDef(name="Slot", profile="http://hl7.org/fhir/profiles/Slot", id="slot")
public class Slot 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>slottype</b>
	 * <p>
	 * Description: <b>The type of appointments that can be booked into the slot</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="slottype", path="Slot.type", description="The type of appointments that can be booked into the slot", type="token"  )
	public static final String SP_SLOTTYPE = "slottype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>slottype</b>
	 * <p>
	 * Description: <b>The type of appointments that can be booked into the slot</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SLOTTYPE = new TokenClientParam(SP_SLOTTYPE);

	/**
	 * Search parameter constant for <b>schedule</b>
	 * <p>
	 * Description: <b>The Schedule Resource that we are seeking a slot within</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Slot.schedule</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="schedule", path="Slot.schedule", description="The Schedule Resource that we are seeking a slot within", type="reference"  )
	public static final String SP_SCHEDULE = "schedule";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>schedule</b>
	 * <p>
	 * Description: <b>The Schedule Resource that we are seeking a slot within</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Slot.schedule</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SCHEDULE = new ReferenceClientParam(SP_SCHEDULE);

	/**
	 * Search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b>Appointment date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Slot.start</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="start", path="Slot.start", description="Appointment date/time.", type="date"  )
	public static final String SP_START = "start";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b>Appointment date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Slot.start</b><br/>
	 * </p>
	 */
	public static final DateClientParam START = new DateClientParam(SP_START);

	/**
	 * Search parameter constant for <b>fbtype</b>
	 * <p>
	 * Description: <b>The free/busy status of the appointment</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.freeBusyType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="fbtype", path="Slot.freeBusyType", description="The free/busy status of the appointment", type="token"  )
	public static final String SP_FBTYPE = "fbtype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>fbtype</b>
	 * <p>
	 * Description: <b>The free/busy status of the appointment</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.freeBusyType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FBTYPE = new TokenClientParam(SP_FBTYPE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Slot.freeBusyType</b>".
	 */
	public static final Include INCLUDE_FREEBUSYTYPE = new Include("Slot.freeBusyType");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Slot.schedule</b>".
	 */
	public static final Include INCLUDE_SCHEDULE = new Include("Slot.schedule");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Slot.start</b>".
	 */
	public static final Include INCLUDE_START = new Include("Slot.start");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Slot.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Slot.type");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private CodeableConceptDt myType;
	
	@Child(name="schedule", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Schedule.class	})
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private ResourceReferenceDt mySchedule;
	
	@Child(name="freeBusyType", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="SlotStatus",
		formalDefinition=""
	)
	private BoundCodeDt<SlotStatusEnum> myFreeBusyType;
	
	@Child(name="start", type=InstantDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private InstantDt myStart;
	
	@Child(name="end", type=InstantDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private InstantDt myEnd;
	
	@Child(name="overbooked", type=BooleanDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private BooleanDt myOverbooked;
	
	@Child(name="comment", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myComment;
	
	@Child(name="lastModified", type=DateTimeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private DateTimeDt myLastModified;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  mySchedule,  myFreeBusyType,  myStart,  myEnd,  myOverbooked,  myComment,  myLastModified);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, mySchedule, myFreeBusyType, myStart, myEnd, myOverbooked, myComment, myLastModified);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
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
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> ()
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
	 * Gets the first repetition for <b>identifier</b> (),
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
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
     * </p> 
	 */
	public Slot setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>schedule</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getSchedule() {  
		if (mySchedule == null) {
			mySchedule = new ResourceReferenceDt();
		}
		return mySchedule;
	}

	/**
	 * Sets the value(s) for <b>schedule</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setSchedule(ResourceReferenceDt theValue) {
		mySchedule = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>freeBusyType</b> (SlotStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BoundCodeDt<SlotStatusEnum> getFreeBusyTypeElement() {  
		if (myFreeBusyType == null) {
			myFreeBusyType = new BoundCodeDt<SlotStatusEnum>(SlotStatusEnum.VALUESET_BINDER);
		}
		return myFreeBusyType;
	}

	
	/**
	 * Gets the value(s) for <b>freeBusyType</b> (SlotStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getFreeBusyType() {  
		return getFreeBusyTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>freeBusyType</b> (SlotStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setFreeBusyType(BoundCodeDt<SlotStatusEnum> theValue) {
		myFreeBusyType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>freeBusyType</b> (SlotStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setFreeBusyType(SlotStatusEnum theValue) {
		getFreeBusyTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>start</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getStartElement() {  
		if (myStart == null) {
			myStart = new InstantDt();
		}
		return myStart;
	}

	
	/**
	 * Gets the value(s) for <b>start</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Date getStart() {  
		return getStartElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>start</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setStart(InstantDt theValue) {
		myStart = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>start</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setStartWithMillisPrecision( Date theDate) {
		myStart = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>start</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setStart( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStart = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getEndElement() {  
		if (myEnd == null) {
			myEnd = new InstantDt();
		}
		return myEnd;
	}

	
	/**
	 * Gets the value(s) for <b>end</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Date getEnd() {  
		return getEndElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>end</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setEnd(InstantDt theValue) {
		myEnd = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>end</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setEndWithMillisPrecision( Date theDate) {
		myEnd = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>overbooked</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public BooleanDt getOverbookedElement() {  
		if (myOverbooked == null) {
			myOverbooked = new BooleanDt();
		}
		return myOverbooked;
	}

	
	/**
	 * Gets the value(s) for <b>overbooked</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Boolean getOverbooked() {  
		return getOverbookedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>overbooked</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setOverbooked(BooleanDt theValue) {
		myOverbooked = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>overbooked</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setOverbooked( boolean theBoolean) {
		myOverbooked = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getCommentElement() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}

	
	/**
	 * Gets the value(s) for <b>comment</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getComment() {  
		return getCommentElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comment</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>lastModified</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getLastModifiedElement() {  
		if (myLastModified == null) {
			myLastModified = new DateTimeDt();
		}
		return myLastModified;
	}

	
	/**
	 * Gets the value(s) for <b>lastModified</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Date getLastModified() {  
		return getLastModifiedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lastModified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setLastModified(DateTimeDt theValue) {
		myLastModified = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lastModified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setLastModified( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myLastModified = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>lastModified</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setLastModifiedWithSecondsPrecision( Date theDate) {
		myLastModified = new DateTimeDt(theDate); 
		return this; 
	}

 


    @Override
    public String getResourceName() {
        return "Slot";
    }

}
