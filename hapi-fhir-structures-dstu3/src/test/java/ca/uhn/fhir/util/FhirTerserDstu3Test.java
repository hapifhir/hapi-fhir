package ca.uhn.fhir.util;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.MarkdownType;
import org.hl7.fhir.dstu3.model.Money;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Patient.LinkType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;

public class FhirTerserDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@Test
	public void testCloneIntoComposite() {
		Quantity source = new Quantity();
		source.setCode("CODE");
		Money target = new Money();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("CODE", target.getCode());
	}
   
	@Test
	public void testCloneIntoCompositeMismatchedFields() {
		Quantity source = new Quantity();
		source.setSystem("SYSTEM");
		source.setUnit("UNIT");
		Identifier target = new Identifier();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("SYSTEM", target.getSystem());

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
		assertEquals("FOO", ((StringType)exts.get(0).getValue()).getValue());
   }


	@Test
	public void testCloneIntoPrimitive() {
		StringType source = new StringType("STR");
		MarkdownType target = new MarkdownType();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("STR", target.getValueAsString());
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
       obs.setComment("COMMENTS");

       Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);
		
		assertEquals("AAA", ((StringType)obs.getValue()).getValue());
		assertEquals("COMMENTS", obs.getComment());
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

		assertThat(toStrings(strings), containsInAnyOrder("PATIENT","ORGANIZATION"));

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
