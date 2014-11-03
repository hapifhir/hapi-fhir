package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ProvidesResources;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill de Beaubien on 11/1/2014.
 */
public class ServerProvidedResourceScannerTest extends TestCase {
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
        RestfulServer server = new RestfulServer();

        // When
        server.init();

        // Then
        assertNull(server.getFhirContext().getElementDefinition(CustomObservation.class));
        assertNull(server.getFhirContext().getElementDefinition(CustomPatient.class));
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
    class CustomPatient extends Patient {
    }

    @ResourceDef(name = "Observation", id="CustomObservation")
    class CustomObservation extends Observation {
    }

    @ProvidesResources(resources={CustomPatient.class,CustomObservation.class})
    class MyServer extends RestfulServer {
    }

    class MyServerWithProvider extends RestfulServer {
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

}
