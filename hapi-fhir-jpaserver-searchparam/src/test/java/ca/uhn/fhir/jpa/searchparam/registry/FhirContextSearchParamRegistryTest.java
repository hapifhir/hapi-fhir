package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_ID;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_LAST_UPDATED;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_PROFILE;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_SECURITY;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_TAG;

class FhirContextSearchParamRegistryTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4();

	FhirContextSearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(ourFhirContext);

	@ParameterizedTest
	@CsvSource({
		SP_RES_ID + ", Resource.id",
		SP_RES_LAST_UPDATED + ", Resource.meta.lastUpdated",
		SP_RES_TAG + ", Resource.meta.tag",
		SP_RES_PROFILE + ", Resource.meta.profile",
		SP_RES_SECURITY + ", Resource.meta.security"
	})
	void testResourceLevelSearchParamsAreRegistered(String theSearchParamName, String theSearchParamPath) {
		RuntimeSearchParam sp = mySearchParamRegistry.getActiveSearchParam("Patient", theSearchParamName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);

		assertThat(sp)
			.as("path is null for search parameter: '%s'", theSearchParamName)
			.isNotNull().extracting("path").isEqualTo(theSearchParamPath);
	}

}
