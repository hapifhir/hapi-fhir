















package ca.uhn.fhir.model.dstu.resource;


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
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.MediaTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Media</b> Resource
 * (A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference)
 *
 * <p>
 * <b>Definition:</b>
 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Media">http://hl7.org/fhir/profiles/Media</a> 
 * </p>
 *
 */
@ResourceDef(name="Media", profile="http://hl7.org/fhir/profiles/Media", id="media")
public class Media extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Media.type", description="", type="token")
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.type</b><br/>
	 * </p>
	 */
	public static final TokenParam TYPE = new TokenParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>subtype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.subtype</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subtype", path="Media.subtype", description="", type="token")
	public static final String SP_SUBTYPE = "subtype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subtype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.subtype</b><br/>
	 * </p>
	 */
	public static final TokenParam SUBTYPE = new TokenParam(SP_SUBTYPE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Media.identifier", description="", type="token")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.identifier</b><br/>
	 * </p>
	 */
	public static final TokenParam IDENTIFIER = new TokenParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Media.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Media.dateTime", description="", type="date")
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Media.dateTime</b><br/>
	 * </p>
	 */
	public static final DateParam DATE = new DateParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Media.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Media.subject", description="", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Media.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Media.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Media.subject");

	/**
	 * Search parameter constant for <b>operator</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Media.operator</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="operator", path="Media.operator", description="", type="reference")
	public static final String SP_OPERATOR = "operator";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>operator</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Media.operator</b><br/>
	 * </p>
	 */
	public static final ReferenceParam OPERATOR = new ReferenceParam(SP_OPERATOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Media.operator</b>".
	 */
	public static final Include INCLUDE_OPERATOR = new Include("Media.operator");

	/**
	 * Search parameter constant for <b>view</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.view</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="view", path="Media.view", description="", type="token")
	public static final String SP_VIEW = "view";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>view</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Media.view</b><br/>
	 * </p>
	 */
	public static final TokenParam VIEW = new TokenParam(SP_VIEW);


	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="photo | video | audio",
		formalDefinition="Whether the media is a photo (still image), an audio recording, or a video recording"
	)
	private BoundCodeDt<MediaTypeEnum> myType;
	
	@Child(name="subtype", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The type of acquisition equipment/process",
		formalDefinition="Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality"
	)
	private CodeableConceptDt mySubtype;
	
	@Child(name="identifier", type=IdentifierDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Identifier(s) for the image",
		formalDefinition="Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="dateTime", type=DateTimeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="When the media was taken/recorded (end)",
		formalDefinition="When the media was originally recorded. For video and audio, if the length of the recording is not insignificant, this is the end of the recording"
	)
	private DateTimeDt myDateTime;
	
	@Child(name="subject", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Specimen.class	})
	@Description(
		shortDefinition="Who/What this Media is a record of",
		formalDefinition="Who/What this Media is a record of"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="operator", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="The person who generated the image",
		formalDefinition="The person who administered the collection of the image"
	)
	private ResourceReferenceDt myOperator;
	
	@Child(name="view", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Imaging view e.g Lateral or Antero-posterior",
		formalDefinition="The name of the imaging view e.g Lateral or Antero-posterior (AP)."
	)
	private CodeableConceptDt myView;
	
	@Child(name="deviceName", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Name of the device/manufacturer",
		formalDefinition="The name of the device / manufacturer of the device  that was used to make the recording"
	)
	private StringDt myDeviceName;
	
	@Child(name="height", type=IntegerDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Height of the image in pixels(photo/video)",
		formalDefinition="Height of the image in pixels(photo/video)"
	)
	private IntegerDt myHeight;
	
	@Child(name="width", type=IntegerDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Width of the image in pixels (photo/video)",
		formalDefinition="Width of the image in pixels (photo/video)"
	)
	private IntegerDt myWidth;
	
	@Child(name="frames", type=IntegerDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Number of frames if > 1 (photo)",
		formalDefinition="The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required"
	)
	private IntegerDt myFrames;
	
	@Child(name="length", type=IntegerDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Length in seconds (audio / video)",
		formalDefinition="The length of the recording in seconds - for audio and video"
	)
	private IntegerDt myLength;
	
	@Child(name="content", type=AttachmentDt.class, order=12, min=1, max=1)	
	@Description(
		shortDefinition="Actual Media - reference or data",
		formalDefinition="The actual content of the media - inline or by direct reference to the media source file"
	)
	private AttachmentDt myContent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySubtype,  myIdentifier,  myDateTime,  mySubject,  myOperator,  myView,  myDeviceName,  myHeight,  myWidth,  myFrames,  myLength,  myContent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySubtype, myIdentifier, myDateTime, mySubject, myOperator, myView, myDeviceName, myHeight, myWidth, myFrames, myLength, myContent);
	}

	/**
	 * Gets the value(s) for <b>type</b> (photo | video | audio).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the media is a photo (still image), an audio recording, or a video recording
     * </p> 
	 */
	public BoundCodeDt<MediaTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<MediaTypeEnum>(MediaTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (photo | video | audio)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the media is a photo (still image), an audio recording, or a video recording
     * </p> 
	 */
	public Media setType(BoundCodeDt<MediaTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (photo | video | audio)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the media is a photo (still image), an audio recording, or a video recording
     * </p> 
	 */
	public Media setType(MediaTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subtype</b> (The type of acquisition equipment/process).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality
     * </p> 
	 */
	public CodeableConceptDt getSubtype() {  
		if (mySubtype == null) {
			mySubtype = new CodeableConceptDt();
		}
		return mySubtype;
	}

	/**
	 * Sets the value(s) for <b>subtype</b> (The type of acquisition equipment/process)
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality
     * </p> 
	 */
	public Media setSubtype(CodeableConceptDt theValue) {
		mySubtype = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier(s) for the image).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier(s) for the image)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers
     * </p> 
	 */
	public Media setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Identifier(s) for the image)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Identifier(s) for the image),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Identifier(s) for the image)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Media addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Identifier(s) for the image)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Media addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dateTime</b> (When the media was taken/recorded (end)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the media was originally recorded. For video and audio, if the length of the recording is not insignificant, this is the end of the recording
     * </p> 
	 */
	public DateTimeDt getDateTime() {  
		if (myDateTime == null) {
			myDateTime = new DateTimeDt();
		}
		return myDateTime;
	}

	/**
	 * Sets the value(s) for <b>dateTime</b> (When the media was taken/recorded (end))
	 *
     * <p>
     * <b>Definition:</b>
     * When the media was originally recorded. For video and audio, if the length of the recording is not insignificant, this is the end of the recording
     * </p> 
	 */
	public Media setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dateTime</b> (When the media was taken/recorded (end))
	 *
     * <p>
     * <b>Definition:</b>
     * When the media was originally recorded. For video and audio, if the length of the recording is not insignificant, this is the end of the recording
     * </p> 
	 */
	public Media setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> (When the media was taken/recorded (end))
	 *
     * <p>
     * <b>Definition:</b>
     * When the media was originally recorded. For video and audio, if the length of the recording is not insignificant, this is the end of the recording
     * </p> 
	 */
	public Media setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who/What this Media is a record of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who/What this Media is a record of
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who/What this Media is a record of)
	 *
     * <p>
     * <b>Definition:</b>
     * Who/What this Media is a record of
     * </p> 
	 */
	public Media setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>operator</b> (The person who generated the image).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who administered the collection of the image
     * </p> 
	 */
	public ResourceReferenceDt getOperator() {  
		if (myOperator == null) {
			myOperator = new ResourceReferenceDt();
		}
		return myOperator;
	}

	/**
	 * Sets the value(s) for <b>operator</b> (The person who generated the image)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who administered the collection of the image
     * </p> 
	 */
	public Media setOperator(ResourceReferenceDt theValue) {
		myOperator = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>view</b> (Imaging view e.g Lateral or Antero-posterior).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the imaging view e.g Lateral or Antero-posterior (AP).
     * </p> 
	 */
	public CodeableConceptDt getView() {  
		if (myView == null) {
			myView = new CodeableConceptDt();
		}
		return myView;
	}

	/**
	 * Sets the value(s) for <b>view</b> (Imaging view e.g Lateral or Antero-posterior)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the imaging view e.g Lateral or Antero-posterior (AP).
     * </p> 
	 */
	public Media setView(CodeableConceptDt theValue) {
		myView = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>deviceName</b> (Name of the device/manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the device / manufacturer of the device  that was used to make the recording
     * </p> 
	 */
	public StringDt getDeviceName() {  
		if (myDeviceName == null) {
			myDeviceName = new StringDt();
		}
		return myDeviceName;
	}

	/**
	 * Sets the value(s) for <b>deviceName</b> (Name of the device/manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the device / manufacturer of the device  that was used to make the recording
     * </p> 
	 */
	public Media setDeviceName(StringDt theValue) {
		myDeviceName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>deviceName</b> (Name of the device/manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the device / manufacturer of the device  that was used to make the recording
     * </p> 
	 */
	public Media setDeviceName( String theString) {
		myDeviceName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>height</b> (Height of the image in pixels(photo/video)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Height of the image in pixels(photo/video)
     * </p> 
	 */
	public IntegerDt getHeight() {  
		if (myHeight == null) {
			myHeight = new IntegerDt();
		}
		return myHeight;
	}

	/**
	 * Sets the value(s) for <b>height</b> (Height of the image in pixels(photo/video))
	 *
     * <p>
     * <b>Definition:</b>
     * Height of the image in pixels(photo/video)
     * </p> 
	 */
	public Media setHeight(IntegerDt theValue) {
		myHeight = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>height</b> (Height of the image in pixels(photo/video))
	 *
     * <p>
     * <b>Definition:</b>
     * Height of the image in pixels(photo/video)
     * </p> 
	 */
	public Media setHeight( int theInteger) {
		myHeight = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>width</b> (Width of the image in pixels (photo/video)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Width of the image in pixels (photo/video)
     * </p> 
	 */
	public IntegerDt getWidth() {  
		if (myWidth == null) {
			myWidth = new IntegerDt();
		}
		return myWidth;
	}

	/**
	 * Sets the value(s) for <b>width</b> (Width of the image in pixels (photo/video))
	 *
     * <p>
     * <b>Definition:</b>
     * Width of the image in pixels (photo/video)
     * </p> 
	 */
	public Media setWidth(IntegerDt theValue) {
		myWidth = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>width</b> (Width of the image in pixels (photo/video))
	 *
     * <p>
     * <b>Definition:</b>
     * Width of the image in pixels (photo/video)
     * </p> 
	 */
	public Media setWidth( int theInteger) {
		myWidth = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>frames</b> (Number of frames if > 1 (photo)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required
     * </p> 
	 */
	public IntegerDt getFrames() {  
		if (myFrames == null) {
			myFrames = new IntegerDt();
		}
		return myFrames;
	}

	/**
	 * Sets the value(s) for <b>frames</b> (Number of frames if > 1 (photo))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required
     * </p> 
	 */
	public Media setFrames(IntegerDt theValue) {
		myFrames = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>frames</b> (Number of frames if > 1 (photo))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required
     * </p> 
	 */
	public Media setFrames( int theInteger) {
		myFrames = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>length</b> (Length in seconds (audio / video)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The length of the recording in seconds - for audio and video
     * </p> 
	 */
	public IntegerDt getLength() {  
		if (myLength == null) {
			myLength = new IntegerDt();
		}
		return myLength;
	}

	/**
	 * Sets the value(s) for <b>length</b> (Length in seconds (audio / video))
	 *
     * <p>
     * <b>Definition:</b>
     * The length of the recording in seconds - for audio and video
     * </p> 
	 */
	public Media setLength(IntegerDt theValue) {
		myLength = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>length</b> (Length in seconds (audio / video))
	 *
     * <p>
     * <b>Definition:</b>
     * The length of the recording in seconds - for audio and video
     * </p> 
	 */
	public Media setLength( int theInteger) {
		myLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>content</b> (Actual Media - reference or data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual content of the media - inline or by direct reference to the media source file
     * </p> 
	 */
	public AttachmentDt getContent() {  
		if (myContent == null) {
			myContent = new AttachmentDt();
		}
		return myContent;
	}

	/**
	 * Sets the value(s) for <b>content</b> (Actual Media - reference or data)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual content of the media - inline or by direct reference to the media source file
     * </p> 
	 */
	public Media setContent(AttachmentDt theValue) {
		myContent = theValue;
		return this;
	}

  


}