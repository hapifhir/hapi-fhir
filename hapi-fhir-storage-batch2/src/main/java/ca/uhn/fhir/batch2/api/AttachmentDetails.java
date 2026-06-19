/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class AttachmentDetails {
	public static final int NO_MAXIMUM_SIZE = -1;
	private final InputStream myInputStream;
	private final AttachmentContentTypeEnum myContentType;
	private final String myFilename;
	private final Integer myMaximumSize;

	private AttachmentDetails(
			@Nonnull InputStream theInputStream,
			@Nonnull AttachmentContentTypeEnum theContentType,
			@Nullable String theFilename,
			@Nonnull Integer theMaximumSize) {
		Validate.notNull(theInputStream, "theInputStream must not be null");
		Validate.notNull(theContentType, "theContentType must not be null");
		Validate.notNull(
				theMaximumSize,
				"No maximum size has been specified, and the maximum size has not been explicitly declared as unlimited.");
		Validate.isTrue(
				theMaximumSize > 0 || theMaximumSize == NO_MAXIMUM_SIZE, "Invalid maximum size: %s", theMaximumSize);
		myInputStream = theInputStream;
		myContentType = theContentType;
		myFilename = theFilename;
		myMaximumSize = theMaximumSize;
	}

	@Nonnull
	public InputStream getInputStream() {
		return myInputStream;
	}

	@Nonnull
	public AttachmentContentTypeEnum getContentType() {
		return myContentType;
	}

	@Nullable
	public String getFilename() {
		return myFilename;
	}

	@Nonnull
	public <T> T getContentsAsJson(Class<T> theType) throws IOException {
		return JsonUtil.deserialize(getInputStream(), theType);
	}

	@Nonnull
	public Optional<Integer> getMaximumSize() {
		return myMaximumSize.equals(NO_MAXIMUM_SIZE) ? Optional.empty() : Optional.of(myMaximumSize);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private InputStream myInputStream;
		private AttachmentContentTypeEnum myContentType;
		private String myFilename;
		private Integer myMaximumSize;

		public Builder withInputStream(InputStream theInputStream) {
			myInputStream = theInputStream;
			return this;
		}

		public Builder withBytes(byte[] theBytes) {
			return withInputStream(new ByteArrayInputStream(theBytes));
		}

		public Builder withContentType(AttachmentContentTypeEnum theContentType) {
			myContentType = theContentType;
			return this;
		}

		/**
		 * The attachment filename, if any. This can be left {@literal null}. If provided, any existing attachment
		 * with the same filename for the given job instance will be replaced.
		 */
		public Builder withFilename(String theFilename) {
			myFilename = theFilename;
			return this;
		}

		/**
		 * Specifies a maximum number of bytes to allow for storage. If too many bytes are submitted,
		 * an {@link ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException} will be thrown.
		 * @param theMaximumSize The maximum number of bytes to receive. Must be > 0.
		 *
		 * @see	#withNoMaximumSize()
		 */
		public Builder withMaximumSize(int theMaximumSize) {
			Validate.isTrue(
					theMaximumSize > 0,
					"Maximum size must be greater than 0. Use withNoMaximumSize() to allow unlimited sized.");
			myMaximumSize = theMaximumSize;
			return this;
		}

		/**
		 * Specifies that there should be no maximum size for the attachment. This should never
		 * be used for any user-submitted attachment types, such as attachments uploaded via
		 * a REST endpoint.
		 *
		 * @see #withMaximumSize(int)
		 */
		public Builder withNoMaximumSize() {
			myMaximumSize = NO_MAXIMUM_SIZE;
			return this;
		}

		public AttachmentDetails build() {
			return new AttachmentDetails(myInputStream, myContentType, myFilename, myMaximumSize);
		}
	}
}
