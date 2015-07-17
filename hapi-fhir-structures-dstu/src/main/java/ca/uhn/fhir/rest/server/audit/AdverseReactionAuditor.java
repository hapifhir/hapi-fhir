package ca.uhn.fhir.rest.server.audit;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class AdverseReactionAuditor implements IResourceAuditor<AdverseReaction> {
	
	private AdverseReaction myResource = null;

	@Override
	public AdverseReaction getResource() {		
		return myResource;
	}

	@Override
	public void setResource(AdverseReaction resource) {
		myResource = resource;
	}

	@Override
	public boolean isAuditable() {		
		return myResource != null;
	}

	@Override
	public String getName() {
		if(myResource == null) return null;
		return "AdverseReaction:" + myResource.getIdentifierFirstRep().getValue();
	}

	@Override
	public BaseIdentifierDt getIdentifier() {
		if(myResource == null) return null;
		return myResource.getIdentifierFirstRep();
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public String getDescription() {		
		return null;
	}

	@Override
	public Map<String, String> getDetail() {
		Map<String, String> details = new HashMap<String, String>();				
		details.put("subject", myResource.getSubject().getReference().getValue());
		details.put("version", myResource.getId().getVersionIdPart());
		return details;
	}

	@Override
	public BaseCodingDt getSensitivity() {
		return null;
	}

}
