















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
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;


/**
 * HAPI/FHIR <b>SequencingAnalysis</b> Resource
 * (Sequencing Analysis)
 *
 * <p>
 * <b>Definition:</b>
 * Computational analysis on a patient's genetic raw file
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SequencingAnalysis">http://hl7.org/fhir/profiles/SequencingAnalysis</a> 
 * </p>
 *
 */
@ResourceDef(name="SequencingAnalysis", profile="http://hl7.org/fhir/profiles/SequencingAnalysis", id="sequencinganalysis")
public class SequencingAnalysis extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SequencingAnalysis.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="SequencingAnalysis.subject", description="Subject of the analysis")
	public static final String SP_SUBJECT = "subject";

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is updated</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SequencingAnalysis.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SequencingAnalysis.date", description="Date when result of the analysis is updated")
	public static final String SP_DATE = "date";

	/**
	 * Search parameter constant for <b>genome</b>
	 * <p>
	 * Description: <b>Name of the reference genome used in the analysis</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SequencingAnalysis.genome.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="genome", path="SequencingAnalysis.genome.name", description="Name of the reference genome used in the analysis")
	public static final String SP_GENOME = "genome";


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject",
		formalDefinition="Subject of the analysis"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="date", type=DateDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Date",
		formalDefinition="Date when result of the analysis is updated"
	)
	private DateDt myDate;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Name",
		formalDefinition="Name of the analysis"
	)
	private StringDt myName;
	
	@Child(name="genome", order=3, min=1, max=1)	
	@Description(
		shortDefinition="Reference genome",
		formalDefinition="Reference genome used in the analysis"
	)
	private Genome myGenome;
	
	@Child(name="file", type=AttachmentDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="File",
		formalDefinition="Files uploaded as result of the analysis"
	)
	private java.util.List<AttachmentDt> myFile;
	
	@Child(name="inputLab", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.SequencingLab.class	})
	@Description(
		shortDefinition="Input lab",
		formalDefinition="SequencingLab taken into account of the analysis"
	)
	private java.util.List<ResourceReferenceDt> myInputLab;
	
	@Child(name="inputAnalysis", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.SequencingAnalysis.class	})
	@Description(
		shortDefinition="Input analysis",
		formalDefinition="SequencingAnalysis taken into account of the analysis"
	)
	private java.util.List<ResourceReferenceDt> myInputAnalysis;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myDate,  myName,  myGenome,  myFile,  myInputLab,  myInputAnalysis);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myDate, myName, myGenome, myFile, myInputLab, myInputAnalysis);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
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
     * Subject of the analysis
     * </p> 
	 */
	public SequencingAnalysis setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
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
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public SequencingAnalysis setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
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
     * Name of the analysis
     * </p> 
	 */
	public SequencingAnalysis setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the analysis
     * </p> 
	 */
	public SequencingAnalysis setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genome</b> (Reference genome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	public Genome getGenome() {  
		if (myGenome == null) {
			myGenome = new Genome();
		}
		return myGenome;
	}

	/**
	 * Sets the value(s) for <b>genome</b> (Reference genome)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	public SequencingAnalysis setGenome(Genome theValue) {
		myGenome = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>file</b> (File).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
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
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public SequencingAnalysis setFile(java.util.List<AttachmentDt> theValue) {
		myFile = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>file</b> (File)
	 *
     * <p>
     * <b>Definition:</b>
     * Files uploaded as result of the analysis
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
     * Files uploaded as result of the analysis
     * </p> 
	 */
	public AttachmentDt getFileFirstRep() {
		if (getFile().isEmpty()) {
			return addFile();
		}
		return getFile().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>inputLab </b> (Input lab).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getInputLab() {  
		if (myInputLab == null) {
			myInputLab = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myInputLab;
	}

	/**
	 * Sets the value(s) for <b>inputLab </b> (Input lab)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public SequencingAnalysis setInputLab(java.util.List<ResourceReferenceDt> theValue) {
		myInputLab = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>inputLab </b> (Input lab)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingLab taken into account of the analysis
     * </p> 
	 */
	public ResourceReferenceDt addInputLab() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getInputLab().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>inputAnalysis </b> (Input analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getInputAnalysis() {  
		if (myInputAnalysis == null) {
			myInputAnalysis = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myInputAnalysis;
	}

	/**
	 * Sets the value(s) for <b>inputAnalysis </b> (Input analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public SequencingAnalysis setInputAnalysis(java.util.List<ResourceReferenceDt> theValue) {
		myInputAnalysis = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>inputAnalysis </b> (Input analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * SequencingAnalysis taken into account of the analysis
     * </p> 
	 */
	public ResourceReferenceDt addInputAnalysis() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getInputAnalysis().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>SequencingAnalysis.genome</b> (Reference genome)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference genome used in the analysis
     * </p> 
	 */
	@Block()	
	public static class Genome extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name",
		formalDefinition="Name of the reference genome"
	)
	private CodeDt myName;
	
	@Child(name="build", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Build",
		formalDefinition="Build number of the refernece genome"
	)
	private StringDt myBuild;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myBuild);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myBuild);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public CodeDt getName() {  
		if (myName == null) {
			myName = new CodeDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public Genome setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the reference genome
     * </p> 
	 */
	public Genome setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>build</b> (Build).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public StringDt getBuild() {  
		if (myBuild == null) {
			myBuild = new StringDt();
		}
		return myBuild;
	}

	/**
	 * Sets the value(s) for <b>build</b> (Build)
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public Genome setBuild(StringDt theValue) {
		myBuild = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>build</b> (Build)
	 *
     * <p>
     * <b>Definition:</b>
     * Build number of the refernece genome
     * </p> 
	 */
	public Genome setBuild( String theString) {
		myBuild = new StringDt(theString); 
		return this; 
	}

 

	}




}
