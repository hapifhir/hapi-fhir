package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.mdm.BaseR4Test;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerserUtilTest extends BaseR4Test {

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
	void testFieldExists() {
		assertTrue(TerserUtil.fieldExists(ourFhirContext, "identifier", new Patient()));
		assertFalse(TerserUtil.fieldExists(ourFhirContext, "randomFieldName", new Patient()));
	}

	@Test
	void testCloneFields() {
		Patient p1 = buildJohny();
		Patient p2 = new Patient();

		TerserUtil.mergeFieldsExceptIdAndMeta(ourFhirContext, p1, p2);

		assertTrue(p2.getIdentifier().isEmpty());

		assertNull(p2.getId());
		assertEquals(1, p2.getName().size());
		assertEquals(p1.getName().get(0).getNameAsSingleString(), p2.getName().get(0).getNameAsSingleString());
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

		TerserUtil.mergeField(ourFhirContext,"address", p1, p2);

		assertEquals(2, p2.getAddress().size());
		assertEquals("[10 Lenin Street]", p2.getAddress().get(0).getLine().toString());
		assertEquals("[10 Main Street]", p2.getAddress().get(1).getLine().toString());
		assertTrue(p2.getAddress().get(1).hasExtension());

		p1 = new Patient();
		p1.addAddress().addLine("10 Main Street").addExtension(ext);
		p2 = new Patient();
		p2.addAddress().addLine("10 Main Street").addExtension(new Extension("demo", new DateTimeType("2021-01-02")));

		TerserUtil.mergeField(ourFhirContext,"address", p1, p2);
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

		TerserUtil.replaceField(ourFhirContext,"address", p1, p2);

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

		TerserUtil.mergeField(ourFhirContext,"address", p1, p2);

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
}
