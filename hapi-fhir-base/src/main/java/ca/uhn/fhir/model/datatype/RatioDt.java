package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="Ratio")
public class RatioDt extends BaseCompositeDatatype {

	@Child(name="numerator", order=0)
	@Constraint(coRequirements= {"denominator"})
	private QuantityDt myNumerator;
	
	@Child(name="denominator", order=1)
	@Constraint(coRequirements= {"numerator"})
	private QuantityDt myDenominator;
}
