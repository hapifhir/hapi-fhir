package ca.uhn.fhir.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.model.api.annotation.Compartment;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.resource.CarePlan;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.MyPatient;
import ca.uhn.fhir.util.TestUtil;

public class ModelScannerDstu1Test {

	/** This failed at one point */
	@Test
	public void testCarePlan() throws DataFormatException {
		FhirContext.forDstu1().getResourceDefinition(CarePlan.class);
	}

	@Test
	public void testExtendedClass() {
		FhirContext ctx = FhirContext.forDstu1();
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
			FhirContext.forDstu1().getResourceDefinition(NoResourceDef.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Resource class[ca.uhn.fhir.context.ModelScannerDstu1Test$NoResourceDef] does not contain any valid HAPI-FHIR annotations", e.getMessage());
		}
	}

	@Test
	public void testScanExtensionTypes() throws DataFormatException {

		FhirContext ctx = FhirContext.forDstu1();
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsA.class);

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

	/**
	 * TODO: re-enable this when Claim compartments are fixed
	 */
	@Test
	@Ignore
	public void testSearchParamWithCompartmentForNonReferenceParam() {
		try {
			FhirContext.forDstu1().getResourceDefinition(CompartmentForNonReferenceParam.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Search param foo provides compartment membershit but is not of type 'reference'", e.getMessage());
		}
	}

	@Test
	public void testSearchParamWithInvalidType() {
		try {
			FhirContext.forDstu1().getResourceDefinition(InvalidParamType.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Search param foo has an invalid type: bar", e.getMessage());
		}
	}

	@ResourceDef(name = "Patient")
	public static class CompartmentForNonReferenceParam extends Patient {
		private static final long serialVersionUID = 1L;

		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "string", providesMembershipIn = { @Compartment(name = "Patient"), @Compartment(name = "Device") })
		public static final String SP_TELECOM = "foo";

	}

	@ResourceDef(name = "Patient")
	public static class InvalidParamType extends Patient {
		private static final long serialVersionUID = 1L;

		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "bar")
		public static final String SP_TELECOM = "foo";

	}

	class NoResourceDef extends Patient {
		private static final long serialVersionUID = 1L;

		@SearchParamDefinition(name = "foo", path = "Patient.telecom", type = "bar")
		public static final String SP_TELECOM = "foo";

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
