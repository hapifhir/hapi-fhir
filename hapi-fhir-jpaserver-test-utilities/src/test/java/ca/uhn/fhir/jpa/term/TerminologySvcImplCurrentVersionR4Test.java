package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
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
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
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

	private static final String BASE_LOINC_URL = "http://loinc.org";
	private static final String BASE_LOINC_VS_URL = BASE_LOINC_URL + "/vs/";

	private static final String LOINC_ALL_VS_URL = BASE_LOINC_URL + "/vs";

	// some ValueSets have a version specified independent of the CS version being uploaded. This one doesn't
	private static final String VS_NO_VERSIONED_ON_UPLOAD_ID = "loinc-rsna-radiology-playbook";
	private static final String VS_NO_VERSIONED_ON_UPLOAD = BASE_LOINC_VS_URL + VS_NO_VERSIONED_ON_UPLOAD_ID;
	private static final String VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE = "17787-3";
	private static final String VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY = "NM Thyroid gland Study report";

	// some ValueSets have a version specified independent of the CS version being uploaded. This is one of them
	private static final String VS_VERSIONED_ON_UPLOAD_ID = "LL1000-0";
	private static final String VS_VERSIONED_ON_UPLOAD = BASE_LOINC_VS_URL + VS_VERSIONED_ON_UPLOAD_ID;
	private static final String VS_VERSIONED_ON_UPLOAD_FIRST_CODE = "LA13825-7";
	private static final String VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY = "1 slice or 1 dinner roll";

	private static final String VS_ANSWER_LIST_VERSION = "Beta.1";
	private static final Set<String> possibleVersions = Sets.newHashSet("2.67", "2.68", "2.69");

	private static final int ALL_VS_QTY = 81;

	@Mock
	private HttpServletResponse mockServletResponse;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ServletRequestDetails mockRequestDetails;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private TermLoaderSvcImpl myTermLoaderSvc;

	@Autowired
	private ITermReadSvc myITermReadSvc;

	@Autowired @Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT)
	private IValidationSupport myJpaPersistedResourceValidationSupport;

	@Autowired private BatchJobHelper myBatchJobHelper;


	private ZipCollectionBuilder myFiles;
	private ServletRequestDetails myRequestDetails = new ServletRequestDetails();

	private Properties uploadProperties;
	private IFhirResourceDao<ValueSet> myValueSetIFhirResourceDao;


	@BeforeEach
	public void beforeEach() throws Exception {
		File file = ResourceUtils.getFile("classpath:loinc-ver/" + LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		uploadProperties = new Properties();
		uploadProperties.load(new FileInputStream(file));

		myValueSetIFhirResourceDao = myDaoRegistry.getResourceDao(ValueSet.class);

		when(mockRequestDetails.getServer().getDefaultPageSize()).thenReturn(25);
	}


	/**
	 * For input version or for current (when input is null) validates search, expand, lookup and validateCode operations
	 */
	private void validateOperations(String currentVersion, Collection<String> theExpectedVersions) {
		validateValueSetSearch(theExpectedVersions);

		validateValueExpand(currentVersion, theExpectedVersions);

		validateValueLookup(currentVersion, theExpectedVersions);

		validateValidateCode(currentVersion, theExpectedVersions);

		// nothing to test for subsumes operation as it works only for concepts which share CodeSystem and  version
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
			new UriType(BASE_LOINC_URL), new StringType(theVersion), new CodeType(VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE),
			null, null, null, null);
		assertNotNull(resultNoVersioned);
		assertEquals(prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), resultNoVersioned.getDisplay());

		IValidationSupport.CodeValidationResult resultVersioned = myCodeSystemDao.validateCode(null,
			new UriType(BASE_LOINC_URL), new StringType(theVersion), new CodeType(VS_VERSIONED_ON_UPLOAD_FIRST_CODE),
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
		return (version == null ? "" : "v" + version + " ") + suffix;

	}

	private void validateValueExpand(String currentVersion, Collection<String> theAllVersions) {
		// for CS ver = null, VS ver = null
		ValueSet vs = myValueSetDao.expandByIdentifier(VS_NO_VERSIONED_ON_UPLOAD, null);
		assertEquals(1, vs.getExpansion().getContains().size());

		// version was added prefixing code display to validate
		assertEquals(prefixWithVersion(currentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs.getExpansion().getContains().iterator().next().getDisplay());


		// for CS ver = null, VS ver != null
		ValueSet vs1 = myValueSetDao.expandByIdentifier(
			VS_VERSIONED_ON_UPLOAD + "|" + VS_ANSWER_LIST_VERSION, null);
		assertEquals(3, vs1.getExpansion().getContains().size());

		assertEquals(prefixWithVersion(currentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs1.getExpansion().getContains().iterator().next().getDisplay());


		validateExpandedTermConcepts(currentVersion, theAllVersions);

		// now for each uploaded version
		theAllVersions.forEach(this::validateValueExpandForVersion);
	}


	private void validateExpandedTermConcepts(String theCurrentVersion, Collection<String> theAllVersions) {
		runInTransaction(() -> {
		TermConcept termConceptNoVerCsvNoVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId is null").getSingleResult();
		assertNotNull(termConceptNoVerCsvNoVer);
		// data should have version because it was loaded with a version
		assertEquals(prefixWithVersion(theCurrentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptNoVerCsvNoVer.getDisplay());

		TermConcept termConceptVerCsvNoVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId is null").getSingleResult();
		assertNotNull(termConceptVerCsvNoVer);
		// data should have version because it was loaded with a version
		assertEquals(prefixWithVersion(theCurrentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptVerCsvNoVer.getDisplay());

		if (theCurrentVersion != null) {
			TermConcept termConceptNoVerCsvVer = (TermConcept) myEntityManager.createQuery(
				"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
					VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId = '" + theCurrentVersion + "'").getSingleResult();
			assertNotNull(termConceptNoVerCsvVer);
			// data should have version because it was loaded with a version
			assertEquals(prefixWithVersion(theCurrentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptNoVerCsvVer.getDisplay());

			TermConcept termConceptVerCsvVer = (TermConcept) myEntityManager.createQuery(
				"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
					VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId = '" + theCurrentVersion + "'").getSingleResult();
			assertNotNull(termConceptVerCsvVer);
			// data should have version because it was loaded with a version
			assertEquals(prefixWithVersion(theCurrentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptVerCsvVer.getDisplay());
		}

		theAllVersions.forEach(this::validateExpandedTermConceptsForVersion);

		});
	}


	private void validateExpandedTermConceptsForVersion(String theVersion) {
		TermConcept termConceptNoVer = (TermConcept) myEntityManager.createQuery(
			"select tc from TermConcept tc join fetch tc.myCodeSystem tcsv where tc.myCode = '" +
				VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' and tcsv.myCodeSystemVersionId = '" + theVersion + "'").getSingleResult();
		assertNotNull(termConceptNoVer);
		assertEquals(prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), termConceptNoVer.getDisplay());

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
		assertEquals(prefixWithVersion(theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
			vs3.getExpansion().getContains().iterator().next().getDisplay());
	}


	private void validateValueSetSearch(Collection<String> theExpectedIdVersions) {
		// first validate search for CS ver = null VS ver = null

		SearchParameterMap paramsNoUploadVer = new SearchParameterMap("url", new UriParam(VS_NO_VERSIONED_ON_UPLOAD));
		int expectedResultQty = theExpectedIdVersions.size() + 1;  // + 1 because an extra null version (the current) is always present
		IBundleProvider noUploadVerResult = myValueSetIFhirResourceDao.search(paramsNoUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> noUploadVerValueSets = noUploadVerResult.getAllResources();
		assertEquals(expectedResultQty, noUploadVerValueSets.size());

		matchUnqualifiedIds(noUploadVerValueSets, theExpectedIdVersions);

		// now  validate search for CS ver = null VS ver != null

		SearchParameterMap paramsUploadVer = new SearchParameterMap("url", new UriParam(VS_VERSIONED_ON_UPLOAD));
		paramsUploadVer.add("version", new TokenParam(VS_ANSWER_LIST_VERSION));
		IBundleProvider uploadVerResult = myValueSetIFhirResourceDao.search(paramsUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadVerValueSets = uploadVerResult.getAllResources();
		assertEquals(1, uploadVerValueSets.size());

		assertEquals(VS_VERSIONED_ON_UPLOAD_ID, uploadVerValueSets.get(0).getIdElement().getIdPart());
		assertEquals(VS_ANSWER_LIST_VERSION, ((ValueSet) uploadVerValueSets.get(0)).getVersion());

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

		IBundleProvider uploadNoVerResult = myValueSetIFhirResourceDao.search(paramsUploadNoVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadNoVerValueSets = uploadNoVerResult.getAllResources();
		assertEquals(1, uploadNoVerValueSets.size());

		ValueSet loadNoVersionValueSet = (ValueSet) uploadNoVerValueSets.get(0);
		String expectedLoadNoVersionUnqualifiedId = VS_NO_VERSIONED_ON_UPLOAD_ID + (theVersion == null ? "" : "-" + theVersion);
		assertEquals(expectedLoadNoVersionUnqualifiedId, loadNoVersionValueSet.getIdElement().getIdPart());


		// versioned VS (VS version, different than  CS version)

		SearchParameterMap paramsUploadVer = new SearchParameterMap("url", new UriParam(VS_VERSIONED_ON_UPLOAD));
		paramsUploadVer.add("version", new TokenParam(VS_ANSWER_LIST_VERSION + "-" + theVersion));

		IBundleProvider uploadVerResult = myValueSetIFhirResourceDao.search(paramsUploadVer, mockRequestDetails, mockServletResponse);
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
	 * @param theValueSets          the ValueSet collection
	 * @param theExpectedIdVersions the collection of expected versions
	 */
	private void matchUnqualifiedIds(List<IBaseResource> theValueSets, Collection<String> theExpectedIdVersions) {
		// set should contain one entry per expectedVersion
		List<String> expectedNoVersionUnqualifiedIds = theExpectedIdVersions.stream()
			.map(expVer -> VS_NO_VERSIONED_ON_UPLOAD_ID + "-" + expVer)
			.collect(Collectors.toList());

		// plus one entry for null version
		expectedNoVersionUnqualifiedIds.add(VS_NO_VERSIONED_ON_UPLOAD_ID);

		List<String> resultUnqualifiedIds = theValueSets.stream()
			.map(r -> r.getIdElement().getIdPart())
			.collect(Collectors.toList());

		assertThat(resultUnqualifiedIds, containsInAnyOrder(resultUnqualifiedIds.toArray()));

		Set<String> theExpectedIdVersionsPlusNull = Sets.newHashSet(theExpectedIdVersions);
		theExpectedIdVersionsPlusNull.add(null);
		assertThat(theExpectedIdVersionsPlusNull, containsInAnyOrder(
			theValueSets.stream().map(r -> ((ValueSet) r).getVersion()).toArray()));

	}


	/**
	 * Validates that:
	 * for CodeSystem:
	 * _ current CS has no version
	 * _ current TCS has no version
	 * for ValueSet:
	 * _ current TVSs with upload version have upload-version with no version append
	 * _ current TVSs with no upload version have null version
	 */
	private void runCommonValidations(List<String> theAllVersions) {
		// for CodeSystem:

		// _ current CS is present and has no version
		CodeSystem codeSystem = myCodeSystemDao.read(new IdType(LOINC_LOW));
		String csString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem);
		ourLog.info("CodeSystem:\n" + csString);

		HashSet<String> shouldNotBePresentVersions = new HashSet<>(possibleVersions);
		theAllVersions.forEach(shouldNotBePresentVersions::remove);
		shouldNotBePresentVersions.forEach(vv -> assertFalse(csString.contains(vv),
			"Found version string: '" + vv + "' in CodeSystem: " + csString));

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
		assertNotNull(vs);
		assertEquals(VS_NO_VERSIONED_ON_UPLOAD, vs.getUrl());
		assertNull(vs.getVersion());

		// current TermVSs with no upload version have null version
		Optional<TermValueSet> noUploadCurrentVsOpt = getCurrentTermValueSet(VS_NO_VERSIONED_ON_UPLOAD);
		assertTrue(noUploadCurrentVsOpt.isPresent());
		assertNull(noUploadCurrentVsOpt.get().getVersion());

		// current VSs with upload version have upload-version with no version append
		Optional<TermValueSet> uploadCurrentVsOpt = getCurrentTermValueSet(VS_VERSIONED_ON_UPLOAD);
		assertTrue(uploadCurrentVsOpt.isPresent());
		assertEquals(VS_ANSWER_LIST_VERSION, uploadCurrentVsOpt.get().getVersion());

	}

	private Optional<TermValueSet> getCurrentTermValueSet(String theTheVsNoVersionedOnUpload) {
		return runInTransaction(() -> myITermReadSvc.findCurrentTermValueSet(theTheVsNoVersionedOnUpload));
	}


	@Test()
	public void uploadCurrentNoVersion() throws Exception {
		IIdType csId = uploadLoincCodeSystem(null, true);

		runCommonValidations(Collections.emptyList());

		//	validate operation for current (no version parameter)
		validateOperations(null, Collections.emptySet());

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList((String) null));
	}


	@Test()
	public void uploadWithVersion() throws Exception {
		String ver = "2.67";
		IIdType csId = uploadLoincCodeSystem(ver, true);

		runCommonValidations(Collections.singletonList(ver));

		//	validate operation for specific version
		validateOperations(ver, Collections.singleton(ver));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(ver, ver));
	}


	@Test
	public void uploadNoVersionThenNoCurrent() throws Exception {
		uploadLoincCodeSystem(null, true);

		String ver = "2.67";
		uploadLoincCodeSystem(ver, false);

//					myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		runCommonValidations(Collections.singletonList(ver));

		//	validate operation for specific version
		validateOperations(null, Collections.singleton(ver));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(null, ver));
	}


	@Test
	public void uploadWithVersionThenNoCurrent() throws Exception {
		String currentVer = "2.67";
		uploadLoincCodeSystem(currentVer, true);

		String nonCurrentVer = "2.68";
		uploadLoincCodeSystem(nonCurrentVer, false);

			//		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runCommonValidations(Lists.newArrayList(currentVer, nonCurrentVer));

		//	validate operation for specific version
		validateOperations(currentVer, Lists.newArrayList(currentVer, nonCurrentVer));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(currentVer, currentVer, nonCurrentVer));


		// this test also checks specifically the loinc-all ValueSet
		validateOperationsLoincAllVS(currentVer, Lists.newArrayList(currentVer, nonCurrentVer));
	}


	/**
	 * For input version or for current (when input is null) validates search, expand, lookup and validateCode operations
	 */
	private void validateOperationsLoincAllVS(String currentVersion, Collection<String> theExpectedVersions) {
		validateValueSetSearchLoincAllVS(theExpectedVersions);

		validateValueExpandLoincAllVS(currentVersion, theExpectedVersions);
	}


	private void validateValueSetSearchLoincAllVS(Collection<String> theExpectedIdVersions) {
		// first validate search for CS ver = null VS ver = null

		SearchParameterMap params = new SearchParameterMap("url", new UriParam(LOINC_ALL_VS_URL));
		int expectedResultQty = theExpectedIdVersions.size() + 1;  // + 1 because an extra null version (the current) is always present
		IBundleProvider result = myValueSetIFhirResourceDao.search(params, mockRequestDetails, mockServletResponse);
		List<IBaseResource> valueSets = result.getAllResources();
		assertEquals(expectedResultQty, valueSets.size());

		matchUnqualifiedIds(valueSets, theExpectedIdVersions);

		// now validate each specific uploaded version
		theExpectedIdVersions.forEach(this::validateValueSetSearchForVersionLoincAllVS);
	}


	private void validateValueSetSearchForVersionLoincAllVS(String theVersion) {
		SearchParameterMap paramsUploadNoVer = new SearchParameterMap("url", new UriParam(LOINC_ALL_VS_URL));
		paramsUploadNoVer.add("version", new TokenParam(theVersion));

		IBundleProvider result = myValueSetIFhirResourceDao.search(paramsUploadNoVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> valueSets = result.getAllResources();
		assertEquals(1, valueSets.size());

		ValueSet loadNoVersionValueSet = (ValueSet) valueSets.get(0);
		String expectedUnqualifiedId = LOINC_ALL_VALUESET_ID + "-" + theVersion;
		assertEquals(expectedUnqualifiedId, loadNoVersionValueSet.getIdElement().getIdPart());
	}




	private void validateValueExpandLoincAllVS(String currentVersion, Collection<String> theAllVersions) {
		ValueSet vs = myValueSetDao.expandByIdentifier(LOINC_ALL_VS_URL, null);
		assertFalse(vs.getExpansion().getContains().isEmpty());

		// version was added prefixing to some code display to validate
		checkContainsElementVersion(vs, currentVersion);

		// now for each uploaded version
		theAllVersions.forEach(this::validateValueExpandLoincAllVsForVersion);
	}

	private void checkContainsElementVersion(ValueSet vs, String version) {
		Optional<String> vsContainsDisplay = vs.getExpansion().getContains().stream()
			.filter(c -> c.getCode().equals(VS_VERSIONED_ON_UPLOAD_FIRST_CODE))
			.map(ValueSet.ValueSetExpansionContainsComponent::getDisplay)
			.findAny();
		assertTrue(vsContainsDisplay.isPresent());
		String expectedDisplay = prefixWithVersion(version, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedDisplay, vsContainsDisplay.get());
	}


	private void validateValidateCodeLoincAllVS(String theCurrentVersion, Collection<String> allVersions) {
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



	private void validateValueExpandLoincAllVsForVersion(String theVersion) {
		ValueSet vs = myValueSetDao.expandByIdentifier(LOINC_ALL_VS_URL + "|" + theVersion, null);
		assertEquals(ALL_VS_QTY, vs.getExpansion().getContains().size());

		// version was added before code display to validate
		checkContainsElementVersion(vs, theVersion);
	}


	/**
	 * Validates TermConcepts were created in the sequence indicated by the parameters
	 * and their displays match the expected versions
	 */
	private void validateTermConcepts(ArrayList<String> theExpectedVersions) {
		runInTransaction(() -> {
		@SuppressWarnings("unchecked")
		List<TermConcept> termConceptNoVerList = (List<TermConcept>) myEntityManager.createQuery(
			"from TermConcept where myCode = '" + VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' order by myId").getResultList();
		assertEquals(theExpectedVersions.size(), termConceptNoVerList.size());
		for (int i = 0; i < theExpectedVersions.size(); i++) {
			assertEquals( prefixWithVersion(theExpectedVersions.get(i), VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
				termConceptNoVerList.get(i).getDisplay(), "TermCode with id: " + i + " display");
		}

		@SuppressWarnings("unchecked")
		List<TermConcept> termConceptWithVerList = (List<TermConcept>) myEntityManager.createQuery(
			"from TermConcept where myCode = '" + VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' order by myId").getResultList();
		assertEquals(theExpectedVersions.size(), termConceptWithVerList.size());
		for (int i = 0; i < theExpectedVersions.size(); i++) {
			assertEquals( prefixWithVersion(theExpectedVersions.get(i), VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
				termConceptWithVerList.get(i).getDisplay(), "TermCode with id: " + i + " display");
		}
		});
	}


	@Test
	public void uploadNoVersionThenNoCurrentThenCurrent() throws Exception {
		uploadLoincCodeSystem(null, true);

		String nonCurrentVer = "2.67";
		uploadLoincCodeSystem(nonCurrentVer, false);

		String currentVer = "2.68";
		uploadLoincCodeSystem(currentVer, true);
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		runCommonValidations(Lists.newArrayList(nonCurrentVer, currentVer));

		//	validate operation for specific version
		validateOperations(currentVer, Lists.newArrayList(nonCurrentVer, currentVer));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(nonCurrentVer, currentVer, currentVer));
	}


	@Test
	public void uploadWithVersionThenNoCurrentThenCurrent() throws Exception {
		String firstCurrentVer = "2.67";
		uploadLoincCodeSystem(firstCurrentVer, true);

		String noCurrentVer = "2.68";
		uploadLoincCodeSystem(noCurrentVer, false);

		String lastCurrentVer = "2.69";
		uploadLoincCodeSystem(lastCurrentVer, true);
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		runCommonValidations(Lists.newArrayList(firstCurrentVer, noCurrentVer, lastCurrentVer));

		//	validate operation for specific version
		validateOperations(lastCurrentVer, Lists.newArrayList(firstCurrentVer, noCurrentVer, lastCurrentVer));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(firstCurrentVer, noCurrentVer, lastCurrentVer, lastCurrentVer));
	}


	private IIdType uploadLoincCodeSystem(String theVersion, boolean theMakeItCurrent) throws Exception {
		myFiles = new ZipCollectionBuilder();

		myRequestDetails.getUserData().put(LOINC_CODESYSTEM_MAKE_CURRENT, theMakeItCurrent);
		uploadProperties.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), Boolean.toString(theMakeItCurrent));

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
			case "2.67":
				return "/loinc-ver/v267/";
			case "2.68":
				return "/loinc-ver/v268/";
			case "2.69":
				return "/loinc-ver/v269/";
		}

		fail("Setup failed. Unexpected version: " + theVersion);
		return null;
	}

	private TermCodeSystemVersion fetchCurrentCodeSystemVersion() {
		runInTransaction(() -> {
			List<TermCodeSystem> tcsList = myEntityManager.createQuery("from TermCodeSystem").getResultList();
			List<TermCodeSystemVersion> tcsvList = myEntityManager.createQuery("from TermCodeSystemVersion").getResultList();
			ourLog.error("tcslist: {}", tcsList.stream().map(tcs -> tcs.toString()).collect(joining("\n", "\n", "")));
			ourLog.error("tcsvlist: {}", tcsvList.stream().map(v -> v.toString()).collect(joining("\n", "\n", "")));

			if (tcsList.size() != 1) {
				throw new IllegalStateException("More than one TCS: " +
					tcsList.stream().map(tcs -> String.valueOf(tcs.getPid())).collect(joining()));
			}
			if (tcsList.get(0).getCurrentVersion() == null) {
				throw new IllegalStateException("Current version is null in TCS: " + tcsList.get(0).getPid());
			}
		});

		return runInTransaction(() -> (TermCodeSystemVersion) myEntityManager.createQuery(
				"select tcsv from TermCodeSystemVersion tcsv join fetch tcsv.myCodeSystem tcs " +
					"where tcs.myCurrentVersion = tcsv").getSingleResult());
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


}
