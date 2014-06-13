















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.CompositeParam;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


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
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Group">http://hl7.org/fhir/profiles/Group</a> 
 * </p>
 *
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
	@SearchParamDefinition(name="type", path="Group.type", description="The type of resources the group contains", type="token")
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of resources the group contains</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.type</b><br/>
	 * </p>
	 */
	public static final TokenParam TYPE = new TokenParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The kind of resources contained</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Group.code", description="The kind of resources contained", type="token")
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>The kind of resources contained</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.code</b><br/>
	 * </p>
	 */
	public static final TokenParam CODE = new TokenParam(SP_CODE);

	/**
	 * Search parameter constant for <b>actual</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.actual</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="actual", path="Group.actual", description="", type="token")
	public static final String SP_ACTUAL = "actual";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>actual</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.actual</b><br/>
	 * </p>
	 */
	public static final TokenParam ACTUAL = new TokenParam(SP_ACTUAL);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Group.identifier", description="", type="token")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.identifier</b><br/>
	 * </p>
	 */
	public static final TokenParam IDENTIFIER = new TokenParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>member</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Group.member</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="member", path="Group.member", description="", type="reference")
	public static final String SP_MEMBER = "member";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>member</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Group.member</b><br/>
	 * </p>
	 */
	public static final ReferenceParam MEMBER = new ReferenceParam(SP_MEMBER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Group.member</b>".
	 */
	public static final Include INCLUDE_MEMBER = new Include("Group.member");

	/**
	 * Search parameter constant for <b>characteristic</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="characteristic", path="Group.characteristic.code", description="", type="token")
	public static final String SP_CHARACTERISTIC = "characteristic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>characteristic</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.code</b><br/>
	 * </p>
	 */
	public static final TokenParam CHARACTERISTIC = new TokenParam(SP_CHARACTERISTIC);

	/**
	 * Search parameter constant for <b>value</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.value[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="value", path="Group.characteristic.value[x]", description="", type="token")
	public static final String SP_VALUE = "value";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>value</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.value[x]</b><br/>
	 * </p>
	 */
	public static final TokenParam VALUE = new TokenParam(SP_VALUE);

	/**
	 * Search parameter constant for <b>exclude</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.exclude</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="exclude", path="Group.characteristic.exclude", description="", type="token")
	public static final String SP_EXCLUDE = "exclude";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>exclude</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Group.characteristic.exclude</b><br/>
	 * </p>
	 */
	public static final TokenParam EXCLUDE = new TokenParam(SP_EXCLUDE);

	/**
	 * Search parameter constant for <b>characteristic-value</b>
	 * <p>
	 * Description: <b>A composite of both characteristic and value</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>characteristic & value</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="characteristic-value", path="characteristic & value", description="A composite of both characteristic and value", type="composite")
	public static final String SP_CHARACTERISTIC_VALUE = "characteristic-value";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>characteristic-value</b>
	 * <p>
	 * Description: <b>A composite of both characteristic and value</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>characteristic & value</b><br/>
	 * </p>
	 */
	public static final CompositeParam CHARACTERISTIC_VALUE = new CompositeParam(SP_CHARACTERISTIC_VALUE);


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
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Medication.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class	})
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
	public Group setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Unique id)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
     * </p> 
	 */
	public Group setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Unique id)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique business identifier for this group
     * </p> 
	 */
	public Group setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
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
	public Group setType(BoundCodeDt<GroupTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (person | animal | practitioner | device | medication | substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the broad classification of the kind of resources the group includes
     * </p> 
	 */
	public Group setType(GroupTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
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
	public Group setActual(BooleanDt theValue) {
		myActual = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>actual</b> (Descriptive or actual)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the resource refers to a specific group of real individuals.  If false, the group defines a set of intended individuals
     * </p> 
	 */
	public Group setActual( boolean theBoolean) {
		myActual = new BooleanDt(theBoolean); 
		return this; 
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
	public Group setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
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
	public Group setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Label for Group)
	 *
     * <p>
     * <b>Definition:</b>
     * A label assigned to the group for human identification and communication
     * </p> 
	 */
	public Group setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
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
	public Group setQuantity(IntegerDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Number of members)
	 *
     * <p>
     * <b>Definition:</b>
     * A count of the number of resource instances that are part of the group
     * </p> 
	 */
	public Group setQuantity( int theInteger) {
		myQuantity = new IntegerDt(theInteger); 
		return this; 
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
	public Group setCharacteristic(java.util.List<Characteristic> theValue) {
		myCharacteristic = theValue;
		return this;
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
	 * Gets the first repetition for <b>characteristic</b> (Trait of group members),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	public Characteristic getCharacteristicFirstRep() {
		if (getCharacteristic().isEmpty()) {
			return addCharacteristic();
		}
		return getCharacteristic().get(0); 
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
	public Group setMember(java.util.List<ResourceReferenceDt> theValue) {
		myMember = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>member</b> (Who is in group)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the resource instances that are members of the group.
     * </p> 
	 */
	public ResourceReferenceDt addMember() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getMember().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>Group.characteristic</b> (Trait of group members)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the traits shared by members of the group
     * </p> 
	 */
	@Block()	
	public static class Characteristic extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Kind of characteristic",
		formalDefinition="A code that identifies the kind of trait being asserted"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="value", order=1, min=1, max=1, type={
		CodeableConceptDt.class, 		BooleanDt.class, 		QuantityDt.class, 		RangeDt.class	})
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
	public Characteristic setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
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
	public Characteristic setValue(IDatatype theValue) {
		myValue = theValue;
		return this;
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
	public Characteristic setExclude(BooleanDt theValue) {
		myExclude = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>exclude</b> (Group includes or excludes)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates the characteristic is one that is NOT held by members of the group
     * </p> 
	 */
	public Characteristic setExclude( boolean theBoolean) {
		myExclude = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}




}
