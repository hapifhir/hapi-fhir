package org.hl7.fhir.validation.r4.tests;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.FhirPublication;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.r4.validation.ValidationEngine;
import org.hl7.fhir.utilities.Utilities;
import org.junit.Assert;
import org.junit.Test;

public class ValidationEngineTests {

  private static final String DEF_TX = "http://tx.fhir.org";
  private static final String DBG_TX = "http://local.fhir.org:960";
  
  public static boolean inbuild;

  @Test
  public void testCurrentXml() throws Exception {
    if (!TestingUtilities.silent) 
      System.out.println("Validate patient-example.xml in Current version");
    ValidationEngine ve = new ValidationEngine(TestingUtilities.content(), DEF_TX, null, FhirPublication.R4);
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.content(),  "patient-example.xml"), null);
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    if (!TestingUtilities.silent) {
      System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
      for (OperationOutcomeIssueComponent iss : op.getIssue()) {
        System.out.println("    "+iss.getDetails().getText());
      }
    }
    Assert.assertTrue(e == 0);
    Assert.assertTrue(w == 0);
    Assert.assertTrue(h == 0);
  }

  @Test
  public void testCurrentJson() throws Exception {
    if (!TestingUtilities.silent)
    System.out.println("Validate patient-example.json in Current version");
    ValidationEngine ve = new ValidationEngine(TestingUtilities.content(), DEF_TX, null, FhirPublication.R4);
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.content(),  "patient-example.json"), null);
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    Assert.assertTrue(e == 0);
    Assert.assertTrue(w == 0);
    Assert.assertTrue(h == 0);
    if (!TestingUtilities.silent)
      System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
  }

  @Test
  public void test140() throws Exception {
    if (inbuild) {
      Assert.assertTrue(true);
      return;
    }
    if (!TestingUtilities.silent)
      System.out.println("Validate patient-example.xml in v1.4.0 version");
    ValidationEngine ve = new ValidationEngine("hl7.fhir.core#1.4.0", DEF_TX, null, FhirPublication.DSTU2016May);
    ve.setNoInvariantChecks(true);
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.home(),  "tests", "validation-examples", "patient140.xml"), null);
    if (!TestingUtilities.silent)
      for (OperationOutcomeIssueComponent iss : op.getIssue()) {
        System.out.println("    "+iss.getDetails().getText());
      }
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    Assert.assertTrue(e == 1);
    Assert.assertTrue(w == 0);
    Assert.assertTrue(h == 0);
    if (!TestingUtilities.silent)
      System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
  }

  @Test
  public void test102() throws Exception {
    if (inbuild) {
      Assert.assertTrue(true);
      return;
    }
    if (!TestingUtilities.silent)
      System.out.println("Validate patient-example.xml in v1.0.2 version");
    ValidationEngine ve = new ValidationEngine("hl7.fhir.core#1.0.2", DEF_TX, null, FhirPublication.DSTU2);
    ve.setNoInvariantChecks(true);
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.home(),  "tests", "validation-examples", "patient102.xml"), null);
    if (!TestingUtilities.silent)
      for (OperationOutcomeIssueComponent iss : op.getIssue()) {
        System.out.println("    "+iss.getDetails().getText());
      }
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    Assert.assertTrue(e == 1);
    Assert.assertTrue(w == 0);
    Assert.assertTrue(h == 0);
    if (!TestingUtilities.silent)
    System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
  }

  @Test
  public void testObs102() throws Exception {
    if (inbuild) {
      Assert.assertTrue(true);
      return;
    }
    if (!TestingUtilities.silent)
      System.out.println("Validate patient-example.xml in v1.0.2 version");
    ValidationEngine ve = new ValidationEngine("hl7.fhir.core#1.0.2", DEF_TX, null, FhirPublication.DSTU2);
    ve.setNoInvariantChecks(true);
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.home(),  "tests", "validation-examples", "observation102.json"), null);
    if (!TestingUtilities.silent)
      for (OperationOutcomeIssueComponent iss : op.getIssue()) {
        System.out.println("    "+iss.getDetails().getText());
      }
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    Assert.assertTrue(e == 1);
    Assert.assertTrue(w == 0);
    Assert.assertTrue(h == 0);
    if (!TestingUtilities.silent)
    System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
  }


  @Test
  public void test301() throws Exception {
    if (!TestingUtilities.silent)
      System.out.println("Validate observation301.xml against Core");
    if (!TestingUtilities.silent)
      System.out.println("  .. load FHIR from " +Utilities.path(TestingUtilities.home(),  "publish"));
    ValidationEngine ve = new ValidationEngine("hl7.fhir.core#3.0.1", DEF_TX, null, FhirPublication.STU3);
    if (!TestingUtilities.silent)
      System.out.println("  .. load USCore");
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.home(),  "tests", "validation-examples", "observation301.xml"), null);
    if (!TestingUtilities.silent)
      for (OperationOutcomeIssueComponent issue : op.getIssue())
        System.out.println("  - "+issue.getDetails().getText());
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    Assert.assertTrue(e == 0);
    if (!TestingUtilities.silent)
      System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
  }

  @Test
  public void test301USCore() throws Exception {
    if (!TestingUtilities.silent)
      System.out.println("Validate patient300.xml against US-Core");
    if (!TestingUtilities.silent)
      System.out.println("  .. load FHIR from " +Utilities.path(TestingUtilities.home(),  "publish"));
    ValidationEngine ve = new ValidationEngine("hl7.fhir.core#3.0.1", DEF_TX, null, FhirPublication.STU3);
    if (!TestingUtilities.silent)
      System.out.println("  .. load USCore");
    ve.loadIg("hl7.fhir.us.core#1.0.1");
    List<String> profiles = new ArrayList<>();
    profiles.add("http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient");
    OperationOutcome op = ve.validate(Utilities.path(TestingUtilities.home(),  "tests", "validation-examples", "patient301.xml"), profiles);
    if (!TestingUtilities.silent)
      for (OperationOutcomeIssueComponent issue : op.getIssue())
        System.out.println("  - "+issue.getDetails().getText());
    int e = errors(op);
    int w = warnings(op);
    int h = hints(op);
    Assert.assertTrue(e == 1);
    Assert.assertTrue(w == 0);
    Assert.assertTrue(h == 0);
    if (!TestingUtilities.silent)
      System.out.println("  .. done: "+Integer.toString(e)+" errors, "+Integer.toString(w)+" warnings, "+Integer.toString(h)+" information messages");
  }

