package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.enm.IdentifierUseEnum;
import ca.uhn.fhir.model.resource.Organization;

@DatatypeDef(name="identifier")
public class IdentifierDt extends BaseCompositeDatatype {

	@Child(name="use", order=0)
	@CodeableConceptElement(type=IdentifierUseEnum.class)
	private CodeDt<IdentifierUseEnum> myUse;
	
	@Child(name="label", order=1)
	private StringDt myLabel;
	
	@Child(name="system", order=2)
	private UriDt mySystem;
	
	@Child(name="value", order=3)
	private StringDt myValue;

	@Child(name="period", order=4)
	private PeriodDt myPeriod;

	@Child(name="assigner", order=5)
	@ChildResource(types= {Organization.class})
	private ResourceReference myAssigner;

	public CodeDt<IdentifierUseEnum> getUse() {
		return myUse;
	}

	public void setUse(CodeDt<IdentifierUseEnum> theUse) {
		myUse = theUse;
	}

	public StringDt getLabel() {
		return myLabel;
	}

	public void setLabel(StringDt theLabel) {
		myLabel = theLabel;
	}

	public UriDt getSystem() {
		return mySystem;
	}

	public void setSystem(UriDt theSystem) {
		mySystem = theSystem;
	}

	public StringDt getValue() {
		return myValue;
	}

	public void setValue(StringDt theValue) {
		myValue = theValue;
	}

	public PeriodDt getPeriod() {
		return myPeriod;
	}

	public void setPeriod(PeriodDt thePeriod) {
		myPeriod = thePeriod;
	}

	public ResourceReference getAssigner() {
		return myAssigner;
	}

	public void setAssigner(ResourceReference theAssigner) {
		myAssigner = theAssigner;
	}

	
}
