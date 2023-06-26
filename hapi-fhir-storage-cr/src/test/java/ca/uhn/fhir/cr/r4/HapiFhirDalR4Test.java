package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.common.HapiFhirDal;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * This class tests the functionality of HapiFhirDal operations inside the cr module
 */
@ExtendWith(SpringExtension.class)
public class HapiFhirDalR4Test extends BaseCrR4Test {
	private static final String MY_TEST_DATA = "ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";

	@Autowired
	JpaStorageSettings myJpaStorageSettings;

	@Test
	void canSearchMoreThan50Patients(){
		loadBundle(MY_TEST_DATA); // load 63 patients

		myJpaStorageSettings.setFetchSizeDefaultMaximum(100);

		HapiFhirDal hapiFhirDal = new HapiFhirDal(this.getDaoRegistry(), null);
		// get all patient resources posted
		var result = hapiFhirDal.search("Patient");
		// count all resources in result
		int counter = 0;
		for (Object i: result) {
			counter++;
		}
		//verify all patient resources captured
		assertEquals(63, counter, "Patient search results don't match available resources");
	}

}
