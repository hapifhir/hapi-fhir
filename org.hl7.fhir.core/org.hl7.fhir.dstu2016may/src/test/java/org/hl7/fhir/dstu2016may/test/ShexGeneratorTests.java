package org.hl7.fhir.dstu2016may.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.utils.ShExGenerator;
import org.hl7.fhir.dstu2016may.utils.ShExGenerator.HTMLLinkPolicy;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.TextFile;
import org.junit.Test;

public class ShexGeneratorTests {

  private void doTest(String name) throws FileNotFoundException, IOException, FHIRException {
    String workingDirectory = "C:\\work\\org.hl7.fhir.2016May\\build\\publish"; // FileSystems.getDefault().getPath(System.getProperty("user.dir"), "data").toString();
    if (TestingUtilities.context == null) {
      // For the time being, put the validation entry in org/hl7/fhir/dstu3/data
      Path path = FileSystems.getDefault().getPath(workingDirectory, "validation-min.xml.zip");
      TestingUtilities.context = SimpleWorkerContext.fromPack(path.toString());
    }
    StructureDefinition sd = TestingUtilities.context.fetchTypeDefinition(name);
    if(sd == null) {
      throw new FHIRException("StructuredDefinition for " + name + "was null");
    }
    Path outPath = FileSystems.getDefault().getPath(workingDirectory, name+".shex");
    TextFile.stringToFile(new ShExGenerator(TestingUtilities.context).generate(HTMLLinkPolicy.NONE, sd), outPath.toString());
  }

  @Test
  public void testId() throws FHIRException, IOException {
    doTest("id");
  }

  @Test
  public void testUri() throws FHIRException, IOException {
    doTest("uri");
  }


  @Test
  public void testObservation() throws FHIRException, IOException {
    doTest("Observation");
  }

  @Test
  public void testRef() throws FHIRException, IOException {
    doTest("Reference");
  }
  
  @Test
  public void testAccount() throws FHIRException, IOException {
    doTest("Account");
  }

  @Test
  public void testMedicationOrder() throws FHIRException, IOException {
    doTest("MedicationOrder");
  }
}