//  @Test
//  public void testTransform() throws Exception {
//    if (!TestingUtilities.silent)
//      System.out.println("Transform CCDA");
//    if (!TestingUtilities.silent)
//      System.out.println("  .. load FHIR from " +Utilities.path(TestingUtilities.home(),  "publish"));
//    ValidationEngine ve = new ValidationEngine(Utilities.path(TestingUtilities.home(),  "publish"), DEF_TX, null, FhirVersion.R4);
//    if (!TestingUtilities.silent)
//      System.out.println("  .. load CCDA from " +Utilities.path(TestingUtilities.home(),  "guides\\ccda2\\mapping\\logical"));
//    ve.loadIg(Utilities.path(TestingUtilities.home(),  "guides\\ccda2\\mapping\\logical"));
//    if (!TestingUtilities.silent)
//      System.out.println("  .. load Maps from " +Utilities.path(TestingUtilities.home(),  "guides\\ccda2\\mapping\\map"));
//    ve.loadIg(Utilities.path(TestingUtilities.home(),  "guides\\ccda2\\mapping\\map"));
//    Resource r = ve.transform(Utilities.path(TestingUtilities.home(),  "guides\\ccda2\\mapping\\example\\ccd.xml"), "http://hl7.org/fhir/StructureMap/cda");
//    if (!TestingUtilities.silent)
//      System.out.println("  .. done");
//  }

  private int errors(OperationOutcome op) {
    int i = 0;
    for (OperationOutcomeIssueComponent vm : op.getIssue()) {
      if (vm.getSeverity() == IssueSeverity.ERROR || vm.getSeverity() == IssueSeverity.FATAL)
        i++;
    }
    return i;
  }

  private int warnings(OperationOutcome op) {
    int i = 0;
    for (OperationOutcomeIssueComponent vm : op.getIssue()) {
      if (vm.getSeverity() == IssueSeverity.WARNING)
        i++;
    }
    return i;
  }

  private int hints(OperationOutcome op) {
    int i = 0;
    for (OperationOutcomeIssueComponent vm : op.getIssue()) {
      if (vm.getSeverity() == IssueSeverity.INFORMATION)
        i++;
    }
    return i;
  }

  public static void execute() throws Exception {
    ValidationEngineTests self = new ValidationEngineTests();
    self.testCurrentXml();
    self.testCurrentJson();
    self.test102();
    self.test140();
    self.test301USCore();
//    self.testTransform();
    System.out.println("Finished");
  }

}
