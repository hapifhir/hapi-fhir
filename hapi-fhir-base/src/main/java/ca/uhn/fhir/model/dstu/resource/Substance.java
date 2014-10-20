















package ca.uhn.fhir.model.dstu.resource;


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;

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
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
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
 * HAPI/FHIR <b>Substance</b> Resource
 * (A homogeneous material with a definite composition)
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
 * <a href="http://hl7.org/fhir/profiles/Substance">http://hl7.org/fhir/profiles/Substance</a> 
 * </p>
 *
 */
@ResourceDef(name="Substance", profile="http://hl7.org/fhir/profiles/Substance", id="substance")
public class Substance extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the substance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Substance.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Substance.type", description="The type of the substance", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the substance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Substance.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Substance.instance.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Substance.instance.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Substance.instance.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>expiry</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Substance.instance.expiry</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="expiry", path="Substance.instance.expiry", description="", type="date"  )
	public static final String SP_EXPIRY = "expiry";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>expiry</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Substance.instance.expiry</b><br/>
	 * </p>
	 */
	public static final DateClientParam EXPIRY = new DateClientParam(SP_EXPIRY);

	/**
	 * Search parameter constant for <b>quantity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Substance.instance.quantity</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="quantity", path="Substance.instance.quantity", description="", type="number"  )
	public static final String SP_QUANTITY = "quantity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>quantity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Substance.instance.quantity</b><br/>
	 * </p>
	 */
	public static final NumberClientParam QUANTITY = new NumberClientParam(SP_QUANTITY);

	/**
	 * Search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Substance.ingredient.substance</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="substance", path="Substance.ingredient.substance", description="", type="reference"  )
	public static final String SP_SUBSTANCE = "substance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Substance.ingredient.substance</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBSTANCE = new ReferenceClientParam(SP_SUBSTANCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Substance.ingredient.substance</b>".
	 */
	public static final Include INCLUDE_INGREDIENT_SUBSTANCE = new Include("Substance.ingredient.substance");


	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="What kind of substance this is",
		formalDefinition="A code (or set of codes) that identify this substance"
	)
	private BoundCodeableConceptDt<SubstanceTypeEnum> myType;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Textual description of the substance, comments",
		formalDefinition="A description of the substance - its appearance, handling requirements, and other usage notes"
	)
	private StringDt myDescription;
	
	@Child(name="instance", order=2, min=0, max=1)	
	@Description(
		shortDefinition="If this describes a specific package/container of the substance",
		formalDefinition="Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance"
	)
	private Instance myInstance;
	
	@Child(name="ingredient", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Composition information about the substance",
		formalDefinition="A substance can be composed of other substances"
	)
	private java.util.List<Ingredient> myIngredient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myDescription,  myInstance,  myIngredient);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myDescription, myInstance, myIngredient);
	}
	

	/**
	 * Gets the value(s) for <b>type</b> (What kind of substance this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this substance
     * </p> 
	 */
	public BoundCodeableConceptDt<SubstanceTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<SubstanceTypeEnum>(SubstanceTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (What kind of substance this is)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this substance
     * </p> 
	 */
	public Substance setType(BoundCodeableConceptDt<SubstanceTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (What kind of substance this is)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this substance
     * </p> 
	 */
	public Substance setType(SubstanceTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Textual description of the substance, comments).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the substance - its appearance, handling requirements, and other usage notes
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Textual description of the substance, comments)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the substance - its appearance, handling requirements, and other usage notes
     * </p> 
	 */
	public Substance setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Textual description of the substance, comments)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the substance - its appearance, handling requirements, and other usage notes
     * </p> 
	 */
	public Substance setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>instance</b> (If this describes a specific package/container of the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	public Instance getInstance() {  
		if (myInstance == null) {
			myInstance = new Instance();
		}
		return myInstance;
	}

	/**
	 * Sets the value(s) for <b>instance</b> (If this describes a specific package/container of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	public Substance setInstance(Instance theValue) {
		myInstance = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>ingredient</b> (Composition information about the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public java.util.List<Ingredient> getIngredient() {  
		if (myIngredient == null) {
			myIngredient = new java.util.ArrayList<Ingredient>();
		}
		return myIngredient;
	}

	/**
	 * Sets the value(s) for <b>ingredient</b> (Composition information about the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public Substance setIngredient(java.util.List<Ingredient> theValue) {
		myIngredient = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>ingredient</b> (Composition information about the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public Ingredient addIngredient() {
		Ingredient newType = new Ingredient();
		getIngredient().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>ingredient</b> (Composition information about the substance),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public Ingredient getIngredientFirstRep() {
		if (getIngredient().isEmpty()) {
			return addIngredient();
		}
		return getIngredient().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Substance.instance</b> (If this describes a specific package/container of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	@Block()	
	public static class Instance extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the package/container",
		formalDefinition="Identifier associated with the package/container (usually a label affixed directly)"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="expiry", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When no longer valid to use",
		formalDefinition="When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry."
	)
	private DateTimeDt myExpiry;
	
	@Child(name="quantity", type=QuantityDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Amount of substance in the package",
		formalDefinition="The amount of the substance"
	)
	private QuantityDt myQuantity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myExpiry,  myQuantity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myExpiry, myQuantity);
	}
	

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier of the package/container).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier associated with the package/container (usually a label affixed directly)
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier of the package/container)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier associated with the package/container (usually a label affixed directly)
     * </p> 
	 */
	public Instance setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Identifier of the package/container)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier associated with the package/container (usually a label affixed directly)
     * </p> 
	 */
	public Instance setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Identifier of the package/container)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier associated with the package/container (usually a label affixed directly)
     * </p> 
	 */
	public Instance setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expiry</b> (When no longer valid to use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public DateTimeDt getExpiry() {  
		if (myExpiry == null) {
			myExpiry = new DateTimeDt();
		}
		return myExpiry;
	}

	/**
	 * Sets the value(s) for <b>expiry</b> (When no longer valid to use)
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public Instance setExpiry(DateTimeDt theValue) {
		myExpiry = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>expiry</b> (When no longer valid to use)
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public Instance setExpiryWithSecondsPrecision( Date theDate) {
		myExpiry = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>expiry</b> (When no longer valid to use)
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public Instance setExpiry( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myExpiry = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount of substance in the package).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public Instance setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Substance.ingredient</b> (Composition information about the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	@Block()	
	public static class Ingredient extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="quantity", type=RatioDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Optional amount (concentration)",
		formalDefinition="The amount of the ingredient in the substance - a concentration ratio"
	)
	private RatioDt myQuantity;
	
	@Child(name="substance", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="A component of the substance",
		formalDefinition="Another substance that is a component of this substance"
	)
	private ResourceReferenceDt mySubstance;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myQuantity,  mySubstance);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myQuantity, mySubstance);
	}
	

	/**
	 * Gets the value(s) for <b>quantity</b> (Optional amount (concentration)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the ingredient in the substance - a concentration ratio
     * </p> 
	 */
	public RatioDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new RatioDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Optional amount (concentration))
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the ingredient in the substance - a concentration ratio
     * </p> 
	 */
	public Ingredient setQuantity(RatioDt theValue) {
		myQuantity = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>substance</b> (A component of the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Another substance that is a component of this substance
     * </p> 
	 */
	public ResourceReferenceDt getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new ResourceReferenceDt();
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (A component of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Another substance that is a component of this substance
     * </p> 
	 */
	public Ingredient setSubstance(ResourceReferenceDt theValue) {
		mySubstance = theValue;
		return this;
	}

  

	}




    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.SUBSTANCE;
    }

}