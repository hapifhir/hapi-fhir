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

import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class MedicationStatementAuditor implements IResourceAuditor<MedicationStatement> {

	MedicationStatement myMedicationStatement;
	
	@Override
	public boolean isAuditable() {
		return myMedicationStatement != null; 
	}

	@Override
	public String getName() {
		if(myMedicationStatement != null){
			Medication m = (Medication) myMedicationStatement.getMedication().getResource();
			if(m != null){
				return "Medication Statement: " + m.getName().getValue();
			}			
			return "Medication Statement: " + myMedicationStatement.getId().getValueAsString(); //if we don't have a medication name, use the id as the name
		}
		return null; 		
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myMedicationStatement != null){
			return myMedicationStatement.getIdentifierFirstRep();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public MedicationStatement getResource() {
		return myMedicationStatement;
	}

	@Override
	public void setResource(MedicationStatement theMedicationPrescription) {
		myMedicationStatement = theMedicationPrescription;
	}

	@Override
	public String getDescription() {		
		return null; //name and ID should suffice for audit purposes
	}

	@Override
	public Map<String, String> getDetail() {
		return null; //no additional details required for audit?
	}

	@Override
	public BaseCodingDt getSensitivity() {		
		return null; //no sensitivity indicated in MedicationStatement
	}

}
