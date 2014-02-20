package ca.uhn.fhir.model.datatype;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.datatype.*;

/**
 * HAPI/FHIR <b>${resourceName}</b> Datatype
 * (A postal address)
 *
 * <p>
 * <b>Definition:</b>
 * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * A postal address
 * </p> 
 */
@DatatypeDef(name="Address") 
public class AddressDt extends BaseCompositeDatatype {

	@Child(name="use", order=0, min=0, max=1)	
	private CodeDt myUse;
	
	@Child(name="text", order=1, min=0, max=1)	
	private String myText;
	
	@Child(name="line", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<String> myLine;
	
	@Child(name="city", order=3, min=0, max=1)	
	private String myCity;
	
	@Child(name="state", order=4, min=0, max=1)	
	private String myState;
	
	@Child(name="zip", order=5, min=0, max=1)	
	private String myZip;
	
	@Child(name="country", order=6, min=0, max=1)	
	private String myCountry;
	
	@Child(name="period", order=7, min=0, max=1)	
	private Period myPeriod;
	
	/**
	 * Gets the value(s) for use (home | work | temp | old - purpose of this address)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this address
     * </p> 
	 */
	public CodeDt getUse() {
		return myUse;
	}

	/**
	 * Sets the value(s) for use (home | work | temp | old - purpose of this address)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this address
     * </p> 
	 */
	public void setUse(CodeDt theValue) {
		myUse = theValue;
	}
	
	/**
	 * Gets the value(s) for text (Text representation of the address)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the address
     * </p> 
	 */
	public String getText() {
		return myText;
	}

	/**
	 * Sets the value(s) for text (Text representation of the address)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the address
     * </p> 
	 */
	public void setText(String theValue) {
		myText = theValue;
	}
	
	/**
	 * Gets the value(s) for line (Street name, number, direction & P.O. Box etc )
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, 
P.O. Box number, delivery hints, and similar address information
     * </p> 
	 */
	public List<String> getLine() {
		return myLine;
	}

	/**
	 * Sets the value(s) for line (Street name, number, direction & P.O. Box etc )
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, 
P.O. Box number, delivery hints, and similar address information
     * </p> 
	 */
	public void setLine(List<String> theValue) {
		myLine = theValue;
	}
	
	/**
	 * Gets the value(s) for city (Name of city, town etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the city, town, village or other community or delivery center.
     * </p> 
	 */
	public String getCity() {
		return myCity;
	}

	/**
	 * Sets the value(s) for city (Name of city, town etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the city, town, village or other community or delivery center.
     * </p> 
	 */
	public void setCity(String theValue) {
		myCity = theValue;
	}
	
	/**
	 * Gets the value(s) for state (Sub-unit of country (abreviations ok))
	 *
     * <p>
     * <b>Definition:</b>
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     * </p> 
	 */
	public String getState() {
		return myState;
	}

	/**
	 * Sets the value(s) for state (Sub-unit of country (abreviations ok))
	 *
     * <p>
     * <b>Definition:</b>
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     * </p> 
	 */
	public void setState(String theValue) {
		myState = theValue;
	}
	
	/**
	 * Gets the value(s) for zip (Postal code for area)
	 *
     * <p>
     * <b>Definition:</b>
     * A postal code designating a region defined by the postal service.
     * </p> 
	 */
	public String getZip() {
		return myZip;
	}

	/**
	 * Sets the value(s) for zip (Postal code for area)
	 *
     * <p>
     * <b>Definition:</b>
     * A postal code designating a region defined by the postal service.
     * </p> 
	 */
	public void setZip(String theValue) {
		myZip = theValue;
	}
	
	/**
	 * Gets the value(s) for country (Country (can be ISO 3166 3 letter code))
	 *
     * <p>
     * <b>Definition:</b>
     * Country - a nation as commonly understood or generally accepted
     * </p> 
	 */
	public String getCountry() {
		return myCountry;
	}

	/**
	 * Sets the value(s) for country (Country (can be ISO 3166 3 letter code))
	 *
     * <p>
     * <b>Definition:</b>
     * Country - a nation as commonly understood or generally accepted
     * </p> 
	 */
	public void setCountry(String theValue) {
		myCountry = theValue;
	}
	
	/**
	 * Gets the value(s) for period (Time period when address was/is in use)
	 *
     * <p>
     * <b>Definition:</b>
     * Time period when address was/is in use
     * </p> 
	 */
	public Period getPeriod() {
		return myPeriod;
	}

	/**
	 * Sets the value(s) for period (Time period when address was/is in use)
	 *
     * <p>
     * <b>Definition:</b>
     * Time period when address was/is in use
     * </p> 
	 */
	public void setPeriod(Period theValue) {
		myPeriod = theValue;
	}
	

}