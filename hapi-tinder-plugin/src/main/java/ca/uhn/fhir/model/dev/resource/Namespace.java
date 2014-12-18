















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
 * HAPI/FHIR <b>Namespace</b> Resource
 * (System of unique identification)
 *
 * <p>
 * <b>Definition:</b>
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a \"System\" used within the Identifier and Coding data types.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Namespace">http://hl7.org/fhir/profiles/Namespace</a> 
 * </p>
 *
 */
@ResourceDef(name="Namespace", profile="http://hl7.org/fhir/profiles/Namespace", id="namespace")
public class Namespace 
    extends  BaseResource     implements IResource {



	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="codesystem | identifier | root",
		formalDefinition="Indicates the purpose for the namespace - what kinds of things does it make unique?"
	)
	private BoundCodeDt<NamespaceTypeEnum> myType;
	
	@Child(name="name", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Human-readable label",
		formalDefinition="The descriptive name of this particular identifier type or code system"
	)
	private StringDt myName;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="proposed | active | retired",
		formalDefinition="Indicates whether the namespace is \"ready for use\" or not."
	)
	private BoundCodeDt<NamespaceStatusEnum> myStatus;
	
	@Child(name="country", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ISO 3-char country code",
		formalDefinition="If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system."
	)
	private CodeDt myCountry;
	
	@Child(name="category", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="driver | provider | patient | bank",
		formalDefinition="Categorizes a namespace for easier search by grouping related namespaces."
	)
	private CodeableConceptDt myCategory;
	
	@Child(name="responsible", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Who maintains system namespace?",
		formalDefinition="The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision."
	)
	private StringDt myResponsible;
	
	@Child(name="description", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="What does namespace identify?",
		formalDefinition="Details about what the namespace identifies including scope, granularity, version labeling, etc."
	)
	private StringDt myDescription;
	
	@Child(name="usage", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="How/where is it used",
		formalDefinition="Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc."
	)
	private StringDt myUsage;
	
	@Child(name="uniqueId", order=8, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Unique identifiers used for system",
		formalDefinition="Indicates how the system may be identified when referenced in electronic exchange"
	)
	private java.util.List<UniqueId> myUniqueId;
	
	@Child(name="contact", order=9, min=0, max=1)	
	@Description(
		shortDefinition="Who should be contacted for questions about namespace",
		formalDefinition="The person who can be contacted about this system registration entry"
	)
	private Contact myContact;
	
	@Child(name="replacedBy", order=10, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Namespace.class	})
	@Description(
		shortDefinition="Use this instead",
		formalDefinition="For namespaces that are retired, indicates the namespace that should be used in their place (if any)"
	)
	private ResourceReferenceDt myReplacedBy;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myName,  myStatus,  myCountry,  myCategory,  myResponsible,  myDescription,  myUsage,  myUniqueId,  myContact,  myReplacedBy);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myName, myStatus, myCountry, myCategory, myResponsible, myDescription, myUsage, myUniqueId, myContact, myReplacedBy);
	}

	/**
	 * Gets the value(s) for <b>type</b> (codesystem | identifier | root).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the purpose for the namespace - what kinds of things does it make unique?
     * </p> 
	 */
	public BoundCodeDt<NamespaceTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<NamespaceTypeEnum>(NamespaceTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (codesystem | identifier | root).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the purpose for the namespace - what kinds of things does it make unique?
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (codesystem | identifier | root)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the purpose for the namespace - what kinds of things does it make unique?
     * </p> 
	 */
	public Namespace setType(BoundCodeDt<NamespaceTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (codesystem | identifier | root)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the purpose for the namespace - what kinds of things does it make unique?
     * </p> 
	 */
	public Namespace setType(NamespaceTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (Human-readable label).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The descriptive name of this particular identifier type or code system
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (Human-readable label).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The descriptive name of this particular identifier type or code system
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (Human-readable label)
	 *
     * <p>
     * <b>Definition:</b>
     * The descriptive name of this particular identifier type or code system
     * </p> 
	 */
	public Namespace setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (Human-readable label)
	 *
     * <p>
     * <b>Definition:</b>
     * The descriptive name of this particular identifier type or code system
     * </p> 
	 */
	public Namespace setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (proposed | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the namespace is \"ready for use\" or not.
     * </p> 
	 */
	public BoundCodeDt<NamespaceStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<NamespaceStatusEnum>(NamespaceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (proposed | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the namespace is \"ready for use\" or not.
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (proposed | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the namespace is \"ready for use\" or not.
     * </p> 
	 */
	public Namespace setStatus(BoundCodeDt<NamespaceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (proposed | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the namespace is \"ready for use\" or not.
     * </p> 
	 */
	public Namespace setStatus(NamespaceStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>country</b> (ISO 3-char country code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     * </p> 
	 */
	public CodeDt getCountryElement() {  
		if (myCountry == null) {
			myCountry = new CodeDt();
		}
		return myCountry;
	}

	
	/**
	 * Gets the value(s) for <b>country</b> (ISO 3-char country code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     * </p> 
	 */
	public String getCountry() {  
		return getCountryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>country</b> (ISO 3-char country code)
	 *
     * <p>
     * <b>Definition:</b>
     * If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     * </p> 
	 */
	public Namespace setCountry(CodeDt theValue) {
		myCountry = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>country</b> (ISO 3-char country code)
	 *
     * <p>
     * <b>Definition:</b>
     * If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     * </p> 
	 */
	public Namespace setCountry( String theCode) {
		myCountry = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>category</b> (driver | provider | patient | bank).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Categorizes a namespace for easier search by grouping related namespaces.
     * </p> 
	 */
	public CodeableConceptDt getCategory() {  
		if (myCategory == null) {
			myCategory = new CodeableConceptDt();
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (driver | provider | patient | bank)
	 *
     * <p>
     * <b>Definition:</b>
     * Categorizes a namespace for easier search by grouping related namespaces.
     * </p> 
	 */
	public Namespace setCategory(CodeableConceptDt theValue) {
		myCategory = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>responsible</b> (Who maintains system namespace?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     * </p> 
	 */
	public StringDt getResponsibleElement() {  
		if (myResponsible == null) {
			myResponsible = new StringDt();
		}
		return myResponsible;
	}

	
	/**
	 * Gets the value(s) for <b>responsible</b> (Who maintains system namespace?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     * </p> 
	 */
	public String getResponsible() {  
		return getResponsibleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>responsible</b> (Who maintains system namespace?)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     * </p> 
	 */
	public Namespace setResponsible(StringDt theValue) {
		myResponsible = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>responsible</b> (Who maintains system namespace?)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     * </p> 
	 */
	public Namespace setResponsible( String theString) {
		myResponsible = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (What does namespace identify?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> (What does namespace identify?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> (What does namespace identify?)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     * </p> 
	 */
	public Namespace setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> (What does namespace identify?)
	 *
     * <p>
     * <b>Definition:</b>
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     * </p> 
	 */
	public Namespace setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>usage</b> (How/where is it used).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     * </p> 
	 */
	public StringDt getUsageElement() {  
		if (myUsage == null) {
			myUsage = new StringDt();
		}
		return myUsage;
	}

	
	/**
	 * Gets the value(s) for <b>usage</b> (How/where is it used).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     * </p> 
	 */
	public String getUsage() {  
		return getUsageElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>usage</b> (How/where is it used)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     * </p> 
	 */
	public Namespace setUsage(StringDt theValue) {
		myUsage = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>usage</b> (How/where is it used)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     * </p> 
	 */
	public Namespace setUsage( String theString) {
		myUsage = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>uniqueId</b> (Unique identifiers used for system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the system may be identified when referenced in electronic exchange
     * </p> 
	 */
	public java.util.List<UniqueId> getUniqueId() {  
		if (myUniqueId == null) {
			myUniqueId = new java.util.ArrayList<UniqueId>();
		}
		return myUniqueId;
	}

	/**
	 * Sets the value(s) for <b>uniqueId</b> (Unique identifiers used for system)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the system may be identified when referenced in electronic exchange
     * </p> 
	 */
	public Namespace setUniqueId(java.util.List<UniqueId> theValue) {
		myUniqueId = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>uniqueId</b> (Unique identifiers used for system)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the system may be identified when referenced in electronic exchange
     * </p> 
	 */
	public UniqueId addUniqueId() {
		UniqueId newType = new UniqueId();
		getUniqueId().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>uniqueId</b> (Unique identifiers used for system),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the system may be identified when referenced in electronic exchange
     * </p> 
	 */
	public UniqueId getUniqueIdFirstRep() {
		if (getUniqueId().isEmpty()) {
			return addUniqueId();
		}
		return getUniqueId().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>contact</b> (Who should be contacted for questions about namespace).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who can be contacted about this system registration entry
     * </p> 
	 */
	public Contact getContact() {  
		if (myContact == null) {
			myContact = new Contact();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> (Who should be contacted for questions about namespace)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who can be contacted about this system registration entry
     * </p> 
	 */
	public Namespace setContact(Contact theValue) {
		myContact = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>replacedBy</b> (Use this instead).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * For namespaces that are retired, indicates the namespace that should be used in their place (if any)
     * </p> 
	 */
	public ResourceReferenceDt getReplacedBy() {  
		if (myReplacedBy == null) {
			myReplacedBy = new ResourceReferenceDt();
		}
		return myReplacedBy;
	}

	/**
	 * Sets the value(s) for <b>replacedBy</b> (Use this instead)
	 *
     * <p>
     * <b>Definition:</b>
     * For namespaces that are retired, indicates the namespace that should be used in their place (if any)
     * </p> 
	 */
	public Namespace setReplacedBy(ResourceReferenceDt theValue) {
		myReplacedBy = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>Namespace.uniqueId</b> (Unique identifiers used for system)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the system may be identified when referenced in electronic exchange
     * </p> 
	 */
	@Block()	
	public static class UniqueId 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="oid | uuid | uri | other",
		formalDefinition="Identifies the unique identifier scheme used for this particular identifier."
	)
	private BoundCodeDt<NamespaceIdentifierTypeEnum> myType;
	
	@Child(name="value", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="The unique identifier",
		formalDefinition="The string that should be sent over the wire to identify the code system or identifier system"
	)
	private StringDt myValue;
	
	@Child(name="preferred", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Is this the id that should be used for this type",
		formalDefinition="Indicates whether this identifier is the \"preferred\" identifier of this type."
	)
	private BooleanDt myPreferred;
	
	@Child(name="period", type=PeriodDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="When is identifier valid?",
		formalDefinition="Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic"
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myValue,  myPreferred,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myValue, myPreferred, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>type</b> (oid | uuid | uri | other).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the unique identifier scheme used for this particular identifier.
     * </p> 
	 */
	public BoundCodeDt<NamespaceIdentifierTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<NamespaceIdentifierTypeEnum>(NamespaceIdentifierTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (oid | uuid | uri | other).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the unique identifier scheme used for this particular identifier.
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (oid | uuid | uri | other)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the unique identifier scheme used for this particular identifier.
     * </p> 
	 */
	public UniqueId setType(BoundCodeDt<NamespaceIdentifierTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (oid | uuid | uri | other)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the unique identifier scheme used for this particular identifier.
     * </p> 
	 */
	public UniqueId setType(NamespaceIdentifierTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>value</b> (The unique identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The string that should be sent over the wire to identify the code system or identifier system
     * </p> 
	 */
	public StringDt getValueElement() {  
		if (myValue == null) {
			myValue = new StringDt();
		}
		return myValue;
	}

	
	/**
	 * Gets the value(s) for <b>value</b> (The unique identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The string that should be sent over the wire to identify the code system or identifier system
     * </p> 
	 */
	public String getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> (The unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * The string that should be sent over the wire to identify the code system or identifier system
     * </p> 
	 */
	public UniqueId setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> (The unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * The string that should be sent over the wire to identify the code system or identifier system
     * </p> 
	 */
	public UniqueId setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>preferred</b> (Is this the id that should be used for this type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this identifier is the \"preferred\" identifier of this type.
     * </p> 
	 */
	public BooleanDt getPreferredElement() {  
		if (myPreferred == null) {
			myPreferred = new BooleanDt();
		}
		return myPreferred;
	}

	
	/**
	 * Gets the value(s) for <b>preferred</b> (Is this the id that should be used for this type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this identifier is the \"preferred\" identifier of this type.
     * </p> 
	 */
	public Boolean getPreferred() {  
		return getPreferredElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>preferred</b> (Is this the id that should be used for this type)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this identifier is the \"preferred\" identifier of this type.
     * </p> 
	 */
	public UniqueId setPreferred(BooleanDt theValue) {
		myPreferred = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>preferred</b> (Is this the id that should be used for this type)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this identifier is the \"preferred\" identifier of this type.
     * </p> 
	 */
	public UniqueId setPreferred( boolean theBoolean) {
		myPreferred = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> (When is identifier valid?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (When is identifier valid?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic
     * </p> 
	 */
	public UniqueId setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>Namespace.contact</b> (Who should be contacted for questions about namespace)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who can be contacted about this system registration entry
     * </p> 
	 */
	@Block()	
	public static class Contact 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=HumanNameDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Name of person",
		formalDefinition="Names of the person who can be contacted"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Phone, email, etc.",
		formalDefinition="Identifies the mechanism(s) by which they can be contacted"
	)
	private java.util.List<ContactPointDt> myTelecom;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myTelecom);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myTelecom);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name of person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Names of the person who can be contacted
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of person)
	 *
     * <p>
     * <b>Definition:</b>
     * Names of the person who can be contacted
     * </p> 
	 */
	public Contact setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>telecom</b> (Phone, email, etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the mechanism(s) by which they can be contacted
     * </p> 
	 */
	public java.util.List<ContactPointDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactPointDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Phone, email, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the mechanism(s) by which they can be contacted
     * </p> 
	 */
	public Contact setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> (Phone, email, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the mechanism(s) by which they can be contacted
     * </p> 
	 */
	public ContactPointDt addTelecom() {
		ContactPointDt newType = new ContactPointDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (Phone, email, etc.),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the mechanism(s) by which they can be contacted
     * </p> 
	 */
	public ContactPointDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  

	}




    @Override
    public String getResourceName() {
        return "Namespace";
    }

}
