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

import java.io.InputStream;

public class AttachmentDetails {
	private final InputStream myInputStream;
	private final AttachmentContentTypeEnum myContentType;
	private final String myFilename;

	public AttachmentDetails(InputStream theInputStream, AttachmentContentTypeEnum theContentType, String theFilename) {
		myInputStream = theInputStream;
		myContentType = theContentType;
		myFilename = theFilename;
	}

	public InputStream getInputStream() {
		return myInputStream;
	}

	public AttachmentContentTypeEnum getContentType() {
		return myContentType;
	}

	public String getFilename() {
		return myFilename;
	}

	public static Builder build() {
		return new Builder();
	}

	public static class Builder {
		private InputStream myInputStream;
		private AttachmentContentTypeEnum myContentType;
		private String myFilename;

		public Builder withInputStream(InputStream theInputStream) {
			myInputStream = theInputStream;
			return this;
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

		public AttachmentDetails build() {
			return new AttachmentDetails(myInputStream, myContentType, myFilename);
		}
	}
}
