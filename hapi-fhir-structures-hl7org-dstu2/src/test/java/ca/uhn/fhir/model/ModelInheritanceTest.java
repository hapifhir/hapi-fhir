package ca.uhn.fhir.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hl7.fhir.instance.model.Address;
import org.hl7.fhir.instance.model.Base;
import org.hl7.fhir.instance.model.BooleanType;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.DecimalType;
import org.hl7.fhir.instance.model.Extension;
import org.hl7.fhir.instance.model.IBase;
import org.hl7.fhir.instance.model.ICompositeType;
import org.hl7.fhir.instance.model.IPrimitiveType;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.IntegerType;
import org.hl7.fhir.instance.model.Meta;
import org.hl7.fhir.instance.model.Narrative;
import org.hl7.fhir.instance.model.PrimitiveType;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.Timing;
import org.hl7.fhir.instance.model.Type;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseDecimalDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseIntegerDatatype;
import org.hl7.fhir.instance.model.api.ICoding;
import org.hl7.fhir.instance.model.api.IDatatypeElement;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IMetaType;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.instance.model.api.IReference;
import org.junit.Test;

public class ModelInheritanceTest {

	/**
	 * <pre>
	 * Other changes:
	 * 
	 * Reference:
	 *  * Add "resource" field, plus constructors and getter/setters for that field
	 *  
	 * Narrative:
	 *  * Add getValueAsDiv and setValueAsDiv
	 *  
	 * XhtmlParser and XhtmlEncoder:
	 *  * Do we need a better exception declaration?
	 *  
	 * ElementDefinition
	 *  * Backbone elements (eg .ElementDefinitionSlicingComponent) do not extend BackboneElement or have a @Block annotation for some reason
	 * </pre>
	 */

	@Test
	public void testType() {
		assertTrue(IBaseDatatype.class.isAssignableFrom(Type.class));
	}

	/**
	 * This one should apply to all composite types
	 */
	@Test
	public void testAddress() {
		assertTrue(ICompositeType.class.isAssignableFrom(Address.class));
	}

	@Test
	public void testBase() {
		assertTrue(IBase.class.isAssignableFrom(Base.class));
	}

	/**
	 * Should be "implements IBaseExtension<Extension>"
	 */
	@Test
	public void testExtension() {
		assertTrue(IBaseExtension.class.isAssignableFrom(Extension.class));
	}

	@Test
	public void testNarrative() {
		assertTrue(INarrative.class.isAssignableFrom(Narrative.class));
	}

	@Test
	public void testBooleanType() {
		assertTrue(IBaseBooleanDatatype.class.isAssignableFrom(BooleanType.class));
	}

	@Test
	public void testDecimalType() {
		assertTrue(IBaseDecimalDatatype.class.isAssignableFrom(DecimalType.class));
	}

	@Test
	public void testIntegerType() {
		assertTrue(IBaseIntegerDatatype.class.isAssignableFrom(IntegerType.class));
	}

	@Test
	public void testPrimitiveType() {
		assertTrue(IPrimitiveType.class.isAssignableFrom(PrimitiveType.class));
	}

	@Test
	public void testResource() {
		assertTrue(IAnyResource.class.isAssignableFrom(Resource.class));
	}

	@Test
	public void testBundle() {
		assertTrue(IBaseBundle.class.isAssignableFrom(Bundle.class));
	}

	public void testIdType() {
		assertTrue(IIdType.class.isAssignableFrom(IdType.class));
	}

	@Test
	public void testReference() {
		assertTrue(IReference.class.isAssignableFrom(Reference.class));
	}

	@Test
	public void testMeta() {
		assertTrue(IMetaType.class.isAssignableFrom(Meta.class));
	}

	@Test
	public void testBackboneElement() {
		assertTrue(IBackboneElement.class.isAssignableFrom(IBackboneElement.class));
	}

	@Test
	public void testCoding() {
		assertTrue(ICoding.class.isAssignableFrom(Coding.class));
	}
	
	@Test
	public void testTiming_TimingRepeatComponent() {
		assertTrue(IDatatypeElement.class.isAssignableFrom(Timing.TimingRepeatComponent.class));
		assertNotNull(Timing.TimingRepeatComponent.class.getAnnotation(Block.class));
	}

}
