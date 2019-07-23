package ca.uhn.fhir.util;

import com.google.common.io.CountingInputStream;

import java.io.IOException;
import java.io.InputStream;

public class CountingAndLimitingInputStream extends InputStream {
	private final int myMaxBytes;
	private final CountingInputStream myWrap;

	@Override
	public int read() throws IOException {
		int retVal = myWrap.read();
		validateCount();
		return retVal;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int retVal = myWrap.read(b, off, len);
		validateCount();
		return retVal;
	}

	@Override
	public int read(byte[] theRead) throws IOException {
		int retVal = myWrap.read(theRead);
		validateCount();
		return retVal;
	}

	private void validateCount() throws IOException {
		if (myWrap.getCount() > myMaxBytes) {
			throw new IOException("Stream exceeds maximum allowable size: " + myMaxBytes);
		}
	}


	/**
	 * Wraps another input stream, counting the number of bytes read.
	 *
	 * @param theWrap the input stream to be wrapped
	 */
	public CountingAndLimitingInputStream(InputStream theWrap, int theMaxBytes) {
		myWrap = new CountingInputStream(theWrap);
		myMaxBytes = theMaxBytes;
	}
}
