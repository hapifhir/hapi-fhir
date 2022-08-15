package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.util.CoverageIgnore;

final class AllowStatusChangeMetadata extends ResourceMetadataKeyEnum<Object> {
	private static final long serialVersionUID = 1;

	AllowStatusChangeMetadata(String theValue) {
		super(theValue);
	}

	@CoverageIgnore
	@Override
	public Object get(IResource theResource) {
		throw new UnsupportedOperationException(Msg.code(805));
	}

	@CoverageIgnore
	@Override
	public void put(IResource theResource, Object theObject) {
		throw new UnsupportedOperationException(Msg.code(806));
	}
}
