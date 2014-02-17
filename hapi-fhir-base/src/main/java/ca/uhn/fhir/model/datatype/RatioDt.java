package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.Constraint;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;

@DatatypeDefinition(name="Ratio")
public class RatioDt extends BaseCompositeDatatype {

	@ResourceElement(name="numerator", order=0)
	@Constraint(coRequirements= {"denominator"})
	private QuantityDt myNumerator;
	
	@ResourceElement(name="denominator", order=1)
	@Constraint(coRequirements= {"numerator"})
	private QuantityDt myDenominator;
}
