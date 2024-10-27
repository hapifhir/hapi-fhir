
package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonResourceSupertypeScannerTest {

  private final CommonResourceSupertypeScanner scanner = new CommonResourceSupertypeScanner();
  
  @Test
  public void testBaseClass() {
    scanner.register(DemoPatient.class);

		assertThat(scanner.getLowestCommonSuperclass()).contains(DemoPatient.class);
  }
  
  @Test
  public void testSubtype() {
    scanner.register(DemoPatient.class);
    scanner.register(DemoPatientTripleSub.class);

		assertThat(scanner.getLowestCommonSuperclass()).contains(DemoPatient.class);
  }
  
  @Test
  public void testHierarchyBranch() {
    scanner.register(DemoPatientSub.class);
    scanner.register(DemoPatientSubSub.class);
    scanner.register(DemoPatientSubSubTwo.class);
    scanner.register(DemoPatientTripleSub.class);

		assertThat(scanner.getLowestCommonSuperclass()).contains(DemoPatientSub.class);
  }
  
  @Test
  public void testSupertypeNotRegistered() {
    scanner.register(DemoPatientTripleSub.class);
    scanner.register(DemoPatientSubSubTwo.class);

		assertThat(scanner.getLowestCommonSuperclass()).contains(DemoPatientSub.class);
  }
  
  @Test
  public void testOnlySubtype() {
    scanner.register(DemoPatientTripleSub.class);

		assertThat(scanner.getLowestCommonSuperclass()).contains(DemoPatientTripleSub.class);
  }
  
  @Test
  public void testEmpty() {
		assertEquals(false, scanner.getLowestCommonSuperclass().isPresent());
  }
  
  @ResourceDef(name = "Patient")
  private static class DemoPatient implements IBaseResource {

    @Override
    public IBaseMetaType getMeta() {
      return null;
    }

    @Override
    public IIdType getIdElement() {
      return null;
    }

    @Override
    public IBaseResource setId(String theId) {
      return null;
    }

    @Override
    public IBaseResource setId(IIdType theId) {
      return null;
    }

    @Override
    public FhirVersionEnum getStructureFhirVersionEnum() {
      return null;
    }

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
  
  @ResourceDef(id = "subOne")
  private static class DemoPatientSub extends DemoPatient {}
  
  @ResourceDef(id = "subSubOne")
  private static class DemoPatientSubSub extends DemoPatientSub {}
  
  @ResourceDef(id = "subSubTwo")
  private static class DemoPatientSubSubTwo extends DemoPatientSub {}
  
  @ResourceDef(id = "tripleSub")
  private static class DemoPatientTripleSub extends DemoPatientSubSub {}
}
