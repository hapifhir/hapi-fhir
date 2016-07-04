package ca.uhn.fhir.parser.i391;

import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@Block
public class CustomBlock extends Observation.Component {

    @Child(name = "value", min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {StringDt.class})
    public StringDt ourValue;

    @Override
    public boolean isEmpty() {
        return super.isBaseEmpty() && ElementUtil.isEmpty(ourValue);
    }

    @Override
    public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
        return ElementUtil.allPopulatedChildElements(theType, ourValue);
    }
} 

