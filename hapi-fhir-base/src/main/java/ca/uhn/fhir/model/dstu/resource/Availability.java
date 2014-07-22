















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
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
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Availability</b> Resource
 * ((informative) A container for slot(s) of time that may be available for booking appointments)
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
 * <a href="http://hl7.org/fhir/profiles/Availability">http://hl7.org/fhir/profiles/Availability</a> 
 * </p>
 *
 */
@ResourceDef(name="Availability", profile="http://hl7.org/fhir/profiles/Availability", id="availability")
public class Availability extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>individual</b>
	 * <p>
	 * Description: <b>The individual to find an availability for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Availability.individual</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="individual", path="Availability.individual", description="The individual to find an availability for", type="reference")
	public static final String SP_INDIVIDUAL = "individual";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>individual</b>
	 * <p>
	 * Description: <b>The individual to find an availability for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Availability.individual</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam INDIVIDUAL = new ReferenceClientParam(SP_INDIVIDUAL);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Availability.individual</b>".
	 */
	public static final Include INCLUDE_INDIVIDUAL = new Include("Availability.individual");

	/**
	 * Search parameter constant for <b>slottype</b>
	 * <p>
	 * Description: <b>The type of appointments that can be booked into associated slot(s)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Availability.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="slottype", path="Availability.type", description="The type of appointments that can be booked into associated slot(s)", type="token")
	public static final String SP_SLOTTYPE = "slottype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>slottype</b>
	 * <p>
	 * Description: <b>The type of appointments that can be booked into associated slot(s)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Availability.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SLOTTYPE = new TokenClientParam(SP_SLOTTYPE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this item",
		formalDefinition=""
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The type of appointments that can be booked into slots attached to this availability resource (ideally this would be an identifiable service - which is at a location, rather than the location itself) - change to CodeableConcept",
		formalDefinition=""
	)
	private CodeableConceptDt myType;
	
	@Child(name="individual", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="The type of resource this availability resource is providing availability information for",
		formalDefinition=""
	)
	private ResourceReferenceDt myIndividual;
	
	@Child(name="period", type=PeriodDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="The period of time that the slots that are attached to this availability resource cover (even if none exist)",
		formalDefinition=""
	)
	private PeriodDt myPeriod;
	
	@Child(name="comment", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated",
		formalDefinition=""
	)
	private StringDt myComment;
	
	@Child(name="author", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who authored the availability",
		formalDefinition=""
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="authorDate", type=DateTimeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="When this availability was created, or last revised",
		formalDefinition=""
	)
	private DateTimeDt myAuthorDate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  myIndividual,  myPeriod,  myComment,  myAuthor,  myAuthorDate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, myIndividual, myPeriod, myComment, myAuthor, myAuthorDate);
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
	public Availability setIdentifier(java.util.List<IdentifierDt> theValue) {
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
	public Availability addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
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
	public Availability addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (The type of appointments that can be booked into slots attached to this availability resource (ideally this would be an identifiable service - which is at a location, rather than the location itself) - change to CodeableConcept).
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
	 * Sets the value(s) for <b>type</b> (The type of appointments that can be booked into slots attached to this availability resource (ideally this would be an identifiable service - which is at a location, rather than the location itself) - change to CodeableConcept)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>individual</b> (The type of resource this availability resource is providing availability information for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new ResourceReferenceDt();
		}
		return myIndividual;
	}

	/**
	 * Sets the value(s) for <b>individual</b> (The type of resource this availability resource is providing availability information for)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setIndividual(ResourceReferenceDt theValue) {
		myIndividual = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (The period of time that the slots that are attached to this availability resource cover (even if none exist)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (The period of time that the slots that are attached to this availability resource cover (even if none exist))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>comment</b> (Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated).
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
	 * Sets the value(s) for <b>comment</b> (Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setComment(StringDt theValue) {
		myComment = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>comment</b> (Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setComment( String theString) {
		myComment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>author</b> (Who authored the availability).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Who authored the availability)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>authorDate</b> (When this availability was created, or last revised).
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
	 * Sets the value(s) for <b>authorDate</b> (When this availability was created, or last revised)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setAuthorDate(DateTimeDt theValue) {
		myAuthorDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>authorDate</b> (When this availability was created, or last revised)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setAuthorDateWithSecondsPrecision( Date theDate) {
		myAuthorDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>authorDate</b> (When this availability was created, or last revised)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Availability setAuthorDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myAuthorDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 


}
