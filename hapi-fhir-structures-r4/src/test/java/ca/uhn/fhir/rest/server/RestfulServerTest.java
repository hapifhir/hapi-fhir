package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestfulServerTest {
	private final FhirContext myCtx = FhirContext.forR4(); // don't use cached, we register custom resources
	private RestfulServer myRestfulServer;

	@BeforeEach
	public void setUp() throws ServletException {
		myRestfulServer = new RestfulServer(myCtx);
		myRestfulServer.init();
	}

	@Test
	public void testRegisterProvidersWithMethodBindings() {
		MyProvider provider = new MyProvider();
		myRestfulServer.registerProvider(provider);
		MyProvider2 provider2 = new MyProvider2();
		myRestfulServer.registerProvider(provider2);

		assertFalse(myRestfulServer.getProviderMethodBindings(provider).isEmpty());
		assertFalse(myRestfulServer.getProviderMethodBindings(provider2).isEmpty());

		myRestfulServer.unregisterProvider(provider);
		assertTrue(myRestfulServer.getProviderMethodBindings(provider).isEmpty());
		assertFalse(myRestfulServer.getProviderMethodBindings(provider2).isEmpty());
	}

	@Test
	public void testRegisterProviders() {
		//test register Plain Provider
		myRestfulServer.registerProvider(new MyClassWithRestInterface());
		assertEquals(1, myRestfulServer.getResourceProviders().size());
		Object plainProvider = myRestfulServer.getResourceProviders().get(0);
		assertTrue(plainProvider instanceof MyClassWithRestInterface);

		//test register Resource Provider
		myRestfulServer.registerProvider(new MyResourceProvider());
		assertEquals(2, myRestfulServer.getResourceProviders().size());
		IResourceProvider resourceProvider = myRestfulServer.getResourceProviders().get(1);
		assertTrue(resourceProvider instanceof MyResourceProvider);

		//test unregister providers
		myRestfulServer.unregisterProvider(plainProvider);
		assertFalse(myRestfulServer.getResourceProviders().isEmpty());
		myRestfulServer.unregisterProvider(resourceProvider);
		assertTrue(myRestfulServer.getResourceProviders().isEmpty());
	}

	@Test
	public void testFailRegisterInterfaceProviderWithoutRestfulMethod() {
		try {
			myRestfulServer.registerProvider(new MyClassWithoutRestInterface());
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(289) + "Did not find any annotated RESTful methods on provider class ca.uhn.fhir.rest.server.RestfulServerTest$MyClassWithoutRestInterface", e.getMessage());
		}
	}


	//--------- Scaffolding ---------//
	private static class MyClassWithoutRestInterface implements Serializable {
	}

	private static class MyClassWithRestInterface implements MyRestInterface, IResourceProvider {
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
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
			return Patient.class;
		}
	}

	private static class MyProvider implements IResourceProvider {
		@Operation(name = "SHOW_ME_THE_MONEY", typeName = "MyResource")
		public IBaseBundle match() {
			return mock(IBaseBundle.class);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return MyResource.class;
		}
	}

	private static class MyProvider2 implements IResourceProvider {
		@Operation(name = "SHOW_ME_MORE_MONEY", typeName = "MyResource2")
		public IBaseBundle match() {
			return mock(IBaseBundle.class);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return MyResource2.class;
		}
	}

	@ResourceDef(name="MyResource")
	public static class MyResource implements IBaseResource {

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean hasFormatComment() {
			return false;
		}

		@Override
		public List<String> getFormatCommentsPre() {
			return null;
		}

		@Override
		public List<String> getFormatCommentsPost() {
			return null;
		}

		@Override
		public Object getUserData(String theName) {
			return null;
		}

		@Override
		public void setUserData(String theName, Object theValue) {

		}

		@Override
		public IBaseMetaType getMeta() {
			return null;
		}

		@Override
		public IIdType getIdElement() {
			return null;
		}

		@Override
		public IBaseResource setId(String theId) {
			return null;
		}

		@Override
		public IBaseResource setId(IIdType theId) {
			return null;
		}

		@Override
		public FhirVersionEnum getStructureFhirVersionEnum() {
			return FhirVersionEnum.R4;
		}
	}

	@ResourceDef(name="MyResource2")
	public static class MyResource2 extends MyResource {
	}

}
