















package ca.uhn.fhir.model.dev.composite;

import java.net.URI;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;
import ca.uhn.fhir.model.dev.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dev.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dev.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dev.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dev.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dev.valueset.NameUseEnum;
import ca.uhn.fhir.model.dev.resource.Organization;
import ca.uhn.fhir.model.dev.composite.PeriodDt;
import ca.uhn.fhir.model.dev.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dev.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dev.composite.QuantityDt;
import ca.uhn.fhir.model.dev.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dev.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dev.resource.ValueSet;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
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
 * HAPI/FHIR <b>QuantityDt</b> Datatype
 * (Quantity)
 *
 * <p>
 * <b>Definition:</b>
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to able to capture all sorts of measured values, even if the measured value are not precisely quantified. Values include exact measures such as 3.51g, customary units such as 3 tablets, and currencies such as $100.32USD
 * </p> 
 */
@DatatypeDef(name="QuantityDt") 
public class QuantityDt
        extends  BaseQuantityDt         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public QuantityDt() {
		// nothing
	}

 
	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name="theValue") double theValue) {
		setValue(theValue);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name="theValue") long theValue) {
		setValue(theValue);
	}
	
	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name = "theComparator") QuantityComparatorEnum theComparator, @SimpleSetter.Parameter(name = "theValue") double theValue,
			@SimpleSetter.Parameter(name = "theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name = "theComparator") QuantityComparatorEnum theComparator, @SimpleSetter.Parameter(name = "theValue") long theValue,
			@SimpleSetter.Parameter(name = "theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name="theComparator") QuantityComparatorEnum theComparator, @SimpleSetter.Parameter(name="theValue") double theValue, @SimpleSetter.Parameter(name="theSystem") String theSystem, @SimpleSetter.Parameter(name="theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name="theComparator") QuantityComparatorEnum theComparator, @SimpleSetter.Parameter(name="theValue") long theValue, @SimpleSetter.Parameter(name="theSystem") String theSystem, @SimpleSetter.Parameter(name="theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setSystem(theSystem);
		setUnits(theUnits);
	}


	@Child(name="value", type=DecimalDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Quantity.value",
		formalDefinition="The value of the measured amount. The value includes an implicit precision in the presentation of the value"
	)
	private DecimalDt myValue;
	
	@Child(name="comparator", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Quantity.comparator",
		formalDefinition="How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value"
	)
	private BoundCodeDt<QuantityComparatorEnum> myComparator;
	
	@Child(name="units", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Quantity.units",
		formalDefinition="A human-readable form of the units"
	)
	private StringDt myUnits;
	
	@Child(name="system", type=UriDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Quantity.system",
		formalDefinition="The identification of the system that provides the coded form of the unit"
	)
	private UriDt mySystem;
	
	@Child(name="code", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Quantity.code",
		formalDefinition="A computer processable form of the units in some unit representation system"
	)
	private CodeDt myCode;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myValue,  myComparator,  myUnits,  mySystem,  myCode);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myValue, myComparator, myUnits, mySystem, myCode);
	}

	/**
	 * Gets the value(s) for <b>value</b> (Quantity.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public DecimalDt getValueElement() {  
		if (myValue == null) {
			myValue = new DecimalDt();
		}
		return myValue;
	}

	
	/**
	 * Gets the value(s) for <b>value</b> (Quantity.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public BigDecimal getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> (Quantity.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public QuantityDt setValue(DecimalDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> (Quantity.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public QuantityDt setValue( long theValue) {
		myValue = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>value</b> (Quantity.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public QuantityDt setValue( double theValue) {
		myValue = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>value</b> (Quantity.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public QuantityDt setValue( java.math.BigDecimal theValue) {
		myValue = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comparator</b> (Quantity.comparator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public BoundCodeDt<QuantityComparatorEnum> getComparatorElement() {  
		if (myComparator == null) {
			myComparator = new BoundCodeDt<QuantityComparatorEnum>(QuantityComparatorEnum.VALUESET_BINDER);
		}
		return myComparator;
	}

	
	/**
	 * Gets the value(s) for <b>comparator</b> (Quantity.comparator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public String getComparator() {  
		return getComparatorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comparator</b> (Quantity.comparator)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public QuantityDt setComparator(BoundCodeDt<QuantityComparatorEnum> theValue) {
		myComparator = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>comparator</b> (Quantity.comparator)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public QuantityDt setComparator(QuantityComparatorEnum theValue) {
		getComparatorElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>units</b> (Quantity.units).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public StringDt getUnitsElement() {  
		if (myUnits == null) {
			myUnits = new StringDt();
		}
		return myUnits;
	}

	
	/**
	 * Gets the value(s) for <b>units</b> (Quantity.units).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public String getUnits() {  
		return getUnitsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>units</b> (Quantity.units)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public QuantityDt setUnits(StringDt theValue) {
		myUnits = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>units</b> (Quantity.units)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public QuantityDt setUnits( String theString) {
		myUnits = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>system</b> (Quantity.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> (Quantity.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public String getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> (Quantity.system)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public QuantityDt setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> (Quantity.system)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public QuantityDt setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Quantity.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (Quantity.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (Quantity.code)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public QuantityDt setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (Quantity.code)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public QuantityDt setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 


}