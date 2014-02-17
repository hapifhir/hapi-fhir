package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.Constraint;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;

@DatatypeDefinition(name="Period")
public class PeriodDt extends BaseCompositeDatatype {

	@ResourceElement(name="start", order=0)
	@Constraint(lessThan= {"end"})
	private DateTimeDt myStart;

	@ResourceElement(name="end", order=1)
	@Constraint(greaterThan= {"start"})
	private DateTimeDt myEnd;

}
