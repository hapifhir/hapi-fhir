











package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

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
public class Group implements IResource {

	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	private CodeDt myType;
	
	@Child(name="actual", type=BooleanDt.class, order=2, min=1, max=1)	
	private BooleanDt myActual;
	
	@Child(name="code", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	private CodeableConceptDt myCode;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="quantity", type=IntegerDt.class, order=5, min=0, max=1)	
	private IntegerDt myQuantity;
	
	@Child(name="characteristic", order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myCharacteristic;
	
	@Child(name="member", order=7, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Patient.class,
		Practitioner.class,
		Device.class,
	})	
	private List<ResourceReference> myMember;
	
	/**
	 * Gets the value(s) for identifier (Unique id)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique id)
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
	 * Gets the value(s) for type (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public CodeDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public void setType(CodeDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for actual (Descriptive or actual)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public BooleanDt getActual() {
		return myActual;
	}

	/**
	 * Sets the value(s) for actual (Descriptive or actual)
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
	 * Gets the value(s) for code (Kind of Group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.
     * </p> 
	 */
	public CodeableConceptDt getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Kind of Group members)
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
	 * Gets the value(s) for name (Label for Group)
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Label for Group)
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
	 * Gets the value(s) for quantity (Number of members)
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public IntegerDt getQuantity() {
		return myQuantity;
	}

	/**
	 * Sets the value(s) for quantity (Number of members)
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
	 * Gets the value(s) for characteristic (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public List<IDatatype> getCharacteristic() {
		return myCharacteristic;
	}

	/**
	 * Sets the value(s) for characteristic (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public void setCharacteristic(List<IDatatype> theValue) {
		myCharacteristic = theValue;
	}
	
	/**
	 * Gets the value(s) for member (Who is in group)
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
	 * Sets the value(s) for member (Who is in group)
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
	public static class Characteristic implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	private CodeDt myType;
	
	@Child(name="actual", type=BooleanDt.class, order=2, min=1, max=1)	
	private BooleanDt myActual;
	
	@Child(name="code", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	private CodeableConceptDt myCode;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="quantity", type=IntegerDt.class, order=5, min=0, max=1)	
	private IntegerDt myQuantity;
	
	@Child(name="characteristic", order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myCharacteristic;
	
	@Child(name="member", order=7, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Patient.class,
		Practitioner.class,
		Device.class,
	})	
	private List<ResourceReference> myMember;
	
	/**
	 * Gets the value(s) for identifier (Unique id)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
     * </p> 
	 */
	public IdentifierDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Unique id)
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
	 * Gets the value(s) for type (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public CodeDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public void setType(CodeDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for actual (Descriptive or actual)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public BooleanDt getActual() {
		return myActual;
	}

	/**
	 * Sets the value(s) for actual (Descriptive or actual)
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
	 * Gets the value(s) for code (Kind of Group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a specific type of resource the group includes.  E.g. "cow", "syringe", etc.
     * </p> 
	 */
	public CodeableConceptDt getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Kind of Group members)
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
	 * Gets the value(s) for name (Label for Group)
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Label for Group)
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
	 * Gets the value(s) for quantity (Number of members)
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public IntegerDt getQuantity() {
		return myQuantity;
	}

	/**
	 * Sets the value(s) for quantity (Number of members)
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
	 * Gets the value(s) for characteristic (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public List<IDatatype> getCharacteristic() {
		return myCharacteristic;
	}

	/**
	 * Sets the value(s) for characteristic (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public void setCharacteristic(List<IDatatype> theValue) {
		myCharacteristic = theValue;
	}
	
	/**
	 * Gets the value(s) for member (Who is in group)
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
	 * Sets the value(s) for member (Who is in group)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public void setMember(List<ResourceReference> theValue) {
		myMember = theValue;
	}
	
	}



}