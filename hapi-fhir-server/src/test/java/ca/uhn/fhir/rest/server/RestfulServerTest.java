package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestfulServerTest {
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private FhirContext myCtx;
	private RestfulServer restfulServer;

	@BeforeEach
	public void setUp() throws ServletException {
		when(myCtx.getVersion().getVersion()).thenReturn(FhirVersionEnum.DSTU3);
		when(myCtx.getVersion().getServerVersion()).thenReturn(new MyFhirVersionServer());

		restfulServer = new RestfulServer(myCtx);
		restfulServer.init();
	}

	@Test
	public void testRegisterProviders() {
		//test register Plain Provider
		restfulServer.registerProvider(new MyClassWithRestInterface());
		assertEquals(1, restfulServer.getPlainProviders().size());
		Object plainProvider = restfulServer.getPlainProviders().iterator().next();
		assertTrue(plainProvider instanceof MyClassWithRestInterface);

		//test register Resource Provider
		restfulServer.registerProvider(new MyResourceProvider());
		assertEquals(1, restfulServer.getResourceProviders().size());
		IResourceProvider resourceProvider = restfulServer.getResourceProviders().iterator().next();
		assertTrue(resourceProvider instanceof MyResourceProvider);

		//test unregister providers
		restfulServer.unregisterProvider(plainProvider);
		assertTrue(restfulServer.getPlainProviders().isEmpty());
		restfulServer.unregisterProvider(resourceProvider);
		assertTrue(restfulServer.getResourceProviders().isEmpty());
	}

	@Test
	public void testFailRegisterInterfaceProviderWithoutRestfulMethod() {
		try {
			restfulServer.registerProvider(new MyClassWithoutRestInterface());
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Did not find any annotated RESTful methods on provider class ca.uhn.fhir.rest.server.RestfulServerTest$MyClassWithoutRestInterface", e.getMessage());
		}
	}


	//--------- Scaffolding ---------//
	private static class MyClassWithoutRestInterface implements Serializable {
	}

	private static class MyClassWithRestInterface implements MyRestInterface {
	}

	@SuppressWarnings("unused")
	private interface MyRestInterface {
		@Create
		default MethodOutcome create(@ResourceParam IBaseResource theResource) {
			return mock(MethodOutcome.class);
		}

	}

	private static class MyFhirVersionServer implements IFhirVersionServer {
		@Override
		public IServerConformanceProvider<? extends IBaseResource> createServerConformanceProvider(RestfulServer theRestfulServer) {
			return new IServerConformanceProvider<IBaseResource>() {

				@Override
				@Metadata
				public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
					return mock(IBaseConformance.class);
				}

				@Override
				public void setRestfulServer(RestfulServer theRestfulServer) {
				}
			};
		}

	}

	@SuppressWarnings("unused")
	private static class MyResourceProvider implements IResourceProvider {
		@Create
		public MethodOutcome create(@ResourceParam IBaseResource theResource) {
			return mock(MethodOutcome.class);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return IBaseResource.class;
		}
	}

}
