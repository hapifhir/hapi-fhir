package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderRule;
import ca.uhn.fhir.test.utilities.server.RestfulServerRule;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResponseSizeCapturingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();
	@ClassRule
	public static RestfulServerRule ourServerRule = new RestfulServerRule(ourCtx);
	private ResponseSizeCapturingInterceptor myInterceptor;
	@Rule
	public HashMapResourceProviderRule<Patient> myPatientProviderRule = new HashMapResourceProviderRule<>(ourServerRule, Patient.class);
	@Mock
	private Consumer<ResponseSizeCapturingInterceptor.Result> myConsumer;
	@Captor
	private ArgumentCaptor<ResponseSizeCapturingInterceptor.Result> myResultCaptor;

	@Before
	public void before() {
		myInterceptor = new ResponseSizeCapturingInterceptor();
		ourServerRule.getRestfulServer().registerInterceptor(myInterceptor);
	}

	@After
	public void after() {
		ourServerRule.getRestfulServer().unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testReadResource() {
		Patient resource = new Patient();
		resource.setActive(true);
		IIdType id = ourServerRule.getFhirClient().create().resource(resource).execute().getId().toUnqualifiedVersionless();

		myInterceptor.registerConsumer(myConsumer);

		resource = ourServerRule.getFhirClient().read().resource(Patient.class).withId(id).execute();
		assertEquals(true, resource.getActive());

		verify(myConsumer, Mockito.timeout(Duration.ofSeconds(10)).times(1)).accept(myResultCaptor.capture());
		assertEquals(100, myResultCaptor.getValue().getWrittenChars());
	}


}
