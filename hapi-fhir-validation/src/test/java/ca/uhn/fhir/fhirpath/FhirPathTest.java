package ca.uhn.fhir.fhirpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.TestUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirPathTest extends BaseValidationTestWithInlineMocks {

	@ParameterizedTest
	@MethodSource("provideContexts")
	public void testEvaluateNormal(FhirContext theFhirContext) {
		IBaseResource resource = createPatientResourceWithTwoNames(theFhirContext);
		IFhirPath fp = theFhirContext.newFhirPath();
		List<IBase> names = fp.evaluate(resource, "Patient.name", IBase.class);
		assertThat(names).hasSize(2);
	}

	@SuppressWarnings("deprecation")
	@ParameterizedTest
	@MethodSource("provideContexts")
	public void testEvaluateNormal_LegacyMethod(FhirContext theFhirContext) {
		IBaseResource p = createPatientResourceWithTwoNames(theFhirContext);

		IFhirPath fp = theFhirContext.newFluentPath();
		List<IBase> names = fp.evaluate(p, "Patient.name", IBase.class);
		assertThat(names).hasSize(2);
	}

	@ParameterizedTest
	@MethodSource("provideContexts")
	public void testEvaluateUnknownPath(FhirContext theFhirContext) {
		IBaseResource p = createPatientResourceWithTwoNames(theFhirContext);

		IFhirPath fp = theFhirContext.newFhirPath();
		List<HumanName> names = fp.evaluate(p, "Patient.nameFOO", HumanName.class);
		assertThat(names).isEmpty();
	}

	@ParameterizedTest
	@MethodSource("provideContexts")
	public void testEvaluateInvalidPath(FhirContext theFhirContext) {
		IBaseResource p = createPatientResourceWithTwoNames(theFhirContext);

		IFhirPath fp = theFhirContext.newFhirPath();
		try {
			fp.evaluate(p, "Patient....nameFOO", HumanName.class);
		} catch (FhirPathExecutionException e) {
			assertThat(e.getMessage()).contains("termination at unexpected token");
		}
	}

	@ParameterizedTest
	@MethodSource("provideContexts")
	public void testEvaluateWrongType(FhirContext theFhirContext) {
		IBaseResource p = createPatientResourceWithTwoNames(theFhirContext);

		Class<? extends IBase> stringType = theFhirContext.getElementDefinition("string").getImplementingClass();

		IFhirPath fp = theFhirContext.newFhirPath();
		try {
			fp.evaluate(p, "Patient.name", stringType);
		} catch (FhirPathExecutionException e) {
			String expected = "FhirPath expression returned unexpected type HumanName - Expected " + stringType.getName();
			assertThat(e.getMessage()).endsWith(expected);
		}
	}

	@Nonnull
	private static IBaseResource createPatientResourceWithTwoNames(FhirContext theFhirContext) {
		IBaseResource resource = theFhirContext.getResourceDefinition("Patient").newInstance();
		FhirTerser terser = theFhirContext.newTerser();
		IBase humanName = terser.addElement(resource, "name");
		terser.addElement(humanName, "family", "N1F1");
		terser.addElement(humanName, "given", "N1G1");
		terser.addElement(humanName, "given", "N1G2");
		IBase humanName2 = terser.addElement(resource, "name");
		terser.addElement(humanName2, "family", "N2F1");
		terser.addElement(humanName2, "given", "N2G1");
		terser.addElement(humanName2, "given", "N2G2");
		return resource;
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static Stream<FhirContext> provideContexts() {
		return Arrays
			.stream(FhirVersionEnum.values())
			.filter(t -> t.isEqualOrNewerThan(FhirVersionEnum.DSTU3))
			.map(FhirContext::forCached);
	}
}
