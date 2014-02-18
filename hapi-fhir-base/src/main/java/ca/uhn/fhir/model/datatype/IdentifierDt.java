package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.Datatype;
import ca.uhn.fhir.model.enm.IdentifierUseEnum;
import ca.uhn.fhir.model.resource.Organization;

@Datatype(name="identifier")
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

}
