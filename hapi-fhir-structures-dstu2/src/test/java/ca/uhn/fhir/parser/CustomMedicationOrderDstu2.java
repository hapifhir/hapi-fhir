package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;

/**
 * See #368
 */
@ResourceDef(/*name="MedicationOrder"*/)
public class CustomMedicationOrderDstu2 extends MedicationOrder {

	private static final long serialVersionUID = 1L;
	
	@Child(name = "medication", order = Child.REPLACE_PARENT, min = 1, max = 1, summary = false, modifier = false, type = { Medication.class })
	private ResourceReferenceDt myMedication;

	@Override
	public ResourceReferenceDt getMedication() {
		return myMedication;
	}

	public void setMedication(ResourceReferenceDt theMedication) {
		myMedication = theMedication;
	}

}
