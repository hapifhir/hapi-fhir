package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
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
			throw new IOException(Msg.code(1807) + "Stream exceeds maximum allowable size: " + myMaxBytes);
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
