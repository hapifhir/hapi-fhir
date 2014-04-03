















package ca.uhn.fhir.model.dstu.resource;


import java.util.List;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.AlertStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;


/**
 * HAPI/FHIR <b>Alert</b> Resource
 * (Key information to flag to healthcare providers)
 *
 * <p>
 * <b>Definition:</b>
 * Prospective warnings of potential issues when providing care to the patient
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Alert">http://hl7.org/fhir/profiles/Alert</a> 
 * </p>
 *
 */
@ResourceDef(name="Alert", profile="http://hl7.org/fhir/profiles/Alert", id="alert")
public class Alert extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of a subject to list alerts for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Alert.subject</b><br/>
	 * </p>
	 */
	public static final String SP_SUBJECT = "subject";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Business identifier",
		formalDefinition="Identifier assigned to the alert for external use (outside the FHIR environment)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="category", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Clinical, administrative, etc.",
		formalDefinition="Allows an alert to be divided into different categories like clinical, administrative etc."
	)
	private CodeableConceptDt myCategory;
	
	@Child(name="status", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="active | inactive | entered in error",
		formalDefinition="Supports basic workflow"
	)
	private BoundCodeDt<AlertStatusEnum> myStatus;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who is alert about?",
		formalDefinition="The person who this alert concerns"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Alert creator",
		formalDefinition="The person or device that created the alert"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="note", type=StringDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Text of alert",
		formalDefinition="The textual component of the alert to display to the user"
	)
	private StringDt myNote;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCategory,  myStatus,  mySubject,  myAuthor,  myNote);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCategory, myStatus, mySubject, myAuthor, myNote);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned to the alert for external use (outside the FHIR environment)
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
     * Identifier assigned to the alert for external use (outside the FHIR environment)
     * </p> 
	 */
	public Alert setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned to the alert for external use (outside the FHIR environment)
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
     * Identifier assigned to the alert for external use (outside the FHIR environment)
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
     * Identifier assigned to the alert for external use (outside the FHIR environment)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Alert addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
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
     * Identifier assigned to the alert for external use (outside the FHIR environment)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Alert addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>category</b> (Clinical, administrative, etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows an alert to be divided into different categories like clinical, administrative etc.
     * </p> 
	 */
	public CodeableConceptDt getCategory() {  
		if (myCategory == null) {
			myCategory = new CodeableConceptDt();
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (Clinical, administrative, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows an alert to be divided into different categories like clinical, administrative etc.
     * </p> 
	 */
	public Alert setCategory(CodeableConceptDt theValue) {
		myCategory = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (active | inactive | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Supports basic workflow
     * </p> 
	 */
	public BoundCodeDt<AlertStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<AlertStatusEnum>(AlertStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (active | inactive | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * Supports basic workflow
     * </p> 
	 */
	public Alert setStatus(BoundCodeDt<AlertStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (active | inactive | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * Supports basic workflow
     * </p> 
	 */
	public Alert setStatus(AlertStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (Who is alert about?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this alert concerns
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who is alert about?)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this alert concerns
     * </p> 
	 */
	public Alert setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Alert creator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or device that created the alert
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Alert creator)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or device that created the alert
     * </p> 
	 */
	public Alert setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (Text of alert).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The textual component of the alert to display to the user
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	/**
	 * Sets the value(s) for <b>note</b> (Text of alert)
	 *
     * <p>
     * <b>Definition:</b>
     * The textual component of the alert to display to the user
     * </p> 
	 */
	public Alert setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (Text of alert)
	 *
     * <p>
     * <b>Definition:</b>
     * The textual component of the alert to display to the user
     * </p> 
	 */
	public Alert setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 


}