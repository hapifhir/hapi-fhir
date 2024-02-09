package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Claim;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

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
	void cloneIdentifierIntoResource() {
		Identifier identifier = new Identifier().setSystem("http://org.com/sys").setValue("123");

		Patient p1 = new Patient();
		p1.addIdentifier(identifier);

		Patient p2 = new Patient();
		RuntimeResourceDefinition definition = ourFhirContext.getResourceDefinition(p1);
		TerserUtil.cloneIdentifierIntoResource(ourFhirContext, definition.getChildByName("identifier"), identifier, p2);

		assertThat(p2.getIdentifier()).hasSize(1);
		assertThat(p2.getIdentifier().get(0).getSystem()).isEqualTo(p1.getIdentifier().get(0).getSystem());
		assertThat(p2.getIdentifier().get(0).getValue()).isEqualTo(p1.getIdentifier().get(0).getValue());
	}

	@Test
	void cloneIdentifierIntoResourceNoDuplicates() {
		Identifier identifier = new Identifier().setSystem("http://org.com/sys").setValue("123");

		Patient p1 = new Patient();
		p1.addIdentifier(identifier);

		Patient p2 = new Patient();
		Identifier dupIdentifier = new Identifier().setSystem("http://org.com/sys").setValue("123");
		p2.addIdentifier(dupIdentifier);
		RuntimeResourceDefinition definition = ourFhirContext.getResourceDefinition(p1);
		TerserUtil.cloneIdentifierIntoResource(ourFhirContext, definition.getChildByName("identifier"), identifier, p2);

		assertThat(p2.getIdentifier()).hasSize(1);
		assertThat(p2.getIdentifier().get(0).getSystem()).isEqualTo(p1.getIdentifier().get(0).getSystem());
		assertThat(p2.getIdentifier().get(0).getValue()).isEqualTo(p1.getIdentifier().get(0).getValue());
	}

	@Test
	void testReplaceBooleanField() {
		Patient p1 = ourFhirContext.newJsonParser().parseResource(Patient.class, SAMPLE_PERSON);

		Patient p2 = new Patient();
		TerserUtil.replaceFields(ourFhirContext, p1, p2, TerserUtil.EXCLUDE_IDS_AND_META);

		assertThat(p2.hasDeceased()).isTrue();
		assertThat("true").isEqualTo(p2.getDeceased());
		assertThat(p2.getExtension()).hasSize(2);
	}

	@Test
	void testMergeBooleanField() {
		Patient p1 = ourFhirContext.newJsonParser().parseResource(Patient.class, SAMPLE_PERSON);

		Patient p2 = new Patient();
		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);

		assertThat(p2.hasDeceased()).isTrue();
		assertThat(p2.getDeceased()).isEqualTo(new BooleanType(true));
		assertThat(p2.getExtension()).hasSize(2);
	}

	@Test
	void testCloneContainedResource() {
		Patient p1 = ourFhirContext.newJsonParser().parseResource(Patient.class, SAMPLE_PERSON);

		Patient p2 = new Patient();
		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);

		Organization org1 = (Organization) p1.getContained().get(0);
		Organization org2 = (Organization) p2.getContained().get(0);
		assertThat(org2).isNotEqualTo(org1);
		assertThat(org1.getName()).isEqualTo("BUILDERS FIRST SOURCE");
		assertThat(org2.getName()).isEqualTo("BUILDERS FIRST SOURCE");
	}

	@Test
	void cloneIdentifierIntoResourceViaHelper() {
		TerserUtilHelper p1Helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
		p1Helper.setField("identifier.system", "http://org.com/sys");
		p1Helper.setField("identifier.value", "123");

		Patient p1 = p1Helper.getResource();
		assertThat(p1.getIdentifier()).hasSize(1);

		TerserUtilHelper p2Helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
		RuntimeResourceDefinition definition = p1Helper.getResourceDefinition();

		TerserUtil.cloneIdentifierIntoResource(ourFhirContext, definition.getChildByName("identifier"),
			 p1.getIdentifier().get(0), p2Helper.getResource());

		assertThat(p2Helper.getFieldValues("identifier")).hasSize(1);

		Identifier id1 = (Identifier) p1Helper.getFieldValues("identifier").get(0);
		Identifier id2 = (Identifier) p2Helper.getFieldValue("identifier");
		assertThat(id1.equalsDeep(id2)).isTrue();
		assertThat(id1.equals(id2)).isFalse();

		assertThat(p2Helper.getFieldValue("address")).isNull();
	}

	@Test
	void testSetFieldsViaHelper() {
		TerserUtilHelper p1Helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
		p1Helper.setField("active", "boolean", "true");
		p1Helper.setField("birthDate", "date", "1999-01-01");
		p1Helper.setField("gender", "code", "male");

		Patient p = p1Helper.getResource();
		assertThat(p.getActive()).isTrue();
		assertThat(p.getGender()).isEqualTo(Enumerations.AdministrativeGender.MALE);

		DateType check = TerserUtil.newElement(ourFhirContext, "date", "1999-01-01");
		assertThat(p.getBirthDate()).isEqualTo(check.getValue());
	}


	@Test
	void testFieldExists() {
		assertThat(TerserUtil.fieldExists(ourFhirContext, "identifier", TerserUtil.newResource(ourFhirContext, "Patient"))).isTrue();
		assertThat(TerserUtil.fieldExists(ourFhirContext, "randomFieldName", TerserUtil.newResource(ourFhirContext, "Patient"))).isFalse();
	}

	@Test
	void testCloneFields() {
		Patient p1 = new Patient();
		p1.addName().addGiven("Sigizmund");

		Patient p2 = new Patient();

		TerserUtil.mergeFieldsExceptIdAndMeta(ourFhirContext, p1, p2);

		assertThat(p2.getIdentifier()).isEmpty();

		assertThat(p2.getId()).isNull();
		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getNameAsSingleString()).isEqualTo(p1.getName().get(0).getNameAsSingleString());
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

		assertThat(p2.getIdentifier()).hasSize(4);
		assertThat(p2.getName()).isEmpty();
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

		assertThat(p2.getIdentifier()).hasSize(4);
		assertThat(p2.getName()).isEmpty();
	}

	@Test
	void testCloneWithNonPrimitves() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName().addGiven("Joe");
		p1.getNameFirstRep().addGiven("George");
		assertThat(p1.getName()).hasSize(1);
		assertThat(p1.getName().get(0).getGiven()).hasSize(2);

		p2.addName().addGiven("Jeff");
		p2.getNameFirstRep().addGiven("George");
		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getGiven()).hasSize(2);

		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);
		assertThat(p2.getName()).hasSize(2);
		assertThat(p2.getName().get(0).getGiven()).hasSize(2);
		assertThat(p2.getName().get(1).getGiven()).hasSize(2);
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

		assertThat(p2.getAddress()).hasSize(2);
		assertThat(p2.getAddress().get(0).getLine().toString()).isEqualTo("[10 Lenin Street]");
		assertThat(p2.getAddress().get(1).getLine().toString()).isEqualTo("[10 Main Street]");
		assertThat(p2.getAddress().get(1).hasExtension()).isTrue();

		p1 = new Patient();
		p1.addAddress().addLine("10 Main Street").addExtension(ext);
		p2 = new Patient();
		p2.addAddress().addLine("10 Main Street").addExtension(new Extension("demo", new DateTimeType("2021-01-02")));

		TerserUtil.mergeField(ourFhirContext, "address", p1, p2);
		assertThat(p2.getAddress()).hasSize(2);
		assertThat(p2.getAddress().get(0).hasExtension()).isTrue();
		assertThat(p2.getAddress().get(1).hasExtension()).isTrue();

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

		assertThat(p2.getAddress()).hasSize(1);
		assertThat(p2.getAddress().get(0).getLine().toString()).isEqualTo("[10 Main Street]");
		assertThat(p2.getAddress().get(0).hasExtension()).isTrue();
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

		assertThat(p2.getAddress()).hasSize(2);
		assertThat(p2.getAddress().get(0).getLine().toString()).isEqualTo("[10 Main Street]");
		assertThat(p2.getAddress().get(1).getLine().toString()).isEqualTo("[10 Main Street]");
		assertThat(p2.getAddress().get(1).hasExtension()).isTrue();
	}


	@Test
	void testCloneWithDuplicateNonPrimitives() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName().addGiven("Jim");
		p1.getNameFirstRep().addGiven("George");

		assertThat(p1.getName()).hasSize(1);
		assertThat(p1.getName().get(0).getGiven()).hasSize(2);

		p2.addName().addGiven("Jim");
		p2.getNameFirstRep().addGiven("George");

		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getGiven()).hasSize(2);

		TerserUtil.mergeAllFields(ourFhirContext, p1, p2);

		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getGiven()).hasSize(2);
	}


	@Test
	void testEqualsFunction() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName(new HumanName().setFamily("family").addGiven("asd"));
		p2.addName(new HumanName().setFamily("family").addGiven("asd"));

		assertThat(TerserUtil.equals(p1, p2)).isTrue();
	}

	@Test
	void testEqualsFunctionNotEqual() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();

		p1.addName(new HumanName().setFamily("family").addGiven("asd"));
		p2.addName(new HumanName().setFamily("family").addGiven("asd1"));

		assertThat(TerserUtil.equals(p1, p2)).isFalse();
	}

	@Test
	void testHasValues() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");

		assertThat(TerserUtil.hasValues(ourFhirContext, p1, "name")).isTrue();
		assertThat(TerserUtil.hasValues(ourFhirContext, p1, "address")).isFalse();
	}

	@Test
	void testGetValues() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");

		assertThat(((HumanName) TerserUtil.getValueFirstRep(ourFhirContext, p1, "name")).getFamily()).isEqualTo("Doe");
		assertThat(TerserUtil.getValues(ourFhirContext, p1, "name")).isNotEmpty();
		assertThat(TerserUtil.getValues(ourFhirContext, p1, "whoaIsThatReal")).isNull();
		assertThat(TerserUtil.getValueFirstRep(ourFhirContext, p1, "whoaIsThatReal")).isNull();
	}

	@Test
	public void testReplaceFields() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");
		Patient p2 = new Patient();
		p2.addName().setFamily("Smith");

		TerserUtil.replaceField(ourFhirContext, "name", p1, p2);

		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getFamily()).isEqualTo("Doe");
	}

	@Test
	public void testReplaceFields_SameValues() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");
		Patient p2 = new Patient();
		p2.setName(p1.getName());

		TerserUtil.replaceField(ourFhirContext, "name", p1, p2);

		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getFamily()).isEqualTo("Doe");
	}

	@Test
	public void testReplaceFieldByEmptyValue() {
		Patient p1 = new Patient();
		Patient p2 = new Patient();
		p2.setActive(true);

		TerserUtil.replaceField(ourFhirContext, "active", p1, p2);

		// expect p2 to have 'active removed'
		assertThat(p2.hasActive()).isFalse();
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
		assertThat(p2.getName()).hasSize(1);
		assertThat(p2.getName().get(0).getFamily()).isEqualTo("Doe");

		assertThat(p2.getGender()).isEqualTo(Enumerations.AdministrativeGender.MALE);
		assertThat(p2.getBirthDate()).isEqualTo(dob);
	}

	@Test
	public void testClearFields() {
		{
			Patient p1 = new Patient();
			p1.addName().setFamily("Doe");
			assertThat(p1.getName()).hasSize(1);

			TerserUtil.clearField(ourFhirContext, p1, "name");

			assertThat(p1.getName()).isEmpty();
		}

		{
			Address a1 = new Address();
			a1.addLine("Line 1");
			a1.addLine("Line 2");
			assertThat(a1.getLine()).hasSize(2);
			a1.setCity("Test");
			TerserUtil.clearField(ourFhirContext, "line", a1);

			assertThat(a1.getLine()).isEmpty();
			assertThat(a1.getCity()).isEqualTo("Test");
		}
	}

	@Test
	public void testClearFieldByFhirPath() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe");
		assertThat(p1.getName()).hasSize(1);

		TerserUtil.clearFieldByFhirPath(ourFhirContext, p1, "name");

		assertThat(p1.getName()).isEmpty();

		Address a1 = new Address();
		a1.addLine("Line 1");
		a1.addLine("Line 2");
		assertThat(a1.getLine()).hasSize(2);
		a1.setCity("Test");
		a1.getPeriod().setStartElement(new DateTimeType("2021-01-01"));
		p1.addAddress(a1);

		assertThat(p1.getAddress().get(0).getPeriod().getStartElement().toHumanDisplay()).isEqualTo("2021-01-01");
		assertThat(p1.getAddress().get(0).getPeriod().getStart()).isNotNull();

		Address a2 = new Address();
		a2.addLine("Line 1");
		a2.addLine("Line 2");
		a2.setCity("Test");
		a2.getPeriod().setStartElement(new DateTimeType("2021-01-01"));
		p1.addAddress(a2);


		TerserUtil.clearFieldByFhirPath(ourFhirContext, p1, "address.line");
		TerserUtil.clearFieldByFhirPath(ourFhirContext, p1, "address.period.start");

		assertThat(p1.getAddress().get(0).getPeriod().getStart()).isNull();

		assertThat(p1.getAddress()).hasSize(2);
		assertThat(p1.getAddress().get(0).getLine()).isEmpty();
		assertThat(p1.getAddress().get(1).getLine()).isEmpty();
		assertThat(p1.getAddress().get(0).getCity()).isEqualTo("Test");
		assertThat(p1.getAddress().get(1).getCity()).isEqualTo("Test");
	}

	@Test
	void testRemoveByFhirPath() {
		// arrange
		Claim claimWithReferences = createClaim();
		claimWithReferences.setPatient(new Reference("Patient/123"));
		String fhirPath = "patient";
		assertThat(claimWithReferences.hasPatient()).isTrue();
		//act
		TerserUtil.clearFieldByFhirPath(ourFhirContext, claimWithReferences, fhirPath);
		//assert
		assertThat(claimWithReferences.hasPatient()).isFalse();
	}

	static Claim createClaim() {
		Claim claim = new Claim();
		claim.setStatus(Claim.ClaimStatus.ACTIVE);
		return claim;
	}

	@Test
	public void testSetField() {
		Patient p1 = new Patient();

		Address address = new Address();
		address.setCity("CITY");

		TerserUtil.setField(ourFhirContext, "address", p1, address);

		assertThat(p1.getAddress()).hasSize(1);
		assertThat(p1.getAddress().get(0).getCity()).isEqualTo("CITY");
	}

	@Test
	public void testSetFieldByFhirPath() {
		Patient p1 = new Patient();

		Address address = new Address();
		address.setCity("CITY");

		TerserUtil.setFieldByFhirPath(ourFhirContext, "address", p1, address);

		assertThat(p1.getAddress()).hasSize(1);
		assertThat(p1.getAddress().get(0).getCity()).isEqualTo("CITY");
	}

	@Test
	public void testSetFieldByCompositeFhirPath() {
		Patient p1 = new Patient();

		TerserUtil.setFieldByFhirPath(ourFhirContext, "address.city", p1, new StringType("CITY"));

		assertThat(p1.getAddress()).hasSize(1);
		assertThat(p1.getAddress().get(0).getCity()).isEqualTo("CITY");
	}

	@Test
	public void testClone() {
		Patient p1 = new Patient();
		p1.addName().setFamily("Doe").addGiven("Joe");

		Patient p2 = TerserUtil.clone(ourFhirContext, p1);

		assertThat(p2.getName().get(0).getNameAsSingleString()).isEqualTo(p1.getName().get(0).getNameAsSingleString());
		assertThat(p1.equalsDeep(p2)).isTrue();
	}

	@Test
	public void testNewElement() {
		assertThat((IBase)TerserUtil.newElement(ourFhirContext, "string")).isNotNull();
		assertThat(((PrimitiveType) TerserUtil.newElement(ourFhirContext, "integer", "1")).getValue()).isEqualTo(1);

		assertThat((IBase)TerserUtil.newElement(ourFhirContext, "string")).isNotNull();
		assertThat(((PrimitiveType) TerserUtil.newElement(ourFhirContext, "integer")).getValue()).isNull();

		assertThat((IBase)TerserUtil.newElement(ourFhirContext, "string", null)).isNotNull();
		assertThat(((PrimitiveType) TerserUtil.newElement(ourFhirContext, "integer", null)).getValue()).isNull();
	}

	@Test
	public void testNewResource() {
		assertThat((IBase)TerserUtil.newResource(ourFhirContext, "Patient")).isNotNull();
		assertThat((IBase)TerserUtil.newResource(ourFhirContext, "Patient", null)).isNotNull();
	}

	@Test
	public void testInstantiateBackboneElement() {
		IBaseBackboneElement patientContact = TerserUtil.instantiateBackboneElement(ourFhirContext, "Patient", "contact");
		assertThat(patientContact).isNotNull();
		assertThat(patientContact.getClass()).isEqualTo(Patient.ContactComponent.class);
		assertThat(patientContact.isEmpty()).isTrue();
	}

}
