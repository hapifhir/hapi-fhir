package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ProvidesResources;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.TestUtil;
import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.Test;

import javax.servlet.ServletException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill de Beaubien on 11/1/2014.
 */
public class ServerProvidedResourceScannerTest extends TestCase {
	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
    public void testWhenRestfulServerInitialized_annotatedResources_shouldBeAddedToContext() throws ServletException {
        // Given
        MyServer server = new MyServer();

        // When
        server.init();

        // Then
        assertNotNull(server.getFhirContext().getElementDefinition(CustomObservation.class));
        assertNotNull(server.getFhirContext().getElementDefinition(CustomPatient.class));
    }

    @Test
    public void testWhenUnannotatedServerInitialized_annotatedResources_shouldNotBeAddedToContext() throws ServletException {
        // Given
        RestfulServer server = new RestfulServer(ourCtx);

        // When
        server.init();

        // Then
        assertEquals(CustomObservation.class, server.getFhirContext().getElementDefinition(CustomObservation.class).getImplementingClass());
        assertEquals(CustomPatient.class, server.getFhirContext().getElementDefinition(CustomPatient.class).getImplementingClass());
    }

    @Test
    public void testWhenServletWithAnnotatedProviderInitialized_annotatedResource_shouldBeAddedToContext() throws ServletException {
        // Given
        MyServerWithProvider server = new MyServerWithProvider();

        // When
        server.init();

        // Then
        assertNotNull(server.getFhirContext().getElementDefinition(CustomObservation.class));
    }

    @ResourceDef(name = "Patient", id="CustomPatient")
    public static class CustomPatient extends Patient {
    }

    @ResourceDef(name = "Observation", id="CustomObservation")
    public static class CustomObservation extends Observation {
    }

    @ProvidesResources(resources={CustomPatient.class,CustomObservation.class})
    class MyServer extends RestfulServer {

		private static final long serialVersionUID = 1L;
		
		public MyServer() {
			super(ourCtx);
		}
		
    }

    class MyServerWithProvider extends RestfulServer {
		private static final long serialVersionUID = 1L;
		
		public MyServerWithProvider() {
			super(ourCtx);
		}

		@Override
        protected void initialize() throws ServletException {
            List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
            providers.add(new MyObservationProvider());
            setResourceProviders(providers);
        }
    }

    @ProvidesResources(resources=CustomObservation.class)
    public static class MyObservationProvider implements IResourceProvider {
        @Override
        public Class<? extends IResource> getResourceType() {
            return CustomObservation.class;
        }

        @Read(version = false)
        public CustomObservation readObservation(@IdParam IdDt theId) {
            return null;
        }
    }


 	@AfterClass
 	public static void afterClassClearContext() {
 		TestUtil.clearAllStaticFieldsForUnitTest();
 	}

}
