package ca.uhn.fhir.parser.i423;

import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.ProcedureRequest;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "ProcedureRequest", id = "custom-procedure-request", profile = "http://test/")
public class CustomProcedureRequest extends ProcedureRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * scheduled
	 */
	@Child(name = FIELD_SCHEDULED, min = 0, max = 1, order = Child.REPLACE_PARENT, type = { CustomTimingDt.class })
	@Description(shortDefinition = "When procedure should occur", formalDefinition = "The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".")
	protected CustomTimingDt ourScheduled;

	public static final String FIELD_SCHEDULED = "scheduled";

	public CustomTimingDt _getScheduled() {
		if (ourScheduled == null)
			ourScheduled = new CustomTimingDt();
		return ourScheduled;
	}

	public CustomProcedureRequest _setScheduled(CustomTimingDt theValue) {
		ourScheduled = theValue;
		return this;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(ourScheduled);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ElementUtil.allPopulatedChildElements(theType, ourScheduled);
	}
}
