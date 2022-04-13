package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerserUtilTest {

	private FhirContext ourFhirContext = FhirContext.forR4();
	private static final String SAMPLE_PERSON =
		"""
			{
			      "resourceType": "Patient",
			      "extension": [
			        {
			          "url": "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
			          "valueCoding": {
			            "system": "MyInternalRace",
			            "code": "X",
			            "display": "Eks"
			          }
			        },
			        {
			          "url": "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity'",
			          "valueCoding": {
			            "system": "MyInternalEthnicity",
			            "display": "NNN"
			          }
			        }
			      ],
			      "identifier": [
			        {
			          "system": "http://example.org/member_id",
			          "value": "123123"
			        },
			        {
			          "system": "http://example.org/medicaid_id",
			          "value": "12312323123Z"
			        },
			        {
			          "system": "http://example.org/CDNS_id",
			          "value": "123123123E"
			        },
			        {
			          "system": "http://example.org/SSN"
			        }
			      ],
			      "active": true,
			      "name": [
			        {
			          "family": "TestFamily",
			          "given": [
			            "Given"
			          ]
			        }
			      ],
			      "telecom": [
			        {
			          "system": "email",
			          "value": "email@email.io"
			        },
			        {
			          "system": "phone",
			          "value": "123123231"
			        },
			        {
			          "system": "phone",
			          "value": "1231232312"
			        },
			        {
			          "system": "phone",
			          "value": "1231232314"
			        }
			      ],
			      "gender": "male",
			      "birthDate": "1900-01-01",
			      "deceasedBoolean": true,
			       "contained": [
			              {
			                  "id": "1",
			                  "identifier": [
			                      {
			                          "system": "urn:hssc:srhs:contact:organizationId",
			                          "value": "1000"
			                      }
			                  ],
			                  "name": "BUILDERS FIRST SOURCE",
			                  "resourceType": "Organization"
			              }
			          ]
			    }
				""";

	@Test
	void testCloneEidIntoResource() {
		Identifier identifier = new Identifier().setSystem("http://org.com/sys").setValue("123");

		Patient p1 = new Patient();
		p1.addIdentifier(identifier);

		Patient p2 = new Patient();
		RuntimeResourceDefinition definition = ourFhirContext.getResourceDefinition(p1);
		TerserUtil.cloneEidIntoResource(ourFhirContext, definition.getChildByName("identifier"), identifier, p2);

		assertEquals(1, p2.getIdentifier().size());
		assertEquals(p1.getIdentifier().get(0).getSystem(), p2.getIdentifier().get(0).getSystem());
		assertEquals(p1.getIdentifier().get(0).getValue(), p2.getIdentifier().get(0).getValue());
	}

	@Test
	void testReplaceBooleanField() {
		Patient p1 = ourFhirContext.newJsonParser().parseResource(Patient.class, SAMPLE_PERSON);

		Patient p2 = new Patient();
		TerserUtil.replaceFields(ourFhirContext, p1, p2, TerserUtil.EXCLUDE_IDS_AND_META);

		assertTrue(p2.hasDeceased());
		assertTrue("true".equals(p2.getDeceased().primitiveValue()));
		assertEquals(2, p2.getExtension().size());
	}

	@Test
	void testMergeBooleanField() {
		Patient p1 = ourFhirContext.newJsonParser().parseResource(Patient.class, SAMPLE_PERSON);

		Patient p2 = new Patient();
		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);

		assertTrue(p2.hasDeceased());
		assertTrue("true".equals(p2.getDeceased().primitiveValue()));
		assertEquals(2, p2.getExtension().size());
	}

	@Test
	void testCloneContainedResource() {
		Patient p1 = ourFhirContext.newJsonParser().parseResource(Patient.class, SAMPLE_PERSON);

		Patient p2 = new Patient();
		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);

		Organization org1 = (Organization) p1.getContained().get(0);
		Organization org2 = (Organization) p2.getContained().get(0);
		assertNotEquals(org1, org2);
		assertEquals("BUILDERS FIRST SOURCE", org1.getName());
		assertEquals("BUILDERS FIRST SOURCE", org2.getName());
	}

	@Test
	void testCloneEidIntoResourceViaHelper() {
		TerserUtilHelper p1Helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
		p1Helper.setField("identifier.system", "http://org.com/sys");
		p1Helper.setField("identifier.value", "123");

		Patient p1 = p1Helper.getResource();
		assertEquals(1, p1.getIdentifier().size());

		TerserUtilHelper p2Helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
		RuntimeResourceDefinition definition = p1Helper.getResourceDefinition();

		TerserUtil.cloneEidIntoResource(ourFhirContext, definition.getChildByName("identifier"),
			p1.getIdentifier().get(0), p2Helper.getResource());

		assertEquals(1, p2Helper.getFieldValues("identifier").size());

		Identifier id1 = (Identifier) p1Helper.getFieldValues("identifier").get(0);
		Identifier id2 = (Identifier) p2Helper.getFieldValue("identifier");
		assertTrue(id1.equalsDeep(id2));
		assertFalse(id1.equals(id2));

		assertNull(p2Helper.getFieldValue("address"));
	}

	@Test
	void testSetFieldsViaHelper() {
		TerserUtilHelper p1Helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
		p1Helper.setField("active", "boolean", "true");
		p1Helper.setField("birthDate", "date", "1999-01-01");
		p1Helper.setField("gender", "code", "male");

		Patient p = p1Helper.getResource();
		assertTrue(p.getActive());
		assertEquals(Enumerations.AdministrativeGender.MALE, p.getGender());

		DateType check = TerserUtil.newElement(ourFhirContext, "date", "1999-01-01");
		assertEquals(check.getValue(), p.getBirthDate());
	}


	@Test
	void testFieldExists() {
		assertTrue(TerserUtil.fieldExists(ourFhirContext, "identifier", TerserUtil.newResource(ourFhirContext, "Patient")));
		assertFalse(TerserUtil.fieldExists(ourFhirContext, "randomFieldName", TerserUtil.newResource(ourFhirContext, "Patient")));
	}

	@Test
	void testCloneFields() {
		Patient p1 = new Patient();
		p1.addName().addGiven("Sigizmund");

		Patient p2 = new Patient();

		TerserUtil.mergeFieldsExceptIdAndMeta(ourFhirContext, p1, p2);

		assertTrue(p2.getIdentifier().isEmpty());

		assertNull(p2.getId());
		assertEquals(1, p2.getName().size());
		assertEquals(p1.getName().get(0).getNameAsSingleString(), p2.getName().get(0).getNameAsSingleString());
	}

	@Test
	void testCloneIdentifiers() {
		Patient p1 = new Patient();
		p1.addIdentifier(new Identifier().setSystem("uri:mi").setValue("123456"));
		p1.addIdentifier(new Identifier().setSystem("uri:mdi").setValue("287351247K"));
		p1.addIdentifier(new Identifier().setSystem("uri:cdns").setValue("654841918"));
		p1.addIdentifier(new Identifier().setSystem("uri:ssn").setValue("855191882"));
		p1.addName().setFamily("Sat").addGiven("Joe");

		Patient p2 = new Patient();
		TerserUtil.mergeField(ourFhirContext, ourFhirContext.newTerser(), "identifier", p1, p2);

		assertEquals(4, p2.getIdentifier().size());
		assertTrue(p2.getName().isEmpty());
	}

	@Test
	void testReplaceIdentifiers() {
		Patient p1 = new Patient();
		p1.addIdentifier(new Identifier().setSystem("uri:mi").setValue("123456"));
		p1.addIdentifier(new Identifier().setSystem("uri:mdi").setValue("287351247K"));
		p1.addIdentifier(new Identifier().setSystem("uri:cdns").setValue("654841918"));
		p1.addIdentifier(new Identifier().setSystem("uri:ssn").setValue("855191882"));
		p1.addName().setFamily("Sat").addGiven("Joe");

		Patient p2 = new Patient();
		TerserUtil.replaceField(ourFhirContext, "identifier", p1, p2);

		assertEquals(4, p2.getIdentifier().size());
		assertTrue(p2.getName().isEmpty());
	}

	@Test
	void testCloneWithNonPrimitves() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName().addGiven("Joe");
		p1.getNameFirstRep().addGiven("George");
		assertThat(p1.getName(), hasSize(1));
		assertThat(p1.getName().get(0).getGiven(), hasSize(2));

		p2.addName().addGiven("Jeff");
		p2.getNameFirstRep().addGiven("George");
		assertThat(p2.getName(), hasSize(1));
		assertThat(p2.getName().get(0).getGiven(), hasSize(2));

		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);
		assertThat(p2.getName(), hasSize(2));
		assertThat(p2.getName().get(0).getGiven(), hasSize(2));
		assertThat(p2.getName().get(1).getGiven(), hasSize(2));
	}

	@Test
	void testMergeForAddressWithExtensions() {
		Extension ext = new Extension();
		ext.setUrl("http://hapifhir.io/extensions/address#create-timestamp");
		ext.setValue(new DateTimeType("2021-01-02T11:13:15"));

		Patient p1 = new Patient();
		p1.addAddress()
			.addLine("10 Main Street")
			.setCity("Hamilton")
			.setState("ON")
			.setPostalCode("Z0Z0Z0")
			.setCountry("Canada")
			.addExtension(ext);

		Patient p2 = new Patient();
		p2.addAddress().addLine("10 Lenin Street").setCity("Severodvinsk").setCountry("Russia");

		TerserUtil.mergeField(ourFhirContext, "address", p1, p2);

		assertEquals(2, p2.getAddress().size());
		assertEquals("[10 Lenin Street]", p2.getAddress().get(0).getLine().toString());
		assertEquals("[10 Main Street]", p2.getAddress().get(1).getLine().toString());
		assertTrue(p2.getAddress().get(1).hasExtension());

		p1 = new Patient();
		p1.addAddress().addLine("10 Main Street").addExtension(ext);
		p2 = new Patient();
		p2.addAddress().addLine("10 Main Street").addExtension(new Extension("demo", new DateTimeType("2021-01-02")));

		TerserUtil.mergeField(ourFhirContext, "address", p1, p2);
		assertEquals(2, p2.getAddress().size());
		assertTrue(p2.getAddress().get(0).hasExtension());
		assertTrue(p2.getAddress().get(1).hasExtension());

	}

	@Test
	void testReplaceForAddressWithExtensions() {
		Extension ext = new Extension();
		ext.setUrl("http://hapifhir.io/extensions/address#create-timestamp");
		ext.setValue(new DateTimeType("2021-01-02T11:13:15"));

		Patient p1 = new Patient();
		p1.addAddress()
			.addLine("10 Main Street")
			.setCity("Hamilton")
			.setState("ON")
			.setPostalCode("Z0Z0Z0")
			.setCountry("Canada")
			.addExtension(ext);

		Patient p2 = new Patient();
		p2.addAddress().addLine("10 Lenin Street").setCity("Severodvinsk").setCountry("Russia");

		TerserUtil.replaceField(ourFhirContext, "address", p1, p2);

		assertEquals(1, p2.getAddress().size());
		assertEquals("[10 Main Street]", p2.getAddress().get(0).getLine().toString());
		assertTrue(p2.getAddress().get(0).hasExtension());
	}

	@Test
	void testMergeForSimilarAddresses() {
		Extension ext = new Extension();
		ext.setUrl("http://hapifhir.io/extensions/address#create-timestamp");
		ext.setValue(new DateTimeType("2021-01-02T11:13:15"));

		Patient p1 = new Patient();
		p1.addAddress()
			.addLine("10 Main Street")
			.setCity("Hamilton")
			.setState("ON")
			.setPostalCode("Z0Z0Z0")
			.setCountry("Canada")
			.addExtension(ext);

		Patient p2 = new Patient();
		p2.addAddress()
			.addLine("10 Main Street")
			.setCity("Hamilton")
			.setState("ON")
			.setPostalCode("Z0Z0Z1")
			.setCountry("Canada")
			.addExtension(ext);

		TerserUtil.mergeField(ourFhirContext, "address", p1, p2);

		assertEquals(2, p2.getAddress().size());
		assertEquals("[10 Main Street]", p2.getAddress().get(0).getLine().toString());
		assertEquals("[10 Main Street]", p2.getAddress().get(1).getLine().toString());
		assertTrue(p2.getAddress().get(1).hasExtension());
	}


	@Test
	void testCloneWithDuplicateNonPrimitives() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName().addGiven("Jim");
		p1.getNameFirstRep().addGiven("George");

		assertThat(p1.getName(), hasSize(1));
		assertThat(p1.getName().get(0).getGiven(), hasSize(2));

		p2.addName().addGiven("Jim");
		p2.getNameFirstRep().addGiven("George");

		assertThat(p2.getName(), hasSize(1));
		assertThat(p2.getName().get(0).getGiven(), hasSize(2));

		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);

		assertThat(p2.getName(), hasSize(1));
		assertThat(p2.getName().get(0).getGiven(), hasSize(2));
	}


	@Test
	void testEqualsFunction() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName(new HumanName().setFamily("family").addGiven("asd"));
		p2.addName(new HumanName().setFamily("family").addGiven("asd"));

		assertTrue(TerserUtil.equals(p1, p2));
	}

	@Test
	void testEqualsFunctionNotEqual() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName(new HumanName().setFamily("family").addGiven("asd"));
		p2.addName(new HumanName().setFamily("family").addGiven("asd1"));

		assertFalse(TerserUtil.equals(p1, p2));
	}

	@Test
	void testHasValues() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");

		assertTrue(TerserUtil.hasValues(ourFhirContext, p1, "name"));
		assertFalse(TerserUtil.hasValues(ourFhirContext, p1, "address"));
	}

	@Test
	void testGetValues() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");

		assertEquals("Doe", ((HumanName) TerserUtil.getValueFirstRep(ourFhirContext, p1, "name")).getFamily());
		assertFalse(TerserUtil.getValues(ourFhirContext, p1, "name").isEmpty());
		assertNull(TerserUtil.getValues(ourFhirContext, p1, "whoaIsThatReal"));
		assertNull(TerserUtil.getValueFirstRep(ourFhirContext, p1, "whoaIsThatReal"));
	}

	@Test
	public void testReplaceFields() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");
		Patient p2 = new Patient();
		p2.addName().setFamily("Smith");

		TerserUtil.replaceField(ourFhirContext, "name", p1, p2);

		assertEquals(1, p2.getName().size());
		assertEquals("Doe", p2.getName().get(0).getFamily());
	}

	@Test
	public void testReplaceFields_SameValues() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");
		Patient p2 = new Patient();
		p2.setName(p1.getName());

		TerserUtil.replaceField(ourFhirContext, "name", p1, p2);

		assertEquals(1, p2.getName().size());
		assertEquals("Doe", p2.getName().get(0).getFamily());
	}

	@Test
	public void testReplaceFieldsByPredicate() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");
		p1.setGender(Enumerations.AdministrativeGender.MALE);

		Patient p2 = new Patient();
		p2.addName().setFamily("Smith");
		Date dob = new Date();
		p2.setBirthDate(dob);

		TerserUtil.replaceFieldsByPredicate(ourFhirContext, p1, p2, TerserUtil.EXCLUDE_IDS_META_AND_EMPTY);

		// expect p2 to have "Doe" and MALE after replace
		assertEquals(1, p2.getName().size());
		assertEquals("Doe", p2.getName().get(0).getFamily());

		assertEquals(Enumerations.AdministrativeGender.MALE, p2.getGender());
		assertEquals(dob, p2.getBirthDate());
	}

	@Test
	public void testClearFields() {
		{
			Patient p1 = new Patient();
			p1.addName().setFamily("Doe");

			TerserUtil.clearField(ourFhirContext, p1, "name");

			assertEquals(0, p1.getName().size());
		}

		{
			Address a1 = new Address();
			a1.addLine("Line 1");
			a1.addLine("Line 2");
			a1.setCity("Test");
			TerserUtil.clearField(ourFhirContext, "line", a1);

			assertEquals(0, a1.getLine().size());
			assertEquals("Test", a1.getCity());
		}
	}

	@Test
	public void testSetField() {
		Patient p1 = new Patient();

		Address address = new Address();
		address.setCity("CITY");

		TerserUtil.setField(ourFhirContext, "address", p1, address);

		assertEquals(1, p1.getAddress().size());
		assertEquals("CITY", p1.getAddress().get(0).getCity());
	}

	@Test
	public void testSetFieldByFhirPath() {
		Patient p1 = new Patient();

		Address address = new Address();
		address.setCity("CITY");

		TerserUtil.setFieldByFhirPath(ourFhirContext, "address", p1, address);

		assertEquals(1, p1.getAddress().size());
		assertEquals("CITY", p1.getAddress().get(0).getCity());
	}

	@Test
	public void testClone() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe").addGiven("Joe");

		Patient p2 = TerserUtil.clone(ourFhirContext, p1);

		assertEquals(p1.getName().get(0).getNameAsSingleString(), p2.getName().get(0).getNameAsSingleString());
		assertTrue(p1.equalsDeep(p2));
	}

	@Test
	public void testNewElement() {
		assertNotNull(TerserUtil.newElement(ourFhirContext, "string"));
		assertEquals(1, ((PrimitiveType) TerserUtil.newElement(ourFhirContext, "integer", "1")).getValue());

		assertNotNull(TerserUtil.newElement(ourFhirContext, "string"));
		assertNull(((PrimitiveType) TerserUtil.newElement(ourFhirContext, "integer")).getValue());

		assertNotNull(TerserUtil.newElement(ourFhirContext, "string", null));
		assertNull(((PrimitiveType) TerserUtil.newElement(ourFhirContext, "integer", null)).getValue());
	}

	@Test
	public void testNewResource() {
		assertNotNull(TerserUtil.newResource(ourFhirContext, "Patient"));
		assertNotNull(TerserUtil.newResource(ourFhirContext, "Patient", null));
	}

}
