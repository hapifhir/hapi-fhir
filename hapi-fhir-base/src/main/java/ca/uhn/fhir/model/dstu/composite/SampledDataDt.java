















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>SampledData</b> Datatype
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
@DatatypeDef(name="SampledData") 
public class SampledDataDt extends BaseElement implements ICompositeDatatype  {


	@Child(name="origin", type=QuantityDt.class, order=0, min=1, max=1)	
	private QuantityDt myOrigin;
	
	@Child(name="period", type=DecimalDt.class, order=1, min=1, max=1)	
	private DecimalDt myPeriod;
	
	@Child(name="factor", type=DecimalDt.class, order=2, min=0, max=1)	
	private DecimalDt myFactor;
	
	@Child(name="lowerLimit", type=DecimalDt.class, order=3, min=0, max=1)	
	private DecimalDt myLowerLimit;
	
	@Child(name="upperLimit", type=DecimalDt.class, order=4, min=0, max=1)	
	private DecimalDt myUpperLimit;
	
	@Child(name="dimensions", type=IntegerDt.class, order=5, min=1, max=1)	
	private IntegerDt myDimensions;
	
	@Child(name="data", type=StringDt.class, order=6, min=1, max=1)	
	private StringDt myData;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myOrigin,  myPeriod,  myFactor,  myLowerLimit,  myUpperLimit,  myDimensions,  myData);
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
	public void setOrigin(QuantityDt theValue) {
		myOrigin = theValue;
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
	public void setPeriod(DecimalDt theValue) {
		myPeriod = theValue;
	}


 	/**
	 * Sets the value for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public void setPeriod( java.math.BigDecimal theValue) {
		myPeriod = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public void setPeriod( double theValue) {
		myPeriod = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>period</b> (Number of milliseconds between samples)
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public void setPeriod( long theValue) {
		myPeriod = new DecimalDt(theValue); 
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
	public void setFactor(DecimalDt theValue) {
		myFactor = theValue;
	}


 	/**
	 * Sets the value for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public void setFactor( java.math.BigDecimal theValue) {
		myFactor = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public void setFactor( double theValue) {
		myFactor = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>factor</b> (Multiply data by this before adding to origin)
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public void setFactor( long theValue) {
		myFactor = new DecimalDt(theValue); 
	}

 
	/**
	 * Gets the value(s) for <b>lowerLimit</b> (Lower limit of detection).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit)
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
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit)
     * </p> 
	 */
	public void setLowerLimit(DecimalDt theValue) {
		myLowerLimit = theValue;
	}


 	/**
	 * Sets the value for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit)
     * </p> 
	 */
	public void setLowerLimit( java.math.BigDecimal theValue) {
		myLowerLimit = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit)
     * </p> 
	 */
	public void setLowerLimit( double theValue) {
		myLowerLimit = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>lowerLimit</b> (Lower limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit)
     * </p> 
	 */
	public void setLowerLimit( long theValue) {
		myLowerLimit = new DecimalDt(theValue); 
	}

 
	/**
	 * Gets the value(s) for <b>upperLimit</b> (Upper limit of detection).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit)
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
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit)
     * </p> 
	 */
	public void setUpperLimit(DecimalDt theValue) {
		myUpperLimit = theValue;
	}


 	/**
	 * Sets the value for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit)
     * </p> 
	 */
	public void setUpperLimit( java.math.BigDecimal theValue) {
		myUpperLimit = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit)
     * </p> 
	 */
	public void setUpperLimit( double theValue) {
		myUpperLimit = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>upperLimit</b> (Upper limit of detection)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit)
     * </p> 
	 */
	public void setUpperLimit( long theValue) {
		myUpperLimit = new DecimalDt(theValue); 
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
	public void setDimensions(IntegerDt theValue) {
		myDimensions = theValue;
	}


 	/**
	 * Sets the value for <b>dimensions</b> (Number of sample points at each time point)
	 *
     * <p>
     * <b>Definition:</b>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once
     * </p> 
	 */
	public void setDimensions( Integer theInteger) {
		myDimensions = new IntegerDt(theInteger); 
	}

 
	/**
	 * Gets the value(s) for <b>data</b> (Decimal values with spaces, or "E" | "U" | "L").
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public StringDt getData() {  
		if (myData == null) {
			myData = new StringDt();
		}
		return myData;
	}

	/**
	 * Sets the value(s) for <b>data</b> (Decimal values with spaces, or "E" | "U" | "L")
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public void setData(StringDt theValue) {
		myData = theValue;
	}


 	/**
	 * Sets the value for <b>data</b> (Decimal values with spaces, or "E" | "U" | "L")
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public void setData( String theString) {
		myData = new StringDt(theString); 
	}

 



}