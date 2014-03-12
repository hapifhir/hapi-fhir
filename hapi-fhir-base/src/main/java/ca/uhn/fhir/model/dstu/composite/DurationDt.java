package ca.uhn.fhir.model.dstu.composite;

public class DurationDt extends QuantityDt {

	// TODO: implement restricions
	
	// There SHALL be a code if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.
	// (f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='http://unitsofmeasure.org')
	
}
