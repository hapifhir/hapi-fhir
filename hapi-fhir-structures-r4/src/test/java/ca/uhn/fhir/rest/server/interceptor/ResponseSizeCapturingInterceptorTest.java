package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.test.concurrency.PointcutLatch;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResponseSizeCapturingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();
	@RegisterExtension
	public static RestfulServerExtension ourServerRule = new RestfulServerExtension(ourCtx);
	private ResponseSizeCapturingInterceptor myInterceptor;
	@RegisterExtension
	public HashMapResourceProviderExtension<Patient> myPatientProviderRule = new HashMapResourceProviderExtension<>(ourServerRule, Patient.class);
	@Mock
	private Consumer<ResponseSizeCapturingInterceptor.Result> myConsumer;
	@Captor
	private ArgumentCaptor<ResponseSizeCapturingInterceptor.Result> myResultCaptor;

	@BeforeEach
	public void before() {
		myInterceptor = new ResponseSizeCapturingInterceptor();
		ourServerRule.getRestfulServer().registerInterceptor(myInterceptor);
	}

	@AfterEach
	public void after() {
		ourServerRule.getRestfulServer().unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testReadResource() throws InterruptedException {
		PointcutLatch createLatch = new PointcutLatch(Pointcut.SERVER_PROCESSING_COMPLETED);
		createLatch.setExpectedCount(1);
		ourServerRule.getRestfulServer().getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_PROCESSING_COMPLETED, createLatch);

		Patient resource = new Patient();
		resource.setActive(true);
		IIdType id = ourServerRule.getFhirClient().create().resource(resource).execute().getId().toUnqualifiedVersionless();

		createLatch.awaitExpected();
		ourServerRule.getRestfulServer().getInterceptorService().unregisterInterceptor(createLatch);

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

		verify(myConsumer, timeout(10000).times(1)).accept(myResultCaptor.capture());
		assertEquals(100, myResultCaptor.getValue().getWrittenChars());
	}


}
