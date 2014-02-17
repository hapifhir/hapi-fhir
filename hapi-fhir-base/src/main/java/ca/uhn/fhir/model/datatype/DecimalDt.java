package ca.uhn.fhir.model.datatype;

import java.math.BigDecimal;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;

@DatatypeDefinition(name="decimal")
public class DecimalDt extends BasePrimitiveDatatype {

	private BigDecimal myValue;
	
}
