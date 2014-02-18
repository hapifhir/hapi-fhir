package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.enm.QuantityComparator;

@DatatypeDef(name="Quantity")
public class QuantityDt extends BaseCompositeDatatype {

	@Child(name="value",order=0, min=0, max=1)
	private DecimalDt myValue;
	
	@Child(name="comparator",order=1, min=0, max=1)
	private QuantityComparator myComparator;
	
	@Child(name="units",order=2, min=0, max=1)
	private StringDt myUnits;
	
	@Child(name="system",order=3, min=0, max=1)
	@Constraint(coRequirements= {"code"})
	private UriDt mySystem;
	
	@Child(name="code",order=4, min=0, max=1)
	@Constraint(coRequirements= {"system"})
	private CodeDt myCode;
	
}
