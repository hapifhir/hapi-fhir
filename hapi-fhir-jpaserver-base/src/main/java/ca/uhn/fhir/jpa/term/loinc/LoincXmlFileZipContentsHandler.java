package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.term.IZipContentsHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;

public class LoincXmlFileZipContentsHandler implements IZipContentsHandler {

	private String myContents;

	@Override
	public void handle(Reader theReader, String theFilename) throws IOException {
		myContents = IOUtils.toString(theReader);
	}

	public String getContents() {
		return myContents;
	}
}
