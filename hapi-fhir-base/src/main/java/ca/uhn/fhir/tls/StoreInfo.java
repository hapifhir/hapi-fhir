package ca.uhn.fhir.tls;

import org.apache.commons.io.FilenameUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class StoreInfo {

	private final String myFilePath;
	private final char[] myStorePass;
	private final String myAlias;
	private final KeyStoreType myType;

	public StoreInfo(String theFilePath, String theStorePass, String theAlias) {
		myFilePath = theFilePath;
		myStorePass = toCharArray(theStorePass);
		myAlias = theAlias;

		String extension = FilenameUtils.getExtension(theFilePath);
		myType = KeyStoreType.fromFileExtension(extension);
	}

	public String getFilePath() {
		return myFilePath;
	}

	public char[] getStorePass() {
		return myStorePass;
	}

	public String getAlias() {
		return myAlias;
	}

	public KeyStoreType getType() {
		return myType;
	}

	protected char[] toCharArray(String theString){
		return isBlank(theString) ? new char[0] : theString.toCharArray();
	}
}
