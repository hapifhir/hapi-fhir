package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.test.utilities.validation.ValidationTestUtil.getValidationErrors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain.CacheConfiguration.defaultValues;

public class VersionSpecificWorkerContextR4Test extends BaseResourceProviderR4Test {
	@Autowired
	private FhirInstanceValidator myInstanceValidator;
	@Autowired
	private IValidationSupport myValidationSupport;

	@Nested
	public class ValidateWithCachePositiveTest {
		@BeforeEach
		public void before() {
			ValidationSupportChain chain = new ValidationSupportChain(defaultValues(), myValidationSupport);
			myInstanceValidator.setValidationSupport(chain);
		}

		@Test
		public void validate_profileResourceFetchedMultipleTimes_returnsNoErrors() {
			// setup
			StructureDefinition profileProcedure = ClasspathUtil.loadResource(myFhirContext, StructureDefinition.class, "validation/practitionerrole/profile-practitionerrole.json");
			myClient.update().resource(profileProcedure).execute();

			Bundle bundle = ClasspathUtil.loadResource(myFhirContext, Bundle.class, "validation/practitionerrole/bundle-with-medicationdispense.json");

			// execute and verify
			List<String> errors = getValidationErrors(myClient, bundle);
			assertThat(errors).isEmpty();
		}
	}

	@Nested
	public class FetchTests {
		private VersionSpecificWorkerContextWrapper myWorkerContext;

		@BeforeEach
		public void before() {
			myWorkerContext = VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(myValidationSupport);
		}

		@Test
		public void fetchResource_withStructureDefinition_usesCache() {
			final String resourceUrl = createStructureDefinition();

			// verify that the snapshot is generated
			org.hl7.fhir.r5.model.StructureDefinition fetchedStructureDefinition1 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.StructureDefinition.class, resourceUrl);
			assertThat(fetchedStructureDefinition1.hasSnapshot()).isTrue();

			// verify that the sub-subsequent fetchResource returns the same resource instance from the cache
			org.hl7.fhir.r5.model.StructureDefinition fetchedStructureDefinition2 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.StructureDefinition.class, resourceUrl);
			assertThat(fetchedStructureDefinition2).isSameAs(fetchedStructureDefinition1);
		}

		@Test
		public void fetchResource_withCodeSystem_cacheIsUsed() {
			final String resourceUrl = "http://example.com/CodeSystem/example-system";

			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setId("CodeSystem/example-system");
			codeSystem.setUrl(resourceUrl);

			myCodeSystemDao.create(codeSystem, mySrd);

			org.hl7.fhir.r5.model.CodeSystem fetchedCodeSystem1 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.CodeSystem.class, resourceUrl);

			// verify that the sub-subsequent fetchResource returns the same resource instance from the cache
			org.hl7.fhir.r5.model.CodeSystem fetchedCodeSystem2 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.CodeSystem.class, resourceUrl);
			assertThat(fetchedCodeSystem2).isSameAs(fetchedCodeSystem1);
		}

		@Test
		public void fetchResource_cacheIsUsed() {
			final String resourceUrl = "http://example.com/ValueSet/example-set";

			ValueSet valueSet = new ValueSet();
			valueSet.setId("ValueSet/example-set");
			valueSet.setUrl(resourceUrl);

			myValueSetDao.create(valueSet, mySrd);

			org.hl7.fhir.r5.model.ValueSet fetchedCodeSystem1 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.ValueSet.class, resourceUrl);

			// verify that the sub-subsequent fetchResource returns the same resource instance from the cache
			org.hl7.fhir.r5.model.ValueSet fetchedCodeSystem2 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.ValueSet.class, resourceUrl);
			assertThat(fetchedCodeSystem2).isSameAs(fetchedCodeSystem1);
		}

		@Test
		public void fetchResource_expireCache_cacheWorksAsExpected() {
			final String resourceUrl = createStructureDefinition();

			org.hl7.fhir.r5.model.StructureDefinition fetchedStructureDefinition1 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.StructureDefinition.class, resourceUrl);

			// simulate cache timeout by invalidating cache
			myValidationSupport.invalidateCaches();

			// verify that the sub-subsequent fetchResource returns a different resource instance from the cache
			org.hl7.fhir.r5.model.StructureDefinition fetchedStructureDefinition3 = myWorkerContext.fetchResource(org.hl7.fhir.r5.model.StructureDefinition.class, resourceUrl);
			assertThat(fetchedStructureDefinition3).isNotSameAs(fetchedStructureDefinition1);
		}
	}

	private String createStructureDefinition() {
		final String resourceUrl = "http://example.com/StructureDefinition/example-profile-patient";
		StructureDefinition structureDefinition = new StructureDefinition();
		structureDefinition.setId("StructureDefinition/example-profile-patient");
		structureDefinition.setUrl(resourceUrl);
		structureDefinition.setType("Patient");
		structureDefinition.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient");
		structureDefinition.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		ElementDefinition telecomElement = structureDefinition.getDifferential().addElement().setPath("Patient.telecom");
		telecomElement.setMax("1");

		myStructureDefinitionDao.create(structureDefinition, mySrd);

		return resourceUrl;
	}
}
