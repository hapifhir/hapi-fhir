















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
 * HAPI/FHIR <b>Medication</b> Resource
 * (Definition of a Medication)
 *
 * <p>
 * <b>Definition:</b>
 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Medication">http://hl7.org/fhir/profiles/Medication</a> 
 * </p>
 *
 */
@ResourceDef(name="Medication", profile="http://hl7.org/fhir/profiles/Medication", id="medication")
public class Medication extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Medication.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Medication.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Medication.manufacturer", description="", type="reference"  )
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MANUFACTURER = new ReferenceClientParam(SP_MANUFACTURER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.manufacturer</b>".
	 */
	public static final Include INCLUDE_MANUFACTURER = new Include("Medication.manufacturer");

	/**
	 * Search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="form", path="Medication.product.form", description="", type="token"  )
	public static final String SP_FORM = "form";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FORM = new TokenClientParam(SP_FORM);

	/**
	 * Search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="ingredient", path="Medication.product.ingredient.item", description="", type="reference"  )
	public static final String SP_INGREDIENT = "ingredient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam INGREDIENT = new ReferenceClientParam(SP_INGREDIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.product.ingredient.item</b>".
	 */
	public static final Include INCLUDE_PRODUCT_INGREDIENT_ITEM = new Include("Medication.product.ingredient.item");

	/**
	 * Search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="container", path="Medication.package.container", description="", type="token"  )
	public static final String SP_CONTAINER = "container";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONTAINER = new TokenClientParam(SP_CONTAINER);

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="content", path="Medication.package.content.item", description="", type="reference"  )
	public static final String SP_CONTENT = "content";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CONTENT = new ReferenceClientParam(SP_CONTENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.package.content.item</b>".
	 */
	public static final Include INCLUDE_PACKAGE_CONTENT_ITEM = new Include("Medication.package.content.item");


	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Common / Commercial name",
		formalDefinition="The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code"
	)
	private StringDt myName;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Codes that identify this medication",
		formalDefinition="A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="isBrand", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="True if a brand",
		formalDefinition="Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)"
	)
	private BooleanDt myIsBrand;
	
	@Child(name="manufacturer", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Manufacturer of the item",
		formalDefinition="Describes the details of the manufacturer"
	)
	private ResourceReferenceDt myManufacturer;
	
	@Child(name="kind", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="product | package",
		formalDefinition="Medications are either a single administrable product or a package that contains one or more products."
	)
	private BoundCodeDt<MedicationKindEnum> myKind;
	
	@Child(name="product", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Administrable medication details",
		formalDefinition="Information that only applies to products (not packages)"
	)
	private Product myProduct;
	
	@Child(name="package", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Details about packaged medications",
		formalDefinition="Information that only applies to packages (not products)"
	)
	private CodeDt myPackage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myCode,  myIsBrand,  myManufacturer,  myKind,  myProduct,  myPackage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myCode, myIsBrand, myManufacturer, myKind, myProduct, myPackage);
	}
	

	/**
	 * Gets the value(s) for <b>name</b> (Common / Commercial name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Common / Commercial name)
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public Medication setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Common / Commercial name)
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public Medication setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Codes that identify this medication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Codes that identify this medication)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes
     * </p> 
	 */
	public Medication setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>isBrand</b> (True if a brand).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public BooleanDt getIsBrand() {  
		if (myIsBrand == null) {
			myIsBrand = new BooleanDt();
		}
		return myIsBrand;
	}

	/**
	 * Sets the value(s) for <b>isBrand</b> (True if a brand)
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public Medication setIsBrand(BooleanDt theValue) {
		myIsBrand = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>isBrand</b> (True if a brand)
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public Medication setIsBrand( boolean theBoolean) {
		myIsBrand = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>manufacturer</b> (Manufacturer of the item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the details of the manufacturer
     * </p> 
	 */
	public ResourceReferenceDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReferenceDt();
		}
		return myManufacturer;
	}

	/**
	 * Sets the value(s) for <b>manufacturer</b> (Manufacturer of the item)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the details of the manufacturer
     * </p> 
	 */
	public Medication setManufacturer(ResourceReferenceDt theValue) {
		myManufacturer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>kind</b> (product | package).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public BoundCodeDt<MedicationKindEnum> getKind() {  
		if (myKind == null) {
			myKind = new BoundCodeDt<MedicationKindEnum>(MedicationKindEnum.VALUESET_BINDER);
		}
		return myKind;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (product | package)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public Medication setKind(BoundCodeDt<MedicationKindEnum> theValue) {
		myKind = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (product | package)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public Medication setKind(MedicationKindEnum theValue) {
		getKind().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>product</b> (Administrable medication details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	public Product getProduct() {  
		if (myProduct == null) {
			myProduct = new Product();
		}
		return myProduct;
	}

	/**
	 * Sets the value(s) for <b>product</b> (Administrable medication details)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	public Medication setProduct(Product theValue) {
		myProduct = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>package</b> (Details about packaged medications).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public CodeDt getPackage() {  
		if (myPackage == null) {
			myPackage = new CodeDt();
		}
		return myPackage;
	}

	/**
	 * Sets the value(s) for <b>package</b> (Details about packaged medications)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public Medication setPackage(CodeDt theValue) {
		myPackage = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>package</b> (Details about packaged medications)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public Medication setPackage( String theCode) {
		myPackage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Medication.product</b> (Administrable medication details)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	@Block()	
	public static class Product extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="form", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="powder | tablets | carton +",
		formalDefinition="Describes the form of the item.  Powder; tables; carton"
	)
	private CodeableConceptDt myForm;
	
	@Child(name="ingredient", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Active or inactive ingredient",
		formalDefinition="Identifies a particular constituent of interest in the product"
	)
	private java.util.List<ProductIngredient> myIngredient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myForm,  myIngredient);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myForm, myIngredient);
	}
	

	/**
	 * Gets the value(s) for <b>form</b> (powder | tablets | carton +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the form of the item.  Powder; tables; carton
     * </p> 
	 */
	public CodeableConceptDt getForm() {  
		if (myForm == null) {
			myForm = new CodeableConceptDt();
		}
		return myForm;
	}

	/**
	 * Sets the value(s) for <b>form</b> (powder | tablets | carton +)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the form of the item.  Powder; tables; carton
     * </p> 
	 */
	public Product setForm(CodeableConceptDt theValue) {
		myForm = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>ingredient</b> (Active or inactive ingredient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public java.util.List<ProductIngredient> getIngredient() {  
		if (myIngredient == null) {
			myIngredient = new java.util.ArrayList<ProductIngredient>();
		}
		return myIngredient;
	}

	/**
	 * Sets the value(s) for <b>ingredient</b> (Active or inactive ingredient)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public Product setIngredient(java.util.List<ProductIngredient> theValue) {
		myIngredient = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>ingredient</b> (Active or inactive ingredient)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public ProductIngredient addIngredient() {
		ProductIngredient newType = new ProductIngredient();
		getIngredient().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>ingredient</b> (Active or inactive ingredient),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public ProductIngredient getIngredientFirstRep() {
		if (getIngredient().isEmpty()) {
			return addIngredient();
		}
		return getIngredient().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Medication.product.ingredient</b> (Active or inactive ingredient)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	@Block()	
	public static class ProductIngredient extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="item", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class, 		ca.uhn.fhir.model.dstu.resource.Medication.class	})
	@Description(
		shortDefinition="The product contained",
		formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication"
	)
	private ResourceReferenceDt myItem;
	
	@Child(name="amount", type=RatioDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="How much ingredient in product",
		formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet"
	)
	private RatioDt myAmount;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myItem,  myAmount);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myItem, myAmount);
	}
	

	/**
	 * Gets the value(s) for <b>item</b> (The product contained).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual ingredient - either a substance (simple ingredient) or another medication
     * </p> 
	 */
	public ResourceReferenceDt getItem() {  
		if (myItem == null) {
			myItem = new ResourceReferenceDt();
		}
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> (The product contained)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual ingredient - either a substance (simple ingredient) or another medication
     * </p> 
	 */
	public ProductIngredient setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>amount</b> (How much ingredient in product).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet
     * </p> 
	 */
	public RatioDt getAmount() {  
		if (myAmount == null) {
			myAmount = new RatioDt();
		}
		return myAmount;
	}

	/**
	 * Sets the value(s) for <b>amount</b> (How much ingredient in product)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet
     * </p> 
	 */
	public ProductIngredient setAmount(RatioDt theValue) {
		myAmount = theValue;
		return this;
	}

  

	}





    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.MEDICATION;
    }

}