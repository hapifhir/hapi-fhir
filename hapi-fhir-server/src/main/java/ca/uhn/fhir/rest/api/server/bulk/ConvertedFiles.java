package ca.uhn.fhir.rest.api.server.bulk;

import java.util.ArrayList;
import java.util.List;

public class ConvertedFiles {

	private List<ConvertedFile> myFiles;

	/**
	 * Retrieves the current list of converted files
	 * (never null)
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
