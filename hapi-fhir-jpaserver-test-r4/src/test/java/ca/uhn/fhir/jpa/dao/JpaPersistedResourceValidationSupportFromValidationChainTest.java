package ca.uhn.fhir.jpa.dao;

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
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.function.Predicate;

import static ca.uhn.fhir.util.ClasspathUtil.loadResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaPersistedResourceValidationSupportFromValidationChainTest {
	private static final FhirContext ourCtx = FhirContext.forR4();

	private IValidationSupport jpaValidator;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao<ValueSet> fhirResourceDaoValueSet;

	@Mock
	private IFhirResourceDao<StructureDefinition> fhirResourceDaoStructureDefinition;

	@Mock
	private IBundleProvider search;

	@BeforeEach
	public void setUp() {
		jpaValidator = new JpaPersistedResourceValidationSupport(ourCtx);
		ReflectionTestUtils.setField(jpaValidator, "myDaoRegistry", myDaoRegistry);
	}

	@Test
	public void validation_Jpa_Bundle_MeasureReferencesLibraryAndLibrary() {
		when(myDaoRegistry.getResourceDao("ValueSet")).thenReturn(fhirResourceDaoValueSet);
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);

		when(fhirResourceDaoValueSet.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);
		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithBadLibrary = getBundleWithBadLibrary();
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithBadLibrary);

		// TODO:  think about the number of errors to assert once the Exception is resolved
		assertEquals(7, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_MeasureOnly() {
		when(myDaoRegistry.getResourceDao("ValueSet")).thenReturn(fhirResourceDaoValueSet);
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);

		when(fhirResourceDaoValueSet.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);
		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureOnly = getBundleWithMeasureOnly();
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureOnly );

		// TODO:  think about the number of errors to assert once the Exception is resolved
		assertEquals(7, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_LibraryOnly() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);

		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithLibraryOnly = getBundleWithLibraryOnly();
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithLibraryOnly);

		assertEquals(2, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@Test
	public void validation_Jpa_Bundle_MeasureOnly_NoLibraryReference() {
		when(myDaoRegistry.getResourceDao("StructureDefinition")).thenReturn(fhirResourceDaoStructureDefinition);
		when(fhirResourceDaoStructureDefinition.search(Mockito.any(SearchParameterMap.class))).thenReturn(search);

		ourCtx.setValidationSupport(getValidationSupportWithJpaPersistedResourceValidationSupport());
		final Bundle bundleWithMeasureOnlyNoLibraryReference = getBundleWithMeasureOnlyNoLibraryReference();
		final FhirValidator validator = getFhirValidator();

		final ValidationResult validationResult = validator.validateWithResult(bundleWithMeasureOnlyNoLibraryReference);

		assertEquals(7, validationResult.getMessages().stream().filter(errorMessagePredicate()).count());
	}

	@NotNull
	private static FhirValidator getFhirValidator() {
		FhirValidator validator;
		final FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ourCtx);
		instanceValidator.setNoTerminologyChecks(true);
		validator = ourCtx.newValidator();

		validator.registerValidatorModule(instanceValidator);
		return validator;
	}

	@NotNull
	private static Bundle getBundleWithBadLibrary() {
		return ourCtx.newJsonParser().parseResource(Bundle.class, loadResource("/r4/3124-bundle-slim-post.json"));
	}

	@NotNull
	private static Bundle getBundleWithLibraryOnly() {
		return ourCtx.newJsonParser().parseResource(Bundle.class, loadResource("/r4/3124-bundle-library-only-post.json"));
	}

	@NotNull
	private static Bundle getBundleWithMeasureOnly() {
		return ourCtx.newJsonParser().parseResource(Bundle.class, loadResource("/r4/3124-bundle-measure-onLy-post.json"));
	}

	@NotNull
	private static Bundle getBundleWithMeasureOnlyNoLibraryReference() {
		return ourCtx.newJsonParser().parseResource(Bundle.class, loadResource("/r4/3124-bundle-measure-onLy-no-library-reference-post.json"));
	}

	@NotNull
	private IValidationSupport getValidationSupportWithJpaPersistedResourceValidationSupport() {
		return new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			jpaValidator
		);
	}

	@NotNull
	private static Predicate<SingleValidationMessage> errorMessagePredicate() {
		return message -> message.getSeverity() == ResultSeverityEnum.ERROR;
	}
}
