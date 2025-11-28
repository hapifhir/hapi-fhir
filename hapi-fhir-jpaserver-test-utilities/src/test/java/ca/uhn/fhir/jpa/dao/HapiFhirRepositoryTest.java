package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.repository.HapiFhirRepository;
import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.repository.IRepositoryTest;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import jakarta.servlet.ServletException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HapiFhirRepositoryTest extends BaseJpaR4Test implements IRepositoryTest {

	private HapiFhirRepository myRepository;

	@BeforeEach
	public void beforeResetDao() {

		RestfulServer restfulServer = new RestfulServer(myFhirContext);

		PatientResourceProvider patientResourceProvider = new PatientResourceProvider();
		patientResourceProvider.setDao(myPatientDao);
		patientResourceProvider.setContext(myFhirContext);

		restfulServer.setResourceProviders(patientResourceProvider);
		try {
			restfulServer.init();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
		var srd = new SystemRequestDetails(restfulServer.getInterceptorService());
		srd.setFhirContext(myFhirContext);
		srd.setServer(restfulServer);
		myRepository = new HapiFhirRepository(myDaoRegistry, srd, restfulServer);
	}

	@AfterEach
	public void afterResetDao() {
		myStorageSettings.setResourceServerIdStrategy(new JpaStorageSettings().getResourceServerIdStrategy());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(new JpaStorageSettings().isDefaultSearchParamsCanBeOverridden());
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
		myStorageSettings.setIndexOnContainedResources(new JpaStorageSettings().isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(new JpaStorageSettings().isIndexOnContainedResourcesRecursively());
	}


	@Override
	public RepositoryTestSupport getRepositoryTestSupport() {
		return new RepositoryTestSupport(myRepository);
	}

	@Test
	void testSearchBySearchParameterMap() {
		// given
		FhirContext context = getRepository().fhirContext();
		var repository = getRepository();
		var b = getTestDataBuilder();
		var patientClass = getTestDataBuilder().buildPatient().getClass();
		b.createPatient(b.withId("abc"));
		b.createPatient(b.withId("def"));
		IBaseBundle bundle = new BundleBuilder(context).getBundle();

		SearchParameterMap searchParams = new SearchParameterMap();
		searchParams.addOrList("_id", List.of(new ReferenceParam("abc"), new ReferenceParam("ghi")));

		// when
		IBaseBundle searchResult = repository.search(bundle.getClass(), patientClass, searchParams, Map.of());

		// then
		List<SearchBundleEntryParts> entries = BundleUtil.getSearchBundleEntryParts(context, searchResult);
		assertThat(entries).hasSize(1);
		SearchBundleEntryParts entry = entries.get(0);
		assertThat(entry.getResource()).isNotNull();
		assertThat(entry.getResource()).isInstanceOf(patientClass);
	}

	@Test
	void testOperation() {
		IIdType patientId = getTestDataBuilder().createPatient(withId("abc"));

		Parameters result = getRepository().invoke(patientId, "$meta", null, Parameters.class, Map.of());

		assertThat(result).isNotNull();
		Meta meta = (Meta) result.getParameter("return").getValue();
		assertThat(meta.getVersionId()).isEqualTo("1");
	}

}
