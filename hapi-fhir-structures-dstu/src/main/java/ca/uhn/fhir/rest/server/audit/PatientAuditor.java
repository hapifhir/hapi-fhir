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
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class PatientAuditor implements IResourceAuditor<Patient> {
	
	private Patient myPatient;

	@Override
	public boolean isAuditable() {
		return myPatient != null; //if we have a patient, we want to audit it
	}

	@Override
	public Patient getResource() {
		return myPatient;
	}

	@Override
	public void setResource(Patient thePatient) {
		myPatient = thePatient;
	}

	@Override
	public String getName() {
		if(myPatient != null){
			return "Patient: " + myPatient.getNameFirstRep().getNameAsSingleString();
		}
		return null;
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myPatient != null){
			return myPatient.getIdentifierFirstRep();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {	 
		return SecurityEventObjectTypeEnum.PERSON;
	}

	@Override
	public String getDescription() {
		return null; //name + identifier ought to suffice?
	}

	@Override
	public Map<String, String> getDetail() {
		if(myPatient != null){
			List<IdentifierDt> ids = myPatient.getIdentifier();
			if(ids != null && !ids.isEmpty()){
				Map<String, String> detailMap = new HashMap<String, String>();
				for(IdentifierDt id: ids){
					String key = id.getSystem().getValueAsString();
					String value = id.getValue().getValueAsString();
					detailMap.put(key, value);
				}
				return detailMap;
			}
			
		}
		return null;
	}

	@Override
	public BaseCodingDt getSensitivity() {		
		return null; //override to include things like locked patient records
	}

}
