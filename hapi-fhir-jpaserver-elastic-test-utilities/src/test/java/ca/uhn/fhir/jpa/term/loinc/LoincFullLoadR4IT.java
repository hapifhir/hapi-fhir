package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithLuceneHSearch;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.util.StopWatch;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Volume test which loads a full LOINC CodeSystem
 * Test is executed from child classes with Lucene or Elastic configuration
 * Requires 6Gb memory (set in pom file, 'surefire_jvm_args' property) and takes 3 Hs to run successfully
 */
@Disabled("The LOINC upload process this test runs consumes over 4G memory, and needs to be fixed before enabling this test.")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	LoincFullLoadR4IT.NoopMandatoryTransactionListener.class
	// pick up elastic or lucene engine:
//	, TestR4ConfigWithElasticHSearch.class
	, TestR4ConfigWithLuceneHSearch.class
})
public class LoincFullLoadR4IT extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(LoincFullLoadR4IT.class);

	public static final String LOINC_URL = "http://loinc.org";
	public static final String TEST_FILES_CLASSPATH = "loinc-full/";
	public static final String NULL = "'null'";

	public static final boolean CLEANUP_DATA = true;

	static {
		System.setProperty("unlimited_db_connection", "true");
	}


// -----------------------------------------------------------------------------------------
// full LOINC file

	public static final String CS_VERSION = "2.72";
	public static final int CS_CONCEPTS_NUMBER = 232_702;

	public static final String LOINC_PROPERTIES_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "v272_loincupload.properties";

	public static final String LOINC_ZIP_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "Loinc_2.72.zip";
// -----------------------------------------------------------------------------------------
// small file for debugging

