package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.junit.jupiter.api.Test;

import static com.helger.commons.mock.CommonsAssert.assertEquals;

public class CompositionBuilderTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Test
	public void testAddAuthor() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.addAuthor(new IdType("Organization/author0"));
		builder.addAuthor(new IdType("Organization/author1"));

		Composition composition = builder.getComposition();
		assertEquals("Organization/author0", composition.getAuthor().get(0).getReference());
		assertEquals("Organization/author1", composition.getAuthor().get(1).getReference());
	}

	@Test
	public void testSetStatus() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.setStatus("final");

		Composition composition = builder.getComposition();
		assertEquals(Composition.CompositionStatus.FINAL, composition.getStatus());
	}

	@Test
	public void testSetSubject() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.setSubject(new IdType("Patient/patient"));

		Composition composition = builder.getComposition();
		assertEquals("Patient/patient", composition.getSubject().getReference());
	}

	@Test
	public void testAddTypeCoding() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.addTypeCoding("system0", "code0", "display0");
		builder.addTypeCoding("system1", "code1", "display1");

		Composition composition = builder.getComposition();
		assertEquals("code0", composition.getType().getCoding().get(0).getCode());
		assertEquals("system0", composition.getType().getCoding().get(0).getSystem());
		assertEquals("display0", composition.getType().getCoding().get(0).getDisplay());
		assertEquals("code1", composition.getType().getCoding().get(1).getCode());
		assertEquals("system1", composition.getType().getCoding().get(1).getSystem());
		assertEquals("display1", composition.getType().getCoding().get(1).getDisplay());
	}

	@Test
	public void testSetDate() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.setDate(new InstantType("2023-01-01T00:00:01Z"));

		Composition composition = builder.getComposition();
		assertEquals("2023-01-01T00:00:01Z", composition.getDateElement().getValueAsString());
	}

	@Test
	public void testSetTitle() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.setTitle("title");

		Composition composition = builder.getComposition();
		assertEquals("title", composition.getTitle());
	}

	@Test
	public void testSetConfidentiality() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		builder.setConfidentiality(Composition.DocumentConfidentiality.L.toCode());

		Composition composition = builder.getComposition();
		assertEquals(Composition.DocumentConfidentiality.L, composition.getConfidentiality());
	}


	@Test
	public void testAddSection() {
		CompositionBuilder builder = new CompositionBuilder(myCtx);
		CompositionBuilder.SectionBuilder sectionBuilder = builder.addSection();
		sectionBuilder.setTitle("title");

		Composition composition = builder.getComposition();
		Composition.SectionComponent section = composition.getSection().get(0);
		assertEquals("title", section.getTitle());

	}

}
