package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CapabilityStatementCustomizationR4Test {

	private final FhirContext myCtx = FhirContext.forR4Cached();
	@Order(0)
	@RegisterExtension
	protected RestfulServerExtension myServerExtension = new RestfulServerExtension(myCtx);
	@Order(1)
	@RegisterExtension
	protected HashMapResourceProviderExtension<Patient> myProviderPatientExtension = new HashMapResourceProviderExtension<>(myServerExtension, Patient.class);

	@AfterEach
	public void afterEach() {
		myServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testCustomizeCapabilityStatement() {

		@Interceptor
		class CapabilityStatementCustomizer {

			@Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
			public void customize(IBaseConformance theCapabilityStatement) {

				// Cast to the appropriate version
				CapabilityStatement cs = (CapabilityStatement) theCapabilityStatement;

				// Customize the CapabilityStatement as desired
				cs
					.getSoftware()
					.setName("Acme FHIR Server")
					.setVersion("1.0")
					.setReleaseDateElement(new DateTimeType("2021-02-06"));

			}

		}

		myServerExtension.getRestfulServer().registerInterceptor(new CapabilityStatementCustomizer());

		CapabilityStatement received = myServerExtension.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
		assertEquals("Acme FHIR Server", received.getSoftware().getName());

	}

	@Test
	public void testReplaceCapabilityStatement() {

		@Interceptor
		class CapabilityStatementCustomizer {

			@Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
			public CapabilityStatement customize(IBaseConformance theCapabilityStatement) {

				CapabilityStatement cs = new CapabilityStatement();

				// Customize the CapabilityStatement as desired
				cs
					.getSoftware()
					.setName("Acme FHIR Server")
					.setVersion("1.0")
					.setReleaseDateElement(new DateTimeType("2021-02-06"));

				return cs;
			}

		}

		myServerExtension.getRestfulServer().registerInterceptor(new CapabilityStatementCustomizer());

		CapabilityStatement received = myServerExtension.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
		assertEquals("Acme FHIR Server", received.getSoftware().getName());

	}

}