//	public static final String CS_VERSION = "2.68";
//	public static final int CS_CONCEPTS_NUMBER = 81;
//
//	public static final String LOINC_PROPERTIES_CLASSPATH =
//		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "Loinc_small_v68.zip";
//
//	public static final String LOINC_ZIP_CLASSPATH =
//		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "v268_loincupload.properties";
	// -----------------------------------------------------------------------------------------

	@Autowired private FhirContext myFhirCtx;
	@Autowired private PlatformTransactionManager myTxManager;
	@Autowired private EntityManager myEntityManager;
	@Autowired private TermLoaderSvcImpl myTermLoaderSvc;
	@Autowired private ITermConceptDao myTermConceptDao;
	@Autowired private ITermReadSvc myTermReadSvc;
	@Autowired private ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired private ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	long termCodeSystemVersionWithVersionId;
	long termCodeSystemVersionWithNoVersionId;

	private Map<String, Long> conceptCounts;
	private Map<String, Long> conceptDesignationCounts;


	@Test()
	public void uploadLoincCodeSystem() throws FileNotFoundException {
		List<ITermLoaderSvc.FileDescriptor> myFileDescriptors = buildFileDescriptors();

		// upload terminology
		StopWatch sw = new StopWatch();
		myTermLoaderSvc.loadLoinc(myFileDescriptors, mySrd);
		ourLog.info("=================> Uploading terminology took {}", sw);

		// save all deferred concepts, properties, links, etc
		sw.restart();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		ourLog.info("=================> Saving all terminology deferred entities took {}", sw);
		validateSavedConceptsCount();

		sw.restart();
		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();
		ourLog.info("=================> Pre-expanding ValueSets took {}", sw);
		saveValueSetPreexpansionCounts();

//		// run reindexing operation
//		sw.restart();
//		reFreetextIndexTerminology();
//		ourLog.info("=================> Deleting and recreating freetext indexes took {}", sw);
//		validateFreetextCounts();
//
//		// remove ValueSet pre-expansions
//		sw.restart();
//		removeValueSetPreExpansions();
//		ourLog.info("=================> Removing all ValueSet pre-expansions took {}", sw);
//
//		// pre-expand  ValueSets again, after freetext reindexing
//		sw.restart();
//		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();
//		ourLog.info("=================> Pre-expanding ValueSets (again, after reindexing) took {}", sw);
//		validateValueSetPreexpansion();
	}



	private void removeValueSetPreExpansions() {
		List<TermValueSet> termValueSets = myTermValueSetDao.findAll();
		for (TermValueSet termValueSet : termValueSets) {
			myTermReadSvc.invalidatePreCalculatedExpansion(
				termValueSet.getResource().getIdDt(), new SystemRequestDetails());
		}
	}


	/**
	 * check number of TermConcepts (DB vs freetext-indexed)
	 */
	private void validateFreetextCounts() {
		int dbTermConceptCountForVersion = runInTransaction(() ->
			myTermConceptDao.countByCodeSystemVersion(termCodeSystemVersionWithVersionId) );
//		assertEquals(CS_CONCEPTS_NUMBER, dbTermConceptCountForVersion);

		long termConceptCountForVersion = searchAllIndexedTermConceptCount(termCodeSystemVersionWithVersionId);
		ourLog.info("=================> Number of freetext found concepts after re-indexing for version {}: {}",
			CS_VERSION, termConceptCountForVersion);
//		assertEquals(CS_CONCEPTS_NUMBER, termConceptCountForVersion);


		int dbTermConceptCountForNullVersion = runInTransaction(() ->
			myTermConceptDao.countByCodeSystemVersion(termCodeSystemVersionWithNoVersionId) );
//		assertEquals(CS_CONCEPTS_NUMBER, dbTermConceptCountForNullVersion);

		long termConceptCountNullVersion = searchAllIndexedTermConceptCount(termCodeSystemVersionWithNoVersionId);
		ourLog.info("=================> Number of freetext found concepts after re-indexing for version {}: {}",
			NULL, termConceptCountNullVersion);
//		assertEquals(CS_CONCEPTS_NUMBER, termConceptCountNullVersion);
	}


	/**
	 * Unused as we make mass indexer to clean them up as part of reindexing
	 */
	private void validateFreetextIndexesEmpty() {
		long termCodeSystemVersionWithVersionId = getTermCodeSystemVersionNotNullId();
		long termConceptCountVersioned = searchAllIndexedTermConceptCount(termCodeSystemVersionWithVersionId);
		assertEquals(0, termConceptCountVersioned);

		long termCodeSystemVersionWithNoVersionId = getTermCodeSystemVersionNullId();
		long termConceptCountNotVersioned = searchAllIndexedTermConceptCount(termCodeSystemVersionWithNoVersionId);
		assertEquals(0, termConceptCountNotVersioned);
	}


	private void saveValueSetPreexpansionCounts() {
		List<TermValueSet> termValueSets = myTermValueSetDao.findAll();
		conceptCounts = termValueSets.stream()
			.filter(vs -> vs.getVersion() == null || ! vs.getVersion().contains(CS_VERSION))
			.collect(Collectors.toMap(TermValueSet::getUrl, TermValueSet::getTotalConcepts));
		conceptDesignationCounts = termValueSets.stream()
			.filter(vs -> vs.getVersion() == null || ! vs.getVersion().contains(CS_VERSION))
			.collect(Collectors.toMap(TermValueSet::getUrl, TermValueSet::getTotalConceptDesignations));
	}


	/**
	 * Checks the number of VS Concepts and ConceptDesignations against values saved before reindexing
	 */
	private void validateValueSetPreexpansion() {
		List<TermValueSet> termValueSets = myTermValueSetDao.findAll();
		ourLog.debug("=================> validating ValueSet pre-expansion after recreating freetext indexes");
		for (TermValueSet termValueSet : termValueSets) {
//			ourLog.debug("=================> testing ValueSet: {}", termValueSet.getUrl());
			long conceptCount  = conceptCounts.get( termValueSet.getUrl() );
			assertEquals(conceptCount, termValueSet.getTotalConcepts());
			long conceptDesignationCount  = conceptDesignationCounts.get( termValueSet.getUrl() );
			assertEquals(conceptDesignationCount, termValueSet.getTotalConceptDesignations());
		}
	}


	private void validateSavedConceptsCount() {
		termCodeSystemVersionWithVersionId = getTermCodeSystemVersionNotNullId();
		int dbVersionedTermConceptCount = runInTransaction(() ->
			myTermConceptDao.countByCodeSystemVersion(termCodeSystemVersionWithVersionId) );
		ourLog.info("=================> Number of stored concepts for version {}: {}", CS_VERSION, dbVersionedTermConceptCount);
		assertEquals(CS_CONCEPTS_NUMBER, dbVersionedTermConceptCount);

		termCodeSystemVersionWithNoVersionId = getTermCodeSystemVersionNullId();
		int dbNotVersionedTermConceptCount = runInTransaction(() ->
			myTermConceptDao.countByCodeSystemVersion(termCodeSystemVersionWithNoVersionId) );
		ourLog.info("=================> Number of stored concepts for version {}: {}", NULL, dbNotVersionedTermConceptCount);
		assertEquals(CS_CONCEPTS_NUMBER, dbNotVersionedTermConceptCount);
	}



	private void reFreetextIndexTerminology() throws InterruptedException {
		myTermReadSvc.reindexTerminology();
	}


	/**
	 * Not used anymore but left as a sample
	 */
	private void reindexNotUsingMassIndexer() {
		int BATCH_READ_SIZE = 100;

		Session session = myEntityManager.unwrap(Session.class);
		SearchSession searchSession = Search.session( myEntityManager );
		CriteriaQuery<TermConcept> criteriaQuery = session.getCriteriaBuilder().createQuery(TermConcept.class);
		// no apparent gains here
//		criteriaQuery.from(TermConcept.class).fetch("myProperties", JoinType.INNER);
		criteriaQuery.from(TermConcept.class);

		SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
		Transaction tx = session.beginTransaction();

		// Scrollable results will avoid loading too many objects in memory
		ScrollableResults results = session.createQuery(criteriaQuery)
			.setFetchSize(BATCH_READ_SIZE)
			.setCacheable(false)
			.setReadOnly(true)
			.scroll(ScrollMode.FORWARD_ONLY);

		int indexedInTx = 0;
		while(results.next()) {
			indexedInTx++;
			indexingPlan.addOrUpdate(results.get(0)); //index each element
			if (indexedInTx % BATCH_READ_SIZE == 0) {
				indexingPlan.execute(); //apply changes to indexes
				session.clear(); //free memory since the queue is processed
			}
			if (indexedInTx % 50_000 == 0) {
				ourLog.info("=============================> Indexed {} TermConcepts", 50_000);
			}
		}
		ourLog.info("=============================> About to commit");
		tx.commit();
		ourLog.info("=============================> Changes committed");
	}




	private long getTermCodeSystemVersionNotNullId() {
		return runInTransaction(() -> {
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(LOINC_URL);
			TermCodeSystemVersion termCodeSystemVersion = myTermCodeSystemVersionDao
				.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), CS_VERSION);
			assertNotNull(termCodeSystemVersion);
			return termCodeSystemVersion.getPid();
		});
	}


	private long getTermCodeSystemVersionNullId() {
		return runInTransaction(() -> {
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(LOINC_URL);
			TermCodeSystemVersion termCodeSystemVersion = myTermCodeSystemVersionDao
				.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertNotNull(termCodeSystemVersion);
			return termCodeSystemVersion.getPid();
		});
	}


	private List<ITermLoaderSvc.FileDescriptor> buildFileDescriptors() throws FileNotFoundException {
		List<ITermLoaderSvc.FileDescriptor> fileDescriptors = new ArrayList<>();

		File propsFile = ResourceUtils.getFile(LOINC_PROPERTIES_CLASSPATH);
		fileDescriptors.add( new TerminologyUploaderProvider.FileBackedFileDescriptor(propsFile) );

		File zipFile = ResourceUtils.getFile(LOINC_ZIP_CLASSPATH);
		fileDescriptors.add( new TerminologyUploaderProvider.FileBackedFileDescriptor(zipFile) );

		return fileDescriptors;
	}



	private long searchAllIndexedTermConceptCount(long theCodeSystemVersionId) {
		return runInTransaction(() -> {
			SearchSession searchSession = Search.session(myEntityManager);
			SearchPredicateFactory predicate = searchSession.scope(TermConcept.class).predicate();
			PredicateFinalStep step = predicate.bool(b ->
				b.must(predicate.match().field("myCodeSystemVersionPid").matching(theCodeSystemVersionId)) );

			SearchQuery<EntityReference> termConceptsQuery = searchSession
				.search(TermConcept.class)
				.selectEntityReference()
				.where(f -> step)
				.toQuery();

			ourLog.trace("About to query: {}", termConceptsQuery.queryString());
			return termConceptsQuery.fetchTotalHitCount();
		});

	}

	/**
	 * This configuration bypasses the MandatoryTransactionListener, which breaks this test
	 */
	@Configuration
	public static class NoopMandatoryTransactionListener {

		@Bean
		public ProxyDataSourceBuilder.SingleQueryExecution getMandatoryTransactionListener() {
			return  new ProxyDataSourceBuilder.SingleQueryExecution() {
				@Override
				public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
				}
			};
		}
	}


	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

}

