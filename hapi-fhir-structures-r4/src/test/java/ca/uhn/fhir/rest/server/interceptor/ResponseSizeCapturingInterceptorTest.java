package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderRule;
import ca.uhn.fhir.test.utilities.server.RestfulServerRule;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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

		List<String> stacks = Collections.synchronizedList(new ArrayList<>());
		doAnswer(t->{
			ResponseSizeCapturingInterceptor.Result result =t.getArgument(0, ResponseSizeCapturingInterceptor.Result.class);
			try {
				throw new Exception();
			} catch (Exception e) {
				stacks.add("INVOCATION\n" + result.getRequestDetails().getCompleteUrl() + "\n" + ExceptionUtils.getStackTrace(e));
			}
			return null;
		}).when(myConsumer).accept(any());

		resource = ourServerRule.getFhirClient().read().resource(Patient.class).withId(id).execute();
		assertEquals(true, resource.getActive());

		await().until(()->stacks.size() > 0);
		await().until(()->stacks.stream().collect(Collectors.joining("\n")), not(matchesPattern(Pattern.compile(".*INVOCATION.*INVOCATION.*", Pattern.MULTILINE | Pattern.DOTALL))));
	}


}
