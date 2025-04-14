package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.interceptor.validation.IRepositoryValidatingRule;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.HealthcareService;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR5CustomSearchParamTest extends BaseJpaR5Test {

	private RepositoryValidatingInterceptor myRepositoryValidatingInterceptor;

	@Autowired
	private RepositoryValidatingRuleBuilder myRepositoryValidatingRuleBuilder;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myRepositoryValidatingInterceptor = new RepositoryValidatingInterceptor();
		myRepositoryValidatingInterceptor.setFhirContext(myFhirContext);
		List<IRepositoryValidatingRule> rules = myRepositoryValidatingRuleBuilder.forResourcesOfType("SearchParameter").requireValidationToDeclaredProfiles().build();
		myRepositoryValidatingInterceptor.setRules(rules);
		myInterceptorRegistry.registerInterceptor(myRepositoryValidatingInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myRepositoryValidatingInterceptor);
	}

	@Test
	public void testCustomSp() {

		String spText = """
			{
			    "resourceType": "SearchParameter",
			    "id": "A",
			    "extension": [ {
			        "url": "http://hapifhir.io/fhir/StructureDefinition/searchparameter-enabled-for-searching",
			        "valueBoolean": true
			    } ],
			    "url": "http://api.loblaw.ca/fhir/SearchParameter/EncounterServiceType",
			    "name": "EncounterServiceType",
			    "status": "active",
			    "description": "Enabling search parameter to search by Encounter.serviceType",
			    "code": "serviceType",
			    "base": ["Encounter"],
			    "type": "reference",
			    "expression": "Encounter.serviceType.reference",
			    "target": ["HealthcareService"],
			    "processingMode": "normal"
			}""";
		SearchParameter sp = myFhirCtx.newJsonParser().parseResource(SearchParameter.class, spText);
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();

		HealthcareService h = new HealthcareService();
		h.setId("HCS");
		h.setActive(true);
		myHealthcareServiceDao.update(h, mySrd);

		Encounter enc = new Encounter();
		enc.setId("E");
		enc.addServiceType().setReference(new Reference("HealthcareService/HCS"));
		myEncounterDao.update(enc, mySrd);

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add("serviceType", new ReferenceParam("HealthcareService/HCS"));
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).asList().containsExactlyInAnyOrder("Encounter/E");
	}


}
