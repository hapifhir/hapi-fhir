















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
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * HAPI/FHIR <b>CodeableConceptDt</b> Datatype
 * (Concept - reference to a terminology or just  text)
 *
 * <p>
 * <b>Definition:</b>
 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * This is a common pattern in healthcare - a concept that may be defined by one or more codes from formal definitions including LOINC and SNOMED CT, and/or defined by the provision of text that captures a human sense of the concept
 * </p> 
 */
@DatatypeDef(name="CodeableConceptDt") 
public class CodeableConceptDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public CodeableConceptDt() {
		// nothing
	}

	/**
	 * Constructor which creates a CodeableConceptDt with one coding repetition, containing
	 * the given system and code
	 */
	public CodeableConceptDt(String theSystem, String theCode) {
		addCoding().setSystem(theSystem).setCode(theCode);
	}

	@Child(name="coding", type=CodingDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Code defined by a terminology system",
		formalDefinition="A reference to a code defined by a terminology system"
	)
	private java.util.List<CodingDt> myCoding;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Plain text representation of the concept",
		formalDefinition="A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user"
	)
	private StringDt myText;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCoding,  myText);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCoding, myText);
	}

	/**
	 * Gets the value(s) for <b>coding</b> (Code defined by a terminology system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public java.util.List<CodingDt> getCoding() {  
		if (myCoding == null) {
			myCoding = new java.util.ArrayList<CodingDt>();
		}
		return myCoding;
	}

	/**
	 * Sets the value(s) for <b>coding</b> (Code defined by a terminology system)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public CodeableConceptDt setCoding(java.util.List<CodingDt> theValue) {
		myCoding = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>coding</b> (Code defined by a terminology system)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public CodingDt addCoding() {
		CodingDt newType = new CodingDt();
		getCoding().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>coding</b> (Code defined by a terminology system),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public CodingDt getCodingFirstRep() {
		if (getCoding().isEmpty()) {
			return addCoding();
		}
		return getCoding().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>text</b> (Plain text representation of the concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public StringDt getText() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	/**
	 * Sets the value(s) for <b>text</b> (Plain text representation of the concept)
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public CodeableConceptDt setText(StringDt theValue) {
		myText = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>text</b> (Plain text representation of the concept)
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public CodeableConceptDt setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 


}
