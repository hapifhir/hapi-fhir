package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.util.StopWatch;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Sandbox test (not intended to run on CI build) so must be kept disabled
 *
 *	 Requires the loinc-full resource directory to contain the following three files:
 *	 _ Loinc.csv.gz
 *	 _ Loinc_1.11.zip and
 *	 _ v1.11_loincupload.properties
 *
 *	 but they are too large for the repo, so before running this test, copy them from:
 *	 https://drive.google.com/drive/folders/18be2R5IurlWnugkl18nDG7wrwPsOtfR-?usp=sharing
 *	 (SmileCDR has access)
 *
 * Can be executed with Lucene or Elastic configuration
 *
 * Requires 4Gb mem to run, so pom needs to be changed to run from IDE:
 *    <surefire_jvm_args>-Dfile.encoding=UTF-8 -Xmx5g</surefire_jvm_args>
 * or to run from maven use:
 * mvn test -pl :hapi-fhir-jpaserver-test-utilities -Dtest=LoincFullLoadR4SandboxIT#uploadLoincCodeSystem -Dsurefire_jvm_args="-Xmx5g"
 *
 */
@Disabled("Sandbox test which requires 5Gb memory")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	LoincFullLoadR4SandboxIT.NoopMandatoryTransactionListener.class

	// one of the following needs to be present
	// TestR4Config.class // uses in-memory DB
	,LoincFullLoadR4SandboxIT.OverriddenR4Config.class // your configured persistent DB

	// pick up elastic or lucene engine:
	,TestHSearchAddInConfig.NoFT.class
})
public class LoincFullLoadR4SandboxIT extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(LoincFullLoadR4SandboxIT.class);

	private static final DecimalFormat ourDecimalFormat = new DecimalFormat("#,###");

	public static final boolean USE_REAL_DB = true;
	public static final boolean LOAD_DB = false;
	public static final String DB_NAME = "testDB_new";


	public static final String LOINC_URL = "http://loinc.org";
	public static final String TEST_FILES_CLASSPATH = "loinc-full/";

	public static final boolean CLEANUP_DATA = true;

	static {
		System.setProperty("unlimited_db_connection", "true");
	}


