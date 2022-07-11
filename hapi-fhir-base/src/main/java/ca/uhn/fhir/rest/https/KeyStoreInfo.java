package ca.uhn.fhir.rest.https;

public class KeyStoreInfo extends StoreInfo {

	private final char[] myKeyPass;

	public KeyStoreInfo(String theFilePath, String theStorePass, String theKeyPass, String theAlias) {
		super(theFilePath, theStorePass, theAlias);
		this.myKeyPass = toCharArray(theKeyPass);
	}

	public char[] getKeyPass() {
		return myKeyPass;
	}
}
