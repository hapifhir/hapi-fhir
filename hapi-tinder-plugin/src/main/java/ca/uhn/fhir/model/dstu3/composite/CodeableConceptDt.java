















package ca.uhn.fhir.model.dstu3.composite;

import ca.uhn.fhir.i18n.Msg;
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
 * ()
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
        extends  BaseIdentifiableElement         implements ICompositeDatatype{

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

	@Child(name="coding", type=CodingDt.class, order=0, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A reference to a code defined by a terminology system"
	)
	private java.util.List<CodingDt> myCoding;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>coding</b> ().
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
	 * Sets the value(s) for <b>coding</b> ()
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
	 * Adds and returns a new value for <b>coding</b> ()
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
	 * Adds a given new value for <b>coding</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * A reference to a code defined by a terminology system
	 * </p>
	 * @param theValue The coding to add (must not be <code>null</code>)
	 */
	public CodeableConceptDt addCoding(CodingDt theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(87) + "theValue must not be null");
		}
		getCoding().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>coding</b> (),
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
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> ()
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
	 * Sets the value for <b>text</b> ()
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