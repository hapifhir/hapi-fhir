















package ca.uhn.fhir.model.dstu.composite;

import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;

/**
 * HAPI/FHIR <b>Ratio</b> Datatype
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
@DatatypeDef(name="Ratio") 
public class RatioDt 
        extends  BaseElement         implements ICompositeDatatype  {


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
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
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
	public void setNumerator(QuantityDt theValue) {
		myNumerator = theValue;
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
	public void setDenominator(QuantityDt theValue) {
		myDenominator = theValue;
	}

  



}