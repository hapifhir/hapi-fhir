package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_MAKE_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_XML_FILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Tests load and validate CodeSystem and ValueSet so test names as uploadFirstCurrent... mean uploadCodeSystemAndValueSetCurrent...
 */
public class TerminologySvcImplCurrentVersionR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplCurrentVersionR4Test.class);

	public static final String BASE_LOINC_URL = "http://loinc.org";
	public static final String BASE_LOINC_VS_URL = BASE_LOINC_URL + "/vs/";

	// some ValueSets have a version specified independent of the CS version being uploaded. This one doesn't
	public static final String VS_NO_VERSIONED_ON_UPLOAD_ID 	= "loinc-rsna-radiology-playbook";
	public static final String VS_NO_VERSIONED_ON_UPLOAD 		= BASE_LOINC_VS_URL + VS_NO_VERSIONED_ON_UPLOAD_ID;
	public static final String VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE 		= "17787-3";
	public static final String VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY 	= "NM Thyroid gland Study report";

	// some ValueSets have a version specified independent of the CS version being uploaded. This is one of them
	public static final String VS_VERSIONED_ON_UPLOAD_ID 	= "LL1000-0";
	public static final String VS_VERSIONED_ON_UPLOAD 		= BASE_LOINC_VS_URL + VS_VERSIONED_ON_UPLOAD_ID;
	public static final String VS_VERSIONED_ON_UPLOAD_FIRST_CODE 		= "LA13825-7";
	public static final String VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY 	= "1 slice or 1 dinner roll";

	public static final String VS_ANSWER_LIST_VERSION 		= "Beta.1";
	public static final Set<String> possibleVersions = Sets.newHashSet("2.67", "2.68", "2.69");

	@Mock
	HttpServletResponse mockServletResponse;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	ServletRequestDetails mockRequestDetails;

	@Autowired private EntityManager myEntityManager;
	@Autowired private TermLoaderSvcImpl myTermLoaderSvc;
	@Autowired private ITermReadSvc myITermReadSvc;

	@Autowired
	@Qualifier(BaseConfig.JPA_VALIDATION_SUPPORT)
	private IValidationSupport myJpaPersistedResourceValidationSupport;

	private ZipCollectionBuilder myFiles;
	private ServletRequestDetails myRequestDetails = new ServletRequestDetails();

	private Properties uploadProperties;
	private IFhirResourceDao<ValueSet> dao;



