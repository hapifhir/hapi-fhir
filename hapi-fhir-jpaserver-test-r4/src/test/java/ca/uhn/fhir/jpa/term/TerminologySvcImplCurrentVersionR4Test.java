package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

	private static final Set<String> possibleVersions = Sets.newHashSet("2.67", "2.68", "2.69");

	private static final int ALL_VS_QTY = 81;

	@Mock
	private HttpServletResponse mockServletResponse;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ServletRequestDetails mockRequestDetails;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@Autowired
	private ITermReadSvc myITermReadSvc;

	@Autowired @Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT)
	private IValidationSupport myJpaPersistedResourceValidationSupport;

	@Autowired
	private Batch2JobHelper myBatchJobHelper;

	private final ServletRequestDetails myRequestDetails = new ServletRequestDetails();

	private IFhirResourceDao<ValueSet> myValueSetIFhirResourceDao;


	@BeforeEach
	public void beforeEach() throws Exception {
		myValueSetIFhirResourceDao = myDaoRegistry.getResourceDao(ValueSet.class);

		when(mockRequestDetails.getServer().getDefaultPageSize()).thenReturn(25);
	}

	@AfterEach
	public void afterEach() {
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
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
		validateValidateCodeForVersion(theCurrentVersion);

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
			new ValidationSupportContext(myValidationSupport),
			new LookupCodeRequest(BASE_LOINC_URL, VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE));
		assertNotNull(resultNoVer);
		String expectedNoVer = prefixWithVersion(theCurrentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedNoVer, resultNoVer.getCodeDisplay());

		IValidationSupport.LookupCodeResult resultWithVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport),
			new LookupCodeRequest(BASE_LOINC_URL, VS_VERSIONED_ON_UPLOAD_FIRST_CODE));
		assertNotNull(resultWithVer);
		String expectedWithVer = prefixWithVersion(theCurrentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedWithVer, resultWithVer.getCodeDisplay());

		allVersions.forEach(this::lookupForVersion);
	}


	private void lookupForVersion(String theVersion) {
		IValidationSupport.LookupCodeResult resultNoVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport),
            new LookupCodeRequest(BASE_LOINC_URL + "|" + theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE));
		assertNotNull(resultNoVer);
		String expectedNoVer = prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertEquals(expectedNoVer, resultNoVer.getCodeDisplay());

		IValidationSupport.LookupCodeResult resultWithVer = myValidationSupport.lookupCode(
			new ValidationSupportContext(myValidationSupport),
            new LookupCodeRequest(BASE_LOINC_URL + "|" + theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_CODE));
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
		assertThat(vs.getExpansion().getContains()).hasSize(1);

		// version was added prefixing code display to validate
		assertEquals(prefixWithVersion(currentVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), vs.getExpansion().getContains().iterator().next().getDisplay());


		// for CS ver = null, VS ver != null
		ValueSet vs1 = myValueSetDao.expandByIdentifier(
			VS_VERSIONED_ON_UPLOAD + "|" + currentVersion, null);
		assertThat(vs1.getExpansion().getContains()).hasSize(3);

		assertEquals(prefixWithVersion(currentVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), vs1.getExpansion().getContains().iterator().next().getDisplay());


		validateExpandedTermConceptsForVersion(currentVersion);
		for (String version : theAllVersions) {
			validateExpandedTermConceptsForVersion(version);
		}

		// now for each uploaded version
		theAllVersions.forEach(this::validateValueExpandForVersion);
	}

	private void validateExpandedTermConceptsForVersion(String theVersion) {
		runInTransaction(()->{
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
		});
	}


	private void validateValueExpandForVersion(String theVersion) {
		// for CS ver != null, VS ver = null

		ValueSet vs2 = myValueSetDao.expandByIdentifier(
			VS_NO_VERSIONED_ON_UPLOAD + "|" + theVersion, null);
		assertThat(vs2.getExpansion().getContains()).hasSize(1);

		// version was added before code display to validate
		assertEquals(prefixWithVersion(theVersion, VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), vs2.getExpansion().getContains().iterator().next().getDisplay());


		// for CS ver != null, VS ver != null

		ValueSet vs3 = myValueSetDao.expandByIdentifier(
			VS_VERSIONED_ON_UPLOAD + "|" + theVersion, null);
		assertThat(vs3.getExpansion().getContains()).hasSize(3);

		// version was added before code display to validate
		assertEquals(prefixWithVersion(theVersion, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY), vs3.getExpansion().getContains().iterator().next().getDisplay());
	}


	private void validateValueSetSearch(Collection<String> theExpectedIdVersions) {
		// first validate search for CS ver = null VS ver = null

		SearchParameterMap paramsNoUploadVer = new SearchParameterMap("url", new UriParam(VS_NO_VERSIONED_ON_UPLOAD));
		int expectedResultQty = theExpectedIdVersions.size();
		IBundleProvider noUploadVerResult = myValueSetIFhirResourceDao.search(paramsNoUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> noUploadVerValueSets = noUploadVerResult.getAllResources();
		assertThat(noUploadVerValueSets).hasSize(expectedResultQty);

		matchUnqualifiedIds(noUploadVerValueSets, theExpectedIdVersions);

		// now  validate search for CS ver = null VS ver != null
		for (String version : theExpectedIdVersions) {
			SearchParameterMap paramsUploadVer = new SearchParameterMap("url", new UriParam(VS_VERSIONED_ON_UPLOAD));
			paramsUploadVer.add("version", new TokenParam(version));

			IBundleProvider uploadVerResult = myValueSetIFhirResourceDao.search(paramsUploadVer, mockRequestDetails, mockServletResponse);
			List<IBaseResource> uploadVerValueSets = uploadVerResult.getAllResources();
			assertThat(uploadVerValueSets).hasSize(1);

			assertEquals(VS_VERSIONED_ON_UPLOAD_ID + "-" + version, uploadVerValueSets.get(0).getIdElement().getIdPart());
			assertEquals(version, ((ValueSet) uploadVerValueSets.get(0)).getVersion());
		}

		// now validate each specific uploaded version
		theExpectedIdVersions.forEach(this::validateValueSetSearchForVersion);
	}


	/**
	 * Some ValueSets (IE: AnswerLists), can have a specific version, different than the version of the
	 * CodeSystem with which they were uploaded. That version is what we distinguish in both sets of tests here,
	 * no the CodeSystem version.
	 */
	private void validateValueSetSearchForVersion(String theVersion) {
		// for no versioned VS (VS version, different from CS version)

		SearchParameterMap paramsUploadNoVer = new SearchParameterMap("url", new UriParam(VS_NO_VERSIONED_ON_UPLOAD));
		paramsUploadNoVer.add("version", new TokenParam(theVersion));

		IBundleProvider uploadNoVerResult = myValueSetIFhirResourceDao.search(paramsUploadNoVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadNoVerValueSets = uploadNoVerResult.getAllResources();
		assertThat(uploadNoVerValueSets).hasSize(1);

		ValueSet loadNoVersionValueSet = (ValueSet) uploadNoVerValueSets.get(0);
		String expectedLoadNoVersionUnqualifiedId = VS_NO_VERSIONED_ON_UPLOAD_ID + (theVersion == null ? "" : "-" + theVersion);
		assertEquals(expectedLoadNoVersionUnqualifiedId, loadNoVersionValueSet.getIdElement().getIdPart());


		// versioned VS (VS version, different from CS version)

		SearchParameterMap paramsUploadVer = new SearchParameterMap("url", new UriParam(VS_VERSIONED_ON_UPLOAD));
		paramsUploadVer.add("version", new TokenParam(theVersion));

		IBundleProvider uploadVerResult = myValueSetIFhirResourceDao.search(paramsUploadVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> uploadVerValueSets = uploadVerResult.getAllResources();
		assertThat(uploadVerValueSets).hasSize(1);

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

		assertThat(resultUnqualifiedIds).containsExactlyInAnyOrderElementsOf(resultUnqualifiedIds);

		List<String> valueSetVersions = theValueSets.stream().map(r -> ((ValueSet) r).getVersion()).toList();
		assertThat(valueSetVersions).containsExactlyInAnyOrderElementsOf(theExpectedIdVersions);
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

		// Validate that CS was created
		for (String version : theAllVersions) {
			CodeSystem codeSystem = myCodeSystemDao.read(new IdType(LOINC_LOW + "-" + version), myRequestDetails);
			assertEquals(version, codeSystem.getVersion());

			// Fetch from validation context
			codeSystem = (CodeSystem) myValidationSupport.fetchCodeSystem(BASE_LOINC_URL + "|" + version);
			assertEquals(version, codeSystem.getVersion());

			// for ValueSet resource
			ValueSet vs = (ValueSet) myValidationSupport.fetchValueSet(VS_NO_VERSIONED_ON_UPLOAD + "|" + version);
			assertNotNull(vs);
			assertEquals(VS_NO_VERSIONED_ON_UPLOAD, vs.getUrl());
			assertEquals(version, vs.getVersion());
		}

	}

	@Test()
	public void uploadWithVersion() throws Exception {
		String ver = "2.67";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(ver, true);

		runCommonValidations(Collections.singletonList(ver));

		//	validate operation for specific version
		validateOperations(ver, Collections.singleton(ver));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(ver));
	}


	@Test
	public void uploadWithVersionThenNoCurrent() throws Exception {
		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		String currentVer = "2.67";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(currentVer, true);

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		String nonCurrentVer = "2.68";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(nonCurrentVer, false);

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		logAllUriIndexes();
		logAllTokenIndexes("version");
		logAllConcepts();

		runCommonValidations(Lists.newArrayList(currentVer, nonCurrentVer));


		//	validate operation for specific version
		validateOperations(currentVer, Lists.newArrayList(currentVer, nonCurrentVer));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(currentVer, nonCurrentVer));


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
		int expectedResultQty = theExpectedIdVersions.size();
		IBundleProvider result = myValueSetIFhirResourceDao.search(params, mockRequestDetails, mockServletResponse);
		List<IBaseResource> valueSets = result.getAllResources();
		assertThat(valueSets).hasSize(expectedResultQty);

		matchUnqualifiedIds(valueSets, theExpectedIdVersions);

		// now validate each specific uploaded version
		theExpectedIdVersions.forEach(this::validateValueSetSearchForVersionLoincAllVS);
	}


	private void validateValueSetSearchForVersionLoincAllVS(String theVersion) {
		SearchParameterMap paramsUploadNoVer = new SearchParameterMap("url", new UriParam(LOINC_ALL_VS_URL));
		paramsUploadNoVer.add("version", new TokenParam(theVersion));

		IBundleProvider result = myValueSetIFhirResourceDao.search(paramsUploadNoVer, mockRequestDetails, mockServletResponse);
		List<IBaseResource> valueSets = result.getAllResources();
		assertThat(valueSets).hasSize(1);

		ValueSet loadNoVersionValueSet = (ValueSet) valueSets.get(0);
		String expectedUnqualifiedId = LOINC_ALL_VALUESET_ID + "-" + theVersion;
		assertEquals(expectedUnqualifiedId, loadNoVersionValueSet.getIdElement().getIdPart());
	}




	private void validateValueExpandLoincAllVS(String currentVersion, Collection<String> theAllVersions) {
		ValueSet vs = myValueSetDao.expandByIdentifier(LOINC_ALL_VS_URL, null);
		assertThat(vs.getExpansion().getContains()).isNotEmpty();

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
		assertThat(vsContainsDisplay).isPresent();
		String expectedDisplay = prefixWithVersion(version, VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY);
		assertThat(vsContainsDisplay).contains(expectedDisplay);
	}

	private void validateValueExpandLoincAllVsForVersion(String theVersion) {
		ValueSet vs = myValueSetDao.expandByIdentifier(LOINC_ALL_VS_URL + "|" + theVersion, null);
		switch (theVersion) {
			case "2.67", "2.68" ->
				assertThat(vs.getExpansion().getContains()).hasSize(82);
			default -> fail("Don't know how to handle version: " + theVersion);
		}

		// version was added before code display to validate
		checkContainsElementVersion(vs, theVersion);
	}

	/**
	 * Validates TermConcepts were created in the sequence indicated by the parameters,
	 * and their displays match the expected versions
	 */
	private void validateTermConcepts(ArrayList<String> theExpectedVersions) {
		runInTransaction(() -> {
		@SuppressWarnings("unchecked")
			List<TermConcept> termConceptNoVerList = (List<TermConcept>) myEntityManager.createQuery(
				"from TermConcept where myCode = '" + VS_NO_VERSIONED_ON_UPLOAD_FIRST_CODE + "' order by myId.myId").getResultList();
			assertEquals(theExpectedVersions.size(), termConceptNoVerList.size());
			for (int i = 0; i < theExpectedVersions.size(); i++) {
				assertEquals( prefixWithVersion(theExpectedVersions.get(i), VS_NO_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
					termConceptNoVerList.get(i).getDisplay(), "TermCode with id: " + i + " display");
			}

			@SuppressWarnings("unchecked")
			List<TermConcept> termConceptWithVerList = (List<TermConcept>) myEntityManager.createQuery(
				"from TermConcept where myCode = '" + VS_VERSIONED_ON_UPLOAD_FIRST_CODE + "' order by myId.myId").getResultList();
			assertEquals(theExpectedVersions.size(), termConceptWithVerList.size());
			for (int i = 0; i < theExpectedVersions.size(); i++) {
				assertEquals( prefixWithVersion(theExpectedVersions.get(i), VS_VERSIONED_ON_UPLOAD_FIRST_DISPLAY),
					termConceptWithVerList.get(i).getDisplay(), "TermCode with id: " + i + " display");
			}
		});
	}


	@Test
	public void uploadWithVersionThenNoCurrentThenCurrent() throws Exception {
		String firstCurrentVer = "2.67";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(firstCurrentVer, true);

		String noCurrentVer = "2.68";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(noCurrentVer, false);

		String lastCurrentVer = "2.69";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(lastCurrentVer, true);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		runCommonValidations(Lists.newArrayList(firstCurrentVer, noCurrentVer, lastCurrentVer));

		//	validate operation for specific version
		validateOperations(lastCurrentVer, Lists.newArrayList(firstCurrentVer, noCurrentVer, lastCurrentVer));

		//	tests conditions which were failing after VS expansion (before fix for issue-2995)
		validateTermConcepts(Lists.newArrayList(firstCurrentVer, noCurrentVer, lastCurrentVer));
	}



	private TermCodeSystemVersion fetchCurrentCodeSystemVersion() {
		runInTransaction(() -> {
			@SuppressWarnings("unchecked")
			List<TermCodeSystem> tcsList = myEntityManager.createQuery("from TermCodeSystem").getResultList();
			@SuppressWarnings("unchecked")
			List<TermCodeSystemVersion> tcsvList = myEntityManager.createQuery("from TermCodeSystemVersion").getResultList();
			ourLog.error("tcslist: {}", tcsList.stream().map(TermCodeSystem::toString).collect(joining("\n", "\n", "")));
			ourLog.error("tcsvlist: {}", tcsvList.stream().map(TermCodeSystemVersion::toString).collect(joining("\n", "\n", "")));

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




}
