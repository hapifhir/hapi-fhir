/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.dao;

import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;

public class JpaPidIdentifierBridge implements IdentifierBridge<JpaPid> {
	@Override
	public String toDocumentIdentifier(JpaPid propertyValue, IdentifierBridgeToDocumentIdentifierContext context) {
		return propertyValue.getId().toString();
	}

	@Override
	public JpaPid fromDocumentIdentifier(
			String documentIdentifier, IdentifierBridgeFromDocumentIdentifierContext context) {
		long pid = Long.parseLong(documentIdentifier);
		return JpaPid.fromId(pid);
	}
}
