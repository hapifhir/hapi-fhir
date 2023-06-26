package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import static java.util.Arrays.asList;

public class PatientNameModifierMdmPreProcessingInterceptor {

	List<String> myNamesToIgnore = asList("John Doe", "Jane Doe");

	@Hook(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)
	public void invoke(IBaseResource theResource) {

		Patient patient = (Patient) theResource;
		List<HumanName> nameList = patient.getName();

		List<HumanName> validHumanNameList = nameList.stream()
			.filter(theHumanName -> !myNamesToIgnore.contains(theHumanName.getNameAsSingleString()))
			.toList();

		patient.setName(validHumanNameList);
	}

}
