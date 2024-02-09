package ca.uhn.fhir.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import org.hl7.fhir.dstu2.model.*;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelInheritanceTest {
  /*
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
   *
   * Extension
   *  * Should URL not be StringType since it can't take extensions?
   * </pre>
   */

  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();

  /**
   * This one should apply to all composite types
   */
  @Test
  public void testAddress() {
		assertThat(ICompositeType.class.isAssignableFrom(Address.class)).isTrue();
  }

  @Test
  public void testBackboneElement() {
		assertThat(IBaseBackboneElement.class.isAssignableFrom(BackboneElement.class)).isTrue();
		assertThat(IBaseHasExtensions.class.isAssignableFrom(BackboneElement.class)).isTrue();
		assertThat(IBaseHasModifierExtensions.class.isAssignableFrom(BackboneElement.class)).isTrue();
  }

  @Test
  public void testBase() {
		assertThat(IBase.class.isAssignableFrom(Base.class)).isTrue();
  }

  @Test
  public void testBinary() {
		assertThat(IBaseBinary.class.isAssignableFrom(Binary.class)).isTrue();
  }

  @Test
  public void testBooleanType() {
		assertThat(IBaseBooleanDatatype.class.isAssignableFrom(BooleanType.class)).isTrue();
  }

  @Test
  public void testBundle() {
		assertThat(IBaseBundle.class.isAssignableFrom(Bundle.class)).isTrue();
  }

  @Test
  public void testCoding() {
		assertThat(IBaseCoding.class.isAssignableFrom(Coding.class)).isTrue();
  }

  @Test
  public void testDecimalType() {
		assertThat(IBaseDecimalDatatype.class.isAssignableFrom(DecimalType.class)).isTrue();
  }

  @Test
  public void testDomainResource() {
		assertThat(IBaseHasExtensions.class.isAssignableFrom(DomainResource.class)).isTrue();
		assertThat(IBaseHasModifierExtensions.class.isAssignableFrom(DomainResource.class)).isTrue();
		assertThat(IDomainResource.class.isAssignableFrom(DomainResource.class)).isTrue();
  }

  @Test
  public void testElement() {
		assertThat(IBaseHasExtensions.class.isAssignableFrom(Element.class)).isTrue();
  }

  @Test
  public void testEnumeration() {
		assertThat(IBaseEnumeration.class.isAssignableFrom(Enumeration.class)).isTrue();

    DatatypeDef def = Enumeration.class.getAnnotation(DatatypeDef.class);
		assertThat(def.isSpecialization()).isTrue();
  }

  /**
   * Should be "implements IBaseExtension<Extension>"
   */
  @Test
  public void testExtension() {
		assertThat(IBaseExtension.class.isAssignableFrom(Extension.class)).isTrue();
		assertThat(IBaseHasExtensions.class.isAssignableFrom(Extension.class)).isTrue();
  }

  @Test
  public void testIdType() {
		assertThat(IIdType.class.isAssignableFrom(IdType.class)).isTrue();
  }

  @Test
  public void testIntegerType() {
		assertThat(IBaseIntegerDatatype.class.isAssignableFrom(IntegerType.class)).isTrue();
  }

  @Test
  public void testList() {
		assertThat(ourCtx.getResourceDefinition(List_.class).getName()).isEqualTo("List");
  }

  @Test
  public void testMeta() {
		assertThat(IBaseMetaType.class.isAssignableFrom(Meta.class)).isTrue();
  }

  @Test
  public void testNarrative() {
		assertThat(INarrative.class.isAssignableFrom(Narrative.class)).isTrue();
  }

  @Test
  public void testParameters() {
		assertThat(IBaseParameters.class.isAssignableFrom(Parameters.class)).isTrue();
  }

  @Test
  public void testPrimitiveType() {
		assertThat(IPrimitiveType.class.isAssignableFrom(PrimitiveType.class)).isTrue();
		assertThat(IBaseHasExtensions.class.isAssignableFrom(PrimitiveType.class)).isTrue();
  }

  @Test
  public void testProfiledDatatype() {
		assertThat(CodeType.class.getSuperclass()).isEqualTo(StringType.class);
		assertThat(CodeType.class.getAnnotation(DatatypeDef.class).profileOf()).isEqualTo(StringType.class);
		assertThat(Money.class.getSuperclass()).isEqualTo(Quantity.class);
		assertThat(Money.class.getAnnotation(DatatypeDef.class).profileOf()).isEqualTo(Quantity.class);
  }

  @Test
  public void testReference() {
		assertThat(IBaseReference.class.isAssignableFrom(Reference.class)).isTrue();
  }

  @Test
  public void testResource() {
		assertThat(IAnyResource.class.isAssignableFrom(Resource.class)).isTrue();
  }

  @Test
  public void testTiming_TimingRepeatComponent() {
		assertThat(IBaseDatatypeElement.class.isAssignableFrom(Timing.TimingRepeatComponent.class)).isTrue();
		assertThat(Timing.TimingRepeatComponent.class.getAnnotation(Block.class)).isNotNull();
  }

  @Test
  public void testType() {
		assertThat(IBaseDatatype.class.isAssignableFrom(Type.class)).isTrue();
  }

  @Test
  public void testXhtml() {
		assertThat(IBaseXhtml.class.isAssignableFrom(XhtmlNode.class)).isTrue();
  }

}
