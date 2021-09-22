package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FulltextSearchSvcImplTest {
	@InjectMocks
	FulltextSearchSvcImpl mySvc;

	@Mock
	private ISearchParamExtractor searchParamExtractor;

	SearchParamExtractorService mySearchParamExtractorService = new SearchParamExtractorService();

	@Test
	void parseSearchParamTextStuff() {
		// fixme mb what's left here to test?

	}
}
