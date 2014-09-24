package ca.uhn.fhir.rest.server.audit;

import java.util.List;

import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectDetail;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class MedicationPrescriptionAuditor implements IResourceAuditor<MedicationPrescription> {

	MedicationPrescription myMedicationPrescription;
	
	@Override
	public boolean isAuditable() {
		return myMedicationPrescription != null; //if we have a medication prescription, we want to audit it
	}

	@Override
	public String getName() {
		if(myMedicationPrescription != null){
			if(myMedicationPrescription.getMedication() != null){
				Medication m = (Medication) myMedicationPrescription.getMedication().getResource();
				if(m != null){
					return m.getName().getValue();
				}
			}
			return myMedicationPrescription.getId().getValueAsString(); //if we don't have a medication name, use the id as the name
		}
		return ""; //no medication prescription, nothing to do here		
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myMedicationPrescription != null){
			return myMedicationPrescription.getIdentifierFirstRep();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public MedicationPrescription getResource() {
		return myMedicationPrescription;
	}

	@Override
	public void setResource(MedicationPrescription theMedicationPrescription) {
		myMedicationPrescription = theMedicationPrescription;
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
		return null; //no sensitivity indicated in MedicationPrescription
	}

}
