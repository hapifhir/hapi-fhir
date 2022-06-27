package ca.uhn.fhir.rest.https;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.ssl.PrivateKeyStrategy;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class KeyStoreInfo {

	private final String myFilePath;
	private final char[] myStorePass;
	private final char[] myKeyPass;
	private final String myAlias;
	private final KeyStoreType myType;

	public KeyStoreInfo(String theFilePath, String theStorePass, String theKeyPass, String theAlias) {
		myFilePath = theFilePath;
		myStorePass = toCharArrayOrNull(theStorePass);
		myKeyPass = toCharArrayOrNull(theKeyPass);
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

	public char[] getKeyPass() {
		return myKeyPass;
	}

	public String getAlias() {
		return myAlias;
	}

	public KeyStoreType getType(){
		String extension = FilenameUtils.getExtension(myFilePath);
		return KeyStoreType.fromFileExtension(extension);
	}

	private char[] toCharArrayOrNull(String theString){
		return isBlank(theString) ? null : theString.toCharArray();
	}
}
