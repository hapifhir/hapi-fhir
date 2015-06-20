package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.resource.CarePlan;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.MyPatient;

public class ModelScannerTest {

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
		assertEquals(Patient.class,baseDef.getImplementingClass());
	}
	
	
	@Test
	public void testScanExtensionTypes() throws DataFormatException {
		
		FhirContext ctx = FhirContext.forDstu1();
		RuntimeResourceDefinition def = ctx.getResourceDefinition(ResourceWithExtensionsA.class);
		
		assertEquals(RuntimeChildCompositeDatatypeDefinition.class, def.getChildByNameOrThrowDataFormatException("identifier").getClass());
		
		RuntimeChildDeclaredExtensionDefinition ext = def.getDeclaredExtension("http://foo/#f1");
		assertNotNull(ext);
		BaseRuntimeElementDefinition<?> valueString = ext.getChildByName("valueString");
		assertNotNull(valueString);
		
		ext = def.getDeclaredExtension("http://foo/#f2");
		assertNotNull(ext);
		valueString = ext.getChildByName("valueString");
		assertNotNull(valueString);

		ext = def.getDeclaredExtension("http://bar/#b1");
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


	
}
