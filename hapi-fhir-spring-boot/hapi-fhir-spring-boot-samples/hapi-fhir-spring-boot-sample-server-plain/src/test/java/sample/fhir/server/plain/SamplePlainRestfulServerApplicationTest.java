package sample.fhir.server.plain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;

import ca.uhn.fhir.spring.boot.autoconfigure.FhirProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SamplePlainRestfulServerApplicationTest {

	@Autowired
	private FhirProperties properties;

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testBean() {
		// Test if exactly one bean has a URL mapping that includes the FHIR server path set in the application configuration
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
		assertThat(count).isEqualTo(1);
	}

}
