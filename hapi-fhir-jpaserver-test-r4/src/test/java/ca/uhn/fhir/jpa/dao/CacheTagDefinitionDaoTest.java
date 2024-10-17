package ca.uhn.fhir.jpa.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.MetaUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CacheTagDefinitionDaoTest {

	private static class TestDao extends CacheTagDefinitionDao {
		public TestDao(ITagDefinitionDao theTagDefinitionDao, MemoryCacheService theMemoryCacheService) {
			super(theTagDefinitionDao, theMemoryCacheService);
		}

		@Override
		protected TagDefinition getTagOrNull(TransactionDetails theDetails,
											 TagTypeEnum theEnum,
											 String theScheme,
											 String theTerm,
											 String theLabel,
											 String theVersion,
											 Boolean theUserSelected) {
			try {
				TransactionSynchronizationManager.initSynchronization();
				return super.getTagOrNull(theDetails, theEnum, theScheme, theTerm, theLabel, theVersion, theUserSelected);
			} finally {
				TransactionSynchronizationManager.clearSynchronization();
			}
		}
	}

	@Mock
	private MemoryCacheService myMemoryCacheService;

	@Mock
	private ITagDefinitionDao tagDefinitionDao;

	@InjectMocks
	private TestDao myTestDao;

	@BeforeEach
	public void init() {
		LoggerFactory.getLogger(BaseHapiFhirDao.class);
	}

	@AfterEach
	public void tearDown() {
		// Cleanup logic if needed
	}

	@Test
	public void testGetTagOrNull_createsTag_ifNotFound() {
		// Arrange
		TagTypeEnum tagType = TagTypeEnum.TAG;
		String scheme = "http://localhost";
		String term = "code123";
		String label = "example label";
		String version = "v1.0";
		Boolean userSelected = true;

		TagDefinition newTag = new TagDefinition(tagType, scheme, term, label);
		newTag.setVersion(version);
		newTag.setUserSelected(userSelected);

		when(tagDefinitionDao.findByTagTypeAndSchemeAndTermAndVersionAndUserSelected(
			eq(tagType), eq(scheme), eq(term), eq(version), eq(userSelected), any(Pageable.class)))
			.thenReturn(List.of());

		when(tagDefinitionDao.save(any(TagDefinition.class))).thenReturn(newTag);

		// Act
		TagDefinition result = myTestDao.getTagOrNull(new TransactionDetails(), tagType, scheme, term, label, version, userSelected);

		// Assert
		assertEquals(newTag, result);
		verify(tagDefinitionDao).save(any(TagDefinition.class));
	}

	@Test
	public void testSimultaneousTagCreation_createsMultipleTags() throws InterruptedException, ExecutionException {
		int threadCount = 10;
		TagTypeEnum tagType = TagTypeEnum.TAG;
		String scheme = "http://localhost";
		String term = "code123";
		String label = "example label";
		String version = "v1.0";
		Boolean userSelected = true;

		TagDefinition expectedTag = new TagDefinition(tagType, scheme, term, label);
		expectedTag.setVersion(version);
		expectedTag.setUserSelected(userSelected);

		when(tagDefinitionDao.findByTagTypeAndSchemeAndTermAndVersionAndUserSelected(
			eq(tagType), eq(scheme), eq(term), eq(version), eq(userSelected), any(Pageable.class)))
			.thenReturn(List.of());
		when(tagDefinitionDao.save(any(TagDefinition.class))).thenReturn(expectedTag);

		// Run the test with multiple threads
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		try {
			List<Future<TagDefinition>> futures = new ArrayList<>();
			for (int i = 0; i < threadCount; i++) {
				futures.add(executor.submit(() -> myTestDao.getTagOrNull(new TransactionDetails(), tagType, scheme, term, label, version, userSelected)));
			}

			// Check results
			for (Future<TagDefinition> future : futures) {
				future.get();
			}
			verify(tagDefinitionDao, times(threadCount)).save(any(TagDefinition.class)); // multiple tags allowed
		} finally {
			executor.shutdown();
		}
	}
	////////// Static access tests

	@Test
	public void cleanProvenanceSourceUri() {
		assertEquals("", MetaUtil.cleanProvenanceSourceUriOrEmpty(null));
		assertEquals("abc", MetaUtil.cleanProvenanceSourceUriOrEmpty("abc"));
		assertEquals("abc", MetaUtil.cleanProvenanceSourceUriOrEmpty("abc#"));
		assertEquals("abc", MetaUtil.cleanProvenanceSourceUriOrEmpty("abc#def"));
		assertEquals("abc", MetaUtil.cleanProvenanceSourceUriOrEmpty("abc#def#ghi"));
	}
}
