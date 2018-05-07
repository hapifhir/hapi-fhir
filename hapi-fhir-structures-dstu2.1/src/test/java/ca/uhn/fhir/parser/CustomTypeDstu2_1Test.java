package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.dstu2016may.model.Extension;
import org.hl7.fhir.instance.model.api.IBase;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.CustomResource364Dstu21.CustomResource364CustomDate;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.TestUtil;

public class CustomTypeDstu2_1Test {

	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomTypeDstu2_1Test.class);

	@Before
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	/**
	 * See #364
	 */
	@Test
	public void testCustomTypeWithCustomDatatype() {
		FhirContext context = FhirContext.forDstu2_1();
		context.registerCustomType(CustomResource364Dstu21.class);
		context.registerCustomType(CustomResource364CustomDate.class);
		IParser parser = context.newXmlParser();

		CustomResource364Dstu21 resource = new CustomResource364Dstu21();
		resource.setBaseValues(new CustomResource364CustomDate().setDate(new DateTimeType("2016-05-13")));

		String xml = parser.encodeResourceToString(resource);
		ourLog.info(xml);

		//@formatter:on
		assertThat(xml, stringContainsInOrder(
			"<CustomResource xmlns=\"http://hl7.org/fhir\">",
			"<meta><profile value=\"http://hl7.org/fhir/profiles/custom-resource\"/></meta>",
			"<baseValueCustomDate><date value=\"2016-05-13\"/></baseValueCustomDate>",
			"</CustomResource>"
		));
		//@formatter:on
		
		CustomResource364Dstu21 parsedResource = parser.parseResource(CustomResource364Dstu21.class, xml);
		assertEquals("2016-05-13", ((CustomResource364CustomDate)parsedResource.getBaseValues()).getDate().getValueAsString());
	}

	/**
	 * See #364
	 */
	@Test
	public void testCustomTypeWithPrimitiveType() {
		FhirContext context = FhirContext.forDstu2_1();
		context.registerCustomTypes(new ArrayList<Class<? extends IBase>>());
		
		IParser parser = context.newXmlParser();

		CustomResource364Dstu21 resource = new CustomResource364Dstu21();
		resource.setBaseValues(new StringType("2016-05-13"));

		String xml = parser.encodeResourceToString(resource);

		//@formatter:on
		assertThat(xml, stringContainsInOrder(
			"<CustomResource xmlns=\"http://hl7.org/fhir\">",
			"<meta><profile value=\"http://hl7.org/fhir/profiles/custom-resource\"/></meta>",
			"<baseValueString value=\"2016-05-13\"/>",
			"</CustomResource>"
		));
		//@formatter:on
		
		CustomResource364Dstu21 parsedResource = parser.parseResource(CustomResource364Dstu21.class, xml);
		assertEquals("2016-05-13", ((StringType)parsedResource.getBaseValues()).getValueAsString());
	}

	
	@Test
	public void parseBundleWithResourceDirective() {
		String input = createBundle(createResource(false), createResource(true));
		
		FhirContext ctx = FhirContext.forDstu2_1();
		ctx.setDefaultTypeForProfile("http://example.com/foo", MyCustomPatient.class);
		
		Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, input);
		
		Patient res0 = (Patient) bundle.getEntry().get(0).getResource();
		assertEquals(0, res0.getMeta().getProfile().size());
		List<Extension> exts = res0.getExtensionsByUrl("http://example.com/Weight");
		assertEquals(1, exts.size());
		assertEquals("185 cm", ((StringType)exts.get(0).getValue()).getValue());

		MyCustomPatient res1 = (MyCustomPatient) bundle.getEntry().get(1).getResource();
		assertEquals(1, res1.getMeta().getProfile().size());
		assertEquals("http://example.com/foo", res1.getMeta().getProfile().get(0).getValue());
		exts = res1.getExtensionsByUrl("http://example.com/Weight");
		assertEquals(0, exts.size());
		assertEquals("185 cm", res1.getWeight().getValue());
	}
	
	@Test
	public void parseResourceWithDirective() {
		String input = createResource(true);
		
		FhirContext ctx = FhirContext.forDstu2_1();
		ctx.setDefaultTypeForProfile("http://example.com/foo", MyCustomPatient.class);
		
		MyCustomPatient parsed = (MyCustomPatient) ctx.newXmlParser().parseResource(input);
		assertEquals(1, parsed.getMeta().getProfile().size());
		assertEquals("http://example.com/foo", parsed.getMeta().getProfile().get(0).getValue());
		
		List<Extension> exts = parsed.getExtensionsByUrl("http://example.com/Weight");
		assertEquals(0, exts.size());
		
		assertEquals("185 cm", parsed.getWeight().getValue());
	}

	
	@Test
	public void parseResourceWithNoDirective() {
		String input = createResource(true);
		
		FhirContext ctx = FhirContext.forDstu2_1();
		Patient parsed = (Patient) ctx.newXmlParser().parseResource(input);
		assertEquals(1, parsed.getMeta().getProfile().size());
		assertEquals("http://example.com/foo", parsed.getMeta().getProfile().get(0).getValue());
		
		List<Extension> exts = parsed.getExtensionsByUrl("http://example.com/Weight");
		assertEquals(1, exts.size());
		assertEquals("185 cm", ((StringType)exts.get(0).getValue()).getValue());
	}
	
	@Test
	public void testAccessEmptyMetaLists() {
		Patient p = new Patient();
		assertThat(p.getMeta().getProfile(), empty());
		assertThat(p.getMeta().getFormatCommentsPost(), empty());
		assertThat(p.getMeta().getFormatCommentsPre(), empty());
		assertThat(p.getMeta().getLastUpdated(), nullValue());
		assertThat(p.getMeta().getSecurity(), empty());
		assertThat(p.getMeta().getSecurity("foo", "bar"), nullValue());
		assertThat(p.getMeta().getTag(), empty());
		assertThat(p.getMeta().getTag("foo", "bar"), nullValue());
		assertThat(p.getMeta().getVersionId(), nullValue());
		
	}

	@Test
	public void testEncodeCompleteMetaLists() {
		Patient p = new Patient();
		p.getMeta().addProfile("http://foo/profile1");
		p.getMeta().addProfile("http://foo/profile2");
		p.getMeta().addSecurity().setSystem("SEC_S1").setCode("SEC_C1").setDisplay("SED_D1");
		p.getMeta().addSecurity().setSystem("SEC_S2").setCode("SEC_C2").setDisplay("SED_D2");
		p.getMeta().addTag().setSystem("TAG_S1").setCode("TAG_C1").setDisplay("TAG_D1");
		p.getMeta().addTag().setSystem("TAG_S2").setCode("TAG_C2").setDisplay("TAG_D2");
		
		String out = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(out);
		
		//@formatter:off
		assertThat(out, stringContainsInOrder(
			"<meta>", 
			"<profile value=\"http://foo/profile1\"/>", 
			"<profile value=\"http://foo/profile2\"/>", 
			"<security>", 
			"<system value=\"SEC_S1\"/>", 
			"<code value=\"SEC_C1\"/>", 
			"<display value=\"SED_D1\"/>", 
			"</security>", 
			"<security>", 
			"<system value=\"SEC_S2\"/>", 
			"<code value=\"SEC_C2\"/>", 
			"<display value=\"SED_D2\"/>", 
			"</security>", 
			"<tag>", 
			"<system value=\"TAG_S1\"/>", 
			"<display value=\"TAG_D1\"/>", 
			"</tag>", 
			"<tag>", 
			"<system value=\"TAG_S2\"/>", 
			"<display value=\"TAG_D2\"/>", 
			"</tag>", 
			"</meta>"));
		//@formatter:on
		
	}
	
	@Test
	public void testEncodeWithCustomType() {

		MyCustomPatient patient = new MyCustomPatient();

		patient.addIdentifier().setSystem("urn:system").setValue("1234");
		patient.addName().addFamily("Rossi").addGiven("Mario");
		patient.setInsulinLevel(new Quantity());
		patient.setGlucoseLevel(new Quantity());
		patient.setHbA1c(new Quantity());
		patient.setBloodPressure(new Quantity());
		patient.setCholesterol(new Quantity());
		patient.setWeight(new StringDt("80 kg"));
		patient.setWeight(new StringDt("185 cm"));
		patient.setCheckDates(new ArrayList<DateTimeDt>());
		patient.getCheckDates().add(new DateTimeDt("2014-01-26T11:11:11"));

		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(patient);

		ourLog.info(messageString);
		
		//@formatter:off
		assertThat(messageString, stringContainsInOrder(
			"<meta>", 
			"<profile value=\"http://example.com/foo\"/>", 
			"</meta>"));
		//@formatter:on

		//@formatter:off
		assertThat(messageString, not(stringContainsInOrder(
			"<meta>", 
			"<profile value=\"http://example.com/foo\"", "/>", 
			"<profile value=\"http://example.com/foo\"/>", 
			"</meta>")));
		//@formatter:on
	}

	@Test
	public void testEncodeWithCustomTypeAndAutoInsertedProfile() {

		MyCustomPatient patient = new MyCustomPatient();

		patient.getMeta().addProfile("http://example.com/foo");
		patient.getMeta().addProfile("http://example.com/bar");
		
		patient.addIdentifier().setSystem("urn:system").setValue("1234");
		patient.addName().addFamily("Rossi").addGiven("Mario");
		patient.setInsulinLevel(new Quantity());
		patient.setGlucoseLevel(new Quantity());
		patient.setHbA1c(new Quantity());
		patient.setBloodPressure(new Quantity());
		patient.setCholesterol(new Quantity());
		patient.setWeight(new StringDt("80 kg"));
		patient.setWeight(new StringDt("185 cm"));
		patient.setCheckDates(new ArrayList<DateTimeDt>());
		patient.getCheckDates().add(new DateTimeDt("2014-01-26T11:11:11"));

		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(patient);

		ourLog.info(messageString);
		
		//@formatter:off
		assertThat(messageString, stringContainsInOrder(
			"<meta>", 
			"<profile value=\"http://example.com/foo\"/>", 
			"<profile value=\"http://example.com/bar\"/>", 
			"</meta>"));
		//@formatter:on

		//@formatter:off
		assertThat(messageString, not(stringContainsInOrder(
			"<meta>", 
			"<profile value=\"http://example.com/foo\"", "/>", 
			"<profile value=\"http://example.com/foo\"/>", 
			"</meta>")));
		//@formatter:on
	}

	/**
	 * See #318
	 */
	@Test
	public void testParseResourceWithContainedResourcesWithProfile() {
		//@formatter:off
		String input = "<MedicationOrder xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"44cfa24c-52e1-a8ff-8428-4e7ce1165460-local\"/> "
				+ "<meta> "
				+ "<profile value=\"http://fhir.something.com/StructureDefinition/our-medication-order\"/> "
				+ "</meta> "
				+ "<contained> "
				+ "<Medication xmlns=\"http://hl7.org/fhir\"> "
				+ "<id value=\"1\"/>"
				+ "<meta> "
				+ "<profile value=\"http://fhir.something.com/StructureDefinition/our-medication\"/> "
				+ "</meta> "
				+ "<code> "
				+ "<text value=\"medication\"/> "
				+ "</code> "
				+ "</Medication> "
				+ "</contained> "
				+ "<medication> "
				+ "<reference value=\"#1\"/> "
				+ "</medication> "
				+ "</MedicationOrder>";
		//@formatter:on
		
		FhirContext ctx = FhirContext.forDstu2_1();
		ctx.setDefaultTypeForProfile("http://fhir.something.com/StructureDefinition/our-medication", MyMedication.class);
		
		MedicationOrder mo = ctx.newXmlParser().parseResource(MedicationOrder.class, input);
		assertEquals(MyMedication.class, mo.getContained().get(0).getClass());
	}

	public static String createBundle(String... theResources) {
		StringBuilder b = new StringBuilder();
		b.append("<Bundle xmlns=\"http://hl7.org/fhir\">\n");
		for (String next : theResources) {
			b.append("	<entry>\n");
			b.append("		<resource>\n");
			b.append(next);
			b.append("		</resource>\n");
			b.append("	</entry>\n");
		}
		b.append("</Bundle>");
		return b.toString();
	}

	public static String createResource(boolean theWithProfile) {
		StringBuilder b = new StringBuilder();
		b.append("<Patient xmlns=\"http://hl7.org/fhir\">\n");
		if (theWithProfile) {
			b.append("   <meta>\n");
			b.append("      <profile value=\"http://example.com/foo\"/>\n");
			b.append("   </meta>\n");
		}
		b.append("   <extension url=\"http://example.com/BloodPressure\">\n");
		b.append("      <valueQuantity>\n");
		b.append("         <value value=\"110\"/>\n");
		b.append("         <system value=\"http://unitsofmeasure.org\"/>\n");
		b.append("         <code value=\"mmHg\"/>\n");
		b.append("      </valueQuantity>\n");
		b.append("   </extension>\n");
		b.append("   <modifierExtension url=\"http://example.com/diabetes2\">\n");
		b.append("      <valueDateTime value=\"2010-01-02\"/>\n");
		b.append("   </modifierExtension>\n");
		b.append("   <modifierExtension url=\"http://example.com/diabetes2\">\n");
		b.append("      <valueDateTime value=\"2014-01-26T11:11:11\"/>\n");
		b.append("   </modifierExtension>\n");
		b.append("   <extension url=\"http://example.com/Cholesterol\">\n");
		b.append("      <valueQuantity>\n");
		b.append("         <value value=\"2\"/>\n");
		b.append("         <system value=\"http://unitsofmeasure.org\"/>\n");
		b.append("         <code value=\"mmol/l\"/>\n");
		b.append("      </valueQuantity>\n");
		b.append("   </extension>\n");
		b.append("   <extension url=\"http://example.com/Glucose\">\n");
		b.append("      <valueQuantity>\n");
		b.append("         <value value=\"95\"/>\n");
		b.append("         <system value=\"http://unitsofmeasure.org\"/>\n");
		b.append("         <code value=\"mg/dl\"/>\n");
		b.append("      </valueQuantity>\n");
		b.append("   </extension>\n");
		b.append("   <extension url=\"http://example.com/HbA1c\">\n");
		b.append("      <valueQuantity>\n");
		b.append("         <value value=\"48\"/>\n");
		b.append("         <system value=\"http://unitsofmeasure.org\"/>\n");
		b.append("         <code value=\"mmol/mol\"/>\n");
		b.append("      </valueQuantity>\n");
		b.append("   </extension>\n");
		b.append("   <extension url=\"http://example.com/Insuline\">\n");
		b.append("      <valueQuantity>\n");
		b.append("         <value value=\"125\"/>\n");
		b.append("         <system value=\"http://unitsofmeasure.org\"/>\n");
		b.append("         <code value=\"pmol/l\"/>\n");
		b.append("      </valueQuantity>\n");
		b.append("   </extension>\n");
		b.append("   <extension url=\"http://example.com/Weight\">\n");
		b.append("      <valueString value=\"185 cm\"/>\n");
		b.append("   </extension>\n");
		b.append("   <identifier>\n");
		b.append("      <system value=\"urn:system\"/>\n");
		b.append("      <value value=\"1234\"/>\n");
		b.append("   </identifier>\n");
		b.append("   <name>\n");
		b.append("      <family value=\"Rossi\"/>\n");
		b.append("      <given value=\"Mario\"/>\n");
		b.append("   </name>\n");
		b.append("</Patient>");
		String input =
			b.toString();
		return input;
	}

	@ResourceDef(name = "Patient", profile = "http://example.com/foo")
	public static class MyCustomPatient extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "bloodPressure") // once every 3 month. The average target is 130/80 mmHg or less
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/BloodPressure", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's blood pressure")
		private Quantity myBloodPressure;

		// Dates of periodic tests
		@Child(name = "CheckDates", max = Child.MAX_UNLIMITED)
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/diabetes2", definedLocally = false, isModifier = true)
		@Description(shortDefinition = "Dates of periodic tests")
		private List<DateTimeDt> myCheckDates;

		@Child(name = "cholesterol") // once a year. The target is triglycerides =< 2 mmol/l e cholesterol =< 4 mmol/l
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/Cholesterol", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's cholesterol")
		private Quantity myCholesterol;

		@Child(name = "glucoseLevel") // fingerprick test
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/Glucose", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's blood glucose")
		private Quantity myGlucoseLevel;

		// Periodic Tests
		@Child(name = "hbA1c") // once every 6 month. The average target is 53 mmol/mol (or 7%) or less.
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/HbA1c", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's glucose")
		private Quantity myHbA1c;

		@Child(name = "Height")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/Height", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The patient's height in cm")
		private StringDt myHeight;

		@Child(name = "insulinLevel") // Normal range is [43,208] pmol/l
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/Insuline", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's insulin")
		private Quantity myInsulinLevel;

		// Other parameters
		@Child(name = "weight")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://example.com/Weight", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The patient's weight in Kg")
		private StringDt myWeight;

		public Quantity Cholesterol() {
			if (myCholesterol == null) {
				myCholesterol = new Quantity();
			}
			myCholesterol.getValue();
			myCholesterol.getSystem();
			myCholesterol.getCode();

			return myCholesterol;
		}

		public Quantity getBloodPressure() {
			if (myBloodPressure == null) {
				myBloodPressure = new Quantity();
			}
			myBloodPressure.getValue();
			myBloodPressure.getSystem();
			myBloodPressure.getCode();

			return myBloodPressure;
		}

		public List<DateTimeDt> getCheckDates() {
			if (myCheckDates == null) {
				myCheckDates = new ArrayList<DateTimeDt>();
			}
			return myCheckDates;
		}

		public Quantity getGlucoseLevel() {
			if (myGlucoseLevel == null) {
				myGlucoseLevel = new Quantity();
			}
			myGlucoseLevel.getValue();
			myGlucoseLevel.getSystem();
			myGlucoseLevel.getCode();

			return myGlucoseLevel;
		}

		public Quantity getHbA1c() {
			if (myHbA1c == null) {
				myHbA1c = new Quantity();
			}
			myHbA1c.getValue();
			myHbA1c.getSystem();
			myHbA1c.getCode();

			return myHbA1c;
		}

		public StringDt getHeight() {
			if (myHeight == null) {
				myHeight = new StringDt();
			}
			return myHeight;
		}

		public Quantity getInsulinLevel() {
			if (myInsulinLevel == null) {
				myInsulinLevel = new Quantity();
			}
			myInsulinLevel.getValue();
			myInsulinLevel.getSystem();
			myInsulinLevel.getCode();

			return myInsulinLevel;
		}

		public StringDt getWeight() {
			if (myWeight == null) {
				myWeight = new StringDt();
			}
			return myWeight;
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() && ElementUtil.isEmpty(myInsulinLevel, myGlucoseLevel, myHbA1c, myBloodPressure, myCholesterol, myWeight, myHeight, myCheckDates);
		}

		public void setBloodPressure(Quantity bloodPressure) {
			myBloodPressure = bloodPressure;
			myBloodPressure.setValue(110);
			myBloodPressure.setSystem("http://unitsofmeasure.org");
			myBloodPressure.setCode("mmHg");
		}

		public void setCheckDates(List<DateTimeDt> theCheckDates) {
			myCheckDates = theCheckDates;
			myCheckDates.add(new DateTimeDt("2010-01-02"));
		}

		public void setCholesterol(Quantity cholesterol) {
			myCholesterol = cholesterol;
			myCholesterol.setValue(2);
			myCholesterol.setSystem("http://unitsofmeasure.org");
			myCholesterol.setCode("mmol/l");
		}

		public void setGlucoseLevel(Quantity glucoseLevel) {
			myGlucoseLevel = glucoseLevel;
			myGlucoseLevel.setValue(95);
			myGlucoseLevel.setSystem("http://unitsofmeasure.org");
			myGlucoseLevel.setCode("mg/dl");
		}

		public void setHbA1c(Quantity hba1c) {
			myHbA1c = hba1c;
			myHbA1c.setValue(48);
			myHbA1c.setSystem("http://unitsofmeasure.org");
			myHbA1c.setCode("mmol/mol");
		}

		public void setHeight(StringDt height) {
			myHeight = height;
		}

		// Setter/Getter methods
		public void setInsulinLevel(Quantity insulinLevel) {
			myInsulinLevel = insulinLevel;
			myInsulinLevel.setValue(125);
			myInsulinLevel.setSystem("http://unitsofmeasure.org");
			myInsulinLevel.setCode("pmol/l");
		}

		public void setWeight(StringDt weight) {
			myWeight = weight;
		}

	}
	
	
	@ResourceDef()
	public static class MyMedication extends Medication
	{

		private static final long serialVersionUID = 1L;
		
	}
}