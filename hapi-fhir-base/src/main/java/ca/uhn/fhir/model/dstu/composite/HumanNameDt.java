















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;

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

	@Child(name="use", type=CodeDt.class, order=0, min=0, max=1)	
	private CodeDt myUse;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myText;
	
	@Child(name="family", type=StringDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myFamily;
	
	@Child(name="given", type=StringDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myGiven;
	
	@Child(name="prefix", type=StringDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myPrefix;
	
	@Child(name="suffix", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> mySuffix;
	
	@Child(name="period", type=PeriodDt.class, order=6, min=0, max=1)	
	private PeriodDt myPeriod;
	
	/**
	 * Gets the value(s) for <b>use</b> (usual | official | temp | nickname | anonymous | old | maiden).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public CodeDt getUse() {  
		if (myUse == null) {
			myUse = new CodeDt();
		}
		return myUse;
	}

	/**
	 * Sets the value(s) for <b>use</b> (usual | official | temp | nickname | anonymous | old | maiden)
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
	 * Gets the value(s) for <b>text</b> (Text representation of the full name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public StringDt getText() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	/**
	 * Sets the value(s) for <b>text</b> (Text representation of the full name)
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
	 * Sets the value(s) for <b>text</b> (Text representation of the full name)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public void setText( String theString) {
		myText = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>family</b> (Family name (often called 'Surname')).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public List<StringDt> getFamily() {  
		if (myFamily == null) {
			myFamily = new ArrayList<StringDt>();
		}
		return myFamily;
	}

	/**
	 * Sets the value(s) for <b>family</b> (Family name (often called 'Surname'))
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
	 * Sets the value(s) for <b>family</b> (Family name (often called 'Surname'))
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public void addFamily( String theString) {
		if (myFamily == null) {
			myFamily = new ArrayList<StringDt>();
		}
		myFamily.add(new StringDt(theString)); 
	}
 
	/**
	 * Gets the value(s) for <b>given</b> (Given names (not always 'first'). Includes middle names).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public List<StringDt> getGiven() {  
		if (myGiven == null) {
			myGiven = new ArrayList<StringDt>();
		}
		return myGiven;
	}

	/**
	 * Sets the value(s) for <b>given</b> (Given names (not always 'first'). Includes middle names)
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
	 * Sets the value(s) for <b>given</b> (Given names (not always 'first'). Includes middle names)
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public void addGiven( String theString) {
		if (myGiven == null) {
			myGiven = new ArrayList<StringDt>();
		}
		myGiven.add(new StringDt(theString)); 
	}
 
	/**
	 * Gets the value(s) for <b>prefix</b> (Parts that come before the name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public List<StringDt> getPrefix() {  
		if (myPrefix == null) {
			myPrefix = new ArrayList<StringDt>();
		}
		return myPrefix;
	}

	/**
	 * Sets the value(s) for <b>prefix</b> (Parts that come before the name)
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
	 * Sets the value(s) for <b>prefix</b> (Parts that come before the name)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public void addPrefix( String theString) {
		if (myPrefix == null) {
			myPrefix = new ArrayList<StringDt>();
		}
		myPrefix.add(new StringDt(theString)); 
	}
 
	/**
	 * Gets the value(s) for <b>suffix</b> (Parts that come after the name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public List<StringDt> getSuffix() {  
		if (mySuffix == null) {
			mySuffix = new ArrayList<StringDt>();
		}
		return mySuffix;
	}

	/**
	 * Sets the value(s) for <b>suffix</b> (Parts that come after the name)
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
	 * Sets the value(s) for <b>suffix</b> (Parts that come after the name)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public void addSuffix( String theString) {
		if (mySuffix == null) {
			mySuffix = new ArrayList<StringDt>();
		}
		mySuffix.add(new StringDt(theString)); 
	}
 
	/**
	 * Gets the value(s) for <b>period</b> (Time period when name was/is in use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time when this name was valid for the named person.
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Time period when name was/is in use)
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