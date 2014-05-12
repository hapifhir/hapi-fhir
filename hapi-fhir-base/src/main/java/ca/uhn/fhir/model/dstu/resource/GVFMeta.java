















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
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;


/**
 * HAPI/FHIR <b>GVFMeta</b> Resource
 * (Meta data of a GVF file)
 *
 * <p>
 * <b>Definition:</b>
 * Pragmas from a GVF
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/GVFMeta">http://hl7.org/fhir/profiles/GVFMeta</a> 
 * </p>
 *
 */
@ResourceDef(name="GVFMeta", profile="http://hl7.org/fhir/profiles/GVFMeta", id="gvfmeta")
public class GVFMeta extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient being described in the file</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GVFMeta.subject.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="GVFMeta.subject.patient", description="Patient being described in the file")
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>Patient being described in the file</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GVFMeta.subject.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceParam PATIENT = new ReferenceParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GVFMeta.subject.patient</b>".
	 */
	public static final Include INCLUDE_SUBJECT_PATIENT = new Include("GVFMeta.subject.patient");

	/**
	 * Search parameter constant for <b>file</b>
	 * <p>
	 * Description: <b>URL to source file of the resource</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GVFMeta.sourceFile</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="file", path="GVFMeta.sourceFile", description="URL to source file of the resource")
	public static final String SP_FILE = "file";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>file</b>
	 * <p>
	 * Description: <b>URL to source file of the resource</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GVFMeta.sourceFile</b><br/>
	 * </p>
	 */
	public static final StringParam FILE = new StringParam(SP_FILE);


	@Child(name="subject", order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Subject being described by the file",
		formalDefinition="Subject being described by the file"
	)
	private java.util.List<Subject> mySubject;
	
	@Child(name="sourceFile", type=AttachmentDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Source GVF file",
		formalDefinition="GVF file from which data of the resource is extracted"
	)
	private AttachmentDt mySourceFile;
	
	@Child(name="gvfVersion", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Version of the GVF file",
		formalDefinition="Valid version of the GVF file"
	)
	private CodeDt myGvfVersion;
	
	@Child(name="referenceFasta", type=UriDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="FASTA file used as reference assembly",
		formalDefinition="URL to FASTA file used as reference assembly"
	)
	private UriDt myReferenceFasta;
	
	@Child(name="featureGFF3", type=UriDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="GFF3 file containing feature being described in the file",
		formalDefinition="GFF3 file containing feature being described in the file"
	)
	private UriDt myFeatureGFF3;
	
	@Child(name="fileDate", type=DateDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Date when the file is updated",
		formalDefinition="Date when the file is updated"
	)
	private DateDt myFileDate;
	
	@Child(name="individual", type=StringDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Id of individual being described in the file",
		formalDefinition="Id of individual being described in the file"
	)
	private java.util.List<StringDt> myIndividual;
	
	@Child(name="population", type=CodeDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Code for population which the individual can be categorized into",
		formalDefinition="Code for population which the individual can be categorized into"
	)
	private CodeDt myPopulation;
	
	@Child(name="platform", order=8, min=0, max=1)	
	@Description(
		shortDefinition="Sequencing platform",
		formalDefinition="Technology platform used in the sequencing"
	)
	private Platform myPlatform;
	
	@Child(name="sequencingScope", type=CodeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Sequencing scope",
		formalDefinition="Scope of the sequencing"
	)
	private CodeDt mySequencingScope;
	
	@Child(name="captureMethod", type=CodeDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Capture method",
		formalDefinition="Capture method used in the sequencing"
	)
	private CodeDt myCaptureMethod;
	
	@Child(name="captureRegions", type=UriDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Region captured in the file",
		formalDefinition="Region captured in the file"
	)
	private UriDt myCaptureRegions;
	
	@Child(name="sequenceAlignment", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="Sequence alignment algorithm/pipline used",
		formalDefinition="Sequence alignment algorithm/pipline used"
	)
	private StringDt mySequenceAlignment;
	
	@Child(name="variantCalling", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="Pipline used for variant calling",
		formalDefinition="Pipline used for variant calling"
	)
	private StringDt myVariantCalling;
	
	@Child(name="sampleDescription", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Description of sample used in the sequencing",
		formalDefinition="Description of sample used in the sequencing"
	)
	private StringDt mySampleDescription;
	
	@Child(name="genomicSource", type=CodeDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="Source of the sample",
		formalDefinition="Source of the sample"
	)
	private CodeDt myGenomicSource;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  mySourceFile,  myGvfVersion,  myReferenceFasta,  myFeatureGFF3,  myFileDate,  myIndividual,  myPopulation,  myPlatform,  mySequencingScope,  myCaptureMethod,  myCaptureRegions,  mySequenceAlignment,  myVariantCalling,  mySampleDescription,  myGenomicSource);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, mySourceFile, myGvfVersion, myReferenceFasta, myFeatureGFF3, myFileDate, myIndividual, myPopulation, myPlatform, mySequencingScope, myCaptureMethod, myCaptureRegions, mySequenceAlignment, myVariantCalling, mySampleDescription, myGenomicSource);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject being described by the file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject being described by the file
     * </p> 
	 */
	public java.util.List<Subject> getSubject() {  
		if (mySubject == null) {
			mySubject = new java.util.ArrayList<Subject>();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject being described by the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject being described by the file
     * </p> 
	 */
	public GVFMeta setSubject(java.util.List<Subject> theValue) {
		mySubject = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>subject</b> (Subject being described by the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject being described by the file
     * </p> 
	 */
	public Subject addSubject() {
		Subject newType = new Subject();
		getSubject().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>subject</b> (Subject being described by the file),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject being described by the file
     * </p> 
	 */
	public Subject getSubjectFirstRep() {
		if (getSubject().isEmpty()) {
			return addSubject();
		}
		return getSubject().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>sourceFile</b> (Source GVF file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * GVF file from which data of the resource is extracted
     * </p> 
	 */
	public AttachmentDt getSourceFile() {  
		if (mySourceFile == null) {
			mySourceFile = new AttachmentDt();
		}
		return mySourceFile;
	}

	/**
	 * Sets the value(s) for <b>sourceFile</b> (Source GVF file)
	 *
     * <p>
     * <b>Definition:</b>
     * GVF file from which data of the resource is extracted
     * </p> 
	 */
	public GVFMeta setSourceFile(AttachmentDt theValue) {
		mySourceFile = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>gvfVersion</b> (Version of the GVF file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Valid version of the GVF file
     * </p> 
	 */
	public CodeDt getGvfVersion() {  
		if (myGvfVersion == null) {
			myGvfVersion = new CodeDt();
		}
		return myGvfVersion;
	}

	/**
	 * Sets the value(s) for <b>gvfVersion</b> (Version of the GVF file)
	 *
     * <p>
     * <b>Definition:</b>
     * Valid version of the GVF file
     * </p> 
	 */
	public GVFMeta setGvfVersion(CodeDt theValue) {
		myGvfVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>gvfVersion</b> (Version of the GVF file)
	 *
     * <p>
     * <b>Definition:</b>
     * Valid version of the GVF file
     * </p> 
	 */
	public GVFMeta setGvfVersion( String theCode) {
		myGvfVersion = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>referenceFasta</b> (FASTA file used as reference assembly).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * URL to FASTA file used as reference assembly
     * </p> 
	 */
	public UriDt getReferenceFasta() {  
		if (myReferenceFasta == null) {
			myReferenceFasta = new UriDt();
		}
		return myReferenceFasta;
	}

	/**
	 * Sets the value(s) for <b>referenceFasta</b> (FASTA file used as reference assembly)
	 *
     * <p>
     * <b>Definition:</b>
     * URL to FASTA file used as reference assembly
     * </p> 
	 */
	public GVFMeta setReferenceFasta(UriDt theValue) {
		myReferenceFasta = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>referenceFasta</b> (FASTA file used as reference assembly)
	 *
     * <p>
     * <b>Definition:</b>
     * URL to FASTA file used as reference assembly
     * </p> 
	 */
	public GVFMeta setReferenceFasta( String theUri) {
		myReferenceFasta = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>featureGFF3</b> (GFF3 file containing feature being described in the file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * GFF3 file containing feature being described in the file
     * </p> 
	 */
	public UriDt getFeatureGFF3() {  
		if (myFeatureGFF3 == null) {
			myFeatureGFF3 = new UriDt();
		}
		return myFeatureGFF3;
	}

	/**
	 * Sets the value(s) for <b>featureGFF3</b> (GFF3 file containing feature being described in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * GFF3 file containing feature being described in the file
     * </p> 
	 */
	public GVFMeta setFeatureGFF3(UriDt theValue) {
		myFeatureGFF3 = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>featureGFF3</b> (GFF3 file containing feature being described in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * GFF3 file containing feature being described in the file
     * </p> 
	 */
	public GVFMeta setFeatureGFF3( String theUri) {
		myFeatureGFF3 = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>fileDate</b> (Date when the file is updated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the file is updated
     * </p> 
	 */
	public DateDt getFileDate() {  
		if (myFileDate == null) {
			myFileDate = new DateDt();
		}
		return myFileDate;
	}

	/**
	 * Sets the value(s) for <b>fileDate</b> (Date when the file is updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the file is updated
     * </p> 
	 */
	public GVFMeta setFileDate(DateDt theValue) {
		myFileDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>fileDate</b> (Date when the file is updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the file is updated
     * </p> 
	 */
	public GVFMeta setFileDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myFileDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>fileDate</b> (Date when the file is updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when the file is updated
     * </p> 
	 */
	public GVFMeta setFileDateWithDayPrecision( Date theDate) {
		myFileDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>individual</b> (Id of individual being described in the file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual being described in the file
     * </p> 
	 */
	public java.util.List<StringDt> getIndividual() {  
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<StringDt>();
		}
		return myIndividual;
	}

	/**
	 * Sets the value(s) for <b>individual</b> (Id of individual being described in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual being described in the file
     * </p> 
	 */
	public GVFMeta setIndividual(java.util.List<StringDt> theValue) {
		myIndividual = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>individual</b> (Id of individual being described in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual being described in the file
     * </p> 
	 */
	public StringDt addIndividual() {
		StringDt newType = new StringDt();
		getIndividual().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>individual</b> (Id of individual being described in the file),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual being described in the file
     * </p> 
	 */
	public StringDt getIndividualFirstRep() {
		if (getIndividual().isEmpty()) {
			return addIndividual();
		}
		return getIndividual().get(0); 
	}
 	/**
	 * Adds a new value for <b>individual</b> (Id of individual being described in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual being described in the file
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GVFMeta addIndividual( String theString) {
		if (myIndividual == null) {
			myIndividual = new java.util.ArrayList<StringDt>();
		}
		myIndividual.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>population</b> (Code for population which the individual can be categorized into).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code for population which the individual can be categorized into
     * </p> 
	 */
	public CodeDt getPopulation() {  
		if (myPopulation == null) {
			myPopulation = new CodeDt();
		}
		return myPopulation;
	}

	/**
	 * Sets the value(s) for <b>population</b> (Code for population which the individual can be categorized into)
	 *
     * <p>
     * <b>Definition:</b>
     * Code for population which the individual can be categorized into
     * </p> 
	 */
	public GVFMeta setPopulation(CodeDt theValue) {
		myPopulation = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>population</b> (Code for population which the individual can be categorized into)
	 *
     * <p>
     * <b>Definition:</b>
     * Code for population which the individual can be categorized into
     * </p> 
	 */
	public GVFMeta setPopulation( String theCode) {
		myPopulation = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>platform</b> (Sequencing platform).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Technology platform used in the sequencing
     * </p> 
	 */
	public Platform getPlatform() {  
		if (myPlatform == null) {
			myPlatform = new Platform();
		}
		return myPlatform;
	}

	/**
	 * Sets the value(s) for <b>platform</b> (Sequencing platform)
	 *
     * <p>
     * <b>Definition:</b>
     * Technology platform used in the sequencing
     * </p> 
	 */
	public GVFMeta setPlatform(Platform theValue) {
		myPlatform = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sequencingScope</b> (Sequencing scope).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Scope of the sequencing
     * </p> 
	 */
	public CodeDt getSequencingScope() {  
		if (mySequencingScope == null) {
			mySequencingScope = new CodeDt();
		}
		return mySequencingScope;
	}

	/**
	 * Sets the value(s) for <b>sequencingScope</b> (Sequencing scope)
	 *
     * <p>
     * <b>Definition:</b>
     * Scope of the sequencing
     * </p> 
	 */
	public GVFMeta setSequencingScope(CodeDt theValue) {
		mySequencingScope = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sequencingScope</b> (Sequencing scope)
	 *
     * <p>
     * <b>Definition:</b>
     * Scope of the sequencing
     * </p> 
	 */
	public GVFMeta setSequencingScope( String theCode) {
		mySequencingScope = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>captureMethod</b> (Capture method).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Capture method used in the sequencing
     * </p> 
	 */
	public CodeDt getCaptureMethod() {  
		if (myCaptureMethod == null) {
			myCaptureMethod = new CodeDt();
		}
		return myCaptureMethod;
	}

	/**
	 * Sets the value(s) for <b>captureMethod</b> (Capture method)
	 *
     * <p>
     * <b>Definition:</b>
     * Capture method used in the sequencing
     * </p> 
	 */
	public GVFMeta setCaptureMethod(CodeDt theValue) {
		myCaptureMethod = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>captureMethod</b> (Capture method)
	 *
     * <p>
     * <b>Definition:</b>
     * Capture method used in the sequencing
     * </p> 
	 */
	public GVFMeta setCaptureMethod( String theCode) {
		myCaptureMethod = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>captureRegions</b> (Region captured in the file).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Region captured in the file
     * </p> 
	 */
	public UriDt getCaptureRegions() {  
		if (myCaptureRegions == null) {
			myCaptureRegions = new UriDt();
		}
		return myCaptureRegions;
	}

	/**
	 * Sets the value(s) for <b>captureRegions</b> (Region captured in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Region captured in the file
     * </p> 
	 */
	public GVFMeta setCaptureRegions(UriDt theValue) {
		myCaptureRegions = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>captureRegions</b> (Region captured in the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Region captured in the file
     * </p> 
	 */
	public GVFMeta setCaptureRegions( String theUri) {
		myCaptureRegions = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sequenceAlignment</b> (Sequence alignment algorithm/pipline used).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence alignment algorithm/pipline used
     * </p> 
	 */
	public StringDt getSequenceAlignment() {  
		if (mySequenceAlignment == null) {
			mySequenceAlignment = new StringDt();
		}
		return mySequenceAlignment;
	}

	/**
	 * Sets the value(s) for <b>sequenceAlignment</b> (Sequence alignment algorithm/pipline used)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence alignment algorithm/pipline used
     * </p> 
	 */
	public GVFMeta setSequenceAlignment(StringDt theValue) {
		mySequenceAlignment = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sequenceAlignment</b> (Sequence alignment algorithm/pipline used)
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence alignment algorithm/pipline used
     * </p> 
	 */
	public GVFMeta setSequenceAlignment( String theString) {
		mySequenceAlignment = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variantCalling</b> (Pipline used for variant calling).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Pipline used for variant calling
     * </p> 
	 */
	public StringDt getVariantCalling() {  
		if (myVariantCalling == null) {
			myVariantCalling = new StringDt();
		}
		return myVariantCalling;
	}

	/**
	 * Sets the value(s) for <b>variantCalling</b> (Pipline used for variant calling)
	 *
     * <p>
     * <b>Definition:</b>
     * Pipline used for variant calling
     * </p> 
	 */
	public GVFMeta setVariantCalling(StringDt theValue) {
		myVariantCalling = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>variantCalling</b> (Pipline used for variant calling)
	 *
     * <p>
     * <b>Definition:</b>
     * Pipline used for variant calling
     * </p> 
	 */
	public GVFMeta setVariantCalling( String theString) {
		myVariantCalling = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sampleDescription</b> (Description of sample used in the sequencing).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of sample used in the sequencing
     * </p> 
	 */
	public StringDt getSampleDescription() {  
		if (mySampleDescription == null) {
			mySampleDescription = new StringDt();
		}
		return mySampleDescription;
	}

	/**
	 * Sets the value(s) for <b>sampleDescription</b> (Description of sample used in the sequencing)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of sample used in the sequencing
     * </p> 
	 */
	public GVFMeta setSampleDescription(StringDt theValue) {
		mySampleDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sampleDescription</b> (Description of sample used in the sequencing)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of sample used in the sequencing
     * </p> 
	 */
	public GVFMeta setSampleDescription( String theString) {
		mySampleDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genomicSource</b> (Source of the sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the sample
     * </p> 
	 */
	public CodeDt getGenomicSource() {  
		if (myGenomicSource == null) {
			myGenomicSource = new CodeDt();
		}
		return myGenomicSource;
	}

	/**
	 * Sets the value(s) for <b>genomicSource</b> (Source of the sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the sample
     * </p> 
	 */
	public GVFMeta setGenomicSource(CodeDt theValue) {
		myGenomicSource = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>genomicSource</b> (Source of the sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Source of the sample
     * </p> 
	 */
	public GVFMeta setGenomicSource( String theCode) {
		myGenomicSource = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>GVFMeta.subject</b> (Subject being described by the file)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject being described by the file
     * </p> 
	 */
	@Block()	
	public static class Subject extends BaseElement implements IResourceBlock {
	
	@Child(name="patient", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Identity of the subejct",
		formalDefinition="Identity of the subejct"
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="fieldId", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Id of individual field of the file that correspond to the subject",
		formalDefinition="Id of individual field of the file that correspond to the subject"
	)
	private StringDt myFieldId;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPatient,  myFieldId);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPatient, myFieldId);
	}

	/**
	 * Gets the value(s) for <b>patient</b> (Identity of the subejct).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of the subejct
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Identity of the subejct)
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of the subejct
     * </p> 
	 */
	public Subject setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>fieldId</b> (Id of individual field of the file that correspond to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual field of the file that correspond to the subject
     * </p> 
	 */
	public StringDt getFieldId() {  
		if (myFieldId == null) {
			myFieldId = new StringDt();
		}
		return myFieldId;
	}

	/**
	 * Sets the value(s) for <b>fieldId</b> (Id of individual field of the file that correspond to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual field of the file that correspond to the subject
     * </p> 
	 */
	public Subject setFieldId(StringDt theValue) {
		myFieldId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>fieldId</b> (Id of individual field of the file that correspond to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of individual field of the file that correspond to the subject
     * </p> 
	 */
	public Subject setFieldId( String theString) {
		myFieldId = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GVFMeta.platform</b> (Sequencing platform)
	 *
     * <p>
     * <b>Definition:</b>
     * Technology platform used in the sequencing
     * </p> 
	 */
	@Block()	
	public static class Platform extends BaseElement implements IResourceBlock {
	
	@Child(name="class", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Platform class",
		formalDefinition="Class of the sequencing platform"
	)
	private CodeDt myClassElement;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Platform version",
		formalDefinition="Version of the platform being used"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Platform name",
		formalDefinition="Name of the platform being used"
	)
	private CodeDt myName;
	
	@Child(name="identity", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Platform id",
		formalDefinition="Id of the platfrom being used"
	)
	private StringDt myIdentity;
	
	@Child(name="readLength", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Read length",
		formalDefinition="Read length of the technology"
	)
	private IntegerDt myReadLength;
	
	@Child(name="readType", type=CodeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Read type",
		formalDefinition="Read type of the technology"
	)
	private CodeDt myReadType;
	
	@Child(name="readPairSpan", type=IntegerDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Read pair span",
		formalDefinition="Read pair span of the technology"
	)
	private IntegerDt myReadPairSpan;
	
	@Child(name="averageCoverage", type=IntegerDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Average coverage",
		formalDefinition="Average coverage of the technology"
	)
	private IntegerDt myAverageCoverage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myClassElement,  myVersion,  myName,  myIdentity,  myReadLength,  myReadType,  myReadPairSpan,  myAverageCoverage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myClassElement, myVersion, myName, myIdentity, myReadLength, myReadType, myReadPairSpan, myAverageCoverage);
	}

	/**
	 * Gets the value(s) for <b>class</b> (Platform class).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the sequencing platform
     * </p> 
	 */
	public CodeDt getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeDt();
		}
		return myClassElement;
	}

	/**
	 * Sets the value(s) for <b>class</b> (Platform class)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the sequencing platform
     * </p> 
	 */
	public Platform setClassElement(CodeDt theValue) {
		myClassElement = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>class</b> (Platform class)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the sequencing platform
     * </p> 
	 */
	public Platform setClassElement( String theCode) {
		myClassElement = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Platform version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the platform being used
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Platform version)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the platform being used
     * </p> 
	 */
	public Platform setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>version</b> (Platform version)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the platform being used
     * </p> 
	 */
	public Platform setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Platform name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the platform being used
     * </p> 
	 */
	public CodeDt getName() {  
		if (myName == null) {
			myName = new CodeDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Platform name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the platform being used
     * </p> 
	 */
	public Platform setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Platform name)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of the platform being used
     * </p> 
	 */
	public Platform setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identity</b> (Platform id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the platfrom being used
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Platform id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the platfrom being used
     * </p> 
	 */
	public Platform setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Platform id)
	 *
     * <p>
     * <b>Definition:</b>
     * Id of the platfrom being used
     * </p> 
	 */
	public Platform setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>readLength</b> (Read length).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Read length of the technology
     * </p> 
	 */
	public IntegerDt getReadLength() {  
		if (myReadLength == null) {
			myReadLength = new IntegerDt();
		}
		return myReadLength;
	}

	/**
	 * Sets the value(s) for <b>readLength</b> (Read length)
	 *
     * <p>
     * <b>Definition:</b>
     * Read length of the technology
     * </p> 
	 */
	public Platform setReadLength(IntegerDt theValue) {
		myReadLength = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>readLength</b> (Read length)
	 *
     * <p>
     * <b>Definition:</b>
     * Read length of the technology
     * </p> 
	 */
	public Platform setReadLength( int theInteger) {
		myReadLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>readType</b> (Read type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Read type of the technology
     * </p> 
	 */
	public CodeDt getReadType() {  
		if (myReadType == null) {
			myReadType = new CodeDt();
		}
		return myReadType;
	}

	/**
	 * Sets the value(s) for <b>readType</b> (Read type)
	 *
     * <p>
     * <b>Definition:</b>
     * Read type of the technology
     * </p> 
	 */
	public Platform setReadType(CodeDt theValue) {
		myReadType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>readType</b> (Read type)
	 *
     * <p>
     * <b>Definition:</b>
     * Read type of the technology
     * </p> 
	 */
	public Platform setReadType( String theCode) {
		myReadType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>readPairSpan</b> (Read pair span).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Read pair span of the technology
     * </p> 
	 */
	public IntegerDt getReadPairSpan() {  
		if (myReadPairSpan == null) {
			myReadPairSpan = new IntegerDt();
		}
		return myReadPairSpan;
	}

	/**
	 * Sets the value(s) for <b>readPairSpan</b> (Read pair span)
	 *
     * <p>
     * <b>Definition:</b>
     * Read pair span of the technology
     * </p> 
	 */
	public Platform setReadPairSpan(IntegerDt theValue) {
		myReadPairSpan = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>readPairSpan</b> (Read pair span)
	 *
     * <p>
     * <b>Definition:</b>
     * Read pair span of the technology
     * </p> 
	 */
	public Platform setReadPairSpan( int theInteger) {
		myReadPairSpan = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>averageCoverage</b> (Average coverage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Average coverage of the technology
     * </p> 
	 */
	public IntegerDt getAverageCoverage() {  
		if (myAverageCoverage == null) {
			myAverageCoverage = new IntegerDt();
		}
		return myAverageCoverage;
	}

	/**
	 * Sets the value(s) for <b>averageCoverage</b> (Average coverage)
	 *
     * <p>
     * <b>Definition:</b>
     * Average coverage of the technology
     * </p> 
	 */
	public Platform setAverageCoverage(IntegerDt theValue) {
		myAverageCoverage = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>averageCoverage</b> (Average coverage)
	 *
     * <p>
     * <b>Definition:</b>
     * Average coverage of the technology
     * </p> 
	 */
	public Platform setAverageCoverage( int theInteger) {
		myAverageCoverage = new IntegerDt(theInteger); 
		return this; 
	}

 

	}




}
