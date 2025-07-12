package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.Repositories;
import ca.uhn.fhir.repository.impl.memory.InMemoryFhirRepository;
import ca.uhn.fhir.repository.impl.memory.InMemoryFhirRepositoryLoader;
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
		IRepository repository1 = Repositories.repositoryForUrl(url, myFhirContext);
		IRepository repository2 = Repositories.repositoryForUrl(url, myFhirContext);

	    // then
	    assertSame(repository1, repository2);
	}

	@Test
	void testDiscoveryThroughServiceLoaderFacade() {
	    // when
		IRepository repository = Repositories.repositoryForUrl(FHIR_REPOSITORY_URL_SCHEME + "memory:my-repo", myFhirContext);

	    // then
	    assertThat(repository).isNotNull()
			.isInstanceOf(InMemoryFhirRepository.class);
	}


}
