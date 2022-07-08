package ca.uhn.fhir.rest.https;

import org.apache.commons.io.FilenameUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TrustStoreInfo {

	private final String myFilePath;
	private final char[] myStorePass;
	private final KeyStoreType myType;
	private final String myAlias;

	public TrustStoreInfo(String theFilePath, String theStorePass, String theAlias) {
		myFilePath = theFilePath;
		myStorePass = toCharArrayOrNull(theStorePass);
		myAlias = theAlias;

		String extension = FilenameUtils.getExtension(myFilePath);
		myType = KeyStoreType.fromFileExtension(extension);
	}

	public String getFilePath() {
		return myFilePath;
	}

	public char[] getStorePass() {
		return myStorePass;
	}

	public KeyStoreType getType(){
		return myType;
	}

	private char[] toCharArrayOrNull(String theString){
		return isBlank(theString) ? null : theString.toCharArray();
	}

	public String getAlias() {
		return myAlias;
	}
}
