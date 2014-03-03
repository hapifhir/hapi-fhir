package ca.uhn.fhir.model.dstu.composite;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="Range")
public class RangeDt extends BaseElement implements ICompositeDatatype {

	@Child(name="low", order=0)
	@Constraint(lessThan= {"high"})
	private QuantityDt myLow;

	@Child(name="high", order=1)
	@Constraint(greaterThan= {"low"})
	private QuantityDt myHigh;

	public QuantityDt getLow() {
		return myLow;
	}

	public void setLow(QuantityDt theLow) {
		myLow = theLow;
	}

	public QuantityDt getHigh() {
		return myHigh;
	}

	public void setHigh(QuantityDt theHigh) {
		myHigh = theHigh;
	}

}
