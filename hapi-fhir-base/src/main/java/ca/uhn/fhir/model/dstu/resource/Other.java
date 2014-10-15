















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
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Other</b> Resource
 * (Resource for non-supported content)
 *
 * <p>
 * <b>Definition:</b>
 * Other is a conformant for handling resource concepts not yet defined for FHIR or outside HL7's scope of interest
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need some way to safely (without breaking interoperability) allow implementers to exchange content not supported by the initial set of declared resources.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Other">http://hl7.org/fhir/profiles/Other</a> 
 * </p>
 *
 */
@ResourceDef(name="Other", profile="http://hl7.org/fhir/profiles/Other", id="other")
public class Other extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Other.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Other.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Other.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Other.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Other.subject");

	/**
	 * Search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Other.created</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="created", path="Other.created", description="", type="date"  )
	public static final String SP_CREATED = "created";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Other.created</b><br/>
	 * </p>
	 */
	public static final DateClientParam CREATED = new DateClientParam(SP_CREATED);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Other.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Other.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Other.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Business identifier",
		formalDefinition="Identifier assigned to the resource for business purposes, outside the context of FHIR"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Kind of Resource",
		formalDefinition="Identifies the 'type' of resource - equivalent to the resource name for other resources."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="Identifies the",
		formalDefinition="Identifies the patient, practitioner, device or any other resource that is the \"focus\" of this resoruce."
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who created",
		formalDefinition="Indicates who was responsible for creating the resource instance"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="created", type=DateDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="When created",
		formalDefinition="Identifies when the resource was first created"
	)
	private DateDt myCreated;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  mySubject,  myAuthor,  myCreated);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, mySubject, myAuthor, myCreated);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned to the resource for business purposes, outside the context of FHIR
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
     * Identifier assigned to the resource for business purposes, outside the context of FHIR
     * </p> 
	 */
	public Other setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned to the resource for business purposes, outside the context of FHIR
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
     * Identifier assigned to the resource for business purposes, outside the context of FHIR
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
     * Identifier assigned to the resource for business purposes, outside the context of FHIR
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Other addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
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
     * Identifier assigned to the resource for business purposes, outside the context of FHIR
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Other addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Kind of Resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the 'type' of resource - equivalent to the resource name for other resources.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Kind of Resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the 'type' of resource - equivalent to the resource name for other resources.
     * </p> 
	 */
	public Other setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (Identifies the).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient, practitioner, device or any other resource that is the \"focus\" of this resoruce.
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Identifies the)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the patient, practitioner, device or any other resource that is the \"focus\" of this resoruce.
     * </p> 
	 */
	public Other setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Who created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates who was responsible for creating the resource instance
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Who created)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates who was responsible for creating the resource instance
     * </p> 
	 */
	public Other setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>created</b> (When created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies when the resource was first created
     * </p> 
	 */
	public DateDt getCreated() {  
		if (myCreated == null) {
			myCreated = new DateDt();
		}
		return myCreated;
	}

	/**
	 * Sets the value(s) for <b>created</b> (When created)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies when the resource was first created
     * </p> 
	 */
	public Other setCreated(DateDt theValue) {
		myCreated = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>created</b> (When created)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies when the resource was first created
     * </p> 
	 */
	public Other setCreatedWithDayPrecision( Date theDate) {
		myCreated = new DateDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>created</b> (When created)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies when the resource was first created
     * </p> 
	 */
	public Other setCreated( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myCreated = new DateDt(theDate, thePrecision); 
		return this; 
	}

	@Override
	public ResourceTypeEnum getResourceType() {
		return ResourceTypeEnum.OTHER;
	} 


}
