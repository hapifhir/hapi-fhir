package ca.uhn.fhir.jpa.term.hsearch;

import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.term.TerminologyTestHelper;
import ca.uhn.fhir.jpa.term.ZipCollectionBuilder;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.persistence.EntityManager;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHSearchAddInConfig.LuceneFilesystem.class
	, ReindexTerminologyHSearchR4Test.NoopMandatoryTransactionListener.class
})
public class ReindexTerminologyHSearchR4Test extends BaseJpaR4Test {
	public static final String LOINC_URL = "http://loinc.org";
	public static final String TEST_FILES_CLASSPATH = "loinc-reindex/";
	public static final String NULL = "'null'";
	// set to false to avoid cleanup for debugging purposes
	public static final boolean CLEANUP_DATA = true;
	public static final String CS_VERSION = "2.68";
	public static final int CS_CONCEPTS_NUMBER = 81;
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexTerminologyHSearchR4Test.class);
	long termCodeSystemVersionWithVersionId;
	Map<String, Long> conceptCounts = Map.ofEntries(
		entry("http://loinc.org/vs", 81L),
		entry("http://loinc.org/vs/LG100-4", 0L),
		entry("http://loinc.org/vs/LG1695-8", 0L),
		entry("http://loinc.org/vs/LL1000-0", 3L),
		entry("http://loinc.org/vs/LL1001-8", 7L),
		entry("http://loinc.org/vs/LL1892-0", 0L),
		entry("http://loinc.org/vs/loinc-document-ontology", 1L),
		entry("http://loinc.org/vs/loinc-imaging-document-codes", 1L),
		entry("http://loinc.org/vs/loinc-rsna-radiology-playbook", 1L),
		entry("http://loinc.org/vs/loinc-universal-order-set", 0L),
		entry("http://loinc.org/vs/top-2000-lab-observations-si", 0L),
		entry("http://loinc.org/vs/top-2000-lab-observations-us", 0L)
	);
	Map<String, Long> conceptDesignationCounts = Map.ofEntries(
		entry("http://loinc.org/vs", 55L),
		entry("http://loinc.org/vs/LG100-4", 0L),
		entry("http://loinc.org/vs/LG1695-8", 0L),
		entry("http://loinc.org/vs/LL1000-0", 0L),
		entry("http://loinc.org/vs/LL1001-8", 0L),
		entry("http://loinc.org/vs/LL1892-0", 0L),
		entry("http://loinc.org/vs/loinc-document-ontology", 0L),
		entry("http://loinc.org/vs/loinc-imaging-document-codes", 1L),
		entry("http://loinc.org/vs/loinc-rsna-radiology-playbook", 1L),
		entry("http://loinc.org/vs/loinc-universal-order-set", 0L),
		entry("http://loinc.org/vs/top-2000-lab-observations-si", 0L),
		entry("http://loinc.org/vs/top-2000-lab-observations-us", 0L)
	);
	@Autowired
	private EntityManager myEntityManager;
	@Autowired
	private ITermConceptDao myTermConceptDao;
	@Autowired
	private ITermReadSvc myTermReadSvc;
	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@Test
	public void uploadLoincCodeSystem() throws IOException, InterruptedException {
		ZipCollectionBuilder fileDescriptors = buildFileDescriptors();

		Properties properties = new Properties();
		properties.load(ClasspathUtil.loadResourceAsStream(TEST_FILES_CLASSPATH + "v268_loincupload.properties"));

		// upload terminology
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion("2.68", fileDescriptors, false, properties);

		// save all deferred concepts, properties, links, etc
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		validateSavedConceptsCount();

		// check the number of freetext-indexed TermConcepts
		validateFreetextCounts();

		// pre-expand  ValueSets
		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();

		// pre-expansion uses freetext so check is to make sure all valuesets have the right number of concepts
		validateValueSetPreexpansion();

		// run reindexing operation
		reFreetextIndexTerminology();

		// validate again after reindexing the number of freetext-indexed TermConcepts
		validateFreetextCounts();

		// remove ValueSet pre-expansions
		removeValueSetPreExpansions();

		// pre-expand  ValueSets again, after freetext reindexing
		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();

		// pre-expansion uses freetext so check is to make sure all valuesets have the right number of concepts
		validateValueSetPreexpansion();
	}


	private void removeValueSetPreExpansions() {
		List<TermValueSet> termValueSets = myTermValueSetDao.findAll();
		for (TermValueSet termValueSet : termValueSets) {
			myTermReadSvc.invalidatePreCalculatedExpansion(termValueSet.getResource().getIdDt(), new SystemRequestDetails());
		}
	}


	/**
	 * check number of TermConcepts (DB vs freetext-indexed)
	 */
	private void validateFreetextCounts() {
		int dbTermConceptCountForVersion = runInTransaction(() ->
			myTermConceptDao.countByCodeSystemVersion(termCodeSystemVersionWithVersionId));
		assertEquals(CS_CONCEPTS_NUMBER, dbTermConceptCountForVersion);

		long termConceptCountForVersion = searchAllIndexedTermConceptCount(termCodeSystemVersionWithVersionId);
		ourLog.info("=================> Number of freetext found concepts after re-indexing for version {}: {}",
			CS_VERSION, termConceptCountForVersion);
		assertEquals(CS_CONCEPTS_NUMBER, termConceptCountForVersion);
	}


	/**
	 * Checks the number of VS Concepts and ConceptDesignations against test pre-specified values
	 */
	private void validateValueSetPreexpansion() {
		List<TermValueSet> termValueSets = myTermValueSetDao.findAll();
		for (TermValueSet termValueSet : termValueSets) {
			ourLog.debug("=================> testing ValueSet: {}", termValueSet.getUrl());
			long conceptCount = conceptCounts.get(termValueSet.getUrl());
			assertEquals(conceptCount, termValueSet.getTotalConcepts());
			long conceptDesignationCount = conceptDesignationCounts.get(termValueSet.getUrl());
			assertEquals(conceptDesignationCount, termValueSet.getTotalConceptDesignations());
		}
	}


	private void validateSavedConceptsCount() {
		termCodeSystemVersionWithVersionId = getTermCodeSystemVersionNotNullId();
		int dbVersionedTermConceptCount = runInTransaction(() -> {
			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findById(new IdAndPartitionId(termCodeSystemVersionWithVersionId)).orElseThrow();
			List<TermConcept> allConcepts = myTermConceptDao.findByCodeSystemVersion(csv);

			Set<String> allConceptsSet = allConcepts
				.stream()
				.map(TermConcept::getCode)
				.collect(Collectors.toCollection(TreeSet::new));
			ourLog.info("All Concepts: {}", allConceptsSet);

			return allConceptsSet.size();
		});
		ourLog.info("=================> Number of stored concepts for version {}: {}", CS_VERSION, dbVersionedTermConceptCount);
		assertEquals(CS_CONCEPTS_NUMBER, dbVersionedTermConceptCount);

	}


	private void reFreetextIndexTerminology() throws InterruptedException {
		myTermReadSvc.reindexTerminology();
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



	private ZipCollectionBuilder buildFileDescriptors() throws IOException {
		return new ZipCollectionBuilder(ClasspathUtil.loadResourceAsByteArray(TEST_FILES_CLASSPATH + "Loinc_small_v68.zip"));
	}


	private long searchAllIndexedTermConceptCount(long theCodeSystemVersionId) {
		return runInTransaction(() -> {
			SearchSession searchSession = Search.session(myEntityManager);
			SearchPredicateFactory predicate = searchSession.scope(TermConcept.class).predicate();
			PredicateFinalStep step = predicate.bool(b ->
				b.must(predicate.match().field("myCodeSystemVersionPid").matching(theCodeSystemVersionId)));

			SearchQuery<EntityReference> termConceptsQuery = searchSession
				.search(TermConcept.class)
				.selectEntityReference()
				.where(f -> step)
				.toQuery();

			ourLog.trace("About to query: {}", termConceptsQuery.queryString());
			return termConceptsQuery.fetchTotalHitCount();
		});

	}

	@Override
	@AfterEach
	public void afterCleanupDao() {
		if (CLEANUP_DATA) {
			super.afterCleanupDao();
		}
	}

	@Override
	@AfterEach
	public void afterClearTerminologyCaches() {
		if (CLEANUP_DATA) {
			super.afterClearTerminologyCaches();
		}
	}

	@Override
	@AfterEach
	public void afterPurgeDatabase() {
		if (CLEANUP_DATA) {
			super.afterPurgeDatabase();
		}
	}

	@Override
	@AfterEach
	public void afterEachClearCaches() {
		if (CLEANUP_DATA) {
			super.afterEachClearCaches();
		}
	}

	/**
	 * This configuration bypasses the MandatoryTransactionListener, which breaks this test
	 * (I think it is because hibernate search massIndexer starts threads which don't participate of test transactions)
	 */
	@Configuration
	public static class NoopMandatoryTransactionListener {

		@Bean
		public ProxyDataSourceBuilder.SingleQueryExecution getMandatoryTransactionListener() {
			return new ProxyDataSourceBuilder.SingleQueryExecution() {
				@Override
				public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
				}
			};
		}
	}
}

