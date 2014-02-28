package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.dstu.resource.ValueSet;


@DatatypeDef(name="Coding")
public class CodingDt extends BaseElement implements ICompositeDatatype {

	@Child(name="system", order=0)
	private UriDt mySystem;
    
	@Child(name="version", order=1)
    private StringDt myVersion;

	@Child(name="code", order=2)
    private CodeDt myCode;
    
	@Child(name="display", order=3)
    private StringDt myDisplay;
	
	@Child(name="primary", order=4)
    private BooleanDt myPrimary;
	
	@Child(name="assigner", order=5)
	@ChildResource(types= {ValueSet.class})
	private ResourceReference myAssigner;

	public UriDt getSystem() {
		return mySystem;
	}

	public void setSystem(UriDt theSystem) {
		mySystem = theSystem;
	}

	public StringDt getVersion() {
		return myVersion;
	}

	public void setVersion(StringDt theVersion) {
		myVersion = theVersion;
	}

	public CodeDt getCode() {
		return myCode;
	}

	public void setCode(CodeDt theCode) {
		myCode = theCode;
	}

	public StringDt getDisplay() {
		return myDisplay;
	}

	public void setDisplay(StringDt theDisplay) {
		myDisplay = theDisplay;
	}

	public BooleanDt getPrimary() {
		return myPrimary;
	}

	public void setPrimary(BooleanDt thePrimary) {
		myPrimary = thePrimary;
	}

	public ResourceReference getAssigner() {
		return myAssigner;
	}

	public void setAssigner(ResourceReference theAssigner) {
		myAssigner = theAssigner;
	}

	
	
}
