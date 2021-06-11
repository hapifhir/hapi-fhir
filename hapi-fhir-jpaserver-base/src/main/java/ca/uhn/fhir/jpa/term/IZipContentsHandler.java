package ca.uhn.fhir.jpa.term;

import java.io.IOException;
import java.io.Reader;

@FunctionalInterface
public interface IZipContentsHandler {

	void handle(Reader theReader, String theFilename) throws IOException;

}
