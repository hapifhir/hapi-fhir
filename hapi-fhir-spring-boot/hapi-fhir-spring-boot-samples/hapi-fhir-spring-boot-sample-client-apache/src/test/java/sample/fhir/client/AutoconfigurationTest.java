package sample.fhir.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.spring.boot.autoconfigure.FhirProperties;

@SpringBootTest
public class AutoconfigurationTest {

	@Autowired
	private FhirProperties properties;

	@Autowired
	private ApplicationContext applicationContext;

	@MockBean
	private CommandLineRunner runner;

	@Test
	public void testContextNotNull() {
		assertThat(applicationContext).isNotNull();

	}

	@Test
	public void testClientBeanCreated() {
		// Test that a client bean is created
		String[] beanNames = applicationContext.getBeanNamesForType(IGenericClient.class);
		assertThat(beanNames).isNotEmpty();
	}

	@Test
	public void testServerBeanNotCreated() {
		// Test that no bean has a URL mapping that includes the FHIR server path set in
		// the application configuration
		String[] beanNames = applicationContext.getBeanNamesForType(ServletRegistrationBean.class);
		String expectedPath = properties.getServer().getPath();
		long count = 0;
		for (String beanName : beanNames) {
			ServletRegistrationBean<?> bean = applicationContext.getBean(beanName, ServletRegistrationBean.class);
			for (String mapping : bean.getUrlMappings()) {
				if (mapping.contains(expectedPath)) {
					count++;
				}
			}
		}
		// The server should not be created because the hapi-fhir-server dependency is
		// not added
		assertThat(count).isEqualTo(0);
	}

}
