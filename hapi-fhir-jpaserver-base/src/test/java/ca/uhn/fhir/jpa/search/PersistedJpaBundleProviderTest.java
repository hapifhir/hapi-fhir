package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class PersistedJpaBundleProviderTest {
	private PersistedJpaBundleProvider myPersistedJpaBundleProvider;
	private IDao myDao;
	private SearchBuilderFactory mySearchBuilderFactory;

	@BeforeEach
	public void init() {
		RequestDetails request = mock(RequestDetails.class);
		String searchUuid = "this is not a hat";
		myDao = mock(IDao.class);
		mySearchBuilderFactory = mock(SearchBuilderFactory.class);
		myPersistedJpaBundleProvider = new PersistedJpaBundleProvider(request, searchUuid);
	}

	@Test
	public void zeroNumFoundDoesntCallCache() {
		Search searchEntity = new Search();
		searchEntity.setTotalCount(1);
		myPersistedJpaBundleProvider.setSearchEntity(searchEntity);
		myPersistedJpaBundleProvider.doSearchOrEverything(0, 1);
		verifyNoInteractions(myDao);
		verifyNoInteractions(mySearchBuilderFactory);
	}
}
