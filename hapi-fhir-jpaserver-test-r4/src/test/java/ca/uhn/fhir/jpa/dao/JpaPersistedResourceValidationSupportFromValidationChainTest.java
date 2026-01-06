package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static ca.uhn.fhir.util.ClasspathUtil.loadResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaPersistedResourceValidationSupportFromValidationChainTest {
	private static final FhirContext ourCtx = FhirContext.forR4();


	private JpaPersistedResourceValidationSupport jpaValidator;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao<StructureDefinition> fhirResourceDaoStructureDefinition;

	@Mock
	private IFhirResourceDao<Library> fhirResourceDaoLibrary;

	@Mock
	private IFhirResourceDao<Measure> fhirResourceDaoMeasure;

	@Mock
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;

	@Mock
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;

	@Mock
	private IBundleProvider search;

	@Captor
	private ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@BeforeEach
	public void setUp() {
		jpaValidator = new JpaPersistedResourceValidationSupport(ourCtx, myDaoRegistry);
		jpaValidator.start();
	}

	@Test
	public void validation_Jpa_Bundle_MeasureReferencesLibraryAndLibrary() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(myDaoRegistry.getResourceDao("Library")).thenReturn(fhirResourceDaoLibrary);

		when(fhirResourceDaoStructureDefinition.search(any(SearchParameterMap.class), any())).thenReturn(search);
		when(fhirResourceDaoLibrary.search(any(SearchParameterMap.class), any())).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithBadLibrary = getBundle("/r4/3124-bundle-measure-and-library-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithBadLibrary);

		assertEquals(10, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_MeasureOnly() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(myDaoRegistry.getResourceDao("Library")).thenReturn(fhirResourceDaoLibrary);

		when(fhirResourceDaoStructureDefinition.search(any(SearchParameterMap.class), any())).thenReturn(search);
		when(fhirResourceDaoLibrary.search(any(SearchParameterMap.class), any())).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureOnly = getBundle("/r4/3124-bundle-measure-only-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureOnly);

		assertEquals(8, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_MeasureOnly_NoLibraryReference() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(fhirResourceDaoStructureDefinition.search(any(SearchParameterMap.class), any())).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureOnlyNoLibraryReference = getBundle("/r4/3124-bundle-measure-only-no-library-reference-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureOnlyNoLibraryReference);

		assertEquals(7, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_LibraryOnly() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);

		when(fhirResourceDaoStructureDefinition.search(any(SearchParameterMap.class), any())).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithLibraryOnly = getBundle("/r4/3124-bundle-library-only-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithLibraryOnly);

		assertEquals(2, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	/*
	 * This is the failure that occurs with this test:
	 *
	 * java.lang.AssertionError: Resource is MeasureReport, expected Bundle or Parameters
	 *
	 * 	at org.hl7.fhir.validation.instance.InstanceValidator.validateContains(InstanceValidator.java:4765)
	 * 	at org.hl7.fhir.validation.instance.InstanceValidator.checkChildByDefinition(InstanceValidator.java:5130)
	 */
	@Test
	@Disabled("Note that running this test with the -ea VM options triggers an assertion failure.  Please refer to this hapi-fhir issue: https://github.com/hapifhir/org.hl7.fhir.core/issues/930. See comment for details on the Exception.")
	public void validation_Jpa_Bundle_MeasureReportToMeasure() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(myDaoRegistry.getResourceDao("Library")).thenReturn(fhirResourceDaoLibrary);
		when(myDaoRegistry.getResourceDao("Measure")).thenReturn(fhirResourceDaoMeasure);

		when(fhirResourceDaoStructureDefinition.search(any(SearchParameterMap.class), any())).thenReturn(search);
		when(fhirResourceDaoLibrary.search(any(SearchParameterMap.class), any())).thenReturn(search);
		when(fhirResourceDaoMeasure.search(any(SearchParameterMap.class), any())).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureReportToReport = getBundle("/r4/3124-bundle-measure-report-to-measure-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureReportToReport);

		assertEquals(29, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void testFetchQuestionnaire_NonVersioned_Found() {
		// Setup
		when(myDaoRegistry.getResourceDao("Questionnaire")).thenReturn(myQuestionnaireDao);
		when(myQuestionnaireDao.search(any(SearchParameterMap.class), any())).thenReturn(newBundleProviderWithOneQuestionnaire());

		IValidationSupport validationSupport = getValidationSupportWithJpaPersistedResourceValidationSupport();

		// Test
		Questionnaire actual = validationSupport.fetchResource(Questionnaire.class, "http://foo");

		// Verify
		assertEquals("123", actual.getIdElement().getIdPart());
		verify(myQuestionnaireDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		assertEquals("?url=http%3A//foo&_sort=-_lastUpdated", mySearchParameterMapCaptor.getValue().toNormalizedQueryString());
	}

	@Test
	public void testFetchQuestionnaire_NonVersioned_NotFound() {
		// Setup
		when(myDaoRegistry.getResourceDao("Questionnaire")).thenReturn(myQuestionnaireDao);
		when(myQuestionnaireDao.search(any(SearchParameterMap.class), any())).thenReturn(new SimpleBundleProvider());

		IValidationSupport validationSupport = getValidationSupportWithJpaPersistedResourceValidationSupport();

		// Test (try twice)
		Questionnaire actual = validationSupport.fetchResource(Questionnaire.class, "http://foo");
		assertNull(actual);
		actual = validationSupport.fetchResource(Questionnaire.class, "http://foo");
		assertNull(actual);

		// Verify
		verify(myQuestionnaireDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		assertEquals("?url=http%3A//foo&_sort=-_lastUpdated", mySearchParameterMapCaptor.getValue().toNormalizedQueryString());
	}

	@Test
	public void testFetchQuestionnaire_Versioned_Found() {
		// Setup
		when(myDaoRegistry.getResourceDao("Questionnaire")).thenReturn(myQuestionnaireDao);
		when(myQuestionnaireDao.search(any(SearchParameterMap.class), any())).thenReturn(newBundleProviderWithOneQuestionnaire());

		IValidationSupport validationSupport = getValidationSupportWithJpaPersistedResourceValidationSupport();

		// Test
		Questionnaire actual = validationSupport.fetchResource(Questionnaire.class, "http://foo|1.0");

		// Verify
		assertEquals("123", actual.getIdElement().getIdPart());
		verify(myQuestionnaireDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		assertEquals("?url=http%3A//foo&version=1.0", mySearchParameterMapCaptor.getValue().toNormalizedQueryString());
	}

	@Test
	public void testFetchQuestionnaire_Versioned_NotFound() {
		// Setup
		when(myDaoRegistry.getResourceDao("Questionnaire")).thenReturn(myQuestionnaireDao);
		when(myQuestionnaireDao.search(any(SearchParameterMap.class), any())).thenReturn(new SimpleBundleProvider());

		IValidationSupport validationSupport = getValidationSupportWithJpaPersistedResourceValidationSupport();

		// Test (try twice)
		Questionnaire actual = validationSupport.fetchResource(Questionnaire.class, "http://foo|1.0");
		assertNull(actual);
		actual = validationSupport.fetchResource(Questionnaire.class, "http://foo|1.0");
		assertNull(actual);

		// Verify
		verify(myQuestionnaireDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		assertEquals("?url=http%3A//foo&version=1.0", mySearchParameterMapCaptor.getValue().toNormalizedQueryString());
	}

	@Test
	public void testFetchCodeSystemWithVersion() {
		// Setup
		String systemUrl = "http://test.com/codeSystem/test";
		String version = "2025-01-01";
		when(myDaoRegistry.getResourceDao("CodeSystem")).thenReturn(myCodeSystemDao);
		when(myCodeSystemDao.search(any(SearchParameterMap.class), any()))
			.thenReturn(getBundleProviderWithCodeSystem(systemUrl, version));

		IValidationSupport validationSupport = getValidationSupportWithJpaPersistedResourceValidationSupport();

		// Execute
		CodeSystem codeSystem = validationSupport.fetchResource(CodeSystem.class, systemUrl + "|" + version);

		// Verify
		verify(myCodeSystemDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		assertThat(mySearchParameterMapCaptor.getValue().toNormalizedQueryString())
			.isEqualTo("?url=http%3A//test.com/codeSystem/test&version=2025-01-01");
		assertThat(codeSystem).isNotNull();
		assertThat(codeSystem.getIdElement().getIdPart()).isEqualTo("5678");
		assertThat(codeSystem.getUrl()).isEqualTo(systemUrl);
		assertThat(codeSystem.getVersion()).isEqualTo(version);
	}

	@Nonnull
	private static SimpleBundleProvider newBundleProviderWithOneQuestionnaire() {
		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setId("123");
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(questionnaire);
		return bundleProvider;
	}


	@Nonnull
	private IValidationSupport getValidationSupportWithJpaPersistedResourceValidationSupport() {
		return new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			jpaValidator
		);
	}

	@Nonnull
	private static FhirValidator getFhirValidator() {
		FhirValidator validator;
		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		instanceValidator.setNoTerminologyChecks(true);
		validator = ourCtx.newValidator();

		validator.registerValidatorModule(instanceValidator);
		return validator;
	}

	@Nonnull
	private static Bundle getBundle(String jsonFilePath) {
		return ourCtx.newJsonParser().parseResource(Bundle.class, loadResource(jsonFilePath));
	}

	@Nonnull
	private static Predicate<SingleValidationMessage> errorMessagePredicate() {
		return message -> message.getSeverity() == ResultSeverityEnum.ERROR;
	}

	private static SimpleBundleProvider getBundleProviderWithCodeSystem(String theUrl, String theVersion) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("5678");
		codeSystem.setUrl(theUrl);
		codeSystem.setVersion(theVersion);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode("code1").setDisplay("Code 1");

		return new SimpleBundleProvider(codeSystem);
	}
}
