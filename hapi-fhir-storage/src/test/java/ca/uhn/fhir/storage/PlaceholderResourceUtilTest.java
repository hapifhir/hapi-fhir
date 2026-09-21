package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Created by Claude Fable 5
class PlaceholderResourceUtilTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Test
	void testBuildPlaceholderResource_stampsMarkerExtensionAndIdentifiers() {
		CanonicalIdentifier identifier = new CanonicalIdentifier();
		identifier.setSystem("http://sys");
		identifier.setValue("val");

		Patient placeholder = (Patient) PlaceholderResourceUtil.buildPlaceholderResource(
				ourFhirContext, ourFhirContext.getResourceDefinition("Patient"), List.of(identifier));

		assertThat(placeholder.getIdentifier()).hasSize(1);
		assertThat(placeholder.getIdentifierFirstRep().getSystem()).isEqualTo("http://sys");
		assertThat(placeholder.getIdentifierFirstRep().getValue()).isEqualTo("val");
		BooleanType markerValue = (BooleanType)
				placeholder.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER).getValue();
		assertThat(markerValue.booleanValue()).isTrue();
	}

	@Test
	void testBuildPlaceholderResource_typeWithoutExtensionSupportSkipsMarker() {
		IBaseResource placeholder = PlaceholderResourceUtil.buildPlaceholderResource(
				ourFhirContext, ourFhirContext.getResourceDefinition("Binary"), List.of());

		assertThat(placeholder).isInstanceOf(Binary.class);
	}

	static List<IBaseResource> nonplaceholderResources() {
		List<IBaseResource> args = new ArrayList<>();

		// 1 basic resource with extension, but 'false'
		{
			Patient patient = new Patient();
			patient.addExtension()
				.setUrl(EXT_RESOURCE_PLACEHOLDER)
				.setValue(new BooleanType(false));
			args.add(patient);
		}
		// 2 basic resource without extension
		{
			args.add(new Patient());
		}
		// 3 basic resource with multiple extensions, but not a placeholder one
		{
			Patient patient = new Patient();
			patient.addExtension()
				.setUrl(EXT_RESOURCE_PLACEHOLDER + "/not")
				.setValue(new BooleanType(true));
			args.add(patient);
		}
		// 4 basic resource with extension with wrong data type
		{
			Practitioner practitioner = new Practitioner();
			practitioner.addExtension()
				.setUrl(EXT_RESOURCE_PLACEHOLDER)
				.setValue(
					new DateTimeType()
				);
			args.add(practitioner);
		}
		// 5 non-IBaseHasExtensions resource
		{
			// Binary extends IBaseResource
			// but not IBaseHasExtensions
			args.add(new Binary());
		}
		// 6 a resource with the extension holding 'null' value
		{
			Patient patient = new Patient();
			patient.addExtension()
				.setUrl(EXT_RESOURCE_PLACEHOLDER)
				.setValue(null);
			args.add(patient);
		}
		// 7 a resource with the extension holding boolean null value
		{
			Patient patient = new Patient();
			patient.addExtension()
				.setUrl(EXT_RESOURCE_PLACEHOLDER)
				.setValue(new BooleanType());
			args.add(patient);
		}

		return args;
	}

	@ParameterizedTest
	@MethodSource("nonplaceholderResources")
	public void isPlaceholderResource_withNonPlaceholderResources_returnsFalse(IBaseResource theResource) {
		// test
		boolean val = PlaceholderResourceUtil.isPlaceholderResource(theResource);

		// validate
		assertFalse(val);
	}

	@Test
	public void isPlaceholderResource_withProperPlaceholderResource_returnsTrue() {
		// setup
		Patient patient = (Patient) PlaceholderResourceUtil.buildPlaceholderResource(
			ourFhirContext,
			ourFhirContext.getResourceDefinition("Patient"),
			List.of()
		);

		// test
		boolean isPlaceholder = PlaceholderResourceUtil.isPlaceholderResource(patient);

		// validate
		assertTrue(isPlaceholder);
	}
}