// -----------------------------------------------------------------------------------------
// full LOINC file 1.11  (initially cloned from 2.73 for tests, with custom lonc.xml file with added 24 new properties)

	public static final String CS_VERSION = "1.11";
	public static final int CS_CONCEPTS_COUNT = 234_390;
	public static final int ASSOCIATED_OBSERVATIONS_COUNT = 8_058;
	public static final int ASK_AT_ORDER_ENTRY_COUNT = 65;

	public static final String LOINC_PROPERTIES_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "v1.11_loincupload.properties";

	public static final String LOINC_ZIP_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "Loinc_1.11.zip";

	public static final String LOINC_CSV_ZIP_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "Loinc.csv.gz";
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


	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;



	private long termCodeSystemVersionWithVersionId;

	private int associatedObservationsCount = 0;
	private int askAtOrderEntryCount = 0;
	private int processedPropertiesCounter = 0;

	private static List<String> recordPropertyNames;
	private static List<String> newRecordPropertyNames = List.of(
		"CHNG_TYPE",
		"DefinitionDescription",
		"CONSUMER_NAME",
		"FORMULA",
		"EXMPL_ANSWERS",
		"SURVEY_QUEST_TEXT",
		"SURVEY_QUEST_SRC",
		"UNITSREQUIRED",
		"RELATEDNAMES2",
		"SHORTNAME",
		"HL7_FIELD_SUBFIELD_ID",
		"EXTERNAL_COPYRIGHT_NOTICE",
		"EXAMPLE_UNITS",
		"LONG_COMMON_NAME",
		"EXAMPLE_UCUM_UNITS",
		"STATUS_REASON",
		"STATUS_TEXT",
		"CHANGE_REASON_PUBLIC",
		"COMMON_TEST_RANK",
		"COMMON_ORDER_RANK",
		"EXTERNAL_COPYRIGHT_LINK",
		"AskAtOrderEntry",				// coding
		"AssociatedObservations",		// Coding
		"ValidHL7AttachmentRequest"
	);

	@Test()
	public void uploadLoincCodeSystem() throws Exception {

		if (USE_REAL_DB && LOAD_DB) {
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
			
			return;
		}

		// validation:
		// create from loinc.csv file map of code | set of not-blank-properties
		// query each code and validate that all properties in map are set (can we check type also)

		List<Map<String, String>> conceptPropertyRecords = readCsvRecordsAsMap();

		validateCreatedConceptsHaveAllProperties( conceptPropertyRecords );

		ourLog.info("Processed properties          : {}", processedPropertiesCounter);
		ourLog.info("associatedObservationsCount   : {}", associatedObservationsCount);
		ourLog.info("askAtOrderEntryCount          : {}", askAtOrderEntryCount);
		ourLog.info("");

		assertEquals(ASK_AT_ORDER_ENTRY_COUNT, askAtOrderEntryCount);
		assertEquals(ASSOCIATED_OBSERVATIONS_COUNT, associatedObservationsCount);

}

	/**
	 * Used occasionally for some manual validation - don't delete
	 */
	private void queryForSpecificValueSet() {
		runInTransaction(() -> {
			Query q = myEntityManager.createQuery("from ForcedId where myForcedId like 'LG8749-6%'");
			@SuppressWarnings("unchecked")
			List<ForcedId> fIds = (List<ForcedId>) q.getResultList();
			long res_id = fIds.stream().map(ForcedId::getId).sorted().findFirst().get();

			Query q1 = myEntityManager.createQuery("from ResourceTable where id = " + res_id);
			@SuppressWarnings("unchecked")
			List<ResourceTable> vsList = (List<ResourceTable>) q1.getResultList();
			assertEquals(1, vsList.size());
			long vsLongId = vsList.get(0).getId();
			ValueSet vs = (ValueSet) myValueSetDao.toResource( vsList.get(0), false );
			assertNotNull(vs);

			Query q2 = myEntityManager.createQuery("from TermValueSet where myResource = " + vsLongId);
			@SuppressWarnings("unchecked")
			List<TermValueSet> tvsList = (List<TermValueSet>) q2.getResultList();
			assertEquals(1, tvsList.size());

			TermValueSet termValueSet = tvsList.get(0);
		});
	}


	private void validateCreatedConceptsHaveAllProperties(List<Map<String, String>> theConceptPropertyInputMap) {
		TermCodeSystemVersion tcsVersion = getTermCodeSystemVersion();

		ourLog.info("Properties to process: {}", ourDecimalFormat.format(theConceptPropertyInputMap.size()));

		for (Map<String, String> tcRecordMap : theConceptPropertyInputMap) {
			String recordCode = getRecordCode(tcRecordMap);
			processedPropertiesCounter++;

			runInTransaction(() -> {
				Optional<TermConcept> tcFomDbOpt = myTermConceptDao.findByCodeSystemAndCode(tcsVersion, recordCode);
				tcFomDbOpt.ifPresentOrElse(
					tc -> validateTermConceptEntry(tc, tcRecordMap),
					() -> ourLog.error("Couldn't find TermConcept with code: {} in DB", recordCode));
			});

			if (processedPropertiesCounter % 10_000 == 0) {
				ourLog.info("Processed properties: {}", ourDecimalFormat.format(processedPropertiesCounter));
			}
		}
		ourLog.info("");
	}


	private String getRecordCode(Map<String, String> tcRecordMap) {
		String recordCode = tcRecordMap.get("LOINC_NUM");
		assertNotNull(recordCode, "Record without LOINC_NUM filed ???");
		assertFalse(recordCode.isEmpty(), "Record with empty LOINC_NUM filed ???");
		return recordCode;
	}


	private void validateTermConceptEntry(TermConcept theTermConcept, Map<String, String> theRecordMap) {
		String recordCode = getRecordCode(theRecordMap);
		if ( ! theTermConcept.getCode().equals(recordCode) ) {
			fail("Received non matching inputs code from file: " + recordCode + ", code from DB: " + theTermConcept.getCode());
		}

		ourLog.trace("Validating TC with code: {}", theTermConcept.getCode());
		Map<String, Set<String>> tcConceptPropertyMap = theTermConcept.getProperties().stream()
			.collect(Collectors.groupingBy(TermConceptProperty::getKey, HashMap::new, mapping(TermConceptProperty::getValue, toSet())));

		validateNewProperties(recordCode, theRecordMap, tcConceptPropertyMap);
	}


	private void validateNewProperties(String theTcCode, Map<String, String> theRecordPropsMap, Map<String, Set<String>> theTcConceptPropertyMap) {
		// make sure we are good so far and both entries to compare are for same TermConcept code
		assertEquals(theTcCode, theRecordPropsMap.get("LOINC_NUM"), "theTcCode and record key (LOINC_NUM) must match");

		for (Map.Entry<String, String> recordEntry : theRecordPropsMap.entrySet()) {

			// match each non-blank property of type String from the file (except LOINC_NUM) to be a property of the concept
			if (recordEntry.getKey().equals("LOINC_NUM") || StringUtils.isEmpty(recordEntry.getValue()) ) { continue; }

			// bypass old properties
			if ( ! newRecordPropertyNames.contains(recordEntry.getKey()) ) { continue; }

			Set<String> tcConceptPropValues = theTcConceptPropertyMap.get(recordEntry.getKey());
			if (CollectionUtils.isEmpty(tcConceptPropValues)) {
				ourLog.error("TCConcept with code: {} does not have property: {} which in csv file has value: {}",
					theTcCode, recordEntry.getKey(), recordEntry.getValue());
				continue;
			}

			// special case because we need to parse ';' separated codes from file property value
			if ( "AssociatedObservations".equals(recordEntry.getKey()) ) {
				associatedObservationsCount++;
				validateAssociatedObservations(theTcCode, recordEntry, tcConceptPropValues);
				continue;
			}

			if ( "AskAtOrderEntry".equals(recordEntry.getKey()) ) { askAtOrderEntryCount++; }

			if ( ! tcConceptPropValues.contains(recordEntry.getValue()) ) {
				ourLog.error("For TC code: {}, prop: {}, values don't match. Record value: {} TC prop value: {}",
					theTcCode, recordEntry.getKey(), recordEntry.getValue(), String.join(" - ", tcConceptPropValues));
			}
		}
	}

	/**
	 * Validate that all file property codes become a "Coding" property on the TermConcept
	 */
	private void validateAssociatedObservations(String theTcCode, Map.Entry<String, String> recordEntry, Set<String> tcConceptPropValues) {
		List<String> recordPropertyCodes = parsePropertyCodeValues(recordEntry.getValue());

		for (String recordPropertyCode : recordPropertyCodes) {
			if ( ! tcConceptPropValues.contains(recordPropertyCode) ) {
				ourLog.error("For TC code: {}, prop: {}, record code: {} not found among properties: {}",
					theTcCode, recordEntry.getKey(), recordPropertyCode, String.join(" - ", tcConceptPropValues));
			}

		}
	}


	private List<String> parsePropertyCodeValues(String theValue) {
		return Arrays.stream( theValue.split(";") )
			.map(String::trim)
			.collect(Collectors.toList());
	}


	private List<Map<String, String>> readCsvRecordsAsMap() throws Exception {
		CSVParser parser = getCsvRecords();
		Iterator<CSVRecord> iter = parser.iterator();

		Map<String, Integer> headerMap = parser.getHeaderMap();
		recordPropertyNames = headerMap.entrySet().stream()
			.sorted(Comparator.comparingInt(Map.Entry::getValue))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
		ourLog.debug("Header map: {}", parser.getHeaderMap());

		int count = 0;

		List<Map<String, String>> records = new ArrayList<>();
		while (iter.hasNext()) {
			CSVRecord nextRecord = iter.next();
			if (! nextRecord.isConsistent()) {
				ourLog.error("Inconsistent record");
				continue;

			}
			records.add( nextRecord.toMap() );
			count++;
		}
		ourLog.info("Read and mapped {} csv file lines", count);
		return records;
	}


	public String getCvsStringFromZip(String theFilePath)  throws Exception {
		InputStream stream = new FileInputStream(ResourceUtils.getFile(theFilePath));
		assertNotNull(stream);
		stream = new GZIPInputStream(stream);
		return IOUtils.toString(stream, StandardCharsets.UTF_8);
	}


	@Nonnull
	private CSVParser getCsvRecords() throws Exception {
		Reader reader = new StringReader(getCvsStringFromZip(LOINC_CSV_ZIP_CLASSPATH));

		CSVFormat format = CSVFormat
			.newFormat(',')
			.withFirstRecordAsHeader()
			.withTrim()
			.withQuote('"')
			.withQuoteMode(QuoteMode.NON_NUMERIC);
		return new CSVParser(reader, format);
	}


	private void validateSavedConceptsCount() {
		termCodeSystemVersionWithVersionId = getTermCodeSystemVersion().getPid();
		int dbVersionedTermConceptCount = runInTransaction(() ->
			myTermConceptDao.countByCodeSystemVersion(termCodeSystemVersionWithVersionId) );
		ourLog.info("=================> Number of stored concepts for version {}: {}", CS_VERSION, dbVersionedTermConceptCount);
		assertEquals(CS_CONCEPTS_COUNT, dbVersionedTermConceptCount);
	}


	private TermCodeSystemVersion getTermCodeSystemVersion() {
		return runInTransaction(() -> {
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(LOINC_URL);
			TermCodeSystemVersion termCodeSystemVersion = myTermCodeSystemVersionDao
				.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), CS_VERSION);
			assertNotNull(termCodeSystemVersion);
			return termCodeSystemVersion;
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


//  List of all columns in Loinc.csv input file
//	private static final String[] recordFieldNames = {
//		"LOINC_NUM"
//		,"COMPONENT"
//		,"PROPERTY"
//		,"TIME_ASPCT"
//		,"SYSTEM"
//		,"SCALE_TYP"
//		,"METHOD_TYP"
//		,"CLASS"
//		,"VersionLastChanged"
//		,"CHNG_TYPE"
//		,"DefinitionDescription"
//		,"STATUS"
//		,"CONSUMER_NAME"
//		,"CLASSTYPE"
//		,"FORMULA"
//		,"EXMPL_ANSWERS"
//		,"SURVEY_QUEST_TEXT"
//		,"SURVEY_QUEST_SRC"
//		,"UNITSREQUIRED"
//		,"RELATEDNAMES2"
//		,"SHORTNAME"
//		,"ORDER_OBS"
//		,"HL7_FIELD_SUBFIELD_ID"
//		,"EXTERNAL_COPYRIGHT_NOTICE"
//		,"EXAMPLE_UNITS"
//		,"LONG_COMMON_NAME"
//		,"EXAMPLE_UCUM_UNITS"
//		,"STATUS_REASON"
//		,"STATUS_TEXT"
//		,"CHANGE_REASON_PUBLIC"
//		,"COMMON_TEST_RANK"
//		,"COMMON_ORDER_RANK"
//		,"COMMON_SI_TEST_RANK"
//		,"HL7_ATTACHMENT_STRUCTURE"
//		,"EXTERNAL_COPYRIGHT_LINK"
//		,"PanelType"
//		,"AskAtOrderEntry"
//		,"AssociatedObservations"
//		,"VersionFirstReleased"
//		,"ValidHL7AttachmentRequest"
//		,"DisplayName"
//	};



	/**
	 * This configuration bypasses the MandatoryTransactionListener, which breaks this test
	 */
	@Configuration
	public static class NoopMandatoryTransactionListener {

		@Bean
		public ProxyDataSourceBuilder.SingleQueryExecution getMandatoryTransactionListener() {
			return getNoopTXListener();
		}
	}


	private static ProxyDataSourceBuilder.SingleQueryExecution getNoopTXListener() {
		return (execInfo, queryInfoList) -> { };
	}


	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}



	@Configuration
	public static class OverriddenR4Config extends TestR4Config {

		@Override
		public void setConnectionProperties(BasicDataSource theDataSource) {
			if (USE_REAL_DB) {
				theDataSource.setDriver(new org.postgresql.Driver());
				theDataSource.setUrl("jdbc:postgresql://localhost/" + DB_NAME);
				theDataSource.setMaxWaitMillis(-1);  // indefinite
				theDataSource.setUsername("cdr");
				theDataSource.setPassword("smileCDR");
				theDataSource.setMaxTotal(ourMaxThreads);
				return;
			}

			super.setConnectionProperties(theDataSource);
		}

		@Override
		public String getHibernateDialect() {
			if (USE_REAL_DB) {
				return PostgreSQL10Dialect.class.getName();
			}

			return super.getHibernateDialect();
		}


		@Override
		public ProxyDataSourceBuilder.SingleQueryExecution getMandatoryTransactionListener() {
			return getNoopTXListener();
		}

	}


}
