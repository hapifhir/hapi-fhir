package ca.uhn.fhir.jpa.searchparam.matcher;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.ResourceLookup;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

@Service
public class InlineResourceLinkResolver implements IResourceLinkResolver {

	@Override
	public IResourceLookup findTargetResource(RuntimeSearchParam theNextSpDef, String theNextPathsUnsplit, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest) {

		/*
		 * TODO: JA - This gets used during runtime in-memory matching for subscription. It's not
		 * really clear if it's useful or not.
		 */

		if (true) throw new UnsupportedOperationException();

		ResourceTable target;
		target = new ResourceTable();
		target.setResourceType(theTypeString);
		if (theNextId.isIdPartValidLong()) {
			return new ResourceLookup(theTypeString, theNextId.getIdPartAsLong(), null);
		} else {
			return new ResourceLookup(theTypeString, null, null);
		}

	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		// When resolving reference in-memory for a single resource, there's nothing to validate
	}
}
