package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.Description;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="SampledData")
public class SampledDataDt extends BaseCompositeDatatype {

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
}
