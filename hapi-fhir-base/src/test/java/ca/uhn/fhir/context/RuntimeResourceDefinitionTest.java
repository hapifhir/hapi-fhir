package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Profile.ExtensionDefn;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;

public class RuntimeResourceDefinitionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RuntimeResourceDefinitionTest.class);

	@Test
	public void testToProfileStandard() {
		FhirContext ctx = new FhirContext(Patient.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(Patient.class);

		Profile profile = def.toProfile();

		ourLog.info(ctx.newXmlParser().encodeResourceToString(profile));
		
	}
	
	@Test
	public void testToProfileExtensions() {
		FhirContext ctx = new FhirContext(ResourceWithExtensionsA.class, Profile.class);
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsA.class);

		Profile profile = def.toProfile();

		ourLog.debug(ctx.newXmlParser().encodeResourceToString(profile));
		
		assertEquals(1, profile.getStructure().get(0).getElement().get(0).getDefinition().getType().size());
		assertEquals("Resource", profile.getStructure().get(0).getElement().get(0).getDefinition().getType().get(0).getCode().getValue());

		ExtensionDefn ext = profile.getExtensionDefn().get(0);
		assertEquals("1/1", ext.getCode().getValue());
		assertEquals(DataTypeEnum.DATE.getCode(), ext.getDefinition().getType().get(0).getCode().getValue());
		
		ext = profile.getExtensionDefn().get(1);
		assertEquals("1/2", ext.getCode().getValue());
		assertEquals(DataTypeEnum.EXTENSION, ext.getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("#1/2/1", ext.getDefinition().getType().get(0).getProfile().getValueAsString());
		assertEquals(DataTypeEnum.EXTENSION, ext.getDefinition().getType().get(1).getCode().getValueAsEnum());
		assertEquals("#1/2/2", ext.getDefinition().getType().get(1).getProfile().getValueAsString());
		
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(1).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(2).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(3).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(4).getPath().getValue());
		assertEquals("ResourceWithExtensionsA.extension", profile.getStructure().get(0).getElement().get(5).getPath().getValue());

		assertEquals(DataTypeEnum.EXTENSION, profile.getStructure().get(0).getElement().get(1).getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("url", profile.getStructure().get(0).getElement().get(1).getSlicing().getDiscriminator().getValue());

		assertEquals(DataTypeEnum.EXTENSION, profile.getStructure().get(0).getElement().get(2).getDefinition().getType().get(0).getCode().getValueAsEnum());
		assertEquals("f1", profile.getStructure().get(0).getElement().get(2).getDefinition().getType().get(0).getProfile().getValueAsString());
		
		assertEquals("ResourceWithExtensionsA.identifier", profile.getStructure().get(0).getElement().get(6).getPath().getValue());

	}

}
