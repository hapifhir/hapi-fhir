package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;

@DatatypeDef(name="SampledData")
public class SampledDataDt extends BaseElement implements ICompositeDatatype {

	@Child(name="origin", order=0, min=1)
	private QuantityDt myOrigin;
	
	@Child(name="period", order=1, min=1)
	private DecimalDt myPeriod;
	
	@Child(name="factor", order=2)
	private DecimalDt myFactor;

	@Child(name="lowerLimit", order=3)
	private DecimalDt myLowerLimit;

	@Child(name="upperLimit", order=4)
	private DecimalDt myUpperLimit;

	@Child(name="dimensions", order=5, min=1)
	private IntegerDt myDimensions;
	
	@Child(name="data", order=6, min=1)
	@Description("Decimal values with spaces, or \"E\" | \"U\" | \"L\"")
	private StringDt myData;

	public QuantityDt getOrigin() {
		return myOrigin;
	}

	public void setOrigin(QuantityDt theOrigin) {
		myOrigin = theOrigin;
	}

	public DecimalDt getPeriod() {
		return myPeriod;
	}

	public void setPeriod(DecimalDt thePeriod) {
		myPeriod = thePeriod;
	}

	public DecimalDt getFactor() {
		return myFactor;
	}

	public void setFactor(DecimalDt theFactor) {
		myFactor = theFactor;
	}

	public DecimalDt getLowerLimit() {
		return myLowerLimit;
	}

	public void setLowerLimit(DecimalDt theLowerLimit) {
		myLowerLimit = theLowerLimit;
	}

	public DecimalDt getUpperLimit() {
		return myUpperLimit;
	}

	public void setUpperLimit(DecimalDt theUpperLimit) {
		myUpperLimit = theUpperLimit;
	}

	public IntegerDt getDimensions() {
		return myDimensions;
	}

	public void setDimensions(IntegerDt theDimensions) {
		myDimensions = theDimensions;
	}

	public StringDt getData() {
		return myData;
	}

	public void setData(StringDt theData) {
		myData = theData;
	}
	
	
}
