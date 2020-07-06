package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("Duplicates")
@ContextConfiguration(classes = {ResourceProviderOnlySomeResourcesProvidedR4Test.OnlySomeResourcesProvidedCtxConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ResourceProviderOnlySomeResourcesProvidedR4Test extends BaseResourceProviderR4Test {

	@Test
	public void testCreateUnsupportedType() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Elizabeth");
		myClient.create().resource(pt1).execute();

		Practitioner pract = new Practitioner();
		pract.setActive(true);
		try {
			myClient.create().resource(pract).execute();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), containsString("Unknown resource type 'Practitioner' - Server knows how to handle:"));
		}
	}

	@org.springframework.context.annotation.Configuration
	public static class OnlySomeResourcesProvidedCtxConfig {

		@Autowired
		private DaoRegistry myDaoRegistry;

		@Bean
		public RegistryConfigurer registryConfigurer() {
			return new RegistryConfigurer(myDaoRegistry);
		}


		public static class RegistryConfigurer {
			private final DaoRegistry myDaoRegistry;

			public RegistryConfigurer(DaoRegistry theDaoRegistry) {
				myDaoRegistry = theDaoRegistry;
			}

			@PostConstruct
			public void start() {
				myDaoRegistry.setSupportedResourceTypes("Patient", "Person", "SearchParameter");
			}

		}

		@PreDestroy
		public void stop() {
			myDaoRegistry.setSupportedResourceTypes();
		}


	}

}
