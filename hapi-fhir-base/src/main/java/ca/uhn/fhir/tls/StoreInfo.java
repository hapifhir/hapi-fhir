package ca.uhn.fhir.tls;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.io.FilenameUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class StoreInfo {

	private final String myFilePath;
	private final PathType pathType;
	private final char[] myStorePass;
	private final String myAlias;
	private final KeyStoreType myType;

	public StoreInfo(String theFilePath, String theStorePass, String theAlias) {
		if(theFilePath.startsWith(PathType.RESOURCE.getPrefix())){
			myFilePath = theFilePath.substring(PathType.RESOURCE.getPrefix().length());
			pathType = PathType.RESOURCE;
		}
		else if(theFilePath.startsWith(PathType.FILE.getPrefix())){
			myFilePath = theFilePath.substring(PathType.FILE.getPrefix().length());
			pathType = PathType.FILE;
		}
		else {
			throw new StoreInfoException(Msg.code(2117)+"Invalid path prefix");
		}

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

	public PathType getPathType() {
		return pathType;
	}

	protected char[] toCharArray(String theString){
		return isBlank(theString) ? "".toCharArray() : theString.toCharArray();
	}

	public static class StoreInfoException extends RuntimeException {
		private static final long serialVersionUID = 1l;

		public StoreInfoException(String theMessage, Throwable theCause) {
			super(theMessage, theCause);
		}

		public StoreInfoException(String theMessage) {
			super(theMessage);
		}
	}
}
