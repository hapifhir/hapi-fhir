















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>Attachment</b> Datatype
 * (Content in a format defined elsewhere)
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
@DatatypeDef(name="Attachment") 
public class AttachmentDt extends BaseElement implements ICompositeDatatype {

	@Child(name="contentType", type=CodeDt.class, order=0, min=1, max=1)	
	private CodeDt myContentType;
	
	@Child(name="language", type=CodeDt.class, order=1, min=0, max=1)	
	private CodeDt myLanguage;
	
	@Child(name="data", type=Base64BinaryDt.class, order=2, min=0, max=1)	
	private Base64BinaryDt myData;
	
	@Child(name="url", type=UriDt.class, order=3, min=0, max=1)	
	private UriDt myUrl;
	
	@Child(name="size", type=IntegerDt.class, order=4, min=0, max=1)	
	private IntegerDt mySize;
	
	@Child(name="hash", type=Base64BinaryDt.class, order=5, min=0, max=1)	
	private Base64BinaryDt myHash;
	
	@Child(name="title", type=StringDt.class, order=6, min=0, max=1)	
	private StringDt myTitle;
	
	/**
	 * Gets the value(s) for <b>contentType</b> (Mime type of the content, with charset etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate
     * </p> 
	 */
	public CodeDt getContentType() {  
		if (myContentType == null) {
			myContentType = new CodeDt();
		}
		return myContentType;
	}

	/**
	 * Sets the value(s) for <b>contentType</b> (Mime type of the content, with charset etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate
     * </p> 
	 */
	public void setContentType(CodeDt theValue) {
		myContentType = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>language</b> (Human language of the content (BCP-47)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The human language of the content. The value can be any valid value according to BCP 47
     * </p> 
	 */
	public CodeDt getLanguage() {  
		if (myLanguage == null) {
			myLanguage = new CodeDt();
		}
		return myLanguage;
	}

	/**
	 * Sets the value(s) for <b>language</b> (Human language of the content (BCP-47))
	 *
     * <p>
     * <b>Definition:</b>
     * The human language of the content. The value can be any valid value according to BCP 47
     * </p> 
	 */
	public void setLanguage(CodeDt theValue) {
		myLanguage = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>data</b> (Data inline, base64ed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64
     * </p> 
	 */
	public Base64BinaryDt getData() {  
		if (myData == null) {
			myData = new Base64BinaryDt();
		}
		return myData;
	}

	/**
	 * Sets the value(s) for <b>data</b> (Data inline, base64ed)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64
     * </p> 
	 */
	public void setData(Base64BinaryDt theValue) {
		myData = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>url</b> (Uri where the data can be found).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (Uri where the data can be found)
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public void setUrl(UriDt theValue) {
		myUrl = theValue;
	}

 	/**
	 * Sets the value(s) for <b>url</b> (Uri where the data can be found)
	 *
     * <p>
     * <b>Definition:</b>
     * An alternative location where the data can be accessed
     * </p> 
	 */
	public void setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
	}
 
	/**
	 * Gets the value(s) for <b>size</b> (Number of bytes of content (if url provided)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of bytes of data that make up this attachment.
     * </p> 
	 */
	public IntegerDt getSize() {  
		if (mySize == null) {
			mySize = new IntegerDt();
		}
		return mySize;
	}

	/**
	 * Sets the value(s) for <b>size</b> (Number of bytes of content (if url provided))
	 *
     * <p>
     * <b>Definition:</b>
     * The number of bytes of data that make up this attachment.
     * </p> 
	 */
	public void setSize(IntegerDt theValue) {
		mySize = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>hash</b> (Hash of the data (sha-1, base64ed )).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The calculated hash of the data using SHA-1. Represented using base64
     * </p> 
	 */
	public Base64BinaryDt getHash() {  
		if (myHash == null) {
			myHash = new Base64BinaryDt();
		}
		return myHash;
	}

	/**
	 * Sets the value(s) for <b>hash</b> (Hash of the data (sha-1, base64ed ))
	 *
     * <p>
     * <b>Definition:</b>
     * The calculated hash of the data using SHA-1. Represented using base64
     * </p> 
	 */
	public void setHash(Base64BinaryDt theValue) {
		myHash = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>title</b> (Label to display in place of the data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public StringDt getTitle() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	/**
	 * Sets the value(s) for <b>title</b> (Label to display in place of the data)
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public void setTitle(StringDt theValue) {
		myTitle = theValue;
	}

 	/**
	 * Sets the value(s) for <b>title</b> (Label to display in place of the data)
	 *
     * <p>
     * <b>Definition:</b>
     * A label or set of text to display in place of the data
     * </p> 
	 */
	public void setTitle( String theString) {
		myTitle = new StringDt(theString); 
	}
 



}