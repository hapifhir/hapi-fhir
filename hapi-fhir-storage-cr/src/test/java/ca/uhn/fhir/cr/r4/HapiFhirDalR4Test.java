package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.common.HapiFhirDal;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the functionality of HapiFhirDal operations inside the cr module
 */
@ExtendWith(SpringExtension.class)
public class HapiFhirDalR4Test extends BaseCrR4TestServer {
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

	@Test
	void canSearchVersionURL(){
		// load measure resource with Library url containing "|", this is the only component of test resource used.
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-HapiFhirDalTestLibrary.json");
		HapiFhirDal hapiFhirDal = new HapiFhirDal(this.getDaoRegistry(), null);

		// library url from loaded measure resource
		String url = "http://content.smilecdr.com/fhir/dqm/Library/ImmunizationStatusRoutine|2.0.1";
		// search for resource given url
		Iterable<IBaseResource> result = hapiFhirDal.searchByUrl("Library", url);
		Iterator<IBaseResource> resultIter = result.iterator();
		// validate Iterable contains a resource
		assertTrue(resultIter.hasNext());
		// get resource
		IBaseResource finalResult = resultIter.next();
		// validate resource exists
		assertNotNull(finalResult);
		}


}
