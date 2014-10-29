















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

import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>QuantityDt</b> Datatype
 * (A measured or measurable amount)
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
	public QuantityDt(@SimpleSetter.Parameter(name = "theComparator") QuantityCompararatorEnum theComparator, @SimpleSetter.Parameter(name = "theValue") double theValue,
			@SimpleSetter.Parameter(name = "theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name = "theComparator") QuantityCompararatorEnum theComparator, @SimpleSetter.Parameter(name = "theValue") long theValue,
			@SimpleSetter.Parameter(name = "theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name="theComparator") QuantityCompararatorEnum theComparator, @SimpleSetter.Parameter(name="theValue") double theValue, @SimpleSetter.Parameter(name="theSystem") String theSystem, @SimpleSetter.Parameter(name="theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public QuantityDt(@SimpleSetter.Parameter(name="theComparator") QuantityCompararatorEnum theComparator, @SimpleSetter.Parameter(name="theValue") long theValue, @SimpleSetter.Parameter(name="theSystem") String theSystem, @SimpleSetter.Parameter(name="theUnits") String theUnits) {
		setValue(theValue);
		setComparator(theComparator);
		setSystem(theSystem);
		setUnits(theUnits);
	}


	@Child(name="value", type=DecimalDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Numerical value (with implicit precision)",
		formalDefinition="The value of the measured amount. The value includes an implicit precision in the presentation of the value"
	)
	private DecimalDt myValue;
	
	@Child(name="comparator", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="< | <= | >= | > - how to understand the value",
		formalDefinition="How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value"
	)
	private BoundCodeDt<QuantityCompararatorEnum> myComparator;
	
	@Child(name="units", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Unit representation",
		formalDefinition="A human-readable form of the units"
	)
	private StringDt myUnits;
	
	@Child(name="system", type=UriDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="System that defines coded unit form",
		formalDefinition="The identification of the system that provides the coded form of the unit"
	)
	private UriDt mySystem;
	
	@Child(name="code", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Coded form of the unit",
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
	 * Gets the value(s) for <b>value</b> (Numerical value (with implicit precision)).
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
	 * Sets the value(s) for <b>value</b> (Numerical value (with implicit precision))
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
	 * Sets the value for <b>value</b> (Numerical value (with implicit precision))
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
	 * Sets the value for <b>value</b> (Numerical value (with implicit precision))
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
	 * Sets the value for <b>value</b> (Numerical value (with implicit precision))
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
	 * Gets the value(s) for <b>comparator</b> (< | <= | >= | > - how to understand the value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public BoundCodeDt<QuantityCompararatorEnum> getComparatorElement() {  
		if (myComparator == null) {
			myComparator = new BoundCodeDt<QuantityCompararatorEnum>(QuantityCompararatorEnum.VALUESET_BINDER);
		}
		return myComparator;
	}

	/**
	 * Sets the value(s) for <b>comparator</b> (< | <= | >= | > - how to understand the value)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public QuantityDt setComparator(BoundCodeDt<QuantityCompararatorEnum> theValue) {
		myComparator = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>comparator</b> (< | <= | >= | > - how to understand the value)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public QuantityDt setComparator(QuantityCompararatorEnum theValue) {
		getComparatorElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>units</b> (Unit representation).
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
	 * Sets the value(s) for <b>units</b> (Unit representation)
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
	 * Sets the value for <b>units</b> (Unit representation)
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
	 * Gets the value(s) for <b>system</b> (System that defines coded unit form).
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
	 * Sets the value(s) for <b>system</b> (System that defines coded unit form)
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
	 * Sets the value for <b>system</b> (System that defines coded unit form)
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
	 * Gets the value(s) for <b>code</b> (Coded form of the unit).
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
	 * Sets the value(s) for <b>code</b> (Coded form of the unit)
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
	 * Sets the value for <b>code</b> (Coded form of the unit)
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
