package ca.uhn.fhir.model.datatype;

import java.util.List;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.api.ICodeEnum;

@DatatypeDefinition(name="CodeableConcept")
public class CodeableConceptDt<T extends ICodeEnum> extends BaseCompositeDatatype {

	@ResourceElement(name="coding", order=0)
    private List<CodingDt> myCoding;

	@ResourceElement(name="text",order=1)
    private StringDt myText;
}
