package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = {TestHSearchAddInConfig.DefaultLuceneHeap.class})
class FulltextSearchSvcImplTest extends BaseJpaR4Test {

	@Autowired
	private IFulltextSearchSvc myFulltextSearchSvc;

	private final RequestDetails mySrd = new SystemRequestDetails();

	@BeforeEach
	void resetStorageSettings() {
		myStorageSettings.setHibernateSearchIndexFullText(false);
	}

	@ParameterizedTest
	@CsvSource({
		"_text",
		"_content"
	})
	void testSearchForResources_WhenFullTextContentDisabled(String theParam) {
		// Given: Fulltext indexing is enabled and parameters are registered
		SearchParameterMap params = new SearchParameterMap();
		params.add(theParam, new StringParam("test"));

		// When/Then: searchForResources should not throw validation exception
		// Note: It may throw other exceptions related to Lucene setup, but not the validation ones
		assertThatThrownBy(() -> myObservationDao.search(params, mySrd))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2566")
			.hasMessageContaining("Fulltext searching is not enabled");
	}

	@ParameterizedTest
	@CsvSource({
		", 2",
		"_text, 1",
		"_content, 1"
	})
	void testSearchForResources_WhenFulltextContentEnabled(String theParam, int resourceCount) {
		// Given: Fulltext indexing is enabled and parameters are registered
		// Create test data - add Observation resources with searchable content
		myStorageSettings.setHibernateSearchIndexFullText(true);
		mySearchParamRegistry.forceRefresh();

		Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.getCode().addCoding().setDisplay("test observation");
		obs1.getText().setDivAsString("<div>test narrative content</div>");
		myObservationDao.create(obs1, mySrd);

		Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(obs2, mySrd);

		SearchParameterMap params = new SearchParameterMap();
		if (theParam != null) {
			params.add(theParam, new StringParam("test"));
		}

		// When/Then: searchForResources should not throw validation exception
		IBundleProvider observations = myObservationDao.search(params, mySrd);
		List<IBaseResource> resources = observations.getResources(0, 10);

		assertThat(resources).hasSize(resourceCount);
		assertThat(resources.get(0).getIdElement()).hasToString(obs1.getId());
	}
}
