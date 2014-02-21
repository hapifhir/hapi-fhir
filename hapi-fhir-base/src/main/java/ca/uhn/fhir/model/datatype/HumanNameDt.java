

package ca.uhn.fhir.model.datatype;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.datatype.*;

/**
 * HAPI/FHIR <b>HumanName</b> Datatype
 * (Name of a human - parts and usage)
 *
 * <p>
 * <b>Definition:</b>
 * A human's name with the ability to identify parts and usage
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to be able to record names, along with notes about their use
 * </p> 
 */
@DatatypeDef(name="HumanName") 
public class HumanNameDt extends BaseCompositeDatatype {

	@Child(name="use", order=0, min=0, max=1)	
	private CodeDt myUse;
	
	@Child(name="text", order=1, min=0, max=1)	
	private StringDt myText;
	
	@Child(name="family", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myFamily;
	
	@Child(name="given", order=3, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myGiven;
	
	@Child(name="prefix", order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myPrefix;
	
	@Child(name="suffix", order=5, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> mySuffix;
	
	@Child(name="period", order=6, min=0, max=1)	
	private PeriodDt myPeriod;
	
	/**
	 * Gets the value(s) for use (usual | official | temp | nickname | anonymous | old | maiden)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public CodeDt getUse() {
		return myUse;
	}

	/**
	 * Sets the value(s) for use (usual | official | temp | nickname | anonymous | old | maiden)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public void setUse(CodeDt theValue) {
		myUse = theValue;
	}
	
	/**
	 * Gets the value(s) for text (Text representation of the full name)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public StringDt getText() {
		return myText;
	}

	/**
	 * Sets the value(s) for text (Text representation of the full name)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public void setText(StringDt theValue) {
		myText = theValue;
	}
	
	/**
	 * Gets the value(s) for family (Family name (often called 'Surname'))
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public List<StringDt> getFamily() {
		return myFamily;
	}

	/**
	 * Sets the value(s) for family (Family name (often called 'Surname'))
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public void setFamily(List<StringDt> theValue) {
		myFamily = theValue;
	}
	
	/**
	 * Gets the value(s) for given (Given names (not always 'first'). Includes middle names)
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public List<StringDt> getGiven() {
		return myGiven;
	}

	/**
	 * Sets the value(s) for given (Given names (not always 'first'). Includes middle names)
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public void setGiven(List<StringDt> theValue) {
		myGiven = theValue;
	}
	
	/**
	 * Gets the value(s) for prefix (Parts that come before the name)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public List<StringDt> getPrefix() {
		return myPrefix;
	}

	/**
	 * Sets the value(s) for prefix (Parts that come before the name)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public void setPrefix(List<StringDt> theValue) {
		myPrefix = theValue;
	}
	
	/**
	 * Gets the value(s) for suffix (Parts that come after the name)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public List<StringDt> getSuffix() {
		return mySuffix;
	}

	/**
	 * Sets the value(s) for suffix (Parts that come after the name)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public void setSuffix(List<StringDt> theValue) {
		mySuffix = theValue;
	}
	
	/**
	 * Gets the value(s) for period (Time period when name was/is in use)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time when this name was valid for the named person.
     * </p> 
	 */
	public PeriodDt getPeriod() {
		return myPeriod;
	}

	/**
	 * Sets the value(s) for period (Time period when name was/is in use)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time when this name was valid for the named person.
     * </p> 
	 */
	public void setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
	}
	

}