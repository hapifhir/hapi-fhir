package ca.uhn.fhir.parser.i391;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum OutcomeEnum {

	ITEM1("item1"), 
	ITEM2("item2");

	public static final IValueSetEnumBinder<OutcomeEnum> VALUESET_BINDER = new OutcomeBinder();

	private final String code;

	OutcomeEnum(String code) {
		this.code = code;
	}

}