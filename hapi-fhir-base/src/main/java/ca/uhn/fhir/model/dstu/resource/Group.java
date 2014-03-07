















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;
import ca.uhn.fhir.model.dstu.valueset.*;

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
@ResourceDef(name="Group")
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
	private IdentifierDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	private BoundCodeDt<GroupTypeEnum> myType;
	
	@Child(name="actual", type=BooleanDt.class, order=2, min=1, max=1)	
	private BooleanDt myActual;
	
	@Child(name="code", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	private CodeableConceptDt myCode;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="quantity", type=IntegerDt.class, order=5, min=0, max=1)	
	private IntegerDt myQuantity;
	
	@Child(name="characteristic", order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<Characteristic> myCharacteristic;
	
	@Child(name="member", order=7, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Patient.class,
		Practitioner.class,
		Device.class,
		Medication.class,
		Substance.class,
	})	
	private List<ResourceReference> myMember;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  myActual,  myCode,  myName,  myQuantity,  myCharacteristic,  myMember);
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
     * Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.
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
     * Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.
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
	public List<Characteristic> getCharacteristic() {  
		if (myCharacteristic == null) {
			myCharacteristic = new ArrayList<Characteristic>();
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
	public void setCharacteristic(List<Characteristic> theValue) {
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
	public List<ResourceReference> getMember() {  
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
	public void setMember(List<ResourceReference> theValue) {
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
	private CodeableConceptDt myCode;
	
	@Child(name="value", order=1, min=1, max=1, choice=@Choice(types= {
		CodeableConceptDt.class,
		BooleanDt.class,
		QuantityDt.class,
		RangeDt.class,
	}))	
	private IDatatype myValue;
	
	@Child(name="exclude", type=BooleanDt.class, order=2, min=1, max=1)	
	private BooleanDt myExclude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myValue,  myExclude);
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