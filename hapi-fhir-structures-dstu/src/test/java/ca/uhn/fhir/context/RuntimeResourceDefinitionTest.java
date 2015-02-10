package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Profile.ExtensionDefn;
import ca.uhn.fhir.model.dstu.resource.Profile.Structure;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureElement;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RuntimeResourceDefinitionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuntimeResourceDefinitionTest.class);

	@Test
	public void testProfileIdIsActualResourceName() {
		FhirContext ctx = new FhirContext(CustomObservation.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(CustomObservation.class);

		Profile profile = (Profile) def.toProfile("http://foo.org/fhir");

		assertEquals("customobservation", profile.getId().toString());
	}
	
	@Test
	public void testToProfileExtensions() throws Exception {
		FhirContext ctx = new FhirContext(ResourceWithExtensionsA.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsA.class);

		Profile profile = (Profile) def.toProfile("http://foo.org/fhir");

		ourLog.info(ctx.newXmlParser().encodeResourceToString(profile));
		
		List<StructureElement> element = profile.getStructure().get(0).getElement();
		assertEquals(1, element.get(0).getDefinition().getType().size());
		assertEquals("Resource", element.get(0).getDefinition().getType().get(0).getCode().getValue());

		ExtensionDefn ext = profile.getExtensionDefn().get(1);
		assertEquals("b1/1", ext.getCode().getValue());
		assertEquals(DataTypeEnum.DATE.getCode(), ext.getDefinition().getType().get(0).getCode().getValue());
		
		ext = profile.getExtensionDefn().get(2);
		assertEquals("b1/2", ext.getCode().getValue());
		assertEquals(DataTypeEnum.EXTENSION, ext.getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("#b1/2/1", ext.getDefinition().getType().get(0).getProfile().getValueAsString());
		assertEquals(DataTypeEnum.EXTENSION, ext.getDefinition().getType().get(1).getCode().getValueAsEnum());
		assertEquals("#b1/2/2", ext.getDefinition().getType().get(1).getProfile().getValueAsString());
		
		assertEquals("ResourceWithExtensionsA.extension", element.get(1).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", element.get(2).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", element.get(3).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", element.get(4).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", element.get(5).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.modifierExtension", element.get(6).getPath().getValue());

		assertEquals(DataTypeEnum.EXTENSION, element.get(1).getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("url", element.get(1).getSlicing().getDiscriminator().getValue());

		assertEquals(DataTypeEnum.EXTENSION, element.get(2).getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("#f1", element.get(2).getDefinition().getType().get(0).getProfile().getValueAsString());
		
		assertEquals("ResourceWithExtensionsA.identifier", element.get(13).getPath().getValue());

	}

	
	@Test
	public void testToProfileStandard() throws Exception {
		FhirContext ctx = new FhirContext(Patient.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(Patient.class);

		Profile profile = (Profile) def.toProfile("http://foo.org/fhir");

		ourLog.info(ctx.newXmlParser().encodeResourceToString(profile));
		
		Structure struct = profile.getStructure().get(0);
		assertEquals("Patient", struct.getElement().get(0).getPath().getValue());
		assertEquals("Patient.extension", struct.getElement().get(1).getPath().getValue());
		assertEquals("Patient.modifierExtension", struct.getElement().get(2).getPath().getValue());
		assertEquals("Patient.text", struct.getElement().get(3).getPath().getValue());
		assertEquals("Patient.contained", struct.getElement().get(4).getPath().getValue());
		assertEquals("Patient.language", struct.getElement().get(5).getPath().getValue());
		
	}

	@Test
	public void testToProfileValueSet() throws Exception {
		FhirContext ctx = new FhirContext(ValueSet.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ValueSet.class);

		Profile profile = (Profile) def.toProfile("http://foo.org/fhir");

		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(profile);
		ourLog.info(encoded);
		
		assertTrue(encoded.contains("<path value=\"ValueSet.compose\"/>"));
		
	}

	@Test
	public void whenProfileAndIdAreBlank_ProfileShouldBeBlank() {
		FhirContext ctx = new FhirContext(PatientWithNoIdOrProfile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(PatientWithNoIdOrProfile.class);
		assertEquals("", def.getResourceProfile("http://foo.org/fhir"));
	}

	@Test
	public void whenResourceProfileHasNoUrl_ProfileShouldBeConstructedFromServerBaseAndProfile() {
		FhirContext ctx = new FhirContext(PatientWithShortProfile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(PatientWithShortProfile.class);
		assertEquals("http://foo.org/fhir/Profile/PatientWithShortProfile", def.getResourceProfile("http://foo.org/fhir"));
	}

	@Test
	public void whenResourceProfileHasUrl_ProfileShouldUseThat() {
		FhirContext ctx = new FhirContext(PatientWithFullProfile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(PatientWithFullProfile.class);
		assertEquals("http://bar.org/Profile/PatientWithFullProfile", def.getResourceProfile("http://foo.org/fhir"));
	}

	@Test
	public void whenResourceProfileNotSet_ProfileShouldBeConstructedFromServerBaseAndId() {
		FhirContext ctx = new FhirContext(PatientSansProfile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(PatientSansProfile.class);
		assertEquals("http://foo.org/fhir/Profile/PatientSansProfile", def.getResourceProfile("http://foo.org/fhir"));
	}

	@ResourceDef(name = "Patient", id = "PatientSansProfile")
	public static class PatientSansProfile extends Patient {
	}

	@ResourceDef(name = "Patient", id = "PatientWithFullProfileId", profile="http://bar.org/Profile/PatientWithFullProfile")
	public static class PatientWithFullProfile extends Patient {
	}

	@ResourceDef(name = "Patient")
	public static class PatientWithNoIdOrProfile extends Patient {
	}

	@ResourceDef(name = "Patient", id = "PatientWithShortProfileId", profile="PatientWithShortProfile")
	public static class PatientWithShortProfile extends Patient {
	}

}
