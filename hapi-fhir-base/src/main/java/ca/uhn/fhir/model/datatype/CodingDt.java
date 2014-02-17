package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.ResourceReferenceElement;
import ca.uhn.fhir.model.resource.ValueSet;


@DatatypeDefinition(name="Coding")
public class CodingDt extends BaseCompositeDatatype {

	@ResourceElement(name="system", order=0)
	private UriDt mySystem;
    
	@ResourceElement(name="version", order=1)
    private StringDt myVersion;

	@ResourceElement(name="code", order=2)
    private CodeDt<?> myCode;
    
	@ResourceElement(name="display", order=3)
    private StringDt myDisplay;
	
	@ResourceElement(name="primary", order=4)
    private BooleanDt myPrimary;
	
	@ResourceElement(name="valueSet", order=5)
	@ResourceReferenceElement(type=ValueSet.class)
	private ResourceReference<ValueSet> myAssigner;

}
