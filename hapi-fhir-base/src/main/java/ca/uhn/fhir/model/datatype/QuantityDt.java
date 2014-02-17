package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.Constraint;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.enm.QuantityComparator;

@DatatypeDefinition(name="Quantity")
public class QuantityDt extends BaseCompositeDatatype {

	@ResourceElement(name="value",order=0, min=0, max=1)
	private DecimalDt myValue;
	
	@ResourceElement(name="comparator",order=1, min=0, max=1)
	private QuantityComparator myComparator;
	
	@ResourceElement(name="units",order=2, min=0, max=1)
	private StringDt myUnits;
	
	@ResourceElement(name="system",order=3, min=0, max=1)
	@Constraint(coRequirements= {"code"})
	private UriDt mySystem;
	
	@ResourceElement(name="code",order=4, min=0, max=1)
	@Constraint(coRequirements= {"system"})
	private CodeDt myCode;
	
}
