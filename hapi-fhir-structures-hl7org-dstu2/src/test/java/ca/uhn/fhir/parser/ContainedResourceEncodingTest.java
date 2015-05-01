package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Composition;
import org.hl7.fhir.instance.model.Condition;
import org.hl7.fhir.instance.model.Condition.ConditionStatus;
import org.hl7.fhir.instance.model.DiagnosticReport;
import org.hl7.fhir.instance.model.HumanName;
import org.hl7.fhir.instance.model.HumanName.NameUse;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Patient.AdministrativeGender;
import org.hl7.fhir.instance.model.Practitioner;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.api.IRiResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu2hl7org.Dstu2Hl7OrgBundleFactory;

/**
 * Initially contributed by Alexander Kley for bug #29
 */
public class ContainedResourceEncodingTest {

	private static Logger logger = LoggerFactory.getLogger(ContainedResourceEncodingTest.class);

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ContainedResourceEncodingTest.class);

	private Practitioner author;

	private Composition comp;

	private FhirContext ctx;

	private final String patFamName1 = "FirstFamilyName";

	private final String patGivName1 = "FirstGivenName";

	private Patient patient;

	private void initAuthor() {
		this.author = new Practitioner();
		this.author.setId((UUID.randomUUID().toString()));
		this.author.addIdentifier().setSystem("DoctorID").setValue("4711");
		this.author.addPractitionerRole().getRole().addCoding().setCode("doctor");
		this.author.setName(new HumanName().addFamily("Mueller").addGiven("Klaus").addPrefix("Prof. Dr."));

	}

	private void initComposition() {
		// new ConditionList

		final Condition c = new Condition();
		c.setId(UUID.randomUUID().toString());
		c.setNotes("This is a note");
		c.setSubject(new Reference(this.patient));
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setSystem("mySystem").setCode("theCode");
		c.setCode(cc);
		c.setStatus(ConditionStatus.CONFIRMED);

		// new General Note Section
		final Composition.SectionComponent generalNoteSection = new Composition.SectionComponent();
		generalNoteSection.setId("Note");
		generalNoteSection.setTitle("Note");
		generalNoteSection.setContent(new Reference(c));

		// new SectionList
		final List<Composition.SectionComponent> sectionList = new ArrayList<Composition.SectionComponent>();
		sectionList.add(generalNoteSection);

		// fill composition
		this.comp = new Composition();
		this.comp.addAuthor().setResource(this.author);
		this.comp.setSubject(new Reference(this.patient));
		this.comp.getSection().addAll(sectionList);
	}

	private void initPatient() {
		this.patient = new Patient();
		this.patient.setId((UUID.randomUUID().toString()));
		this.patient.addIdentifier().setSystem("http://example.com/fictitious-mrns").setValue("MRN001");
		this.patient.setGender(AdministrativeGender.MALE);
		this.patient.addName().setUse(NameUse.OFFICIAL).addFamily(this.patFamName1).addGiven(this.patGivName1);

	}

	@Before
	public void initTest() {
		logger.info("[initTest]");

		initPatient();
		initAuthor();
		initComposition();
		this.ctx = new FhirContext();

	}

	@Test
	public void testBundleWithContained() {

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(("123"));

		Observation observation = new Observation();

		CodeableConcept obsName = new CodeableConcept();
		obsName.setText("name");
		observation.setCode(obsName);

		Reference result = dr.addResult();
		result.setResource(observation);

		ArrayList<Reference> performers = new ArrayList<Reference>();
		Reference performer = new Reference();

		Practitioner p = new Practitioner();
		p.setId((UUID.randomUUID().toString()));
		p.addIdentifier().setSystem("DoctorID").setValue("4711");
		p.addPractitionerRole().getRole().setText("Doctor");
		p.setName(new HumanName().addFamily("Mueller").addGiven("Klaus").addPrefix("Prof. Dr."));

		performer.setResource(p);
		performers.add(performer);
		observation.getPerformer().addAll(performers);

		List<IBaseResource> list = new ArrayList<IBaseResource>();
		list.add(dr);

		Dstu2Hl7OrgBundleFactory builder = new Dstu2Hl7OrgBundleFactory(ctx);
		Set<Include> inc2 = Collections.emptySet();
		builder.addResourcesToBundle(list, BundleTypeEnum.TRANSACTION, "http://foo", BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE, inc2);
		IBaseResource bundle = builder.getResourceBundle();

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(bundle);
		ourLog.info(xml);
		Assert.assertTrue(xml.contains("Mueller"));

	}

	@Test
	public void testBundleWithContainedWithNoIdDt() {

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId("123");

		Observation observation = new Observation();

		CodeableConcept obsName = new CodeableConcept();
		obsName.setText("name");
		observation.setCode(obsName);

		Reference result = dr.addResult();
		result.setResource(observation);

		ArrayList<Reference> performers = new ArrayList<Reference>();
		Reference performer = new Reference();

		Practitioner p = new Practitioner();
		// no idDt on practitioner p
		p.addIdentifier().setSystem("DoctorID").setValue("4711");
		p.addPractitionerRole().getRole().setText("Doctor");
		p.setName(new HumanName().addFamily("Mueller").addGiven("Klaus").addPrefix("Prof. Dr."));

		performer.setResource(p);
		performers.add(performer);
		observation.getPerformer().addAll(performers);

		List<IBaseResource> list = new ArrayList<IBaseResource>();
		list.add(dr);

		Dstu2Hl7OrgBundleFactory builder = new Dstu2Hl7OrgBundleFactory(ctx);
		Set<Include> inc2 = Collections.emptySet();
		builder.addResourcesToBundle(list, BundleTypeEnum.TRANSACTION, "http://foo", BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE, inc2);
		IBaseResource bundle = builder.getResourceBundle();

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(bundle);
		Assert.assertTrue(xml.contains("Mueller"));

	}

	@Test
	public void testComposition() {

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);

		assertEquals(0, this.comp.getContained().size());

		/**
		 * This doesn't works, secund encoding creates corrupt xml
		 */
		final String expectedCompXml = parser.encodeResourceToString(this.comp);
		logger.debug("[xmlEncoding] first encoding: {}", expectedCompXml);

		assertEquals(0, this.comp.getContained().size());

		final String actualCompXml = parser.encodeResourceToString(this.comp);

		assertEquals(0, this.comp.getContained().size());

		// second encoding - xml could not be parsed back to compositon - i.e.: patient content 4 times! should be the same
		// as after first encoding!
		logger.debug("[xmlEncoding] second encoding: {}", actualCompXml);

		final String thirdCompXml = parser.encodeResourceToString(this.comp);

		assertEquals(0, this.comp.getContained().size());

		// third encoding - xml could not be parsed back to compositon i.e.: patient content 4 times! should be the same as
		// afterfirst encoding!
		logger.debug("[xmlEncoding] third encoding: {}", thirdCompXml);

		Assert.assertEquals(expectedCompXml.length(), actualCompXml.length());
		Assert.assertArrayEquals(expectedCompXml.getBytes(), actualCompXml.getBytes());

	}

	@Test
	public void testPatient() {
		logger.debug("[xmlEncoding] encode resource to xml.");

		/**
		 * This works fine, although patient instance is modifing from encoder
		 */
		final String expectedPatientXml = this.ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(this.patient);
		logger.debug("[xmlEncoding] first encoding: {}", expectedPatientXml);
		final String actualPatientXml = this.ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(this.patient);
		// second encoding - xml is corrupt - i.e.: patient content 4 times! should be the same as after first encoding!
		logger.debug("[xmlEncoding] second encoding: {}", actualPatientXml);

		Assert.assertEquals(expectedPatientXml.length(), actualPatientXml.length());
		Assert.assertArrayEquals(expectedPatientXml.getBytes(), actualPatientXml.getBytes());

	}

}
