















package ca.uhn.fhir.model.dstu3.composite;

import java.math.BigDecimal;
import java.util.List;

import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>QuantityDt</b> Datatype
 * ()
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
        extends  BaseQuantityDt         implements ICompositeDatatype{

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

	/**
	 * @deprecated Use {@link #setUnit(String)} instead - Quantity.units was renamed to Quantity.unit in DSTU2
	 */
	@Deprecated
	@Override
	public BaseQuantityDt setUnits(String theString) {
		return setUnit(theString);
	}

	/**
	 * @deprecated Use {@link #getUnitElement()} - Quantity.units was renamed to Quantity.unit in DSTU2
	 */
	@Deprecated
	@Override
	public StringDt getUnitsElement() {
		return getUnitElement();
	}

	@Child(name="value", type=DecimalDt.class, order=0, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The value of the measured amount. The value includes an implicit precision in the presentation of the value"
	)
	private DecimalDt myValue;
	
	@Child(name="comparator", type=CodeDt.class, order=1, min=0, max=1, summary=true, modifier=true)	
	@Description(
		shortDefinition="",
		formalDefinition="How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues; e.g. if the comparator is \"<\" , then the real value is < stated value"
	)
	private BoundCodeDt<QuantityComparatorEnum> myComparator;
	
	@Child(name="unit", type=StringDt.class, order=2, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A human-readable form of the unit"
	)
	private StringDt myUnit;
	
	@Child(name="system", type=UriDt.class, order=3, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The identification of the system that provides the coded form of the unit"
	)
	private UriDt mySystem;
	
	@Child(name="code", type=CodeDt.class, order=4, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A computer processable form of the unit in some unit representation system"
	)
	private CodeDt myCode;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myValue,  myComparator,  myUnit,  mySystem,  myCode);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myValue, myComparator, myUnit, mySystem, myCode);
	}

	/**
	 * Gets the value(s) for <b>value</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	@Override
	public DecimalDt getValueElement() {
		if (myValue == null) {
			myValue = new DecimalDt();
		}
		return myValue;
	}

	
	/**
	 * Gets the value(s) for <b>value</b> ().
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
	 * Sets the value(s) for <b>value</b> ()
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
	 * Sets the value for <b>value</b> ()
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
	 * Sets the value for <b>value</b> ()
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
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	@Override
	public QuantityDt setValue(java.math.BigDecimal theValue) {
		myValue = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comparator</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues; e.g. if the comparator is \&quot;&lt;\&quot; , then the real value is &lt; stated value
     * </p> 
	 */
	@Override
	public BoundCodeDt<QuantityComparatorEnum> getComparatorElement() {
		if (myComparator == null) {
			myComparator = new BoundCodeDt<QuantityComparatorEnum>(QuantityComparatorEnum.VALUESET_BINDER);
		}
		return myComparator;
	}

	
	/**
	 * Gets the value(s) for <b>comparator</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues; e.g. if the comparator is \&quot;&lt;\&quot; , then the real value is &lt; stated value
     * </p> 
	 */
	public String getComparator() {  
		return getComparatorElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comparator</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues; e.g. if the comparator is \&quot;&lt;\&quot; , then the real value is &lt; stated value
     * </p> 
	 */
	public QuantityDt setComparator(BoundCodeDt<QuantityComparatorEnum> theValue) {
		myComparator = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>comparator</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues; e.g. if the comparator is \&quot;&lt;\&quot; , then the real value is &lt; stated value
     * </p> 
	 */
	public QuantityDt setComparator(QuantityComparatorEnum theValue) {
		setComparator(new BoundCodeDt<QuantityComparatorEnum>(QuantityComparatorEnum.VALUESET_BINDER, theValue));
		
/*
		getComparatorElement().setValueAsEnum(theValue);
*/
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>unit</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the unit
     * </p> 
	 */
	public StringDt getUnitElement() {  
		if (myUnit == null) {
			myUnit = new StringDt();
		}
		return myUnit;
	}

	
	/**
	 * Gets the value(s) for <b>unit</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the unit
     * </p> 
	 */
	public String getUnit() {  
		return getUnitElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>unit</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the unit
     * </p> 
	 */
	public QuantityDt setUnit(StringDt theValue) {
		myUnit = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>unit</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the unit
     * </p> 
	 */
	public QuantityDt setUnit( String theString) {
		myUnit = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	@Override
	public UriDt getSystemElement() {
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> ().
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
	 * Sets the value(s) for <b>system</b> ()
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
	 * Sets the value for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	@Override
	public QuantityDt setSystem(String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the unit in some unit representation system
     * </p> 
	 */
	@Override
	public CodeDt getCodeElement() {
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the unit in some unit representation system
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the unit in some unit representation system
     * </p> 
	 */
	public QuantityDt setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the unit in some unit representation system
     * </p> 
	 */
	@Override
	public QuantityDt setCode(String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 


}
