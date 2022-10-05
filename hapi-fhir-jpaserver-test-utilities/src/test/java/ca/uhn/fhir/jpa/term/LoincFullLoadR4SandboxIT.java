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
import ca.uhn.fhir.jpa.term.loinc.LoincMapToHandler;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.testing.google.MultimapAsMapGetTester;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
@Disabled("Sandbox test")
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
	public static final String DB_NAME = "testDB_mapto";




	public static final String LOINC_URL = "http://loinc.org";
	public static final String TEST_FILES_CLASSPATH = "loinc-full/";

	public static final boolean CLEANUP_DATA = true;

	static {
		System.setProperty("unlimited_db_connection", "true");
	}

	private final Collection<Executable> mapToAsserts = new ArrayList<>();


// -----------------------------------------------------------------------------------------
// full LOINC file 1.11  (initially cloned from 2.73 for tests, with custom lonc.xml file with added 24 new properties)

	public static final String CS_VERSION = "1.11";
	public static final int CS_CONCEPTS_COUNT = 234_390;
	public static final int ASSOCIATED_OBSERVATIONS_COUNT = 8_058;
	public static final int ASK_AT_ORDER_ENTRY_COUNT = 65;

	public static final String LOINC_PROPERTIES_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "v1.11_loincupload.properties";

	public static final String LOINC_ZIP_CLASSPATH =
		ResourceUtils.CLASSPATH_URL_PREFIX + TEST_FILES_CLASSPATH + "Loinc_1.11_changed.zip";

	public static final String LOINC_CSV_FILE_ZIP_PATH = "Loinc_1.11_changed/LoincTable/Loinc.csv";

	public static final String LOINC_MAP_TO_FILE_ZIP_PATH = "Loinc_1.11_changed/LoincTable/MapTo.csv";
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
//			saveAllDeferredNoTimeout();
			myTerminologyDeferredStorageSvc.saveAllDeferred();

			ourLog.info("=================> Saving all terminology deferred entities took {}", sw);
			validateSavedConceptsCount();

			// tested properties have no special relation with ValueSet(s)
