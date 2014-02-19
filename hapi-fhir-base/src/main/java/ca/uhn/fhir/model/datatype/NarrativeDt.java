package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.enm.NarrativeStatusEnum;

@DatatypeDef(name="Narrative")
public class NarrativeDt extends BaseCompositeDatatype {

	@Child(name="status", order=0, min=1)
	@CodeableConceptElement(type=NarrativeStatusEnum.class)
    private CodeDt<NarrativeStatusEnum> myStatus;

	@Child(name="div", order=1)
	private StringDt myDiv;
	
	public StringDt getDiv() {
		return myDiv;
	}

	public CodeDt<NarrativeStatusEnum> getStatus() {
		return myStatus;
	}


	public void setDiv(StringDt theDiv) {
		myDiv = theDiv;
	}

	public void setStatus(CodeDt<NarrativeStatusEnum> theStatus) {
		myStatus = theStatus;
	}

	
}
