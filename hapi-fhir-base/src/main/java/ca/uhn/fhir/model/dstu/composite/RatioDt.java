package ca.uhn.fhir.model.dstu.composite;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="Ratio")
public class RatioDt extends BaseElement implements ICompositeDatatype {

	@Child(name="numerator", order=0)
	@Constraint(coRequirements= {"denominator"})
	private QuantityDt myNumerator;
	
	@Child(name="denominator", order=1)
	@Constraint(coRequirements= {"numerator"})
	private QuantityDt myDenominator;

	public QuantityDt getNumerator() {
		return myNumerator;
	}

	public void setNumerator(QuantityDt theNumerator) {
		myNumerator = theNumerator;
	}

	public QuantityDt getDenominator() {
		return myDenominator;
	}

	public void setDenominator(QuantityDt theDenominator) {
		myDenominator = theDenominator;
	}
	
	
}
