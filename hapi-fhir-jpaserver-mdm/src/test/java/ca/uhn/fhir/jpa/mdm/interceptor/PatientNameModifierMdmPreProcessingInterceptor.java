package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PatientNameModifierMdmPreProcessingInterceptor {

	List<String> myNamesToIgnore = asList("John Doe", "Jane Doe");

	String myNewValue = EMPTY;

	@Hook(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)
	public IBaseResource invoke(IBaseResource theResource) {

		HumanName patientHumanName = ((Patient) theResource).getNameFirstRep();
		String patientGivenAndSurname = patientHumanName.getNameAsSingleString();

		if (myNamesToIgnore.stream().anyMatch(toIgnore -> toIgnore.equalsIgnoreCase(patientGivenAndSurname))) {
			patientHumanName.setFamily(myNewValue);
		}

		return theResource;
	}

	public void setNewValue(String theNewValue) {
		myNewValue = theNewValue;
	}
}
