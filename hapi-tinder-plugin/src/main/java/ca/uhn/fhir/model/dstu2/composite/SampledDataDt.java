















package ca.uhn.fhir.model.dstu2.composite;

import java.net.URI;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu2.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>SampledDataDt</b> Datatype
 * (SampledData)
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
		shortDefinition="SampledData.origin",
		formalDefinition="The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series"
	)
	private QuantityDt myOrigin;
	
	@Child(name="period", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="SampledData.period",
		formalDefinition="The length of time between sampling times, measured in milliseconds"
	)
	private DecimalDt myPeriod;
	
	@Child(name="factor", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="SampledData.factor",
		formalDefinition="A correction factor that is applied to the sampled data points before they are added to the origin"
	)
	private DecimalDt myFactor;
	
	@Child(name="lowerLimit", type=DecimalDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="SampledData.lowerLimit",
		formalDefinition="The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)"
	)
	private DecimalDt myLowerLimit;
	
	@Child(name="upperLimit", type=DecimalDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="SampledData.upperLimit",
		formalDefinition="The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)"
	)
	private DecimalDt myUpperLimit;
	
	@Child(name="dimensions", type=IntegerDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="SampledData.dimensions",
		formalDefinition="The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once"
	)
	private IntegerDt myDimensions;
	
	@Child(name="data", type=StringDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="SampledData.data",
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
	 * Gets the value(s) for <b>origin</b> (SampledData.origin).
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
	 * Sets the value(s) for <b>origin</b> (SampledData.origin)
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
	 * Gets the value(s) for <b>period</b> (SampledData.period).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public DecimalDt getPeriodElement() {  
		if (myPeriod == null) {
			myPeriod = new DecimalDt();
		}
		return myPeriod;
	}

	
	/**
	 * Gets the value(s) for <b>period</b> (SampledData.period).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The length of time between sampling times, measured in milliseconds
     * </p> 
	 */
	public BigDecimal getPeriod() {  
		return getPeriodElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>period</b> (SampledData.period)
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
	 * Sets the value for <b>period</b> (SampledData.period)
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
	 * Sets the value for <b>period</b> (SampledData.period)
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
	 * Sets the value for <b>period</b> (SampledData.period)
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
	 * Gets the value(s) for <b>factor</b> (SampledData.factor).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public DecimalDt getFactorElement() {  
		if (myFactor == null) {
			myFactor = new DecimalDt();
		}
		return myFactor;
	}

	
	/**
	 * Gets the value(s) for <b>factor</b> (SampledData.factor).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A correction factor that is applied to the sampled data points before they are added to the origin
     * </p> 
	 */
	public BigDecimal getFactor() {  
		return getFactorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>factor</b> (SampledData.factor)
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
	 * Sets the value for <b>factor</b> (SampledData.factor)
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
	 * Sets the value for <b>factor</b> (SampledData.factor)
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
	 * Sets the value for <b>factor</b> (SampledData.factor)
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
	 * Gets the value(s) for <b>lowerLimit</b> (SampledData.lowerLimit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \&quot;L\&quot; (lower than detection limit)
     * </p> 
	 */
	public DecimalDt getLowerLimitElement() {  
		if (myLowerLimit == null) {
			myLowerLimit = new DecimalDt();
		}
		return myLowerLimit;
	}

	
	/**
	 * Gets the value(s) for <b>lowerLimit</b> (SampledData.lowerLimit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \&quot;L\&quot; (lower than detection limit)
     * </p> 
	 */
	public BigDecimal getLowerLimit() {  
		return getLowerLimitElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lowerLimit</b> (SampledData.lowerLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \&quot;L\&quot; (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit(DecimalDt theValue) {
		myLowerLimit = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lowerLimit</b> (SampledData.lowerLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \&quot;L\&quot; (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit( long theValue) {
		myLowerLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>lowerLimit</b> (SampledData.lowerLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \&quot;L\&quot; (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit( double theValue) {
		myLowerLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>lowerLimit</b> (SampledData.lowerLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value \&quot;L\&quot; (lower than detection limit)
     * </p> 
	 */
	public SampledDataDt setLowerLimit( java.math.BigDecimal theValue) {
		myLowerLimit = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>upperLimit</b> (SampledData.upperLimit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \&quot;U\&quot; (higher than detection limit)
     * </p> 
	 */
	public DecimalDt getUpperLimitElement() {  
		if (myUpperLimit == null) {
			myUpperLimit = new DecimalDt();
		}
		return myUpperLimit;
	}

	
	/**
	 * Gets the value(s) for <b>upperLimit</b> (SampledData.upperLimit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \&quot;U\&quot; (higher than detection limit)
     * </p> 
	 */
	public BigDecimal getUpperLimit() {  
		return getUpperLimitElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>upperLimit</b> (SampledData.upperLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \&quot;U\&quot; (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit(DecimalDt theValue) {
		myUpperLimit = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>upperLimit</b> (SampledData.upperLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \&quot;U\&quot; (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit( long theValue) {
		myUpperLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>upperLimit</b> (SampledData.upperLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \&quot;U\&quot; (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit( double theValue) {
		myUpperLimit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>upperLimit</b> (SampledData.upperLimit)
	 *
     * <p>
     * <b>Definition:</b>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value \&quot;U\&quot; (higher than detection limit)
     * </p> 
	 */
	public SampledDataDt setUpperLimit( java.math.BigDecimal theValue) {
		myUpperLimit = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dimensions</b> (SampledData.dimensions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once
     * </p> 
	 */
	public IntegerDt getDimensionsElement() {  
		if (myDimensions == null) {
			myDimensions = new IntegerDt();
		}
		return myDimensions;
	}

	
	/**
	 * Gets the value(s) for <b>dimensions</b> (SampledData.dimensions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once
     * </p> 
	 */
	public Integer getDimensions() {  
		return getDimensionsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>dimensions</b> (SampledData.dimensions)
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
	 * Sets the value for <b>dimensions</b> (SampledData.dimensions)
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
	 * Gets the value(s) for <b>data</b> (SampledData.data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \&quot;E\&quot; (error), \&quot;L\&quot; (below detection limit) and \&quot;U\&quot; (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public StringDt getDataElement() {  
		if (myData == null) {
			myData = new StringDt();
		}
		return myData;
	}

	
	/**
	 * Gets the value(s) for <b>data</b> (SampledData.data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \&quot;E\&quot; (error), \&quot;L\&quot; (below detection limit) and \&quot;U\&quot; (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public String getData() {  
		return getDataElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>data</b> (SampledData.data)
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \&quot;E\&quot; (error), \&quot;L\&quot; (below detection limit) and \&quot;U\&quot; (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public SampledDataDt setData(StringDt theValue) {
		myData = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>data</b> (SampledData.data)
	 *
     * <p>
     * <b>Definition:</b>
     * A series of data points which are decimal values separated by a single space (character u20). The special values \&quot;E\&quot; (error), \&quot;L\&quot; (below detection limit) and \&quot;U\&quot; (above detection limit) can also be used in place of a decimal value
     * </p> 
	 */
	public SampledDataDt setData( String theString) {
		myData = new StringDt(theString); 
		return this; 
	}

 


}