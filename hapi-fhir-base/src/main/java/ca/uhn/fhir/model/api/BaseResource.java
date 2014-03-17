package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.util.ElementUtil;

public abstract class BaseResource extends BaseElement implements IResource {

	@Child(name="language", order=0, min=0, max=Child.MAX_UNLIMITED)
	private CodeDt myLanguage;

	@Child(name="text", order=1, min=0, max=1)
	private NarrativeDt myText;

	@Child(name="contained", order=2, min=0, max=1)
	private ContainedDt myContained;

	@Override
	public ContainedDt getContained() {
		if (myContained == null) {
			myContained = new ContainedDt();
		}
		return myContained;
	}

	public void setContained(ContainedDt theContained) {
		myContained = theContained;
	}

	public CodeDt getLanguage() {
		return myLanguage;
	}

	public NarrativeDt getText() {
		if (myText == null) {
			myText = new NarrativeDt();
		}
		return myText;
	}

	public void setLanguage(CodeDt theLanguage) {
		myLanguage = theLanguage;
	}

	public void setText(NarrativeDt theText) {
		myText = theText;
	}
	
	/**
	 * Intended to be called by extending classes {@link #isEmpty()} implementations, returns <code>true</code>
	 * if all content in this superclass instance is empty per the semantics of {@link #isEmpty()}. 
	 */
	@Override
	protected boolean isBaseEmpty() {
		return super.isBaseEmpty() && ElementUtil.isEmpty(myLanguage, myText);
	}

	
}
