















package ca.uhn.fhir.model.dstu.resource;


import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;

import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AdmitSourceEnum;
import ca.uhn.fhir.model.dstu.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.resource.Group;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.valueset.LocationModeEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.LocationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu.resource.Media;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageEventEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageTransportEnum;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.ObservationInterpretationCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.OrganizationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ParticipantTypeEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.PatientRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerSpecialtyEnum;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.dstu.resource.Substance;
import ca.uhn.fhir.model.dstu.valueset.SubstanceTypeEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>Group</b> Resource
 * (Group of multiple entities)
 *
 * <p>
 * <b>Definition:</b>
 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not expected to act collectively and are not formally or legally recognized.  I.e. A collection of entities that isn't an Organization
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Group", profile="http://hl7.org/fhir/profiles/Group", id="group")
public class Group extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of resources the group contains</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.type</b><br/>
	 * </p>
	 */
	public static final String SP_TYPE = "type";

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The kind of resources contained</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.code</b><br/>
	 * </p>
	 */
	public static final String SP_CODE = "code";

	/**
	 * Search parameter constant for <b>actual</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.actual</b><br/>
	 * </p>
	 */
	public static final String SP_ACTUAL = "actual";

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>member</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Group.member</b><br/>
	 * </p>
	 */
	public static final String SP_MEMBER = "member";

	/**
	 * Search parameter constant for <b>characteristic</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.code</b><br/>
	 * </p>
	 */
	public static final String SP_CHARACTERISTIC = "characteristic";

	/**
	 * Search parameter constant for <b>value</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.value[x]</b><br/>
	 * </p>
	 */
	public static final String SP_VALUE = "value";

	/**
	 * Search parameter constant for <b>exclude</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.exclude</b><br/>
	 * </p>
	 */
	public static final String SP_EXCLUDE = "exclude";

	/**
	 * Search parameter constant for <b>characteristic-value</b>
	 * <p>
	 * Description: <b>A composite of both characteristic and value</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>characteristic & value</b><br/>
	 * </p>
	 */
	public static final String SP_CHARACTERISTIC_VALUE = "characteristic-value";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Unique id",
		formalDefinition="A unique business identifier for this group"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="person | animal | practitioner | device | medication | substance",
		formalDefinition="Identifies the broad classification of the kind of resources the group includes"
	)
	private BoundCodeDt<GroupTypeEnum> myType;
	
	@Child(name="actual", type=BooleanDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Descriptive or actual",
		formalDefinition="If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals"
	)
	private BooleanDt myActual;
	
	@Child(name="code", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Kind of Group members",
		formalDefinition="Provides a specific type of resource the group includes.  E.g. \"cow\", \"syringe\", etc."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Label for Group",
		formalDefinition="A label assigned to the group for human identification and communication"
	)
	private StringDt myName;
	
	@Child(name="quantity", type=IntegerDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Number of members",
		formalDefinition="A count of the number of resource instances that are part of the group"
	)
	private IntegerDt myQuantity;
	
	@Child(name="characteristic", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Trait of group members",
		formalDefinition="Identifies the traits shared by members of the group"
	)
	private java.util.List<Characteristic> myCharacteristic;
	
	@Child(name="member", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class,
		ca.uhn.fhir.model.dstu.resource.Practitioner.class,
		ca.uhn.fhir.model.dstu.resource.Device.class,
		ca.uhn.fhir.model.dstu.resource.Medication.class,
		ca.uhn.fhir.model.dstu.resource.Substance.class,
	})
	@Description(
		shortDefinition="Who is in group",
		formalDefinition="Identifies the resource instances that are members of the group."
	)
	private java.util.List<ResourceReferenceDt> myMember;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  myActual,  myCode,  myName,  myQuantity,  myCharacteristic,  myMember);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, myActual, myCode, myName, myQuantity, myCharacteristic, myMember);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Unique id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Unique id)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>type</b> (person | animal | practitioner | device | medication | substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public BoundCodeDt<GroupTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<GroupTypeEnum>(GroupTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public void setType(BoundCodeDt<GroupTypeEnum> theValue) {
		myType = theValue;
	}


	/**
	 * Sets the value(s) for <b>type</b> (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public void setType(GroupTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>actual</b> (Descriptive or actual).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public BooleanDt getActual() {  
		if (myActual == null) {
			myActual = new BooleanDt();
		}
		return myActual;
	}

	/**
	 * Sets the value(s) for <b>actual</b> (Descriptive or actual)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public void setActual(BooleanDt theValue) {
		myActual = theValue;
	}


 	/**
	 * Sets the value for <b>actual</b> (Descriptive or actual)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public void setActual( Boolean theBoolean) {
		myActual = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Kind of Group members).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a specific type of resource the group includes.  E.g. \"cow\", \"syringe\", etc.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Kind of Group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a specific type of resource the group includes.  E.g. \"cow\", \"syringe\", etc.
     * </p> 
	 */
	public void setCode(CodeableConceptDt theValue) {
		myCode = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>name</b> (Label for Group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Label for Group)
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Label for Group)
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>quantity</b> (Number of members).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public IntegerDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new IntegerDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Number of members)
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public void setQuantity(IntegerDt theValue) {
		myQuantity = theValue;
	}


 	/**
	 * Sets the value for <b>quantity</b> (Number of members)
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public void setQuantity( Integer theInteger) {
		myQuantity = new IntegerDt(theInteger); 
	}

 
	/**
	 * Gets the value(s) for <b>characteristic</b> (Trait of group members).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public java.util.List<Characteristic> getCharacteristic() {  
		if (myCharacteristic == null) {
			myCharacteristic = new java.util.ArrayList<Characteristic>();
		}
		return myCharacteristic;
	}

	/**
	 * Sets the value(s) for <b>characteristic</b> (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public void setCharacteristic(java.util.List<Characteristic> theValue) {
		myCharacteristic = theValue;
	}

	/**
	 * Adds and returns a new value for <b>characteristic</b> (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public Characteristic addCharacteristic() {
		Characteristic newType = new Characteristic();
		getCharacteristic().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>member</b> (Who is in group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getMember() {  
		return myMember;
	}

	/**
	 * Sets the value(s) for <b>member</b> (Who is in group)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public void setMember(java.util.List<ResourceReferenceDt> theValue) {
		myMember = theValue;
	}


  
	/**
	 * Block class for child element: <b>Group.characteristic</b> (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	@Block(name="Group.characteristic")	
	public static class Characteristic extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Kind of characteristic",
		formalDefinition="A code that identifies the kind of trait being asserted"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="value", order=1, min=1, max=1, type={
		CodeableConceptDt.class,
		BooleanDt.class,
		QuantityDt.class,
		RangeDt.class,
	})
	@Description(
		shortDefinition="Value held by characteristic",
		formalDefinition="The value of the trait that holds (or does not hold - see 'exclude') for members of the group"
	)
	private IDatatype myValue;
	
	@Child(name="exclude", type=BooleanDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Group includes or excludes",
		formalDefinition="If true, indicates the characteristic is one that is NOT held by members of the group"
	)
	private BooleanDt myExclude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myValue,  myExclude);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myValue, myExclude);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Kind of characteristic).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies the kind of trait being asserted
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Kind of characteristic)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies the kind of trait being asserted
     * </p> 
	 */
	public void setCode(CodeableConceptDt theValue) {
		myCode = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>value[x]</b> (Value held by characteristic).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the trait that holds (or does not hold - see 'exclude') for members of the group
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> (Value held by characteristic)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the trait that holds (or does not hold - see 'exclude') for members of the group
     * </p> 
	 */
	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>exclude</b> (Group includes or excludes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public BooleanDt getExclude() {  
		if (myExclude == null) {
			myExclude = new BooleanDt();
		}
		return myExclude;
	}

	/**
	 * Sets the value(s) for <b>exclude</b> (Group includes or excludes)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public void setExclude(BooleanDt theValue) {
		myExclude = theValue;
	}


 	/**
	 * Sets the value for <b>exclude</b> (Group includes or excludes)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public void setExclude( Boolean theBoolean) {
		myExclude = new BooleanDt(theBoolean); 
	}

 

	}




}