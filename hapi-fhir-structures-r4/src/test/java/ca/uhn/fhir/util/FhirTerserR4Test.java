package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.parser.DataFormatException;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Money;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Patient.LinkType;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.SimpleQuantity;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Substance;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FhirTerserR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirTerserR4Test.class);
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Test
	public void testAddElement() {
		Patient patient = new Patient();
		IBase family = myCtx.newTerser().addElement(patient, "Patient.name.family");

		assertThat(patient.getName()).hasSize(1);
		assertThat(patient.getName().get(0).getFamilyElement()).isSameAs(family);
	}

	@Test
	public void testAddElementWithValue() {
		Patient patient = new Patient();
		IBase family = myCtx.newTerser().addElement(patient, "Patient.name.family", "FOO");

		assertThat(patient.getName()).hasSize(1);
		assertThat(patient.getName().get(0).getFamilyElement()).isSameAs(family);
		assertThat(patient.getName().get(0).getFamilyElement().getValue()).isEqualTo("FOO");
	}

	@Test
	public void testAddElementWithValue_NonPrimitivePath() {
		Patient patient = new Patient();

		try {
			myCtx.newTerser().addElement(patient, "Patient.name", "FOO");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1800) + "Element at path Patient.name is not a primitive datatype. Found: HumanName");
		}

	}

	@Test
	public void testAddElements_NonRepeatingPath() {
		Patient patient = new Patient();

		try {
			myCtx.newTerser().addElements(patient, "Patient.name.family", Lists.newArrayList("FOO", "BAR"));
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1798) + "Can not add multiple values at path Patient.name.family: Element does not repeat");
		}

	}

	@Test
	public void testAddElementReusesExisting() {
		Patient patient = new Patient();
		StringType existingFamily = patient.addName().getFamilyElement();
		patient.addName().setFamily("FAM2");
		patient.addName().setFamily("FAM3");

		IBase family = myCtx.newTerser().addElement(patient, "Patient.name.family");

		assertThat(patient.getName()).hasSize(3);
		assertTrue(existingFamily == patient.getName().get(0).getFamilyElement());
		assertThat(patient.getName().get(0).getFamilyElement()).isSameAs(family);
	}

	@Test
	public void testAddElementCantReuseExistingBecauseItIsNotEmpty() {
		Patient patient = new Patient();
		patient.addName().setFamily("FAM1");
		patient.addName().setFamily("FAM2");
		patient.addName().setFamily("FAM3");

		try {
			myCtx.newTerser().addElement(patient, "Patient.name.family");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1797) + "Element at path Patient.name.family is not repeatable and not empty");
		}
	}

	@Test
	public void testAddElementInvalidPath() {
		Patient patient = new Patient();

		// So much foo....

		try {
			myCtx.newTerser().addElement(patient, "foo");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1796) + "Invalid path foo: Element of type Patient has no child named foo. Valid names: active, address, birthDate, communication, contact, contained, deceased, extension, gender, generalPractitioner, id, identifier, implicitRules, language, link, managingOrganization, maritalStatus, meta, modifierExtension, multipleBirth, name, photo, telecom, text");
		}

		try {
			myCtx.newTerser().addElement(patient, "Patient.foo");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1796) + "Invalid path Patient.foo: Element of type Patient has no child named foo. Valid names: active, address, birthDate, communication, contact, contained, deceased, extension, gender, generalPractitioner, id, identifier, implicitRules, language, link, managingOrganization, maritalStatus, meta, modifierExtension, multipleBirth, name, photo, telecom, text");
		}

		try {
			myCtx.newTerser().addElement(patient, "Patient.name.foo");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1796) + "Invalid path Patient.name.foo: Element of type HumanName has no child named foo. Valid names: extension, family, given, id, period, prefix, suffix, text, use");
		}

		try {
			myCtx.newTerser().addElement(patient, "Patient.name.family.foo");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1799) + "Invalid path Patient.name.family.foo: Element of type HumanName has no child named family (this is a primitive type)");
		}
	}

	@Test
	public void testContainResourcesWithModify() {

		MedicationRequest mr = new MedicationRequest();
		mr.setMedication(new Reference(new Medication().setStatus(Medication.MedicationStatus.ACTIVE)));
		mr.getRequester().setResource(new Practitioner().setActive(true));

		FhirTerser.ContainedResources contained = myCtx.newTerser().containResources(mr, FhirTerser.OptionsEnum.MODIFY_RESOURCE, FhirTerser.OptionsEnum.STORE_AND_REUSE_RESULTS);

		assertThat(mr.getContained().get(0).getId()).isEqualTo("#1");
		assertThat(mr.getContained().get(1).getId()).isEqualTo("#2");
		assertThat(mr.getContained().get(0).getResourceType()).isEqualTo(ResourceType.Medication);
		assertThat(mr.getContained().get(1).getResourceType()).isEqualTo(ResourceType.Practitioner);
		assertThat(mr.getMedicationReference().getReference()).isEqualTo("#1");
		assertThat(mr.getRequester().getReference()).isEqualTo("#2");

		FhirTerser.ContainedResources secondPass = myCtx.newTerser().containResources(mr, FhirTerser.OptionsEnum.MODIFY_RESOURCE, FhirTerser.OptionsEnum.STORE_AND_REUSE_RESULTS);
		assertThat(secondPass).isSameAs(contained);
	}

	@Test
	public void testContainedResourcesWithModify_DoubleLink() {
		Substance ingredient = new Substance();
		ingredient.getCode().addCoding().setSystem("system").setCode("code");

		Medication medication = new Medication();
		medication.addIngredient().setItem(new Reference(ingredient));

		MedicationAdministration medAdmin = new MedicationAdministration();
		medAdmin.setMedication(new Reference(medication));

		myCtx.newTerser().containResources(medAdmin, FhirTerser.OptionsEnum.MODIFY_RESOURCE, FhirTerser.OptionsEnum.STORE_AND_REUSE_RESULTS);

		assertThat(medAdmin.getContained().get(0).getId()).isEqualTo("#1");
		assertThat(medAdmin.getContained().get(1).getId()).isEqualTo("#2");
		assertThat(medAdmin.getContained().get(0).getResourceType()).isEqualTo(ResourceType.Medication);
		assertThat(medAdmin.getContained().get(1).getResourceType()).isEqualTo(ResourceType.Substance);
		assertThat(medAdmin.getMedicationReference().getReference()).isEqualTo("#1");
		assertThat(((Medication) (medAdmin.getContained().get(0))).getIngredientFirstRep().getItemReference().getReference()).isEqualTo("#2");

	}

	@Test
	public void testContainResourcesWithModify_DontTouchEmbeddedResources() {

		MedicationRequest mr = new MedicationRequest();
		mr.setMedication(new Reference(new Medication().setStatus(Medication.MedicationStatus.ACTIVE)));
		mr.getRequester().setResource(new Practitioner().setActive(true));

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(mr);

		myCtx.newTerser().containResources(bundle, FhirTerser.OptionsEnum.MODIFY_RESOURCE);

		assertThat(mr.getContained()).isEmpty();
		assertNull(mr.getRequester().getReference());
		assertNull(mr.getMedicationReference().getReference());

	}

	@Test
	public void testGetValuesCreateEnumeration_SetsEnumFactory() {

		Patient patient = new Patient();

		Enumeration<?> enumeration = (Enumeration<?>) myCtx.newTerser().getValues(patient, "Patient.gender", Enumeration.class, true).get(0);
		assertNotNull(enumeration.getEnumFactory());
	}

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

		myCtx.newTerser().clear(input);

		String output = myCtx.newJsonParser().encodeResourceToString(input);
		ourLog.info(output);
		assertTrue(input.isEmpty());
		assertThat(output).isEqualTo("{\"resourceType\":\"Bundle\"}");
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
		myCtx.newTerser().cloneInto(input, ionputClone, false);
	}

	@Test
	public void testCloneIntoComposite() {
		Quantity source = new Quantity();
		source.setCode("CODE");
		SimpleQuantity target = new SimpleQuantity();

		myCtx.newTerser().cloneInto(source, target, true);

		assertThat(target.getCode()).isEqualTo("CODE");
	}

	@Test
	public void testCloneIntoCompositeMismatchedFields() {
		Quantity source = new Quantity();
		source.setSystem("SYSTEM");
		source.setUnit("UNIT");
		Identifier target = new Identifier();

		myCtx.newTerser().cloneInto(source, target, true);

		assertThat(target.getSystem()).isEqualTo("SYSTEM");

		try {
			myCtx.newTerser().cloneInto(source, target, false);
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

		patient.addExtension(new Extension("http://example.com", new StringType("FOO")));

		Patient target = new Patient();
		myCtx.newTerser().cloneInto(patient, target, false);

		List<Extension> exts = target.getExtensionsByUrl("http://example.com");
		assertThat(exts).hasSize(1);
		assertThat(((StringType) exts.get(0).getValue()).getValue()).isEqualTo("FOO");
	}

	@Test
	public void testCloneIntoExtensionOnPrimitive() {
		DocumentReference source = new DocumentReference();
		source.addContent().getAttachment().getDataElement().addExtension("http://foo", new StringType("bar"));

		DocumentReference target = new DocumentReference();

		myCtx.newTerser().cloneInto(source, target, true);

		assertThat(target.getContentFirstRep().getAttachment().getDataElement().getExtension()).hasSize(1);
		assertThat(target.getContentFirstRep().getAttachment().getDataElement().getExtension().get(0).getUrl()).isEqualTo("http://foo");
		assertThat(target.getContentFirstRep().getAttachment().getDataElement().getExtension().get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("bar");
	}

	@Test
	public void testCloneIntoExtensionWithChildExtension() {
		Patient patient = new Patient();

		Extension ext = new Extension("http://example.com", new StringType("FOO"));
		patient.addExtension((Extension) new Extension().setUrl("http://foo").addExtension(ext));

		Patient target = new Patient();
		myCtx.newTerser().cloneInto(patient, target, false);

		List<Extension> exts = target.getExtensionsByUrl("http://foo");
		assertThat(exts).hasSize(1);
		exts = exts.get(0).getExtensionsByUrl("http://example.com");
		assertThat(((StringType) exts.get(0).getValue()).getValue()).isEqualTo("FOO");
	}

	@Test
	public void testCloneEnumeration() {
		Patient patient = new Patient();
		patient.setGender(Enumerations.AdministrativeGender.MALE);

		Patient target = new Patient();
		myCtx.newTerser().cloneInto(patient, target, false);

		assertThat(target.getGenderElement().getSystem()).isEqualTo("http://hl7.org/fhir/administrative-gender");
	}

	@Test
	public void testCloneIntoPrimitive() {
		StringType source = new StringType("STR");
		source.setId("STRING_ID");
		MarkdownType target = new MarkdownType();

		myCtx.newTerser().cloneInto(source, target, true);

		assertThat(target.getValueAsString()).isEqualTo("STR");
		assertThat(target.getId()).isEqualTo("STRING_ID");
	}


	@Test
	public void testCloneIntoPrimitiveFails() {
		StringType source = new StringType("STR");
		Money target = new Money();

		myCtx.newTerser().cloneInto(source, target, true);
		assertTrue(target.isEmpty());

		try {
			myCtx.newTerser().cloneInto(source, target, false);
			fail("");		} catch (DataFormatException e) {
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
		myCtx.newTerser().cloneInto(obs, target, false);

		assertThat(((StringType) obs.getValue()).getValue()).isEqualTo("AAA");
		assertThat(obs.getNote().get(0).getText()).isEqualTo("COMMENTS");
	}


	@Test
	public void testCloneIntoResourceCopiesId() {
		Observation obs = new Observation();
		obs.setId("http://foo/base/Observation/_history/123");
		obs.setValue(new StringType("AAA"));
		obs.addNote().setText("COMMENTS");

		Observation target = new Observation();
		myCtx.newTerser().cloneInto(obs, target, false);

		assertThat(target.getId()).isEqualTo("http://foo/base/Observation/_history/123");
	}


	@Test
	public void testCloneIntoResourceCopiesElementId() {
		Observation obs = new Observation();
		StringType string = new StringType("AAA");
		string.setId("BBB");
		obs.setValue(string);

		Observation target = new Observation();
		myCtx.newTerser().cloneInto(obs, target, false);

		assertThat(target.getValueStringType().getId()).isEqualTo("BBB");
	}


	@Test
	public void testGetAllPopulatedChildElementsOfTypeDescendsIntoContained() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		Organization o = new Organization();
		o.getNameElement().setValue("ORGANIZATION");
		p.getContained().add(o);

		FhirTerser t = myCtx.newTerser();
		List<StringType> strings = t.getAllPopulatedChildElementsOfType(p, StringType.class);

		assertThat(toStrings(strings)).containsExactlyInAnyOrder("PATIENT", "ORGANIZATION");

	}

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDoesntDescendIntoEmbedded() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		Bundle b = new Bundle();
		b.addEntry().setResource(p);
		b.addLink().setRelation("BUNDLE");

		FhirTerser t = myCtx.newTerser();
		List<StringType> strings = t.getAllPopulatedChildElementsOfType(b, StringType.class);

		assertThat(strings).hasSize(1);
		assertThat(toStrings(strings)).containsExactlyInAnyOrder("BUNDLE");

	}

	@Test
	public void testGetAllResourceReferences() {
		// setup
		Provenance p = new Provenance();
		p.addTarget(new Reference("Observation/1"));
		p.addTarget(new Reference("Observation/2"));
		p.setLocation(new Reference("Location/3"));
		p.getAgentFirstRep().setWho(new Reference("Practitioner/4"));
		p.getAgentFirstRep().setOnBehalfOf(new Reference("Organization/5"));
		p.getEntityFirstRep().setWhat(new Reference("DocumentReference/6"));

		// execute
		FhirTerser t = myCtx.newTerser();
		List<ResourceReferenceInfo> references = t.getAllResourceReferences(p);

		// validate
		assertThat(references).hasSize(6);
		assertThat(toResourceIds(references)).containsExactlyInAnyOrder("Observation/1", "Observation/2", "Location/3", "Practitioner/4", "Organization/5", "DocumentReference/6");
	}

	@Test
	public void testGetAllResourceReferencesExcluding() {
		// setup
		Provenance p = new Provenance();
		p.addTarget(new Reference("Observation/1"));
		p.addTarget(new Reference("Observation/2"));
		p.setLocation(new Reference("Location/3"));
		p.getAgentFirstRep().setWho(new Reference("Practitioner/4"));
		p.getAgentFirstRep().setOnBehalfOf(new Reference("Organization/5"));
		p.getEntityFirstRep().setWhat(new Reference("DocumentReference/6"));

		// execute
		FhirTerser t = myCtx.newTerser();
		List<ResourceReferenceInfo> references = t.getAllResourceReferencesExcluding(p, List.of("target"));

		// validate
		assertThat(references).hasSize(4);
		assertThat(toResourceIds(references)).containsExactlyInAnyOrder("Location/3", "Practitioner/4", "Organization/5", "DocumentReference/6");
	}

	private List<String> toResourceIds(List<ResourceReferenceInfo> references) {
		return references.stream()
			 .map(ResourceReferenceInfo::getResourceReference)
			 .map(IBaseReference::getReferenceElement)
			 .map(IIdType::getValue)
			 .collect(Collectors.toList());
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

		FhirTerser t = myCtx.newTerser();
		List<IBaseReference> refs = t.getAllPopulatedChildElementsOfType(p, IBaseReference.class);
		assertThat(refs).hasSize(1);
		assertThat(refs.get(0)).isSameAs(ref);
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
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

		FhirTerser t = myCtx.newTerser();

		Collection<IBaseResource> resources;

		// Not recursive
		resources = t.getAllEmbeddedResources(outerBundle, false);
		assertThat(resources).as(toUnqualifiedVersionlessIdValues(resources).toString()).containsExactlyInAnyOrder(innerBundle1, innerBundle2, innerPatient1);

		// Recursive
		resources = t.getAllEmbeddedResources(outerBundle, true);
		assertThat(resources).as(toUnqualifiedVersionlessIdValues(resources).toString()).containsExactlyInAnyOrder(innerBundle1, innerBundle1innerPatient1, innerBundle2, innerBundle2innerPatient1, innerPatient1);
	}

	@Test
	public void testGetEmbeddedResourcesOnContainedResources() {

		Patient patient = new Patient();

		Practitioner practitioner1 = new Practitioner();
		practitioner1.setId("practitioner1");
		practitioner1.setActive(true);
		patient.getContained().add(practitioner1);
		patient.addGeneralPractitioner().setReference("#practitioner1");

		FhirTerser t = myCtx.newTerser();

		Collection<IBaseResource> resources;

		resources = t.getAllEmbeddedResources(patient, false);
		assertThat(resources).as(toUnqualifiedVersionlessIdValues(resources).toString()).containsExactlyInAnyOrder(practitioner1);

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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		((BooleanType) values.get(0)).setValue(Boolean.FALSE);

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = myCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertFalse(((BooleanType) values.get(0)).booleanValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		((Extension) values.get(0)).setValue(new StringType("modifiedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifiedValue");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		((Extension) values.get(0)).setValue(new StringType("modifiedModifierValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifiedModifierValue");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");

		((Extension) values.get(0)).setValue(new StringType("modifiedNestedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifiedNestedValue");
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value1");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("value2");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue1");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("modifierValue2");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue1");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("nestedValue2");
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<PrimitiveType> values = myCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		List<Extension> extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("value");

		extValues = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifierValue");

		extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) extValues.get(0).getValue()).getValueAsString()).isEqualTo("nestedValue");
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<PrimitiveType> values = myCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		((BooleanType) values.get(0)).setValue(Boolean.FALSE);

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = myCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanType);
		assertFalse(((BooleanType) values.get(0)).booleanValue());

		List<Extension> extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("value");

		extValues.get(0).setValue(new StringType("modifiedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifiedValue");

		extValues = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifierValue");

		extValues.get(0).setValue(new StringType("modifiedModifierValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) (extValues.get(0).getValue())).getValueAsString()).isEqualTo("modifiedModifierValue");

		extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) extValues.get(0).getValue()).getValueAsString()).isEqualTo("nestedValue");

		extValues.get(0).setValue(new StringType("modifiedNestedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class);
		assertThat(extValues).hasSize(1);
		assertTrue(extValues.get(0).getValue() instanceof StringType);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) extValues.get(0).getValue()).getValueAsString()).isEqualTo("modifiedNestedValue");
	}

	@Test
	public void testGetValuesWithWantedClassAndTheCreate() {
		Patient p = new Patient();

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<PrimitiveType> values = myCtx.newTerser().getValues(p, "Patient.active", PrimitiveType.class, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof BooleanType);
		assertNull(((BooleanType) values.get(0)).getValue());

		List<Extension> extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", Extension.class, true);
		assertThat(extValues).hasSize(1);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(extValues.get(0).getValue());

		extValues = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", Extension.class, true);
		assertThat(extValues).hasSize(1);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(extValues.get(0).getValue());

		extValues = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", Extension.class, true);
		assertThat(extValues).hasSize(1);
		assertThat(extValues.get(0).getUrl()).isEqualTo("http://acme.org/childExtension");
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		// No change.
		values = myCtx.newTerser().getValues(p, "Patient.active", false, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedValue");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedModifierValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedModifierValue");

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", false, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedNestedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedNestedValue");
	}

	@Test
	public void testGetValuesWithTheCreate() {
		Patient p = new Patient();

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.active", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertNull(((BooleanType) values.get(0)).getValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(((Extension) values.get(0)).getValue());

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(((Extension) values.get(0)).getValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.active");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		// No change.
		values = myCtx.newTerser().getValues(p, "Patient.active", true, true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedValue");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedModifierValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedModifierValue");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')");
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true, true);
		assertThat(values).hasSize(2);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertNull(((Extension) values.get(1)).getValue());

		((Extension) values.get(1)).setValue(new StringType("addedNestedValue"));

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		assertTrue(values.get(1) instanceof IBaseExtension);
		assertTrue(values.get(1) instanceof Extension);
		assertThat(((Extension) values.get(1)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(1)).getValue()).getValueAsString()).isEqualTo("addedNestedValue");
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

		System.out.println(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));

		List<IBase> values = myCtx.newTerser().getValues(p, "Patient.active", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof PrimitiveType);
		assertTrue(values.get(0) instanceof BooleanType);
		assertTrue(((BooleanType) values.get(0)).booleanValue());

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/extension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/extension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("value");

		values = myCtx.newTerser().getValues(p, "Patient.modifierExtension('http://acme.org/modifierExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/modifierExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("modifierValue");

		values = myCtx.newTerser().getValues(p, "Patient.extension('http://acme.org/parentExtension').extension('http://acme.org/childExtension')", true);
		assertThat(values).hasSize(1);
		assertTrue(values.get(0) instanceof IBaseExtension);
		assertTrue(values.get(0) instanceof Extension);
		assertThat(((Extension) values.get(0)).getUrl()).isEqualTo("http://acme.org/childExtension");
		assertThat(((StringType) ((Extension) values.get(0)).getValue()).getValueAsString()).isEqualTo("nestedValue");
	}

	@Test
	public void testVisitWithCustomSubclass() {

		ValueSet.ValueSetExpansionComponent component = new MyValueSetExpansionComponent();
		ValueSet vs = new ValueSet();
		vs.setExpansion(component);
		vs.getExpansion().setIdentifier("http://foo");

		Set<String> strings = new HashSet<>();
		myCtx.newTerser().visit(vs, new IModelVisitor() {
			@Override
			public void acceptElement(IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement instanceof IPrimitiveType) {
					strings.add(((IPrimitiveType) theElement).getValueAsString());
				}
			}
		});
		assertThat(strings).containsExactly("http://foo");

		strings.clear();
		myCtx.newTerser().visit(vs, new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				if (theElement instanceof IPrimitiveType) {
					strings.add(((IPrimitiveType) theElement).getValueAsString());
				}
				return true;
			}

			@Override
			public boolean acceptUndeclaredExtension(IBaseExtension<?, ?> theNextExt, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				return true;
			}
		});
		assertThat(strings).containsExactly("http://foo");
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
		myCtx.newTerser().visit(p, visitor);

		assertThat(element.getAllValues()).hasSize(3);
		assertThat(element.getAllValues().get(0)).isSameAs(p);
		assertThat(element.getAllValues().get(1)).isSameAs(p.getLink().get(0));
		assertThat(element.getAllValues().get(2)).isSameAs(p.getLink().get(0).getTypeElement());

		assertThat(containingElementPath.getAllValues()).hasSize(3);
		// assertEquals(0, containingElementPath.getAllValues().get(0).size());
		// assertEquals(1, containingElementPath.getAllValues().get(1).size());
		// assertEquals(2, containingElementPath.getAllValues().get(2).size());

	}

	@Test
	public void testGetAllPopulatedChildElementsOfType() {

		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.MALE);
		p.addIdentifier().setSystem("urn:foo");
		p.addAddress().addLine("Line1");
		p.addAddress().addLine("Line2");
		p.addName().setFamily("Line3");

		FhirTerser t = myCtx.newTerser();
		List<StringType> strings = t.getAllPopulatedChildElementsOfType(p, StringType.class);

		assertThat(strings).hasSize(3);

		Set<String> allStrings = new HashSet<>();
		for (StringType next : strings) {
			allStrings.add(next.getValue());
		}

		assertThat(allStrings).containsExactlyInAnyOrder("Line1", "Line2", "Line3");

	}

	@Test
	public void testMultiValueTypes() {

		Observation obs = new Observation();
		obs.setValue(new Quantity(123L));

		FhirTerser t = myCtx.newTerser();

		// As string
		{
			List<IBase> values = t.getValues(obs, "Observation.valueString");
			assertThat(values).isEmpty();
		}

		// As quantity
		{
			List<IBase> values = t.getValues(obs, "Observation.valueQuantity");
			assertThat(values).hasSize(1);
			Quantity actual = (Quantity) values.get(0);
			assertThat(actual.getValueElement().getValueAsString()).isEqualTo("123");
		}
	}

	@Test
	public void testTerser() {

		//@formatter:off
		String msg = "<Observation xmlns=\"http://hl7.org/fhir\">\n" +
			"    <text>\n" +
			"        <status value=\"empty\"/>\n" +
			"        <div xmlns=\"http://www.w3.org/1999/xhtml\"/>\n" +
			"    </text>\n" +
			"    <!-- The test code  - may not be correct -->\n" +
			"    <name>\n" +
			"        <coding>\n" +
			"            <system value=\"http://loinc.org\"/>\n" +
			"            <code value=\"43151-0\"/>\n" +
			"            <display value=\"Glucose Meter Device Panel\"/>\n" +
			"        </coding>\n" +
			"    </name>\n" +
			"    <valueQuantity>\n" +
			"        <value value=\"7.7\"/>\n" +
			"        <units value=\"mmol/L\"/>\n" +
			"        <system value=\"http://unitsofmeasure.org\"/>\n" +
			"    </valueQuantity>\n" +
			"    <appliesDateTime value=\"2014-05-28T22:12:21Z\"/>\n" +
			"    <status value=\"final\"/>\n" +
			"    <reliability value=\"ok\"/>\n" +
			"    <subject>\n" +
			"        <reference value=\"cid:patient@bundle\"/>\n" +
			"    </subject>\n" +
			"    <performer>\n" +
			"        <reference value=\"cid:device@bundle\"></reference>\n" +
			"    </performer>\n" +
			"</Observation>";
		//@formatter:on

		Observation parsed = myCtx.newXmlParser().parseResource(Observation.class, msg);
		FhirTerser t = myCtx.newTerser();

		List<Reference> elems = t.getAllPopulatedChildElementsOfType(parsed, Reference.class);
		assertThat(elems).hasSize(2);
		assertThat(elems.get(0).getReferenceElement().getValue()).isEqualTo("cid:patient@bundle");
		assertThat(elems.get(1).getReferenceElement().getValue()).isEqualTo("cid:device@bundle");
	}

	@Test
	public void testConcurrentTerserCalls() throws ExecutionException, InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {

			FhirContext ctx = FhirContext.forR4();

			List<Future<?>> futures = new ArrayList<>();
			for (int i = 0; i < 10; i++) {

				Runnable runnable = () -> {

					Observation observation = new Observation();
					observation.setSubject(new Reference("Patient/123"));
					HashSet<String> additionalCompartmentParamNames = new HashSet<>();
					additionalCompartmentParamNames.add("test");
					boolean outcome = ctx.newTerser().isSourceInCompartmentForTarget("Patient", observation, new IdType("Patient/123"), additionalCompartmentParamNames);
					assertTrue(outcome);

				};
				Future<?> future = executor.submit(runnable);
				futures.add(future);
			}

			for (var next : futures) {
				next.get();
			}

		}finally {
			executor.shutdown();
		}
	}



	private List<String> toStrings(List<StringType> theStrings) {
		ArrayList<String> retVal = new ArrayList<>();
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

	@Block
	public static class MyValueSetExpansionComponent extends ValueSet.ValueSetExpansionComponent {
		private static final long serialVersionUID = 2624360513249904086L;
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

}
