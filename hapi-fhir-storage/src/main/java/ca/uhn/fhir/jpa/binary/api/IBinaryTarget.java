package ca.uhn.fhir.jpa.binary.api;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	@SuppressWarnings("unchecked")
	default Optional<String> getAttachmentId() {
		return getTarget()
			.getExtension()
			.stream()
			.filter(t -> HapiExtensions.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
			.filter(t -> t.getValue() instanceof IPrimitiveType)
			.map(t -> (IPrimitiveType<String>) t.getValue())
			.map(t -> t.getValue())
			.filter(t -> isNotBlank(t))
			.findFirst();
	}
}
