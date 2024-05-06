package ca.uhn.fhir.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.parser.PatientWithExtendedContactDstu3.CustomContactComponent;
import ca.uhn.fhir.parser.XmlParserDstu3Test.TestPatientFor327;
import ca.uhn.fhir.parser.json.BaseJsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.BaseJsonLikeValue.ValueType;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.Address.AddressUseEnumFactory;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.Basic;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus;
import org.hl7.fhir.dstu3.model.Coverage;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.EnumFactory;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.ExplanationOfBenefit;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Linkage;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedPerson;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.SampledData;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JsonParserDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserDstu3Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();

	@AfterEach
	public void after() {
		ourCtx.setNarrativeGenerator(null);
	}

	@Test
	public void testEncodedResourceWithIncorrectRepresentationOfDecimalTypeToJson() {
		DecimalType decimalType = new DecimalType();
		decimalType.setValueAsString(".5");
		MedicationRequest mr = new MedicationRequest();
		Dosage dosage = new Dosage();
		dosage.setDose(new SimpleQuantity()
			.setValue(decimalType.getValue())
			.setUnit("{tablet}")
			.setSystem("http://unitsofmeasure.org")
			.setCode("{tablet}"));
		mr.addDosageInstruction(dosage);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newJsonParser().parseResource(MedicationRequest.class, encoded);
		assertThat(mr.getDosageInstructionFirstRep().getDoseSimpleQuantity().getValue()).isEqualTo(BigDecimal.valueOf(0.5));
		assertThat(encoded).contains("0.5");
	}

	/**
	 * See #563
	 */
	@Test
	public void testBadMessageForUnknownElement() throws IOException {
		String input = ClasspathUtil.loadResource("/bad_parse_bundle_1.json");

		IParser p = ourCtx.newJsonParser();
		p.setParserErrorHandler(new StrictErrorHandler());
		try {
			p.parseResource(input);
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1861) + "Failed to parse JSON encoded FHIR content: Unexpected character ('=' (code 61)): was expecting a colon to separate field name and value\n" +
				" at [line: 4, column: 18]");
		}
	}

	/**
	 * See #563
	 */
	@Test
	public void testBadMessageForUnknownElement2() throws IOException {
		String input = ClasspathUtil.loadResource("/bad_parse_bundle_2.json");

		IParser p = ourCtx.newJsonParser();
		p.setParserErrorHandler(new StrictErrorHandler());
		try {
			p.parseResource(input);
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1820) + "Found incorrect type for element context - Expected OBJECT and found SCALAR (STRING)");
		}
	}

	@Test
	public void testBaseUrlFooResourceCorrectlySerializedInExtensionValueReference() {
		String refVal = "http://my.org/FooBar";

		Patient fhirPat = new Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new Reference(refVal));

		IParser parser = ourCtx.newJsonParser();

		String output = parser.encodeResourceToString(fhirPat);
		System.out.println("output: " + output);

		// Deserialize then check that valueReference value is still correct
		fhirPat = parser.parseResource(Patient.class, output);

		List<Extension> extensions = fhirPat.getExtensionsByUrl("x1");
		assertThat(extensions).hasSize(1);
		assertThat(((Reference) extensions.get(0).getValue()).getReference()).isEqualTo(refVal);
	}

	/**
	 * See #544
	 */
	@Test
	public void testBundleStitchReferencesByUuid() {
		Bundle bundle = new Bundle();

		DocumentManifest dm = new DocumentManifest();
		dm.getSubject().setReference("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");
		bundle.addEntry().setResource(dm);

		Patient patient = new Patient();
		patient.addName().setFamily("FAMILY");
		bundle.addEntry().setResource(patient).setFullUrl("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);

		bundle = ourCtx.newJsonParser().parseResource(Bundle.class, encoded);
		dm = (DocumentManifest) bundle.getEntry().get(0).getResource();

		assertThat(dm.getSubject().getReference()).isEqualTo("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");

		Patient subject = (Patient) dm.getSubject().getResource();
		assertNotNull(subject);
		assertThat(subject.getNameFirstRep().getFamily()).isEqualTo("FAMILY");
	}

	/**
	 * Test for the url generated based on the server config
	 */
	@Test
	public void testCustomUrlExtension() {
		final String expected = "{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"http://www.example.com/petname\",\"valueString\":\"myName\"}]}";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringType("myName"));

		final IParser jsonParser = ourCtx.newJsonParser();
		jsonParser.setServerBaseUrl("http://www.example.com");

		final String parsedPatient = jsonParser.encodeResourceToString(patient);
		System.out.println(parsedPatient);
		assertThat(parsedPatient).isEqualTo(expected);

		// Parse with string
		MyPatientWithCustomUrlExtension newPatient = jsonParser.parseResource(MyPatientWithCustomUrlExtension.class, parsedPatient);
		assertThat(newPatient.getPetName().getValue()).isEqualTo("myName");

		// Parse with stream
		newPatient = jsonParser.parseResource(MyPatientWithCustomUrlExtension.class, new StringReader(parsedPatient));
		assertThat(newPatient.getPetName().getValue()).isEqualTo("myName");

		// Check no NPE if base server not configure
		newPatient = ourCtx.newJsonParser().parseResource(MyPatientWithCustomUrlExtension.class, new StringReader(parsedPatient));
		assertNull(newPatient.getPetName().getValue());
		assertThat(((StringType) newPatient.getExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue()).isEqualTo("myName");
	}

	@Test
	public void testCustomUrlExtensionInBundle() {
		final String expected = "{\"resourceType\":\"Bundle\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"http://www.example.com/petname\",\"valueString\":\"myName\"}]}}]}";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringType("myName"));

		final Bundle bundle = new Bundle();
		final BundleEntryComponent entry = new BundleEntryComponent();
		entry.setResource(patient);
		bundle.addEntry(entry);

		final IParser jsonParser = ourCtx.newJsonParser();
		jsonParser.setServerBaseUrl("http://www.example.com");

		final String parsedBundle = jsonParser.encodeResourceToString(bundle);
		System.out.println(parsedBundle);
		assertThat(parsedBundle).isEqualTo(expected);

		// Parse with string
		Bundle newBundle = jsonParser.parseResource(Bundle.class, parsedBundle);
		assertNotNull(newBundle);
		assertThat(newBundle.getEntry()).hasSize(1);
		Patient newPatient = (Patient) newBundle.getEntry().get(0).getResource();
		assertThat(((StringType) newPatient.getExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue()).isEqualTo("myName");

		// Parse with stream
		newBundle = jsonParser.parseResource(Bundle.class, new StringReader(parsedBundle));
		assertNotNull(newBundle);
		assertThat(newBundle.getEntry()).hasSize(1);
		newPatient = (Patient) newBundle.getEntry().get(0).getResource();
		assertThat(((StringType) newPatient.getExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue()).isEqualTo("myName");

	}

	/**
	 * See #276
	 */
	@Test
	public void testDoubleEncodingContainedResources() {
		Patient patient = new Patient();
		patient.setId("#patient-1");
		patient.setActive(true);

		Coverage coverage = new Coverage();
		coverage.setId("#coverage-1");
		coverage.getBeneficiary().setResource(patient);

		Claim resource = new Claim();
		resource.getContained().add(patient);
		resource.getContained().add(coverage);
		resource.getPatient().setReference("#patient-1");
		resource.addInsurance().getCoverage().setReference("#coverage-1");

		IParser p = ourCtx.newJsonParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(resource);
		ourLog.info(encoded);

		assertThat(countMatches(encoded, "resourceType")).isEqualTo(3);
	}

	@Test
	public void testEncodeAndParseExtensions() {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		Extension ext = new Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeType("2011-01-02T11:13:15"));
		patient.addExtension(ext);

		Extension parent = new Extension().setUrl("http://example.com#parent");
		patient.addExtension(parent);
		Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child1);
		Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value2"));
		parent.addExtension(child2);

		Extension modExt = new Extension();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new DateType("1995-01-02"));
		patient.addModifierExtension(modExt);

		HumanName name = patient.addName();
		name.setFamily("Blah");
		StringType given = name.addGivenElement();
		given.setValue("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
		given.addExtension(ext2);

		StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
		given2.addExtension(given2ext);
		given2ext.addExtension(new Extension().setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD")));

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(enc).contains("{\"resourceType\":\"Patient\",", "\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
			 "{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}");
		assertThat(enc).contains("\"modifierExtension\":[" + "{" + "\"url\":\"http://example.com/extensions#modext\"," + "\"valueDate\":\"1995-01-02\"" + "}" + "],");
		assertThat(enc).contains("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{" + "\"extension\":[" + "{"
			+ "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_child\"," + "\"valueString\":\"CHILD\"" + "}" + "]" + "}"
			+ "]" + "}");

		/*
		 * Now parse this back
		 */

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		ext = parsed.getExtension().get(0);
		assertThat(ext.getUrl()).isEqualTo("http://example.com/extensions#someext");
		assertThat(((DateTimeType) ext.getValue()).getValueAsString()).isEqualTo("2011-01-02T11:13:15");

		parent = patient.getExtension().get(1);
		assertThat(parent.getUrl()).isEqualTo("http://example.com#parent");
		assertNull(parent.getValue());
		child1 = parent.getExtension().get(0);
		assertThat(child1.getUrl()).isEqualTo("http://example.com#child");
		assertThat(((StringType) child1.getValue()).getValueAsString()).isEqualTo("value1");
		child2 = parent.getExtension().get(1);
		assertThat(child2.getUrl()).isEqualTo("http://example.com#child");
		assertThat(((StringType) child2.getValue()).getValueAsString()).isEqualTo("value2");

		modExt = parsed.getModifierExtension().get(0);
		assertThat(modExt.getUrl()).isEqualTo("http://example.com/extensions#modext");
		assertThat(((DateType) modExt.getValue()).getValueAsString()).isEqualTo("1995-01-02");

		name = parsed.getName().get(0);

		ext2 = name.getGiven().get(0).getExtension().get(0);
		assertThat(ext2.getUrl()).isEqualTo("http://examples.com#givenext");
		assertThat(((StringType) ext2.getValue()).getValueAsString()).isEqualTo("given");

		given2ext = name.getGiven().get(1).getExtension().get(0);
		assertThat(given2ext.getUrl()).isEqualTo("http://examples.com#givenext_parent");
		assertNull(given2ext.getValue());
		Extension given2ext2 = given2ext.getExtension().get(0);
		assertThat(given2ext2.getUrl()).isEqualTo("http://examples.com#givenext_child");
		assertThat(((StringType) given2ext2.getValue()).getValue()).isEqualTo("CHILD");

	}

	/**
	 * See #402
	 */
	@Test
	public void testEncodeCompositionDoesntOverwriteNarrative() {
		FhirContext ctx = FhirContext.forDstu3();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		Composition composition  = new Composition();
		composition.getText().setDivAsString("<div>root</div>");
		composition.addSection().getText().setDivAsString("<div>section0</div>");
		composition.addSection().getText().setDivAsString("<div>section1</div>");

		String output = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(composition);
		ourLog.info(output);

		assertThat(output).contains("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">root</div>");
		assertThat(output).contains("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">section0</div>");
		assertThat(output).contains("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">section1</div>");

	}


	@Test
	public void testEncodeAndParseMetaProfileAndTags() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		p.getMeta().addProfile("http://foo/Profile1");
		p.getMeta().addProfile("http://foo/Profile2");

		p.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
		p.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");

		p.getMeta().addSecurity().setSystem("sec_scheme1").setCode("sec_term1").setDisplay("sec_label1");
		p.getMeta().addSecurity().setSystem("sec_scheme2").setCode("sec_term2").setDisplay("sec_label2");

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc).contains("\"meta\": {",
			"\"profile\": [",
			"\"http://foo/Profile1\",",
			"\"http://foo/Profile2\"",
			"],",
			"\"security\": [",
			"{",
			"\"system\": \"sec_scheme1\",",
			"\"code\": \"sec_term1\",",
			"\"display\": \"sec_label1\"",
			"},",
			"{",
			"\"system\": \"sec_scheme2\",",
			"\"code\": \"sec_term2\",",
			"\"display\": \"sec_label2\"",
			"}",
			"],",
			"\"tag\": [",
			"{",
			"\"system\": \"scheme1\",",
			"\"code\": \"term1\",",
			"\"display\": \"label1\"",
			"},",
			"{",
			"\"system\": \"scheme2\",",
			"\"code\": \"term2\",",
			"\"display\": \"label2\"",
			"}",
			"]",
			"},");
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);

		List<UriType> gotLabels = parsed.getMeta().getProfile();
		assertThat(gotLabels).hasSize(2);
		UriType label = gotLabels.get(0);
		assertThat(label.getValue()).isEqualTo("http://foo/Profile1");
		label = gotLabels.get(1);
		assertThat(label.getValue()).isEqualTo("http://foo/Profile2");

		List<Coding> tagList = parsed.getMeta().getTag();
		assertThat(tagList).hasSize(2);
		assertThat(tagList.get(0).getSystem()).isEqualTo("scheme1");
		assertThat(tagList.get(0).getCode()).isEqualTo("term1");
		assertThat(tagList.get(0).getDisplay()).isEqualTo("label1");
		assertThat(tagList.get(1).getSystem()).isEqualTo("scheme2");
		assertThat(tagList.get(1).getCode()).isEqualTo("term2");
		assertThat(tagList.get(1).getDisplay()).isEqualTo("label2");

		tagList = parsed.getMeta().getSecurity();
		assertThat(tagList).hasSize(2);
		assertThat(tagList.get(0).getSystem()).isEqualTo("sec_scheme1");
		assertThat(tagList.get(0).getCode()).isEqualTo("sec_term1");
		assertThat(tagList.get(0).getDisplay()).isEqualTo("sec_label1");
		assertThat(tagList.get(1).getSystem()).isEqualTo("sec_scheme2");
		assertThat(tagList.get(1).getCode()).isEqualTo("sec_term2");
		assertThat(tagList.get(1).getDisplay()).isEqualTo("sec_label2");
	}

	/**
	 * See #336
	 */
	@SuppressWarnings("SpellCheckingInspection")
	@Test
	public void testEncodeAndParseNullPrimitiveWithExtensions() {

		Patient p = new Patient();
		p.setId("patid");
		HumanName name = p.addName();
		name.addGivenElement().setValue(null).setId("f0").addExtension(new Extension("http://foo", new StringType("FOOEXT0")));
		name.addGivenElement().setValue("V1").setId("f1").addExtension((Extension) new Extension("http://foo", new StringType("FOOEXT1")).setId("ext1id"));
		name.addGivenElement(); // this one shouldn't get encoded
		name.addGivenElement().setValue(null).addExtension(new Extension("http://foo", new StringType("FOOEXT3")));
		name.setId("nameid");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		output = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(p);
		String expected = "{\"resourceType\":\"Patient\",\"id\":\"patid\",\"name\":[{\"id\":\"nameid\",\"given\":[null,\"V1\",null],\"_given\":[{\"id\":\"f0\",\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"FOOEXT0\"}]},{\"id\":\"f1\",\"extension\":[{\"id\":\"ext1id\",\"url\":\"http://foo\",\"valueString\":\"FOOEXT1\"}]},{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"FOOEXT3\"}]}]}]}";
		assertThat(output).isEqualTo(expected);

		p = ourCtx.newJsonParser().parseResource(Patient.class, output);
		assertThat(p.getIdElement().getIdPart()).isEqualTo("patid");

		name = p.getName().get(0);
		assertThat(name.getId()).isEqualTo("nameid");
		assertThat(name.getGiven()).hasSize(3);

		assertNull(name.getGiven().get(0).getValue());
		assertThat(name.getGiven().get(1).getValue()).isEqualTo("V1");
		assertNull(name.getGiven().get(2).getValue());

		assertThat(name.getGiven().get(0).getId()).isEqualTo("f0");
		assertThat(name.getGiven().get(1).getId()).isEqualTo("f1");
		assertNull(name.getGiven().get(2).getId());

		assertThat(name.getGiven().get(0).getExtension()).hasSize(1);
		assertThat(name.getGiven().get(0).getExtension().get(0).getUrl()).isEqualTo("http://foo");
		assertThat(((StringType) name.getGiven().get(0).getExtension().get(0).getValue()).getValue()).isEqualTo("FOOEXT0");
		assertNull(name.getGiven().get(0).getExtension().get(0).getId());

		assertThat(name.getGiven().get(1).getExtension()).hasSize(1);
		assertThat(name.getGiven().get(1).getExtension().get(0).getUrl()).isEqualTo("http://foo");
		assertThat(((StringType) name.getGiven().get(1).getExtension().get(0).getValue()).getValue()).isEqualTo("FOOEXT1");
		assertThat(name.getGiven().get(1).getExtension().get(0).getId()).isEqualTo("ext1id");

		assertThat(name.getGiven().get(2).getExtension()).hasSize(1);
		assertThat(name.getGiven().get(2).getExtension().get(0).getUrl()).isEqualTo("http://foo");
		assertThat(((StringType) name.getGiven().get(2).getExtension().get(0).getValue()).getValue()).isEqualTo("FOOEXT3");
		assertNull(name.getGiven().get(2).getExtension().get(0).getId());

	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		List<Coding> labels = new ArrayList<>();
		labels.add(new Coding().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setVersion("VERSION1"));
		labels.add(new Coding().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setVersion("VERSION2"));
		p.getMeta().getSecurity().addAll(labels);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		assertThat(enc.trim()).contains("{",
			 "  \"resourceType\": \"Patient\",",
			 "  \"meta\": {",
			 "    \"security\": [ {",
			 "      \"system\": \"SYSTEM1\",",
			 "      \"version\": \"VERSION1\",",
			 "      \"code\": \"CODE1\",",
			 "      \"display\": \"DISPLAY1\"",
			 "    }, {",
			 "      \"system\": \"SYSTEM2\",",
			 "      \"version\": \"VERSION2\",",
			 "      \"code\": \"CODE2\",",
			 "      \"display\": \"DISPLAY2\"",
			 "    } ]",
			 "  },",
			 "  \"name\": [ {",
			 "    \"family\": \"FAMILY\"",
			 "  } ]",
			 "}");

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		List<Coding> gotLabels = parsed.getMeta().getSecurity();

		assertThat(gotLabels).hasSize(2);

		Coding label = gotLabels.get(0);
		assertThat(label.getSystem()).isEqualTo("SYSTEM1");
		assertThat(label.getCode()).isEqualTo("CODE1");
		assertThat(label.getDisplay()).isEqualTo("DISPLAY1");
		assertThat(label.getVersion()).isEqualTo("VERSION1");

		label = gotLabels.get(1);
		assertThat(label.getSystem()).isEqualTo("SYSTEM2");
		assertThat(label.getCode()).isEqualTo("CODE2");
		assertThat(label.getDisplay()).isEqualTo("DISPLAY2");
		assertThat(label.getVersion()).isEqualTo("VERSION2");
	}

	@Test
	public void testEncodeBinaryWithSecurityContext() {
		Binary bin = new Binary();
		bin.setContentType("text/plain");
		bin.setContent("Now is the time".getBytes());
		Reference securityContext = new Reference();
		securityContext.setReference("DiagnosticReport/1");
		bin.setSecurityContext(securityContext);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(bin);
		ourLog.info(encoded);
		assertThat(encoded).contains("Binary");
		assertThat(encoded).contains("\"contentType\":\"text/plain\"");
		assertThat(encoded).contains("\"content\":\"Tm93IGlzIHRoZSB0aW1l\"");
		assertThat(encoded).contains("\"securityContext\":{\"reference\":\"DiagnosticReport/1\"}");
	}

	@Test
	public void testEncodeBundleNewBundleNoText() {

		Bundle b = new Bundle();

		BundleEntryComponent e = b.addEntry();
		e.setResource(new Patient());

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val).doesNotContain("text");

		val = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val).doesNotContain("text");

	}

	/**
	 * See #326
	 */
	@Test
	public void testEncodeContainedResource() {
		Patient patient = new Patient();
		patient.getBirthDateElement().setValueAsString("2016-04-05");
		patient.addExtension().setUrl("test").setValue(new Reference(new Condition()));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded).contains(
			"{",
			"\"resourceType\": \"Patient\",",
			"\"contained\": [",
			"{",
			"\"resourceType\": \"Condition\",",
			"\"id\": \"1\"",
			"}",
			"],",
			"\"extension\": [",
			"{",
			"\"url\": \"test\",",
			"\"valueReference\": {",
			"\"reference\": \"#1\"",
			"}",
			"}",
			"],",
			"\"birthDate\": \"2016-04-05\"",
			"}"
		);
		//@formatter:on
	}

	@Test
	public void testEncodeDoesntIncludeUuidId() {
		Patient p = new Patient();
		p.setId(new IdType("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
		p.addIdentifier().setSystem("ACME");

		String actual = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		assertThat(actual).doesNotContain("78ef6f64c2f2");
	}

	@Test
	public void testEncodeEmptyBinary() {
		String output = ourCtx.newJsonParser().encodeResourceToString(new Binary());
		assertThat(output).isEqualTo("{\"resourceType\":\"Binary\"}");
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag() {
		ArrayList<Coding> tagList = new ArrayList<>();
		tagList.add(new Coding());
		tagList.add(new Coding().setDisplay("Label"));

		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		assertThat(encoded).doesNotContain("tag");
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag2() {
		ArrayList<Coding> tagList = new ArrayList<>();
		tagList.add(new Coding().setSystem("scheme").setCode("code"));
		tagList.add(new Coding().setDisplay("Label"));

		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		assertThat(encoded).contains("tag");
		assertThat(encoded).contains("scheme");
		assertThat(encoded).doesNotContain("Label");
	}

	/**
	 * #480
	 */
	@Test
	public void testEncodeEmptyValue() {
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setId("123");
		qr.getAuthoredElement().setValueAsString("");
		qr.getItemFirstRep().setLinkIdElement(new StringType());
		qr.getItemFirstRep().addItem().setLinkIdElement(new StringType(""));
		qr.getItemFirstRep().addItem().setLinkIdElement(new StringType("LINKID"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(qr);
		ourLog.info(encoded);

		assertThat(encoded).contains("123");
		assertThat(encoded).isNotNull().doesNotContain("\"\"");
	}

	@Test
	public void testEncodeExtendedInfrastructureComponent() {
		IParser parser = ourCtx.newJsonParser();

		PatientWithExtendedContactDstu3 patient = new PatientWithExtendedContactDstu3();
		patient.setId("123");

		CustomContactComponent customContactComponent = new CustomContactComponent();
		customContactComponent.getEyeColour().setValue("EYE");
		customContactComponent.getName().setFamily("FAMILY");
		patient.getCustomContact().add(customContactComponent);

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);

		assertThat(val).isEqualTo("{\"resourceType\":\"Patient\",\"id\":\"123\",\"contact\":[{\"extension\":[{\"url\":\"http://foo.com/contact-eyecolour\",\"valueIdentifier\":{\"value\":\"EYE\"}}],\"name\":{\"family\":\"FAMILY\"}}]}");

		FhirContext newCtx = FhirContext.forDstu3();
		PatientWithExtendedContactDstu3 actual = newCtx.newJsonParser().parseResource(PatientWithExtendedContactDstu3.class, val);
		assertThat(actual.getCustomContact().get(0).getEyeColour().getValue()).isEqualTo("EYE");

	}

	@Test
	public void testEncodeExtensionInPrimitiveElement() {

		CapabilityStatement c = new CapabilityStatement();
		c.getAcceptUnknownElement().addExtension().setUrl("http://foo").setValue(new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertThat("{\"resourceType\":\"CapabilityStatement\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}").isEqualTo(encoded);

		// Now with a value
		ourLog.info("---------------");

		c = new CapabilityStatement();
		c.getAcceptUnknownElement().setValue(UnknownContentCode.ELEMENTS);
		c.getAcceptUnknownElement().addExtension().setUrl("http://foo").setValue(new StringType("AAA"));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertThat("{\"resourceType\":\"CapabilityStatement\",\"acceptUnknown\":\"elements\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}").isEqualTo(encoded);

	}

	@Test
	public void testEncodeExtensionOnRoot() {
		Patient p = new Patient();
		p.setId("Patient/B");
		p
			.addExtension()
			.setUrl("http://foo")
			.setValue(new Reference("Practitioner/A"));

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		parser.setDontEncodeElements(new HashSet<>(Arrays.asList("*.id", "*.meta")));

		String encoded = parser.encodeResourceToString(p);
		ourLog.info(encoded);

		assertThat(encoded).contains("http://foo");
		assertThat(encoded).contains("Practitioner/A");

		// Now with exclude

		parser = ourCtx.newJsonParser().setPrettyPrint(true);
		parser.setDontEncodeElements(new HashSet<>(Arrays.asList("*.id", "*.meta", "*.extension")));

		encoded = parser.encodeResourceToString(p);
		ourLog.info(encoded);

		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("Practitioner/A");


	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifier() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		obs.setStatus(ObservationStatus.FINAL);
		Extension ext = obs.addExtension();
		ext.setUrl("http://exturl").setValue(new StringType("ext_url_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newJsonParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		//@formatter:off
		assertThat(output).contains(
			"\"id\": \"1\"",
			"\"meta\"",
			"\"extension\"",
			"\"url\": \"http://exturl\"",
			"\"valueString\": \"ext_url_value\"",
			"\"code\":"
		);
		assertThat(output).doesNotContainPattern("\"url\": \"http://exturl\".*,.*\"url\": \"http://exturl\"");
		//@formatter:on

		obs = parser.parseResource(Observation.class, output);
		assertThat(obs.getExtension()).hasSize(1);
		assertThat(obs.getExtension().get(0).getUrl()).isEqualTo("http://exturl");
		assertThat(((StringType) obs.getExtension().get(0).getValue()).getValue()).isEqualTo("ext_url_value");
		assertThat(obs.getStatusElement().getValueAsString()).isEqualTo("final");
		assertThat(obs.getStatusElement().getValue()).isEqualTo(ObservationStatus.FINAL);

	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifierWithChildExtension() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		Extension ext = obs.addExtension();
		ext.setUrl("http://exturl");

		Extension subExt = ext.addExtension();
		subExt.setUrl("http://subext").setValue(new StringType("sub_ext_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newJsonParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		//@formatter:off
		assertThat(output).contains(
			"\"id\": \"1\"",
			"\"meta\"",
			"\"extension\"",
			"\"url\": \"http://exturl\"",
			"\"extension\"",
			"\"url\": \"http://subext\"",
			"\"valueString\": \"sub_ext_value\"",
			"\"code\":"
		);
		assertThat(output).doesNotContainPattern("\"url\": \"http://exturl\".*,.*\"url\": \"http://exturl\"");
		//@formatter:on

		obs = parser.parseResource(Observation.class, output);
		assertThat(obs.getExtension()).hasSize(1);
		assertThat(obs.getExtension().get(0).getUrl()).isEqualTo("http://exturl");
		assertThat(obs.getExtension().get(0).getExtension()).hasSize(1);
		assertThat(obs.getExtension().get(0).getExtension().get(0).getUrl()).isEqualTo("http://subext");
		assertThat(((StringType) obs.getExtension().get(0).getExtension().get(0).getValue()).getValue()).isEqualTo("sub_ext_value");
	}

	/**
	 * See #327
	 */
	@Test
	public void testEncodeExtensionWithContainedResource() {

		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDateElement(new DateType("2016-04-14"));

		List<Reference> conditions = new ArrayList<>();
		Condition condition = new Condition();
		condition.addBodySite().setText("BODY SITE");
		conditions.add(new Reference(condition));
		patient.setCondition(conditions);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded).contains(
			"\"resourceType\": \"Patient\"",
			"\"contained\": [",
			"\"resourceType\": \"Condition\"",
			"\"id\": \"1\"",
			"\"bodySite\": [",
			"\"text\": \"BODY SITE\"",
			"\"extension\": [",
			"\"url\": \"testCondition\",",
			"\"valueReference\": {",
			"\"reference\": \"#1\"",
			"\"birthDate\": \"2016-04-14\"",
			"}"
		);
		//@formatter:on
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath1() {
		ourCtx = FhirContext.forDstu3();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		parser.setDontStripVersionsFromReferencesAtPaths("Patient.managingOrganization");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath2() {
		ourCtx = FhirContext.forDstu3();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());
		assertTrue(ourCtx.getParserOptions().isStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		parser.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath3() {
		ourCtx = FhirContext.forDstu3();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.addEntity().setReference(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		parser.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");

		parser.setDontStripVersionsFromReferencesAtPaths(new ArrayList<>());
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");

		parser.setDontStripVersionsFromReferencesAtPaths((String[]) null);
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");

		parser.setDontStripVersionsFromReferencesAtPaths((List<String>) null);
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPathUsingOptions() {
		ourCtx = FhirContext.forDstu3();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());
		assertTrue(ourCtx.getParserOptions().isStripVersionsFromReferences());
		assertThat(ourCtx.getParserOptions().getDontStripVersionsFromReferencesAtPaths()).isEmpty();

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Patient.managingOrganization");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");

		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths(Collections.singletonList("Patient.managingOrganization"));
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");

		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths(new HashSet<>(Collections.singletonList("Patient.managingOrganization")));
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");
	}

	@Test
	public void testEncodeHistoryStripVersionsFromReferences() {
		ourCtx = FhirContext.forDstu3();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");

		parser.setStripVersionsFromReferences(false);
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");

		ourCtx = FhirContext.forDstu3();
	}

	@Test
	public void testEncodeHistoryStripVersionsFromReferencesFromContext() {
		ourCtx = FhirContext.forDstu3();

		assertTrue(ourCtx.getParserOptions().isStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");

		ourCtx.getParserOptions().setStripVersionsFromReferences(false);
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2/_history/1\"");

		parser.setStripVersionsFromReferences(true);
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc).contains("\"reference\": \"http://foo.com/Organization/2\"");

		ourCtx = FhirContext.forDstu3();
	}

	@Test
	public void testEncodeNarrativeShouldIncludeNamespace() {

		Patient p = new Patient();
		p.getText().setDivAsString("<div>VALUE</div>");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);
		assertThat(output).contains("\"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">VALUE</div>\"");
	}

	@Test
	public void testEncodeNarrativeShouldIncludeNamespaceWithProcessingInstruction() {

		Patient p = new Patient();
		p.getText().setDivAsString("<?xml version=\"1.0\" encoding=\"UTF-8\"?><div>VALUE</div>");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);
		assertThat(output).contains("\"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">VALUE</div>\"");
	}

	@Test
	public void testEncodeNarrativeSuppressed() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.getMaritalStatus().addCoding().setCode("D");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSuppressNarratives(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded).contains("Patient");
		assertThat(encoded).contains(ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3, ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE);
		assertThat(encoded).doesNotContain("text");
		assertThat(encoded).doesNotContain("THE DIV");
		assertThat(encoded).contains("family");
		assertThat(encoded).contains("maritalStatus");
	}

	@Test
	public void testEncodeParametersWithId() {
		Parameters reqParms = new Parameters();
		IdType patient = new IdType(1);
		reqParms.addParameter().setName("patient").setValue(patient);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(reqParms);
		ourLog.info(enc);

		assertThat(enc).contains("\"valueId\": \"1\"");
	}

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.addPhoto().setTitle("green");
		patient.getMaritalStatus().addCoding().setCode("D");

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded).contains("Patient");
		assertThat(encoded).contains("\"tag\"", "\"system\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\",", "\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"");
		assertThat(encoded).doesNotContain("THE DIV");
		assertThat(encoded).contains("family");
		assertThat(encoded).doesNotContain("maritalStatus");
	}

	/**
	 * We specifically include extensions on CapabilityStatment even in
	 * summary mode, since this is behaviour that people depend on
	 */
	@Test
	public void testEncodeSummaryCapabilityStatementExtensions() {

		CapabilityStatement cs = new CapabilityStatement();
		CapabilityStatement.CapabilityStatementRestComponent rest = cs.addRest();
		rest.setMode(CapabilityStatement.RestfulCapabilityMode.CLIENT);
		rest.getSecurity()
			.addExtension()
			.setUrl("http://foo")
			.setValue(new StringType("bar"));

		cs.getVersionElement().addExtension()
			.setUrl("http://goo")
			.setValue(new StringType("ber"));

		String encoded = ourCtx.newJsonParser().setSummaryMode(true).setPrettyPrint(true).setPrettyPrint(true).encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("http://foo");
		assertThat(encoded).contains("bar");
		assertThat(encoded).contains("http://goo");
		assertThat(encoded).contains("ber");
	}

	@Test
	public void testEncodeSummaryPatientExtensions() {

		Patient cs = new Patient();
		Address address = cs.addAddress();
		address.setCity("CITY");
		address
			.addExtension()
			.setUrl("http://foo")
			.setValue(new StringType("bar"));
		address.getCityElement().addExtension()
			.setUrl("http://goo")
			.setValue(new StringType("ber"));

		String encoded = ourCtx.newJsonParser().setSummaryMode(true).setPrettyPrint(true).setPrettyPrint(true).encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("bar");
		assertThat(encoded).doesNotContain("http://goo");
		assertThat(encoded).doesNotContain("ber");
	}

	@Test
	public void testEncodeSummary2() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.getMaritalStatus().addCoding().setCode("D");

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded).contains("Patient");
		assertThat(encoded).contains("\"tag\"", "\"system\": \"foo\",", "\"code\": \"bar\"", "\"system\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"",
			 "\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"");
		assertThat(encoded).doesNotContain("THE DIV");
		assertThat(encoded).contains("family");
		assertThat(encoded).doesNotContain("maritalStatus");
	}

	/**
	 * See #205
	 */
	@Test
	public void testEncodeTags() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("sys").setValue("val");

		pt.getMeta().addTag().setSystem("scheme").setCode("term").setDisplay("display");

		String enc = ourCtx.newJsonParser().encodeResourceToString(pt);
		ourLog.info(enc);

		assertThat(enc).isEqualTo("{\"resourceType\":\"Patient\",\"meta\":{\"tag\":[{\"system\":\"scheme\",\"code\":\"term\",\"display\":\"display\"}]},\"identifier\":[{\"system\":\"sys\",\"value\":\"val\"}]}");

	}

	/**
	 * See #241
	 */
	@Test
	public void testEncodeThenParseShouldNotAddSpuriousId() {
		Condition condition = new Condition().setVerificationStatus(ConditionVerificationStatus.CONFIRMED);
		Bundle bundle = new Bundle();
		BundleEntryComponent entry = new Bundle.BundleEntryComponent();
		entry.setId("123");
		entry.setResource(condition);
		bundle.getEntry().add(entry);
		IParser parser = ourCtx.newJsonParser();
		String json = parser.encodeResourceToString(bundle);
		ourLog.info(json);
		bundle = (Bundle) parser.parseResource(json);

		assertThat(bundle.getEntry().get(0).getId()).isEqualTo("123");

		condition = (Condition) bundle.getEntry().get(0).getResource();
		assertNull(condition.getId());
	}

	@Test
	public void testEncodeUndeclaredBlock() {
		FooMessageHeader.FooMessageSourceComponent source = new FooMessageHeader.FooMessageSourceComponent();
		source.getMessageHeaderApplicationId().setValue("APPID");
		source.setName("NAME");

		FooMessageHeader header = new FooMessageHeader();
		header.setSource(source);

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(header);

		IParser p = ourCtx.newJsonParser();
		p.setPrettyPrint(true);

		String encode = p.encodeResourceToString(bundle);
		ourLog.info(encode);

		assertThat(encode).contains("\"value\": \"APPID\"");
	}

	@Test
	public void testEncodeUndeclaredExtensionWithEnumerationContent() {
		IParser parser = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		EnumFactory<AddressUse> fact = new AddressUseEnumFactory();
		PrimitiveType<AddressUse> enumeration = new Enumeration<>(fact).setValue(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(enumeration);

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val).contains("\"extension\":[{\"url\":\"urn:foo\",\"valueCode\":\"home\"}]");

		MyPatientWithOneDeclaredEnumerationExtensionDstu3 actual = parser.parseResource(MyPatientWithOneDeclaredEnumerationExtensionDstu3.class, val);
		assertThat(patient.getAddress().get(0).getUse()).isEqualTo(AddressUse.HOME);
		Enumeration<AddressUse> ref = actual.getFoo();
		assertThat(ref.getValue().toCode()).isEqualTo("home");

	}

	@Test
	public void testEncodeWithDontEncodeElements() {
		Patient patient = new Patient();
		patient.setId("123");

		patient.getMeta().addProfile(("http://profile"));
		patient.addName().setFamily("FAMILY").addGiven("GIVEN");
		patient.addAddress().addLine("LINE1");

		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("*.meta", "*.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out).contains("Patient");
			assertThat(out).contains("name");
			assertThat(out).contains("address");
			assertThat(out).doesNotContain("id");
			assertThat(out).doesNotContain("meta");
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.meta", "Patient.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out).contains("Patient");
			assertThat(out).contains("name");
			assertThat(out).contains("address");
			assertThat(out).doesNotContain("id");
			assertThat(out).doesNotContain("meta");
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.name.family"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out).contains("GIVEN");
			assertThat(out).doesNotContain("FAMILY");
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("*.meta", "*.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out).contains("Patient");
			assertThat(out).contains("name");
			assertThat(out).contains("address");
			assertThat(out).doesNotContain("id");
			assertThat(out).doesNotContain("meta");
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.meta"));
			p.setEncodeElements(new HashSet<>(Collections.singletonList("Patient.name")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out).contains("Patient");
			assertThat(out).contains("name");
			assertThat(out).contains("id");
			assertThat(out).doesNotContain("address");
			assertThat(out).doesNotContain("meta");
			assertThat(out).doesNotContain("SUBSETTED");
		}
	}

	@Test
	public void testEncodeWithNarrative() {
		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");

		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		String output = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(output);

		assertThat(output).contains("\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><div class=\\\"hapiHeaderText\\\">John <b>SMITH </b></div>");
	}

	@Test
	public void testEncodingNullExtension() {
		Patient p = new Patient();
		Extension extension = new Extension("http://foo#bar");
		p.addExtension(extension);
		String str = ourCtx.newJsonParser().encodeResourceToString(p);

		assertThat(str).isEqualTo("{\"resourceType\":\"Patient\"}");

		extension.setValue(new StringType());

		str = ourCtx.newJsonParser().encodeResourceToString(p);
		assertThat(str).isEqualTo("{\"resourceType\":\"Patient\"}");

		extension.setValue(new StringType(""));

		str = ourCtx.newJsonParser().encodeResourceToString(p);
		assertThat(str).isEqualTo("{\"resourceType\":\"Patient\"}");

	}

	/**
	 * See #341
	 */
	@Test
	public void testExplanationOfBenefit() {
		//@formatter:off
		String input = "{" +
			"  \"resourceType\": \"ExplanationOfBenefit\"," +
			"  \"insurance\": {\n" +
			"    \"coverage\": {\n" +
			"      \"reference\": \"Coverage/123\"\n" +
			"    }\n" +
			"  },\n" +
			"  \"relationship\": {\n" +
			"    \"system\": \"http://hl7.org/fhir/relationship\",\n" +
			"    \"code\": \"1\",\n" +
			"    \"display\": \"self\"\n" +
			"  }\n" +
			"}";
		//@formatter:on

		ExplanationOfBenefit eob = ourCtx.newJsonParser().parseResource(ExplanationOfBenefit.class, input);
		assertThat(eob.getInsurance().getCoverage().getClass()).isEqualTo(Reference.class);

		Reference coverage = eob.getInsurance().getCoverage();
		assertThat(coverage.getReference()).isEqualTo("Coverage/123");
	}

	@Test
	public void testExponentDoesntGetEncodedAsSuch() {
		Observation obs = new Observation();
		obs.setValue(new Quantity().setValue(new BigDecimal("0.000000000000000100")));

		String str = ourCtx.newJsonParser().encodeResourceToString(obs);
		ourLog.info(str);

		assertThat(str).isEqualTo("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.000000000000000100}}");
	}

	@Test
	public void testExponentParseWorks() {
		String input = "{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.0000000000000001}}";
		Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, input);

		DecimalType valueElement = ((Quantity) obs.getValue()).getValueElement();
		assertThat(valueElement.getValueAsString()).isEqualTo("0.0000000000000001");

		String str = ourCtx.newJsonParser().encodeResourceToString(obs);
		ourLog.info(str);
		assertThat(str).isEqualTo("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.0000000000000001}}");
	}

	/**
	 * See #658
	 */
	@Test
	public void testExtraElement() throws Exception {
		IParser p = ourCtx.newJsonParser();
		p.setParserErrorHandler(new StrictErrorHandler());
		try {
			p.parseResource(IOUtils.toString(JsonParserDstu3Test.class.getResourceAsStream("/Patient.json.txt"), Charsets.UTF_8));
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1820) + "Found incorrect type for element assigner - Expected OBJECT and found SCALAR (STRING)");
		}

	}

	@Test
	public void testIncorrectJsonTypesIdAndArray() {

		// ID should be a String and communication should be an Array
		String input = "{\"resourceType\": \"Patient\",\n" +
			"  \"id\": 123,\n" +
			"  \"communication\": {\n" +
			"    \"language\": {\n" +
			"      \"text\": \"Hindi\"\n" +
			"    },\n" +
			"    \"preferred\": true\n" +
			"  }\n" +
			"}";

		IParser p = ourCtx.newJsonParser();

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
		p.setParserErrorHandler(errorHandler);
		Patient patient = (Patient) p.parseResource(input);

		ArgumentCaptor<String> elementName = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<ValueType> found = ArgumentCaptor.forClass(ValueType.class);
		ArgumentCaptor<ValueType> expected = ArgumentCaptor.forClass(ValueType.class);
		ArgumentCaptor<ScalarType> expectedScalarType = ArgumentCaptor.forClass(ScalarType.class);
		ArgumentCaptor<ScalarType> foundScalarType = ArgumentCaptor.forClass(ScalarType.class);
		verify(errorHandler, times(2)).incorrectJsonType(nullable(IParseLocation.class), elementName.capture(), expected.capture(), expectedScalarType.capture(), found.capture(), foundScalarType.capture());

		assertThat(found.getAllValues().get(0)).isEqualTo(ValueType.SCALAR);
		assertThat(expected.getAllValues().get(0)).isEqualTo(ValueType.SCALAR);
		assertThat(foundScalarType.getAllValues().get(0)).isEqualTo(ScalarType.NUMBER);
		assertThat(expectedScalarType.getAllValues().get(0)).isEqualTo(ScalarType.STRING);

		assertThat(found.getAllValues().get(1)).isEqualTo(ValueType.OBJECT);
		assertThat(expected.getAllValues().get(1)).isEqualTo(ValueType.ARRAY);
		assertNull(foundScalarType.getAllValues().get(1));
		assertNull(expectedScalarType.getAllValues().get(1));

		assertThat(patient.getIdElement().getIdPart()).isEqualTo("123");
		assertThat(patient.getCommunicationFirstRep().getLanguage().getText()).isEqualTo("Hindi");
	}

	@Test
	public void testIncorrectJsonTypesNone() {

		// ID should be a String and communication should be an Array
		String input = "{\"resourceType\": \"Patient\",\n" +
			"  \"id\": \"123\",\n" +
			"  \"communication\": [{\n" +
			"    \"language\": {\n" +
			"      \"text\": \"Hindi\"\n" +
			"    },\n" +
			"    \"preferred\": true\n" +
			"  }]\n" +
			"}";

		IParser p = ourCtx.newJsonParser();

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
		p.setParserErrorHandler(errorHandler);
		Patient patient = (Patient) p.parseResource(input);

		ArgumentCaptor<String> elementName = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<ValueType> found = ArgumentCaptor.forClass(ValueType.class);
		ArgumentCaptor<ValueType> expected = ArgumentCaptor.forClass(ValueType.class);
		ArgumentCaptor<ScalarType> expectedScalarType = ArgumentCaptor.forClass(ScalarType.class);
		ArgumentCaptor<ScalarType> foundScalarType = ArgumentCaptor.forClass(ScalarType.class);
		verify(errorHandler, times(0)).incorrectJsonType(any(IParseLocation.class), elementName.capture(), expected.capture(), expectedScalarType.capture(), found.capture(), foundScalarType.capture());

		assertThat(patient.getIdElement().getIdPart()).isEqualTo("123");
		assertThat(patient.getCommunicationFirstRep().getLanguage().getText()).isEqualTo("Hindi");
	}

	@Test
	public void testInvalidDateTimeValueInvalid() {
		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		String res = "{ \"resourceType\": \"Observation\", \"valueDateTime\": \"foo\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(errorHandler);
		Observation parsed = parser.parseResource(Observation.class, res);

		assertNull(parsed.getValueDateTimeType().getValue());
		assertThat(parsed.getValueDateTimeType().getValueAsString()).isEqualTo("foo");

		ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).invalidValue(any(), eq("foo"), msgCaptor.capture());
		assertThat(msgCaptor.getValue()).isEqualTo("Invalid date/time format: \"foo\"");

		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		assertThat(encoded).isEqualTo("{\"resourceType\":\"Observation\",\"valueDateTime\":\"foo\"}");
	}

	/**
	 * #516
	 */
	@Test
	public void testInvalidEnumValue() {
		try {
			String res = "{ \"resourceType\": \"ValueSet\", \"url\": \"http://sample/ValueSet/education-levels\", \"version\": \"1\", \"name\": \"Education Levels\", \"status\": \"draft\", \"compose\": { \"include\": [ { \"filter\": [ { \"property\": \"n\", \"op\": \"n\", \"value\": \"365460000\" } ], \"system\": \"http://snomed.info/sct\" } ], \"exclude\": [ { \"concept\": [ { \"code\": \"224298008\" }, { \"code\": \"365460000\" }, { \"code\": \"473462005\" }, { \"code\": \"424587006\" } ], \"system\": \"http://snomed.info/sct\" } ] }, \"description\": \"A selection of Education Levels\", \"text\": { \"status\": \"generated\", \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><h2>Education Levels</h2><tt>http://csiro.au/ValueSet/education-levels</tt><p>A selection of Education Levels</p></div>\" }, \"experimental\": true, \"date\": \"2016-07-26\" }";
			IParser parser = ourCtx.newJsonParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(ValueSet.class, res);
			fail("DataFormat Invalid attribute exception should be thrown");
		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1821) + "[element=\"op\"] Invalid attribute value \"n\": Unknown FilterOperator code 'n'");
		}
	}

	@Test
	public void testInvalidEnumValueBlank() {
		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		String res = "{ \"resourceType\": \"Patient\", \"gender\": \"\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(errorHandler);
		Patient parsed = parser.parseResource(Patient.class, res);

		assertNull(parsed.getGenderElement().getValue());
		assertNull(parsed.getGenderElement().getValueAsString());

		ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).invalidValue(any(), eq(""), msgCaptor.capture());
		assertThat(msgCaptor.getValue()).isEqualTo("Attribute value must not be empty (\"\")");

		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		assertThat(encoded).isEqualTo("{\"resourceType\":\"Patient\"}");
	}

	@Test
	public void testInvalidEnumValueInvalid() {
		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		String res = "{ \"resourceType\": \"Patient\", \"gender\": \"foo\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(errorHandler);
		Patient parsed = parser.parseResource(Patient.class, res);

		assertNull(parsed.getGenderElement().getValue());
		assertThat(parsed.getGenderElement().getValueAsString()).isEqualTo("foo");

		ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).invalidValue(any(), eq("foo"), msgCaptor.capture());
		assertThat(msgCaptor.getValue()).isEqualTo("Unknown AdministrativeGender code 'foo'");

		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		assertThat(encoded).isEqualTo("{\"resourceType\":\"Patient\",\"gender\":\"foo\"}");
	}

	/**
	 * #65
	 */
	@Test
	public void testJsonPrimitiveWithExtensionEncoding() {

		QuestionnaireResponse parsed = new QuestionnaireResponse();
		parsed.addItem().setLinkId("value123");
		parsed.getItem().get(0).getLinkIdElement().addExtension(new Extension("http://123", new StringType("HELLO")));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded).contains("{\"linkId\":\"value123\",\"_linkId\":{\"extension\":[{\"url\":\"http://123\",\"valueString\":\"HELLO\"}]}}");

	}

	@Test
	public void testLinkage() {
		Linkage l = new Linkage();
		l.addItem().getResource().setDisplay("FOO");
		String out = ourCtx.newXmlParser().encodeResourceToString(l);
		ourLog.info(out);
		assertThat(out).isEqualTo("<Linkage xmlns=\"http://hl7.org/fhir\"><item><resource><display value=\"FOO\"/></resource></item></Linkage>");
	}

	@Test
	public void testOmitResourceId() {
		Patient p = new Patient();
		p.setId("123");
		p.addName().setFamily("ABC");

		assertThat(ourCtx.newJsonParser().encodeResourceToString(p)).contains("123", "ABC");
		assertThat(ourCtx.newJsonParser().setOmitResourceId(true).encodeResourceToString(p)).contains("ABC");
		assertThat(ourCtx.newJsonParser().setOmitResourceId(true).encodeResourceToString(p)).doesNotContain("123");
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnFhirContext() {
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			ourCtx.getParserOptions().setOverrideResourceIdWithBundleEntryFullUrl(false);
			Bundle bundle = (Bundle) ourCtx.newJsonParser().parseResource(tmp);
			assertThat(bundle.getEntry()).hasSize(1);
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertFalse(o1Id.hasBaseUrl());
				assertThat(o1Id.getResourceType()).isEqualTo("Patient");
				assertThat(o1Id.getIdPart()).isEqualTo("patxuzos");
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = FhirContext.forDstu3();
		}
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnParser() {
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			Bundle bundle = (Bundle) ourCtx.newJsonParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
			assertThat(bundle.getEntry()).hasSize(1);
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertFalse(o1Id.hasBaseUrl());
				assertThat(o1Id.getResourceType()).isEqualTo("Patient");
				assertThat(o1Id.getIdPart()).isEqualTo("patxuzos");
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = FhirContext.forDstu3();
		}
	}

	@Test
	@Disabled
	public void testParseAndEncodeBundle() throws Exception {
		String content = ClasspathUtil.loadResource("/bundle-example.json");

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);
		assertThat(parsed.getIdElement().getValue()).isEqualTo("Bundle/example/_history/1");
		assertThat(parsed.getMeta().getVersionId()).isEqualTo("1");
		assertThat(parsed.getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(parsed.getMeta().getLastUpdatedElement().getValueAsString()).isEqualTo(("2014-08-18T01:43:30Z"));
		assertThat(parsed.getType().toCode()).isEqualTo("searchset");
		assertThat(parsed.getTotal()).isEqualTo(3);
		assertThat(parsed.getLink("next").getUrl()).isEqualTo("https://example.com/base/MedicationRequest?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2");
		assertThat(parsed.getLink("self").getUrl()).isEqualTo("https://example.com/base/MedicationRequest?patient=347&_include=MedicationRequest.medication");

		assertThat(parsed.getEntry()).hasSize(2);
		assertThat(parsed.getEntry().get(0).getLink("search").getUrl()).isEqualTo("http://foo?search");

		assertThat(parsed.getEntry().get(0).getLink("alternate").getUrl()).isEqualTo("http://example.com/base/MedicationRequest/3123/_history/1");
		MedicationRequest p = (MedicationRequest) parsed.getEntry().get(0).getResource();
		assertThat(p.getSubject().getReference()).isEqualTo("Patient/347");
		assertThat(p.getMeta().getLastUpdatedElement().getValueAsString()).isEqualTo("2014-08-16T05:31:17Z");
		assertThat(p.getId()).isEqualTo("http://example.com/base/MedicationRequest/3123/_history/1");

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertThat(m.getId()).isEqualTo("http://example.com/base/Medication/example");
		assertThat(m).isSameAs(((Reference) p.getMedication()).getResource());

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertThat(act).isEqualTo(exp);

	}

	/**
	 * Test for #146
	 */
	@Test
	@Disabled
	public void testParseAndEncodeBundleFromXmlToJson() throws Exception {
		String content = ClasspathUtil.loadResource("/bundle-example2.xml");

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);

		MedicationRequest p = (MedicationRequest) parsed.getEntry().get(0).getResource();
		assertThat(((Reference) p.getMedication()).getReference()).isEqualTo("#med");

		Medication m = (Medication) ((Reference) p.getMedication()).getResource();
		assertNotNull(m);
		assertThat(m.getIdElement().getValue()).isEqualTo("#med");
		assertThat(p.getContained()).hasSize(1);
		assertThat(p.getContained().get(0)).isSameAs(m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded).contains("contained");

		reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded).contains("contained");
	}

	@Test
	public void testParseAndEncodeBundleWithUuidBase() {
		//@formatter:off
		String input =
			"{\n" +
				"    \"resourceType\":\"Bundle\",\n" +
				"    \"type\":\"document\",\n" +
				"    \"entry\":[\n" +
				"        {\n" +
				"            \"fullUrl\":\"urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57\",\n" +
				"            \"resource\":{\n" +
				"                \"resourceType\":\"Composition\",\n" +
				"                \"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\",\n" +
				"                \"meta\":{\n" +
				"                    \"lastUpdated\":\"2013-05-28T22:12:21Z\"\n" +
				"                },\n" +
				"                \"text\":{\n" +
				"                    \"status\":\"generated\",\n" +
				"                    \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: 180f219f-97a8-486d-99d9-ed631fe4fc57</p><p><b>meta</b>: </p><p><b>date</b>: Feb 1, 2013 12:30:02 PM</p><p><b>type</b>: Discharge Summary from Responsible Clinician <span>(Details : {LOINC code '28655-9' = 'Physician attending Discharge summary)</span></p><p><b>status</b>: final</p><p><b>confidentiality</b>: N</p><p><b>author</b>: <a>Doctor Dave. Generated Summary: 23; Adam Careful </a></p><p><b>encounter</b>: <a>http://fhir.healthintersections.com.au/open/Encounter/doc-example</a></p></div>\"\n" +
				"                },\n" +
				"                \"date\":\"2013-02-01T12:30:02Z\",\n" +
				"                \"type\":{\n" +
				"                    \"coding\":[\n" +
				"                        {\n" +
				"                            \"system\":\"http://loinc.org\",\n" +
				"                            \"code\":\"28655-9\"\n" +
				"                        }\n" +
				"                    ],\n" +
				"                    \"text\":\"Discharge Summary from Responsible Clinician\"\n" +
				"                },\n" +
				"                \"status\":\"final\",\n" +
				"                \"confidentiality\":\"N\",\n" +
				"                \"subject\":{\n" +
				"                    \"reference\":\"http://fhir.healthintersections.com.au/open/Patient/d1\",\n" +
				"                    \"display\":\"Eve Everywoman\"\n" +
				"                },\n" +
				"                \"author\":[\n" +
				"                    {\n" +
				"                        \"reference\":\"Practitioner/example\",\n" +
				"                        \"display\":\"Doctor Dave\"\n" +
				"                    }\n" +
				"                ],\n" +
				"                \"encounter\":{\n" +
				"                    \"reference\":\"http://fhir.healthintersections.com.au/open/Encounter/doc-example\"\n" +
				"                },\n" +
				"                \"section\":[\n" +
				"                    {\n" +
				"                        \"title\":\"Reason for admission\",\n" +
				"                        \"content\":{\n" +
				"                            \"reference\":\"urn:uuid:d0dd51d3-3ab2-4c84-b697-a630c3e40e7a\"\n" +
				"                        }\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"title\":\"Medications on Discharge\",\n" +
				"                        \"content\":{\n" +
				"                            \"reference\":\"urn:uuid:673f8db5-0ffd-4395-9657-6da00420bbc1\"\n" +
				"                        }\n" +
				"                    },\n" +
				"                    {\n" +
				"                        \"title\":\"Known allergies\",\n" +
				"                        \"content\":{\n" +
				"                            \"reference\":\"urn:uuid:68f86194-e6e1-4f65-b64a-5314256f8d7b\"\n" +
				"                        }\n" +
				"                    }\n" +
				"                ]\n" +
				"            }\n" +
				"        }" +
				"    ]" +
				"}";
		//@formatter:on

		Bundle parsed = ourCtx.newJsonParser().parseResource(Bundle.class, input);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);

		assertThat(parsed.getEntry().get(0).getResource().getIdElement().getValue()).isEqualTo("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertNull(parsed.getEntry().get(0).getResource().getIdElement().getBaseUrl());
		assertThat(parsed.getEntry().get(0).getResource().getIdElement().getIdPart()).isEqualTo("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertThat(encoded).doesNotContain("\"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\"");
	}

	@Test
	public void testParseAndEncodeComments() {
		//@formatter:off
		String input = "{\n" +
			"  \"resourceType\": \"Patient\",\n" +
			"  \"id\": \"pat1\",\n" +
			"  \"text\": {\n" +
			"    \"status\": \"generated\",\n" +
			"    \"div\": \"<div>\\n      \\n      <p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p>\\n    \\n    </div>\"\n" +
			"  },\n" +
			"  \"identifier\": [\n" +
			"    {\n" +
			"      \"fhir_comments\":[\"identifier comment 1\",\"identifier comment 2\"],\n" +
			"      \"use\": \"usual\",\n" +
			"      \"_use\": {\n" +
			"        \"fhir_comments\":[\"use comment 1\",\"use comment 2\"]\n" +
			"      },\n" +
			"      \"type\": {\n" +
			"        \"coding\": [\n" +
			"          {\n" +
			"            \"system\": \"http://hl7.org/fhir/v2/0203\",\n" +
			"            \"code\": \"MR\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"system\": \"urn:oid:0.1.2.3.4.5.6.7\",\n" +
			"      \"value\": \"654321\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"active\": true" +
			"}";
		//@formatter:off

		Patient res = ourCtx.newJsonParser().parseResource(Patient.class, input);
		res.getFormatCommentsPre();
		assertThat(res.getId()).isEqualTo("Patient/pat1");
		assertThat(res.getIdentifier().get(0).getValue()).isEqualTo("654321");
		assertThat(res.getActive()).isEqualTo(true);

		assertThat(res.getIdentifier().get(0).getFormatCommentsPre()).doesNotContain("identifier comment 1", "identifier comment 2");
		assertThat(res.getIdentifier().get(0).getUseElement().getFormatCommentsPre()).doesNotContain("use comment 1", "use comment 2");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

		assertThat(encoded).doesNotContain("use comment 1");
		assertThat(encoded).doesNotContain("use comment 2");
		assertThat(encoded).doesNotContain("identifier comment 1");
		assertThat(encoded).doesNotContain("identifier comment 2");
	}

	@Test
	public void testParseBundleWithBinary() {
		Binary patient = new Binary();
		patient.setId(new IdType("http://base/Binary/11/_history/22"));
		patient.setContentType("foo");
		patient.setContent(new byte[]{1, 2, 3, 4});

		String val = ourCtx.newJsonParser().encodeResourceToString(patient);

		String expected = "{\"resourceType\":\"Binary\",\"id\":\"11\",\"meta\":{\"versionId\":\"22\"},\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}";
		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", val);
		assertThat(val).isEqualTo(expected);
	}

	/**
	 * See #399
	 */
	@Test
	public void testParseCommunicationWithThreeTypes() throws IOException {
		String content = ClasspathUtil.loadResource("/tara-test.json");
		Communication comm = ourCtx.newJsonParser().parseResource(Communication.class, content);

		assertThat(comm.getPayload()).hasSize(3);
		assertThat(comm.getPayload().get(0).getContent().getClass()).isEqualTo(Attachment.class);
		assertThat(comm.getPayload().get(1).getContent().getClass()).isEqualTo(Reference.class);
		assertThat(comm.getPayload().get(2).getContent().getClass()).isEqualTo(StringType.class);
	}

	/**
	 * see #144 and #146
	 */
	@Test
	@Disabled
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu2();
		IParser parser = c.newJsonParser().setPrettyPrint(true);

		Observation o = new Observation();
		o.getCode().setText("obs text");

		Patient p = new Patient();
		p.addName().setFamily("patient family");
		o.getSubject().setResource(p);

		String enc = parser.encodeResourceToString(o);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc).contains(
			"\"resourceType\":\"Observation\"",
			"\"contained\":[",
			"\"resourceType\":\"Patient\",",
			"\"id\":\"1\"",
			"\"reference\":\"#1\""
		);
		//@formatter:on

		o = parser.parseResource(Observation.class, enc);
		assertThat(o.getCode().getText()).isEqualTo("obs text");

		assertNotNull(o.getSubject().getResource());
		p = (Patient) o.getSubject().getResource();
		assertThat(p.getName().get(0).getFamilyElement().getValue()).isEqualTo("patient family");
	}

	/**
	 * See #720
	 */
	@Test
	public void testParseCustomResourceType() {
		String input = "{\"resourceType\":\"Bug720ResourceType\",\"meta\":{\"profile\":[\"http://example.com/StructureDefinition/dontuse#Bug720ResourceType\"]},\"supportedVersion\":\"2.5.x\",\"templatesConsentTemplate\":[{\"domainName\":\"name\",\"Name\":\"template_01\",\"version\":\"1.0\",\"title\":\"title\",\"comment\":\"comment\",\"contact\":{\"resourceType\":\"Person\",\"name\":[{\"family\":\"Mustermann\",\"given\":[\"Max\"]}],\"telecom\":[{\"system\":\"email\",\"value\":\"max.mustermann@mail.de\"},{\"system\":\"phone\",\"value\":\"+49 1234 23232\"}],\"address\":[{\"text\":\"street 1-2\",\"city\":\"city\",\"postalCode\":\"12345\",\"country\":\"Germany\"}]}}]}";
		Bug720ResourceType parsed = ourCtx.newJsonParser().parseResource(Bug720ResourceType.class, input);

		assertThat(parsed.getTemplates()).hasSize(1);
		assertThat(parsed.getTemplates().get(0).getClass()).isEqualTo(Bug720Datatype.class);
		assertThat(((Bug720Datatype) parsed.getTemplates().get(0)).getContact().getNameFirstRep().getFamily()).isEqualTo("Mustermann");

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed));
	}

	/**
	 * #480
	 */
	@Test
	public void testParseEmptyValue() {
		String input = "{\"resourceType\":\"QuestionnaireResponse\",\"id\":\"123\",\"authored\":\"\",\"group\":{\"linkId\":\"\"}}";
		IParser parser = ourCtx.newJsonParser();

		parser.setParserErrorHandler(new LenientErrorHandler().setErrorOnInvalidValue(false));
		QuestionnaireResponse qr = parser.parseResource(QuestionnaireResponse.class, input);

		assertThat(qr.getIdElement().getValue()).isEqualTo("QuestionnaireResponse/123");
		assertNull(qr.getAuthored());
		assertNull(qr.getAuthoredElement().getValue());
		assertNull(qr.getAuthoredElement().getValueAsString());
		assertNull(qr.getItemFirstRep().getLinkId());
		assertNull(qr.getItemFirstRep().getLinkIdElement().getValue());
	}

	/**
	 * See #335
	 */
	@Test
	public void testParseExtensionWithId() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/json-edge-case-modified-335.json"), StandardCharsets.UTF_8);

		Patient p = ourCtx.newJsonParser().parseResource(Patient.class, input);
		StringType family1 = p.getContact().get(0).getName().getGiven().get(1);
		assertThat(family1.getValue()).isEqualTo("Denise");
		assertThat(family1.getId()).isEqualTo("a3");
	}

	/**
	 * See #342
	 */
	@Test()
	public void testParseInvalid() {
		try {
			ourCtx.newJsonParser().parseResource("FOO");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1861) + "Failed to parse JSON encoded FHIR content: " + Msg.code(1859) + "Content does not appear to be FHIR JSON, first non-whitespace character was: 'F' (must be '{')");
		}
		try {
			ourCtx.newJsonParser().parseResource("[\"aaa\"]");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1861) + "Failed to parse JSON encoded FHIR content: " + Msg.code(1859) + "Content does not appear to be FHIR JSON, first non-whitespace character was: '[' (must be '{')");
		}

		assertThat(ourCtx.newJsonParser().parseResource("  {\"resourceType\" : \"Bundle\"}").getClass()).isEqualTo(Bundle.class);

	}

	/**
	 * See #414
	 */
	@Test
	public void testParseJsonExtensionWithoutUrl() {
		//@formatter:off
		String input =
			"{\"resourceType\":\"Patient\"," +
				"\"extension\":[ {\"valueDateTime\":\"2011-01-02T11:13:15\"} ]" +
				"}";
		//@formatter:on

		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertThat(parsed.getExtension()).hasSize(1);
		assertNull(parsed.getExtension().get(0).getUrl());
		assertThat(parsed.getExtension().get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("2011-01-02T11:13:15");

		try {
			parser = ourCtx.newJsonParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1822) + "Resource is missing required element 'url' in parent element 'extension'");
		}

	}

	/**
	 * See #414
	 */
	@Test
	public void testParseJsonModifierExtensionWithoutUrl() {
		//@formatter:off
		String input =
			"{\"resourceType\":\"Patient\"," +
				"\"modifierExtension\":[ {\"valueDateTime\":\"2011-01-02T11:13:15\"} ]" +
				"}";
		//@formatter:on

		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertThat(parsed.getModifierExtension()).hasSize(1);
		assertNull(parsed.getModifierExtension().get(0).getUrl());
		assertThat(parsed.getModifierExtension().get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("2011-01-02T11:13:15");

		try {
			parser = ourCtx.newJsonParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1822) + "Resource is missing required element 'url' in parent element 'modifierExtension'");
		}

	}

	@Test
	public void testParseMetadata() {
		//@formatter:off
		String bundle = "{\n" +
			"  \"resourceType\" : \"Bundle\",\n" +
			"  \"total\" : 1,\n" +
			"   \"link\": [{\n" +
			"      \"relation\" : \"self\",\n" +
			"      \"url\" : \"http://localhost:52788/Binary?_pretty=true\"\n" +
			"   }],\n" +
			"   \"entry\" : [{\n" +
			"      \"fullUrl\" : \"http://foo/fhirBase2/Patient/1/_history/2\",\n" +
			"      \"resource\" : {\n" +
			"         \"resourceType\" : \"Patient\",\n" +
			"         \"id\" : \"1\",\n" +
			"         \"meta\" : {\n" +
			"            \"versionId\" : \"2\",\n" +
			"            \"lastUpdated\" : \"2001-02-22T11:22:33-05:00\"\n" +
			"         },\n" +
			"         \"birthDate\" : \"2012-01-02\"\n" +
			"      },\n" +
			"      \"search\" : {\n" +
			"         \"mode\" : \"match\",\n" +
			"         \"score\" : 0.123\n" +
			"      },\n" +
			"      \"request\" : {\n" +
			"         \"method\" : \"POST\",\n" +
			"         \"url\" : \"http://foo/Patient?identifier=value\"\n" +
			"      }\n" +
			"   }]\n" +
			"}";
		//@formatter:on

		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, bundle);
		assertThat(b.getEntry()).hasSize(1);

		BundleEntryComponent entry = b.getEntry().get(0);
		Patient pt = (Patient) entry.getResource();
		assertThat(pt.getIdElement().getValue()).isEqualTo("http://foo/fhirBase2/Patient/1/_history/2");
		assertThat(pt.getBirthDateElement().getValueAsString()).isEqualTo("2012-01-02");
		assertThat(entry.getSearch().getScore().toString()).isEqualTo("0.123");
		assertThat(entry.getSearch().getMode().toCode()).isEqualTo("match");
		assertThat(entry.getRequest().getMethod().toCode()).isEqualTo("POST");
		assertThat(entry.getRequest().getUrl()).isEqualTo("http://foo/Patient?identifier=value");
		assertThat(pt.getMeta().getLastUpdatedElement().getValueAsString()).isEqualTo("2001-02-22T11:22:33-05:00");

		String reEncoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(bundle.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reEncoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertThat(act).isEqualTo(exp);

	}

	@Test
	public void testParseMissingArray() throws IOException {
		// RelatedPerson.name is 0..* but this file has it as a 0..1 (no array around the object)

		// We're lenient so we accept it. Maybe this could change, or be a warning in future though

		String input = ClasspathUtil.loadResource("/missing_array.json");
		RelatedPerson rp = ourCtx.newJsonParser().parseResource(RelatedPerson.class, input);
		assertThat(rp.getName()).hasSize(1);
		assertThat(rp.getName().get(0).getFamily()).isEqualTo("Doe");

	}

	/**
	 * See #484
	 */
	@Test
	public void testParseNarrativeWithEmptyDiv() {
		String input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div/>\"}}";
		Basic basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertNull(basic.getText().getDivAsString());

		input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div></div>\"}}";
		basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertNull(basic.getText().getDivAsString());

		input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div> </div>\"}}";
		basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertThat(basic.getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\"> </div>");

	}

	/**
	 * See #1658
	 */
	@Test
	public void testParseNarrativeWithLang() {
		String input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\" lang=\\\"en-US\\\">foo</div>\"}}";
		Basic basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertThat(basic.getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\">foo</div>");


	}

	/**
	 * See #163
	 */
	@Test
	public void testParseResourceType() {
		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);

		// Patient
		Patient patient = new Patient();
		String patientId = UUID.randomUUID().toString();
		patient.setId(new IdType("Patient", patientId));
		patient.addName().addGiven("John").setFamily("Smith");
		patient.setGender(AdministrativeGender.MALE);
		patient.setBirthDateElement(new DateType("1987-04-16"));

		// Bundle
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = jsonParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		Bundle reincarnatedBundle = jsonParser.parseResource(Bundle.class, bundleText);
		Patient reincarnatedPatient = (Patient) reincarnatedBundle.getEntry().get(0).getResource();

		assertThat(patient.getIdElement().getResourceType()).isEqualTo("Patient");
		assertThat(reincarnatedPatient.getIdElement().getResourceType()).isEqualTo("Patient");
	}

	/**
	 * See #207
	 */
	@Test
	public void testParseResourceWithInvalidType() {
		String input = "{" + "\"resourceType\":\"Patient\"," + "\"contained\":[" + "    {" + "       \"rezType\":\"Organization\"" + "    }" + "  ]" + "}";

		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
		try {
			jsonParser.parseResource(input);
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1843) + "Missing required element 'resourceType' from JSON resource object, unable to parse");
		}
	}

	@Test
	public void testParseWithPrecision() {

//		BigDecimal d0 = new BigDecimal("0.1");
//		BigDecimal d1 = new BigDecimal("0.1000");
//
//		ourLog.info("Value: {}", d0);
//		ourLog.info("Value: {}", d1);

		{
			String input = "{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.0100}}";
			Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, input);
			DecimalType valueElement = ((Quantity) obs.getValue()).getValueElement();
			assertThat(valueElement.getValueAsString()).isEqualTo("0.0100");
			String str = ourCtx.newJsonParser().encodeResourceToString(obs);
			ourLog.info(str);
			assertThat(str).isEqualTo("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.0100}}");
		}
		{
			String input = "{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.000000000000000100}}";
			Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, input);
			DecimalType valueElement = ((Quantity) obs.getValue()).getValueElement();
			assertThat(valueElement.getValueAsString()).isEqualTo("0.000000000000000100");
			String str = ourCtx.newJsonParser().encodeResourceToString(obs);
			ourLog.info(str);
			assertThat(str).isEqualTo("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.000000000000000100}}");
		}
	}

	@Test
	public void testParseWithTrailingContent() {
		String bundle = "{\n" +
			"  \"resourceType\": \"Bundle\",\n" +
			"  \"total\": 1\n" +
			"}}";

		try {
			ourCtx.newJsonParser().parseResource(Bundle.class, bundle);
			fail("");		} catch (DataFormatException e) {
			// I'm hoping at some point we can get rid of the REDACTED message entirely.
			// Request filed with Jackson: https://github.com/FasterXML/jackson-core/issues/1158
			assertThat(e.getMessage()).isEqualTo(Msg.code(1861) + "Failed to parse JSON encoded FHIR content: Unexpected close marker '}': expected ']' (for root starting at [line: 1])\n" +
				" at [line: 4, column: 3]");
		}
	}

	@Test
	@Disabled
	public void testParseWithWrongTypeObjectShouldBeArray() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/invalid_metadata.json"), Charsets.UTF_8);
		try {
			ourCtx.newJsonParser().parseResource(CapabilityStatement.class, input);
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Syntax error parsing JSON FHIR structure: Expected ARRAY at element 'modifierExtension', found 'OBJECT'");
		}
	}

	/**
	 * See #344
	 */
	@Test
	public void testParserIsCaseSensitive() {
		Observation obs = new Observation();
		SampledData data = new SampledData();
		data.setData("1 2 3");
		data.setOrigin((SimpleQuantity) new SimpleQuantity().setValue(0L));
		data.setPeriod(1000L);
		obs.setValue(data);

		IParser p = ourCtx.newJsonParser().setPrettyPrint(true).setParserErrorHandler(new StrictErrorHandler());
		String encoded = p.encodeResourceToString(obs);
		ourLog.info(encoded);

		p.parseResource(encoded);

		try {
			p.parseResource(encoded.replace("Observation", "observation"));
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1815) + "Unknown resource type 'observation': Resource names are case sensitive, found similar name: 'Observation'");
		}

		try {
			p.parseResource(encoded.replace("valueSampledData", "valueSampleddata"));
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1825) + "Unknown element 'valueSampleddata' found during parse");
		}
	}

	/**
	 * See #144 and #146
	 */
	@Test
	public void testReportSerialize() {

		ReportObservationDstu3 obsv = new ReportObservationDstu3();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringType("value test"));
		obsv.setStatus(ObservationStatus.FINAL);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().add(obsv);
		report.addResult().setResource(obsv);

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		assertThat(message).contains("contained");
	}

	/**
	 * See #144 and #146
	 */
	@Test
	public void testReportSerializeWithMatchingId() {

		ReportObservationDstu3 obsv = new ReportObservationDstu3();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringType("value test"));
		obsv.setStatus(ObservationStatus.FINAL);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().add(obsv);

		obsv.setId("#123");
		report.addResult().setReference("#123");

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		assertThat(message).contains("contained");
	}

	/**
	 * See #477
	 */
	@Test
	public void testUnexpectedElementsWithUnderscoreAtStartOfName() throws Exception {
		String input = ClasspathUtil.loadResource("/bug477.json");

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		// Do it once without the custom error handler just for the logging
		IParser p = ourCtx.newJsonParser();
		p.parseResource(Patient.class, input);

		p = ourCtx.newJsonParser();
		p.setParserErrorHandler(errorHandler);

		Patient parsed = p.parseResource(Patient.class, input);
		assertThat(parsed.getIdElement().getIdPart()).isEqualTo("1");

		ArgumentCaptor<String> elementName = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<ValueType> expected = ArgumentCaptor.forClass(ValueType.class);
		ArgumentCaptor<ValueType> actual = ArgumentCaptor.forClass(ValueType.class);
		ArgumentCaptor<ScalarType> expectedScalar = ArgumentCaptor.forClass(ScalarType.class);
		ArgumentCaptor<ScalarType> actualScalar = ArgumentCaptor.forClass(ScalarType.class);
		verify(errorHandler, atLeastOnce()).incorrectJsonType(nullable(IParseLocation.class), elementName.capture(), expected.capture(), expectedScalar.capture(), actual.capture(),
			actualScalar.capture());
		verify(errorHandler, atLeastOnce()).incorrectJsonType(nullable(IParseLocation.class), eq("_id"), eq(ValueType.OBJECT), expectedScalar.capture(), eq(ValueType.SCALAR),
			actualScalar.capture());
		verify(errorHandler, atLeastOnce()).incorrectJsonType(nullable(IParseLocation.class), eq("__v"), eq(ValueType.OBJECT), expectedScalar.capture(), eq(ValueType.SCALAR),
			actualScalar.capture());
		verify(errorHandler, atLeastOnce()).incorrectJsonType(nullable(IParseLocation.class), eq("_status"), eq(ValueType.OBJECT), expectedScalar.capture(),
			eq(ValueType.SCALAR), actualScalar.capture());

		assertThat(elementName.getAllValues().get(0)).isEqualTo("_id");
		assertThat(expected.getAllValues().get(0)).isEqualTo(ValueType.OBJECT);
		assertThat(actual.getAllValues().get(0)).isEqualTo(ValueType.SCALAR);
		assertNull(expectedScalar.getAllValues().get(0));
		assertNull(actualScalar.getAllValues().get(0));
	}

	@Test
	public void testValidateCustomStructure() {

		FooMessageHeader.FooMessageSourceComponent source = new FooMessageHeader.FooMessageSourceComponent();
		source.getMessageHeaderApplicationId().setValue("APPID");
		source.setName("NAME");
		source.setEndpoint("http://foo");

		FooMessageHeader header = new FooMessageHeader();
		header.setTimestamp(new Date());
		header.getEvent().setSystem("http://system").setCode("value");
		header.setSource(source);

		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(true);

		ValidationResult result = val.validateWithResult(header);

		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}

	@Test
	public void encodeResourceToString_withEXCLUDE_ELEMENTS_IN_ENCODED_metaKeptInContainedResource() {
		// Arrange
		Organization containedOrganization = new Organization();
		containedOrganization.getMeta().addProfile(UUID.randomUUID().toString());
		containedOrganization.getMeta().setLastUpdated(new Date());
		containedOrganization.getMeta().setVersionId(UUID.randomUUID().toString());
		containedOrganization.getMeta().setSecurity(Collections.singletonList(new Coding(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())));
		containedOrganization.getMeta().setTag(Collections.singletonList(new Coding(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())));

		Patient patient = new Patient();
		patient.setId(UUID.randomUUID().toString());
		patient.getMeta().addProfile(UUID.randomUUID().toString());
		patient.setGeneralPractitioner(Collections.singletonList(new Reference(containedOrganization)));

		HashSet<String> excludeElementsInEncoded = new HashSet<>(); // ResourceMetaParams.EXCLUDE_ELEMENTS_IN_ENCODED
		excludeElementsInEncoded.add("id");
		excludeElementsInEncoded.add("*.meta");

		IParser parser = ourCtx.newJsonParser();
		parser.setDontEncodeElements(excludeElementsInEncoded);

		// Act
		String encodedPatient = parser.encodeResourceToString(patient);

		// Assert
		Patient parsedPatient = (Patient) parser.parseResource(encodedPatient);
		assertNull(parsedPatient.getId());
		assertTrue(parsedPatient.getMeta().isEmpty());

		Resource containedResource = parsedPatient.getContained().get(0);
		assertNotNull(containedResource.getMeta());
		assertNull(containedResource.getMeta().getVersionId());
		assertNull(containedResource.getMeta().getLastUpdated());
		assertThat(containedResource.getMeta().getSecurity()).isEmpty();
		assertThat(containedResource.getMeta().getProfile()).hasSize(1);
		assertThat(containedResource.getMeta().getTag()).hasSize(1);
	}

	@Test
	public void testPreCommentsToFhirComments() {
		final Patient patient = new Patient();

		final Identifier identifier = new Identifier();
		identifier.setValue("myId");
		identifier.getFormatCommentsPre().add("This is a comment");
		patient.getIdentifier().add(identifier);

		final HumanName humanName1 = new HumanName();
		humanName1.addGiven("given1");
		humanName1.getFormatCommentsPre().add("This is another comment");
		patient.getName().add(humanName1);

		final HumanName humanName2 = new HumanName();
		humanName2.addGiven("given1");
		humanName2.getFormatCommentsPre().add("This is yet another comment");
		patient.getName().add(humanName2);

		final String patientString = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(patientString).doesNotContain("fhir_comment");
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
