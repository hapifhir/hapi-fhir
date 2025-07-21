package ca.uhn.fhir.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoriesTest {

	@Test
	void testIsUrl() {
	    // given
		String url = "fhir-repository:memory:my-repo";

	    // when
		boolean repositoryUrl = Repositories.isRepositoryUrl(url);

		// then
	    assertTrue(repositoryUrl);
	}

}
