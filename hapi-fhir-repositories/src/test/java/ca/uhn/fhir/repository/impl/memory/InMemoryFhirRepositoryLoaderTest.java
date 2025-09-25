package ca.uhn.fhir.repository.impl.memory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.Repositories;
import ca.uhn.fhir.repository.impl.UrlRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static ca.uhn.fhir.repository.impl.UrlRepositoryFactory.FHIR_REPOSITORY_URL_SCHEME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class InMemoryFhirRepositoryLoaderTest {

	@Mock
	FhirContext myFhirContext;
	InMemoryFhirRepositoryLoader myMemoryFhirRepositoryLoader = new InMemoryFhirRepositoryLoader();

	@ParameterizedTest
	@CsvSource(textBlock = """
		fhir-repository:memory:my-repo,	true
		fhir-repository:baz:my-repo, false
		memory:my-repo, false
		""")
	void testUrlCheck(String theUrl, boolean theExpectedResult) {
		boolean result = myMemoryFhirRepositoryLoader.canLoad(UrlRepositoryFactory.buildRequest(theUrl, myFhirContext));
		assertEquals(theExpectedResult, result);
	}

	@Test
	void testSameSlugGivesSameRepository() {
	    // given
		String url = FHIR_REPOSITORY_URL_SCHEME + "memory:my-repo";
	    // when
		IRepository repository1 = Repositories.repositoryForUrl(myFhirContext, url);
		IRepository repository2 = Repositories.repositoryForUrl(myFhirContext, url);

	    // then
	    assertSame(repository1, repository2);
	}

	@Test
	void testDiscoveryThroughServiceLoaderFacade() {
	    // when
		String repositoryUrl = FHIR_REPOSITORY_URL_SCHEME + "memory:my-repo";
		IRepository repository = Repositories.repositoryForUrl(myFhirContext, repositoryUrl);

	    // then
	    assertThat(repository).isNotNull()
			.isInstanceOf(InMemoryFhirRepository.class);
	}


}
