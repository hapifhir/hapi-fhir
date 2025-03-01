/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.parser;

import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

public class PreserveStringReader extends Reader {

	private final Reader myReader;

	private final StringWriter myWriter;

	public PreserveStringReader(Reader theReader) {
		super(theReader);
		myReader = theReader;
		myWriter = new StringWriter();
	}

	@Override
	public int read(@Nonnull char[] theBuffer, int theOffset, int theLength) throws IOException {
		int out = myReader.read(theBuffer, theOffset, theLength);
		if (out >= 0) {
			myWriter.write(theBuffer, theOffset, out);
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
