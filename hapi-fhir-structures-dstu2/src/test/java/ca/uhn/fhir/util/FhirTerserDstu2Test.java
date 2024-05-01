package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import static org.assertj.core.api.Assertions.fail;

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

		assertThat(target.getCode()).isEqualTo("CODE");
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
		assertThat(clonedPatient.getActive().booleanValue()).isEqualTo(true);
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

		assertThat(target.getSystem()).isEqualTo("SYSTEM");

		try {
			ourCtx.newTerser().cloneInto(source, target, false);
			fail("");		} catch (DataFormatException e) {
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
		 assertThat(((StringDt) exts.get(0).getValue()).getValue()).isEqualTo("FOO");
   }


	@Test
	public void testCloneIntoPrimitive() {
		StringDt source = new StringDt("STR");
		MarkdownDt target = new MarkdownDt();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertThat(target.getValueAsString()).isEqualTo("STR");
	}


	@Test
	public void testCloneIntoPrimitiveFails() {
		StringDt source = new StringDt("STR");
		MoneyDt target = new MoneyDt();

		ourCtx.newTerser().cloneInto(source, target, true);
		assertTrue(target.isEmpty());

		try {
			ourCtx.newTerser().cloneInto(source, target, false);
			fail("");		} catch (DataFormatException e) {
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

		assertThat(target.getId().getValue()).isEqualTo("http://foo/base/Observation/_history/123");
	}


	@Test
	public void testCloneIntoResourceCopiesElementId() {
		Observation obs = new Observation();
		StringDt string = new StringDt("AAA");
		string.setId("BBB");
		obs.setValue(string);

		Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);

		assertThat(((StringDt) target.getValue()).getElementSpecificId()).isEqualTo("BBB");
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

		 assertThat(((StringDt) obs.getValue()).getValue()).isEqualTo("AAA");
		 assertThat(obs.getComments()).isEqualTo("COMMENTS");
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

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
	}

	@Test
	public void testGetValuesAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		((BooleanDt) values.get(0)).setValue(Boolean.FALSE);

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertFalse(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		((ExtensionDt) values.get(0)).setValue(new StringDt("modifiedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifiedValue");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		((ExtensionDt) values.get(0)).setValue(new StringDt("modifiedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifiedModifierValue");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");

		((ExtensionDt) values.get(0)).setValue(new StringDt("modifiedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifiedNestedValue");
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

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value1");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("value2");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue1");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("modifierValue2");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue1");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("nestedValue2");
	}

	@Test
	public void testGetValuesWithWantedClass() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IPrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		List<ExtensionDt> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) extValues.get(0).getValue()).getValueAsString()).isEqualTo("value");

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifierValue");

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) extValues.get(0).getValue()).getValueAsString()).isEqualTo("nestedValue");
	}

	@Test
	public void testGetValuesWithWantedClassAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IPrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		((BooleanDt) values.get(0)).setValue(Boolean.FALSE);

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertFalse(((BooleanDt) values.get(0)).getValue());

		List<ExtensionDt> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("value");

		extValues.get(0).setValue(new StringDt("modifiedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifiedValue");

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifierValue");

		extValues.get(0).setValue(new StringDt("modifiedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifiedModifierValue");

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) extValues.get(0).getValue()).getValueAsString()).isEqualTo("nestedValue");

		extValues.get(0).setValue(new StringDt("modifiedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringDt);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) extValues.get(0).getValue()).getValueAsString()).isEqualTo("modifiedNestedValue");
	}

	@Test
	public void testGetValuesWithWantedClassAndTheCreate() {
		Patient p = new Patient();

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IPrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", IPrimitiveType.class, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertNull(((BooleanDt) values.get(0)).getValue());

		List<ExtensionDt> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", ExtensionDt.class, true);
		assertThat(extValues).hasSize(1);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(extValues.get(0).getValue());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", ExtensionDt.class, true);
		assertThat(extValues).hasSize(1);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(extValues.get(0).getValue());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", ExtensionDt.class, true);
		assertThat(extValues).hasSize(1);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
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

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

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
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedValue");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedModifierValue");

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedNestedValue");
	}

	@Test
	public void testGetValuesWithTheCreate() {
		Patient p = new Patient();

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertNull(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(((ExtensionDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(((ExtensionDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
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

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

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
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedValue");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedModifierValue");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof  IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertNull(((ExtensionDt) values.get(1)).getValue());

		((ExtensionDt) values.get(1)).setValue(new StringDt("addedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof  IBaseExtension);
		assertTrue(values.get(1) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedNestedValue");
	}

	@Test
	public void testGetValuesWithTheCreateAndNoOverwrite() {
		Patient p = new Patient();
		p.setActive(true);
		p.addUndeclaredExtension(false, "http://acme.org/extension", new StringDt("value"));
		p.addUndeclaredExtension(false, "http://acme.org/otherExtension", new StringDt("otherValue"));
		p.addUndeclaredExtension(true, "http://acme.org/modifierExtension", new StringDt("modifierValue"));
		p.addUndeclaredExtension(false, "http://acme.org/parentExtension").addUndeclaredExtension(false, "http://acme.org/childExtension", new StringDt("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = ourCtx.newTerser().getValues(p, "Patient.active", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IPrimitiveType);
		assertTrue(values.get(0) instanceof BooleanDt);
		assertTrue(((BooleanDt) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof ExtensionDt);
		assertThat(((ExtensionDt) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringDt) ((ExtensionDt) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
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
