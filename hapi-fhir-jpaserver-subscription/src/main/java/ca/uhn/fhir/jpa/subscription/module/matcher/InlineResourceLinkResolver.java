package ca.uhn.fhir.jpa.subscription.module.matcher;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

@Service
public class InlineResourceLinkResolver implements IResourceLinkResolver {

	@Override
	public ResourceTable findTargetResource(RuntimeSearchParam theNextSpDef, String theNextPathsUnsplit, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, String theId) {
		ResourceTable target;
		target = new ResourceTable();
		target.setResourceType(theTypeString);
		if (theNextId.isIdPartValidLong()) {
			target.setId(theNextId.getIdPartAsLong());
		} else {
			ForcedId forcedId = new ForcedId();
			forcedId.setForcedId(theId);
			target.setForcedId(forcedId);
		}
		return target;
	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		// When resolving reference in-memory for a single resource, there's nothing to validate
	}
}
