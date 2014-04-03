















package ca.uhn.fhir.model.dstu.resource;


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ListModeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;


/**
 * HAPI/FHIR <b>ListResource</b> Resource
 * (Information summarized from a list of other resources)
 *
 * <p>
 * <b>Definition:</b>
 * A set of information summarized from a list of other resources
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/List">http://hl7.org/fhir/profiles/List</a> 
 * </p>
 *
 */
@ResourceDef(name="ListResource", profile="http://hl7.org/fhir/profiles/List", id="list")
public class ListResource extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.source</b><br/>
	 * </p>
	 */
	public static final String SP_SOURCE = "source";

	/**
	 * Search parameter constant for <b>item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.entry.item</b><br/>
	 * </p>
	 */
	public static final String SP_ITEM = "item";

	/**
	 * Search parameter constant for <b>empty-reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.emptyReason</b><br/>
	 * </p>
	 */
	public static final String SP_EMPTY_REASON = "empty-reason";

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>List.date</b><br/>
	 * </p>
	 */
	public static final String SP_DATE = "date";

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>List.code</b><br/>
	 * </p>
	 */
	public static final String SP_CODE = "code";

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>List.subject</b><br/>
	 * </p>
	 */
	public static final String SP_SUBJECT = "subject";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Business identifier",
		formalDefinition="Identifier for the List assigned for business purposes outside the context of FHIR."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="What the purpose of this list is",
		formalDefinition="This code defines the purpose of the list - why it was created"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class,
		ca.uhn.fhir.model.dstu.resource.Group.class,
		ca.uhn.fhir.model.dstu.resource.Device.class,
		ca.uhn.fhir.model.dstu.resource.Location.class,
	})
	@Description(
		shortDefinition="If all resources have the same subject",
		formalDefinition="The common subject (or patient) of the resources that are in the list, if there is one"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="source", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class,
		ca.uhn.fhir.model.dstu.resource.Patient.class,
		ca.uhn.fhir.model.dstu.resource.Device.class,
	})
	@Description(
		shortDefinition="Who and/or what defined the list contents",
		formalDefinition="The entity responsible for deciding what the contents of the list were"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="date", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="When the list was prepared",
		formalDefinition="The date that the list was prepared"
	)
	private DateTimeDt myDate;
	
	@Child(name="ordered", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Whether items in the list have a meaningful order",
		formalDefinition="Whether items in the list have a meaningful order"
	)
	private BooleanDt myOrdered;
	
	@Child(name="mode", type=CodeDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="working | snapshot | changes",
		formalDefinition="How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted"
	)
	private BoundCodeDt<ListModeEnum> myMode;
	
	@Child(name="entry", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Entries in the list",
		formalDefinition="Entries in this list"
	)
	private java.util.List<Entry> myEntry;
	
	@Child(name="emptyReason", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Why list is empty",
		formalDefinition="If the list is empty, why the list is empty"
	)
	private CodeableConceptDt myEmptyReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  mySubject,  mySource,  myDate,  myOrdered,  myMode,  myEntry,  myEmptyReason);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, mySubject, mySource, myDate, myOrdered, myMode, myEntry, myEmptyReason);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public ListResource setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Business identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ListResource addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ListResource addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (What the purpose of this list is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (What the purpose of this list is)
	 *
     * <p>
     * <b>Definition:</b>
     * This code defines the purpose of the list - why it was created
     * </p> 
	 */
	public ListResource setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (If all resources have the same subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common subject (or patient) of the resources that are in the list, if there is one
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (If all resources have the same subject)
	 *
     * <p>
     * <b>Definition:</b>
     * The common subject (or patient) of the resources that are in the list, if there is one
     * </p> 
	 */
	public ListResource setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>source</b> (Who and/or what defined the list contents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Who and/or what defined the list contents)
	 *
     * <p>
     * <b>Definition:</b>
     * The entity responsible for deciding what the contents of the list were
     * </p> 
	 */
	public ListResource setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (When the list was prepared).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the list was prepared)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When the list was prepared)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the list was prepared)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the list was prepared
     * </p> 
	 */
	public ListResource setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>ordered</b> (Whether items in the list have a meaningful order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public BooleanDt getOrdered() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}

	/**
	 * Sets the value(s) for <b>ordered</b> (Whether items in the list have a meaningful order)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public ListResource setOrdered(BooleanDt theValue) {
		myOrdered = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>ordered</b> (Whether items in the list have a meaningful order)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether items in the list have a meaningful order
     * </p> 
	 */
	public ListResource setOrdered( boolean theBoolean) {
		myOrdered = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mode</b> (working | snapshot | changes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public BoundCodeDt<ListModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ListModeEnum>(ListModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (working | snapshot | changes)
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public ListResource setMode(BoundCodeDt<ListModeEnum> theValue) {
		myMode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (working | snapshot | changes)
	 *
     * <p>
     * <b>Definition:</b>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted
     * </p> 
	 */
	public ListResource setMode(ListModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>entry</b> (Entries in the list).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public java.util.List<Entry> getEntry() {  
		if (myEntry == null) {
			myEntry = new java.util.ArrayList<Entry>();
		}
		return myEntry;
	}

	/**
	 * Sets the value(s) for <b>entry</b> (Entries in the list)
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public ListResource setEntry(java.util.List<Entry> theValue) {
		myEntry = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>entry</b> (Entries in the list)
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public Entry addEntry() {
		Entry newType = new Entry();
		getEntry().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>entry</b> (Entries in the list),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	public Entry getEntryFirstRep() {
		if (getEntry().isEmpty()) {
			return addEntry();
		}
		return getEntry().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>emptyReason</b> (Why list is empty).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public CodeableConceptDt getEmptyReason() {  
		if (myEmptyReason == null) {
			myEmptyReason = new CodeableConceptDt();
		}
		return myEmptyReason;
	}

	/**
	 * Sets the value(s) for <b>emptyReason</b> (Why list is empty)
	 *
     * <p>
     * <b>Definition:</b>
     * If the list is empty, why the list is empty
     * </p> 
	 */
	public ListResource setEmptyReason(CodeableConceptDt theValue) {
		myEmptyReason = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>List.entry</b> (Entries in the list)
	 *
     * <p>
     * <b>Definition:</b>
     * Entries in this list
     * </p> 
	 */
	@Block(name="List.entry")	
	public static class Entry extends BaseElement implements IResourceBlock {
	
	@Child(name="flag", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Workflow information about this item",
		formalDefinition="The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list"
	)
	private java.util.List<CodeableConceptDt> myFlag;
	
	@Child(name="deleted", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="If this item is actually marked as deleted",
		formalDefinition="True if this item is marked as deleted in the list."
	)
	private BooleanDt myDeleted;
	
	@Child(name="date", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="When item added to list",
		formalDefinition="When this item was added to the list"
	)
	private DateTimeDt myDate;
	
	@Child(name="item", order=3, min=1, max=1, type={
		IResource.class,
	})
	@Description(
		shortDefinition="Actual entry",
		formalDefinition="A reference to the actual resource from which data was derived"
	)
	private ResourceReferenceDt myItem;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myFlag,  myDeleted,  myDate,  myItem);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myFlag, myDeleted, myDate, myItem);
	}

	/**
	 * Gets the value(s) for <b>flag</b> (Workflow information about this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getFlag() {  
		if (myFlag == null) {
			myFlag = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myFlag;
	}

	/**
	 * Sets the value(s) for <b>flag</b> (Workflow information about this item)
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public Entry setFlag(java.util.List<CodeableConceptDt> theValue) {
		myFlag = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>flag</b> (Workflow information about this item)
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public CodeableConceptDt addFlag() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getFlag().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>flag</b> (Workflow information about this item),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The flag allows the system constructing the list to make one or more statements about the role and significance of the item in the list
     * </p> 
	 */
	public CodeableConceptDt getFlagFirstRep() {
		if (getFlag().isEmpty()) {
			return addFlag();
		}
		return getFlag().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>deleted</b> (If this item is actually marked as deleted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public BooleanDt getDeleted() {  
		if (myDeleted == null) {
			myDeleted = new BooleanDt();
		}
		return myDeleted;
	}

	/**
	 * Sets the value(s) for <b>deleted</b> (If this item is actually marked as deleted)
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Entry setDeleted(BooleanDt theValue) {
		myDeleted = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>deleted</b> (If this item is actually marked as deleted)
	 *
     * <p>
     * <b>Definition:</b>
     * True if this item is marked as deleted in the list.
     * </p> 
	 */
	public Entry setDeleted( boolean theBoolean) {
		myDeleted = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (When item added to list).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When item added to list)
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When item added to list)
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When item added to list)
	 *
     * <p>
     * <b>Definition:</b>
     * When this item was added to the list
     * </p> 
	 */
	public Entry setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>item</b> (Actual entry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the actual resource from which data was derived
     * </p> 
	 */
	public ResourceReferenceDt getItem() {  
		if (myItem == null) {
			myItem = new ResourceReferenceDt();
		}
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> (Actual entry)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the actual resource from which data was derived
     * </p> 
	 */
	public Entry setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
	}

  

	}




}