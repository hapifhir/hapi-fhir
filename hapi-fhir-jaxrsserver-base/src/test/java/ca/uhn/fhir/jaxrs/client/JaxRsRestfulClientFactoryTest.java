package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.context.FhirContext;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastien Riviere on 31/07/2017.
 */
public class JaxRsRestfulClientFactoryTest {

    private final FhirContext context = FhirContext.forDstu2();
    private JaxRsRestfulClientFactory factory;

    @Before
    public void setUp() {
        factory = new JaxRsRestfulClientFactory(context);
    }

    @Test
    public void emptyConstructorTest() {
        assertNotNull(new JaxRsRestfulClientFactory());
    }

    @Test
    public void getDefaultNativeClientTest() {
        assertNotNull(factory.getNativeClientClient());
    }

    @Test
    public void getNativeClientEmptyRegisteredComponentListTest() {
        factory.register(new ArrayList<Class<?>>());
        final Client result = factory.getNativeClientClient();
        assertNotNull(result);
        assertTrue(result.getConfiguration().getClasses().isEmpty());
    }

    @Test
    public void getNativeClientRegisteredComponentListTest() {
        factory.register(Arrays.asList(MyFilter.class, String.class));
        final Client result = factory.getNativeClientClient();
        assertNotNull(result);
        assertEquals(1, result.getConfiguration().getClasses().size());
    }
}