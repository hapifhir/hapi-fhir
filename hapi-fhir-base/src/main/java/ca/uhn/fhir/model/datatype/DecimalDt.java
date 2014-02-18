package ca.uhn.fhir.model.datatype;

import java.math.BigDecimal;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="decimal")
public class DecimalDt extends BasePrimitiveDatatype {

	private BigDecimal myValue;
	
}
