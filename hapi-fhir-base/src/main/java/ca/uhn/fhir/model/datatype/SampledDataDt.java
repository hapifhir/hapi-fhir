package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.api.Description;

@DatatypeDefinition(name="SampledData")
public class SampledDataDt extends BaseCompositeDatatype {

	@ResourceElement(name="origin", order=0, min=1)
	private QuantityDt myOrigin;
	
	@ResourceElement(name="period", order=1, min=1)
	private DecimalDt myPeriod;
	
	@ResourceElement(name="factor", order=2)
	private DecimalDt myFactor;

	@ResourceElement(name="lowerLimit", order=3)
	private DecimalDt myLowerLimit;

	@ResourceElement(name="upperLimit", order=4)
	private DecimalDt myUpperLimit;

	@ResourceElement(name="dimensions", order=5, min=1)
	private IntegerDt myDimensions;
	
	@ResourceElement(name="data", order=6, min=1)
	@Description("Decimal values with spaces, or \"E\" | \"U\" | \"L\"")
	private StringDt myData;
}