//			sw.restart();
//			myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();
//			ourLog.info("=================> Pre-expanding ValueSets took {}", sw);
			
			return;
		}

		// validation:
		// create from loinc.csv file map of code | set of not-blank-properties
		// create from mapto.csv file map of code | Pair<mapToCode, display>
		// query each code and validate that all properties in both maps are set

		List<Map<String, String>> conceptPropertyCvsMap = readLoincCsvRecordsAsMap();
		Multimap<String, Pair<String, String>> conceptMapToCvsMap = readMapToCsvRecordsAsMap();

		validateCreatedConceptsHaveAllProperties( conceptPropertyCvsMap, conceptMapToCvsMap );

		ourLog.info("Processed properties          : {}", processedPropertiesCounter);
		ourLog.info("associatedObservationsCount   : {}", associatedObservationsCount);
		ourLog.info("askAtOrderEntryCount          : {}", askAtOrderEntryCount);
		ourLog.info("");

		assertEquals(ASK_AT_ORDER_ENTRY_COUNT, askAtOrderEntryCount);
		assertEquals(ASSOCIATED_OBSERVATIONS_COUNT, associatedObservationsCount);

		// ass asserts are used for some validation, but we want all problems to be displayed,
		// we just collect assertions and execute them all et the end (here).
		assertAll(mapToAsserts);
	}


	private void saveAllDeferredNoTimeout() {
		while( ! myTerminologyDeferredStorageSvc.isStorageQueueEmpty() ) {
			myTerminologyDeferredStorageSvc.saveDeferred();
		}
	}

	/**
	 * Used occasionally for some manual validation - don't delete
	 */
	private void queryForSpecificValueSet() {
		runInTransaction(() -> {
			Query q = myEntityManager.createQuery("from ForcedId where myForcedId like 'LG8749-6%'");
			@SuppressWarnings("unchecked")
			List<ForcedId> fIds = (List<ForcedId>) q.getResultList();
			long res_id = fIds.stream().map(ForcedId::getId).sorted().findFirst().orElse(fail("ForcedId not found"));

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


	private void validateCreatedConceptsHaveAllProperties(List<Map<String, String>> theConceptPropertyInputMap,
			Multimap<String, Pair<String, String>> theConceptMapToCvsMap) {

		TermCodeSystemVersion tcsVersion = getTermCodeSystemVersion();

		ourLog.info("Properties to process: {}", ourDecimalFormat.format(theConceptPropertyInputMap.size()));

		for (Map<String, String> tcRecordMap : theConceptPropertyInputMap) {
			String recordCode = getRecordCode(tcRecordMap);
			processedPropertiesCounter++;

			runInTransaction(() -> {
				Optional<TermConcept> tcFomDbOpt = myTermConceptDao.findByCodeSystemAndCode(tcsVersion, recordCode);
				tcFomDbOpt.ifPresentOrElse(
					tc -> validateTermConceptEntry(tc, tcRecordMap, theConceptMapToCvsMap),
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


	private void validateTermConceptEntry(TermConcept theTermConcept,
													  Map<String, String> theRecordMap, Multimap<String, Pair<String, String>> theConceptMapToCvsMap) {

		String recordCode = getRecordCode(theRecordMap);
		if ( ! theTermConcept.getCode().equals(recordCode) ) {
			fail("Received non matching inputs code from file: " + recordCode + ", code from DB: " + theTermConcept.getCode());
		}

		ourLog.trace("Validating new properties for TC with code: {}", theTermConcept.getCode());
		// map of TC property name | set of property values
		HashMap<String, Set<Pair<String, String>>> tcConceptPropertyMap = theTermConcept.getProperties().stream()
			.collect(Collectors.groupingBy(TermConceptProperty::getKey,
				HashMap::new,
				mapping(tcp -> Pair.of(tcp.getValue(), tcp.getDisplay()), toSet())));

		validateNewProperties(recordCode, theRecordMap, tcConceptPropertyMap);

		Collection<Pair<String, String>> toMapRecordForTermConcept = theConceptMapToCvsMap.get(recordCode);
		validateMapToProperties(recordCode, tcConceptPropertyMap, toMapRecordForTermConcept);
	}


	private void validateMapToProperties(String theRecordCode,
			HashMap<String, Set<Pair<String, String>>> theTcConceptPropertyMap,
			Collection<Pair<String, String>> theToMapRecordForTC) {

		if (CollectionUtils.isEmpty(theToMapRecordForTC)) { return; } // no MapTo record for this TermConcept

		ourLog.trace("Validating MapTo properties for TC with code: {}", theRecordCode);

		Set<Pair<String, String>> tcConceptProps = theTcConceptPropertyMap.get("MAP_TO");
		HashSet<Pair<String, String>> theToMapRecordForTCAsSet = new HashSet<>(theToMapRecordForTC);

		mapToAsserts.add( () -> assertEquals(tcConceptProps, theToMapRecordForTCAsSet, "TermConcept for code: '" +
				theRecordCode + "' 'MAP_TO' properties don't match MapTo.csv file properties") );
	}


	private void validateNewProperties(String theTcCode, Map<String, String> theRecordPropsMap,
												  HashMap<String, Set<Pair<String, String>>> theTcConceptPropertyMap) {

		// make sure we are good so far and both entries to compare are for same TermConcept code
		assertEquals(theTcCode, theRecordPropsMap.get("LOINC_NUM"), "theTcCode and record key (LOINC_NUM) must match");

		for (Map.Entry<String, String> recordEntry : theRecordPropsMap.entrySet()) {

			// match each non-blank property of type String from the file (except LOINC_NUM) to be a property of the concept
			if (recordEntry.getKey().equals("LOINC_NUM") || StringUtils.isEmpty(recordEntry.getValue()) ) { continue; }

			// bypass old properties
			if ( ! newRecordPropertyNames.contains(recordEntry.getKey()) ) { continue; }

			Set<Pair<String, String>> tcConceptPropValues = theTcConceptPropertyMap.get(recordEntry.getKey());
			if (CollectionUtils.isEmpty(tcConceptPropValues)) {
				ourLog.error("TCConcept with code: {} does not have property: {} which in csv file has value: {}",
					theTcCode, recordEntry.getKey(), recordEntry.getValue());
				continue;
			}

			// extract only codes from code-display pairs
			Set<String> tcConceptCodeValues = tcConceptPropValues.stream().map(Pair::getLeft).collect(toSet());

			// special case because we need to parse ';' separated codes from file property value
			if ( "AssociatedObservations".equals(recordEntry.getKey()) ) {
				associatedObservationsCount++;
				validateAssociatedObservations(theTcCode, recordEntry, tcConceptCodeValues);
				continue;
			}

			if ( "AskAtOrderEntry".equals(recordEntry.getKey()) ) { askAtOrderEntryCount++; }

			if ( ! tcConceptCodeValues.contains(recordEntry.getValue()) ) {
				ourLog.error("For TC code: {}, prop: {}, values don't match. Record value: {} TC prop value: {}",
					theTcCode, recordEntry.getKey(), recordEntry.getValue(), String.join(" - ", tcConceptCodeValues));
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


	private List<Map<String, String>> readLoincCsvRecordsAsMap() throws Exception {
		CSVParser parser = getParserForZipFile(LOINC_ZIP_CLASSPATH, LOINC_CSV_FILE_ZIP_PATH);
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


	private Multimap<String, Pair<String, String>> readMapToCsvRecordsAsMap() throws Exception {
		CSVParser parser = getParserForZipFile(LOINC_ZIP_CLASSPATH, LOINC_MAP_TO_FILE_ZIP_PATH);

		Map<String, Integer> headerMap = parser.getHeaderMap();
		recordPropertyNames = headerMap.entrySet().stream()
			.sorted(Comparator.comparingInt(Map.Entry::getValue))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
		ourLog.debug("Header map: {}", parser.getHeaderMap());

		Multimap<String, Pair<String, String>> records = ArrayListMultimap.create();
		int count = 0;

		for (CSVRecord nextRecord : parser) {
			if (!nextRecord.isConsistent()) {
				ourLog.error("Inconsistent record");
				continue;
			}
			String code = nextRecord.get(LoincMapToHandler.CONCEPT_CODE_PROP_NAME);
			assertNotNull(code, "MapTo record with blank '" + LoincMapToHandler.CONCEPT_CODE_PROP_NAME + "' field: " + nextRecord);
			String toValue = nextRecord.get(LoincMapToHandler.MAP_TO_PROP_NAME);
			assertNotNull(code, "MapTo record with blank '" + LoincMapToHandler.MAP_TO_PROP_NAME + "' field: " + nextRecord);

			records.put(code, Pair.of(toValue, nextRecord.get(LoincMapToHandler.DISPLAY_PROP_NAME)));
			count++;
		}
		ourLog.info("Read and mapped {} csv file lines into {} map entries", count, records.asMap().size());
		return records;
	}




	@Nonnull
	private CSVParser getParserForZipFile(String theZipFileClassPath, String theFileEntryPath) throws Exception {
		Reader reader = new StringReader(getCvsStringFromZip(theZipFileClassPath, theFileEntryPath));

		CSVFormat format = CSVFormat
			.newFormat(',')
			.withFirstRecordAsHeader()
			.withTrim()
			.withQuote('"')
			.withQuoteMode(QuoteMode.NON_NUMERIC);
		return new CSVParser(reader, format);
	}


	public String getCvsStringFromZip(String theFilePath, String theZipFileEntryPath) {
		try (ZipFile zipFile = new ZipFile(ResourceUtils.getFile(theFilePath))) {

			ZipEntry zipEntry = zipFile.getEntry(theZipFileEntryPath);
			assertNotNull(zipEntry, "Couldn't find file: " + theZipFileEntryPath + " inside zip file: " + theFilePath);
			return IOUtils.toString(zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8);

		} catch (IOException e) {
			fail(e.getMessage());
		}

		fail("Couldn't find " + theFilePath + "/" + theZipFileEntryPath);
		return null;
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
