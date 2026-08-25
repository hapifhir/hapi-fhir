package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
				placeholder.getExtensionByUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER).getValue();
		assertThat(markerValue.booleanValue()).isTrue();
	}

	@Test
	void testBuildPlaceholderResource_typeWithoutExtensionSupportSkipsMarker() {
		IBaseResource placeholder = PlaceholderResourceUtil.buildPlaceholderResource(
				ourFhirContext, ourFhirContext.getResourceDefinition("Binary"), List.of());

		assertThat(placeholder).isInstanceOf(Binary.class);
	}
}
