package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="Period")
public class PeriodDt extends BaseCompositeDatatype {

	@Child(name="start", order=0)
	@Constraint(lessThan= {"end"})
	private DateTimeDt myStart;

	@Child(name="end", order=1)
	@Constraint(greaterThan= {"start"})
	private DateTimeDt myEnd;

}
