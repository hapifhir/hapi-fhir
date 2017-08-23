package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FhirExtensionGeneratorTest {

	private FhirExtensionGenerator generator;

	@Before
	public void setup() {
		this.generator = new FhirExtensionGenerator();
	}

	@Test
	public void generateExtensionStructureDefinition() throws Exception {
		try {
			List<StringType> contexts = new ArrayList<>();
			contexts.add(new StringType("Patient"));
			StructureDefinition extensionStrDef = generator.generateExtensionStructureDefinition("MyExtension", contexts, "An extension", "An extension definition", 0, "*", "Integer");
			validateExtensionStructureDefinition(extensionStrDef);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error generating extension definition " + e.getMessage());
		}
	}

	@Test
	public void generateExtensionElementDefinitions() throws Exception {
		try {
			List<ElementDefinition> extensions = generator.generateExtensionElementDefinitions(true, "Patient", "MyExtension", "Short description", "Long description", 0, "1", "StructureDefinition/MyExtension");
			assertNotNull(extensions);
			assertEquals(2, extensions.size());
			validateSlicingElement(extensions.get(0));
			validateExtensionReferenceElement(extensions.get(1));
		} catch (Exception e) {
			fail("Error generating resource extensions " + e.getMessage());
		}
	}

	@Test
	public void generateExtensionElementDefinitionsWithoutSlicing() throws Exception {
		try {
			List<ElementDefinition> extensions = generator.generateExtensionElementDefinitions(false, "Patient", "MyExtension", "Short description", "Long description", 0, "1", "StructureDefinition/MyExtension");
			assertNotNull(extensions);
			assertEquals(1, extensions.size());
			validateExtensionReferenceElement(extensions.get(0));
		} catch (Exception e) {
			fail("Error generating resource extensions " + e.getMessage());
		}
	}

	private void validateSlicingElement(ElementDefinition elementDefinition) {
		assertEquals("open", elementDefinition.getSlicing().getRulesElement().getCode());
		assertEquals("Patient.extension", elementDefinition.getPath());
		assertEquals(ElementDefinition.DiscriminatorType.VALUE, elementDefinition.getSlicing().getDiscriminatorFirstRep().getType());
		assertEquals("url", elementDefinition.getSlicing().getDiscriminatorFirstRep().getPath());
	}

	private void validateExtensionReferenceElement(ElementDefinition elementDefinition) {
		assertEquals("Patient.extension", elementDefinition.getPath());
		assertEquals("Patient.extension:MyExtension", elementDefinition.getId());
		assertEquals("MyExtension", elementDefinition.getSliceName());
		assertEquals("MyExtension", elementDefinition.getLabel());
		assertEquals("Short description", elementDefinition.getShort());
		assertEquals("Long description", elementDefinition.getDefinition());
		assertEquals(0, elementDefinition.getMin());
		assertEquals("1", elementDefinition.getMax());
		assertEquals("Extension", elementDefinition.getTypeFirstRep().getCode());
		assertEquals("StructureDefinition/MyExtension", elementDefinition.getTypeFirstRep().getProfile());
		assertFalse(elementDefinition.getMustSupport());
	}

	private void validateExtensionStructureDefinition(StructureDefinition sd) {
		try {
			assertEquals("MyExtension", sd.getId());
			assertEquals("StructureDefinition/MyExtension", sd.getUrl());
			assertEquals("MyExtension", sd.getName());
			assertEquals(StructureDefinition.StructureDefinitionKind.COMPLEXTYPE, sd.getKind());
			assertEquals(StructureDefinition.ExtensionContext.RESOURCE, sd.getContextType());
			assertEquals("Patient", sd.getContext().get(0).getValue());
			assertEquals("Extension", sd.getType());
			assertEquals("StructureDefinition/MyExtension", sd.getUrl());
			assertEquals(StructureDefinition.TypeDerivationRule.CONSTRAINT, sd.getDerivation());
			assertEquals(3, sd.getDifferential().getElement().size());
			ElementDefinition extensionDef = sd.getDifferential().getElement().get(0);
			assertNotNull(extensionDef);
			assertEquals("Extension", extensionDef.getId());
			assertEquals("Extension", extensionDef.getPath());
			assertEquals("Short description", extensionDef.getShort());
			assertEquals("Long description", extensionDef.getDefinition());
			ElementDefinition extensionUrl = sd.getDifferential().getElement().get(1);
			assertNotNull(extensionUrl);
			assertEquals("Extension.url", extensionUrl.getId());
			assertEquals("Extension.url", extensionUrl.getPath());
			assertEquals("StructureDefinition/MyExtension", extensionUrl.getFixed());
			ElementDefinition extensionValue = sd.getDifferential().getElement().get(2);
			assertNotNull(extensionValue);
			assertEquals("Extension.value[x]:valueInteger", extensionValue.getId());
			assertEquals("Extension.valueInteger", extensionValue.getPath());
			assertEquals("valueInteger", extensionValue.getSliceName());
			assertEquals("Short description", extensionValue.getShort());
			assertEquals("Long description", extensionValue.getDefinition());
			assertEquals("Integer", extensionValue.getTypeFirstRep().getCode());
			assertEquals("0", extensionValue.getDefaultValue());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
