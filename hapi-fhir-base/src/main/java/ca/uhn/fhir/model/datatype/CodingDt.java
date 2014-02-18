package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.resource.ValueSet;


@DatatypeDef(name="Coding")
public class CodingDt extends BaseCompositeDatatype {

	@Child(name="system", order=0)
	private UriDt mySystem;
    
	@Child(name="version", order=1)
    private StringDt myVersion;

	@Child(name="code", order=2)
    private CodeDt<?> myCode;
    
	@Child(name="display", order=3)
    private StringDt myDisplay;
	
	@Child(name="primary", order=4)
    private BooleanDt myPrimary;
	
	@Child(name="valueSet", order=5)
	@ChildResource(types= {ValueSet.class})
	private ResourceReference myAssigner;

}
