/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.binary.api;

import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

import java.util.Optional;

/**
 * Wraps an Attachment datatype or Binary resource, since they both
 * hold binary content but don't look entirely similar
 */
public interface IBinaryTarget {

	void setSize(Integer theSize);

	String getContentType();

	void setContentType(String theContentType);

	byte[] getData();

	void setData(byte[] theBytes);

	IBaseHasExtensions getTarget();

	default Optional<String> getAttachmentId() {
		return AttachmentUtil.getFirstExtension(getTarget(), HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
	}

	default Optional<String> getHashExtension() {
		return AttachmentUtil.getFirstExtension(getTarget(), HapiExtensions.EXT_EXTERNALIZED_BINARY_HASH_SHA_256);
	}
}
