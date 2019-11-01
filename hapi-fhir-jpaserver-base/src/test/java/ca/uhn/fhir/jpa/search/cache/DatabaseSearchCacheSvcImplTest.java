package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseSearchCacheSvcImplTest {

	private DatabaseSearchCacheSvcImpl mySvc;

	@Mock
	private ISearchDao mySearchDao;

	@Mock
	private PlatformTransactionManager myTxManager;

	@Before
	public void before() {
		mySvc = new DatabaseSearchCacheSvcImpl();
		mySvc.setSearchDaoForUnitTest(mySearchDao);
		mySvc.setTxManagerForUnitTest(myTxManager);
	}

	@Test
	public void tryToMarkSearchAsInProgressSuccess() {
		Search updated = new Search();
		updated.setStatus(SearchStatusEnum.PASSCMPLET);
		when(mySearchDao.findById(any())).thenReturn(Optional.of(updated));
		when(mySearchDao.save(any())).thenReturn(updated);

		Search search = new Search();
		Optional<Search> outcome = mySvc.tryToMarkSearchAsInProgress(search);
		assertTrue(outcome.isPresent());

		verify(mySearchDao, times(1)).save(any());
		assertEquals(SearchStatusEnum.LOADING, updated.getStatus());
	}

	@Test
	public void tryToMarkSearchAsInProgressFail() {
		Search updated = new Search();
		updated.setStatus(SearchStatusEnum.PASSCMPLET);
		when(mySearchDao.findById(any())).thenReturn(Optional.of(updated));
		when(mySearchDao.save(any())).thenThrow(new HibernateException("FOO"));

		Search search = new Search();
		Optional<Search> outcome = mySvc.tryToMarkSearchAsInProgress(search);
		assertFalse(outcome.isPresent());
		verify(mySearchDao, times(1)).save(any());
	}

	@Test
	public void tryToMarkSearchAsInProgressAlreadyLoading() {
		Search updated = new Search();
		updated.setStatus(SearchStatusEnum.LOADING);
		when(mySearchDao.findById(any())).thenReturn(Optional.of(updated));

		Search search = new Search();
		Optional<Search> outcome = mySvc.tryToMarkSearchAsInProgress(search);
		assertFalse(outcome.isPresent());
		verify(mySearchDao, never()).save(any());
	}

}
