package ca.uhn.fhir.model.datatype;

import java.util.List;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="CodeableConcept")
public class CodeableConceptDt<T extends ICodeEnum> extends BaseCompositeDatatype {

	@Child(name="coding", order=0)
    private List<CodingDt> myCoding;

	@Child(name="text",order=1)
    private StringDt myText;
}
