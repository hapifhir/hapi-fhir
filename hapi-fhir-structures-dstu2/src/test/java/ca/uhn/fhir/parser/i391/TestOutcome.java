package ca.uhn.fhir.parser.i391;

import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(profile = "test-outcome", id = "merge-operation-outcome")
public class TestOutcome extends OperationOutcome {

	private static final long serialVersionUID = 1L;

	@Child(name = "element", order = 0, min = 1, max = 1)
	@Description(shortDefinition = "description")
	private BoundCodeDt<OutcomeEnum> element;

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ElementUtil.allPopulatedChildElements(theType, element);
	}

	public BoundCodeDt<OutcomeEnum> getElement() {
		return element;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(element);
	}

	public void setElement(BoundCodeDt<OutcomeEnum> theElement) {
		element = theElement;
	}

}
