















package ca.uhn.fhir.model.dev.composite;

import java.net.URI;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;

import ca.uhn.fhir.model.dev.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dev.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dev.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dev.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dev.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dev.valueset.NameUseEnum;
import ca.uhn.fhir.model.dev.resource.Organization;
import ca.uhn.fhir.model.dev.composite.PeriodDt;
import ca.uhn.fhir.model.dev.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dev.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dev.composite.QuantityDt;
import ca.uhn.fhir.model.dev.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dev.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dev.resource.ValueSet;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>AttachmentDt</b> Datatype
 * (Attachment)
 *
 * <p>
 * <b>Definition:</b>
 * For referring to data content defined in other formats.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Many models need to include data defined in other specifications that is complex and opaque to the healthcare model. This includes documents, media recordings, structured data, etc.
 * </p> 
 */
@DatatypeDef(name="AttachmentDt") 
public class AttachmentDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public AttachmentDt() {
		// nothing
	}


	@Child(name="contentType", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.contentType",
		formalDefinition="Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate"
	)
	private CodeDt myContentType;
	
	@Child(name="language", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.language",
		formalDefinition="The human language of the content. The value can be any valid value according to BCP 47"
	)
	private CodeDt myLanguage;
	
	@Child(name="data", type=Base64BinaryDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.data",
		formalDefinition="The actual data of the attachment - a sequence of bytes. In XML, represented using base64"
	)
	private Base64BinaryDt myData;
	
	@Child(name="url", type=UriDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.url",
		formalDefinition="An alternative location where the data can be accessed"
	)
	private UriDt myUrl;
	
	@Child(name="size", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.size",
		formalDefinition="The number of bytes of data that make up this attachment."
	)
	private IntegerDt mySize;
	
	@Child(name="hash", type=Base64BinaryDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.hash",
		formalDefinition="The calculated hash of the data using SHA-1. Represented using base64"
	)
	private Base64BinaryDt myHash;
	
	@Child(name="title", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Attachment.title",
		formalDefinition="A label or set of text to display in place of the data"
	)
	private StringDt myTitle;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myContentType,  myLanguage,  myData,  myUrl,  mySize,  myHash,  myTitle);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myContentType, myLanguage, myData, myUrl, mySize, myHash, myTitle);
	}

	/**
	 * Gets the value(s) for <b>contentType</b> (Attachment.contentType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate
     * </p> 
	 */
	public CodeDt getContentTypeElement() {  
		if (myContentType == null) {
			myContentType = new CodeDt();
		}
		return myContentType;
	}

	
	/**
	 * Gets the value(s) for <b>contentType</b> (Attachment.contentType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate
     * </p> 
	 */
	public String getContentType() {  
		return getContentTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>contentType</b> (Attachment.contentType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate
     * </p> 
	 */
	public AttachmentDt setContentType(CodeDt theValue) {
		myContentType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>contentType</b> (Attachment.contentType)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate
     * </p> 
	 */
	public AttachmentDt setContentType( String theCode) {
		myContentType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>language</b> (Attachment.language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The human language of the content. The value can be any valid value according to BCP 47
     * </p> 
	 */
	public CodeDt getLanguageElement() {  
		if (myLanguage == null) {
			myLanguage = new CodeDt();
		}
		return myLanguage;
	}

	
	/**
	 * Gets the value(s) for <b>language</b> (Attachment.language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The human language of the content. The value can be any valid value according to BCP 47
     * </p> 
	 */
	public String getLanguage() {  
		return getLanguageElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>language</b> (Attachment.language)
	 *
     * <p>
     * <b>Definition:</b>
     * The human language of the content. The value can be any valid value according to BCP 47
     * </p> 
	 */
	public AttachmentDt setLanguage(CodeDt theValue) {
		myLanguage = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>language</b> (Attachment.language)
	 *
     * <p>
     * <b>Definition:</b>
     * The human language of the content. The value can be any valid value according to BCP 47
     * </p> 
	 */
	public AttachmentDt setLanguage( String theCode) {
		myLanguage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>data</b> (Attachment.data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64
     * </p> 
	 */
	public Base64BinaryDt getDataElement() {  
		if (myData == null) {
			myData = new Base64BinaryDt();
		}
		return myData;
	}

	
	/**
	 * Gets the value(s) for <b>data</b> (Attachment.data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64
     * </p> 
	 */
	public byte[] getData() {  
		return getDataElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>data</b> (Attachment.data)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64
     * </p> 
	 */
	public AttachmentDt setData(Base64BinaryDt theValue) {
		myData = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>data</b> (Attachment.data)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64
     * </p> 
	 */
	public AttachmentDt setData( byte[] theBytes) {
		myData = new Base64BinaryDt(theBytes); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>url</b> (Attachment.url).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> (Attachment.url).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> (Attachment.url)
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public AttachmentDt setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> (Attachment.url)
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public AttachmentDt setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>size</b> (Attachment.size).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of bytes of data that make up this attachment.
     * </p> 
	 */
	public IntegerDt getSizeElement() {  
		if (mySize == null) {
			mySize = new IntegerDt();
		}
		return mySize;
	}

	
	/**
	 * Gets the value(s) for <b>size</b> (Attachment.size).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of bytes of data that make up this attachment.
     * </p> 
	 */
	public Integer getSize() {  
		return getSizeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>size</b> (Attachment.size)
	 *
     * <p>
     * <b>Definition:</b>
     * The number of bytes of data that make up this attachment.
     * </p> 
	 */
	public AttachmentDt setSize(IntegerDt theValue) {
		mySize = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>size</b> (Attachment.size)
	 *
     * <p>
     * <b>Definition:</b>
     * The number of bytes of data that make up this attachment.
     * </p> 
	 */
	public AttachmentDt setSize( int theInteger) {
		mySize = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>hash</b> (Attachment.hash).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The calculated hash of the data using SHA-1. Represented using base64
     * </p> 
	 */
	public Base64BinaryDt getHashElement() {  
		if (myHash == null) {
			myHash = new Base64BinaryDt();
		}
		return myHash;
	}

	
	/**
	 * Gets the value(s) for <b>hash</b> (Attachment.hash).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The calculated hash of the data using SHA-1. Represented using base64
     * </p> 
	 */
	public byte[] getHash() {  
		return getHashElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>hash</b> (Attachment.hash)
	 *
     * <p>
     * <b>Definition:</b>
     * The calculated hash of the data using SHA-1. Represented using base64
     * </p> 
	 */
	public AttachmentDt setHash(Base64BinaryDt theValue) {
		myHash = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>hash</b> (Attachment.hash)
	 *
     * <p>
     * <b>Definition:</b>
     * The calculated hash of the data using SHA-1. Represented using base64
     * </p> 
	 */
	public AttachmentDt setHash( byte[] theBytes) {
		myHash = new Base64BinaryDt(theBytes); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>title</b> (Attachment.title).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public StringDt getTitleElement() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	
	/**
	 * Gets the value(s) for <b>title</b> (Attachment.title).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public String getTitle() {  
		return getTitleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>title</b> (Attachment.title)
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public AttachmentDt setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>title</b> (Attachment.title)
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public AttachmentDt setTitle( String theString) {
		myTitle = new StringDt(theString); 
		return this; 
	}

 


}