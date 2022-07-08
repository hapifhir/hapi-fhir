package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.provider.BaseJpaProvider;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.rp.r4.CodeSystemResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.MeasureResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.List;

import static com.healthmarketscience.sqlbuilder.Conditions.not;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;

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
			assertThat(errorMessage, CoreMatchers.allOf(
				containsString("Unknown resource type 'Practitioner' - Server knows how to handle:"),

				// Error message should contain all resources providers
				containsString("Patient"),
				containsString("Practitioner"),
				containsString("SearchParameter"),

				// Error message should not contain the registered plain providers
				Matchers.not(containsString("ValueSet")),
				Matchers.not(containsString("CodeSystem")),
				Matchers.not(containsString("OperationDefinition"))
			));
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
