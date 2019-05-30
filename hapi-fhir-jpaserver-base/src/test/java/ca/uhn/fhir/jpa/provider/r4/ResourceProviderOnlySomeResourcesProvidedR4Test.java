package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@SuppressWarnings("Duplicates")
@ContextConfiguration(classes = {ResourceProviderOnlySomeResourcesProvidedR4Test.OnlySomeResourcesProvidedCtxConfig.class})
public class ResourceProviderOnlySomeResourcesProvidedR4Test extends BaseResourceProviderR4Test {

	@Test
	public void testCreateUnsupportedType() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Elizabeth");
		ourClient.create().resource(pt1).execute();

		Practitioner pract = new Practitioner();
		pract.setActive(true);
		try {
			ourClient.create().resource(pract).execute();
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
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
