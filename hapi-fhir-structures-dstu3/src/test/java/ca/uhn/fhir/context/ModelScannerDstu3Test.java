package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.*;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ModelScannerDstu3Test {

	@Test
	public void testScanBundle() {
		FhirContext ctx = FhirContext.forDstu3();
		RuntimeResourceDefinition def = ctx.getResourceDefinition("Bundle");

		assertNotNull(def.getSearchParam("composition"));
		assertNotNull(def.getSearchParam("_id"));
	}

	@Test
	public void testBundleMustImplementIBaseBundle() throws DataFormatException {
		FhirContext ctx = FhirContext.forDstu3();
		try {
			ctx.getResourceDefinition(MyBundle.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Resource type declares resource name Bundle but does not implement IBaseBundle", e.getMessage());
		}
	}

	/**
	 * This failed at one point
	 */
	@Test
	public void testCarePlan() throws DataFormatException {
		FhirContext.forDstu3().getResourceDefinition(CarePlan.class);
	}

	@Test
	public void testExtendedClass() {
		FhirContext ctx = FhirContext.forDstu3();
		ctx.getResourceDefinition(MyPatient.class);

		RuntimeResourceDefinition patient = ctx.getResourceDefinition("Patient");
		assertEquals(Patient.class, patient.getImplementingClass());

		RuntimeResourceDefinition def = ctx.getResourceDefinition(MyPatient.class);
		RuntimeResourceDefinition baseDef = def.getBaseDefinition();
		assertEquals(Patient.class, baseDef.getImplementingClass());
	}

	@Test
	public void testResourceWithNoDef() {
		try {
			FhirContext.forDstu3().getResourceDefinition(NoResourceDef.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Resource class[ca.uhn.fhir.context.ModelScannerDstu3Test$NoResourceDef] does not contain any valid HAPI-FHIR annotations", e.getMessage());
		}
	}

	@Test
	public void testScanExtensionTypes() throws DataFormatException {

		FhirContext ctx = FhirContext.forDstu3();
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsDstu3A.class);

		assertEquals(RuntimeChildCompositeDatatypeDefinition.class, def.getChildByNameOrThrowDataFormatException("identifier").getClass());

		RuntimeChildDeclaredExtensionDefinition ext = def.getDeclaredExtension("http://foo/#f1", "");
		assertNotNull(ext);
		BaseRuntimeElementDefinition<?> valueString = ext.getChildByName("valueString");
		assertNotNull(valueString);

		ext = def.getDeclaredExtension("http://foo/#f2", "");
		assertNotNull(ext);
		valueString = ext.getChildByName("valueString");
		assertNotNull(valueString);

		ext = def.getDeclaredExtension("http://bar/#b1", "");
		assertNotNull(ext);
		RuntimeChildDeclaredExtensionDefinition childExt = ext.getChildExtensionForUrl("http://bar/#b1/1");
		assertNotNull(childExt);
		BaseRuntimeElementDefinition<?> valueDate = childExt.getChildByName("valueDate");
		assertNotNull(valueDate);
		childExt = ext.getChildExtensionForUrl("http://bar/#b1/2");
		assertNotNull(childExt);
		childExt = childExt.getChildExtensionForUrl("http://bar/#b1/2/1");
		assertNotNull(childExt);
		valueDate = childExt.getChildByName("valueDate");
		assertNotNull(valueDate);

	}

	@Test
	public void testScanDstu3TypeWithDstu2Backend() throws DataFormatException {
		FhirContext ctx = FhirContext.forDstu3();
		try {
			ctx.getResourceDefinition(CustomDstu3ClassWithDstu2Base.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("@Block class for version DSTU3 should not extend BaseIdentifiableElement: ca.uhn.fhir.context.CustomDstu3ClassWithDstu2Base$Bar1", e.getMessage());
		}
	}

	/**
	 * TODO: Re-enable this when Claim's compartment defs are cleaned up
	 */
	@Test
	@Ignore
	public void testSearchParamWithCompartmentForNonReferenceParam() {
		try {
			FhirContext.forDstu3().getResourceDefinition(CompartmentForNonReferenceParam.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Search param foo provides compartment membershit but is not of type 'reference'", e.getMessage());
		}
	}

	@Test
	public void testSearchParamWithInvalidType() {
		try {
			FhirContext.forDstu3().getResourceDefinition(InvalidParamType.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Search param foo has an invalid type: bar", e.getMessage());
		}
	}

	/**
	 * See #504
	 */
	@Test
	public void testBinaryMayNotHaveExtensions() {
		FhirContext ctx = FhirContext.forDstu3();
		try {
			ctx.getResourceDefinition(LetterTemplate.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Class \"class ca.uhn.fhir.context.ModelScannerDstu3Test$LetterTemplate\" is invalid. This resource type is not a DomainResource, it must not have extensions", e.getMessage());
		}
	}

	class NoResourceDef extends Patient {
		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "bar")
		public static final String SP_TELECOM = "foo";
		private static final long serialVersionUID = 1L;

	}

	@ResourceDef(name = "Patient")
	public static class CompartmentForNonReferenceParam extends Patient {
		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "string", providesMembershipIn = {@Compartment(name = "Patient"), @Compartment(name = "Device")})
		public static final String SP_TELECOM = "foo";
		private static final long serialVersionUID = 1L;

	}

	@ResourceDef(name = "Patient")
	public static class InvalidParamType extends Patient {
		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "bar")
		public static final String SP_TELECOM = "foo";
		private static final long serialVersionUID = 1L;

	}

	@ResourceDef(name = "Bundle")
	public static class MyBundle extends Resource {
		private static final long serialVersionUID = 1L;

		@Override
		public String fhirType() {
			return null;
		}

		@Override
		public String getId() {
			return null;
		}

		@Override
		public IdType getIdElement() {
			return null;
		}

		@Override
		public CodeType getLanguageElement() {
			return null;
		}

		@Override
		public Meta getMeta() {
			return null;
		}

		@Override
		protected void listChildren(List<Property> theResult) {
			// nothing
		}

		@Override
		public Resource setId(String theId) {
			return null;
		}

		@Override
		public Resource setIdElement(IdType theIdType) {
			return null;
		}

		@Override
		public Resource copy() {
			return null;
		}

		@Override
		public ResourceType getResourceType() {
			return null;
		}

	}

	@ResourceDef(name = "Binary", id = "letter-template", profile = "http://www.something.org/StructureDefinition/letter-template")
	public static class LetterTemplate extends Binary {

		private static final long serialVersionUID = 1L;

		@Child(name = "name")
		@Extension(url = "http://example.com/dontuse#name", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The name of the template")
		private StringDt myName;

		public LetterTemplate() {
		}

		public StringDt getName() {
			return myName;
		}

		public void setName(StringDt name) {
			myName = name;
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
