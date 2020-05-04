package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchBuilderTest {


	@Test
	public void testIncludeIterator() {
		BaseHapiFhirDao<?> mockDao = mock(BaseHapiFhirDao.class);
		SearchBuilder searchBuilder = new SearchBuilder(mockDao, null, null);
		searchBuilder.setDaoConfigForUnitTest(new DaoConfig());
		searchBuilder.setParamsForUnitTest(new SearchParameterMap());
		EntityManager mockEntityManager = mock(EntityManager.class);
		searchBuilder.setEntityManagerForUnitTest(mockEntityManager);

		Set<ResourcePersistentId> pidSet = new HashSet<>();
		pidSet.add(new ResourcePersistentId(1L));
		pidSet.add(new ResourcePersistentId(2L));

		TypedQuery mockQuery = mock(TypedQuery.class);
		when(mockEntityManager.createQuery(any(), any())).thenReturn(mockQuery);
		List<Long> resultList = new ArrayList<>();
		Long link = 1L;
		ResourceTable target = new ResourceTable();
		target.setId(1L);
		resultList.add(link);
		when(mockQuery.getResultList()).thenReturn(resultList);

		SearchBuilder.IncludesIterator includesIterator = searchBuilder.new IncludesIterator(pidSet, null);
		// hasNext() should return false if the pid added was already on our list going in.
		assertFalse(includesIterator.hasNext());
	}
}
