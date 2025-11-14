// Created by claude-sonnet-4-5
package ca.uhn.fhir.batch2.jobs.merge;

import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExtensionBasedLinkServiceTest {

	private static final String REPLACES_EXTENSION_URL = "http://hapifhir.io/fhir/StructureDefinition/replaces";
	private static final String REPLACED_BY_EXTENSION_URL = "http://hapifhir.io/fhir/StructureDefinition/replaced-by";

	private ExtensionBasedLinkService myService;

	@BeforeEach
	void setUp() {
		myService = new ExtensionBasedLinkService();
	}

	@Test
	void addReplacesLink_shouldAddExtensionWithCorrectUrlAndValue() {
		// Given
		Observation target = new Observation();
		target.setId("Observation/target-123");
		Reference sourceRef = new Reference("Observation/source-456");

		// When
		myService.addReplacesLink(target, sourceRef);

		// Then
		assertThat(target.getExtension()).hasSize(1);
		assertThat(target.getExtension().get(0).getUrl())
			.isEqualTo(REPLACES_EXTENSION_URL);
		Reference storedRef = (Reference) target.getExtension().get(0).getValue();
		assertThat(storedRef.getReference()).isEqualTo("Observation/source-456");
	}


	@Test
	void addReplacedByLink_shouldAddExtensionWithCorrectUrlAndValue() {
		// Given
		Observation source = new Observation();
		source.setId("Observation/source-456");
		Reference targetRef = new Reference("Observation/target-123");

		// When
		myService.addReplacedByLink(source, targetRef);

		// Then
		assertThat(source.getExtension()).hasSize(1);
		assertThat(source.getExtension().get(0).getUrl())
			.isEqualTo(REPLACED_BY_EXTENSION_URL);
		Reference storedRef = (Reference) source.getExtension().get(0).getValue();
		assertThat(storedRef.getReference()).isEqualTo("Observation/target-123");
	}

	@Test
	void getReplacesLinks_shouldReturnEmptyListWhenNoExtensions() {
		// Given
		Observation resource = new Observation();

		// When
		List<IBaseReference> links = myService.getReplacesLinks(resource);

		// Then
		assertThat(links).isEmpty();
	}

	@Test
	void getReplacesLinks_shouldReturnAllReplacesReferences() {
		// Given
		Observation target = new Observation();
		Reference sourceRef1 = new Reference("Observation/source-456");
		Reference sourceRef2 = new Reference("Observation/source-789");
		myService.addReplacesLink(target, sourceRef1);
		myService.addReplacesLink(target, sourceRef2);

		// When
		List<IBaseReference> links = myService.getReplacesLinks(target);

		// Then
		assertThat(links).hasSize(2);
		assertThat(target.getExtension().get(0).getUrl()).isEqualTo(REPLACES_EXTENSION_URL);
		assertThat(target.getExtension().get(1).getUrl()).isEqualTo(REPLACES_EXTENSION_URL);
		assertThat(links.get(0).getReferenceElement().getValue()).isEqualTo("Observation/source-456");
		assertThat(links.get(1).getReferenceElement().getValue()).isEqualTo("Observation/source-789");
	}

	@Test
	void getReplacedByLinks_shouldReturnEmptyListWhenNoExtensions() {
		// Given
		Observation resource = new Observation();

		// When
		List<IBaseReference> links = myService.getReplacedByLinks(resource);

		// Then
		assertThat(links).isEmpty();
	}

	@Test
	void getReplacedByLinks_shouldReturnAllReplacedByReferences() {
		// Given
		Observation source = new Observation();
		Reference targetRef1 = new Reference("Observation/target-123");
		Reference targetRef2 = new Reference("Observation/target-456");
		myService.addReplacedByLink(source, targetRef1);
		myService.addReplacedByLink(source, targetRef2);

		// When
		List<IBaseReference> links = myService.getReplacedByLinks(source);

		// Then
		assertThat(links).hasSize(2);
		assertThat(source.getExtension().get(0).getUrl()).isEqualTo(REPLACED_BY_EXTENSION_URL);
		assertThat(source.getExtension().get(1).getUrl()).isEqualTo(REPLACED_BY_EXTENSION_URL);
		assertThat(links.get(0).getReferenceElement().getValue()).isEqualTo("Observation/target-123");
		assertThat(links.get(1).getReferenceElement().getValue()).isEqualTo("Observation/target-456");
	}

	@Test
	void hasReplacedByLink_shouldReturnFalseWhenNoExtensions() {
		// Given
		Observation resource = new Observation();

		// When
		boolean hasLink = myService.hasReplacedByLink(resource);

		// Then
		assertThat(hasLink).isFalse();
	}

	@Test
	void hasReplacedByLink_shouldReturnTrueWhenExtensionExists() {
		// Given
		Observation source = new Observation();
		Reference targetRef = new Reference("Observation/target-123");
		myService.addReplacedByLink(source, targetRef);

		// When
		boolean hasLink = myService.hasReplacedByLink(source);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void hasReplacesLinkTo_shouldReturnFalseWhenNoExtensions() {
		// Given
		Observation resource = new Observation();
		IIdType targetId = new IdType("Observation/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(resource, targetId);

		// Then
		assertThat(hasLink).isFalse();
	}

	@Test
	void hasReplacesLinkTo_shouldReturnTrueWhenMatchingLinkExists() {
		// Given
		Observation target = new Observation();
		Reference sourceRef = new Reference("Observation/source-456");
		myService.addReplacesLink(target, sourceRef);
		IIdType targetId = new IdType("Observation/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetId);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void hasReplacesLinkTo_shouldReturnFalseWhenNoMatchingLink() {
		// Given
		Observation target = new Observation();
		Reference sourceRef = new Reference("Observation/source-456");
		myService.addReplacesLink(target, sourceRef);
		IIdType targetId = new IdType("Observation/source-999");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetId);

		// Then
		assertThat(hasLink).isFalse();
	}

	@Test
	void hasReplacesLinkTo_shouldCompareVersionlessIds() {
		// Given
		Observation target = new Observation();
		Reference sourceRef = new Reference("Observation/source-456/_history/1");
		myService.addReplacesLink(target, sourceRef);
		IIdType targetIdUnversioned = new IdType("Observation/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetIdUnversioned);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void shouldNotConfuseReplacesAndReplacedByExtensions() {
		// Given
		Observation resource = new Observation();
		Reference replacesRef = new Reference("Observation/source-456");
		Reference replacedByRef = new Reference("Observation/target-789");

		// When
		myService.addReplacesLink(resource, replacesRef);
		myService.addReplacedByLink(resource, replacedByRef);

		// Then
		List<IBaseReference> replacesLinks = myService.getReplacesLinks(resource);
		List<IBaseReference> replacedByLinks = myService.getReplacedByLinks(resource);

		assertThat(replacesLinks).hasSize(1);
		assertThat(replacedByLinks).hasSize(1);
		assertThat(replacesLinks.get(0).getReferenceElement().getValue()).isEqualTo("Observation/source-456");
		assertThat(replacedByLinks.get(0).getReferenceElement().getValue()).isEqualTo("Observation/target-789");
	}

	@Test
	void hasReplacesLinkTo_shouldHandleEmptyReferenceElement() {
		// Given
		Observation target = new Observation();

		// Manually add an extension with an empty Reference
		Reference emptyRef = new Reference();
		target.addExtension()
			.setUrl(REPLACES_EXTENSION_URL)
			.setValue(emptyRef);

		// Add a valid reference as well
		Reference validRef = new Reference("Observation/source-456");
		target.addExtension()
			.setUrl(REPLACES_EXTENSION_URL)
			.setValue(validRef);

		IIdType targetId = new IdType("Observation/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetId);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void getLinksWithExtensionUrl_shouldSkipNonReferenceValue() {
		// Given
		Observation resource = new Observation();

		// Manually add an extension with a non-Reference value (StringType)
		resource.addExtension()
			.setUrl(REPLACES_EXTENSION_URL)
			.setValue(new StringType("invalid-value"));

		// Add a valid reference as well
		Reference validRef = new Reference("Observation/source-456");
		resource.addExtension()
			.setUrl(REPLACES_EXTENSION_URL)
			.setValue(validRef);

		// When
		List<IBaseReference> links = myService.getReplacesLinks(resource);

		// Then - should only return the valid reference, skipping the invalid one
		assertThat(links).hasSize(1);
		assertThat(links.get(0).getReferenceElement().getValue()).isEqualTo("Observation/source-456");
	}
}
