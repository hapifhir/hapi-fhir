package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Patient.LinkType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FhirTerserR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirTerserR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testClear() {
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		Patient pt = new Patient();
		pt.setId("pt");
		pt.setActive(true);
		input
			.addEntry()
			.setResource(pt)
			.getRequest()
			.setUrl("Patient/pt")
			.setMethod(Bundle.HTTPVerb.PUT);

		Observation obs = new Observation();
		obs.setId("obs");
		obs.getSubject().setReference("Patient/pt");
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setUrl("Observation/obs")
			.setMethod(Bundle.HTTPVerb.PUT);

		ourCtx.newTerser().clear(input);

		String output = ourCtx.newJsonParser().encodeResourceToString(input);
		ourLog.info(output);
		assertTrue(input.isEmpty());
		assertEquals("{\"resourceType\":\"Bundle\"}", output);
	}

	@Test
	public void testCloneIntoBundle() {
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		Patient pt = new Patient();
		pt.setId("pt");
		pt.setActive(true);
		input
			.addEntry()
			.setResource(pt)
			.getRequest()
			.setUrl("Patient/pt")
			.setMethod(Bundle.HTTPVerb.PUT);

		Observation obs = new Observation();
		obs.setId("obs");
		obs.getSubject().setReference("Patient/pt");
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setUrl("Observation/obs")
			.setMethod(Bundle.HTTPVerb.PUT);

		Bundle ionputClone = new Bundle();
		ourCtx.newTerser().cloneInto(input, ionputClone, false);
	}

	@Test
	public void testCloneIntoComposite() {
		Quantity source = new Quantity();
		source.setCode("CODE");
		SimpleQuantity target = new SimpleQuantity();

		ourCtx.newTerser().cloneInto(source, target, true);

		Assert.assertEquals("CODE", target.getCode());
	}

	@Test
	public void testCloneIntoCompositeMismatchedFields() {
		Quantity source = new Quantity();
		source.setSystem("SYSTEM");
		source.setUnit("UNIT");
		Identifier target = new Identifier();

		ourCtx.newTerser().cloneInto(source, target, true);

		Assert.assertEquals("SYSTEM", target.getSystem());

		try {
			ourCtx.newTerser().cloneInto(source, target, false);
			fail();
		} catch (DataFormatException e) {
			// good
		}
	}

	/**
	 * See #369
	 */
	@Test
	public void testCloneIntoExtension() {
		Patient patient = new Patient();

		patient.addExtension(new Extension("http://example.com", new StringType("FOO")));

		Patient target = new Patient();
		ourCtx.newTerser().cloneInto(patient, target, false);

		List<Extension> exts = target.getExtensionsByUrl("http://example.com");
		assertEquals(1, exts.size());
		Assert.assertEquals("FOO", ((StringType) exts.get(0).getValue()).getValue());
	}


	@Test
	public void testCloneIntoPrimitive() {
		StringType source = new StringType("STR");
		MarkdownType target = new MarkdownType();

		ourCtx.newTerser().cloneInto(source, target, true);

		Assert.assertEquals("STR", target.getValueAsString());
	}


	@Test
	public void testCloneIntoPrimitiveFails() {
		StringType source = new StringType("STR");
		Money target = new Money();

		ourCtx.newTerser().cloneInto(source, target, true);
		assertTrue(target.isEmpty());

		try {
			ourCtx.newTerser().cloneInto(source, target, false);
			fail();
		} catch (DataFormatException e) {
			// good
		}

	}

	/**
	 * See #369
	 */
	@Test
	public void testCloneIntoValues() {
		Observation obs = new Observation();
		obs.setValue(new StringType("AAA"));
		obs.addNote().setText("COMMENTS");

		Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);

		Assert.assertEquals("AAA", ((StringType) obs.getValue()).getValue());
		Assert.assertEquals("COMMENTS", obs.getNote().get(0).getText());
	}

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDescendsIntoContained() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		Organization o = new Organization();
		o.getNameElement().setValue("ORGANIZATION");
		p.getContained().add(o);

		FhirTerser t = ourCtx.newTerser();
		List<StringType> strings = t.getAllPopulatedChildElementsOfType(p, StringType.class);

		assertThat(toStrings(strings), containsInAnyOrder("PATIENT", "ORGANIZATION"));

	}

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDoesntDescendIntoEmbedded() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		Bundle b = new Bundle();
		b.addEntry().setResource(p);
		b.addLink().setRelation("BUNDLE");

		FhirTerser t = ourCtx.newTerser();
		List<StringType> strings = t.getAllPopulatedChildElementsOfType(b, StringType.class);

		assertEquals(1, strings.size());
		assertThat(toStrings(strings), containsInAnyOrder("BUNDLE"));

	}

	@Test
	public void testGetResourceReferenceInExtension() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		Reference ref = new Reference(o);
		Extension ext = new Extension("urn:foo", ref);
		p.addExtension(ext);

		FhirTerser t = ourCtx.newTerser();
		List<IBaseReference> refs = t.getAllPopulatedChildElementsOfType(p, IBaseReference.class);
		assertEquals(1, refs.size());
		assertSame(ref, refs.get(0));
	}

	@Test
	public void testGetValues() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
	}

	@Test
	public void testGetEmbeddedResourcesOnBundle() {

		Bundle outerBundle = new Bundle();
		outerBundle.setId("outerBundle");

		Bundle innerBundle1 = new Bundle();
		innerBundle1.setId("innerBundle1");
		innerBundle1.setTotal(1);
		outerBundle.addEntry().setResource(innerBundle1);
		Patient innerBundle1innerPatient1 = new Patient();
		innerBundle1innerPatient1.setId("innerBundle1innerPatient1");
		innerBundle1innerPatient1.setActive(true);
		innerBundle1.addEntry().setResource(innerBundle1innerPatient1);

		Bundle innerBundle2 = new Bundle();
		innerBundle2.setId("innerBundle2");
		innerBundle2.setTotal(1);
		outerBundle.addEntry().setResource(innerBundle2);
		Patient innerBundle2innerPatient1 = new Patient();
		innerBundle2innerPatient1.setId("innerBundle2innerPatient1");
		innerBundle2innerPatient1.setActive(true);
		innerBundle2.addEntry().setResource(innerBundle2innerPatient1);

		Patient innerPatient1 = new Patient();
		innerPatient1.setId("innerPatient1");
		innerPatient1.setActive(true);
		outerBundle.addEntry().setResource(innerPatient1);

		FhirTerser t = ourCtx.newTerser();

		Collection<IBaseResource> resources;

		// Not recursive
		resources = t.getAllEmbeddedResources(outerBundle, false);
		assertThat(toUnqualifiedVersionlessIdValues(resources).toString(), resources,
			Matchers.containsInAnyOrder(innerBundle1, innerBundle2, innerPatient1));

		// Recursive
		resources = t.getAllEmbeddedResources(outerBundle, true);
		assertThat(toUnqualifiedVersionlessIdValues(resources).toString(), resources,
			Matchers.containsInAnyOrder(innerBundle1, innerBundle1innerPatient1, innerBundle2, innerBundle2innerPatient1, innerPatient1));
	}

	@Test
	public void testGetEmbeddedResourcesOnContainedResources() {

		Patient patient = new Patient();

		Practitioner practitioner1 = new Practitioner();
		practitioner1.setId("practitioner1");
		practitioner1.setActive(true);
		patient.getContained().add(practitioner1);
		patient.addGeneralPractitioner().setReference("#practitioner1");

		FhirTerser t = ourCtx.newTerser();

		Collection<IBaseResource> resources;

		resources = t.getAllEmbeddedResources(patient, false);
		assertThat(toUnqualifiedVersionlessIdValues(resources).toString(), resources,
			containsInAnyOrder(practitioner1));

	}

	private List<String> toUnqualifiedVersionlessIdValues(Collection<IBaseResource> theResources) {
		return theResources
			.stream()
			.map(t -> t.getIdElement().toUnqualifiedVersionless().getValue())
			.collect(Collectors.toList());
	}


	@Test
	public void testGetValuesAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		((BooleanType) values.get(0)).setValue(Boolean.FALSE);

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertFalse(((BooleanType) values.get(0)).booleanValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		((Extension) values.get(0)).setValue(new StringType("modifiedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifiedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		((Extension) values.get(0)).setValue(new StringType("modifiedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifiedModifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		((Extension) values.get(0)).setValue(new StringType("modifiedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifiedNestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesMultiple() {
		Patient p = new Patient();
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value1"));
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value2"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue1"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue2"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue1"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue2"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value1", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("value2", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue1", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("modifierValue2", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue1", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("nestedValue2", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithWantedClass() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<PrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		List<Extension> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		Assert.assertEquals("value", ((StringType) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) extValues.get(0).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithWantedClassAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<PrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		((BooleanType) values.get(0)).setValue(Boolean.FALSE);

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof BooleanType);
		assertFalse(((BooleanType) values.get(0)).booleanValue());

		List<Extension> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		Assert.assertEquals("value", ((StringType) (extValues.get(0).getValue())).getValueAsString());

		extValues.get(0).setValue(new StringType("modifiedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		Assert.assertEquals("modifiedValue", ((StringType) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) (extValues.get(0).getValue())).getValueAsString());

		extValues.get(0).setValue(new StringType("modifiedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		Assert.assertEquals("modifiedModifierValue", ((StringType) (extValues.get(0).getValue())).getValueAsString());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) extValues.get(0).getValue()).getValueAsString());

		extValues.get(0).setValue(new StringType("modifiedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class);
		assertEquals(1, extValues.size());
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		Assert.assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		Assert.assertEquals("modifiedNestedValue", ((StringType) extValues.get(0).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithWantedClassAndTheCreate() {
		Patient p = new Patient();

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<PrimitiveType> values = ourCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class, true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof BooleanType);
		assertNull(((BooleanType) values.get(0)).getValue());

		List<Extension> extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class, true);
		assertEquals(1, extValues.size());
		Assert.assertEquals("http://acme.org/extension", extValues.get(0).getUrl());
		assertNull(extValues.get(0).getValue());

		extValues = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class, true);
		assertEquals(1, extValues.size());
		Assert.assertEquals("http://acme.org/modifierExtension", extValues.get(0).getUrl());
		assertNull(extValues.get(0).getValue());

		extValues = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class, true);
		assertEquals(1, extValues.size());
		Assert.assertEquals("http://acme.org/childExtension", extValues.get(0).getUrl());
		assertNull(extValues.get(0).getValue());
	}

	@Test
	public void testGetValuesWithTheAddExtensionAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		// No change.
		values = ourCtx.newTerser().getValues(p, "Patient.active", false, true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", false, true);
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(1)).getUrl());
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("addedValue", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", false, true);
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(1)).getUrl());
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("addedModifierValue", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", false, true);
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(1)).getUrl());
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("addedNestedValue", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithTheCreate() {
		Patient p = new Patient();

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.active", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertNull(((BooleanType) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		assertNull(((Extension) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		assertNull(((Extension) values.get(0)).getValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		assertNull(((Extension) values.get(0)).getValue());
	}

	@Test
	public void testGetValuesWithTheCreateAndTheAddExtensionAndModify() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.active");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		// No change.
		values = ourCtx.newTerser().getValues(p, "Patient.active", true, true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true, true);
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(1)).getUrl());
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("addedValue", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true, true);
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(1)).getUrl());
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedModifierValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("addedModifierValue", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true, true);
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(1)).getUrl());
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedNestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(1)).getUrl());
		Assert.assertEquals("addedNestedValue", ((StringType) ((Extension) values.get(1)).getValue()).getValueAsString());
	}

	@Test
	public void testGetValuesWithTheCreateAndNoOverwrite() {
		Patient p = new Patient();
		p.setActive(true);
		p.addExtension()
			.setUrl("http://acme.org/extension")
			.setValue(new StringType("value"));
		p.addExtension()
			.setUrl("http://acme.org/otherExtension")
			.setValue(new StringType("otherValue"));
		p.addModifierExtension()
			.setUrl("http://acme.org/modifierExtension")
			.setValue(new StringType("modifierValue"));
		p.addExtension()
			.setUrl("http://acme.org/parentExtension")
			.addExtension()
			.setUrl("http://acme.org/childExtension")
			.setValue(new StringType("nestedValue"));

		System.out.println(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<Object> values = ourCtx.newTerser().getValues(p, "Patient.active", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/extension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("value", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/modifierExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("modifierValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());

		values = ourCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertEquals(1, values.size());
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		Assert.assertEquals("http://acme.org/childExtension", ((Extension) values.get(0)).getUrl());
		Assert.assertEquals("nestedValue", ((StringType) ((Extension) values.get(0)).getValue()).getValueAsString());
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
		p.addLink().getTypeElement().setValue(LinkType.REFER);
		ourCtx.newTerser().visit(p, visitor);

		assertEquals(3, element.getAllValues().size());
		assertSame(p, element.getAllValues().get(0));
		assertSame(p.getLink().get(0), element.getAllValues().get(1));
		assertSame(p.getLink().get(0).getTypeElement(), element.getAllValues().get(2));

		assertEquals(3, containingElementPath.getAllValues().size());
		// assertEquals(0, containingElementPath.getAllValues().get(0).size());
		// assertEquals(1, containingElementPath.getAllValues().get(1).size());
		// assertEquals(2, containingElementPath.getAllValues().get(2).size());

	}

	private List<String> toStrings(List<StringType> theStrings) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (StringType next : theStrings) {
			retVal.add(next.getValue());
		}
		return retVal;
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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	/**
	 * See http://stackoverflow.com/questions/182636/how-to-determine-the-class-of-a-generic-type
	 */
	private static <T> Class<List<T>> getListClass(Class<T> theClass) {
		return new ClassGetter<List<T>>() {
		}.get();
	}

	/**
	 * See http://stackoverflow.com/questions/182636/how-to-determine-the-class-of-a-generic-type
	 */
	private static Class<List<BaseRuntimeElementDefinition<?>>> getListClass2() {
		return new ClassGetter<List<BaseRuntimeElementDefinition<?>>>() {
		}.get();
	}

}
