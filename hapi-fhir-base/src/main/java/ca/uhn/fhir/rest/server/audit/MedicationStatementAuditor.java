package ca.uhn.fhir.rest.server.audit;

import java.util.List;

import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectDetail;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
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
				return m.getName().getValue();
			}			
			return myMedicationStatement.getId().getValueAsString(); //if we don't have a medication name, use the id as the name
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
	public List<ObjectDetail> getDetail() {
		return null; //no additional details required for audit?
	}

	@Override
	public SecurityEventObjectSensitivityEnum getSensitivity() {		
		return null; //no sensitivity indicated in MedicationStatement
	}

}
