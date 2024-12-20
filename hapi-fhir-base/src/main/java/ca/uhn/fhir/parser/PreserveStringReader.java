package ca.uhn.fhir.parser;

import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

public class PreserveStringReader extends Reader {

	private final Reader myReader;

	private final StringWriter myWriter;

	public PreserveStringReader(Reader theReader) {
		System.out.println("reader " + theReader.getClass().getSimpleName());
		myReader = theReader;
		myWriter = new StringWriter();
	}

	@Override
	public int read(@Nonnull char[] theBuffer, int theOffset, int theLength) throws IOException {
		int out = myReader.read(theBuffer, theOffset, theLength);
		if (out >= 0) {
			for (int i = theOffset; i < theLength; i++) {
				// ignore null char
				if (theBuffer[i] != '\u0000') {
					myWriter.write(theBuffer, i, 1);
				}
			}
		}
		return out;
	}

	@Override
	public void close() throws IOException {
		myReader.close();
		myWriter.close();
	}

	public boolean hasString() {
		return myWriter.getBuffer().length() > 0;
	}

	public String toString() {
		return myWriter.toString();
	}
}
