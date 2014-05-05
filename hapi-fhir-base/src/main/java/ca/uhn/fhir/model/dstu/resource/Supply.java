















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
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyItemTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.SupplyTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;


/**
 * HAPI/FHIR <b>Supply</b> Resource
 * (A supply -  request and provision)
 *
 * <p>
 * <b>Definition:</b>
 * A supply - a  request for something, and provision of what is supplied
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Supply">http://hl7.org/fhir/profiles/Supply</a> 
 * </p>
 *
 */
@ResourceDef(name="Supply", profile="http://hl7.org/fhir/profiles/Supply", id="supply")
public class Supply extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.kind</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="kind", path="Supply.kind", description="")
	public static final String SP_KIND = "kind";

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Supply.identifier", description="")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Supply.status", description="")
	public static final String SP_STATUS = "status";

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="Supply.patient", description="")
	public static final String SP_PATIENT = "patient";

	/**
	 * Search parameter constant for <b>supplier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Supply.dispense.supplier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supplier", path="Supply.dispense.supplier", description="")
	public static final String SP_SUPPLIER = "supplier";

	/**
	 * Search parameter constant for <b>dispenseid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispenseid", path="Supply.dispense.identifier", description="")
	public static final String SP_DISPENSEID = "dispenseid";

	/**
	 * Search parameter constant for <b>dispensestatus</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Supply.dispense.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispensestatus", path="Supply.dispense.status", description="")
	public static final String SP_DISPENSESTATUS = "dispensestatus";


	@Child(name="kind", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The kind of supply (central, non-stock, etc)",
		formalDefinition="Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process"
	)
	private BoundCodeableConceptDt<SupplyTypeEnum> myKind;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Unique identifier",
		formalDefinition="Unique identifier for this supply request"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="requested | dispensed | received | failed | cancelled",
		formalDefinition="Status of the supply request"
	)
	private BoundCodeDt<SupplyStatusEnum> myStatus;
	
	@Child(name="orderedItem", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Medication, Substance, or Device requested to be supplied",
		formalDefinition="The item that is requested to be supplied"
	)
	private ResourceReferenceDt myOrderedItem;
	
	@Child(name="patient", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient for whom the item is supplied",
		formalDefinition="A link to a resource representing the person whom the ordered item is for"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="dispense", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Supply details",
		formalDefinition="Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed."
	)
	private java.util.List<Dispense> myDispense;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myKind,  myIdentifier,  myStatus,  myOrderedItem,  myPatient,  myDispense);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myKind, myIdentifier, myStatus, myOrderedItem, myPatient, myDispense);
	}

	/**
	 * Gets the value(s) for <b>kind</b> (The kind of supply (central, non-stock, etc)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public BoundCodeableConceptDt<SupplyTypeEnum> getKind() {  
		if (myKind == null) {
			myKind = new BoundCodeableConceptDt<SupplyTypeEnum>(SupplyTypeEnum.VALUESET_BINDER);
		}
		return myKind;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (The kind of supply (central, non-stock, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public Supply setKind(BoundCodeableConceptDt<SupplyTypeEnum> theValue) {
		myKind = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (The kind of supply (central, non-stock, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process
     * </p> 
	 */
	public Supply setKind(SupplyTypeEnum theValue) {
		getKind().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (Unique identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Unique identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for this supply request
     * </p> 
	 */
	public Supply setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (requested | dispensed | received | failed | cancelled).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public BoundCodeDt<SupplyStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SupplyStatusEnum>(SupplyStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | dispensed | received | failed | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public Supply setStatus(BoundCodeDt<SupplyStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | dispensed | received | failed | cancelled)
	 *
     * <p>
     * <b>Definition:</b>
     * Status of the supply request
     * </p> 
	 */
	public Supply setStatus(SupplyStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>orderedItem</b> (Medication, Substance, or Device requested to be supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The item that is requested to be supplied
     * </p> 
	 */
	public ResourceReferenceDt getOrderedItem() {  
		return myOrderedItem;
	}

	/**
	 * Sets the value(s) for <b>orderedItem</b> (Medication, Substance, or Device requested to be supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * The item that is requested to be supplied
     * </p> 
	 */
	public Supply setOrderedItem(ResourceReferenceDt theValue) {
		myOrderedItem = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (Patient for whom the item is supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person whom the ordered item is for
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Patient for whom the item is supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person whom the ordered item is for
     * </p> 
	 */
	public Supply setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dispense</b> (Supply details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public java.util.List<Dispense> getDispense() {  
		if (myDispense == null) {
			myDispense = new java.util.ArrayList<Dispense>();
		}
		return myDispense;
	}

	/**
	 * Sets the value(s) for <b>dispense</b> (Supply details)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Supply setDispense(java.util.List<Dispense> theValue) {
		myDispense = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dispense</b> (Supply details)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Dispense addDispense() {
		Dispense newType = new Dispense();
		getDispense().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dispense</b> (Supply details),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	public Dispense getDispenseFirstRep() {
		if (getDispense().isEmpty()) {
			return addDispense();
		}
		return getDispense().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Supply.dispense</b> (Supply details)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     * </p> 
	 */
	@Block()	
	public static class Dispense extends BaseElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="External identifier",
		formalDefinition="Identifier assigned by the dispensing facility when the dispense occurs"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="in progress | dispensed | abandoned",
		formalDefinition="A code specifying the state of the dispense event."
	)
	private BoundCodeDt<SupplyDispenseStatusEnum> myStatus;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Category of dispense event",
		formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc."
	)
	private BoundCodeableConceptDt<SupplyItemTypeEnum> myType;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Amount dispensed",
		formalDefinition="The amount of supply that has been dispensed. Includes unit of measure."
	)
	private QuantityDt myQuantity;
	
	@Child(name="suppliedItem", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Medication, Substance, or Device supplied",
		formalDefinition="Identifies the medication or substance being dispensed. This is either a link to a resource representing the details of the medication or substance or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt mySuppliedItem;
	
	@Child(name="supplier", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Dispenser",
		formalDefinition="The individual responsible for dispensing the medication"
	)
	private ResourceReferenceDt mySupplier;
	
	@Child(name="whenPrepared", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Dispensing time",
		formalDefinition="The time the dispense event occurred."
	)
	private PeriodDt myWhenPrepared;
	
	@Child(name="whenHandedOver", type=PeriodDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Handover time",
		formalDefinition="The time the dispensed item was sent or handed to the patient (or agent)."
	)
	private PeriodDt myWhenHandedOver;
	
	@Child(name="destination", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where the Supply was sent",
		formalDefinition="Identification of the facility/location where the Supply was shipped to, as part of the dispense event."
	)
	private ResourceReferenceDt myDestination;
	
	@Child(name="receiver", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who collected the Supply",
		formalDefinition="Identifies the person who picked up the Supply."
	)
	private java.util.List<ResourceReferenceDt> myReceiver;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myType,  myQuantity,  mySuppliedItem,  mySupplier,  myWhenPrepared,  myWhenHandedOver,  myDestination,  myReceiver);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myType, myQuantity, mySuppliedItem, mySupplier, myWhenPrepared, myWhenHandedOver, myDestination, myReceiver);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public Dispense setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public Dispense setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility when the dispense occurs
     * </p> 
	 */
	public Dispense setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | dispensed | abandoned).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public BoundCodeDt<SupplyDispenseStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<SupplyDispenseStatusEnum>(SupplyDispenseStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | dispensed | abandoned)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(BoundCodeDt<SupplyDispenseStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | dispensed | abandoned)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(SupplyDispenseStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Category of dispense event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public BoundCodeableConceptDt<SupplyItemTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<SupplyItemTypeEnum>(SupplyItemTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Category of dispense event)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(BoundCodeableConceptDt<SupplyItemTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Category of dispense event)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(SupplyItemTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount dispensed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of supply that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>suppliedItem</b> (Medication, Substance, or Device supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication or substance being dispensed. This is either a link to a resource representing the details of the medication or substance or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getSuppliedItem() {  
		return mySuppliedItem;
	}

	/**
	 * Sets the value(s) for <b>suppliedItem</b> (Medication, Substance, or Device supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication or substance being dispensed. This is either a link to a resource representing the details of the medication or substance or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public Dispense setSuppliedItem(ResourceReferenceDt theValue) {
		mySuppliedItem = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>supplier</b> (Dispenser).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication
     * </p> 
	 */
	public ResourceReferenceDt getSupplier() {  
		if (mySupplier == null) {
			mySupplier = new ResourceReferenceDt();
		}
		return mySupplier;
	}

	/**
	 * Sets the value(s) for <b>supplier</b> (Dispenser)
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication
     * </p> 
	 */
	public Dispense setSupplier(ResourceReferenceDt theValue) {
		mySupplier = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>whenPrepared</b> (Dispensing time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispense event occurred.
     * </p> 
	 */
	public PeriodDt getWhenPrepared() {  
		if (myWhenPrepared == null) {
			myWhenPrepared = new PeriodDt();
		}
		return myWhenPrepared;
	}

	/**
	 * Sets the value(s) for <b>whenPrepared</b> (Dispensing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispense event occurred.
     * </p> 
	 */
	public Dispense setWhenPrepared(PeriodDt theValue) {
		myWhenPrepared = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>whenHandedOver</b> (Handover time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed item was sent or handed to the patient (or agent).
     * </p> 
	 */
	public PeriodDt getWhenHandedOver() {  
		if (myWhenHandedOver == null) {
			myWhenHandedOver = new PeriodDt();
		}
		return myWhenHandedOver;
	}

	/**
	 * Sets the value(s) for <b>whenHandedOver</b> (Handover time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed item was sent or handed to the patient (or agent).
     * </p> 
	 */
	public Dispense setWhenHandedOver(PeriodDt theValue) {
		myWhenHandedOver = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>destination</b> (Where the Supply was sent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * </p> 
	 */
	public ResourceReferenceDt getDestination() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> (Where the Supply was sent)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * </p> 
	 */
	public Dispense setDestination(ResourceReferenceDt theValue) {
		myDestination = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>receiver</b> (Who collected the Supply).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReceiver() {  
		if (myReceiver == null) {
			myReceiver = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReceiver;
	}

	/**
	 * Sets the value(s) for <b>receiver</b> (Who collected the Supply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public Dispense setReceiver(java.util.List<ResourceReferenceDt> theValue) {
		myReceiver = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>receiver</b> (Who collected the Supply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the Supply.
     * </p> 
	 */
	public ResourceReferenceDt addReceiver() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReceiver().add(newType);
		return newType; 
	}
  

	}




}
