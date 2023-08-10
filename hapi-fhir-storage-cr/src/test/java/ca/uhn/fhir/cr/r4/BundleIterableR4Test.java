package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.common.BundleIterable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.collect.Iterables;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class BundleIterableR4Test extends BaseCrR4TestServer {
	private static final RequestDetails theRequestDetails = null;
	private static final String MY_TEST_DATA = "ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";

	@Test
	public void searchAllPatients() {
		// load bundle
		loadBundle(MY_TEST_DATA); //63 patients
		var bundle1 = searchPatient(); //return BundleIterable
		var firstCount = Iterables.size(bundle1); //count Patients
		assertEquals(63, firstCount);
	}

	public Iterable<IBaseResource> searchPatient() {
		var b = this.myDaoRegistry.getResourceDao("Patient")
			.search(new SearchParameterMap(), theRequestDetails);
		return new BundleIterable(theRequestDetails, b);
	}
}
