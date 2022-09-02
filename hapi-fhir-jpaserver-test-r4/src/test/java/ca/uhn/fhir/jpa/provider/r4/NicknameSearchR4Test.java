package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NicknameSearchR4Test extends BaseResourceProviderR4Test {
	@Autowired
	NicknameInterceptor myNicknameInterceptor;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myInterceptorRegistry.unregisterInterceptor(myNicknameInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myInterceptorRegistry.registerInterceptor(myNicknameInterceptor);
	}

	@Test
	public void testExpandNickname() {
		Patient patient1 = new Patient();
		patient1.getNameFirstRep().addGiven("ken");
		myClient.create().resource(patient1).execute();

		Patient patient2 = new Patient();
		patient2.getNameFirstRep().addGiven("bob");
		myClient.create().resource(patient2).execute();

		Bundle result = myClient
			.loadPage()
			.byUrl(ourServerBase + "/Patient?name:nickname=kenneth")
			.andReturnBundle(Bundle.class)
			.execute();

		List<Patient> resources = BundleUtil.toListOfResourcesOfType(myFhirContext,result, Patient.class);
		assertThat(resources, hasSize(1));
		assertEquals("ken", resources.get(0).getNameFirstRep().getGivenAsSingleString());
	}
}
