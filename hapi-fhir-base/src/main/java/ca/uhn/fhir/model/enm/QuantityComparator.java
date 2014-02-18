package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;

@CodeTableDef(tableId=14, name="quantity-comparator")
public enum QuantityComparator implements ICodeEnum {

	@EnumeratedCodeValue("<")
	@Description("The actual value is less than the given value.")
	LESSTHAN,

	@EnumeratedCodeValue("<=")
	@Description("The actual value is less than or equal to the given value.")
	LESSTHAN_OR_EQUAL,

	@EnumeratedCodeValue(">=")
	@Description("The actual value is greater than or equal to the given value.")
	GREATERTHAN_OR_EQUAL,

	@EnumeratedCodeValue(">")
	@Description("The actual value is greater than the given value.")
	GREATERTHAN;

}
