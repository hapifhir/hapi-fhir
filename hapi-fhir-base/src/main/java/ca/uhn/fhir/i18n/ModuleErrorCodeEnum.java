package ca.uhn.fhir.i18n;

public enum ModuleErrorCodeEnum {
	BASE(1),
	JPA(2),
	PLAIN(3),
	STORE(4),
	VALID(5),
	SUBS(6),
	BATCH(7),
	MDM(8),
	SQL(10),
	;

	private final int myCode;

	ModuleErrorCodeEnum(int theCode) {
		myCode = theCode;
	}


	@Override
	public String toString() {
		return String.format("%02d", myCode);
	}
}
