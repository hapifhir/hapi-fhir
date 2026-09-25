package ca.uhn.fhir.rest.api.server.bulk;

import java.util.ArrayList;
import java.util.List;

public class ConvertedFiles {

	private List<ConvertedFile> myFiles;

	/**
	 * Retrieves the current list of converted files
	 * (never null).
	 * We might want to consider making this a stream instead of a list
	 * if customers decide to convert our list of resources into an
	 * even larger list of "contents".
	 */
	public List<ConvertedFile> getFiles() {
		if (myFiles == null) {
			myFiles = new ArrayList<>();
		}
		return myFiles;
	}

	/**
	 * Adds a converted file to the collection
	 */
	public ConvertedFiles addFile(ConvertedFile theFile) {
		getFiles().add(theFile);
		return this;
	}
}
