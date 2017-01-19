package ca.uhn.fhir.rest.server.audit;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class ObservationAuditor implements IResourceAuditor<Observation> {
	
	private Observation myResource = null;

	@Override
	public Observation getResource() {
		return myResource;
	}

	@Override
	public void setResource(Observation resource) {
		myResource = resource;
	}

	@Override
	public boolean isAuditable() {
		return myResource != null;
	}

	@Override
	public String getName() {
		if(myResource == null) return null;
		return "Observation:" + myResource.getName().getCodingFirstRep().getCode().getValue();
	}

	@Override
	public BaseIdentifierDt getIdentifier() {
		return myResource.getIdentifier();
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
		if(myResource == null) return null;
		Map<String, String> details = new HashMap<String, String>();
		details.put("dateIssued", myResource.getIssued().getValueAsString());
		details.put("version", myResource.getId().getVersionIdPart());
		details.put("subject", myResource.getSubject().getReference().getValue());
		return details;
	}

	@Override
	public BaseCodingDt getSensitivity() {
		return null;
	}

}
