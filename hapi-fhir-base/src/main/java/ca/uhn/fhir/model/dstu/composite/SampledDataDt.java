















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
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * HAPI/FHIR <b>SampledDataDt</b> Datatype
 * (A series of measurements taken by a device)
 *
 * <p>
 * <b>Definition:</b>
 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * There is a need for a concise way to handle the data produced by devices that sample a physical state at a high frequency
 * </p> 
 */
@DatatypeDef(name="SampledDataDt") 
public class SampledDataDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public SampledDataDt() {
		// nothing
	}


	@Child(name="origin", type=QuantityDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Zero value and units",
		formalDefinition="The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series"
	)
	private QuantityDt myOrigin;
	
	@Child(name="period", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Number of milliseconds between samples",
		formalDefinition="The length of time between sampling times, measured in milliseconds"
	)
	private DecimalDt myPeriod;
	
	@Child(name="factor", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Multiply data by this before adding to origin",
		formalDefinition="A correction factor that is applied to the sampled data points before they are added to the origin"
	)
	private DecimalDt myFactor;
	
	@Child(name="lowerLimit", type=DecimalDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Lower limit of detection",
		formalDefinition="The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)"
	)
	private DecimalDt myLowerLimit;
	
	@Child(name="upperLimit", type=DecimalDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Upper limit of detection",
		formalDefinition="The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)"
	)
	private DecimalDt myUpperLimit;
	
	@Child(name="dimensions", type=IntegerDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Number of sample points at each time point",
		formalDefinition="The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once"
	)
	private IntegerDt myDimensions;
	
	@Child(name="data", type=StringDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="Decimal values with spaces, or \"E\" | \"U\" | \"L\"",
		formalDefinition="A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value"
	)
	private StringDt myData;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myOrigin,  myPeriod,  myFactor,  myLowerLimit,  myUpperLimit,  myDimensions,  myData);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myOrigin, myPeriod, myFactor, myLowerLimit, myUpperLimit, myDimensions, myData);
	}

	/**
	 * Gets the value(s) for <b>origin</b> (Zero value and units).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public QuantityDt getOrigin() {  
		if (myOrigin == null) {
			myOrigin = new QuantityDt();
		}
		return myOrigin;
	}

	/**
	 * Sets the value(s) for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin(QuantityDt theValue) {
		myOrigin = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myOrigin = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myOrigin = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myOrigin = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myOrigin = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin( double theValue) {
		myOrigin = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>origin</b> (Zero value and units)
	 *
     * <p>
     * <b>Definition:</b>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series
     * </p> 
	 */
	public SampledDataDt setOrigin( long theValue) {
		myOrigin = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> (Number of milliseconds between samples).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public DecimalDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new DecimalDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public SampledDataDt setPeriod(DecimalDt theValue) {
		myPeriod = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public SampledDataDt setPeriod( long theValue) {
		myPeriod = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public SampledDataDt setPeriod( double theValue) {
		myPeriod = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public SampledDataDt setPeriod( java.math.BigDecimal theValue) {
		myPeriod = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>factor</b> (Multiply data by this before adding to origin).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public DecimalDt getFactor() {  
		if (myFactor == null) {
			myFactor = new DecimalDt();
		}
		return myFactor;
	}

	/**
	 * Sets the value(s) for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public SampledDataDt setFactor(DecimalDt theValue) {
		myFactor = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public SampledDataDt setFactor( long theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public SampledDataDt setFactor( double theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public SampledDataDt setFactor( java.math.BigDecimal theValue) {
		myFactor = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>lowerLimit</b> (Lower limit of detection).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)
     * </p> 
	 */
	public DecimalDt getLowerLimit() {  
		if (myLowerLimit == null) {
			myLowerLimit = new DecimalDt();
		}
		return myLowerLimit;
	}

	/**
	 * Sets the value(s) for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit(DecimalDt theValue) {
		myLowerLimit = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit( long theValue) {
		myLowerLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit( double theValue) {
		myLowerLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit( java.math.BigDecimal theValue) {
		myLowerLimit = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>upperLimit</b> (Upper limit of detection).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)
     * </p> 
	 */
	public DecimalDt getUpperLimit() {  
		if (myUpperLimit == null) {
			myUpperLimit = new DecimalDt();
		}
		return myUpperLimit;
	}

	/**
	 * Sets the value(s) for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit(DecimalDt theValue) {
		myUpperLimit = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit( long theValue) {
		myUpperLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit( double theValue) {
		myUpperLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit( java.math.BigDecimal theValue) {
		myUpperLimit = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dimensions</b> (Number of sample points at each time point).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once
     * </p> 
	 */
	public IntegerDt getDimensions() {  
		if (myDimensions == null) {
			myDimensions = new IntegerDt();
		}
		return myDimensions;
	}

	/**
	 * Sets the value(s) for <b>dimensions</b> (Number of sample points at each time point)
	 *
     * <p>
     * <b>Definition:</b>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once
     * </p> 
	 */
	public SampledDataDt setDimensions(IntegerDt theValue) {
		myDimensions = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dimensions</b> (Number of sample points at each time point)
	 *
     * <p>
     * <b>Definition:</b>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once
     * </p> 
	 */
	public SampledDataDt setDimensions( int theInteger) {
		myDimensions = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>data</b> (Decimal values with spaces, or \"E\" | \"U\" | \"L\").
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public StringDt getData() {  
		if (myData == null) {
			myData = new StringDt();
		}
		return myData;
	}

	/**
	 * Sets the value(s) for <b>data</b> (Decimal values with spaces, or \"E\" | \"U\" | \"L\")
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public SampledDataDt setData(StringDt theValue) {
		myData = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>data</b> (Decimal values with spaces, or \"E\" | \"U\" | \"L\")
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public SampledDataDt setData( String theString) {
		myData = new StringDt(theString); 
		return this; 
	}

 


}