//	@BeforeAll
//	public static void beforeAll() throws Exception {
//		// remove DB
//		Files.deleteIfExists(Paths.get(
//			"/Users/juan.marchionattosmilecdr.com/projects/hapi-fhir/hapi-fhir-jpaserver-base/testdb_r4_2.mv.db"));
//	}

	@BeforeEach
	public void beforeEach() throws Exception {
//		myTermLoaderSvc = TermLoaderSvcImpl.withoutProxyCheck(myTermDeferredStorageSvc, myTermCodeSystemStorageSvc);

		//		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();


		File file = ResourceUtils.getFile("classpath:loinc-ver/" + LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		uploadProperties = new Properties();
		uploadProperties.load(new FileInputStream(file));

		dao = (IFhirResourceDao<ValueSet>) myDaoRegistry.getResourceDao(ValueSet.class);

		when(mockRequestDetails.getServer().getDefaultPageSize()).thenReturn(25);

	}


	/**
	 * For input version or for current (when input is null) validates search, expand, lookup and validateCode operations
	 */
	private void validateOperations(String currentVersion, Collection<String> theExpectedVersions) {
		validateValueSetSearch(theExpectedVersions);

		validateValueExpand(currentVersion, theExpectedVersions);

//		validateValueLookup(currentVersion, theExpectedVersions);
//
//		validateValidateCode(currentVersion, theExpectedVersions);
	}


	private void validateValidateCode(String theCurrentVersion, Collection<String> allVersions) {
		IValidationSupport.CodeValidationResult resultNoVersioned = myCodeSystemDao.validateCode(null,
			new UriType(BASE_LOINC_URL), null, new CodeType(VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE),
			null, null, null, null);
		assertNotNull(resultNoVersioned);
		assertEquals(prefixWithVersion(theCurrentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), resultNoVersioned.getDisplay());

		IValidationSupport.CodeValidationResult resultVersioned = myCodeSystemDao.validateCode(null,
			new UriType(BASE_LOINC_URL), null, new CodeType(VS_VERSIONED_ON_UPLOAD_FIRST_CODE),
			null, null, null, null);
		assertNotNull(resultVersioned);
		assertEquals(prefixWithVersion(theCurrentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), resultVersioned.getDisplay());

		allVersions.forEach(this::validateValidateCodeForVersion);
	}


	private void validateValidateCodeForVersion(String theVersion) {
		IValidationSupport.CodeValidationResult resultNoVersioned = myCodeSystemDao.validateCode(null,
			new UriType(BASE_LOINC_URL), null, new CodeType(VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE),
			null, null, null, null);
		assertNotNull(resultNoVersioned);
		assertEquals(prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), resultNoVersioned.getDisplay());

		IValidationSupport.CodeValidationResult resultVersioned = myCodeSystemDao.validateCode(null,
			new UriType(BASE_LOINC_URL), null, new CodeType(VS_VERSIONED_ON_UPLOAD_FIRST_CODE),
			null, null, null, null);
		assertNotNull(resultVersioned);
		assertEquals(prefixWithVersion(theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), resultVersioned.getDisplay());
	}



	private void validateValueLookup(String theCurrentVersion, Collection<String> allVersions) {
		IValidationSupport.LookupCodeResult resultNoVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport), BASE_LOINC_URL, VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE, null);
		assertNotNull(resultNoVer);
		String expectedNoVer = prefixWithVersion(theCurrentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedNoVer, resultNoVer.getCodeDisplay());

		IValidationSupport.LookupCodeResult resultWithVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport), BASE_LOINC_URL, VS_VERSIONED_ON_UPLOAD_FIRST_CODE, null);
		assertNotNull(resultWithVer);
		String expectedWithVer = prefixWithVersion(theCurrentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedWithVer, resultWithVer.getCodeDisplay());

		allVersions.forEach(this::lookupForVersion);
	}


	private void lookupForVersion(String theVersion) {
		IValidationSupport.LookupCodeResult resultNoVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport), BASE_LOINC_URL + "|" + theVersion,
			VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE, null);
		assertNotNull(resultNoVer);
		String expectedNoVer = prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedNoVer, resultNoVer.getCodeDisplay());

		IValidationSupport.LookupCodeResult resultWithVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport), BASE_LOINC_URL + "|" + theVersion,
			VS_VERSIONED_ON_UPLOAD_FIRST_CODE, null);
		assertNotNull(resultWithVer);
		String expectedWithVer = prefixWithVersion(theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedWithVer, resultWithVer.getCodeDisplay());
	}


	private String prefixWithVersion(String version, String suffix) {
		return (version == null ? "" : "v" + version + " ")  + suffix;

	}

	private void validateValueExpand(String currentVersion, Collection<String> theAllVersions) {
		// for CS ver = null, VS ver = null
		ValueSet vs = myValueSetDao.expandByIdentifier(VS_NO_VERSIONED_ON_UPLOAD, null);
		assertEquals(1, vs.getExpansion().getContains().size());

		// version was added before code display to validate
		assertEquals(prefixWithVersion(currentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs.getExpansion().getContains().iterator().next().getDisplay());

		// for CS ver = null, VS ver != null
		ValueSet vs1 = myValueSetDao.expandByIdentifier(VS_VERSIONED_ON_UPLOAD + "|" + VS_ANSWER_LIST_VERSION, null);
		assertEquals(3, vs1.getExpansion().getContains().size());

		assertEquals(prefixWithVersion(currentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs1.getExpansion().getContains().iterator().next().getDisplay());

		validateExpandedTermConcepts(currentVersion, theAllVersions);

		// now for each uploaded version
		theAllVersions.forEach(this::validateValueExpandForVersion);
	}


	private void validateExpandedTermConcepts(String theCurrentVersion, Collection<String> theAllVersions) {
		@SuppressWarnings("unchecked")
		TermConcept termConceptNoVerCsvNoVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId is null").getSingleResult();
		assertNotNull(termConceptNoVerCsvNoVer);
		assertEquals(VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY, termConceptNoVerCsvNoVer.getDisplay());

		@SuppressWarnings("unchecked")
		TermConcept termConceptVerCsvNoVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId is null").getSingleResult();
		assertNotNull(termConceptVerCsvNoVer);
		assertEquals(VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY, termConceptVerCsvNoVer.getDisplay());

		if (theCurrentVersion != null) {
			@SuppressWarnings("unchecked")
			TermConcept termConceptNoVerCsvVer = (TermConcept) myEntityManager.createQuery(
				"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
					VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId is null").getSingleResult();
			assertNotNull(termConceptNoVerCsvVer);
			assertEquals(VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY, termConceptNoVerCsvVer.getDisplay());

			@SuppressWarnings("unchecked")
			TermConcept termConceptVerCsvVer = (TermConcept) myEntityManager.createQuery(
				"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
					VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId is null").getSingleResult();
			assertNotNull(termConceptVerCsvVer);
			assertEquals(VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY, termConceptVerCsvVer.getDisplay());
		}


		theAllVersions.forEach(this::validateExpandedTermConceptsForVersion);
	}


	private void validateExpandedTermConceptsForVersion(String theVersion) {
		@SuppressWarnings("unchecked")
		TermConcept termConceptNoVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId = '" + theVersion + "'").getSingleResult();
		assertNotNull(termConceptNoVer);
		assertEquals(prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptNoVer.getDisplay());

		@SuppressWarnings("unchecked")
		TermConcept termConceptVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId = '" + theVersion + "'").getSingleResult();
		assertNotNull(termConceptVer);
		assertEquals(prefixWithVersion(theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptVer.getDisplay());
	}


	private void validateValueExpandForVersion(String theVersion) {
		// for CS ver != null, VS ver = null

		ValueSet vs2 = myValueSetDao.expandByIdentifier(
			VS_NO_VERSIONED_ON_UPLOAD + "|" + theVersion, null);
		assertEquals(1, vs2.getExpansion().getContains().size());

		// version was added before code display to validate
		assertEquals(prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs2.getExpansion().getContains().iterator().next().getDisplay());


		// for CS ver != null, VS ver != null

		ValueSet vs3 = myValueSetDao.expandByIdentifier(
			VS_VERSIONED_ON_UPLOAD + "|" + VS_ANSWER_LIST_VERSION + "-" + theVersion, null);
		assertEquals(3, vs3.getExpansion().getContains().size());

		// version was added before code display to validate
		assertEquals(prefixWithVersion( theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs3.getExpansion().getContains().iterator().next().getDisplay());
	}


	private void validateValueSetSearch(Collection<String> theExpectedIdVersions) {
		// first validate search for CS ver = null VS ver = null

		SearchParameterMap paramsNoUploadVer = new SearchParameterMap("url", new UriParam(VS_NO_VERSIONED_ON_UPLOAD));
		int expectedResultQty = theExpectedIdVersions.size() + 1;  // + 1 because an extra null version (the current) is always present
		IBundleProvider noUploadVerResult  = dao.search(paramsNoUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> noUploadVerValueSets = noUploadVerResult.getAllResources();
		assertEquals(expectedResultQty, noUploadVerValueSets.size());

		matchUnqualifiedIds(noUploadVerValueSets, theExpectedIdVersions);

		// now  validate search for CS ver = null VS ver != null

		SearchParameterMap paramsUploadVer = new SearchParameterMap("url", new UriParam(VS_VERSIONED_ON_UPLOAD));
		paramsUploadVer.add("version", new TokenParam(VS_ANSWER_LIST_VERSION));
		IBundleProvider uploadVerResult  = dao.search(paramsUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadVerValueSets = uploadVerResult.getAllResources();
		assertEquals(1, uploadVerValueSets.size());

		assertEquals(VS_VERSIONED_ON_UPLOAD_ID, uploadVerValueSets.get(0).getIdElement().getIdPart());
		assertEquals( VS_ANSWER_LIST_VERSION, ((ValueSet ) uploadVerValueSets.get(0)).getVersion());

		// now validate each specific uploaded version
		theExpectedIdVersions.forEach(this::validateValueSetSearchForVersion);
	}


	/**
	 * Some ValueSets (IE: AnswerLists), can have a specific version, different than the version of the
	 * CodeSystem with which they were uploaded. That version is what we distinguish in both sets of tests here,
	 * no the CodeSystem version.
	 */
	private void validateValueSetSearchForVersion(String theVersion) {
		// for no versioned VS (VS version, different than  CS version)

		SearchParameterMap paramsUploadNoVer = new SearchParameterMap("url", new UriParam(VS_NO_VERSIONED_ON_UPLOAD));
		paramsUploadNoVer.add("version", new TokenParam(theVersion));

		IBundleProvider uploadNoVerResult  = dao.search(paramsUploadNoVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadNoVerValueSets = uploadNoVerResult.getAllResources();
		assertEquals(1, uploadNoVerValueSets.size());

		ValueSet loadNoVersionValueSet = (ValueSet) uploadNoVerValueSets.get(0);
		String expectedLoadNoVersionUnqualifiedId = VS_NO_VERSIONED_ON_UPLOAD_ID + (theVersion == null ? "" : "-" + theVersion);
		assertEquals(expectedLoadNoVersionUnqualifiedId, loadNoVersionValueSet.getIdElement().getIdPart());


		// versioned VS (VS version, different than  CS version)

		SearchParameterMap paramsUploadVer = new SearchParameterMap("url", new UriParam(VS_VERSIONED_ON_UPLOAD));
		paramsUploadVer.add("version", new TokenParam(VS_ANSWER_LIST_VERSION + "-" + theVersion));

		IBundleProvider uploadVerResult  = dao.search(paramsUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadVerValueSets = uploadVerResult.getAllResources();
		assertEquals(1, uploadVerValueSets.size());

		ValueSet loadVersionValueSet = (ValueSet) uploadVerValueSets.get(0);
		String expectedLoadVersionUnqualifiedId = VS_VERSIONED_ON_UPLOAD_ID + (theVersion == null ? "" : "-" + theVersion);
		assertEquals(expectedLoadVersionUnqualifiedId, loadVersionValueSet.getIdElement().getIdPart());
	}


	/**
	 * Validates that the collection of unqualified IDs of each element of theValueSets matches the expected
	 * unqualifiedIds corresponding to the uploaded versions plus one with no version
	 *
	 * @param theValueSets the ValueSet collection
	 * @param theExpectedIdVersions the collection of expected versions
	 */
	private void matchUnqualifiedIds(List<IBaseResource> theValueSets, Collection<String> theExpectedIdVersions) {
		// set should contain one entry per expectedVersion
		List<String> expectedNoVersionUnqualifiedIds = theExpectedIdVersions.stream()
			.map(expVer -> VS_NO_VERSIONED_ON_UPLOAD_ID + "-" + expVer)
			.collect(Collectors.toList());

		// plus one entry for null version
		expectedNoVersionUnqualifiedIds.add(VS_NO_VERSIONED_ON_UPLOAD_ID);

		List <String> resultUnqualifiedIds = theValueSets.stream()
			.map(r -> r.getIdElement().getIdPart())
			.collect(Collectors.toList());

		assertThat(resultUnqualifiedIds, containsInAnyOrder(resultUnqualifiedIds.toArray()));

		List <String> resultVersions = theValueSets.stream()
			.map(r -> ((ValueSet) r).getVersion())
			.collect(Collectors.toList());

		Set<String> theExpectedIdVersionsPlusNull = Sets.newHashSet(theExpectedIdVersions);
		theExpectedIdVersionsPlusNull.add(null);
		assertThat(theExpectedIdVersionsPlusNull, containsInAnyOrder(resultVersions.toArray()));
	}


	/**
	 * Validates that:
	 *   for CodeSystem:
	 * 	_ current CS has no version
	 * 	_ current TCS has no version
	 * for ValueSet:
	 * 	_ current TVSs with upload version have upload-version with no version append
	 * 	_ current TVSs with no upload version have null version
	 */
	private void runCommonValidations(String theVersion) {
	 	// for CodeSystem:

		// _ current CS is present and has no version
		CodeSystem codeSystem = myCodeSystemDao.read(new IdType("loinc"));
		String csString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem);
		ourLog.info("CodeSystem:\n" + csString);

		HashSet<String> shouldNotBePresentVersions = new HashSet<>(possibleVersions);
		shouldNotBePresentVersions.remove(theVersion);
		shouldNotBePresentVersions.stream().forEach(vv -> assertFalse(csString.contains(vv)));

		// same reading it from term service
		CodeSystem cs = myITermReadSvc.fetchCanonicalCodeSystemFromCompleteContext(BASE_LOINC_URL);
		assertEquals(BASE_LOINC_URL, cs.getUrl());
		assertNull(cs.getVersion());

		//	_ current TermCodeSystem has no version
		TermCodeSystemVersion termCSVersion = fetchCurrentCodeSystemVersion();
		assertNotNull(termCSVersion);
		assertNull(termCSVersion.getCodeSystemVersionId());

		//	for ValueSet:

		// for ValueSet resource
		ValueSet vs = (ValueSet) myJpaPersistedResourceValidationSupport.fetchValueSet(VS_NO_VERSIONED_ON_UPLOAD);
		assertEquals(VS_NO_VERSIONED_ON_UPLOAD, vs.getUrl());
		assertNull(vs.getVersion());

		// current TermVSs with no upload version have null version
		Optional<TermValueSet> noUploadCurrentVsOpt = myITermReadSvc.findCurrentTermValueSet(VS_NO_VERSIONED_ON_UPLOAD);
		assertTrue(noUploadCurrentVsOpt.isPresent());
		assertNull(noUploadCurrentVsOpt.get().getVersion());

		// current VSs with upload version have upload-version with no version append
		Optional<TermValueSet> uploadCurrentVsOpt = myITermReadSvc.findCurrentTermValueSet(VS_VERSIONED_ON_UPLOAD);
		assertTrue(uploadCurrentVsOpt.isPresent());
		assertEquals(VS_ANSWER_LIST_VERSION, uploadCurrentVsOpt.get().getVersion());

	}


	@Test()
	public void uploadCurrentNoVersion() throws Exception {
		IIdType csId = uploadLoincCodeSystem(null, true);

		runCommonValidations(null);

		//	validate operation for current (no version parameter)
		validateOperations(null, Collections.emptySet());
	}


	@Test()
	public void uploadCurrentWithVersion() throws Exception {
		String ver = "2.67";
		IIdType csId = uploadLoincCodeSystem(ver, true);

		runCommonValidations(ver);

		//	validate operation for specific version
		validateOperations(ver, Collections.singleton(ver));
	}


	@Test
	public void uploadCurrentNoVersionThenNoCurrent() throws Exception {
		uploadLoincCodeSystem(null, true);

		String ver = "2.67";
		uploadLoincCodeSystem(ver, false);

					myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runCommonValidations(ver);

		//	validate operation for specific version
		validateOperations(null, Collections.singleton(ver));
	}


	@Test
	public void uploadFirstCurrentWithVersionThenNoCurrent() throws Exception {
		String firstVer = "2.67";
		uploadLoincCodeSystem(firstVer, true);

		String secondVer = "2.68";
		uploadLoincCodeSystem(secondVer, false);

		runCommonValidations(firstVer);
		runCommonValidations(secondVer);

		//	validate operation for specific version
		validateOperations(null, Lists.newArrayList(firstVer, secondVer));
	}


	@Test
	public void uploadFirstCurrentNoVersionThenNoCurrentThenCurrent() throws Exception {
		uploadLoincCodeSystem(null, true);

		String firstVer = "2.67";
		uploadLoincCodeSystem(firstVer, false);

		String secondVer = "2.68";
		uploadLoincCodeSystem(secondVer, true);

		runCommonValidations(firstVer);
		runCommonValidations(secondVer);

		//	validate operation for specific version
		validateOperations(secondVer, Lists.newArrayList(firstVer, secondVer));
	}


	@Test
	public void uploadFirstCurrentWithVersionThenNoCurrentThenCurrent() throws Exception {
		String firstVer = "2.67";
		uploadLoincCodeSystem(firstVer, true);

		String secondVer = "2.68";
		uploadLoincCodeSystem(secondVer, false);

		String thirdVer = "2.68";
		uploadLoincCodeSystem(thirdVer, true);

		runCommonValidations(firstVer);
		runCommonValidations(secondVer);
		runCommonValidations(thirdVer);

		//	validate operation for specific version
		validateOperations(thirdVer, Lists.newArrayList(firstVer, secondVer, thirdVer));
	}




	private IIdType uploadLoincCodeSystem(String theVersion, boolean theMakeItCurrent) throws Exception {
		myFiles = new ZipCollectionBuilder();

		if (! theMakeItCurrent) {
			myRequestDetails.getUserData().put(LOINC_CODESYSTEM_MAKE_CURRENT, false);
			uploadProperties.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), "false");
		}

		assertTrue(
			theVersion == null || theVersion.equals("2.67") || theVersion.equals("2.68") || theVersion.equals("2.69"),
			"Version supported are: 2.67, 2.68, 2.69 and null" );

		if (StringUtils.isBlank(theVersion)) {
			uploadProperties.remove(LOINC_CODESYSTEM_VERSION.getCode());
		} else {
			uploadProperties.put(LOINC_CODESYSTEM_VERSION.getCode(), theVersion);
		}

		addLoincMandatoryFilesToZip(myFiles, theVersion);

		UploadStatistics stats = myTermLoaderSvc.loadLoinc(myFiles.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		return stats.getTarget();
	}


	public void addLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles, String theVersion) throws IOException {
		String theClassPathPrefix = getClassPathPrefix(theVersion);
		addBaseLoincMandatoryFilesToZip(theFiles, true, theClassPathPrefix);
		theFiles.addPropertiesZip(uploadProperties, LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
	}


	private String getClassPathPrefix(String theVersion) {
		String theClassPathPrefix = "/loinc-ver/v-no-version/";

		if (StringUtils.isBlank(theVersion))   return theClassPathPrefix;

		switch(theVersion) {
			case "2.67": return "/loinc-ver/v267/";
			case "2.68": return "/loinc-ver/v268/";
			case "2.69": return "/loinc-ver/v269/";
		};

		fail("Setup failed. Unexpected version: " + theVersion);
		return null;
	}


	private static void addBaseLoincMandatoryFilesToZip(
		ZipCollectionBuilder theFiles, Boolean theIncludeTop2000, String theClassPathPrefix) throws IOException {
		theFiles.addFileZip(theClassPathPrefix, LOINC_XML_FILE.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_GROUP_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_GROUP_TERMS_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PARENT_GROUP_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_HIERARCHY_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode());
		if (theIncludeTop2000) {
			theFiles.addFileZip(theClassPathPrefix, LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode());
			theFiles.addFileZip(theClassPathPrefix, LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode());
		}
	}

	private void logAllTermValueSets() {
		List<TermValueSet> vsList = myTermValueSetDao.findAll();

		vsList.forEach(vs -> {
			ourLog.info("ValueSet:\n" + vs);
		});

	}


	private TermCodeSystemVersion fetchCurrentCodeSystemVersion() {
		return (TermCodeSystemVersion) myEntityManager.createQuery(
			"select tcsv from TermCodeSystemVersion tcsv join fetch tcsv.myCodeSystem tcs " +
				"where tcs.myCurrentVersion = tcsv" ).getSingleResult();
	}

	private List<TermValueSet> fetchTermValueSets(String url) {
		return myEntityManager.createQuery("from TermValueSet where url = '" + url + "'").getResultList();
	}

	private List<ValueSet> fetchValueSets(Collection<Long> ids) {
//		ResourcePersistentId rscIds = myIdHelperService.resolveResourcePersistentIds(
//			RequestPartitionId.allPartitions(), "ValueSet", url);
//
		return myEntityManager.createQuery("from ResourceTable where myResourceType = 'ValueSet'").getResultList();
	}





//	@Test
//	public void testValidateCodeIsInPreExpandedValueSet() throws Exception {
//		myDaoConfig.setPreExpandValueSets(true);
//
//		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);
//
//		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
//		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));
//
//		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
//		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));
//
//		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
//
//		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, null);
//		assertNull(result);
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "BOGUS", null, null, null);
//		assertFalse(result.isOk());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "11378-7", null, null, null);
//		assertFalse(result.isOk());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", null, null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, coding, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		CodeableConcept codeableConcept = new CodeableConcept();
//		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
//		codeableConcept.addCoding(coding);
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, codeableConcept);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//	}



//	@Test
//	public void testCreateCodeSystemTwoVersions() {
//		CodeSystem codeSystem = new CodeSystem();
//		codeSystem.setUrl(CS_URL);
//		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
//		codeSystem
//			.addConcept().setCode("A").setDisplay("Code A");
//		codeSystem
//			.addConcept().setCode("B").setDisplay("Code A");
//
//		codeSystem.setVersion("1");
//
//		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
//
//		Set<TermConcept> codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "A");
//		assertThat(toCodes(codes), containsInAnyOrder("A"));
//
//		codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "B");
//		assertThat(toCodes(codes), containsInAnyOrder("B"));
//
//		runInTransaction(() -> {
//			List<TermCodeSystemVersion> termCodeSystemVersions = myTermCodeSystemVersionDao.findAll();
//			assertEquals(termCodeSystemVersions.size(), 1);
//			TermCodeSystemVersion termCodeSystemVersion_1 = termCodeSystemVersions.get(0);
//			assertEquals(termCodeSystemVersion_1.getConcepts().size(), 2);
//			Set<TermConcept> termConcepts = new HashSet<>(termCodeSystemVersion_1.getConcepts());
//			assertThat(toCodes(termConcepts), containsInAnyOrder("A", "B"));
//
//			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(id.getIdPartAsLong());
//			assertEquals("1", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
//
//		});
//
//		codeSystem.setVersion("2");
//		codeSystem
//			.addConcept().setCode("C").setDisplay("Code C");
//
//		IIdType id_v2 = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
//		codes = myTermSvc.findCodesBelow(id_v2.getIdPartAsLong(), id_v2.getVersionIdPartAsLong(), "C");
//		assertThat(toCodes(codes), containsInAnyOrder("C"));
//
//		runInTransaction(() -> {
//			List<TermCodeSystemVersion> termCodeSystemVersions_updated = myTermCodeSystemVersionDao.findAll();
//			assertEquals(termCodeSystemVersions_updated.size(), 2);
//			TermCodeSystemVersion termCodeSystemVersion_2 = termCodeSystemVersions_updated.get(1);
//			assertEquals(termCodeSystemVersion_2.getConcepts().size(), 3);
//			Set<TermConcept> termConcepts_updated = new HashSet<>(termCodeSystemVersion_2.getConcepts());
//			assertThat(toCodes(termConcepts_updated), containsInAnyOrder("A", "B", "C"));
//
//			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(id_v2.getIdPartAsLong());
//			assertEquals("2", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
//		});
//	}






}
