package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestHSearchAddInConfig.DefaultLuceneHeap.class})
class FulltextSearchSvcImplTest extends BaseJpaR4Test {

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

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
		assertThatThrownBy(() -> myFulltextSearchSvc.searchForResources("Patient", params, mySrd))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2566")
			.hasMessageContaining("Fulltext searching is not enabled");
	}

	@ParameterizedTest
	@CsvSource({
		"_text",
		"_content"
	})
	void testSearchForResources_WhenFulltextContentEnabled(String theParam) {
		// Given: Fulltext indexing is enabled and parameters are registered
		SearchParameterMap params = new SearchParameterMap();
		params.add(theParam, new StringParam("test"));

		// When/Then: searchForResources should not throw validation exception
		myStorageSettings.setHibernateSearchIndexFullText(true);
		List<IBaseResource> resources = myFulltextSearchSvc.searchForResources("Observation", params, mySrd);

		assertThat(resources).isEmpty();
	}
}
