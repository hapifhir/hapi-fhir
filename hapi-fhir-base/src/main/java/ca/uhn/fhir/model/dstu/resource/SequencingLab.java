















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
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;


/**
 * HAPI/FHIR <b>SequencingLab</b> Resource
 * (Sequencing Lab)
 *
 * <p>
 * <b>Definition:</b>
 * A lab for sequencing
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SequencingLab">http://hl7.org/fhir/profiles/SequencingLab</a> 
 * </p>
 *
 */
@ResourceDef(name="SequencingLab", profile="http://hl7.org/fhir/profiles/SequencingLab", id="sequencinglab")
public class SequencingLab extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the lab</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingLab.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="SequencingLab.subject", description="Subject of the lab")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the lab</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingLab.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SequencingLab.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("SequencingLab.subject");

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>Type of the specimen used for the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.specimen.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="SequencingLab.specimen.type", description="Type of the specimen used for the lab")
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b>Type of the specimen used for the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.specimen.type</b><br/>
	 * </p>
	 */
	public static final StringParam SPECIMEN = new StringParam(SP_SPECIMEN);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the lab is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingLab.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SequencingLab.date", description="Date when result of the lab is uploaded")
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the lab is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingLab.date</b><br/>
	 * </p>
	 */
	public static final DateParam DATE = new DateParam(SP_DATE);

	/**
	 * Search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>Organization that does the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.organization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="organization", path="SequencingLab.organization", description="Organization that does the lab")
	public static final String SP_ORGANIZATION = "organization";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>organization</b>
	 * <p>
	 * Description: <b>Organization that does the lab</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.organization</b><br/>
	 * </p>
	 */
	public static final StringParam ORGANIZATION = new StringParam(SP_ORGANIZATION);

	/**
	 * Search parameter constant for <b>system-class</b>
	 * <p>
	 * Description: <b>Class of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.class</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="system-class", path="SequencingLab.system.class", description="Class of the sequencing system")
	public static final String SP_SYSTEM_CLASS = "system-class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system-class</b>
	 * <p>
	 * Description: <b>Class of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.class</b><br/>
	 * </p>
	 */
	public static final StringParam SYSTEM_CLASS = new StringParam(SP_SYSTEM_CLASS);

	/**
	 * Search parameter constant for <b>system-name</b>
	 * <p>
	 * Description: <b>Name of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="system-name", path="SequencingLab.system.name", description="Name of the sequencing system")
	public static final String SP_SYSTEM_NAME = "system-name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system-name</b>
	 * <p>
	 * Description: <b>Name of the sequencing system</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingLab.system.name</b><br/>
	 * </p>
	 */
	public static final StringParam SYSTEM_NAME = new StringParam(SP_SYSTEM_NAME);


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject",
		formalDefinition="Subject of the sequencing lab"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="organization", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Organization",
		formalDefinition="Organization that does the sequencing"
	)
	private StringDt myOrganization;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Name",
		formalDefinition="Name of the lab"
	)
	private StringDt myName;
	
	@Child(name="date", type=DateDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Date",
		formalDefinition="Date when the result of the lab is uploaded"
	)
	private DateDt myDate;
	
	@Child(name="type", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Type",
		formalDefinition="Type of the sequencing lab"
	)
	private CodeDt myType;
	
	@Child(name="system", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Sequencing System",
		formalDefinition="System of machine used for sequencing"
	)
	private System mySystem;
	
	@Child(name="specimen", order=6, min=1, max=1)	
	@Description(
		shortDefinition="Specimen of the lab",
		formalDefinition="Specimen of the lab"
	)
	private Specimen mySpecimen;
	
	@Child(name="file", type=AttachmentDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="File",
		formalDefinition="Files uploaded as results of the lab"
	)
	private java.util.List<AttachmentDt> myFile;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myOrganization,  myName,  myDate,  myType,  mySystem,  mySpecimen,  myFile);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myOrganization, myName, myDate, myType, mySystem, mySpecimen, myFile);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the sequencing lab
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the sequencing lab
     * </p> 
	 */
	public SequencingLab setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>organization</b> (Organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public StringDt getOrganization() {  
		if (myOrganization == null) {
			myOrganization = new StringDt();
		}
		return myOrganization;
	}

	/**
	 * Sets the value(s) for <b>organization</b> (Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public SequencingLab setOrganization(StringDt theValue) {
		myOrganization = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>organization</b> (Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that does the sequencing
     * </p> 
	 */
	public SequencingLab setOrganization( String theString) {
		myOrganization = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the lab
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the lab
     * </p> 
	 */
	public SequencingLab setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the lab
     * </p> 
	 */
	public SequencingLab setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public DateDt getDate() {  
		if (myDate == null) {
			myDate = new DateDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public SequencingLab setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public SequencingLab setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the result of the lab is uploaded
     * </p> 
	 */
	public SequencingLab setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public SequencingLab setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>type</b> (Type)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of the sequencing lab
     * </p> 
	 */
	public SequencingLab setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>system</b> (Sequencing System).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * System of machine used for sequencing
     * </p> 
	 */
	public System getSystem() {  
		if (mySystem == null) {
			mySystem = new System();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (Sequencing System)
	 *
     * <p>
     * <b>Definition:</b>
     * System of machine used for sequencing
     * </p> 
	 */
	public SequencingLab setSystem(System theValue) {
		mySystem = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specimen</b> (Specimen of the lab).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen of the lab
     * </p> 
	 */
	public Specimen getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new Specimen();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> (Specimen of the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen of the lab
     * </p> 
	 */
	public SequencingLab setSpecimen(Specimen theValue) {
		mySpecimen = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>file</b> (File).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public java.util.List<AttachmentDt> getFile() {  
		if (myFile == null) {
			myFile = new java.util.ArrayList<AttachmentDt>();
		}
		return myFile;
	}

	/**
	 * Sets the value(s) for <b>file</b> (File)
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public SequencingLab setFile(java.util.List<AttachmentDt> theValue) {
		myFile = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>file</b> (File)
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public AttachmentDt addFile() {
		AttachmentDt newType = new AttachmentDt();
		getFile().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>file</b> (File),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as results of the lab
     * </p> 
	 */
	public AttachmentDt getFileFirstRep() {
		if (getFile().isEmpty()) {
			return addFile();
		}
		return getFile().get(0); 
	}
  
	/**
	 * Block class for child element: <b>SequencingLab.system</b> (Sequencing System)
	 *
     * <p>
     * <b>Definition:</b>
     * System of machine used for sequencing
     * </p> 
	 */
	@Block()	
	public static class System extends BaseElement implements IResourceBlock {
	
	@Child(name="class", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Class of sequencing system",
		formalDefinition="Class of sequencing system"
	)
	private CodeDt myClassElement;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Version of sequencing system",
		formalDefinition="Version of sequencing system"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Name of sequencing system",
		formalDefinition="Name of sequencing system"
	)
	private CodeDt myName;
	
	@Child(name="identity", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Id of sequencing system",
		formalDefinition="Id of sequencing system"
	)
	private StringDt myIdentity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myClassElement,  myVersion,  myName,  myIdentity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myClassElement, myVersion, myName, myIdentity);
	}

	/**
	 * Gets the value(s) for <b>class</b> (Class of sequencing system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public CodeDt getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeDt();
		}
		return myClassElement;
	}

	/**
	 * Sets the value(s) for <b>class</b> (Class of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public System setClassElement(CodeDt theValue) {
		myClassElement = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>class</b> (Class of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of sequencing system
     * </p> 
	 */
	public System setClassElement( String theCode) {
		myClassElement = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Version of sequencing system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public System setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>version</b> (Version of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of sequencing system
     * </p> 
	 */
	public System setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Name of sequencing system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public CodeDt getName() {  
		if (myName == null) {
			myName = new CodeDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public System setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of sequencing system
     * </p> 
	 */
	public System setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identity</b> (Id of sequencing system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Id of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public System setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Id of sequencing system)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of sequencing system
     * </p> 
	 */
	public System setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>SequencingLab.specimen</b> (Specimen of the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * Specimen of the lab
     * </p> 
	 */
	@Block()	
	public static class Specimen extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Class of the specimen",
		formalDefinition="Whether the specimen is from germline or somatic cells of the patient"
	)
	private CodeDt myType;
	
	@Child(name="source", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Source of specimen",
		formalDefinition="Source of the specimen"
	)
	private CodeableConceptDt mySource;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySource);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySource);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Class of the specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Class of the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public Specimen setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>type</b> (Class of the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the specimen is from germline or somatic cells of the patient
     * </p> 
	 */
	public Specimen setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> (Source of specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public CodeableConceptDt getSource() {  
		if (mySource == null) {
			mySource = new CodeableConceptDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Source of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the specimen
     * </p> 
	 */
	public Specimen setSource(CodeableConceptDt theValue) {
		mySource = theValue;
		return this;
	}

  

	}




}
