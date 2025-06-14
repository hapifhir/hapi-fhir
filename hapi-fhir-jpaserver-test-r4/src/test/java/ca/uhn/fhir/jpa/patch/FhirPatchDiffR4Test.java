package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumeration;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

		assertThat(diff.getParameter()).hasSize(2);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.identifier[0].system", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("system-1", extractPartValuePrimitive(diff, 0, "operation", "value"));
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.identifier[0].value", extractPartValuePrimitive(diff, 1, "operation", "path"));
		assertEquals("value-1", extractPartValuePrimitive(diff, 1, "operation", "value"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testInsertCompositeImmediateChild_whenChildIsEnumerationDataType() {
		Encounter oldValue = new Encounter();

		Encounter newValue = new Encounter();
		newValue.getLocationFirstRep().setStatus(Encounter.EncounterLocationStatus.ACTIVE);

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(2);
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 0, "operation", "path")).isEqualTo("Encounter.location");

		assertThat(extractPartValue(diff, 0, "operation", "value", IBase.class)).isNull();

		assertThat(extractPartValuePrimitive(diff, 1, "operation", "type")).isEqualTo("insert");
		assertThat(extractPartValuePrimitive(diff, 1, "operation", "path")).isEqualTo("Encounter.location[0].status");
		String locationCode = extractPartValue(diff, 1, "operation", "value", Enumeration.class).getCode();
		assertThat(locationCode).isEqualTo("active");

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testReplaceCompositeChild_whenChildIsEnumerationDataType() {
		Encounter oldValue = new Encounter();
		oldValue.getLocationFirstRep().setStatus(Encounter.EncounterLocationStatus.RESERVED);

		Encounter newValue = new Encounter();
		newValue.getLocationFirstRep().setStatus(Encounter.EncounterLocationStatus.ACTIVE);

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.deceased", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("2020-05-16", extractPartValuePrimitive(diff, 0, "operation", "value"));

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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.deceased", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("true", extractPartValuePrimitive(diff, 0, "operation", "value"));

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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("insert", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active.extension", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("0", extractPartValuePrimitive(diff, 0, "operation", "index"));
		assertEquals("http://foo", extractPartValue(diff, 0, "operation", "value", Extension.class).getUrl());
		assertEquals("a value", extractPartValue(diff, 0, "operation", "value", Extension.class).getValueAsPrimitive().getValueAsString());

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
		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("delete", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active.extension[0]", extractPartValuePrimitive(diff, 0, "operation", "path"));

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
		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active.extension[0].value", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("a new value", extractPartValuePrimitive(diff, 0, "operation", "value"));

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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("insert", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.name[0].extension", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("0", extractPartValuePrimitive(diff, 0, "operation", "index"));
		assertEquals("http://foo", extractPartValue(diff, 0, "operation", "value", Extension.class).getUrl());
		assertEquals("a value", extractPartValue(diff, 0, "operation", "value", Extension.class).getValueAsPrimitive().getValueAsString());

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
		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("delete", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.name[0].extension[0]", extractPartValuePrimitive(diff, 0, "operation", "path"));

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
		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.name[0].extension[0].value", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("a new value", extractPartValuePrimitive(diff, 0, "operation", "value"));

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
		assertThat(diff.getParameter()).hasSize(2);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.id", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("456", extractPartValuePrimitive(diff, 0, "operation", "value"));
		assertEquals("replace", extractPartValuePrimitive(diff, 1, "operation", "type"));
		assertEquals("Patient.meta.versionId", extractPartValuePrimitive(diff, 1, "operation", "path"));
		assertEquals("667", extractPartValuePrimitive(diff, 1, "operation", "value"));

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
		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.meta.versionId", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("3", extractPartValuePrimitive(diff, 0, "operation", "value"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testModifyNarrative() {
		// Patient.text.div=123
		Patient oldValue = new Patient();
		oldValue.getText().getDiv().setValue("<div>123</div>");
		oldValue.addName().setFamily("Family");

		// Patient.text.div=456
		Patient newValue = new Patient();
		newValue.getText().getDiv().setValue("<div>456</div>");
		newValue.addName().setFamily("Family");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));
		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.text.div", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">456</div>", extractPartValuePrimitive(diff, 0, "operation", "value"));

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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("insert", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("1", extractPartValuePrimitive(diff, 0, "operation", "index"));
		assertEquals("Patient.identifier", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("system-1", extractPartValue(diff, 0, "operation", "value", Identifier.class).getSystem());
		assertEquals("value-1", extractPartValue(diff, 0, "operation", "value", Identifier.class).getValue());

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

		assertThat(diff.getParameter()).hasSize(2);
		assertEquals("insert", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.contact", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertNull(extractPartValue(diff, 0, "operation", "value", IBase.class));
		assertEquals("insert", extractPartValuePrimitive(diff, 1, "operation", "type"));
		assertEquals("Patient.contact[0].name", extractPartValuePrimitive(diff, 1, "operation", "path"));
		assertEquals("My Family", extractPartValue(diff, 1, "operation", "value", HumanName.class).getFamily());

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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("false", extractPartValuePrimitive(diff, 0, "operation", "value"));
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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("false", extractPartValuePrimitive(diff, 0, "operation", "value"));
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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("false", extractPartValuePrimitive(diff, 0, "operation", "value"));
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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("false", extractPartValuePrimitive(diff, 0, "operation", "value"));
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

		assertThat(diff.getParameter()).hasSize(1);
		assertEquals("delete", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.identifier[1]", extractPartValuePrimitive(diff, 0, "operation", "path"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	public void validateDiffProducesSameResults(IBaseResource theOldValue, IBaseResource theNewValue, FhirPatch theSvc, Parameters theDiff) {
		IParser iParser = ourCtx.newJsonParser();

		theSvc.apply(theOldValue, theDiff);
		String expected = iParser.encodeResourceToString(theNewValue);
		String actual = iParser.encodeResourceToString(theOldValue);
		assertEquals(expected, actual);

		expected = iParser.encodeResourceToString(theNewValue);
		actual = iParser.encodeResourceToString(theOldValue);
		assertEquals(expected, actual);
	}

}
