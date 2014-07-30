















package ca.uhn.fhir.model.dstu.composite;

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

import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>AttachmentDt</b> Datatype
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


	@Child(name="contentType", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Mime type of the content, with charset etc.",
		formalDefinition="Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate"
	)
	private CodeDt myContentType;
	
	@Child(name="language", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Human language of the content (BCP-47)",
		formalDefinition="The human language of the content. The value can be any valid value according to BCP 47"
	)
	private CodeDt myLanguage;
	
	@Child(name="data", type=Base64BinaryDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Data inline, base64ed",
		formalDefinition="The actual data of the attachment - a sequence of bytes. In XML, represented using base64"
	)
	private Base64BinaryDt myData;
	
	@Child(name="url", type=UriDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Uri where the data can be found",
		formalDefinition="An alternative location where the data can be accessed"
	)
	private UriDt myUrl;
	
	@Child(name="size", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Number of bytes of content (if url provided)",
		formalDefinition="The number of bytes of data that make up this attachment."
	)
	private IntegerDt mySize;
	
	@Child(name="hash", type=Base64BinaryDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Hash of the data (sha-1, base64ed )",
		formalDefinition="The calculated hash of the data using SHA-1. Represented using base64"
	)
	private Base64BinaryDt myHash;
	
	@Child(name="title", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Label to display in place of the data",
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
	public AttachmentDt setContentType(CodeDt theValue) {
		myContentType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>contentType</b> (Mime type of the content, with charset etc.)
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
	public AttachmentDt setLanguage(CodeDt theValue) {
		myLanguage = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>language</b> (Human language of the content (BCP-47))
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
	public AttachmentDt setData(Base64BinaryDt theValue) {
		myData = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>data</b> (Data inline, base64ed)
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
	public AttachmentDt setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>url</b> (Uri where the data can be found)
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
	public AttachmentDt setSize(IntegerDt theValue) {
		mySize = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>size</b> (Number of bytes of content (if url provided))
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
	public AttachmentDt setHash(Base64BinaryDt theValue) {
		myHash = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>hash</b> (Hash of the data (sha-1, base64ed ))
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
	public AttachmentDt setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>title</b> (Label to display in place of the data)
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
