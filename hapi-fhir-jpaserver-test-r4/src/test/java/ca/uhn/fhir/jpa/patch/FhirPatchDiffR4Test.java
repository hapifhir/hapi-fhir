package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.jpa.patch.FhirPatchApplyR4Test.extractPartValue;
import static ca.uhn.fhir.jpa.patch.FhirPatchApplyR4Test.extractPartValuePrimitive;
import static org.assertj.core.api.Assertions.assertThat;

public class FhirPatchDiffR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirPatchDiffR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Test
	public void testReplaceIdentifier() {
		Patient oldValue = new Patient();
		oldValue.addIdentifier().setSystem("system-0").setValue("value-0");

		Patient newValue = new Patient();
		newValue.addIdentifier().setSystem("system-1").setValue("value-1");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(2);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.identifier[0].system");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("system-1");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "path")).isEqualTo("Patient.identifier[0].value");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "value")).isEqualTo("value-1");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testReplaceChoice() {
		Patient oldValue = new Patient();
		oldValue.setDeceased(new BooleanType(true));

		Patient newValue = new Patient();
		newValue.setDeceased(new DateTimeType("2020-05-16"));


		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.deceased");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("2020-05-16");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testReplaceChoice2() {
		Patient oldValue = new Patient();
		oldValue.setDeceased(new DateTimeType("2020-05-16"));

		Patient newValue = new Patient();
		newValue.setDeceased(new BooleanType(true));


		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.deceased");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("true");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testAddExtensionOnPrimitive() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);

		Patient newValue = new Patient();
		newValue.setActive(true);
		newValue.getActiveElement().addExtension("http://foo", new StringType("a value"));

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active.extension");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "index")).isEqualTo("0");
		assertThat(extractPartValue(diff, 0, "operation", "value", Extension.class).getUrl()).isEqualTo("http://foo");
		assertThat(extractPartValue(diff, 0, "operation", "value", Extension.class).getValueAsPrimitive().getValueAsString()).isEqualTo("a value");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testRemoveExtensionOnPrimitive() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);
		oldValue.getActiveElement().addExtension("http://foo", new StringType("a value"));

		Patient newValue = new Patient();
		newValue.setActive(true);

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("delete");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active.extension[0]");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testModifyExtensionOnPrimitive() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);
		oldValue.getActiveElement().addExtension("http://foo", new StringType("a value"));

		Patient newValue = new Patient();
		newValue.setActive(true);
		newValue.getActiveElement().addExtension("http://foo", new StringType("a new value"));

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active.extension[0].value");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("a new value");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}


	@Test
	public void testAddExtensionOnComposite() {
		Patient oldValue = new Patient();
		oldValue.addName().setFamily("Family");

		Patient newValue = new Patient();
		newValue.addName().setFamily("Family");
		newValue.getNameFirstRep().addExtension("http://foo", new StringType("a value"));

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.name[0].extension");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "index")).isEqualTo("0");
		assertThat(extractPartValue(diff, 0, "operation", "value", Extension.class).getUrl()).isEqualTo("http://foo");
		assertThat(extractPartValue(diff, 0, "operation", "value", Extension.class).getValueAsPrimitive().getValueAsString()).isEqualTo("a value");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testRemoveExtensionOnComposite() {
		Patient oldValue = new Patient();
		oldValue.addName().setFamily("Family");
		oldValue.getNameFirstRep().addExtension("http://foo", new StringType("a value"));

		Patient newValue = new Patient();
		newValue.addName().setFamily("Family");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("delete");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.name[0].extension[0]");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testModifyExtensionOnComposite() {
		Patient oldValue = new Patient();
		oldValue.addName().setFamily("Family");
		oldValue.getNameFirstRep().addExtension("http://foo", new StringType("a value"));

		Patient newValue = new Patient();
		newValue.addName().setFamily("Family");
		newValue.getNameFirstRep().addExtension("http://foo", new StringType("a new value"));

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.name[0].extension[0].value");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("a new value");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testModifyId() {
		Patient oldValue = new Patient();
		oldValue.setId("http://foo/Patient/123/_history/2");
		oldValue.getMeta().setVersionId("2");
		oldValue.addName().setFamily("Family");

		Patient newValue = new Patient();
		newValue.setId("http://bar/Patient/456/_history/667");
		newValue.getMeta().setVersionId("667");
		newValue.addName().setFamily("Family");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(2);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.id");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("456");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "path")).isEqualTo("Patient.meta.versionId");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "value")).isEqualTo("667");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testModifyId_OnlyVersionDifferent() {
		Patient oldValue = new Patient();
		oldValue.setId("http://foo/Patient/123/_history/2");
		oldValue.getMeta().setVersionId("2");
		oldValue.addName().setFamily("Family");

		Patient newValue = new Patient();
		newValue.setId("http://foo/Patient/123/_history/3");
		newValue.getMeta().setVersionId("3");
		newValue.addName().setFamily("Family");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.meta.versionId");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("3");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testModifyNarrative() {
		Patient oldValue = new Patient();
		oldValue.getText().getDiv().setValue("<div>123</div>");
		oldValue.addName().setFamily("Family");

		Patient newValue = new Patient();
		newValue.getText().getDiv().setValue("<div>456</div>");
		newValue.addName().setFamily("Family");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.text.div");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">456</div>");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testInsertIdentifier() {
		Patient oldValue = new Patient();
		oldValue.addIdentifier().setSystem("system-0").setValue("value-0");

		Patient newValue = new Patient();
		newValue.addIdentifier().setSystem("system-0").setValue("value-0");
		newValue.addIdentifier().setSystem("system-1").setValue("value-1");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "index")).isEqualTo("1");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.identifier");
		assertThat(extractPartValue(diff, 0, "operation", "value", Identifier.class).getSystem()).isEqualTo("system-1");
		assertThat(extractPartValue(diff, 0, "operation", "value", Identifier.class).getValue()).isEqualTo("value-1");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testInsertContact() {
		Patient oldValue = new Patient();

		Patient newValue = new Patient();
		newValue.addContact().getName().setFamily("My Family");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(2);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.contact");
		assertThat(extractPartValue(diff, 0, "operation", "value", IBase.class)).isEqualTo(null);
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "path")).isEqualTo("Patient.contact[0].name");
		assertThat(extractPartValue(diff, 1, "operation", "value", HumanName.class).getFamily()).isEqualTo("My Family");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}


	@Test
	public void testIgnoreElementComposite_Resource() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);
		oldValue.getMeta().setSource("123");

		Patient newValue = new Patient();
		newValue.setActive(false);
		newValue.getMeta().setSource("456");

		FhirPatch svc = new FhirPatch(ourCtx);
		svc.addIgnorePath("Patient.meta");
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("false");
	}

	@Test
	public void testIgnoreElementComposite_Star() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);
		oldValue.getMeta().setSource("123");

		Patient newValue = new Patient();
		newValue.setActive(false);
		newValue.getMeta().setSource("456");

		FhirPatch svc = new FhirPatch(ourCtx);
		svc.addIgnorePath("*.meta");
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("false");
	}

	@Test
	public void testIgnoreElementPrimitive() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);
		oldValue.getMeta().setSource("123");

		Patient newValue = new Patient();
		newValue.setActive(false);
		newValue.getMeta().setSource("456");

		FhirPatch svc = new FhirPatch(ourCtx);
		svc.addIgnorePath("Patient.meta.source");
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("false");
	}

	@Test
	public void testIgnoreId() {
		Patient oldValue = new Patient();
		oldValue.setId("1");
		oldValue.setActive(true);

		Patient newValue = new Patient();
		newValue.setId("2");
		newValue.setActive(false);

		FhirPatch svc = new FhirPatch(ourCtx);
		svc.addIgnorePath("*.id");
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("replace");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.active");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "value")).isEqualTo("false");
	}

	@Test
	public void testDeleteIdentifier() {
		Patient oldValue = new Patient();
		oldValue.addIdentifier().setSystem("system-0").setValue("value-0");
		oldValue.addIdentifier().setSystem("system-1").setValue("value-1");

		Patient newValue = new Patient();
		newValue.addIdentifier().setSystem("system-0").setValue("value-0");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter().size()).isEqualTo(1);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("delete");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Patient.identifier[1]");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	public void validateDiffProducesSameResults(Patient theOldValue, Patient theNewValue, FhirPatch theSvc, Parameters theDiff) {
		theSvc.apply(theOldValue, theDiff);
		String expected = ourCtx.newJsonParser().encodeResourceToString(theNewValue);
		String actual = ourCtx.newJsonParser().encodeResourceToString(theOldValue);
		assertThat(actual).isEqualTo(expected);

		expected = ourCtx.newXmlParser().encodeResourceToString(theNewValue);
		actual = ourCtx.newXmlParser().encodeResourceToString(theOldValue);
		assertThat(actual).isEqualTo(expected);
	}


}
