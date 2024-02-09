package ca.uhn.fhir.context;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Compartment;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Property;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.instance.model.api.IBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ModelScannerDstu3Test {

	@Test
	public void testScanBundle() {
		FhirContext ctx = FhirContext.forDstu3();
		RuntimeResourceDefinition def = ctx.getResourceDefinition("Bundle");

		assertThat(def.getSearchParam("composition")).isNotNull();
		assertThat(def.getSearchParam("_id")).isNotNull();
	}

	@Test
	public void testBundleMustImplementIBaseBundle() throws DataFormatException {
		FhirContext ctx = FhirContext.forDstu3();
		try {
			ctx.getResourceDefinition(MyBundle.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1687) + "Resource type declares resource name Bundle but does not implement IBaseBundle");
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
		assertThat(patient.getImplementingClass()).isEqualTo(Patient.class);

		RuntimeResourceDefinition def = ctx.getResourceDefinition(MyPatient.class);
		RuntimeResourceDefinition baseDef = def.getBaseDefinition();
		assertThat(baseDef.getImplementingClass()).isEqualTo(Patient.class);
	}

	@Test
	public void testResourceWithNoDef() {
		try {
			FhirContext.forDstu3().getResourceDefinition(NoResourceDef.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1716) + "Resource class[ca.uhn.fhir.context.ModelScannerDstu3Test$NoResourceDef] does not contain any valid HAPI-FHIR annotations");
		}
	}

	@Test
	public void testResourceWithInheritedDef() {
		try {
			FhirContext.forDstu3().getResourceDefinition(InheritedResourceDef.class);
		} catch (ConfigurationException e) {
			fail("The InheritedResourceDef class should contain a valid HAPI-FHIR annotation inherited from superclass");
		}
	}

	@Test
	public void testScanExtensionTypes() throws DataFormatException {

		FhirContext ctx = FhirContext.forDstu3();
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsDstu3A.class);

		assertThat(def.getChildByNameOrThrowDataFormatException("identifier").getClass()).isEqualTo(RuntimeChildCompositeDatatypeDefinition.class);

		RuntimeChildDeclaredExtensionDefinition ext = def.getDeclaredExtension("http://foo/#f1", "");
		assertThat(ext).isNotNull();
		BaseRuntimeElementDefinition<?> valueString = ext.getChildByName("valueString");
		assertThat(valueString).isNotNull();

		ext = def.getDeclaredExtension("http://foo/#f2", "");
		assertThat(ext).isNotNull();
		valueString = ext.getChildByName("valueString");
		assertThat(valueString).isNotNull();

		ext = def.getDeclaredExtension("http://bar/#b1", "");
		assertThat(ext).isNotNull();
		RuntimeChildDeclaredExtensionDefinition childExt = ext.getChildExtensionForUrl("http://bar/#b1/1");
		assertThat(childExt).isNotNull();
		BaseRuntimeElementDefinition<?> valueDate = childExt.getChildByName("valueDate");
		assertThat(valueDate).isNotNull();
		childExt = ext.getChildExtensionForUrl("http://bar/#b1/2");
		assertThat(childExt).isNotNull();
		childExt = childExt.getChildExtensionForUrl("http://bar/#b1/2/1");
		assertThat(childExt).isNotNull();
		valueDate = childExt.getChildByName("valueDate");
		assertThat(valueDate).isNotNull();

	}

	@Test
	public void testScanDstu3TypeWithDstu2Backend() throws DataFormatException {
		FhirContext ctx = FhirContext.forDstu3();
		try {
			ctx.getResourceDefinition(CustomDstu3ClassWithDstu2Base.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1717) + "@Block class for version DSTU3 should not extend BaseIdentifiableElement: ca.uhn.fhir.context.CustomDstu3ClassWithDstu2Base$Bar1");
		}
	}

	/**
	 * TODO: Re-enable this when Claim's compartment defs are cleaned up
	 */
	@Test
	@Disabled
	public void testSearchParamWithCompartmentForNonReferenceParam() {
		try {
			FhirContext.forDstu3().getResourceDefinition(CompartmentForNonReferenceParam.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo("Search param foo provides compartment membershit but is not of type 'reference'");
		}
	}

	@Test
	public void testSearchParamWithInvalidType() {
		try {
			FhirContext.forDstu3().getResourceDefinition(InvalidParamType.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1721) + "Search param foo has an invalid type: bar");
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
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1733) + "Class \"class ca.uhn.fhir.context.ModelScannerDstu3Test$LetterTemplate\" is invalid. This resource type is not a DomainResource, it must not have extensions");
		}
	}

	@Test
	public void testScanDuplicate() {
		FhirContext ctx = FhirContext.forDstu3();
		FhirVersionEnum version = FhirVersionEnum.DSTU3;
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> definitions = new HashMap<>();
		Collection<Class<? extends IBase>> resourceTypes = new ArrayList<>();
		resourceTypes.add(Patient.class);
		ModelScanner scanner = new ModelScanner(ctx, version, definitions, resourceTypes);
		assertThat(resourceTypes).containsExactly(Patient.class);

		// Extra scans don't do anything
		scanner.scan(Patient.class);
		scanner.scan(Patient.class);
		assertThat(resourceTypes).containsExactly(Patient.class);
	}

	@Test
	public void testScanInvalidResource() {
		FhirContext ctx = FhirContext.forDstu3();
		FhirVersionEnum version = FhirVersionEnum.DSTU3;
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> definitions = new HashMap<>();
		Collection<Class<? extends IBase>> resourceTypes = new ArrayList<>();
		ModelScanner scanner = new ModelScanner(ctx, version, definitions, resourceTypes);

		try {
			scanner.scan(BadPatient.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1714) + "Resource type contains a @ResourceDef annotation but does not implement ca.uhn.fhir.model.api.IResource: ca.uhn.fhir.context.ModelScannerDstu3Test.BadPatient");
		}
	}

	@Test
	public void testScanInvalidType() {
		FhirContext ctx = FhirContext.forDstu3();
		FhirVersionEnum version = FhirVersionEnum.DSTU3;
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> definitions = new HashMap<>();
		Collection<Class<? extends IBase>> resourceTypes = new ArrayList<>();
		ModelScanner scanner = new ModelScanner(ctx, version, definitions, resourceTypes);

		Class clazz = String.class;
		try {
			scanner.scan(clazz);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1716) + "Resource class[java.lang.String] does not contain any valid HAPI-FHIR annotations");
		}
	}

	@Test
	public void testScanInvalidBlock() {


		FhirContext ctx = FhirContext.forDstu3();
		FhirVersionEnum version = FhirVersionEnum.DSTU3;
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> definitions = new HashMap<>();
		Collection<Class<? extends IBase>> resourceTypes = new ArrayList<>();
		ModelScanner scanner = new ModelScanner(ctx, version, definitions, resourceTypes);

		try {
			scanner.scan(BadPatient.BadBlock.class);
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1715) + "Type contains a @Block annotation but does not implement ca.uhn.fhir.model.api.IResourceBlock: ca.uhn.fhir.context.ModelScannerDstu3Test.BadPatient.BadBlock");
		}
	}

	class NoResourceDef extends DomainResource {
		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "bar")
		public static final String SP_TELECOM = "foo";
		private static final long serialVersionUID = 1L;

		@Override
		public DomainResource copy() {
			return null;
		}

		@Override
		public ResourceType getResourceType() {
			return null;
		}
	}

	static class InheritedResourceDef extends Patient {
		public InheritedResourceDef() {
		}
	}

	@ResourceDef(name = "Patient")
	public static class BadPatient implements IBase {

		@Child(name = "badBlock")
		private BadBlock myChild;

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean hasFormatComment() {
			return false;
		}

		@Override
		public List<String> getFormatCommentsPre() {
			return null;
		}

		@Override
		public List<String> getFormatCommentsPost() {
			return null;
		}

		@Override
		public Object getUserData(String theName) {
			return null;
		}

		@Override
		public void setUserData(String theName, Object theValue) {

		}

		@Block
		public static class BadBlock implements IBase {

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public boolean hasFormatComment() {
				return false;
			}

			@Override
			public List<String> getFormatCommentsPre() {
				return null;
			}

			@Override
			public List<String> getFormatCommentsPost() {
				return null;
			}

			@Override
			public Object getUserData(String theName) {
				return null;
			}

			@Override
			public void setUserData(String theName, Object theValue) {

			}
		}

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

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
