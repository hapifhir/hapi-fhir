















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
 * HAPI/FHIR <b>AdverseReaction</b> Resource
 * (Specific reactions to a substance)
 *
 * <p>
 * <b>Definition:</b>
 * Records an unexpected reaction suspected to be related to the exposure of the reaction subject to a substance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Used to track reactions when it is unknown the exact cause but there's a desire to flag/track potential causes.  Also used to capture reactions that are significant for inclusion in the health record or as evidence for an allergy or intolerance.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/AdverseReaction">http://hl7.org/fhir/profiles/AdverseReaction</a> 
 * </p>
 *
 */
@ResourceDef(name="AdverseReaction", profile="http://hl7.org/fhir/profiles/AdverseReaction", id="adversereaction")
public class AdverseReaction 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>symptom</b>
	 * <p>
	 * Description: <b>One of the symptoms of the reaction</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReaction.symptom.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="symptom", path="AdverseReaction.symptom.code", description="One of the symptoms of the reaction", type="token"  )
	public static final String SP_SYMPTOM = "symptom";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>symptom</b>
	 * <p>
	 * Description: <b>One of the symptoms of the reaction</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReaction.symptom.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SYMPTOM = new TokenClientParam(SP_SYMPTOM);

	/**
	 * Search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b>The name or code of the substance that produces the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.exposure.substance</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="substance", path="AdverseReaction.exposure.substance", description="The name or code of the substance that produces the sensitivity", type="reference"  )
	public static final String SP_SUBSTANCE = "substance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b>The name or code of the substance that produces the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.exposure.substance</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBSTANCE = new ReferenceClientParam(SP_SUBSTANCE);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date of the reaction</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReaction.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="AdverseReaction.date", description="The date of the reaction", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date of the reaction</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReaction.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="AdverseReaction.subject", description="The subject that the sensitivity is about", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReaction.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("AdverseReaction.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReaction.exposure.substance</b>".
	 */
	public static final Include INCLUDE_EXPOSURE_SUBSTANCE = new Include("AdverseReaction.exposure.substance");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReaction.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("AdverseReaction.subject");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReaction.symptom.code</b>".
	 */
	public static final Include INCLUDE_SYMPTOM_CODE = new Include("AdverseReaction.symptom.code");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this adverse reaction",
		formalDefinition="This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When the reaction occurred",
		formalDefinition="The date (and possibly time) when the reaction began"
	)
	private DateTimeDt myDate;
	
	@Child(name="subject", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="Who had the reaction",
		formalDefinition="The subject of the adverse reaction"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="didNotOccurFlag", type=BooleanDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Indicates lack of reaction",
		formalDefinition="If true, indicates that no reaction occurred."
	)
	private BooleanDt myDidNotOccurFlag;
	
	@Child(name="recorder", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Practitioner.class, 		ca.uhn.fhir.model.dev.resource.Patient.class	})
	@Description(
		shortDefinition="Who recorded the reaction",
		formalDefinition="Identifies the individual responsible for the information in the reaction record."
	)
	private ResourceReferenceDt myRecorder;
	
	@Child(name="symptom", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What was reaction?",
		formalDefinition="The signs and symptoms that were observed as part of the reaction"
	)
	private java.util.List<Symptom> mySymptom;
	
	@Child(name="exposure", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Suspected substance",
		formalDefinition="An exposure to a substance that preceded a reaction occurrence"
	)
	private java.util.List<Exposure> myExposure;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  mySubject,  myDidNotOccurFlag,  myRecorder,  mySymptom,  myExposure);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, mySubject, myDidNotOccurFlag, myRecorder, mySymptom, myExposure);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this adverse reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this adverse reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public AdverseReaction setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this adverse reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this adverse reaction),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>date</b> (When the reaction occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	
	/**
	 * Gets the value(s) for <b>date</b> (When the reaction occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public Date getDate() {  
		return getDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the reaction occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public AdverseReaction setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> (When the reaction occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public AdverseReaction setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the reaction occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public AdverseReaction setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who had the reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the adverse reaction
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who had the reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the adverse reaction
     * </p> 
	 */
	public AdverseReaction setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>didNotOccurFlag</b> (Indicates lack of reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public BooleanDt getDidNotOccurFlagElement() {  
		if (myDidNotOccurFlag == null) {
			myDidNotOccurFlag = new BooleanDt();
		}
		return myDidNotOccurFlag;
	}

	
	/**
	 * Gets the value(s) for <b>didNotOccurFlag</b> (Indicates lack of reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public Boolean getDidNotOccurFlag() {  
		return getDidNotOccurFlagElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>didNotOccurFlag</b> (Indicates lack of reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public AdverseReaction setDidNotOccurFlag(BooleanDt theValue) {
		myDidNotOccurFlag = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>didNotOccurFlag</b> (Indicates lack of reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public AdverseReaction setDidNotOccurFlag( boolean theBoolean) {
		myDidNotOccurFlag = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the individual responsible for the information in the reaction record.
     * </p> 
	 */
	public ResourceReferenceDt getRecorder() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}

	/**
	 * Sets the value(s) for <b>recorder</b> (Who recorded the reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the individual responsible for the information in the reaction record.
     * </p> 
	 */
	public AdverseReaction setRecorder(ResourceReferenceDt theValue) {
		myRecorder = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>symptom</b> (What was reaction?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public java.util.List<Symptom> getSymptom() {  
		if (mySymptom == null) {
			mySymptom = new java.util.ArrayList<Symptom>();
		}
		return mySymptom;
	}

	/**
	 * Sets the value(s) for <b>symptom</b> (What was reaction?)
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public AdverseReaction setSymptom(java.util.List<Symptom> theValue) {
		mySymptom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>symptom</b> (What was reaction?)
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public Symptom addSymptom() {
		Symptom newType = new Symptom();
		getSymptom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>symptom</b> (What was reaction?),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public Symptom getSymptomFirstRep() {
		if (getSymptom().isEmpty()) {
			return addSymptom();
		}
		return getSymptom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>exposure</b> (Suspected substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public java.util.List<Exposure> getExposure() {  
		if (myExposure == null) {
			myExposure = new java.util.ArrayList<Exposure>();
		}
		return myExposure;
	}

	/**
	 * Sets the value(s) for <b>exposure</b> (Suspected substance)
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public AdverseReaction setExposure(java.util.List<Exposure> theValue) {
		myExposure = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>exposure</b> (Suspected substance)
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public Exposure addExposure() {
		Exposure newType = new Exposure();
		getExposure().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>exposure</b> (Suspected substance),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public Exposure getExposureFirstRep() {
		if (getExposure().isEmpty()) {
			return addExposure();
		}
		return getExposure().get(0); 
	}
  
	/**
	 * Block class for child element: <b>AdverseReaction.symptom</b> (What was reaction?)
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	@Block()	
	public static class Symptom 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="E.g. Rash, vomiting",
		formalDefinition="Indicates the specific sign or symptom that was observed"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="severity", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="severe | serious | moderate | minor",
		formalDefinition="The severity of the sign or symptom"
	)
	private BoundCodeDt<ReactionSeverityEnum> mySeverity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  mySeverity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, mySeverity);
	}

	/**
	 * Gets the value(s) for <b>code</b> (E.g. Rash, vomiting).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the specific sign or symptom that was observed
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (E.g. Rash, vomiting)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the specific sign or symptom that was observed
     * </p> 
	 */
	public Symptom setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>severity</b> (severe | serious | moderate | minor).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public BoundCodeDt<ReactionSeverityEnum> getSeverityElement() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<ReactionSeverityEnum>(ReactionSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	
	/**
	 * Gets the value(s) for <b>severity</b> (severe | serious | moderate | minor).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public String getSeverity() {  
		return getSeverityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>severity</b> (severe | serious | moderate | minor)
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public Symptom setSeverity(BoundCodeDt<ReactionSeverityEnum> theValue) {
		mySeverity = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>severity</b> (severe | serious | moderate | minor)
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public Symptom setSeverity(ReactionSeverityEnum theValue) {
		getSeverityElement().setValueAsEnum(theValue);
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>AdverseReaction.exposure</b> (Suspected substance)
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	@Block()	
	public static class Exposure 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="date", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="When the exposure occurred",
		formalDefinition="Identifies the initial date of the exposure that is suspected to be related to the reaction"
	)
	private DateTimeDt myDate;
	
	@Child(name="type", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="drugadmin | immuniz | coincidental",
		formalDefinition="The type of exposure: Drug Administration, Immunization, Coincidental"
	)
	private BoundCodeDt<ExposureTypeEnum> myType;
	
	@Child(name="causalityExpectation", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="likely | unlikely | confirmed | unknown",
		formalDefinition="A statement of how confident that the recorder was that this exposure caused the reaction"
	)
	private BoundCodeDt<CausalityExpectationEnum> myCausalityExpectation;
	
	@Child(name="substance", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Substance.class	})
	@Description(
		shortDefinition="Presumed causative substance",
		formalDefinition="Substance that is presumed to have caused the adverse reaction"
	)
	private ResourceReferenceDt mySubstance;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDate,  myType,  myCausalityExpectation,  mySubstance);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDate, myType, myCausalityExpectation, mySubstance);
	}

	/**
	 * Gets the value(s) for <b>date</b> (When the exposure occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	
	/**
	 * Gets the value(s) for <b>date</b> (When the exposure occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Date getDate() {  
		return getDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the exposure occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Exposure setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> (When the exposure occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Exposure setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the exposure occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Exposure setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public BoundCodeDt<ExposureTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<ExposureTypeEnum>(ExposureTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public Exposure setType(BoundCodeDt<ExposureTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public Exposure setType(ExposureTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public BoundCodeDt<CausalityExpectationEnum> getCausalityExpectationElement() {  
		if (myCausalityExpectation == null) {
			myCausalityExpectation = new BoundCodeDt<CausalityExpectationEnum>(CausalityExpectationEnum.VALUESET_BINDER);
		}
		return myCausalityExpectation;
	}

	
	/**
	 * Gets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public String getCausalityExpectation() {  
		return getCausalityExpectationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown)
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public Exposure setCausalityExpectation(BoundCodeDt<CausalityExpectationEnum> theValue) {
		myCausalityExpectation = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown)
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public Exposure setCausalityExpectation(CausalityExpectationEnum theValue) {
		getCausalityExpectationElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>substance</b> (Presumed causative substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Substance that is presumed to have caused the adverse reaction
     * </p> 
	 */
	public ResourceReferenceDt getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new ResourceReferenceDt();
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (Presumed causative substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance that is presumed to have caused the adverse reaction
     * </p> 
	 */
	public Exposure setSubstance(ResourceReferenceDt theValue) {
		mySubstance = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "AdverseReaction";
    }

}
