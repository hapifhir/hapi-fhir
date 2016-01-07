package ca.uhn.fhir.rest.server.audit;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class EncounterAuditor implements IResourceAuditor<Encounter> {
	
	private Encounter myEncounter;

	@Override
	public Encounter getResource() {
		return myEncounter;
	}

	@Override
	public void setResource(Encounter theEncounter) {
		myEncounter = theEncounter;		
	}

	@Override
	public boolean isAuditable() {
		return myEncounter != null;
	}

	@Override
	public String getName() {
		if(myEncounter != null){			
			String id = myEncounter.getIdentifierFirstRep().getValue().getValue();
			String system = myEncounter.getIdentifierFirstRep().getSystem().getValueAsString();
			String service = myEncounter.getServiceProvider().getDisplay().getValue();
			return "Encounter: " + id + "/" + system + ": " + service;
		}
		return null;
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myEncounter != null){
			return myEncounter.getIdentifierFirstRep();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public String getDescription() {
		if(myEncounter != null){						
			String type = myEncounter.getTypeFirstRep().getText().getValue();
			String status = myEncounter.getStatus().getValueAsString();
			String startDate = myEncounter.getPeriod().getStart().getValueAsString();
			String endDate = myEncounter.getPeriod().getEnd().getValueAsString();
			return "Encounter: " + type + ": " + status +", "+ startDate + " - " + endDate; 			
		}
		return null;
	}

	@Override
	public Map<String, String> getDetail() {
		Map<String, String> details = new HashMap<String, String>();		
		details.put("startDate", myEncounter.getPeriod().getStart().getValueAsString());
		details.put("endDate", myEncounter.getPeriod().getEnd().getValueAsString());
		details.put("service", myEncounter.getServiceProvider().getDisplay().getValue());
		details.put("type", myEncounter.getTypeFirstRep().getText().getValue());
		details.put("status", myEncounter.getStatus().getValueAsString());
		details.put("subject", myEncounter.getSubject().getReference().getValue());
		return details;
	}

	@Override
	public BaseCodingDt getSensitivity() {
		//override this method to provide sensitivity information about the visit
		return null;
	}

}
