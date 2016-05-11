package ca.uhn.fhir.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Composition.Section;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.TestUtil;

/**
 * Initially contributed by Alexander Kley for bug #29
 */
public class ContainedResourceEncodingTest {

	private static Logger ourLog = LoggerFactory.getLogger(ContainedResourceEncodingTest.class);

	private FhirContext ctx;

	private Composition comp;

	private Practitioner author;

	private Patient patient;

	private final String patFamName1 = "FirstFamilyName";

	private final String patGivName1 = "FirstGivenName";

	@Before
	public void initTest() {
		ourLog.info("[initTest]");

		initPatient();
		initAuthor();
		initComposition();
		this.ctx = FhirContext.forDstu1();

	}

	private void initComposition() {
		// new ConditionList

		final Condition c = new Condition();
		c.setId(UUID.randomUUID().toString());
		c.setNotes("This is a note");
		c.setSubject(new ResourceReferenceDt(this.patient));
		c.setCode(new CodeableConceptDt("mySystem", "theCode"));
		c.setStatus(ConditionStatusEnum.CONFIRMED);

		// new General Note Section
		final Section generalNoteSection = new Section();
		generalNoteSection.setElementSpecificId("Note");
		generalNoteSection.setTitle("Note");
		generalNoteSection.setContent(new ResourceReferenceDt(c));

		// new SectionList
		final List<Section> sectionList = new ArrayList<Section>();
		sectionList.add(generalNoteSection);

		// fill composition
		this.comp = new Composition();
		this.comp.addAuthor().setResource(this.author);
		this.comp.setSubject(new ResourceReferenceDt(this.patient));
		this.comp.setSection(sectionList);
	}

	private void initPatient() {
		this.patient = new Patient();
		this.patient.setId(new IdDt(UUID.randomUUID().toString()));
		this.patient.addIdentifier().setSystem("http://example.com/fictitious-mrns").setValue("MRN001");
		this.patient.setGender(AdministrativeGenderCodesEnum.M);
		this.patient.addName().setUse(NameUseEnum.OFFICIAL).addFamily(this.patFamName1).addGiven(this.patGivName1);

	}

	private void initAuthor() {
		this.author = new Practitioner();
		this.author.setId(new IdDt(UUID.randomUUID().toString()));
		this.author.addIdentifier().setSystem("DoctorID").setValue("4711");
		this.author.addRole(PractitionerRoleEnum.DOCTOR);
		this.author.setName(new HumanNameDt().addFamily("Mueller").addGiven("Klaus").addPrefix("Prof. Dr."));

	}

	@Test
	public void testPatient() {
		ourLog.debug("[xmlEncoding] encode resource to xml.");

		/**
		 * This works fine, although patient instance is modifing from encoder
		 */
		final String expectedPatientXml = this.ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(this.patient);
		ourLog.debug("[xmlEncoding] first encoding: {}", expectedPatientXml);
		final String actualPatientXml = this.ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(this.patient);
		// second encoding - xml is corrupt - i.e.: patient content 4 times! should be the same as after first encoding!
		ourLog.debug("[xmlEncoding] second encoding: {}", actualPatientXml);

		Assert.assertEquals(expectedPatientXml.length(), actualPatientXml.length());
		Assert.assertArrayEquals(expectedPatientXml.getBytes(), actualPatientXml.getBytes());

	}

	@Test
	public void testComposition() {

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		/**
		 * This doesn't works, secund encoding creates corrupt xml
		 */
		final String expectedCompXml = parser.encodeResourceToString(this.comp);
		ourLog.debug("[xmlEncoding] first encoding: {}", expectedCompXml);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		final String actualCompXml = parser.encodeResourceToString(this.comp);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		// second encoding - xml could not be parsed back to compositon - i.e.: patient content 4 times! should be the same
		// as after first encoding!
		ourLog.debug("[xmlEncoding] second encoding: {}", actualCompXml);

		final String thirdCompXml = parser.encodeResourceToString(this.comp);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		// third encoding - xml could not be parsed back to compositon i.e.: patient content 4 times! should be the same as
		// afterfirst encoding!
		ourLog.debug("[xmlEncoding] third encoding: {}", thirdCompXml);

		Assert.assertEquals(expectedCompXml.length(), actualCompXml.length());
		Assert.assertArrayEquals(expectedCompXml.getBytes(), actualCompXml.getBytes());

	}

	@Test
	public void testBundleWithContained() {

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(new IdDt("DiagnosticReport", "123"));

		Observation observation = new Observation();

		CodeableConceptDt obsName = new CodeableConceptDt();
		obsName.setText("name");
		observation.setName(obsName);

		dr.addResult().setResource(observation);

		ArrayList<ResourceReferenceDt> performers = new ArrayList<ResourceReferenceDt>();
		ResourceReferenceDt performer = new ResourceReferenceDt();

		Practitioner p = new Practitioner();
		p.setId("#" + IdDt.newRandomUuid());
		p.addIdentifier().setSystem("DoctorID").setValue("4711");
		p.addRole(PractitionerRoleEnum.DOCTOR);
		p.setName(new HumanNameDt().addFamily("Mueller").addGiven("Klaus").addPrefix("Prof. Dr."));

		performer.setResource(p);
		performers.add(performer);
		observation.setPerformer(performers);

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(dr);
		ourLog.info(xml);
		Assert.assertTrue(xml.contains("Mueller"));
		
		List<IBaseResource> list = new ArrayList<IBaseResource>();
		list.add(dr);

		IVersionSpecificBundleFactory factory = ctx.newBundleFactory();
		factory.initializeBundleFromResourceList("", list, "http://foo", "http://foo", 2, null);
		Bundle bundle = factory.getDstu1Bundle();

		parser = this.ctx.newXmlParser().setPrettyPrint(true);
		xml = parser.encodeBundleToString(bundle);
		ourLog.info(xml);
		Assert.assertTrue(xml.contains("Mueller"));

	}

	@Test
	public void testBundleWithContainedWithNoIdDt() {

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(new IdDt("DiagnosticReport", "123"));

		Observation observation = new Observation();

		CodeableConceptDt obsName = new CodeableConceptDt();
		obsName.setText("name");
		observation.setName(obsName);

		ResourceReferenceDt result = dr.addResult();
		result.setResource(observation);

		ArrayList<ResourceReferenceDt> performers = new ArrayList<ResourceReferenceDt>();
		ResourceReferenceDt performer = new ResourceReferenceDt();

		Practitioner p = new Practitioner();
		// no idDt on practitioner p
		p.addIdentifier().setSystem("DoctorID").setValue("4711");
		p.addRole(PractitionerRoleEnum.DOCTOR);
		p.setName(new HumanNameDt().addFamily("Mueller").addGiven("Klaus").addPrefix("Prof. Dr."));

		performer.setResource(p);
		performers.add(performer);
		observation.setPerformer(performers);

		List<IBaseResource> list = new ArrayList<IBaseResource>();
		list.add(dr);

		IVersionSpecificBundleFactory factory = ctx.newBundleFactory();
		factory.initializeBundleFromResourceList("", list, "http://foo", "http://foo", 2, null);
		Bundle bundle = factory.getDstu1Bundle();

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeBundleToString(bundle);
		ourLog.info(xml);
		Assert.assertTrue(xml.contains("Mueller"));

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
