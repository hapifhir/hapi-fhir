package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Profile.ExtensionDefn;
import ca.uhn.fhir.model.dstu.resource.Profile.Structure;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;

public class RuntimeResourceDefinitionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuntimeResourceDefinitionTest.class);

	@Test
	public void testToProfileStandard() throws Exception {
		FhirContext ctx = new FhirContext(Patient.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(Patient.class);

		Profile profile = def.toProfile();

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
	public void testToProfileExtensions() throws Exception {
		FhirContext ctx = new FhirContext(ResourceWithExtensionsA.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsA.class);

		Profile profile = def.toProfile();

		ourLog.info(ctx.newXmlParser().encodeResourceToString(profile));
		
		assertEquals(1, profile.getStructure().get(0).getElement().get(0).getDefinition().getType().size());
		assertEquals("Resource", profile.getStructure().get(0).getElement().get(0).getDefinition().getType().get(0).getCode().getValue());

		ExtensionDefn ext = profile.getExtensionDefn().get(1);
		assertEquals("b1/1", ext.getCode().getValue());
		assertEquals(DataTypeEnum.DATE.getCode(), ext.getDefinition().getType().get(0).getCode().getValue());
		
		ext = profile.getExtensionDefn().get(2);
		assertEquals("b1/2", ext.getCode().getValue());
		assertEquals(DataTypeEnum.EXTENSION, ext.getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("#b1/2/1", ext.getDefinition().getType().get(0).getProfile().getValueAsString());
		assertEquals(DataTypeEnum.EXTENSION, ext.getDefinition().getType().get(1).getCode().getValueAsEnum());
		assertEquals("#b1/2/2", ext.getDefinition().getType().get(1).getProfile().getValueAsString());
		
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(1).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(2).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(3).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(4).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.modifierExtension", profile.getStructure().get(0).getElement().get(5).getPath().getValue());

		assertEquals(DataTypeEnum.EXTENSION, profile.getStructure().get(0).getElement().get(1).getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("url", profile.getStructure().get(0).getElement().get(1).getSlicing().getDiscriminator().getValue());

		assertEquals(DataTypeEnum.EXTENSION, profile.getStructure().get(0).getElement().get(2).getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("#f1", profile.getStructure().get(0).getElement().get(2).getDefinition().getType().get(0).getProfile().getValueAsString());
		
		assertEquals("ResourceWithExtensionsA.identifier", profile.getStructure().get(0).getElement().get(9).getPath().getValue());

	}

}
