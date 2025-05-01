package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

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
			String errorMessage = e.getMessage();
			assertThat(errorMessage)
				.satisfies(
					arg -> assertThat(arg).contains("Unknown resource type 'Practitioner' - Server knows how to handle:"),
					arg -> assertThat(arg).contains("Patient"),
					arg -> assertThat(arg).contains("Practitioner"),
					arg -> assertThat(arg).contains("SearchParameter"),
					arg -> assertThat(arg).doesNotContain("ValueSet"),
					arg -> assertThat(arg).doesNotContain("CodeSystem"),
					arg -> assertThat(arg).doesNotContain("OperationDefinition")
				);
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
