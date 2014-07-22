















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

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImagingModalityEnum;
import ca.uhn.fhir.model.dstu.valueset.InstanceAvailabilityEnum;
import ca.uhn.fhir.model.dstu.valueset.ModalityEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.OidDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.NumberClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>ImagingStudy</b> Resource
 * (A set of images produced in single study (one or more series of references images))
 *
 * <p>
 * <b>Definition:</b>
 * Manifest of a set of images produced in study. The set of images may include every image in the study, or it may be an incomplete sample, such as a list of key images
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ImagingStudy">http://hl7.org/fhir/profiles/ImagingStudy</a> 
 * </p>
 *
 */
@ResourceDef(name="ImagingStudy", profile="http://hl7.org/fhir/profiles/ImagingStudy", id="imagingstudy")
public class ImagingStudy extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Who the study is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImagingStudy.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="ImagingStudy.subject", description="Who the study is about", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Who the study is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImagingStudy.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImagingStudy.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("ImagingStudy.subject");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date the study was done was taken</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImagingStudy.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="ImagingStudy.dateTime", description="The date the study was done was taken", type="date")
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date the study was done was taken</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImagingStudy.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>accession</b>
	 * <p>
	 * Description: <b>The accession id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.accessionNo</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="accession", path="ImagingStudy.accessionNo", description="The accession id for the image", type="token")
	public static final String SP_ACCESSION = "accession";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>accession</b>
	 * <p>
	 * Description: <b>The accession id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.accessionNo</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACCESSION = new TokenClientParam(SP_ACCESSION);

	/**
	 * Search parameter constant for <b>study</b>
	 * <p>
	 * Description: <b>The study id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.uid</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="study", path="ImagingStudy.uid", description="The study id for the image", type="token")
	public static final String SP_STUDY = "study";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>study</b>
	 * <p>
	 * Description: <b>The study id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.uid</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STUDY = new TokenClientParam(SP_STUDY);

	/**
	 * Search parameter constant for <b>series</b>
	 * <p>
	 * Description: <b>The series id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.uid</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="series", path="ImagingStudy.series.uid", description="The series id for the image", type="token")
	public static final String SP_SERIES = "series";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>series</b>
	 * <p>
	 * Description: <b>The series id for the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.uid</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SERIES = new TokenClientParam(SP_SERIES);

	/**
	 * Search parameter constant for <b>modality</b>
	 * <p>
	 * Description: <b>The modality of the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.modality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="modality", path="ImagingStudy.series.modality", description="The modality of the image", type="token")
	public static final String SP_MODALITY = "modality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>modality</b>
	 * <p>
	 * Description: <b>The modality of the image</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.modality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam MODALITY = new TokenClientParam(SP_MODALITY);

	/**
	 * Search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b>The size of the image in MB - may include > or < in the value</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="size", path="", description="The size of the image in MB - may include > or < in the value", type="number")
	public static final String SP_SIZE = "size";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b>The size of the image in MB - may include > or < in the value</b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final NumberClientParam SIZE = new NumberClientParam(SP_SIZE);

	/**
	 * Search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.bodySite</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="bodysite", path="ImagingStudy.series.bodySite", description="", type="token")
	public static final String SP_BODYSITE = "bodysite";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.bodySite</b><br/>
	 * </p>
	 */
	public static final TokenClientParam BODYSITE = new TokenClientParam(SP_BODYSITE);

	/**
	 * Search parameter constant for <b>uid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.uid</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="uid", path="ImagingStudy.series.instance.uid", description="", type="token")
	public static final String SP_UID = "uid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>uid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.uid</b><br/>
	 * </p>
	 */
	public static final TokenClientParam UID = new TokenClientParam(SP_UID);

	/**
	 * Search parameter constant for <b>dicom-class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.sopclass</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dicom-class", path="ImagingStudy.series.instance.sopclass", description="", type="token")
	public static final String SP_DICOM_CLASS = "dicom-class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dicom-class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImagingStudy.series.instance.sopclass</b><br/>
	 * </p>
	 */
	public static final TokenClientParam DICOM_CLASS = new TokenClientParam(SP_DICOM_CLASS);


	@Child(name="dateTime", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="When the study was performed",
		formalDefinition="Date and Time the study took place"
	)
	private DateTimeDt myDateTime;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who the images are of",
		formalDefinition="Who the images are of"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="uid", type=OidDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Formal identifier for the study (0020,000D)",
		formalDefinition="Formal identifier for the study"
	)
	private OidDt myUid;
	
	@Child(name="accessionNo", type=IdentifierDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Accession Number (0008,0050)",
		formalDefinition="Accession Number"
	)
	private IdentifierDt myAccessionNo;
	
	@Child(name="identifier", type=IdentifierDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Other identifiers for the study (0020,0010)",
		formalDefinition="Other identifiers for the study"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="order", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.DiagnosticOrder.class	})
	@Description(
		shortDefinition="Order(s) that caused this study to be performed",
		formalDefinition="A list of the diagnostic orders that resulted in this imaging study being performed"
	)
	private java.util.List<ResourceReferenceDt> myOrder;
	
	@Child(name="modality", type=CodeDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="All series.modality if actual acquisition modalities",
		formalDefinition="A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)"
	)
	private java.util.List<BoundCodeDt<ImagingModalityEnum>> myModality;
	
	@Child(name="referrer", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Referring physician (0008,0090)",
		formalDefinition="The requesting/referring physician"
	)
	private ResourceReferenceDt myReferrer;
	
	@Child(name="availability", type=CodeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)",
		formalDefinition="Availability of study (online, offline or nearline)"
	)
	private BoundCodeDt<InstanceAvailabilityEnum> myAvailability;
	
	@Child(name="url", type=UriDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Retrieve URI (0008,1190)",
		formalDefinition="WADO-RS URI where Study is available"
	)
	private UriDt myUrl;
	
	@Child(name="numberOfSeries", type=IntegerDt.class, order=10, min=1, max=1)	
	@Description(
		shortDefinition="Number of Study Related Series (0020,1206)",
		formalDefinition="Number of Series in Study"
	)
	private IntegerDt myNumberOfSeries;
	
	@Child(name="numberOfInstances", type=IntegerDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="Number of Study Related Instances (0020,1208)",
		formalDefinition="Number of SOP Instances in Study"
	)
	private IntegerDt myNumberOfInstances;
	
	@Child(name="clinicalInformation", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="Diagnoses etc with request (0040,1002)",
		formalDefinition="Diagnoses etc provided with request"
	)
	private StringDt myClinicalInformation;
	
	@Child(name="procedure", type=CodingDt.class, order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Type of procedure performed (0008,1032)",
		formalDefinition="Type of procedure performed"
	)
	private java.util.List<CodingDt> myProcedure;
	
	@Child(name="interpreter", order=14, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who interpreted images (0008,1060)",
		formalDefinition="Who read study and interpreted the images"
	)
	private ResourceReferenceDt myInterpreter;
	
	@Child(name="description", type=StringDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="Institution-generated description (0008,1030)",
		formalDefinition="Institution-generated description or classification of the Study (component) performed"
	)
	private StringDt myDescription;
	
	@Child(name="series", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Each study has one or more series of instances",
		formalDefinition="Each study has one or more series of image instances"
	)
	private java.util.List<Series> mySeries;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDateTime,  mySubject,  myUid,  myAccessionNo,  myIdentifier,  myOrder,  myModality,  myReferrer,  myAvailability,  myUrl,  myNumberOfSeries,  myNumberOfInstances,  myClinicalInformation,  myProcedure,  myInterpreter,  myDescription,  mySeries);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDateTime, mySubject, myUid, myAccessionNo, myIdentifier, myOrder, myModality, myReferrer, myAvailability, myUrl, myNumberOfSeries, myNumberOfInstances, myClinicalInformation, myProcedure, myInterpreter, myDescription, mySeries);
	}

	/**
	 * Gets the value(s) for <b>dateTime</b> (When the study was performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study took place
     * </p> 
	 */
	public DateTimeDt getDateTime() {  
		if (myDateTime == null) {
			myDateTime = new DateTimeDt();
		}
		return myDateTime;
	}

	/**
	 * Sets the value(s) for <b>dateTime</b> (When the study was performed)
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study took place
     * </p> 
	 */
	public ImagingStudy setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dateTime</b> (When the study was performed)
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study took place
     * </p> 
	 */
	public ImagingStudy setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> (When the study was performed)
	 *
     * <p>
     * <b>Definition:</b>
     * Date and Time the study took place
     * </p> 
	 */
	public ImagingStudy setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who the images are of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who the images are of
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who the images are of)
	 *
     * <p>
     * <b>Definition:</b>
     * Who the images are of
     * </p> 
	 */
	public ImagingStudy setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>uid</b> (Formal identifier for the study (0020,000D)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for the study
     * </p> 
	 */
	public OidDt getUid() {  
		if (myUid == null) {
			myUid = new OidDt();
		}
		return myUid;
	}

	/**
	 * Sets the value(s) for <b>uid</b> (Formal identifier for the study (0020,000D))
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for the study
     * </p> 
	 */
	public ImagingStudy setUid(OidDt theValue) {
		myUid = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>accessionNo</b> (Accession Number (0008,0050)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Accession Number
     * </p> 
	 */
	public IdentifierDt getAccessionNo() {  
		if (myAccessionNo == null) {
			myAccessionNo = new IdentifierDt();
		}
		return myAccessionNo;
	}

	/**
	 * Sets the value(s) for <b>accessionNo</b> (Accession Number (0008,0050))
	 *
     * <p>
     * <b>Definition:</b>
     * Accession Number
     * </p> 
	 */
	public ImagingStudy setAccessionNo(IdentifierDt theValue) {
		myAccessionNo = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>accessionNo</b> (Accession Number (0008,0050))
	 *
     * <p>
     * <b>Definition:</b>
     * Accession Number
     * </p> 
	 */
	public ImagingStudy setAccessionNo( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myAccessionNo = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>accessionNo</b> (Accession Number (0008,0050))
	 *
     * <p>
     * <b>Definition:</b>
     * Accession Number
     * </p> 
	 */
	public ImagingStudy setAccessionNo( String theSystem,  String theValue) {
		myAccessionNo = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (Other identifiers for the study (0020,0010)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Other identifiers for the study (0020,0010))
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
     * </p> 
	 */
	public ImagingStudy setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Other identifiers for the study (0020,0010))
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Other identifiers for the study (0020,0010)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the study (0020,0010))
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ImagingStudy addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the study (0020,0010))
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers for the study
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ImagingStudy addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>order</b> (Order(s) that caused this study to be performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the diagnostic orders that resulted in this imaging study being performed
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getOrder() {  
		if (myOrder == null) {
			myOrder = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myOrder;
	}

	/**
	 * Sets the value(s) for <b>order</b> (Order(s) that caused this study to be performed)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the diagnostic orders that resulted in this imaging study being performed
     * </p> 
	 */
	public ImagingStudy setOrder(java.util.List<ResourceReferenceDt> theValue) {
		myOrder = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>order</b> (Order(s) that caused this study to be performed)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the diagnostic orders that resulted in this imaging study being performed
     * </p> 
	 */
	public ResourceReferenceDt addOrder() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getOrder().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>modality</b> (All series.modality if actual acquisition modalities).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public java.util.List<BoundCodeDt<ImagingModalityEnum>> getModality() {  
		if (myModality == null) {
			myModality = new java.util.ArrayList<BoundCodeDt<ImagingModalityEnum>>();
		}
		return myModality;
	}

	/**
	 * Sets the value(s) for <b>modality</b> (All series.modality if actual acquisition modalities)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public ImagingStudy setModality(java.util.List<BoundCodeDt<ImagingModalityEnum>> theValue) {
		myModality = theValue;
		return this;
	}

	/**
	 * Add a value for <b>modality</b> (All series.modality if actual acquisition modalities) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public BoundCodeDt<ImagingModalityEnum> addModality(ImagingModalityEnum theValue) {
		BoundCodeDt<ImagingModalityEnum> retVal = new BoundCodeDt<ImagingModalityEnum>(ImagingModalityEnum.VALUESET_BINDER, theValue);
		getModality().add(retVal);
		return retVal;
	}

	/**
	 * Add a value for <b>modality</b> (All series.modality if actual acquisition modalities)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public BoundCodeDt<ImagingModalityEnum> addModality() {
		BoundCodeDt<ImagingModalityEnum> retVal = new BoundCodeDt<ImagingModalityEnum>(ImagingModalityEnum.VALUESET_BINDER);
		getModality().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>modality</b> (All series.modality if actual acquisition modalities)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)
     * </p> 
	 */
	public ImagingStudy setModality(ImagingModalityEnum theValue) {
		getModality().clear();
		addModality(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>referrer</b> (Referring physician (0008,0090)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The requesting/referring physician
     * </p> 
	 */
	public ResourceReferenceDt getReferrer() {  
		if (myReferrer == null) {
			myReferrer = new ResourceReferenceDt();
		}
		return myReferrer;
	}

	/**
	 * Sets the value(s) for <b>referrer</b> (Referring physician (0008,0090))
	 *
     * <p>
     * <b>Definition:</b>
     * The requesting/referring physician
     * </p> 
	 */
	public ImagingStudy setReferrer(ResourceReferenceDt theValue) {
		myReferrer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>availability</b> (ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public BoundCodeDt<InstanceAvailabilityEnum> getAvailability() {  
		if (myAvailability == null) {
			myAvailability = new BoundCodeDt<InstanceAvailabilityEnum>(InstanceAvailabilityEnum.VALUESET_BINDER);
		}
		return myAvailability;
	}

	/**
	 * Sets the value(s) for <b>availability</b> (ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056))
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public ImagingStudy setAvailability(BoundCodeDt<InstanceAvailabilityEnum> theValue) {
		myAvailability = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>availability</b> (ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056))
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of study (online, offline or nearline)
     * </p> 
	 */
	public ImagingStudy setAvailability(InstanceAvailabilityEnum theValue) {
		getAvailability().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> (Retrieve URI (0008,1190)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (Retrieve URI (0008,1190))
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public ImagingStudy setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>url</b> (Retrieve URI (0008,1190))
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Study is available
     * </p> 
	 */
	public ImagingStudy setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>numberOfSeries</b> (Number of Study Related Series (0020,1206)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public IntegerDt getNumberOfSeries() {  
		if (myNumberOfSeries == null) {
			myNumberOfSeries = new IntegerDt();
		}
		return myNumberOfSeries;
	}

	/**
	 * Sets the value(s) for <b>numberOfSeries</b> (Number of Study Related Series (0020,1206))
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfSeries(IntegerDt theValue) {
		myNumberOfSeries = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>numberOfSeries</b> (Number of Study Related Series (0020,1206))
	 *
     * <p>
     * <b>Definition:</b>
     * Number of Series in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfSeries( int theInteger) {
		myNumberOfSeries = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>numberOfInstances</b> (Number of Study Related Instances (0020,1208)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public IntegerDt getNumberOfInstances() {  
		if (myNumberOfInstances == null) {
			myNumberOfInstances = new IntegerDt();
		}
		return myNumberOfInstances;
	}

	/**
	 * Sets the value(s) for <b>numberOfInstances</b> (Number of Study Related Instances (0020,1208))
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfInstances(IntegerDt theValue) {
		myNumberOfInstances = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>numberOfInstances</b> (Number of Study Related Instances (0020,1208))
	 *
     * <p>
     * <b>Definition:</b>
     * Number of SOP Instances in Study
     * </p> 
	 */
	public ImagingStudy setNumberOfInstances( int theInteger) {
		myNumberOfInstances = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>clinicalInformation</b> (Diagnoses etc with request (0040,1002)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public StringDt getClinicalInformation() {  
		if (myClinicalInformation == null) {
			myClinicalInformation = new StringDt();
		}
		return myClinicalInformation;
	}

	/**
	 * Sets the value(s) for <b>clinicalInformation</b> (Diagnoses etc with request (0040,1002))
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public ImagingStudy setClinicalInformation(StringDt theValue) {
		myClinicalInformation = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>clinicalInformation</b> (Diagnoses etc with request (0040,1002))
	 *
     * <p>
     * <b>Definition:</b>
     * Diagnoses etc provided with request
     * </p> 
	 */
	public ImagingStudy setClinicalInformation( String theString) {
		myClinicalInformation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>procedure</b> (Type of procedure performed (0008,1032)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public java.util.List<CodingDt> getProcedure() {  
		if (myProcedure == null) {
			myProcedure = new java.util.ArrayList<CodingDt>();
		}
		return myProcedure;
	}

	/**
	 * Sets the value(s) for <b>procedure</b> (Type of procedure performed (0008,1032))
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public ImagingStudy setProcedure(java.util.List<CodingDt> theValue) {
		myProcedure = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>procedure</b> (Type of procedure performed (0008,1032))
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public CodingDt addProcedure() {
		CodingDt newType = new CodingDt();
		getProcedure().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>procedure</b> (Type of procedure performed (0008,1032)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of procedure performed
     * </p> 
	 */
	public CodingDt getProcedureFirstRep() {
		if (getProcedure().isEmpty()) {
			return addProcedure();
		}
		return getProcedure().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>interpreter</b> (Who interpreted images (0008,1060)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who read study and interpreted the images
     * </p> 
	 */
	public ResourceReferenceDt getInterpreter() {  
		if (myInterpreter == null) {
			myInterpreter = new ResourceReferenceDt();
		}
		return myInterpreter;
	}

	/**
	 * Sets the value(s) for <b>interpreter</b> (Who interpreted images (0008,1060))
	 *
     * <p>
     * <b>Definition:</b>
     * Who read study and interpreted the images
     * </p> 
	 */
	public ImagingStudy setInterpreter(ResourceReferenceDt theValue) {
		myInterpreter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Institution-generated description (0008,1030)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Institution-generated description or classification of the Study (component) performed
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Institution-generated description (0008,1030))
	 *
     * <p>
     * <b>Definition:</b>
     * Institution-generated description or classification of the Study (component) performed
     * </p> 
	 */
	public ImagingStudy setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Institution-generated description (0008,1030))
	 *
     * <p>
     * <b>Definition:</b>
     * Institution-generated description or classification of the Study (component) performed
     * </p> 
	 */
	public ImagingStudy setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>series</b> (Each study has one or more series of instances).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public java.util.List<Series> getSeries() {  
		if (mySeries == null) {
			mySeries = new java.util.ArrayList<Series>();
		}
		return mySeries;
	}

	/**
	 * Sets the value(s) for <b>series</b> (Each study has one or more series of instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public ImagingStudy setSeries(java.util.List<Series> theValue) {
		mySeries = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>series</b> (Each study has one or more series of instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public Series addSeries() {
		Series newType = new Series();
		getSeries().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>series</b> (Each study has one or more series of instances),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	public Series getSeriesFirstRep() {
		if (getSeries().isEmpty()) {
			return addSeries();
		}
		return getSeries().get(0); 
	}
  
	/**
	 * Block class for child element: <b>ImagingStudy.series</b> (Each study has one or more series of instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Each study has one or more series of image instances
     * </p> 
	 */
	@Block()	
	public static class Series extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="number", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Number of this series in overall sequence (0020,0011)",
		formalDefinition="The number of this series in the overall sequence"
	)
	private IntegerDt myNumber;
	
	@Child(name="modality", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="The modality of the instances in the series (0008,0060)",
		formalDefinition="The modality of this series sequence"
	)
	private BoundCodeDt<ModalityEnum> myModality;
	
	@Child(name="uid", type=OidDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Formal identifier for this series (0020,000E)",
		formalDefinition="Formal identifier for this series"
	)
	private OidDt myUid;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="A description of the series (0008,103E)",
		formalDefinition="A description of the series"
	)
	private StringDt myDescription;
	
	@Child(name="numberOfInstances", type=IntegerDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Number of Series Related Instances (0020,1209)",
		formalDefinition="Sequence that contains attributes from the"
	)
	private IntegerDt myNumberOfInstances;
	
	@Child(name="availability", type=CodeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)",
		formalDefinition="Availability of series (online, offline or nearline)"
	)
	private BoundCodeDt<InstanceAvailabilityEnum> myAvailability;
	
	@Child(name="url", type=UriDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Retrieve URI (0008,1115 > 0008,1190)",
		formalDefinition="WADO-RS URI where Series is available"
	)
	private UriDt myUrl;
	
	@Child(name="bodySite", type=CodingDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Body part examined (Map from 0018,0015)",
		formalDefinition="Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed"
	)
	private CodingDt myBodySite;
	
	@Child(name="dateTime", type=DateTimeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="When the series started",
		formalDefinition=""
	)
	private DateTimeDt myDateTime;
	
	@Child(name="instance", order=9, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A single instance taken from a patient (image or other)",
		formalDefinition="A single image taken from a patient"
	)
	private java.util.List<SeriesInstance> myInstance;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myNumber,  myModality,  myUid,  myDescription,  myNumberOfInstances,  myAvailability,  myUrl,  myBodySite,  myDateTime,  myInstance);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myNumber, myModality, myUid, myDescription, myNumberOfInstances, myAvailability, myUrl, myBodySite, myDateTime, myInstance);
	}

	/**
	 * Gets the value(s) for <b>number</b> (Number of this series in overall sequence (0020,0011)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this series in the overall sequence
     * </p> 
	 */
	public IntegerDt getNumber() {  
		if (myNumber == null) {
			myNumber = new IntegerDt();
		}
		return myNumber;
	}

	/**
	 * Sets the value(s) for <b>number</b> (Number of this series in overall sequence (0020,0011))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this series in the overall sequence
     * </p> 
	 */
	public Series setNumber(IntegerDt theValue) {
		myNumber = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>number</b> (Number of this series in overall sequence (0020,0011))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this series in the overall sequence
     * </p> 
	 */
	public Series setNumber( int theInteger) {
		myNumber = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>modality</b> (The modality of the instances in the series (0008,0060)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public BoundCodeDt<ModalityEnum> getModality() {  
		if (myModality == null) {
			myModality = new BoundCodeDt<ModalityEnum>(ModalityEnum.VALUESET_BINDER);
		}
		return myModality;
	}

	/**
	 * Sets the value(s) for <b>modality</b> (The modality of the instances in the series (0008,0060))
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public Series setModality(BoundCodeDt<ModalityEnum> theValue) {
		myModality = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>modality</b> (The modality of the instances in the series (0008,0060))
	 *
     * <p>
     * <b>Definition:</b>
     * The modality of this series sequence
     * </p> 
	 */
	public Series setModality(ModalityEnum theValue) {
		getModality().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>uid</b> (Formal identifier for this series (0020,000E)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this series
     * </p> 
	 */
	public OidDt getUid() {  
		if (myUid == null) {
			myUid = new OidDt();
		}
		return myUid;
	}

	/**
	 * Sets the value(s) for <b>uid</b> (Formal identifier for this series (0020,000E))
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this series
     * </p> 
	 */
	public Series setUid(OidDt theValue) {
		myUid = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (A description of the series (0008,103E)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the series
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (A description of the series (0008,103E))
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the series
     * </p> 
	 */
	public Series setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (A description of the series (0008,103E))
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the series
     * </p> 
	 */
	public Series setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>numberOfInstances</b> (Number of Series Related Instances (0020,1209)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public IntegerDt getNumberOfInstances() {  
		if (myNumberOfInstances == null) {
			myNumberOfInstances = new IntegerDt();
		}
		return myNumberOfInstances;
	}

	/**
	 * Sets the value(s) for <b>numberOfInstances</b> (Number of Series Related Instances (0020,1209))
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public Series setNumberOfInstances(IntegerDt theValue) {
		myNumberOfInstances = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>numberOfInstances</b> (Number of Series Related Instances (0020,1209))
	 *
     * <p>
     * <b>Definition:</b>
     * Sequence that contains attributes from the
     * </p> 
	 */
	public Series setNumberOfInstances( int theInteger) {
		myNumberOfInstances = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>availability</b> (ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public BoundCodeDt<InstanceAvailabilityEnum> getAvailability() {  
		if (myAvailability == null) {
			myAvailability = new BoundCodeDt<InstanceAvailabilityEnum>(InstanceAvailabilityEnum.VALUESET_BINDER);
		}
		return myAvailability;
	}

	/**
	 * Sets the value(s) for <b>availability</b> (ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056))
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public Series setAvailability(BoundCodeDt<InstanceAvailabilityEnum> theValue) {
		myAvailability = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>availability</b> (ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056))
	 *
     * <p>
     * <b>Definition:</b>
     * Availability of series (online, offline or nearline)
     * </p> 
	 */
	public Series setAvailability(InstanceAvailabilityEnum theValue) {
		getAvailability().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> (Retrieve URI (0008,1115 > 0008,1190)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (Retrieve URI (0008,1115 > 0008,1190))
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public Series setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>url</b> (Retrieve URI (0008,1115 > 0008,1190))
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS URI where Series is available
     * </p> 
	 */
	public Series setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>bodySite</b> (Body part examined (Map from 0018,0015)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed
     * </p> 
	 */
	public CodingDt getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new CodingDt();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (Body part examined (Map from 0018,0015))
	 *
     * <p>
     * <b>Definition:</b>
     * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed
     * </p> 
	 */
	public Series setBodySite(CodingDt theValue) {
		myBodySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateTime</b> (When the series started).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DateTimeDt getDateTime() {  
		if (myDateTime == null) {
			myDateTime = new DateTimeDt();
		}
		return myDateTime;
	}

	/**
	 * Sets the value(s) for <b>dateTime</b> (When the series started)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Series setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dateTime</b> (When the series started)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Series setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> (When the series started)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Series setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>instance</b> (A single instance taken from a patient (image or other)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public java.util.List<SeriesInstance> getInstance() {  
		if (myInstance == null) {
			myInstance = new java.util.ArrayList<SeriesInstance>();
		}
		return myInstance;
	}

	/**
	 * Sets the value(s) for <b>instance</b> (A single instance taken from a patient (image or other))
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public Series setInstance(java.util.List<SeriesInstance> theValue) {
		myInstance = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>instance</b> (A single instance taken from a patient (image or other))
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public SeriesInstance addInstance() {
		SeriesInstance newType = new SeriesInstance();
		getInstance().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>instance</b> (A single instance taken from a patient (image or other)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	public SeriesInstance getInstanceFirstRep() {
		if (getInstance().isEmpty()) {
			return addInstance();
		}
		return getInstance().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>ImagingStudy.series.instance</b> (A single instance taken from a patient (image or other))
	 *
     * <p>
     * <b>Definition:</b>
     * A single image taken from a patient
     * </p> 
	 */
	@Block()	
	public static class SeriesInstance extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="number", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The number of this instance in the series (0020,0013)",
		formalDefinition="The number of this image in the series"
	)
	private IntegerDt myNumber;
	
	@Child(name="uid", type=OidDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Formal identifier for this instance (0008,0018)",
		formalDefinition="Formal identifier for this image"
	)
	private OidDt myUid;
	
	@Child(name="sopclass", type=OidDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="DICOM class type (0008,0016)",
		formalDefinition="DICOM Image type"
	)
	private OidDt mySopclass;
	
	@Child(name="type", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Type of instance (image etc) (0004,1430)",
		formalDefinition=""
	)
	private StringDt myType;
	
	@Child(name="title", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Description (0070,0080 | 0040,A043 > 0008,0104 | 0042,0010 | 0008,0008)",
		formalDefinition=""
	)
	private StringDt myTitle;
	
	@Child(name="url", type=UriDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="WADO-RS service where instance is available  (0008,1199 > 0008,1190)",
		formalDefinition="WADO-RS url where image is available"
	)
	private UriDt myUrl;
	
	@Child(name="attachment", order=6, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="A FHIR resource with content for this instance",
		formalDefinition=""
	)
	private ResourceReferenceDt myAttachment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myNumber,  myUid,  mySopclass,  myType,  myTitle,  myUrl,  myAttachment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myNumber, myUid, mySopclass, myType, myTitle, myUrl, myAttachment);
	}

	/**
	 * Gets the value(s) for <b>number</b> (The number of this instance in the series (0020,0013)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public IntegerDt getNumber() {  
		if (myNumber == null) {
			myNumber = new IntegerDt();
		}
		return myNumber;
	}

	/**
	 * Sets the value(s) for <b>number</b> (The number of this instance in the series (0020,0013))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public SeriesInstance setNumber(IntegerDt theValue) {
		myNumber = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>number</b> (The number of this instance in the series (0020,0013))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of this image in the series
     * </p> 
	 */
	public SeriesInstance setNumber( int theInteger) {
		myNumber = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>uid</b> (Formal identifier for this instance (0008,0018)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this image
     * </p> 
	 */
	public OidDt getUid() {  
		if (myUid == null) {
			myUid = new OidDt();
		}
		return myUid;
	}

	/**
	 * Sets the value(s) for <b>uid</b> (Formal identifier for this instance (0008,0018))
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier for this image
     * </p> 
	 */
	public SeriesInstance setUid(OidDt theValue) {
		myUid = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sopclass</b> (DICOM class type (0008,0016)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DICOM Image type
     * </p> 
	 */
	public OidDt getSopclass() {  
		if (mySopclass == null) {
			mySopclass = new OidDt();
		}
		return mySopclass;
	}

	/**
	 * Sets the value(s) for <b>sopclass</b> (DICOM class type (0008,0016))
	 *
     * <p>
     * <b>Definition:</b>
     * DICOM Image type
     * </p> 
	 */
	public SeriesInstance setSopclass(OidDt theValue) {
		mySopclass = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type </b> (Type of instance (image etc) (0004,1430)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getType() {  
		if (myType == null) {
			myType = new StringDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type </b> (Type of instance (image etc) (0004,1430))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SeriesInstance setType(StringDt theValue) {
		myType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>type </b> (Type of instance (image etc) (0004,1430))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SeriesInstance setType( String theString) {
		myType = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>title</b> (Description (0070,0080 | 0040,A043 > 0008,0104 | 0042,0010 | 0008,0008)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getTitle() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	/**
	 * Sets the value(s) for <b>title</b> (Description (0070,0080 | 0040,A043 > 0008,0104 | 0042,0010 | 0008,0008))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SeriesInstance setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>title</b> (Description (0070,0080 | 0040,A043 > 0008,0104 | 0042,0010 | 0008,0008))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SeriesInstance setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>url</b> (WADO-RS service where instance is available  (0008,1199 > 0008,1190)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (WADO-RS service where instance is available  (0008,1199 > 0008,1190))
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public SeriesInstance setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>url</b> (WADO-RS service where instance is available  (0008,1199 > 0008,1190))
	 *
     * <p>
     * <b>Definition:</b>
     * WADO-RS url where image is available
     * </p> 
	 */
	public SeriesInstance setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>attachment</b> (A FHIR resource with content for this instance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getAttachment() {  
		if (myAttachment == null) {
			myAttachment = new ResourceReferenceDt();
		}
		return myAttachment;
	}

	/**
	 * Sets the value(s) for <b>attachment</b> (A FHIR resource with content for this instance)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SeriesInstance setAttachment(ResourceReferenceDt theValue) {
		myAttachment = theValue;
		return this;
	}

  

	}





}
