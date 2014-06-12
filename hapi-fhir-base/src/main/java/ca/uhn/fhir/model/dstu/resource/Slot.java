















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


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Slot</b> Resource
 * ((informative) A slot of time that may be available for booking appointments)
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
 * <a href="http://hl7.org/fhir/profiles/Slot">http://hl7.org/fhir/profiles/Slot</a> 
 * </p>
 *
 */
@ResourceDef(name="Slot", profile="http://hl7.org/fhir/profiles/Slot", id="slot")
public class Slot extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>slottype</b>
	 * <p>
	 * Description: <b>The type of appointments that can be booked into the slot</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="slottype", path="Slot.type", description="The type of appointments that can be booked into the slot", type="token")
	public static final String SP_SLOTTYPE = "slottype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>slottype</b>
	 * <p>
	 * Description: <b>The type of appointments that can be booked into the slot</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.type</b><br/>
	 * </p>
	 */
	public static final TokenParam SLOTTYPE = new TokenParam(SP_SLOTTYPE);

	/**
	 * Search parameter constant for <b>availability</b>
	 * <p>
	 * Description: <b>The Availability Resource that we are seeking a slot within</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Slot.availability</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="availability", path="Slot.availability", description="The Availability Resource that we are seeking a slot within", type="reference")
	public static final String SP_AVAILABILITY = "availability";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>availability</b>
	 * <p>
	 * Description: <b>The Availability Resource that we are seeking a slot within</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Slot.availability</b><br/>
	 * </p>
	 */
	public static final ReferenceParam AVAILABILITY = new ReferenceParam(SP_AVAILABILITY);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Slot.availability</b>".
	 */
	public static final Include INCLUDE_AVAILABILITY = new Include("Slot.availability");

	/**
	 * Search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b>Appointment date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Slot.start</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="start", path="Slot.start", description="Appointment date/time.", type="date")
	public static final String SP_START = "start";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b>Appointment date/time.</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Slot.start</b><br/>
	 * </p>
	 */
	public static final DateParam START = new DateParam(SP_START);

	/**
	 * Search parameter constant for <b>fbtype</b>
	 * <p>
	 * Description: <b>The free/busy status of the appointment</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.freeBusyType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="fbtype", path="Slot.freeBusyType", description="The free/busy status of the appointment", type="token")
	public static final String SP_FBTYPE = "fbtype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>fbtype</b>
	 * <p>
	 * Description: <b>The free/busy status of the appointment</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Slot.freeBusyType</b><br/>
	 * </p>
	 */
	public static final TokenParam FBTYPE = new TokenParam(SP_FBTYPE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this item",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource",
		formalDefinition=""
	)
	private CodeableConceptDt myType;
	
	@Child(name="availability", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Availability.class	})
	@Description(
		shortDefinition="The availability resource that this slot defines an interval of status information",
		formalDefinition=""
	)
	private ResourceReferenceDt myAvailability;
	
	@Child(name="freeBusyType", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE",
		formalDefinition=""
	)
	private CodeDt myFreeBusyType;
	
	@Child(name="start", type=InstantDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Date/Time that the slot is to begin",
		formalDefinition=""
	)
	private InstantDt myStart;
	
	@Child(name="end", type=InstantDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Date/Time that the slot is to conclude",
		formalDefinition=""
	)
	private InstantDt myEnd;
	
	@Child(name="comment", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Comments on the slot to describe any extended information. Such as custom constraints on the slot",
		formalDefinition=""
	)
	private StringDt myComment;
	
	@Child(name="author", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who authored the slot",
		formalDefinition=""
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="authorDate", type=DateTimeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="When this slot was created, or last revised",
		formalDefinition=""
	)
	private DateTimeDt myAuthorDate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  myAvailability,  myFreeBusyType,  myStart,  myEnd,  myComment,  myAuthor,  myAuthorDate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, myAvailability, myFreeBusyType, myStart, myEnd, myComment, myAuthor, myAuthorDate);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this item),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Slot addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this item)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Slot addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>availability</b> (The availability resource that this slot defines an interval of status information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getAvailability() {  
		if (myAvailability == null) {
			myAvailability = new ResourceReferenceDt();
		}
		return myAvailability;
	}

	/**
	 * Sets the value(s) for <b>availability</b> (The availability resource that this slot defines an interval of status information)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setAvailability(ResourceReferenceDt theValue) {
		myAvailability = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>freeBusyType</b> (BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getFreeBusyType() {  
		if (myFreeBusyType == null) {
			myFreeBusyType = new CodeDt();
		}
		return myFreeBusyType;
	}

	/**
	 * Sets the value(s) for <b>freeBusyType</b> (BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setFreeBusyType(CodeDt theValue) {
		myFreeBusyType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>freeBusyType</b> (BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setFreeBusyType( String theCode) {
		myFreeBusyType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Date/Time that the slot is to begin).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getStart() {  
		if (myStart == null) {
			myStart = new InstantDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Date/Time that the slot is to begin)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setStart(InstantDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Date/Time that the slot is to begin)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setStartWithMillisPrecision( Date theDate) {
		myStart = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>start</b> (Date/Time that the slot is to begin)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setStart( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStart = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (Date/Time that the slot is to conclude).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getEnd() {  
		if (myEnd == null) {
			myEnd = new InstantDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (Date/Time that the slot is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setEnd(InstantDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (Date/Time that the slot is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setEndWithMillisPrecision( Date theDate) {
		myEnd = new InstantDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> (Date/Time that the slot is to conclude)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new InstantDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comment</b> (Comments on the slot to describe any extended information. Such as custom constraints on the slot).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getComment() {  
		if (myComment == null) {
			myComment = new StringDt();
		}
		return myComment;
	}

	/**
	 * Sets the value(s) for <b>comment</b> (Comments on the slot to describe any extended information. Such as custom constraints on the slot)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>comment</b> (Comments on the slot to describe any extended information. Such as custom constraints on the slot)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>author</b> (Who authored the slot).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Who authored the slot)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>authorDate</b> (When this slot was created, or last revised).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getAuthorDate() {  
		if (myAuthorDate == null) {
			myAuthorDate = new DateTimeDt();
		}
		return myAuthorDate;
	}

	/**
	 * Sets the value(s) for <b>authorDate</b> (When this slot was created, or last revised)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setAuthorDate(DateTimeDt theValue) {
		myAuthorDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>authorDate</b> (When this slot was created, or last revised)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setAuthorDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myAuthorDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>authorDate</b> (When this slot was created, or last revised)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Slot setAuthorDateWithSecondsPrecision( Date theDate) {
		myAuthorDate = new DateTimeDt(theDate); 
		return this; 
	}

 


}
