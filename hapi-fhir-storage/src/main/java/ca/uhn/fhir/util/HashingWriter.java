/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.io.Writer;

/**
 * A Writer implementation that calculates a hash of the resource content.
 */
@SuppressWarnings("UnstableApiUsage")
public class HashingWriter extends Writer {

	private final Hasher myHasher = Hashing.goodFastHash(128).newHasher();

	@Override
	public void write(@Nonnull char[] theCbuf, final int theOffset, final int theLength) {
		for (int i = theOffset; i < theOffset + theLength; i++) {
			myHasher.putChar(theCbuf[i]);
		}
	}

	@Override
	public void flush() {
		// nothing
	}

	@Override
	public void close() {
		// nothing
	}

	public void append(FhirContext theFhirContext, IBaseResource theResource) {
		IParser parser = theFhirContext.newJsonParser().setPrettyPrint(false);
		append(parser, theResource);
	}

	public void append(IParser theParser, IBaseResource theResource) {
		try {
			theParser.encodeResourceToWriter(theResource, this);
		} catch (IOException e) {
			// This shouldn't happen since we don't do any IO in this writer
			throw new InternalErrorException(Msg.code(2785) + "Failed to calculate resource hash", e);
		}
	}

	public boolean matches(HashingWriter theHashingWriter) {
		return getHash().equals(theHashingWriter.myHasher.hash());
	}

	@Nonnull
	public HashCode getHash() {
		return myHasher.hash();
	}
}
