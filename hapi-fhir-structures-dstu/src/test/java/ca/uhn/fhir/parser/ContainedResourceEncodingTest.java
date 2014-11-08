package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.model.dstu.resource.Composition.Section;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu.valueset.PractitionerRoleEnum;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * Initially contributed by Alexander Kley for bug #29
 */
public class ContainedResourceEncodingTest {

	private static Logger logger = LoggerFactory.getLogger(ContainedResourceEncodingTest.class);

	private FhirContext ctx;

	private Composition comp;

	private Practitioner author;

	private Patient patient;

	private final String patFamName1 = "FirstFamilyName";

	private final String patGivName1 = "FirstGivenName";

	@Before
	public void initTest() {
		logger.info("[initTest]");

		initPatient();
		initAuthor();
		initComposition();
		this.ctx = new FhirContext();

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
	
	@Test
	public void testComposition() {

		IParser parser = this.ctx.newXmlParser().setPrettyPrint(true);
		
		assertEquals(0, this.comp.getContained().getContainedResources().size());
		
		/**
		 * This doesn't works, secund encoding creates corrupt xml
		 */
		final String expectedCompXml = parser.encodeResourceToString(this.comp);
		logger.debug("[xmlEncoding] first encoding: {}", expectedCompXml);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		final String actualCompXml = parser.encodeResourceToString(this.comp);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		// second encoding - xml could not be parsed back to compositon - i.e.: patient content 4 times! should be the same
		// as after first encoding!
		logger.debug("[xmlEncoding] second encoding: {}", actualCompXml);

		final String thirdCompXml = parser.encodeResourceToString(this.comp);

		assertEquals(0, this.comp.getContained().getContainedResources().size());

		// third encoding - xml could not be parsed back to compositon i.e.: patient content 4 times! should be the same as
		// afterfirst encoding!
		logger.debug("[xmlEncoding] third encoding: {}", thirdCompXml);

		Assert.assertEquals(expectedCompXml.length(), actualCompXml.length());
		Assert.assertArrayEquals(expectedCompXml.getBytes(), actualCompXml.getBytes());

	}

}
