package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sebastien Riviere on 31/07/2017.
 */
public class JaxRsRestfulClientFactoryTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JaxRsRestfulClientFactoryTest.class);
	private final FhirContext context = FhirContext.forDstu2();
	private JaxRsRestfulClientFactory factory;

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
		factory.register(new ArrayList<>());
		final Client result = factory.getNativeClientClient();
		assertNotNull(result);
		ourLog.info("Classes: {}", result.getConfiguration().getClasses());
		assertThat(result.getConfiguration().getClasses(), not(hasItem(ca.uhn.fhir.jaxrs.client.MyFilter.class)));
	}

	@Test
	public void getNativeClientRegisteredComponentListTest() {
		factory.register(Arrays.asList(MyFilter.class, String.class));
		final Client result = factory.getNativeClientClient();
		assertNotNull(result);
		ourLog.info("Classes: {}", result.getConfiguration().getClasses());
		assertThat(result.getConfiguration().getClasses(), hasItem(ca.uhn.fhir.jaxrs.client.MyFilter.class));
	}

	@BeforeEach
	public void setUp() {
		factory = new JaxRsRestfulClientFactory(context);
	}
}
