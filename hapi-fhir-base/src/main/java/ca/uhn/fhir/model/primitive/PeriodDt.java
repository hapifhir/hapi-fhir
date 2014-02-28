package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="Period")
public class PeriodDt extends BaseElement implements ICompositeDatatype {

	@Child(name="start", order=0)
	@Constraint(lessThan= {"end"})
	private DateTimeDt myStart;

	@Child(name="end", order=1)
	@Constraint(greaterThan= {"start"})
	private DateTimeDt myEnd;

	public DateTimeDt getStart() {
		return myStart;
	}

	public void setStart(DateTimeDt theStart) {
		myStart = theStart;
	}

	public DateTimeDt getEnd() {
		return myEnd;
	}

	public void setEnd(DateTimeDt theEnd) {
		myEnd = theEnd;
	}

	
}
