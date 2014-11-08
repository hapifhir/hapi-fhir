















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
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;

/**
 * HAPI/FHIR <b>RatioDt</b> Datatype
 * (A ratio of two Quantity values - a numerator and a denominator)
 *
 * <p>
 * <b>Definition:</b>
 * A relationship of two Quantity values - expressed as a numerator and a denominator.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to able to capture ratios for some measurements (titers) and some rates (costs)
 * </p> 
 */
@DatatypeDef(name="RatioDt") 
public class RatioDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public RatioDt() {
		// nothing
	}


	@Child(name="numerator", type=QuantityDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Numerator value",
		formalDefinition="The value of the numerator"
	)
	private QuantityDt myNumerator;
	
	@Child(name="denominator", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Denominator value",
		formalDefinition="The value of the denominator"
	)
	private QuantityDt myDenominator;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myNumerator,  myDenominator);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myNumerator, myDenominator);
	}

	/**
	 * Gets the value(s) for <b>numerator</b> (Numerator value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public QuantityDt getNumerator() {  
		if (myNumerator == null) {
			myNumerator = new QuantityDt();
		}
		return myNumerator;
	}

	/**
	 * Sets the value(s) for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator(QuantityDt theValue) {
		myNumerator = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myNumerator = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myNumerator = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myNumerator = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myNumerator = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator( double theValue) {
		myNumerator = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>numerator</b> (Numerator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the numerator
     * </p> 
	 */
	public RatioDt setNumerator( long theValue) {
		myNumerator = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>denominator</b> (Denominator value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public QuantityDt getDenominator() {  
		if (myDenominator == null) {
			myDenominator = new QuantityDt();
		}
		return myDenominator;
	}

	/**
	 * Sets the value(s) for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator(QuantityDt theValue) {
		myDenominator = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myDenominator = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myDenominator = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myDenominator = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myDenominator = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator( double theValue) {
		myDenominator = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>denominator</b> (Denominator value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the denominator
     * </p> 
	 */
	public RatioDt setDenominator( long theValue) {
		myDenominator = new QuantityDt(theValue); 
		return this; 
	}

 


}
