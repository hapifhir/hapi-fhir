package ca.uhn.fhir.jpa.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.annotation.Nonnull;
import java.util.function.Predicate;

import static ca.uhn.fhir.util.ClasspathUtil.loadResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaPersistedResourceValidationSupportFromValidationChainTest {
	private static final FhirContext ourCtx = FhirContext.forR4();

	private IValidationSupport jpaValidator;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao<StructureDefinition> fhirResourceDaoStructureDefinition;

	@Mock
	private IFhirResourceDao<Library> fhirResourceDaoLibrary;

	@Mock
	private IFhirResourceDao<Measure> fhirResourceDaoMeasure;

	@Mock
	private IBundleProvider search;

	@BeforeEach
	public void setUp() {
		jpaValidator = new JpaPersistedResourceValidationSupport(ourCtx);
		ReflectionTestUtils.setField(jpaValidator, "myDaoRegistry", myDaoRegistry);
	}

	@Test
	public void validation_Jpa_Bundle_MeasureReferencesLibraryAndLibrary() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(myDaoRegistry.getResourceDao("Library")).thenReturn(fhirResourceDaoLibrary);

		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);
		when(fhirResourceDaoLibrary.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithBadLibrary = getBundle("/r4/3124-bundle-measure-and-library-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithBadLibrary);

		assertEquals(12, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_MeasureOnly() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(myDaoRegistry.getResourceDao("Library")).thenReturn(fhirResourceDaoLibrary);

		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);
		when(fhirResourceDaoLibrary.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureOnly = getBundle("/r4/3124-bundle-measure-only-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureOnly );

		assertEquals(10, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_MeasureOnly_NoLibraryReference() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureOnlyNoLibraryReference = getBundle("/r4/3124-bundle-measure-only-no-library-reference-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureOnlyNoLibraryReference);

		assertEquals(9, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_LibraryOnly() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);

		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

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

		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);
		when(fhirResourceDaoLibrary.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);
		when(fhirResourceDaoMeasure.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureReportToReport = getBundle("/r4/3124-bundle-measure-report-to-measure-post.json");
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureReportToReport);

		assertEquals(29, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
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
	private IValidationSupport getValidationSupportWithJpaPersistedResourceValidationSupport() {
		return new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			jpaValidator
		);
	}

	@Nonnull
	private static Predicate<SingleValidationMessage> errorMessagePredicate() {
		return message -> message.getSeverity() == ResultSeverityEnum.ERROR;
	}
}
