















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
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * HAPI/FHIR <b>AddressDt</b> Datatype
 * (A postal address)
 *
 * <p>
 * <b>Definition:</b>
 * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to be able to record postal addresses, along with notes about their use
 * </p> 
 */
@DatatypeDef(name="AddressDt") 
public class AddressDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public AddressDt() {
		// nothing
	}


	@Child(name="use", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="home | work | temp | old - purpose of this address",
		formalDefinition="The purpose of this address"
	)
	private BoundCodeDt<AddressUseEnum> myUse;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Text representation of the address",
		formalDefinition="A full text representation of the address"
	)
	private StringDt myText;
	
	@Child(name="line", type=StringDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Street name, number, direction & P.O. Box etc",
		formalDefinition="This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery hints, and similar address information"
	)
	private java.util.List<StringDt> myLine;
	
	@Child(name="city", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Name of city, town etc.",
		formalDefinition="The name of the city, town, village or other community or delivery center."
	)
	private StringDt myCity;
	
	@Child(name="state", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Sub-unit of country (abreviations ok)",
		formalDefinition="Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes)."
	)
	private StringDt myState;
	
	@Child(name="zip", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Postal code for area",
		formalDefinition="A postal code designating a region defined by the postal service."
	)
	private StringDt myZip;
	
	@Child(name="country", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Country (can be ISO 3166 3 letter code)",
		formalDefinition="Country - a nation as commonly understood or generally accepted"
	)
	private StringDt myCountry;
	
	@Child(name="period", type=PeriodDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Time period when address was/is in use",
		formalDefinition="Time period when address was/is in use"
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUse,  myText,  myLine,  myCity,  myState,  myZip,  myCountry,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUse, myText, myLine, myCity, myState, myZip, myCountry, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>use</b> (home | work | temp | old - purpose of this address).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this address
     * </p> 
	 */
	public BoundCodeDt<AddressUseEnum> getUse() {  
		if (myUse == null) {
			myUse = new BoundCodeDt<AddressUseEnum>(AddressUseEnum.VALUESET_BINDER);
		}
		return myUse;
	}

	/**
	 * Sets the value(s) for <b>use</b> (home | work | temp | old - purpose of this address)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this address
     * </p> 
	 */
	public AddressDt setUse(BoundCodeDt<AddressUseEnum> theValue) {
		myUse = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>use</b> (home | work | temp | old - purpose of this address)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this address
     * </p> 
	 */
	public AddressDt setUse(AddressUseEnum theValue) {
		getUse().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>text</b> (Text representation of the address).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the address
     * </p> 
	 */
	public StringDt getText() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	/**
	 * Sets the value(s) for <b>text</b> (Text representation of the address)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the address
     * </p> 
	 */
	public AddressDt setText(StringDt theValue) {
		myText = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>text</b> (Text representation of the address)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the address
     * </p> 
	 */
	public AddressDt setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>line</b> (Street name, number, direction & P.O. Box etc).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery hints, and similar address information
     * </p> 
	 */
	public java.util.List<StringDt> getLine() {  
		if (myLine == null) {
			myLine = new java.util.ArrayList<StringDt>();
		}
		return myLine;
	}

	/**
	 * Sets the value(s) for <b>line</b> (Street name, number, direction & P.O. Box etc)
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery hints, and similar address information
     * </p> 
	 */
	public AddressDt setLine(java.util.List<StringDt> theValue) {
		myLine = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>line</b> (Street name, number, direction & P.O. Box etc)
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery hints, and similar address information
     * </p> 
	 */
	public StringDt addLine() {
		StringDt newType = new StringDt();
		getLine().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>line</b> (Street name, number, direction & P.O. Box etc),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery hints, and similar address information
     * </p> 
	 */
	public StringDt getLineFirstRep() {
		if (getLine().isEmpty()) {
			return addLine();
		}
		return getLine().get(0); 
	}
 	/**
	 * Adds a new value for <b>line</b> (Street name, number, direction & P.O. Box etc)
	 *
     * <p>
     * <b>Definition:</b>
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery hints, and similar address information
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AddressDt addLine( String theString) {
		if (myLine == null) {
			myLine = new java.util.ArrayList<StringDt>();
		}
		myLine.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>city</b> (Name of city, town etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the city, town, village or other community or delivery center.
     * </p> 
	 */
	public StringDt getCity() {  
		if (myCity == null) {
			myCity = new StringDt();
		}
		return myCity;
	}

	/**
	 * Sets the value(s) for <b>city</b> (Name of city, town etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the city, town, village or other community or delivery center.
     * </p> 
	 */
	public AddressDt setCity(StringDt theValue) {
		myCity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>city</b> (Name of city, town etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the city, town, village or other community or delivery center.
     * </p> 
	 */
	public AddressDt setCity( String theString) {
		myCity = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>state</b> (Sub-unit of country (abreviations ok)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     * </p> 
	 */
	public StringDt getState() {  
		if (myState == null) {
			myState = new StringDt();
		}
		return myState;
	}

	/**
	 * Sets the value(s) for <b>state</b> (Sub-unit of country (abreviations ok))
	 *
     * <p>
     * <b>Definition:</b>
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     * </p> 
	 */
	public AddressDt setState(StringDt theValue) {
		myState = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>state</b> (Sub-unit of country (abreviations ok))
	 *
     * <p>
     * <b>Definition:</b>
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in common use (i.e. US 2 letter state codes).
     * </p> 
	 */
	public AddressDt setState( String theString) {
		myState = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>zip</b> (Postal code for area).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A postal code designating a region defined by the postal service.
     * </p> 
	 */
	public StringDt getZip() {  
		if (myZip == null) {
			myZip = new StringDt();
		}
		return myZip;
	}

	/**
	 * Sets the value(s) for <b>zip</b> (Postal code for area)
	 *
     * <p>
     * <b>Definition:</b>
     * A postal code designating a region defined by the postal service.
     * </p> 
	 */
	public AddressDt setZip(StringDt theValue) {
		myZip = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>zip</b> (Postal code for area)
	 *
     * <p>
     * <b>Definition:</b>
     * A postal code designating a region defined by the postal service.
     * </p> 
	 */
	public AddressDt setZip( String theString) {
		myZip = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>country</b> (Country (can be ISO 3166 3 letter code)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Country - a nation as commonly understood or generally accepted
     * </p> 
	 */
	public StringDt getCountry() {  
		if (myCountry == null) {
			myCountry = new StringDt();
		}
		return myCountry;
	}

	/**
	 * Sets the value(s) for <b>country</b> (Country (can be ISO 3166 3 letter code))
	 *
     * <p>
     * <b>Definition:</b>
     * Country - a nation as commonly understood or generally accepted
     * </p> 
	 */
	public AddressDt setCountry(StringDt theValue) {
		myCountry = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>country</b> (Country (can be ISO 3166 3 letter code))
	 *
     * <p>
     * <b>Definition:</b>
     * Country - a nation as commonly understood or generally accepted
     * </p> 
	 */
	public AddressDt setCountry( String theString) {
		myCountry = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> (Time period when address was/is in use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time period when address was/is in use
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Time period when address was/is in use)
	 *
     * <p>
     * <b>Definition:</b>
     * Time period when address was/is in use
     * </p> 
	 */
	public AddressDt setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  


}
