package ca.uhn.fhir.model.datatype;

import java.util.List;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="CodeableConcept")
public class CodeableConceptDt extends BaseCompositeDatatype implements ICodedDatatype {

	@Child(name="coding", order=0, min=0, max=Child.MAX_UNLIMITED)
    private List<CodingDt> myCoding;

	@Child(name="text",order=1)
    private StringDt myText;

	public List<CodingDt> getCoding() {
		return myCoding;
	}

	public void setCoding(List<CodingDt> theCoding) {
		myCoding = theCoding;
	}

	public StringDt getText() {
		return myText;
	}

	public void setText(StringDt theText) {
		myText = theText;
	}
	
	
	
}
