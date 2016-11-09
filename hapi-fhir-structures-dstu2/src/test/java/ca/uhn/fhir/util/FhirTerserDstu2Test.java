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
import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
import ca.uhn.fhir.model.primitive.MarkdownDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

public class FhirTerserDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@Test
	public void testCloneIntoComposite() {
		QuantityDt source = new QuantityDt();
		source.setCode("CODE");
		MoneyDt target = new MoneyDt();

		ourCtx.newTerser().cloneInto(source, target, true);

		assertEquals("CODE", target.getCode());
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

       patient.addUndeclaredExtension(new ExtensionDt(false, "http://example.com", new StringDt("FOO")));

       Patient target = new Patient();
		ourCtx.newTerser().cloneInto(patient, target, false);
		
		List<ExtensionDt> exts = target.getUndeclaredExtensionsByUrl("http://example.com");
		assertEquals(1, exts.size());
		assertEquals("FOO", ((StringDt)exts.get(0).getValue()).getValue());
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
       obs.setValue(new StringDt("AAA"));
       obs.setComments("COMMENTS");

       Observation target = new Observation();
		ourCtx.newTerser().cloneInto(obs, target, false);
		
		assertEquals("AAA", ((StringDt)obs.getValue()).getValue());
		assertEquals("COMMENTS", obs.getComments());
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

		assertEquals(2, strings.size());
		assertThat(strings, containsInAnyOrder(new StringDt("PATIENT"), new StringDt("ORGANIZATION")));

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

		assertEquals(1, strings.size());
		assertThat(strings, containsInAnyOrder(new StringDt("BUNDLE")));

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
		p.addLink().getTypeElement().setValue("CODE");
		ourCtx.newTerser().visit(p, visitor);

		assertEquals(3, element.getAllValues().size());
		assertSame(p, element.getAllValues().get(0));
		assertSame(p.getLinkFirstRep(), element.getAllValues().get(1));
		assertSame(p.getLinkFirstRep().getTypeElement(), element.getAllValues().get(2));

		assertEquals(3, containingElementPath.getAllValues().size());
		// assertEquals(0, containingElementPath.getAllValues().get(0).size());
		// assertEquals(1, containingElementPath.getAllValues().get(1).size());
		// assertEquals(2, containingElementPath.getAllValues().get(2).size());

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
