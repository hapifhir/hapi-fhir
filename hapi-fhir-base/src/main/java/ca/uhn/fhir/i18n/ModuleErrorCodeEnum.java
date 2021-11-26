package ca.uhn.fhir.i18n;

public enum ModuleErrorCodeEnum {
	BASE("BAS"),
	JPA("JPA"),
	PLAIN("PLN"),
	STORAGE("STO"),
	VALIDATION("VAL"),
	SUBSCRIPTION("SUB"),
	BATCH("BAT"),
	MDM("MDM"),
	SQL_MIGRATION("SQL"),
	;

	private final String myShortCode;

	ModuleErrorCodeEnum(String theShortCode) {
		myShortCode = theShortCode;
	}
	
	@Override
	public String toString() {
		return myShortCode;
	}
}
