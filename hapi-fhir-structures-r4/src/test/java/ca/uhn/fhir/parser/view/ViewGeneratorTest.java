package ca.uhn.fhir.parser.view;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewGeneratorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ViewGeneratorTest.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testView() {

		ExtPatient src = new ExtPatient();
		src.addIdentifier().setSystem("urn:sys").setValue("id1");
		src.addIdentifier().setSystem("urn:sys").setValue("id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(src);

		ourLog.info(enc);

		IParser parser = ourCtx.newXmlParser();
		Patient nonExt = parser.parseResource(Patient.class, enc);

		assertThat(nonExt.getClass()).isEqualTo(Patient.class);
		assertThat(nonExt.getIdentifier().get(0).getSystem()).isEqualTo("urn:sys");
		assertThat(nonExt.getIdentifier().get(0).getValue()).isEqualTo("id1");
		assertThat(nonExt.getIdentifier().get(1).getSystem()).isEqualTo("urn:sys");
		assertThat(nonExt.getIdentifier().get(1).getValue()).isEqualTo("id2");

		List<Extension> ext = nonExt.getExtensionsByUrl("urn:ext");
		assertThat(ext).hasSize(1);
		assertThat(ext.get(0).getUrl()).isEqualTo("urn:ext");
		assertThat(ext.get(0).getValueAsPrimitive().getClass()).isEqualTo(IntegerType.class);
		assertThat(ext.get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("100");

		List<Extension> modExt = nonExt.getExtensionsByUrl("urn:modExt");
		assertThat(modExt).hasSize(1);
		assertThat(modExt.get(0).getUrl()).isEqualTo("urn:modExt");
		assertThat(modExt.get(0).getValueAsPrimitive().getClass()).isEqualTo(IntegerType.class);
		assertThat(modExt.get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("200");

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertThat(va.getIdentifier().get(0).getSystem()).isEqualTo("urn:sys");
		assertThat(va.getIdentifier().get(0).getValue()).isEqualTo("id1");
		assertThat(va.getIdentifier().get(1).getSystem()).isEqualTo("urn:sys");
		assertThat(va.getIdentifier().get(1).getValue()).isEqualTo("id2");
		assertThat(va.getExt().getValue().intValue()).isEqualTo(100);
		assertThat(va.getModExt().getValue().intValue()).isEqualTo(200);

		assertThat(va.getExtension()).isEmpty();
	}

	@Test
	public void testViewJson() {

		ExtPatient src = new ExtPatient();
		src.addIdentifier().setSystem("urn:sys").setValue("id1");
		src.addIdentifier().setSystem("urn:sys").setValue("id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(src);

		ourLog.info(enc);

		IParser parser = ourCtx.newJsonParser();
		Patient nonExt = parser.parseResource(Patient.class, enc);

		assertThat(nonExt.getClass()).isEqualTo(Patient.class);
		assertThat(nonExt.getIdentifier().get(0).getSystem()).isEqualTo("urn:sys");
		assertThat(nonExt.getIdentifier().get(0).getValue()).isEqualTo("id1");
		assertThat(nonExt.getIdentifier().get(1).getSystem()).isEqualTo("urn:sys");
		assertThat(nonExt.getIdentifier().get(1).getValue()).isEqualTo("id2");

		List<Extension> ext = nonExt.getExtensionsByUrl("urn:ext");
		assertThat(ext).hasSize(1);
		assertThat(ext.get(0).getUrl()).isEqualTo("urn:ext");
		assertThat(ext.get(0).getValueAsPrimitive().getClass()).isEqualTo(IntegerType.class);
		assertThat(ext.get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("100");

		List<Extension> modExt = nonExt.getExtensionsByUrl("urn:modExt");
		assertThat(modExt).hasSize(1);
		assertThat(modExt.get(0).getUrl()).isEqualTo("urn:modExt");
		assertThat(modExt.get(0).getValueAsPrimitive().getClass()).isEqualTo(IntegerType.class);
		assertThat(modExt.get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("200");

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertThat(va.getIdentifier().get(0).getSystem()).isEqualTo("urn:sys");
		assertThat(va.getIdentifier().get(0).getValue()).isEqualTo("id1");
		assertThat(va.getIdentifier().get(1).getSystem()).isEqualTo("urn:sys");
		assertThat(va.getIdentifier().get(1).getValue()).isEqualTo("id2");
		assertThat(va.getExt().getValue().intValue()).isEqualTo(100);
		assertThat(va.getModExt().getValue().intValue()).isEqualTo(200);

		assertThat(va.getExtension()).isEmpty();
	}

}
