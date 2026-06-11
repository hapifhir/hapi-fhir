package ca.uhn.fhir.jpa.term;

import org.apache.commons.io.input.BOMInputStream;

import java.io.InputStream;

public class NonClosableBOMInputStream extends BOMInputStream {
	public NonClosableBOMInputStream(InputStream theWrap) {
		super(theWrap);
	}

	@Override
	public void close() {
		// nothing
	}
}
