















package ca.uhn.fhir.model.dstu.resource;


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
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;


/**
 * HAPI/FHIR <b>OperationOutcome</b> Resource
 * (Information about the success/failure of an action)
 *
 * <p>
 * <b>Definition:</b>
 * A collection of error, warning or information messages that result from a system action
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/OperationOutcome">http://hl7.org/fhir/profiles/OperationOutcome</a> 
 * </p>
 *
 */
@ResourceDef(name="OperationOutcome", profile="http://hl7.org/fhir/profiles/OperationOutcome", id="operationoutcome")
public class OperationOutcome extends BaseResource implements IResource {


	@Child(name="issue", order=0, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A single issue associated with the action",
		formalDefinition="An error, warning or information message that results from a system action"
	)
	private java.util.List<Issue> myIssue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIssue);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIssue);
	}

	/**
	 * Gets the value(s) for <b>issue</b> (A single issue associated with the action).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public java.util.List<Issue> getIssue() {  
		if (myIssue == null) {
			myIssue = new java.util.ArrayList<Issue>();
		}
		return myIssue;
	}

	/**
	 * Sets the value(s) for <b>issue</b> (A single issue associated with the action)
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public OperationOutcome setIssue(java.util.List<Issue> theValue) {
		myIssue = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>issue</b> (A single issue associated with the action)
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public Issue addIssue() {
		Issue newType = new Issue();
		getIssue().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>issue</b> (A single issue associated with the action),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	public Issue getIssueFirstRep() {
		if (getIssue().isEmpty()) {
			return addIssue();
		}
		return getIssue().get(0); 
	}
  
	/**
	 * Block class for child element: <b>OperationOutcome.issue</b> (A single issue associated with the action)
	 *
     * <p>
     * <b>Definition:</b>
     * An error, warning or information message that results from a system action
     * </p> 
	 */
	@Block(name="OperationOutcome.issue")	
	public static class Issue extends BaseElement implements IResourceBlock {
	
	@Child(name="severity", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="fatal | error | warning | information",
		formalDefinition="Indicates whether the issue indicates a variation from successful processing"
	)
	private BoundCodeDt<IssueSeverityEnum> mySeverity;
	
	@Child(name="type", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Error or warning code",
		formalDefinition="A code indicating the type of error, warning or information message."
	)
	private CodingDt myType;
	
	@Child(name="details", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional description of the issue",
		formalDefinition="Additional description of the issue"
	)
	private StringDt myDetails;
	
	@Child(name="location", type=StringDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="XPath of element(s) related to issue",
		formalDefinition="A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised."
	)
	private java.util.List<StringDt> myLocation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySeverity,  myType,  myDetails,  myLocation);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySeverity, myType, myDetails, myLocation);
	}

	/**
	 * Gets the value(s) for <b>severity</b> (fatal | error | warning | information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the issue indicates a variation from successful processing
     * </p> 
	 */
	public BoundCodeDt<IssueSeverityEnum> getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<IssueSeverityEnum>(IssueSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (fatal | error | warning | information)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the issue indicates a variation from successful processing
     * </p> 
	 */
	public Issue setSeverity(BoundCodeDt<IssueSeverityEnum> theValue) {
		mySeverity = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (fatal | error | warning | information)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the issue indicates a variation from successful processing
     * </p> 
	 */
	public Issue setSeverity(IssueSeverityEnum theValue) {
		getSeverity().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Error or warning code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating the type of error, warning or information message.
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Error or warning code)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating the type of error, warning or information message.
     * </p> 
	 */
	public Issue setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>details</b> (Additional description of the issue).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description of the issue
     * </p> 
	 */
	public StringDt getDetails() {  
		if (myDetails == null) {
			myDetails = new StringDt();
		}
		return myDetails;
	}

	/**
	 * Sets the value(s) for <b>details</b> (Additional description of the issue)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description of the issue
     * </p> 
	 */
	public Issue setDetails(StringDt theValue) {
		myDetails = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>details</b> (Additional description of the issue)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description of the issue
     * </p> 
	 */
	public Issue setDetails( String theString) {
		myDetails = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>location</b> (XPath of element(s) related to issue).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public java.util.List<StringDt> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<StringDt>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (XPath of element(s) related to issue)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public Issue setLocation(java.util.List<StringDt> theValue) {
		myLocation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>location</b> (XPath of element(s) related to issue)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public StringDt addLocation() {
		StringDt newType = new StringDt();
		getLocation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>location</b> (XPath of element(s) related to issue),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
	 */
	public StringDt getLocationFirstRep() {
		if (getLocation().isEmpty()) {
			return addLocation();
		}
		return getLocation().get(0); 
	}
 	/**
	 * Adds a new value for <b>location</b> (XPath of element(s) related to issue)
	 *
     * <p>
     * <b>Definition:</b>
     * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Issue addLocation( String theString) {
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<StringDt>();
		}
		myLocation.add(new StringDt(theString));
		return this; 
	}

 

	}




}