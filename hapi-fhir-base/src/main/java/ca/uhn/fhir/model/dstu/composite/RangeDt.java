















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
 * HAPI/FHIR <b>RangeDt</b> Datatype
 * (Set of values bounded by low and high)
 *
 * <p>
 * <b>Definition:</b>
 * A set of ordered Quantities defined by a low and high limit.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to be able to specify ranges of values
 * </p> 
 */
@DatatypeDef(name="RangeDt") 
public class RangeDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public RangeDt() {
		// nothing
	}


	@Child(name="low", type=QuantityDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Low limit",
		formalDefinition="The low limit. The boundary is inclusive."
	)
	private QuantityDt myLow;
	
	@Child(name="high", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="High limit",
		formalDefinition="The high limit. The boundary is inclusive."
	)
	private QuantityDt myHigh;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLow,  myHigh);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLow, myHigh);
	}

	/**
	 * Gets the value(s) for <b>low</b> (Low limit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public QuantityDt getLow() {  
		if (myLow == null) {
			myLow = new QuantityDt();
		}
		return myLow;
	}

	/**
	 * Sets the value(s) for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow(QuantityDt theValue) {
		myLow = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( double theValue) {
		myLow = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( long theValue) {
		myLow = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>high</b> (High limit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public QuantityDt getHigh() {  
		if (myHigh == null) {
			myHigh = new QuantityDt();
		}
		return myHigh;
	}

	/**
	 * Sets the value(s) for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh(QuantityDt theValue) {
		myHigh = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( double theValue) {
		myHigh = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( long theValue) {
		myHigh = new QuantityDt(theValue); 
		return this; 
	}

 


}
