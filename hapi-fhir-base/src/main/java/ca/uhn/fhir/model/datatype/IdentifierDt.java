package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.ResourceReferenceElement;
import ca.uhn.fhir.model.enm.IdentifierUseEnum;
import ca.uhn.fhir.model.resource.Organization;

@DatatypeDefinition(name="identifier")
public class IdentifierDt extends BaseCompositeDatatype {

	@ResourceElement(name="use", order=0)
	@CodeableConceptElement(type=IdentifierUseEnum.class)
	private CodeDt<IdentifierUseEnum> myUse;
	
	@ResourceElement(name="label", order=1)
	private StringDt myLabel;
	
	@ResourceElement(name="system", order=2)
	private UriDt mySystem;
	
	@ResourceElement(name="value", order=3)
	private StringDt myValue;

	@ResourceElement(name="period", order=4)
	private PeriodDt myPeriod;

	@ResourceElement(name="assigner", order=5)
	@ResourceReferenceElement(type=Organization.class)
	private ResourceReference<Organization> myAssigner;

}
