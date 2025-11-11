// Created by claude-sonnet-4-5
package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PatientNativeLinkServiceTest {

	private static final String REPLACES_LINK_TYPE = "replaces";
	private static final String REPLACED_BY_LINK_TYPE = "replaced-by";

	private PatientNativeLinkService myService;

	@BeforeEach
	void setUp() {
		myService = new PatientNativeLinkService();
	}

	@Test
	void addReplacesLink_shouldAddLinkWithCorrectTypeAndReference() {
		// Given
		Patient target = new Patient();
		target.setId("Patient/target-123");
		Reference sourceRef = new Reference("Patient/source-456");

		// When
		myService.addReplacesLink(target, sourceRef);

		// Then
		assertThat(target.getLink()).hasSize(1);
		assertThat(target.getLink().get(0).getType().toCode())
			.isEqualTo(REPLACES_LINK_TYPE);
		Reference storedRef = target.getLink().get(0).getOther();
		assertThat(storedRef.getReference()).isEqualTo("Patient/source-456");
	}


	@Test
	void addReplacedByLink_shouldAddLinkWithCorrectTypeAndReference() {
		// Given
		Patient source = new Patient();
		source.setId("Patient/source-456");
		Reference targetRef = new Reference("Patient/target-123");

		// When
		myService.addReplacedByLink(source, targetRef);

		// Then
		assertThat(source.getLink()).hasSize(1);
		assertThat(source.getLink().get(0).getType().toCode())
			.isEqualTo(REPLACED_BY_LINK_TYPE);
		Reference storedRef = source.getLink().get(0).getOther();
		assertThat(storedRef.getReference()).isEqualTo("Patient/target-123");
	}

	@Test
	void getReplacesLinks_shouldReturnEmptyListWhenNoLinks() {
		// Given
		Patient resource = new Patient();

		// When
		List<IBaseReference> links = myService.getReplacesLinks(resource);

		// Then
		assertThat(links).isEmpty();
	}

	@Test
	void getReplacesLinks_shouldReturnAllReplacesReferences() {
		// Given
		Patient target = new Patient();
		Reference sourceRef1 = new Reference("Patient/source-456");
		Reference sourceRef2 = new Reference("Patient/source-789");
		myService.addReplacesLink(target, sourceRef1);
		myService.addReplacesLink(target, sourceRef2);

		// When
		List<IBaseReference> links = myService.getReplacesLinks(target);

		// Then
		assertThat(links).hasSize(2);
		assertThat(target.getLink().get(0).getType().toCode()).isEqualTo(REPLACES_LINK_TYPE);
		assertThat(target.getLink().get(1).getType().toCode()).isEqualTo(REPLACES_LINK_TYPE);
		assertThat(links.get(0).getReferenceElement().getValue()).isEqualTo("Patient/source-456");
		assertThat(links.get(1).getReferenceElement().getValue()).isEqualTo("Patient/source-789");
	}

	@Test
	void getReplacedByLinks_shouldReturnEmptyListWhenNoLinks() {
		// Given
		Patient resource = new Patient();

		// When
		List<IBaseReference> links = myService.getReplacedByLinks(resource);

		// Then
		assertThat(links).isEmpty();
	}

	@Test
	void getReplacedByLinks_shouldReturnAllReplacedByReferences() {
		// Given
		Patient source = new Patient();
		Reference targetRef1 = new Reference("Patient/target-123");
		Reference targetRef2 = new Reference("Patient/target-456");
		myService.addReplacedByLink(source, targetRef1);
		myService.addReplacedByLink(source, targetRef2);

		// When
		List<IBaseReference> links = myService.getReplacedByLinks(source);

		// Then
		assertThat(links).hasSize(2);
		assertThat(source.getLink().get(0).getType().toCode()).isEqualTo(REPLACED_BY_LINK_TYPE);
		assertThat(source.getLink().get(1).getType().toCode()).isEqualTo(REPLACED_BY_LINK_TYPE);
		assertThat(links.get(0).getReferenceElement().getValue()).isEqualTo("Patient/target-123");
		assertThat(links.get(1).getReferenceElement().getValue()).isEqualTo("Patient/target-456");
	}

	@Test
	void hasReplacedByLink_shouldReturnFalseWhenNoLinks() {
		// Given
		Patient resource = new Patient();

		// When
		boolean hasLink = myService.hasReplacedByLink(resource);

		// Then
		assertThat(hasLink).isFalse();
	}

	@Test
	void hasReplacedByLink_shouldReturnTrueWhenLinkExists() {
		// Given
		Patient source = new Patient();
		Reference targetRef = new Reference("Patient/target-123");
		myService.addReplacedByLink(source, targetRef);

		// When
		boolean hasLink = myService.hasReplacedByLink(source);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void hasReplacesLinkTo_shouldReturnFalseWhenNoLinks() {
		// Given
		Patient resource = new Patient();
		IIdType targetId = new IdType("Patient/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(resource, targetId);

		// Then
		assertThat(hasLink).isFalse();
	}

	@Test
	void hasReplacesLinkTo_shouldReturnTrueWhenMatchingLinkExists() {
		// Given
		Patient target = new Patient();
		Reference sourceRef = new Reference("Patient/source-456");
		myService.addReplacesLink(target, sourceRef);
		IIdType targetId = new IdType("Patient/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetId);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void hasReplacesLinkTo_shouldReturnFalseWhenNoMatchingLink() {
		// Given
		Patient target = new Patient();
		Reference sourceRef = new Reference("Patient/source-456");
		myService.addReplacesLink(target, sourceRef);
		IIdType targetId = new IdType("Patient/source-999");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetId);

		// Then
		assertThat(hasLink).isFalse();
	}

	@Test
	void hasReplacesLinkTo_shouldCompareVersionlessIds() {
		// Given
		Patient target = new Patient();
		Reference sourceRef = new Reference("Patient/source-456/_history/1");
		myService.addReplacesLink(target, sourceRef);
		IIdType targetIdUnversioned = new IdType("Patient/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetIdUnversioned);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void shouldNotConfuseReplacesAndReplacedByLinks() {
		// Given
		Patient resource = new Patient();
		Reference replacesRef = new Reference("Patient/source-456");
		Reference replacedByRef = new Reference("Patient/target-789");

		// When
		myService.addReplacesLink(resource, replacesRef);
		myService.addReplacedByLink(resource, replacedByRef);

		// Then
		List<IBaseReference> replacesLinks = myService.getReplacesLinks(resource);
		List<IBaseReference> replacedByLinks = myService.getReplacedByLinks(resource);

		assertThat(replacesLinks).hasSize(1);
		assertThat(replacedByLinks).hasSize(1);
		assertThat(replacesLinks.get(0).getReferenceElement().getValue()).isEqualTo("Patient/source-456");
		assertThat(replacedByLinks.get(0).getReferenceElement().getValue()).isEqualTo("Patient/target-789");
	}

	@Test
	void hasReplacesLinkTo_shouldHandleEmptyReferenceElement() {
		// Given
		Patient target = new Patient();

		// Manually add a link with an empty Reference
		Reference emptyRef = new Reference();
		target.addLink()
			.setType(Patient.LinkType.REPLACES)
			.setOther(emptyRef);

		// Add a valid reference as well
		Reference validRef = new Reference("Patient/source-456");
		target.addLink()
			.setType(Patient.LinkType.REPLACES)
			.setOther(validRef);

		IIdType targetId = new IdType("Patient/source-456");

		// When
		boolean hasLink = myService.hasReplacesLinkTo(target, targetId);

		// Then
		assertThat(hasLink).isTrue();
	}

	@Test
	void getLinksWithType_shouldSkipNullOtherReference() {
		// Given
		Patient resource = new Patient();

		// Manually add a link with null other reference
		resource.addLink()
			.setType(Patient.LinkType.REPLACES);

		// Add a valid reference as well
		Reference validRef = new Reference("Patient/source-456");
		resource.addLink()
			.setType(Patient.LinkType.REPLACES)
			.setOther(validRef);

		// When
		List<IBaseReference> links = myService.getReplacesLinks(resource);

		// Then - should only return the valid reference, skipping the null one
		assertThat(links).hasSize(1);
		assertThat(links.get(0).getReferenceElement().getValue()).isEqualTo("Patient/source-456");
	}

	@Test
	void shouldPreserveReferenceDisplayText() {
		// Given
		Patient patient = new Patient();
		patient.setId("Patient/pat-123");
		Reference ref = new Reference("Patient/pat-456").setDisplay("Test Display");

		// When
		myService.addReplacesLink(patient, ref);

		// Then
		IBaseReference retrieved = myService.getReplacesLinks(patient).get(0);
		assertThat(((Reference) retrieved).getDisplay()).isEqualTo("Test Display");
		assertThat(retrieved.getReferenceElement().getValue()).isEqualTo("Patient/pat-456");
	}

	@Test
	void shouldNotAffectOtherLinkTypes() {
		// Given - Patient with SEEALSO link
		Patient patient = new Patient();
		patient.setId("Patient/pat-123");
		patient.addLink()
			.setType(Patient.LinkType.SEEALSO)
			.setOther(new Reference("Patient/other-patient"));

		// When
		myService.addReplacesLink(patient, new Reference("Patient/pat-456"));
		myService.addReplacedByLink(patient, new Reference("Patient/pat-789"));
		// Then - Verify both links exist
		assertThat(patient.getLink()).hasSize(3);

		// Verify first link (SEEALSO) is untouched - also verifies link order preserved
		assertThat(patient.getLink().get(0).getType()).isEqualTo(Patient.LinkType.SEEALSO);
		assertThat(patient.getLink().get(0).getOther().getReference()).isEqualTo("Patient/other-patient");

		// Verify second link is the replaces link we added
		assertThat(patient.getLink().get(1).getType()).isEqualTo(Patient.LinkType.REPLACES);
		assertThat(patient.getLink().get(1).getOther().getReference()).isEqualTo("Patient/pat-456");
		// Verify third link is the replaced-by link we added
		assertThat(patient.getLink().get(2).getType()).isEqualTo(Patient.LinkType.REPLACEDBY);
		assertThat(patient.getLink().get(2).getOther().getReference()).isEqualTo("Patient/pat-789");
	}

	@Test
	void hasReplacesLinkTo_shouldMatchWhenTargetIdHasVersion() {
		// Given - Reference without version
		Patient patient = new Patient();
		patient.setId("Patient/pat-123");
		Reference ref = new Reference("Patient/pat-456");
		myService.addReplacesLink(patient, ref);

		// When - Query with versioned ID
		IIdType targetIdVersioned = new IdType("Patient/pat-456/_history/2");

		// Then - Should match even though target has version
		assertThat(myService.hasReplacesLinkTo(patient, targetIdVersioned)).isTrue();
	}

	@Test
	void hasReplacedByLink_shouldReturnTrueWithMultipleLinks() {
		// Given
		Patient patient = new Patient();
		patient.setId("Patient/pat-123");
		myService.addReplacedByLink(patient, new Reference("Patient/target1"));
		myService.addReplacedByLink(patient, new Reference("Patient/target2"));

		// When
		boolean hasLink = myService.hasReplacedByLink(patient);

		// Then
		assertThat(hasLink).isTrue();
		assertThat(myService.getReplacedByLinks(patient)).hasSize(2);
	}

	@Test
	void hasReplacesLinkTo_shouldFindMatchAmongMultipleLinks() {
		// Given - Multiple replaces links
		Patient patient = new Patient();
		patient.setId("Patient/pat-123");
		myService.addReplacesLink(patient, new Reference("Patient/source1"));
		myService.addReplacesLink(patient, new Reference("Patient/source2"));
		myService.addReplacesLink(patient, new Reference("Patient/source3"));

		// When/Then - Should find match
		assertThat(myService.hasReplacesLinkTo(patient, new IdType("Patient/source2"))).isTrue();

		// When/Then - Should not find non-existent match
		assertThat(myService.hasReplacesLinkTo(patient, new IdType("Patient/source999"))).isFalse();

		// Verify all three links are present
		assertThat(myService.getReplacesLinks(patient)).hasSize(3);
	}
}
