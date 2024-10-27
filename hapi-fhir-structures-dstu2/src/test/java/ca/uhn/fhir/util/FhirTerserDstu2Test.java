package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.MoneyDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.MarkdownDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FhirTerserDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(FhirTerserDstu2Test.class);

	@Test
	public void testCloneIntoComposite() {
		QuantityDt source = new QuantityDt();
		source.setCode("CODE");
		MoneyDt target = new MoneyDt();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("CODE", target.getCode());
	}

	@Test
	public void testCloneResource() {
		Organization org = new Organization();
		org.setName("Contained Org Name");
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getManagingOrganization().setResource(org);

		// Re-encode
		String string = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info("Encoded: {}", string);
		patient = ourCtx.newJsonParser().parseResource(Patient.class, string);

		Patient clonedPatient = ourCtx.newTerser().clone(patient);
		assertEquals(true, clonedPatient.getActive().booleanValue());
		string = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(clonedPatient);
		ourLog.info("Encoded: {}", string);
		assertThat(string).contains("\"contained\"");
	}


	@Test
	public void testCloneIntoCompositeMismatchedFields() {
		QuantityDt source = new QuantityDt();
		source.setSystem("SYSTEM");
		source.setUnit("UNIT");
		IdentifierDt target = new IdentifierDt();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("SYSTEM", target.getSystem());

		try {
			ourCtx.newTerser().cloneInto(source, target, false);
			fail();		} catch (DataFormatException e) {
			// good
		}
}

   /**
	 * See #369
	 */
   @Test
   public void testCloneIntoExtension() {
       Patient patient = new Patient();

       patient.addUndeclaredExtension(new ExtensionDt(false, "http://example.com", new StringDt("FOO")));

       Patient target = new Patient();
		ourCtx.newTerser().cloneInto(patient, target, false);
		
		List<ExtensionDt> exts = target.getUndeclaredExtensionsByUrl("http://example.com");
			assertThat(exts).hasSize(1);
		 assertEquals("FOO", ((StringDt) exts.get(0).getValue()).getValue());
   }


	@Test
	public void testCloneIntoPrimitive() {
		StringDt source = new StringDt("STR");
		MarkdownDt target = new MarkdownDt();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("STR", target.getValueAsString());
	}


	@Test
	public void testCloneIntoPrimitiveFails() {
		StringDt source = new StringDt("STR");
		MoneyDt target = new MoneyDt();

		ourCtx.newTerser().cloneInto(source, target, true);
		assertTrue(target.isEmpty());

		try {
			ourCtx.newTerser().cloneInto(source, target, false);
			fail();		} catch (DataFormatException e) {
			// good
		}

	}

	@Test
	public void testCloneIntoResourceCopiesId() {
		Observation obs = new Observation();
		obs.setId("http://foo/base/Observation/_history/123");
		obs.setValue(new StringDt("AAA"));

		Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);

		assertEquals("http://foo/base/Observation/_history/123", target.getId().getValue());
	}


	@Test
	public void testCloneIntoResourceCopiesElementId() {
		Observation obs = new Observation();
		StringDt string = new StringDt("AAA");
		string.setId("BBB");
		obs.setValue(string);

		Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);

		assertEquals("BBB", ((StringDt) target.getValue()).getElementSpecificId());
	}


	/**
	 * See #369
	 */
   @Test
   public void testCloneIntoValues() {
       Observation obs = new Observation();
       obs.setValue(new StringDt("AAA"));
       obs.setComments("COMMENTS");

       Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);

		 assertEquals("AAA", ((StringDt) obs.getValue()).getValue());
		 assertEquals("COMMENTS", obs.getComments());
   }

	@Test
	public void testCloneIntoReferenceWithResource() {
		// set up
		Practitioner practitioner = new Practitioner();
		ResourceReferenceDt source = new ResourceReferenceDt(practitioner);
		ResourceReferenceDt target = new ResourceReferenceDt();

		// execute
		ourCtx.newTerser().cloneInto(source, target, false);

		assertThat(target.getResource()).isSameAs(practitioner);
	}

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDescendsIntoContained() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.getNameElement().setValue("ORGANIZATION");
		p.getContained().getContainedResources().add(o);

		FhirTerser t = ourCtx.newTerser();
		List<StringDt> strings = t.getAllPopulatedChildElementsOfType(p, StringDt.class);

		assertThat(strings).hasSize(2);
		assertThat(strings).containsExactlyInAnyOrder(new StringDt("PATIENT"), new StringDt("ORGANIZATION"));

	}

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDoesntDescendIntoEmbedded() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Bundle b = new Bundle();
		b.addEntry().setResource(p);
		b.addLink().setRelation("BUNDLE");

		FhirTerser t = ourCtx.newTerser();
		List<StringDt> strings = t.getAllPopulatedChildElementsOfType(b, StringDt.class);

		assertThat(strings).hasSize(1);
		assertThat(strings).containsExactlyInAnyOrder(new StringDt("BUNDLE"));

	}

	@Test
	public void testGetResourceReferenceInExtension() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		ResourceReferenceDt ref = new ResourceReferenceDt(o);
		ExtensionDt ext = new ExtensionDt(false, "urn:foo", ref);
		p.addUndeclaredExtension(ext);

		List<IBaseReference> refs = ourCtx.newTerser().getAllPopulatedChildElementsOfType(p, IBaseReference.class);
		assertThat(refs).hasSize(1);
		assertThat(refs.get(0)).isSameAs(ref);
	}

	@Test
	public void testGetValues() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		((BooleanDt) values.get(0)).setValue(Boolean.FALSE);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertFalse(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		((ExtensionDt) values.get(0)).setValue(new StringDt("modifiedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifiedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		((ExtensionDt) values.get(0)).setValue(new StringDt("modifiedModifierValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifiedModifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		((ExtensionDt) values.get(0)).setValue(new StringDt("modifiedNestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifiedNestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesMultiple() {
		Patient p = new Patient();
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value1"));
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value2"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue1"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue2"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue1"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue2"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value1", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("value2", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue1", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("modifierValue2", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue1", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("nestedValue2", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithWantedClass() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IPrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		List<ExtensionDt> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		assertEquals("value", ((StringDt) extValues.get(0).getValue()).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		assertEquals("modifierValue", ((StringDt) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		assertEquals("nestedValue", ((StringDt) extValues.get(0).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithWantedClassAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IPrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		((BooleanDt) values.get(0)).setValue(Boolean.FALSE);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertFalse(((BooleanDt) values.get(0)).getValue());

		List<ExtensionDt> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		assertEquals("value", ((StringDt) (extValues.get(0).getValue())).getValueAsString());

		extValues.get(0).setValue(new StringDt("modifiedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		assertEquals("modifiedValue", ((StringDt) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		assertEquals("modifierValue", ((StringDt) (extValues.get(0).getValue())).getValueAsString());

		extValues.get(0).setValue(new StringDt("modifiedModifierValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		assertEquals("modifiedModifierValue", ((StringDt) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		assertEquals("nestedValue", ((StringDt) extValues.get(0).getValue()).getValueAsString());

		extValues.get(0).setValue(new StringDt("modifiedNestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		assertEquals("modifiedNestedValue", ((StringDt) extValues.get(0).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithWantedClassAndTheCreate() {
		Patient p = new Patient();

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IPrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertNull(((BooleanDt) values.get(0)).getValue());

		List<ExtensionDt> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class, true);
		assertThat(extValues).hasSize(1);
		assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		assertNull(extValues.get(0).getValue());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class, true);
		assertThat(extValues).hasSize(1);
		assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		assertNull(extValues.get(0).getValue());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class, true);
		assertThat(extValues).hasSize(1);
		assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		assertNull(extValues.get(0).getValue());
	}

	@Test
	public void testGetValuesWithTheAddExtensionAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		// No change.
		values = ourCtx.newTerser().getValues(p, "Patient.active", false, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(1)).getUrl());
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("addedValue", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedModifierValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("addedModifierValue", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedNestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("addedNestedValue", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithTheCreate() {
		Patient p = new Patient();

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertNull(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertNull(((ExtensionDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertNull(((ExtensionDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertNull(((ExtensionDt) values.get(0)).getValue());
	}

	@Test
	public void testGetValuesWithTheCreateAndTheAddExtensionAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		// No change.
		values = ourCtx.newTerser().getValues(p, "Patient.active", true, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(1)).getUrl());
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("addedValue", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedModifierValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("addedModifierValue", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedNestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(1)).getUrl());
		assertEquals("addedNestedValue", ((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithTheCreateAndNoOverwrite() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/extension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("value", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/modifierExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("modifierValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertEquals("http://acme.org/childExtension", ((ExtensionDt) values.get(0)).getUrl());
		assertEquals("nestedValue", ((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString());
	}
	
	@Test
	public void testVisitWithModelVisitor2() {
		IModelVisitor2 visitor = mock(IModelVisitor2.class);

		ArgumentCaptor<IBase> element = ArgumentCaptor.forClass(IBase.class);
		ArgumentCaptor<List<IBase>> containingElementPath = ArgumentCaptor.forClass(getListClass(IBase.class));
		ArgumentCaptor<List<BaseRuntimeChildDefinition>> childDefinitionPath = ArgumentCaptor.forClass(getListClass(BaseRuntimeChildDefinition.class));
		ArgumentCaptor<List<BaseRuntimeElementDefinition<?>>> elementDefinitionPath = ArgumentCaptor.forClass(getListClass2());
		when(visitor.acceptElement(element.capture(), containingElementPath.capture(), childDefinitionPath.capture(), elementDefinitionPath.capture())).thenReturn(true);

		Patient p = new Patient();
		p.addLink().getTypeElement().setValue("CODE");
		ourCtx.newTerser().visit(p, visitor);

		assertThat(element.getAllValues()).hasSize(3);
		assertThat(element.getAllValues().get(0)).isSameAs(p);
		assertThat(element.getAllValues().get(1)).isSameAs(p.getLinkFirstRep());
		assertThat(element.getAllValues().get(2)).isSameAs(p.getLinkFirstRep().getTypeElement());

		assertThat(containingElementPath.getAllValues()).hasSize(3);
		// assertEquals(0, containingElementPath.getAllValues().get(0).size());
		// assertEquals(1, containingElementPath.getAllValues().get(1).size());
		// assertEquals(2, containingElementPath.getAllValues().get(2).size());

	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	/**
	 * See http://stackoverflow.com/questions/182636/how-to-determine-the-class-of-a-generic-type
	 */
	@SuppressWarnings({"UnnecessaryLocalVariable", "unchecked"})
	private static <T> Class<List<T>> getListClass(Class<T> theClass) {
		Class listClass = List.class;
		return listClass;
	}

	/**
	 * See http://stackoverflow.com/questions/182636/how-to-determine-the-class-of-a-generic-type
	 */
	@SuppressWarnings({"UnnecessaryLocalVariable", "unchecked"})
	private static Class<List<BaseRuntimeElementDefinition<?>>> getListClass2() {
		Class listClass = List.class;
		return listClass;
	}

	/**
	 * See http://stackoverflow.com/questions/182636/how-to-determine-the-class-of-a-generic-type
	 */
	private static abstract class ClassGetter<T> {
		@SuppressWarnings("unchecked")
		public final Class<T> get() {
			final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
			Type type = superclass.getActualTypeArguments()[0];
			if (type instanceof ParameterizedType) {
				return (Class<T>) ((ParameterizedType) type).getOwnerType();
			}
			return (Class<T>) type;
		}
	}

}
